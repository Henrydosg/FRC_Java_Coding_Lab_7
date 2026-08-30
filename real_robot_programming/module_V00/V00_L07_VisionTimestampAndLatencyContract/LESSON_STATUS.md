# LESSON_STATUS

## Identity

- **Module:** V00 - AprilTag Vision Observation and Pose Fusion
- **Lesson:** V00_L07_VisionTimestampAndLatencyContract
- **Title:** V00_L07 - Vision Timestamp and Latency Contract
- **Previous Lesson:** V00_L06_VisionMeasurementQualityContract @ 1327bf4
- **Previous Lesson State:** COMPLETE / FROZEN / READ-ONLY / PUBLISHED
- **Predecessor Metadata Reconciliation:** 49c4286
- **Status:** COMPLETE
- **Active State:** COMPLETE / FROZEN / READ-ONLY
- **Freeze State:** FROZEN
- **Active Lesson Count:** 0
- **Lesson Goal:** deterministic vendor-neutral measurement timestamp and
  latency semantics, including freshness, ordering, and duplicate handling

## Required status fields

- **Architecture Review:** PASS / INHERITANCE AND DESIGN-LOCK GATES
- **Baseline Build:** PASS / USER VERIFIED / WPILib Java 17 / INHERITED
- **Build:** PASS / CLEAN `build`
- **Automated Verification:** PASS / FOCUSED, VISION REGRESSION, FULL SUITE
- **Simulation:** NOT APPLICABLE / PURE DETERMINISTIC CONTRACT
- **Driver Station / Glass:** NOT APPLICABLE / NO RUNTIME TELEMETRY
- **Real Robot:** NOT APPLICABLE / NO CAMERA, FUSION, OR ACTUATION
- **Transition Guide:** PASS / IMPLEMENTATION AND VERIFICATION RECORDED
- **Final Architecture Review:** PASS / READ-ONLY CLOSURE REVIEW
- **Documentation:** PASS / FINAL CLOSURE RECONCILIATION COMPLETE
- **Closure:** PASS / AUTHORIZED COMPLETE AND FROZEN
- **Publication:** NOT YET PUBLISHED
- **Git Commit:** PENDING USER GIT
- **Git Push:** PENDING USER GIT
- **Known Issues:** NONE CURRENT. The first sandbox-local Java compiler attempt
  exposed a classpath/file-access environment defect; the authorized Java 17
  runs with normal filesystem access passed and supersede that diagnostic.
  This historical diagnostic is not a lesson defect.

## Lifecycle gates

| Gate | Result | Evidence / meaning |
| --- | --- | --- |
| V00_L06 authority | PASS | V00_L06 is frozen and published at 1327bf4; lesson-local publication metadata reconciliation is recorded at 49c4286. |
| User copy/rename preparation | PASS | The candidate has the ADR-locked V00_L07 identity. |
| Generated-artifact handling | PASS | Copied generated build output was removed before the inherited baseline build. |
| Inheritance audit | PASS | 236 comparable non-generated files matched with zero differences. |
| Production Java inheritance | PASS | 77 inherited production Java files are identical. |
| Test Java inheritance | PASS | 63 inherited test Java files are identical. |
| Build/configuration/dependencies/assets | PASS | Gradle, wrapper, vendordeps, deploy/resources, and PathPlanner content are inherited unchanged. |
| Baseline build | PASS | User supplied WPILib Java 17 clean-build evidence: BUILD SUCCESSFUL in 55s, exit code 0. |
| Frozen Backbone | PASS / PRESERVED | RobotContainer, package responsibilities, observation flow, and dependency direction remain unchanged. |
| Frozen Interface Contract | PASS / PRESERVED | Existing IO and observation interfaces remain unchanged during activation. |
| Document C | PASS / PRESERVED | Future timing data remains vendor-neutral, immutable, and separate from telemetry and control. |
| Predecessor protection | PASS | V00_L06 remains frozen and unchanged. |
| Design Lock | LOCKED | ChatGPT Architect locked the V00_L07 timing and latency responsibility. |
| Controlled activation | PASS | V00_L07 is now the sole IN_PROGRESS / DESIGN LOCKED / EDITABLE lesson. |
| Implementation authorization | PASS | Architect authorization permitted only the locked vendor-neutral timing contract and focused tests. |
| Implementation | PASS | Added `VisionTiming` and `VisionTimingEvaluator`; no existing production contract or runtime wiring changed. |
| Focused timing tests | PASS | `VisionTimingTest` and `VisionTimingEvaluatorTest` passed under WPILib Java 17; exit code 0. |
| Vision regression tests | PASS | Existing VisionIO, VisionIOSim, VisionObservation, L06 quality, L05 pose-estimator, and L03 transform tests passed; exit code 0. |
| Full test suite | PASS | 593 tests, 0 failures, 0 errors, and 0 skipped. |
| Clean build | PASS | `clean build` completed with 7 actionable tasks executed; exit code 0. |
| Final architecture review | PASS | The read-only review found no architecture, scope, inheritance, or verification blocker. |
| Final closure / freeze authorization | PASS | Architect-authorized lesson closure metadata records `COMPLETE / FROZEN / READ-ONLY`. |
| Documentation reconciliation | PASS | Lesson-local README, status, plan, checklist, and transition guide record the final closure state and preserve historical intermediate state. |
| Git publication | PENDING | User owns add, commit, and push. |

