# ADR: M00 Competition Mechanism Foundations Roadmap

- Status: APPROVED
- Date: 2026-09-13
- Roadmap State: APPROVED / ROADMAP AUTHORIZED
- Preparation State: COMPLETE / ACCEPTED
- Preparation Authorization: PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED
- Runtime / Lesson Activation: NO ACTIVE M00 LESSON
- Controlled Activation: M00_L04 PASS / RECORDED
- Freeze State: M00_L04 FROZEN
- Design Lock: PASS_M00_L04_FINAL_DESIGN_LOCK
- Implementation Authorization: CONSUMED / IMPLEMENTATION COMPLETE
- Active Lesson Count: 0
- M00_L03 Lifecycle: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
- M00_L03 Primary Git Publication: PASS
- M00_L03 Primary Publication Commit: 3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3
- M00_L03 Metadata Publication Commit: b2464f66da42a6281acb7bfc709f2a3b83296505
- M00_L03 Final Publication Verification: PASS
- M00_L01 Publication: PUBLISHED / VERIFIED @ 83907ab
- M00_L01 Metadata Publication: PUBLISHED / VERIFIED @ f523118
- M00_L02 Primary Publication Commit: 65a92a4a5806fd5134e0114e851c4e4cc093c58e
- M00_L02 Primary Push: PASS
- M00_L02 Publication Metadata: PUBLISHED / VERIFIED @ 84010ff5022a33a946888fafedbbca0d67439e0c
- M00_L02 Final Publication Completion: PASS_M00_L02_FINAL_PUBLICATION_COMPLETE
- M00_L03 Preparation: PASS
- M00_L03 Baseline Build: PASS / EXIT CODE 0
- M00_L03 Architecture / Inheritance Audit: PASS
- M00_L03 Implementation: COMPLETE FOR AUTHORIZED M00_L03 SCOPE
- M00_L04 Preparation: PASS
- M00_L04 Baseline Build: PASS / BUILD SUCCESSFUL IN 48s / EXIT CODE 0
- M00_L04 Architecture / Inheritance Audit: PASS
- M00_L04 Lifecycle: COMPLETE / FROZEN / READ-ONLY
- M00_L04 One New Concept: SCHEDULER-MANAGED MANUAL OWNERSHIP OF EXISTING INTAKE
- M00_L04 Locked Scope: RunIntakeCommand + RIGHT BUMPER whileTrue + FOCUSED LIFECYCLE/OWNERSHIP TESTS
- M00_L04 Implementation: COMPLETE / VERIFIED
- M00_L04 Focused Tests: VERIFIED / 14 OF 14 PASS
- M00_L04 Full Regression: VERIFIED
- M00_L04 Simulation: SIMULATION VERIFIED / BOUNDED
- M00_L04 Driver Station: VERIFIED / BOUNDED
- M00_L04 Glass: NOT APPLICABLE AS A DISTINCT COMPLETION GATE
- M00_L04 Real Hardware: REAL HARDWARE DEFERRED
- M00_L04 Independent Implementation Review: PASS
- M00_L04 Documentation: COMPLETE / VERIFIED
- M00_L04 Final Closure Build: PASS / BUILD SUCCESSFUL IN 23s / 7 OF 7 TASKS EXECUTED / EXIT CODE 0
- M00_L04 Final Closure Review: PASS
- M00_L04 Freeze: COMPLETE / FROZEN / READ-ONLY
- M00_L04 Primary Publication: COMPLETE
- M00_L04 Primary Publication Commit: 5c86be3
- M00_L04 Primary Remote Alignment: PASS
- M00_L04 Publication Metadata Reconciliation: COMPLETE IN WORKING TREE
- M00_L04 Metadata Git Publication: PENDING USER ACTION
- M00_L04 Final Publication Verification: PENDING
- Current Active M00 Lesson: NONE
- M00_L05: NOT ACTIVE / NOT CREATED
- Scope: Future post-V00 mechanism curriculum roadmap
- Authority: Approved successor ADR to
  `ADR_V00_AprilTag_Vision_Observation_and_Pose_Fusion_Roadmap.md`. The
  repository authority order remains unchanged.

## Context

`V00_L09_SwervePoseEstimatorVisionFusion` is `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED / VERIFIED`. Its implementation/freeze publication is `6548c98` with
subject `Complete V00_L09 Swerve pose estimator vision fusion`. Its later
metadata-reconciliation publication is `5d36529` with subject `Record V00_L09
publication metadata`. The accepted repository state is `HEAD = origin/main =
origin/HEAD = 5d36529`, ahead `0`, behind `0`, and the final V00 closure gate is
`PASS_V00_MODULE_FINAL_CLOSURE_CONFIRMED`. No V00 lesson is active or reopened.

The repository already teaches the Frozen Backbone, IO and IOInputs, real versus
simulation replacement, subsystem ownership, safe stop, immutable Observations,
read-only telemetry, RobotContainer composition, vendor isolation, command
requirements, and dependency direction through the S00/A00/A01/V00 lineage.

The D01 mechanism lessons are a separate historical/parallel Tank Drive
practice line. They provide reference examples, but they are not the
predecessor of the post-V00 main line.

The accepted Architect roadmap gate is
`PASS_M00_COMPACT_REUSE_ROADMAP_ARCHITECT_ACCEPTED`, following the independent
review gate `PASS_M00_COMPACT_REUSE_ROADMAP_READY_FOR_ARCHITECT_LOCK`.

## Decision

Document the future successor module:

`M00 - Competition Mechanism Foundations`

Document its future repository location:

`real_robot_programming/module_M00/`

M00 shall inherit from the final frozen and User-published
`V00_L09_SwervePoseEstimatorVisionFusion` main-line snapshot. M00 shall not
inherit from D01.

The original ADR approval authorized the future roadmap only. The separate
2026-09-15 governance decision now authorizes preparation after this record is
reviewed and User-published. It does not activate M00, make M00_L01
`IN_PROGRESS`, create anything during this recording task, or authorize
production Java, tests, configuration, dependencies, simulation, real-robot
work, or implementation.

## Rationale

The new module provides a practical mechanism progression without redesigning
the existing Swerve, autonomous, PathPlanner, Vision, estimator, or fusion
architecture. The sequence reuses mastered architecture in complete mechanism
Foundation lessons and isolates genuinely new control, readiness, safety, and
coordination concepts.

The module is a curriculum boundary, not a version-control boundary. Git branch
selection remains User-owned and cannot substitute for an ADR or lesson
inheritance boundary.

## One-New-Concept Rule

Every lesson introduces exactly one new architectural, control, safety, or
coordination concept. One lesson is not required to equal one Java file, class,
package, or architecture layer.

Previously mastered architecture may be bundled as supporting implementation in
one lesson when all changed files express that lesson's single new concept.
New behavior such as feedback control, readiness, homing, travel-limit
enforcement, coordination, or autonomous event integration remains isolated.

## Prior-Knowledge Reuse Rule

The following are prior knowledge and may be applied together in a Foundation
lesson:

- vendor-neutral IO and IOInputs;
- deterministic Simulation/Noop and Real adapter replacement;
- subsystem ownership and safe stop;
- immutable mechanism Observations;
- read-only telemetry facades and RobotTelemetry integration;
- RobotContainer composition and implementation selection;
- vendor isolation, Constants authority, command requirements, and testing.

