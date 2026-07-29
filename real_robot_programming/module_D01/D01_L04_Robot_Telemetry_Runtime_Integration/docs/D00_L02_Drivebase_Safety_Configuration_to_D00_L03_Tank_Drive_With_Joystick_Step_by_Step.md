# D00_L02 to D00_L03 Transition Guide

## 1. Lesson Identity

- Previous lesson: `D00_L02_Drivebase_Safety_Configuration`
- Current lesson: `D00_L03_Tank_Drive_With_Joystick`
- Current status: `IN_PROGRESS`
- New concept: continuous tank-drive control from two joystick Y axes

## 2. Starting Architecture

D00_L02 already provided:

- `CommandXboxController` button bindings for fixed-output drive tests.
- `DriveInputProcessor` as the driver-input processing boundary.
- `DriveSubsystem` with `tankDrive(...)` and `stop()`.
- Vendor-independent `DriveIO` with `DriveIOInputs`.
- `DriveIOSparkMax` for leader/follower motor control.
- `RobotContainer` as the composition root.

## 3. Lesson Objective

Add a default command that continuously reads the driver's left and right joystick Y axes, processes each value through `DriveInputProcessor`, and commands independent left and right drivetrain outputs without changing the frozen package responsibilities.

## 4. Files Added

- `src/main/java/frc/robot/commands/drive/DefaultDriveCommand.java`
  - Injects `DriveSubsystem`, `DriveInputProcessor`, and two `DoubleSupplier` inputs.
  - Declares the drivetrain requirement.
  - Processes both joystick inputs and calls `DriveSubsystem.tankDrive(...)`.
  - Calls `DriveSubsystem.stop()` when the command ends.

## 5. Files Modified

- `src/main/java/frc/robot/RobotContainer.java`
  - Constructs `DefaultDriveCommand`.
  - Injects `driverController::getLeftY` and `driverController::getRightY`.
  - Registers the command as the drivetrain default command.
  - Retains composition-only responsibility.
- `LESSON_STATUS.md`
  - Records the lesson as `IN_PROGRESS`.
  - Records verified and incomplete work without marking the lesson complete.

Temporary console logging used during verification was removed from all Java files.

## 6. Step-by-Step Implementation Summary

1. Set the D00_L03 lesson state to `IN_PROGRESS`.
2. Add `DefaultDriveCommand` with constructor-injected subsystem, processor, and joystick suppliers.
3. Read each supplier once per `execute()` cycle.
4. Process both raw inputs through `DriveInputProcessor`.
5. Send the processed pair to `DriveSubsystem.tankDrive(...)`.
6. Stop the drivetrain from `end(...)`.
7. Construct and register the default command in `RobotContainer`.
8. Verify the command, subsystem, and IO boundaries with temporary console logging.
9. Remove all temporary console logging and rerun clean builds.

## 7. Final Control Flow

```text
Keyboard
→ HAL Simulation
→ CommandXboxController
→ DefaultDriveCommand
→ DriveSubsystem
→ DriveIO
```

`DriveInputProcessor` is injected into `DefaultDriveCommand` and processes each joystick value before the subsystem call.

## 8. Simulation Verification

Status: `PASS`

HAL Simulation was run in Teleop mode. Keyboard-controlled left-Y and right-Y values changed from `0.0` through intermediate values to `1.0`, then returned to `0.0`. Temporary logs confirmed the changing pair at:

- `DefaultDriveCommand`
- `DriveSubsystem.tankDrive(...)`
- `DriveIOSparkMax.setTankOutputs(...)`

The temporary logs were removed after verification.

## 9. Build Verification

Status: `PASS`

The original D00_L03 project completed:

```powershell
.\gradlew.bat clean build --no-daemon
```

Result: `BUILD SUCCESSFUL`

## 10. Real Robot Status

Status: `NOT TESTED`

No real robot behavior, motor direction, follower behavior, current limiting, voltage compensation, brake mode, or open-loop ramping was verified.

## 11. Known Limitations

- Desktop HAL Simulation instantiates `DriveIOSparkMax`; no dedicated `DriveIOSim` exists in this lesson.
- Simulation does not validate physical SPARK MAX firmware, CAN communication, wiring, or drivetrain motion.
- `DriveInputProcessor` currently preserves the signed input without deadband, scaling, inversion, or slew-rate limiting.
- Driver Station and Glass verification is not complete.
- Real robot testing, documentation status update, Git commit, and Git push remain incomplete.

## 12. Completion Checklist

- [x] Frozen architecture preserved
- [x] `DefaultDriveCommand` added
- [x] Default drivetrain command registered
- [x] HAL Simulation keyboard control verified
- [x] Temporary console logging removed
- [x] Clean build passed
- [x] Transition guide created
- [ ] Driver Station / Glass verified
- [ ] Real robot tested
- [ ] Lesson marked `COMPLETE`
- [ ] Git commit completed
- [ ] Git push completed
