# Governance 2.0 Delegated Rule — LIFECYCLE

> ACTIVE DELEGATED GOVERNANCE 2.0 RULE
>
> Incorporated by [root AGENTS.md](../../../AGENTS.md) within its exact delegated scope. Root controls conflicts pending HOLD and Architect reconciliation.

Delegated scope: Independent lessons, inheritance, editable count, lifecycle/suspension/reopen, bounded repair and separate gates. This file creates no lesson, repair, publication or later-gate authorization.

Registered migration decision: [GOV2 ADR](../../architecture_decisions/ADR_GOV2_Governance_2_0_Agent_Instructions_State_and_History_Migration.md).

## Active lifecycle and exceptional-reopen contract

One lesson is one independent WPILib project with one new concept. A registered applicable roadmap and separate action authorization precede work. Normal successors copy/rename the correct approved COMPLETE/FROZEN predecessor; never recreate from scratch or silently substitute donors. Preserve reconstruction and exception provenance. User-authorized generated-cache cleanup is confined to the specifically approved copied editable project, never unrelated or protected state.

At most one authorized lesson is editable; zero is valid. IN_PROGRESS is editable within scope; COMPLETE is a frozen snapshot with FROZEN/READ-ONLY protection. SUSPENDED preserves unfinished bytes, is neither COMPLETE nor FROZEN nor editable, and does not count as active; approved resume uses preserved state unless a separate reconciliation is authorized. REOPENED is provenance qualifying IN_PROGRESS, not a generic status.

Material post-freeze safety/correctness/architecture/hardware-runtime/verification counterevidence requires explicit Architect and User approval, exact bounded scope, preserved historical evidence, one editable lesson, focused and inherited regression gates, applicable Simulation/real-robot evidence and explicit re-freeze. No unrelated feature/refactor is permitted.

Roadmap registration, preparation, activation, Design Lock/implementation, repair, closure, freeze, publication, and successor work are separate permissions. COMPLETE/FROZEN does not prove publication or physical performance. Historical/consumed approvals remain consumed.

Maintain README, LESSON_STATUS, LESSON_PLAN, LESSON_CHECKLIST and the applicable transition guide; finalize the guide only after implementation and required verification, before COMPLETE/FROZEN. Approved legacy applicability qualifications remain traceable; do not invent missing historical documents or verification.

The lifecycle verification sequence in the preserved source family remains User-operated and applicability-qualified. Applicable hardware must not be silently skipped; approved deferral must not be retroactively converted into a physical requirement.

## Traceable source families

These reviewed clauses retain full accepted A/H/C families. Normalization is limited to LF presentation, relocated section pointers, scoped User cache ownership and explicit registered GOV2 Git/physical-scope/future-reading decisions. Exact original bytes remain in the archive. Named historical lesson exclusions retain their original applicability and do not authorize work.

### Family A-L1966-L1989 — Inheritance, active lesson, frozen lifecycle

Source: AGENTS L1966–L1989; accepted classification A; Core lifecycle inline; full procedure in `rules/LIFECYCLE.md`

## 8. Lesson Lifecycle

Copy previous completed lesson
→ Rename
→ User removes build/ and .gradle/ only inside the expressly authorized copied editable lesson
→ Baseline Build
→ Create and maintain Transition Guide
→ Add ONE concept
→ Build
→ Simulation
→ Real Robot
→ Finalize Documentation
→ User Commit
→ User Push

Rules

- Never recreate from scratch.
- Never modify the source code of completed lessons except under the narrowly
  approved exceptional frozen-reopen contract preserved in LIFECYCLE.md.
- Documentation or metadata may be updated only with explicit user approval.
- Only the lesson with Status = IN_PROGRESS is editable.
- COMPLETE lessons are frozen snapshots.

### Family A-L1990-L2003 — SUSPENDED/REOPENED semantics

Source: AGENTS L1990–L2003; accepted classification A; Explicit distinction inline; approved exceptional workflow referenced

### Exceptional Suspension and Reopen Lifecycle

- `SUSPENDED / READ-ONLY` preserves unfinished work exactly as-is. It is not
  COMPLETE, not FROZEN, not editable, and does not count as the active editable
  lesson. No production, test, documentation, configuration, dependency,
  asset, or feature change is permitted while suspended. Resume requires
  explicit governance approval and uses the exact preserved state unless a
  separately approved reconciliation is required.
- `SUSPENDED / READ-ONLY` is reserved for exceptional higher-priority safety or
  robustness work; it is not a normal lesson workflow state.
- `REOPENED` is a provenance qualifier for an `IN_PROGRESS` lesson, not an
  additional generic lifecycle status.
- The exceptional frozen-reopen requirements are preserved in this LIFECYCLE rule and the applicable approved reopen ADR.

### Family A-L2438-L2445 — General exceptional-reopen rule

Source: AGENTS L2438–L2445; accepted classification A; Extract as standing rule; must not disappear into history

A frozen lesson may use this exception only for new post-freeze evidence of a
material safety, correctness, architecture, hardware-runtime, or verification
defect that invalidates a frozen assumption. It requires explicit Architect and
User approval, written evidence, exact scope, preserved historical evidence,
one editable lesson, focused and inherited regression gates, applicable
Simulation and real-robot verification, explicit re-freeze, and no unrelated
feature or refactor.
