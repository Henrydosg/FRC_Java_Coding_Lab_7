# Imported Baseline to D00_L01 - Step-by-Step Transition Guide

## 1. Lesson Summary

- Previous lesson: Imported competition baseline
- Current lesson: D00_L01 - Competition Robot Foundation
- Lesson objective: Restore the frozen command-based competition backbone without changing drivetrain hardware behavior.
- Final verified status: BUILD_PASSED
- Build result: PASS
- Simulation result: NOT TESTED
- Driver Station / Glass result: NOT TESTED
- Real robot result: NOT TESTED

This lesson introduces one architectural concept: restoring the complete frozen control pipeline while retaining the imported drivetrain configuration and A/B test behavior.

## 2. Starting Architecture

```text
Driver
-> Xbox Controller
-> RobotContainer drivetrain methods
-> DriveSubsystem
-> DriveIO
-> DriveIOSparkMax
-> hardware
```

The imported baseline used `Commands.startEnd(...)` bindings in `RobotContainer`. The container also owned `driveForward()`, `driveReverse()`, and detailed test-printing logic. This bypassed the required `controls` and dedicated `commands` layers.

The initial IO abstraction provided output methods but no `DriveIOInputs` snapshot or `updateInputs(...)` contract.

## 3. Target Architecture

```text
Driver
-> CommandXboxController
-> DriveInputProcessor
-> DriveTestCommand
-> DriveSubsystem
-> DriveIO
-> DriveIOSparkMax
-> hardware
```

The restored design follows the frozen dependency direction:

- `controls` converts a signed driver request into processed drive intent.
- `commands` coordinates the held-button drive action.
- `subsystems` owns drivetrain behavior.
- `io` defines the vendor-independent hardware contract.
- `DriveIOSparkMax` owns REV hardware access and configuration.
- `RobotContainer` remains the composition root and binding location.

## 4. Files Created

| File | Responsibility | Why Required |
|---|---|---|
| `src/main/java/frc/robot/controls/DriveInputProcessor.java` | Preserves the signed test request as processed driver intent. | Restores the mandatory `controls` stage without adding deadband or scaling that would change behavior. |
| `src/main/java/frc/robot/commands/drive/DriveTestCommand.java` | Runs both drivetrain sides at one signed output until interrupted, then stops. | Moves drivetrain action coordination out of `RobotContainer` and declares the subsystem requirement. |

## 5. Files Modified

| File | Previous Responsibility | Change | Why Required |
|---|---|---|---|
| `src/main/java/frc/robot/Constants.java` | Stored controller, drivetrain hardware, and test constants. | Added named minimum and maximum drive-output bounds and normalized touched-file comments to concise English. All existing configuration values were retained. | Removes output-bound magic numbers while keeping `Constants.java` as the configuration authority. |
| `src/main/java/frc/robot/RobotContainer.java` | Constructed components, configured bindings, contained drivetrain behavior methods, and printed test instructions. | Constructs `DriveInputProcessor`, `DriveIO`, and `DriveSubsystem`; binds A/B to `DriveTestCommand`; removes drive behavior methods and detailed printing; preserves `getAutonomousCommand()`. | Restores the composition-root boundary. |
| `src/main/java/frc/robot/io/drive/DriveIO.java` | Defined default output and stop methods. | Added `DriveIOInputs`, added `updateInputs(...)`, and made required output and stop methods explicit. | Implements the frozen per-mechanism IO snapshot contract and prevents silent no-op hardware behavior. |
| `src/main/java/frc/robot/io/drive/DriveIOSparkMax.java` | Configured and controlled the four SPARK MAX controllers. | Implements `updateInputs(...)` using leader applied outputs and normalizes touched-file comments. Hardware construction, configuration, output, and stop behavior remain unchanged. | Satisfies the revised IO contract without inventing sensors. |
| `src/main/java/frc/robot/subsystems/DriveSubsystem.java` | Owned high-level `tankDrive(...)` and `stop()` behavior. | Owns one `DriveIOInputs` snapshot, refreshes it in `periodic()`, and uses named output bounds. | Completes the Hardware-to-IO-to-Inputs-to-Subsystem observation path. |

