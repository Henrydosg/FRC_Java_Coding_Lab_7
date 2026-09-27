# New WPILib Project to S00 L01 Step by Step

## Step 1 - Establish the generated-project baseline

- Objective: Confirm the clean WPILib Command Robot project is a valid starting point.
- Why: A known baseline isolates the foundation lesson from generated-project failures.
- Action: Record the verified clean-project baseline build in `LESSON_STATUS.md`.
- Files Changed: `LESSON_STATUS.md`.
- Verification: The clean generated project baseline build passed before initialization.
- Expected Result: The lesson begins from a verified WPILib project.

## Step 2 - Remove generated example behavior

- Objective: Remove the WPILib example subsystem, command, and autonomous factory.
- Why: Future lessons must add only real responsibilities in the approved package boundaries.
- Action: Delete `ExampleSubsystem`, `ExampleCommand`, and `Autos`, then replace the generated autonomous handoff with an empty command.
- Files Changed: `src/main/java/frc/robot/subsystems/ExampleSubsystem.java`, `src/main/java/frc/robot/commands/ExampleCommand.java`, `src/main/java/frc/robot/commands/Autos.java`, and `src/main/java/frc/robot/RobotContainer.java`.
- Verification: Source review confirmed that no WPILib example classes or references remain.
- Expected Result: No generated robot behavior remains.

## Step 3 - Establish the frozen package backbone

- Objective: Create the approved top-level package boundaries.
- Why: Future work requires stable ownership without speculative mechanism classes.
- Action: Add package documentation for `controls`, `commands`, `io`, `observation`, `subsystems`, and `telemetry`.
- Files Changed: The six corresponding `package-info.java` files.
- Verification: Source review confirmed that exactly the six approved top-level package boundaries exist.
- Expected Result: Future responsibilities have their frozen locations without placeholder classes.

## Step 4 - Preserve composition and lifecycle boundaries

- Objective: Keep RobotContainer as the composition root and preserve the Robot lifecycle.
- Why: Lifecycle scheduling and component wiring must not absorb mechanism, input-processing, telemetry, or hardware logic.
- Action: Simplify `RobotContainer` to return `Commands.none()` and retain the generated lifecycle behavior in `Robot.java`.
- Files Changed: `src/main/java/frc/robot/RobotContainer.java`, `src/main/java/frc/robot/Robot.java`, `src/main/java/frc/robot/Main.java`, and `src/main/java/frc/robot/Constants.java`.
- Verification: Architecture review confirmed composition-only RobotContainer and unchanged Robot lifecycle behavior.
- Expected Result: The project is ready for the next approved capability without redesigning the backbone.

## Step 5 - Verify the architecture foundation build

- Objective: Confirm the foundation compiles after generated examples are removed.
- Why: A successful build validates the package declarations and empty autonomous handoff.
- Action: Run `./gradlew.bat clean build --no-daemon --warning-mode all` with the WPILib 2026 JDK.
- Files Changed: No source files.
- Verification: `BUILD SUCCESSFUL` with five executed Gradle tasks.
- Expected Result: The architecture foundation is buildable.

## Step 6 - Record runtime applicability

- Objective: Record the verified runtime scope for this architecture-only lesson.
- Why: Verification status must distinguish user-verified simulation from checks that do not apply before hardware and telemetry exist.
- Action: Record the user-verified simulation PASS and mark Driver Station / Glass and Real Robot as NOT APPLICABLE.
- Files Changed: `LESSON_STATUS.md`.
- Verification: User-provided verification evidence.
- Expected Result: The lesson status reflects its verified scope without claiming unperformed hardware or dashboard testing.
