# V00_L07 Lesson Checklist - Vision Timestamp and Latency Contract

Status: COMPLETE / FROZEN / READ-ONLY
Freeze state: FROZEN
Predecessor: V00_L06 @ 1327bf4 - COMPLETE / FROZEN / READ-ONLY / PUBLISHED
Predecessor metadata reconciliation: 49c4286
Design Lock: PASS / R1/R2/R3 REPAIR DESIGN LOCK
Implementation authorization: PASS / EXACT AUTHORIZED REPAIR BOUNDARY
Implementation: PASS / R1/R2/R3 COMPLETE
Verification: PASS / FOCUSED, INHERITED, AND 600/600 FULL SUITE
Active lesson count: 0
Post-Implementation Architecture Review: PASS
Final Read-Only Closure Review: PASS
Final Closure / Freeze: PASS / AUTHORIZED COMPLETE AND FROZEN
Publication: HISTORICAL PRE-REPAIR BASELINE @ d58bef0 / CORRECTED REPAIR PUBLISHED @ 4704cfc / USER VERIFIED
Historical publication commit: d58bef0d17d202ce1dd0b8645635a8c35095dd3f
Historical publication subject: HISTORICAL: Complete V00_L07 vision timestamp and latency contract
Corrected publication commit: 4704cfc0801910e30c8abb7cffcc467e4f4df016
Corrected publication subject: Complete corrected V00_L07 Swerve integrity repair
Controlled staging: PASS / unexpected staged 0
Unexpected committed: 0
Commit: HISTORICAL BASELINE PASS / CORRECTED REPAIR PASS / USER VERIFIED
Push: HISTORICAL BASELINE PASS / CORRECTED REPAIR PASS / origin/main / USER VERIFIED
HEAD == origin/main at corrected repair publication: PASS / 4704cfc0801910e30c8abb7cffcc467e4f4df016 / USER VERIFIED

## Current Exceptional Reopen Checklist

- [x] AUDIT-REV-03B evidence identifies R1, R2, and R3.
- [x] Repair-placement governance audit selects V00_L07.
- [x] Architect approves V00_L07 as the exceptional repair location.
- [x] User approves the exact R1/R2/R3 governance lifecycle.
- [x] Governance proposal and reopen ADR are PASS / recorded.
- [x] Documentation-only lifecycle transition is authorized.
- [x] Fresh reopened inherited baseline: 593/593 tests PASS; clean build PASS.
- [x] Focused post-baseline Architecture Audit.
- [x] Exact R1/R2/R3 Design Lock.
- [x] Separate implementation authorization.
- [x] Production implementation within the exact authorized boundary.
- [x] Test implementation or focused test additions within the exact authorized boundary.
- [x] Focused regression tests.
- [x] Full inherited test suite: 600/600 tests PASS.
- [x] Clean build.
- [x] Simulation verification.
- [ ] Driver Station / Glass verification: no separate post-repair Glass evidence; no telemetry change.
- [x] Later User hardware evidence reconciled: Teleop and Autonomous usability USER VERIFIED.
- [x] BL quantitative anomaly retained as KNOWN / DEFERRED HARDWARE MAINTENANCE; no quantitative drivetrain PASS claimed.
- [x] Post-implementation architecture and Frozen Backbone review.
- [x] Final read-only closure review.
- [x] Explicit re-freeze approval.
- [x] Corrected repair publication by User: `4704cfc0801910e30c8abb7cffcc467e4f4df016` / USER VERIFIED.
- [ ] Authorized one-time preservation-based reconciliation of the existing V00_L08 candidate.

The remaining unchecked lifecycle work is the separately governed one-time
preservation-based reconciliation of the existing V00_L08 candidate. The
historical fresh-reconstruction item is SUPERSEDED PROSPECTIVELY for this exact
candidate and is not marked complete. Driver Station / Glass has no separate
post-repair evidence and is not promoted to PASS. The later User functional
hardware evidence is accepted at exactly the recorded Teleop/Autonomous
usability level. The BL quantitative anomaly is separate deferred maintenance
and is not a quantitative drivetrain PASS. No historical evidence is erased.
Driver Station / Glass has no separate post-repair evidence and is not promoted to PASS. The
later User functional hardware evidence is accepted at exactly the recorded
Teleop/Autonomous usability level. The BL quantitative anomaly is separate
deferred maintenance and is not a quantitative drivetrain PASS. No historical
PASS below is reused as fresh evidence for the reopened repair.

