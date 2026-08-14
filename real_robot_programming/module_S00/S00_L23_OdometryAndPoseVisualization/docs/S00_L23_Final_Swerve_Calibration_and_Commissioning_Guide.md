# S00_L23 Final Swerve Calibration and Commissioning Guide

## Status and Purpose

- Lesson: `S00_L23_OdometryAndPoseVisualization`
- Lesson state: `COMPLETE / FROZEN / READ-ONLY`
- Document role: authoritative final L23 calibration, commissioning, distance-validation, odometry, and visualization reference
- Production and test code: unchanged by this documentation amendment

This guide consolidates the final L23 drivetrain facts and operator procedures so they can be reproduced without chat history. It does not replace the source-code configuration authority in `Constants.java` or the CTRE application/readback behavior in `SwerveModuleIOCTRE.java`. When a value is not explicitly established by current source and recorded hardware evidence, this guide labels it unconfigured, unresolved, or deferred rather than inventing a value.

## 1. Authority and Supersession

The repository authority order remains:

1. `AGENTS.md`
2. Document A
3. Document B
4. Document C
5. repository and lesson documentation
6. current frozen source code

For final L23 drivetrain configuration, use this guide together with the frozen L23 source. Earlier lesson documents remain historical evidence of what was known at that time.

### Historical L14 matrix

`docs/S00_L14_Swerve_Hardware_Commissioning_Matrix.md` is preserved unchanged. It must not be silently rewritten because it records the L14 knowledge state.

| Historical L14 entry | Final L23 authority | Supersession decision |
|---|---|---|
| Drive ratio `7.85:1` / inherited configured value `7.846153846153847` | `6.75:1` | **SUPERSEDED.** Hardware documentation and repeated FR raw TalonFX `RotorPosition` measurements over 20 wheel rotations produced approximately `6.74-6.75:1`. |
| Drive inversion unresolved | FL `false`, FR `true`, BL `false`, BR `true` | **SUPERSEDED** by the frozen L23 `Constants.java` mapping and CTRE configuration contract. |
| Steer inversion unresolved | FL/FR/BL/BR all `true` | **SUPERSEDED** by the frozen L23 mapping and configuration contract. |
| CANcoder direction partially unresolved | FL/FR/BL/BR all `CounterClockwise_Positive` | **SUPERSEDED** by final per-module constants and CTRE configuration. |
| Absolute offsets unresolved | Final offsets listed in Section 3 | **SUPERSEDED** by the L16 Phoenix calibration evidence retained through L23. |
| Current limits unresolved | Drive supply `70 A`, steer stator `60 A`, both enabled | **SUPERSEDED** by the final configuration contract. |
| Steer ratio unresolved | Configured `15.42857142857143:1` | **SUPERSEDED** by the frozen configuration authority. |
| Neutral mode unresolved | No explicit L23 neutral-mode assignment | **NOT SUPERSEDED.** Do not infer or claim a calibrated neutral mode from a vendor default. |
| Ramp rates unresolved | No explicit L23 ramp-rate assignment | **NOT SUPERSEDED.** No final ramp calibration is claimed. |
| Peak output settings unresolved | No explicit L23 peak-forward/peak-reverse configuration assignment | **NOT SUPERSEDED.** Software request clamps are documented separately and are not TalonFX peak-output calibration. |

## 2. Three Distinct Concepts

### A. Hardware calibration

Hardware calibration establishes physical and device facts: CAN IDs, ratios, motor inversion, CANcoder direction, magnet offsets, measurement signs, current limits, and verified signal conventions. These values define what sensor and actuator numbers mean.

### B. Control tuning

Control tuning establishes closed-loop response: Slot 0 PID and feedforward gains. The current drive values are sufficient for L23 validation but remain provisional. Tuning must not be used to conceal an incorrect ratio, offset, inversion, sensor direction, or unit conversion.

### C. Runtime field and pose initialization

Runtime initialization chooses the field-heading zero for the current robot program run and allows odometry to initialize. Pressing Xbox Back/View while Disabled does not recalibrate the Pigeon, motor controllers, CANcoders, or mechanical module zero.

## 3. Final Hardware Configuration

### CAN map

All devices use the default roboRIO CAN bus; the historical hardware audit names it `rio`.

