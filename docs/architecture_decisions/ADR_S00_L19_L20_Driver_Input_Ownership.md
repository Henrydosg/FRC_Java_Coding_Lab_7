# ADR: S00_L19 / S00_L20 Driver-Input Ownership

- Status: APPROVED
- Date: 2026-08-08
- Scope: `S00_L19_DriverInputProcessing` and S00_L20 Robot-Relative Teleop Integration
- Authority: Referenced by `AGENTS.md` Section 14. The repository authority order remains unchanged.

## Context

S00_L19 introduces external Xbox acquisition, semantic axis mapping, deterministic driver-input
processing, an immutable `DriverInputObservation`, and read-only telemetry. The lesson is strictly
non-actuating.

External human/operator input is semantically different from mechanism hardware observation.
`AGENTS.md` therefore permits controls to produce an immutable, vendor-neutral Observation from one
coherent external human/operator input sample. This exception does not permit controls to produce
mechanism Observations. Mechanism Observations continue to follow:

```text
hardware
-> IOInputs
-> subsystem / estimator
-> immutable Observation
-> telemetry
```

S00_L20 is the Robot-Relative Teleop Integration lesson. Before it may actuate Swerve, driver-input
sampling ownership must move from the L19-only telemetry pull arrangement to one authoritative
sample per control cycle.

## Decision

### Decision A - S00_L19

S00_L19 is approved as a non-actuating observable driver-input pipeline:

```text
Xbox acquisition
-> semantic mapping
-> processing
-> immutable DriverInputObservation
-> read-only telemetry
```

For S00_L19 only, `RobotTelemetry` may synchronously call `XboxDriverInputSource.read()`.

S00_L19 includes no:

- command or default drive command;
- `ChassisSpeeds` conversion;
- `SwerveSubsystem` request;
- module-state generation; or
- drivetrain actuation.

Disabled, non-actuating real-roboRIO verification is required. The robot shall remain Disabled for
this verification. Drivetrain actuation verification is NOT APPLICABLE to S00_L19.

### Decision B - S00_L20 Migration

Before any driver input may actuate Swerve in S00_L20, the lesson shall establish exactly one
authoritative driver-input sample per control cycle.

- Telemetry and drive control must not independently poll Xbox.
- Telemetry shall publish the same authoritative immutable sample or a documented immutable
  projection of that sample.
- S00_L20 remains Robot-Relative Teleop Integration.
- This decision does not change the Frozen Backbone or the S00_L15-S00_L24 roadmap.

## Rationale

The L19 pull composition supports observation and verification without creating a control or
actuation path. Approving it only for L19 avoids unnecessary source churn in a non-actuating lesson.

Once driver input can command Swerve in L20, independent telemetry and control polling could create
two different samples within one control cycle. A single authoritative sample preserves coherent
control intent, deterministic telemetry, and reviewable dependency ownership.

## Constraints

- The Frozen Backbone remains unchanged.
- The mechanism Observation flow remains unchanged.
- The external input exception applies only to human/operator input.
- Controls may not produce mechanism Observations.
- `DriverInputObservation` remains immutable and vendor-neutral.
- Telemetry remains read-only and may not schedule commands or control behavior.
- The L19 synchronous pull approval may not be generalized to another lesson without review.
- S00_L20 may not actuate Swerve until its single-sample ownership design passes architecture review.
- This ADR does not rename, reorder, add, remove, or rescope roadmap lessons.

## Consequences

- L19 may retain its verified non-actuating acquisition, processing, Observation, and telemetry path.
- L19 must complete Disabled real-roboRIO verification before closure.
- L19 requires no drivetrain actuation verification.
- L20 must replace the L19-only sampling ownership before drivetrain actuation.
- Telemetry in L20 observes the same sample used by drive control, or a documented immutable
  projection of it.
- No permission is created for telemetry to control behavior or for controls to create mechanism
  Observations.

## L19 Boundary

L19 ends at an immutable, observable processed driver-input sample. It does not create robot motion,
request subsystem behavior, generate module states, or write drivetrain outputs.

## L20 Migration Requirement

L20 shall identify one owner that acquires and processes the driver-input sample once per control
cycle. Every drive-control and telemetry consumer shall use that authoritative immutable value or an
explicitly documented immutable projection. Independent Xbox polling by telemetry and drive control
is forbidden.

## Verification Implications

### S00_L19

- Focused processing, source, and telemetry tests must pass.
- Required regression and clean-build verification must pass.
- Simulation and Glass / AdvantageScope must show the driver-input topics updating.
- Non-actuating real-roboRIO verification must pass while the robot remains Disabled.
- Drivetrain actuation verification is NOT APPLICABLE.

### S00_L20

- Architecture review must identify the authoritative sample owner before implementation proceeds
  to actuation.
- Tests must prove telemetry and drive control do not independently poll Xbox.
- Verification must show telemetry represents the same control-cycle sample used by drive control,
  or its documented immutable projection.
- Normal Simulation, Driver Station, and required real-robot actuation safety gates remain in force.
