# Governance 2.0 Delegated Rule — READING AND AUTHORITY

> ACTIVE DELEGATED GOVERNANCE 2.0 RULE
>
> Incorporated by [root AGENTS.md](../../../AGENTS.md) within its exact delegated scope. Root controls conflicts pending HOLD and Architect reconciliation.

Delegated scope: Task-reading matrix, authority order, PDF/mirror trust/fallback, state projection and current/history precedence. This file creates no lesson, repair, publication or later-gate authorization.

Registered migration decision: [GOV2 ADR](../../architecture_decisions/ADR_GOV2_Governance_2_0_Agent_Instructions_State_and_History_Migration.md).

## Active loading and authority contract

Under active exact root incorporation and delegated scope, normal startup reads root AGENTS, CURRENT_STATE, then the root task-reading matrix. No file gains authority merely by existing, linking, indexing, or tool discovery. Root controls inconsistencies pending STOP/HOLD and Architect reconciliation. Delegated rules cannot contradict Documents A/B/C; registered ADR scope and applicability remain unchanged.

The authority order remains root AGENTS -> authoritative English Document A -> Document B -> Document C -> root README -> repository source. A VERIFIED mirror has no independent authority. Its matching source/mirror hashes and trust metadata permit machine reading within fidelity limits; source PDFs control conflict, ambiguity, forensic/fidelity investigation, missing or untrusted mirrors, and poster spatial meaning. DOCX is editable source; Vietnamese is reference translation. Existing .gitattributes byte-integrity policy and manifest scope remain unchanged.

| Task | Required applicable reading before dependent work |
| --- | --- |
| Every task | Root, canonical CURRENT_STATE, exact authorization and scope; expand matrix when another source may govern |
| Lesson design/implementation/review | Applicable A/B/C; registered roadmap; relevant ADRs and exceptions; lesson lifecycle documents/transition guide; relevant source/tests/evidence; predecessor and verification limits |
| Architecture review | A/B/C and applicable registered architecture/repair ADRs; actual ownership/dependency evidence and approved scope |
| Governance/state/history work | Governing ADR, accepted decisions, source map and rule coverage; relevant original ranges/events and chronology |
| Mirror consumption | Required current validator PASS, manifest registration/VERIFIED status, both source and mirror integrity, fidelity limits and PDF fallback |
| M00 student documentation | M00 roadmap, applicable lifecycle/guide records, separate equivalent English/Vietnamese documentation contract |

History is loaded when needed for the task, not globally on every ordinary task. Task scoping never permits skipping potentially governing technical rules. The source required-reading family below is normalized to the explicitly approved active loading model; task scope does not waive applicable reading obligations. Historical approvals, candidates and parked work do not supply fresh permission.

The single CURRENT_STATE is an operational projection, not an independent roadmap, implementation, repair or publication authority. Its changes require accepted adjudication/already-authorized gate, exact recording scope, semantic review and User-owned publication; it cannot self-authorize. Root and README point to that cursor. Accepted decisions and explicit supersession determine precedence; file position, a Current heading, apparent SHA recency and an old approval token do not. Prior state remains archived with provenance in the event store.

Applicable package-repair decisions also remain at their registered paths: [ACM-01-F01](../../architecture_decisions/ADR_M00_L16_ACM_01_F01_Swerve_Observation_Package_Repair.md), [ACM-01-F02](../../architecture_decisions/ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md), [ACM-01-F03](../../architecture_decisions/ADR_M00_L16_ACM_01_F03_Learning_Trajectory_Factory_Package_Repair.md), and [ACM-01-F04](../../architecture_decisions/ADR_M00_L16_ACM_01_F04_Autonomous_Command_Family_Package_Repair.md). Their consumed repair scope is historical; reading them is not renewed implementation permission.

## Traceable source families

These reviewed clauses retain full accepted A/H/C families. Normalization is limited to LF presentation, relocated section pointers, scoped User cache ownership and explicit registered GOV2 Git/physical-scope/future-reading decisions. Exact original bytes remain in the archive. Named historical lesson exclusions retain their original applicability and do not authorize work.

### Family A-L1353-L1356 — Repository identity and English normativity

Source: AGENTS L1353–L1356; accepted classification A; Keep identity/language rule directly visible

# FRC Java Coding Lab 7.0 — Repository Rules

English is normative. Vietnamese is explanatory.

### Family A-L1711-L1735 — Required reading before work

Source: AGENTS L1711–L1735; accepted classification A; Inline obligation; detailed reading matrix in `rules/READING_AND_AUTHORITY.md`

## 1. Required Reading

Under the approved task-reading matrix, Codex MUST read the applicable potentially governing items below before dependent work. Root AGENTS and canonical CURRENT_STATE are the startup minimum:

1. AGENTS.md
2. README.md
3. docs/Document_A/FRC_Final_Frozen_Backbone_Guide_EN.pdf
4. docs/Document_A/ES-06_Frozen_Interface_Contract_EN.pdf
5. docs/Document_B/English/00_Engineering_Standard_Overview_EN.pdf
6. docs/Document_B/English/01_Frozen_Development_Workflow_EN.pdf
7. docs/Document_B/English/02_Java_Coding_Standard_EN.pdf
8. docs/Document_B/English/03_Architecture_Review_Checklist_EN.pdf
9. docs/Document_B/English/04_Lesson_Module_Checklist_EN.pdf
10. docs/Document_C/English/00_Observation_Architecture_Overview_EN.pdf
11. docs/Document_C/English/01_Observation_Model_Contract_EN.pdf
12. docs/Document_C/English/02_Observation_Package_Standard_EN.pdf
13. docs/Document_C/English/03_Observation_Architecture_Checklist_EN.pdf
14. Active lesson LESSON_STATUS.md
15. Active lesson source code

