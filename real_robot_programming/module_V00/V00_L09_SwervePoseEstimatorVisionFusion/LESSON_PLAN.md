# V00_L09 Lesson Plan and Lifecycle Record

## Current state

- **Lesson:** `V00_L09 - Swerve Pose Estimator Vision Fusion`
- **Directory:** `V00_L09_SwervePoseEstimatorVisionFusion`
- **Predecessor:** `V00_L08_RealVisionAdapterIntegration @ f34b210`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`
- **Predecessor publication metadata history:** `6415b17 / USER-REPORTED HISTORY`
- **Status:** `COMPLETE`
- **Active state:** `COMPLETE / FROZEN / READ-ONLY`
- **Freeze state:** `FROZEN / READ-ONLY`
- **Active lesson count:** `0`
- **Design Lock:** `PASS_V00_L09_FINAL_DESIGN_LOCK`
- **Implementation:** `PASS / COMPLETED UNDER ARCHITECT-CONTROLLED WORKFLOW`
- **Automated verification:** `PASS / FOCUSED, INHERITED, DIRECT A-G, FULL SUITE, AND CLEAN BUILD`
- **Simulation:** `PASS / GATE 1, ESTIMATOR INITIALIZATION, AND GATE 2 COMPLETE`
- **Driver Station / Glass:** `PASS`
- **Real hardware:** `PASS / DEPLOYED LIMELIGHT, REAL FUSION, RECOVERY, AND BOUNDED AUTONOMOUS GATES VERIFIED`
- **Final architecture review:** `PASS / PASS_V00_L09_FINAL_ARCHITECTURE_REVIEW_READY_FOR_DOCUMENTATION_CLOSURE`
- **Final documentation review:** `PASS / PASS_V00_L09_FINAL_DOCUMENTATION_REVIEW_READY_FOR_FREEZE_AUTHORIZATION`
- **Final freeze authorization:** `PASS / PASS_V00_L09_FINAL_FREEZE_AUTHORIZATION`
- **Documentation:** `PASS / FINAL DOCUMENTATION REVIEW AND LIFECYCLE RECORDING COMPLETE`
- **Git commit:** `6548c98`
- **Commit message:** `Complete V00_L09 Swerve pose estimator vision fusion`
- **Git push:** `COMPLETE / VERIFIED`
- **Remote:** `origin/main = 6548c98`
- **Publication:** `PUBLISHED / VERIFIED`
- **Final publication gate:** `PASS_V00_L09_REMOTE_PUBLICATION_VERIFIED`
- **Metadata reconciliation commit:** `PENDING USER COMMIT`

## One-concept objective

Teach guarded admission of a qualified timestamped AprilTag measurement into
the estimator owned by `SwerveSubsystem`:

```text
qualified timestamped AprilTag measurement
    -> post-scheduler VisionFusionCoordinator requirement
    -> guarded Swerve-owned admission
    -> SwerveDrivePoseEstimator.addVisionMeasurement(...)