Reusing these patterns does not constitute multiple new concepts. A Foundation
lesson must not add closed-loop velocity, ready-at-speed, active homing,
travel-limit enforcement, cross-mechanism coordination, or autonomous event
integration at the same time.

## Foundation Reuse Policy

Each mechanism Foundation may contain the complete known architecture slice:

```text
hardware candidate
-> mechanism IO / IOInputs
-> Simulation/Noop and, when justified, selected Real IO
-> mechanism Subsystem and safe stop
-> immutable Observation
-> read-only TelemetryFacade
-> RobotTelemetry / RobotContainer composition
```

The Foundation's sole new concept is the independently owned mechanism
capability. A missing hardware adapter may remain `REAL HARDWARE DEFERRED`;
Simulation/Noop is valid evidence for architecture learning.

## Locked 16-Lesson Roadmap

The following order is locked for this approved ADR. Each later lesson inherits
only from the immediately preceding lesson after that predecessor is
`COMPLETE / FROZEN / READ-ONLY`.

### M00_L01 - Mechanism Architecture Reuse

- One concept: apply the mastered Frozen mechanism architecture to future
  non-drivetrain mechanism capabilities.
- Student question: how does the mastered drivetrain, vision, and autonomous
  architecture apply to non-drivetrain mechanisms?
- Locked directory identity: `M00_L01_MechanismArchitectureReuse`.
- Prerequisite: final frozen/published V00_L09.
- Excludes mechanism implementation, hardware selection, and behavior.

### M00_L02 - Mechanism Hardware Evidence Audit

- One concept: classify mechanism facts as `VERIFIED`, `PROVISIONAL`,
  `UNKNOWN`, or `NOT APPLICABLE`.
- Prerequisite: M00_L01 ownership map.
- Excludes guessed device values and mandatory final hardware selection.

### M00_L03 - Intake Foundation

- One concept: Intake is one independently owned robot mechanism capability.
- Prerequisite: M00_L02 evidence method.
- Excludes manual command ownership, automatic intake, coordination, and
  autonomous events.

### M00_L04 - Intake Command Ownership

- One concept: scheduler-managed manual ownership of Intake.
- Prerequisite: verified M00_L03 Intake capability.
- Excludes automatic sensing, Feeder coordination, and autonomous use.

### M00_L05 - Feeder Foundation

- One concept: Feeder is one independently owned transport mechanism.
- Prerequisite: M00_L04 and reused mechanism architecture.
- Excludes manual transport ownership, shooting, transfer, and autonomous use.

### M00_L06 - Feeder Command Ownership

- One concept: scheduler-managed manual transport ownership of Feeder.
- Prerequisite: verified M00_L05 Feeder capability.
- Excludes shooting, automatic staging, Intake transfer, and autonomous use.

### M00_L07 - Flywheel Foundation

- One concept: Flywheel is one independently owned rotational-speed mechanism.
- Prerequisite: M00_L06 and reused mechanism architecture.
- Excludes velocity feedback, readiness, shooting, and autonomous behavior.

### M00_L08 - Flywheel Closed-Loop Velocity

- One concept: velocity-setpoint feedback control.
- Prerequisite: verified M00_L07 Flywheel measurement and ownership.
- Excludes ready-at-speed policy, Feeder action, and shooting coordination.

### M00_L09 - Flywheel Ready-at-Speed

- One concept: deterministic readiness classification separate from velocity
  control.
- Prerequisite: verified M00_L08 velocity control.
- Excludes Feeder sequencing and autonomous behavior.

### M00_L10 - Elevator Foundation and Position-Reference Semantics

- One concept: Elevator is an independently owned position mechanism whose
  reported position has explicit reference meaning.
- Prerequisite: M00_L09 and the reused mechanism architecture.
- May define units, positive direction, zero meaning, relative/absolute
  semantics, and valid/unknown/unreferenced/disconnected states.
- Excludes active homing, position feedback, and travel-limit enforcement.

### M00_L11 - Elevator Closed-Loop Position

- One concept: position-setpoint feedback control.
- Prerequisite: verified M00_L10 position-reference semantics.
- Excludes active homing, travel-limit policy, and coordination.

### M00_L12 - Elevator Homing

- One concept: safely establish a trusted Elevator position reference.
- Prerequisite: verified M00_L11 position control and reference contract.
- Excludes general travel-limit enforcement and scoring behavior.

### M00_L13 - Elevator Travel-Limit Safety

- One concept: prevent Elevator motion outside its valid travel envelope.
- Prerequisite: verified M00_L12 homing/reference behavior.
- Excludes homing redesign and cross-mechanism coordination.

### M00_L14 - Shoot Coordination

- One concept: one scheduler-managed command coordinates Flywheel readiness and
  Feeder action.
- Prerequisite: verified M00_L06 Feeder ownership and M00_L09 readiness.
- Requires both `FlywheelSubsystem` and `FeederSubsystem`.
- Excludes a ShooterSubsystem, ShooterIO, vision aiming, and autonomous events.

### M00_L15 - Intake-to-Feeder Coordination

- One concept: one scheduler-managed command coordinates Intake and Feeder
  transfer.
- Prerequisite: verified M00_L04 Intake and M00_L06 Feeder ownership.
- Excludes shooting, new sensing, and autonomous integration.

### M00_L16 - Mechanism Autonomous Event Integration

- One concept: schedule exactly one already-verified mechanism command through
  the existing autonomous event infrastructure.
- Prerequisite: verified M00_L14 or M00_L15 command and the frozen A01 event
  boundary.
- Excludes new mechanism behavior, path redesign, PathPlanner redesign, Swerve,
  Vision, pose-fusion, and multiple mechanism commands.

## Hardware-Evidence Policy

M00_L02 establishes an evidence method, not a requirement that every final
mechanism be selected at module start.

The following facts require explicit evidence and must use exactly one of the
four classifications `VERIFIED`, `PROVISIONAL`, `UNKNOWN`, or
`NOT APPLICABLE`:

- mechanical purpose and operating direction;
- motor/controller family, CAN bus, CAN ID, motor count, and follower layout;
- sensor type and mounting;
- gear ratio and mechanism conversion;
- inversion and sensor phase;
- neutral behavior;
- voltage, supply-current, stator-current, ramp, and peak-output limits;
- physical travel limits;
- firmware and vendor-library compatibility;
- configuration apply/readback behavior;
- Simulation support and commissioning/emergency-stop procedure.

Kraken X60 with Talon FX, Kraken X44 with Talon FX, and Minion with Talon FXS
remain candidates until evidence selects them. No CAN ID, bus, ratio, sensor,
limit, gain, speed, current, voltage, geometry, or calibration value may be
invented.

A Foundation may close with `REAL HARDWARE DEFERRED` when hardware evidence is
unavailable. A later real adapter must preserve the subsystem, command,
Observation, and telemetry contracts; a motor/controller swap should remain
primarily an IO/hardware-layer concern.

## Mechanism Ownership

The independent mechanism owners are:

- `IntakeSubsystem` with `IntakeIO`;
- `FeederSubsystem` with `FeederIO`;
- `FlywheelSubsystem` with `FlywheelIO`;
- `ElevatorSubsystem` with `ElevatorIO`.

Each IO interface owns its mechanism-specific Inputs snapshot. Each subsystem
owns behavior, state, and safe stop. Vendor APIs remain inside concrete IO
adapters. No mechanism is added merely because it appears in a command name.

## Shooter Ownership Decision

