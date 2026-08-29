# LESSON_STATUS

## Identity

- **Module:** V00 - AprilTag Vision Observation and Pose Fusion
- **Lesson:** V00_L06_VisionMeasurementQualityContract
- **Title:** V00_L06 - Vision Measurement Quality Contract
- **Previous Lesson:** V00_L05_AprilTagRobotPoseEstimation @ 6482160
- **Previous Lesson State:** COMPLETE / FROZEN / READ-ONLY / PUBLISHED
- **Predecessor Metadata Reconciliation:** 3161dfb
- **Status:** COMPLETE
- **Active State:** COMPLETE / FROZEN / READ-ONLY
- **Freeze State:** FROZEN / READ-ONLY
- **Active Lesson Count:** 0
- **Lesson Goal:** deterministic distance-based acceptance and qualitative
  uncertainty classification for one immutable TargetObservation

## Required status fields

- **Architecture Review:** PASS / DESIGN LOCK APPROVED
- **Baseline Build:** PASS / USER VERIFIED / WPILib Java 17 / INHERITED
- **Build:** PASS / USER VERIFIED / WPILib Java 17 / STANDARD CLEAN BUILD
- **Automated Verification:** PASS / USER VERIFIED / WPILib Java 17 / FOCUSED, INHERITED, AND FULL SUITE
- **Simulation:** NOT APPLICABLE / PURE STATELESS CONTRACT
- **Driver Station / Glass:** NOT APPLICABLE / NO RUNTIME TELEMETRY
- **Real Robot:** NOT APPLICABLE / NO CAMERA, FUSION, OR ACTUATION
- **Transition Guide:** COMPLETE / PASS
- **Documentation:** COMPLETE / PASS
- **Closure:** APPROVED / COMPLETE
- **Git Commit:** PENDING USER GIT
- **Git Push:** PENDING USER GIT
- **Known Issues:** NONE CURRENT. The earlier TERRA/Codex-local Windows
  Gradle/JDK test-classpath failure is historical and classified as
  `SUPERSEDED / ENVIRONMENT-PROCESS-ONLY`; User-controlled standard WPILib
  Java 17 verification completed successfully.

## Lifecycle gates

| Gate | Result | Evidence / meaning |
| --- | --- | --- |
| V00_L05 authority | PASS | V00_L05 is complete, frozen, read-only, and User-published at 6482160. |
| Predecessor metadata reconciliation | PASS | User-published reconciliation is recorded at 3161dfb. |
| User copy/rename preparation | PASS | Candidate has the ADR-locked V00_L06 identity. |
| Generated-artifact handling | PASS | Copied generated artifacts were removed before baseline verification. |
| Inheritance audit | PASS | 232 comparable non-generated files; zero differences. |
| Production Java inheritance | PASS | 75 inherited production Java files are identical. |
| Test Java inheritance | PASS | 62 inherited test Java files are identical. |
| Build/configuration/dependencies/assets | PASS | Gradle, wrapper, vendordeps, deploy/resources, and PathPlanner content are inherited unchanged. |
| Baseline build | PASS | User supplied WPILib Java 17 inherited baseline-build evidence. |
| Frozen Backbone | PASS / PRESERVED | Package responsibilities and dependency direction remain unchanged. |
| Frozen Interface Contract | PASS / PRESERVED | Existing IO and immutable observation contracts remain unchanged. |
| Document C | PASS / PRESERVED | Quality types remain immutable, vendor-neutral read models and pure evaluation. |
| Predecessor protection | PASS | V00_L01-L05 remain frozen and unchanged. |
| Design Lock | APPROVED | Architect approved the exact distance-only quality contract below. |
| Controlled activation | PASS | Historical activation made V00_L06 the sole IN_PROGRESS / EDITABLE lesson; it is now frozen. |
| Production implementation authorization | GRANTED | Exactly two new production files are authorized. |
| Focused test authorization | GRANTED | Exactly one new focused test file is authorized. |
| Implementation | PASS / COMPLETE | Exactly two new production files and one focused test file implement the authorized boundary. |
| Production compile | PASS / USER VERIFIED | Standard compileJava passed under WPILib Java 17. |
| Standard test compile | PASS / USER VERIFIED | User-controlled standard compileTestJava passed under WPILib Java 17. |
| Focused verification | PASS / USER VERIFIED | VisionMeasurementQualityEvaluatorTest passed under the standard verification workflow. |
| Inherited regression | PASS / USER VERIFIED | Required inherited vision regressions passed under WPILib Java 17. |
| Full test suite | PASS / USER VERIFIED | The complete lesson test suite passed under WPILib Java 17. |
| Clean build | PASS / USER VERIFIED | Standard clean build passed with BUILD SUCCESSFUL. |
| Final architecture review | PASS | Independent read-only review found architecture and implementation PASS; the required lesson-local documentation reconciliation is complete. |
| Final closure | APPROVED / COMPLETE | ChatGPT Architect authorized final closure and freeze metadata. |
| Git publication | PENDING | User owns add, commit, and push. |
| Freeze transition | PASS | V00_L06 is COMPLETE / FROZEN / READ-ONLY; Git publication remains separate and pending. |

