# D01_L05 Intake Foundation

## Lesson Information

| Item | Value |
| --- | --- |
| Lesson | D01_L05_Intake_Foundation |
| Module | D01 |
| Previous Lesson | D01_L04_Robot_Telemetry_Runtime_Integration |
| Development Model | Feature Lesson |
| Status | COMPLETE |
| Freeze Status | FROZEN |

---

# Lesson Objective

Build a complete Intake feature that can be safely controlled and verified in both WPILib Simulation and on the real robot while preserving the frozen project architecture and all inherited drivetrain behavior.

---

# Previous Lesson

```
D01_L04_Robot_Telemetry_Runtime_Integration
```

D01_L05 inherits the completed D01_L04 project.

D01_L04 remains **COMPLETE**, **FROZEN**, and unchanged.

---

# Frozen Architecture

The inherited control flow remains unchanged.

```
Driver
→ Xbox Controller
→ controls
→ commands
→ subsystems
→ io
→ Hardware / Simulation
```

The inherited telemetry pipeline remains unchanged.

```
Subsystem
→ Observation
→ RobotTelemetry
→ TelemetryFacade
→ NetworkTables / Glass
```

RobotContainer remains the composition root.

Responsibilities are limited to:

- Object creation
- Implementation selection
- Dependency injection
- Default command configuration
- Controller bindings

RobotContainer must not contain:

- Business logic
- Hardware logic
- Input processing
- Telemetry calculations

---

# Target Intake Architecture

```
Driver
→ Xbox Controller
→ IntakeInputProcessor
→ ManualIntakeCommand
→ IntakeSubsystem
→ IntakeIO
    ├── IntakeIOTalonFX
    └── IntakeIOSim
→ Hardware / Simulation
```

---

# Confirmed Intake Hardware

| Item | Value |
| --- | --- |
| Motor Controller | CTRE TalonFX (Kraken X60) |
| CAN ID | 12 |
| Encoder | Integrated TalonFX Encoder |
| Output Range | -1.0 ~ 1.0 |

---

# Completed Work

## Intake IO

- IntakeIO
- IntakeIOInputs
- updateInputs()
- setOutput()
- stop()

## Real Hardware

- IntakeIOTalonFX
- TalonFX Configuration
- Integrated Encoder
- Applied Output
- Safe Stop

## Simulation

- IntakeIOSim
- Deterministic Simulation
- Applied Output Simulation
- Safe Stop

## Runtime Integration

- IntakeSubsystem
- IntakeInputProcessor
- ManualIntakeCommand
- Real / Simulation IO Selection
- RT Intake
- LT Outtake

## Telemetry

- IntakeObservation
- IntakeTelemetryFacade
- Applied Output
- Mode
- Connected
- NetworkTables
- Glass

## Constants

- CAN ID
- Output Constants
- Trigger Threshold
- NetworkTables Keys

---

# Verification

| Item | Result |
| --- | --- |
| Architecture Review | PASS |
| Implementation Review | PASS |
| IntakeIO Contract | PASS |
| IntakeIOTalonFX | PASS |
| IntakeIOSim | PASS |
| Runtime Integration | PASS |
| Glass Telemetry | PASS |
| WPILib Simulation | PASS |
| Real Robot | PASS |

---

# Remaining Work

None

---

# Files Added

```
src/main/java/frc/robot/io/intake/IntakeIO.java
src/main/java/frc/robot/io/intake/IntakeIOTalonFX.java
src/main/java/frc/robot/io/intake/IntakeIOSim.java
src/main/java/frc/robot/controls/IntakeInputProcessor.java
src/main/java/frc/robot/commands/intake/ManualIntakeCommand.java
src/main/java/frc/robot/telemetry/intake/IntakeObservation.java
src/main/java/frc/robot/telemetry/intake/IntakeTelemetryFacade.java
```

---

# Files Modified

```
src/main/java/frc/robot/Constants.java
src/main/java/frc/robot/RobotContainer.java
src/main/java/frc/robot/subsystems/IntakeSubsystem.java
src/main/java/frc/robot/telemetry/RobotTelemetry.java
```

---

# Safety Rules

- Use low default output.
- Keep outputs in Constants.
- Hold trigger to run.
- Release stops motor.
- Robot Disable stops motor.
- Command interruption stops motor.
- Verify motor direction before inversion.
- Keep hands away from the mechanism.

---

# Out of Scope

This lesson does not include:

- PID
- Feedforward
- Automatic game piece detection
- Autonomous intake
- Jam detection
- Shooter integration
- Loader integration

---

# Verification History

| Review | Result |
| --- | --- |
| D01_L05_A1 | PASS |
| D01_L05_I1 | PASS |
| D01_L05_V1 | FAIL |
| D01_L05_I2 | PASS |
| D01_L05_V2 | PASS |
| Feature Integration | PASS |
| Simulation Runtime | PASS |
| Glass Verification | PASS |
| Real Robot Test | PASS |

---

# Current Build

Command

```
.\gradlew.bat clean build --no-daemon
```

Result

```
BUILD SUCCESSFUL
```

---

# Completion Criteria

- Intake IO implemented.
- Runtime integration completed.
- Manual control completed.
- Simulation verified.
- Glass verified.
- Real robot verified.
- Documentation completed.
- Architecture preserved.

---

# Current Result

The Intake feature is fully integrated.

Verified on:

- WPILib Simulation
- Glass
- Real Robot

The lesson is complete and frozen.

---

# Lesson Status

```
LESSON
D01_L05_Intake_Foundation

STATUS
COMPLETE

SIMULATION
PASS

GLASS
PASS

REAL ROBOT
PASS

FREEZE STATUS
FROZEN
```