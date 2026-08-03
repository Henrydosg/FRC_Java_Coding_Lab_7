# Framework Version

2.1

# Lesson

S00_L05_Observation_Foundation

# Goal

Establish the immutable, vendor-neutral observation boundary for the Swerve subsystem.

# Learning Objective

Learn how a subsystem converts copied IOInputs data into one coherent immutable Observation without exposing hardware, vendor APIs, mutable state, or telemetry destinations.

# WHY

Observation is the stable read-model boundary between mechanism state and read-only telemetry. It preserves hardware abstraction while giving later lessons a meaningful, testable robot state model.

# Scope

- Define the minimal Swerve observation responsibility under frc.robot.observation.
- Select and document vendor-neutral fields, units, validity, and sample-time semantics from approved S00_L04 inputs.
- Have SwerveSubsystem produce or expose the immutable observation only after architecture approval.

# Out of Scope

Hardware access, vendor APIs, IO contract changes, commands, controls, RobotContainer wiring, telemetry publishers, NetworkTables, Glass, logging destinations, kinematics, odometry, pose estimation, control behavior, PID, calibration, inversion, configuration, simulation/Noop implementations, and unrelated mechanism observations.

# Prerequisites

- S00_L04_Swerve_Subsystem_Foundation must be reviewed for completion and freeze status.
- S00_L04 SwerveModuleIO, GyroIO, and SwerveSubsystem contracts are available.
- Read AGENTS.md, Document A, ES-06, Document B, Document C, and FAR before implementation.

# Governance References

- AGENTS.md
- docs/Document_A/
- docs/Document_B/English/
- docs/Document_C/English/
- docs/Future_Architecture_Reference_EN.pdf

# Changes from Previous Lesson

Adds the immutable observation boundary while preserving the inherited IO contracts, subsystem lifecycle, vendor isolation, Constants authority, and composition-root rules.

# Verification Checklist

- Baseline build recorded before implementation.
- Observation architecture review approved.
- Immutable model and subsystem integration statically reviewed.
- Build result recorded after implementation.
- Simulation, Driver Station / Glass, and Real Robot applicability decided with rationale.
- Documentation, transition, commit, push, and freeze evidence recorded.

# Next Lesson

UNRESOLVED - select after S00_L05 verification.