## Architect-approved Design Lock

### Responsibility and flow

V00_L06 owns one pure deterministic policy decision:

~~~text
TargetObservation
-> camera-to-target translation norm
-> ordered inclusive distance thresholds
-> immutable VisionMeasurementQuality
~~~

The evaluator does not receive the V00_L05 Pose3d candidate or a whole
VisionObservation.

### Exact production boundary

Only these two new files are authorized:

~~~text
src/main/java/frc/robot/observation/vision/VisionMeasurementQuality.java
src/main/java/frc/robot/observation/vision/VisionMeasurementQualityEvaluator.java
~~~

VisionMeasurementQuality is a public immutable record with nested public enums:

- Acceptance: ACCEPTED, REJECTED
- UncertaintyClass: LOW, MEDIUM, HIGH, UNUSABLE
- RejectionReason: NONE, TARGET_TOO_FAR

Its compact constructor accepts only:

- ACCEPTED / LOW / NONE
- ACCEPTED / MEDIUM / NONE
- ACCEPTED / HIGH / NONE
- REJECTED / UNUSABLE / TARGET_TOO_FAR

Null enum values and every other tuple are invalid programming states.

VisionMeasurementQualityEvaluator is a final stateless utility with:

~~~java
public static VisionMeasurementQuality evaluate(
    VisionObservation.TargetObservation target,
    Policy policy)
~~~

Its nested public immutable Policy record owns lowMaxMeters,
mediumMaxMeters, and maximumAcceptedMeters.

### Policy and classification

Policy values must be finite and satisfy:

~~~text
0 <= lowMaxMeters <= mediumMaxMeters <= maximumAcceptedMeters
~~~

Equality is explicitly valid. Ordered inclusive classification is:

- distance <= lowMaxMeters: ACCEPTED / LOW / NONE
- distance <= mediumMaxMeters: ACCEPTED / MEDIUM / NONE
- distance <= maximumAcceptedMeters: ACCEPTED / HIGH / NONE
- otherwise: REJECTED / UNUSABLE / TARGET_TOO_FAR

Distance is exactly:

~~~java
target.cameraToTarget().getTranslation().getNorm()
~~~

The computed norm must be checked with Double.isFinite. Null required
arguments are programming errors and produce NullPointerException. Invalid
policy values, invalid quality tuples, and nonfinite computed distance produce
IllegalArgumentException. They are not measurement rejections.

### Exact test boundary

Only this new focused test file is authorized:

~~~text
src/test/java/frc/robot/observation/vision/VisionMeasurementQualityEvaluatorTest.java
~~~

It must lock threshold boundaries, equality policies, malformed policy input,
nonfinite target geometry and norm overflow, all valid/invalid quality tuples,
determinism, caller immutability, and null handling.

## Protected and deferred scope

- V00_L01-L05 remain COMPLETE / FROZEN / READ-ONLY / PUBLISHED.
- V00_L07 owns timestamp and latency semantics.
- V00_L08 owns the reviewed real-camera adapter.
- V00_L09 owns accepted timestamped fusion into the Swerve-owned estimator.
- No vendor API, whole-observation scoring, target ranking, covariance,
  standard deviation, fusion, alliance transform, runtime wiring, telemetry,
  NetworkTables, autonomous, PathPlanner, or actuator behavior is authorized.
- A01_L04 remains the sole alliance-transform owner.
- A01_L10 remains prohibited.

## Current result

~~~text
V00_L06: COMPLETE / FROZEN / READ-ONLY
CONTROLLED ACTIVATION: PASS
DESIGN LOCK: APPROVED
IMPLEMENTATION AUTHORIZATION: GRANTED
IMPLEMENTATION: COMPLETE / AUTHORIZED BOUNDARY
PRODUCTION COMPILE: PASS / CODEX-LOCAL / JAVA 17
FOCUSED TEST: PASS / USER VERIFIED / WPILib Java 17
INHERITED VISION REGRESSIONS: PASS / USER VERIFIED / WPILib Java 17
FULL TEST SUITE: PASS / USER VERIFIED / WPILib Java 17
STANDARD compileTestJava: PASS / USER VERIFIED
CLEAN BUILD: PASS / USER VERIFIED / BUILD SUCCESSFUL
EARLIER TERRA/CODEX-LOCAL HOLD: SUPERSEDED / ENVIRONMENT-PROCESS-ONLY / NOT A CURRENT BLOCKER
TRANSITION GUIDE: COMPLETE / PASS
FINAL ARCHITECTURE REVIEW: PASS / DOCUMENTATION RECONCILIATION COMPLETE
FINAL CLOSURE: APPROVED / COMPLETE
FREEZE STATE: FROZEN / READ-ONLY
ACTIVE V00 LESSON COUNT: 0
GIT PUBLICATION: PENDING USER GIT
~~~
