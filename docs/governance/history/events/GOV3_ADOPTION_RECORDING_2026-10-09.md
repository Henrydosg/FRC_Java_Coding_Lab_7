# GOV3 — Engineer Roles and Lightweight Lesson Publication — Adoption Recording — 2026-10-09

## Authority and status

- **Authorization:** Architect brief "FRC JAVA CODING LAB 7.0 — GOV3 FINAL BOUNDED CORRECTION" (verdict APPROVED WITH CHANGES on revision 3), supplied by the User in chat on 2026-10-09. Primary Engineer: Claude Code. Bounded governance documentation recording only.
- **Decision record:** [ADR_GOV3](../../../architecture_decisions/ADR_GOV3_Engineer_Roles_and_Lightweight_Lesson_Publication.md) — FINAL CANDIDATE.
- **Review chain:** revision 1 APPROVED WITH CHANGES (#1–#7) → revision 2 APPROVED WITH CHANGES (seven correction groups; §8 decided YES) → revision 3 APPROVED WITH CHANGES (final bounded corrections: fresh remote verification, WORKFLOW synchronization, disclosure recording).
- **Status at recording:** working-tree recording only; self-review only; final Architect acceptance and User approval/publication pending. GOV3 is not effective governance until User-owned publication. No commit/push SHA is claimed.
- **Lesson state unchanged:** M01_L01 COMPLETE / FROZEN / READ-ONLY / PUBLISHED / REMOTE VERIFIED; active editable lesson NONE; M01_L02 NOT AUTHORIZED.

## Exact recording scope

- docs/architecture_decisions/ADR_GOV3_Engineer_Roles_and_Lightweight_Lesson_Publication.md (new)
- AGENTS.md — §2 role table, §11 engineer software-only build/test permission, §15 navigation link
- docs/governance/rules/WORKFLOW.md — §"Active operational contract" one-sentence synchronization
- docs/governance/rules/PROTECTED_PATHS.md — A01_L06 path entry annotation
- CLAUDE.md (new)
- docs/governance/history/events/GOV3_ADOPTION_RECORDING_2026-10-09.md (this event, new)

CURRENT_STATE, root README, history indexes, frozen lessons, Java, tests, Constants, configuration and deploy assets are unchanged. Under ADR_GOV3 §4 Group 1, CURRENT_STATE next records GOV3 status when a lesson is activated.

## Checks performed by the engineer

- Governance mirror deterministic preflight (`py -3 docs/tools/governance/validate_governance_mirrors.py`): RESULT PASS — 12 mirrors, 12 source hashes matched, 0 deterministic findings. Integrity check only; no semantic-fidelity certification.
- Document B English 00–04: requires a build after each change; does not assign who runs it. No higher-authority conflict with ADR_GOV3 §8 found.
- WORKFLOW.md lines 39–40 and 64 describe User-run authoritative verification and do not exclusively prohibit engineer convenience runs; left unchanged. Historical source-family text naming Codex only is preserved as provenance.
- No project build/test, Simulation, Glass/Driver Station, real hardware or SysId execution. No Git writes.

## Disclosure

1. **Recovery codes.** The User reported moving github-recovery-codes.txt out of the repository on 2026-10-09 (User-run Test-Path at the former repository path = False; `git status` no longer lists it). Claude Code never opened the file. This evidence shows only absence from the checked path; the new location is not recorded here.
2. **A01_L06 path file — disclosed scope deviation.** Claude Code advised, and the User performed, `git restore` on real_robot_programming/module_A01/A01_L06_PathPlannerPathAndRuntimeIntegration/src/main/deploy/pathplanner/paths/A01_L06_OneMeter_Forward.path, which PROTECTED_PATHS listed as an existing unrelated draft to preserve. Before the restore, Claude Code inspected the diff read-only and characterized it as apparent PathPlanner GUI auto-save output (added schema fields, a floating-point artifact and one empty event marker). That characterization is an engineer observation, not independent proof that the discarded changes were unimportant. A read-only check after the restore found the path clean and equal to HEAD e831756. The historical fact of the earlier uncommitted modification is preserved in PROTECTED_PATHS. No further restore, reset or clean is authorized.
3. The protected T00 candidate and CAN registry contents were not accessed.
