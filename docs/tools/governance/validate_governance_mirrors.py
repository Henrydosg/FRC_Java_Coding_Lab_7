"""Deterministic preflight checks for governance Markdown mirrors.

This module deliberately does not certify semantic fidelity.  A successful
run means only: "No configured deterministic defect detected."  It never
writes files, repairs files, or changes mirror trust state.
"""

from __future__ import annotations

import argparse
import hashlib
import json
import re
import sys
from dataclasses import dataclass, field
from datetime import date
from html.parser import HTMLParser
from pathlib import Path
from typing import Any, Iterable


APPROVED_FIDELITY_CLASSES = {
    "TEXTUAL",
    "SEMANTIC_WITH_VISUAL_REFERENCE",
}
APPROVED_MIRROR_STATES = {"UNVERIFIED", "VERIFIED", "STALE", "HOLD"}
APPROVED_VERIFICATION_METHOD = (
    "Independent PDF-to-Markdown semantic fidelity review"
)
CANONICAL_MANIFEST = "docs/GOVERNANCE_DOCUMENT_MANIFEST.md"
VERIFIED_NOTICE = (
    "> This is a VERIFIED machine-readable mirror that has passed independent\n"
    "> semantic fidelity review. The English PDF remains authoritative, and this\n"
    "> mirror has no independent or equal authority rank. If a conflict exists, the\n"
    "> PDF controls."
)
REQUIRED_FRONT_MATTER = {
    "document_id",
    "document_title",
    "document_class",
    "language",
    "mirror_status",
    "mirror_fidelity_class",
    "authoritative_source",
    "authoritative_source_sha256",
    "source_version",
    "source_status",
    "verified_on",
    "verification_method",
    "manifest",
}
ALLOWED_FRONT_MATTER = REQUIRED_FRONT_MATTER
CONFIG_ENTRY_REQUIRED = {
    "path",
    "expected_document_id",
    "expected_title",
    "expected_language",
    "expected_document_class",
    "expected_fidelity_class",
    "expected_source_version",
    "expected_source_status",
}
CONFIG_ENTRY_OPTIONAL = {
    "expected_checkbox_total",
    "expected_checked_total",
    "headerless_tables",
}
HTML_TABLE_TAGS = {"table", "tr", "td"}
HTML_LIKE_RE = re.compile(r"</?[A-Za-z][^>\r\n]*>")
HEADING_RE = re.compile(r"^(#{1,6})\s+(.+?)\s*$")
CHECKBOX_RE = re.compile(r"^\s*[-*+]\s+\[([ xX])\]")
PIPE_TABLE_LINE_RE = re.compile(r"^\s*\|.*\|\s*$")
TABLE_SEPARATOR_CELL_RE = re.compile(r"^:?-{3,}:?$")
FRONT_MATTER_LINE_RE = re.compile(
    r'^(?P<key>[A-Za-z][A-Za-z0-9_]*): (?P<value>null|"(?:\\.|[^"\\])*")$'
)
SHA256_RE = re.compile(r"^[0-9a-f]{64}$")
MIRROR_HASH_DECLARATION_RE = re.compile(
    r"(?im)^\s*(?:mirror|markdown)(?:[ _-]+mirror)?"
    r"[ _-]+(?:sha[ _-]?256|hash)"
    r"(?:[ \t]+\([^\)\r\n]*\))?[ \t]*[:=]?[ \t]+[0-9a-fA-F]{64}(?![0-9a-fA-F])"
)
# These are multi-character extraction-corruption prefixes.  Isolated Ã/Â
# characters are valid Unicode and are intentionally not treated as errors.
HIGH_CONFIDENCE_MOJIBAKE = ("â€", "ï¿")
UNVERIFIED_NOTICE = (
    "> This is an UNVERIFIED candidate conversion and is not usable for governance reading.\n"
    "> The English PDF remains authoritative, and this mirror has no independent or equal\n"
    "> authority rank. If a conflict exists, the PDF controls."
)
FENCE_MARKERS = {"`", "~"}


class ConfigError(ValueError):
    """Raised when the validator configuration is not its locked schema."""


@dataclass(frozen=True)
class Finding:
    path: str
    rule_id: str
    message: str
    category: str
    line: int | None = None

    def format(self) -> str:
        location = self.path
        if self.line is not None:
            location += f":{self.line}"
        return (
            f"FAIL [{self.rule_id}] {location}: {self.message} "
            f"(category: {self.category})"
        )


@dataclass
class ValidationReport:
    mirrors_discovered: int = 0
    source_pdfs_found: int = 0
    source_hashes_matched: int = 0
    metadata_trust_passes: int = 0
    checklist_passes: int = 0
    headerless_structure_passes: int = 0
    findings: list[Finding] = field(default_factory=list)

    def add(
        self,
        path: str,
        rule_id: str,
        message: str,
        category: str,
        line: int | None = None,
    ) -> None:
        self.findings.append(Finding(path, rule_id, message, category, line))


def _reject_duplicate_json_keys(pairs: list[tuple[str, Any]]) -> dict[str, Any]:
    result: dict[str, Any] = {}
    for key, value in pairs:
        if key in result:
            raise ConfigError(f"duplicate JSON key: {key}")
        result[key] = value
    return result


