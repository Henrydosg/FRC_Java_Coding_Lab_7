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

## Governance PDFs and VERIFIED Markdown Mirrors

Authoritative English governance PDFs remain authoritative. Their co-located
VERIFIED Markdown mirrors are machine-readable representations and do not have
independent or equal authority. The normative operating rules are in
`AGENTS.md`; source/mirror paths, trust metadata, and final mirror hashes are
indexed in `docs/GOVERNANCE_DOCUMENT_MANIFEST.md`.

At first governance use in a task or at formal audit start, run:

```powershell
py -3 docs/tools/governance/validate_governance_mirrors.py
```

Then consult the manifest, confirm the applicable mirror is VERIFIED and
registered, compare its current Markdown SHA-256 with the manifest, and read
all applicable sections sufficiently for the decision. Targeted section
retrieval is allowed for follow-up work in the same unchanged scope after these
checks. Use the authoritative PDF when a mirror is missing, not VERIFIED,
hash-mismatched, disputed, ambiguous, under fidelity review, needed for
forensics, or otherwise subject to the fallback rules in `AGENTS.md`.

The Architecture Poster mirror may represent its explicit text and
relationships. Consult the authoritative poster PDF whenever spatial layout,
adjacency, shared boxes, color emphasis, grouping, relative prominence, or
visual hierarchy matters.

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
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at `cc20d62` after the final
architecture and closure reviews passed and the User confirmed publication.
Its final state is `IMPLEMENTATION COMPLETE / USER-VERIFIED / DOCUMENTATION
COMPLETE / FINAL ARCHITECTURE AUDIT PASS / PREDECESSOR PROVENANCE PASS / FINAL
CLOSURE REVIEW PASS`.

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
prohibited. User-owned Git publication of V00_L03 is complete at `cc20d62`.

The User subsequently prepared
`V00_L04_DeterministicVisionSimulation` from authoritative V00_L03 by the
approved copy/rename and generated-artifact cleanup workflow and supplied
WPILib Java 17 inherited baseline-build PASS evidence. Its read-only
inheritance, roadmap-scope, Frozen Backbone, Frozen Interface Contract, and
Document C audits passed, and the Architect approved the refined
deterministic-vision-simulation Design Lock. At the historical activation
stage, V00_L04 became the sole `IN_PROGRESS / EDITABLE` lesson. Separate
authorization later permitted exactly `VisionIOSim.java` and `VisionIOSimTest.java`; implementation is now
`IMPLEMENTED / VERIFIED`.

The locked L04 scope is one deterministic, vendor-neutral `VisionIOSim`
implementation of the frozen `VisionIO` contract. It uses official AprilTag
field geometry, a fixed `robotToCamera`, an immutable caller-selected frame,
explicit ground truth only for target-present frames, direct WPILib forward
geometry, and complete per-cycle `VisionIOInputs` overwrite. L05 pose
estimation, L06 quality, L07 timing, L08 real-camera integration, and L09
fusion remain deferred.

Authoritative User verification under WPILib Java 17 records
`compileTestJava` PASS, `VisionIOSimTest` PASS, inherited vision regressions
PASS, full test suite PASS, and clean build PASS. The earlier Codex-local
classpath failure is `RESOLVED / SUPERSEDED / NON-REPRODUCIBLE`. The
post-implementation architecture review and User-owned artifact cleanup are
PASS, and the required L03-to-L04 transition guide and lesson documentation are
reconciled. The final read-only architecture/documentation review and Architect
closure authorization are PASS. V00_L04 is now
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 5461555 / USER VERIFIED`. At the
V00_L04 publication point, no V00 lesson was active and the active V00 lesson
count was `0`; the prepared V00_L05 successor was not yet activated.

The User subsequently prepared the ADR-locked
`V00_L05_AprilTagRobotPoseEstimation` directory from the final V00_L04
snapshot. The inheritance and architecture audits recorded 229 comparable
non-generated files, zero differences, identical production/test Java and
documentation, unchanged build/configuration/dependencies/deploy resources,
and preserved V00_L01-L04 protection. The prior candidate naming HOLD was
resolved by using the official ADR identity, with no ADR amendment.

The Architect approved the V00_L05 Design Lock. The authorized implementation
is complete. The narrow Java 17 compatibility repair (`getFirst()` to
`get(0)`) and noncommutativity test-fixture repair were test-only, and the API
reflection hardening is complete. The implementation architecture review and
authoritative post-hardening User verification are PASS, including clean
`compileTestJava`, focused L05 tests, inherited vision regressions, the full
suite, and clean build. Final documentation reconciliation and Architect
closure authorization are also PASS. V00_L05 is now
`COMPLETE / FROZEN / READ-ONLY`; its pure deterministic canonical AprilTag
robot-pose candidate estimation remains the locked concept:

```text
fieldToTag + cameraToTarget + robotToCamera
    -> canonical Blue-origin fieldToRobot pose candidate
