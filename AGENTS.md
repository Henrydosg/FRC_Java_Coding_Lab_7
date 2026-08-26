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
    ├── module_A01/
    ├── module_D00/
    ├── module_D01/
    ├── module_S00/
    └── module_V00/ (authorized; V00_L01 complete, V00_L02 suspended)
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
- Never modify the source code of completed lessons except under the narrowly
  approved exceptional frozen-reopen rule in Section 14.
- Documentation or metadata may be updated only with explicit user approval.
- Only the lesson with Status = IN_PROGRESS is editable.
- COMPLETE lessons are frozen snapshots.

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
- The exceptional frozen-reopen requirements are defined in Section 14.

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
- SUSPENDED

Only `IN_PROGRESS` is editable. `SUSPENDED` is always read-only and is neither
COMPLETE nor FROZEN. `REOPENED` may qualify an IN_PROGRESS lesson's active state
but is not a separate lesson status.

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
- Post-A00 A01 roadmap authorization:
  `docs/architecture_decisions/ADR_A01_Autonomous_Navigation_Path_Following_Roadmap.md`
- Post-A01 V00 roadmap authorization:
  `docs/architecture_decisions/ADR_V00_AprilTag_Vision_Observation_and_Pose_Fusion_Roadmap.md`
- A01_L08 exceptional autonomous safety/robustness reopen:
  `docs/architecture_decisions/ADR_A01_L08_Autonomous_Safety_Robustness_Reopen.md`

The S00_L19/S00_L20 decision does not change the Frozen Backbone, the authority order, or the
S00_L15-S00_L24 roadmap. The separately referenced A00 decision authorizes only the post-S00
module and `module_A00` location; it does not change the Frozen Backbone or authority order.

The approved A01 decision authorizes `A01 - Autonomous Navigation and Path Following` and
`module_A01` as the successor boundary after frozen A00_L04. A00 is closed at A00_L04; A00_L05
is prohibited. A01 inherits frozen A00_L04, and its order is governed by the approved A01 ADR.
Lessons shall not be reordered, renamed, merged, split, inserted, or skipped without the
architecture/governance approval required by that ADR. One lesson remains one new architectural
concept, and frozen predecessor protection remains mandatory.

The authorized A01 lesson order is:

1. `A01_L01 - Autonomous Starting-Pose and Field-Frame Contract`
2. `A01_L02 - Pose-Targeted Autonomous Motion`
3. `A01_L03 - Trajectory Generation and Sampling Fundamentals`
4. `A01_L04 - Field and Alliance Transform Contract`
5. `A01_L05 - Holonomic Trajectory Following`
6. `A01_L06 - PathPlanner Path and Runtime Integration`
7. `A01_L07 - AutoBuilder Contract Integration`
8. `A01_L08 - Autonomous Routine Selection and Safe Composition`
9. `A01_L09 - PathPlanner NamedCommands and Event Markers`

A00_L04's Autonomous+Enabled safety invariant and centralized
`SwerveSubsystem.stop()` authority remain authoritative. `RobotContainer` remains the composition
root only, and Simulation-before-real-robot verification remains mandatory. PathPlanner is
prohibited before A01_L06, AutoBuilder is prohibited before A01_L07, and the A01_L06 mandatory
compatibility entry gate remains authoritative. Vision/AprilTags are outside the A01 baseline,
and D01 retains mechanism architecture ownership.

The approved V00 decision authorizes `V00 - AprilTag Vision Observation and Pose Fusion` and
`module_V00` as the successor boundary after frozen A01_L09. A01 is closed at
A01_L09; A01_L10 is prohibited. V00_L01 shall inherit frozen A01_L09 through the standard copy,
rename, generated-artifact cleanup, baseline-build, and transition-guide workflow. This governance
registration does not select a camera vendor. V00_L01 has since completed and frozen; V00_L02 was
activated and implemented, then suspended read-only by the exceptional A01_L08 safety decision.

The authorized V00 lesson order is:

1. `V00_L01 - Vision Coordinate Frames and Camera Extrinsics`
2. `V00_L02 - AprilTag Field Layout Contract`
3. `V00_L03 - Vision IO and Immutable Observation Contract`
4. `V00_L04 - Deterministic Vision Simulation`
5. `V00_L05 - AprilTag Robot Pose Estimation`
6. `V00_L06 - Vision Measurement Quality Contract`
7. `V00_L07 - Vision Timestamp and Latency Contract`
8. `V00_L08 - Real Vision Adapter Integration`
9. `V00_L09 - Swerve Pose Estimator Vision Fusion`

V00 preserves the Frozen Backbone and Observation Architecture. Vision vendor APIs may exist only
inside the selected real VisionIO adapter; Vision models and evaluators remain immutable and
vendor-neutral; telemetry remains read-only; and `SwerveSubsystem` remains the sole owner of
`SwerveDrivePoseEstimator`. Vision supplies accepted timestamped measurements only, and the
approved fusion boundary uses `addVisionMeasurement(...)` rather than continuous pose reset.
Autonomous continues to consume `getEstimatedPose()` and shall not access camera, VisionIO, or
vendor APIs directly. A01_L04 remains the sole alliance-transform owner; vision measurements use
canonical WPILib field coordinates and are not alliance-flipped. Simulation shall not use
EstimatedPose as camera ground truth and shall pass before real-robot fusion verification.

