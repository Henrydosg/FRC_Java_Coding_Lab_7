# Governance 2.0 Delegated Rule — PROTECTED PATHS

> ACTIVE DELEGATED GOVERNANCE 2.0 RULE
>
> Incorporated by [root AGENTS.md](../../../AGENTS.md) within its exact delegated scope. Root controls conflicts pending HOLD and Architect reconciliation.

Delegated scope: Exact scope, frozen/unrelated state, sensitive/registry handling, User Git ownership and safe read-only inspection. This file creates no lesson, repair, publication or later-gate authorization.

Registered migration decision: [GOV2 ADR](../../architecture_decisions/ADR_GOV2_Governance_2_0_Agent_Instructions_State_and_History_Migration.md).

## Active scope/protected-state contract

Operate only within named authorized actions/paths. Preserve unrelated tracked drafts, untracked materials, frozen predecessors and parked candidates. Do not stage, clean, restore, reset, delete, overwrite, move, copy, normalize or absorb them without separate exact authorization. Normal lesson cache operations apply only inside the approved copied editable lesson.

ALL Git writes are User-owned. Sol/Codex must not add, commit, tag, push, restore, checkout, reset, clean, stash or perform equivalent mutations. Task-relevant non-mutating status/diff/log/show/rev-parse/ls-files inspection does not transfer ownership or establish remote publication.

NEVER open or read github-recovery-codes.txt. Avoid protected hardware-registry content unless strictly required and separately authorized; the G7 cutover does not authorize reading it. Report access truthfully, preserving the earlier disclosed search-scope incident; do not rewrite that history as never accessed.

| Protected example | Boundary |
| --- | --- |
| real_robot_programming/module_A01/A01_L06_PathPlannerPathAndRuntimeIntegration/src/main/deploy/pathplanner/paths/A01_L06_OneMeter_Forward.path | Existing unrelated tracked path draft, not a migration source |
| PathPlanner_Practice_2026/ | Preserve unrelated practice project |
| curriculum/FRC_Robot_Programming_For_Dummies/ | Preserve unrelated curriculum drafts/content |
| org/ and existing caches | Preserve unrelated content; no cleanup |
| docs/architecture_decisions/ADR_T00_Swerve_Characterization_and_Closed_Loop_Tuning_Roadmap.md | PARKED; not read, registered or activated |
| docs/Robot_Hardware/FRC_Robot_CAN_Address_Allocation_Registry.md | Not accessed/modified by G7 cutover |
| github-recovery-codes.txt | Never open/read; no sensitive contents copied into governance |
| C:/Users/xps7350i7/Desktop/FRC_GOV2_PreMigration_Recovery/GOV2_G2_PreMigration | Separate User recovery snapshot; do not modify/restore/clean |

The pre-GOV2 committed checkpoint protects committed bytes only; accepted separate G2 recovery protects dirty/untracked state. Cutover neither repeats recovery nor restores it. Governance destinations cannot absorb unrelated materials or make parked work authoritative.

## Traceable source families

These reviewed clauses retain full accepted A/H/C families. Normalization is limited to LF presentation, relocated section pointers, scoped User cache ownership and explicit registered GOV2 Git/physical-scope/future-reading decisions. Exact original bytes remain in the archive. Named historical lesson exclusions retain their original applicability and do not authorize work.

### Family A-L1826-L1834 — Repository structure invariants

Source: AGENTS L1826–L1834; accepted classification A; Inline no unauthorized reorganization; detailed layout rules

Rules

- One lesson = One independent WPILib project.
- Every lesson has its own docs.
- Every lesson has LESSON_STATUS.md.
- Do not create folders outside the approved structure.

---

### Family A-L2085-L2096 — Filesystem/change safety

Source: AGENTS L2085–L2096; accepted classification A; Protected paths and exact authorized scope inline

### Repository Safety

Never

- rename repository folders
- move repository folders
- delete repository folders
- overwrite repository folders
- reorganize repository structure

unless explicitly approved by the user.

### Family A-L2180-L2194 — Git ownership/prohibitions

Source: AGENTS L2180–L2194; accepted classification A; User Git writes inline; read-only Git wording requires decision

The decision is established in registered GOV2 ADR §13. The inventory note is retained as design provenance, not a current unresolved permission question.

## 13. Git Rules

ALL Git writes are User-owned. Codex may perform task-relevant read-only Git inspection, but shall not perform Git mutations.

Workflow

git status
→ git add
→ git commit
→ git push

Never claim GitHub was updated unless push succeeds.

---