| Device/module | Drive TalonFX | Steer TalonFX | CANcoder | Pigeon2 |
|---|---:|---:|---:|---:|
| Front Left (FL) | 21 | 22 | 23 | - |
| Front Right (FR) | 24 | 25 | 26 | - |
| Back Left (BL) | 27 | 28 | 29 | - |
| Back Right (BR) | 30 | 31 | 32 | - |
| Robot gyro | - | - | - | 20 |

### Mechanical and measurement configuration

| Item | Final L23 value | Meaning/status |
|---|---:|---|
| Drive ratio | `6.75:1` | Verified physical ratio; motor rotor rotations per wheel rotation. |
| Steer ratio | `15.42857142857143:1` | Frozen configured mechanical ratio. The current remote-CANcoder closed-loop feedback is expressed directly in module rotations. |
| Wheel diameter | `4.0 in = 0.1016 m` | Production distance geometry. |
| Wheel radius | `2.0 in = 0.0508 m` | `kWheelRadiusMeters`; used directly in circumference calculations. |
| Wheel circumference | `2 * pi * 0.0508 = 0.319185813604723 m` | Distance traveled per wheel rotation under the nominal wheel model. |
| Drive raw-position normalization signs | FL `+1`, FR `+1`, BL `+1`, BR `+1` | Applied only at the measured-position boundary. These signs are not motor inversion. |
| Maximum production wheel-speed request | `4.0 m/s` | Software clamp in the output/CTRE request path, not a TalonFX peak-output setting. |

The BR sign is supported by repeated disabled physical calibration. Moving BR physical-forward by approximately one wheel revolution produced increasing raw TalonFX rotor positions with deltas `+6.661621`, `+6.921387`, and `+6.501953` rotations. Therefore BR physical-forward raw position uses sign `+1`.

### Per-module motor and encoder configuration

`false` motor inversion maps to CTRE `CounterClockwise_Positive`; `true` maps to `Clockwise_Positive`. All CANcoder inversion constants are `false`, which maps to `CounterClockwise_Positive`.

| Module | Drive inverted | Drive CTRE positive | Steer inverted | Steer CTRE positive | CANcoder direction | Magnet offset (rotations) |
|---|---:|---|---:|---|---|---:|
| FL | `false` | `CounterClockwise_Positive` | `true` | `Clockwise_Positive` | `CounterClockwise_Positive` | `0.067138671875` |
| FR | `true` | `Clockwise_Positive` | `true` | `Clockwise_Positive` | `CounterClockwise_Positive` | `0.02099609375` |
| BL | `false` | `CounterClockwise_Positive` | `true` | `Clockwise_Positive` | `CounterClockwise_Positive` | `0.464599609375` |
| BR | `true` | `Clockwise_Positive` | `true` | `Clockwise_Positive` | `CounterClockwise_Positive` | `-0.052978515625` |

### CTRE configuration fields explicitly authoritative in L23

| Device/configuration area | Explicit final L23 configuration |
|---|---|
| Drive TalonFX current limit | Supply current limit `70.0 A`, enabled. |
| Drive TalonFX feedback | `RotorSensor`; `RotorToSensorRatio=1.0`; `SensorToMechanismRatio=6.75`. |
| Drive TalonFX closed loop | Slot 0 values in Section 5. |
| Drive velocity request | `VelocityVoltage`, Slot 0, FOC explicitly disabled. Request units are wheel/mechanism rotations per second. |
| Steer TalonFX current limit | Stator current limit `60.0 A`, enabled. |
| Steer TalonFX feedback | `RemoteCANcoder` with that module's encoder CAN ID; `RotorToSensorRatio=1.0`; `SensorToMechanismRatio=1.0`. |
| Steer TalonFX closed loop | Continuous wrap enabled; Slot 0 `kP=100.0`, `kI=0.0`, `kD=0.5`; `PositionVoltage`, Slot 0, FOC explicitly disabled. |
| CANcoder | Per-module `CounterClockwise_Positive` direction and magnet offset from the table above. |
| Pigeon2 | CAN ID 20; L23 reads yaw, pitch, roll, and XYZ angular velocity. No L23 mounting, yaw-offset, or other Pigeon configuration is applied. Configuration health reports successful refresh of a default `Pigeon2Configuration`, not a calibrated mounting transform. |
| Static-friction request | `VoltageOut`, FOC explicitly disabled; FL only. |

