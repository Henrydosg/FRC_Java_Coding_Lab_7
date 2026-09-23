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
    ├── module_V00/ (authorized; V00_L01-L09 complete/frozen/read-only)
    └── module_M00/ (authorized; M00_L01-L12 complete/frozen/read-only; M00_L11 published/verified; M00_L12 primary frozen snapshot committed and verified, metadata publication pending; active lesson count 0; no active M00 lesson; M00_L13 inactive/not created)
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
- Post-V00 M00 roadmap and preparation authorization:
  `docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md`
- A01_L08 exceptional autonomous safety/robustness reopen:
  `docs/architecture_decisions/ADR_A01_L08_Autonomous_Safety_Robustness_Reopen.md`
- V00_L07 inherited Swerve architecture/robustness integrity reopen:
  `docs/architecture_decisions/ADR_V00_L07_Inherited_Swerve_Architecture_Robustness_Integrity_Reopen.md`

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

V00_L05 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 6482160 / USER
VERIFIED`, and V00_L06 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @
1327bf4 / USER VERIFIED`. V00_L06 lesson publication is recorded as
`1327bf41736c8fe79ba58ec5eea9e0120bd978fb` with subject `Complete V00_L06
vision measurement quality contract`; its lesson-local publication metadata
reconciliation is recorded at `49c4286` as `Reconcile V00_L06 publication
metadata`.

The latest published vision snapshot remains the original
V00_L07_VisionTimestampAndLatencyContract publication:
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ d58bef0 / USER VERIFIED`.
Its lesson publication commit is
`d58bef0d17d202ce1dd0b8645635a8c35095dd3f` with subject `Complete V00_L07
vision timestamp and latency contract`; its lesson-local publication metadata
reconciliation is recorded at `618dd09` as `Reconcile V00_L07 publication
metadata`. At the time of the separately approved exceptional inherited-Swerve
reopen, V00_L07 became the sole current editable lesson with state
`REOPENED / IN_PROGRESS / EDITABLE`; implementation, fresh verification,
re-freeze, and repair publication were then pending, and the original d58bef0
publication remains historical pre-repair evidence.

The authorized V00_L07 R1/R2/R3 repair is now implemented and documented. The
fresh pre-repair baseline passed with 593/593 tests and a clean build; the
post-repair full suite passed with 600/600 tests and a clean build. Runtime
WPILib Simulation and the post-implementation read-only architecture/Frozen
Backbone review passed. At the earlier repair stage, real-robot verification
was `DEFERRED — ROBOT UNAVAILABLE`. Later User evidence verifies Teleop and
Autonomous usability. The BL quantitative drivetrain anomaly remains `KNOWN /
DEFERRED HARDWARE MAINTENANCE`; no quantitative drivetrain PASS, completed
tuning/calibration, or issue resolution is claimed. Under the explicit
Architect/User disposition it does not block the Vision curriculum closure
sequence. The final read-only closure review passed and the Architect/User
authorized re-freeze. V00_L07 is now `COMPLETE / FROZEN / READ-ONLY`; the
corrected repair publication is now `PUBLISHED @ 4704cfc`. The following L08
state is the historical pre-activation record: V00_L08 was then the next
roadmap lesson and remained `NOT STARTED / NOT ACTIVATED / NOT IMPLEMENTED /
NOT PUBLISHED / NON-AUTHORITATIVE / READ-ONLY`; its candidate had to remain
preserved until separately authorized reconciliation. The current L08 state is
recorded in the controlled repair activation section below. V00_L09 remains
not started.

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

### V00_L05 Controlled Activation and Design Lock — 2026-08-28

The User then prepared `V00_L05_AprilTagRobotPoseEstimation` from the final
published V00_L04 snapshot through the approved copy, rename, generated-artifact
cleanup, and inherited WPILib Java 17 baseline-build workflow. The read-only
inheritance and architecture audits recorded 229 comparable non-generated files,
zero differences, identical production and test Java, unchanged build/config,
vendordeps, deploy/resources/PathPlanner content, and preserved predecessor
protection. The earlier candidate naming HOLD was resolved by retaining the
ADR-locked V00_L05 identity; no ADR amendment was required.

The Architect approved the V00_L05 Design Lock. The lesson teaches one pure,
deterministic, vendor-neutral responsibility: derive a canonical Blue-origin
`fieldToRobot` robot-pose candidate from `fieldToTag`, `cameraToTarget`, and
`robotToCamera`. The locked estimator belongs in `frc.robot.vision` as the
stateless `AprilTagRobotPoseEstimator` utility with exactly one public method,
`estimateFieldToRobotCandidate(Pose3d, Transform3d, Transform3d)`. The approved
structural validation, output semantics, frozen dependency ownership, test
matrix, and L06 quality boundary are recorded in the active lesson documents.

This controlled activation changes lifecycle/documentation metadata only.
V00_L05 is now the repository's sole `IN_PROGRESS / EDITABLE` lesson with
active lesson count `1`. Production implementation and focused-test
implementation remain `NOT STARTED / NOT AUTHORIZED`. Focused tests, inherited
regressions, full-suite verification, clean build, Simulation, Driver Station /
Glass, and real-robot verification remain pending or not applicable according
to the pure-geometry Design Lock. V00_L01-L04 remain complete, frozen, and
protected; A01_L04 remains the sole alliance-transform owner; V00_L06 and later
lessons remain deferred; and A01_L10 remains prohibited.

The User-owned Git add, commit, and push operations remain separate future
gates. Codex performed no Git operation.

### V00_L05 Post-Implementation Verification and Documentation Reconciliation — 2026-08-28

The separately authorized V00_L05 implementation and focused test are complete
within the approved two-file boundary. The Java 17 compatibility adjustment
from `getFirst()` to `get(0)` was test-only. The original noncommutativity
fixture defect was also repaired in the focused test only: its compared inverse
transforms were pure translations and therefore commuted. The replacement
nondegenerate fixture proves the locked order by producing translations
`(3, 2, 1.5)` and `(0, 1, 1.5)` for locked and reversed composition. API
reflection hardening now protects the final class and exactly one public
declared method.

Authoritative User verification under WPILib Temurin Java 17.0.16 is PASS for
clean `compileTestJava`, focused L05 tests, inherited vision regressions, the
full test suite, and clean build (`BUILD SUCCESSFUL`). The post-implementation
architecture review, Frozen Backbone, Frozen Interface Contract, Documents
A/B/C, and frozen V00_L01-L04 predecessor protection are PASS. V00_L05 remains
the sole `IN_PROGRESS / EDITABLE` lesson; final closure authorization, freeze,
and User-owned Git publication remain pending. No Git publication hash is
claimed and Codex performed no Git operation.

### V00_L05 Final Closure and Freeze — 2026-08-28

The preceding activation and post-implementation sections are preserved as
historical lifecycle records. Architect closure authorization is now
`APPROVED`. V00_L05 is therefore `COMPLETE / FROZEN / READ-ONLY`.

Implementation, production architecture review, public API, locked transform
order, validation, conceptual test matrix, API reflection hardening,
noncommutativity repair, independent oracle, frozen predecessor protection,
build/configuration/dependency protection, Frozen Backbone, Frozen Interface
Contract, Documents A/B/C, User Java 17 verification, post-hardening
verification, and documentation reconciliation are `PASS`. Simulation, Driver
Station, Glass, and real-robot verification remain `NOT APPLICABLE` because
this lesson is pure deterministic geometry without runtime camera or hardware
integration.

V00_L05 is no longer an active lesson; no V00 lesson is active, and V00_L06
has not been activated or created by this closure. At this historical closure
point, Git publication remained `PENDING USER GIT PUBLICATION`; no commit hash,
push result, or published state was claimed. The User remains the sole Git
operator.

### V00_L05 Publication Metadata Reconciliation — 2026-08-28

The User subsequently confirmed the actual V00_L05 lesson publication:

`6482160 Complete V00_L05 AprilTag robot pose estimation`

The full publication commit is
`648216094fbea7eb5ebf26252f1ea457b93fcce8`, and the User confirmed that
`HEAD` and `origin/main` resolved to that commit at publication time. V00_L05
is therefore recorded as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 6482160`.

This entry reconciles publication metadata only. Any later commit that records
this metadata reconciliation is separate from the actual lesson publication
commit and remains User-owned, not yet created, and not yet published. No
production, test, configuration, dependency, deploy, asset, predecessor, or
V00_L06 content is changed by this reconciliation.

### V00_L06 Publication Metadata Reconciliation — 2026-08-30

The User subsequently confirmed the actual V00_L06 lesson publication and its
lesson-local publication metadata reconciliation:

`1327bf4 Complete V00_L06 vision measurement quality contract`

`49c4286 Reconcile V00_L06 publication metadata`

V00_L06 is therefore recorded as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @
1327bf4 / USER VERIFIED`. The current V00 lifecycle contains no active lesson.
The prepared V00_L07 candidate is an inherited, pre-activation candidate with
a User-verified baseline clean build PASS; it is not active, editable, Design
Locked, implemented, or published. This repository-level record changes
documentation metadata only and does not change V00_L01-L06 lesson content,
the V00_L07 candidate, the V00 roadmap, or any production/test/configuration
file.

---

### V00_L07 Repository Lifecycle Reconciliation — 2026-08-30

The User subsequently confirmed publication of the frozen V00_L07 snapshot and
its lesson-local publication metadata reconciliation:

`d58bef0 Complete V00_L07 vision timestamp and latency contract`

`618dd09 Reconcile V00_L07 publication metadata`

V00_L07 is therefore recorded as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @
d58bef0 / USER VERIFIED`. The current V00 lifecycle contains no active lesson.
V00_L08 remains the next roadmap lesson and is `NOT STARTED / NOT ACTIVATED /
NOT IMPLEMENTED / NOT PUBLISHED`. This repository-level reconciliation changes
documentation metadata only; it does not change lesson implementation,
production/test code, the V00 roadmap, or any frozen predecessor.

The documentation changes in this reconciliation remain subject to the User's
separate Git publication operation. No future repository-reconciliation commit
hash is recorded here.

### V00_L07 Exceptional Swerve Integrity Reopen — 2026-08-31 (Historical activation record)

The Architect and User approved a documentation-only exceptional reopen of
V00_L07 for exactly three inherited Swerve integrity repairs: R1 removal of a
command-layer dependency on the IO-owned
`SwerveModuleIO.StaticFrictionStopReason` type; R2 best-effort all-module stop
fanout when one module stop throws; and R3 coherent physical-forward measured
drive position and velocity when `physicalForwardSign = -1`. The authoritative
drive ratio remains `6.75:1`.

The decision is recorded in
`docs/architecture_decisions/ADR_V00_L07_Inherited_Swerve_Architecture_Robustness_Integrity_Reopen.md`.
At this activation point, V00_L07 was the sole current
`IN_PROGRESS / REOPENED / EDITABLE` lesson. The original publication at
`d58bef0` remains historical pre-repair evidence; at that time no Design Lock,
implementation authorization, implementation, fresh baseline, verification,
re-freeze, or repair publication had yet occurred. The exact repair boundary
and all exclusions were subject to the ADR and a later Design Lock; no
production or test file was changed by that documentation-only lifecycle
update.

At that historical stage, V00_L08 remained unactivated, non-authoritative,
read-only, and `NOT STARTED / NOT ACTIVATED / NOT IMPLEMENTED / NOT PUBLISHED`,
with fresh reconstruction from corrected V00_L07 as the then-authorized next
procedure. That prospective procedure is now superseded for the existing
candidate by the one-time preservation-based reconciliation authorized by the
V00_L07 reopen ADR. The independent V00_L08 Limelight physical-evidence HOLD
is unchanged. The V00 roadmap and lesson identities are unchanged, and
A01_L10 remains prohibited.

This latest reconciliation changes governance and lesson documentation only.
Git publication remains User-owned and pending.

### V00_L07 Post-Repair Documentation Reconciliation — 2026-08-31 (Historical robot-unavailable stage)

The separately authorized R1/R2/R3 implementation is complete within the
approved boundary. The fresh pre-repair baseline passed with 593/593 tests and
a clean build. The post-repair focused tests, inherited regressions, and full
600/600 test suite passed, and the clean build passed. Runtime WPILib
Simulation and the final read-only architecture/Frozen Backbone review passed.
Real-robot verification is `DEFERRED — ROBOT UNAVAILABLE`, so V00_L07 remains
the sole `REOPENED / IN_PROGRESS / EDITABLE` lesson; re-freeze and repair
publication remain pending. V00_L08 remains unactivated and read-only, must be
reconstructed only from corrected published V00_L07, and its independent
Limelight physical-evidence HOLD is unchanged. This reconciliation changes
documentation metadata only.

### V00_L07 Pre-Closure Hardware-Evidence Reconciliation — 2026-09-07

The preceding 2026-08-31 record preserves the historical stage at which the
robot was unavailable. Later User evidence verifies Teleop and Autonomous
usability. The BL quantitative drivetrain anomaly remains `KNOWN / DEFERRED
HARDWARE MAINTENANCE`; it is unresolved and is not BL PASS, quantitative
drivetrain PASS, matched-module evidence, completed tuning/calibration, or
issue resolution. The Architect/User disposition makes that separate
maintenance condition non-blocking for continued Vision curriculum closure.

The R1/R2/R3 implementation boundary, 593/593 reopened baseline, focused and
inherited regressions, 600/600 post-repair suite, clean build, runtime WPILib
Simulation, post-implementation architecture review, and Frozen Backbone
review remain PASS. Pre-closure documentation evidence is reconciled. V00_L07
remains the sole `REOPENED / IN_PROGRESS / EDITABLE` lesson and is
`CLOSURE-READY / PENDING FINAL READ-ONLY CLOSURE REVIEW`. Explicit
Architect/User re-freeze approval and User-owned corrected repair publication
remain pending. Historical `d58bef0` remains the pre-repair publication.
V00_L08 remains unactivated, non-authoritative, read-only, and untouched.

### V00_L07 Final Re-Freeze Closure — 2026-09-07

The final read-only closure review returned
`READY_FOR_EXPLICIT_L07_REFREEZE_AUTHORIZATION`, and the Architect/User
explicitly authorized the documentation-only re-freeze. V00_L07 is therefore
`COMPLETE / FROZEN / READ-ONLY`. The R1/R2/R3 repair, verification evidence,
Frozen Backbone, Frozen Interface Contract, and documentation are PASS.

Teleop and Autonomous usability remain User-verified. The BL quantitative
drivetrain anomaly remains `KNOWN / DEFERRED HARDWARE MAINTENANCE`; no BL PASS,
quantitative drivetrain PASS, matched-module result, completed tuning or
calibration, or issue resolution is claimed. Historical `d58bef0` remains the
pre-repair V00_L07 publication only. The corrected repair publication is
`PUBLISHED @ 4704cfc0801910e30c8abb7cffcc467e4f4df016` with subject
`Complete corrected V00_L07 Swerve integrity repair`; User commit and push are
verified, and HEAD equals origin/main at that commit. No V00 lesson is active;
V00_L08 remains unactivated, non-authoritative, read-only, stale pre-repair
inheritance and untouched. V00_L09 remains not started.

### V00_L07 Corrected Publication-Metadata Reconciliation — 2026-09-07

The User performed the authorized exact-allowlist publication. The corrected
post-repair V00_L07 publication is `PUBLISHED / USER VERIFIED` at
`4704cfc0801910e30c8abb7cffcc467e4f4df016`, with subject
`Complete corrected V00_L07 Swerve integrity repair`. HEAD and origin/main both
resolved to that commit at the time of that publication. The earlier
`d58bef0d17d202ce1dd0b8645635a8c35095dd3f`
publication remains preserved as historical pre-repair provenance.

V00_L07 remains `COMPLETE / FROZEN / READ-ONLY`. V00_L08 remains untouched and
unactivated; its one-time preservation-based reconciliation is a separate
governed action. V00_L09 remains not started. This reconciliation changes
documentation metadata only.

### V00_L08 One-Time Preservation-Based Reconciliation Amendment — 2026-09-07

The previous reconstruction-only procedure is HISTORICAL and is SUPERSEDED
PROSPECTIVELY only for the existing V00_L08 candidate. The candidate remains
preserved, NOT STARTED, NOT ACTIVATED, and READ-ONLY. Before any authorized
reconciliation change, the User must create and verify a byte-preserving
filesystem checkpoint outside the repository, including hidden files, `.Glass`,
build, `bin`, `.gradle`, test artifacts, deploy assets, configuration, and
documentation; the checkpoint is not staged or committed here.

The only permitted forward-port boundary is the exact seven-file R1/R2/R3
boundary from corrected V00_L07 publication
`4704cfc0801910e30c8abb7cffcc467e4f4df016`, as enumerated in the ADR. Focused
tests, inherited Swerve/vision/autonomous regressions, the full suite, clean
build, changed-file verification, and a post-reconciliation inheritance /
architecture review are mandatory. Only after those gates may normal L08
Real Vision Adapter Design Lock and controlled activation proceed.

This amendment changes no Documents A/B/C, Frozen Backbone, Frozen Interface
Contract, roadmap, lesson scope, or User Git ownership. It authorizes no
Limelight implementation, NetworkTables acquisition, vendor dependency, pose
normalization, estimator fusion, PathPlanner change, drivetrain tuning, BL
investigation, or V00_L09 work. The Limelight evidence distinction remains
`Limelight -> roboRIO NetworkTables server -> Glass` as VERIFIED USER HARDWARE
EVIDENCE, while `Limelight -> Java VisionIO -> immutable VisionObservation`
remains NOT YET IMPLEMENTED. Detailed evidence must later be captured in a
dedicated L08 experiment/evidence document; unresolved physical measurements
are not governance authority.

### V00_L08 Controlled Repair Activation and Reconciliation — 2026-09-09

This is a historical pre-closure lifecycle record.