```

Simulation, Driver Station, Glass, and real-robot verification remain
`NOT APPLICABLE` because V00_L05 adds no runtime camera or hardware integration.

The approved package is `frc.robot.vision`; the approved class is the final,
stateless, non-instantiable `AprilTagRobotPoseEstimator`; and the only approved
public method is
`estimateFieldToRobotCandidate(Pose3d, Transform3d, Transform3d)`. The exact
mathematics, structural validation, output semantics, dependency ownership,
test matrix, and V00_L06 quality boundary are recorded in the lesson
documentation. No runtime wiring, camera vendor, telemetry, Swerve, alliance
transform, or pose fusion is authorized.

V00_L01-L06 remain complete, frozen, protected, and published. V00_L06 is
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 1327bf4 / USER VERIFIED` after
the User-confirmed publication commit
`1327bf41736c8fe79ba58ec5eea9e0120bd978fb` (`Complete V00_L06 vision
measurement quality contract`) and lesson-local metadata reconciliation
`49c4286` (`Reconcile V00_L06 publication metadata`). The original V00_L07
lesson publication remains historical pre-repair evidence:
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ d58bef0 / USER VERIFIED`.
Its User-confirmed publication commit is
`d58bef0d17d202ce1dd0b8645635a8c35095dd3f` (`Complete V00_L07 vision
timestamp and latency contract`) and its lesson-local metadata reconciliation
is `618dd09` (`Reconcile V00_L07 publication metadata`).

At exceptional-reopen activation, V00_L07 became the sole current editable
lesson under an Architect/User-approved reopen:
`IN_PROGRESS / REOPENED / EDITABLE`. The reopen was limited to inherited
Swerve integrity repairs R1/R2/R3; implementation, fresh verification,
re-freeze, and repair publication were then pending. The original d58bef0
publication remains historical pre-repair evidence.

The authorized V00_L07 repair is now implemented and documented. The fresh
pre-repair baseline passed with 593/593 tests and a clean build; the post-repair
full suite passed with 600/600 tests and a clean build. Runtime WPILib
Simulation and the post-implementation read-only architecture/Frozen Backbone
review passed.
At an earlier stage, real-robot verification was deferred because the robot was
unavailable. Later User-supplied evidence verifies Teleop and Autonomous
usability. No stronger bounded stop, Disable, or no-unintended-restart claim is
added beyond that supplied evidence. The separately observed BL quantitative
drivetrain anomaly remains `KNOWN / DEFERRED HARDWARE MAINTENANCE`; it is not BL
PASS, quantitative drivetrain PASS, completed tuning/calibration, or a resolved
issue, and the approved disposition does not make it a Vision-curriculum
closure blocker.

The final read-only closure review passed and the Architect/User explicitly
authorized re-freeze. V00_L07 is now `COMPLETE / FROZEN / READ-ONLY / PUBLISHED
@ 4704cfc / USER VERIFIED`; no V00 lesson is active. The corrected repair
publication commit is
`4704cfc0801910e30c8abb7cffcc467e4f4df016` with subject `Complete corrected
V00_L07 Swerve integrity repair`. Historical `d58bef0` is not that corrected
repair publication.
The preceding V00_L08 lifecycle statement is historical and is superseded for
the existing candidate by the controlled repair activation below. V00_L09 is
not started. A01_L10 remains prohibited.

## Current V00_L08 final closure and freeze state - 2026-09-10

The User authorized the bounded V00_L08 repair based on the final Astra closure
audit. V00_L08 is now `COMPLETE / FROZEN / READ-ONLY`, and no V00 lesson is
active. The repair was
limited to real-adapter freshness/coherence,
the periodic observation-only runtime owner, read-only diagnostics, the private
Limelight schema boundary, directly related tests, and L08/root documentation.
V00_L09 fusion, Swerve/drivetrain/IO/tuning/calibration, autonomous behavior,
PathPlanner, vendor dependency/configuration changes, and H1 promotion remain
excluded.

The adapter requires heartbeat progression and fresh target-pose evidence after
both the prior target-refresh boundary and the prior heartbeat-change boundary
before accepting a target. Stable current `tv` and `tid` values remain
required structural fields; a changed heartbeat, tag id, or visibility field
alone is insufficient. Target changes observed while the heartbeat is stalled
are not recovery evidence. Missing, reset, reconnect, stale, partial,
malformed, or unstable data fails closed.
Independent NetworkTables topics are not atomic; two equal target snapshots
plus heartbeat rechecks provide only a read-stability and bounded-coherence
guard, not an atomic frame guarantee. `tv == 0` is an invalid acquisition
sample. Both real and simulation VisionIO use the same path:

```text
VisionSubsystem / immutable VisionObservation
        |
        +----> read-only Vision telemetry
        |
        +----> future V00_L09 estimator-fusion boundary
