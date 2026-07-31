# D01_L08 Feeder Complete Foundation to D01_L09 Shooter Complete Foundation

## Purpose

This guide records the inheritance transition from the completed and frozen
`D01_L08_Feeder_Complete_Foundation` lesson to
`D01_L09_Shooter_Complete_Foundation`.

The lesson introduces one concept: one command coordinates the existing
`FlywheelSubsystem` and `FeederSubsystem`.

## Architecture Decision

| Item | Decision |
| --- | --- |
| Reason | Coordinate the existing Flywheel and Feeder for one hold-to-shoot action |
| Scope | One command and RobotContainer wiring |
| Impact | Adds command coordination without changing either mechanism boundary |
| Decision | APPROVED |

The existing subsystems remain independent. Their IO contracts, Inputs snapshots, vendor
implementations, state ownership, safe-stop behavior, and telemetry remain unchanged. No
`ShooterSubsystem` or `ShooterIO` is created.

## Step 1 - Inherit the Frozen Lesson

### Objective

Create D01_L09 from the completed D01_L08 project.

### Why

The repository requires inheritance development and prohibits recreating a lesson from scratch.

### Action

Copy `D01_L08_Feeder_Complete_Foundation` to
`D01_L09_Shooter_Complete_Foundation`, excluding generated build state.

### Files Changed

All inherited project files were copied into the D01_L09 lesson directory.

### Verification

Compare inherited files against D01_L08 and run the baseline build.

### Expected Result

D01_L09 begins as a buildable copy of frozen D01_L08.

## Step 2 - Add ManualShootCommand

### Objective

Coordinate the existing Flywheel and Feeder mechanisms with one command.

### Why

Commands own action coordination, while subsystems retain mechanism behavior and state.

### Action

Create `commands/shooter/ManualShootCommand.java`. Inject `FlywheelSubsystem` and
`FeederSubsystem`, require both, command their supplied outputs in `execute()`, and stop both in
`end(boolean interrupted)`.

### Files Changed

- `src/main/java/frc/robot/commands/shooter/ManualShootCommand.java`

### Verification

Confirm `addRequirements()` includes both subsystems and `end()` calls both safe-stop methods.

### Expected Result

The command owns exclusive scheduler access to both mechanisms and safely stops both when it
ends.

## Step 3 - Wire the Coordinated Command

### Objective

Make the coordinated shoot action available as a hold control.

### Why

`RobotContainer` is the approved composition root for dependency injection and controller
bindings.

### Action

Bind the driver's Y button with `whileTrue()` to a `ManualShootCommand`. Inject the existing
Flywheel and Feeder subsystems and the existing configured outputs:

- Flywheel: `FlywheelConstants.kFlywheelTestOutput` (`+0.10`)
- Feeder: `FeederConstants.kManualFeedOutput` (`+0.20`)

Retain the inherited individual Feeder RB/LB binding.

### Files Changed

- `src/main/java/frc/robot/RobotContainer.java`

### Verification

Hold Y and observe both outputs. Release Y and confirm both mechanisms stop.

### Expected Result

Y provides hold-to-shoot coordination without moving mechanism logic into `RobotContainer`.

## Step 4 - Verify Safety and Inherited Controls

### Objective

Verify coordinated behavior and safe termination.

### Why

Both mechanisms must return to a safe state on every command termination path.

### Action

Verify the command in simulation, Glass, and on the real robot. Test release, disable, and
interruption. Recheck the individual Feeder right- and left-bumper controls.

### Files Changed

None.

### Verification

The user verified:

- Simulation PASS.
- Glass PASS.
- Real Robot PASS.
- Hold Y commands Flywheel `+0.10` and Feeder `+0.20`.
- Release, disable, and interruption stop both safely.
- Individual Feeder RB/LB controls still work.

### Expected Result

The coordinated and inherited controls behave safely with no mechanism ownership changes.

## Step 5 - Close and Freeze the Lesson

### Objective

Record verified results and freeze D01_L09.

### Why

A completed lesson must contain accurate status, lesson documentation, and a transition guide.

### Action

Update the lesson README and `LESSON_STATUS.md`, run a clean build, commit only the D01_L09
lesson, and push the commit.

### Files Changed

- `README.md`
- `LESSON_STATUS.md`
- `docs/D01_L08_Feeder_Complete_Foundation_to_D01_L09_Shooter_Complete_Foundation_Step_by_Step.md`

### Verification

Confirm the clean build succeeds, D01_L08 has no changes, only D01_L09 is staged, the commit
succeeds, and the push succeeds.

### Expected Result

D01_L09 is `COMPLETE` and `FROZEN`, with no next lesson created.

## Final Architecture

```text
Driver
-> Xbox Controller
-> ManualShootCommand
-> FlywheelSubsystem and FeederSubsystem
-> Existing FlywheelIO and FeederIO contracts
-> Existing hardware or simulation implementations
```

The Flywheel and Feeder remain separate mechanisms coordinated only at the command layer.
