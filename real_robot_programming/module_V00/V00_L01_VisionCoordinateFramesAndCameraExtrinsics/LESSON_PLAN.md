# V00_L01 - Documentation Reconciliation Plan and Record

## Current State

- Lesson: `V00_L01_VisionCoordinateFramesAndCameraExtrinsics`
- Authoritative predecessor: `A01_L09 @ 6b243bb`.
- Status: `COMPLETE / FROZEN / READ-ONLY`.
- Production implementation: `COMPLETE / VERIFIED / NO CHANGE AUTHORIZED`.
- Test implementation: `COMPLETE / VERIFIED / NO CHANGE AUTHORIZED`.
- Documentation reconciliation: `COMPLETE / PASS`.
- Final Architecture Review: `PASS`.
- Final Closure Review and freeze: `PASS / APPROVED`.
- Git publication: `PUBLISHED @ 7d52ebf / USER VERIFIED`.

## Reason for Reconstruction

The historical V00_L01 baseline was not allowed to remain authoritative after
final A01_L09 was reconstructed and published. It predated the accepted A01
safety/event lineage. Treating it as the predecessor would have skipped the
immediately preceding frozen lesson and preserved stale package locations.

The approved remedy was a controlled reconstruction:

```text
final A01_L09 @ 6b243bb
    -> isolated V00_L01 candidate
    -> inheritance and architecture audit
    -> locked Vision implementation and independent oracle tests
    -> candidate focused/full verification
    -> controlled canonical transfer
    -> canonical clean/focused/full User verification
    -> documentation reconciliation
```

No historical Git command, transfer command, duration, or count is invented in
this record.

## Locked Lesson Objective

Teach only the deterministic relationship among canonical field, robot, and
camera frames using one fixed `robotToCamera` extrinsic.

Locked formulas:

```text
cameraToRobot = inverse(robotToCamera)
fieldToCamera = fieldToRobot.transformBy(robotToCamera)
fieldToRobot  = fieldToCamera.transformBy(cameraToRobot)
```

Locked production API:

```java
VisionFrameTransform.fieldToCamera(Pose3d, Transform3d)
VisionFrameTransform.cameraToRobot(Transform3d)
VisionFrameTransform.fieldToRobotFromCamera(Pose3d, Transform3d)
```

The helper remains in `frc.robot.vision`, not
`frc.robot.observation.vision`, because pure coordinate mathematics is not an
Observation sample or mechanism read model.

## Completed Engineering Boundary

### Inherited from final A01_L09

- 73 production and 56 test files are hash-identical.
- Gradle, vendordeps, deploy assets, and inherited lesson architecture remain
  unchanged.
- The final A01 preparation, scheduler exception, fatal bridge, terminal
  HOLDING, stop ownership, Teleop gate, NamedCommands, deferred event command,
  event observation/telemetry, and no-restart contracts remain present.
- Manual child-command lifecycle delegation remains absent.

### New in V00_L01

- `src/main/java/frc/robot/vision/VisionFrameTransform.java`.
- `src/test/java/frc/robot/vision/VisionFrameTransformTest.java`.

The tests include independent numerical oracles so the implementation is not
verified only by repeating the same WPILib composition expression. The locked
example proves that a robot at `(1, 2, 0)` with yaw `+90 degrees` and a camera
one meter robot-forward places the camera at `(1, 3, 0)`.

## Completed Documentation Work

1. Reconciled lesson README from copied A01_L09 metadata to V00_L01 history,
   scope, architecture, verification, and current reopened state.
2. Reconciled LESSON_STATUS with truthful verification ownership and pending
   final-review gates.
3. Reconciled this plan and LESSON_CHECKLIST.
4. Created a chronological student-facing reconstruction guide.
5. Created English and Vietnamese learning guides teaching the architecture and
   mathematics rather than merely listing files.
6. Classified and preserved all 61 inherited A01 documents; no useful history
   was renamed, deleted, or rewritten.

## Verification Record

Authoritative User evidence for the canonical Java 17 project:

- Clean: `PASS`.
- Focused Vision test: `PASS`.
- Full build: `PASS`.
- Accidental `-Recurse` artifact: `ABSENT`.

Codex's read-only audit independently confirms the exact inherited/new source
boundary and required stale-path absence. The task does not authorize Codex to
rerun or reinterpret the User-owned verification as HOLD.

Simulation, Driver Station / Glass, and real robot are `NOT APPLICABLE` to the
new L01 concept because it has no runtime camera, IO, telemetry, scheduler,
drivetrain, fusion, lookup, or actuation path.

## Deferred and Prohibited Work

- No VisionIO, vendor, hardware, AprilTag lookup, target selection, quality,
  timestamp, latency, pose estimation, or fusion.
- No physical extrinsic values until measured and separately reviewed.
- No autonomous, PathPlanner, Swerve, RobotContainer, telemetry, IO, Gradle,
  vendordep, deploy asset, source, or test change.
- No A01 change and no A01_L10.
- No V00_L02 edit, activation, copy, merge, or implementation.
- No Git operation by Codex.

## Final Closure Record

1. Final Architecture Review: `PASS`.
2. Final Closure / freeze authorization: `PASS / APPROVED`.
3. Lesson metadata: `COMPLETE / FROZEN / READ-ONLY`.
4. User Git add/commit/push: `PUBLISHED @ 7d52ebf / USER VERIFIED`.
5. V00_L02 was separately reconciled and published at `53e9b9f`; no automatic
   activation or modification was performed by this closure.

V00_L01 is frozen and published. This closure did not start, resume, or modify
V00_L02.