No camera or vendor is selected in V00_L01 through V00_L07. V00_L08 may select exactly one real
vision implementation only after explicit review of actual camera hardware, WPILib 2026
compatibility, the exact vendor library/version, timestamp semantics, dependency resolution, and
simulation support where applicable. Lessons shall not be reordered, renamed, merged, split,
inserted, or skipped without the architecture/governance approval required by the V00 ADR.

### Exceptional A01_L08 Safety / Robustness Reopen

The approved A01_L08 reopen ADR temporarily supplements the ordinary A01/V00
lifecycle without changing either roadmap. New post-freeze and post-reopen
real-robot evidence identified material autonomous preparation/readiness and
terminal mode-ownership defects. Source review also identified manual
child-command lifecycle delegation that conflicts with the A01 scheduler-native
composition contract. With explicit Architect and User approval:

- A01_L01-L07 and A01_L09 remain COMPLETE / FROZEN / READ-ONLY;
- V00_L01 remains COMPLETE / FROZEN / READ-ONLY;
- A01_L08 completed its authorized reopen and is now
  `COMPLETE / FROZEN / READ-ONLY`;
- V00_L02 has Status SUSPENDED and Active State
  `SUSPENDED / READ-ONLY`; its unfinished engineering is preserved;
- no lesson is made editable by this closed exception; V00_L02 remains
  suspended until separately resumed;
- the future terminal repair may use a scheduler-native Swerve-owning hold,
  make SAFE_STOP retain safe ownership during active Autonomous, add a minimum
  defensive Teleop-enabled output gate, replace the affected manual lifecycle
  delegation with WPILib-native composition, and add exactly one `HOLDING`
  lifecycle state if required; and
- the original scope amendment authorized governance scope only; a later
  separately recorded implementation authorization permitted the exact repair
  boundary. The implementation has now removed the active adapter's manual
  child lifecycle delegation and added the approved scheduler-native Robot
  exception boundary. Later environment recovery and verification passed
  `compileTestJava`, the scheduler exception test, the full 449/449 suite, and
  the clean build. User-owned Simulation and real-robot re-verification passed,
  and the lesson was explicitly re-frozen on 2026-08-26.

The authorized target terminal lifecycle is
`CONSUMED -> RUNNING -> HOLDING -> COMPLETE`. While `HOLDING`, path motion is
complete, centralized Swerve stop has occurred, the Autonomous session remains
active, Swerve remains required, default Teleop drive cannot reacquire it, and
no autonomous motion restarts. Changes to SwerveSubsystem, CTRE or other IO,
CANcoder offsets, calibration, PID/feedforward, PathPlanner assets, Gradle,
vendordeps, RobotContainer without separate review, or downstream frozen or
suspended lessons are not authorized.

A frozen lesson may use this exception only for new post-freeze evidence of a
material safety, correctness, architecture, hardware-runtime, or verification
defect that invalidates a frozen assumption. It requires explicit Architect and
User approval, written evidence, exact scope, preserved historical evidence,
one editable lesson, focused and inherited regression gates, applicable
Simulation and real-robot verification, explicit re-freeze, and no unrelated
feature or refactor.

V00_L02 may resume only by explicit governance approval after A01_L08 is
repaired, fully re-verified, and explicitly re-frozen. Those A01_L08 gates are
now complete. V00_L02 nevertheless remains suspended until a separate
downstream reconciliation confirms V00 work remained unchanged and determines
whether the accepted L08 repair must be forward-ported through the inherited
lineage. Resume is never automatic.

### A01_L08 Scheduler Exception Boundary Governance Amendment — 2026-08-25

New source evidence confirms that the active `SafeAutoBuilderCommand` manually
delegates child `initialize()`, `execute()`, `isFinished()`, and `end()` callbacks.
This violates the A01 scheduler-native composition contract. A single adapter-only
replacement cannot preserve equivalent fail-closed exception safety because the
WPILib 2026.2.1 `CommandScheduler` and PathPlanner 2026.1.2 do not provide the
required project fault boundary around arbitrary child lifecycle exceptions, and
`finallyDo`/decorators do not catch those exceptions.

Architect and User approval is `APPROVED` for governance scope expansion only.
The approved future design is Option F: scheduler-native AutoBuilder composition,
existing narrow callback/output protections, a Robot-level scheduler
`RuntimeException` boundary, a coordinator/adapter fault bridge, centralized
Swerve stop, immutable `FAULTED` observation, and no automatic restart. This
amendment does not authorize implementation.

After separate implementation authorization, the exact production scope for this
scheduler exception-boundary repair is limited to:

- `AutoBuilderContractAdapter.java`;
- `AutonomousPreparationCoordinator.java`;
- `RobotContainer.java`; and
- `Robot.java`.

The directly authorized test scope is limited to
`RobotContainerPathPlannerIntegrationTest.java`, `AutonomousRoutineFactoryTest.java`,
`AutonomousPreparationCoordinatorTest.java`, and the new
`RobotSchedulerExceptionBoundaryTest.java`; unchanged related safety tests may be
rerun. No other production or test file is authorized by this amendment. The
SwerveSubsystem, IO, CTRE, tuning, calibration, Constants tuning, PathPlanner
assets, RobotConfig, Gradle, vendordeps, frozen lessons, and suspended V00_L02
remain excluded.

The required safety contract is fail-closed Swerve behavior, centralized stop,
latched first-fault preservation, immutable operator-visible `FAULTED`, no
automatic autonomous restart, and terminal `HOLDING` where applicable. Re-freeze
remains `HOLD` until the scheduler-native lifecycle, Robot-level exception
boundary, focused exception tests, inherited regression, clean build, Simulation,
real-robot verification, changed-file audit, documentation closure, and explicit
Architect/User re-freeze gates all pass.

### A01_L08 Final Scheduler-Native Implementation Authorization — 2026-08-25

The final Architect/User action separately authorized implementation of the
exact four-file production boundary and named test boundary described above.
The implementation removed `SafeAutoBuilderCommand` manual child lifecycle
delegation and added the approved scheduler-native composition and Robot-level
exception boundary. A01_L08 remains `REOPENED / IN_PROGRESS / EDITABLE` and
V00_L02 remains `SUSPENDED / READ-ONLY`.

The local production compile passed under WPILib Java 17. Test compilation is
currently held by the existing Windows Gradle/Javac classpath-resolution
failure; Simulation and real-robot verification remain User-owned gates and
were not rerun. Re-freeze remains `HOLD`.

### A01_L08 Final Re-Freeze Closure — 2026-08-26

The preceding implementation-authorization result is preserved as historical
evidence. Subsequent environment recovery established `compileJava` PASS,
`compileTestJava` PASS, `RobotSchedulerExceptionBoundaryTest` PASS, the full
449/449 test suite PASS, and clean build PASS. User-supplied Simulation and
real-robot re-verification also passed the Blue/Red path, terminal ownership,
SAFE_STOP, Teleop gate, recovery, and no-automatic-restart gates.

The observed one-time terminal steering event is classified as `KNOWN / BOUNDED
TERMINAL STEER TRANSIENT`, `ACCEPTED FOR CURRENT LESSON`, and `DEFERRED FOR
FUTURE DRIVETRAIN / PATH-FOLLOWING TUNING`. Its exact physical root cause is not
fully proven, and it does not justify a PID/feedforward, CANcoder, CTRE,
PathPlanner, Swerve, configuration, or asset change. A single approximately
5.9 ms desktop `SwerveSubsystem.periodic()` sample is not roboRIO performance
proof; no blocking CAN wait or production performance defect was found, and it
is not a closure blocker.

A01_L08 is therefore `COMPLETE / FROZEN / READ-ONLY` after its authorized
safety/robustness reopen. The Frozen Backbone and Frozen Interface Contract are
preserved. V00_L02 remains `SUSPENDED / READ-ONLY / UNMODIFIED`; this closure
does not resume it.

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
| 1.4 | 2026-08-16 | FROZEN | APPROVED: register the post-A00 A01 roadmap and `module_A01` successor boundary without creating lessons or changing frozen S00/A00 architecture. |
| 1.5 | 2026-08-23 | FROZEN | APPROVED: register the post-A01 V00 roadmap and future `module_V00` successor boundary without creating or activating V00_L01, selecting a camera vendor, or changing frozen S00/A00/A01 architecture. |
| 1.6 | 2026-08-24 | FROZEN | APPROVED: add the exceptional SUSPENDED / READ-ONLY lifecycle and narrowly reopen A01_L08 for safety/robustness governance review while preserving V00_L02 unfinished work read-only; implementation is not authorized. |
| 1.7 | 2026-08-24 | FROZEN | APPROVED: expand the A01_L08 reopen scope for scheduler-native autonomous terminal ownership, SAFE_STOP ownership, a defensive Teleop-mode output gate, and removal of manual child lifecycle delegation; implementation remains unauthorized. |
| 1.8 | 2026-08-25 | FROZEN | APPROVED: expand the A01_L08 governance boundary for the scheduler-native AutoBuilder exception design and Robot-level scheduler `RuntimeException` boundary across the exact four-file production scope and named focused tests; implementation remains unauthorized. |
| 1.9 | 2026-08-26 | FROZEN | APPROVED: record final A01_L08 verification and re-freeze it as `COMPLETE / FROZEN / READ-ONLY`; V00_L02 remains `SUSPENDED / READ-ONLY` pending separate reconciliation and resume approval. |
