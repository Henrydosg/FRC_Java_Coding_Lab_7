# S00 L02 to S00 L03 Step by Step

## Step 1 - Copy the completed hardware-audit lesson

- Objective: Create S00_L03 from the completed S00_L02 project.
- Why: The lesson lifecycle requires each lesson to inherit the previous completed project.
- Action: Copy `S00_L02_Swerve_Hardware_Audit` to `S00_L03_CTRE_IO_Foundation`.
- Files Changed: New S00_L03 lesson project files.
- Verification: `LESSON_STATUS.md` identifies S00_L02 as the previous and source lesson.
- Expected Result: S00_L03 begins with the verified hardware audit and frozen architecture intact.

## Step 2 - Remove inherited build output

- Objective: Establish a clean build boundary for S00_L03.
- Why: Generated `build/` and `.gradle/` content cannot serve as current lesson evidence.
- Action: Remove inherited generated build output before the baseline build.
- Files Changed: Generated `build/` and `.gradle/` directories removed.
- Verification: The baseline build runs from source and Gradle configuration.
- Expected Result: S00_L03 begins without inherited build artifacts.

## Step 3 - Verify the inherited baseline

- Objective: Confirm the inherited S00_L02 project is buildable before implementation.
- Why: New CTRE work must not hide an inherited failure.
- Action: Run the approved baseline build.
- Files Changed: No source files.
- Verification: `LESSON_STATUS.md` records Baseline Build as PASS.
- Expected Result: The implementation begins from a verified project.

## Step 4 - Initialize the S00_L03 lesson status

- Objective: Establish S00_L03 as the active implementation lesson.
- Why: The status record defines the editable lesson, source lesson, and verification state.
- Action: Set the lesson name, previous lesson, source lesson, and `IN_PROGRESS` status.
- Files Changed: `LESSON_STATUS.md`.
- Verification: The status identifies `S00_L03_CTRE_IO_Foundation` and `S00_L02_Swerve_Hardware_Audit`.
- Expected Result: S00_L03 has an auditable lifecycle record.

## Step 5 - Approve the minimal CTRE IO scope

- Objective: Resolve only the prerequisites required for raw CTRE IO.
- Why: Unverified offsets, inversions, steer reduction, neutral modes, and current limits must not enter the implementation.
- Action: Approve Phoenix 6 version 26.3.0, the default roboRIO CAN bus, verified CAN IDs, raw signals, normalized open-loop outputs, and safe stop behavior.
- Files Changed: No files.
- Verification: The approved scope explicitly defers all unresolved configuration and higher-level Swerve behavior.
- Expected Result: Implementation can proceed without guessing hardware configuration.

## Step 6 - Add the approved Phoenix 6 dependency

- Objective: Make the verified CTRE device APIs available to S00_L03.
- Why: The real IO implementations require Phoenix 6 while the public IO contracts remain vendor-neutral.
- Action: Copy the existing Phoenix 6 version 26.3.0 vendordep from the reviewed D01 lesson.
- Files Changed: `vendordeps/Phoenix6-frc2026-latest.json`.
- Verification: The copied vendordep is byte-identical to the approved D01 source and declares version 26.3.0.
- Expected Result: Gradle resolves the approved CTRE Java and native dependencies.

## Step 7 - Record verified Swerve constants

- Objective: Centralize verified hardware identifiers and geometry.
- Why: CAN IDs and geometry must not be duplicated as magic numbers in IO implementations.
- Action: Add the Pigeon2 and FL/FR/BL/BR device IDs, drive ratio, wheel diameter/radius, wheelbase, track width, and Phoenix version to `SwerveConstants`.
- Files Changed: `src/main/java/frc/robot/Constants.java`.
- Verification: Every value matches the completed S00_L02 hardware audit.
- Expected Result: `Constants.java` remains the configuration authority for verified Swerve facts.

## Step 8 - Define the vendor-neutral Swerve module IO contract

