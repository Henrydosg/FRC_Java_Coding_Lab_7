# D01_L06 Intake Complete Foundation to D01_L07 Flywheel Complete Foundation

## Transition Summary

D01_L07 inherits the frozen D01_L06 Drivebase, Intake, simulation, and telemetry foundations. It
adds one complete Flywheel mechanism with manual Y-button control, vendor-independent IO,
Talon FX real hardware, deterministic simulation, immutable observation, and read-only
telemetry.

## Step 1 - Inherit and Activate D01_L07

### Objective

Create D01_L07 from the completed D01_L06 project.

### Why

Direct inheritance preserves the verified Drivebase and Intake behavior and prevents architectural
restart.

### Action

Copy D01_L06, rename the lesson to `D01_L07_Flywheel_Complete_Foundation`, preserve the inherited
source, and establish D01_L07 as the active lesson.

### Files Changed

- `README.md`
- `LESSON_STATUS.md`

### Verification

The inherited baseline and clean build are recorded as PASS.

### Expected Result

D01_L07 begins with the complete D01_L06 architecture and behavior.

## Step 2 - Freeze the Flywheel Architecture and Hardware

### Objective

Define the smallest complete Flywheel foundation without changing package responsibilities.

### Why

The Frozen Backbone requires driver input, commands, mechanism behavior, hardware abstraction, and
telemetry to remain separated.

### Action

Approve these paths:

```text
CONTROL
Driver
-> Xbox Controller
-> FlywheelInputProcessor
-> ManualFlywheelCommand
-> FlywheelSubsystem
-> FlywheelIO
-> FlywheelIOTalonFX or FlywheelIOSim

OBSERVATION
FlywheelIO
-> FlywheelIOInputs
-> FlywheelSubsystem
-> FlywheelObservation
-> RobotTelemetry
-> FlywheelTelemetryFacade
-> NetworkTables / Glass
```

Record one Kraken X60, one Talon FX, CAN ID 9, and CAN bus `rio`.

### Files Changed

- `README.md`
- `LESSON_STATUS.md`

### Verification

The architecture review, composition-root boundary, dependency direction, and hardware map are
recorded as PASS.

### Expected Result

The Flywheel implementation has explicit boundaries and confirmed hardware authority.

## Step 3 - Add the Vendor-Independent Flywheel Foundation

### Objective

Create the subsystem and interchangeable IO boundary.

### Why

Subsystems must depend on mechanism operations and observations, not vendor hardware types.

### Action

Create `FlywheelIO` and its inputs snapshot, `FlywheelIOSim`, `FlywheelIONoop`, and
`FlywheelSubsystem`. Support open-loop output and safe stop only.

### Files Changed

- `src/main/java/frc/robot/io/flywheel/FlywheelIO.java`
- `src/main/java/frc/robot/io/flywheel/FlywheelIOSim.java`
- `src/main/java/frc/robot/io/flywheel/FlywheelIONoop.java`
- `src/main/java/frc/robot/subsystems/FlywheelSubsystem.java`

### Verification

The IO contract is vendor-independent, simulation values are deterministic, and stop behavior is
safe.

### Expected Result

The subsystem can use real, simulation, or no-op IO without changing its dependency.

## Step 4 - Add Talon FX Real IO

### Objective

Connect the Flywheel abstraction to the confirmed real hardware.

### Why

Phoenix 6 creation, configuration, control, and status signals belong only in the real IO
implementation.

### Action

Create `FlywheelIOTalonFX` for Talon FX CAN ID 9 on bus `rio`. Use `DutyCycleOut` and apply:

- `CounterClockwise_Positive`
- Coast neutral mode
- 25 A supply-current limit
- 40 A stator-current limit
- 1.0 s open-loop ramp
- 0.0 reverse peak
- +0.20 forward peak

Apply and check the configuration inside the real IO class.

### Files Changed

- `src/main/java/frc/robot/Constants.java`
- `src/main/java/frc/robot/io/flywheel/FlywheelIOTalonFX.java`

### Verification

Real hardware uses the confirmed device address and safety configuration. Phoenix 6 types do not
escape the IO package.

### Expected Result

The real Flywheel starts stopped and accepts only the approved forward duty-cycle range.

## Step 5 - Add Manual Y-Button Control

### Objective

Provide safe hold-to-run driver control.

### Why

Input processing belongs in controls, subsystem coordination belongs in commands, and bindings
belong in the composition root.

### Action

Create `FlywheelInputProcessor` and `ManualFlywheelCommand`. Bind Xbox Y with hold-to-run behavior.
Holding Y commands `+0.10`; release, interruption, and robot disable command zero.

