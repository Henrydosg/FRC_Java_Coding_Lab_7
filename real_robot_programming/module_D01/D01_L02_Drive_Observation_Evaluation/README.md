# D01_L02 Drive Observation Evaluation

## Objective

Introduce one pure, stateless evaluator that determines whether an immutable
`DriveObservation` represents a stopped drivetrain using a caller-provided tolerance.

## Previous Lesson

`D01_L01_Drive_Observation_Boundary`

This lesson was inherited directly from D01_L01. All 12 inherited production Java files remain
byte-identical.

## Frozen Architecture

```text
Driver
-> Xbox Controller
-> controls
-> commands
-> DriveSubsystem
-> DriveIO
-> Hardware or Simulation
```

Read-only evaluation path:

```text
DriveIO
-> DriveIOInputs
-> DriveSubsystem
-> DriveObservation
-> DriveObservationEvaluator
-> boolean
```

`DriveObservationEvaluator` has no mutable state and no dependency on the subsystem, IO,
hardware, telemetry, or publishing layers.

## Source Impact

Created:

- `src/main/java/frc/robot/observation/drive/DriveObservationEvaluator.java`

Modified production Java:

- None

## Verification

- Production build: PASS
- External simulation harness: PASS (25 checks)
- Side-effect verification: PASS
- Snapshot immutability: PASS
- Real Robot Verification: NOT TESTED

## Documentation

- [Lesson guide](docs/D01_L02_Drive_Observation_Evaluation_Guide.md)
- [Transition guide](docs/D01_L01_Drive_Observation_Boundary_to_D01_L02_Drive_Observation_Evaluation_Step_by_Step.md)

## Status

`IN_PROGRESS`
