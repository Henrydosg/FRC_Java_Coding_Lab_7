# V00_L07 Lesson Plan and Lifecycle Record

## Current state

- **Lesson:** V00_L07 - Vision Timestamp and Latency Contract
- **Directory:** V00_L07_VisionTimestampAndLatencyContract
- **Predecessor:** V00_L06_VisionMeasurementQualityContract @ 1327bf4
- **Predecessor metadata reconciliation:** 49c4286
- **Predecessor state:** COMPLETE / FROZEN / READ-ONLY / PUBLISHED
- **Status:** COMPLETE / FROZEN / READ-ONLY
- **Freeze state:** FROZEN
- **Active lesson count:** 0
- **Design Lock:** PASS / R1/R2/R3 REPAIR DESIGN LOCK
- **Implementation authorization:** PASS / EXACT AUTHORIZED REPAIR BOUNDARY
- **Implementation:** PASS / R1/R2/R3 COMPLETE
- **Verification:** PASS / 600 TESTS, CLEAN BUILD, AND SIMULATION
- **Post-Implementation Architecture Review:** PASS / READ-ONLY R1/R2/R3 REVIEW
- **Documentation:** PASS / FINAL RE-FREEZE METADATA RECONCILED
- **Final closure:** PASS / AUTHORIZED COMPLETE AND FROZEN
- **Publication:** HISTORICAL PRE-REPAIR BASELINE @ d58bef0 / CORRECTED REPAIR PUBLISHED @ 4704cfc / USER VERIFIED
- **Historical publication commit:** d58bef0d17d202ce1dd0b8645635a8c35095dd3f
- **Historical publication subject:** HISTORICAL: Complete V00_L07 vision timestamp and latency contract
- **Corrected publication commit:** 4704cfc0801910e30c8abb7cffcc467e4f4df016
- **Corrected publication subject:** Complete corrected V00_L07 Swerve integrity repair
- **Git publication:** HISTORICAL BASELINE PASS / CORRECTED REPAIR PASS / USER VERIFIED
- **HEAD == origin/main at corrected repair publication:** PASS / 4704cfc0801910e30c8abb7cffcc467e4f4df016 / USER VERIFIED

## Exceptional inherited Swerve integrity repair cycle (current)

This documentation reconciliation records the completed authorized repair.
The original V00_L07 timing objective remains valid; the reopen addressed
exactly three inherited Swerve integrity findings selected by the Architect
and approved by the User:

- R1: remove the command-layer dependency on the IO-owned
  `SwerveModuleIO.StaticFrictionStopReason` type.
- R2: make `SwerveSubsystem` all-module stop fanout best-effort when one stop
  throws.
- R3: make `physicalForwardSign = -1` coherent for measured drive position and
  velocity.

The authoritative drive ratio remains `6.75:1`. Excluded are live module-health
policy, CAN/status redesign, nonfinite/general IO cleanup, telemetry, tuning,
calibration, odometry, pose estimation, autonomous, PathPlanner, vision,
Limelight, AprilTag, pose fusion, and unrelated refactoring. The Frozen
Backbone, Frozen Interface Contract, and roadmap remain unchanged.

The required sequence is:

1. Record Architect/User governance authorization and the exceptional reopen.
2. Perform the documentation-only transition to the sole
   `REOPENED / IN_PROGRESS / EDITABLE` V00_L07 lesson.
3. Have the User establish a fresh Java 17 inherited baseline and changed-file
   boundary — **COMPLETE; 593/593 baseline tests PASS**.
4. Perform the focused Architecture Audit and obtain the exact R1/R2/R3
   Design Lock — **COMPLETE / PASS**.
5. Obtain separate implementation authorization naming the minimum files —
   **COMPLETE / PASS**.
6. Implement only R1/R2/R3 and add only authorized focused tests —
   **COMPLETE / PASS**.
7. Run focused regressions, inherited/full tests, and a clean build —
   **COMPLETE / PASS; 600/600 full suite, clean build PASS**.