```

RobotContainer remains the composition root.

The bounded repair and its verification gates are PASS by User evidence:
the focused adapter tests are 37/37 PASS, the full regression is 642/642 PASS,
and the clean build is PASS under Java 17. The earlier 642-test result of
637 passed and five isolated failures remains historical pre-repair evidence;
those five defects are no longer a current automated hold.

User-owned runtime evidence is also PASS. WPILib Simulation and Driver
Station/Glass confirm the Java observation path and telemetry; the inherited
default `VisionIOSim` state is `UNAVAILABLE` with no target, which is an
acceptable deterministic simulation result. This does not claim that the real
Limelight adapter or LimelightOS was simulated. On the real robot, Limelight 4
target acquisition for AprilTag 32, target loss, and reacquisition were
observed through Java telemetry with the expected `TARGETS_PRESENT` and
`INVALID_SAMPLE` states. H1 remains the **PROVISIONAL COMMISSIONING LOCK** and
is not promoted to official or proven vendor semantics. The final architecture
and closure review returned `PASS_V00_L08_FINAL_CLOSURE_REVIEW`; transition
documentation and the final documentation reconciliation are PASS. Codex did
not run Git.

At the V00_L08 closure point, the final lesson content/state was
`COMPLETE / FROZEN / READ-ONLY`. V00_L09 was still future work at that
historical point and no fusion implementation was added. The later controlled
V00_L09 activation is recorded below. The User still owns Git publication.

Publication: `PUBLISHED @ f34b210 / USER VERIFIED`; message `Complete V00_L08
real vision adapter integration`; push PASS to `origin/main`.

## Current V00_L09 implementation, evidence, and documentation state - 2026-09-15

V00_L08 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ f34b210`. Its
implementation publication is `f34b210` with message `Complete V00_L08 real vision adapter
integration`. The User separately identifies `6415b17` as the later V00_L08
publication-metadata reconciliation; that history remains distinct from the
predecessor implementation publication, and no V00_L08 file is modified by
this activation.

The prepared ADR-locked
`V00_L09_SwervePoseEstimatorVisionFusion` candidate became the sole active V00
lesson during implementation and is now closed by explicit Architect freeze
authorization:

```text
STATUS: COMPLETE
ACTIVE STATE: COMPLETE / FROZEN / READ-ONLY
FREEZE STATE: FROZEN / READ-ONLY
ACTIVE LESSON COUNT: 0
GIT COMMIT: 6548c98
COMMIT MESSAGE: Complete V00_L09 Swerve pose estimator vision fusion
GIT PUSH: COMPLETE / VERIFIED
REMOTE: origin/main = 5d36529
ORIGIN/HEAD: origin/HEAD = 5d36529
PUBLICATION: PUBLISHED / VERIFIED
FINAL PUBLICATION GATE: PASS_V00_L09_REMOTE_PUBLICATION_VERIFIED
METADATA RECONCILIATION COMMIT: 5d36529
METADATA COMMIT MESSAGE: Record V00_L09 publication metadata
FINAL REPOSITORY STATE: HEAD = origin/main = origin/HEAD = 5d36529
SYNC STATE: ahead = 0 / behind = 0
V00 FINAL CLOSURE GATE: PASS_V00_MODULE_FINAL_CLOSURE_CONFIRMED
```

The User-supplied evidence records candidate preparation, generated-artifact
cleanup, inherited baseline clean-build PASS, roadmap identity reconciliation,
architecture audit PASS, timing-source investigation PASS, runtime
orchestration micro-audit PASS, coordinator decision PASS, and the Architect
gate `PASS_V00_L09_FINAL_DESIGN_LOCK`.

The one locked L09 concept is:

```text
qualified timestamped AprilTag measurement
    -> post-scheduler VisionFusionCoordinator requirement
    -> guarded Swerve-owned admission
    -> SwerveDrivePoseEstimator.addVisionMeasurement(...)
```

Swerve remains the sole owner of estimator state. Vision remains vendor-neutral
above IO. RobotContainer remains the composition root. No MegaTag migration,
dynamic quality covariance, Constants tuning, or unrelated architecture
change is included.

The locked implementation is complete. User-verified focused and inherited
tests, direct A-G failure/admission evidence, `compileTestJava`, the full
637/637 suite, and clean build are PASS; the preserved generated report records
637 tests with zero failures, errors, and skipped tests. The narrow
failure-boundary test repair changed only invalid test fixtures and reused the
existing deterministic quality-valid VisionIOSimHarness Frame A/B path;
production policy and behavior were not changed. Simulation Gate 1, estimator
initialization, Simulation Gate 2, and Driver Station / Glass are PASS.

Deployed V00_L09 Limelight flat-root `/limelight/json` parser, timing/result,
Available/Connected/SampleValid, and AprilTag 32 acquisition verification are
PASS. Real estimator initialization, stationary accepted fusion, positive
accepted-fusion count, geometry consistency, controlled translation/rotation,
target loss/reacquisition, and camera disconnect/recovery are PASS.

