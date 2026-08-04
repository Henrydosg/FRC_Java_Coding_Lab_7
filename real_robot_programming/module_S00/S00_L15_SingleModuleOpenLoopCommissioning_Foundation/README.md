# Framework Version

2.1

# Lesson

S00_L15_SingleModuleOpenLoopCommissioning_Foundation

# Previous Lesson

S00_L14_SwerveHardwareConfiguration_Commissioning_Foundation

# Status

COMPLETE — FROZEN / READ-ONLY

# Goal

Provide four explicit Glass/SmartDashboard Test-mode command buttons for bounded Front Left-only
open-loop commissioning. This lesson does not add teleop, Xbox input, chassis control, closed-loop
control, configuration writes, or automatic scheduling.

# Architecture Decision

APPROVED. `RobotContainer` constructs one `SwerveFrontLeftCommissioningDashboard` publisher and
does not contain button logic or mechanism behavior. The publisher exposes exactly four command
objects. Each command requires `SwerveSubsystem`, gates execution with
`DriverStation.isTestEnabled()`, and uses only a private fixed factory path. `SwerveSubsystem`
independently owns Front Left-only output, mutual exclusion, fixed duty clamping, mode rejection,
and the 0.25-second watchdog.

# Activation Path

1. User opens Glass or SmartDashboard and selects exactly one of `FL Drive Positive`,
   `FL Drive Negative`, `FL Steer Positive`, or `FL Steer Negative`.
2. The published command is scheduled by WPILib's command button mechanism.
3. `Robot.robotPeriodic()` runs `CommandScheduler`.
4. In Test mode, the command calls `SwerveSubsystem.startFrontLeftCommissioning()`.
5. The subsystem clears both Front Left outputs, applies one fixed ±0.05 output through the
   existing `SwerveModuleIO`, and stops automatically at or before 0.25 seconds.
6. Normal completion, cancellation, interruption, mode exit, or failure stops Front Left.

The command does not run while Disabled, Teleop, or Autonomous. FR, BL, and BR are never touched.

# Published Commands

Exactly four command identities are published:

- `FL Drive Positive`
- `FL Drive Negative`
- `FL Steer Positive`
- `FL Steer Negative`

# Hardening

- The command constructor is private; only the four fixed factories are public.
- Factory duty magnitudes are fixed at `0.05`; factory duration is fixed at `0.25` seconds.
- `DriverStation.isTestEnabled()` is checked by both command and subsystem.
- The subsystem rejects a second active commissioning action, clamps absolute output to the fixed
  limit, enforces drive/steer mutual exclusion, and owns an independent watchdog.
- Command end, cancellation, interruption, mode exit, and output failure stop Front Left.
- Disabled scheduling remains blocked by the command's default `runsWhenDisabled()` behavior.

# Resolved Verification Defects

- HAL is initialized before any `DriverStationSim` call.
- `DriverStationSim.resetData()` runs before each test establishes Driver Station state.
- The duplicate Front Left stop on output failure was removed.
- `SwerveSubsystem` owns output-failure cleanup.
- A `finally` path guarantees inactive commissioning state and watchdog cleanup.
- Stop failures are preserved as suppressed exceptions on the original output failure.

# Verification Scope

- Architecture Review: PASS.
- Implementation: PASS.
- Baseline Build: PASS.
- Focused Tests: PASS (12/12).
- Full Test Suite: PASS (48/48).
- Full Build: PASS.
- Simulation: NOT TESTED; no simulation result was supplied for this commissioning lesson.
- Glass Commissioning: PASS.
- Driver Station: PASS.
- Real Robot: PASS.
- Documentation: PASS.
- Commit: PASS — `193fd4a`.
- Push: PASS — `origin/main`.
- Freeze: PASS — local `main` matches `origin/main`; lesson is FROZEN / READ-ONLY.

# Safety Boundary

No automatic scheduling at startup or mode transition. No Xbox, NetworkButton booleans, direct
Phoenix Tuner controls, LiveWindow actuator control, teleop, kinematics, PID, closed loop,
odometry, pose estimation, or configuration writes were added.