The initial shooting architecture is locked as:

```text
FlywheelSubsystem
+
FeederSubsystem
+
ShootCommand requiring both
```

No `ShooterSubsystem` or `ShooterIO` is authorized by this ADR. Shooting is
initially command-level coordination of independently owned Flywheel and Feeder
capabilities. A future hood, turret, pivot, or other independently owned
actuator receives its own reviewed subsystem/IO boundary. An umbrella Shooter
owner requires new ownership evidence and formal architecture review.

## RobotContainer Policy

`RobotContainer` remains the Composition Root only. It may construct objects,
select Real/Simulation/Noop implementations, inject dependencies, configure
bindings, construct telemetry facades, and connect dependencies.

It shall not contain PID or control math, velocity or position control,
readiness decisions, homing policy, travel-limit policy, sensor interpretation,
motor safety logic, telemetry calculations, or vendor behavior.

Simple private helpers such as `createIntakeIO()`, `createFeederIO()`,
`createFlywheelIO()`, and `createElevatorIO()` are allowed only for direct
implementation selection. Dependency-injection frameworks, service locators,
reflection, arbitrary registries, and generic mechanism factories are excluded.

## Observation and Telemetry Policy

Every mechanism follows:

```text
hardware
-> IOInputs
-> subsystem / estimator
-> immutable Observation
-> read-only telemetry
-> NT4 / Glass / log
```

IOInputs is mutable one-cycle transport only and must not be retained as public
mechanism state. Subsystems create immutable, vendor-neutral Observations with
explicit units, timing, and validity where applicable. Telemetry publishes
Observations only and cannot control behavior, schedule commands, interpret
hardware, or mutate state.

Readiness, homing, travel-limit, and other policies remain in their approved
subsystem/evaluator/command boundaries, not in telemetry.

## Protected Existing Systems

M00 shall preserve without redesign:

- the Frozen Backbone and Frozen Interface Contract;
- Swerve module architecture and `SwerveSubsystem` ownership;
- odometry and estimated-pose semantics;
- autonomous safety and centralized stop authority;
- PathPlanner, AutoBuilder, and NamedCommands/event ownership;
- VisionIO, Limelight adapter, VisionSubsystem, and VisionFusionCoordinator;
- `SwerveDrivePoseEstimator` ownership and vision-fusion boundaries;
- existing alliance-transform ownership and canonical field-frame rules.

M00_L16 may consume the existing autonomous event boundary but may not redesign
it.

## Evidence and Verification Policy

The course evidence vocabulary remains:

- `THEORY VERIFIED`;
- `SIMULATION VERIFIED`;
- `REAL HARDWARE VERIFIED`;
- `REAL HARDWARE DEFERRED`;
- `NOT APPLICABLE`.

Simulation cannot prove CAN identity, wiring, physical direction, gearing,
sensor phase, current or thermal behavior, travel limits, loading, friction,
shooter performance, or physical safety margins. Real-robot claims remain
bounded to the exact tested scope.

Each M00 lesson must pass the applicable architecture review, inherited
baseline build, focused tests, inherited regression, clean build, Simulation,
Driver Station/Glass, real-robot, documentation, and freeze gates. No lesson
may be marked complete from a build result alone.

## Activation Prerequisites

M00 activation requires all of the following:

1. V00_L09 is `COMPLETE`.
2. V00_L09 is `FROZEN / READ-ONLY`.
3. V00_L09 is User-published.
4. Final predecessor and publication evidence is recorded.
5. The normal copy/rename/generated-artifact-cleanup workflow is authorized.
6. `module_M00` and its first lesson are separately created through the
   approved lifecycle.
7. The active lesson count is reconciled so only the intended lesson is
   editable.

ADR approval does not activate M00. No active lesson state changes because of
this ADR.

## Governance Preparation Authorization

The Architect authorization
`PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED` is recorded with these exact state
distinctions:

```text
M00 Roadmap: APPROVED / ROADMAP AUTHORIZED
M00 Preparation: AUTHORIZED
M00 Runtime/Lesson Activation: NOT ACTIVE
Active Lesson Count: 0
M00_L01: NOT ACTIVE / NOT YET CREATED
Implementation Authorization: NONE
```

Preparation may occur only after this governance record is reviewed and
User-published. M00_L01 is not `IN_PROGRESS`, and neither `module_M00` nor its
first lesson is created by this authorization-recording task.

The locked first lesson identity is:

```text
Lesson: M00_L01 - Mechanism Architecture Reuse
Directory: M00_L01_MechanismArchitectureReuse
```

The exact predecessor/source is the complete frozen V00_L09 snapshot at
repository state `5d36529`, with implementation/freeze commit `6548c98`:

```text
C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7\real_robot_programming\module_V00\V00_L09_SwervePoseEstimatorVisionFusion
```

D01 is not the predecessor. The exact future destination is:

```text
C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7\real_robot_programming\module_M00\M00_L01_MechanismArchitectureReuse
```

The future preparation sequence is User-owned:

1. Start at the repository root.
2. Copy the complete frozen V00_L09 directory.
3. Create `module_M00` only as part of the authorized copy workflow.
4. Rename only the destination copy to `M00_L01_MechanismArchitectureReuse`.
5. Remove only the destination `build\` and `.gradle\`.
6. Select WPILib 2026 Java 17.
7. Run the inherited baseline clean build.
8. Report `BUILD SUCCESSFUL` and Git status.
9. Only then proceed to Architecture Audit, Design Lock, lifecycle activation,
   and separately granted implementation authorization.

The following command is recorded but is not run by this governance task. The
User runs it inside the future destination:

```powershell
$env:JAVA_HOME = "C:\Users\Public\wpilib\2026\jdk"
.\gradlew.bat clean build "-Dorg.gradle.java.home=C:\Users\Public\wpilib\2026\jdk"
```

M00_L01's sole concept is Mechanism Architecture Reuse. It may teach reuse of
subsystem ownership, IO, immutable Observations, read-only telemetry,
composition-root assembly, safe stop, and architecture mapping. It must not
implement Intake, Feeder, Flywheel, Elevator, closed-loop control, readiness,
elevator position control, homing, travel-limit safety, coordination,
autonomous events, or any new hardware API.

Any future student-facing M00 Markdown must be two separate files, one English
and one Vietnamese, with identical structure, course/chapter identity, meaning,
evidence, and architecture rules. English is normative. Vietnamese must remain
student-friendly while preserving the same meaning. No student-facing M00
Markdown is created by this task.

Evidence classifications are limited to `THEORY VERIFIED`, `SIMULATION
VERIFIED`, `REAL HARDWARE VERIFIED`, `REAL HARDWARE DEFERRED`, and `NOT
APPLICABLE`. Runtime applicability is not claimed for M00_L01; its future
Design Lock decides which runtime evidence applies. No V00 rerun is required.

## M00_L01 Controlled Activation — 2026-09-15

The User completed the authorized copy, rename, destination-only generated
artifact cleanup, and WPILib Java 17 inherited baseline build. The accepted
baseline result is `BUILD SUCCESSFUL in 20s` with seven actionable tasks
executed. The independent read-only Architecture / Inheritance Audit passed
with 601 comparable non-generated files in both the predecessor and candidate,
zero missing files, zero candidate-only files, and zero SHA-256 differences.

The Architect then issued `PASS_M00_L01_FINAL_DESIGN_LOCK`. The locked lesson
is documentation-only and teaches Mechanism Architecture Reuse. Production
Java, test code, configuration, vendordeps, deploy assets, new runtime behavior,
and mechanism hardware APIs all have authorization `NONE`.

This controlled activation records:

```text
M00 Roadmap: APPROVED / ROADMAP AUTHORIZED
M00 Preparation: COMPLETE / ACCEPTED
M00_L01 Status: IN_PROGRESS
M00_L01 Active State: IN_PROGRESS / EDITABLE
M00_L01 Freeze State: EDITABLE
Active Lesson Count: 1
Implementation Authorization: NONE
Production Code Authorization: NONE
```

V00_L09 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`.
At this controlled-activation point, M00_L01 evidence was `THEORY VERIFIED`.
Focused new tests were `NOT APPLICABLE`; Simulation, Driver Station / Glass,
and real hardware were `NOT APPLICABLE` because no executable or hardware
behavior was introduced. The User-owned final inherited clean build/regression
was still required before final lesson closure; its later result is recorded
below.

