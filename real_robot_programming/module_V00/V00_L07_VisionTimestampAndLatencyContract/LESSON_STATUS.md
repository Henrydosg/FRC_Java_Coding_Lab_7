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

- **Architecture Review:** PASS / POST-IMPLEMENTATION READ-ONLY REPAIR REVIEW; FROZEN BACKBONE PASS
- **Baseline Build:** PASS / FRESH PRE-REPAIR BASELINE; 593/593 TESTS PASS
- **Build:** PASS / POST-REPAIR CLEAN BUILD
- **Automated Verification:** PASS / FOCUSED, INHERITED, AND 600/600 FULL SUITE
- **Simulation:** PASS / RUNTIME WPILIB SIMULATION
- **Driver Station / Glass:** NOT TESTED / NO SEPARATE POST-REPAIR GLASS EVIDENCE RECORDED; NO TELEMETRY CHANGE
- **Real Robot:** USER VERIFIED / TELEOP AND AUTONOMOUS USABILITY; NOT A QUANTITATIVE DRIVETRAIN PASS
- **Transition Guide:** PASS / FINAL RE-FREEZE STATE RECONCILED
- **Final Architecture Review:** PASS / FINAL READ-ONLY CLOSURE REVIEW
- **Documentation:** PASS / FINAL RE-FREEZE METADATA RECONCILED
- **Closure:** PASS / AUTHORIZED COMPLETE AND FROZEN
- **Publication:** HISTORICAL PRE-REPAIR BASELINE @ d58bef0 / CORRECTED REPAIR PUBLICATION PENDING USER PUBLICATION
- **Publication Commit:** HISTORICAL d58bef0d17d202ce1dd0b8645635a8c35095dd3f
- **Publication Subject:** HISTORICAL: Complete V00_L07 vision timestamp and latency contract
- **Git Commit:** HISTORICAL BASELINE PASS / CORRECTED REPAIR PENDING USER COMMIT
- **Git Push:** HISTORICAL BASELINE PASS / CORRECTED REPAIR PENDING USER PUSH
- **Current Repository HEAD:** `a1c3b9f4be0a706812156a8dda36a78c67db22db` (`Reconcile A01_L09 verification and publication metadata`)
- **Current Repository origin/main:** `a1c3b9f4be0a706812156a8dda36a78c67db22db` / CURRENT V00_L07 REPAIR NOT PUBLISHED
- **Known Issues:** BL QUANTITATIVE DRIVETRAIN ANOMALY — KNOWN / DEFERRED
  HARDWARE MAINTENANCE. It is not a quantitative drivetrain PASS, is not
  resolved, and does not block this Vision-curriculum closure sequence under
  the explicit Architect/User disposition. The earlier robot-unavailable state
  remains historical. The original sandbox-local Java compiler diagnostic also
  remains historical and is superseded by the authorized Java 17 evidence.

## Exceptional Reopen Cycle (Completed and Re-Frozen)

V00_L07 was reopened by explicit Architect and User approval for exactly the
three inherited Swerve integrity repairs recorded in
`docs/architecture_decisions/ADR_V00_L07_Inherited_Swerve_Architecture_Robustness_Integrity_Reopen.md`:

1. R1 — remove the command-layer dependency on the IO-owned
   `SwerveModuleIO.StaticFrictionStopReason` type.
2. R2 — make all-module stop fanout best-effort when one module stop throws.
3. R3 — make physical-forward measured drive position and velocity coherent
   for `physicalForwardSign = -1`.

The three repairs were separately authorized within the exact Design Lock and
are now implementation-complete. The original V00_L07 timing lesson and its
`d58bef0` publication remain preserved as historical evidence. The fresh
reopened baseline recorded 593/593 tests PASS; the post-repair full suite
recorded 600/600 tests PASS, with 0 failures, 0 errors, and 0 skipped, and the
clean build passed. The drive ratio remains `6.75:1`.

