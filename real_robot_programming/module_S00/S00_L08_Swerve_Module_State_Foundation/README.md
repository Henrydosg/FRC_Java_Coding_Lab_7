# Framework Version

2.1

# Lesson

S00_L08_Swerve_Module_State_Foundation

# Goal

Prepare the module-state boundary for Swerve without deciding module-state architecture before review.

# Learning Objective

Learn how raw module observations can be interpreted into a vendor-neutral module state while preserving IO ownership, immutable observations, and deferred drivetrain behavior.

# WHY

Module state is a foundational meaning boundary for later Swerve control and estimation, so its ownership, units, validity, and lifecycle must be reviewed before implementation.

# Scope

- Audit the inherited Swerve IOInputs and SwerveObservation contracts.
- Define the smallest module-state responsibility and dependency direction for architecture review.
- Preserve the existing runtime telemetry, Observation, and composition boundaries.

# Out of Scope

Module-state implementation before approval, kinematics, odometry, pose estimation, commands, controls, motor behavior, PID, calibration, offsets, inversions, gear-ratio conversion, hardware configuration, telemetry topic changes, RobotContainer wiring, Robot lifecycle changes, simulation physics, and vendor APIs.

# Prerequisites

- S00_L07_Runtime_Telemetry_Integration is the inherited source lesson.
- SwerveModuleIO, SwerveObservation, SwerveSubsystem, and runtime telemetry are available for review.
- Read AGENTS.md, Document A, ES-06, Document B, Document C, and FAR before implementation.

# Governance References

- AGENTS.md
- docs/Document_A/
- docs/Document_B/English/
- docs/Document_C/English/
- docs/Future_Architecture_Reference_EN.pdf

# Changes from Previous Lesson

Begins module-state review over the inherited IO and Observation boundaries while preserving runtime telemetry, RobotContainer composition, scheduler order, and lifecycle safety.

# Verification Checklist

- Baseline build recorded before implementation.
- Module-state architecture audited and formally approved.
- Units, validity, ownership, and dependency direction statically reviewed.
- Build result recorded after implementation.
- Simulation, Driver Station / Glass, and Real Robot applicability decided with rationale.
- Documentation, transition, commit, push, and freeze evidence recorded.

# Next Lesson

UNRESOLVED - select after S00_L08 verification.
