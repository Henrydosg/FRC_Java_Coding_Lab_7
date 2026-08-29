# V00_L06 - Vision Measurement Quality Contract

## Current lesson state

- Directory: V00_L06_VisionMeasurementQualityContract
- Authoritative predecessor: V00_L05_AprilTagRobotPoseEstimation @ 6482160
- Predecessor metadata reconciliation: 3161dfb
- Predecessor state: COMPLETE / FROZEN / READ-ONLY / PUBLISHED
- Status: COMPLETE
- Active state: COMPLETE / FROZEN / READ-ONLY
- Freeze state: FROZEN / READ-ONLY
- Active lesson count: 0
- Design Lock: APPROVED BY CHATGPT ARCHITECT
- Controlled activation: PASS
- Implementation authorization: GRANTED
- Implementation: COMPLETE / AUTHORIZED BOUNDARY
- Verification: PASS / USER VERIFIED / WPILib Java 17 / STANDARD CLEAN BUILD
- Documentation: COMPLETE / PASS
- Closure: APPROVED / COMPLETE
- Git publication: PUBLISHED @ 1327bf4 / USER VERIFIED
- Publication commit: 1327bf41736c8fe79ba58ec5eea9e0120bd978fb
- Publication subject: Complete V00_L06 vision measurement quality contract

V00_L06 is complete, frozen, and read-only. V00_L01 through V00_L05 remain
frozen historical snapshots and must not be modified. No current blocker
remains. Git publication is confirmed at `1327bf4` and matches `origin/main`.
The separate post-publication documentation reconciliation commit has no
claimed hash here.

## One-concept objective

This lesson adds deterministic acceptance/rejection and qualitative
uncertainty classification using only the distance from one immutable,
vendor-neutral target observation:

~~~text
VisionIO
-> VisionObservation / TargetObservation
-> AprilTagRobotPoseEstimator [V00_L05]
-> raw fieldToRobot candidate
-> VisionMeasurementQualityEvaluator [V00_L06]
-> quality result
-> V00_L07 timing
-> V00_L08 real camera
-> V00_L09 fusion
~~~

The evaluator receives TargetObservation and does not receive the L05 Pose3d
candidate or a whole VisionObservation.

## Locked distance policy

Distance is exactly:

~~~java
target.cameraToTarget().getTranslation().getNorm()
~~~

The immutable policy thresholds satisfy:

~~~text
0 <= lowMaxMeters <= mediumMaxMeters <= maximumAcceptedMeters
~~~

Equality is valid and may intentionally create an empty uncertainty band.
Classification uses ordered inclusive upper bounds:

| Distance | Acceptance | Uncertainty | Rejection reason |
| --- | --- | --- | --- |
| distance <= lowMaxMeters | ACCEPTED | LOW | NONE |
| lowMaxMeters < distance <= mediumMaxMeters | ACCEPTED | MEDIUM | NONE |
| mediumMaxMeters < distance <= maximumAcceptedMeters | ACCEPTED | HIGH | NONE |
| distance > maximumAcceptedMeters | REJECTED | UNUSABLE | TARGET_TOO_FAR |

## Approved API boundary

Exactly two new production files are authorized:

~~~text
src/main/java/frc/robot/observation/vision/VisionMeasurementQuality.java
src/main/java/frc/robot/observation/vision/VisionMeasurementQualityEvaluator.java
~~~

VisionMeasurementQuality is an immutable record with Acceptance,
UncertaintyClass, and RejectionReason enums. Its constructor rejects every
tuple except the three accepted uncertainty classes with NONE and the one
rejected UNUSABLE/TARGET_TOO_FAR tuple.

VisionMeasurementQualityEvaluator is a final, stateless utility. It owns the
immutable nested Policy record and one public static evaluate method.

Exactly one focused test file is authorized:

~~~text
src/test/java/frc/robot/observation/vision/VisionMeasurementQualityEvaluatorTest.java
~~~

No existing production file may change.

## Malformed input boundary

Null required arguments, nonfinite or negative thresholds, invalid threshold
ordering, and a nonfinite computed target distance are programming/configuration
errors. They throw exceptions and are never represented as measurement
rejections.

TargetObservation already validates and defensively owns its Transform3d. The
evaluator still checks the final translation norm with Double.isFinite because
finite components can theoretically overflow during norm calculation.

## Implementation result

The authorized delta is complete:

~~~text
NEW production:
  VisionMeasurementQuality.java
  VisionMeasurementQualityEvaluator.java

NEW focused test:
  VisionMeasurementQualityEvaluatorTest.java

Existing production modifications:
  NONE
~~~

The result record enforces exactly the four locked states. The evaluator owns
no mutable state and uses exactly one TargetObservation translation norm with
an explicit finite-result guard.

## Verification evidence and historical environment note

User-controlled standard WPILib Java 17 verification:

- clean: PASS;
- compileJava: PASS;
- compileTestJava: PASS;
- focused VisionMeasurementQualityEvaluatorTest: PASS;
- inherited vision regressions: PASS;
- complete lesson test suite: PASS;
- clean build: PASS;
- standard result: BUILD SUCCESSFUL.

The earlier TERRA/Codex-local compileTestJava failure and bounded-javac
verification workaround remain historical evidence only. They are classified
as `SUPERSEDED / ENVIRONMENT-PROCESS-ONLY` and are not a current lesson
blocker. No Gradle or classpath repair is required.

## Verification sequence

Required verification proceeds through:

1. compileJava and compileTestJava;
2. focused V00_L06 tests;
3. inherited VisionObservation, VisionIO, VisionIOSim, VisionFrameTransform,
   AprilTagFieldLayoutContract, and AprilTagRobotPoseEstimator tests;
4. the complete lesson test suite; and
5. a clean build.

Simulation, Glass, Driver Station, physical-camera, and real-robot verification
are NOT REQUIRED for this pure deterministic contract with no runtime wiring.

## Explicit exclusions

V00_L06 contains no Limelight, PhotonVision, timestamp, latency, covariance,
standard deviation, pose fusion, addVisionMeasurement, Swerve wiring,
RobotContainer wiring, telemetry, NetworkTables, alliance transformation,
PathPlanner change, target ranking, multi-target aggregation, or whole
VisionObservation scoring.

V00_L07 owns timing. V00_L08 owns the reviewed real camera adapter. V00_L09
owns accepted timestamped fusion into the Swerve-owned estimator.

## Lifecycle boundary

This lesson is COMPLETE / FROZEN / READ-ONLY / PUBLISHED. User verification,
documentation reconciliation, final architecture review, closure approval, and
User-owned Git publication is complete. The publication commit is
`1327bf41736c8fe79ba58ec5eea9e0120bd978fb` with subject
`Complete V00_L06 vision measurement quality contract`.
