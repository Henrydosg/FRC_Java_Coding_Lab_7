# V00_L04 Lesson Plan and Execution Record

## Current state

- **Predecessor:** `V00_L03 @ cc20d62`
- **Status:** `COMPLETE / FROZEN / READ-ONLY`
- **Implementation:** `IMPLEMENTED / VERIFIED`
- **Automated verification:** `PASS / USER VERIFIED / WPILib Java 17`
- **Post-implementation architecture review:** `PASS`
- **Artifact cleanup:** `PASS / USER REPORTED`
- **Documentation:** `COMPLETE / PASS`
- **Closure:** `AUTHORIZED / PASS`
- **Freeze:** `COMPLETE / FROZEN / READ-ONLY`
- **Publication:** `PUBLISHED @ 5461555 / USER VERIFIED`
- **Active lesson count:** `0`

## One-concept objective

Implement one deterministic, vendor-neutral simulation adapter for the frozen
`VisionIO` contract without adding pose estimation, measurement quality,
timing, real-camera integration, or pose-estimator fusion.

## Completed lifecycle phases

1. The User copied published V00_L03 to the approved V00_L04 identity.
2. The User removed inherited generated artifacts.
3. The User supplied WPILib Java 17 inherited baseline-build PASS evidence.
4. An initial lifecycle metadata conflict was identified and reconciled.
5. Inheritance, roadmap scope, Frozen Backbone, Frozen Interface Contract, and
   Document C audits passed.
6. The initial Design Lock proposal was refined.
7. The ChatGPT Architect approved the refined Design Lock.
8. Controlled activation made V00_L04 the sole `IN_PROGRESS / EDITABLE` lesson.
9. Separate implementation authorization approved exactly
   `VisionIOSim.java` and `VisionIOSimTest.java`.
10. The two-file implementation was completed.
11. Focused behavior verification passed.
12. An initial Codex-local Gradle/classpath failure was recorded.
13. The User reran the repository-standard WPILib Java 17 workflow.
14. `compileTestJava`, `VisionIOSimTest`, inherited vision regressions, the full
    suite, and the clean build all passed with exit code 0 where supplied.
15. The post-implementation read-only architecture review passed.
16. Temporary/untracked artifacts were audited and the User completed the
    authorized cleanup.
17. Documentation and the required transition guide were reconciled.
18. The final read-only architecture/documentation review returned `PASS` and
    readiness for Architect closure authorization.
19. The Architect authorized controlled closure.
20. V00_L04 was recorded as `COMPLETE / FROZEN / READ-ONLY`.
21. The User confirmed publication at `5461555`, with successful push,
    `HEAD == origin/main`, and a clean working tree.

## Implemented architecture

```text
official fieldToTag geometry
        +
known fieldToRobot ground truth
        +
fixed robotToCamera
        |
        v
fieldToCamera
        |
        v
cameraToTarget
        |
        v
VisionIOInputs
```

This is deterministic forward measurement synthesis. `setFrame(...)` is the
only progression mechanism. Every update overwrites the complete inherited
transport cycle, and validation is fail-atomic. There is no clock, randomness,
thread, vendor API, NetworkTables acquisition, Driver Station state, scheduler
state, runtime Observation producer, RobotContainer wiring, or telemetry.

V00_L05 will later use vision measurements in the opposite conceptual
direction to construct robot-pose candidates. L05 is deferred and has not been
created or implemented.

## Verification record

- Baseline build: `PASS / USER VERIFIED`.
- `compileTestJava`: `PASS / USER VERIFIED / EXIT CODE 0`.
- `VisionIOSimTest`: `PASS / USER VERIFIED / EXIT CODE 0`.
- Required inherited vision regressions: `PASS / USER VERIFIED / EXIT CODE 0`.
- Full test suite: `PASS / USER VERIFIED / EXIT CODE 0`.
- Clean build: `PASS / USER VERIFIED / EXIT CODE 0`.
- Post-implementation architecture review: `PASS`.
- Cleanup: `PASS / USER REPORTED`.

The earlier local classpath result is `RESOLVED / SUPERSEDED /
NON-REPRODUCIBLE`. No Gradle repair is proposed or authorized.

## Deferred and prohibited work

- L05 robot-pose candidate estimation and multi-tag solving;
- L06 quality, ambiguity, uncertainty, covariance, and acceptance policy;
- L07 timestamp, latency, freshness, ordering, and temporal policy;
- L08 real-camera/vendor integration and physical calibration;
- L09 Swerve pose-estimator vision fusion;
- runtime wiring, telemetry, autonomous, PathPlanner, Swerve, configuration,
  dependencies, or deploy assets;
- V00_L05 creation; and
- any Codex Git operation.

## Current closure point

The lesson content and lifecycle state are complete, frozen, and read-only.
User-owned Git publication is confirmed at `5461555`.
No V00_L05 lesson has been created.

```text
CURRENT STATE: COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 5461555 / USER VERIFIED
PUBLICATION: PUBLISHED @ 5461555 / USER VERIFIED
```
