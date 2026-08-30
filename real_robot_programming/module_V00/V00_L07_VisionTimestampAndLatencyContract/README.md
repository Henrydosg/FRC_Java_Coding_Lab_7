# V00_L07 - Vision Timestamp and Latency Contract

## Current lesson state

- Directory: `V00_L07_VisionTimestampAndLatencyContract`
- Authoritative predecessor: V00_L06_VisionMeasurementQualityContract @ 1327bf4
- Predecessor state: COMPLETE / FROZEN / READ-ONLY / PUBLISHED
- Predecessor publication reconciliation: 49c4286
- Status: COMPLETE
- Active state: COMPLETE / FROZEN / READ-ONLY
- Freeze state: FROZEN
- Active lesson count: 0
- Design Lock: LOCKED BY CHATGPT ARCHITECT
- Implementation authorization: AUTHORIZED BY ARCHITECT
- Implementation: COMPLETE / AUTHORIZED BOUNDARY
- Verification: PASS / FOCUSED, INHERITED REGRESSION, FULL SUITE, CLEAN BUILD
- Final Architecture Review: PASS
- Final Closure / Freeze Authorization: PASS
- Documentation: FINAL CLOSURE RECONCILIATION COMPLETE
- Publication: PUBLISHED @ d58bef0 / USER VERIFIED
- Publication commit: d58bef0d17d202ce1dd0b8645635a8c35095dd3f
- Publication subject: Complete V00_L07 vision timestamp and latency contract
- Git commit: PASS / USER VERIFIED
- Git push: PASS / origin/main / USER VERIFIED
- HEAD: d58bef0d17d202ce1dd0b8645635a8c35095dd3f
- origin/main: d58bef0d17d202ce1dd0b8645635a8c35095dd3f

V00_L07 was prepared by copying the final V00_L06 project and cleaning copied
generated output before the inherited baseline build. The inherited candidate
passed the User-controlled WPILib Java 17 baseline clean build:
`BUILD SUCCESSFUL in 55s`, `7 actionable tasks: 6 executed, 1 up-to-date`,
exit code `0`.

The Architect subsequently authorized implementation within the locked
vendor-neutral boundary. The implementation adds only `VisionTiming` and
`VisionTimingEvaluator` under `frc.robot.observation.vision`, plus their
focused deterministic tests. Existing VisionIO, VisionObservation,
VisionIOSim, Swerve, RobotContainer, telemetry, and estimator code remain
unchanged.

Implementation and automated verification are complete for this boundary. The
final read-only architecture review and closure/freeze authorization are also
PASS. V00_L07 is complete, frozen, read-only, and User-published at
`d58bef0`. The publication commit and push are complete; the separate
repository-level lifecycle reconciliation remains pending.

Historical lifecycle note: before the final review, this lesson was recorded as
`IN_PROGRESS / DESIGN LOCKED / EDITABLE` while implementation and verification
were completed. That intermediate state is retained as provenance; it is no
longer the current lesson state.

## One-concept objective

V00_L07 teaches a vendor-neutral deterministic contract that tells the robot
when a vision measurement occurred, not merely when the robot received it.
The locked concept includes:

- measurement or capture timestamp;
- robot receive timestamp;
- total capture-to-receive latency;
- freshness and stale-measurement semantics;
- duplicate semantics; and
- out-of-order semantics.

All temporal values use seconds.

## Locked timing relationship

The canonical conceptual relationship is:

~~~text
measurementTimestampSeconds
    = receiveTimestampSeconds - totalLatencySeconds
~~~

The measurement timestamp must be finite, must not be later than the receive
timestamp, and must eventually use the estimator-compatible robot timebase.
The Java implementation derives this value from the two independent timing
facts; it does not store a third mutable/source-of-truth timestamp.

## Locked deterministic semantics

Latency is finite and nonnegative. Zero latency is valid. Freshness is evaluated
against an explicitly supplied reference timestamp and an explicitly supplied
policy. No evaluator may read a global clock or hidden `Timer` state.