The User explicitly authorized the bounded V00_L08 repair based on the final
Astra closure audit. At that historical stage, the preserved candidate was the
sole current `IN_PROGRESS / EDITABLE` lesson. The authorized repair boundary is limited to
the real Limelight adapter freshness/coherence policy, the observation-only
periodic runtime owner, read-only diagnostic telemetry, the public Limelight
constant boundary, the directly related focused tests, and L08 lesson/root
documentation reconciliation. V00_L09, pose-estimator fusion,
Swerve/drivetrain/IO/tuning/calibration, autonomous behavior, PathPlanner,
vendor dependency/configuration changes, and H1 promotion remain excluded.

This entry records the User-authorized activation and bounded repair scope only;
it does not promote an implementation-selected freshness recipe to frozen
governance, declare runtime readiness, or alter the frozen VisionIO contract.
The repair remains subject to the normal implementation, official build, test,
Simulation, physical-camera, documentation, and closure gates.

Both `VisionIOLimelight` and `VisionIOSim` are wired through the same periodic
observation-only runtime path. RobotContainer remains the composition root and
does not acquire samples or perform business logic. Limelight topic names remain
private to the real adapter, and no vendor type is exposed through public IO,
Observation, or telemetry contracts.

At this recorded stage, `compileJava` and the direct focused execution were
historical evidence only; official `compileTestJava`, inherited regressions,
the full suite, clean build, Simulation, Driver Station/Glass, and real-camera
validation were not closure evidence. H1 remains provisional. The lesson
remains `IN_PROGRESS / EDITABLE`; User Git ownership is unchanged and Codex did
not run Git.

### V00_L08 Bounded Repair Clarification — 2026-09-10

The 2026-09-09 entry is a lifecycle and scope authorization only. It does not
freeze a particular NetworkTables freshness algorithm, heartbeat threshold,
recovery recipe, or read-coherence implementation as global governance policy.
At that historical pre-closure stage, the repair had to remain fail-closed and
was judged by the explicit L08 verification gates. Until official test/build,
Simulation, Driver Station/Glass, and real-camera evidence were complete,
V00_L08 remained `IN_PROGRESS / EDITABLE`; no runtime-ready,
production-convention, or `COMPLETE / FROZEN` claim was authorized by that
record.

### V00_L08 Final Closure and Freeze — 2026-09-10

The final read-only architecture and closure review returned
`PASS_V00_L08_FINAL_CLOSURE_REVIEW`, and the Architect authorized the final
documentation-only freeze. V00_L08 is now
`COMPLETE / FROZEN / READ-ONLY`.

The authoritative evidence remains PASS for governance, the Frozen Backbone,
the Frozen Interface Contract, focused verification (`37/37`), the full
regression (`642/642`), clean build, WPILib Simulation, Driver Station/Glass,
and real-robot Limelight acquisition, target loss, and reacquisition. The
transition guide and documentation reconciliation are final and PASS. No
current technical blocker or unexpected architectural drift remains.

H1 remains a **PROVISIONAL COMMISSIONING LOCK** and is not promoted to official
or proven vendor semantics. NetworkTables behavior remains documented as
bounded freshness/coherence/read-stability checking, not atomic multi-topic
reads. V00_L08 contains no estimator fusion; V00_L09 remains future work.

No V00 lesson is active and V00_L09 remains not started. At the closure point
before later publication, lesson content/state was complete and frozen while
Git publication remained `PENDING USER COMMIT/PUSH`; Codex performed no Git
operation.

### V00_L08 User Git Publication Reconciliation — 2026-09-10

The User subsequently confirmed the authorized publication of the frozen
V00_L08 lesson. The authoritative publication commit is `f34b210` with
message `Complete V00_L08 real vision adapter integration`. The User also
confirmed push `PASS` and that `origin/main` contains `f34b210`.

V00_L08 remains `COMPLETE / FROZEN / READ-ONLY`; no production or test source
was changed by this metadata reconciliation, and V00_L09 remains not started.
Codex performed no Git operation.

### V00_L09 Controlled Activation — 2026-09-11

The preceding V00_L08 closure and publication records are preserved as
historical evidence. The User then prepared the ADR-locked
`V00_L09_SwervePoseEstimatorVisionFusion` candidate from frozen V00_L08,
removed copied generated artifacts, supplied inherited baseline clean-build
PASS, reconciled the roadmap identity, and supplied PASS evidence for the
pre-implementation architecture audit, real timing-source investigation,
runtime orchestration micro-audit, coordinator decision, and the Architect
gate `PASS_V00_L09_FINAL_DESIGN_LOCK`.

The V00_L08 implementation publication remains `f34b210` with subject
`Complete V00_L08 real vision adapter integration`. The User separately
identifies `6415b17` as the later V00_L08 publication-metadata reconciliation;
that publication-metadata history is retained as distinct from the
predecessor implementation publication. This activation does not modify any
V00_L08 file.

With explicit Architect authorization for this documentation-only action,
V00_L09 is now the repository's sole `IN_PROGRESS / EDITABLE` lesson with
active lesson count `1`. V00_L08 and V00_L01-L07 remain
`COMPLETE / FROZEN / READ-ONLY`. The activation reconciles lifecycle metadata
only; it does not claim that Java implementation has completed or authorize
any source change by itself.

The locked V00_L09 concept is qualified timestamped AprilTag measurement
admission through the post-scheduler `VisionFusionCoordinator` requirement
and guarded Swerve-owned admission into
`SwerveDrivePoseEstimator.addVisionMeasurement(...)`. Swerve remains the sole
owner of estimator state, Vision remains vendor-neutral above IO,
RobotContainer remains the composition root, and the lesson does not add a
MegaTag migration, dynamic quality covariance, Constants tuning, or a second
concept.

At this activation point, L09 implementation, focused tests, inherited
regression, post-implementation clean build, Simulation, Driver Station /
Glass, deployed Limelight JSON verification, real-robot verification, final
architecture review, final documentation closure, and COMPLETE/FROZEN
transition remained pending. The prior implementation authorization was
blocked by the lifecycle mismatch and required later reconciliation. The
subsequent implementation-and-evidence section below supersedes this pending
activation-point state. User Git add, commit, and push remained separate and
pending. Codex performed no Git operation.

This activation does not amend the V00 roadmap ADR, change lesson order,
reopen V00_L08, or modify any unrelated module.

### V00_L09 Implementation and Evidence Reconciliation — 2026-09-12

The preceding controlled-activation record remains historical evidence.
Implementation subsequently completed under the Architect-controlled workflow
and preserves the locked L09 concept: qualified timestamped AprilTag
measurement, post-scheduler `VisionFusionCoordinator`, guarded Swerve-owned
admission, and `SwerveDrivePoseEstimator.addVisionMeasurement(...)`. Swerve
remains the sole estimator owner and mutator; `getCurrentPose()` remains
odometry-only, `getEstimatedPose()` retains fused-estimator semantics,
RobotContainer remains the composition root, and telemetry remains read-only.

The User verified focused tests, inherited regression, `compileTestJava`, the
full suite, and a clean build. Direct automated evidence now covers stale,
future, and out-of-order rejection; reset-barrier cached-pre-reset rejection
and fresh post-reset acceptance; scheduler-failure fusion suppression;
coordinator failure through the existing Robot fail-closed boundary; and
telemetry execution through the `finally` path. The initial Robot-boundary
tests failed before those boundaries because their `Pose3d.kZero` fixture was
outside the production quality policy. Under
`PASS_V00_L09_NARROW_TEST_EVIDENCE_AUTHORIZED`, the fixture-only repair reused
the existing deterministic quality-valid `VisionIOSimHarness` Frame A/B path,
preserved the assertions, and changed no production behavior. The Architect
accepted the repair and automated evidence through
`PASS_V00_L09_FAILURE_BOUNDARY_TEST_FIXTURE_REPAIR_ACCEPTED` and
`PASS_V00_L09_AUTOMATED_EVIDENCE_CLOSURE_VERIFIED`.

User-owned Simulation evidence is PASS for runtime startup, estimator
initialization, Frame A/B accepted one-shot fusion, vision loss, and
reacquisition. Driver Station / Glass verification is also PASS. Deployed
Limelight L09 timing/result verification and real-robot estimator
vision-fusion verification remain `PENDING / REAL HARDWARE DEFERRED`; V00_L08
real-camera evidence does not satisfy those L09 gates. Final
architecture/Frozen Backbone review, final documentation and transition-guide
closure, the `COMPLETE / FROZEN / READ-ONLY` transition, and User-owned Git
publication remain pending. V00_L09 remains the sole `IN_PROGRESS / EDITABLE`
lesson with active lesson count `1`; V00_L08 remains
`COMPLETE / FROZEN / READ-ONLY` and unmodified.

### V00_L09 Documentation Reconciliation — 2026-09-15

The preceding implementation-and-evidence record remains historical evidence.
The accepted L09 evidence is now documentation-reconciled without changing the
lesson lifecycle: L09 remains the sole `IN_PROGRESS / EDITABLE` lesson with
active lesson count `1`, and V00_L08 remains `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED @ f34b210` and unmodified.

The preserved generated automated-evidence report records 637 tests, zero
failures, zero errors, and zero skipped tests; the User-verified compile,
regression, and clean-build results remain PASS.

The evidence classifications are explicit. `THEORY VERIFIED` covers governance,
Design Lock, architecture, Frozen Backbone preservation, and the independent
final architecture review. `SIMULATION VERIFIED` covers startup, estimator
initialization, one-shot fusion, loss, reacquisition, and Glass/runtime
simulation evidence. `REAL HARDWARE VERIFIED` covers deployed V00_L09
Limelight flat-root `/limelight/json` acquisition/parser behavior, timing and
result handling, AprilTag 32 acquisition, stationary accepted fusion,
controlled translation and rotation, target loss/reacquisition, camera
disconnect/recovery, and the bounded autonomous lifecycle.

The bounded BLUE `ONE_METER_PATH` Gate 9 reached READY and completed with
`Reason = COMMAND_COMPLETED`, `State = COMPLETE`, and no adapter fatal fault.
Vision was intentionally invalid/suppressed during that run because the
physical Tag 32 placement was not asserted to match the official field
coordinate. Gate 9 therefore proves bounded autonomous lifecycle and estimator
compatibility; it does not prove exact 1.000 m endpoint accuracy or physical
absolute-pose calibration accuracy.

One historical Driver Station overrun warning was observed. Independent
read-only review found no proven structural runtime defect, busy-wait,
`Thread.sleep` production path, or unbounded retry. PathPlanner preparation and
full Limelight JSON parsing remain plausible but unproven timing contributors.
If the warning recurs, capture WPILib/Driver Station timing epochs before any
performance repair. No specific cause or resolution is claimed.

The final independent architecture review gate is
`PASS_V00_L09_FINAL_ARCHITECTURE_REVIEW_READY_FOR_DOCUMENTATION_CLOSURE`.
Documentation reconciliation is complete, but separate final documentation
review and explicit `COMPLETE / FROZEN / READ-ONLY` authorization remain
pending. User-owned Git add, commit, and push remain pending. This record does
not amend the V00 roadmap, create V00_L10, reopen V00_L08, or authorize source,
test, deploy, vendordep, configuration, or tuning changes.

### V00_L09 Final Freeze Recording — 2026-09-15

The independent final documentation review returned
`PASS_V00_L09_FINAL_DOCUMENTATION_REVIEW_READY_FOR_FREEZE_AUTHORIZATION`. The
Architect then explicitly authorized the documentation-only lifecycle
transition with `PASS_V00_L09_FINAL_FREEZE_AUTHORIZATION`.

V00_L09 is now recorded as the completed frozen lesson:

```text
STATUS: COMPLETE
ACTIVE STATE: COMPLETE / FROZEN / READ-ONLY
FREEZE STATE: FROZEN / READ-ONLY
ACTIVE LESSON COUNT: 0
GIT COMMIT: PENDING USER COMMIT
GIT PUSH: PENDING USER PUSH
PUBLICATION: PENDING USER PUBLICATION
```

The accepted `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE
VERIFIED` classifications remain unchanged. Gate 9 remains qualified as
bounded autonomous lifecycle and estimator compatibility evidence only;
vision was intentionally invalid/suppressed because the physical Tag 32
placement was not asserted to match the official field coordinate. Exact
1.000 m endpoint accuracy and physical absolute-pose calibration accuracy are
not claimed.

One historical Driver Station overrun warning remains documented as having no
proven structural runtime defect. PathPlanner preparation and full Limelight
JSON parsing remain plausible but unproven contributors; if the warning recurs,
capture WPILib/Driver Station timing epochs before any performance repair. No
specific cause or performance repair is claimed.

V00_L08 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ f34b210` and
unmodified. At the time of this freeze record, User-owned L09 Git commit,
push, and publication remained pending; no L09 publication metadata was
claimed at that point.

### V00_L09 Post-Publication Metadata Reconciliation — 2026-09-15

The User supplied and verified the final Git publication state for the frozen
L09 implementation. The implementation publication commit is `6548c98` with
subject `Complete V00_L09 Swerve pose estimator vision fusion`. The User
verified that `origin/main = 6548c98` and `origin/HEAD = 6548c98`; the recorded
push result was `Everything up-to-date`.

The accepted publication gate is
`PASS_V00_L09_REMOTE_PUBLICATION_VERIFIED`. The reconciled publication record
is:

```text
GIT COMMIT: 6548c98
COMMIT MESSAGE: Complete V00_L09 Swerve pose estimator vision fusion
GIT PUSH: COMPLETE / VERIFIED
REMOTE: origin/main = 6548c98
PUBLICATION: PUBLISHED / VERIFIED
FINAL PUBLICATION GATE: PASS_V00_L09_REMOTE_PUBLICATION_VERIFIED
METADATA RECONCILIATION COMMIT: PENDING USER COMMIT
```

The metadata-reconciliation commit is distinct from the implementation
publication commit and is not claimed to exist. This documentation-only
reconciliation does not change the accepted THEORY VERIFIED, SIMULATION
VERIFIED, or REAL HARDWARE VERIFIED evidence; it does not change Gate 9's
bounded qualification or the historical overrun wording.

V00_L09 remains `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`.
V00_L08 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ f34b210` and
unmodified. Codex performed no Git operation, and no source, test, deploy,
vendordep, configuration, tuning, roadmap, or predecessor change is included.

### V00 Module Final Closure Confirmation — 2026-09-15

The User subsequently published the documentation-only metadata reconciliation
at `5d36529` with subject `Record V00_L09 publication metadata`. The accepted
final repository state is `HEAD = origin/main = origin/HEAD = 5d36529`, with
ahead `0` and behind `0`. The V00 final closure gate is
`PASS_V00_MODULE_FINAL_CLOSURE_CONFIRMED`.

`V00_L09_SwervePoseEstimatorVisionFusion` remains `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED / VERIFIED`. Its implementation/freeze publication is
`6548c98` with subject `Complete V00_L09 Swerve pose estimator vision fusion`;
`5d36529` is the later metadata-reconciliation publication. No V00 lesson is
reopened, and the active lesson count remains `0`.

### M00 Governance Preparation Authorization — 2026-09-15

The controlling successor record is
`docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md`.
The M00 roadmap is `APPROVED / ROADMAP AUTHORIZED`; governance preparation is
`AUTHORIZED` by `PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED`; M00 runtime and
lesson activation remain `NOT ACTIVE`.

```text
Active Lesson Count: 0
```

The locked roadmap contains exactly 16 lessons, `M00_L01` through `M00_L16`,
in the ADR-defined order. The first lesson is locked as
`M00_L01 - Mechanism Architecture Reuse`, with directory identity
`M00_L01_MechanismArchitectureReuse`. It is `NOT ACTIVE / NOT YET CREATED`, is
not `IN_PROGRESS`, and has no implementation authorization.

After this governance record is reviewed and User-published, the User may
perform only the normal preparation workflow. The exact predecessor is the
published V00_L09 snapshot at repository state `5d36529`, with implementation
commit `6548c98`, at:

```text
C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7\real_robot_programming\module_V00\V00_L09_SwervePoseEstimatorVisionFusion
```

The exact future destination is:

```text
C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7\real_robot_programming\module_M00\M00_L01_MechanismArchitectureReuse
```