### Fields intentionally not claimed as calibrated

The frozen source does not explicitly assign neutral mode, open-loop ramp, closed-loop ramp, TalonFX peak-forward output, TalonFX peak-reverse output, voltage-compensation configuration, Pigeon mounting pose, or a Pigeon hardware yaw offset. A newly constructed Phoenix configuration is applied, but this guide does not promote library defaults into verified physical calibration values.

## 4. Numeric Conversion and Sign Contract

### Raw rotor position to distance

`SwerveModuleIOCTRE` reads TalonFX raw `RotorPosition` in rotor rotations and transports it as `drivePositionRotations`. The subsystem converts each module independently:

```text
normalizedRotorRotations = physicalForwardSign * rawRotorRotations
wheelRotations = normalizedRotorRotations / 6.75
distanceMeters = wheelRotations * (2 * pi * 0.0508 m)
```

For the frozen L23 configuration, every `physicalForwardSign` is `+1`.

Equivalent single expression:

```text
distanceMeters = physicalForwardSign
    * rawRotorRotations
    / 6.75
    * 0.319185813604723 m
```

Motor inversion controls the commanded motor-positive direction. It does not replace or imply the measured-position normalization sign. Measurement signs must be established from physical-forward raw-position evidence.

### Drive velocity request

```text
wheelRotationsPerSecond = velocityMetersPerSecond / wheelCircumferenceMeters
VelocityVoltage request = wheelRotationsPerSecond
expectedRotorRotationsPerSecond = wheelRotationsPerSecond * 6.75
```

Phoenix interprets the `VelocityVoltage` request in wheel/mechanism rotations per second because `SensorToMechanismRatio=6.75` is configured. The ratio is not multiplied into the Java request a second time.

At `+0.30 m/s`:

```text
wheel speed = 0.30 / 0.319185813604723
            = 0.9398913962119804 wheel rotations/s
expected raw rotor speed = 0.9398913962119804 * 6.75
                         = 6.344266924430867 rotor rotations/s
```

### Module angle

The configured CANcoder applies its sensor direction and magnet offset. The resulting absolute position, in module rotations, is transported as `encoderAbsolutePositionRotations` and converted with:

```text
moduleAngle = Rotation2d.fromRotations(encoderAbsolutePositionRotations)
```

WPILib-positive rotation and gyro yaw are counterclockwise positive.

### Optimized Swerve distance during the 3 m test

Swerve optimization may command negative signed wheel travel while the module points near 180 degrees. That can still produce positive robot-forward motion. The 3 m command therefore accumulates incremental forward projection without using absolute value:

```text
signedIncrement = currentSignedDistanceMeters - previousSignedDistanceMeters
forwardIncrement = signedIncrement * cos(currentMeasuredModuleAngle)
accumulatedForwardDistance += forwardIncrement
```

## 5. Control Tuning

### Drive Slot 0

**PROVISIONAL COMMISSIONING BASELINE - NOT FINAL OPTIMIZED GAINS**

| Gain | Final frozen L23 value | Mechanism-space meaning used by Phoenix `VelocityVoltage` |
|---|---:|---|
| `kP` | `0.675` | Volts per wheel-rotation-per-second velocity error. |
| `kI` | `0.0` | Integral term disabled. |
| `kD` | `0.0` | Derivative term disabled. |
| `kS` | `0.15 V` | Static feedforward voltage. |
| `kV` | `0.837 V/(rotation/s)` | Velocity feedforward per wheel/mechanism rotation per second. |
| `kA` | `0.0` | Acceleration feedforward disabled. |

These values produced a functional L23 real-floor 3 m validation and plausible real odometry. They are not evidence of final optimal transient response, efficiency, current draw, traction behavior, or battery-voltage robustness.

Before this provisional baseline was applied, the `+0.30 m/s` FL request produced a Phoenix mechanism-space velocity reference of approximately `0.93945 rotations/s`, confirming the request and ratio path. Raw rotor velocity remained far below the expected approximately `6.34427 rotations/s`, while motor voltage was only approximately `0.20 V` and duty cycle approximately `1.5%`. Additional real observations included BL output `-0.016602` with rotor velocity `-0.515625 rotations/s` and BR output `+0.014648` with rotor velocity `+0.306641 rotations/s`. Those measurements moved the investigation boundary to closed-loop gains; they did not justify changing the verified ratio or distance conversion.

