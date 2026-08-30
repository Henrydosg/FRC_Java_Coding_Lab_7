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
- **Design Lock:** LOCKED BY CHATGPT ARCHITECT
- **Implementation authorization:** AUTHORIZED BY ARCHITECT
- **Implementation:** COMPLETE / AUTHORIZED BOUNDARY
- **Verification:** PASS / FOCUSED, VISION REGRESSION, FULL SUITE, CLEAN BUILD
- **Final Architecture Review:** PASS
- **Documentation:** FINAL CLOSURE RECONCILIATION COMPLETE
- **Final closure:** PASS / COMPLETE / FROZEN / READ-ONLY
- **Publication:** PUBLISHED @ d58bef0 / USER VERIFIED
- **Publication commit:** d58bef0d17d202ce1dd0b8645635a8c35095dd3f
- **Publication subject:** Complete V00_L07 vision timestamp and latency contract
- **Git publication:** PASS / USER VERIFIED / origin/main
- **HEAD == origin/main:** PASS

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

## Completed preparation and activation

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

## Completed implementation and verification

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

## Verification strategy

Because the locked responsibility is a pure deterministic contract, verification
used focused unit testing, inherited regression, the complete test suite, and a
clean build. HALSIM, Glass, Driver Station, real-robot, and physical-camera
verification are not required for this contract-only lesson.

## Final lifecycle boundary

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