Any new student-facing M00 Markdown remains subject to the separate English and
Vietnamese file rule with identical structure, identity, meaning, evidence, and
architecture rules. No student learning guide was created by the activation
itself.

## M00_L01 Pre-Freeze Documentation and Closure Readiness — 2026-09-15

The separately authorized paired English and Vietnamese learning guides were
created with identical 21-section structure and equivalent technical meaning.
The first independent documentation review returned
`HOLD_M00_L01_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_CONSTANTS_AUTHORITY`
because both guides omitted the rule that `Constants.java` remains the default
configuration authority. The minimal two-guide repair added equivalent
statements and defined no mechanism configuration value, producing
`PASS_M00_L01_CONSTANTS_AUTHORITY_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`. The independent
rereview passed at
`PASS_M00_L01_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD`,
and the Architect recorded
`PASS_M00_L01_DOCUMENTATION_REVIEW_COMPLETE_READY_FOR_FINAL_BUILD`.

The User then supplied the distinct final inherited clean build/regression:
`BUILD SUCCESSFUL in 23s`, with 7 actionable tasks executed. The accepted gate
is `PASS_M00_L01_FINAL_INHERITED_CLEAN_BUILD_REGRESSION`. The final read-only
architecture/documentation closure review passed at
`PASS_M00_L01_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION`, and
the Architect accepted that review through
`PASS_M00_L01_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
Technical/content closure readiness is `PASS`.

At that pre-freeze reconciliation point, M00_L01 remained the sole `IN_PROGRESS
/ EDITABLE` lesson, freeze state `EDITABLE`, with active lesson count `1`, Design Lock
`PASS_M00_L01_FINAL_DESIGN_LOCK`, and production-code authorization `NONE`.
Evidence remains `THEORY VERIFIED`; focused new tests, Simulation, Driver
Station / Glass, and real hardware are `NOT APPLICABLE`. No production Java,
test code, configuration, vendordep, deploy asset, runtime behavior, mechanism
hardware API, or mechanism implementation changed. Explicit Architect freeze
authorization and User-owned Git publication remained pending.

## M00_L01 Final Documentation-Only Freeze Closure — 2026-09-15

The pre-freeze documentation reconciliation passed through
`PASS_M00_L01_DOCUMENTATION_RECONCILED_READY_FOR_FREEZE_AUTHORIZATION`,
`PASS_M00_L01_DOCUMENTATION_RECONCILIATION_ACCEPTED_READY_FOR_INDEPENDENT_REVIEW`,
and
`PASS_M00_L01_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`.
The Architect then issued `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION`.

At the freeze-recording point, M00_L01 was `COMPLETE / FROZEN / READ-ONLY`, its freeze state was `FROZEN /
READ-ONLY`, and active lesson count is `0`. Technical/content closure remains
`PASS`, the Design Lock remains `PASS_M00_L01_FINAL_DESIGN_LOCK`, and
production-code authorization remains `NONE`. Evidence remains `THEORY
VERIFIED`; focused new tests, Simulation, Driver Station / Glass, and real
hardware remain `NOT APPLICABLE`.

Publication was `NOT YET PUBLISHED / PENDING USER GIT`; Git commit was
`PENDING USER COMMIT`, Git push was `PENDING USER PUSH`, and remote verification
was `PENDING`. M00_L02 was not active, was not created, and received no lifecycle
change from this closure. The M00 module is not declared complete. The locked
16-lesson roadmap is unchanged.

## M00_L01 Lesson Publication Metadata — 2026-09-15

The User published the frozen lesson at commit `83907ab` with subject `Complete
M00_L01 mechanism architecture reuse`. User-supplied evidence records branch
`main` with `HEAD = origin/main = origin/HEAD = 83907ab`; remote publication
verification passed at `PASS_M00_L01_REMOTE_PUBLICATION_VERIFIED`.

M00_L01 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`, active
lesson count remains `0`, and M00_L02 remains `NOT ACTIVE / NOT CREATED`.
Publication-metadata reconciliation is a distinct later record and remains
`PENDING USER COMMIT`; metadata push is `PENDING USER PUSH`, and final remote
verification of that later commit is `PENDING`. This publication record does
not complete the M00 module or alter the locked roadmap.

## M00_L02 Final Freeze and Lifecycle Closure — 2026-09-16

The accepted M00_L01 final state is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED
/ VERIFIED`, with primary publication `83907ab`, metadata publication
`f523118`, and `PASS_M00_L01_FINAL_PUBLICATION_COMPLETE`. It remains the frozen
predecessor.

M00_L02 preparation and inheritance are accepted: the outer candidate baseline
passed under WPILib Java 17 with `BUILD SUCCESSFUL in 58s`; generated reports
record 637 tests with no failures, errors, or skips. The accidental nested
M00_L01 project was forensically isolated, removed through the controlled
repair, and verified absent. Current inheritance evidence is 604/604 comparable
files and 173/173 protected files with zero missing, extra, or SHA-256-different
files. The Architecture / Inheritance Audit passed at
`PASS_M00_L02_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`.

The Architect explicitly issued `PASS_M00_L02_FINAL_DESIGN_LOCK`. The
controlled activation passed through
`PASS_M00_L02_CONTROLLED_ACTIVATION_RECORDED_READY_FOR_DOCUMENTATION_IMPLEMENTATION_AUTHORIZATION`
and `PASS_M00_L02_CONTROLLED_ACTIVATION_ACCEPTED`.

Documentation implementation was separately authorized at
`PASS_M00_L02_DOCUMENTATION_IMPLEMENTATION_AUTHORIZED`. The paired English and
Vietnamese guides are complete with matching 25-section structure, 15
knowledge-check questions, 15 answers, and 80-row evidence matrices. Each
matrix contains 8 `VERIFIED`, 0 `PROVISIONAL`, and 72 `UNKNOWN` rows.

The initial independent review preserved the exact documentation-quality gate
`HOLD_M00_L02_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_REQUIRED_OWNERSHIP_CONSTANTS_AND_KNOWLEDGE_CHECK_COVERAGE`.
It identified the missing explicit `Constants.java` authorization boundary,
the incomplete full M00 ownership/shooting lock, and incomplete required
knowledge-check coverage. The bounded two-guide repair passed through
`PASS_M00_L02_MINIMAL_DOCUMENTATION_REPAIR_AUTHORIZED`,
`PASS_M00_L02_MINIMAL_DOCUMENTATION_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`, and
`PASS_M00_L02_MINIMAL_DOCUMENTATION_REPAIR_ACCEPTED_READY_FOR_INDEPENDENT_REREVIEW`.
Independent rereview then passed at
`PASS_M00_L02_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD` and
was accepted at
`PASS_M00_L02_INDEPENDENT_DOCUMENTATION_REREVIEW_ACCEPTED_READY_FOR_FINAL_USER_BUILD`.

The User-supplied final build/regression passed with `BUILD SUCCESSFUL in 33s`
and 7 actionable tasks executed. The accepted gates are
`PASS_M00_L02_FINAL_USER_BUILD_REGRESSION` and
`PASS_M00_L02_FINAL_USER_BUILD_REGRESSION_ACCEPTED_READY_FOR_FINAL_CLOSURE_REVIEW`.
The final read-only closure review passed through
`PASS_M00_L02_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION` and
`PASS_M00_L02_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
Reconciliation was authorized by
`PASS_M00_L02_DOCUMENTATION_RECONCILIATION_AUTHORIZED` and is now complete and
recorded at
`PASS_M00_L02_DOCUMENTATION_RECONCILED_READY_FOR_INDEPENDENT_REVIEW`.

