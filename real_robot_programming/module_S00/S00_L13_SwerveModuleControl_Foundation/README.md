# Framework Version

2.1

# Lesson

S00_L13_SwerveModuleControl_Foundation

# Previous Lesson

S00_L12_SwerveOutputPipeline_Foundation

# Status

IN_PROGRESS

# Goal

Integrate the inherited `SwerveOutputPipeline` into `SwerveSubsystem`. The subsystem owns the four
final `SwerveModuleState` values in Front Left, Front Right, Back Left, Back Right order without
commanding hardware.

# Architecture Boundary

`SwerveSubsystem` remains the subsystem owner of mechanism state and continues to depend only on
vendor-neutral IO interfaces. After each periodic IO refresh, it passes the copied chassis intent
and the four current encoder angles to the existing stateless `SwerveOutputPipeline`. The pipeline
performs kinematics, per-module optimization, and wheel-speed desaturation in that order.

The subsystem stores four owned final states and exposes only defensive copies through
`getFinalModuleStates()`. No state is sent to IO. The Frozen Backbone, Interface Contract,
observation flow, RobotContainer, commands, telemetry, and IO contracts remain unchanged.

# Verification Scope

- Architecture Review: PASS.
- Implementation: PASS.
- Baseline build: PASS; inherited baseline result was supplied by the user.
- Focused tests: PASS - 8/8.
- Full build: PASS.
- Simulation, Glass, Driver Station, and Real Robot: NOT APPLICABLE; no runtime hardware actuation path was added.
- Documentation: PASS for the S00_L13 metadata and transition guide.
- Commit, Push, and Freeze: NOT TESTED by instruction.

# Focused Test Coverage

`SwerveSubsystemTest` verifies pipeline-derived FL/FR/BL/BR ownership, deterministic repeated
reads, defensive-copy isolation, observation refresh without actuation, safe stop delegation,
and null chassis-speed rejection. Focused verification passed 8/8.

# Out of Scope

Vendor APIs, motor output, IO changes, RobotContainer changes, commands, telemetry expansion,
odometry, pose estimation, gyro integration, PID, closed-loop control, a separate module
controller or dispatcher, and modifications to prior frozen lessons.
