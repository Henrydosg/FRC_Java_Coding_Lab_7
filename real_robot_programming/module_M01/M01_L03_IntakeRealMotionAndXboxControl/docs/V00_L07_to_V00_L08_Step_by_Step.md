# V00_L07 to V00_L08 - Step-by-Step Transition Guide

## Guide state

- **Current lesson:** V00_L08 - Real Vision Adapter Integration
- **Previous lesson:** V00_L07 - Vision Timestamp and Latency Contract @ `4704cfc`
- **Guide status:** PASS / FINAL
- **Lesson status:** COMPLETE / FROZEN / READ-ONLY
- **Finalization rule:** User-owned automated, Simulation, Driver Station/Glass,
  and real-robot gates are complete. The final read-only architecture review
  and explicit closure approval are recorded, so this guide is final.

## Step 1 - Preserve the candidate and reconcile the inherited repair

**Objective**  Preserve the existing V00_L08 candidate and confirm its corrected
V00_L07 inheritance before editing.

**Why**  The historical preservation/reconciliation checkpoint required an
exact seven-file forward-port from the corrected V00_L07 donor. That donor
comparison is provenance evidence, not a new current repair boundary.

**Action**  Compare the candidate read-only with the external checkpoint at
`C:\Users\xps7350i7\Desktop\FRC_L08_Preservation_Checkpoint_2026-09-07_VERIFIED`
and the corrected V00_L07 donor, excluding generated output.

**Files Changed**  None.

**Verification** 242 comparable files were present; exactly the seven
ADR-authorized inherited repair files differed from the checkpoint, and those
seven matched the corrected donor. This is historical pre-repair
reconciliation evidence.

**Expected Result** The candidate is preserved and provenance is PASS before
the new adapter boundary is edited.

## Step 2 - Lock the H1 commissioning convention

**Objective** Record the authorized provisional rotation convention and keep the
existing extrinsic unchanged.

**Why**  The real adapter needs one deterministic interpretation while physical
camera uncertainty remains visible.

**Action** Lock `R = Rx(-a3) * Ry(+a4) * Rz(+a5)` with column-vector matrix
semantics, where angles are the final three Limelight camera-space values in
degrees. Preserve `Translation3d(-0.038, +0.050, +0.114)` metres and
`Rotation3d(0, -20 degrees, 0)`.

**Files Changed** `src/main/java/frc/robot/Constants.java` and lesson-local
documentation.

**Verification** Focused tests include independent matrix multiplication, each
single-axis sign, a noncommutative fixture, and a known tuple.

**Expected Result** The rotation status is explicitly **PROVISIONAL
COMMISSIONING LOCK**, not final proof.

## Step 3 - Implement the real NetworkTables adapter

**Objective** Decode the Limelight schema into the frozen VisionIO contract.

**Why** IO owns hardware/schema access; higher layers must remain vendor-neutral.

**Action** Read `tv`, `tid`, `hb`, and `targetpose_cameraspace`; require a
finite nonnegative heartbeat and `tv == 1` target evidence; reject
malformed/nonfinite/unacceptable values; map the translation axes; construct
`cameraToTarget`; never invert the transform. Treat repeated unchanged
heartbeats as stale immediately and fail closed after two repeated cycles.
After reset/reconnect, require fresh target-pose evidence beyond both the prior
target-refresh boundary and the prior heartbeat-change boundary. Stable current
`tv` and `tid` values remain required structural fields; a changed tag id or
visibility value alone is insufficient, and target changes observed while the
heartbeat is stalled are not recovery evidence.
Read the target tuple twice, require the snapshots and their change metadata to
agree, and recheck heartbeat around the reads. This is a bounded read-stability
and coherence guard, not an atomic Limelight frame guarantee: NetworkTables
does not provide an atomic multi-topic transaction.

**Files Changed** `src/main/java/frc/robot/io/vision/VisionIOLimelight.java`.

**Verification** User evidence confirms focused post-repair tests 37/37 PASS,
including the five historical freshness/coherence regressions. The earlier
642-test result of 637 passed and five failures is preserved as historical
pre-repair evidence.

**Expected Result** Every update fully overwrites the transport snapshot and
fails closed without adding vendor types to public contracts.

## Step 4 - Select real or deterministic simulation IO

**Objective** Keep the composition root responsible for implementation choice.

**Why** Simulation must remain deterministic and independent from the real
camera transport.