### Phoenix kS readback quantization

```text
expected kS = 0.150000000000 V
actual kS = 0.150390625000 V
difference = +0.000390625000 V
quantization step = 1 / 1024 = 0.0009765625 V
nearest-bin tolerance = half step = 1 / 2048 = 0.00048828125 V
```

The observed difference is within one half-step and is accepted. Adjacent quantization bins remain rejected by focused tests. This special `1/2048 V` tolerance applies only to the evidenced `kS` readback representation. It is not generalized to `kP`, `kI`, `kD`, `kV`, `kA`, ratios, current limits, offsets, or other fields. Other Slot 0 gains retain the narrow float32 readback tolerance defined in `SwerveModuleIOCTRE`.

The frozen comparison values are `3.0e-8` absolute tolerance for `kP`, `kI`, `kD`, `kV`, and `kA`; a combined `1.0e-6` absolute/relative tolerance for feedback ratios; `1.0e-9` for strict base numeric fields; and one `1/4096` rotation step for modulo-one CANcoder offset comparison.

## 6. Safe Commissioning Procedure

### Universal prerequisites

1. Use the real robot only with trained supervision, a clear exclusion zone, a reachable disable control, and the robot mechanically secured for the selected test.
2. Raise all wheels securely for single-module open-loop, closed-loop, steer-step, and static-friction tests. The 3 m floor test is a separate procedure in Section 7.
3. Confirm CAN IDs and wiring against Section 3.
4. Start Disabled and inspect `/Swerve/FrontLeft`, `/Swerve/FrontRight`, `/Swerve/BackLeft`, `/Swerve/BackRight`, and `/Swerve/Gyro` health.
5. Require all relevant connection and configuration-health values to be true before enabling Test mode.
6. Use Driver Station enabled **Test mode**, not Teleop or Autonomous.
7. Trigger only one dashboard command at a time. Every commissioning command requires `SwerveSubsystem`, preventing simultaneous subsystem commands.
8. Disable immediately for unexpected direction, vibration, steering motion, current, sound, heat, communication loss, or configuration-health loss.

### Dashboard commands and bounded behavior

| SmartDashboard/Glass command | Exact action | Bound |
|---|---|---|
| `FL Drive Positive` / `FL Drive Negative` | FL drive only at `+/-0.05` duty cycle; steer is first commanded zero output. | `0.25 s` pulse. |
| `FL Steer Positive` / `FL Steer Negative` | FL steer only at `+/-0.05` duty cycle; drive is first commanded zero output. | `0.25 s` pulse. |
| `FL Closed-Loop Drive Positive` / `FL Closed-Loop Drive Negative` | FL drive velocity `+/-0.30 m/s`, clamped inside `+/-0.50 m/s`. **This command does not command or hold a steer angle.** Secure the raised robot and establish a safe wheel orientation before use. | `1.0 s` timeout. |
| `FL Closed-Loop Steer Positive` / `FL Closed-Loop Steer Negative` | Relative steer target `+/-0.0625` module rotations from the measured CANcoder angle; maximum allowed step `0.125` rotations. | `1.0 s` timeout. |
| `FL Drive Static Friction +0.10 V` through `+1.00 V` | One independent FL positive voltage pulse in `0.10 V` increments. It never automatically sweeps. | `0.25 s` per pulse; maximum `1.0 V`. |

### Fail-closed contracts

- Open-loop nonzero output requires healthy drive-base, steer-base, and encoder-base configuration. Zero/stop remains permitted.
- Closed-loop drive requires the factory closed-loop module path, healthy drive closed-loop configuration, and healthy encoder base configuration.
- Closed-loop steer requires healthy steer closed-loop configuration and healthy encoder base configuration.
- Static-friction characterization additionally requires Front Left identity, enabled Test mode, healthy drive-base configuration, and a finite positive request.
- An invalid request, unhealthy required configuration, mode exit, timeout, interruption, submission exception, or watchdog expiration stops the affected module. The full 3 m command stops all modules.
- Subsystem mutual exclusion prevents a second FL commissioning session from starting while one is active.