## Automated verification commands

All commands below were run with `JAVA_HOME` set to the repository's WPILib
Java 17 toolchain.

```text
.\gradlew.bat --no-daemon test --tests frc.robot.observation.vision.VisionTimingTest --tests frc.robot.observation.vision.VisionTimingEvaluatorTest
PASS / exit code 0

.\gradlew.bat --no-daemon test --tests frc.robot.io.vision.VisionIOTest --tests frc.robot.io.vision.VisionIOSimTest --tests frc.robot.observation.vision.VisionObservationTest --tests frc.robot.observation.vision.VisionMeasurementQualityEvaluatorTest --tests frc.robot.vision.AprilTagRobotPoseEstimatorTest --tests frc.robot.vision.VisionFrameTransformTest
PASS / exit code 0

.\gradlew.bat --no-daemon test
PASS / 593 tests / 0 failures / 0 errors / 0 skipped / exit code 0

.\gradlew.bat --no-daemon clean build
PASS / BUILD SUCCESSFUL in 22s / 7 actionable tasks executed / exit code 0
```

## Architect Design Lock

V00_L07 owns one concept: a vendor-neutral deterministic measurement timestamp
and latency contract, including freshness, ordering, and duplicate semantics.

The canonical conceptual relationship is:

~~~text
measurementTimestampSeconds
    = receiveTimestampSeconds - totalLatencySeconds
~~~

All temporal values use seconds. Latency is finite and nonnegative, with zero
allowed. Timestamps are finite. The measurement timestamp cannot be later than
the receive timestamp and must be compatible with the future estimator
timebase.

Freshness uses an explicit reference timestamp and explicit policy. No timing
evaluator may read a global clock. Ordering is deterministic: a newer timestamp
is ordered, an equal timestamp is a duplicate, and an older timestamp is
out-of-order. Stale classification uses measurement age against the explicit
freshness policy.

Malformed required inputs, NaN, infinity, negative latency, invalid negative
freshness policy, and an impossible later-than-receive measurement timestamp
are programming-contract errors, not ordinary measurement rejections.

## Deferred scope

- V00_L08 owns real camera adapter integration, vendor fields and conversion,
  synchronization, network transport, physical-camera integration, and camera
  verification.
- V00_L09 owns `SwerveDrivePoseEstimator.addVisionMeasurement(...)`, accepted
  vision fusion, estimator wiring, covariance/stddev selection, and runtime
  estimator correction.
- Limelight, PhotonVision, Swerve, RobotContainer, commands, scheduler,
  NetworkTables, telemetry, PathPlanner, alliance transforms, and hardware
  verification are outside this activation.

## Final closure result

~~~text
V00_L07: COMPLETE / FROZEN / READ-ONLY
IMPLEMENTATION AUTHORIZATION: AUTHORIZED BY ARCHITECT
IMPLEMENTATION: COMPLETE / AUTHORIZED BOUNDARY
AUTOMATED VERIFICATION: PASS
BUILD: PASS / CLEAN BUILD
FINAL ARCHITECTURE REVIEW: PASS
FINAL CLOSURE / FREEZE: PASS
PUBLICATION: PENDING USER GIT PUBLICATION
~~~

The historical activation state was `IN_PROGRESS / DESIGN LOCKED / EDITABLE`;
the final lesson content is now `COMPLETE / FROZEN / READ-ONLY`. No Git commit
or push is claimed.