def _read_json_strict(path: Path) -> dict[str, Any]:
    try:
        raw = path.read_bytes()
        text = raw.decode("utf-8")
        if text.startswith("\ufeff"):
            raise ConfigError("JSON configuration contains a UTF-8 BOM")
        value = json.loads(text, object_pairs_hook=_reject_duplicate_json_keys)
    except UnicodeDecodeError as exc:
        raise ConfigError(f"configuration is not valid UTF-8: {exc}") from exc
    except json.JSONDecodeError as exc:
        raise ConfigError(f"invalid JSON: {exc}") from exc
    if not isinstance(value, dict):
        raise ConfigError("configuration root must be an object")
    return value


def _require_exact_keys(
    value: dict[str, Any], required: set[str], optional: set[str], context: str
) -> None:
    missing = required - value.keys()
    unknown = value.keys() - required - optional
    if missing:
        raise ConfigError(f"{context} missing keys: {sorted(missing)}")
    if unknown:
        raise ConfigError(f"{context} unknown keys: {sorted(unknown)}")


def _validate_relative_config_path(value: Any, context: str) -> None:
    if not isinstance(value, str) or not value:
        raise ConfigError(f"{context} must be a non-empty string")
    if "\\" in value or Path(value).is_absolute():
        raise ConfigError(f"{context} must be a repository-relative / path")
    parts = Path(value).parts
    if any(part in {"", ".", ".."} for part in parts):
        raise ConfigError(f"{context} contains an invalid path component")


def load_config(path: Path) -> dict[str, Any]:
    config = _read_json_strict(path)
    _require_exact_keys(
        config,
        {"schema_version", "manifest_required", "mirrors"},
        set(),
        "configuration",
    )
    if config["schema_version"] != 1:
        raise ConfigError("configuration schema_version must be 1")
    if not isinstance(config["manifest_required"], bool):
        raise ConfigError("configuration manifest_required must be boolean")
    mirrors = config["mirrors"]
    if not isinstance(mirrors, list) or not mirrors:
        raise ConfigError("configuration mirrors must be a non-empty array")

    seen_paths: set[str] = set()
    for index, entry in enumerate(mirrors):
        context = f"mirror entry {index + 1}"
        if not isinstance(entry, dict):
            raise ConfigError(f"{context} must be an object")
        _require_exact_keys(entry, CONFIG_ENTRY_REQUIRED, CONFIG_ENTRY_OPTIONAL, context)
        _validate_relative_config_path(entry["path"], f"{context} path")
        if entry["path"] in seen_paths:
            raise ConfigError(f"duplicate configured mirror path: {entry['path']}")
        seen_paths.add(entry["path"])
        for key in CONFIG_ENTRY_REQUIRED - {"path"}:
            if not isinstance(entry[key], str) or not entry[key]:
                raise ConfigError(f"{context} {key} must be a non-empty string")
        if entry["expected_fidelity_class"] not in APPROVED_FIDELITY_CLASSES:
            raise ConfigError(f"{context} uses an unapproved fidelity class")
        for key in ("expected_checkbox_total", "expected_checked_total"):
            if key in entry and (
                not isinstance(entry[key], int) or isinstance(entry[key], bool)
            ):
                raise ConfigError(f"{context} {key} must be an integer")
        if ("expected_checkbox_total" in entry) != (
            "expected_checked_total" in entry
        ):
            raise ConfigError(
                f"{context} must configure both checklist counts or neither"
            )
        tables = entry.get("headerless_tables", [])
        if not isinstance(tables, list):
            raise ConfigError(f"{context} headerless_tables must be an array")
        for table_index, table in enumerate(tables):
            table_context = f"{context} headerless table {table_index + 1}"
            if not isinstance(table, dict):
                raise ConfigError(f"{table_context} must be an object")
            allowed = {
                "heading",
                "rows",
                "columns",
                "expected_table_count",
                "expected_empty_cells",
                "suspicious_headers",
            }
            _require_exact_keys(
                table,
                {"heading", "rows", "columns"},
                {
                    "expected_table_count",
                    "expected_empty_cells",
                    "suspicious_headers",
                },
                table_context,
            )
            if not isinstance(table["heading"], str) or not table["heading"]:
                raise ConfigError(f"{table_context} heading must be a string")
            for key in ("rows", "columns"):
                if not isinstance(table[key], int) or table[key] <= 0:
                    raise ConfigError(f"{table_context} {key} must be positive")
            if "expected_table_count" in table and (
                not isinstance(table["expected_table_count"], int)
                or isinstance(table["expected_table_count"], bool)
                or table["expected_table_count"] <= 0
            ):
                raise ConfigError(
                    f"{table_context} expected_table_count must be positive"
                )
            if "expected_empty_cells" in table and (
                not isinstance(table["expected_empty_cells"], int)
                or table["expected_empty_cells"] < 0
            ):
                raise ConfigError(
                    f"{table_context} expected_empty_cells must be nonnegative"
                )
            if "suspicious_headers" in table:
                headers = table["suspicious_headers"]
                if not isinstance(headers, list) or not all(
                    isinstance(header, str) for header in headers
                ):
                    raise ConfigError(
                        f"{table_context} suspicious_headers must be strings"
                    )
    return config


