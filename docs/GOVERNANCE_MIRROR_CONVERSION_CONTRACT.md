# Governance Mirror Conversion Contract

Operational guidance for converting authoritative English governance PDFs into
machine-readable Markdown mirrors.

## Authority and scope

The authoritative English PDF remains the source of truth. This contract is
subordinate to `AGENTS.md`, the authoritative English Documents A, B, and C,
and `docs/architecture_decisions/ADR_Governance_PDF_Verified_Markdown_Mirrors.md`.
It has no independent governance-authority rank.

The deterministic validator described by this contract is preflight and
regression tooling only. A validator PASS means only:

> No configured deterministic defect detected.

It does not certify semantic fidelity, change mirror trust state, or replace
SOL semantic review and Architect authorization.

## Conversion rules

### M-01 Headerless source tables

Preserve headerless table semantics. Do not invent Markdown column headers such
as `Field | Value`. A source-reviewed headerless structure may use the narrow
configured HTML form `<table><tr><td>...</td></tr></table>`.

### M-02 Literal generic identifiers containing `<...>`

Protect a complete literal identifier or pattern containing angle-bracket
placeholders with inline code, for example
`<Mechanism>Observation`, `<Mechanism>ObservationEvaluator`, and
`frc.robot.observation.<mechanism>`.

### M-03 Checklists

Preserve checklist wording, order, total count, and checked state. The
validator may verify only configured count and state; that mechanical result
does not prove wording or semantic fidelity.

### M-04 Blank templates

Preserve blank fields, blank cells, unchecked boxes, and other unselected
template state. Configure a structural blank-state expectation only when it is
deterministically known.

### M-05 Package and code identifiers

Preserve literal punctuation, capitalization, spelling, units, and identifier
boundaries.

### M-06 Architecture flows

Preserve explicit nodes, order, and arrows in textual representation when
`TEXTUAL` fidelity is appropriate. The validator does not prove that an arrow,
ownership relationship, or dependency direction retains its semantic meaning.

### M-07 PDF layout artifacts

Non-semantic page numbers and repeated layout-only headers may be omitted.
Repeated semantic content, labels, relationships, and revision information may
not be omitted merely because it repeats on a PDF page.

### M-08 Trust state

A candidate remains `UNVERIFIED` until independent semantic review and Architect
authorization. The validator does not transition `UNVERIFIED`, `VERIFIED`,
`STALE`, or `HOLD`.

### M-09 Provenance schema

Use the repository's strict flat front matter: one-line scalar keys, quoted
strings, literal `null`, no nested objects, no arrays, no multiline YAML, no
duplicate keys, and no unknown keys. Include the canonical trust notice that
the English PDF controls conflicts.

### M-10 Hash separation

The mirror records the authoritative PDF SHA-256 in its provenance header. The
final Markdown mirror SHA-256 belongs later in the G6 manifest. Never embed the
mirror's own final hash in the mirror.

### M-11 Encoding

Use strict UTF-8. Reject replacement characters, disallowed control characters,
and high-confidence PDF extraction corruption such as the private-use bullet
U+F0B7.

### M-12 Validator boundary

Deterministic validation is preflight and regression evidence only. It may
detect mechanical, schema, source-identity, rendering, and structural defects,
but it must never certify semantic fidelity.

## Deterministic validator

The repository-wide implementation is
`docs/tools/governance/validate_governance_mirrors.py`.

It reads the per-document expectations in
`docs/tools/governance/governance_mirror_expectations.json`, checks the
co-located PDFs and configured mirrors, and reports findings. It does not write
or repair files and does not require the future G6 manifest while
`manifest_required` is `false`.

Run it from the repository root with:

```powershell
py -3 docs/tools/governance/validate_governance_mirrors.py
```

Exit code `0` means all configured deterministic checks passed. Exit code `1`
means one or more mirror findings. Exit code `2` means a configuration or
setup error.

## Human review boundary

SOL and the Architect remain responsible for normative language, negation,
omissions, additions, ownership, dependency direction, flow meaning, table
meaning, checklist wording and order, and all spatial meaning in the
Architecture Poster. The poster remains
`SEMANTIC_WITH_VISUAL_REFERENCE` and requires direct PDF visual review.
