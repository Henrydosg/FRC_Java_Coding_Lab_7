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
    └── module_V00/ (authorized; V00_L01-L03 published/frozen; V00_L04 complete/frozen; no active lesson)
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
`module_V00` as the successor boundary after final closure of A01_L09. A01 closes at
A01_L09 after the required closure approval; A01_L10 is prohibited. V00_L01 shall inherit the
then-frozen A01_L09 through the standard copy,
rename, generated-artifact cleanup, baseline-build, and transition-guide workflow. This governance
registration does not select a camera vendor. The historical pre-reconstruction V00_L01 later
completed and froze, and V00_L02 was activated and implemented from that lineage before being
suspended read-only by the exceptional A01_L08 safety decision. After final A01_L09 was
reconstructed and published at `6b243bb`, the historical V00_L01 lineage was classified stale.
The current canonical V00_L01 was reconstructed from that final A01_L09, passed
its final architecture and closure reviews, and is
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at `7d52ebf`. The stale historical
V00_L02 was preserved outside the active lesson lineage. The current canonical
V00_L02 was reconstructed from published V00_L01, passed its inheritance,
baseline-build, architecture, design-lock, controlled-activation,
implementation, User-verification, documentation-completion, final architecture,
and final closure gates. It is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at
`53e9b9f`. User-owned Git publication was subsequently confirmed by the User.
The current canonical V00_L03 was then prepared by the User from published
V00_L02, passed its User-owned Java 17 baseline build, inheritance audit,
architecture audit, approved Design Lock, exact implementation boundary,
focused tests, inherited regressions, full suite, clean build, and
documentation-completion audit. It is now `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED` at `cc20d62`. Its final state is `IMPLEMENTATION COMPLETE /
USER-VERIFIED / DOCUMENTATION COMPLETE / FINAL ARCHITECTURE AUDIT PASS /
PREDECESSOR PROVENANCE PASS / FINAL CLOSURE REVIEW PASS`. The User then
prepared V00_L04 from that authoritative snapshot through copy/rename,
generated-artifact cleanup, and a User-verified WPILib Java 17 inherited
baseline build. Its inheritance, roadmap-scope, Frozen Backbone, Frozen
Interface Contract, and Document C audits passed, and the Architect approved
the refined deterministic-vision-simulation Design Lock. At the historical
activation stage, V00_L04 became the sole `IN_PROGRESS / EDITABLE` lesson.
Separate implementation authorization was later granted for exactly
`VisionIOSim.java` and `VisionIOSimTest.java`.
That implementation is complete and User-verified. `compileTestJava`, the
focused test, inherited vision regressions, the full suite, and the clean build
are `PASS`; the earlier Codex-local classpath result is `RESOLVED /
SUPERSEDED / NON-REPRODUCIBLE`. The post-implementation architecture review,
artifact cleanup, documentation reconciliation, final read-only review, and
closure authorization are `PASS`. V00_L04 is now
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 5461555 / USER VERIFIED`; no V00
lesson is active. User-owned Git publication is confirmed at `5461555`.

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

- A01_L01-L07 and A01_L09 are `COMPLETE / FROZEN / READ-ONLY`; A01_L09 final
  architecture and closure reviews are PASS, and User-owned Git publication is
  complete at `6b243bb`;
- the historical pre-reconstruction V00_L01 had reached
  `COMPLETE / FROZEN / READ-ONLY`, but that lineage is stale and non-authoritative;
- the current reconstructed V00_L01 passed final architecture and closure
  review and is `COMPLETE / FROZEN / READ-ONLY`;
- A01_L08 completed its authorized reopen and is now
  `COMPLETE / FROZEN / READ-ONLY`;
- V00_L02 has Status SUSPENDED and Active State
  `SUSPENDED / READ-ONLY`; its unfinished engineering is preserved;
- this closed A01_L08 exception does not itself make a lesson editable; current
  reconstructed V00_L01 is frozen after its separately authorized closure, and
  V00_L02 remains suspended until separately resumed;
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

That paragraph records the prerequisite state established by the A01_L08
exception. The later controlled reconstruction and activation decision dated
2026-08-27, recorded below, completed the required downstream reconciliation
and supersedes the suspension for the current canonical V00_L02 only.

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

### V00_L02 Controlled Reconstruction and Activation — 2026-08-27

The preceding V00_L02 suspension statements are retained as historical records
of the A01_L08 exception and the stale downstream lineage. After A01_L08 was
re-frozen, final A01_L09 was published at `6b243bb`, and reconstructed V00_L01
was published at `7d52ebf`, the stale historical V00_L02 was backed up outside
the active lesson lineage. The current canonical V00_L02 was reconstructed from
published V00_L01 through the required copy, rename, generated-artifact cleanup,
Java 17 baseline-build, and transition-document workflow.

Architect and User approval was `APPROVED` for controlled activation and
documentation/lifecycle reconciliation only. Repository inheritance review,
the reconstructed baseline, the full inherited test suite, the architecture
audit, and the pre-implementation design lock were PASS. At that historical
activation stage, V00_L02 became the sole `IN_PROGRESS / EDITABLE` lesson with
active state `RECONSTRUCTED BASELINE VERIFIED / DESIGN LOCK REVIEWED /
IMPLEMENTATION NOT YET AUTHORIZED`. V00_L01 remained `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED` at `7d52ebf`.

The approved future lesson boundary is package `frc.robot.vision`, future class
`AprilTagFieldLayoutContract`, future API
`loadOfficial2026(Constants.FieldTransformConstants.FieldVariant)` and
`getTagPose(int)`, and the existing field variants `REBUILT_WELDED` and
`REBUILT_ANDYMARK`. Returned poses use canonical WPILib Blue-origin
`fieldToTag` semantics. This lesson shall not alliance-flip or invert those
poses and shall not expose the mutable raw layout or mutable tag objects. A
future lookup of an unknown positive ID returns `Optional.empty()`; a
nonpositive ID throws `IllegalArgumentException`. The future production
dependency boundary is WPILib AprilTag/geometry plus the JDK and the existing
`FieldVariant` ownership only.

The package-private `fromLayout(AprilTagFieldLayout)` seam is explicitly not
approved. Adding that seam or any equivalent injection surface requires a
separate Architect decision. VisionIO, runtime camera access, Observation
production, NetworkTables, RobotContainer, Swerve, autonomous, PathPlanner,
pose fusion, Java implementation, and test implementation remain outside this
activation. Simulation, Driver Station / Glass, real-robot, and physical-camera
verification are `NOT APPLICABLE` to the current pure reference-data scope.
Future implementation requires separate explicit authorization.

### V00_L02 Implementation Verification and Documentation Completion — 2026-08-27

The preceding controlled-activation section records the historical state before
implementation authorization. A later explicit Architect/User action authorized
exactly one production file,
`frc/robot/vision/AprilTagFieldLayoutContract.java`, and exactly one focused test,
`frc/robot/vision/AprilTagFieldLayoutContractTest.java`. No other production or
test file changed. The implementation maps `REBUILT_WELDED` to
`AprilTagFields.k2026RebuiltWelded` and `REBUILT_ANDYMARK` to
`AprilTagFields.k2026RebuiltAndymark`, snapshots validated canonical Blue-origin
`fieldToTag` poses into immutable owned state, and exposes only the approved load
and lookup API. It does not use `kDefaultField`, alliance flipping, pose
inversion, `fromLayout(...)`, raw mutable layout/tag exposure, or runtime wiring.

Authoritative User verification under WPILib Java 17 records
`AprilTagFieldLayoutContractTest` PASS, inherited `VisionFrameTransformTest`
PASS, full test suite PASS, and clean full build PASS with `BUILD SUCCESSFUL in
24s` and `7 actionable tasks: 7 executed`. The earlier Codex-side incremental
classpath failure is an environment/process discrepancy and is not an accepted
implementation defect. Simulation, Driver Station / Glass, real robot, and
physical camera remain `NOT APPLICABLE` because V00_L02 adds immutable reference
geometry only.

Documentation completion and the pre-closure architecture audit were PASS. At
that pre-closure stage, V00_L02 remained the sole `IN_PROGRESS / EDITABLE`
lesson while final read-only architecture review, closure authorization, freeze
metadata, and User-owned Git publication remained pending. That historical
entry did not mark the lesson `COMPLETE` or `FROZEN` and did not start V00_L03.

### V00_L02 Final Closure and Freeze — 2026-08-27

The final read-only architecture and closure audit returned `PASS`, and the
Architect explicitly authorized final closure. V00_L02 is therefore
`COMPLETE / FROZEN / READ-ONLY`. Implementation verification, documentation,
the transition guide, the Frozen Backbone, Frozen Interface Contract, and
Document C boundaries are PASS. Simulation, Driver Station / Glass, real robot,
and physical camera remain `NOT APPLICABLE` because the lesson adds immutable
deterministic field-reference geometry only.

V00_L01 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at `7d52ebf`.
V00_L02 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at `53e9b9f`.
V00_L03 was subsequently prepared from published V00_L02 and activated through
the separately approved controlled activation recorded below. The activation
state and its later implementation completion are preserved in chronological
order below; at that historical point V00_L03 was the sole `IN_PROGRESS /
EDITABLE` lesson pending closure review.

### V00_L02 User Git Publication Reconciliation — 2026-08-27

The User subsequently verified publication of the frozen V00_L02 snapshot at
`53e9b9f`. The User also verified that `HEAD` equals `origin/main` and that
the working tree is clean. This reconciliation updates current lifecycle
metadata only; the User remains the sole Git add/commit/push operator.

### V00_L03 Controlled Activation — 2026-08-27 (historical activation record)

After V00_L02 was published at `53e9b9f`, the User prepared
`V00_L03_VisionIOAndImmutableObservationContract` by copying that authoritative
predecessor, renaming the copy, handling generated artifacts, and running the
inherited baseline build with WPILib Java 17. A no-Git inheritance audit found
219 comparable non-generated files in each lesson and zero differences. Final
V00_L01 frame semantics, final V00_L02 field-layout semantics, inherited A01
safety/event architecture, Gradle, vendordeps, configuration, source resources,
and deploy/PathPlanner assets remain preserved.

Architect and User approval is `APPROVED` for controlled lifecycle activation
and documentation reconciliation only. V00_L03 is the sole `IN_PROGRESS /
EDITABLE` lesson. Preparation is complete, the User-supplied baseline build is
PASS, the read-only architecture audit is PASS, and the Design Lock is
APPROVED. Java implementation, focused tests, runtime wiring, Simulation,
Driver Station / Glass, physical-camera work, and Git publication have not been
authorized or completed.

The approved future L03 responsibility is a vendor-neutral one-cycle
`VisionIO` transport plus immutable `VisionObservation` contract. Package
`frc.robot.io.vision` will own `VisionIO`, `VisionIOInputs`, and
`VisionTargetInputs`; package `frc.robot.observation.vision` will own immutable
`VisionObservation`, its state, and immutable target values. The only approved
future IO method is `void updateInputs(VisionIOInputs inputs)`. The locked
transport fields are `available`, `connected`, `sampleValid`, and a multiple-
target collection containing positive `tagId` identity and WPILib
`Transform3d cameraToTarget`, meaning target relative to camera. The immutable
states are `UNAVAILABLE`, `DISCONNECTED`, `INVALID_SAMPLE`, `NO_TARGETS`, and
`TARGETS_PRESENT`. Runtime producer ownership, field-layout use, telemetry,
runtime wiring, simulation, vendor integration, pose estimation, quality,
timing, and fusion remain deferred to their separately governed roadmap gates.

No Limelight, PhotonVision, vendor result object, NetworkTables acquisition,
camera implementation, best-target policy, ambiguity/quality, timestamp,
latency, field-to-robot estimate, Swerve fusion, alliance transform,
autonomous, PathPlanner, Robot, RobotContainer, command, subsystem, or
scheduler change is authorized by this activation. Implementation requires a
separate explicit Architect/User authorization.

### V00_L03 Implementation Verification and Documentation Completion — 2026-08-27 (historical pre-closure record)

The preceding V00_L03 section records the historical activation state before
implementation authorization. A separate Architect/User action authorized the
exact two-file production and two-file focused-test boundary defined by the
V00_L03 Design Lock. The implementation added only
`frc.robot.io.vision.VisionIO`, including its mutable one-cycle
`VisionIOInputs` and `VisionTargetInputs` transport, and
`frc.robot.observation.vision.VisionObservation`, including immutable state and
target values. No runtime producer, vendor adapter, NetworkTables acquisition,
telemetry, simulation implementation, camera, pose estimation, quality,
timestamp, latency, fusion, Swerve, autonomous, or RobotContainer change was
added.

The earlier failing test expectation for an effectively zero quaternion norm
was a false oracle at the locked `Transform3d` boundary. WPILib
`Rotation3d` canonicalization had already converted that raw construction to a
valid identity rotation before the Observation contract could observe it. The
authorized repair changed the test oracle to verify a valid identity
`Rotation3d`; it did not add raw quaternion fields, a new API, or a production
contract expansion. No production repair was required.

Authoritative User verification is PASS for `VisionObservationTest`,
`VisionIOTest`, inherited `VisionFrameTransformTest`, inherited
`AprilTagFieldLayoutContractTest`, the full test suite, and the clean full
build. The final documentation reconciliation and read-only architecture audit
are PASS. Simulation, Driver Station / Glass, physical camera, and real-robot
verification remain `NOT APPLICABLE` to this contract-only lesson and are
deferred to their governed V00 lessons.

At that historical pre-closure stage, V00_L03 was the sole `IN_PROGRESS /
EDITABLE` lesson with implementation and documentation complete, pending
ChatGPT's final closure review and freeze decision. User-owned Git publication
remained pending; Codex performed no Git operations.

### V00_L03 Final Closure and Freeze — 2026-08-27

The Architect's final closure review returned `PASS`. The authoritative User
verification record remains PASS for the Java 17 baseline, focused
`VisionObservationTest` and `VisionIOTest`, inherited
`VisionFrameTransformTest` and `AprilTagFieldLayoutContractTest`, the full
512/512 test suite, and the clean full build. Inheritance, Frozen Backbone,
Frozen Interface Contract, Document A/B/C compliance, the exact implementation
boundary, documentation completion, and predecessor provenance are PASS.

V00_L03 is now `COMPLETE / FROZEN / READ-ONLY`. The lesson content/state is
complete and frozen. At this closure point, Git publication was still
`PENDING / USER OWNED`; the later User publication is recorded in the
reconciliation section below. No active lesson remained. Simulation, Driver
Station / Glass, physical camera, and real-robot vision are `NOT APPLICABLE` or
deferred by the contract-only scope; they are not claimed as runtime PASS
results.

V00_L01 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at `7d52ebf` and
V00_L02 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at `53e9b9f`.
At this closure point V00_L04 had not yet been prepared or activated, and
A01_L10 remained prohibited.

### V00_L03 User Publication and V00_L04 Preparation Reconciliation — 2026-08-27

The User subsequently confirmed V00_L03 publication at `cc20d62`
(`cc20d62c5ce1c2d0411375eaccd9b98b0c53cf33`) with `HEAD == origin/main`.
V00_L03 is therefore `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`.

After confirming a clean working tree, the User copied authoritative V00_L03
to `V00_L04_DeterministicVisionSimulation`, renamed the copy, removed generated
build artifacts, selected WPILib Java 17, and supplied inherited baseline-build
PASS evidence. The V00_L04 directory therefore exists as a prepared inherited
copy, but the lesson is not activated, not `IN_PROGRESS`, and not editable.
Its architecture audit and Design Lock remain pending, and implementation is
not authorized. The repository has zero active lessons. V00_L01 and V00_L02
remain published and frozen, V00_L03 remains published and frozen, and
A01_L10 remains prohibited.

### V00_L04 Controlled Activation — 2026-08-28

The preceding preparation record is retained as historical evidence. A later
read-only audit confirmed V00_L04 faithfully inherits published V00_L03 at
`cc20d62`; the inheritance, roadmap-scope, Frozen Backbone, Frozen Interface
Contract, and Document C gates are `PASS`. The Architect approved the refined
V00_L04 Design Lock.

V00_L04 is therefore the repository's sole `IN_PROGRESS / EDITABLE` lesson.
Its one concept is a deterministic, vendor-neutral `VisionIOSim`
implementation of the frozen `VisionIO` contract. The locked design uses an
immutable caller-selected frame, official AprilTag field geometry, a fixed
`robotToCamera`, explicit simulation ground truth only when targets are
present, direct WPILib forward geometry, and a complete `VisionIOInputs`
overwrite on every update. Duplicate visible tag IDs are rejected; progression
is explicit; initial state is `UNAVAILABLE`.

This activation authorizes lifecycle/documentation metadata only. Production
and test implementation are `NOT STARTED`; implementation authorization is the
next gate. No clock, FPGA time, randomness, threads, NetworkTables, Driver
Station, alliance, scheduler, Observation producer, RobotContainer wiring,
Swerve change, telemetry, vendor integration, physical camera, pose
estimation, quality/ambiguity, timestamp/latency, or fusion is authorized.
V00_L01-L03 remain published and frozen, V00_L05-L09 remain deferred, and
A01_L10 remains prohibited.

### V00_L04 Implementation Verification and Documentation Reconciliation — 2026-08-28

The preceding activation section is preserved as the historical state before
separate implementation authorization. A later Architect/User action
authorized exactly one production file,
`src/main/java/frc/robot/io/vision/VisionIOSim.java`, and exactly one focused
test, `src/test/java/frc/robot/io/vision/VisionIOSimTest.java`. No other
production or test implementation was authorized.

The implementation provides deterministic, vendor-neutral forward measurement
synthesis. For target-present frames it combines known `fieldToRobot` ground
truth, the fixed `robotToCamera`, and official `fieldToTag` geometry to produce
`fieldToCamera` and then camera-relative `cameraToTarget` values. Progression
occurs only through `setFrame(...)`; complete-cycle overwrite prevents stale
targets; validation is fail-atomic; and no clock, randomness, vendor API,
NetworkTables, Driver Station, scheduler, runtime wiring, Observation producer,
or telemetry dependency was added. V00_L05 pose-candidate estimation and all
later V00 responsibilities remain deferred.

Authoritative User verification under WPILib Java 17 records
`compileTestJava` PASS, `VisionIOSimTest` PASS, required inherited vision
regressions PASS, full test suite PASS, and clean build PASS, each with exit
code 0 where supplied. The earlier Codex-local test-classpath failure is
`RESOLVED / SUPERSEDED / NON-REPRODUCIBLE` and is not a current blocker. The
post-implementation read-only architecture review returned `PASS`, including
the corrected independent `-2.5 m` geometry oracle, Frozen Backbone, Frozen
Interface Contract, Document C, and V00_L01-L03 protection.

The User also deleted the audited temporary compile-forensics log and
accidental untracked V00_L03 path copy. Documentation reconciliation and the
required `V00_L03_to_V00_L04_Step_by_Step.md` transition guide are complete.
V00_L04 remains the sole `IN_PROGRESS / EDITABLE` lesson with state
`IMPLEMENTED / VERIFIED / DOCUMENTATION RECONCILED / CLOSURE PENDING`.
Final closure review, freeze metadata, and User-owned Git publication remain
pending. V00_L05 has not been created.

### V00_L04 Controlled Closure and Freeze — 2026-08-28

The preceding activation and implementation sections are preserved as
historical lifecycle records. After the final read-only architecture and
documentation review returned `READY FOR ARCHITECT CLOSURE AUTHORIZATION /
PASS`, the Architect authorized controlled closure.

V00_L04 is now `COMPLETE / FROZEN / READ-ONLY`. Architecture, Design Lock,
implementation, `compileTestJava`, `VisionIOSimTest`, inherited vision
regressions, full test suite, clean build, post-implementation architecture
review, artifact cleanup, documentation reconciliation, transition guide,
Frozen Backbone, Frozen Interface Contract, Document C, V00_L01 protection,
V00_L02 protection, V00_L03 protection, and V00_L05-L09 scope isolation are
`PASS`.

The earlier Gradle/classpath failure remains only historical as `RESOLVED /
SUPERSEDED / NON-REPRODUCIBLE`. No active V00 lesson remains, V00_L05 has not
been created, and A01_L10 remains prohibited. Git publication is
`PENDING USER GIT`; no commit hash is claimed.

### V00_L04 Publication Metadata Reconciliation — 2026-08-28

The preceding activation, implementation, and closure sections are preserved
as chronological records. The User subsequently confirmed the completed
publication of the frozen V00_L04 snapshot:

`5461555 Complete V00_L04 deterministic vision simulation`

V00_L04 is now recorded as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @
5461555 / USER VERIFIED`. The User also confirmed `HEAD == origin/main`, a
clean working tree, and a successful push to `origin/main`. No production,
test, configuration, dependency, deploy, predecessor, or V00_L05 content was
changed by this metadata reconciliation.

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
| 1.10 | 2026-08-26 | FROZEN | APPROVED: reconcile stale historical V00_L01 lifecycle metadata; current reconstructed V00_L01 is the sole `REOPENED / IN_PROGRESS / EDITABLE` lesson pending final closure, and V00_L02 remains `SUSPENDED / READ-ONLY / UNMODIFIED`. |
| 1.11 | 2026-08-26 | FROZEN | APPROVED: record final V00_L01 architecture and closure review PASS and freeze the reconstructed lesson as `COMPLETE / FROZEN / READ-ONLY`; V00_L02 remains `SUSPENDED / READ-ONLY / UNMODIFIED`. |
| 1.12 | 2026-08-27 | FROZEN | APPROVED: complete the downstream reconstruction reconciliation and activate current canonical V00_L02 as the sole `IN_PROGRESS / EDITABLE` lesson with verified reconstructed baseline and reviewed design lock; implementation remains unauthorized, and V00_L01 remains published and frozen at `7d52ebf`. |
| 1.13 | 2026-08-27 | FROZEN | APPROVED: record the exact two-file V00_L02 implementation, authoritative User verification PASS, documentation completion, and pre-closure architecture preservation; V00_L02 remains `IN_PROGRESS / EDITABLE` pending final read-only architecture review and closure authorization. |
| 1.14 | 2026-08-27 | FROZEN | APPROVED: record final V00_L02 architecture and closure review PASS and freeze the lesson as `COMPLETE / FROZEN / READ-ONLY`; User-owned Git publication remains pending and V00_L03 is not activated. |
| 1.15 | 2026-08-27 | FROZEN | APPROVED: reconcile current V00_L01 and V00_L02 publication metadata to User-verified published commits `7d52ebf` and `53e9b9f`; no lesson is active, V00_L03 remains uncreated, and A01_L10 remains prohibited. |
| 1.16 | 2026-08-27 | FROZEN | APPROVED: activate prepared V00_L03 as the sole `IN_PROGRESS / EDITABLE` lesson after clean inheritance, User-verified Java 17 baseline build, architecture audit PASS, and approved Design Lock; implementation remains unauthorized, while V00_L01 and V00_L02 remain published and frozen. |
| 1.17 | 2026-08-27 | FROZEN | APPROVED: record V00_L03 implementation verification, test-oracle clarification, documentation completion, and final read-only architecture audit PASS; V00_L03 remains `IN_PROGRESS / EDITABLE` pending ChatGPT closure review and freeze, with Git publication User-owned and pending. |
| 1.18 | 2026-08-27 | FROZEN | APPROVED: record V00_L03 final closure review PASS and freeze the lesson as `COMPLETE / FROZEN / READ-ONLY`; no lesson is active, User-owned Git publication remains pending, V00_L04 is not started, and A01_L10 remains prohibited. |
| 1.19 | 2026-08-27 | FROZEN | APPROVED: reconcile User-confirmed V00_L03 publication at `cc20d62` and record V00_L04 as a prepared inherited copy with User-verified Java 17 baseline build PASS; V00_L04 is not activated, not editable, and implementation remains unauthorized; no lesson is active. |
| 1.20 | 2026-08-28 | FROZEN | APPROVED: activate V00_L04 as the sole `IN_PROGRESS / EDITABLE` lesson after inheritance and architecture PASS and Architect approval of the refined Design Lock; implementation is not started and remains pending separate authorization. |
| 1.21 | 2026-08-28 | FROZEN | APPROVED: record the exact two-file V00_L04 implementation, authoritative User verification PASS, post-implementation architecture review PASS, artifact cleanup PASS, and documentation reconciliation; V00_L04 remains the sole `IN_PROGRESS / EDITABLE` lesson pending final closure review, freeze, and User-owned Git publication. |
| 1.22 | 2026-08-28 | FROZEN | APPROVED: close and freeze V00_L04 as `COMPLETE / FROZEN / READ-ONLY` after final architecture/documentation review PASS; no V00 lesson remains active, V00_L05 is not created, and publication remains `PENDING USER GIT`. |
| 1.23 | 2026-08-28 | FROZEN | APPROVED: reconcile User-confirmed V00_L04 publication at `5461555`; V00_L04 remains `COMPLETE / FROZEN / READ-ONLY`, no V00 lesson is active, and publication is recorded as `PUBLISHED @ 5461555 / USER VERIFIED`. |