The future User-owned preparation sequence is: start at the repository root;
copy the complete frozen V00_L09 directory; create `module_M00` only as part of
that authorized copy workflow; rename only the destination copy to the locked
directory identity; remove only the destination `build\` and `.gradle\`;
select WPILib 2026 Java 17; run the inherited baseline clean build; and report
`BUILD SUCCESSFUL` plus Git status. Only after that evidence may Architecture
Audit, Design Lock, lifecycle activation, and implementation authorization be
considered separately. The baseline command to be run inside the future
destination is:

```powershell
$env:JAVA_HOME = "C:\Users\Public\wpilib\2026\jdk"
.\gradlew.bat clean build "-Dorg.gradle.java.home=C:\Users\Public\wpilib\2026\jdk"
```

M00 preserves the Frozen Backbone, Frozen Interface Contract, Constants as the
default configuration authority, frozen predecessor protection, and one lesson
per new concept. RobotContainer remains composition root only. Vendor APIs
remain confined to concrete IO adapters. Mechanism data continues to flow
`hardware -> IOInputs -> subsystem/processing -> immutable Observation ->
read-only telemetry`. Intake, Feeder, Flywheel, and Elevator retain independent
ownership. Shooting composition remains `FlywheelSubsystem + FeederSubsystem +
ShootCommand`; no `ShooterSubsystem` or `ShooterIO` is authorized absent a
later formal architecture change.

M00_L01's sole concept is Mechanism Architecture Reuse: how the mastered
drivetrain, vision, and autonomous architecture applies to non-drivetrain
mechanisms. It may teach subsystem ownership, IO, immutable Observations,
read-only telemetry, composition-root assembly, safe stop, and architecture
mapping. It must not implement Intake, Feeder, Flywheel, Elevator, closed-loop
control, readiness, homing, travel limits, coordination, autonomous events, or
a new hardware API.

Any future student-facing M00 Markdown must be delivered as separate English
and Vietnamese files with identical structure, course/chapter identity,
meaning, evidence, and architecture rules. English is normative; Vietnamese is
student-friendly but semantically equivalent. Evidence must use only `THEORY
VERIFIED`, `SIMULATION VERIFIED`, `REAL HARDWARE VERIFIED`, `REAL HARDWARE
DEFERRED`, or `NOT APPLICABLE`. M00_L01 runtime applicability is not claimed;
the future Design Lock decides it. No V00 rerun is required by this preparation
authorization.

### M00_L01 Controlled Activation — 2026-09-15

The accepted preparation, inherited Java 17 baseline build, independent
Architecture / Inheritance Audit, Frozen Backbone audit, M00-specific ownership
audit, and one-new-concept audit are `PASS`. The Architect Design Lock is
`PASS_M00_L01_FINAL_DESIGN_LOCK`.

The canonical lesson is `M00_L01 - Mechanism Architecture Reuse`, with locked
directory identity `M00_L01_MechanismArchitectureReuse`. Its predecessor is
`V00_L09_SwervePoseEstimatorVisionFusion`, which remains `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED / VERIFIED` and is not modified by this activation.

This documentation-only controlled activation makes M00_L01 the sole current
`IN_PROGRESS / EDITABLE` lesson and reconciles the active lesson count to `1`.
Implementation authorization and production-code authorization remain `NONE`.
No Java, test, configuration, vendordep, deploy, mechanism, Simulation, Driver
Station / Glass, or real-hardware change is authorized. M00_L01 evidence is
currently `THEORY VERIFIED`; Simulation, Driver Station / Glass, and real
hardware are `NOT APPLICABLE` because the lesson is architecture-only.

At this controlled-activation point, the transition guide and student-facing
learning documentation, their reviews, the final inherited clean
build/regression, final architecture/documentation review, freeze, and User Git
publication remained pending and separately controlled. The later current state
is recorded below.

### M00_L01 Pre-Freeze Documentation and Closure Readiness Reconciliation — 2026-09-15

The paired English and Vietnamese student learning guides are `COMPLETE /
REVIEWED` with identical 21-section structure and equivalent technical meaning.
The initial independent review returned
`HOLD_M00_L01_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_CONSTANTS_AUTHORITY`
because both guides omitted the rule that `Constants.java` remains the default
configuration authority. A minimal two-guide repair added that rule without
defining mechanism configuration values and passed at
`PASS_M00_L01_CONSTANTS_AUTHORITY_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`; the
independent rereview passed at
`PASS_M00_L01_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD`.
The Architect accepted documentation review completion through
`PASS_M00_L01_DOCUMENTATION_REVIEW_COMPLETE_READY_FOR_FINAL_BUILD`.

The User then supplied the distinct final inherited clean build/regression:
`BUILD SUCCESSFUL in 23s`, with 7 actionable tasks executed. Its accepted gate
is `PASS_M00_L01_FINAL_INHERITED_CLEAN_BUILD_REGRESSION`. The final read-only
architecture/documentation closure review passed at
`PASS_M00_L01_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION`, and
the Architect accepted that result through
`PASS_M00_L01_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
Technical/content closure readiness is `PASS`.

```text
M00_L01 Status: IN_PROGRESS
M00_L01 Active State: IN_PROGRESS / EDITABLE
M00_L01 Freeze State: EDITABLE
Active Lesson Count: 1
Design Lock: PASS_M00_L01_FINAL_DESIGN_LOCK
Student Documentation: COMPLETE / REVIEWED
Constants Repair: COMPLETE
Independent Rereview: PASS
Final Inherited Clean Build/Regression: PASS
Final Closure Review: PASS
Technical/Content Closure Readiness: PASS
Production Code Authorization: NONE
Freeze Authorization: PENDING
Git Publication: PENDING USER GIT
```

No Java, test, configuration, vendordep, deploy, runtime, hardware API, or
mechanism implementation changed. Evidence remains `THEORY VERIFIED`; focused
new tests, Simulation, Driver Station / Glass, and real hardware remain `NOT
APPLICABLE`. At that pre-freeze reconciliation point, M00_L01 was not yet
`COMPLETE`, `FROZEN`, `READ-ONLY`, or `PUBLISHED`. Independent reconciliation
review, explicit Architect freeze authorization, lifecycle transition, and
User-owned publication remained pending.

### M00_L01 Final Documentation-Only Freeze Closure — 2026-09-15

The completed pre-freeze documentation reconciliation passed at
`PASS_M00_L01_DOCUMENTATION_RECONCILED_READY_FOR_FREEZE_AUTHORIZATION`, the
Architect accepted it for independent review at
`PASS_M00_L01_DOCUMENTATION_RECONCILIATION_ACCEPTED_READY_FOR_INDEPENDENT_REVIEW`,
and the independent read-only reconciliation review passed at
`PASS_M00_L01_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`.
The Architect then explicitly issued `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION`.

That authorization closes the documentation-only lesson lifecycle as follows:

```text
M00_L01 Status: COMPLETE
M00_L01 Active State: COMPLETE / FROZEN / READ-ONLY
M00_L01 Freeze State: FROZEN / READ-ONLY
Active Lesson Count: 0
Design Lock: PASS_M00_L01_FINAL_DESIGN_LOCK
Final Clean Build/Regression: PASS
Final Documentation Review: PASS
Final Reconciliation Review: PASS
Technical/Content Closure: PASS
Production Code Authorization: NONE
Freeze Authorization: PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION
Publication: NOT YET PUBLISHED / PENDING USER GIT
Git Commit: PENDING USER COMMIT
Git Push: PENDING USER PUSH
Remote Verification: PENDING
```

Evidence remains `THEORY VERIFIED`; focused new tests, Simulation, Driver
Station / Glass, and real hardware remain `NOT APPLICABLE`. No Java, test,
configuration, vendordep, deploy, runtime, hardware API, mechanism
implementation, EN/VI guide, or frozen V00_L09 content changed. M00_L02 is not
active and is not created. The M00 module is not declared complete. At this
freeze-recording point, User-owned Git staging, commit, push, remote
verification, and any required publication metadata reconciliation remained
pending.

### M00_L01 Lesson Publication Metadata Reconciliation — 2026-09-15

The User subsequently committed and pushed the frozen M00_L01 lesson. The
accepted publication evidence is commit `83907ab` with subject `Complete
M00_L01 mechanism architecture reuse`. The User verified `HEAD = origin/main =
origin/HEAD = 83907ab` on branch `main`, and the remote publication gate is
`PASS_M00_L01_REMOTE_PUBLICATION_VERIFIED`. The later isolated PowerShell
`else` entry error is an interactive syntax issue after the PASS gate and does
not invalidate publication evidence.

```text
M00_L01: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
Lesson Publication Commit: 83907ab
Lesson Publication Commit Message: Complete M00_L01 mechanism architecture reuse
Published Branch: main
Verified Remote: origin/main
Published Repository State: 83907ab
Remote Publication Verification: PASS_M00_L01_REMOTE_PUBLICATION_VERIFIED
Active Lesson Count: 0
M00_L02: NOT ACTIVE / NOT CREATED
Publication Metadata Reconciliation: PENDING USER COMMIT
Publication Metadata Push: PENDING USER PUSH
Final Metadata Remote Verification: PENDING
```

This metadata reconciliation does not alter technical closure, evidence,
runtime applicability, the frozen roadmap, or the M00_L01 Design Lock. The M00
module is not declared complete, and M00_L02 remains inactive and uncreated.

### M00_L02 Final Freeze and Lifecycle Closure — 2026-09-16

The accepted M00_L01 predecessor state is `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED / VERIFIED`, with primary publication `83907ab`, metadata publication
`f523118`, and final gate `PASS_M00_L01_FINAL_PUBLICATION_COMPLETE`. M00_L01
remains frozen and was not modified.

M00_L02 preparation, the inherited Java 17 baseline, the accidental nested-copy
forensic review and controlled repair, post-repair verification, and the
Architecture / Inheritance Audit are accepted. The Architect issued
`PASS_M00_L02_FINAL_DESIGN_LOCK`, and the controlled activation passed through
`PASS_M00_L02_CONTROLLED_ACTIVATION_RECORDED_READY_FOR_DOCUMENTATION_IMPLEMENTATION_AUTHORIZATION`
and `PASS_M00_L02_CONTROLLED_ACTIVATION_ACCEPTED`.

The separately authorized English and Vietnamese learning guides are complete.
Each contains 25 sections, 15 knowledge-check questions, 15 answers, and an
80-row evidence matrix. The matrices contain 8 `VERIFIED`, 0 `PROVISIONAL`, and
72 `UNKNOWN` rows. The initial independent review accurately recorded
`HOLD_M00_L02_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_REQUIRED_OWNERSHIP_CONSTANTS_AND_KNOWLEDGE_CHECK_COVERAGE`.
The bounded two-guide repair then passed through
`PASS_M00_L02_MINIMAL_DOCUMENTATION_REPAIR_AUTHORIZED`,
`PASS_M00_L02_MINIMAL_DOCUMENTATION_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`, and
`PASS_M00_L02_MINIMAL_DOCUMENTATION_REPAIR_ACCEPTED_READY_FOR_INDEPENDENT_REREVIEW`.
The independent rereview passed at
`PASS_M00_L02_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD` and
was accepted at
`PASS_M00_L02_INDEPENDENT_DOCUMENTATION_REREVIEW_ACCEPTED_READY_FOR_FINAL_USER_BUILD`.

The User-supplied final clean build/regression passed with `BUILD SUCCESSFUL in
33s` and 7 actionable tasks executed. Its gates are
`PASS_M00_L02_FINAL_USER_BUILD_REGRESSION` and
`PASS_M00_L02_FINAL_USER_BUILD_REGRESSION_ACCEPTED_READY_FOR_FINAL_CLOSURE_REVIEW`.
The final read-only closure review passed at
`PASS_M00_L02_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION` and
was accepted at
`PASS_M00_L02_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
Documentation reconciliation was authorized by
`PASS_M00_L02_DOCUMENTATION_RECONCILIATION_AUTHORIZED` and is now recorded at
`PASS_M00_L02_DOCUMENTATION_RECONCILED_READY_FOR_INDEPENDENT_REVIEW`.
The independent reconciliation review then passed at
`PASS_M00_L02_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`,
and the Architect issued `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION`.

```text
Lesson: M00_L02 - Mechanism Hardware Evidence Audit
Filesystem: M00_L02_MechanismHardwareEvidenceAudit
Status: COMPLETE
Active State: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN / READ-ONLY
Active Lesson Count: 0
Predecessor: M00_L01 - Mechanism Architecture Reuse
Design Lock: PASS_M00_L02_FINAL_DESIGN_LOCK
Documentation Implementation: COMPLETE
Independent Documentation Rereview: PASS
Final User Build: PASS
Final Closure Review: PASS
Documentation Reconciliation: PASS
Independent Reconciliation Review: PASS
Production Code Authorization: NONE
Test Implementation Authorization: NONE
Configuration Authorization: NONE
Runtime Behavior Authorization: NONE
Freeze Authorization: PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION
Git Commit: PENDING USER COMMIT
Git Push: PENDING USER PUSH
Remote Verification: PENDING
Publication Metadata Reconciliation: PENDING
Publication: PENDING USER GIT PUBLICATION
M00_L03: NOT ACTIVE / NOT CREATED
```

The sole new concept remains `MECHANISM HARDWARE EVIDENCE AUDIT`. Evidence is
`THEORY VERIFIED`; focused new tests, Simulation, and Driver Station / Glass are
`NOT APPLICABLE`; real hardware is `REAL HARDWARE DEFERRED`. No production Java,
test, configuration, vendordep, deploy, runtime behavior, mechanism API,
mechanism Observation, command, adapter, constant, CAN/PID configuration, or
telemetry behavior changed. The Frozen Backbone, complete M00 ownership lock,
Constants authority, and locked 16-lesson roadmap remain preserved. M00_L02 is
now `COMPLETE / FROZEN / READ-ONLY`, no lesson is active, and M00_L03 is `NOT
ACTIVE / NOT CREATED`. No further M00_L02 lesson edit is authorized except a
later bounded publication-metadata reconciliation after User Git publication.
Publication remains pending and is not claimed complete.

### M00_L02 Post-Publication Metadata Reconciliation — 2026-09-16

The User completed and pushed the primary frozen-lesson publication. Accepted
evidence records full commit
`65a92a4a5806fd5134e0114e851c4e4cc093c58e` with subject `Complete M00_L02
mechanism hardware evidence audit`. After the push, `HEAD`, `origin/main`, and
`origin/HEAD` were observed aligned at short commit `65a92a4`. The accepted
gates are `PASS_M00_L02_PRIMARY_GIT_PUBLICATION` and
`PASS_M00_L02_PRIMARY_GIT_PUBLICATION_ACCEPTED_READY_FOR_PUBLICATION_METADATA_RECONCILIATION`.

```text
M00_L02: COMPLETE / FROZEN / READ-ONLY
Active Lesson Count: 0
Primary Publication Commit: 65a92a4a5806fd5134e0114e851c4e4cc093c58e
Primary Publication Push: PASS
Publication Metadata Reconciliation: COMPLETE / RECORDED
Metadata Commit: PENDING USER COMMIT
Metadata Push: PENDING USER PUSH
Final Remote Verification: PENDING
Final Publication Completion: PENDING
M00_L03: NOT ACTIVE / NOT CREATED
```

This bounded metadata exception does not reopen M00_L02 or authorize technical,
student-guide, or roadmap changes. The future metadata commit does not yet
exist and is not claimed.

### M00_L03 Controlled Documentation-Only Lifecycle Activation — 2026-09-16

M00_L02 is accepted as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
at primary publication `65a92a4a5806fd5134e0114e851c4e4cc093c58e`, metadata
publication `84010ff5022a33a946888fafedbbca0d67439e0c`, and final gate
`PASS_M00_L02_FINAL_PUBLICATION_COMPLETE`. It remains untouched.

The prepared `M00_L03_IntakeFoundation` candidate passed its accepted Java
17.0.16 Temurin baseline with exit code `0`. The original preparation script's
lesson-local `AGENTS.md` expectation was a script-check defect only;
repository-root `AGENTS.md` is authoritative, the candidate was unaffected,
and no recopy was required. Inheritance is 607/607 comparable files and 173/173
protected files with zero missing, extra, or SHA-256-different files.

The Architecture / Inheritance Audit passed at
`PASS_M00_L03_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`, and
the Architect issued `PASS_M00_L03_FINAL_DESIGN_LOCK`.

```text
Active Lesson: M00_L03 - Intake Foundation
Filesystem: M00_L03_IntakeFoundation
Status: IN_PROGRESS
Active State: IN_PROGRESS / EDITABLE
Freeze State: EDITABLE
Active Lesson Count: 1
Predecessor: M00_L02 - Mechanism Hardware Evidence Audit
Predecessor State: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
Design Lock: PASS_M00_L03_FINAL_DESIGN_LOCK
Production Implementation: PENDING SEPARATE ARCHITECT AUTHORIZATION
Test Implementation: PENDING SEPARATE ARCHITECT AUTHORIZATION
Documentation Implementation: PENDING SEPARATE ARCHITECT AUTHORIZATION
Theory / Architecture: THEORY VERIFIED REQUIRED
Focused Tests: REQUIRED LATER
Simulation: APPLICABLE / REQUIRED LATER
Driver Station / Glass: NOT APPLICABLE COMPLETION GATES
Real Hardware: DEFERRED
M00_L04: NOT ACTIVE / NOT CREATED
```

The sole new concept is an independently owned, vendor-neutral Intake mechanism
foundation. No Intake implementation, test, command, binding, vendor adapter,
configuration, Constants change, runtime behavior, or student guide is
authorized by this activation.

### M00_L03 Implementation, Verification, and Student Documentation Reconciliation — 2026-09-16

The preceding activation record is preserved as the historical pre-
implementation state. Separate Architect/User authorization subsequently
issued `PASS_M00_L03_PRODUCTION_TEST_IMPLEMENTATION_AUTHORIZED`. The authorized
vendor-neutral Intake foundation is implemented and the independent
implementation review passed and was accepted.

The first focused run executed 16 tests: 15 passed and
`IntakeSubsystemTest.periodicBuildsFreshImmutableSnapshotsFromCurrentInputsAndIntent`
failed because an optional callback was dereferenced at line 69. This was
classified as a test defect. The bounded repair changed only
`IntakeSubsystemTest.java`, guarded the optional callback, and removed no
assertions. The focused retest passed with `BUILD SUCCESSFUL in 4s` and exit
code `0`. The full clean regression passed with `BUILD SUCCESSFUL in 20s`,
exit code `0`, and 7 of 7 actionable tasks executed.

User-owned Simulation passed only for startup, Noop composition, subsystem
integration, and Intake NetworkTables/telemetry presence while Disabled. It is
bounded software-architecture evidence, not proof of physical hardware,
wiring, CAN identity, direction, motion, current, load, force, or stopping.
Driver Station / Glass are not applicable completion gates and real-hardware
verification remains deferred.

The documentation gate
`PASS_M00_L03_DOCUMENTATION_IMPLEMENTATION_AND_LIFECYCLE_RECONCILIATION_AUTHORIZED`
is consumed. The English and Vietnamese student guides are implemented with
matching 34-section structures and 15-question/15-answer review sets. No
production Java, test, configuration, vendordep, or deploy asset was changed by
this documentation task.

```text
Active Lesson: M00_L03 - Intake Foundation
Status: IN_PROGRESS
Active State: IN_PROGRESS / EDITABLE
Freeze State: EDITABLE
Active Lesson Count: 1
Implementation: COMPLETE FOR AUTHORIZED M00_L03 SCOPE
Focused Tests: VERIFIED
Full Clean Regression: VERIFIED
Simulation: VERIFIED — BOUNDED SOFTWARE/NOOP/COMPOSITION EVIDENCE ONLY
Independent Implementation Review: PASS / ACCEPTED
Student Documentation: IMPLEMENTED
Independent Documentation Review: PENDING
Final User Closure Build: PENDING
Final Closure Review: PENDING
Freeze Authorization: PENDING
COMPLETE / FROZEN / READ-ONLY: NOT YET AUTHORIZED
Publication: NOT YET PUBLISHED
M00_L04: NOT ACTIVE / NOT CREATED
```

No Intake vendor adapter or `Constants.java` change was introduced. Unsupported
hardware facts remain `UNKNOWN`. M00_L04 retains exclusive ownership of
Commands, requirements, bindings, interruption behavior, and default/manual
mechanism ownership.

### M00_L03 Final Closure Review and Lifecycle Reconciliation — 2026-09-17

The preceding implementation and student-documentation record is preserved as
the historical state before independent documentation review and final closure
evidence. The initial independent documentation review returned `HOLD` because
Step 16 of the transition guide incorrectly described the mutable one-cycle
`IntakeIOInputs` transport snapshot as immutable. The authorized one-line
repair changed that wording to `mutable one-cycle input snapshot` without
changing production Java, tests, student guides, configuration, vendordeps, or
deploy assets. The repair passed at
`PASS_M00_L03_TRANSITION_GUIDE_MUTABLE_IOINPUTS_ONE_LINE_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`,
was accepted at
`PASS_M00_L03_TRANSITION_GUIDE_MUTABLE_IOINPUTS_REPAIR_ACCEPTED_READY_FOR_INDEPENDENT_DOCUMENTATION_REREVIEW`,
and the independent documentation rereview passed at
`PASS_M00_L03_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_CLOSURE_BUILD`.
The Architect accepted that rereview at
`PASS_M00_L03_INDEPENDENT_DOCUMENTATION_REREVIEW_ACCEPTED_READY_FOR_FINAL_USER_CLOSURE_BUILD`.

The User then supplied the final Java 17 closure build/regression:
`BUILD SUCCESSFUL in 42s`, 7 actionable tasks executed, exit code `0`. The
accepted gates are `PASS_M00_L03_FINAL_USER_CLOSURE_BUILD_REGRESSION` and
`PASS_M00_L03_FINAL_USER_CLOSURE_BUILD_REGRESSION_ACCEPTED_READY_FOR_FINAL_CLOSURE_REVIEW`.
The final read-only closure review passed at
`PASS_M00_L03_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION` and
was accepted at
`PASS_M00_L03_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
No technical or substantive documentation defect remains.

