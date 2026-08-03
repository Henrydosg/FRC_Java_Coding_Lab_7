# Framework Version

2.1

# Lesson

S00_L06_Telemetry_Foundation

# Goal

Prepare the read-only telemetry boundary for immutable Swerve observations without deciding telemetry architecture before review.

# Learning Objective

Learn how telemetry may consume immutable vendor-neutral Observations while remaining separate from hardware access, mechanism behavior, and control flow.

# WHY

Telemetry makes robot state observable for operators, diagnostics, and logs, but must never become a second control path or publish mutable IOInputs directly.

# Scope

- Audit the governance rules and inherited SwerveObservation contract relevant to telemetry.
- Define the smallest telemetry responsibility and its dependency direction for architecture review.
- Preserve immutable Observation meaning and defer implementation choices until approval.

# Out of Scope

Telemetry implementation before approval, NetworkTables topic names, Glass layout, logging destinations, rates, serialization policy, hardware access, vendor APIs, IO changes, subsystem behavior, RobotContainer wiring, commands, controls, simulation/Noop, kinematics, odometry, pose estimation, and control behavior.

# Prerequisites

- S00_L05_Observation_Foundation is the inherited source lesson.
- S00_L05 SwerveObservation and SwerveSubsystem observation accessor are available for review.
- Read AGENTS.md, Document A, ES-06, Document B, Document C, and FAR before implementation.

# Governance References

- AGENTS.md
- docs/Document_A/
- docs/Document_B/English/
- docs/Document_C/English/
- docs/Future_Architecture_Reference_EN.pdf

# Changes from Previous Lesson

Begins telemetry-boundary review over immutable SwerveObservation values while preserving the inherited IO, subsystem, Observation, lifecycle, and composition-root contracts.

# Verification Checklist

- Baseline build recorded before implementation.
- Telemetry architecture audited and formally approved.
- Dependency direction and immutable Observation consumption statically reviewed.
- Build result recorded after implementation.
- Simulation, Driver Station / Glass, and Real Robot applicability decided with rationale.
- Documentation, transition, commit, push, and freeze evidence recorded.

# Next Lesson

UNRESOLVED - select after S00_L06 verification.
