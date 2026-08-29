# ADR: Governance PDF to Verified Markdown Mirrors

- Decision: `APPROVED`
- Date: `2026-08-28`
- Decision owners: Architect and User
- Scope: Governance-document consumption and provenance only
- Implementation phase: `G1 — Governance ADR/schema decision`

## Context

The repository uses English Documents A, B, and C PDFs as its authoritative
governance sources. `AGENTS.md` has the highest authority, followed by Document
A, Document B, Document C, the repository README, and repository source code.
DOCX files remain editable source documents, and Vietnamese documents remain
reference translations.

PDFs are stable authority artifacts but are less convenient for deterministic
search, targeted retrieval, machine-readable comparison, and repeatable audit
work. The repository needs a controlled way to add Markdown representations
without creating a second authority or weakening historical evidence.

## Problem

An ungoverned PDF-to-Markdown conversion could omit normative language, flatten
tables or checklists, lose poster relationships, become stale after a PDF
change, or be treated as an independent authority. It could also create an
incorrect impression that prior PDF-based lesson reviews are invalid.

## Decision

The repository may later create faithful Markdown mirrors of authoritative
English Documents A, B, and C PDFs. This ADR establishes the governing model
and verification contract only. It does not create a mirror, convert a PDF,
create a manifest, or activate routine Markdown consumption.

The approved future flow is:

```text
Authoritative English PDF
    -> faithful conversion
    -> VERIFIED Markdown Mirror
    -> routine machine-readable governance consumption
```

## Authority Model

The existing authority order remains unchanged:

```text
AGENTS.md
    -> authoritative English Documents A/B/C PDFs
    -> README.md
    -> repository implementation
```

A Markdown mirror has no independent authority rank. It is a derived
machine-readable representation of one authoritative PDF. If a mirror and its
PDF differ, the PDF controls.

DOCX files remain editable sources. Vietnamese documents remain reference
translations. This ADR does not change the Frozen Backbone, the Frozen
Interface Contract, package responsibilities, roadmap governance, or lesson
lifecycle rules.

## Mirror States

The only allowed mirror states are:

- `UNVERIFIED`: candidate conversion; not usable for governance reading.
- `VERIFIED`: independently reviewed against the identified PDF; eligible for
  routine machine-readable governance consumption only after later policy
  activation.
- `STALE`: source PDF or mirror integrity no longer matches recorded
  provenance; not usable for governance reading.
- `HOLD`: a conflict, ambiguity, or fidelity concern is under reconciliation;
  mirror-based consumption stops.

Only `VERIFIED` mirrors may satisfy routine machine-readable governance reading
after the complete migration and separately authorized policy activation.

## Fidelity Classes

The only approved fidelity classes are:

- `TEXTUAL`: a faithful Markdown rendering of normative prose, lists, tables,
  checklists, flows, and revision information.
- `SEMANTIC_WITH_VISUAL_REFERENCE`: a faithful textual and relationship-focused
  representation that retains the PDF as the required reference for spatial or
  layout meaning.

The Frozen Backbone Architecture Poster must use
`SEMANTIC_WITH_VISUAL_REFERENCE`. Its mirror must never claim pixel or layout
equivalence. When the poster's spatial meaning matters, the authoritative PDF
remains required.

## Directory Mapping and Provenance

Future mirrors shall be colocated with their source PDFs and use the same file
name stem. For example:

```text
docs/Document_B/English/01_Frozen_Development_Workflow_EN.pdf
docs/Document_B/English/01_Frozen_Development_Workflow_EN.md
```

Separate PDF and Markdown directory trees are prohibited.

Each future mirror shall include a concise provenance header that identifies at
least its document ID, title, class, language, mirror state, fidelity class,
authoritative PDF path, authoritative PDF SHA-256, source version/status,
verification date, verification method, and future manifest reference. The
header shall state that the PDF controls if a conflict exists.

Verification metadata is controlled by `mirror_status`:

```text
UNVERIFIED
    -> verified_on: null
    -> verification_method: null

VERIFIED
    -> verified_on: "YYYY-MM-DD"
    -> verification_method:
       "Independent PDF-to-Markdown semantic fidelity review"
```

For `VERIFIED`, both verification fields must be populated with the actual
independent-review and Architect-authorization evidence. `STALE` and `HOLD` are
not eligible for routine trusted mirror consumption. They may retain historical
`verified_on` and `verification_method` values when the mirror was previously
`VERIFIED`; retained historical metadata does not imply current `VERIFIED`
status. `mirror_status` is the controlling trust-state field.

## Manifest and Hash Policy

The future central manifest path is:

`docs/GOVERNANCE_DOCUMENT_MANIFEST.md`

It is a verification and index record, not an independent semantic authority.
It is not created during G1 and may be created only in the later authorized G6
migration phase. The manifest shall record, for each source/mirror pair:

- document ID, class, and title;
- authoritative PDF path and SHA-256;
- Markdown mirror path and final SHA-256;
- mirror state and fidelity class;
- verification date and evidence; and
- source version/status and any exceptional notes.

A SHA-256 proves file identity and integrity only. It does not prove semantic
fidelity. Semantic fidelity requires independent review against the
authoritative PDF.

A future mirror may be consumed only when the current PDF hash matches the
mirror header and manifest, and the current Markdown hash matches the manifest.
Any mismatch changes the mirror to `STALE` or `HOLD` and requires direct PDF
consultation and reconciliation.

The non-circular final-hash order is:

```text
candidate mirror
    -> UNVERIFIED
    -> independent semantic fidelity audit
    -> Architect authorization
    -> populate VERIFIED metadata
    -> final mirror content stabilizes
    -> calculate final Markdown mirror SHA-256
    -> record final hash in the manifest
```

The mirror may record the authoritative source PDF SHA-256. It must not contain
its own Markdown SHA-256; the manifest records the final Markdown mirror
SHA-256.

## Fidelity Verification Principle

Conversion must preserve normative language, negation, identifiers, numbers,
units, paths, tables, checklist states, flows, and revision records. PDF text
extraction and the editable DOCX may assist conversion, but neither replaces
comparison against the authoritative PDF.

Textual documents require section, table, checklist, and visual review as
applicable. The poster requires faithful transcription, Markdown tables,
explicit relationship descriptions, and plain-text or ASCII flows. Mermaid may
be supplementary only if separately authorized; it must never be the sole
semantic representation.

## Separation of Duties

The locked workflow is:

```text
TERRA
    -> creates an UNVERIFIED candidate mirror
SOL
    -> independently performs a read-only fidelity audit
ChatGPT Architect
    -> reviews evidence and authorizes VERIFIED status
User
    -> owns Git publication
```

The implementation agent that creates a candidate mirror must not independently
declare that same candidate semantically `VERIFIED` in the same implementation
step.

## Future Reading Policy

After complete migration and separately authorized policy activation, routine
governance work may consume `VERIFIED` Markdown mirrors for deterministic search
and relevant section-targeted retrieval where repository governance permits it.
This does not replace every required PDF review with indiscriminate reading of
every Markdown file.

Direct authoritative PDF consultation remains required when a mirror is missing,
not `VERIFIED`, stale, on `HOLD`, hash-mismatched, ambiguous, semantically in
conflict, under formal fidelity investigation, needed for forensic
reconstruction, or when poster spatial/layout meaning matters.

## Historical Continuity

Previous lessons were reviewed against authoritative PDFs. Their historical
evidence remains valid. Markdown mirrors do not retroactively replace that
evidence or reinterpret prior reviews.

Future lessons may consume `VERIFIED` mirrors only after the later policy
activation. A future mirror defect does not invalidate the authoritative PDF or
completed historical PDF-based reviews.

## Conflict, HOLD, and Rollback

If the PDF and a mirror conflict, the PDF controls; the affected mirror enters
`HOLD`; mirror-based consumption stops; and reconciliation plus independent
verification are required before a `VERIFIED` state can be restored.

Rollback is direct consumption of the authoritative PDFs. A mirror may be
marked `STALE` or `HOLD` without modifying the source PDF or historical lesson
evidence. No work may rely on a disputed mirror for a governing requirement.

## Migration Phases

The approved conceptual sequence is:

1. `G1` — Governance ADR/schema decision.
2. `G2` — One low-risk pilot mirror.
3. `G3` — Independent pilot fidelity audit.
4. `G4` — Remaining textual mirrors.
5. `G5` — Architecture poster mirror separately.
6. `G6` — Manifest and final hashes.
7. `G7` — AGENTS/README/index policy activation.
8. `G8` — Final read-only governance audit.
9. `G9` — User Git publication boundary.
10. `G10` — Return to paused lesson workflow.

The approved future pilot source is:

`docs/Document_B/English/01_Frozen_Development_Workflow_EN.pdf`

The pilot is not authorized by this ADR implementation step.

## Explicit Non-Goals

This ADR does not authorize:

- creating any Markdown mirror or converting any PDF;
- creating a governance manifest;
- modifying `AGENTS.md`, `README.md`, Document B/C indexes, PDFs, DOCX files,
  lessons, source code, tests, Gradle, vendordeps, resources, or PathPlanner
  assets;
- changing PDF authority rank, the Frozen Backbone, the Frozen Interface
  Contract, or roadmap governance;
- activating or implementing V00_L06; or
- Git add, commit, push, restore, checkout, clean, reset, or stash operations.

## Consequences

This decision provides a controlled future migration path while preserving
authoritative PDF governance and historical evidence. It creates no currently
usable mirror and changes no current required-reading policy. Each later phase
requires its own authorization and verification evidence. Until G7 is complete,
the repository continues to use the existing PDF-authority reading policy.