This documentation-only reconciliation is complete. M00_L03 remains the sole
`IN_PROGRESS / EDITABLE` lesson with active lesson count `1` and freeze state
`EDITABLE`; it is not yet `COMPLETE / FROZEN / READ-ONLY`. Implementation is
complete for the authorized scope, focused tests and full regression are
verified, the final closure build is verified, bounded software/Noop/composition
Simulation is verified, Driver Station / Glass are not applicable completion
gates, real hardware remains deferred, student documentation and its rereview
are verified, and final closure review is PASS. Independent reconciliation
review, explicit Architect freeze authorization, freeze recording, and
User-owned publication remain pending. M00_L04 remains `NOT ACTIVE / NOT
CREATED`.

### M00_L03 Final Lifecycle Freeze — 2026-09-17

The independent reconciliation review passed at
`PASS_M00_L03_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`.
The Architect then explicitly issued
`PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION`.

The authorized documentation-only lifecycle transition is now recorded:

```text
Lesson: M00_L03 - Intake Foundation
Status: COMPLETE
Active State: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN
Active Lesson Count: 0
Implementation: COMPLETE FOR AUTHORIZED M00_L03 SCOPE
Focused Tests: VERIFIED
Full Regression: VERIFIED
Final Closure Build: VERIFIED
Simulation: VERIFIED — BOUNDED SOFTWARE/NOOP/COMPOSITION ONLY
Driver Station / Glass: NOT APPLICABLE
Real Hardware: REAL HARDWARE DEFERRED
Student Documentation: VERIFIED
Independent Documentation Rereview: PASS
Final Closure Review: PASS
Lifecycle Reconciliation: COMPLETE
Independent Reconciliation Review: PASS
Architect Freeze Authorization: PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION
Publication: PENDING USER GIT
M00_L04: NOT ACTIVE / NOT CREATED
```

M00_L03 is no longer editable. This freeze does not publish the lesson and
does not authorize M00_L04 preparation or activation. Production Java, tests,
student guides, Constants, configuration, vendordeps, deploy assets, and
frozen M00_L02 remain unchanged. The next lifecycle work is User-owned
publication and any separately authorized publication-metadata reconciliation.

### M00_L03 Primary Publication and Metadata Reconciliation — 2026-09-17

The User completed the Architect-authorized explicit-allowlist primary Git
publication. The accepted primary publication commit is
`3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`; the accepted push result is
`84010ff..3d94dc6  main -> main`. Post-push local `HEAD` and `origin/main`
both resolved to `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`, so primary
remote alignment is `PASS`. The accepted gates are
`PASS_M00_L03_PRIMARY_GIT_PUBLICATION` and
`PASS_M00_L03_PRIMARY_GIT_PUBLICATION_ACCEPTED_READY_FOR_PUBLICATION_METADATA_RECONCILIATION`.

```text
Lesson: M00_L03 - Intake Foundation
Lifecycle: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN
Active Lesson Count: 0
Primary Git Publication: PASS
Primary Publication Commit: 3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3
Primary Push: PASS
Remote Alignment: PASS
Publication Metadata Reconciliation: COMPLETE
Metadata Publication: PENDING USER GIT
Final Publication Verification: PENDING
M00_L04: NOT ACTIVE / NOT CREATED
```

This reconciliation does not claim the metadata commit, metadata push, final
remote verification, or final publication completion. M00_L03 remains frozen
and read-only; M00_L04 remains inactive and uncreated.

### M00_L03 Final Publication and M00_L04 Controlled Activation — 2026-09-18

M00_L03 completed the two-commit publication workflow. Its primary publication
is `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`, its metadata publication is
`b2464f66da42a6281acb7bfc709f2a3b83296505`, metadata remote alignment is
`PASS`, and final publication verification passed at
`PASS_M00_L03_FINAL_PUBLICATION_VERIFICATION_READY_FOR_ARCHITECT_FINAL_PUBLICATION_COMPLETE`.
M00_L03 is therefore `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
and remains untouched.

The User-prepared `M00_L04_IntakeCommandOwnership` candidate passed its
accepted baseline build (`BUILD SUCCESSFUL in 48s`, 7 actionable tasks: 6
executed and 1 up-to-date, exit code `0`). Its Architecture / Inheritance Audit
passed at `PASS_M00_L04_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`:
279 of 279 comparable inherited files matched M00_L03 with no governed delta.
The Architect then issued `PASS_M00_L04_FINAL_DESIGN_LOCK`.

```text
Active Lesson: M00_L04 - Intake Command Ownership
Filesystem: M00_L04_IntakeCommandOwnership
Status: IN_PROGRESS
Active State: IN_PROGRESS / EDITABLE
Freeze State: EDITABLE
Active Lesson Count: 1
Predecessor: M00_L03 - Intake Foundation
Predecessor State: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
Preparation: PASS
Baseline Build: PASS
Architecture / Inheritance Audit: PASS
Final Design Lock: PASS_M00_L04_FINAL_DESIGN_LOCK
One New Concept: SCHEDULER-MANAGED MANUAL OWNERSHIP OF THE EXISTING INTAKE CAPABILITY
Locked Command: RunIntakeCommand
Locked Binding: Xbox Right Bumper / whileTrue
Implementation: NOT STARTED
Implementation Authorization: PENDING
Focused Tests: NOT RUN
Simulation: NOT RUN
Driver Station: NOT RUN
Glass: NOT APPLICABLE
Real Hardware: REAL HARDWARE DEFERRED
M00_L05: NOT ACTIVE / NOT CREATED
```

This controlled activation authorizes lifecycle documentation only. It does
not authorize Java or test implementation. The locked future production scope
is one `RunIntakeCommand` plus the bounded `RobotContainer` Right Bumper
`whileTrue` binding; focused lifecycle and binding tests remain pending a
separate implementation authorization. No Intake subsystem, IO, Observation,
telemetry, Constants, vendor adapter, hardware configuration, or later-lesson
work is authorized.

### M00_L04 Post-Verification Documentation and Lifecycle Reconciliation — 2026-09-18

The preceding controlled-activation section is preserved as the historical
pre-implementation state. Separate Architect/User authorization subsequently
issued
`PASS_M00_L04_CONTROLLED_ACTIVATION_ACCEPTED_PRODUCTION_TEST_IMPLEMENTATION_AUTHORIZED`.
The exact implementation created `RunIntakeCommand.java`, modified only the
bounded `RobotContainer` controller composition, created the two authorized
focused-test files, and narrowly updated `IntakeArchitectureBoundaryTest.java`.
No other production or test Java changed.

`RunIntakeCommand` requires exactly the existing `IntakeSubsystem`, requests
Intake once in `initialize()`, performs no repeated request in `execute()`,
never self-finishes, and delegates unconditionally to `IntakeSubsystem.stop()`
from `end(...)`. RobotContainer binds the existing controller's semantic Right
Bumper through `whileTrue` and preserves the Back/View Prepare Autonomous
binding. No Intake default command, IO change, Observation change, telemetry
change, Constants change, vendor adapter, or hardware selection was added.

Authoritative User evidence records 14/14 focused tests PASS with `BUILD
SUCCESSFUL in 28s`, four tasks executed, and exit code `0`; the full clean
regression passed with `BUILD SUCCESSFUL in 47s`, seven tasks executed, and
exit code `0`. Bounded WPILib Simulation is `SIMULATION VERIFIED / BOUNDED`.
Bounded Driver Station verification is `VERIFIED / BOUNDED` and observed the
software sequence `STOPPED -> INTAKE_REQUESTED -> STOPPED` across Right Bumper
hold and release. `Available=false` and `Connected=false` are expected because
the selected implementation remains `IntakeIONoop`. Glass is supporting
read-only visualization only and `NOT APPLICABLE` as a distinct completion
gate. Real hardware remains `REAL HARDWARE DEFERRED`.

The independent implementation review passed at
`PASS_M00_L04_INDEPENDENT_IMPLEMENTATION_REVIEW_READY_FOR_DOCUMENTATION_AUTHORIZATION`
and was accepted at
`PASS_M00_L04_INDEPENDENT_IMPLEMENTATION_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_IMPLEMENTATION`.
The paired English and Vietnamese student guides are now created with matching
34-section and 15-question/15-answer structures. Documentation reconciliation
is complete for this authorized task and remains pending independent
documentation review.

```text
M00_L04 Status: IN_PROGRESS
M00_L04 Active State: IN_PROGRESS / EDITABLE
M00_L04 Freeze State: EDITABLE
Active Lesson Count: 1
Implementation: COMPLETE
Focused Tests: VERIFIED / 14 OF 14 PASS
Full Regression: VERIFIED
Simulation: SIMULATION VERIFIED / BOUNDED
Driver Station: VERIFIED / BOUNDED
Glass: NOT APPLICABLE AS A DISTINCT COMPLETION GATE
Real Hardware: REAL HARDWARE DEFERRED
Independent Implementation Review: PASS
Student Documentation: CREATED / PENDING INDEPENDENT DOCUMENTATION REVIEW
Freeze Authorization: NOT YET AUTHORIZED
Publication: NOT STARTED
M00_L05: NOT ACTIVE / NOT CREATED
```

`IntakeIOInputs` remains a mutable one-cycle transport/input snapshot;
`IntakeObservation` remains an immutable vendor-neutral observation/value
snapshot. No physical motor, CAN, wiring, direction, speed, current, sensing,
game-piece, or physical-stop claim is made. M00_L04 is not `COMPLETE`,
`FROZEN`, `READ-ONLY`, or published.

### M00_L04 Final Lifecycle Reconciliation and Freeze — 2026-09-18

The preceding post-verification section is preserved as historical pre-closure
state. The initial documentation HOLD was resolved by the authorized bounded
guide repair and independent rereview. The initial final-closure HOLD was
resolved by the authorized transition-guide reconciliation and independent
confirmation. The resumed independent final closure review passed at
`PASS_M00_L04_FINAL_CLOSURE_REVIEW_READY_FOR_LIFECYCLE_RECONCILIATION_AND_FREEZE_PREPARATION`,
and the Architect accepted it at
`PASS_M00_L04_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_FINAL_LIFECYCLE_RECONCILIATION_AND_FREEZE_PREPARATION`.

```text
M00_L03: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L04: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN
Editable Boundary: NONE
Active Lesson: NO
Active Lesson Count: 0
Implementation: COMPLETE / VERIFIED TO AUTHORIZED LESSON SCOPE
Documentation: COMPLETE / VERIFIED
Final Closure Build: PASS / BUILD SUCCESSFUL IN 23s / 7 OF 7 TASKS EXECUTED / EXIT CODE 0
Final Closure Review: PASS
Simulation: SIMULATION VERIFIED / BOUNDED
Driver Station: VERIFIED / BOUNDED
Glass: NOT APPLICABLE AS A DISTINCT COMPLETION GATE
Real Hardware: REAL HARDWARE DEFERRED
Git Publication: PENDING USER ACTION
Publication State: NOT YET PUBLISHED
M00_L05: NOT ACTIVE / NOT CREATED
```

The frozen lesson preserves scheduler-managed manual Intake ownership through
the semantic Right Bumper `whileTrue` binding, one-shot initialization request,
and unconditional subsystem-owned stop on command end. `IntakeIONoop` remains
deterministic, vendor-neutral, unavailable, disconnected, and incapable of
physical output. No M00 lesson is active. This reconciliation does not perform
Git, claim publication, invent a commit, or activate M00_L05.

### M00_L04 Primary Publication and Metadata Reconciliation — 2026-09-18

The preceding lifecycle-freeze section is preserved as the historical
pre-publication state. The User completed the primary M00_L04 publication at
commit `5c86be3` with subject `Complete M00_L04 intake command ownership`.
Accepted post-push evidence records `HEAD = origin/main = origin/HEAD =
5c86be3`; primary remote alignment is `PASS`.

```text
M00_L04: COMPLETE / FROZEN / READ-ONLY
Implementation: COMPLETE / VERIFIED
Documentation: COMPLETE / VERIFIED
Final Closure Review: PASS
Primary Publication: COMPLETE
Primary Publication Commit: 5c86be3
Primary Remote Alignment: PASS
Publication Metadata Reconciliation: COMPLETE IN WORKING TREE
Metadata Git Publication: PENDING USER ACTION
Final Publication Verification: PENDING
Real Hardware: REAL HARDWARE DEFERRED
Active Lesson Count: 0
Current Active M00 Lesson: NONE
M00_L05: NOT ACTIVE / NOT CREATED
```

Primary publication is complete; the separate metadata commit does not yet
exist. Independent metadata review, User-owned metadata commit/push, metadata
remote verification, and final publication verification remain pending. No
M00 lesson is active, and M00_L05 is not created or activated.

### M00_L04 Final Publication and M00_L05 Controlled Activation — 2026-09-18

The preceding section is preserved as historical pre-metadata state. The User
completed M00_L04 metadata publication at commit `24738e6` with subject
`Record M00_L04 publication metadata`. Accepted evidence records `HEAD =
origin/main = origin/HEAD = 24738e6`, metadata remote alignment `PASS`, and
final publication verification `PASS`. M00_L04 is `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED / VERIFIED` and remains untouched.

The User then prepared `M00_L05_FeederFoundation` from final M00_L04 through
copy, rename, candidate-only artifact cleanup, and a Java 17 baseline build.
The accepted baseline is `BUILD SUCCESSFUL in 41s` with 7 actionable tasks, 6
executed and 1 up-to-date. The independent inheritance audit found 285/285
governed files byte-identical with zero unexpected delta and passed at
`PASS_M00_L05_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`.
The Architect issued
`PASS_M00_L05_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION`.

```text
M00_L04: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L04 Primary Commit: 5c86be3
M00_L04 Metadata Commit: 24738e6
M00_L05: IN_PROGRESS / EDITABLE WITHIN LOCKED DESIGN BOUNDARY
Freeze State: NOT FROZEN / EDITABLE WITHIN LOCKED DESIGN BOUNDARY
Active Lesson Count: 1
Current Active M00 Lesson: M00_L05
One New Concept: FEEDER AS ONE INDEPENDENTLY OWNED TRANSPORT MECHANISM CAPABILITY
Runtime Strategy: FeederIONoop ONLY
Dynamic Feeder Simulation: NOT AUTHORIZED
Real Adapter: NOT AUTHORIZED
Real Hardware: REAL HARDWARE DEFERRED
Implementation Authorization: PENDING
Constants.java Changes: NOT AUTHORIZED
M00_L06: NOT ACTIVE / NOT CREATED
```

The locked future production boundary is exactly five new Feeder foundation
types plus bounded modifications to `RobotContainer.java` and
`RobotTelemetry.java`. The locked focused-test boundary is exactly the six
named Feeder tests. This activation does not implement Feeder, authorize Java
or test changes, configure CAN 45-49, or activate M00_L06.

### M00_L05 Post-Verification Documentation Reconciliation — 2026-09-19

The preceding activation record is historical. Separate authorization was
consumed, and M00_L05 implementation is complete within the exact
five-created/two-modified production boundary and six-test boundary.

The initial focused run passed 18 of 19 tests. The single failure was a brittle
architecture-test text match against the valid Javadoc phrase `current cycle`.
`FeederArchitectureBoundaryTest` was repaired to inspect non-static,
non-synthetic `FeederIOInputs` fields reflectively. All six focused test
classes then passed with `BUILD SUCCESSFUL in 26s` and exit code `0`.

The initial full clean regression passed 681 of 682 tests. The remaining
failure was a test-isolation defect: `new FeederSubsystem(null)` registered a
partially constructed `SubsystemBase` with the singleton `CommandScheduler`
before null rejection. The one-file `FeederSubsystemTest` repair added
`@AfterEach` cleanup through `unregisterAllSubsystems()` and preserved the
null-rejection assertion. The final full clean regression passed all 682 tests
with `BUILD SUCCESSFUL in 51s`, seven tasks executed, and exit code `0`.

Bounded WPILib Simulation and HALSIM Robot State verification passed with
`Available=false`, `Connected=false`, and `RequestedState=STOPPED` in both
Disabled and Teleoperated Enabled states. These results verify composition,
Noop runtime stability, observation flow, telemetry publication, and software
state only. Real Feeder hardware remains `REAL HARDWARE DEFERRED`; physical
hardware facts remain unknown, and CAN 45-49 remains a planning reservation.

The independent implementation review, paired English and Vietnamese student
guides, bounded documentation repair, independent documentation rereview,
final closure build, and independent final closure review passed. The accepted
gate `PASS_M00_L05_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_LIFECYCLE_RECONCILIATION_AND_FREEZE`
authorized this documentation-only lifecycle freeze. M00_L05 is now
`COMPLETE / FROZEN / READ-ONLY`, active lesson count is `0`, and no M00 lesson
is active. User evidence records primary publication commit
`5709f1d74b3318303bcc56779315b243dd81770b` (`Complete M00_L05 feeder
foundation`) with `HEAD == origin/main`. Publication metadata reconciliation is
complete; later User evidence records metadata publication commit
`1d6fadeec57fbfd3be245746b21d06e58b79518f` (`Record M00_L05 publication
metadata`), remote alignment PASS, and final publication gate
`PASS_M00_L05_FINAL_PUBLICATION_COMPLETE`.

### M00_L06 Controlled Activation — 2026-09-19

The User prepared `M00_L06_FeederCommandOwnership` from final published
M00_L05, removed candidate build artifacts, and supplied a passing baseline:
`BUILD SUCCESSFUL in 40s`, 7 actionable tasks, 6 executed, 1 up-to-date, exit
code `0`. The independent architecture/inheritance audit compared 299 governed
files: 299 were byte-identical, with no missing, added, or changed files. The
accepted audit gate is
`PASS_M00_L06_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`, and
the Architect accepted it before issuing
`PASS_M00_L06_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION`.

M00_L06 is now the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` lesson; active lesson count is `1`. Its one new concept is
scheduler-managed command ownership of the existing Feeder semantic API. The
locked future scope is `RunFeederCommand`, exactly one `FeederSubsystem`
requirement, one `requestFeed()` call from `initialize()`, empty `execute()`,
non-terminating `isFinished()`, unconditional subsystem `stop()` on normal or
interrupted end, and Left Bumper `whileTrue` composition in RobotContainer. No
default Feeder command is authorized.

