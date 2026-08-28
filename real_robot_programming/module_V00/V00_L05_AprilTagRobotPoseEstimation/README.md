# V00_L05 - AprilTag Robot Pose Estimation

## Current lesson state

- **Directory:** `V00_L05_AprilTagRobotPoseEstimation`
- **Authoritative predecessor:** `V00_L04_DeterministicVisionSimulation @ 5461555`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`
- **Status:** `COMPLETE`
- **Active state:** `COMPLETE / FROZEN / READ-ONLY`
- **Freeze state:** `FROZEN / READ-ONLY`
- **Active lesson count:** `0`
- **Design Lock:** `APPROVED BY CHATGPT ARCHITECT`
- **Implementation:** `COMPLETE / AUTHORIZED BOUNDARY`
- **Focused tests:** `COMPLETED / PASS / USER VERIFIED / WPILib Java 17`
- **API reflection hardening:** `COMPLETED / PASS / USER VERIFIED`
- **Documentation:** `COMPLETE / PASS`
- **Closure:** `APPROVED / COMPLETE`
- **Git publication:** `PENDING USER GIT PUBLICATION`

V00_L05 is closed and frozen. This document records the approved Design Lock,
the completed narrow implementation, the completed verification evidence, and
the approved final closure. Git publication remains a separate User-owned
operation and has not yet occurred.

## Inheritance and predecessor authority

V00_L05 was prepared by copying the final V00_L04 lesson, removing copied
generated artifacts, and completing the inherited WPILib Java 17 baseline
build. The read-only inheritance audit recorded:

- 229 comparable non-generated files;
- zero content differences;
- 74 production Java files identical;
- 61 test Java files identical;
- unchanged Gradle/configuration and wrapper files;
- unchanged vendordeps;
- unchanged deploy, resources, and PathPlanner content; and
- inherited documentation preserved exactly until this controlled activation.

V00_L04 remains the authoritative predecessor:

```text
V00_L04_DeterministicVisionSimulation
    COMPLETE / FROZEN / READ-ONLY
    PUBLISHED @ 5461555 / USER VERIFIED
    publication metadata reconciliation: 96dcb4d
        -> V00_L05_AprilTagRobotPoseEstimation
```

The previous naming HOLD was resolved by using the official ADR-locked lesson
identity. No ADR amendment was required. V00_L01 through V00_L04 remain
protected and unchanged.

## One-concept objective

V00_L05 teaches one pure, deterministic, vendor-neutral operation:

```text
fieldToTag
    + cameraToTarget
    + robotToCamera
    -> canonical fieldToRobot robot-pose candidate
```

The technical phrase “canonical robot-pose candidate estimation” describes
the concept. The official lesson identity remains `V00_L05 - AprilTag Robot
Pose Estimation`.

## Canonical mathematical contract

The approved WPILib geometry relationship is:

```java
Pose3d fieldToCamera =
    fieldToTag.transformBy(cameraToTarget.inverse());

Pose3d fieldToRobot =
    fieldToCamera.transformBy(robotToCamera.inverse());
```

Equivalently:

```java
Pose3d fieldToRobot =
    fieldToTag
        .transformBy(cameraToTarget.inverse())
        .transformBy(robotToCamera.inverse());
```

The geometry is canonical WPILib Blue-origin, right-handed NWU, with meters
for translations and radians for rotations. Composition order is meaningful
and must not be reversed.

## Approved package, class, and API

The Architect-approved production boundary is:

- Package: `frc.robot.vision`.
- Class: `AprilTagRobotPoseEstimator`.
- Form: final, stateless, non-instantiable utility.
- Public API: exactly one method.

```java
public static Pose3d estimateFieldToRobotCandidate(
    Pose3d fieldToTag,
    Transform3d cameraToTarget,
    Transform3d robotToCamera);