The independent reconciliation review passed at
`PASS_M00_L02_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`.
The Architect then issued `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION`. The final
pre-publication lifecycle is:

```text
M00_L02 Status: COMPLETE
M00_L02 Active State: COMPLETE / FROZEN / READ-ONLY
M00_L02 Freeze State: FROZEN / READ-ONLY
Active Lesson Count: 0
Design Lock: PASS_M00_L02_FINAL_DESIGN_LOCK
Production Code Authorization: NONE
Test Implementation Authorization: NONE
Configuration Authorization: NONE
Runtime Behavior Authorization: NONE
Documentation Implementation Authorization: PASS_M00_L02_DOCUMENTATION_IMPLEMENTATION_AUTHORIZED
Documentation Implementation: COMPLETE
Independent Documentation Rereview: PASS
Final User Build: PASS
Final Closure Review: PASS
Documentation Reconciliation: PASS
Independent Reconciliation Review: PASS
Freeze Authorization: PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION
Git Commit: PENDING USER COMMIT
Git Push: PENDING USER PUSH
Remote Verification: PENDING
Publication Metadata Reconciliation: PENDING
Publication: PENDING USER GIT PUBLICATION
M00_L03: NOT ACTIVE / NOT CREATED
```

The documentation implementation authorization is
`PASS_M00_L02_DOCUMENTATION_IMPLEMENTATION_AUTHORIZED`; the historical pending
value in the activation record is superseded by that later authorization. The
sole concept remains Mechanism Hardware Evidence Audit. Each audited fact
must use exactly one state: `VERIFIED`, `PROVISIONAL`, `UNKNOWN`, or `NOT
APPLICABLE`. No guessed hardware fact or typical FRC value may be promoted to
`VERIFIED`. Evidence is `THEORY VERIFIED` as a required lesson gate; focused
new tests and Simulation are `NOT APPLICABLE`; Driver Station / Glass is `NOT
APPLICABLE`; real hardware is `REAL HARDWARE DEFERRED`.

No mechanism implementation, command, Observation, adapter, sensor boundary,
constant, CAN/PID configuration, runtime telemetry, or technical refactor was
introduced. M00_L02 is `COMPLETE / FROZEN / READ-ONLY` but not yet published,
and M00_L03 is `NOT ACTIVE / NOT CREATED`. The locked 16-lesson order and every
lesson scope remain unchanged.

## M00_L02 Primary Publication and Metadata Reconciliation — 2026-09-16

The User completed the primary publication at full commit
`65a92a4a5806fd5134e0114e851c4e4cc093c58e` with subject `Complete M00_L02
mechanism hardware evidence audit`. The primary push is `PASS`; `HEAD`,
`origin/main`, and `origin/HEAD` were observed aligned at short commit
`65a92a4`. The primary publication gates are
`PASS_M00_L02_PRIMARY_GIT_PUBLICATION` and
`PASS_M00_L02_PRIMARY_GIT_PUBLICATION_ACCEPTED_READY_FOR_PUBLICATION_METADATA_RECONCILIATION`.

M00_L02 remains `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`.
Publication metadata reconciliation is `COMPLETE`, while metadata Git
publication and final publication completion remain `PENDING`. M00_L03 remains
`NOT ACTIVE / NOT CREATED`; all 16 lesson headings and scopes remain unchanged.

## M00_L03 Controlled Documentation-Only Lifecycle Activation — 2026-09-16

The prepared candidate identity is `M00_L03 - Intake Foundation`, filesystem
`M00_L03_IntakeFoundation`, with M00_L02 as its final frozen and published
predecessor. The candidate baseline build passed under Java 17.0.16 Temurin
with exit code `0`. The earlier preparation-script requirement for a
lesson-local `AGENTS.md` was defective; repository-root `AGENTS.md` is the
authority, the candidate was unaffected, and no recopy was required.

Inheritance passed at 607/607 comparable files and 173/173 protected files,
with zero missing, extra, or SHA-256-different files. The audit gate is
`PASS_M00_L03_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`, and
the Architect Design Lock is `PASS_M00_L03_FINAL_DESIGN_LOCK`.

M00_L03 is now the sole `IN_PROGRESS / EDITABLE` lesson with active lesson
count `1` and freeze state `EDITABLE`. Its sole concept is an independently
owned, vendor-neutral Intake mechanism foundation. Production, test, and
documentation implementation remain pending separate Architect authorization.
No vendor adapter, Constants change, command, binding, default/manual command
ownership, or physical hardware claim is authorized. M00_L04 remains `NOT
ACTIVE / NOT CREATED`, and the locked 16-lesson roadmap is unchanged.

## M00_L03 Implementation, Verification, and Documentation Reconciliation — 2026-09-16

The activation paragraph above is preserved as historical state. Separate
authorization later issued
`PASS_M00_L03_PRODUCTION_TEST_IMPLEMENTATION_AUTHORIZED`, and the bounded
M00_L03 Intake foundation was implemented. The authorized architecture contains
vendor-neutral `IntakeIO` and its owned input snapshot, `IntakeIONoop`,
`IntakeSubsystem`, semantic `requestIntake()`, centralized `stop()`, requested
states `STOPPED` and `INTAKE_REQUESTED`, immutable `IntakeObservation`,
read-only telemetry, RobotContainer composition, and focused tests.

The initial focused run executed 16 tests: 15 passed and one failed in
`IntakeSubsystemTest.periodicBuildsFreshImmutableSnapshotsFromCurrentInputsAndIntent`
because an optional callback was dereferenced at line 69. This was classified
as a test defect. The minimal repair guarded the optional callback in
`IntakeSubsystemTest.java` and removed no assertions. The focused retest passed
with `BUILD SUCCESSFUL in 4s` and exit code `0`. The full clean regression
passed with `BUILD SUCCESSFUL in 20s`, exit code `0`, and 7 of 7 actionable
tasks executed. The independent implementation review passed and was accepted.

User-owned Simulation passed for startup, Noop composition, subsystem
integration, and Intake NetworkTables/telemetry presence while Disabled. This
is bounded software evidence only. It does not verify wiring, CAN identity,
controller configuration, physical direction, motion, current, load, force, or
physical stopping. Real-hardware verification remains deferred.

