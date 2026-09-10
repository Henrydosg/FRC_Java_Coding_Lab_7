# V00_L08 Lesson Plan - Real Vision Adapter Integration

## Current state

- **Lesson:** V00_L08 - Real Vision Adapter Integration
- **Directory:** `V00_L08_RealVisionAdapterIntegration`
- **Predecessor:** V00_L07 - Vision Timestamp and Latency Contract @ `4704cfc`
- **Status:** COMPLETE / FROZEN / READ-ONLY
- **Freeze state:** FROZEN
- **Implementation result:** COMPLETE / VERIFIED / DOCUMENTATION COMPLETE /
  FINAL ARCHITECTURE AND CLOSURE REVIEW PASS
- **Git publication:** `PUBLISHED @ f34b210 / USER VERIFIED`; message
  `Complete V00_L08 real vision adapter integration`; push PASS to
  `origin/main`. Codex did not run Git.

## One-concept objective

Integrate one real Limelight NetworkTables adapter behind the frozen vendor-
neutral `VisionIO` contract. The adapter owns transport/schema decoding and
camera-frame conversion. It does not estimate robot pose, fuse measurements,
publish telemetry, control the robot, or expose vendor types through public
contracts.

## Locked transport and safety contract

The adapter reads `/limelight/tv`, `/limelight/tid`, `/limelight/hb`, and
`/limelight/targetpose_cameraspace`. A finite nonnegative heartbeat establishes
source availability. A target is accepted only for `tv == 1`, a positive
integer tag id, and exactly six finite pose values. `tv == 0` is an invalid
acquisition sample. Repeated unchanged heartbeats invalidate the current
sample and fail closed after two repeated cycles. Heartbeat reset/reconnect
requires fresh target-pose evidence beyond both the prior target-refresh
boundary and the prior heartbeat-change boundary. Stable current `tv` and
`tid` values remain required structural fields; a changed tag id or visibility
value alone is insufficient, and target changes observed while the heartbeat is
stalled are not recovery evidence. The independent topics are not an
atomic NetworkTables transaction; two equal target snapshots plus heartbeat
rechecks form the bounded coherence guard. Every invalid or unusable target
cycle clears targets and fails closed.

The output direction is `cameraToTarget`; the adapter does not invert the
transform. Limelight translation axes map to WPILib as
`(x, y, z) = (limelightZ, -limelightX, -limelightY)`.

**ROTATION STATUS: PROVISIONAL COMMISSIONING LOCK**

The authorized H1 convention is:

```text
R = Rx(-a3) * Ry(+a4) * Rz(+a5)
```

with angles in degrees from `targetpose_cameraspace[3..5]`, evaluated using
column-vector matrix multiplication. This remains provisional until physical
camera validation.

The inherited extrinsic remains
`Translation3d(-0.038, +0.050, +0.114)` metres and
`Rotation3d(0, -20 degrees, 0)`.

## Implementation boundary

### Production files

- `src/main/java/frc/robot/io/vision/VisionIOLimelight.java`
- `src/main/java/frc/robot/subsystems/VisionSubsystem.java`
- `src/main/java/frc/robot/telemetry/vision/VisionTelemetryFacade.java`
- `src/main/java/frc/robot/telemetry/RobotTelemetry.java`
- `src/main/java/frc/robot/Constants.java`
- `src/main/java/frc/robot/RobotContainer.java`

### Test files

- `src/test/java/frc/robot/io/vision/VisionIOLimelightTest.java`
- `src/test/java/frc/robot/subsystems/VisionSubsystemTest.java`
- `src/test/java/frc/robot/telemetry/vision/VisionTelemetryFacadeTest.java`

### Explicitly excluded

V00_L09 fusion, `SwerveDrivePoseEstimator` wiring, autonomous behavior,
pose-fusion behavior, autonomous behavior, Swerve/IO/tuning/calibration,
PathPlanner, vendordeps, camera-vendor helper libraries, and repository Git
operations.

## Completed implementation sequence

1. Preserved the candidate and reconciled the exact seven authorized inherited
   repair files against the corrected V00_L07 donor.
2. Recorded the H1 provisional commissioning lock and preserved extrinsic.
3. Added the Limelight NetworkTables adapter with strict fail-closed decoding.
4. Added real-versus-simulation adapter selection at the composition root.
5. Added the observation-only runtime owner and read-only diagnostic telemetry;
   both real and simulation IO use the same path.
6. Added focused freshness, reconnect/reset, coherence, runtime-owner, and
   public-boundary tests.
7. User evidence confirms `compileJava`, `compileTestJava`, focused 37/37,
   full regression 642/642, and clean build PASS under Java 17. The earlier
   642-test result of 637 passed and five isolated failures is retained as
   historical pre-repair evidence.
8. User-owned WPILib Simulation, Driver Station/Glass, and real Limelight
   acquisition/loss/reacquisition evidence are PASS. Simulation validates the
   Java runtime path and inherited `VisionIOSim` default only; it does not claim
   that LimelightOS or the real adapter was simulated.

## Final lifecycle sequence

1. The final read-only architecture and documentation review returned
   `PASS_V00_L08_FINAL_CLOSURE_REVIEW`.
2. Closure approval records that H1 remains provisional and that no fusion,
   estimator, or vendor-boundary drift occurred.
3. The transition guide, status, plan, checklist, and lesson README are final
   and record `COMPLETE / FROZEN / READ-ONLY`.
4. The User performed the Git add/commit/push workflow separately. Publication
   is recorded at `f34b210` with push PASS to `origin/main`; Codex did not run
   Git.