Implementation is `NOT STARTED` and requires separate authorization. No Java,
test, Constants, CAN-registry, vendordep, Gradle, deploy, hardware, or prior-
lesson change is authorized by activation. `FeederIONoop` remains the only
runtime implementation, real hardware remains deferred, CAN 45-49 remains a
planning reservation only, and M00_L14 through M00_L16 scopes remain protected.

### M00_L06 Final Lifecycle Freeze — 2026-09-20

The preceding controlled-activation section is preserved as historical. The
authorized `RunFeederCommand` and Left Bumper binding implementation completed
within the exact two-file production boundary. The four focused tests, full
clean regression, and bounded Simulation evidence passed. The initial closure
review `HOLD`, three-item bounded documentation/lifecycle repair, later
transition-history `HOLD`, and final bounded transition-history repair are
preserved in the lesson transition guide.

The final Independent Closure Rereview returned `READY_FOR_FREEZE` with no
remaining findings. The Architect accepted that result through
`PASS_M00_L06_INDEPENDENT_CLOSURE_REREVIEW_ACCEPTED` and authorized
`AUTHORIZED_FOR_FREEZE`. M00_L06 is now `COMPLETE / FROZEN / READ-ONLY`, active
lesson count is `0`, and no M00 lesson is active. Evidence remains `THEORY
VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`; `FeederIONoop` remains
the only runtime implementation, and CAN 45-49 remains planning-only.

User-owned publication is pending and no Git commit or push is claimed.
M00_L07 remains `NOT ACTIVE / NOT CREATED`, and M00_L14 through M00_L16 remain
protected future scope.

### M00_L06 Primary Publication and Metadata Reconciliation — 2026-09-20

The preceding lifecycle-freeze section is preserved as the historical
pre-publication state. The accepted gate `PASS_M00_L06_PRIMARY_PUBLICATION`
records the User-owned primary publication commit
`f102a5e662877f8cb49eb63f2cfd888ac356bea4` with subject `Complete M00_L06
Feeder command ownership`. Primary push is `PASS`, and accepted remote evidence
records `HEAD = origin/main = f102a5e662877f8cb49eb63f2cfd888ac356bea4`;
primary remote alignment is `PASS`.

M00_L06 remains `COMPLETE / FROZEN / READ-ONLY`, active lesson count remains
`0`, and no M00 lesson is active. Publication metadata reconciliation is
`COMPLETE / PREPARED FOR USER COMMIT`. The separate metadata Git publication
and final publication verification remain `PENDING`; M00_L06 is not yet
recorded as final `PUBLISHED / VERIFIED`. M00_L07 remains `NOT ACTIVE / NOT
CREATED`, and M00_L14 through M00_L16 remain protected future scope.

### M00_L07 Controlled Activation — 2026-09-20

The preceding M00_L06 publication-metadata section is preserved as historical.
The accepted activation prerequisite now records M00_L06 as `COMPLETE / FROZEN
/ READ-ONLY / PUBLISHED / VERIFIED`. The User prepared
`M00_L07_FlywheelFoundation` from that predecessor, removed candidate generated
artifacts, and supplied a passing untouched-inheritance baseline: `BUILD
SUCCESSFUL in 38s`, 6 actionable tasks, all 6 executed. The accepted
architecture/inheritance audit found 306 of 306 governed files byte-identical,
with zero missing, added, or changed files. The Architect accepted
`PASS_M00_L07_FINAL_DESIGN_LOCK`.

M00_L07 is now the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` lesson; active lesson count is `1`, and the current active M00 lesson is
`M00_L07`. Its one new concept is: Flywheel is one independently owned
rotational-speed mechanism. Controlled Activation changes lifecycle and
documentation identity only. Implementation remains `PENDING SEPARATE
AUTHORIZATION`; no production Java, test Java, command, controller binding,
Constants entry, hardware adapter, or autonomous integration is authorized or
implemented by activation.

The locked future runtime is `FlywheelIONoop` only. CAN 50-54 remains a planning
reservation only, physical Flywheel hardware remains unknown, and real hardware
remains deferred. M00_L08 is `INACTIVE / NOT CREATED`; M00_L08 closed-loop
velocity, M00_L09 ready-at-speed, M00_L14 Shoot Coordination, and M00_L16
autonomous event integration remain protected future scope.

### M00_L07 implementation, verification, and documentation reconciliation — 2026-09-20

The preceding Controlled Activation section is preserved as historical
pre-implementation state. After
`PASS_M00_L07_GOVERNANCE_ADJUDICATION` and
`PASS_M00_L07_INDEPENDENT_ACTIVATION_REVIEW_AFTER_ADJUDICATION`, the exact
bounded implementation was authorized and accepted at
`PASS_M00_L07_IMPLEMENTATION_REPORT_ACCEPTED`. It created the vendor-neutral
Flywheel IO/IOInputs, `FlywheelIONoop`, immutable Observation, subsystem, and
read-only telemetry foundation and integrated it through `RobotContainer` and
`RobotTelemetry`. Six focused test classes were created; no inherited test was
modified.

The initial Independent Static Review accepted production and returned `HOLD`
for four focused-test quality findings. The first bounded test-only repair
closed the request-ordering, negative-infinity, and RobotContainer absence
boundary findings, but static rereview retained one `HOLD` for
comment-sensitive raw-source checks in
`FlywheelArchitectureBoundaryTest.java`. A final single-file repair replaced
those checks with semantic type inspection and comment-free import parsing.
The final review passed at
`PASS_M00_L07_FINAL_INDEPENDENT_STATIC_REREVIEW`.

User evidence records all six focused test classes as `BUILD SUCCESSFUL` and
`FOCUSED TESTS: PASS`. The clean full regression passed at `BUILD SUCCESSFUL
in 36s`, with 7 actionable tasks and all 7 executed. Bounded WPILib Simulation
passed at `PASS_M00_L07_BOUNDED_SIMULATION` for Disabled initial, Teleop
Enabled idle with no Flywheel action, and return to Disabled. Each checkpoint
reported unavailable, disconnected, velocity invalid, `velocityRpm = 0.0`,
and `STOPPED`. Because velocity is invalid, `0.0` is the canonical Noop
invalid representation, not a verified physical zero-speed measurement.

Evidence is `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.
M00_L07 remains the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` lesson with active lesson count `1`; it is not complete, frozen,
read-only, or published. Independent Closure Review is the next gate. M00_L08
remains inactive/uncreated; closed-loop velocity, ready-at-speed, Flywheel
command ownership, shooting coordination, Feeder/Flywheel orchestration,
NamedCommands, and autonomous mechanism integration remain protected future
scope.

### M00_L07 controlled freeze transition — 2026-09-20

The preceding implementation and documentation-reconciliation section remains
the historical pre-freeze state. The Independent Closure Review passed at
`PASS_M00_L07_INDEPENDENT_CLOSURE_REVIEW` with verdict
`CLOSURE_REVIEW_PASS`, recommendation `READY_FOR_FREEZE_AUTHORIZATION`, and
exact remaining findings `NONE`. The Architect accepted the result and
authorized the controlled freeze transition.

M00_L07 is now `COMPLETE / FROZEN / READ-ONLY`. Active lesson count is `0`,
and no M00 lesson is active. Implementation, final static review, focused
tests, clean full regression, bounded Simulation, documentation reconciliation,
and Independent Closure Review remain accepted. Evidence remains `THEORY
VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`; invalid Noop
`velocityRpm = 0.0` remains non-physical evidence. `FlywheelIONoop` remains
the only Flywheel runtime adapter, and CAN 50-54 remains planning-only.

Publication is `PENDING / NOT YET PUBLISHED`. No commit, push, remote
alignment, or publication verification is claimed. M00_L08 remains `INACTIVE /
NOT CREATED`, and all M00_L08, M00_L09, coordination, command-ownership, and
autonomous-integration boundaries remain protected.

### M00_L07 Primary Publication and Metadata Reconciliation — 2026-09-20

The preceding controlled-freeze section is preserved as the historical
pre-publication state. Accepted gate
`PASS_M00_L07_PRIMARY_PUBLICATION_EVIDENCE` records the User-owned primary
publication commit `50e5f440bb0c9d96bdcd57eed533651d8d59ca93` with subject
`Complete M00_L07 Flywheel foundation`. Primary push is `PASS`, and accepted
remote evidence records `HEAD = origin/main =
50e5f440bb0c9d96bdcd57eed533651d8d59ca93`; primary remote alignment is
`PASS`.

M00_L07 remains `COMPLETE / FROZEN / READ-ONLY`. Active lesson count remains
`0`, and the current active M00 lesson remains `NONE`. Publication metadata
reconciliation is `COMPLETE / PREPARED FOR USER COMMIT`. The metadata commit
and push remain `PENDING USER ACTION`, and final publication verification
remains `PENDING`; final `PUBLISHED / VERIFIED` status is not yet claimed.
M00_L08 remains `INACTIVE / NOT CREATED`.

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
| 1.24 | 2026-08-28 | FROZEN | APPROVED: activate the prepared ADR-locked V00_L05 identity as the sole `IN_PROGRESS / EDITABLE` lesson and record the Architect-approved pure pose-candidate Design Lock; implementation remains `NOT STARTED / NOT AUTHORIZED`, predecessor lessons remain frozen, and User-owned Git operations remain pending. |
| 1.25 | 2026-08-28 | FROZEN | APPROVED: record the authorized V00_L05 implementation, test-only Java 17 compatibility and noncommutativity-fixture repairs, API reflection hardening, authoritative User verification PASS, and architecture/documentation PASS; V00_L05 remains `IN_PROGRESS / EDITABLE` pending final closure authorization and User-owned publication. |
| 1.26 | 2026-08-28 | FROZEN | APPROVED: close and freeze V00_L05 as `COMPLETE / FROZEN / READ-ONLY` after final architecture, verification, and documentation PASS; V00_L06 remains inactive/not created, and User Git publication remained pending at that historical closure point. |
| 1.27 | 2026-08-28 | FROZEN | APPROVED: reconcile User-confirmed V00_L05 publication at `6482160`; retain `COMPLETE / FROZEN / READ-ONLY`, distinguish the actual lesson publication from the future metadata-reconciliation commit, and keep V00_L06 inactive. |
| 1.28 | 2026-08-29 | FROZEN | APPROVED: activate the VERIFIED Markdown mirror reading policy while preserving authoritative English PDF precedence, integrity verification, direct-PDF fallback, and poster visual-reference requirements. |
| 1.29 | 2026-08-30 | FROZEN | APPROVED: reconcile User-confirmed V00_L06 publication at `1327bf4` and lesson-local metadata reconciliation at `49c4286`; record V00_L01-L06 as published/frozen, no active V00 lesson, and V00_L07 as a prepared inherited pre-activation candidate. |
| 1.30 | 2026-08-30 | FROZEN | APPROVED: reconcile repository current lifecycle through User-published V00_L07 at `d58bef0` and lesson-local metadata reconciliation at `618dd09`; record no active V00 lesson and keep V00_L08 `NOT STARTED / NOT ACTIVATED / NOT IMPLEMENTED / NOT PUBLISHED`. |
| 1.31 | 2026-08-31 | FROZEN | APPROVED: record the documentation-only exceptional V00_L07 reopen for exactly R1/R2/R3; V00_L07 is the sole `REOPENED / IN_PROGRESS / EDITABLE` lesson, implementation remains unauthorized, V00_L08 remains unactivated, and the original `d58bef0` publication remains historical. |
| 1.32 | 2026-09-07 | FROZEN | APPROVED: reconcile later User-verified Teleop/Autonomous usability, retain the unresolved BL quantitative anomaly as `KNOWN / DEFERRED HARDWARE MAINTENANCE` without a quantitative drivetrain PASS claim, and record V00_L07 as closure-ready while it remains `REOPENED / IN_PROGRESS / EDITABLE` pending final read-only closure review, explicit re-freeze approval, and User-owned corrected repair publication; V00_L08 remains untouched and unactivated. |
| 1.33 | 2026-09-07 | FROZEN | APPROVED: record final V00_L07 closure review PASS and re-freeze the repaired lesson as `COMPLETE / FROZEN / READ-ONLY`; retain historical `d58bef0` as pre-repair provenance, keep corrected publication `PENDING USER PUBLICATION`, record no active V00 lesson, and leave stale unactivated V00_L08 untouched. |
| 1.34 | 2026-09-07 | FROZEN | APPROVED: reconcile User-confirmed corrected V00_L07 publication at `4704cfc`; retain historical `d58bef0`, record `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`, verify HEAD == origin/main, and leave V00_L08 untouched and unactivated. |
| 1.35 | 2026-09-07 | FROZEN | APPROVED: supersede the prospective reconstruction-only procedure for the exact existing V00_L08 candidate with a one-time preservation-based reconciliation; preserve the exact seven-file donor boundary, mandatory checkpoint and fresh verification, and keep L08 unactivated/read-only until post-reconciliation review. |
| 1.36 | 2026-09-09 | FROZEN | APPROVED: record the User-authorized V00_L08 activation and bounded repair scope, preserve the observation-only runtime boundary and private Limelight schema boundary, and keep V00_L09/fusion/drivetrain/autonomous/configuration changes excluded. This entry does not approve an implementation-selected freshness recipe or declare runtime readiness. |
| 1.37 | 2026-09-10 | FROZEN | APPROVED: clarify that the bounded V00_L08 repair does not promote implementation-selected freshness, threshold, recovery, or coherence behavior into global governance and remains verification-gated. |
| 1.38 | 2026-09-10 | FROZEN | APPROVED: record final V00_L08 architecture/closure PASS and freeze the lesson as `COMPLETE / FROZEN / READ-ONLY`; retain H1 and NetworkTables qualifications, keep V00_L09/fusion out of scope, and leave Git publication pending User commit/push. |
| 1.39 | 2026-09-10 | FROZEN | APPROVED: reconcile User-confirmed V00_L08 publication at `f34b210`, record message `Complete V00_L08 real vision adapter integration` and push PASS to `origin/main`, while preserving the frozen lesson state and V00_L09 boundary. |
| 1.40 | 2026-09-11 | FROZEN | APPROVED: perform the documentation-only controlled activation of the prepared ADR-locked V00_L09 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`; preserve V00_L08 frozen protection and keep implementation, verification, closure, and User Git publication pending. |
| 1.41 | 2026-09-12 | FROZEN | APPROVED: reconcile completed V00_L09 implementation, User-verified automated and Simulation evidence, Driver Station / Glass PASS, and the narrow test-only failure-boundary fixture repair; retain L09 as the sole `IN_PROGRESS / EDITABLE` lesson pending real Limelight timing/result, real estimator-fusion, final architecture/documentation closure, freeze, and User publication gates. |
| 1.42 | 2026-09-15 | FROZEN | APPROVED: record final V00_L09 documentation review and explicit `PASS_V00_L09_FINAL_FREEZE_AUTHORIZATION`; transition L09 to `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, preserve V00_L08 frozen protection, and leave User Git commit, push, and publication pending. |
| 1.43 | 2026-09-15 | FROZEN | APPROVED: reconcile User-verified V00_L09 implementation publication at `6548c98`, record `origin/main = 6548c98`, `origin/HEAD = 6548c98`, push `COMPLETE / VERIFIED`, and `PASS_V00_L09_REMOTE_PUBLICATION_VERIFIED`; preserve the distinct metadata-reconciliation commit as `PENDING USER COMMIT`. |
| 1.44 | 2026-09-15 | FROZEN | APPROVED: record V00 final closure through metadata reconciliation `5d36529` and `PASS_V00_MODULE_FINAL_CLOSURE_CONFIRMED`; register the locked 16-lesson M00 ADR and `PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED`, exact M00_L01 identity, predecessor, future destination, baseline command, bilingual/evidence rules, active lesson count `0`, and `NOT ACTIVE / NOT YET CREATED` with no implementation authorization. |
| 1.45 | 2026-09-15 | FROZEN | APPROVED: consume `PASS_M00_L01_FINAL_DESIGN_LOCK` and record documentation-only controlled activation of M00_L01 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`; preserve V00_L09 frozen protection and retain implementation and production-code authorization as `NONE`. |
| 1.46 | 2026-09-15 | FROZEN | APPROVED: reconcile completed bilingual documentation, the preserved Constants-authority HOLD and repair, independent rereview PASS, User final inherited clean build/regression PASS, final closure review PASS, and technical/content readiness PASS while retaining M00_L01 as `IN_PROGRESS / EDITABLE`, active lesson count `1`, freeze authorization pending, and User Git publication pending. |
| 1.47 | 2026-09-15 | FROZEN | APPROVED: consume `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION` and record M00_L01 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`; preserve the documentation-only technical boundary, keep M00_L02 inactive and uncreated, and leave User Git publication and remote verification pending. |
| 1.48 | 2026-09-15 | FROZEN | APPROVED: reconcile User-verified M00_L01 lesson publication at `83907ab` with `PASS_M00_L01_REMOTE_PUBLICATION_VERIFIED`; record `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`, preserve active lesson count `0` and inactive/uncreated M00_L02, and leave the distinct publication-metadata commit, push, and final remote verification pending. |
| 1.49 | 2026-09-16 | FROZEN | APPROVED: consume `PASS_M00_L02_FINAL_DESIGN_LOCK` and record the documentation-only controlled activation of M00_L02 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`; preserve M00_L01 frozen publication, keep all technical implementation unauthorized, and leave student documentation pending separate Architect authorization. |
| 1.50 | 2026-09-16 | FROZEN | APPROVED: reconcile M00_L02 documentation authorization and implementation, preserve the initial documentation HOLD and bounded repair, record independent rereview PASS, User final build/regression PASS, final closure review PASS, and `PASS_M00_L02_DOCUMENTATION_RECONCILED_READY_FOR_INDEPENDENT_REVIEW`; retain `IN_PROGRESS / EDITABLE`, active lesson count `1`, pending independent reconciliation review and freeze authorization, no technical changes, and inactive/uncreated M00_L03. |
| 1.51 | 2026-09-16 | FROZEN | APPROVED: consume `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION` after independent reconciliation review PASS; record M00_L02 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, preserve the documentation-only technical boundary and M00_L01 frozen publication, leave M00_L03 inactive/uncreated, and retain User Git publication and publication metadata reconciliation as pending. |
| 1.52 | 2026-09-16 | FROZEN | APPROVED: reconcile User-confirmed M00_L02 primary publication at `65a92a4a5806fd5134e0114e851c4e4cc093c58e` and primary push PASS; retain `COMPLETE / FROZEN / READ-ONLY`, record publication metadata reconciliation complete, and leave the separate metadata commit, metadata push, final remote verification, and final publication completion pending. |
| 1.53 | 2026-09-16 | FROZEN | APPROVED: consume `PASS_M00_L03_FINAL_DESIGN_LOCK` and record the prepared M00_L03 candidate as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`; preserve the lesson-local AGENTS script-defect history, M00_L02 final publication, the vendor-neutral Intake Foundation boundary, and all implementation authorizations as separately pending. |
| 1.54 | 2026-09-16 | FROZEN | APPROVED: reconcile M00_L03 production/test authorization, completed bounded Intake implementation, preserved initial focused-test HOLD and minimal test-only repair, focused retest PASS, full clean regression PASS, bounded Simulation PASS, independent implementation review PASS, and authorized bilingual student-documentation implementation; retain M00_L03 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`, independent documentation review and all closure/freeze/publication gates pending, and M00_L04 inactive/uncreated. |
| 1.55 | 2026-09-17 | FROZEN | APPROVED: record the M00_L03 transition-guide terminology HOLD and one-line repair, independent documentation rereview PASS, final User closure build PASS, final closure review PASS, and completed documentation/lifecycle reconciliation; retain M00_L03 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`, freeze authorization and publication pending, and M00_L04 inactive/uncreated. |
| 1.56 | 2026-09-17 | FROZEN | APPROVED: consume `PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION` after independent reconciliation review PASS; record M00_L03 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, preserve all accepted evidence and protected technical content, keep M00_L04 inactive/uncreated, and leave User-owned publication pending. |
| 1.57 | 2026-09-17 | FROZEN | APPROVED: reconcile accepted M00_L03 primary publication at `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`, primary push and remote alignment PASS; record publication metadata reconciliation complete while leaving metadata Git publication and final publication verification pending; preserve frozen M00_L03 and inactive/uncreated M00_L04. |
| 1.58 | 2026-09-18 | FROZEN | APPROVED: record M00_L03 final two-commit publication as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`; consume `PASS_M00_L04_FINAL_DESIGN_LOCK` after preparation and architecture/inheritance PASS; activate M00_L04 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`, locked Right Bumper `whileTrue` command-ownership scope, implementation authorization pending, and M00_L05 inactive/uncreated. |
| 1.59 | 2026-09-18 | FROZEN | APPROVED: reconcile the authorized M00_L04 command/binding implementation, 14/14 focused tests, full clean regression, bounded Simulation and Driver Station evidence, independent implementation review PASS, and paired student-guide creation; retain M00_L04 as the sole `IN_PROGRESS / EDITABLE` lesson pending independent documentation review and later closure/freeze/publication gates, with M00_L05 inactive/uncreated. |
| 1.60 | 2026-09-18 | FROZEN | APPROVED: consume `PASS_M00_L04_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_FINAL_LIFECYCLE_RECONCILIATION_AND_FREEZE_PREPARATION`; preserve both resolved HOLD histories, record M00_L04 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, retain bounded software evidence and `REAL HARDWARE DEFERRED`, keep M00_L05 inactive/uncreated, and leave User-owned Git publication pending. |
| 1.61 | 2026-09-18 | FROZEN | APPROVED: reconcile accepted M00_L04 primary publication at `5c86be3` with primary remote alignment PASS; record publication metadata reconciliation complete in the working tree while leaving the separate metadata commit/push and final publication verification pending; preserve active lesson count `0`, deferred real hardware, and inactive/uncreated M00_L05. |
| 1.62 | 2026-09-18 | FROZEN | APPROVED: record M00_L04 final two-commit publication at primary `5c86be3` and metadata `24738e6`; consume `PASS_M00_L05_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION`; activate M00_L05 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`, FeederIONoop-only foundation scope, implementation authorization pending, deferred real hardware, and M00_L06 inactive/uncreated. |
| 1.63 | 2026-09-19 | FROZEN | APPROVED: reconcile the authorized M00_L05 Feeder implementation, both bounded test-defect repairs, focused-test PASS, 682-test full clean regression PASS, bounded Simulation and HALSIM Driver Station evidence, independent implementation review PASS, and paired student-guide implementation; retain M00_L05 as the sole `IN_PROGRESS / EDITABLE` lesson pending independent documentation review and later closure/freeze/publication gates, with M00_L06 inactive/uncreated. |
| 1.64 | 2026-09-19 | FROZEN | APPROVED: consume `PASS_M00_L05_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_LIFECYCLE_RECONCILIATION_AND_FREEZE`; preserve the resolved documentation HOLD and repair history, independent documentation rereview PASS, final closure build PASS, and final closure review PASS; record M00_L05 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, keep M00_L06 inactive/uncreated, and leave all User-owned publication stages pending. |
| 1.65 | 2026-09-19 | FROZEN | APPROVED: reconcile User-owned M00_L05 primary publication at `5709f1d74b3318303bcc56779315b243dd81770b` with subject `Complete M00_L05 feeder foundation`, push and primary remote alignment PASS; record publication metadata reconciliation complete while leaving metadata publication and final publication verification pending; preserve active lesson count `0` and inactive/uncreated M00_L06. |
| 1.66 | 2026-09-19 | FROZEN | APPROVED: record final M00_L05 metadata publication at `1d6fadeec57fbfd3be245746b21d06e58b79518f` and `PASS_M00_L05_FINAL_PUBLICATION_COMPLETE`; consume `PASS_M00_L06_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION`; activate M00_L06 as the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK` lesson with active lesson count `1`, locked `RunFeederCommand` plus Left Bumper `whileTrue` scope, implementation pending separate authorization, deferred real hardware, and protected M00_L14-L16 scope. |
| 1.67 | 2026-09-20 | FROZEN | APPROVED: consume `PASS_M00_L06_INDEPENDENT_CLOSURE_REREVIEW_ACCEPTED` and `AUTHORIZED_FOR_FREEZE` after final independent closure rereview `READY_FOR_FREEZE` with no remaining findings; record M00_L06 as `COMPLETE / FROZEN / READ-ONLY`, active lesson count `0`, no active M00 lesson, publication pending, deferred real hardware, and inactive/uncreated M00_L07. |
| 1.68 | 2026-09-20 | FROZEN | APPROVED: reconcile accepted M00_L06 primary publication at `f102a5e662877f8cb49eb63f2cfd888ac356bea4` with subject `Complete M00_L06 Feeder command ownership`, primary push PASS, and remote alignment PASS; record publication metadata reconciliation complete/prepared for User commit while leaving metadata Git publication and final publication verification pending; preserve frozen M00_L06 and inactive/uncreated M00_L07. |
| 1.69 | 2026-09-20 | FROZEN | APPROVED: consume `PASS_M00_L07_FINAL_DESIGN_LOCK` after accepted M00_L06 final publication, M00_L07 preparation, 306-of-306 byte-identical inheritance audit, and inherited baseline PASS; activate M00_L07 as the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK` lesson with active lesson count `1`, implementation pending separate authorization, `FlywheelIONoop`-only future runtime, CAN 50-54 planning-only, deferred real hardware, and inactive/uncreated M00_L08. |
| 1.70 | 2026-09-20 | FROZEN | APPROVED: reconcile accepted M00_L07 governance adjudication, activation rereview, bounded implementation, preserved static-review HOLD and two-stage test-only repair history, final static rereview PASS, User focused-test PASS, clean full-regression PASS, bounded Simulation PASS, and documentation completion; retain M00_L07 as the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK` lesson with active lesson count `1`, evidence `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`, Independent Closure Review next, freeze/publication unclaimed, and M00_L08 inactive/uncreated. |
| 1.71 | 2026-09-20 | FROZEN | APPROVED: consume `PASS_M00_L07_INDEPENDENT_CLOSURE_REVIEW` after `CLOSURE_REVIEW_PASS`, `READY_FOR_FREEZE_AUTHORIZATION`, and no remaining findings; record M00_L07 as `COMPLETE / FROZEN / READ-ONLY`, active lesson count `0`, no active M00 lesson, preserved theory/Simulation/deferred-hardware evidence, publication pending/not yet published, and M00_L08 inactive/uncreated. |
| 1.72 | 2026-09-20 | FROZEN | APPROVED: reconcile accepted M00_L07 primary publication at `50e5f440bb0c9d96bdcd57eed533651d8d59ca93` with subject `Complete M00_L07 Flywheel foundation`, primary push PASS, and remote alignment PASS; record publication metadata reconciliation complete/prepared for User commit while leaving the metadata commit/push and final publication verification pending; preserve frozen M00_L07, active lesson count `0`, no active M00 lesson, and inactive/uncreated M00_L08. |
---

### M00_L08 Controlled Activation — 2026-09-20

The accepted M00_L08 preparation baseline, Architecture / Inheritance Audit,
and Final Design Lock are recorded as PASS. The User-prepared candidate was
copied from canonical M00_L07, generated artifacts were removed, and the
untouched-copy baseline passed. Inheritance remains 103/103 production and
96/96 test files byte-identical, with the accepted overall non-generated
comparison of 711/711 identical.

M00_L08 is now the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` lesson. Active lesson count is `1`, and the current active M00 lesson is
`M00_L08`. Its one new concept is vendor-neutral Flywheel closed-loop velocity
control through one validated semantic RPM request while preserving
measurement-only Observation and explicit safe stop.