### Relevant NT4 telemetry

For each module, replace `<Module>` with `FrontLeft`, `FrontRight`, `BackLeft`, or `BackRight`:

```text
/Swerve/<Module>/DriveAppliedOutput
/Swerve/<Module>/DrivePositionRotations
/Swerve/<Module>/DriveVelocityRotationsPerSecond
/Swerve/<Module>/SteerAppliedOutput
/Swerve/<Module>/SteerPositionRotations
/Swerve/<Module>/SteerVelocityRotationsPerSecond
/Swerve/<Module>/EncoderAbsolutePositionRotations
/Swerve/<Module>/DriveConnected
/Swerve/<Module>/SteerConnected
/Swerve/<Module>/EncoderConnected
/Swerve/<Module>/DriveConfigurationHealthy
/Swerve/<Module>/SteerConfigurationHealthy
/Swerve/<Module>/EncoderConfigurationHealthy
```

`DriveVelocityRotationsPerSecond` is raw TalonFX rotor velocity. `DriveAppliedOutput` is the reported duty-cycle signal.

### Relevant Phoenix Tuner signals

For FL drive TalonFX CAN 21, record at minimum:

- `RotorPosition` and `RotorVelocity`;
- mechanism `Position` and `Velocity`;
- closed-loop reference, error, output, and active Slot;
- `MotorVoltage`, `DutyCycle`, and `SupplyVoltage`;
- supply, stator, and torque current;
- control mode, device-enable state, and motor-output status;
- active faults and configuration/apply status.

Supply voltage/current, motor voltage, torque current, and detailed closed-loop signals exist at the CTRE IO boundary but are not all published by the current Swerve NT facade. Use Phoenix Tuner/logging for those measurements rather than assuming a missing NT value is zero.

Each static-friction pulse emits a console result containing requested voltage, `setControl` status, peak rotor velocity, peak mechanism velocity, peak supply current, peak torque current, breakaway classification, and stop reason. The configured breakaway threshold is `0.10 rotor rotations/s`.

## 7. Physical 3 m Distance Validation

This diagnostic validates `SwerveModulePosition.distanceMeters`. It is not autonomous functionality and does not use odometry for completion.

### Setup

1. Choose a level, high-traction, unobstructed lane longer than 3 m with safe stopping clearance.
2. Mark the robot reference-point start and the physical 3.000 m direction with a tape measure. Record which robot point is used for both start and finish comparison.
3. Place the robot on the floor with its head aligned along the clear test lane. A field-heading capture is not required because this command is robot-relative.
4. Inspect all wheels, steering assemblies, battery, bumpers, and CAN wiring. Keep all people outside the motion lane.
5. Connect Driver Station and Glass. Remain Disabled while confirming all module connection and configuration-health values are true.
6. Open the `Drive 3m Validation` command and the telemetry listed below.

### Run

1. Select and enable Driver Station **Test mode**.
2. Confirm the lane is clear and trigger `Drive 3m Validation` once.
3. Do not issue another drivetrain command. Be prepared to disable immediately.
4. The command snapshots FL/FR/BL/BR measured positions before requesting motion.
5. It requests robot-relative `vx=+0.30 m/s`, `vy=0`, `omega=0` through the normal Swerve output pipeline.
6. It accumulates each module's incremental projected-forward distance, computes the median of four accumulators, and stops at measured median `>= 3.000 m`.
7. It preserves real overshoot; it does not clamp the reported result to `3.000 m`.

At the nominal request, 3 m requires approximately 10 seconds. The safety timeout is 15 seconds.

### Telemetry

```text
/DriveThreeMeterValidation/TargetMeters
/DriveThreeMeterValidation/MeasuredMeters
/DriveThreeMeterValidation/FLDeltaMeters
/DriveThreeMeterValidation/FRDeltaMeters
/DriveThreeMeterValidation/BLDeltaMeters
/DriveThreeMeterValidation/BRDeltaMeters
/DriveThreeMeterValidation/Running
/DriveThreeMeterValidation/Complete
/DriveThreeMeterValidation/FaultAbortReason
```