The evidence classifications are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and
`REAL HARDWARE VERIFIED`. For bounded autonomous Gate 9, BLUE `ONE_METER_PATH`
preparation and completion passed. Vision was intentionally invalid/suppressed
because the physical Tag 32 placement was not asserted to match the official
field coordinate. Gate 9 proves bounded autonomous lifecycle and estimator
compatibility; it does not claim exact 1.000 m endpoint accuracy or physical
absolute-pose calibration accuracy.

The final independent architecture review passed with
`PASS_V00_L09_FINAL_ARCHITECTURE_REVIEW_READY_FOR_DOCUMENTATION_CLOSURE`. Final
documentation review passed with
`PASS_V00_L09_FINAL_DOCUMENTATION_REVIEW_READY_FOR_FREEZE_AUTHORIZATION`, and
the Architect issued `PASS_V00_L09_FINAL_FREEZE_AUTHORIZATION`. The
documentation-only lifecycle transition is now recorded as
`COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`.

One historical Driver Station overrun warning remains non-proven; if it recurs,
capture WPILib/Driver Station timing epochs before any performance repair. User
Git commit `6548c98` and remote publication are verified. The distinct
metadata-reconciliation commit is User-published at `5d36529` with subject
`Record V00_L09 publication metadata`.

The User supplied the final repository evidence: `HEAD = origin/main =
origin/HEAD = 5d36529`, ahead `0`, and behind `0`. The implementation/freeze
publication remains `6548c98`; the later metadata reconciliation is `5d36529`.
The final gates are `PASS_V00_L09_REMOTE_PUBLICATION_VERIFIED` and
`PASS_V00_MODULE_FINAL_CLOSURE_CONFIRMED`. This reconciliation changes
documentation metadata only and does not alter the accepted technical evidence,
Gate 9 qualification, or historical overrun wording.

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

## M00_L01 final publication state — 2026-09-15

The controlling successor record is
`docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md`.
The M00 roadmap is `APPROVED / ROADMAP AUTHORIZED`. Preparation was authorized
by `PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED`, published, completed by the
User, and accepted through the inherited Java 17 baseline build and independent
Architecture / Inheritance Audit. The Architect Design Lock is
`PASS_M00_L01_FINAL_DESIGN_LOCK`.

```text
Active Lesson Count: 0
```

The locked roadmap contains exactly 16 lessons, `M00_L01` through `M00_L16`, in
the ADR-defined order. The first lesson is exactly
`M00_L01 - Mechanism Architecture Reuse`, with locked directory identity
`M00_L01_MechanismArchitectureReuse`. It is now `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED / VERIFIED`, and no M00 lesson is active. Implementation
authorization and production-code authorization remain `NONE`.

Its exact predecessor is the complete frozen V00_L09 snapshot at repository
state `5d36529`, with implementation/freeze commit `6548c98`:

```text
C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7\real_robot_programming\module_V00\V00_L09_SwervePoseEstimatorVisionFusion
```

D01 is not the predecessor. The exact future destination is:

```text
C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7\real_robot_programming\module_M00\M00_L01_MechanismArchitectureReuse
```

