# FRC Java Coding Lab 7.0
## Real Robot Programming

> Learn professional FRC robot software engineering through incremental inheritance development.

> Học lập trình robot FRC theo phương pháp phát triển kế thừa (Inheritance Development) với các dự án robot thật.

---

# Repository Purpose
# Mục tiêu Repository

This repository is a complete learning roadmap for developing FRC robots using Java and WPILib.

The goal is not only to build a working robot, but also to learn software architecture, engineering workflow, testing, documentation, and long-term project maintenance.

Repository này là lộ trình học hoàn chỉnh để phát triển robot FRC bằng Java và WPILib.

Mục tiêu không chỉ là làm robot chạy được, mà còn học kiến trúc phần mềm, quy trình phát triển, kiểm thử, tài liệu hóa và bảo trì dự án lâu dài.

---

# Core Principles
# Nguyên tắc cốt lõi

- Frozen Architecture
- Inheritance Development
- One Lesson = One Project
- One Lesson = One New Concept
- Step-by-Step Learning
- Build Before Continue
- Simulation Before Real Robot
- Complete Documentation

---

# Repository Structure
# Cấu trúc Repository

```text
FRC_Java_Coding_Lab_7/
│
├── AGENTS.md
├── README.md
├── .gitignore
│
├── docs/
│   ├── Document_A/
│   ├── Document_B/
│   └── Document_C/
│
└── real_robot_programming/
    ├── D00_L01_Competition_Robot_Foundation/
    ├── D00_L02_Drivebase_Safety_Configuration/
    ├── D00_L03_Tank_Drive_With_Joystick/
    └── ...
```

---

# Directory Description
# Giải thích thư mục

## AGENTS.md

Defines the operating rules for Codex.

Định nghĩa quy tắc vận hành của Codex.

---

## docs/

Contains the repository engineering standards.

Chứa các tiêu chuẩn kỹ thuật của toàn bộ repository.

Typical contents:

- Frozen Backbone
- Frozen Interface Contract
- Engineering Standard
- Architecture Documents
- Observation Architecture and Model Contract

`Document_C` formally defines `frc.robot.observation` as the permanent
top-level package for immutable, vendor-neutral read models.

`Document_C` chính thức định nghĩa `frc.robot.observation` là package
top-level cố định cho các read model bất biến và độc lập với vendor.

---

## real_robot_programming/

Contains every lesson project.

Mỗi lesson là một project WPILib độc lập.

Each lesson inherits from the previous completed lesson.

Mỗi lesson kế thừa lesson hoàn thành trước đó.

Example:

```text
D00_L01
    ↓ Copy
D00_L02
    ↓ Copy
D00_L03
```

## Approved Autonomous Roadmap

### A01 - Autonomous Navigation and Path Following

A01 is the approved successor roadmap after frozen
`A00_L04_AutonomousMotionSafetyGating`. Its purpose is to progress from the
autonomous command foundation to deterministic field-based trajectory
execution and PathPlanner/AutoBuilder integration.

Authority:
`docs/architecture_decisions/ADR_A01_Autonomous_Navigation_Path_Following_Roadmap.md`

Current implementation state: A01 is authorized and `module_A01` exists.
`A01_L01` through `A01_L07_AutoBuilderContractIntegration` are `COMPLETE /
FROZEN / READ-ONLY`. After the latest Swerve zero-offset recalibration, the user
physically executed the L06 one-meter autonomous on both Blue and Red. A slight
Blue endpoint overshoot followed by a small reverse correction was observed;
exact endpoint accuracy is not formally measured or claimed, and final
PID/feedforward and physical-model tuning remain deferred. `A01_L07` is
`COMPLETE / FROZEN / READ-ONLY` after the user-confirmed implementation,
Simulation, and real-robot verification gates passed. `A01_L08` was
`COMPLETE / FROZEN / READ-ONLY` at its original closure after the user-verified
post-repair WPILib VS Code build (`BUILD SUCCESSFUL in 1s`; 6 actionable tasks:
1 executed, 5 up-to-date), the accepted 430/430 test result, Simulation PASS,
and Real Robot PASS. The 11 initial failures were independently classified and
repaired as minimal L08 test-contract migrations, with no production defect
found. L08 preserves the locked routine-selection, readiness, alliance,
requirement, and centralized-stop contracts and was the frozen inheritance
source for L09. Exact endpoint accuracy, final PID/feedforward tuning, and final
physical characterization remain explicitly unclaimed.
`A01_L09_PathPlannerNamedCommandsAndEventMarkers` is now `COMPLETE / FROZEN /
READ-ONLY` after the final Architect/Reviewer closure decision PASS. Its
approved ADR amendment
permits one safe, observable, deterministic non-mechanism `LEARNING_EVENT`
binding because D01 remains an independent Tank Drive project with no approved
shared command boundary. The implemented `ONE_METER_WITH_EVENT` routine
preserves `SAFE_STOP` as the chooser default and the inherited `ONE_METER_PATH`
routine. Repository evidence records compileJava and compileTestJava PASS,
focused L09 tests PASS, 384 unchanged inherited regression tests PASS, the full
446/446 suite PASS, and isolated clean-build PASS. The user supplied Simulation,
Driver Station / Glass, and Real Robot PASS evidence for Blue and Red, event
dispatch and telemetry, concurrent path/event execution, Disable/mode-loss
stop, no automatic restart, and recovery to Teleop. Documentation reconciliation,
final architecture review, and final closure review are PASS. User-owned Git
publication is complete at `6b243bb`; the event remains a
non-mechanism demonstration, and no Intake, Feeder, Flywheel, or other D01
mechanism integration is claimed. Exact endpoint accuracy, final
PID/feedforward tuning, and final physical characterization remain explicitly
unclaimed.