### Acceptance criteria

PASS requires all of the following:

- `TargetMeters = 3.000`;
- `MeasuredMeters >= 3.000` without fabricated clamping;
- `Complete=true`, `Running=false`, and `FaultAbortReason=COMPLETE`;
- all four accumulated projected distances are finite and nonnegative;
- every module remains connected and configuration-healthy;
- each module remains within `0.15 m` of the four-module median;
- no timeout, mode exit, interruption, submission failure, invalid measurement, negative projected travel, module disagreement, module-health fault, or telemetry failure;
- every module is stopped at command termination;
- the independent tape-measured physical distance is reasonably consistent with the software result for this learning validation, with the measurement method and uncertainty recorded.

An abort is a fail-closed diagnostic result, not permission to bypass health, negative-travel, disagreement, or timeout checks.

### Recorded L23 evidence

- The final real-floor `Drive 3m Validation` completed at approximately 3.0 m with the verified `6.75:1` ratio and current provisional drive gains.
- Earlier timeout evidence at 15 seconds recorded `MeasuredMeters=0.249419`, FL `0.254200`, FR `0.248921`, BL `0.241972`, and BR `0.249917`, with physical travel approximately `0.32 m`. The four consistent module values isolated insufficient actuation from ratio, wheel geometry, sign, projection, and consensus behavior. It is not the final PASS run.
- No exact final per-module endpoint values were preserved in the frozen lesson evidence, so this guide does not invent them.

## 8. Runtime Field Heading, Odometry, Pose Telemetry, and Field2d

### Field-heading capture

1. Keep the robot Disabled.
2. Align the robot head with the chosen field `+X` direction.
3. Wait for at least one complete gyro refresh and confirm Pigeon connection/configuration health and finite yaw.
4. Press and release Xbox Back/View (Button 7) once.
5. The command captures the current raw Pigeon yaw as a software reference, stops/disarms the drivetrain, and finishes immediately.
6. Enable only after the capture. The next field-relative drive request must be a fresh request.

The runtime field heading is:

```text
fieldHeadingDegrees = wrap(rawYawDegrees - capturedRawYawDegrees, [-180, +180))
```

Positive heading is counterclockwise. Capture does not reset or configure Pigeon hardware. The reference survives ordinary Disable-to-Enable transitions in the same robot-program runtime. Recapture only when intentionally redefining field zero or after reference/gyro validity loss; do not recapture merely because the robot has rotated.

### Odometry initialization and validity

- `SwerveSubsystem` owns `SwerveDriveOdometry` and the current `Pose2d`.
- Before a valid field reference and complete healthy finite four-module sample, pose is unavailable.
- The first valid sample initializes pose at `X=0 m`, `Y=0 m`, and the accepted software field heading, normally approximately `0 degrees` immediately after capture.
- Module order is fixed `FL, FR, BL, BR` and uses the same geometry authority as the output pipeline.
- Odometry updates exactly once per subsystem `periodic()` cycle.
- Invalid/nonfinite gyro data, unhealthy modules, or invalid module positions do not update pose. The last valid pose is held and published with `MeasurementSampleValid=false`.
- On recovery, odometry is re-established at the held pose and current valid measurements so movement during the invalid interval is not integrated as a jump.

### Pose NT4 keys

```text
/Swerve/Pose/Available
/Swerve/Pose/XMeters
/Swerve/Pose/YMeters
/Swerve/Pose/HeadingDegrees
/Swerve/Pose/MeasurementSampleValid
```

Before initialization, `Available=false` and numeric pose values are not fabricated. Once available, an invalid current sample leaves the held numeric pose visible while `MeasurementSampleValid=false`.

### Field2d publication and Glass procedure

The telemetry layer owns one `Field2d`. It registers only after pose becomes available:

```text
/SmartDashboard/Swerve/Field
```

Glass/NT4 may show the source as a `Swerve/Field` node under `SmartDashboard`, with child topics including `.type="Field2d"` and `Robot` pose data.