**Action** Select `VisionIOLimelight` on real hardware and the existing
`VisionIOSim` with the preserved field/extrinsic contract in simulation. Inject
the selected IO into the periodic `VisionSubsystem`; publish only the
immutable observation through the read-only vision telemetry facade.

**Files Changed**

- `src/main/java/frc/robot/RobotContainer.java`
- `src/main/java/frc/robot/subsystems/VisionSubsystem.java`
- `src/main/java/frc/robot/telemetry/RobotTelemetry.java`
- `src/main/java/frc/robot/telemetry/vision/VisionTelemetryFacade.java`
- `src/test/java/frc/robot/subsystems/VisionSubsystemTest.java`
- `src/test/java/frc/robot/telemetry/vision/VisionTelemetryFacadeTest.java`

**Verification** Focused coverage confirms the real/simulation selection
boundary; `VisionSubsystemTest` confirms periodic `updateInputs` invocation
and observation mapping. The same path is used for real and sim, and
simulation has no Limelight topic dependency.

**Expected Result** No simulation path depends on Limelight NetworkTables.

## Step 5 - Preserve deferred boundaries

**Objective** Keep V00_L08 limited to adapter integration.

**Why** V00_L09 owns accepted vision fusion and estimator wiring.

**Action** Do not add `SwerveDrivePoseEstimator.addVisionMeasurement(...)`,
pose reset, telemetry control, autonomous camera access, Swerve tuning, vendor
dependencies, or configuration changes.

**Files Changed** None beyond the authorized L08 implementation boundary.

**Verification** Source review confirms no fusion path, estimator call, pose
reset, autonomous camera access, control behavior, or vendor type leak was
added. User evidence confirms the compile gates, full 642/642 regression, and
clean build PASS.

**Expected Result** Frozen Backbone and Observation ownership remain intact.

## Step 6 - Run automated verification

**Objective** Verify the adapter and inherited lesson behavior from a clean
build state.

**Why** Automated evidence must precede runtime commissioning.

**Action** Run focused tests, inherited vision regressions, the full test suite,
and `clean build` with the WPILib Java 17 toolchain.

**Files Changed** Generated build output only.

**Verification** User evidence confirms focused 37/37 PASS, full 642/642 PASS,
and clean-build PASS under Java 17. The 637/5 result is historical pre-repair
evidence.

**Expected Result** The repaired source, focused behavior, inherited behavior,
and clean-build gates are verified.

## Step 7 - Complete user-owned runtime validation

**Objective** Validate the physical camera and provisional H1 lock.

**Why** Desktop tests cannot prove camera mounting, vendor output semantics,
network availability, or physical rotation interpretation.

**Action** Run WPILib Simulation, inspect Driver Station/Glass/NT4 as applicable,
then connect the real Limelight and validate runtime observation telemetry,
heartbeat progression/staleness/recovery, target evidence, translation signs,
`cameraToTarget` direction, preserved extrinsic, and the provisional H1 lock.

**Files Changed** None unless a separately authorized correction is required.

**Verification** User evidence records Simulation startup/Teleop, Java Glass
telemetry, and real Limelight acquisition, target loss, and reacquisition.
Simulation shows the inherited no-target `VisionIOSim` default; it does not
claim the real Limelight adapter was simulated. Real target loss is observed as
`INVALID_SAMPLE` while connection remains available, and reacquisition returns
`TARGETS_PRESENT` for AprilTag 32.

**Expected Result** The runtime path is accepted for closure review. H1 remains
provisional and is not promoted to official or proven vendor semantics.

## Step 8 - Closure only after runtime evidence

**Objective** Finish V00_L08 lifecycle gates without overstating verification.

**Why** The lesson cannot be frozen on automated tests alone.

**Action** Finalize this guide, complete the final architecture review, update
status, and let the User perform commit/push only after all gates pass.

**Files Changed** Lesson-local documentation and status only, after evidence.

**Verification** Documentation records the supplied automated, Simulation,
Driver Station/Glass, and real-robot PASS evidence. The final read-only
architecture and closure review returned
`PASS_V00_L08_FINAL_CLOSURE_REVIEW`, and the guide was finalized.

**Expected Result** V00_L08 is `COMPLETE / FROZEN / READ-ONLY`. The User-owned
publication is recorded at `f34b210` with message `Complete V00_L08 real vision
adapter integration`; push PASS to `origin/main` was confirmed. Codex did not
run Git.
