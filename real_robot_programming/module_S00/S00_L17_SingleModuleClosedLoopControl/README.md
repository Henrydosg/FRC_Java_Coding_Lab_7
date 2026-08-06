# S00_L17 Single Module Closed-Loop Control

## Final Lesson State

`COMPLETE / FROZEN / READ-ONLY`

## Objective

Extend the frozen S00_L16 hardware contract with the smallest vendor-neutral closed-loop contract
for one representative module: Front Left drive velocity in meters per second and steer angle through
`Rotation2d`. The S00_L16 open-loop commissioning path, configuration health, fail-closed behavior,
Frozen Backbone, and read-only telemetry remain preserved.

## Architecture Constraints

- Preserve `Driver -> controls -> commands -> subsystems -> IO -> hardware`.
- Keep `RobotContainer` as the composition root only.
- Keep Phoenix APIs inside `SwerveModuleIOCTRE`.
- Keep `SwerveModuleIO` vendor-neutral.
- Actuate Front Left only during Test-mode commissioning.
- Do not add teleop input, four-module actuation, FusedCANcoder, Motion Magic, kinematics,
  odometry, pose estimation, or generated CTRE drivetrain architecture.

## Implemented Scope

- Drive: `VelocityVoltage`, Slot 0, FOC disabled.
- Steer: `PositionVoltage`, Slot 0, FOC disabled, RemoteCANcoder feedback, continuous wrap.
- Drive Slot 0 commissioning baseline: `kP=0.1`, `kI=0`, `kD=0`, `kS=0`, `kV=0.124`, `kA=0`.
- Closed-loop commissioning: Front Left `+0.30/-0.30 m/s`, clamped to `±0.50 m/s`, timeout `1.0 s`.
- Static-friction characterization: separate manual `+0.10` through `+1.00 V` commands,
  `0.25 s` pulses, peak rotor-velocity threshold `0.10 rps`, VoltageOut with FOC false.
- Each characterization click produces one result record with peak values and a typed stop reason.

## Final Verification

- Architecture Audit: PASS.
- Frozen Backbone: PASS.
- Interface Contract: PASS.
- Simulation: PASS.
- Glass / Driver Station: PASS.
- Front Left Drive Positive at `+0.30 m/s`: PASS.
- Front Left Drive Negative at `-0.30 m/s`: PASS.
- Correct directions: PASS.
- Automatic `1.0 s` stop: PASS.
- No visible oscillation or hunting: PASS.
- Static-friction result line: PASS.
- Repeated positive unloaded breakaway verification: PASS.
- Focused tests: PASS.
- Full tests: PASS.
- Clean build: PASS.

## Technical Debt and Deferred Scope

- PID and feedforward values are commissioning baselines, not production-final values.
- `kS` remains deferred and must not be inferred from one breakaway observation.
- Full SysId and static-friction characterization remain future work.
- Commissioning commands are Test-mode tools only and must not become normal drive controls.
- FusedCANcoder closed-loop feedback, Motion Magic, closed-loop teleop requests, four-module state
  actuation, kinematics, odometry, and pose estimation remain deferred.

S00_L16 remains unchanged. S00_L18 must inherit only from this frozen S00_L17 snapshot and must not
be created or modified as part of this lesson.