## Current Repair Evidence and Final Closure Gate

The authorized repair is complete from the architecture, implementation, and
automated-verification perspectives. R1 moves command-facing
`StaticFrictionStopReason` ownership to `SwerveSubsystem`, privately
translating to the unchanged `SwerveModuleIO` reason. R2 makes
`SwerveSubsystem.stop()` clear actuation state, attempt FL → FR → BL → BR,
preserve the first `RuntimeException`, suppress later exceptions in encounter
order, and rethrow the first after all attempts. CTRE drive/steer module-local
exception isolation was not part of the repair. R3 keeps raw IO/Observation
values raw while applying the same physical-forward sign to measured position
and velocity in `SwerveSubsystem`; the current configuration and `6.75:1`
drive ratio remain unchanged, and CTRE/Sim production IO remain unchanged.

The post-repair evidence is: focused R1/R2/R3 PASS, inherited Swerve
regressions PASS, full suite 600 tests with 0 failures/0 errors/0 skipped,
clean build PASS, runtime WPILib Simulation PASS, final read-only architecture
review PASS, and Frozen Backbone review PASS.

At an earlier repair stage the robot was unavailable, so real-robot
verification was recorded as deferred. Later User evidence verifies Teleop and
Autonomous usability. No stronger bounded stop/Disable/no-unintended-restart
claim is added beyond the supplied evidence.

The BL quantitative drivetrain anomaly remains **KNOWN / DEFERRED HARDWARE
MAINTENANCE**. This checklist does not claim BL PASS, quantitative drivetrain
PASS, matched module measurements, completed tuning, completed calibration, or
issue resolution. Under the explicit Architect/User disposition, that separate
maintenance condition does not block the Vision curriculum closure sequence.
No tuning, recalibration, autonomous redesign, vision, Limelight, or
PathPlanner validation belongs to this repair.

## Historical Original V00_L07 Closure Checklist (Preserved)

The checked items below document the original Vision Timestamp and Latency
Contract lesson and its historical closure at `d58bef0`; they did not
independently close the later exceptional repair cycle.

## Governance and predecessor

- [x] Repository governance and authoritative English Documents A/B/C read.
- [x] Applicable V00 roadmap ADR reviewed.
- [x] V00_L06 confirmed complete, frozen, read-only, and published at 1327bf4.
- [x] V00_L06 lesson-local publication reconciliation recorded at 49c4286.
- [x] V00_L01-L06 remain protected frozen predecessors.
- [x] V00_L08 and V00_L09 responsibilities remain deferred.
- [x] No V00 lesson other than V00_L07 is active.

## Preparation and inheritance

- [x] User prepared the ADR-locked V00_L07 identity.
- [x] Candidate was inherited from final V00_L06.
- [x] Copied generated output was cleaned before baseline verification.
- [x] User-supplied inherited WPILib Java 17 baseline build passed.
- [x] Baseline evidence recorded: BUILD SUCCESSFUL in 55s, exit code 0.
- [x] 236 comparable non-generated files matched.
- [x] Zero inheritance differences confirmed.
- [x] 77 production Java files matched the predecessor.
- [x] 63 test Java files matched the predecessor.
- [x] Gradle and wrapper configuration remain inherited unchanged.
- [x] Vendordeps remain inherited unchanged.
- [x] Deploy, resources, and PathPlanner assets remain inherited unchanged.
- [x] V00_L07 timing implementation remains limited to the authorized
      vendor-neutral contract.
- [x] No Limelight or PhotonVision integration exists.
- [x] No estimator fusion or runtime wiring exists.

## Design Lock and activation