The locked semantic request is `void requestVelocity(double targetRpm)` in
finite, nonnegative Flywheel mechanism RPM. Exactly `0.0` is the canonical
safe-stop request. Invalid values fail closed; `requestSpin()` is
`REMOVED / SUPERSEDED`; requested states are `STOPPED` and
`VELOCITY_REQUESTED`. Observation remains measurement-only. Runtime remains
`FlywheelIONoop` only, with no physical adapter, hardware configuration, gain,
target RPM, or CAN assignment authorized.

The Architect Simulation clarification is preserved: test doubles and focused
unit tests are not WPILib runtime Simulation evidence. Future bounded runtime
Simulation may claim only deterministic Noop composition, telemetry and
measurement state, STOPPED idle behavior, no automatic Teleop request, and
Disabled → Teleop → Disabled persistence. Physical regulation, convergence,
tuning, sensor fidelity, CAN, RPM accuracy, and physical stop behavior remain
unverified; real hardware remains deferred.

Implementation is `PENDING SEPARATE AUTHORIZATION`; verification is pending.
Only the four locked Flywheel production files and six named inherited
Flywheel-focused tests may later be modified. M00_L07 remains
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`. M00_L09 remains
`INACTIVE / NOT CREATED`. This activation changed lifecycle and documentation
only and performed no production or test implementation.

### M00_L08 Controlled Activation Documentation Repair — 2026-09-20

The Independent Activation Review HOLD was limited to documentation
completeness. The following clarification is binding for the future,
separately authorized implementation; it does not implement production code,
tests, or runtime verification.

The exact future `FlywheelIO` contract is:

- `FlywheelIOInputs` fields: `boolean available`, `boolean connected`,
  `boolean velocityValid`, and `double velocityRpm`;
- `void updateInputs(FlywheelIOInputs inputs)`;
- `void requestVelocity(double targetRpm)`; and
- `void stop()`.

`requestSpin()` is `REMOVED / SUPERSEDED`. No vendor type, vendor control
object, gain parameter, or hardware-configuration parameter is permitted.

For a valid positive request, future behavior is ordered as: validate,
record `requestedVelocityRpm`, set `VELOCITY_REQUESTED`, replace the immutable
Observation, and forward exactly one `flywheelIO.requestVelocity(targetRpm)`.
If forwarding throws, the recorded target, state, and Observation remain,
the exception propagates, no rollback occurs, and `periodic()` does not retry.
For `0.0`, the target is zero, the state is `STOPPED`, the Observation is
replaced, and exactly one `flywheelIO.stop()` is forwarded.

Invalid values are NaN, positive infinity, negative infinity, and negative
finite values. Invalid handling records zero and `STOPPED`, replaces the
Observation, attempts `flywheelIO.stop()`, then throws
`IllegalArgumentException`; no invalid target is forwarded. If stop throws,
the IllegalArgumentException remains primary with the stop failure suppressed,
and zero/STOPPED software intent remains recorded.

Explicit stop sets zero, sets `STOPPED`, replaces the Observation, and calls
`flywheelIO.stop()` unconditionally. If it throws, zero/STOPPED state and the
updated Observation remain and the exception propagates. There is no rollback,
automatic restart, or periodic output reissue.

`FlywheelIONoop.updateInputs()` must set exactly
`available = false`, `connected = false`, `velocityValid = false`, and
`velocityRpm = 0.0`. Its `requestVelocity(double)` and `stop()` are safe
deterministic no-ops with no convergence or physical-velocity model.
`velocityRpm = 0.0` while invalid is not measured physical zero RPM.

`RobotContainer` remains unchanged and its exact runtime composition remains
`new FlywheelSubsystem(new FlywheelIONoop())`. It adds no Flywheel command,
binding, default command, direct `requestVelocity(...)`, direct `stop()`,
autonomous registration, NamedCommands, event markers, Feeder/Flywheel
coordination, physical hardware selection, or Flywheel-specific
`RobotBase.isReal()` branch.

Future test reconciliation is limited to these six existing files:

1. `src/test/java/frc/robot/FlywheelArchitectureBoundaryTest.java`
2. `src/test/java/frc/robot/io/flywheel/FlywheelIONoopTest.java`
3. `src/test/java/frc/robot/observation/flywheel/FlywheelObservationTest.java`
4. `src/test/java/frc/robot/subsystems/FlywheelSubsystemTest.java`
5. `src/test/java/frc/robot/telemetry/flywheel/FlywheelTelemetryFacadeTest.java`
6. `src/test/java/frc/robot/RobotContainerFlywheelCompositionTest.java`

No new test file or unrelated inherited test change is authorized. M00_L08
remains `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`, active
lesson count `1`, implementation and verification pending, and M00_L09
inactive/not created.

### M00_L08 Post-Verification Documentation Reconciliation — 2026-09-20

The accepted implementation and User-owned verification evidence are now
reconciled into the lifecycle record. This entry is documentation-only and
does not authorize closure, freeze, publication, or M00_L09 activation.

The final implemented concept is exactly one new concept: vendor-neutral
Flywheel closed-loop velocity control through one validated semantic
mechanism-RPM request while preserving measurement-only Observation and
explicit safe stop. The final API is
`void requestVelocity(double targetRpm)`; `requestSpin()` is
`REMOVED / SUPERSEDED`; requested states are exactly `STOPPED` and
`VELOCITY_REQUESTED`.

The final `FlywheelIOInputs` fields are exactly `boolean available`,
`boolean connected`, `boolean velocityValid`, and `double velocityRpm`. The
exact methods are `void updateInputs(FlywheelIOInputs inputs)`,
`void requestVelocity(double targetRpm)`, and `void stop()`. No vendor types,
gains, vendor control object, or hardware-specific configuration is present.
Valid targets are finite, nonnegative mechanism RPM. A positive finite target
records the target, enters `VELOCITY_REQUESTED`, replaces the immutable
Observation, and forwards exactly one IO request. `+0.0` and `-0.0` are
canonical safe-stop behavior. NaN, positive infinity, negative infinity, and
negative finite values fail closed to zero/`STOPPED`, update Observation,
attempt IO stop, throw `IllegalArgumentException`, and suppress any stop
failure onto that primary exception; no invalid value is forwarded.

Safe-stop ordering is: set `requestedVelocityRpm = 0.0`, set
`requestedState = STOPPED`, update immutable Observation, then unconditionally
call `flywheelIO.stop()`. If IO stop throws, zero/`STOPPED`/Observation remain
recorded, the original exception propagates, and there is no rollback or
automatic restart. `periodic()` only calls `updateInputs()` and rebuilds the
immutable Observation; it does not request velocity, stop automatically,
reissue a target, implement PID, or implement readiness. Observation remains
exactly `available`, `connected`, `velocityValid`, `velocityRpm`, and
`requestedState`; target RPM remains subsystem control intent and is not
Observation or telemetry data.

Runtime remains `FlywheelIONoop` only. It deterministically reports
`available=false`, `connected=false`, `velocityValid=false`, and
`velocityRpm=0.0`; request and stop are deterministic no-ops with no
convergence or hardware model. CAN 50–54 is planning reservation only and
real hardware remains deferred.

Relative to frozen M00_L07, production integrity is `Compared: 103`,
`Byte-identical: 99`, `Changed: 4`, `Missing: 0`, `Added: 0`. The changed
production files are exactly `src/main/java/frc/robot/io/flywheel/FlywheelIO.java`,
`src/main/java/frc/robot/io/flywheel/FlywheelIONoop.java`,
`src/main/java/frc/robot/observation/flywheel/FlywheelObservation.java`, and
`src/main/java/frc/robot/subsystems/FlywheelSubsystem.java`; no production
file was added. Test integrity is `Compared: 96`, `Byte-identical: 90`,
`Changed: 6`, `Missing: 0`, `Added: 0`. The changed tests are exactly
`src/test/java/frc/robot/FlywheelArchitectureBoundaryTest.java`,
`src/test/java/frc/robot/io/flywheel/FlywheelIONoopTest.java`,
`src/test/java/frc/robot/observation/flywheel/FlywheelObservationTest.java`,
`src/test/java/frc/robot/subsystems/FlywheelSubsystemTest.java`,
`src/test/java/frc/robot/telemetry/flywheel/FlywheelTelemetryFacadeTest.java`,
and `src/test/java/frc/robot/RobotContainerFlywheelCompositionTest.java`; no
test file was added. `Constants.java`, `RobotContainer.java`,
`FlywheelTelemetryFacade.java`, `RobotTelemetry.java`, Gradle, vendordeps,
deploy, and the frozen M00_L07 predecessor remain unchanged.

The static-review chronology is preserved: the initial Independent Static
Review was `HOLD` for (1) stale `requestedVelocityRpm` after stop, (2) weak
Noop post-request measurement assertions, (3) missing explicit negative-zero
coverage, and (4) brittle regex comment stripping in the structural test.
The bounded repair is `COMPLETE`; the final Independent Static Re-review is
`PASS`, gate `PASS_M00_L08_FINAL_INDEPENDENT_STATIC_REREVIEW`.

Accepted User focused-test evidence is: six authorized focused test classes,
`BUILD SUCCESSFUL in 16s`, `4 actionable tasks: 3 executed, 1 up-to-date`,
and `FOCUSED TESTS: PASS` (`PASS_M00_L08_USER_FOCUSED_TESTS`). Accepted clean
regression evidence is `BUILD SUCCESSFUL in 26s`, `7 actionable tasks: 7
executed`, and `CLEAN REGRESSION: PASS`
(`PASS_M00_L08_CLEAN_FULL_REGRESSION`).

Accepted bounded WPILib Simulation evidence is:

1. `PASS_M00_L08_SIMULATION_CHECKPOINT_1_DISABLED`: Disabled; Available=false,
   Connected=false, RequestedState=STOPPED, VelocityRpm=0.0,
   VelocityValid=false.
2. `PASS_M00_L08_SIMULATION_CHECKPOINT_2_TELEOP_IDLE`: Teleoperated enabled
   with no driver action; Robot Enabled=Yes; Available=false,
   Connected=false, RequestedState=STOPPED, VelocityRpm=0.0,
   VelocityValid=false.
3. `PASS_M00_L08_SIMULATION_CHECKPOINT_3_DISABLED`: returned Disabled; FMS
   Robot Enabled=No; Available=false, Connected=false,
   RequestedState=STOPPED, VelocityRpm=0.0, VelocityValid=false.

Overall gate is `PASS_M00_L08_BOUNDED_SIMULATION`. `VelocityRpm=0.0` while
`VelocityValid=false` is the canonical invalid-Noop representation, not
measured physical zero RPM. Simulation verifies only Noop composition,
telemetry presence, deterministic unavailable/disconnected/invalid state,
STOPPED idle behavior, no automatic Teleop request, and Disabled → Teleop →
Disabled persistence. It does not verify runtime exercise of
`requestVelocity()`, physical control, convergence, gains, feedforward,
sensor fidelity, RPM accuracy, direction, CAN, or physical stop behavior.
In particular, **requestVelocity runtime exercise was NOT claimed**; focused
tests, not runtime Simulation, verified request semantics.

Evidence classification is exactly `THEORY VERIFIED`, `SIMULATION VERIFIED`,
and `REAL HARDWARE DEFERRED`. M00_L08 remains
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`, active lesson
count `1`, current active M00 lesson `M00_L08`, implementation `COMPLETE`,
Independent Static Re-review `PASS`, focused tests `PASS`, clean regression
`PASS`, and bounded Simulation `PASS`. Independent Closure Review is
`PENDING`; Freeze is `NOT AUTHORIZED`; Publication is `NOT AUTHORIZED`.
M00_L09 Flywheel Ready-at-Speed remains `INACTIVE / NOT CREATED`, and later
command, shooting, feeder coordination, autonomous mechanism, readiness,
convergence, tolerance, dwell, and debounce scope remains protected.

