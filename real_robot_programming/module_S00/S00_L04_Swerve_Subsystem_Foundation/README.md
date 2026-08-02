# Framework Version

2.1

# Lesson

S00_L04_Swerve_Subsystem_Foundation

# Goal

Establish the Swerve subsystem boundary over the verified S00_L03 IO contracts.

# Learning Objective

Learn subsystem ownership, interface-only dependency injection, periodic IO refresh, and safe stop delegation.

# WHY

The subsystem is the mechanism-state boundary between commands and hardware abstraction.

# Scope

- Own four module IO dependencies and one gyro IO dependency.
- Own and refresh their IOInputs snapshots.
- Provide the approved safe-stop delegation.

# Out of Scope

Observation, telemetry, commands, controls, RobotContainer wiring, simulation/Noop IO, kinematics, odometry, pose estimation, PID, calibration, inversion, and hardware configuration.

# Prerequisites

- S00_L03_CTRE_IO_Foundation is COMPLETE.
- S00_L03 `SwerveModuleIO` and `GyroIO` contracts are available.
- Read [AGENTS.md](../../../AGENTS.md), Document A, ES-06, Document B, Document C, and FAR before implementation.

# Governance References

- `AGENTS.md`
- `docs/Document_A/`
- `docs/Document_B/English/`
- `docs/Document_C/English/`
- `docs/Future_Architecture_Reference_EN.pdf`

# Changes from Previous Lesson

Adds the subsystem ownership boundary; preserves S00_L03 IO interfaces, CTRE implementations, constants, vendordeps, and lifecycle files.

# Next Lesson

UNRESOLVED - select after S00_L04 verification.