8. Complete applicable Simulation and reconcile later real-robot evidence —
   **Simulation PASS; Teleop and Autonomous usability USER VERIFIED; BL
   quantitative anomaly KNOWN / DEFERRED HARDWARE MAINTENANCE and not a
   quantitative drivetrain PASS**.
9. Complete the post-implementation architecture review, documentation
   reconciliation, final read-only closure review, and explicit re-freeze —
   **COMPLETE / PASS / AUTHORIZED**.
10. Only then, under the separately authorized one-time amendment, preserve
    and reconcile the existing V00_L08 candidate by forward-porting exactly the
    seven authorized R1/R2/R3 files from corrected published V00_L07.

The repaired lesson passed final read-only closure review, received explicit
Architect/User re-freeze authorization, and is now `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED @ 4704cfc`. Corrected publication is User-verified at
`4704cfc0801910e30c8abb7cffcc467e4f4df016`. The independent V00_L08
Limelight physical-evidence HOLD remains unchanged.

## Current repair verification and final closure state

The following evidence applies to the repaired cycle and is distinct from the
historical original timing-lesson closure recorded later in this document:

- Pre-repair reopened baseline: 593 tests, 0 failures, 0 errors, 0 skipped;
  clean build PASS.
- Focused R1/R2/R3 verification: PASS.
- Inherited Swerve regressions: PASS.
- Post-repair full suite: 600 tests, 0 failures, 0 errors, 0 skipped.
- Post-repair clean build: PASS.
- Runtime WPILib Simulation: PASS.
- Post-implementation read-only architecture review: PASS.
- Frozen Backbone review: PASS.

R1 moves command-facing `StaticFrictionStopReason` ownership to
`SwerveSubsystem`. The command no longer depends on `frc.robot.io`; the
subsystem privately translates to the unchanged `SwerveModuleIO` reason, and
commissioning lifecycle semantics remain equivalent.

R2 makes `SwerveSubsystem.stop()` clear actuation state before attempting FL,
FR, BL, and BR. Every module is attempted after a `RuntimeException`; the
first exception is preserved, later exceptions are suppressed in encounter
order, and the first is rethrown after fanout. CTRE drive/steer module-local
exception isolation was not part of the repair.

R3 keeps IO and Observation values in the raw sensor domain while making
`SwerveSubsystem` the owner of physical-forward normalization. Measured
position and velocity use the same `physicalForwardSign` semantics. The
current robot configuration and `6.75:1` drive ratio are unchanged, and CTRE
IO and Sim IO production code remain unchanged.

At an earlier repair stage the robot was unavailable, so the real-robot gate
was recorded as deferred. Later User evidence verifies Teleop and Autonomous
usability. This reconciliation does not add a stronger bounded
stop/Disable/no-unintended-restart claim beyond the supplied evidence and does
not claim quantitative drivetrain verification.

The BL quantitative drivetrain anomaly remains **KNOWN / DEFERRED HARDWARE
MAINTENANCE**. It is unresolved and is not BL PASS, quantitative drivetrain
PASS, matched-module evidence, completed tuning, or completed calibration.
Under the explicit Architect/User disposition, it does not block continued
Vision curriculum closure. No Swerve diagnosis or repair is part of this task.

## One-concept objective

Understand and implement a deterministic vendor-neutral contract that tells
the robot when a vision measurement occurred, not merely when it arrived.

The one concept includes measurement/capture time, robot receive time, total
capture-to-receive latency, freshness, stale-measurement handling, duplicate
handling, and out-of-order handling. It does not select a camera or fuse a
pose.

## Locked timing semantics

All temporal values use seconds. The canonical relationship is:

~~~text
measurementTimestampSeconds
    = receiveTimestampSeconds - totalLatencySeconds
~~~

Latency is finite and nonnegative; zero is valid. Timestamps are finite. The
measurement timestamp must not be later than the receive timestamp and must
eventually use the same compatible robot timebase as the future estimator.

Freshness is evaluated using an explicit reference timestamp and explicit
policy. No evaluator reads a global clock. For valid timestamps:

~~~text
new > previous  -> newer / ordered
new == previous -> duplicate
new < previous  -> out-of-order
~~~

Stale classification uses measurement age against the explicit freshness
policy.

## Locked error boundary

Null required inputs, NaN, infinity, negative latency, invalid negative
freshness policy, and a measurement timestamp later than the receive timestamp
are programming-contract errors. They must not be silently normalized or
reported as ordinary measurement-quality rejection. This error boundary is
implemented by the authorized production contract.

## Historical original preparation and activation (preserved)

1. The final frozen V00_L06 predecessor was confirmed at `1327bf4`; its
   lesson-local publication metadata reconciliation is `49c4286`.
2. The User copied the final V00_L06 project to the ADR-locked V00_L07
   directory and cleaned copied generated output before the baseline build.
3. The inheritance audit found 236 comparable non-generated files with zero
   differences, including 77 production Java and 63 test Java files.
4. Gradle, wrapper, vendordeps, deploy/resources, and PathPlanner assets remain
   inherited unchanged.
5. The User supplied WPILib Java 17 baseline evidence:
   `BUILD SUCCESSFUL in 55s`, 7 actionable tasks, 6 executed and 1 up-to-date,
   exit code 0.
6. Frozen Backbone, Frozen Interface Contract, Document C, predecessor
   protection, and roadmap-scope audits passed.
7. The Architect formally locked the V00_L07 timing and latency concept.
8. Controlled activation changed lesson-local metadata only; that historical
   stage remained implementation-free.
9. The Architect then authorized exactly two production types and two focused
   test classes within the locked vendor-neutral boundary.
10. Focused timing tests, inherited vision regressions, the complete 593-test
    suite, and a clean build all passed under the WPILib Java 17 environment.

## Historical original implementation and verification (preserved)

The authorized implementation added only:

- `src/main/java/frc/robot/observation/vision/VisionTiming.java`
- `src/main/java/frc/robot/observation/vision/VisionTimingEvaluator.java`
- `src/test/java/frc/robot/observation/vision/VisionTimingTest.java`
- `src/test/java/frc/robot/observation/vision/VisionTimingEvaluatorTest.java`

`VisionTiming` derives the canonical measurement timestamp from receive time
and total latency. `VisionTimingEvaluator` performs explicit-input freshness
and ordering classification. No existing production or test contract was
modified, and no camera, runtime, telemetry, or fusion work was added.

Verification evidence:

1. Focused timing tests passed with exit code `0`.
2. Existing VisionIO, VisionIOSim, VisionObservation, L06 quality, L05 pose
   estimator, and L03 frame-transform regression tests passed with exit code
   `0`.
3. The complete test suite passed: 593 tests, 0 failures, 0 errors, and 0
   skipped.
4. `clean build` passed with 7 actionable tasks executed and exit code `0`.

The first sandbox-local Java compiler attempt reported a classpath/file-access
environment defect. The same authorized commands passed with normal filesystem
access under the repository's WPILib Java 17 environment; that diagnostic is
not a code failure.

## Historical remaining lifecycle workflow

The following workflow was pending when the implementation documentation was
first reconciled. It is retained as historical provenance; the first three
items were subsequently completed by the final read-only review and authorized
closure update. At that historical stage, User-owned Git publication remained
pending; it was subsequently completed at `d58bef0`.

1. Complete final architecture review and record its decision.
2. Obtain the separate User/Architect closure decision.
3. If closure is authorized, update freeze metadata only then.
4. The User performs Git add, commit, and push.

No step above authorizes camera integration, runtime wiring, estimator fusion,
or hardware verification.

## Deferred responsibilities

V00_L08 owns the real camera adapter, vendor compatibility decision, vendor
timestamp/latency fields, timebase conversion, synchronization, transport,
physical-camera integration, and camera verification.

V00_L09 owns accepted vision fusion through
`SwerveDrivePoseEstimator.addVisionMeasurement(...)`, estimator wiring,
covariance/stddev selection, and runtime estimator correction.