def _relative_path(path: Path, repo_root: Path) -> str:
    return path.resolve().relative_to(repo_root.resolve()).as_posix()


def _find_front_matter_end(lines: list[str]) -> int | None:
    for index in range(1, len(lines)):
        if lines[index] == "---":
            return index
    return None


def parse_front_matter(
    text: str, display_path: str, report: ValidationReport
) -> tuple[dict[str, Any], int | None]:
    lines = text.splitlines()
    if not lines or lines[0] != "---":
        report.add(
            display_path,
            "META-001",
            "front matter must begin with one opening --- delimiter",
            "schema",
            1,
        )
        return {}, None
    end = _find_front_matter_end(lines)
    if end is None:
        report.add(
            display_path,
            "META-002",
            "front matter has no closing --- delimiter",
            "schema",
            1,
        )
        return {}, None

    values: dict[str, Any] = {}
    for line_number, line in enumerate(lines[1:end], start=2):
        match = FRONT_MATTER_LINE_RE.fullmatch(line)
        if match is None:
            report.add(
                display_path,
                "META-003",
                "front matter must contain only one-line scalar key/value entries",
                "schema",
                line_number,
            )
            continue
        key = match.group("key")
        if key in values:
            report.add(
                display_path,
                "META-004",
                f"duplicate front-matter key: {key}",
                "schema",
                line_number,
            )
            continue
        if key not in ALLOWED_FRONT_MATTER:
            report.add(
                display_path,
                "META-005",
                f"unknown front-matter key: {key}",
                "schema",
                line_number,
            )
            continue
        raw_value = match.group("value")
        if raw_value == "null":
            values[key] = None
        else:
            try:
                parsed = json.loads(raw_value)
            except json.JSONDecodeError:
                parsed = None
            if not isinstance(parsed, str):
                report.add(
                    display_path,
                    "META-006",
                    f"front-matter value for {key} is not a quoted string",
                    "schema",
                    line_number,
                )
            else:
                values[key] = parsed

    missing = REQUIRED_FRONT_MATTER - values.keys()
    for key in sorted(missing):
        report.add(
            display_path,
            "META-007",
            f"required front-matter key is missing: {key}",
            "schema",
            1,
        )
    return values, end + 1


def _check_encoding(
    raw: bytes, display_path: str, report: ValidationReport
) -> str | None:
    try:
        text = raw.decode("utf-8")
    except UnicodeDecodeError as exc:
        report.add(
            display_path,
            "ENC-001",
            f"mirror is not valid UTF-8: {exc}",
            "encoding",
        )
        return None
    if text.startswith("\ufeff"):
        report.add(
            display_path,
            "ENC-002",
            "UTF-8 BOM is not part of the mirror convention",
            "encoding",
            1,
        )
    for index, character in enumerate(text):
        codepoint = ord(character)
        if character == "\ufffd":
            report.add(
                display_path,
                "ENC-003",
                "replacement character U+FFFD detected",
                "encoding",
                text.count("\n", 0, index) + 1,
            )
        elif character == "\x00" or (
            (ord(character) < 32 and character not in "\r\n\t")
            or 0x7F <= codepoint <= 0x9F
        ):
            report.add(
                display_path,
                "ENC-004",
                f"disallowed control character U+{codepoint:04X} detected",
                "encoding",
                text.count("\n", 0, index) + 1,
            )
        elif character == "\uf0b7":
            report.add(
                display_path,
                "ENC-005",
                "private-use extraction bullet U+F0B7 detected",
                "encoding",
                text.count("\n", 0, index) + 1,
            )
    for marker in HIGH_CONFIDENCE_MOJIBAKE:
        if marker in text:
            report.add(
                display_path,
                "ENC-006",
                f"high-confidence extraction-corruption marker detected: {marker}",
                "encoding",
            )
    return text


def _fence_run(line: str) -> tuple[str, int, str] | None:
    candidate = line.lstrip(" ")
    if not candidate or candidate[0] not in FENCE_MARKERS:
        return None
    marker = candidate[0]
    length = 0
    while length < len(candidate) and candidate[length] == marker:
        length += 1
    if length < 3:
        return None
    return marker, length, candidate[length:]


def _mask_inline_code(line: str) -> tuple[str, list[int]]:
    masked = list(line)
    errors: list[int] = []
    index = 0
    while index < len(line):
        if line[index] != "`":
            index += 1
            continue
        start = index
        while index < len(line) and line[index] == "`":
            index += 1
        length = index - start
        delimiter = "`" * length
        closing = line.find(delimiter, index)
        if closing < 0:
            errors.append(start)
            break
        for position in range(start, closing + length):
            masked[position] = " "
        index = closing + length
    return "".join(masked), errors


