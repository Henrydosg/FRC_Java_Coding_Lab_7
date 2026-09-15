# V00_L09 - Swerve Pose Estimator Vision Fusion

## Current lesson state

- **Lesson identity:** `V00_L09_SwervePoseEstimatorVisionFusion`
- **Status:** `COMPLETE`
- **Active state:** `COMPLETE / FROZEN / READ-ONLY`
- **Freeze state:** `FROZEN / READ-ONLY`
- **Active lesson count:** `0`
- **Predecessor:** `V00_L08 - Real Vision Adapter Integration @ f34b210`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`
- **Predecessor publication metadata history:** `6415b17 / USER-REPORTED HISTORY`
- **Design Lock:** `PASS_V00_L09_FINAL_DESIGN_LOCK`
- **Implementation:** `PASS / COMPLETED`
- **Automated verification:** `PASS / FOCUSED, INHERITED, DIRECT A-G, FULL SUITE, AND CLEAN BUILD`
- **Simulation:** `PASS / GATE 1, ESTIMATOR INITIALIZATION, AND GATE 2 COMPLETE`
- **Driver Station / Glass:** `PASS`
- **Real Limelight L09 timing/result:** `PASS / USER-VERIFIED DEPLOYED FLAT-ROOT JSON, TIMING, AND ACQUISITION`
- **Real estimator vision fusion:** `PASS / USER-VERIFIED QUALIFIED/ACCEPTED FUSION, LOSS, RECOVERY, AND CONTROLLED MOTION`
- **Bounded autonomous Gate 9:** `PASS / BLUE ONE_METER_PATH PREPARATION AND COMPLETION`
- **Final architecture review:** `PASS / READY FOR DOCUMENTATION CLOSURE`
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

V00_L08 remains frozen and unmodified. This lesson was the sole active V00
lesson during implementation. Implementation, automated, Simulation/Glass,
approved real-hardware, architecture-review, documentation-reconciliation,
final documentation review, and freeze authorization are complete. L09 is now
`COMPLETE / FROZEN / READ-ONLY`; implementation publication `6548c98` is
`PUBLISHED / VERIFIED`. Only the distinct metadata-reconciliation commit
remains User-owned and pending.

## Publication metadata

The User supplied and verified the final implementation publication record:

- Git commit: `6548c98`;
- Commit message: `Complete V00_L09 Swerve pose estimator vision fusion`;
- Git push: `COMPLETE / VERIFIED`;
- Remote: `origin/main = 6548c98` and `origin/HEAD = 6548c98`;
- Publication: `PUBLISHED / VERIFIED`;
- Final publication gate: `PASS_V00_L09_REMOTE_PUBLICATION_VERIFIED`.

The metadata-reconciliation commit is distinct from the implementation
publication commit and remains `PENDING USER COMMIT`.

## Objective

Fuse one qualified timestamped AprilTag measurement through a guarded,
Swerve-owned estimator boundary:

```text
Vision qualified measurement
    -> post-scheduler VisionFusionCoordinator requirement
    -> Swerve guarded admission
    -> SwerveDrivePoseEstimator.addVisionMeasurement(...)
```

This is measurement fusion, not continuous pose reset. `SwerveSubsystem`
remains the sole owner and mutator of estimator state. Vision remains
vendor-neutral above IO, and `RobotContainer` remains the composition root.

## Locked boundaries

- Preserve the Frozen Backbone and Frozen Interface Contract.
- Preserve the inherited V00_L08 real-adapter and observation boundaries.
- Admit only qualified timestamped measurements according to the approved L09
  Design Lock.
- Keep orchestration in the post-scheduler `VisionFusionCoordinator` boundary.
- Do not migrate to MegaTag.
- Do not add dynamic quality covariance.
- Do not tune Constants.
- Do not add a second primary concept.

## Preparation, implementation, and verification evidence

The User/Architect-supplied preparation record includes candidate copying from
frozen V00_L08, generated-artifact cleanup, inherited baseline clean-build
PASS, roadmap identity reconciliation, architecture audit PASS, timing-source
investigation PASS, runtime orchestration micro-audit PASS, coordinator
decision acceptance, and the Architect gate
`PASS_V00_L09_FINAL_DESIGN_LOCK`.

Implementation subsequently completed under the Architect-controlled workflow.
The current source preserves the locked coordinator, guarded Swerve admission,
and estimator ownership boundaries. User verification passed focused tests,
  inherited regression, `compileTestJava`, the full 637/637 suite, and a clean
build. The later direct A-G evidence repair was explicitly authorized by
`PASS_V00_L09_NARROW_TEST_EVIDENCE_AUTHORIZED`; it changed test fixtures only,
reusing deterministic quality-valid `VisionIOSimHarness` Frame A/B inputs.

Simulation Gate 1, estimator initialization, Simulation Gate 2 Frame A/B,
one-shot behavior, loss, and reacquisition passed. Driver Station / Glass
verification also passed. These results are accepted under
`PASS_V00_L09_AUTOMATED_EVIDENCE_CLOSURE_VERIFIED` and the previously supplied
runtime gates.

Real-hardware verification passed for the deployed flat-root `/limelight/json`
parser path, Available/Connected/SampleValid state, AprilTag 32 acquisition,
coherent timing/result handling, estimator initialization, stationary accepted
fusion, geometry consistency, controlled translation/rotation, target loss and
reacquisition, and camera disconnect/recovery.

Evidence classification:

- `THEORY VERIFIED`: governance, Design Lock, architecture, Frozen Backbone,
  and final architecture review.
- `SIMULATION VERIFIED`: startup, estimator initialization, one-shot fusion,
  loss, reacquisition, and Glass/runtime simulation evidence.
- `REAL HARDWARE VERIFIED`: deployed acquisition/parser, real timing/fusion,
  controlled motion, fail-closed recovery, disconnect/recovery, and bounded
  autonomous lifecycle.

For Gate 9, vision was intentionally invalid/suppressed during the BLUE
`ONE_METER_PATH` run because the physical Tag 32 placement was not asserted to
match the official field coordinate. Gate 9 proves bounded autonomous lifecycle
and estimator compatibility; it does not claim exact 1.000 m endpoint accuracy
or physical absolute-pose calibration accuracy.

One historical Driver Station overrun warning was observed. Independent
read-only review found no proven structural runtime defect. If the warning
recurs, capture WPILib/Driver Station timing epochs before any performance
repair; no specific cause is claimed.

## Remaining User-owned publication action

Only the metadata-reconciliation commit remains User-owned and
`PENDING USER COMMIT`.
