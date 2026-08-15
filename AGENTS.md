# AGENTS.md

# FRC Java Coding Lab 7.0 — Repository Rules

English is normative. Vietnamese is explanatory.

---

## 1. Required Reading

Before any analysis, architecture review, implementation, testing analysis, documentation, or
repository audit, Codex MUST read:

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

### Authority Order

1. AGENTS.md
2. Document A
3. Document B
4. Document C
5. README.md
6. Repository Source Code

If documents conflict, the higher priority document wins.

Stop immediately if repository code conflicts with Document A or Document B.

---

## 2. Repository Structure

Repository layout is fixed.

```
FRC_Java_Coding_Lab_7/
├── AGENTS.md
├── README.md
├── docs/
│   ├── Document_A/
│   ├── Document_B/
│   ├── Document_C/
│   └── architecture_decisions/
└── real_robot_programming/
    ├── module_A00/
    ├── module_D00/
    ├── module_D01/
    └── module_S00/
        └── <LESSON_NAME>/
            ├── docs/
            ├── src/
            ├── build.gradle
            ├── settings.gradle
            └── LESSON_STATUS.md
```

Rules

- One lesson = One independent WPILib project.
- Every lesson has its own docs.
- Every lesson has LESSON_STATUS.md.
- Do not create folders outside the approved structure.

---

## 3. Frozen Backbone

Always preserve

Driver
→ Xbox Controller
→ controls
→ commands
→ subsystems
→ io
→ hardware

Observation flow

hardware
→ IOInputs
→ subsystem / estimator
→ immutable Observation
→ telemetry
→ NT4 / Glass / log

Telemetry is read-only.

This mechanism observation flow remains unchanged. The narrowly approved external human/operator
input exception is defined in Section 14 and does not apply to mechanism Observations.

---

## 4. Package Responsibilities

controls
- Driver input processing only.
- For external human/operator input only, controls may acquire one coherent controller sample and
  produce an immutable, vendor-neutral DriverInputObservation.
- This exception does not permit controls to produce mechanism Observations.

commands
- Coordinate subsystem actions.

subsystems
- Own mechanism behavior and state.

io
- Hardware abstraction only.

observation
- Immutable, vendor-neutral read models and pure evaluators only.
- Subsystems or dedicated estimators produce mechanism Observations.
- The Section 14 external human/operator input exception is the only approved controls-produced
  Observation exception.
- No hardware access, vendor APIs, NetworkTables, CommandScheduler, RobotContainer, mutable mechanism state, or control behavior.

telemetry
- Consume and publish immutable Observations only.
- No behavior control or hardware access.
- Lesson-specific approved exceptions are recorded in the architecture decision records referenced
  by Section 14 and do not establish general package dependencies.

util
- Generic shared reusable helpers only.

---

## 5. RobotContainer

RobotContainer is the Composition Root.

Allowed

- object creation
- dependency injection
- implementation selection
- default commands
- button bindings

Forbidden

- hardware logic
- mechanism logic
- input processing
- telemetry calculations
- business logic

---

## 6. IO Contract

Every mechanism must provide

- IO interface
- Inputs snapshot
- Real implementation
- Simulation or Noop implementation when required by the current lesson
- Safe stop()

Flow

Hardware
→ IO
→ IOInputs
→ Subsystem
→ immutable Observation
→ Telemetry

Subsystems never access vendor hardware directly.
Telemetry never publishes directly from mutable IOInputs when an Observation contract exists.

---

## 7. Java Rules

- Complete Java files only.
- No partial code.
- No omitted lines.
- No deprecated APIs.
- No magic numbers.
- English comments only.
- Preserve WPILib header.
- Add

/**
 * Author: SSIS
 * Mentor: SSIS
 */

before package.

Keep Constants.java as the default configuration authority.

---

## 8. Lesson Lifecycle

Copy previous completed lesson
→ Rename
→ Delete build/ and .gradle/
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
- Never modify the source code of completed lessons.
- Documentation or metadata may be updated only with explicit user approval.
- Only the lesson with Status = IN_PROGRESS is editable.
- COMPLETE lessons are frozen snapshots.

### Fixed Role Ownership

- ChatGPT is the Architect, Mentor, and Reviewer.
- Codex is the repository implementation and audit engineer.
- The User runs and verifies builds, Simulation, Glass / AdvantageScope, Driver Station, and
  real-robot testing.
- The User is the only Git commit and push operator.
- Codex shall not run Git, commit, push, or claim user-owned verification without supplied evidence.

---

## 9. LESSON_STATUS.md

Required fields

- Lesson
- Previous Lesson
- Status
- Architecture Review
- Baseline Build
- Build
- Simulation
- Driver Station / Glass
- Real Robot
- Transition Guide
- Git Commit
- Git Push
- Known Issues

Lesson status

- IN_PROGRESS
- COMPLETE

Verification

- PASS
- FAIL
- NOT TESTED
- NOT APPLICABLE

Never report PASS without evidence.

---

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

### Repository Safety

Never

- rename repository folders
- move repository folders
- delete repository folders
- overwrite repository folders
- reorganize repository structure

unless explicitly approved by the user.

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

### Reserved Commands

Start next lesson

Finish lesson

Publish lesson

Reserved for future repository automation.

---

## 13. Git Rules

Git is User-owned. Codex shall not run Git commands.

Workflow

git status
→ git add
→ git commit
→ git push

Never claim GitHub was updated unless push succeeds.

---

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

### Approved External Operator-Input Observation Exception

For external human/operator input only, controls may produce an immutable, vendor-neutral
DriverInputObservation from one coherent controller sample.

This exception:

- does not change the mechanism observation flow;
- does not permit controls to produce mechanism Observations;
- does not permit Observation to contain hardware access, vendor APIs, NetworkTables,
  CommandScheduler, RobotContainer, mutable state, or control behavior; and
- does not permit telemetry to control robot behavior.

### Approved Architecture Decision Records

Lesson-specific decisions shall be recorded outside global governance and referenced here.

- S00_L19 / S00_L20 driver-input ownership and migration:
  `docs/architecture_decisions/ADR_S00_L19_L20_Driver_Input_Ownership.md`
- Post-S00 A00 roadmap authorization:
  `docs/architecture_decisions/ADR_A00_Autonomous_Command_Foundation_Roadmap.md`

The S00_L19/S00_L20 decision does not change the Frozen Backbone, the authority order, or the
S00_L15-S00_L24 roadmap. The separately referenced A00 decision authorizes only the post-S00
module and `module_A00` location; it does not change the Frozen Backbone or authority order.

---

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

## 16. Governance Revision History

| Version | Date | Status | Decision |
| --- | --- | --- | --- |
| 1.0 | 2026-07-18 | FROZEN | Initial repository governance. |
| 1.1 | 2026-08-01 | FROZEN | APPROVED: recognize `frc.robot.observation` as the permanent immutable read-model boundary; control flow remains unchanged. |
| 1.2 | 2026-08-08 | FROZEN | APPROVED: fixed role ownership, transition-guide lifecycle, module structure, durable external operator-input Observation exception, and referenced lesson-specific architecture decision records. |
| 1.3 | 2026-08-16 | FROZEN | APPROVED: authorize the post-S00 A00 roadmap and `module_A00` location without changing S00 or the Frozen Backbone. |