Documentation authorization is recorded at
`PASS_M00_L03_DOCUMENTATION_IMPLEMENTATION_AND_LIFECYCLE_RECONCILIATION_AUTHORIZED`.
The matching English and Vietnamese 34-section learning guides, each with 15
review questions and 15 answers, are implemented. Independent documentation
review, the final User closure build, final closure review, final lifecycle
reconciliation, freeze authorization, `COMPLETE / FROZEN / READ-ONLY`, and User
publication remain pending.

No Intake vendor adapter or `Constants.java` change was introduced. Unsupported
physical hardware facts remain `UNKNOWN`. M00_L04 retains exclusive ownership
of scheduler-managed Intake Commands, requirements, bindings, interruption
behavior, and default/manual ownership. It remains inactive and uncreated.

## M00_L03 Final Closure Review and Lifecycle Reconciliation — 2026-09-17

The initial independent documentation review returned `HOLD` because Step 16
of the transition guide incorrectly described `IntakeIOInputs` as an immutable
input snapshot. The correct architecture is a mutable one-cycle IO transport
snapshot feeding subsystem processing, followed by an immutable
`IntakeObservation`. The authorized bounded repair changed only the Step 16
phrase to `mutable one-cycle input snapshot` and passed at
`PASS_M00_L03_TRANSITION_GUIDE_MUTABLE_IOINPUTS_ONE_LINE_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`.
The Architect accepted the repair at
`PASS_M00_L03_TRANSITION_GUIDE_MUTABLE_IOINPUTS_REPAIR_ACCEPTED_READY_FOR_INDEPENDENT_DOCUMENTATION_REREVIEW`.

The independent documentation rereview passed at
`PASS_M00_L03_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_CLOSURE_BUILD`
and was accepted at
`PASS_M00_L03_INDEPENDENT_DOCUMENTATION_REREVIEW_ACCEPTED_READY_FOR_FINAL_USER_CLOSURE_BUILD`.
The User then supplied final closure-build evidence: `BUILD SUCCESSFUL in 42s`,
7 actionable tasks executed, and exit code `0`. The accepted build gates are
`PASS_M00_L03_FINAL_USER_CLOSURE_BUILD_REGRESSION` and
`PASS_M00_L03_FINAL_USER_CLOSURE_BUILD_REGRESSION_ACCEPTED_READY_FOR_FINAL_CLOSURE_REVIEW`.

The final read-only closure review passed at
`PASS_M00_L03_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION` and
was accepted at
`PASS_M00_L03_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
Technical defects remaining are `NONE`; substantive documentation defects
remaining are `NONE`. This documentation/lifecycle reconciliation is complete.

M00_L03 remains the sole `IN_PROGRESS / EDITABLE` lesson, freeze state
`EDITABLE`, with active lesson count `1`. It is not yet `COMPLETE / FROZEN /
READ-ONLY`. Focused tests, full regression, final closure build, bounded
software/Noop/composition Simulation, student documentation, independent
documentation rereview, and final closure review are verified or PASS as
applicable. Driver Station / Glass remain `NOT APPLICABLE`; real hardware
remains `REAL HARDWARE DEFERRED`. Independent reconciliation review, explicit
Architect freeze authorization, freeze recording, and User-owned publication
remain pending. M00_L04 remains `NOT ACTIVE / NOT CREATED`.

## M00_L03 Final Lifecycle Freeze — 2026-09-17

The preceding final-closure section is preserved as the historical pre-freeze
state. Independent reconciliation review passed at
`PASS_M00_L03_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`.
The Architect then explicitly authorized the documentation-only lifecycle
transition through `PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION`.

M00_L03 is now `COMPLETE / FROZEN / READ-ONLY`, with freeze state `FROZEN` and
active lesson count `0`. Preparation, Architecture / Inheritance Audit, Final
Design Lock, controlled activation, implementation, minimal test repair,
focused retest, full regression, bounded Simulation, independent
implementation review, student documentation, bounded documentation repair,
independent documentation rereview, final closure build, final closure review,
lifecycle reconciliation, independent reconciliation review, and Architect
freeze authorization are PASS.

```text
Preparation: PASS
Architecture / Inheritance Audit: PASS
Final Design Lock: PASS
Controlled Activation: PASS
Implementation: PASS
Focused Test Initial Run: HOLD — TEST DEFECT
Minimal Test Repair: PASS
Focused Retest: PASS / VERIFIED
Full Regression: PASS / VERIFIED
Bounded Simulation: PASS / VERIFIED — SOFTWARE/NOOP/COMPOSITION ONLY
Independent Implementation Review: PASS
Student Documentation: PASS / VERIFIED
Independent Documentation Review: HOLD — STEP 16 TERMINOLOGY DEFECT
Bounded Documentation Repair: PASS
Independent Documentation Rereview: PASS
Final Closure Build: PASS / VERIFIED
Final Closure Review: PASS
Lifecycle Reconciliation: PASS / COMPLETE
Independent Reconciliation Review: PASS
Architect Freeze Authorization: PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION
Lifecycle: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN
Active Lesson Count: 0
Publication: PENDING USER GIT
M00_L04: NOT ACTIVE / NOT CREATED
```

The initial focused-test HOLD remains preserved as a test defect, and the
initial independent documentation HOLD remains preserved as the Step 16
mutable/immutable terminology defect. `IntakeIOInputs` remains a mutable
one-cycle transport snapshot; `IntakeObservation` remains immutable. Real
hardware remains `REAL HARDWARE DEFERRED`, and unsupported physical hardware
facts remain `UNKNOWN`.

Publication remains `PENDING USER GIT`. This freeze does not claim a commit,
push, remote verification, or publication completion. M00_L04 remains `NOT
ACTIVE / NOT CREATED`, and the locked M00_L01 through M00_L16 roadmap remains
unchanged.

## M00_L03 Primary Publication and Metadata Reconciliation — 2026-09-17

The accepted primary publication gate is
`PASS_M00_L03_PRIMARY_GIT_PUBLICATION`, with Architect acceptance at
`PASS_M00_L03_PRIMARY_GIT_PUBLICATION_ACCEPTED_READY_FOR_PUBLICATION_METADATA_RECONCILIATION`.
The User-owned primary publication commit is
`3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`; the primary push passed as
`84010ff..3d94dc6  main -> main`. Post-push local `HEAD` and `origin/main`
both matched the full primary commit, so remote alignment is `PASS`.

```text
Lifecycle: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN
Active Lesson Count: 0
Primary Git Publication: PASS
Primary Publication Commit: 3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3
Remote Alignment: PASS
Publication Metadata Reconciliation: COMPLETE
Metadata Git Publication: PENDING USER GIT
Final Publication Verification: PENDING
M00_L04: NOT ACTIVE / NOT CREATED
```

The primary lesson snapshot is published, but the two-commit publication model
is not yet complete. Independent metadata review, User-owned metadata Git
publication, metadata remote verification, and final publication verification
remain pending. The locked M00_L01 through M00_L16 roadmap is unchanged.

## M00_L03 Final Publication and M00_L04 Controlled Activation — 2026-09-18

M00_L03 completed final publication at primary commit
`3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3` and metadata commit
`b2464f66da42a6281acb7bfc709f2a3b83296505`; metadata remote alignment and
final publication verification are `PASS`. M00_L03 remains
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`.