```

This is measurement fusion, not continuous pose reset. Swerve remains the
sole owner and mutator of estimator state. Vision remains vendor-neutral above
IO, and RobotContainer remains the composition root.

## Preserved boundaries

- V00_L08 remains `COMPLETE / FROZEN / READ-ONLY`.
- V00_L01-L07 remain frozen and protected.
- Vision does not expose vendor APIs above the approved IO boundary.
- `SwerveSubsystem` owns estimator state and guarded admission.
- The post-scheduler `VisionFusionCoordinator` requirement is preserved.
- No MegaTag migration is included.
- No dynamic quality covariance is included.
- No Constants tuning is included.
- No second primary concept is included.

## Completed preparation and activation phases

1. The User preserved V00_L08 as the frozen predecessor.
2. The User copied the candidate from frozen V00_L08.
3. The candidate was renamed to the ADR-locked
   `V00_L09_SwervePoseEstimatorVisionFusion` identity.
4. Copied generated artifacts were removed during preparation.
5. The User supplied inherited baseline clean-build PASS.
6. Roadmap identity reconciliation passed.
7. The pre-implementation architecture audit passed.
8. The real timing-source investigation passed.
9. The Architect-approved Design Lock passed as
   `PASS_V00_L09_FINAL_DESIGN_LOCK`.
10. The runtime orchestration micro-audit passed.
11. The coordinator requirement decision was accepted.
12. This documentation-only controlled activation made L09 the sole
    `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`.

These preparation and design results remain historical prerequisites. Later
implementation and verification evidence is reconciled below without changing
the approved concept or lifecycle identity.

## Completed implementation and verification

- The locked L09 production boundary is implemented: post-scheduler
  coordination, guarded Swerve-owned admission, and
  `SwerveDrivePoseEstimator.addVisionMeasurement(...)`.
- User-verified focused tests, inherited regression, `compileTestJava`, the
  full 637/637 suite, and the clean build are PASS. The preserved generated
  report records 637 tests, zero failures, zero errors, and zero skipped.
- Direct automated evidence A-G is PASS for stale, future, out-of-order,
  reset-barrier, scheduler-suppression, coordinator-failure, and
  telemetry-finally behavior.
- The failure-boundary evidence uses the existing deterministic,
  quality-valid `VisionIOSimHarness` Frame A/B fixtures after an explicitly
  authorized test-only repair. No production policy or behavior changed.
- Simulation Gate 1, estimator initialization, Simulation Gate 2 Frame A/B,
  one-shot behavior, loss, and reacquisition are PASS.
- Driver Station / Glass verification is PASS.

- Deployed V00_L09 Limelight verification is PASS: the flat-root
  `/limelight/json` schema repair was verified on the real robot, with
  Available/Connected/SampleValid and AprilTag 32 acquisition observed.
- Real fusion verification is PASS for estimator initialization, stationary
  qualified and accepted fusion, positive accepted-fusion count, geometry
  consistency, controlled translation/rotation, target loss/reacquisition,
  and camera disconnect/recovery.
- Bounded autonomous Gate 9 is PASS for BLUE `ONE_METER_PATH` preparation and
  completion. Vision was intentionally suppressed because the physical Tag 32
  placement was not asserted to match the official field coordinate. This gate
  does not claim exact 1.000 m endpoint accuracy or physical absolute-pose
  calibration accuracy.

Evidence classification is explicit:

- `THEORY VERIFIED`: governance, Design Lock, architecture, Frozen Backbone,
  and final architecture review.
- `SIMULATION VERIFIED`: startup, estimator initialization, one-shot fusion,
  loss, reacquisition, and Glass/runtime simulation evidence.
- `REAL HARDWARE VERIFIED`: deployed Limelight acquisition/parser, real timing
  and fusion, controlled motion, fail-closed loss/recovery, disconnect/recovery,
  and bounded autonomous lifecycle.

The authorization history is recorded conservatively: controlled activation
and Design Lock occurred; implementation subsequently occurred under the
Architect-controlled workflow; and the later narrow test-evidence repair was
explicitly authorized by `PASS_V00_L09_NARROW_TEST_EVIDENCE_AUTHORIZED`.

## Publication metadata reconciliation

The User supplied and verified the implementation publication record:

- Git commit: `6548c98`;
- Commit message: `Complete V00_L09 Swerve pose estimator vision fusion`;
- Git push: `COMPLETE / VERIFIED`;
- Remote: `origin/main = 6548c98` and `origin/HEAD = 6548c98`;
- Publication: `PUBLISHED / VERIFIED`;
- Final publication gate: `PASS_V00_L09_REMOTE_PUBLICATION_VERIFIED`.

The metadata-reconciliation commit is distinct from the implementation
publication commit and remains User-owned as `PENDING USER COMMIT`.

One historical Driver Station overrun warning was observed. Independent
read-only review found no proven structural runtime defect, busy-wait,
`Thread.sleep` production path, or unbounded retry. PathPlanner preparation and
full Limelight JSON parsing remain plausible but unproven timing contributors.
If the warning recurs, capture WPILib/Driver Station timing epochs before any
performance repair.

L09 is now `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`. Its
implementation publication is `6548c98` and is `PUBLISHED / VERIFIED`; only the
distinct metadata-reconciliation commit remains User-owned and pending.