## Historical pre-publication closure and freeze

The final read-only architecture review passed with no blocking finding. The
Architect authorized the lesson-local lifecycle transition from
`IN_PROGRESS / DESIGN LOCKED / EDITABLE` to
`COMPLETE / FROZEN / READ-ONLY`. The implementation, User verification,
Frozen Backbone, observation purity, L08 deferral, and L09 deferral remain
unchanged by this metadata transition.

At that historical closure point, publication was a separate User-owned gate:
`PENDING USER GIT PUBLICATION`. The subsequent User publication is recorded in
the current-state section below.

## Lesson-local publication reconciliation

The User subsequently published the frozen V00_L07 lesson with commit
`d58bef0d17d202ce1dd0b8645635a8c35095dd3f` (`d58bef0`). The commit subject is
`Complete V00_L07 vision timestamp and latency contract`. User verification
recorded commit PASS, push PASS to `origin/main`, and HEAD equal to
`origin/main` at the same full commit.

This records lesson-local publication metadata only. Repository-level
reconciliation of AGENTS.md and the root README remains a separate pending
task before V00_L08 preparation.

## Historical original verification strategy

Because the locked responsibility is a pure deterministic contract, verification
used focused unit testing, inherited regression, the complete test suite, and a
clean build. HALSIM, Glass, Driver Station, real-robot, and physical-camera
verification are not required for this contract-only lesson.

## Historical original final lifecycle boundary

V00_L07 was historically active because the Architect Design Lock was complete.
Its authorized implementation and automated verification are complete, and the
final architecture review authorized closure and freeze. Publication remains
separate and complete at the lesson level:

~~~text
STATUS: COMPLETE
ACTIVE STATE: COMPLETE / FROZEN / READ-ONLY
FREEZE STATE: FROZEN
IMPLEMENTATION AUTHORIZATION: AUTHORIZED BY ARCHITECT
IMPLEMENTATION: COMPLETE / AUTHORIZED BOUNDARY
VERIFICATION: PASS
FULL TEST SUITE: PASS / 593 TESTS / 0 FAILURES / 0 ERRORS / 0 SKIPPED
CLEAN BUILD: PASS / BUILD SUCCESSFUL in 22s / 7 ACTIONABLE TASKS EXECUTED / EXIT CODE 0
FINAL ARCHITECTURE REVIEW: PASS
FINAL CLOSURE / FREEZE: PASS
PUBLICATION: PUBLISHED @ d58bef0 / USER VERIFIED
PUBLICATION COMMIT: d58bef0d17d202ce1dd0b8645635a8c35095dd3f
PUBLICATION SUBJECT: Complete V00_L07 vision timestamp and latency contract
PUSH: PASS / origin/main / USER VERIFIED
HEAD == origin/main: PASS
~~~

## Current exceptional repair result

~~~text
STATUS: COMPLETE
ACTIVE STATE: COMPLETE / FROZEN / READ-ONLY
FREEZE STATE: FROZEN
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
RE-FREEZE: PASS / EXPLICIT ARCHITECT/USER AUTHORIZATION
REPAIR PUBLICATION: PUBLISHED @ 4704cfc / USER VERIFIED
REPAIR PUBLICATION COMMIT: 4704cfc0801910e30c8abb7cffcc467e4f4df016
REPAIR PUBLICATION SUBJECT: Complete corrected V00_L07 Swerve integrity repair
HEAD == origin/main: PASS / USER VERIFIED
~~~

The original timing lesson closure and publication at `d58bef0` remain
historical pre-repair evidence. The current repair implementation, automated
evidence, Simulation, later User functional hardware evidence, and deferred BL
maintenance classification are reconciled. The repaired lesson passed final
read-only closure review, received explicit Architect/User re-freeze
authorization, and is `COMPLETE / FROZEN / READ-ONLY`.
V00_L08 remains protected and will require the separately authorized
preservation-based reconciliation from the published corrected V00_L07. This
documentation reconciliation changes no production or test implementation.
