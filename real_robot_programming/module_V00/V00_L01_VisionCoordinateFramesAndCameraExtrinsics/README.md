# V00_L01 - Vision Coordinate Frames and Camera Extrinsics

## Lesson State

- Module: `V00 - AprilTag Vision Observation and Pose Fusion`
- Lesson: `V00_L01_VisionCoordinateFramesAndCameraExtrinsics`
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Implementation: `COMPLETE / VERIFIED`
- Documentation reconciliation: `COMPLETE / PASS`
- Final Architecture Review: `PASS`
- Final Closure Review: `PASS`
- Freeze State: `FROZEN / READ-ONLY`
- Git publication: `PUBLISHED @ 7d52ebf / USER VERIFIED`

The reconstructed lesson passed final architecture and closure review and is
now a frozen, read-only lesson snapshot. User-owned Git publication was
confirmed at `7d52ebf`.

## Authoritative Inheritance

The authoritative predecessor is:

`A01_L09 @ 6b243bb - Complete reconstructed A01_L09 named commands and event markers`

A01 ends at A01_L09; A01_L10 is prohibited. The earlier historical V00_L01
predated the final reconstructed A01_L09 baseline and therefore became stale.
Continuing from it would have omitted accepted A01 safety/event architecture
and violated the immediate-predecessor inheritance rule.

The canonical V00_L01 was consequently reconstructed from final A01_L09 in an
isolated candidate, audited, verified, and transferred into this lesson
directory. The current source/test comparison establishes this boundary:

| Classification | Canonical content |
| --- | --- |
| Inherited from final A01_L09 | 73 production files and 56 test files are hash-identical to the authoritative predecessor. Gradle, vendordeps, and deploy assets are also preserved. |
| New in V00_L01 | `src/main/java/frc/robot/vision/VisionFrameTransform.java` and `src/test/java/frc/robot/vision/VisionFrameTransformTest.java` only. |
| Historical stale locations | The old `frc.robot.observation.vision` helper/test and `frc.robot.commands.AutonomousEventId` are absent. |

This documentation records the user-supplied commit identity; Codex did not
run a Git command to derive or alter it.

## One Learning Objective

V00_L01 establishes the pure geometry foundation for later vision lessons:

- canonical field, robot-body, and camera coordinate frames;
- WPILib NWU axes and right-handed rotation semantics;
- a fixed `robotToCamera` mounting extrinsic;
- rigid 3D composition and inversion; and
- reconstruction of a robot field pose from a known camera field pose.

The locked API is:

```java
public static Pose3d fieldToCamera(
    Pose3d fieldToRobot,
    Transform3d robotToCamera)

public static Transform3d cameraToRobot(
    Transform3d robotToCamera)

public static Pose3d fieldToRobotFromCamera(
    Pose3d fieldToCamera,
    Transform3d robotToCamera)
```

The helper is stateless, non-instantiable, deterministic, vendor-neutral, and
limited to WPILib geometry. It belongs in `frc.robot.vision` because it
performs frame mathematics; it is not a sampled fact or immutable mechanism
Observation and therefore does not belong in `frc.robot.observation.vision`.

## Mathematics Contract

All translations use meters. Rotations use radians and WPILib's right-handed
NWU convention: +X forward, +Y left, and +Z up.

```text
cameraToRobot = inverse(robotToCamera)
fieldToCamera = fieldToRobot.transformBy(robotToCamera)
fieldToRobot  = fieldToCamera.transformBy(cameraToRobot)
```

Composition order matters because the mounting translation is expressed in
robot axes. A robot at `(1 m, 2 m, 0 m)` with yaw `+90 degrees` and a camera
mounted `1 m` robot-forward places the camera at `(1 m, 3 m, 0 m)`. It is not
at `(2 m, 2 m, 0 m)`, because robot-forward points along field +Y after the
robot rotates.

## Preserved A01 Architecture

The reconstructed baseline preserves the Frozen Backbone:

```text
Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware
```

It also preserves the mechanism observation flow:

```text
hardware -> IOInputs -> subsystem/estimator -> immutable Observation
         -> telemetry -> NT4 / Glass / log
```

Final A01_L09 safety/event behavior is inherited unchanged, including the
preparation coordinator and observation/telemetry path, scheduler-native
AutoBuilder composition, Robot-level scheduler exception boundary, fatal-fault
bridge, terminal `HOLDING`, centralized Swerve stop authority, defensive
Teleop-enabled output gate, `frc.robot.autonomous.AutonomousEventId`, deferred
fresh event commands, NamedCommands/event markers, and the prohibition on
manual child-command lifecycle delegation.

