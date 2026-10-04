# Governance 2.0 Delegated Rule — WORKFLOW

> ACTIVE DELEGATED GOVERNANCE 2.0 RULE
>
> Incorporated by [root AGENTS.md](../../../AGENTS.md) within its exact delegated scope. Root controls conflicts pending HOLD and Architect reconciliation.

Delegated scope: Roles, scoped steps, change control, self-review, transition-guide procedure and truthful reporting. This file creates no lesson, repair, publication or later-gate authorization.

Registered migration decision: [GOV2 ADR](../../architecture_decisions/ADR_GOV2_Governance_2_0_Agent_Instructions_State_and_History_Migration.md).

## Active operational contract

ChatGPT is Architect/Mentor/Reviewer/Design Authority; Sol/Codex implements or reviews only explicitly authorized scope; User owns PowerShell, project builds/tests/Simulation/Glass/Driver Station/hardware/SysId execution and ALL Git writes. Task-relevant static inspection and read-only Git are permitted where authorized.

Each step has one objective and independently verifiable result. Read governance, confirm target/scope/concept and inspect dependencies before work. Stop on missing authority, missing required documents, scope expansion, conflict or failed required gate; do not advance dependent claims. Do not use a build PASS to bypass architecture or documentation gates.

Self-review covers source/target lineage, architecture, all changed paths, scope, meaningful evidence and limits, required documentation and no unsupported claims. User supplies execution evidence; report accepted historical execution separately from new operations. Preserve failure chronology and test-fixture versus production-defect distinctions.

The transition guide records Step, Objective, Why, Action, Files Changed, Verification and Expected Result; maintain it during authorized IN_PROGRESS work and finalize only after required gates. Reserved start/finish/publish wording is not active automation permission.

Change-control review records reason, scope, impact and APPROVED/REJECTED decision before changes to Backbone, responsibilities, dependencies, IO, RobotContainer, Constants architecture or frozen lessons. Exceptional repair obeys the Lifecycle rule contract. No unauthorized cleanup, inferred successor activation or reuse of consumed authorization is permitted.

Final reports distinguish inspected/changed files, source and active lesson/NONE, architecture/documentation results, supplied baseline/build/runtime/hardware results, Git publication status, known limits and actual actions. Never invent PASS, counts, hashes, exit codes, commits or pushes.

## Traceable source families

These reviewed clauses retain full accepted A/H/C families. Normalization is limited to LF presentation, relocated section pointers, scoped User cache ownership and explicit registered GOV2 Git/physical-scope/future-reading decisions. Exact original bytes remain in the archive. Named historical lesson exclusions retain their original applicability and do not authorize work.

### Family A-L2004-L2014 — Fixed responsibilities

Source: AGENTS L2004–L2014; accepted classification A; Roles inline; Git-command wording requires adjudication

That accepted inventory note describes the earlier design stage. Registered GOV2 ADR §13 supplies the explicit read-only Git clarification; User ownership of every Git write remains unchanged.

### Fixed Role Ownership

- ChatGPT is the Architect, Mentor, and Reviewer.
- Codex is the repository implementation and audit engineer.
- The User runs and verifies builds, Simulation, Glass / AdvantageScope, Driver Station, and
  real-robot testing.
- The User is the only Git commit and push operator.
- Codex may perform task-relevant read-only Git inspection; ALL Git writes remain User-owned and prohibited to Codex; it shall not claim user-owned verification without supplied evidence.

---

### Family A-L2054-L2084 — Step-by-step work discipline

Source: AGENTS L2054–L2084; accepted classification A; One step/result and stop-on-failure inline; detail in `rules/WORKFLOW.md`

## 10. Development Workflow

Before coding

- Read required documents.
- Confirm active lesson.
- Review Backbone.
- Review architecture.
- Confirm lesson objective.

During coding

- Each implementation step shall have one objective and one independently verifiable result.
- Preserve architecture.
- The User runs builds frequently and supplies the result as verification evidence.

After coding

- The User runs the required build and verification workflow.
- Codex records only supplied or directly authorized evidence.
- Record issues.
- Update LESSON_STATUS.md.

Stop when

- required documents missing
- architecture conflict
- Document A/B conflict
- build fails
- verification fails

### Family A-L2097-L2113 — Agent self-review

Source: AGENTS L2097–L2113; accepted classification A; Full checklist in workflow rules; requirement retained inline

### Self Review

Before reporting success verify

- Documents read
- Document A reviewed
- Document B reviewed
- Backbone preserved
- Architecture preserved
- RobotContainer preserved
- Build reported
- Verification reported
- Documentation reported
- No unsupported claims

---

### Family A-L2114-L2142 — Required transition documentation

Source: AGENTS L2114–L2142; accepted classification A; Completion prerequisite inline; detailed guide contract in workflow rules

## 11. Documentation

Every completed lesson contains

real_robot_programming/<MODULE>/<LESSON>/docs/

Required guide

<PREVIOUS>_to_<CURRENT>_Step_by_Step.md

The transition guide is created and maintained during the lesson.
It is finalized only after implementation and all required verification are complete.
Transition Guide may be marked PASS only when the guide is final.
The final guide must exist before the lesson becomes COMPLETE / FROZEN.

Each step contains

- Step
- Objective
- Why
- Action
- Files Changed
- Verification
- Expected Result

One step = One change.

---

### Family A-L2143-L2167 — “Make guide” workflow

Source: AGENTS L2143–L2167; accepted classification A; Detailed conditional procedure; not an unconditional write authorization

## 12. Built-in Commands

### Make step by step docs

Codex shall

- Read AGENTS
- Read Document A
- Read Document B
- Read README
- Read LESSON_STATUS
- Compare previous and current lesson
- Generate guide
- Save guide
- Update LESSON_STATUS
- Report results

Guide creation and maintenance are allowed while the lesson is IN_PROGRESS.

Stop guide finalization and do not mark Transition Guide PASS if

- implementation incomplete
- build failed
- verification missing

### Family A-L2168-L2179 — Reserved start/finish/publish workflow

Source: AGENTS L2168–L2179; accepted classification A; Preserve command semantics and ownership; future automation is not active permission

### Reserved Commands

Start next lesson

Finish lesson

Publish lesson

Reserved for future repository automation.

---

### Family A-L2195-L2215 — Formal change control

Source: AGENTS L2195–L2215; accepted classification A; Reason, scope, impact, decision and approval requirement inline

## 14. Change Control

Formal review required before changing

- Frozen Backbone
- Package responsibilities
- Dependency direction
- IO contracts
- RobotContainer role
- Constants architecture
- Completed lessons

The permanent top-level package `frc.robot.observation` is part of the Frozen Backbone.

The review shall document:

- Reason
- Scope
- Impact
- Decision (APPROVED / REJECTED)

### Family A-L4378-L4402 — Final report requirements

Source: AGENTS L4378–L4402; accepted classification A; Detailed report contract in workflow rules; evidence honesty inline

## 15. Final Report

Always report

- Required Documents
- Architecture Check
- Source Lesson
- Active Lesson
- Files Inspected
- Files Changed
- Baseline Build
- Build Result
- Simulation Result
- Driver Station / Glass Result
- Real Robot Result
- Documentation Result
- Git Commit Result
- Git Push Result
- Known Issues
- Lesson Status

Only report verified facts.

---