The Frozen Backbone, Frozen Interface Contract, and V00 roadmap are unchanged.
V00_L08 remains unactivated, non-authoritative, read-only, and protected for a
later fresh reconstruction from a corrected published V00_L07. The independent
Limelight physical-evidence HOLD remains unresolved and outside this reopen.

## Current Reopen Gate State

| Gate | Result | Evidence / meaning |
| --- | --- | --- |
| Audit evidence for R1/R2/R3 | PASS | AUDIT-REV-03B and the approved reopen record identify exactly three inherited defects. |
| Repair-placement audit | PASS | V00_L07 selected as the latest authoritative repair point. |
| Architect selection and User approval | PASS | Exceptional reopen authorized for governance/lifecycle planning. |
| Documentation-only transition | PASS | This metadata update and the exceptional repair guide are authorized. |
| Fresh reopened baseline | PASS | Pre-repair baseline recorded as 593/593 tests PASS. |
| Focused Architecture Audit | PASS | Exact R1/R2/R3 repair boundary confirmed. |
| Design Lock | PASS | Ownership, stop-fanout, and physical-forward semantics locked. |
| Implementation authorization | PASS | Exact two production and five test files authorized. |
| Implementation | PASS | R1/R2/R3 implementation complete within the authorized boundary. |
| Focused/full tests and clean build | PASS | Focused tests, inherited regressions, 600/600 full suite, and clean build PASS. |
| Runtime Simulation | PASS | User-supplied runtime WPILib Simulation PASS. |
| Driver Station / Glass | NOT TESTED | No separate post-repair Glass evidence is recorded; no telemetry change was introduced. |
| Real-robot functional usability | USER VERIFIED | The User later verified Teleop and Autonomous usability. This is not a quantitative drivetrain PASS. |
| BL quantitative drivetrain condition | KNOWN / DEFERRED HARDWARE MAINTENANCE | The unresolved BL quantitative anomaly is retained for future maintenance and is non-blocking for the Vision curriculum under the explicit Architect/User disposition. |
| Post-implementation architecture review | PASS | The read-only R1/R2/R3 and Frozen Backbone reviews passed. |
| Pre-closure documentation reconciliation | PASS | Later User hardware evidence and the deferred BL condition are reconciled without rewriting historical records. |
| Final read-only closure review | PASS | The independent final review returned `READY_FOR_EXPLICIT_L07_REFREEZE_AUTHORIZATION`. |
| Re-freeze | PASS | Explicit Architect/User authorization records `COMPLETE / FROZEN / READ-ONLY`. |
| Repair publication | PENDING USER PUBLICATION | Re-freeze is complete; no corrected publication hash exists yet. |
| Fresh V00_L08 reconstruction | PENDING | Starts only after corrected V00_L07 is published. |

## Post-Repair Verification and Hardware-Evidence State

The exceptional repair is complete from an architecture, implementation, and
automated-verification perspective. This section is the current repaired-cycle
record; the historical original V00_L07 closure records below remain unchanged
provenance for the pre-repair timing lesson.

### Fresh verification evidence

- **Pre-repair reopened baseline:** 593 tests, 0 failures, 0 errors, 0 skipped;
  clean build PASS.
- **Post-repair focused R1/R2/R3 verification:** PASS.
- **Post-repair inherited Swerve regressions:** PASS.
- **Post-repair full suite:** 600 tests, 0 failures, 0 errors, 0 skipped.
- **Post-repair clean build:** PASS.
- **Runtime WPILib Simulation:** PASS.
- **Post-implementation read-only architecture review:** PASS.
- **Frozen Backbone review:** PASS.

### Repair meaning

- **R1:** `StaticFrictionStopReason` is command-facing at the
  `SwerveSubsystem` boundary. The command no longer depends on
  `frc.robot.io`; the subsystem privately translates its reason to the
  unchanged `SwerveModuleIO` reason. Commissioning lifecycle semantics remain
  equivalent.
- **R2:** `SwerveSubsystem.stop()` clears actuation state before attempting
  FL, FR, BL, and BR. It attempts every module after a `RuntimeException`,
  preserves the first exception, suppresses later exceptions in encounter
  order, and rethrows the first after all attempts. CTRE drive/steer
  module-local exception isolation was not part of this repair.