The User completed the authorized preparation sequence: copy the complete
frozen V00_L09 directory, create `module_M00` as part of that workflow, rename
only the destination copy, remove only its copied `build\` and `.gradle\`, and
run the inherited baseline clean build under WPILib 2026 Java 17. The accepted
result is `BUILD SUCCESSFUL in 20s` with 7 actionable tasks executed. The
recorded command was:

```powershell
$env:JAVA_HOME = "C:\Users\Public\wpilib\2026\jdk"
.\gradlew.bat clean build "-Dorg.gradle.java.home=C:\Users\Public\wpilib\2026\jdk"
```

The Architecture / Inheritance Audit passed with 601 comparable non-generated
files on each side, zero missing or candidate-only files, and zero SHA-256
differences. The Architect Design Lock then passed, and this documentation-only
controlled activation reconciled M00_L01 to `IN_PROGRESS / EDITABLE` with
active lesson count `1`. M00 preserves the Frozen Backbone,
Frozen Interface Contract, Constants authority, frozen lesson protection, and
one-lesson/one-new-concept rule. RobotContainer remains composition root only;
vendor APIs remain inside concrete IO adapters; mechanism data follows
`hardware -> IOInputs -> subsystem/processing -> immutable Observation ->
read-only telemetry`. Intake, Feeder, Flywheel, and Elevator retain independent
ownership. Shooting remains `FlywheelSubsystem + FeederSubsystem +
ShootCommand`; no `ShooterSubsystem` or `ShooterIO` is authorized absent a
later formal change.

M00_L01's sole concept is Mechanism Architecture Reuse: how mastered
drivetrain, vision, and autonomous architecture applies to non-drivetrain
mechanisms. It may teach ownership, IO, immutable Observations, read-only
telemetry, composition-root assembly, safe stop, and architecture mapping. It
must not implement mechanisms, control/readiness behavior, elevator behavior,
homing, travel limits, coordination, autonomous events, or a new hardware API.

Future student-facing M00 Markdown must use separate English and Vietnamese
files with identical structure, course/chapter identity, meaning, evidence, and
architecture rules. English is normative; Vietnamese is student-friendly but
semantically equivalent. Evidence labels are limited to `THEORY VERIFIED`,
`SIMULATION VERIFIED`, `REAL HARDWARE VERIFIED`, `REAL HARDWARE DEFERRED`, and
`NOT APPLICABLE`. Under the approved Design Lock, M00_L01 is architecture-only:
Simulation, Driver Station / Glass, and real hardware are `NOT APPLICABLE`.
Focused new tests and test implementation are also `NOT APPLICABLE` / `NONE`.
The paired English and Vietnamese student learning guides are complete and
reviewed with identical 21-section structure and equivalent technical meaning.
The initial independent review returned
`HOLD_M00_L01_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_CONSTANTS_AUTHORITY`.
The minimal repair added the missing `Constants.java` default-configuration-
authority statement without defining mechanism configuration values and passed
at `PASS_M00_L01_CONSTANTS_AUTHORITY_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`; the
independent rereview passed at
`PASS_M00_L01_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD`.
The Architect documentation review then passed at
`PASS_M00_L01_DOCUMENTATION_REVIEW_COMPLETE_READY_FOR_FINAL_BUILD`.

The User-supplied final inherited clean build/regression is distinct from the
20-second preparation baseline and passed with `BUILD SUCCESSFUL in 23s`; 7
actionable tasks were executed. Its gate is
`PASS_M00_L01_FINAL_INHERITED_CLEAN_BUILD_REGRESSION`. The final read-only
closure review passed at
`PASS_M00_L01_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION` and
was accepted through
`PASS_M00_L01_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
Technical/content closure readiness is `PASS`.

The controlled documentation reconciliation then passed at
`PASS_M00_L01_DOCUMENTATION_RECONCILED_READY_FOR_FREEZE_AUTHORIZATION`, was
accepted for independent review at
`PASS_M00_L01_DOCUMENTATION_RECONCILIATION_ACCEPTED_READY_FOR_INDEPENDENT_REVIEW`,
and passed independent review at
`PASS_M00_L01_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`.
The Architect issued `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION`.

At the freeze-recording point, M00_L01 was `COMPLETE / FROZEN / READ-ONLY`
with active lesson count `0`, freeze state `FROZEN / READ-ONLY`, Design Lock
`PASS_M00_L01_FINAL_DESIGN_LOCK`, and production-code authorization `NONE`.
Technical/content work is complete; evidence remains `THEORY VERIFIED`, and
runtime verification remains `NOT APPLICABLE`. Publication, Git commit, Git
push, and remote verification were then pending.

The User subsequently published the frozen lesson at commit `83907ab` with
subject `Complete M00_L01 mechanism architecture reuse`. User-supplied evidence
records `HEAD = origin/main = origin/HEAD = 83907ab` on branch `main`; remote
verification passed at `PASS_M00_L01_REMOTE_PUBLICATION_VERIFIED`.

M00_L01 is therefore `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`,
with active lesson count `0`. M00_L02 is `NOT ACTIVE / NOT CREATED`, and the M00
module is not declared complete. The distinct publication-metadata
reconciliation remains `PENDING USER COMMIT`, its push remains `PENDING USER
PUSH`, and final remote verification of that later metadata commit remains
`PENDING`. No V00 runtime evidence is reused as M00_L01 verification.

## M00_L02 final freeze and lifecycle closure — 2026-09-16