def _mask_code_regions(
    body_lines: list[str], body_start_line: int, display_path: str, report: ValidationReport
) -> tuple[list[str], list[bool]]:
    masked_lines: list[str] = []
    protected_lines: list[bool] = []
    in_fence = False
    fence_marker: str | None = None
    fence_length: int | None = None
    fence_start: int | None = None
    malformed_fence = False
    for offset, line in enumerate(body_lines):
        line_number = body_start_line + offset
        fence = _fence_run(line)
        if not in_fence:
            if fence is not None:
                fence_marker, fence_length, _ = fence
                in_fence = True
                malformed_fence = False
                fence_start = line_number
                masked_lines.append(" " * len(line))
                protected_lines.append(True)
                continue
        elif fence is not None and fence_marker is not None and fence_length is not None:
            marker, length, suffix = fence
            if marker == fence_marker and not suffix.strip():
                if length >= fence_length:
                    in_fence = False
                    fence_marker = None
                    fence_length = None
                    fence_start = None
                    masked_lines.append(" " * len(line))
                    protected_lines.append(True)
                    continue
                report.add(
                    display_path,
                    "MD-005",
                    "fenced code closing delimiter is shorter than its opening delimiter",
                    "Markdown structure",
                    line_number,
                )
                malformed_fence = True
                masked_lines.append(" " * len(line))
                protected_lines.append(False)
                continue
        if in_fence:
            # Once an incompatible closer is seen, fail closed: expose later
            # content to hazard checks instead of hiding it in a malformed
            # protected region.
            if malformed_fence:
                masked, errors = _mask_inline_code(line)
                for position in errors:
                    report.add(
                        display_path,
                        "MD-002",
                        "unterminated or unbalanced inline code span",
                        "Markdown structure",
                        line_number,
                    )
                masked_lines.append(masked)
                protected_lines.append(False)
            else:
                masked_lines.append(" " * len(line))
                protected_lines.append(True)
            continue
        masked, errors = _mask_inline_code(line)
        for position in errors:
            report.add(
                display_path,
                "MD-002",
                "unterminated or unbalanced inline code span",
                "Markdown structure",
                line_number,
            )
        masked_lines.append(masked)
        protected_lines.append(False)
    if in_fence:
        report.add(
            display_path,
            "MD-001",
            f"unterminated fenced code block opened at line {fence_start}",
            "Markdown structure",
            fence_start,
        )
    return masked_lines, protected_lines


def _validate_html_tokens(
    masked_lines: list[str],
    protected_lines: list[bool],
    body_start_line: int,
    display_path: str,
    report: ValidationReport,
) -> None:
    for offset, (line, protected) in enumerate(zip(masked_lines, protected_lines)):
        if protected:
            continue
        line_number = body_start_line + offset
        for match in HTML_LIKE_RE.finditer(line):
            token = match.group(0)
            token_name = re.fullmatch(r"</?([A-Za-z][A-Za-z0-9]*)>", token)
            if token_name is None or token_name.group(1).lower() not in HTML_TABLE_TAGS:
                report.add(
                    display_path,
                    "MD-003",
                    f"raw HTML-like or angle-bracket placeholder is not protected: {token}",
                    "rendering hazard",
                    line_number,
                )


def _normalize_heading(text: str) -> str:
    return " ".join(text.strip().lower().split())


def _validate_duplicate_headings(
    masked_lines: list[str],
    protected_lines: list[bool],
    body_start_line: int,
    display_path: str,
    report: ValidationReport,
) -> None:
    seen: set[tuple[int, str]] = set()
    for offset, (line, protected) in enumerate(zip(masked_lines, protected_lines)):
        if protected:
            continue
        match = HEADING_RE.fullmatch(line)
        if match is None:
            continue
        key = (len(match.group(1)), _normalize_heading(match.group(2)))
        if key in seen:
            report.add(
                display_path,
                "MD-004",
                f"duplicate normalized heading: {match.group(2).strip()}",
                "Markdown structure",
                body_start_line + offset,
            )
        seen.add(key)


def _split_pipe_cells(line: str) -> list[str]:
    content = line.strip()[1:-1]
    cells: list[str] = []
    current: list[str] = []
    escaped = False
    for character in content:
        if character == "|" and not escaped:
            cells.append("".join(current).strip())
            current = []
            continue
        if character == "\\" and not escaped:
            escaped = True
            current.append(character)
            continue
        escaped = False
        current.append(character)
    cells.append("".join(current).strip())
    return cells


def _validate_pipe_tables(
    masked_lines: list[str],
    protected_lines: list[bool],
    body_start_line: int,
    display_path: str,
    report: ValidationReport,
) -> None:
    offset = 0
    while offset < len(masked_lines):
        if protected_lines[offset] or not PIPE_TABLE_LINE_RE.match(masked_lines[offset]):
            offset += 1
            continue
        start = offset
        while (
            offset < len(masked_lines)
            and not protected_lines[offset]
            and PIPE_TABLE_LINE_RE.match(masked_lines[offset])
        ):
            offset += 1
        group = masked_lines[start:offset]
        if len(group) < 2:
            report.add(
                display_path,
                "TABLE-001",
                "pipe table requires a header row and delimiter row",
                "table structure",
                body_start_line + start,
            )
            continue
        rows = [_split_pipe_cells(line) for line in group]
        if not all(TABLE_SEPARATOR_CELL_RE.fullmatch(cell) for cell in rows[1]):
            report.add(
                display_path,
                "TABLE-002",
                "pipe table delimiter row is malformed",
                "table structure",
                body_start_line + start + 1,
            )
        if len(rows[0]) != len(rows[1]):
            report.add(
                display_path,
                "TABLE-003",
                "pipe table header and delimiter column counts differ",
                "table structure",
                body_start_line + start,
            )
        expected_columns = len(rows[0])
        for row_offset, row in enumerate(rows[2:], start=2):
            if len(row) != expected_columns:
                report.add(
                    display_path,
                    "TABLE-004",
                    "pipe table row has an inconsistent column count",
                    "table structure",
                    body_start_line + start + row_offset,
                )