```

The authorized implementation is complete in exactly these L05 files:

```text
src/main/java/frc/robot/vision/AprilTagRobotPoseEstimator.java
src/test/java/frc/robot/vision/AprilTagRobotPoseEstimatorTest.java
```

## Dependency ownership

- `AprilTagFieldLayoutContract` remains the caller's responsibility. L05 does
  not own tag lookup or depend on a layout object.
- `VisionObservation` and `TargetObservation` remain upstream immutable data;
  they are not direct estimator parameters.
- `VisionIO` and mutable `VisionIOInputs` remain upstream acquisition transport.
- `VisionIOSim` is a test-only round-trip source and reference.
- `VisionFrameTransform` may be reused for its frozen camera-extrinsic
  reconstruction operation and must not be modified or used as the sole test
  oracle.

## Output and validation semantics

The output is:

> A deterministic canonical Blue-origin `fieldToRobot` robot-pose candidate
> derived from one structurally valid AprilTag geometry relationship and the
> fixed robot-to-camera extrinsic.

It is not a quality-approved measurement, accepted fusion measurement,
timestamped or latency-compensated value, fused estimator pose, or authority to
mutate odometry.

The structural validation boundary is limited to:

- reject null `fieldToTag`, `cameraToTarget`, or `robotToCamera` with
  `NullPointerException`;
- reject observable nonfinite input or computed geometry with
  `IllegalArgumentException`;
- preserve caller-owned geometry;
- remain deterministic; and
- return independent result geometry.

This API has no tag ID, so nonpositive and unknown-tag behavior remains with
`AprilTagFieldLayoutContract` and its caller. The stateless design makes
failure atomicity inherent.

## Quality and timing boundary

Structural validity in L05 must not become measurement-quality policy. V00_L06
owns ambiguity thresholds, confidence, quality scoring, ranking, geometric
plausibility, distance/viewing-angle acceptance, covariance/uncertainty, and
accepted/rejected measurement status.

V00_L07 owns timestamp, latency, freshness, ordering, and duplicate semantics.
V00_L08 owns real camera/vendor integration. V00_L09 owns Swerve estimator
fusion.

## Implemented focused tests

Focused tests cover:

1. identity/simple geometry;
2. translation-only reconstruction;
3. rotation-only reconstruction;
4. combined translation and rotation;
5. noncommutative transform-order proof;
6. an independent nontrivial 3D numeric oracle;
7. L04 forward to L05 reverse round trip;
8. nonzero `robotToCamera` extrinsic;
9. multiple field-to-tag positions;
10. meters, radians, and NWU axis signs;
11. deterministic repeated calls;
12. fresh results and unchanged caller geometry;
13. null and observable nonfinite rejection; and
14. the exact public API/reflection contract.

Key translation, rotation, order, and nontrivial 3D tests must use
independent numeric expected values. The L04↔L05 round trip supplements those
oracles and does not replace them.

## Verification surfaces

- Focused deterministic unit tests: `PASS / USER VERIFIED / WPILib Java 17`.
- Inherited vision regression tests: `PASS / USER VERIFIED / WPILib Java 17`.
- Full test suite: `PASS / USER VERIFIED / WPILib Java 17`.
- Clean build: `PASS / USER VERIFIED / WPILib Java 17`.
- WPILib Simulation: `NOT REQUIRED / NOT APPLICABLE` for this pure geometry
  scope.
- HALSIM, Glass, Driver Station, Limelight, physical camera, and real robot:
  `NOT REQUIRED / NOT APPLICABLE`.

The inherited baseline build and the completed post-hardening L05 verification
are recorded as User-verified PASS under WPILib Java 17. The Java 17
compatibility adjustment from `getFirst()` to `get(0)` and the narrow
noncommutativity-fixture repair were test-only; no production repair was
required. The original fixture defect and its forensic correction are detailed
in the transition guide.

## Deferred and prohibited scope

V00_L05 must not introduce:

- Limelight, PhotonVision, NetworkTables, or vendor-specific types;
- ambiguity, quality, confidence, ranking, or acceptance policy;
- timestamps, latency, or temporal policy;
- real camera IO;
- pose fusion or `SwerveDrivePoseEstimator.addVisionMeasurement(...)`;
- SwerveSubsystem, RobotContainer, telemetry, PathPlanner, autonomous,
  commands, or scheduler changes;
- alliance transforms or Red-origin geometry;
- HALSIM runtime wiring, Glass, Driver Station, or real-robot integration; or
- V00_L06 and later responsibilities.

A01_L04 remains the sole alliance-transform owner. V00_L06 remains the next
roadmap lesson; no later lesson has been started.

## Lifecycle and Git boundary

The completed activation state is:

```text
DESIGN LOCK: APPROVED
LESSON: COMPLETE / FROZEN / READ-ONLY
IMPLEMENTATION: COMPLETE
TEST-ONLY NONCOMMUTATIVITY FIXTURE REPAIR: COMPLETED
POST-REPAIR USER VERIFICATION: PASS / WPILib Java 17
POST-HARDENING USER VERIFICATION: PASS / WPILib Java 17
FINAL ARCHITECT CLOSURE: APPROVED / COMPLETE
ACTIVE V00 LESSON COUNT: 0
GIT PUBLICATION: PENDING USER GIT PUBLICATION
```

Closure and freeze are complete. User-owned Git add/commit/push and any later
publication-metadata reconciliation remain separate future gates. Codex
performs no Git operations.
