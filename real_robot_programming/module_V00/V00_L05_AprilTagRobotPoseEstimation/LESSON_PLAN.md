# V00_L05 Lesson Plan and Lifecycle Record

## Current state

- **Lesson:** `V00_L05 - AprilTag Robot Pose Estimation`
- **Directory:** `V00_L05_AprilTagRobotPoseEstimation`
- **Predecessor:** `V00_L04_DeterministicVisionSimulation @ 5461555`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`
- **Status:** `COMPLETE / FROZEN / READ-ONLY`
- **Active lesson count:** `0`
- **Design Lock:** `APPROVED BY CHATGPT ARCHITECT`
- **Implementation:** `COMPLETE / AUTHORIZED BOUNDARY`
- **Post-repair user verification:** `PASS / WPILib Java 17`
- **Documentation:** `COMPLETE / PASS`
- **Final closure:** `APPROVED / COMPLETE`
- **Git publication:** `PUBLISHED @ 6482160 / USER VERIFIED`
- **Metadata reconciliation:** `PENDING USER GIT`

## One-concept objective

Derive a deterministic canonical Blue-origin field-relative robot-pose
candidate from already available vendor-neutral geometry:

```text
fieldToTag + cameraToTarget + robotToCamera
    -> fieldToRobot robot-pose candidate
```

L05 does not accept measurements, score quality, handle time, fuse pose, or
mutate robot localization.

## Completed preparation and activation phases

1. The User confirmed final V00_L04 authority as `COMPLETE / FROZEN /
   READ-ONLY / PUBLISHED @ 5461555 / USER VERIFIED`; publication metadata
   reconciliation is recorded at `96dcb4d`.
2. The User copied final V00_L04 into the ADR-approved
   `V00_L05_AprilTagRobotPoseEstimation` identity.
3. The User removed copied generated artifacts before baseline verification.
4. The User supplied the inherited WPILib Java 17 baseline build PASS.
5. The read-only inheritance audit confirmed 229 comparable non-generated
   files and zero differences, including 74 production Java and 61 test Java
   files.
6. The audit confirmed unchanged build/configuration, wrapper, vendordeps,
   deploy/resources/PathPlanner content, and inherited documentation.
7. The audit confirmed no L05 implementation and protected V00_L01-L04.
8. The initial candidate naming HOLD was identified because the first
   untracked directory name did not match the locked ADR identity.
9. The Architect decided to retain the frozen ADR identity and not amend the
   ADR; the technical phrase “canonical robot-pose candidate estimation” is
   descriptive only.
10. The User renamed the untracked candidate directory to
    `V00_L05_AprilTagRobotPoseEstimation`.
11. The governance identity re-audit confirmed the naming HOLD was resolved
    and no ADR amendment was required.
12. The Design-Lock planning audit recommended the pure explicit-geometry
    calculator boundary in `frc.robot.vision`.
13. The Architect approved the exact Design Lock recorded below.
14. This controlled documentation activation recorded V00_L05 as the sole
    `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`.

## Architect-approved Design Lock

### Package and class

- Package: `frc.robot.vision`
- Class: `AprilTagRobotPoseEstimator`
- Form: final, stateless, non-instantiable utility

### Exact public API

```java
public static Pose3d estimateFieldToRobotCandidate(
    Pose3d fieldToTag,
    Transform3d cameraToTarget,
    Transform3d robotToCamera);
```

No additional public API is authorized.

### Mathematical contract

```java
Pose3d fieldToCamera =
    fieldToTag.transformBy(cameraToTarget.inverse());

Pose3d fieldToRobot =
    fieldToCamera.transformBy(robotToCamera.inverse());
```

The frame is canonical WPILib Blue-origin and right-handed NWU. Translations
use meters and rotations use radians. Composition order is not interchangeable.

### Ownership and validation

- `AprilTagFieldLayoutContract` and tag lookup remain caller-owned.
- `VisionObservation`, `TargetObservation`, `VisionIO`, and `VisionIOInputs`
  remain upstream and are not direct estimator parameters.
- `VisionIOSim` is test-only round-trip/reference support.
- `VisionFrameTransform` may be reused for its frozen camera-extrinsic
  reconstruction operation and must not be modified or be the sole oracle.
- Null arguments produce `NullPointerException`.
- Observable nonfinite input, intermediate, or result geometry produces
  `IllegalArgumentException`.
- The stateless design provides inherent failure atomicity.

### Quality boundary

L05 structural validity is not measurement quality. V00_L06 owns ambiguity,
confidence, scoring, ranking, geometric plausibility, acceptance thresholds,
covariance/uncertainty, and accepted/rejected status. V00_L07 owns time and
latency semantics.

## Completed implementation and verification phases

15. **Separate implementation authorization — PASS.** The approved boundary
    named exactly `AprilTagRobotPoseEstimator.java` and its focused test file.
16. **Implement the estimator — PASS.** The approved pure deterministic
    geometry calculator was completed without production scope expansion.
17. **Implement focused tests — PASS.** The focused contract test matrix was
    completed. A narrow test-only noncommutativity-fixture repair was also
    completed; no production repair was required.
18. **Run focused deterministic tests — PASS / USER VERIFIED / WPILib Java
    17.** The matrix includes identity, translation, rotation, combined 3D,
    noncommutative order, independent numeric oracle, validation,
    determinism, ownership, and the exact API contract.
19. **Run the L04 forward/L05 reverse round trip — PASS / USER VERIFIED.** It
    uses `VisionIOSim` only as a test/reference source and supplements the
    independent numeric oracle tests.
20. **Run inherited vision regressions — PASS / USER VERIFIED.** L01-L04
    behavior remains protected.
21. **Run the full test suite — PASS / USER VERIFIED.** The full suite passed
    under WPILib Java 17.
22. **Run the clean build — PASS / USER VERIFIED.** The clean build passed
    under WPILib Java 17.
23. **Post-implementation architecture review and documentation
    reconciliation — PASS.** The implementation architecture passed, the
    lifecycle records distinguish completed evidence, and the API reflection
    test is hardened to lock final-class and one-public-method behavior.
    Post-hardening User verification is PASS.
24. **Final Architect closure and freeze — PASS / APPROVED.** The Architect
    authorized final closure. V00_L05 is now `COMPLETE / FROZEN / READ-ONLY`,
    with no active V00 lesson.
25. **Actual User lesson publication — PASS / USER VERIFIED.** The User
    confirmed commit `6482160`, subject `Complete V00_L05 AprilTag robot pose
    estimation`, was pushed to `origin/main`; `HEAD` and `origin/main` resolved
    to the full publication commit at publication time.
26. **Post-publication metadata reconciliation — PENDING / USER OWNED.** The
    current documentation reconciliation is separate from the actual lesson
    publication commit. The User must commit and push this metadata update; its
    future hash is not yet known.

## Verification surfaces

- Focused deterministic unit tests: required.
- Inherited vision regression tests: required.
- Full test suite: required before closure.
- Clean build: required before closure.
- WPILib Simulation, HALSIM, Glass, Driver Station, physical camera, and real
  robot: not required/applicable to this pure geometry scope.

## Explicit exclusions

No Limelight, PhotonVision, vendor API, NetworkTables, runtime camera IO,
telemetry, SwerveSubsystem, RobotContainer wiring, alliance transform,
Red-origin geometry, PathPlanner, autonomous behavior, command/scheduler,
pose-estimator fusion, `addVisionMeasurement(...)`, HALSIM runtime, Glass,
Driver Station, real-robot verification, or V00_L06+ responsibility belongs in
this lesson.

A01_L04 remains the sole alliance-transform owner. A01_L10 remains prohibited.
