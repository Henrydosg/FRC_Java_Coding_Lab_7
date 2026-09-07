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
- Design Lock: PASS / R1/R2/R3 REPAIR DESIGN LOCK
- Implementation authorization: PASS / EXACT AUTHORIZED REPAIR BOUNDARY
- Implementation: PASS / R1/R2/R3 COMPLETE
- Verification: PASS / FOCUSED, INHERITED, AND 600/600 FULL SUITE
- Simulation: PASS / RUNTIME WPILIB SIMULATION
- Post-Implementation Architecture Review: PASS / READ-ONLY R1/R2/R3 REVIEW
- Final Read-Only Closure Review: PASS
- Final Closure / Freeze Authorization: PASS / AUTHORIZED COMPLETE AND FROZEN
- Documentation: PASS / FINAL RE-FREEZE METADATA RECONCILED
- Publication: HISTORICAL PRE-REPAIR BASELINE @ d58bef0 / CORRECTED REPAIR PUBLISHED @ 4704cfc / USER VERIFIED
- Historical publication commit: d58bef0d17d202ce1dd0b8645635a8c35095dd3f
- Historical publication subject: HISTORICAL: Complete V00_L07 vision timestamp and latency contract
- Git commit: HISTORICAL BASELINE PASS / CORRECTED REPAIR PASS / USER VERIFIED
- Git push: HISTORICAL BASELINE PASS / CORRECTED REPAIR PASS / origin/main / USER VERIFIED
- Corrected publication commit: 4704cfc0801910e30c8abb7cffcc467e4f4df016
- Corrected publication subject: Complete corrected V00_L07 Swerve integrity repair
- Current repository HEAD: `5c4ceb469f218a02daa1b9aecbe89890b7992daa` (`Reconcile corrected V00_L07 publication metadata`)
- Current repository origin/main: `5c4ceb469f218a02daa1b9aecbe89890b7992daa`
- Corrected V00_L07 repair publication identity: `4704cfc0801910e30c8abb7cffcc467e4f4df016` / USER VERIFIED

## Exceptional repair and re-freeze notice

The original Vision Timestamp and Latency Contract remains valid historical
lesson content. V00_L07 was exceptionally reopened only as the latest
authoritative repair point for three inherited Swerve integrity defects:

- R1: remove the command-layer dependency on the IO-owned
  `SwerveModuleIO.StaticFrictionStopReason` type;
- R2: make all-module stop fanout best-effort if one module stop throws; and
- R3: make physical-forward measured drive position and velocity coherent for
  `physicalForwardSign = -1`.

The timing semantics are not redesigned. The repair was separately authorized
and completed only within the R1/R2/R3 boundary; no tuning, calibration,
configuration, PathPlanner, vision, telemetry, or unrelated architecture was
changed. The authoritative drive ratio remains `6.75:1`.

The original `d58bef0` publication remains historical pre-repair evidence. The
fresh reopened baseline recorded 593/593 tests PASS; the post-repair full suite
recorded 600/600 tests PASS with 0 failures, 0 errors, and 0 skipped; the clean
build, runtime WPILib Simulation, post-implementation read-only architecture
review, and Frozen Backbone review also passed. At an earlier stage the robot
was unavailable and real-robot verification was deferred. Later User evidence
verifies Teleop and Autonomous usability. The unresolved BL quantitative
drivetrain anomaly remains **KNOWN / DEFERRED HARDWARE MAINTENANCE**; no BL
PASS, quantitative drivetrain PASS, matched-module claim, tuning completion,
calibration completion, or issue resolution is asserted. Under the explicit
Architect/User disposition, it does not block the Vision curriculum closure
sequence. V00_L08 is unactivated and protected. The historical
reconstruction-only procedure is SUPERSEDED PROSPECTIVELY for this exact
existing candidate by the authorized one-time preservation-based reconciliation
in the V00_L07 reopen ADR. The independent Limelight physical-evidence HOLD
remains unchanged.

## Current repair result

R1 moves command-facing `StaticFrictionStopReason` ownership to
`SwerveSubsystem`. The characterization command no longer depends on
`frc.robot.io`; the subsystem privately translates to the unchanged
`SwerveModuleIO` reason, preserving commissioning lifecycle semantics.

