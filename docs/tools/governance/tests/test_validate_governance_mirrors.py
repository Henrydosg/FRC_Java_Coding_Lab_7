"""Focused standard-library tests for deterministic governance mirror checks."""

from __future__ import annotations

import contextlib
import io
import json
import shutil
import sys
import tempfile
import unittest
from pathlib import Path


GOVERNANCE_TOOL_DIR = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(GOVERNANCE_TOOL_DIR))

import validate_governance_mirrors as validator  # noqa: E402


FIXTURES = Path(__file__).resolve().parent / "fixtures"
SOURCE_FIXTURE = FIXTURES / "authoritative_source.pdf"
SOURCE_HASH = "2e7cd81435638c6d1f1547a1ca829f041cfd60f95236d9dcaeb2e66b08de9f27"


FIXTURE_METADATA = {
    "valid_textual_candidate.md": (
        "FIXTURE-VALID-TEXTUAL",
        "VALID TEXTUAL FIXTURE",
    ),
    "valid_headerless_html.md": (
        "FIXTURE-VALID-HEADERLESS",
        "VALID HEADERLESS FIXTURE",
    ),
    "fail_raw_mechanism_placeholder.md": (
        "FIXTURE-RAW-MECHANISM",
        "RAW MECHANISM FIXTURE",
    ),
    "fail_raw_package_placeholder.md": (
        "FIXTURE-RAW-PACKAGE",
        "RAW PACKAGE FIXTURE",
    ),
    "fail_invented_field_value_header.md": (
        "FIXTURE-INVENTED-HEADER",
        "INVENTED HEADER FIXTURE",
    ),
    "fail_replacement_character.md": (
        "FIXTURE-REPLACEMENT-CHARACTER",
        "REPLACEMENT CHARACTER FIXTURE",
    ),
}