The original A01_L08 closure evidence remains preserved as historical evidence,
but post-freeze real-robot evidence identified material preparation/readiness and
terminal mode-ownership defects. Under the approved supplemental ADR, A01_L08
was reopened narrowly for safety/robustness repair; at that historical stage,
V00_L02 was `SUSPENDED / READ-ONLY`. The preparation/readiness repair and the separately
authorized Option D terminal repair are implemented and locally verified. The
final user-owned Simulation and Real Robot evidence confirms recoverable
preparation without Robot Code restart, deterministic Blue/Red execution,
session-long terminal Swerve ownership, no controller leakage while Autonomous
remains enabled, safe mode-loss handling, and normal Teleop recovery. The final
pre-implementation architecture audit was `HOLD`: the active
`AutoBuilderContractAdapter.SafeAutoBuilderCommand` manually delegated
child lifecycle callbacks, conflicting with the scheduler-native re-freeze gate.
At that pre-implementation audit stage, A01_L08 remained `REOPENED /
IN_PROGRESS / EDITABLE`. The repair changed only the approved L08
production/test boundary; no SwerveSubsystem, CTRE/IO, tuning, calibration,
PathPlanner asset, Gradle, vendordep, frozen predecessor, successor, or V00 file
was changed. Exact endpoint accuracy, final PID/feedforward tuning, and final
physical characterization remain explicitly unclaimed. A01_L09 is
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at `6b243bb`, and the A01 lesson
order remains unchanged.
A01 ends at L09; A01_L10 is not authorized. A00 is closed at A00_L04; A00_L05
is not authorized.

On 2026-08-25, the A01_L08 governance amendment expanded the future repair
boundary for the scheduler exception design to exactly four production files:
`AutoBuilderContractAdapter.java`, `AutonomousPreparationCoordinator.java`,
`RobotContainer.java`, and `Robot.java`, with four named focused test files. The
amendment was governance-only; a separate final implementation action is
recorded below. At that historical stage, the final architecture re-freeze gate
remained `HOLD` pending the required test, runtime, and user-owned
re-verification gates.

Temporary change-control authority:
`docs/architecture_decisions/ADR_A01_L08_Autonomous_Safety_Robustness_Reopen.md`

The final 2026-08-25 A01_L08 implementation action separately authorized and
implemented that exact four-file scheduler-native exception-boundary repair.
`SafeAutoBuilderCommand` manual child lifecycle delegation is removed; the
Robot-level scheduler exception boundary, RobotContainer safety bridge, and
coordinator fatal entry are present. That implementation record did not itself
re-freeze A01_L08: local `compileJava` passed under WPILib Java 17 while
`compileTestJava` was still held by the then-existing Windows Gradle/Javac
classpath-resolution failure, and Simulation and real-robot re-verification
remained user gates.

