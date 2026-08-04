# Framework Version

2.1

# Lesson

S00_L12_SwerveOutputPipeline_Foundation

# Status

IN_PROGRESS

# Goal

Create one pure, vendor-neutral output pipeline that converts robot-relative `ChassisSpeeds` and four current module angles into optimized and desaturated `SwerveModuleState` values in Front Left, Front Right, Back Left, Back Right order.

# Architecture Boundary

`SwerveOutputPipeline` is a stateless subsystem-owned helper. It copies the caller-owned `ChassisSpeeds`, validates the four current angles, delegates conversion to `SwerveKinematics`, delegates direction optimization to `SwerveModuleStateOptimizer`, and delegates wheel-speed scaling to WPILib's supported `SwerveDriveKinematics.desaturateWheelSpeeds` API.

The pipeline has no hardware, IO, subsystem state, telemetry, commands, controls, RobotContainer, vendor, or motor-output dependency.

# Pipeline Order

The final pipeline order is:

1. Robot-relative `ChassisSpeeds` to module states through `SwerveKinematics`.
2. Per-module optimization against the matching current angle.
3. Wheel-speed desaturation through WPILib's supported API.

The returned state order remains Front Left, Front Right, Back Left, Back Right (FL/FR/BL/BR).

# Constant Decision

No legitimate maximum wheel-speed constant existed in the inherited S00_L11 source. The smallest configuration change is `Constants.SwerveConstants.kMaxWheelSpeedMetersPerSecond = 4.0`. This is a provisional software baseline, not a verified hardware capability, and requires later hardware validation.

# Verification Scope

- Architecture Review: PASS.
- Implementation: PASS.
- Focused tests: PASS - 13/13.
- Baseline build: PASS; inherited S00_L11 baseline status was supplied by the user.
- Full build: PASS.
- Simulation, Glass, Driver Station, and Real Robot: NOT APPLICABLE; no runtime wiring or hardware-output path was added.
- Documentation: PASS.
- Commit, Push, and Freeze: NOT TESTED; Git operations remain user-owned and untouched.

# Focused Test Coverage

The focused test class covers zero speeds, normal motion below the limit, proportional desaturation, the absolute speed limit, optimized reversal through desaturation, combined translation and rotation, deterministic module ordering, null inputs, wrong angle-array length, invalid explicit maximum speeds, and caller-input immutability. All 13 focused tests passed.

# Out of Scope

Field-relative driving, cosine compensation, PID, motor control, IO, telemetry, commands, controls, RobotContainer, autonomous behavior, odometry, pose estimation, gyro logic, hardware configuration, Gradle, vendordeps, and changes to frozen lessons.