class _ControlledTableParser(HTMLParser):
    def __init__(self) -> None:
        super().__init__(convert_charrefs=True)
        self.stack: list[str] = []
        self.rows: list[list[str]] = []
        self.current_cell: list[str] | None = None
        self.errors: list[str] = []
        self.table_count = 0

    def handle_starttag(self, tag: str, attrs: list[tuple[str, str | None]]) -> None:
        tag = tag.lower()
        if attrs or tag not in HTML_TABLE_TAGS:
            self.errors.append(f"unsupported HTML table tag or attribute: {tag}")
            return
        if tag == "table":
            if self.stack:
                self.errors.append("nested table")
            self.table_count += 1
        elif tag == "tr":
            if self.stack != ["table"]:
                self.errors.append("tr is not directly inside table")
            self.rows.append([])
        elif tag == "td":
            if self.stack != ["table", "tr"] or not self.rows:
                self.errors.append("td is not directly inside tr")
            if self.current_cell is not None:
                self.errors.append("nested td")
            self.current_cell = []
        self.stack.append(tag)

    def handle_endtag(self, tag: str) -> None:
        tag = tag.lower()
        if not self.stack or self.stack[-1] != tag:
            self.errors.append(f"mismatched closing tag: {tag}")
            return
        if tag == "td":
            assert self.current_cell is not None
            self.rows[-1].append("".join(self.current_cell).strip())
            self.current_cell = None
        self.stack.pop()

    def handle_startendtag(
        self, tag: str, attrs: list[tuple[str, str | None]]
    ) -> None:
        self.errors.append(f"self-closing table tag is not allowed: {tag}")

    def handle_data(self, data: str) -> None:
        if self.current_cell is not None:
            self.current_cell.append(data)
        elif data.strip():
            self.errors.append("non-whitespace data outside a td")

    def close(self) -> None:
        super().close()
        if self.stack:
            self.errors.append("unclosed HTML table tag")
        if self.current_cell is not None:
            self.errors.append("unclosed td")


def _section_bounds(
    lines: list[str], heading_index: int, heading: str
) -> tuple[int, int]:
    level = len(heading.split()[0])
    end = len(lines)
    for index in range(heading_index + 1, len(lines)):
        match = HEADING_RE.fullmatch(lines[index].strip())
        if match and len(match.group(1)) <= level:
            end = index
            break
    return heading_index, end


def _validate_headerless_tables(
    body_lines: list[str],
    masked_lines: list[str],
    protected_lines: list[bool],
    body_start_line: int,
    specs: list[dict[str, Any]],
    display_path: str,
    report: ValidationReport,
) -> None:
    for spec in specs:
        before = len(report.findings)
        heading = spec["heading"]
        try:
            heading_index = next(
                index
                for index, line in enumerate(masked_lines)
                if not protected_lines[index] and line.strip() == heading
            )
        except StopIteration:
            report.add(
                display_path,
                "TABLE-010",
                f"configured headerless-table heading not found: {heading}",
                "table configuration",
            )
            continue
        start, end = _section_bounds(masked_lines, heading_index, heading)
        section_lines = body_lines[start:end]
        section_masked = masked_lines[start:end]
        for relative, line in enumerate(section_masked):
            normalized = " ".join(line.strip(" |\t").split()).lower()
            if normalized in {
                " ".join(header.lower().split())
                for header in spec.get("suspicious_headers", [])
            }:
                report.add(
                    display_path,
                    "TABLE-011",
                    f"invented suspicious header in declared headerless section: {line.strip()}",
                    "headerless-table conversion",
                    body_start_line + start + relative,
                )
            if re.search(r"<\s*(?:th|thead)\b", line, re.IGNORECASE):
                report.add(
                    display_path,
                    "TABLE-012",
                    "<th> or <thead> is forbidden in a declared headerless table",
                    "headerless-table conversion",
                    body_start_line + start + relative,
                )
        expected_table_count = spec.get("expected_table_count", 1)
        table_starts = [
            index
            for index, line in enumerate(section_masked)
            if line.strip() == "<table>"
        ]
        table_ends = [
            index
            for index, line in enumerate(section_masked)
            if line.strip() == "</table>"
        ]
        if (
            len(table_starts) != expected_table_count
            or len(table_ends) != expected_table_count
        ):
            report.add(
                display_path,
                "TABLE-013",
                "configured headerless section has "
                f"{len(table_starts)} table starts and {len(table_ends)} table ends; "
                f"expected {expected_table_count} complete table(s)",
                "headerless-table conversion",
                body_start_line + start,
            )
        table_ranges: list[tuple[int, int]] = []
        end_cursor = 0
        for table_start in table_starts:
            while end_cursor < len(table_ends) and table_ends[end_cursor] <= table_start:
                end_cursor += 1
            if end_cursor >= len(table_ends):
                report.add(
                    display_path,
                    "TABLE-014",
                    "controlled HTML table has no closing </table>",
                    "headerless-table structure",
                    body_start_line + start + table_start,
                )
                continue
            table_end = table_ends[end_cursor]
            end_cursor += 1
            table_ranges.append((table_start, table_end))

        for table_start, table_end in table_ranges:
            parser = _ControlledTableParser()
            parser.feed("\n".join(section_lines[table_start : table_end + 1]))
            parser.close()
            if parser.table_count != 1:
                parser.errors.append("expected exactly one table")
            if parser.errors:
                for error in parser.errors:
                    report.add(
                        display_path,
                        "TABLE-014",
                        error,
                        "headerless-table structure",
                        body_start_line + start + table_start,
                    )
                continue
            if len(parser.rows) != spec["rows"]:
                report.add(
                    display_path,
                    "TABLE-015",
                    f"configured table has {len(parser.rows)} rows; expected {spec['rows']}",
                    "headerless-table structure",
                    body_start_line + start + table_start,
                )
            if any(len(row) != spec["columns"] for row in parser.rows):
                report.add(
                    display_path,
                    "TABLE-016",
                    "configured table has an unexpected cell count",
                    "headerless-table structure",
                    body_start_line + start + table_start,
                )
            if "expected_empty_cells" in spec:
                empty_cells = sum(not cell for row in parser.rows for cell in row)
                if empty_cells != spec["expected_empty_cells"]:
                    report.add(
                        display_path,
                        "TABLE-017",
                        f"configured table has {empty_cells} empty cells; expected {spec['expected_empty_cells']}",
                        "headerless-table template state",
                        body_start_line + start + table_start,
                    )
        if len(report.findings) == before:
            report.headerless_structure_passes += 1