The prepared M00_L04 candidate passed its accepted Java baseline, exact
inheritance audit, Frozen Backbone review, and one-new-concept review. The
Architect issued `PASS_M00_L04_FINAL_DESIGN_LOCK`. M00_L04 is now the sole
`IN_PROGRESS / EDITABLE` lesson with active lesson count `1`.

The locked scope is exactly one dedicated `RunIntakeCommand`, a semantic Xbox
Right Bumper `whileTrue` binding in `RobotContainer`, and focused command
lifecycle, requirement ownership, safe-stop, binding, and architecture tests.
Implementation remains `NOT STARTED / PENDING AUTHORIZATION`. M00_L05 remains
inactive and uncreated; the M00_L01 through M00_L16 sequence is unchanged.

## M00_L04 Post-Verification Documentation and Lifecycle Reconciliation — 2026-09-18

The preceding activation paragraph is preserved as historical pre-
implementation state. Separate authorization was consumed, and the exact
locked implementation is complete. Production created
`RunIntakeCommand.java` and modified only the bounded `RobotContainer` wiring.
Tests created `RunIntakeCommandTest.java` and
`RobotContainerIntakeCommandBindingTest.java` and narrowly updated
`IntakeArchitectureBoundaryTest.java`. No other production or test Java
changed.

The command requires exactly `IntakeSubsystem`, requests once during
`initialize()`, performs no repeated request during `execute()`, does not
self-finish, and unconditionally delegates `end(...)` to
`IntakeSubsystem.stop()`. The existing semantic Right Bumper uses `whileTrue`;
the inherited Back/View binding remains intact; no Intake default command was
added.

The User-verified evidence is 14/14 focused tests PASS with `BUILD SUCCESSFUL
in 28s`, four tasks executed, exit code 0; full clean regression PASS with
`BUILD SUCCESSFUL in 47s`, seven tasks executed, exit code 0; `SIMULATION
VERIFIED / BOUNDED`; and Driver Station `VERIFIED / BOUNDED` with observed
software state `STOPPED -> INTAKE_REQUESTED -> STOPPED`. Glass remains `NOT
APPLICABLE` as a distinct completion gate. Real hardware remains `REAL
HARDWARE DEFERRED`.

The independent implementation review passed and was accepted for
documentation implementation. Matching English and Vietnamese student guides
are now created. `IntakeIOInputs` remains mutable one-cycle transport;
`IntakeObservation` remains immutable vendor-neutral meaning. The Noop's
`Available=false` and `Connected=false` are expected and do not contradict an
`INTAKE_REQUESTED` software intent.

M00_L04 remains the sole `IN_PROGRESS / EDITABLE` lesson with active lesson
count `1`. Independent documentation review, final closure build/review,
lifecycle reconciliation, freeze authorization, freeze, and the two-commit
publication workflow remain pending. M00_L04 is not complete, frozen, or
published. M00_L05 remains inactive and uncreated.

## M00_L04 Primary Publication and Metadata Reconciliation — 2026-09-18

The preceding lifecycle history remains preserved. After final closure and
freeze, the User completed the primary M00_L04 publication at commit
`5c86be3`, subject `Complete M00_L04 intake command ownership`. Accepted
post-push evidence records `HEAD = origin/main = origin/HEAD = 5c86be3`, so
primary remote alignment is `PASS`.

M00_L04 remains `COMPLETE / FROZEN / READ-ONLY`; implementation and
documentation are `COMPLETE / VERIFIED`; final closure review is `PASS`; real
hardware remains `REAL HARDWARE DEFERRED`; active lesson count is `0`; no M00
lesson is active; and M00_L05 remains `NOT ACTIVE / NOT CREATED`. Publication
metadata reconciliation is complete in the working tree, while the separate
User-owned metadata commit and push remain pending. No metadata SHA or final
publication completion is claimed.

## Non-Goals and Exclusions

This ADR does not authorize:

- any additional M00 runtime/lifecycle activation or lesson-directory creation;
- source, test, Gradle, vendordep, PathPlanner, or configuration changes;
- changes to V00_L09 or any frozen predecessor;
- redesign of Swerve, Vision, autonomous, estimator, or fusion ownership;
- guessed hardware values or unsupported tuning claims;
- a ShooterSubsystem or ShooterIO without new ownership evidence;
- additional M00 lessons or a reordered/renamed/merged/split sequence;
- competition strategy, final characterization, or readiness claims.

## Consequences

- M00 has a documented future boundary after final published V00_L09.
- D01 remains useful as historical mechanism reference without becoming a
  predecessor.
- Mechanism architecture can be reused without repetitive layer-only lessons.
- New feedback, readiness, homing, limit, coordination, and event concepts
  remain independently teachable and verifiable.
- Hardware uncertainty can be recorded honestly without inventing constants or
  blocking architecture learning.
- Existing Swerve, autonomous, Vision, and fusion capabilities remain protected.

## Future-Change Policy

Formal architecture review and an ADR amendment or successor ADR are required
before changing the M00 module boundary, lesson identity/order, predecessor
rule, mechanism ownership, shooter ownership, RobotContainer role, Observation
flow, hardware-evidence policy, protected-system boundary, or activation gates.

Any future real hardware selection must document the evidence, exact adapter,
vendor/library compatibility, and bounded verification scope. It must not be
introduced by familiarity or copied historical constants.

## Review Result

This ADR is the M00 roadmap authority. Its roadmap remains `APPROVED / ROADMAP
AUTHORIZED`. M00_L01 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED /
VERIFIED` at primary publication `83907ab` and metadata publication `f523118`.
M00_L02 is `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, Design
Lock `PASS_M00_L02_FINAL_DESIGN_LOCK`, documentation implementation complete,
independent rereview PASS, final User build PASS, final closure review PASS,
documentation reconciliation PASS, independent reconciliation PASS, and freeze
authorization `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION`. Production-code,
test, configuration, vendordep, deploy, runtime-behavior, and mechanism-API
authorization remain `NONE`. The primary publication is complete at
`65a92a4a5806fd5134e0114e851c4e4cc093c58e`, publication metadata reconciliation
is complete, and the separate metadata Git publication and final publication
completion remain pending. M00_L03 is `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED / VERIFIED` with freeze state `FROZEN`. Its bounded implementation,
focused retest, full clean regression, bounded Simulation, independent
implementation review, bilingual student documentation, bounded Step 16
terminology repair, independent documentation rereview, final User closure
build, final closure review, documentation/lifecycle reconciliation, and
independent reconciliation review are accepted. Architect freeze authorization
is recorded at `PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION`. Primary Git
publication is complete at `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`,
metadata publication is complete at
`b2464f66da42a6281acb7bfc709f2a3b83296505`, and final publication
verification is PASS. M00_L04 completed its authorized `RunIntakeCommand`,
Xbox Right Bumper `whileTrue` binding, and focused lifecycle/ownership test
scope. Focused tests, full regression, bounded Simulation, bounded Driver
Station verification, independent implementation review, bilingual
documentation review and repair, final closure build, transition
reconciliation, independent confirmation, and resumed final closure review
are accepted. M00_L04 is now `COMPLETE / FROZEN / READ-ONLY`, with active
lesson count `0`. Its primary publication is complete at `5c86be3`, primary
remote alignment is `PASS`, and publication metadata reconciliation is
complete in the working tree; the separate metadata Git publication remains
pending User action. M00_L05 remains inactive and uncreated. Roadmap approval remains recorded by
`PASS_M00_ROADMAP_ADR_ARCHITECT_APPROVED`; preparation authorization remains
recorded by `PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED`. This reconciliation
does not change the roadmap, publish M00_L04, or activate M00_L05.