1. Connect Glass to the robot and open the NetworkTables view.
2. Confirm `/Swerve/Pose/Available=true` after the Disabled field-heading capture and first healthy sample.
3. Expand `SmartDashboard`, then `Swerve`, and locate `Field`.
4. Confirm the source reports `.type="Field2d"` and contains the `Robot` double-array data.
5. Open/add a Field2d widget and bind/select `/SmartDashboard/Swerve/Field` as its source.
6. Move the robot through a small controlled floor motion and confirm the marker changes consistently with `/Swerve/Pose/XMeters`, `/Swerve/Pose/YMeters`, and `/Swerve/Pose/HeadingDegrees`.
7. If `Available=false`, do not expect Field2d registration. If `MeasurementSampleValid=false` after initialization, the widget intentionally holds the last valid pose.

### Recorded L23 pose evidence

- Module and gyro connection/configuration health: PASS.
- Disabled software field-heading capture: PASS.
- Pose transitioned from unavailable to available: PASS.
- Initial pose approximately `(0 m, 0 m, 0 degrees)`: PASS.
- Healthy measurements published `MeasurementSampleValid=true`: PASS.
- Approximately `0.45 m` physical forward movement produced `XMeters=0.458470 m`, with heading remaining near zero: PASS.
- `/Swerve/Pose/...` NT4 telemetry: PASS.
- `/SmartDashboard/Swerve/Field` publication: PASS.
- Glass recognized `.type="Field2d"` and the `Robot` double array: PASS.
- Field2d widget opened and its marker moved with the real robot: PASS.

## 9. Configuration Readback and Health

Each real module applies its drive TalonFX, steer TalonFX, and CANcoder configuration, refreshes readback, and verifies required fields. Configuration health is not forced true.

Drive closed-loop health includes apply and refresh status, drive inversion, supply-current-limit enable/value, feedback source, ratios, and Slot 0 `kP`, `kI`, `kD`, `kS`, `kV`, and `kA`.

Steer health includes inversion, stator-current limit, remote CANcoder ID and feedback fields, continuous wrap, and steer Slot 0. Encoder health includes apply/refresh, direction, and modulo-one offset comparison within one CANcoder quantization step (`1/4096` rotation).

On a drive configuration mismatch, startup diagnostics identify the first failing field with expected value, actual value, difference, and tolerance. A false health flag prevents the corresponding nonzero request while preserving safe stop behavior.

## 10. Deferred and Unresolved Work

- final optimized drive PID/feedforward tuning across all modules, battery conditions, and loaded-floor operation;
- independent retained raw-ratio characterization for every module beyond the verified common ratio and successful four-module floor evidence;
- explicit neutral-mode calibration;
- explicit open-loop/closed-loop ramp-rate calibration;
- explicit TalonFX peak-forward/peak-reverse output calibration;
- high-fidelity battery, traction, current, thermal, or chassis physics simulation;
- known-field-pose reset architecture;
- `SwerveDrivePoseEstimator`;
- vision or AprilTag fusion;
- PathPlanner, trajectories, autonomous behavior, alliance transforms, and L24 autonomous readiness.

Deferred work must begin from new evidence and the next editable lesson. It must not modify this frozen production snapshot merely to improve tuning or add L24 capability.

## 11. Reproducibility Checklist

- [ ] Confirm CAN map, ratios, and wheel geometry.
- [ ] Confirm per-module drive/steer inversion and CANcoder direction/offset.
- [ ] Confirm current limits and all configuration-health flags.
- [ ] Do not assume a neutral mode, ramp, peak output, or Pigeon mounting value that is not explicitly configured.
- [ ] Keep hardware calibration separate from control tuning and runtime initialization.
- [ ] Treat drive Slot 0 values as provisional commissioning gains.
- [ ] Use raised-wheel, enabled-Test-mode, bounded commissioning commands only.
- [ ] Record Phoenix mechanism reference, rotor velocity, voltage, duty cycle, current, error, health, and faults.
- [ ] Run the floor 3 m validation with full telemetry and an independent tape measurement.
- [ ] Capture software field heading while Disabled before field-relative/odometry verification.
- [ ] Verify pose availability, validity, NT4 keys, and Field2d marker behavior.
- [ ] Record new evidence without rewriting historical frozen lesson documents.

## Final Statement

This guide is the authoritative final L23 calibration and commissioning reference. The L14 matrix remains historical evidence. Frozen source remains the executable configuration authority. Values not proven by source and recorded evidence remain explicitly unclaimed.
