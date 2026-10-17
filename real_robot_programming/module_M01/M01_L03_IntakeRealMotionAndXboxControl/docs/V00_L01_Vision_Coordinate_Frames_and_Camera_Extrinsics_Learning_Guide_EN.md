# V00_L01 Learning Guide - Vision Coordinate Frames and Camera Extrinsics

Status: `FINAL / PASS`  
Lesson state: `COMPLETE / FROZEN / READ-ONLY`  
Authoritative predecessor: `A01_L09 @ 6b243bb`

## 1. Learning Goal

V00_L01 builds the mathematical foundation required before a robot can use
camera observations safely. It teaches how to name coordinate frames, how to
describe a fixed camera mount, and how to compose and invert rigid 3D
transforms.

This lesson does not acquire a camera measurement, identify an AprilTag,
estimate robot pose, or fuse vision into localization. Those later operations
depend on the frame contract established here.

## 2. Where Vision Fits in the Robot Architecture

The inherited robot control architecture remains:

```text
Autonomous Command
    -> drivetrain subsystem
    -> IO
    -> hardware
```

More generally, the Frozen Backbone remains:

```text
Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware
```

V00_L01 does not add a new arrow to either control flow. It provides pure
mathematics that later vision lessons will use.

The planned vision foundation develops in a different, observation-oriented
direction:

```text
camera measurement
    -> camera/robot coordinate relationship
    -> vendor-neutral vision representation
    -> observation/localization layer
    -> later pose-estimator fusion
```

Only the **camera/robot coordinate relationship** mathematics begins in L01.
Camera measurement acquisition, vendor-neutral VisionIO/Observation models,
localization decisions, timing, quality, and fusion belong to later V00
lessons. The existing mechanism observation flow remains unchanged:

```text
hardware -> IOInputs -> subsystem/estimator -> immutable Observation
         -> read-only telemetry -> NT4 / Glass / log
```

## 3. Pose3d and Transform3d Answer Different Questions

A `Pose3d` answers:

> Where is a frame, relative to a reference frame?

`fieldToRobot` is therefore the robot's position and orientation in the
canonical field frame.

A `Transform3d` answers:

> What rigid translation and rotation moves from one frame to another?

`robotToCamera` is therefore the fixed mounting relationship from the robot
frame to the camera frame.

The words before and after `To` are part of the contract. A
`robotToCamera` transform cannot be used in the opposite direction unless it
is inverted.

## 4. The Three L01 Frames

### Canonical field frame

The field frame is the stable WPILib world/reference frame. A field-relative
pose such as `fieldToRobot` or `fieldToCamera` is expressed in this frame. The
A01_L04 alliance-transform ownership remains unchanged; V00_L01 does not add
or apply an alliance flip.

### Robot body frame

The robot frame is fixed to the chassis reference used by the inherited Swerve
localization architecture:

- +X points robot-forward;
- +Y points robot-left; and
- +Z points up.

A camera mounting translation is expressed in these robot axes.

### Camera frame

The camera frame is fixed to the camera body. V00 uses a WPILib-normalized
camera frame for geometry. A future vendor adapter may need to convert its own
optical-axis convention before producing vendor-neutral data, but no vendor
adapter exists in L01.

## 5. WPILib NWU Convention

WPILib 3D geometry uses a right-handed north-west-up (NWU) convention:

```text
             +Z up
               |
               |
               o------ +X forward
              /
           +Y left
```

- Translation units are meters.
- Rotation units are radians.
- Roll rotates about +X.
- Pitch rotates about +Y.
- Yaw rotates about +Z.
- Positive rotation follows the right-hand rule.

The helper does not silently convert inches to meters or degrees to radians.
Inputs must already use the locked units and convention.

## 6. The Fixed Mounting Extrinsic

`robotToCamera` describes where the camera is mounted relative to the robot:

```text
FIELD
  | fieldToRobot
  v
ROBOT
  | robotToCamera
  v
CAMERA
```

Its translation gives the camera origin in robot axes. Its rotation gives the
camera orientation relative to the robot.

The inverse relationship is:

```text
cameraToRobot = inverse(robotToCamera)
```

That inverse is essential. It changes both rotation and translation as one
rigid transform; it is not simply a label change or a negation of three
numbers.

## 7. Forward Composition and Reverse Reconstruction

To find the camera field pose from a known robot field pose:

```java
Pose3d fieldToCamera = fieldToRobot.transformBy(robotToCamera);
```

Mathematically:

```text
fieldToCamera = fieldToRobot * robotToCamera
```

To reconstruct the robot field pose from a known camera field pose:

```java
Transform3d cameraToRobot = robotToCamera.inverse();
Pose3d fieldToRobot = fieldToCamera.transformBy(cameraToRobot);
```