def _validate_checklist(
    masked_lines: list[str],
    protected_lines: list[bool],
    display_path: str,
    expected_total: int | None,
    expected_checked: int | None,
    report: ValidationReport,
) -> None:
    if expected_total is None or expected_checked is None:
        return
    total = 0
    checked = 0
    for line, protected in zip(masked_lines, protected_lines):
        if protected:
            continue
        match = CHECKBOX_RE.match(line)
        if match is None:
            continue
        total += 1
        if match.group(1).lower() == "x":
            checked += 1
    before = len(report.findings)
    if total != expected_total:
        report.add(
            display_path,
            "CHECKLIST-001",
            f"checkbox total is {total}; expected {expected_total}",
            "checklist structure",
        )
    if checked != expected_checked:
        report.add(
            display_path,
            "CHECKLIST-002",
            f"checked checkbox total is {checked}; expected {expected_checked}",
            "checklist structure",
        )
    if len(report.findings) == before:
        report.checklist_passes += 1


def _validate_manifest_reference(
    mirror_path: Path,
    fields: dict[str, Any],
    repo_root: Path,
    manifest_required: bool,
    display_path: str,
    report: ValidationReport,
) -> None:
    value = fields.get("manifest")
    if not isinstance(value, str) or not value:
        report.add(
            display_path,
            "MANIFEST-001",
            "manifest reference must be a non-empty relative forward-slash path",
            "manifest provenance",
        )
        return
    if (
        "\\" in value
        or Path(value).is_absolute()
        or re.match(r"^[A-Za-z][A-Za-z0-9+.-]*:", value)
    ):
        report.add(
            display_path,
            "MANIFEST-001",
            "manifest reference must be a relative forward-slash path",
            "manifest provenance",
        )
        return
    resolved = (mirror_path.parent / Path(value)).resolve()
    expected = (repo_root / Path(CANONICAL_MANIFEST)).resolve()
    if resolved != expected:
        report.add(
            display_path,
            "MANIFEST-002",
            f"manifest reference resolves to {resolved.as_posix()}, expected {expected.as_posix()}",
            "manifest provenance",
        )
        return
    if manifest_required and not expected.is_file():
        report.add(
            display_path,
            "MANIFEST-003",
            "manifest is required by configuration but does not exist",
            "manifest setup",
        )