class GovernanceMirrorValidatorTest(unittest.TestCase):
    def _entry(self, document_id: str, title: str) -> dict[str, object]:
        return {
            "path": "docs/Document_B/English/authoritative_source.md",
            "expected_document_id": document_id,
            "expected_title": title,
            "expected_language": "English",
            "expected_document_class": "Document B",
            "expected_fidelity_class": "TEXTUAL",
            "expected_source_version": "1.0",
            "expected_source_status": "FROZEN",
        }

    def _stage(
        self,
        fixture_name: str,
        *,
        mutate=None,
        entry_mutate=None,
        source_bytes: bytes | None = None,
        raw_override: bytes | None = None,
    ) -> tuple[tempfile.TemporaryDirectory[str], Path]:
        temporary = tempfile.TemporaryDirectory()
        root = Path(temporary.name)
        mirror_directory = root / "docs" / "Document_B" / "English"
        mirror_directory.mkdir(parents=True)
        shutil.copyfile(SOURCE_FIXTURE, mirror_directory / "authoritative_source.pdf")
        if source_bytes is not None:
            (mirror_directory / "authoritative_source.pdf").write_bytes(source_bytes)
        content = (FIXTURES / fixture_name).read_bytes()
        if mutate is not None:
            content = mutate(content)
        if raw_override is not None:
            content = raw_override
        (mirror_directory / "authoritative_source.md").write_bytes(content)
        document_id, title = FIXTURE_METADATA[fixture_name]
        entry = self._entry(document_id, title)
        if fixture_name == "valid_headerless_html.md":
            entry["headerless_tables"] = [
                {
                    "heading": "## Review Result",
                    "expected_table_count": 1,
                    "rows": 4,
                    "columns": 2,
                    "expected_empty_cells": 3,
                    "suspicious_headers": ["Field | Value"],
                }
            ]
        if entry_mutate is not None:
            entry_mutate(entry)
        config = {
            "schema_version": 1,
            "manifest_required": False,
            "mirrors": [entry],
        }
        config_path = root / "config.json"
        config_path.write_text(json.dumps(config), encoding="utf-8")
        return temporary, config_path

    def _run(self, config_path: Path, root: Path) -> tuple[int, str]:
        output = io.StringIO()
        with contextlib.redirect_stdout(output):
            code = validator.main(
                ["--repo-root", str(root), "--config", str(config_path)]
            )
        return code, output.getvalue()

    def _run_fixture(self, fixture_name: str, **kwargs) -> tuple[int, str, tempfile.TemporaryDirectory[str]]:
        temporary, config_path = self._stage(fixture_name, **kwargs)
        return (*self._run(config_path, Path(temporary.name)), temporary)

    def _unverified_content(self, notice: bytes) -> bytes:
        content = (FIXTURES / "valid_textual_candidate.md").read_bytes()
        return (
            content.replace(
                b'mirror_status: "VERIFIED"', b'mirror_status: "UNVERIFIED"'
            )
            .replace(b'verified_on: "2026-08-29"', b"verified_on: null")
            .replace(
                b'verification_method: "Independent PDF-to-Markdown semantic fidelity review"',
                b"verification_method: null",
            )
            .replace(validator.VERIFIED_NOTICE.encode(), notice)
        )

    def _assert_hash_declaration_fails(self, declaration: bytes) -> None:
        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md",
            mutate=lambda content: content + b"\n" + declaration + b"a" * 64 + b"\n",
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("HASH-001", output)

    def _assert_checkbox_counts(
        self, suffix: bytes, expected_total: int, expected_checked: int
    ) -> None:
        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md",
            mutate=lambda content: content + suffix,
            entry_mutate=lambda entry: entry.update(
                {
                    "expected_checkbox_total": expected_total,
                    "expected_checked_total": expected_checked,
                }
            ),
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 0, output)

    def test_raw_mechanism_placeholder_fails(self) -> None:
        code, output, temporary = self._run_fixture("fail_raw_mechanism_placeholder.md")
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("MD-003", output)

    def test_protected_mechanism_placeholder_passes(self) -> None:
        code, output, temporary = self._run_fixture("valid_textual_candidate.md")
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 0, output)

    def test_raw_package_placeholder_fails(self) -> None:
        code, output, temporary = self._run_fixture("fail_raw_package_placeholder.md")
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("MD-003", output)

    def test_empty_authoritative_source_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content.replace(
                b'authoritative_source: "authoritative_source.pdf"',
                b'authoritative_source: ""',
            )

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("SRC-001", output)

    def test_null_manifest_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content.replace(
                b'manifest: "../../GOVERNANCE_DOCUMENT_MANIFEST.md"',
                b"manifest: null",
            )

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("MANIFEST-001", output)

    def test_valid_unverified_notice_passes(self) -> None:
        temporary, config_path = self._stage(
            "valid_textual_candidate.md",
            raw_override=self._unverified_content(validator.UNVERIFIED_NOTICE.encode()),
        )
        self.addCleanup(temporary.cleanup)
        code, output = self._run(config_path, Path(temporary.name))
        self.assertEqual(code, 0, output)

    def test_incomplete_unverified_notice_fails(self) -> None:
        temporary, config_path = self._stage(
            "valid_textual_candidate.md",
            raw_override=self._unverified_content(b"> PDF controls."),
        )
        self.addCleanup(temporary.cleanup)
        code, output = self._run(config_path, Path(temporary.name))
        self.assertEqual(code, 1)
        self.assertIn("STATE-012", output)

    def test_invented_field_value_header_fails(self) -> None:
        code, output, temporary = self._run_fixture(
            "fail_invented_field_value_header.md",
            entry_mutate=lambda entry: entry.update(
                {
                    "headerless_tables": [
                        {
                            "heading": "## Review Result",
                            "expected_table_count": 1,
                            "rows": 4,
                            "columns": 2,
                            "expected_empty_cells": 3,
                            "suspicious_headers": ["Field | Value"],
                        }
                    ]
                }
            ),
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("TABLE-011", output)

    def test_valid_headerless_html_passes(self) -> None:
        code, output, temporary = self._run_fixture("valid_headerless_html.md")
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 0, output)

    def test_missing_configured_headerless_table_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            start = content.index(b"<table>")
            end = content.index(b"</table>", start) + len(b"</table>")
            return content[:start] + content[end:]

        code, output, temporary = self._run_fixture(
            "valid_headerless_html.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("TABLE-013", output)

    def test_extra_configured_headerless_table_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content + b"\n<table>\n<tr><td>extra</td><td>table</td></tr>\n</table>\n"

        code, output, temporary = self._run_fixture(
            "valid_headerless_html.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("TABLE-013", output)

    def test_es04_shaped_headerless_html_passes(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content.replace(b"## Review Result", b"## Information")

        def mutate_entry(entry: dict[str, object]) -> None:
            tables = entry["headerless_tables"]
            assert isinstance(tables, list)
            assert isinstance(tables[0], dict)
            tables[0]["heading"] = "## Information"

        code, output, temporary = self._run_fixture(
            "valid_headerless_html.md",
            mutate=mutate,
            entry_mutate=mutate_entry,
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 0, output)

    def test_unverified_with_date_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content.replace(
                b'mirror_status: "VERIFIED"', b'mirror_status: "UNVERIFIED"'
            )

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("STATE-002", output)

    def test_verified_with_null_method_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content.replace(
                b'verification_method: "Independent PDF-to-Markdown semantic fidelity review"',
                b"verification_method: null",
            )

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("STATE-006", output)

    def test_source_hash_mismatch_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content.replace(SOURCE_HASH.encode(), b"0" * 64)

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("SRC-006", output)

    def test_wrong_manifest_reference_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content.replace(
                b"../../GOVERNANCE_DOCUMENT_MANIFEST.md", b"../wrong-manifest.md"
            )

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("MANIFEST-002", output)

    def test_mirror_self_hash_declaration_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content + b"\nMirror SHA-256: " + b"a" * 64 + b"\n"

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("HASH-001", output)

    def test_markdown_sha256_self_hash_declaration_fails(self) -> None:
        self._assert_hash_declaration_fails(b"Markdown SHA-256: ")

    def test_markdown_sha256_without_separator_self_hash_declaration_fails(self) -> None:
        self._assert_hash_declaration_fails(b"Markdown SHA256: ")

    def test_markdown_final_sha256_self_hash_declaration_fails(self) -> None:
        self._assert_hash_declaration_fails(b"Markdown SHA-256 (final): ")

    def test_markdown_hash_self_hash_declaration_fails(self) -> None:
        self._assert_hash_declaration_fails(b"Markdown hash: ")

    def test_authoritative_source_hash_is_not_self_hash(self) -> None:
        code, output, temporary = self._run_fixture("valid_textual_candidate.md")
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 0, output)

    def test_ordinary_sha256_discussion_passes(self) -> None:
        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md",
            mutate=lambda content: content
            + b"\nThe authoritative PDF provenance uses SHA-256.\n",
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 0, output)

    def test_checklist_count_state_mismatch_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content + b"\n- [ ] one configured checklist item\n"

        def mutate_entry(entry: dict[str, object]) -> None:
            entry.update({"expected_checkbox_total": 0, "expected_checked_total": 0})

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md",
            mutate=mutate,
            entry_mutate=mutate_entry,
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("CHECKLIST-001", output)

    def test_real_unchecked_checkbox_counts_normally(self) -> None:
        self._assert_checkbox_counts(b"\n- [ ] real item\n", 1, 0)

    def test_real_lowercase_checked_checkbox_counts_normally(self) -> None:
        self._assert_checkbox_counts(b"\n- [x] real item\n", 1, 1)

    def test_fenced_unchecked_checkbox_is_ignored(self) -> None:
        self._assert_checkbox_counts(b"\n```text\n- [ ] example\n```\n", 0, 0)

    def test_fenced_lowercase_checked_checkbox_is_ignored(self) -> None:
        self._assert_checkbox_counts(b"\n```text\n- [x] example\n```\n", 0, 0)

    def test_fenced_uppercase_checked_checkbox_is_ignored(self) -> None:
        self._assert_checkbox_counts(b"\n```text\n- [X] example\n```\n", 0, 0)

    def test_mixed_real_and_fenced_checkboxes_count_only_real(self) -> None:
        self._assert_checkbox_counts(
            b"\n- [ ] real item\n```text\n- [x] example\n- [X] example\n```\n",
            1,
            0,
        )

    def test_replacement_character_fails(self) -> None:
        code, output, temporary = self._run_fixture(
            "fail_replacement_character.md"
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("ENC-003", output)

    def test_invalid_utf8_fails(self) -> None:
        temporary, config_path = self._stage(
            "valid_textual_candidate.md", raw_override=b"\xff\xfe\xfa"
        )
        self.addCleanup(temporary.cleanup)
        code, output = self._run(config_path, Path(temporary.name))
        self.assertEqual(code, 1)
        self.assertIn("ENC-001", output)

    def test_duplicate_front_matter_key_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content.replace(
                b'manifest: "../../GOVERNANCE_DOCUMENT_MANIFEST.md"',
                b'manifest: "../../GOVERNANCE_DOCUMENT_MANIFEST.md"\n'
                b'document_id: "DUPLICATE"',
            )

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("META-004", output)

    def test_unknown_front_matter_key_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content.replace(
                b'manifest: "../../GOVERNANCE_DOCUMENT_MANIFEST.md"',
                b'manifest: "../../GOVERNANCE_DOCUMENT_MANIFEST.md"\n'
                b'unknown_key: "not allowed"',
            )

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("META-005", output)

    def test_partial_stale_metadata_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content.replace(
                b'mirror_status: "VERIFIED"', b'mirror_status: "STALE"'
            ).replace(
                b'verified_on: "2026-08-29"', b"verified_on: null"
            )

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("STATE-007", output)

    def test_duplicate_trust_notice_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content + b"\n" + validator.VERIFIED_NOTICE.encode() + b"\n"

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("STATE-011", output)

    def test_unterminated_fenced_block_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content + b"\n```text\nunterminated\n"

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("MD-001", output)

    def test_supported_fenced_block_passes(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content + b"\n```text\nprotected\n```\n"

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 0, output)

    def test_four_backtick_fence_with_compatible_closer_passes(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content + b"\n````text\nprotected\n````\n"

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 0, output)

    def test_four_backtick_fence_with_shorter_closer_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content + b"\n````text\nprotected\n```\n"

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("MD-005", output)

    def test_raw_placeholder_after_malformed_fence_cannot_false_pass(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content + b"\n````text\n```\n<Mechanism>Observation\n````\n"

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("MD-003", output)

    def test_unterminated_four_backtick_fence_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content + b"\n````text\nunterminated\n"

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("MD-001", output)

    def test_unbalanced_inline_code_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content + b"\nBroken `inline code\n"

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("MD-002", output)

    def test_malformed_markdown_table_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content + b"\n| A | B |\n| ---- |\n"

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("TABLE-003", output)

    def test_invalid_controlled_html_table_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content.replace(
                b"<td>PASS / FAIL</td>", b"<th>PASS / FAIL</th>"
            )

        code, output, temporary = self._run_fixture(
            "valid_headerless_html.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("MD-003", output)

    def test_private_use_extraction_character_fails(self) -> None:
        def mutate(content: bytes) -> bytes:
            return content + "\nPrivate bullet: \uf0b7\n".encode("utf-8")

        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md", mutate=mutate
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("ENC-005", output)

    def test_legitimate_capital_a_tilde_passes(self) -> None:
        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md",
            mutate=lambda content: content + "\nProper Unicode: Ã\n".encode("utf-8"),
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 0, output)

    def test_legitimate_capital_a_circumflex_passes(self) -> None:
        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md",
            mutate=lambda content: content + "\nProper Unicode: Â\n".encode("utf-8"),
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 0, output)

    def test_legitimate_extended_unicode_passes(self) -> None:
        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md",
            mutate=lambda content: content + "\nExtended Unicode: Ελληνικά 日本語\n".encode(
                "utf-8"
            ),
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 0, output)

    def test_retained_high_confidence_mojibake_fails(self) -> None:
        code, output, temporary = self._run_fixture(
            "valid_textual_candidate.md",
            mutate=lambda content: content + "\nCorrupted extraction: â€\n".encode(
                "utf-8"
            ),
        )
        self.addCleanup(temporary.cleanup)
        self.assertEqual(code, 1)
        self.assertIn("ENC-006", output)

    def test_duplicate_json_configuration_key_fails_with_exit_two(self) -> None:
        temporary = tempfile.TemporaryDirectory()
        self.addCleanup(temporary.cleanup)
        root = Path(temporary.name)
        config_path = root / "duplicate.json"
        config_path.write_text(
            '{"schema_version": 1, "schema_version": 1, '
            '"manifest_required": false, "mirrors": []}',
            encoding="utf-8",
        )
        code, output = self._run(config_path, root)
        self.assertEqual(code, 2)
        self.assertIn("CONFIG-001", output)


if __name__ == "__main__":
    unittest.main()