M00_L01 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED` at
primary publication `83907ab` and metadata publication `f523118`. The Architect
issued `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION`, and M00_L02 is now frozen:

```text
Lesson: M00_L02 - Mechanism Hardware Evidence Audit
Filesystem: M00_L02_MechanismHardwareEvidenceAudit
Status: COMPLETE
Active State: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN / READ-ONLY
Active Lesson Count: 0
Design Lock: PASS_M00_L02_FINAL_DESIGN_LOCK
Technical Implementation: NONE AUTHORIZED
Documentation Implementation: COMPLETE
Independent Documentation Rereview: PASS
Final User Build: PASS
Final Closure Review: PASS
Documentation Reconciliation: COMPLETE / RECORDED
Independent Reconciliation Review: PASS
Freeze Authorization: PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION
Theory: VERIFIED
Simulation: NOT APPLICABLE
Driver Station / Glass: NOT APPLICABLE
Real Hardware: DEFERRED
Primary Publication Commit: 65a92a4a5806fd5134e0114e851c4e4cc093c58e
Primary Publication Push: PASS
Publication Metadata Reconciliation: COMPLETE / RECORDED
Final Publication: PENDING METADATA COMMIT/PUSH/REMOTE VERIFICATION
M00_L03: NOT ACTIVE / NOT CREATED
```

The accepted inherited baseline is `BUILD SUCCESSFUL in 58s` with 637 tests,
zero failures, zero errors, and zero skipped. Post-repair inheritance is
604/604 comparable files and 173/173 protected files with zero missing, extra,
or SHA-256-different files. The accidental nested M00_L01 project was removed
through the controlled repair and verified absent.

The sole concept is `MECHANISM HARDWARE EVIDENCE AUDIT`: distinguish facts that
are `VERIFIED`, `PROVISIONAL`, `UNKNOWN`, or `NOT APPLICABLE`. Unsupported
physical facts may not be promoted to `VERIFIED`, and no hardware value may be
invented. Evidence is `THEORY VERIFIED` as a required lesson gate; focused new
tests, Simulation, and Driver Station / Glass are `NOT APPLICABLE`; real
hardware is `REAL HARDWARE DEFERRED`.

Production Java, tests, configuration, vendordeps, deploy assets, runtime
behavior, and mechanism APIs remain unauthorized. The paired English and
Vietnamese learning guides were separately authorized and implemented with
matching 25-section structure, 15 questions, 15 answers, and 80-row evidence
matrices containing 8 `VERIFIED`, 0 `PROVISIONAL`, and 72 `UNKNOWN` rows.

The initial independent documentation review found the bounded missing
Constants authorization boundary, ownership/shooting lock, and knowledge-check
coverage. The authorized minimal two-guide repair resolved those findings, and
the independent rereview passed. The User-supplied final build/regression passed
with `BUILD SUCCESSFUL in 33s` and all 7 actionable tasks executed. The final
closure review passed, and documentation reconciliation is complete and
recorded at `PASS_M00_L02_DOCUMENTATION_RECONCILED_READY_FOR_INDEPENDENT_REVIEW`.
The independent reconciliation review passed, and the Architect-authorized
transition to `COMPLETE / FROZEN / READ-ONLY` is recorded with active lesson
count `0`. Theory is `VERIFIED`; Simulation and Driver Station / Glass are `NOT
APPLICABLE`; real hardware remains `DEFERRED`. The User completed the primary
publication at full commit `65a92a4a5806fd5134e0114e851c4e4cc093c58e` with
push `PASS`. Publication metadata reconciliation is now `COMPLETE / RECORDED`.
The distinct metadata commit, metadata push, final remote verification, and
final publication completion remain pending. M00_L03 exists in the roadmap but
remains `NOT ACTIVE / NOT CREATED`.

## M00_L03 controlled lifecycle activation — 2026-09-16

M00_L02 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED` at
primary publication `65a92a4a5806fd5134e0114e851c4e4cc093c58e` and metadata
publication `84010ff5022a33a946888fafedbbca0d67439e0c`. M00_L03 is now the sole
active lesson:

```text
Lesson: M00_L03 - Intake Foundation
Filesystem: M00_L03_IntakeFoundation
Status: IN_PROGRESS
Active State: IN_PROGRESS / EDITABLE
Freeze State: EDITABLE
Active Lesson Count: 1
Sole Concept: AN INDEPENDENTLY OWNED, VENDOR-NEUTRAL INTAKE MECHANISM FOUNDATION
Final Design Lock: PASS_M00_L03_FINAL_DESIGN_LOCK
Implementation: PENDING SEPARATE AUTHORIZATION
M00_L04: NOT ACTIVE / NOT CREATED
```

This documentation-only activation introduces no Intake Java, tests,
configuration, vendor adapter, Constants change, command, binding, or runtime
behavior. Focused tests and bounded Fake/Noop software simulation are required
later; Driver Station / Glass are not completion gates, and real hardware is
deferred.

## M00_L03 implementation and student-documentation reconciliation — 2026-09-16

The activation record above is historical. Separate production/test
authorization was issued, and the bounded vendor-neutral Intake foundation is
now implemented. The initial focused run recorded 16 tests with 15 passing and
one test-defect failure caused by an optional callback dereference in
`IntakeSubsystemTest`. A minimal guard-only repair removed no assertions. The
focused retest passed (`BUILD SUCCESSFUL in 4s`, exit code `0`), and the full
clean regression passed (`BUILD SUCCESSFUL in 20s`, exit code `0`, 7 of 7
actionable tasks executed).

Bounded Simulation passed for startup, Noop composition, subsystem integration,
and Intake telemetry presence while Disabled. It does not prove physical
hardware behavior. Real-hardware verification remains deferred, no Intake
vendor adapter was added, and `Constants.java` is unchanged.

The independent implementation review passed. The authorized English and
Vietnamese learning guides are implemented and await independent documentation
review. M00_L03 remains the sole `IN_PROGRESS / EDITABLE` lesson with active
lesson count `1`; final closure build, closure review, freeze authorization,
`COMPLETE / FROZEN / READ-ONLY`, and User publication remain pending. M00_L04
remains `NOT ACTIVE / NOT CREATED`.

