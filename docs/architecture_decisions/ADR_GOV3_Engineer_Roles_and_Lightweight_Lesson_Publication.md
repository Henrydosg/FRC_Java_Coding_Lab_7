# ADR: GOV3 — Engineer Roles and Lightweight Lesson Publication

- Status: **FINAL CANDIDATE** — revision 3 with final bounded corrections; Architect design review APPROVED WITH CHANGES; final Architect acceptance and User approval/publication pending. Not effective governance until User-owned publication.
- Date: 2026-10-09.
- Decision authority: ChatGPT / Architect. User approval required.
- Drafting engineer: Claude Code (Primary Engineer for this documentation task only).
- Revision basis: Architect reviews of revision 1 (required changes #1–#7), revision 2 (seven correction groups) and revision 3 (final bounded corrections).
- Recording event: [GOV3 adoption recording](../governance/history/events/GOV3_ADOPTION_RECORDING_2026-10-09.md).

## 1. Context

Governance 2.0 is complete. M01_L01 needed many separate preparation, Design Lock,
closure, snapshot-publication and metadata-reconciliation commits while adding no
runtime code. M01 has 17 remaining lessons. GOV3 shortens publication mechanics
without weakening architecture, Simulation or hardware-safety gates.
Design principle: simple enough, safe enough, correct enough — do not over-engineer.

## 2. Role model

| Role | Responsibility and boundary |
| --- | --- |
| ChatGPT | Architect / Mentor / Reviewer; scope authorization, governance review and PASS / HOLD / FAIL gate authority. |
| Claude Code | Repository-aware Engineer; same engineering permission envelope as Codex; no independent governance authority. |
| Codex | Repository-aware Engineer; same engineering permission envelope as Claude Code; authorized scope only. |
| User | Verification Engineer; sole Git/GitHub and powered-hardware operator; owner of authoritative build/test, Simulation, Glass / AdvantageScope, Driver Station, real-hardware and SysId verification. |

Claude Code and Codex have equivalent engineering permissions, not equivalent
governance authority. Neither engineer may:

- authorize its own scope;
- activate a lesson;
- approve a lifecycle gate;
- override the Architect;
- perform Git writes;
- perform powered hardware verification on behalf of the User.

Neither engineer may convert BUILD SUCCESSFUL or Simulation results into
REAL HARDWARE VERIFIED, or authorize motor movement.

## 3. Engineer policy

Every engineer-assigned task has exactly one Primary Engineer, named when the
task is authorized (e.g. "Primary Engineer: Claude Code"). Work performed by the
User is not an engineer-assigned task. A second engineer participates only when
useful — independent review, difficult debugging or comparison — and does not edit
the repository while the Primary Engineer's work is uncommitted. Using both
engineers on every lesson is not required. Review of one's own work is self-review,
not independent review.

## 4. Standard lesson publication — three commit groups

Commit grouping changes publication mechanics only. It does not merge, weaken,
bypass or replace any architecture, Simulation, safety or real-hardware gate.
Each gate remains a separate decision with its existing authority and ordering.

### GROUP 1 — ACTIVATE / BASELINE

- User copies the frozen predecessor, renames it, deletes generated artifacts and
  runs the inherited baseline build.
- Engineer performs the inheritance and architecture audit and drafts the Design Lock.
- Architect decides the Design Lock.
- Lifecycle activation is recorded and CURRENT_STATE is set to IN_PROGRESS.
- The commit tracks the FULL inherited independent WPILib lesson snapshot (src,
  Gradle files, vendordeps, deploy assets and lifecycle docs), not only docs.
- Example message: "Activate M01_L02 and record inherited baseline".

### GROUP 2 — IMPLEMENT / VERIFY

- Authorized technical scope only: Java, tests, permitted Constants/config,
  IO adapters, Simulation changes and implementation evidence.
- The User performs the required verification gates.
- An evidence-only lesson with no Java implementation is valid. Group 2 then
  records only the required evidence, or is merged into Group 3 when no separate
  evidence commit is needed. Empty or placeholder commits are never created to
  satisfy the group count.
- Example message: "Implement M01_L02 Intake real IO and safe stop".

### GROUP 3 — COMPLETE / FREEZE

- Records together: README, LESSON_STATUS, LESSON_PLAN and LESSON_CHECKLIST final;
  transition guide FINAL/PASS; closure evidence; CURRENT_STATE; required history record.
- Before push: after Architect closure acceptance, the documents record the accepted
  lifecycle state COMPLETE / FROZEN / READ-ONLY. They do not claim PUBLISHED or
  REMOTE VERIFIED.
- After successful User-owned push and fresh remote verification (e.g. `git fetch
  origin main` followed by HEAD == origin/main), the lesson publication is
  established by Git history and the User's verification report. The lesson is then
  COMPLETE / FROZEN / READ-ONLY / PUBLISHED / REMOTE VERIFIED.
- A stale local remote-tracking reference alone is not sufficient evidence of
  REMOTE VERIFIED.
- No additional metadata-reconciliation commit is required. CURRENT_STATE and frozen
  lesson documents are NOT edited after push merely to record a SHA or to change
  PENDING to PUBLISHED.
- Prospective exception to GOV2: from M01_L02 onward, this Git-derived publication
  evidence replaces the GOV2 publication-metadata-reconciliation recording mechanism.
  Records made under GOV2 remain unchanged. The two mechanisms never apply to the
  same lesson, so no competing authority is created.
- Example message: "Complete and freeze M01_L02 Intake real IO configuration".

### Exceptions — bounded repair

Three groups are the standard happy-path lifecycle, not an absolute three-commit
limit. Evidence-driven bounded repair commits may occur inside the active group
when necessary (e.g. G2 test FAIL → bounded repair → verify PASS; a real closure
documentation defect → bounded repair inside G3). A repair creates no new lifecycle
phase.

A repair after a frozen snapshot has been published is NOT a group-internal repair;
it still requires exceptional bounded-repair authorization under root AGENTS.md §9
and LIFECYCLE governance.

## 5. Commit identity

Lifecycle files never record the SHA of the commit that contains them. Group 3
documents use the semantic identifier "GOV3 GROUP 3 — this closure publication
commit". Git history is the authority for the actual SHA. The User's post-push Git
verification evidence (fresh fetch, then HEAD == origin/main) is reported to the
Architect and is not required to be written back into frozen documentation.

## 6. Hardware safety — unchanged

Commit consolidation does not consolidate safety gates. For every powered step:

    Engineer writes code → User build/tests → Simulation where applicable
    → Architect authorizes bounded powered verification → User operates robot
    → User reports evidence → Architect PASS / HOLD / FAIL

All existing Simulation-before-hardware, zero-output inhibition, output/time bounds,
Test-mode admission, interruption/mode-loss/Disable stop checks, truthful evidence
vocabulary, Frozen Backbone, Documents A/B/C, the registered M01 roadmap, contract
change control and one-editable-lesson rules remain in force.

## 7. Applicability and root changes

Applies from M01_L02 onward; historical records are unchanged. Root AGENTS.md §2
(role table), §11 (engineer software-only builds/tests) and §15 (navigation) are
updated. The delegated rule docs/governance/rules/WORKFLOW.md is synchronized with
§11. CLAUDE.md imports AGENTS.md and adds no authority.

## 8. Engineer software-only builds and unit tests — DECIDED

Architect decision 2026-10-09: YES. Claude Code and Codex may run explicitly
authorized local, non-hardware Gradle builds and unit tests (e.g. `.\gradlew build`,
`.\gradlew test`) within the authorized lesson, as preliminary convenience evidence.

Workflow: engineer writes code → engineer runs software-only build/tests and fixes
in-scope failures → User runs authoritative verification → Architect PASS / HOLD / FAIL.

Engineer-run results never replace User-owned authoritative verification or
Architect gate decisions. Deployment, powered hardware, Driver Station, interactive
Simulation and SysId remain User-owned. Task-specific READ-ONLY or NO-EXECUTION
restrictions take precedence. Subject to Documents A/B/C and all higher authority.

Engineer check at drafting: English Document B 00–04 require a build after each
change but do not assign who runs it; no higher-authority conflict was found.