- [x] One-concept objective recorded: measurement timestamp and latency.
- [x] Capture/measurement time distinguished from receive time.
- [x] Total capture-to-receive latency recorded.
- [x] Seconds established as the temporal unit.
- [x] Canonical relationship recorded:
      measurementTimestampSeconds = receiveTimestampSeconds - totalLatencySeconds.
- [x] Finite timestamp and nonnegative latency semantics recorded.
- [x] Zero-latency case recorded as valid.
- [x] Explicit reference timestamp and freshness policy recorded.
- [x] Duplicate and out-of-order semantics recorded.
- [x] Programming-contract error boundary recorded.
- [x] No hidden global-clock access permitted.
- [x] Architect Design Lock confirmed.
- [x] V00_L07 activated as IN_PROGRESS / DESIGN LOCKED / EDITABLE.
- [x] Architect authorized the exact implementation boundary.

## Historical Original Implementation

- [x] Receive separate exact implementation authorization.
- [x] Create only the authorized vendor-neutral timing production types.
- [x] Create only the authorized focused timing tests.
- [x] Preserve immutable observations and pure evaluation.
- [x] Do not add vendor-specific timing fields or APIs.
- [x] Do not modify Swerve, RobotContainer, telemetry, NetworkTables, or
      autonomous behavior.

## Historical Original Required Focused Coverage

- [x] Zero latency.
- [x] Positive finite latency.
- [x] Negative latency rejection.
- [x] NaN and positive/negative infinity rejection.
- [x] Measurement timestamp equal to receive timestamp.
- [x] Measurement timestamp before receive timestamp.
- [x] Measurement timestamp later than receive timestamp rejection through the
      finite/nonnegative source contract.
- [x] Fresh and stale classification against an explicit reference timestamp.
- [x] Newer/ordered, duplicate, and out-of-order timestamps.
- [x] Deterministic repeated evaluation.
- [x] Seconds-unit consistency.
- [x] No hidden clock, hardware, or vendor dependency.

## Historical Original Verification and Closure

- [x] Production compile after implementation authorization.
- [x] Test compile after implementation authorization.
- [x] Focused V00_L07 tests.
- [x] Required inherited regression tests.
- [x] Full test suite: 593 tests, 0 failures, 0 errors, 0 skipped.
- [x] Clean build.
- [x] Documentation reconciliation after implementation.
- [x] Final architecture review.
- [x] Final closure authorization.
- [x] Freeze metadata update.
- [x] User-owned Git commit and push: PASS / exit 0.
- [x] Publication commit recorded: `d58bef0`.
- [ ] Separate repository-level lifecycle metadata reconciliation for
      `AGENTS.md` and the root `README.md`.

## Historical Original Protected and Deferred Scope

V00_L08 owns real camera integration, Limelight/PhotonVision compatibility,
vendor timestamp/latency fields, vendor timebase conversion, synchronization,
transport, physical camera integration, and camera verification.

V00_L09 owns `SwerveDrivePoseEstimator.addVisionMeasurement(...)`, accepted
vision fusion, estimator wiring, covariance/stddev selection, and runtime
estimator correction.

No HALSIM, Glass, Driver Station, real robot, or physical Limelight gate is
required for this pure deterministic contract lesson.

## Historical Original Final Result

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
PUSH: PASS / origin/main / USER VERIFIED
HEAD == origin/main: PASS
~~~

## Current Exceptional Repair Result

~~~text
V00_L07: COMPLETE / FROZEN / READ-ONLY
REPAIR SCOPE: R1 / R2 / R3 ONLY
IMPLEMENTATION AUTHORIZATION: PASS / EXACT AUTHORIZED BOUNDARY
IMPLEMENTATION: COMPLETE / R1/R2/R3
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
V00_L08: NOT STARTED / NOT ACTIVATED / NOT IMPLEMENTED / PROTECTED
~~~

The original completion checklist remains preserved as historical provenance.
The repaired lesson passed final read-only closure review, received explicit
Architect/User re-freeze authorization, and is now `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED @ 4704cfc`. Corrected repair publication is User-verified
at `4704cfc0801910e30c8abb7cffcc467e4f4df016`.
