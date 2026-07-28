# D01_L03 Drive Observation Publishing

## Objective

Introduce one typed, read-only telemetry facade that publishes immutable
`DriveObservation` applied-output values without affecting drivetrain behavior.

## Previous Lesson

`D01_L02_Drive_Observation_Evaluation`

D01_L03 was inherited directly from the completed and frozen D01_L02 lesson. All 13 inherited
production Java files remain byte-identical.

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

Read-only publishing path:

```text
DriveIO
-> DriveIOInputs
-> DriveSubsystem
-> DriveObservation
-> DriveTelemetryFacade
-> typed NetworkTables publishers
```

`DriveTelemetryFacade` consumes only caller-supplied immutable observations. It does not own or
call the subsystem, IO, evaluator, RobotContainer, or global NetworkTables instance.

## Source Impact

Created:

- `src/main/java/frc/robot/telemetry/drive/DriveTelemetryFacade.java`

Modified inherited production Java:

- None

## Verification

- Production clean and build: PASS
- Isolated NetworkTables harness: PASS
- Harness cases: 15
- Runtime and reflection checks: 50
- Inherited Java comparison: PASS
- Driver Station / Glass: NOT TESTED
- Real Robot Verification: NOT TESTED

## Documentation

- [Transition guide](docs/D01_L02_Drive_Observation_Evaluation_to_D01_L03_Drive_Observation_Publishing_Step_by_Step.md)

## Status

`COMPLETE`
