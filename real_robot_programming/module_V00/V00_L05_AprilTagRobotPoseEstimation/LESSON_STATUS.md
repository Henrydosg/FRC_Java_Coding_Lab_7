# LESSON_STATUS

## Identity

- **Module:** `V00 - AprilTag Vision Observation and Pose Fusion`
- **Lesson:** `V00_L05_AprilTagRobotPoseEstimation`
- **Title:** `V00_L05 - AprilTag Robot Pose Estimation`
- **Previous Lesson:** `V00_L04_DeterministicVisionSimulation @ 5461555`
- **Previous Lesson State:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`
- **Predecessor Publication Metadata Reconciliation:** `96dcb4d`
- **Actual Lesson Publication:** `6482160 / USER VERIFIED TO origin/main`
- **Publication Subject:** `Complete V00_L05 AprilTag robot pose estimation`
- **Status:** `COMPLETE`
- **Active State:** `COMPLETE / FROZEN / READ-ONLY`
- **Freeze State:** `FROZEN / READ-ONLY`
- **Active Lesson Count:** `0`
- **Lesson Goal:** pure deterministic vendor-neutral canonical AprilTag
  robot-pose candidate estimation

## Required status fields

- **Architecture Review:** `PASS / DESIGN LOCK APPROVED`
- **Baseline Build:** `PASS / USER VERIFIED / WPILib Java 17 / INHERITED`
- **Build:** `PASS / USER VERIFIED / WPILib Java 17 / CLEAN BUILD`
- **Automated Verification:** `PASS / USER VERIFIED / WPILib Java 17 / FOCUSED, INHERITED, AND FULL SUITE`
- **Simulation:** `NOT APPLICABLE / PURE GEOMETRY SCOPE`
- **Driver Station / Glass:** `NOT APPLICABLE / NO RUNTIME TELEMETRY`
- **Real Robot:** `NOT APPLICABLE / NO CAMERA OR ACTUATION`
- **Transition Guide:** `COMPLETE / PASS`
- **Documentation:** `COMPLETE / PASS`
- **Closure:** `APPROVED / COMPLETE`
- **Git Commit:** `PUBLISHED @ 6482160 / USER VERIFIED`
- **Git Push:** `PUBLISHED TO origin/main / USER VERIFIED`
- **Metadata Reconciliation:** `PENDING USER GIT`
- **Known Issues:** `NONE CURRENT`. Technical closure and lesson publication
  are complete; this metadata reconciliation remains User-owned and is not
  yet published.

## Lifecycle gates

| Gate | Result | Evidence / meaning |
| --- | --- | --- |
| V00_L04 Authority | PASS | V00_L04 is complete, frozen, read-only, and User-published at `5461555`. |
| Publication Metadata Reconciliation | PASS | User supplied reconciliation commit `96dcb4d`. |
| User Copy/Rename Preparation | PASS | Candidate was copied into the ADR-locked V00_L05 identity. |
| Generated-Artifact Handling | PASS | Copied generated artifacts were removed before the inherited baseline build. |
| Inheritance Audit | PASS | 229 comparable non-generated files; zero differences. |
| Production Java Inheritance | PASS | 74 inherited production Java files are identical. |
| Test Java Inheritance | PASS | 61 inherited test Java files are identical. |
| Build/Config/Dependencies/Assets | PASS | Gradle, wrapper, vendordeps, deploy/resources, and PathPlanner content are inherited unchanged. |
| Baseline Build | PASS | User supplied WPILib Java 17 inherited baseline-build evidence. |
| Naming Governance | PASS | The candidate now matches the frozen ADR identity; no amendment was required. |
| Frozen Backbone | PASS / PRESERVED | Package responsibilities and dependency direction remain unchanged. |
| Frozen Interface Contract | PASS / PRESERVED | Existing IO and geometry contracts remain unchanged. |
| Document C | PASS / PRESERVED | Observation ownership and immutability remain unchanged. |
| Predecessor Protection | PASS | V00_L01-L04 remain frozen and unchanged. |
| Design Lock | APPROVED | Architect approved the exact pure pose-candidate boundary recorded below. |
| Controlled Activation | PASS | Historical activation recorded V00_L05 as the sole `IN_PROGRESS / EDITABLE` lesson before implementation and closure. |
| Production Implementation Authorization | PASS | Authorized L05 production boundary was completed without scope expansion. |
| Focused Test Authorization | PASS | Authorized L05 focused-test boundary was completed without scope expansion. |
| Test-Only Noncommutativity Fixture Repair | PASS | The focused fixture was repaired to prove noncommutative 3D composition; production remained unchanged. |
| Focused Verification | PASS | User verified focused L05 tests under WPILib Java 17. |
| Inherited Regression | PASS | User verified inherited vision regressions under WPILib Java 17. |
| Full Test Suite | PASS | User verified the full suite under WPILib Java 17. |
| Clean Build | PASS | User verified the clean build under WPILib Java 17. |
| Simulation | NOT APPLICABLE | No runtime simulation is introduced by the pure geometry scope. |
| Driver Station / Glass | NOT APPLICABLE | No runtime telemetry is introduced. |
| Real Robot | NOT APPLICABLE | No camera, actuation, or robot wiring is introduced. |
| Post-Implementation Architecture Review | PASS | The implementation architecture passed; no production architecture defect exists. |
| Documentation Reconciliation | PASS | Active L05 records reconcile implementation, verification, closure, freeze, and the actual User-published lesson identity. |
| API Reflection-Test Hardening | PASS | The focused test locks final class behavior and exactly one public declared method; User post-hardening verification passed. |
| Final Architect Closure Review | APPROVED / COMPLETE | Architect authorized final closure; the lesson is frozen and read-only. |
| Freeze Transition | PASS | V00_L05 transitioned to `COMPLETE / FROZEN / READ-ONLY`. |
| Git Publication | PASS | User confirmed lesson publication at `6482160` and push to `origin/main`; no metadata-reconciliation hash is claimed. |
| Metadata Reconciliation Publication | PENDING | User must commit and push this documentation-only reconciliation separately from the lesson publication. |

## Architect-approved Design Lock

### Responsibility

V00_L05 owns one pure deterministic vendor-neutral calculation:

```text
fieldToTag + cameraToTarget + robotToCamera
    -> canonical Blue-origin fieldToRobot robot-pose candidate