R2 makes `SwerveSubsystem.stop()` clear actuation state before attempting FL,
FR, BL, and BR. It attempts every module after a `RuntimeException`, preserves
the first exception, suppresses later exceptions in encounter order, and
rethrows the first after all attempts. CTRE drive/steer module-local exception
isolation was not part of this repair.

R3 keeps IO and Observation values in the raw sensor domain while making
`SwerveSubsystem` the owner of physical-forward normalization for both measured
position and velocity. The current robot configuration and `6.75:1` drive
ratio are unchanged; CTRE IO and Sim IO production code are unchanged.

The earlier planned hardware gate was bounded and did not expand scope. R1
needed no additional hardware verification for type ownership alone. The later
User evidence is preserved only at the supported Teleop and Autonomous
usability level; no stronger stop/Disable/no-unintended-restart claim is added
by this reconciliation. The BL quantitative anomaly remains deferred
maintenance; `physicalForwardSign` must not be changed merely to force a
`-1` case. Tuning, recalibration, autonomous, vision, Limelight, and PathPlanner
validation are excluded.

## Historical original preparation, implementation, and verification

The following record describes the original timing lesson closure before the
exceptional reopen. It is preserved as historical provenance.

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

## Historical original verification strategy

The completed verification strategy used deterministic focused unit tests,
inherited vision regression tests, the complete test suite, and a clean build.
The focused tests cover zero and positive finite latency, malformed numeric
values, impossible timestamp relationships, freshness, stale samples,
duplicates, out-of-order samples, repeated evaluation, and seconds-unit
consistency. The full suite reported 593 tests with 0 failures, 0 errors, and
0 skipped tests.

HALSIM, Glass, Driver Station, real-robot, and physical-Limelight verification
are not completion requirements for this pure contract lesson.

## Historical original implementation boundary and final lifecycle

The authorized implementation changed only V00_L07. It added two production
types and two focused test classes. No existing production or test contract was
modified. Gradle, vendordeps, deploy/resources, PathPlanner assets, V00_L06,
governance documents, and the V00 roadmap ADR remain unchanged.

The pure contract scope does not require Simulation, Driver Station / Glass,
real-robot, or physical-camera verification. Those gates remain outside this
lesson because no runtime wiring, telemetry, camera adapter, actuation, or
estimator fusion was added.

The historical pre-repair final lesson state was:

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

## Current exceptional repair state

~~~text
V00_L07: COMPLETE / FROZEN / READ-ONLY
REPAIR SCOPE: R1 / R2 / R3 ONLY
DESIGN LOCK: PASS
IMPLEMENTATION AUTHORIZATION: PASS / EXACT AUTHORIZED BOUNDARY
IMPLEMENTATION: PASS / R1/R2/R3 COMPLETE
AUTOMATED VERIFICATION: PASS / 600 TESTS / 0 FAILURES / 0 ERRORS / 0 SKIPPED
SIMULATION: PASS
POST-IMPLEMENTATION ARCHITECTURE REVIEW: PASS
REAL ROBOT: USER VERIFIED / TELEOP AND AUTONOMOUS USABILITY
BL QUANTITATIVE ANOMALY: KNOWN / DEFERRED HARDWARE MAINTENANCE
QUANTITATIVE DRIVETRAIN PASS: NOT CLAIMED
FINAL READ-ONLY CLOSURE REVIEW: PASS
RE-FREEZE: PASS / AUTHORIZED COMPLETE AND FROZEN
REPAIR PUBLICATION: PUBLISHED @ 4704cfc / USER VERIFIED
REPAIR PUBLICATION COMMIT: 4704cfc0801910e30c8abb7cffcc467e4f4df016
REPAIR PUBLICATION SUBJECT: Complete corrected V00_L07 Swerve integrity repair
HEAD == origin/main: PASS / USER VERIFIED
~~~

The complete/frozen/published state shown in the historical section describes
the pre-repair snapshot only. The repaired implementation, automated evidence,
Simulation, later User functional hardware evidence, and deferred BL
maintenance classification are reconciled. The final read-only closure review
passed and the Architect/User authorized re-freeze. The repaired lesson is now
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 4704cfc`; corrected publication is
User-verified at the recorded commit and subject. V00_L08 remains protected and
unactivated; its existing candidate may proceed only through the authorized
one-time preservation-based reconciliation. No production or test
implementation was changed by this documentation transition.
