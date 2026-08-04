# Framework Version

2.1

# Lesson

S00_L09_ChassisSpeeds_Foundation

# Status

COMPLETE / FROZEN / READ-ONLY

# Goal

Audit the minimum runtime foundation for `edu.wpi.first.math.kinematics.ChassisSpeeds`: `SwerveSubsystem` accepts robot-relative chassis velocity intent without kinematics, module-state conversion, or hardware output.

# Verification

- Architecture Review: PASS
- Unit tests: PASS, 5/5 `SwerveSubsystem` tests
- Build: PASS, `BUILD SUCCESSFUL in 1m 11s`
- Simulation, Glass, Driver Station, and Real Robot: NOT APPLICABLE; this lesson adds no runtime consumer or hardware-output path.
- Commit, Push, and Freeze: PASS after final delivery

# Learning Objective

Learn how a subsystem can accept a mutable WPILib value object safely by copying its scalar intent into immutable internal state while preserving the Frozen Backbone.

# Scope

- Accept robot-relative chassis velocity intent at the `SwerveSubsystem` boundary.
- Copy `vxMetersPerSecond`, `vyMetersPerSecond`, and `omegaRadiansPerSecond` immediately.
- Define zero intent, `stop()`, and periodic semantics without actuator behavior.
- Verify that the caller's mutable `ChassisSpeeds` object is never retained.

# Out of Scope

Field-relative conversion, discretization, kinematics, module-state conversion, odometry, pose estimation, controls, commands, bindings, autonomous behavior, telemetry, IO, motor output, vendor APIs, and hardware configuration.

# Architecture Review

The candidate is a valid single architectural concept as an intent acceptance and storage boundary. Implementation is limited to the approved lock; runtime behavior remains intentionally inert.

# Prerequisites

- S00_L08_Swerve_Module_State_Foundation is COMPLETE and FROZEN.
- S00_L08 completion commit: `573c814`; documentation correction commit: `3efb143`.
- Existing Swerve IO, immutable observations, telemetry, composition root, and WPILib dependency are available.