The V00_L01 helper is not wired into RobotContainer, autonomous, Swerve, IO,
telemetry, or hardware. It changes neither control nor observation flow.

## Verification Evidence

Authoritative User verification of the reconstructed canonical lesson under
Java 17 records:

- Clean: `PASS`.
- Focused `VisionFrameTransformTest`: `PASS`.
- Full build: `PASS`.
- Accidental `-Recurse` artifact: `ABSENT`.

The candidate had previously passed independent mathematical-oracle review,
the focused Vision test, and the full build. No unsupported command line,
duration, measurement, test count, or Git result is claimed here.

Simulation, Driver Station / Glass, and real-robot testing are `NOT APPLICABLE`
for this lesson's new concept. V00_L01 adds no runtime camera acquisition,
VisionIO, NetworkTables behavior, scheduler behavior, drivetrain behavior,
AprilTag lookup, pose-estimator integration, fusion, or robot actuation.

## Explicitly Deferred

V00_L01 does not add or claim:

- VisionIO or a camera vendor such as PhotonVision or Limelight;
- camera hardware or physical camera calibration values;
- AprilTag field-layout lookup, target selection, or quality evaluation;
- timestamps or latency compensation;
- pose-estimator integration or vision fusion;
- autonomous, PathPlanner, Swerve, telemetry, or hardware behavior changes.

Those responsibilities remain with their later V00 lessons and required
reviews. No physical mounting values may be guessed or stored as production
authority in this lesson.

## Inherited Documentation Classification

The 61 files currently inherited under `docs/` remain byte-identical to final
A01_L09 and are preserved as useful curriculum history. The following table is
an exhaustive classification; ranges refer to the matching filenames already
present in this directory and do not authorize creation of a missing lesson.

| Category | Inherited files covered |
| --- | --- |
| **A - Required inherited learning/history** | `New_WPILib_Project_to_S00_L01_Step_by_Step.md`; every existing `S00_L01_to_S00_L02` through `S00_L24_to_A00_L01` transition guide; `S00_L02_Swerve_Hardware_Audit.md`; `S00_L14_Swerve_Hardware_Commissioning_Matrix.md`; `A00_L01_to_A00_L02_Step_by_Step.md`; `A00_L02_to_A00_L03_Step_by_Step.md`; `A00_L04_to_A01_L01_Step_by_Step.md`; `A00_Robot_Autonomous_Architecture_Layers.md`; every existing A01 transition guide from `A01_L01_to_A01_L02` through `A01_L07_to_A01_L08`; `A01_L02_Pose_Targeted_Autonomous_Motion_Verification_Guide.md`; both A01_L06 learning guides; all A01_L07 preactivation/bilingual learning guides; both A01_L08 learning guides; both A01_L09 learning guides; `Blue_Red_Transform_Mathematics.md`; `hardware_map.png`; `L01_to_L05_Autonomous_Learning_Map.md`; `L03_to_L04_to_L05_Data_Flow.md`; `Official_2026_Field_Variants.md`; `Swerve_Robot_Hardware_Map_v2.0.docx`; `Swerve_Robot_Hardware_Map_v2.0.pdf`; `Trajectory_Transform_Semantics.md`; `Transform_Ownership_and_Double_Transform_Prevention.md`; `Unknown_Alliance_Safety_Contract.md`; and `WPILib_Field_Coordinate_System.md`. |
| **B - Required transition evidence** | `A01_L08_to_A01_L09_Step_by_Step.md` and `A01_L09_Phase_2B_Implementation_Record.md`. |
| **C - Stale metadata reconciled in this task** | The copied lesson-root `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`, and `LESSON_CHECKLIST.md`. These are not part of the 61-file `docs/` count. |
| **D - Unnecessary duplicate** | None identified. |

No inherited document was deleted, renamed, or rewritten.

The new V00 transition guide and bilingual learning guides are additive lesson
documents, not renamed A01 history.

## Protected Successor and Closure Boundary

`V00_L02_AprilTagFieldLayoutContract` is a separate
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED` lesson at `53e9b9f`. It is not this
lesson's predecessor; its later controlled reconstruction and publication did
not change V00_L01.

Final closure sequence:

```text
documentation reconciliation complete
    -> Final Architecture Review PASS
    -> Final Closure Review PASS
    -> COMPLETE / FROZEN / READ-ONLY
    -> User-owned Git add/commit/push confirmed @ 7d52ebf
```

V00_L01 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 7d52ebf`. V00_L02 is
separately `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 53e9b9f`; it was not
resumed or modified by this V00_L01 closure.