## Revision History

| Version | Date | Status | Notes |
| --- | --- | --- | --- |
| 1.0 | 2026-09-13 | PROPOSED | Created from the Architect-accepted compact reuse roadmap; future authorization only. |
| 1.1 | 2026-09-13 | APPROVED | Architect approval recorded at `PASS_M00_ROADMAP_ADR_ARCHITECT_APPROVED`; roadmap authorized but not active. |
| 1.2 | 2026-09-15 | APPROVED | Recorded V00 final closure at implementation `6548c98` and metadata publication `5d36529`; recorded `PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED`, exact M00_L01 identity, predecessor, future destination, User-owned baseline sequence, bilingual/evidence rules, and active lesson count `0`; M00 runtime remains `NOT ACTIVE`, M00_L01 remains `NOT YET CREATED`, and implementation is not authorized. |
| 1.3 | 2026-09-15 | APPROVED | Consumed `PASS_M00_L01_FINAL_DESIGN_LOCK` and recorded the documentation-only controlled activation of M00_L01 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`; implementation and production-code authorization remain `NONE`. |
| 1.4 | 2026-09-15 | APPROVED | Reconciled paired learning documentation, the preserved Constants-authority HOLD and repair, independent rereview PASS, User final inherited clean build/regression PASS, final closure review and Architect acceptance, and technical/content readiness PASS while retaining `IN_PROGRESS / EDITABLE`, active lesson count `1`, pending freeze authorization, and pending User Git publication. |
| 1.5 | 2026-09-15 | APPROVED | Consumed `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION`; recorded M00_L01 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, preserved the final technical boundary and locked 16-lesson roadmap, left M00_L02 inactive and uncreated, and retained User Git publication and remote verification as pending. |
| 1.6 | 2026-09-15 | APPROVED | Reconciled User-verified M00_L01 lesson publication at `83907ab` and `PASS_M00_L01_REMOTE_PUBLICATION_VERIFIED`; recorded `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`, preserved active lesson count `0` and inactive/uncreated M00_L02, and left the distinct publication-metadata commit, push, and final remote verification pending. |
| 1.7 | 2026-09-16 | APPROVED | Consumed `PASS_M00_L02_FINAL_DESIGN_LOCK`; recorded M00_L02 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`; preserved M00_L01 frozen publication and the locked 16-lesson roadmap; retained all technical implementation authorization as `NONE`; and left student documentation pending separate Architect authorization. |
| 1.8 | 2026-09-16 | APPROVED | Reconciled documentation authorization and implementation, preserved the initial independent-review HOLD and bounded repair, recorded independent rereview PASS, User final build/regression PASS, final closure review PASS, and `PASS_M00_L02_DOCUMENTATION_RECONCILED_READY_FOR_INDEPENDENT_REVIEW`; retained M00_L02 as `IN_PROGRESS / EDITABLE` with active lesson count `1`, pending freeze authorization and publication, and left M00_L03 inactive/uncreated. |
| 1.9 | 2026-09-16 | APPROVED | Consumed `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION` after independent reconciliation PASS; recorded M00_L02 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, preserved all technical authorization as `NONE`, kept M00_L03 inactive/uncreated, and retained User Git publication and publication metadata reconciliation as pending. |
| 1.10 | 2026-09-16 | APPROVED | Reconciled User-confirmed M00_L02 primary publication at `65a92a4a5806fd5134e0114e851c4e4cc093c58e` with primary push PASS; recorded publication metadata reconciliation complete while leaving the separate metadata Git publication and final publication completion pending; preserved the 16-lesson roadmap and inactive/uncreated M00_L03. |
| 1.11 | 2026-09-16 | APPROVED | Consumed `PASS_M00_L03_FINAL_DESIGN_LOCK`; recorded the prepared M00_L03 candidate as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`, preserved the script-check-defect history and exact inheritance evidence, retained all implementation authorization as separately pending, and left M00_L04 inactive/uncreated. |
| 1.12 | 2026-09-16 | APPROVED | Reconciled M00_L03 implementation authorization and completion, preserved the initial focused-test HOLD and minimal test-only repair, recorded focused retest PASS, full clean regression PASS, bounded Simulation PASS, independent implementation review PASS, and authorized bilingual student-documentation implementation; retained M00_L03 as `IN_PROGRESS / EDITABLE` with active lesson count `1`, independent documentation review and closure/freeze/publication pending, and M00_L04 inactive/uncreated. |
| 1.13 | 2026-09-17 | APPROVED | Recorded the Step 16 mutable-IOInputs documentation HOLD and bounded one-line repair, independent documentation rereview PASS, final User closure build PASS, final closure review PASS, and completed lifecycle reconciliation; retained M00_L03 as `IN_PROGRESS / EDITABLE`, active lesson count `1`, freeze authorization and publication pending, and M00_L04 inactive/uncreated. |
| 1.14 | 2026-09-17 | APPROVED | Consumed `PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION` after independent reconciliation review PASS; recorded M00_L03 as `COMPLETE / FROZEN / READ-ONLY` with freeze state `FROZEN` and active lesson count `0`; preserved accepted technical/documentation evidence, M00_L02 protection, the 16-lesson roadmap, and inactive/uncreated M00_L04; left User-owned publication pending. |
| 1.15 | 2026-09-17 | APPROVED | Reconciled accepted M00_L03 primary publication at `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`, primary push PASS, and remote alignment PASS; recorded publication metadata reconciliation complete while leaving metadata Git publication and final publication verification pending; preserved frozen M00_L03, inactive/uncreated M00_L04, and the locked 16-lesson roadmap. |
| 1.16 | 2026-09-18 | APPROVED | Recorded M00_L03 final publication at primary commit `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3` and metadata commit `b2464f66da42a6281acb7bfc709f2a3b83296505`; consumed `PASS_M00_L04_FINAL_DESIGN_LOCK`; activated M00_L04 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`, locked `RunIntakeCommand` plus Right Bumper `whileTrue` scope, implementation pending authorization, and M00_L05 inactive/uncreated. |
| 1.17 | 2026-09-18 | APPROVED | Reconciled completed M00_L04 implementation, 14/14 focused tests, full clean regression, bounded Simulation and Driver Station verification, independent implementation review PASS, and paired student-guide creation; retained `IN_PROGRESS / EDITABLE`, active lesson count `1`, pending independent documentation review and all closure/freeze/publication gates, and inactive/uncreated M00_L05. |
| 1.18 | 2026-09-18 | APPROVED | Consumed `PASS_M00_L04_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_FINAL_LIFECYCLE_RECONCILIATION_AND_FREEZE_PREPARATION`; preserved the resolved documentation and transition-history HOLDs, recorded M00_L04 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, retained bounded evidence and deferred real hardware, kept M00_L05 inactive/uncreated, and left User-owned Git publication pending. |
| 1.19 | 2026-09-18 | APPROVED | Reconciled accepted M00_L04 primary publication at `5c86be3` and primary remote alignment PASS; recorded publication metadata reconciliation complete in the working tree while leaving metadata Git publication and final publication verification pending; preserved the locked 16-lesson roadmap and inactive/uncreated M00_L05. |