### M00_L08 Controlled Freeze Transition — 2026-09-20

The accepted `PASS_M00_L08_INDEPENDENT_CLOSURE_REVIEW` verdict is
`READY_FOR_FREEZE_AUTHORIZATION`, and the Architect decision is `FREEZE
AUTHORIZED`. The controlled freeze transition changes lifecycle state only;
it does not change production source, tests, configuration, dependencies,
deployment content, verification evidence, or publication state.

M00_L08 is now exactly:

```text
STATUS: COMPLETE
ACTIVE STATE: COMPLETE / FROZEN / READ-ONLY
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
INDEPENDENT CLOSURE REVIEW: PASS
FREEZE: COMPLETE / FROZEN / READ-ONLY
PUBLICATION: PENDING / NOT YET PUBLISHED
FINAL PUBLICATION VERIFICATION: NOT YET PERFORMED
M00_L09: INACTIVE / NOT CREATED
```

The final implemented concept remains exactly one vendor-neutral Flywheel
closed-loop velocity concept through `void requestVelocity(double targetRpm)`;
`requestSpin()` is `REMOVED / SUPERSEDED`; requested states remain
`STOPPED` and `VELOCITY_REQUESTED`; and the exact IO, validation, safe-stop,
Observation, periodic, Noop, telemetry, and RobotContainer boundaries remain
unchanged. Evidence remains exactly `THEORY VERIFIED`, `SIMULATION VERIFIED`,
and `REAL HARDWARE DEFERRED`.

The accepted integrity and verification evidence remains unchanged:
production `103 / 99 / 4 / 0 / 0`, tests `96 / 90 / 6 / 0 / 0`, final static
re-review PASS, focused tests PASS, clean regression PASS, and bounded
Simulation PASS. Simulation remains Noop/lifecycle evidence only; physical
Flywheel behavior and runtime `requestVelocity` exercise are not claimed.

M00_L07 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED` under
the accepted primary publication `50e5f440bb0c9d96bdcd57eed533651d8d59ca93`,
metadata publication `62199c3ecd1ac7940e188dbef3d28de784c8da2c`, and final
`PUBLICATION_VERIFIED` evidence. M00_L07 is not modified. M00_L09 remains
inactive and uncreated; no later lesson or mechanism scope is activated.

### M00_L08 Primary Publication and Metadata Reconciliation — 2026-09-20

The accepted gate `PASS_M00_L08_PRIMARY_PUBLICATION` records the User-owned
primary publication of the frozen lesson. M00_L08 remains exactly
`COMPLETE / FROZEN / READ-ONLY`. The primary publication commit is
`5daecd970ff95fb906d6de9d5bc22a5cb094877d` with subject
`Complete M00_L08 Flywheel closed-loop velocity`. Primary push is `PASS`, and
local HEAD and `origin/main` both align to that same SHA; primary remote
alignment is `PASS`.

The publication metadata reconciliation is now the next bounded publication
stage and remains `PENDING METADATA COMMIT`. No metadata commit SHA, metadata
push, or final `PUBLICATION_VERIFIED` verdict is claimed. Final independent
publication verification remains `PENDING / NOT YET PERFORMED`. Active lesson
count remains `0`, the current active M00 lesson remains `NONE`, and M00_L09
remains `INACTIVE / NOT CREATED`.

This record follows the resolved Historical Snapshot Model: the frozen
lesson-local snapshot is preserved unchanged, including historical pending
publication wording; no third publication commit is required merely to record
later independent verification. Evidence remains exactly `THEORY VERIFIED`,
`SIMULATION VERIFIED`, and `REAL HARDWARE DEFERRED`. The bounded Simulation did
not claim runtime `requestVelocity` exercise or physical Flywheel behavior.

### M00_L09 Controlled Activation — 2026-09-21

The canonical M00_L08 predecessor is consumed as
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`. The accepted
preparation baseline is `PASS_M00_L09_PREPARATION_BASELINE` with
`BUILD SUCCESSFUL in 34s` and six actionable tasks, all six executed. The
accepted architecture and design gates are
`PASS_M00_L09_ARCHITECTURE_INHERITANCE_AUDIT` and
`PASS_M00_L09_FINAL_DESIGN_LOCK`, verdict `READY_FOR_CONTROLLED_ACTIVATION`.

Controlled Activation is complete for exactly one new concept: vendor-neutral
instantaneous Flywheel Ready-at-Speed classification. `FlywheelSubsystem`
owns one private deterministic side-effect-free helper; the immutable
Observation adds exactly `readyAtSpeed`; telemetry adds exactly `ReadyAtSpeed`;
IO, Noop, RobotTelemetry, RobotContainer, commands, coordination, autonomous,
and hardware remain outside scope. Readiness is true only for positive finite
velocity intent, valid connected measurement, finite measurement, and
inclusive symmetric error within
`Constants.FlywheelConstants.kReadyAtSpeedToleranceRpm`, exactly `50.0`
mechanism RPM. This is a provisional software-policy acceptance tolerance,
not hardware-tuned and not real-robot validated. Dwell, debounce, hysteresis,
history, state-machine policy, and automatic action are excluded.

M00_L09 is now the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` lesson; Active Lesson Count is `1`, Current Active M00 Lesson is
`M00_L09`, implementation is `NOT STARTED`, and Independent Activation Review
is `PENDING`. M00_L10 is `INACTIVE / NOT CREATED`. No production or test
source was changed, no build/test/Simulation/closure/freeze/publication/Git
evidence is claimed, and M00_L08 remains untouched.

### M00_L09 Activation Documentation Repair — 2026-09-21

The accepted Architect gate is `PASS_M00_L09_CONTROLLED_ACTIVATION`. The
bounded Controlled Activation engineer verdict is
`CONTROLLED_ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`.
These are distinct and both are recorded. The current review remains
`HOLD_M00_L09_INDEPENDENT_ACTIVATION_REVIEW` pending independent re-review.

The canonical external M00_L08 predecessor state is
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`. Its primary
publication is `5daecd970ff95fb906d6de9d5bc22a5cb094877d` (subject
`Complete M00_L08 Flywheel closed-loop velocity`); its metadata publication is
`a76dc33c2b485b4988e7058cbfed0fa3362cc560`, whose parent is the primary; the
final remote-aligned published HEAD is the metadata SHA and the independent
verdict is `PUBLICATION_VERIFIED`. No third publication commit was required.
The two-commit Historical Snapshot Model is PASS. Frozen M00_L08 local records
may retain historical pending-publication wording; that wording is not the
canonical external lifecycle and the frozen lesson was not edited.

The M00_L09 Design Lock is preserved exactly. The readiness truth rule remains
the positive finite requested target, available/connected/valid finite
measurement, and inclusive `Math.abs(measured - target) <=
Constants.FlywheelConstants.kReadyAtSpeedToleranceRpm` comparison with the
provisional `50.0` RPM policy tolerance. A measured velocity of `0.0` RPM does
not, by itself, establish Ready-at-Speed; this is clarification only and is not
an additional formula condition. Ready-at-Speed remains observation-only and
cannot feed a game piece, run Feeder, fire, stage, stop Flywheel, schedule a
command, advance another subsystem, or trigger autonomous behavior.

The future telemetry contract is exactly `Available`, `Connected`,
`VelocityValid`, `VelocityRpm`, `RequestedState`, and `ReadyAtSpeed`; only
`ReadyAtSpeed` is new, sourced from `FlywheelObservation.readyAtSpeed()`.
`TargetRpm`, `VelocityErrorRpm`, `ToleranceRpm`, `Dwell`, `Debounce`, and
`ReadinessDuration` are excluded, and `RobotTelemetry.java` remains unchanged
and read-only. A positive request forwarding exception preserves the new
target/state/derived Observation, propagates the original IO exception
unchanged, and performs no rollback, retry, or readiness-specific masking.
Ready-at-Speed classifies cached requested intent plus cached measurement; it
does not certify successful delivery of the newest IO request.

The eventual evidence plan is `THEORY VERIFIED`, `SIMULATION VERIFIED`, and
`REAL HARDWARE DEFERRED`; current M00_L09 verification remains PENDING. The
full hardware boundary remains unknown/deferred (motor/controller, vendor, CAN
ID/bus, count/topology, sensor, ratio, inversion/direction, current/voltage
limits, neutral/ramping, physical target/max RPM, hardware tolerance, PID/PIDF,
feedforward, convergence, and real-hardware readiness). CAN 50–54 is planning
reservation only and 50.0 RPM is provisional software policy, not a physical
threshold. Production/test boundaries and the M00_L01–M00_L16 roadmap remain
unchanged; M00_L10 is inactive/not created.

### M00_L09 Implementation, Verification, and Documentation Reconciliation — 2026-09-21

The canonical predecessor M00_L08 remains `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED / VERIFIED` with primary publication
`5daecd970ff95fb906d6de9d5bc22a5cb094877d` and metadata publication
`a76dc33c2b485b4988e7058cbfed0fa3362cc560`. This reconciliation did not edit
M00_L08.

M00_L09 implemented exactly one locked concept: instantaneous, immutable,
vendor-neutral Flywheel Ready-at-Speed classification. The positive finite
requested target, available/connected/valid finite measurement, and inclusive
`Math.abs(measured - requested) <=
Constants.FlywheelConstants.kReadyAtSpeedToleranceRpm` rule remain the exact
predicate; the 50 RPM tolerance remains provisional software policy only.
The implementation gate `PASS_M00_L09_IMPLEMENTATION_REPORT_ACCEPTED` was
accepted. The initial Independent Static Review `HOLD` is preserved as
history; the bounded repair passed and the final independent static re-review
passed.

The implementation integrity record is production `Compared=103,
ByteIdentical=99, Changed=4, Missing=0, Added=0`, with changes limited to
`Constants.java`, `FlywheelObservation.java`, `FlywheelSubsystem.java`, and
`FlywheelTelemetryFacade.java`. Tests are `Compared=96, ByteIdentical=92,
Changed=4, Missing=0, Added=0`, with changes limited to the four authorized
Ready-at-Speed test files. Deployment/configuration is `Compared=4,
ByteIdentical=4, Changed=0, Missing=0, Added=0`; no new files or unrelated
changes were introduced. IO, Noop, RobotTelemetry, RobotContainer, commands,
autonomous, configuration, and frozen lessons remain protected.

User evidence accepted the focused gate
`PASS_M00_L09_USER_FOCUSED_TESTS` (`BUILD SUCCESSFUL in 22s`; four actionable
tasks, three executed and one up-to-date) and the clean gate
`PASS_M00_L09_CLEAN_FULL_REGRESSION` (`BUILD SUCCESSFUL`; seven actionable
tasks, all seven executed). User Simulation evidence accepted
`PASS_M00_L09_SIMULATION_CHECKPOINT_1_DISABLED`,
`PASS_M00_L09_SIMULATION_CHECKPOINT_2_TELEOP_IDLE`,
`PASS_M00_L09_SIMULATION_CHECKPOINT_3_DISABLED`, and
`PASS_M00_L09_BOUNDED_SIMULATION`. The three checkpoints retained
`Available=false`, `Connected=false`, `ReadyAtSpeed=false`,
`RequestedState=STOPPED`, `VelocityRpm=0.0`, and `VelocityValid=false`.
Simulation is bounded to Noop composition, read-only telemetry, fail-safe
idle, no automatic request, no readiness-triggered actuation, and
Disabled→Teleop→Disabled persistence. It does not claim runtime 50-RPM
boundaries, physical convergence, sensor fidelity, CAN behavior, or physical
hardware.