For valid timestamps, ordering is:

~~~text
new measurement timestamp > previous timestamp  -> newer / ordered
new measurement timestamp == previous timestamp -> duplicate
new measurement timestamp < previous timestamp  -> out-of-order
~~~

Stale classification is based on deterministic measurement age against the
explicit freshness policy.

## Programming-contract error boundary

Malformed structural inputs are not ordinary vision-measurement rejections.
The implementation rejects required nulls, NaN, positive or
negative infinity, negative latency, invalid negative freshness policy, and a
measurement timestamp later than the receive timestamp. It does not silently
normalize malformed data. `STALE` remains a valid-but-too-old classification,
not a malformed-input result.

## Architecture and package boundaries

V00_L07 preserves the Frozen Backbone and the inherited V00 architecture:

~~~text
vision acquisition facts
    -> immutable vendor-neutral timing contract
    -> pure deterministic timing evaluation
    -> timestamp / freshness / ordering result
~~~

RobotContainer remains the Composition Root only. Observation models remain
immutable and vendor-neutral. Telemetry remains read-only. No timing evaluator
may access hardware, vendor APIs, NetworkTables, commands, the scheduler, or
RobotContainer. Swerve, autonomous, alliance-transform ownership, and the
existing V00_L06 measurement-quality contract remain unchanged.

## Deferred responsibilities

V00_L08 owns the real camera adapter, Limelight/PhotonVision compatibility,
vendor timestamp and latency fields, vendor timebase conversion, camera
synchronization, network transport, physical-camera integration, and real
camera verification.

V00_L09 owns `SwerveDrivePoseEstimator.addVisionMeasurement(...)`, accepted
vision fusion, estimator wiring, covariance or standard-deviation selection,
and runtime estimator-correction behavior.

V00_L07 introduces none of those responsibilities. No camera vendor is selected
through V00_L07.

## Verification strategy

The completed verification strategy used deterministic focused unit tests,
inherited vision regression tests, the complete test suite, and a clean build.
The focused tests cover zero and positive finite latency, malformed numeric
values, impossible timestamp relationships, freshness, stale samples,
duplicates, out-of-order samples, repeated evaluation, and seconds-unit
consistency. The full suite reported 593 tests with 0 failures, 0 errors, and
0 skipped tests.

HALSIM, Glass, Driver Station, real-robot, and physical-Limelight verification
are not completion requirements for this pure contract lesson.

## Implementation boundary and final lifecycle

The authorized implementation changed only V00_L07. It added two production
types and two focused test classes. No existing production or test contract was
modified. Gradle, vendordeps, deploy/resources, PathPlanner assets, V00_L06,
governance documents, and the V00 roadmap ADR remain unchanged.

The pure contract scope does not require Simulation, Driver Station / Glass,
real-robot, or physical-camera verification. Those gates remain outside this
lesson because no runtime wiring, telemetry, camera adapter, actuation, or
estimator fusion was added.

The final lesson state is:

~~~text
V00_L07: COMPLETE / FROZEN / READ-ONLY
IMPLEMENTATION AUTHORIZATION: AUTHORIZED BY ARCHITECT
IMPLEMENTATION: COMPLETE / AUTHORIZED BOUNDARY
AUTOMATED VERIFICATION: PASS
FULL TEST SUITE: PASS / 593 TESTS / 0 FAILURES / 0 ERRORS / 0 SKIPPED
CLEAN BUILD: PASS / BUILD SUCCESSFUL in 22s / 7 ACTIONABLE TASKS EXECUTED / EXIT CODE 0
FINAL ARCHITECTURE REVIEW: PASS
FINAL CLOSURE / FREEZE: PASS
PUBLICATION: PUBLISHED @ d58bef0 / USER VERIFIED
PUBLICATION COMMIT: d58bef0d17d202ce1dd0b8645635a8c35095dd3f
PUBLICATION SUBJECT: Complete V00_L07 vision timestamp and latency contract
HEAD == origin/main: PASS
~~~