## 6. Step-by-Step Implementation

### Step 1 - Add the controls layer

**Objective**

Represent the signed drive request as processed driver intent.

**Why**

The frozen backbone requires controller input to pass through `controls` before reaching a command.

**Action**

Created `DriveInputProcessor` with a focused `process(...)` method. It returns the signed test request unchanged because this lesson requires no deadband or scaling.

**Files Changed**

- `src/main/java/frc/robot/controls/DriveInputProcessor.java`

**Verification**

Source inspection confirmed the method has no hardware, subsystem, telemetry, or controller dependency.

**Expected Result**

Forward and reverse test values retain their existing magnitude and sign.

### Step 2 - Add the drive command

**Objective**

Move the held-button drivetrain action into the command layer.

**Why**

Commands must coordinate robot actions, declare subsystem requirements, and stop safely when interrupted.

**Action**

Created `DriveTestCommand`. Its constructor receives `DriveSubsystem` and one signed output, calls `addRequirements(...)`, drives both sides in `execute()`, stops in `end(...)`, and returns `false` from `isFinished()`.

**Files Changed**

- `src/main/java/frc/robot/commands/drive/DriveTestCommand.java`

**Verification**

Source inspection confirmed dependency injection, command requirement ownership, interruption-only lifetime, and safe stop behavior.

**Expected Result**

The drivetrain runs while the command is scheduled and stops when the held-button binding interrupts it.

### Step 3 - Complete the DriveIO contract

**Objective**

Add the mandatory drivetrain observation snapshot.

**Why**

Document A and the frozen interface contract require every mechanism IO interface to own an inputs snapshot and an update method.

**Action**

Added `DriveIOInputs` with left and right applied-output observations, added `updateInputs(...)`, and made `setTankOutputs(...)` and `stop()` mandatory.

**Files Changed**

- `src/main/java/frc/robot/io/drive/DriveIO.java`

**Verification**

Source inspection confirmed the interface is vendor-independent and contains no telemetry, command, or business logic.

**Expected Result**

The subsystem can consume one-cycle drivetrain observations through the IO boundary.

### Step 4 - Implement the revised hardware IO contract

**Objective**

Populate the inputs snapshot without changing hardware control.

**Why**

The real IO implementation must fully update the observations defined by `DriveIO`.

**Action**

Implemented `updateInputs(...)` by reading the applied outputs from the left and right leaders. All existing SPARK MAX construction, configuration, leader/follower setup, output, and stop code was retained.

**Files Changed**

- `src/main/java/frc/robot/io/drive/DriveIOSparkMax.java`

**Verification**

Commit-diff and source inspection confirmed the existing CAN IDs, brushed motor type, current limits, voltage compensation, inversion, leader/follower relationships, configuration modes, and leader output calls were unchanged.

**Expected Result**

The hardware behaves as before while exposing valid applied-output observations.

### Step 5 - Consume the inputs snapshot in the subsystem

**Objective**

Complete the observation path through the subsystem.

**Why**

The subsystem must own the snapshot and request fresh IO observations each scheduler cycle.

**Action**

Added one `DriveIOInputs` instance and called `io.updateInputs(inputs)` from `periodic()`. Existing `tankDrive(...)` and `stop()` behavior was preserved. Output bounds now reference `DriveConstants`.

**Files Changed**

- `src/main/java/frc/robot/subsystems/DriveSubsystem.java`
- `src/main/java/frc/robot/Constants.java`

**Verification**

Source inspection confirmed the subsystem depends only on `DriveIO`, performs no vendor access, and still clamps both output sides to the same numeric range.

**Expected Result**

Drive commands remain safely bounded and hardware observations update periodically.

### Step 6 - Restore RobotContainer as the composition root

**Objective**

Limit `RobotContainer` to construction, injection, implementation selection, and bindings.

**Why**

The frozen architecture forbids mechanism behavior and input-processing logic in the composition root.

**Action**

Constructed the input processor, `DriveIO`, and subsystem as explicit dependencies. Replaced `Commands.startEnd(...)` and local drive methods with `DriveTestCommand` instances. Removed detailed test printing and retained `getAutonomousCommand()`.