def _validate_trust_state(
    text: str,
    fields: dict[str, Any],
    display_path: str,
    report: ValidationReport,
) -> None:
    state = fields.get("mirror_status")
    if state not in APPROVED_MIRROR_STATES:
        report.add(
            display_path,
            "STATE-001",
            f"unapproved mirror_status: {state!r}",
            "trust state",
        )
        return
    verified_on = fields.get("verified_on")
    method = fields.get("verification_method")
    if state == "UNVERIFIED":
        if verified_on is not None:
            report.add(
                display_path,
                "STATE-002",
                "UNVERIFIED mirror must have verified_on: null",
                "trust state",
            )
        if method is not None:
            report.add(
                display_path,
                "STATE-003",
                "UNVERIFIED mirror must have verification_method: null",
                "trust state",
            )
    elif state == "VERIFIED":
        if not isinstance(verified_on, str) or not re.fullmatch(
            r"\d{4}-\d{2}-\d{2}", verified_on
        ):
            report.add(
                display_path,
                "STATE-004",
                "VERIFIED mirror must have an ISO YYYY-MM-DD verified_on value",
                "trust state",
            )
        else:
            try:
                date.fromisoformat(verified_on)
            except ValueError:
                report.add(
                    display_path,
                    "STATE-005",
                    "VERIFIED verified_on is not a real calendar date",
                    "trust state",
                )
        if method != APPROVED_VERIFICATION_METHOD:
            report.add(
                display_path,
                "STATE-006",
                "VERIFIED mirror has an invalid verification_method",
                "trust state",
            )
    else:
        if (verified_on is None) != (method is None):
            report.add(
                display_path,
                "STATE-007",
                f"{state} historical verification fields must both be null or both retained",
                "trust state",
            )
        elif verified_on is not None:
            if not isinstance(verified_on, str) or not re.fullmatch(
                r"\d{4}-\d{2}-\d{2}", verified_on
            ):
                report.add(
                    display_path,
                    "STATE-008",
                    f"{state} retained verified_on is not ISO YYYY-MM-DD",
                    "trust state",
                )
            else:
                try:
                    date.fromisoformat(verified_on)
                except ValueError:
                    report.add(
                        display_path,
                        "STATE-009",
                        f"{state} retained verified_on is not a real date",
                        "trust state",
                    )
            if method != APPROVED_VERIFICATION_METHOD:
                report.add(
                    display_path,
                    "STATE-010",
                    f"{state} retained verification_method is invalid",
                    "trust state",
                )

    notice_count = text.count("PDF controls.")
    if notice_count != 1:
        report.add(
            display_path,
            "STATE-011",
            f"expected exactly one canonical trust notice marker; found {notice_count}",
            "trust notice",
        )
    if state == "UNVERIFIED" and UNVERIFIED_NOTICE not in text:
        report.add(
            display_path,
            "STATE-012",
            "UNVERIFIED mirror does not contain the canonical candidate trust notice",
            "trust notice",
        )
    if state == "VERIFIED" and VERIFIED_NOTICE not in text:
        report.add(
            display_path,
            "STATE-013",
            "VERIFIED mirror does not contain the canonical trust notice",
            "trust notice",
        )


def _validate_metadata(
    text: str,
    fields: dict[str, Any],
    entry: dict[str, Any],
    display_path: str,
    report: ValidationReport,
) -> None:
    comparisons = {
        "document_id": "expected_document_id",
        "document_title": "expected_title",
        "language": "expected_language",
        "document_class": "expected_document_class",
        "mirror_fidelity_class": "expected_fidelity_class",
        "source_version": "expected_source_version",
        "source_status": "expected_source_status",
    }
    for field_name, config_name in comparisons.items():
        if fields.get(field_name) != entry[config_name]:
            report.add(
                display_path,
                "META-008",
                f"{field_name} does not match configured convention",
                "metadata schema",
            )
    if fields.get("document_class") not in {"Document A", "Document B", "Document C"}:
        report.add(
            display_path,
            "META-009",
            "document_class is not an approved Document A/B/C value",
            "metadata schema",
        )
    if fields.get("mirror_fidelity_class") not in APPROVED_FIDELITY_CLASSES:
        report.add(
            display_path,
            "META-010",
            "mirror_fidelity_class is not an ADR-approved value",
            "metadata schema",
        )
    if MIRROR_HASH_DECLARATION_RE.search(text):
        report.add(
            display_path,
            "HASH-001",
            "mirror body declares its own Markdown/mirror SHA-256",
            "self-hash contamination",
        )


def _validate_source(
    mirror_path: Path,
    fields: dict[str, Any],
    repo_root: Path,
    display_path: str,
    report: ValidationReport,
) -> None:
    source_name = fields.get("authoritative_source")
    source_hash = fields.get("authoritative_source_sha256")
    if not isinstance(source_name, str) or not source_name:
        report.add(
            display_path,
            "SRC-001",
            "authoritative_source must be a non-empty co-located PDF basename",
            "source identity",
        )
        return
    if (
        Path(source_name).name != source_name
        or "/" in source_name
        or "\\" in source_name
        or not source_name.lower().endswith(".pdf")
    ):
        report.add(
            display_path,
            "SRC-001",
            "authoritative_source must be a co-located PDF basename",
            "source identity",
        )
        return
    if Path(source_name).stem != mirror_path.stem:
        report.add(
            display_path,
            "SRC-002",
            "authoritative PDF stem does not match mirror stem",
            "source identity",
        )
    source_path = mirror_path.parent / source_name
    if not source_path.is_file():
        report.add(
            display_path,
            "SRC-003",
            f"authoritative source PDF does not exist: {source_name}",
            "source identity",
        )
        return
    report.source_pdfs_found += 1
    source_bytes = source_path.read_bytes()
    if not source_bytes.startswith(b"%PDF-"):
        report.add(
            display_path,
            "SRC-004",
            "authoritative source does not begin with %PDF magic",
            "source identity",
        )
    if not isinstance(source_hash, str) or not SHA256_RE.fullmatch(source_hash):
        report.add(
            display_path,
            "SRC-005",
            "authoritative_source_sha256 must be 64 lowercase hexadecimal characters",
            "source identity",
        )
        return
    actual_hash = hashlib.sha256(source_bytes).hexdigest()
    if actual_hash != source_hash:
        report.add(
            display_path,
            "SRC-006",
            "fresh authoritative PDF SHA-256 does not match mirror provenance",
            "source identity",
        )
    else:
        report.source_hashes_matched += 1