Only the English PDF documents are authoritative.
DOCX files are editable source documents.
Vietnamese documents are reference translations and are not required reading.

### Family A-L1736-L1748 — Authority order and conflict stop

Source: AGENTS L1736–L1748; accepted classification A; Entire authority order and HOLD rule inline

### Authority Order

1. AGENTS.md
2. Document A
3. Document B
4. Document C
5. README.md
6. Repository Source Code

If documents conflict, the higher priority document wins.

Stop immediately if repository code conflicts with Document A or Document B.

### Family A-L1749-L1796 — VERIFIED mirror reading policy

Source: AGENTS L1749–L1796; accepted classification A; Inline trust/fallback minimum; full procedure in reading rules; manifest/mirror ADR

### Verified Markdown Mirror Reading Policy

Activated on 2026-08-29. Authoritative English PDFs remain authoritative, and
the authority order above is unchanged. A required English PDF reading item may
be satisfied for routine machine-readable reading by its co-located Markdown
mirror only when the mirror is `VERIFIED`, is registered in
`docs/GOVERNANCE_DOCUMENT_MANIFEST.md`, passes the required integrity checks,
and is used within its fidelity-class limits. A mirror has no independent or
equal authority, and the PDF controls every conflict.

At the first governance use in a task, and at the start of every formal
governance or architecture audit, Codex MUST:

1. run or confirm a current PASS from
   `py -3 docs/tools/governance/validate_governance_mirrors.py`;
2. consult `docs/GOVERNANCE_DOCUMENT_MANIFEST.md`;
3. confirm every applicable mirror is `VERIFIED` and registered;
4. compare each applicable current Markdown SHA-256 with its manifest hash; and
5. sufficiently read every applicable VERIFIED mirror to cover all potentially
   governing sections. Narrow snippets alone do not satisfy a formal audit.

After those checks pass, targeted retrieval of relevant mirror sections is
allowed for follow-up work within the same unchanged task and scope. Reading
must expand whenever another section could govern the decision. Targeted
reading never permits skipping relevant governance requirements.

Direct authoritative PDF consultation is mandatory when a mirror or manifest
record is missing; a mirror is `UNVERIFIED`, `STALE`, or `HOLD`; a source or
Markdown hash mismatches; PDF and Markdown conflict; wording is ambiguous;
fidelity is questioned or under review; forensic or historical reconstruction
is required; a formal review explicitly requires source confirmation; or the
Architecture Poster's spatial or visual meaning matters. Mirror consumption
stops for a conflicting or disputed area. Do not silently rewrite a PDF,
mirror, hash, or trust state; governed reconciliation is required.

The 11 `TEXTUAL` mirrors may support routine semantic reading after integrity
verification, but they are not authoritative. The VERIFIED Architecture Poster
mirror is `SEMANTIC_WITH_VISUAL_REFERENCE` and may represent explicit text,
relationships, and source-preserved flow wording. Its authoritative PDF remains
mandatory for landscape arrangement, adjacency, shared boxes, color emphasis,
spatial grouping, relative prominence, or visual hierarchy.

The manifest is an integrity and verification index only, not semantic
authority. It supports source/mirror mapping, provenance checks, and final
Markdown hash lookup. It does not automatically transition mirror trust state.

---

### Family C-L2229-L2249 — Registered ADR references

Source: AGENTS L2229–L2249; accepted classification C; Root essential pointers plus navigation registry; existing ADRs remain in place

### Approved Architecture Decision Records

Lesson-specific decisions shall be recorded outside global governance and referenced here.

- S00_L19 / S00_L20 driver-input ownership and migration:
  `docs/architecture_decisions/ADR_S00_L19_L20_Driver_Input_Ownership.md`
- Post-S00 A00 roadmap authorization:
  `docs/architecture_decisions/ADR_A00_Autonomous_Command_Foundation_Roadmap.md`
- Post-A00 A01 roadmap authorization:
  `docs/architecture_decisions/ADR_A01_Autonomous_Navigation_Path_Following_Roadmap.md`
- Post-A01 V00 roadmap authorization:
  `docs/architecture_decisions/ADR_V00_AprilTag_Vision_Observation_and_Pose_Fusion_Roadmap.md`
- Post-V00 M00 roadmap and preparation authorization:
  `docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md`
- A01_L08 exceptional autonomous safety/robustness reopen:
  `docs/architecture_decisions/ADR_A01_L08_Autonomous_Safety_Robustness_Reopen.md`
- V00_L07 inherited Swerve architecture/robustness integrity reopen:
  `docs/architecture_decisions/ADR_V00_L07_Inherited_Swerve_Architecture_Robustness_Integrity_Reopen.md`
- M00_L16 P3-H01 exceptional configuration-authority repair:
  `docs/architecture_decisions/ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md`

### Family A-L2250-L2260 — Roadmap compliance constraints

Source: AGENTS L2250–L2260; accepted classification A; Registration, locked order, and separate activation inline

The S00_L19/S00_L20 decision does not change the Frozen Backbone, the authority order, or the
S00_L15-S00_L24 roadmap. The separately referenced A00 decision authorizes only the post-S00
module and `module_A00` location; it does not change the Frozen Backbone or authority order.

The approved A01 decision authorizes `A01 - Autonomous Navigation and Path Following` and
`module_A01` as the successor boundary after frozen A00_L04. A00 is closed at A00_L04; A00_L05
is prohibited. A01 inherits frozen A00_L04, and its order is governed by the approved A01 ADR.
Lessons shall not be reordered, renamed, merged, split, inserted, or skipped without the
architecture/governance approval required by that ADR. One lesson remains one new architectural
concept, and frozen predecessor protection remains mandatory.