**Files Changed**

- `src/main/java/frc/robot/RobotContainer.java`

**Verification**

Source inspection confirmed no drivetrain behavior method, hardware configuration, telemetry calculation, or detailed printing remains in the container.

**Expected Result**

The A/B bindings use the complete frozen control pipeline.

### Step 7 - Verify the implementation

**Objective**

Confirm that the restored architecture compiles as a complete WPILib project.

**Why**

A successful clean build is the required technical verification before documentation.

**Action**

Ran the lesson Gradle wrapper with the WPILib 2026 JDK:

```powershell
.\gradlew.bat clean build
```

**Files Changed**

- None

**Verification**

Gradle reported `BUILD SUCCESSFUL in 23s` with five actionable tasks executed.

**Expected Result**

All production Java sources compile and the lesson artifact is assembled successfully.

## 7. Complete Verification

### Behavior Preservation

| Item | Preserved value or behavior | Verification |
|---|---|---|
| Controller port | USB port `0` | Source and implementation-diff inspection |
| Left leader CAN ID | `11` | Source and implementation-diff inspection |
| Left follower CAN ID | `8` | Source and implementation-diff inspection |
| Right leader CAN ID | `10` | Source and implementation-diff inspection |
| Right follower CAN ID | `7` | Source and implementation-diff inspection |
| Motor type | Brushed | Source and implementation-diff inspection |
| Left inversion | `true` | Source and implementation-diff inspection |
| Right inversion | `false` | Source and implementation-diff inspection |
| Current limit | `60 A` | Source and implementation-diff inspection |
| Voltage compensation | `12.0 V` | Source and implementation-diff inspection |
| Leader/follower setup | CAN 8 follows CAN 11; CAN 7 follows CAN 10 | Source and implementation-diff inspection |
| A button | Holds `+kDriveTestOutput` on both sides | Source inspection |
| B button | Holds `-kDriveTestOutput` on both sides | Source inspection |
| Button release | Interrupts command and calls `DriveSubsystem.stop()` | Source inspection |
| Team number | Unchanged | No team-configuration file changed |
| Idle mode | Unchanged; no explicit idle-mode configuration was added or removed | Implementation-diff inspection |

### Verification Results

| Verification | Result | Evidence |
|---|---|---|
| Architecture Review | PASS | Source and commit-diff inspection against Document A and Document B |
| Baseline Build | PASS | Existing lesson status recorded that the imported WPILib baseline build succeeded |
| Final Build | PASS | `.\gradlew.bat clean build`; `BUILD SUCCESSFUL in 23s` |
| Simulation | NOT TESTED | No simulation execution evidence |
| Driver Station / Glass | NOT TESTED | No Driver Station or Glass evidence |
| Real Robot | NOT TESTED | No real-robot execution evidence |
| Documentation Review | PASS | Required sections, file inventory, per-step fields, verification evidence, and untested stages reviewed |

## 8. Known Issues

- A simulation or no-op `DriveIO` implementation is deferred to a later lesson.
- Simulation behavior has not been tested.
- Driver Station and Glass behavior has not been tested.
- Real-robot behavior has not been tested.
- The applied-output snapshot is not yet published through telemetry.
- Untouched imported files retain minor code-standard debt, including unused vendor imports in `Robot.java` and a missing SSIS author block in `Main.java`.

## 9. Final Checklist

- [x] Frozen backbone preserved.
- [x] `RobotContainer` remains the composition root only.
- [x] Drive IO interface owns an inputs snapshot.
- [x] Real IO implementation updates the snapshot.
- [x] Commands declare subsystem requirements.
- [x] Hardware configuration values are preserved.
- [x] A/B source behavior is preserved.
- [x] Complete Java files were used.
- [x] Final clean build passed.
- [ ] Simulation passed - NOT TESTED.
- [ ] Driver Station / Glass passed - NOT TESTED.
- [ ] Real robot passed - NOT TESTED.
- [x] Transition documentation created.
- [x] Documentation reviewed.
- [x] `LESSON_STATUS.md` updated.
- [ ] User review completed.
- [ ] Documentation/status commit performed.
- [ ] Push performed.