Evidence classification is exactly `THEORY VERIFIED`, `SIMULATION VERIFIED`,
`REAL HARDWARE DEFERRED`. Hardware readiness, CAN 50–54, topology, sensor,
gearing, limits, tuning, convergence, and physical thresholds remain unknown
or deferred; CAN 50–54 is planning only. M00_L09 remains
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`; implementation,
static review, focused tests, clean regression, bounded Simulation, and
Documentation Reconciliation are complete. Independent Closure Review remains
pending, Freeze is not authorized, and Publication is not authorized. Active
Lesson Count is `1`, current active M00 lesson is M00_L09, and M00_L10 is
inactive/not created.

### M00_L09 Controlled Freeze Transition — 2026-09-21

The final independent closure re-review passed with
`PASS_M00_L09_FINAL_INDEPENDENT_CLOSURE_REVIEW`, and Architect freeze
authorization is accepted. M00_L09 is now `COMPLETE / FROZEN / READ-ONLY`.
The technical contract remains unchanged: one vendor-neutral instantaneous
Ready-at-Speed concept, the immutable six-field Flywheel Observation, the
inclusive symmetric 50.0 RPM provisional software-policy tolerance, unchanged
IO/Noop/RobotTelemetry/RobotContainer boundaries, and no automatic action.

Production integrity remains `103 / 99 / 4 / 0 / 0`; test integrity remains
`96 / 92 / 4 / 0 / 0`; full deploy/config/support remains
`24 / 24 / 0 / 0 / 0`. Focused evidence remains one invocation selecting all
six classes with `BUILD SUCCESSFUL in 22s`, four actionable tasks, three
executed and one up-to-date. Clean regression, all three Simulation
checkpoints, bounded Simulation, and the exact evidence classification remain
accepted. Real hardware remains deferred and CAN 50–54 remains planning only.

Independent Freeze Review is `PENDING`. Publication is `PENDING / NOT YET
PUBLISHED`; no publication SHA or Git event is claimed. Active Lesson Count is
`0`, Current Active M00 Lesson is `NONE`, and M00_L10 remains
`INACTIVE / NOT CREATED`. The freeze chronology stops before Independent Freeze
Review, publication, and successor activation.

### M00_L09 Primary Publication and Metadata Reconciliation — 2026-09-21

The accepted Independent Freeze Review gate is
`PASS_M00_L09_INDEPENDENT_FREEZE_REVIEW`. M00_L09 remains exactly
`COMPLETE / FROZEN / READ-ONLY`; its frozen lesson-local tree is unchanged.
The accepted primary snapshot gate is
`PASS_M00_L09_PRIMARY_FROZEN_SNAPSHOT_COMMIT`.

Primary publication identity:

- SHA: `3c822a1e3956850c9d0ba9954c5b163d83b801b9`
- Subject: `Complete M00_L09 Flywheel ready-at-speed`

The primary frozen snapshot commit exists locally. It has not been pushed, so
no primary remote alignment, origin state, or remote publication verification
is claimed. The canonical publication phase is `PENDING METADATA COMMIT`.
No metadata commit SHA is available, no metadata push has occurred, and no
`PUBLICATION_VERIFIED` verdict is claimed.

This follows the established two-commit Historical Snapshot model: frozen
lesson snapshot primary commit, repository-level metadata commit, and later
external final publication verification. No third commit is required merely to
record final verification. Frozen lesson-local pending wording remains
historical and was not rewritten.

Evidence remains exactly `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL
HARDWARE DEFERRED`. Production integrity is `103 / 99 / 4 / 0 / 0`, test
integrity is `96 / 92 / 4 / 0 / 0`, and full deploy/config/support integrity is
`24 / 24 / 0 / 0 / 0`. Active Lesson Count remains `0`, the current active M00
lesson remains `NONE`, and M00_L10 remains `INACTIVE / NOT CREATED`. The
M00_L01–M00_L16 roadmap is unchanged.

### M00_L10 Controlled Activation — 2026-09-21

The accepted M00_L09 predecessor is `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED / VERIFIED`, with primary SHA
`3c822a1e3956850c9d0ba9954c5b163d83b801b9`, metadata SHA
`249100db23262430ce2557eaa5e67d70b7b0a79c`, and final verdict
`PUBLICATION_VERIFIED`. M00_L10 is now the sole active lesson:
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`, Active Lesson Count
`1`, Current Active M00 Lesson `M00_L10`, implementation `NOT STARTED`, and
Independent Activation Review `PENDING`. Freeze and Publication are not
authorized; M00_L11 is inactive/not created.

The accepted M00_L10 preparation baseline, architecture/inheritance audit, and
final design lock recorded zero non-generated source, test, lesson-document,
deploy/configuration, or support drift from M00_L09. This is a documentation-
only activation. No source, test, build, Simulation, Git, frozen predecessor,
or roadmap change is claimed. The exact position/reference contract and
future bounded file lists are recorded in the candidate transition guide
`real_robot_programming/module_M00/M00_L10_ElevatorFoundationAndPositionReferenceSemantics/docs/M00_L09_to_M00_L10_Step_by_Step.md`.

### M00_L10 Primary Publication and Metadata Reconciliation — 2026-09-22

The accepted Independent Freeze Review gate is
`PASS_M00_L10_INDEPENDENT_FREEZE_REVIEW`. M00_L10 remains exactly
`COMPLETE / FROZEN / READ-ONLY`; its frozen lesson-local tree is unchanged.
The accepted primary snapshot gate is
`PASS_M00_L10_PRIMARY_PUBLICATION_SNAPSHOT`.

Primary publication identity:

- SHA: `531bceabddf53f194b1edaabbd972ee6865e9ff0`

The User independently verified that the primary snapshot contains only files
inside the frozen M00_L10 lesson folder. The canonical publication phase is
`PENDING METADATA COMMIT`. No metadata commit SHA is available, no metadata
push has occurred, and no `PUBLICATION_VERIFIED` verdict is claimed.

This preserves the two-commit Historical Snapshot model: frozen lesson
snapshot primary commit, repository-level metadata commit, and later external
final publication verification. No third commit is required merely to record
final verification. The frozen M00_L10 lesson-local records remain unchanged.

M00_L10 introduces exactly one concept: vendor-neutral Elevator position
observation and reference semantics. Position control remains M00_L11 scope,
homing and trusted-reference establishment remain M00_L12 scope, and
travel-limit enforcement remains M00_L13 scope. Evidence remains exactly
`THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE DEFERRED`.
Active Lesson Count remains `0`, Current Active M00 Lesson remains `NONE`,
M00_L11 remains `INACTIVE / NOT CREATED`, and the locked M00_L01–M00_L16
roadmap remains unchanged with no M00_L17.

### M00_L11 Controlled Activation — 2026-09-22

The accepted predecessor is M00_L10 in canonical state
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`, with primary SHA
`531bceabddf53f194b1edaabbd972ee6865e9ff0`, metadata SHA
`cb8c125da0bbc7e2bf0aeebe40163c1a72909445`, and final gate
`PASS_M00_L10_FINAL_PUBLICATION_VERIFICATION`. M00_L10 remains untouched.

The accepted M00_L11 gates are `PASS_M00_L11_PREPARATION_BASELINE`,
`PASS_M00_L11_ARCHITECTURE_INHERITANCE_AUDIT`,
`READY_FOR_M00_L11_FINAL_DESIGN_LOCK`, and
`PASS_M00_L11_FINAL_DESIGN_LOCK`. Inheritance is exact: production
`108 / 108 / 0 / 0 / 0`, tests `102 / 102 / 0 / 0 / 0`, deploy/config/support
`24 / 24 / 0 / 0 / 0`, and lesson-local documentation `98 / 98 / 0 / 0 / 0`
for Compared / Byte-identical / Changed / Missing / Added. Unexpected
substantive drift is `NONE`.

M00_L11 is now the sole active lesson:

```text
STATUS: IN_PROGRESS
ACTIVE STATE: ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
ACTIVE LESSON COUNT: 1
CURRENT ACTIVE M00 LESSON: M00_L11
IMPLEMENTATION: NOT STARTED
INDEPENDENT ACTIVATION REVIEW: PENDING
M00_L10: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L12: INACTIVE / NOT CREATED
```

M00_L11 introduces exactly one concept: vendor-neutral Elevator closed-loop
position request semantics in the M00_L10 logical position frame. The locked
future IO request is `requestPositionMeters(double)` beside `updateInputs(...)`
and `stop()`. The exact requested states are `STOPPED` and
`POSITION_REQUESTED`. The future observation adds requested state, target
position, and derived position error to the inherited five members. Requests
require finite target, valid position, and trusted reference; finite negative
targets remain valid and no physical range clamp exists. Valid requests record
intent, rebuild the immutable observation, and forward once. Invalid requests
make no mutation or IO call. `periodic()` does not reissue. Stop records
stopped/target `0.0`, rebuilds, and forwards once.

The future write boundary is one requested-state production file and five
existing Elevator production files, with five existing focused Elevator tests
modifiable. RobotContainer, RobotTelemetry, Constants, commands, adapters,
and ElevatorIOSim remain unchanged. M00_L12 owns homing/reference
establishment; M00_L13 owns travel-limit safety and target clamping. Runtime is
Noop-only. Current M00_L11 implementation, build/test, and Simulation evidence
are `NOT YET ESTABLISHED FOR M00_L11`; real hardware is `DEFERRED`. The eventual
closure target is `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE
DEFERRED`, explicitly planned rather than achieved. The activation record is
`real_robot_programming/module_M00/M00_L11_ElevatorClosedLoopPosition/docs/M00_L10_to_M00_L11_Step_by_Step.md`.
No implementation, test, build, Simulation, closure, freeze, publication, or
Git result is claimed.

### M00_L11 Implementation, Verification, and Documentation Reconciliation — 2026-09-22

The preceding M00_L11 activation record is historical. The current lesson is
`IN_PROGRESS / ACTIVE / IMPLEMENTATION COMPLETE`, with accepted User gates
`PASS_M00_L11_USER_FOCUSED_TESTS`, `PASS_M00_L11_USER_CLEAN_REGRESSION`, and
`PASS_M00_L11_BOUNDED_SIMULATION_VERIFICATION`. Documentation Reconciliation
is complete; Independent Closure Review remains pending; Freeze and Publication
are not authorized.

The first compile failure and the later observation equality failure remain
preserved as test defects with no production causality. The implementation and
test deltas remain bounded to the authorized M00_L11 scope. Simulation evidence
is limited to Noop composition, exact telemetry, safe idle, and
Disabled → Teleop Enabled → Disabled persistence. The classification is exactly
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.

M00_L12 retains homing/reference establishment, M00_L13 retains travel-limit
safety and target clamping, and real hardware remains deferred. No closure,
freeze, publication, or Git result is claimed by this reconciliation.

### M00_L11 Controlled Freeze — 2026-09-22

The preceding implementation and reconciliation section is historical. The accepted closure gate is `PASS_M00_L11_INDEPENDENT_CLOSURE_REVIEW` with verdict `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`. The reconciled implementation, focused tests, clean regression, and bounded Simulation evidence remain unchanged.

The authoritative current M00_L11 lifecycle is:

```text
STATUS: COMPLETE
LIFECYCLE: COMPLETE / FROZEN / READ-ONLY / READY FOR INDEPENDENT FREEZE REVIEW / NOT YET PUBLISHED
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
IMPLEMENTATION: COMPLETE
STATIC REVIEW: PASS
FOCUSED TESTS: PASS_M00_L11_USER_FOCUSED_TESTS
CLEAN REGRESSION: PASS_M00_L11_USER_CLEAN_REGRESSION
SIMULATION: PASS_M00_L11_BOUNDED_SIMULATION_VERIFICATION
DOCUMENTATION RECONCILIATION: COMPLETE
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
INDEPENDENT FREEZE REVIEW: PENDING
PUBLICATION: NOT YET PUBLISHED / USER-OWNED
M00_L12: INACTIVE / NOT CREATED
M00_L13: FUTURE SCOPE / NOT ACTIVATED
```

The locked one-concept boundary, Frozen Backbone, predecessor integrity, both historical test defects and their bounded repairs, and all verification limits remain preserved. No M00_L12 activation or creation, roadmap movement, publication, remote verification, or hardware verification is authorized by this freeze record. Earlier active-state text in this file remains historical chronology.

### M00_L11 Publication Phase 1 Primary Frozen Snapshot — 2026-09-22

The User-accepted gate is `PASS_M00_L11_PUBLICATION_PHASE_1_PRIMARY_FROZEN_SNAPSHOT`.
The authoritative primary frozen lesson snapshot is:

```text
SHA: 385bf1d4ff6550dafb3e15de06ceaa9f1e9edc03
COMMIT SUBJECT: Complete M00_L11 Elevator closed-loop position
BRANCH: main
```

User evidence confirms that this primary commit contains only M00_L11 lesson files and that the Git index was empty after the primary commit. The primary frozen snapshot is committed locally; it has not yet been pushed as part of the final publication workflow. No metadata publication commit SHA exists yet.

The current publication metadata state is:

```text
M00_L11: COMPLETE / FROZEN / READ-ONLY
PRIMARY FROZEN SNAPSHOT: 385bf1d4ff6550dafb3e15de06ceaa9f1e9edc03 / COMMITTED LOCALLY
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
NEXT ROADMAP LESSON: M00_L12 - Elevator Homing
M00_L12: INACTIVE / NOT CREATED
M00_L13: FUTURE ELEVATOR TRAVEL-LIMIT SAFETY
METADATA PUBLICATION COMMIT: NOT YET CREATED / USER-OWNED
FINAL REMOTE PUBLICATION VERIFICATION: PENDING / EXTERNAL FUTURE GATE
```

This record follows the Historical Snapshot publication model. It does not claim `REMOTE VERIFIED`, `FINAL PUBLICATION VERIFIED`, `origin/main` alignment, remote-main alignment, or a metadata commit SHA. The M00_L11 frozen lesson and its technical Design Lock remain unchanged.

## M00_L12 Controlled Freeze — 2026-09-23

The current authoritative M00_L12 lifecycle record follows the accepted Independent Closure Review: `PASS_M00_L12_INDEPENDENT_CLOSURE_REVIEW`, verdict `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`, with remaining legitimate closure findings `NONE`. Earlier activation, implementation, verification, and documentation-reconciliation records remain historical chronology.

Accepted implementation and verification gates remain `PASS_M00_L12_FINAL_INDEPENDENT_STATIC_REREVIEW`, `PASS_M00_L12_USER_FOCUSED_TESTS`, `PASS_M00_L12_USER_CLEAN_REGRESSION`, and `PASS_M00_L12_BOUNDED_SIMULATION_VERIFICATION`. The test-only architecture repair chronology and exact production, test, and deploy/config/support deltas remain recorded in the transition guide. The accepted closure review did not modify files or run Git, Gradle, tests, build, or Simulation.

M00_L12 preserves one concept: a bounded, scheduler-managed Elevator homing lifecycle that requests vendor-neutral reference acquisition and recognizes success only from normalized IO-reported `positionReferenced`. Zero position alone does not establish home. The evidence remains `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`; Simulation is bounded software/runtime and truthful Noop evidence only. Physical homing, motion, sensor activation, calibration, reference accuracy, and hardware convergence are not established.

The transition to `COMPLETE / FROZEN / READ-ONLY` is documentation/lifecycle reconciliation only. Publication has not occurred; no M00_L12 publication SHA or remote verification is recorded. M00_L13 remains inactive and not created. The canonical chronology is `real_robot_programming/module_M00/M00_L12_ElevatorHoming/docs/M00_L11_to_M00_L12_Step_by_Step.md`.

```text
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
M00_L12: COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED
IMPLEMENTATION: COMPLETE
STATIC REVIEW: PASS_M00_L12_FINAL_INDEPENDENT_STATIC_REREVIEW
FOCUSED TESTS: PASS_M00_L12_USER_FOCUSED_TESTS
CLEAN REGRESSION: PASS_M00_L12_USER_CLEAN_REGRESSION
SIMULATION: PASS_M00_L12_BOUNDED_SIMULATION_VERIFICATION / BOUNDED SOFTWARE-NOOP EVIDENCE
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
INDEPENDENT CLOSURE REVIEW: PASS_M00_L12_INDEPENDENT_CLOSURE_REVIEW / CLOSURE_REVIEW_PASS_READY_FOR_FREEZE
FREEZE RECONCILIATION: COMPLETE
INDEPENDENT FREEZE REVIEW: PENDING / NEXT GATE
PUBLICATION: NOT PUBLISHED / PENDING / USER-OWNED
PUBLICATION SHA: NONE / NOT YET ESTABLISHED
REAL HARDWARE: DEFERRED
M00_L13: INACTIVE / NOT CREATED
```

Earlier M00_L12 active-state text in this file remains historical chronology. The lesson remains in its normal repository location; no filesystem permissions were changed.

### M00_L12 Metadata Publication Reconciliation — 2026-09-23

The accepted independent freeze-review gate is `PASS_M00_L12_FINAL_INDEPENDENT_FREEZE_REVIEW`, with verdict `FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION`. M00_L12 remains `COMPLETE / FROZEN / READ-ONLY`. The User-verified primary frozen snapshot gate is `PASS_M00_L12_PRIMARY_FROZEN_SNAPSHOT_COMMIT`:

```text
PRIMARY SHA: 5cc4c2c1bc6b4108f2c710c3ca0b0cdbdc26ba49
PRIMARY COMMIT SUBJECT: Complete M00_L12 Elevator homing
PRIMARY FROZEN SNAPSHOT: COMMITTED AND VERIFIED
USER EVIDENCE: HEAD contains only M00_L12 lesson files
```

The publication progression remains `PRIMARY FROZEN SNAPSHOT COMMITTED` → `METADATA PUBLICATION COMMIT PENDING` → `REMOTE PUSH PENDING` → `FINAL PUBLICATION VERIFICATION PENDING`.

```text
METADATA PUBLICATION COMMIT: PENDING
METADATA PUBLICATION SHA: NONE / NOT YET ESTABLISHED
REMOTE PUSH: PENDING
REMOTE PUBLICATION SHA: NONE / NOT YET VERIFIED
FINAL PUBLICATION VERIFICATION: PENDING
FULLY PUBLISHED: NO
```

M00_L12 is not marked fully published or remote verified.

The evidence classification remains `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`. Simulation evidence is bounded runtime and `ElevatorIONoop` verification only; physical homing is not established. Active Lesson Count remains `0`, Current Active M00 Lesson remains `NONE`, and M00_L13 Elevator Travel-Limit Safety remains `INACTIVE / NOT CREATED`. The frozen M00_L12 lesson files remain unchanged.