## M00_L03 final closure review and lifecycle reconciliation — 2026-09-17

The initial independent documentation review returned `HOLD` because Step 16
of the transition guide described the mutable one-cycle `IntakeIOInputs`
transport snapshot as immutable. The bounded one-line repair corrected the
phrase to `mutable one-cycle input snapshot`. Independent documentation
rereview passed and was accepted by the Architect. The English and Vietnamese
student guides remain unchanged and verified with matching 34-section,
15-question, and 15-answer structures.

The User-supplied final Java 17 closure build passed with `BUILD SUCCESSFUL in
42s`, 7 actionable tasks executed, and exit code `0`. The final read-only
closure review passed at
`PASS_M00_L03_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION` and
was accepted at
`PASS_M00_L03_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
This documentation-only lifecycle reconciliation is complete.

```text
M00_L03: IN_PROGRESS / EDITABLE
Freeze State: EDITABLE
Active Lesson Count: 1
Technical Implementation: COMPLETE
Focused Tests: VERIFIED
Full Regression: VERIFIED
Final Closure Build: VERIFIED
Simulation: VERIFIED — BOUNDED SOFTWARE/NOOP/COMPOSITION ONLY
Driver Station / Glass: NOT APPLICABLE
Real Hardware: REAL HARDWARE DEFERRED
Student Documentation: VERIFIED AFTER BOUNDED REPAIR AND REREVIEW
Final Closure Review: PASS
Lifecycle Reconciliation: COMPLETE
Freeze Authorization: PENDING
Publication: PENDING
M00_L04: NOT ACTIVE / NOT CREATED
```

M00_L03 is not yet `COMPLETE / FROZEN / READ-ONLY`. Independent reconciliation
review, explicit Architect freeze authorization, freeze recording, and
User-owned publication remain pending. No new technical feature work remains.

## M00_L03 final lifecycle freeze — 2026-09-17

The preceding reconciliation section is preserved as the historical
pre-freeze state. The independent reconciliation review passed at
`PASS_M00_L03_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`,
and the Architect issued `PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION`.

```text
M00_L03: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN
Active Lesson Count: 0
Technical Implementation: COMPLETE
Focused Tests: VERIFIED
Full Regression: VERIFIED
Final Closure Build: VERIFIED
Simulation: VERIFIED — BOUNDED SOFTWARE/NOOP/COMPOSITION ONLY
Driver Station / Glass: NOT APPLICABLE
Real Hardware: REAL HARDWARE DEFERRED
Student Documentation: VERIFIED
Final Closure Review: PASS
Lifecycle Reconciliation: COMPLETE
Independent Reconciliation Review: PASS
Freeze: COMPLETE / AUTHORIZED
Architect Freeze Authorization: PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION
Git Publication: PENDING
M00_L04: NOT ACTIVE / NOT CREATED
```

M00_L03 is no longer editable. Remaining work is publication only. This
freeze does not claim a Git commit, push, remote verification, or completed
publication, and it does not create or activate M00_L04.

## M00_L03 primary publication and metadata reconciliation — 2026-09-17

M00_L03 remains `COMPLETE / FROZEN / READ-ONLY`, with freeze state `FROZEN`
and active lesson count `0`. The User completed the primary Git publication at
full commit `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`. The primary push passed,
and post-push local `HEAD` and `origin/main` both matched that commit; primary
remote alignment is `PASS`.

```text
Primary Git Publication: COMPLETE
Primary Commit: 3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3
Primary Push: PASS
Primary Remote Alignment: PASS
Publication Metadata: RECONCILED
Metadata Git Publication: PENDING USER GIT
Final Publication Verification: PENDING
M00_L04: NOT ACTIVE / NOT CREATED
```

This metadata reconciliation is complete, but final publication is not. The
remaining sequence is independent metadata review, User-owned metadata Git
publication, metadata remote-alignment verification, and final publication
verification. M00_L04 remains inactive and uncreated.

## M00_L03 final publication and M00_L04 controlled activation — 2026-09-18

M00_L03 completed its two-commit publication sequence at primary commit
`3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3` and metadata commit
`b2464f66da42a6281acb7bfc709f2a3b83296505`. Metadata remote alignment and
final publication verification passed. M00_L03 is now accepted as
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`.

M00_L04 is the sole active lesson:

```text
Lesson: M00_L04 - Intake Command Ownership
State: IN_PROGRESS / EDITABLE
Freeze State: EDITABLE
Active Lesson Count: 1
Current Phase: CONTROLLED ACTIVATION COMPLETE
Final Design Lock: PASS_M00_L04_FINAL_DESIGN_LOCK
Implementation: NOT STARTED
Implementation Authorization: PENDING
One New Concept: SCHEDULER-MANAGED MANUAL INTAKE OWNERSHIP
Controller Binding: RIGHT BUMPER / whileTrue / RunIntakeCommand
Simulation: NOT RUN
Driver Station: NOT RUN
Glass: NOT APPLICABLE
Real Hardware: REAL HARDWARE DEFERRED
M00_L05: NOT ACTIVE / NOT CREATED
```

