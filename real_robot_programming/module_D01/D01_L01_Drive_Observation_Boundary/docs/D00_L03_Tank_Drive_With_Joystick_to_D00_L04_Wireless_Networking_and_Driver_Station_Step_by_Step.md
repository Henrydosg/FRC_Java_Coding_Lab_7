# D00_L03 to D00_L04 Step-by-Step Transition Guide

## 1. Purpose

D00_L03 established continuous tank-drive control from the Xbox controller. D00_L04 follows by explaining how the Driver Station, network connection, roboRIO, WPILib lifecycle, and safety layers deliver that driver intent to the unchanged robot program.

## 2. Learning Objectives

D00_L04 introduces:

- The Driver Station's role in robot identity, communication, mode selection, and safety.
- The Disabled, Autonomous, Teleop, Test, and simulation lifecycle callbacks.
- The difference between Disabled program execution and Enabled actuator behavior.
- HAL Simulation and simulated Driver Station verification.
- Layered safety responsibilities across the Driver Station, HAL/control system, WPILib, and robot program.

## 3. Prerequisites

Before starting D00_L04, D00_L03 must provide:

- A successful clean build.
- A working `CommandXboxController` on USB port `0`.
- `DefaultDriveCommand` as the drivetrain default command.
- `DriveInputProcessor`, `DriveSubsystem`, and the vendor-independent `DriveIO` boundary.
- Safe command interruption through `DriveSubsystem.stop()`.
- The frozen package responsibilities and dependency direction.
- Lesson status `COMPLETE`.

## 4. Step-by-Step Transition

### Step 1 - Inherit D00_L03

- **Objective:** Create D00_L04 from the completed D00_L03 project.
- **Why:** Preserve incremental inheritance and begin from verified behavior.
- **Action:** Copy the complete project, remove inherited generated directories, and initialize D00_L04 as `IN_PROGRESS`.
- **Files Changed:** New D00_L04 project tree and D00_L04 `LESSON_STATUS.md`.
- **Verification:** Java source hashes matched D00_L03 and the baseline clean build succeeded.
- **Expected Result:** D00_L04 starts as a buildable, architecture-identical lesson.

### Step 2 - Review Scope and Architecture

- **Objective:** Define the networking and Driver Station lesson boundary.
- **Why:** Prevent networking education from changing robot-control responsibilities.
- **Action:** Review governance documents, inherited Java architecture, WPILib configuration, and hardware documentation.
- **Files Changed:** None.
- **Verification:** Architecture review `PASS`; Java changes not required.
- **Expected Result:** The Frozen Backbone remains unchanged.

### Step 3 - Verify Team Number and Robot Identity

- **Objective:** Confirm consistent robot identity.
- **Why:** GradleRIO, Driver Station, roboRIO, and radio configuration must use the same team identity.
- **Action:** Verify team `10951`, GradleRIO team lookup, hostname `roboRIO-10951-FRC.local`, USB address `172.22.11.2`, and team subnet `10.109.51.x`.
- **Files Changed:** None.
- **Verification:** No conflicting team number or identity value was found.
- **Expected Result:** No WPILib configuration change is required.

### Step 4 - Create the Communication Map

- **Objective:** Document the complete Driver Station-to-motor-controller communication path.
- **Why:** Separate physical/network transport from the frozen Java control pipeline.
- **Action:** Document USB, Ethernet, team-radio, robot-identity, responsibility, simulation, hardware, and safety boundaries.
- **Files Changed:** `docs/D00_L04_Driver_Station_Communication_Map.md`.
- **Verification:** Communication Map `PASS`; clean build succeeded.
- **Expected Result:** Students can trace driver input from the controller through the network and robot program.

### Step 5 - Analyze the Robot Lifecycle

- **Objective:** Explain every `TimedRobot` lifecycle callback.
- **Why:** Driver Station modes determine which initialization and periodic callbacks run.
- **Action:** Inspect `Robot.java` and verify `CommandScheduler.run()` remains in `robotPeriodic()`.
- **Files Changed:** None.
- **Verification:** Robot Lifecycle Analysis `PASS`; WPILib best-practice placement confirmed.
- **Expected Result:** Disabled, Autonomous, Teleop, Test, and simulation callbacks are understood.

### Step 6 - Analyze Disabled and Enabled Behavior