- Objective: Define the smallest raw hardware boundary for one Swerve module.
- Why: Subsystems must depend on a vendor-neutral interface rather than CTRE device classes.
- Action: Add `SwerveModuleIO`, its mutable one-cycle `SwerveModuleIOInputs`, normalized drive/steer output methods, and `stop()`.
- Files Changed: `src/main/java/frc/robot/io/swerve/SwerveModuleIO.java`.
- Verification: The interface contains no CTRE, NetworkTables, command, subsystem, Observation, or telemetry dependency.
- Expected Result: The module contract exposes raw hardware facts and safe output capabilities only.

## Step 9 - Implement the four CTRE module mappings

- Objective: Provide real IO for all four verified CTRE Swerve modules.
- Why: Each TalonFX/TalonFX/CANcoder triplet requires a vendor-isolated implementation.
- Action: Add `SwerveModuleIOCTRE` with verified FL, FR, BL, and BR factories, raw status-signal acquisition, configuration-read health, normalized `DutyCycleOut` requests, and safe stop behavior.
- Files Changed: `src/main/java/frc/robot/io/swerve/SwerveModuleIOCTRE.java`.
- Verification: The implementation uses device-ID-only constructors, preserves the verified CAN mappings, and performs no offset, inversion, steer-ratio, neutral-mode, current-limit, PID, or closed-loop configuration.
- Expected Result: Four CTRE module IO instances can be created without adding higher-level Swerve behavior.

## Step 10 - Define and implement the gyro IO boundary

- Objective: Expose raw Pigeon2 orientation and angular-velocity signals through a vendor-neutral contract.
- Why: Future subsystem or estimator code must not access the Pigeon2 directly.
- Action: Add `GyroIO`, `GyroIOInputs`, and `GyroIOPigeon2` using the verified Pigeon2 CAN ID.
- Files Changed: `src/main/java/frc/robot/io/gyro/GyroIO.java` and `src/main/java/frc/robot/io/gyro/GyroIOPigeon2.java`.
- Verification: The interface is vendor-neutral; the CTRE implementation reads yaw, pitch, roll, device-frame angular velocities, connectivity, and configuration-read health without applying offsets or inversions.
- Expected Result: Raw gyro hardware facts flow through `GyroIOInputs`.

## Step 11 - Verify construction and stop safety

- Objective: Ensure the lesson cannot command nonzero output during device construction.
- Why: Real hardware must enter the lesson in a safe stopped state.
- Action: Clamp drive and steer requests to `[-1.0, 1.0]` and call `stop()` during each module implementation's construction.
- Files Changed: `src/main/java/frc/robot/io/swerve/SwerveModuleIOCTRE.java`.
- Verification: `stop()` calls `stopMotor()` on both TalonFX devices, and no constructor issues a nonzero control request.
- Expected Result: Newly constructed module IO begins with zero drive and steer output.

## Step 12 - Perform the final clean build

- Objective: Verify the complete S00_L03 project and approved dependency compile together.
- Why: A completed lesson requires current build evidence.
- Action: Run `.\gradlew.bat clean build --no-daemon --warning-mode all` with the WPILib 2026 JDK.
- Files Changed: No source files.
- Verification: User verified Build as PASS.
- Expected Result: The project reports `BUILD SUCCESSFUL`.

## Step 13 - Verify simulation and real hardware

- Objective: Confirm the IO foundation operates in the approved runtime environments.
- Why: Build success alone does not verify runtime behavior or physical CTRE device communication.
- Action: Run the approved simulation and real-robot verification procedures.
- Files Changed: No files.
- Verification: User verified Simulation PASS and Real Robot PASS.
- Expected Result: `LESSON_STATUS.md` records both results as PASS.

## Step 14 - Complete the lesson documentation

- Objective: Close S00_L03 with consistent evidence and transition documentation.
- Why: Completed lessons require a status record and a step-by-step guide.
- Action: Review the implemented scope, verification evidence, known unresolved configuration, and this transition guide; then set Status to `COMPLETE`.
- Files Changed: `LESSON_STATUS.md` and `docs/S00_L02_to_S00_L03_Step_by_Step.md`.
- Verification: Status, architecture evidence, build, simulation, real-robot evidence, and known issues agree with the lesson contents.
- Expected Result: S00_L03 is a complete, documented CTRE IO foundation with unresolved higher-level configuration explicitly deferred.