Mathematically:

```text
fieldToRobot = fieldToCamera * cameraToRobot
```

The starting pose/frame appears first, and the transform from that starting
frame to the destination appears second.

## 8. Why Composition Order Matters

Rigid transforms are not generally commutative. The camera translation is
measured along robot axes, and those axes rotate with the robot.

Use the locked independent numerical example:

```text
Robot field position: (1 m, 2 m, 0 m)
Robot yaw:            +90 degrees
Camera mount:         1 m forward from the robot
```

Before rotation, robot-forward is the robot's +X axis. After the robot has yaw
`+90 degrees`, robot +X points along field +Y. Therefore the one-meter camera
offset increases the field Y coordinate:

```text
Expected camera field position: (1 m, 3 m, 0 m)
```

The camera does **not** move to `(2 m, 2 m, 0 m)`. That incorrect answer adds
the mounting offset directly along field +X and ignores the robot's rotation.

This is why `fieldToRobot.transformBy(robotToCamera)` is meaningful and why
reversing the operands or directly adding coordinates is incorrect.

## 9. Implemented Helper and Package Ownership

The lesson implements exactly one production class:

`frc.robot.vision.VisionFrameTransform`

Its locked public API is:

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

The class is final, non-instantiable, stateless, deterministic, and
vendor-neutral. It uses WPILib geometry only. It rejects null inputs,
nonfinite translations, nonfinite rotations, and nonfinite computed results.
It does not mutate caller-owned geometry.

### Why `frc.robot.vision`, not `frc.robot.observation.vision`

An Observation is an immutable description of something known about the robot
at a coherent sample time, normally produced by a subsystem or estimator from
IOInputs. `VisionFrameTransform` stores no sample, timestamp, connection state,
validity state, target, or measurement. It only computes geometry from
explicit arguments.

Therefore:

```text
frc.robot.vision                    correct: pure vision-domain geometry
frc.robot.observation.vision        incorrect: would imply an Observation owner
```

Pure mathematics can support a future Observation without itself becoming an
Observation.

## 10. How the Tests Act as a Mathematical Oracle

The focused tests cover:

- identity, translation-only, rotation-only, and combined transforms;
- independent numeric expectations for rotated translation and inversion;
- forward/reverse round-trip reconstruction;
- noncommutative composition order;
- NWU signs, meters, and radians;
- null and nonfinite rejection; and
- determinism and no caller mutation.

Some tests compare with WPILib's geometry operations, while independent tests
use explicit expected coordinates and angles. This balance checks API
agreement without relying entirely on the same expression as production.

## 11. What Remains Inherited from A01_L09

The Vision helper does not modify the final autonomous architecture. The
canonical project still contains, unchanged:

- `AutonomousPreparationCoordinator`, `PrepareAutonomousCommand`, immutable
  preparation observation, and read-only preparation telemetry;
- scheduler-native AutoBuilder composition;
- Robot-level scheduler `RuntimeException` handling and the fatal-fault bridge;
- terminal `HOLDING`, centralized `SwerveSubsystem.stop()`, SAFE_STOP,
  defensive Teleop-enabled output gating, and no automatic restart;
- `frc.robot.autonomous.AutonomousEventId`;
- NamedCommands event markers and `Commands.defer(...)` fresh command
  construction; and
- no manual child-command lifecycle delegation.

These are inherited A01 behaviors, not new V00_L01 behavior.

## 12. Deferred Work

The following are deliberately absent:

- VisionIO and VisionIOInputs;
- PhotonVision, Limelight, or any camera vendor;
- camera hardware and physical X/Y/Z/roll/pitch/yaw calibration values;
- AprilTag field-layout lookup and target selection;
- target/measurement quality decisions;
- timestamps and latency compensation;
- robot-pose estimation from a target;
- Swerve pose-estimator integration and vision fusion;
- autonomous, PathPlanner, or drivetrain behavior changes.

No physical mounting value may be guessed. Later lessons must introduce their
own single concept under the V00 roadmap and the normal architecture review.

## 13. Verification and Lesson State

Authoritative User evidence for the canonical Java 17 project records:

- Clean: PASS.
- Focused `VisionFrameTransformTest`: PASS.
- Full build: PASS.
- Accidental `-Recurse` artifact: absent.

Simulation, Driver Station / Glass, and real robot are not applicable to this
new concept because it has no runtime camera, IO, telemetry, scheduler,
drivetrain, lookup, fusion, or actuation behavior.

V00_L01 is `COMPLETE / FROZEN / READ-ONLY` after Final Architecture Review and
Final Closure Review PASS. Git add, commit, and push remain User-owned and
pending.