### Files Changed

- `src/main/java/frc/robot/controls/FlywheelInputProcessor.java`
- `src/main/java/frc/robot/commands/flywheel/ManualFlywheelCommand.java`
- `src/main/java/frc/robot/Constants.java`
- `src/main/java/frc/robot/RobotContainer.java`

### Verification

Y hold, Y release, command interruption, and disabled stop behavior are recorded as PASS.

### Expected Result

The driver can run the Flywheel only while Y remains held.

## Step 6 - Add Read-Only Flywheel Telemetry

### Objective

Publish a stable immutable Flywheel observation.

### Why

Hardware observations must cross the IO snapshot and immutable observation boundary before
telemetry publishing.

### Action

Create `FlywheelObservation` and `FlywheelTelemetryFacade`. Extend `FlywheelIOInputs` and real,
simulation, and no-op implementations with equivalent values. Coordinate publication through
`RobotTelemetry` under `/Flywheel`.

Publish:

```text
/Flywheel/AppliedOutput
/Flywheel/VelocityRpm
/Flywheel/SupplyCurrentAmps
/Flywheel/StatorCurrentAmps
/Flywheel/TemperatureCelsius
/Flywheel/Connected
/Flywheel/ConfigurationHealthy
/Flywheel/Mode
```

### Files Changed

- `src/main/java/frc/robot/io/flywheel/FlywheelIO.java`
- `src/main/java/frc/robot/io/flywheel/FlywheelIOTalonFX.java`
- `src/main/java/frc/robot/io/flywheel/FlywheelIOSim.java`
- `src/main/java/frc/robot/io/flywheel/FlywheelIONoop.java`
- `src/main/java/frc/robot/observation/flywheel/FlywheelObservation.java`
- `src/main/java/frc/robot/subsystems/FlywheelSubsystem.java`
- `src/main/java/frc/robot/telemetry/flywheel/FlywheelTelemetryFacade.java`
- `src/main/java/frc/robot/telemetry/RobotTelemetry.java`
- `src/main/java/frc/robot/Constants.java`
- `src/main/java/frc/robot/RobotContainer.java`

### Verification

Flywheel telemetry and Glass publishing are recorded as PASS. Telemetry remains read-only.

### Expected Result

Real and simulation implementations publish equivalent fields without control side effects.

## Step 7 - Verify Inherited Behavior

### Objective

Confirm that the new mechanism does not regress the frozen baseline.

### Why

Inheritance Development requires every lesson to preserve completed behavior.

### Action

Verify simulation startup, manual Flywheel behavior, telemetry, real hardware, Drivebase behavior,
and Intake behavior.

### Files Changed

- No production files

### Verification

- WPILib Simulation: PASS
- Driver Station / Glass: PASS
- Real Robot: PASS
- Drivebase regression: PASS
- Intake regression: PASS

### Expected Result

Flywheel functionality coexists with the inherited Drivebase and Intake foundations.

## Step 8 - Complete and Freeze D01_L07

### Objective

Record the verified implementation as a frozen lesson snapshot.

### Why

Completed lessons require accurate verification records and a transition guide.

### Action

Update README and `LESSON_STATUS.md`, create this transition guide, record the final verification
results, and mark D01_L07 `COMPLETE` and `FROZEN`.

### Files Changed

- `README.md`
- `LESSON_STATUS.md`
- `docs/D01_L06_Intake_Complete_Foundation_to_D01_L07_Flywheel_Complete_Foundation_Step_by_Step.md`

### Verification

- Build: PASS
- Simulation: PASS
- Driver Station / Glass: PASS
- Real Robot: PASS
- Y-button behavior: PASS
- Flywheel telemetry: PASS
- Drivebase regression: PASS
- Intake regression: PASS

### Expected Result

`D01_L07_Flywheel_Complete_Foundation` is `COMPLETE` and `FROZEN`.

## Final Architecture

```text
CONTROL
Driver
-> Xbox Controller
-> FlywheelInputProcessor
-> ManualFlywheelCommand
-> FlywheelSubsystem
-> FlywheelIO
-> FlywheelIOTalonFX or FlywheelIOSim
-> Hardware or Simulation

OBSERVATION
FlywheelIO
-> FlywheelIOInputs
-> FlywheelSubsystem
-> FlywheelObservation
-> RobotTelemetry
-> FlywheelTelemetryFacade
-> NetworkTables / Glass
```

D01_L07 adds no Feeder, automatic Flywheel behavior, closed-loop velocity control, PID,
feedforward, or unrelated architecture.