def validate_mirror(
    mirror_path: Path,
    entry: dict[str, Any],
    repo_root: Path,
    manifest_required: bool,
    report: ValidationReport,
) -> None:
    display_path = _relative_path(mirror_path, repo_root)
    if not mirror_path.is_file():
        report.add(
            display_path,
            "SRC-007",
            "configured mirror does not exist",
            "setup",
        )
        return
    raw = mirror_path.read_bytes()
    text = _check_encoding(raw, display_path, report)
    if text is None:
        return
    fields, body_start = parse_front_matter(text, display_path, report)
    metadata_before = len(report.findings)
    _validate_metadata(text, fields, entry, display_path, report)
    _validate_trust_state(text, fields, display_path, report)
    _validate_source(mirror_path, fields, repo_root, display_path, report)
    _validate_manifest_reference(
        mirror_path,
        fields,
        repo_root,
        manifest_required,
        display_path,
        report,
    )
    if len(report.findings) == metadata_before:
        report.metadata_trust_passes += 1
    if body_start is None:
        return
    lines = text.splitlines()
    body_lines = lines[body_start:]
    masked_lines, protected_lines = _mask_code_regions(
        body_lines, body_start + 1, display_path, report
    )
    _validate_html_tokens(
        masked_lines,
        protected_lines,
        body_start + 1,
        display_path,
        report,
    )
    _validate_duplicate_headings(
        masked_lines,
        protected_lines,
        body_start + 1,
        display_path,
        report,
    )
    _validate_pipe_tables(
        masked_lines,
        protected_lines,
        body_start + 1,
        display_path,
        report,
    )
    _validate_headerless_tables(
        body_lines,
        masked_lines,
        protected_lines,
        body_start + 1,
        entry.get("headerless_tables", []),
        display_path,
        report,
    )
    _validate_checklist(
        masked_lines,
        protected_lines,
        display_path,
        entry.get("expected_checkbox_total"),
        entry.get("expected_checked_total"),
        report,
    )


def validate_repository(repo_root: Path, config: dict[str, Any]) -> ValidationReport:
    report = ValidationReport(mirrors_discovered=len(config["mirrors"]))
    seen_ids: set[str] = set()
    for entry in config["mirrors"]:
        mirror_path = (repo_root / Path(entry["path"])).resolve()
        document_id = entry["expected_document_id"]
        display_path = _relative_path(mirror_path, repo_root)
        if document_id in seen_ids:
            report.add(
                display_path,
                "META-011",
                f"duplicate configured document ID: {document_id}",
                "metadata schema",
            )
        seen_ids.add(document_id)
        validate_mirror(
            mirror_path,
            entry,
            repo_root,
            config["manifest_required"],
            report,
        )
    return report


def _print_report(report: ValidationReport) -> None:
    print("Governance mirror deterministic preflight")
    print("Semantic fidelity certification: NOT PERFORMED")
    for finding in report.findings:
        print(finding.format())
    print(f"Mirrors discovered: {report.mirrors_discovered}")
    print(f"Source PDFs found: {report.source_pdfs_found}")
    print(f"Source hashes matched: {report.source_hashes_matched}")
    print(f"Metadata/trust checks passed: {report.metadata_trust_passes}")
    print(f"Checklist structures passed: {report.checklist_passes}")
    print(f"Headerless structures passed: {report.headerless_structure_passes}")
    print(f"Deterministic findings: {len(report.findings)}")
    if report.findings:
        print("RESULT: FAIL")
    else:
        print('RESULT: PASS - No configured deterministic defect detected.')


def main(argv: Iterable[str] | None = None) -> int:
    parser = argparse.ArgumentParser(
        description=(
            "Run deterministic governance mirror preflight checks. "
            "PASS does not certify semantic fidelity."
        )
    )
    default_config = Path(__file__).resolve().with_name(
        "governance_mirror_expectations.json"
    )
    parser.add_argument("--config", type=Path, default=default_config)
    parser.add_argument(
        "--repo-root",
        type=Path,
        default=Path(__file__).resolve().parents[3],
        help="repository root; primarily useful for isolated fixture tests",
    )
    args = parser.parse_args(list(argv) if argv is not None else None)
    try:
        config = load_config(args.config.resolve())
        repo_root = args.repo_root.resolve()
        report = validate_repository(repo_root, config)
    except (OSError, ConfigError) as exc:
        print(f"FAIL [CONFIG-001] {args.config}: {exc} (category: setup)")
        return 2
    _print_report(report)
    return 1 if report.findings else 0


if __name__ == "__main__":
    sys.exit(main())
