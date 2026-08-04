# Framework Version

2.1

# Lesson

S00_L10_SwerveDriveKinematics_Foundation

# Status

COMPLETE / FROZEN / READ-ONLY

# Goal

Introduce the WPILib `SwerveDriveKinematics` foundation that converts robot-relative `ChassisSpeeds` into four ordered `SwerveModuleState` values.

# Verification Scope

- Focused tests: PASS, 7/7.
- Build: PASS.
- Simulation, Glass, Driver Station, and Real Robot: NOT APPLICABLE; no runtime wiring or hardware-output path is added.
- Commit: PASS.
- Push: PASS.
- Freeze: PASS; lesson is frozen and read-only.

Focused coverage includes zero chassis speeds, robot-forward translation, robot-left translation,
counterclockwise rotation, combined translation and rotation, deterministic FL/FR/BL/BR ordering,
and null input rejection.

# Architecture Boundary

`SwerveKinematics` is a pure subsystem-owned, vendor-neutral conversion helper. It derives four module locations from `Constants.java`, constructs one `SwerveDriveKinematics`, and returns states in Front Left, Front Right, Back Left, Back Right order.

# Out of Scope

Field-relative driving, discretization, desaturation, optimization, odometry, pose estimation, closed-loop control, motor output, telemetry, IO changes, RobotContainer changes, commands, controls, autonomous behavior, vendor APIs, and hardware configuration.
