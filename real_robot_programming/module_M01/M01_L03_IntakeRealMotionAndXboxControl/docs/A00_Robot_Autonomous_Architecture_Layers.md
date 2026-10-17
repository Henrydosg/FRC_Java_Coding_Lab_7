# A00 Robot Autonomous Architecture Layers

## Purpose

This document explains the reusable architecture underneath autonomous
behavior. It is a learning abstraction, not a request to add Mecanum or Tank
production classes to this repository.

The generic principle is:

```text
Autonomous Command
        ->
Drivetrain Subsystem
        ->
Drive IO
        ->
Physical drivetrain mechanisms
```

Autonomous behavior does not inherently require Swerve. Swerve, Mecanum, and
Tank drivetrains can use the same Command -> Subsystem -> IO -> Hardware
dependency direction. `drivetrain` is the generic concept; names such as
`SwerveSubsystem` are implementation-specific.

## Side-by-Side Learning Abstraction

```text
Swerve:
Autonomous Command
        ->
SwerveSubsystem
        ->
Swerve IO
        ->
4 Swerve Modules

Mecanum:
Autonomous Command
        ->
MecanumDriveSubsystem
        ->
Drive IO
        ->
4 Mecanum Wheels

Tank:
Autonomous Command
        ->
DriveSubsystem
        ->
Drive IO
        ->
Left / Right Motors
```

## Four-Layer Responsibility Model

| Layer | Responsibility | Belongs here | Must not belong here |
|---|---|---|---|
| Autonomous Command | Coordinate one bounded robot action and its command lifecycle | Requirements, initialize/execute/end behavior, completion and interruption policy, calls to a subsystem API | Vendor APIs, direct IO access, motor/module details, telemetry publication, duplicated subsystem validation |
| Drivetrain Subsystem | Own mechanism behavior and the safe actuation boundary | Drivetrain intent acceptance, kinematics, mechanism coordination, localization use where applicable, centralized `stop()` | Controller sampling, command scheduling policy, direct vendor hardware access, NetworkTables publishing |
| Drive IO | Hide hardware/simulation implementation behind a stable contract | Inputs snapshots, actuator requests, health, safe stop, real/simulation implementations | Autonomous decisions, field strategy, command lifecycle, mutable observations, dashboard logic |
| Physical mechanisms | Produce and measure physical motion | Motors, modules, wheels, encoders, and vendor device behavior behind IO | Autonomous sequencing, subsystem policy, controller input, telemetry/business logic |

### Dependency Direction

Dependencies point downward from coordination to implementation:

```text
Autonomous Command
    -> Drivetrain Subsystem
        -> Drive IO
            -> Physical mechanisms / hardware
```

The lower layers do not call upward into commands. This separation makes a
command testable with a subsystem test double, makes a subsystem testable with
IO fakes or simulation, and permits a drivetrain implementation to be
replaced without rewriting autonomous lifecycle code.

## Drivetrain Examples

| Drivetrain | Command layer | Subsystem layer | IO layer | Physical mechanisms |
|---|---|---|---|---|
| Swerve | Autonomous Command | `SwerveSubsystem` | Swerve IO | Four independently steered and driven Swerve Modules |
| Mecanum | Autonomous Command | `MecanumDriveSubsystem` | Drive IO | Four Mecanum Wheels |
| Tank | Autonomous Command | `DriveSubsystem` | Drive IO | Left / Right Motors |

The Mecanum and Tank names above describe architectural roles only. This
repository does not invent or add their production classes.

## Current Robot Mapping

The current A00_L01 implementation is Swerve-specific:

```text
AutonomousSafetyHoldCommand
        ->
SwerveSubsystem
        ->
SwerveModuleIO / GyroIO
        ->
CTRE hardware / four Swerve modules
```

`AutonomousSafetyHoldCommand` owns only its bounded command lifecycle. It
requires `SwerveSubsystem`, calls the subsystem's centralized `stop()` at
initialization and termination, and issues no drivetrain request from
`execute()`. `SwerveSubsystem` remains responsible for drivetrain behavior and
stop ownership. `SwerveModuleIO` and `GyroIO` provide the vendor-neutral
boundaries; the real implementations contain the CTRE hardware APIs.

This mapping is an instance of the generic model, not a claim that the generic
model is a complete description of the repository.

## Repository Roles Outside the Four Layers

The four-layer diagram intentionally omits several separate architectural
roles:

- `Robot` owns WPILib robot lifecycle callbacks and scheduler/mode lifecycle.
- `RobotContainer` is the composition root. It creates objects, injects
  dependencies, selects implementations, installs defaults, and binds
  controls. It does not contain drivetrain business logic or telemetry
  calculations.
- Controls acquire and process driver input. They do not own mechanism state
  or autonomous behavior.
- Observation models are immutable, vendor-neutral read models produced from
  mechanism state.
- Telemetry consumes observations and publishes them read-only to NT4/Glass,
  logs, or other approved outputs.

The repository Frozen Backbone therefore remains:

```text
Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware
```

The observation flow remains separate:

```text
hardware -> IOInputs -> subsystem / estimator
         -> immutable Observation -> telemetry -> NT4 / Glass / log
```

Vendor APIs remain confined to real IO implementations. Neither autonomous
commands nor RobotContainer bypass the IO boundary.

## Why the Separation Matters

The same lifecycle command can coordinate different drivetrain subsystems
because it depends on a subsystem contract rather than motor vendor details.
A subsystem can be tested against fake IO without a robot or CTRE device.
Hardware, simulation, and future drivetrain replacements can implement the IO
contract independently. Stop behavior remains centralized, so interruption,
completion, cancellation, and mode-safety rules do not need to be duplicated
inside every motor or module.

For A00_L01, this abstraction supports one precise lesson: establish
autonomous command lifecycle and stop ownership while preserving the
zero-motion boundary. A00_L02 remains zero-motion. A00_L03 is the first A00
lesson permitted to issue a nonzero autonomous drivetrain request.