```

### Mathematical contract

```java
Pose3d fieldToCamera =
    fieldToTag.transformBy(cameraToTarget.inverse());

Pose3d fieldToRobot =
    fieldToCamera.transformBy(robotToCamera.inverse());
```

Geometry uses the WPILib Blue-origin field frame, right-handed NWU, meters,
and radians. Composition order is locked.

### Exact package and API

- Package: `frc.robot.vision`
- Class: `AprilTagRobotPoseEstimator`
- Form: final, stateless, non-instantiable utility
- Exact public method:

```java
public static Pose3d estimateFieldToRobotCandidate(
    Pose3d fieldToTag,
    Transform3d cameraToTarget,
    Transform3d robotToCamera);
```

No additional public API is authorized.

### Dependency ownership

- `AprilTagFieldLayoutContract`: caller responsibility, not an estimator
  dependency.
- `VisionObservation` and `TargetObservation`: upstream only, not direct
  estimator parameters.
- `VisionIO` and `VisionIOInputs`: upstream only.
- `VisionIOSim`: test-only round-trip source/reference.
- `VisionFrameTransform`: approved production reuse of its frozen camera-
  extrinsic reconstruction operation; it must not be modified or be the sole
  test oracle.

### Output semantics

The return value is a deterministic canonical Blue-origin `fieldToRobot`
robot-pose candidate derived from one structurally valid AprilTag geometry
relationship and the fixed robot-to-camera extrinsic.

It is not a quality-approved measurement, accepted fusion measurement,
timestamped or latency-compensated value, fused estimator pose, or authority to
mutate odometry.

### Structural validation

The implementation must reject null arguments with `NullPointerException` and
observable nonfinite input, intermediate, or result geometry with
`IllegalArgumentException`. It must preserve caller-owned geometry, remain
deterministic, and return independent result geometry. Tag lookup and unknown
tag behavior remain with `AprilTagFieldLayoutContract` and its caller.

### L06 boundary

L05 must not add ambiguity thresholds, confidence, quality scoring, ranking,
geometric plausibility policy, distance/viewing-angle acceptance, covariance,
uncertainty, or accepted/rejected measurement status. Those responsibilities
are deferred to V00_L06. Timestamp and latency semantics are deferred to
V00_L07.

## Completed verification and closure

Focused tests now cover identity, translation, rotation, combined 3D
composition, noncommutative order, independent numeric oracles, the L04
forward/L05 reverse round trip, nonzero extrinsics, multiple field-to-tag
positions, meters, radians, NWU signs, determinism, fresh results, caller
immutability, null/nonfinite rejection, and the exact public API. Independent
numeric expected values cover the key translation, rotation, order, and
nontrivial 3D cases.

The round-trip test supplements, and does not replace, the independent oracle
tests. Post-hardening User verification is PASS. Technical closure, freeze,
and the actual lesson publication at `6482160` are complete; this metadata
reconciliation remains User-owned and pending publication.

## Protected and deferred scope

- V00_L01: `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 7d52ebf`.
- V00_L02: `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 53e9b9f`.
- V00_L03: `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ cc20d62`.
- V00_L04: `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 5461555`.
- V00_L06-L09: `DEFERRED / NOT STARTED`.
- A01_L10: `PROHIBITED`.
- A01_L04: sole alliance-transform owner.
- Production/test/configuration/dependency/deploy changes in this task: `NONE`.
- Git operations by Codex: `NONE`.

## Current result

```text
V00_L05: COMPLETE / FROZEN / READ-ONLY
DESIGN LOCK: APPROVED
IMPLEMENTATION: COMPLETE
TEST-ONLY NONCOMMUTATIVITY FIXTURE REPAIR: COMPLETED
POST-REPAIR USER VERIFICATION: PASS / WPILib Java 17
POST-HARDENING USER VERIFICATION: PASS / WPILib Java 17
POST-IMPLEMENTATION ARCHITECTURE REVIEW: PASS
DOCUMENTATION: COMPLETE / PASS
FINAL ARCHITECT CLOSURE: APPROVED / COMPLETE
ACTIVE V00 LESSON COUNT: 0
GIT PUBLICATION: PUBLISHED @ 6482160 / USER VERIFIED
METADATA RECONCILIATION: PENDING USER GIT
```