The locked future command will require the existing `IntakeSubsystem`, request
Intake once during initialization, remain scheduled while held, and call the
existing subsystem `stop()` unconditionally when ending. Observation,
telemetry, IO, Constants, and hardware selection remain unchanged.

## M00_L04 post-verification documentation state — 2026-09-18

The preceding activation record is historical. The separately authorized
implementation is complete within the exact production/test boundary. The
implemented path is:

```text
Right Bumper
-> whileTrue
-> RunIntakeCommand
-> IntakeSubsystem
-> existing requestIntake() / stop()
```

`RunIntakeCommand` requires exactly `IntakeSubsystem`, requests Intake once in
`initialize()`, performs no repeated request in `execute()`, returns `false`
from `isFinished()`, and unconditionally calls `IntakeSubsystem.stop()` from
`end(...)`. There is no Intake default command. The inherited Back/View
Prepare Autonomous binding remains present.

User evidence records 14/14 focused tests PASS (`BUILD SUCCESSFUL in 28s`,
four tasks executed, exit code 0) and full clean regression PASS (`BUILD
SUCCESSFUL in 47s`, seven tasks executed, exit code 0). Simulation is
`SIMULATION VERIFIED / BOUNDED`. Driver Station verification is `VERIFIED /
BOUNDED` and observed `STOPPED -> INTAKE_REQUESTED -> STOPPED` for Right Bumper
hold and release. Glass is `NOT APPLICABLE` as a distinct completion gate.

The selected implementation remains `IntakeIONoop`; therefore
`Available=false` and `Connected=false` are expected while `RequestedState`
records software intent. This evidence does not prove physical Intake motion
or stopping. Real hardware remains `REAL HARDWARE DEFERRED`.

The independent implementation review is PASS. The paired
[English](real_robot_programming/module_M00/M00_L04_IntakeCommandOwnership/docs/M00_L04_Intake_Command_Ownership_Learning_Guide_EN.md)
and
[Vietnamese](real_robot_programming/module_M00/M00_L04_IntakeCommandOwnership/docs/M00_L04_Intake_Command_Ownership_Learning_Guide_VI.md)
learning guides are created and await independent documentation review.
M00_L04 remains the sole `IN_PROGRESS / EDITABLE` lesson with active lesson
count `1`; freeze is not authorized, publication is not started, and M00_L05
is `NOT ACTIVE / NOT CREATED`.

## M00_L04 final lifecycle reconciliation and freeze — 2026-09-18

The preceding section is preserved as historical pre-closure state. The
documentation review/repair/rereview sequence, transition-history
reconciliation and independent confirmation, final User closure build, and
resumed independent final closure review are complete and accepted. The final
closure build passed with `BUILD SUCCESSFUL in 23s`, 7/7 actionable tasks
executed, and exit code `0`.

M00_L04 established scheduler-managed manual Intake ownership through the
semantic Right Bumper `whileTrue` binding. `RunIntakeCommand` requests Intake
once during `initialize()`, retains scheduler ownership while held, and
unconditionally delegates to `IntakeSubsystem.stop()` when ending. Focused
tests passed 14/14, full regression passed, Simulation is `SIMULATION VERIFIED
/ BOUNDED`, and Driver Station verification is `VERIFIED / BOUNDED` for
`STOPPED -> INTAKE_REQUESTED -> STOPPED`.

`IntakeIONoop` remains deterministic and vendor-neutral. It intentionally
reports `Available=false` and `Connected=false`, produces no physical output,
and supports bounded software verification only. Glass is `NOT APPLICABLE` as
a distinct completion gate, and real hardware remains `REAL HARDWARE DEFERRED`.

```text
M00_L03: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L04: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN
Editable Boundary: NONE
Active Lesson: NO
Active Lesson Count: 0
Documentation: COMPLETE / VERIFIED
Final Closure Review: PASS
Git Publication: PENDING USER ACTION
Publication State: NOT YET PUBLISHED
M00_L05: NOT ACTIVE / NOT CREATED
```

No M00 lesson is active. This lifecycle reconciliation does not publish
M00_L04, claim a commit or push, or activate M00_L05.

## M00_L04 primary publication and metadata reconciliation — 2026-09-18

The preceding lifecycle section is preserved as historical pre-publication
state. Primary M00_L04 publication is complete at commit `5c86be3` with
subject `Complete M00_L04 intake command ownership`. Accepted post-push
evidence records `HEAD`, `origin/main`, and `origin/HEAD` at `5c86be3`, so
primary remote alignment is `PASS`.

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

The metadata reconciliation is prepared but has not been committed. Its
User-owned metadata commit/push, remote verification, and final publication
verification remain pending. No future metadata SHA is claimed.

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