- **Objective:** Explain what continues, stops, and becomes eligible across enable-state transitions.
- **Why:** Disabled operation is layered safety behavior, not termination of the robot program.
- **Action:** Trace commands, subsystem periodic methods, IO observation, scheduler behavior, HAL gating, and safe command interruption.
- **Files Changed:** None.
- **Verification:** Disabled vs Enabled Analysis `PASS`.
- **Expected Result:** Students understand the safety responsibilities of every layer.

### Step 7 - Prepare the HAL Verification Record

- **Objective:** Create an evidence-controlled manual test record.
- **Why:** Verification results must not be marked `PASS` before direct observation.
- **Action:** Create a record with all simulated mode results initially set to `NOT TESTED`.
- **Files Changed:** `docs/D00_L04_HAL_Driver_Station_Verification_Record.md`.
- **Verification:** The template contained no invented PASS result.
- **Expected Result:** Manual observations have a consistent place to be recorded.

### Step 8 - Verify HAL Simulation and Driver Station Modes

- **Objective:** Verify the simulated communication and mode transitions.
- **Why:** D00_L04 requires evidence that Driver Station state reaches the robot program safely.
- **Action:** The user directly observed Disabled, Autonomous, Teleop, Test, connection stability, centered-control behavior, and safe return to Disabled.
- **Files Changed:** `docs/D00_L04_HAL_Driver_Station_Verification_Record.md`.
- **Verification:** All four modes, safety behavior, HAL Simulation, and manual Driver Station verification recorded `PASS`; final state `DISABLED`.
- **Expected Result:** Simulation objectives are verified without claiming physical-network behavior.

### Step 9 - Update Lesson Documentation

- **Objective:** Record verified work and remaining closure gates.
- **Why:** Lesson status must distinguish completed simulation evidence from deferred real-hardware checks.
- **Action:** Update the verification status and create the D00_L04 completion checklist.
- **Files Changed:** `LESSON_STATUS.md` and `docs/D00_L04_Lesson_Completion_Checklist.md`.
- **Verification:** Supported items are `PASS`; physical checks remain `DEFERRED` or `NOT VERIFIED`; lesson remains `IN_PROGRESS`.
- **Expected Result:** The lesson has an evidence-based path to formal closure.

### Step 10 - Create This Transition Guide

- **Objective:** Document the verified transition from D00_L03 to D00_L04.
- **Why:** Every lesson requires a reproducible inheritance and verification guide.
- **Action:** Record the actual sequence, evidence, deferred work, and next-lesson gate.
- **Files Changed:** `docs/D00_L03_Tank_Drive_With_Joystick_to_D00_L04_Wireless_Networking_and_Driver_Station_Step_by_Step.md`.
- **Verification:** Final clean build required after guide creation.
- **Expected Result:** D00_L04 documentation is ready for the later formal closure step.

## 5. Code Changes

- Java source changes: **NONE**
- Architecture changes: **NONE**
- Frozen Backbone: **PRESERVED**

## 6. Verification Summary

The following items have verified `PASS` evidence:

- Lesson objective and architecture review.
- Team number `10951` and expected robot identity.
- Baseline and repeated clean builds.
- Robot lifecycle analysis.
- Disabled versus Enabled analysis.
- Driver Station communication map.
- HAL Simulation startup and simulated Driver Station connection.
- Disabled, Autonomous, Teleop, and Test mode observations.
- Connection stability and safe return to Disabled.
- Centered controls produced no unintended drive request.
- No startup exceptions or scheduler errors were observed.
- Safety behavior and final Disabled state.

## 7. Deferred Items

The following real-hardware checks remain deferred or not verified:

- USB communication.
- Ethernet communication.
- Team radio and wireless communication.
- roboRIO imaging and hostname resolution.
- Physical SPARK MAX behavior and CAN wiring.
- Motor output and drivetrain motion.
- Latency, packet loss, and reconnection.

No physical robot verification is claimed.

## 8. Next Lesson

`D00_L05_Drive_Input_Processing` may begin only after D00_L04 is formally closed, the final clean build passes, `LESSON_STATUS.md` is changed to `COMPLETE`, the Git commit and push succeed, and D00_L04 becomes a frozen snapshot.

D00_L04 remains `IN_PROGRESS` during this transition-guide step.
