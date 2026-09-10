# V00_L08 - Real Vision Adapter Integration

## Current lesson state

- **Status:** COMPLETE / FROZEN / READ-ONLY
- **Freeze state:** FROZEN
- **Predecessor:** V00_L07 - Vision Timestamp and Latency Contract @ `4704cfc`
- **Implementation result:** COMPLETE / VERIFIED / DOCUMENTATION COMPLETE /
  FINAL ARCHITECTURE AND CLOSURE REVIEW PASS
- **Git:** User-owned; Codex did not run Git

This lesson-local record reflects the user-authorized V00_L08 repair and final
closure review. V00_L08 is complete and frozen; V00_L09 and all fusion work
remain out of scope.

## Objective

Add the first real vision transport adapter while preserving the Frozen
Backbone and the vendor-neutral VisionIO/Observation boundary. The adapter is
an acquisition and conversion layer only. It does not estimate field pose,
fuse vision, reset the estimator, publish telemetry, or control autonomous or
Swerve behavior.

## Limelight transport

The adapter reads only these entries from the `/limelight` NetworkTables table:

```text
/limelight/tv
/limelight/tid
/limelight/hb
/limelight/targetpose_cameraspace
```

Acceptance requires a finite nonnegative heartbeat, `tv == 1`, a positive
integer tag id, and an exact six-element finite pose array. `tv == 0` is an
invalid acquisition sample, not a valid target-bearing observation. Repeated
unchanged heartbeats invalidate the sample immediately and fail closed after
two repeated cycles. A heartbeat reset or reconnect requires fresh target-pose
evidence beyond both the prior target-refresh boundary and the prior heartbeat-
change boundary. Stable current `tv` and `tid` values remain required
structural fields; a changed tag id or visibility value alone is not recovery
evidence, and target changes observed while the heartbeat is stalled are not
recovery evidence. Missing or malformed data clears the current target list.
The independent NetworkTables topics are not atomic; the
adapter reads the target tuple twice, rejects disagreement, and
uses heartbeat rechecks as a bounded coherence guard. A six-zero pose with no
target does not create a measurement.

The adapter returns `cameraToTarget` exactly as the camera-relative target
transform. It does not invert the transform. No LimelightHelpers or other
vendor runtime library is used.

## Frame and extrinsic contract

Limelight translation axes are right/down/forward. The locked WPILib mapping is:

```text
x_wpilib = +z_limelight
y_wpilib = -x_limelight
z_wpilib = -y_limelight
```

The inherited robot-to-camera extrinsic is preserved:

```text
Translation3d(-0.038, +0.050, +0.114) metres
Rotation3d(0, -20 degrees, 0)
```

**ROTATION STATUS: PROVISIONAL COMMISSIONING LOCK**

The newly authorized H1 rotation is implemented with column-vector matrices:

```text
R = Rx(-a3) * Ry(+a4) * Rz(+a5)
```

`a3`, `a4`, and `a5` are `targetpose_cameraspace[3..5]` in degrees. Focused
tests cover each axis, a noncommutative combined fixture, and a known captured
tuple. This remains provisional until real-camera validation; it is not claimed
as vendor-official or lab-grade proof.

The commissioning evidence basis is preserved: controlled signed +Z testing
reduced the candidate set; controlled signed +Y testing confirmed positive-Y
behavior; H1 was the only candidate classified STRONG in both earlier matrix
tests; the real-robot Pigeon yaw reference was `+0.104370 deg`, the negative
endpoint was `-24.798889 deg`, and the physical delta was `-24.903259 deg`.
H1 decoded to approximately `25.166 deg`, with approximately `0.263 deg`
magnitude disagreement. The positive-yaw test was less consistent, so this is
commissioning evidence rather than laboratory-grade proof.

## Architecture boundary

```text
Limelight NetworkTables / VisionIOSim
        -> VisionIO.updateInputs(...)
        -> VisionSubsystem
        -> immutable VisionObservation
                |\
                | +--> read-only VisionTelemetryFacade / NT4
                +----> future V00_L09 observation/fusion boundary
```

Simulation selects the existing deterministic `VisionIOSim`. Real hardware
selects `VisionIOLimelight`. Both use the same observation-only runtime owner;
simulation does not read Limelight topics. No runtime fusion or estimator
wiring is included in V00_L08. Existing V00_L06/V00_L07 timing and quality
types remain inherited contracts and are not fabricated or widened by this
adapter. RobotContainer remains the composition root and only constructs and
injects the owner.

Limelight topic names are private to the real adapter. Public contracts and
the frozen observation model expose no Limelight or vendor types.

## Verification evidence

- User-verified `compileJava` and `compileTestJava`: PASS under Java 17.
- Focused post-repair adapter tests: 37/37 PASS.
- Full regression after repair: 642/642 PASS.
- Clean build after repair: PASS.
- The earlier 37-test/5-failure focused result and 642/637/5 full result are
  preserved as historical pre-repair evidence, not current failures.
- WPILib Simulation startup and Teleop runtime: PASS. The inherited
  `VisionIOSim` default reports no target (`UNAVAILABLE`), which is expected.
  This validates the Java simulation path only; it does not claim LimelightOS
  or the real `VisionIOLimelight` adapter was simulated.
- Driver Station/Glass: PASS; Java telemetry is visible.
- Real Limelight: PASS for AprilTag 32 acquisition, target loss, and
  reacquisition. Target loss reports `INVALID_SAMPLE` while the connection
  remains available; reacquisition returns `TARGETS_PRESENT` with valid pose
  telemetry.

## Closure and final freeze

The implementation preserves the vendor-neutral boundary, observation-only
runtime ownership, and the exclusion of V00_L09 estimator fusion. H1 remains
the **PROVISIONAL COMMISSIONING LOCK**; real evidence does not promote it to
official or proven vendor semantics. The bounded NetworkTables double-read and
heartbeat checks are a coherence guard, not an atomic multi-topic read claim.

The final read-only architecture and closure review returned
`PASS_V00_L08_FINAL_CLOSURE_REVIEW`. The transition guide and documentation
reconciliation are final and PASS. V00_L08 is now documented as
`COMPLETE / FROZEN / READ-ONLY`. Git publication remains pending the User's
commit/push; no publication commit hash is claimed.
