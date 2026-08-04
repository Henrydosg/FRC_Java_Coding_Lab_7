# Framework Version

2.1

# Lesson

S00_L11_SwerveModuleStateOptimization_Foundation

# Status

COMPLETE / FROZEN / READ-ONLY

# Goal

Introduce a pure, vendor-neutral foundation for optimizing one desired WPILib `SwerveModuleState` against the module's current `Rotation2d`.

# Verification Scope

- Focused tests: PASS, 8/8.
- Build: PASS.
- Simulation, Glass, Driver Station, and Real Robot: NOT APPLICABLE; no runtime wiring or hardware-output path is added.
- Commit: PASS — `18d308b`.
- Push: PASS — pushed to `origin/main`.
- Freeze: PASS — lesson is frozen and read-only.

# Architecture Boundary

`SwerveModuleStateOptimizer` is a stateless, pure subsystem-owned helper. It delegates optimization to WPILib's supported instance API, `SwerveModuleState.optimize(Rotation2d)`, after copying the desired input so caller-owned mutable state is not modified.

# Out of Scope

FL/FR/BL/BR ordering changes, kinematics, field-relative driving, desaturation, cosine compensation, PID, motor control, IO, telemetry, commands, controls, autonomous behavior, odometry, pose estimation, vendor APIs, and hardware configuration.