- **R3:** IO and Observation values remain raw sensor-domain measurements.
  `SwerveSubsystem` owns physical-forward normalization, and measured position
  and velocity use the same `physicalForwardSign` semantics. The current robot
  configuration and the authoritative `6.75:1` drive ratio are unchanged;
  CTRE IO and Sim IO production code are unchanged.

### Current hardware evidence and deferred maintenance

At an earlier repair stage the robot was unavailable, so real-robot verification
was truthfully recorded as deferred. The User later verified that the robot
remains usable in Teleop and Autonomous. No separate post-repair Glass evidence
or stronger bounded stop/Disable/no-restart claim is added by this reconciliation
beyond the evidence supplied.

The BL quantitative drivetrain anomaly remains **KNOWN / DEFERRED HARDWARE
MAINTENANCE**. This record does not claim BL PASS, quantitative drivetrain PASS,
matched module measurements, completed tuning, completed calibration, or issue
resolution. Under the explicit Architect/User disposition, that separate
maintenance condition does not block the Vision curriculum closure sequence.
R1/R2/R3 automated evidence, the clean build, Simulation, and the
post-implementation architecture/Frozen Backbone reviews remain PASS.

No tuning, recalibration, autonomous redesign, vision validation, Limelight
validation, or PathPlanner validation is included in this repair.

The final read-only closure review passed and explicit Architect/User re-freeze
authorization was granted. The lesson is therefore `COMPLETE / FROZEN /
READ-ONLY`. Corrected repair publication remains User-owned and pending; no
corrected publication hash is claimed.

## Historical Original V00_L07 Lifecycle Gates (Preserved)

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
| Lesson-local publication metadata reconciliation | PASS | User publication is recorded at `d58bef0`; commit and push passed, and HEAD matches origin/main. |
| Repository-level lifecycle reconciliation | PENDING | AGENTS.md and root README.md require a separate authorized reconciliation task. |

## Historical Original Automated Verification Commands (Preserved)

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

## Historical Original Timing Design Lock (Preserved)

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

## Historical Original Deferred Scope (Preserved)

- V00_L08 owns real camera adapter integration, vendor fields and conversion,
  synchronization, network transport, physical-camera integration, and camera
  verification.
- V00_L09 owns `SwerveDrivePoseEstimator.addVisionMeasurement(...)`, accepted
  vision fusion, estimator wiring, covariance/stddev selection, and runtime
  estimator correction.
- Limelight, PhotonVision, Swerve, RobotContainer, commands, scheduler,
  NetworkTables, telemetry, PathPlanner, alliance transforms, and hardware
  verification are outside this activation.

## Historical Original Final Closure Result (Preserved)

~~~text
V00_L07: COMPLETE / FROZEN / READ-ONLY
IMPLEMENTATION AUTHORIZATION: AUTHORIZED BY ARCHITECT
IMPLEMENTATION: COMPLETE / AUTHORIZED BOUNDARY
AUTOMATED VERIFICATION: PASS
BUILD: PASS / CLEAN BUILD
FINAL ARCHITECTURE REVIEW: PASS
FINAL CLOSURE / FREEZE: PASS
PUBLICATION: PUBLISHED @ d58bef0 / USER VERIFIED
PUBLICATION COMMIT: d58bef0d17d202ce1dd0b8645635a8c35095dd3f
PUBLICATION SUBJECT: Complete V00_L07 vision timestamp and latency contract
PUSH: PASS / origin/main / USER VERIFIED
HEAD == origin/main: PASS
~~~

The historical activation and pre-publication closure states are preserved in
the preceding records. The `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @
d58bef0 / USER VERIFIED` statement applies only to the historical pre-repair
snapshot. The repaired lesson passed final read-only closure review, received
explicit Architect/User re-freeze authorization, and is now `COMPLETE / FROZEN
/ READ-ONLY`. Its corrected repair publication remains pending User commit and
push.