On 2026-08-26, later verification closed those gates: `compileJava` PASS,
`compileTestJava` PASS, `RobotSchedulerExceptionBoundaryTest` PASS, full 449/449
test suite PASS, and clean build PASS. User-verified Simulation and real-robot
retests passed Blue/Red execution, terminal ownership, SAFE_STOP, the Teleop
mode gate, recovery, and no automatic restart. A brief terminal steering event
is recorded as `KNOWN / BOUNDED TERMINAL STEER TRANSIENT`, `ACCEPTED FOR CURRENT
LESSON`, and `DEFERRED FOR FUTURE DRIVETRAIN / PATH-FOLLOWING TUNING`; its exact
physical root cause is not fully proven and no drivetrain, tuning, encoder,
CTRE, PathPlanner, or asset change is justified. A01_L08 is now `COMPLETE /
FROZEN / READ-ONLY` after its authorized reopen. At that closure point V00_L02
remained `SUSPENDED / READ-ONLY`; the later controlled reconstruction and
activation described in the V00 section was a separate governance decision.

Approved lesson sequence:

1. `A01_L01 - Autonomous Starting-Pose and Field-Frame Contract`
2. `A01_L02 - Pose-Targeted Autonomous Motion`
3. `A01_L03 - Trajectory Generation and Sampling Fundamentals`
4. `A01_L04 - Field and Alliance Transform Contract`
5. `A01_L05 - Holonomic Trajectory Following`
6. `A01_L06 - PathPlanner Path and Runtime Integration`
7. `A01_L07 - AutoBuilder Contract Integration`
8. `A01_L08 - Autonomous Routine Selection and Safe Composition`
9. `A01_L09 - PathPlanner NamedCommands and Event Markers`

### V00 - AprilTag Vision Observation and Pose Fusion

V00 is the approved successor roadmap after final closure and freezing of
`A01_L09_PathPlannerNamedCommandsAndEventMarkers`.

Authority:
`docs/architecture_decisions/ADR_V00_AprilTag_Vision_Observation_and_Pose_Fusion_Roadmap.md`

Current state: roadmap `APPROVED / FROZEN`; `module_V00` exists. The historical
V00_L01 lineage became stale when final A01_L09 was reconstructed and published
at `6b243bb`. The current canonical V00_L01 was reconstructed from final
A01_L09, passed final architecture and closure review, and is `COMPLETE /
FROZEN / READ-ONLY / PUBLISHED` at `7d52ebf`.

The V00_L02 that had been activated and implemented from the stale historical
lineage was suspended and later preserved outside the active lesson lineage.
The current canonical V00_L02 was reconstructed from published V00_L01. Its
inheritance audit, Java 17 baseline build, full inherited test suite,
architecture audit, pre-implementation design lock, implementation,
verification, documentation, final architecture review, and final closure
review are PASS. It is now `COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at
`53e9b9f`. The exact V00_L02 Java delta is
`AprilTagFieldLayoutContract.java` plus
`AprilTagFieldLayoutContractTest.java`; no other production or test file
changed. Authoritative User verification under WPILib Java 17 records the
focused AprilTag test PASS, inherited `VisionFrameTransformTest` PASS, full test
suite PASS, and clean full build PASS (`BUILD SUCCESSFUL in 24s`; 7 actionable
tasks, 7 executed).

V00_L02 now has state `COMPLETE / FROZEN / READ-ONLY`. The contract explicitly
selects the welded or AndyMark official 2026
resource, returns canonical Blue-origin `fieldToTag` poses, and retains no raw
mutable layout/tag state. It does not use `kDefaultField`, alliance flipping,
pose inversion, `fromLayout(...)`, runtime wiring, camera/vendor integration,
VisionIO, Observation, telemetry, autonomous, PathPlanner, or Swerve behavior.
Simulation, Driver Station / Glass, real robot, and physical camera are `NOT
APPLICABLE`. User-owned Git publication is complete at `53e9b9f`.

The User prepared `V00_L03_VisionIOAndImmutableObservationContract` directly
from published V00_L02. Its WPILib Java 17 baseline build is User-verified
PASS, its no-Git inheritance audit found zero differences across 219 comparable
non-generated files, its architecture audit and Design Lock are PASS/APPROVED,
and the separately authorized implementation is complete. V00_L03 is now
`COMPLETE / FROZEN / READ-ONLY` after the final architecture and closure
reviews passed. Its final state is `IMPLEMENTATION COMPLETE / USER-VERIFIED /
DOCUMENTATION COMPLETE / FINAL ARCHITECTURE AUDIT PASS / PREDECESSOR
PROVENANCE PASS / FINAL CLOSURE REVIEW PASS`.

The completed L03 concept is vendor-neutral one-cycle VisionIO transport plus
an immutable Vision Observation contract. The exact production boundary is
`VisionIO.java` and `VisionObservation.java`; the exact focused-test boundary
is `VisionIOTest.java` and `VisionObservationTest.java`. No runtime producer,
vendor adapter, NetworkTables acquisition, telemetry, simulation
implementation, Limelight, PhotonVision, physical-camera work, pose
estimation, quality/ambiguity, timestamps/latency, fusion, Swerve,
autonomous, PathPlanner, Robot, or RobotContainer change was added.

The earlier effectively-zero-quaternion test failure was a false oracle at the
locked `Transform3d` boundary because WPILib `Rotation3d` canonicalization had
already produced a valid identity rotation. The authorized repair changed the
test oracle to verify valid identity rotation; no raw quaternion API or
production contract expansion was introduced. Focused tests, inherited L01/L02
regressions, the full suite, clean build, final documentation, and the
read-only architecture audit are PASS. Simulation, Driver Station / Glass,
physical camera, and real robot remain not applicable to this contract-only
lesson and are deferred to the governed V00 lessons. V00_L01 remains published
at `7d52ebf`, V00_L02 remains published at `53e9b9f`, and A01_L10 remains
prohibited. No active lesson remains. Git publication of V00_L03 remains
`PENDING / USER OWNED` until the User performs and reports the commit/push.

Approved lesson sequence:

1. `V00_L01 - Vision Coordinate Frames and Camera Extrinsics`
2. `V00_L02 - AprilTag Field Layout Contract`
3. `V00_L03 - Vision IO and Immutable Observation Contract`
4. `V00_L04 - Deterministic Vision Simulation`
5. `V00_L05 - AprilTag Robot Pose Estimation`
6. `V00_L06 - Vision Measurement Quality Contract`
7. `V00_L07 - Vision Timestamp and Latency Contract`
8. `V00_L08 - Real Vision Adapter Integration`
9. `V00_L09 - Swerve Pose Estimator Vision Fusion`

The roadmap preserves frozen S00 and A01, RobotContainer's composition-root
role, vendor-neutral VisionIO and Observation contracts, read-only telemetry,
SwerveSubsystem ownership of `SwerveDrivePoseEstimator`, autonomous consumption
of `getEstimatedPose()`, and A01_L04 ownership of the sole alliance transform.
Vision measurements use canonical WPILib field coordinates and are not
alliance-flipped. Simulation must use independent ground truth, must not use
EstimatedPose as camera truth, and must pass before real-robot fusion
verification.

No camera/vendor is selected in V00_L01 through V00_L07. V00_L08 may select one
real implementation only after the ADR's hardware, WPILib 2026, vendor-version,
timestamp, dependency, and applicable simulation compatibility gate passes.

---

# Lesson Structure
# Cấu trúc một Lesson

```text
D00_L02_Drivebase_Safety_Configuration/
│
├── docs/
├── src/
├── vendordeps/
├── gradle/
├── build.gradle
├── settings.gradle
├── WPILib-License.md
└── LESSON_STATUS.md
```

---

## docs/

Contains the learning documentation for this lesson.

Chứa tài liệu học của lesson.

Example:

```
D00_L01_to_D00_L02_Step_by_Step.md
```

---

## src/

Robot source code.

Mã nguồn robot.

---

## LESSON_STATUS.md

Records the lesson status.

Ghi lại trạng thái lesson.

Typical information:

- Current lesson
- Previous lesson
- Build result
- Simulation result
- Real robot result
- Git status

---

# Learning Workflow
# Quy trình học

```text
Previous Lesson Completed
            │
            ▼
Copy Previous Project
            │
            ▼
Rename New Lesson
            │
            ▼
Remove Generated Folders
            │
            ▼
Baseline Build
            │
            ▼
Learn One New Concept
            │
            ▼
Build
            │
            ▼
Simulation
            │
            ▼
Real Robot
            │
            ▼
Create Step-by-Step Guide
            │
            ▼
Git Commit
            │
            ▼
Git Push
```

---

# Required Documents
# Tài liệu bắt buộc

Every completed lesson contains:

- source code
- lesson status
- transition guide

Mỗi lesson hoàn thành bao gồm:

- mã nguồn
- trạng thái lesson
- tài liệu chuyển đổi

---

# Repository Rules
# Quy tắc Repository

Always read:

1. AGENTS.md
2. Engineering Documents
3. Current Lesson

before modifying code.

Luôn đọc:

1. AGENTS.md
2. Tài liệu kỹ thuật
3. Lesson hiện tại

trước khi sửa mã nguồn.

---

# Goal
# Mục tiêu

Learn professional robot software engineering through continuous inheritance development.

Học phát triển phần mềm robot chuyên nghiệp thông qua phát triển kế thừa liên tục.
