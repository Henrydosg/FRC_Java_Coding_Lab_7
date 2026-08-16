# S00_L15 to S00_L16 Step by Step

## Lesson Objective

S00_L16 establishes a deterministic CTRE hardware configuration contract for one Swerve module.
The contract owns desired values in `Constants.java`, applies TalonFX and CANcoder configuration
inside `SwerveModuleIOCTRE`, verifies apply and readback results, exposes configuration health through
the existing observation path, and rejects nonzero output when configuration is unhealthy.

The lesson preserves the S00_L15 commissioning safety boundary. It does not add closed-loop control
or expand commissioning commands to other modules.

## Step 1

- Objective: Copy the frozen S00_L15 lesson as the implementation baseline.
- Why: Each lesson is an independent WPILib project, and completed lessons are immutable reference
  points.
- Action: Copy `S00_L15_SingleModuleOpenLoopCommissioning_Foundation` to
  `S00_L16_ModuleHardwareConfigurationContract_Foundation`.
- Files Changed: New S00_L16 lesson directory only.
- Verification: The copied project contains the S00_L15 source, tests, Gradle files, and lesson
  documentation.
- Expected Result: S00_L16 starts as an exact inherited lesson snapshot.

## Step 2

- Objective: Rename the copied lesson to the S00_L16 identity.
- Why: The active lesson must have an unambiguous project and documentation identity before any new
  concept is implemented.
- Action: Rename the copied directory and replace inherited S00_L15 identity metadata with
  `S00_L16_ModuleHardwareConfigurationContract_Foundation`.
- Files Changed: S00_L16 metadata files.
- Verification: The lesson name, previous lesson, and source lesson identify S00_L16 and frozen
  S00_L15 respectively.
- Expected Result: S00_L16 is the active editable lesson; S00_L15 remains frozen.

## Step 3

- Objective: Remove copied build artifacts.
- Why: Build output must be regenerated for the new independent project rather than carried forward
  from the source lesson.
- Action: Delete the copied `build/` and `.gradle/` directories before building S00_L16.
- Files Changed: Generated artifacts only.
- Verification: S00_L16 has no inherited build output before the baseline build.
- Expected Result: The first S00_L16 build is a clean baseline.

## Step 4

- Objective: Establish the inherited baseline build result.
- Why: A passing baseline separates copied-lesson defects from S00_L16 implementation defects.
- Action: From the S00_L16 project directory, run `.\gradlew.bat build -x test --no-daemon --console=plain`.
- Files Changed: Generated build output only.
- Verification: The inherited baseline build passed.
- Expected Result: S00_L16 compiles and packages before the new hardware configuration concept.

## Step 5

- Objective: Confirm the new lesson is the only new working-tree scope.
- Why: Lesson lifecycle and change review require the copied S00_L16 directory to be distinguishable
  from frozen previous lessons.
- Action: The user checks Git status and confirms the new S00_L16 directory is untracked.
- Files Changed: None.
- Verification: User-reported Git status showed only the new S00_L16 directory as untracked.
- Expected Result: No previous lesson is modified; Git remains user-owned.

## Step 6

- Objective: Correct inherited lesson metadata.
- Why: Stale S00_L15 metadata would misidentify the architecture, lifecycle state, and source lesson.
- Action: Set the active lesson identity to S00_L16, record S00_L15 as the frozen source, set status
  to `IN_PROGRESS`, and record the baseline build and Architecture Audit results.
- Files Changed: `README.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, `LESSON_STATUS.md`.
- Verification: Metadata reported S00_L16 as active and implementation as not yet started at this
  stage.
- Expected Result: The lesson lifecycle is correctly initialized before implementation.

## Step 7

- Objective: Run the Architecture Audit.
- Why: Configuration changes must remain inside the existing package and dependency boundaries.
- Action: Inspect the inherited source, tests, Frozen Backbone, RobotContainer, IO contracts,
  observation flow, telemetry, and S00_L15 commissioning evidence.
- Files Changed: None.
- Verification: Architecture Audit PASS; Frozen Backbone, RobotContainer composition-root behavior,
  vendor isolation, and read-only telemetry were preserved.
- Expected Result: The deterministic configuration contract is approved for implementation.

## Step 8

- Objective: Lock the architecture constraints before coding.
- Why: Hardware configuration must not become a new control architecture or bypass the observation
  boundary.
- Action: Preserve `Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware`
  and `hardware -> IOInputs -> subsystem / estimator -> immutable Observation -> telemetry`.
- Files Changed: None.
- Verification: Vendor APIs remain in IO implementations; `SwerveModuleIO` remains vendor-neutral;
  RobotContainer remains composition root only.
- Expected Result: The lesson scope is limited to deterministic hardware configuration and health.

## Step 9

- Objective: Audit the inherited CTRE configuration behavior.
- Why: The implementation must identify existing configuration gaps without guessing physical values.
- Action: Inspect `Constants.java`, `SwerveModuleIO.java`, `SwerveModuleIOCTRE.java`,
  `SwerveSubsystem.java`, `RobotContainer.java`, tests, hardware maps, and S00_L15 commissioning
  records.
- Files Changed: None.
- Verification: Existing IO exposed raw signals and health fields but did not yet provide the complete
  S00_L16 deterministic configuration contract.
- Expected Result: Only the smallest missing configuration behavior is selected for implementation.

## Step 10

- Objective: Use Phoenix Tuner X output as calibration evidence.
- Why: Generated calibration values are authoritative hardware evidence, but generated drivetrain
  architecture does not belong in the Coding Lab.
- Action: Inspect `generated/TunerConstants.java` and extract IDs, ratios, inversions, CANcoder
  direction, offsets, and confirmed current limits.
- Files Changed: None.
- Verification: The generated facts were mapped into existing `Constants.java` and
  `SwerveModuleIOCTRE.java` responsibilities without copying CTRE generated drivetrain classes.
- Expected Result: Calibration facts are retained while the Frozen Backbone remains unchanged.

## Step 11

- Objective: Add the calibrated hardware values to Constants.
- Why: Constants is the repository configuration authority and must contain the approved values used
  by each module factory.
- Action: Implement the calibrated drive and steer inversion mappings, CANcoder direction, four
  CANcoder offsets, drive and steer gear ratios, 70 A drive supply current limit, and 60 A steer
  stator current limit.
- Files Changed: `src/main/java/frc/robot/Constants.java`.
- Verification: Focused constants tests cover ratios, per-module inversion/direction mappings,
  offsets, and current limits.
- Expected Result: Every module factory receives its own approved hardware values.

## Step 12

- Objective: Construct deterministic CTRE configurations inside IO.
- Why: Vendor configuration APIs belong in the hardware IO implementation, not in subsystems or
  RobotContainer.
- Action: Build `TalonFXConfiguration` for drive and steer and `CANcoderConfiguration` for the
  absolute encoder during `SwerveModuleIOCTRE` construction.
- Files Changed: `src/main/java/frc/robot/io/swerve/SwerveModuleIOCTRE.java`.
- Verification: Drive and steer inversion/current-limit fields and CANcoder direction/offset fields
  are populated from Constants and the factory arguments.
- Expected Result: Configuration ownership remains inside IO.

## Step 13

- Objective: Apply and verify the hardware configuration.
- Why: A requested configuration is not a verified configuration until the device accepts it and
  returns the expected readback.
- Action: Apply each configuration to its own device, capture `StatusCode`, refresh readback after
  apply, and compare required fields.
- Files Changed: `SwerveModuleIOCTRE.java`.
- Verification: Apply status and refresh status are required to be `OK`; sensor direction uses exact
  equality; required offset and motor configuration fields are compared.
- Expected Result: Configuration health reflects actual device state rather than intent alone.

## Step 14

- Objective: Preserve health flags and fail-closed behavior.
- Why: Hardware configuration failure must not permit nonzero actuation.
- Action: Populate the existing configuration-health fields and reject nonzero drive or steer output
  when the module configuration is unhealthy; keep `stop()` permitted.
- Files Changed: `SwerveModuleIOCTRE.java`.
- Verification: The vendor-neutral `SwerveModuleIO` interface remains unchanged, and existing output
  safety tests continue to pass.
- Expected Result: An unhealthy module cannot receive nonzero output, while safe stop remains usable.

## Step 15

- Objective: Add deterministic focused tests.
- Why: Constants and pure readback-comparison behavior can be tested without constructing unsafe
  real CTRE hardware.
- Action: Add the constants contract tests and configuration-comparison tests for exact matches,
  one-turn wrapping, one quantization step, larger mismatches, and direction mismatches.
- Files Changed: `src/test/java/frc/robot/SwerveModuleHardwareConfigurationContractTest.java`,
  `src/test/java/frc/robot/io/swerve/SwerveModuleIOCTREConfigurationTest.java`.
- Verification: The focused contract tests eventually completed at `10/10 PASS`.
- Expected Result: Calibration mappings and strict comparison behavior are protected by tests.

## Step 16

- Objective: Run the initial software verification.
- Why: Build and tests must pass before simulation or hardware verification can provide useful evidence.
- Action: Run the focused tests, full test suite, and clean full build.
- Files Changed: Generated build and test output only.
- Verification: The final software results were focused tests `10/10 PASS`, full tests `58/58 PASS`,
  and clean full build `BUILD SUCCESSFUL`.
- Expected Result: The implementation is ready for user-owned Simulation, Glass, Driver Station, and
  real-robot verification.

## Step 17

- Objective: Verify Simulation and Glass regression behavior.
- Why: The configuration contract must not break the existing observation and telemetry paths.
- Action: Run Simulation and inspect Glass telemetry for module values, connectivity, configuration
  health, applied outputs, and safe-stop behavior.
- Files Changed: None.
- Verification: Simulation PASS and Glass PASS were user-verified.
- Expected Result: Existing observation and read-only telemetry behavior remains intact.

## Step 18

- Objective: Deploy the configuration contract to the real robot.
- Why: Apply/readback behavior, physical direction, and device health cannot be established by unit
  tests alone.
- Action: The user deploys S00_L16 with the robot disabled and observes Driver Station startup.
- Files Changed: Deployed robot image, user-owned.
- Verification: Driver Station PASS; no unresolved startup configuration issue remained.
- Expected Result: The robot reaches the expected disabled/test verification state safely.

## Step 19

- Objective: Verify all four modules on the real robot.
- Why: The configuration contract is per-module, so one healthy module cannot prove the other three.
- Action: Inspect Glass and Phoenix device status for Front Left, Front Right, Back Left, and Back
  Right.
- Files Changed: None.
- Verification: All four modules reported `DriveConnected`, `SteerConnected`, and
  `EncoderConnected` true, plus all three configuration-health flags true.
- Expected Result: Every module is connected and configuration-healthy.

## Step 20

- Objective: Recalibrate CANcoder offsets from the latest Phoenix Tuner X evidence.
- Why: Absolute offsets are physical calibration values and must follow the latest verified hardware
  state.
- Action: Recalibrate each CANcoder in Phoenix Tuner X and synchronize the approved values into
  `Constants.java` and the offset test fixture.
- Files Changed: `Constants.java`,
  `SwerveModuleHardwareConfigurationContractTest.java`.
- Verification: Final offsets were recorded as FL `0.067138671875`, FR `0.02099609375`, BL
  `0.464599609375`, and BR `-0.052978515625` rotations.
- Expected Result: Code and focused tests agree with the latest approved calibration.

## Step 21

- Objective: Diagnose the BackRight CANcoder readback mismatch.
- Why: BackRight reported a live absolute position and connectivity but remained configuration-
  unhealthy, so the configuration comparison—not signal transport—required investigation.
- Action: Inspect startup diagnostics for CANcoder 32 and compare apply status, refresh status,
  direction, expected offset, actual offset, and numeric differences.
- Files Changed: `SwerveModuleIOCTRE.java` diagnostics.
- Verification: BackRight reported apply `OK`, refresh `OK`, matching
  `CounterClockwise_Positive` direction, expected `-0.052978515625`, actual `-0.052734375`, and a
  difference of exactly `0.000244140625` rotations.
- Expected Result: The failure is classified as device quantization rather than a connectivity or
  direction defect.

## Step 22

- Objective: Correct the CANcoder offset comparison without weakening failure detection.
- Why: Phoenix CANcoder readback may use a modulo-one representation, and the real device showed
  one quantization step of `1 / 4096` rotation.
- Action: Compare offsets modulo one rotation and accept no more than the named
  `1 / 4096 = 0.000244140625` rotation quantization step. Preserve apply `StatusCode.OK`, refresh
  `StatusCode.OK`, exact `SensorDirection` equality, and rejection of larger mismatches.
- Files Changed: `SwerveModuleIOCTRE.java`,
  `SwerveModuleIOCTREConfigurationTest.java`.
- Verification: Focused tests cover exact match, one-turn wrap equivalence, one-step acceptance,
  larger mismatch rejection, and direction mismatch rejection.
- Expected Result: A genuine one-step CANcoder representation difference is accepted without forcing
  health true or hiding real configuration failures.

## Step 23

- Objective: Re-run complete software verification after the readback fix.
- Why: The comparison change affects hardware health semantics and must not regress the existing
  lesson behavior.
- Action: Run:
  `.\gradlew.bat test --tests frc.robot.SwerveModuleHardwareConfigurationContractTest --tests frc.robot.io.swerve.SwerveModuleIOCTREConfigurationTest --no-daemon --console=plain`;
  then `.\gradlew.bat test --no-daemon --console=plain`; then
  `.\gradlew.bat clean build --no-daemon --console=plain`.
- Files Changed: Generated build and test output only.
- Verification: Focused tests `10/10 PASS`, full tests `58/58 PASS`, and clean full build
  `BUILD SUCCESSFUL`.
- Expected Result: The final software verification record is complete.

## Step 24

- Objective: Complete final real-robot direction and safety verification.
- Why: Software health does not replace physical direction and stop verification.
- Action: The user verifies each module’s connectivity and health, then runs the approved Front Left
  open-loop checks only: Drive Positive, Drive Negative, Steer Positive, Steer Negative, and safe
  stop/output return to zero.
- Files Changed: None.
- Verification: All four module connectivity and configuration-health groups PASS. Front Left Drive
  Positive PASS, Drive Negative PASS, Steer Positive PASS, Steer Negative PASS, and safe stop/output
  return to zero PASS.
- Expected Result: The one-module commissioning safety boundary remains intact after configuration.

## Step 25

- Objective: Finalize the S00_L16 documentation record.
- Why: Completion evidence must be preserved before user-owned Git and freeze actions.
- Action: Update the active README, lesson plan, checklist, status, and this transition guide with
  the final architecture, implementation, test, build, simulation, Glass, Driver Station, and
  real-robot evidence.
- Files Changed: S00_L16 active documentation, including this guide.
- Verification: Implementation, verification, and documentation finalization are recorded complete;
  Git commit, push, freeze, and working-tree-clean remain not complete.
- Expected Result: S00_L16 is ready for the user’s final Git and freeze workflow.

## Step 26

- Objective: Record the completion commit boundary.
- Why: The completion identifier must be preserved without claiming Codex performed Git operations.
- Action: Record the user-supplied completion commit as `eb65523 Complete S00_L16 module hardware
  configuration contract`.
- Files Changed: Documentation record only.
- Verification: The identifier is recorded as the completion commit reference; Codex did not run Git.
- Expected Result: The user performs or verifies the commit, push, freeze commit, and final clean
  working-tree check.

## Architecture Constraints

- Preserve `Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware`.
- Preserve `hardware -> IOInputs -> subsystem / estimator -> immutable Observation -> telemetry`.
- Keep RobotContainer as composition root only.
- Keep vendor APIs inside IO implementations only.
- Keep telemetry read-only.
- Do not modify previous frozen lessons.
- Do not add commissioning commands for other modules.

## Troubleshooting Notes

- If test compilation cannot resolve local main classes while Gradle shows the main output on the
  classpath, compare against the copied S00_L15 baseline and use a valid JDK/Gradle execution
  context. Do not weaken or bypass tests.
- If `EncoderConnected` is true but `EncoderConfigurationHealthy` is false, inspect the failure-only
  startup diagnostic. Apply status, refresh status, direction, raw offset, and wrapped offset must
  be considered separately.
- A one-step difference of `0.000244140625` rotations is accepted because it is `1 / 4096`; a larger
  mismatch remains unhealthy.
- A direction mismatch or non-OK apply/refresh status remains a failure even when the encoder signal
  updates correctly.
- An unhealthy module rejects nonzero output, while `stop()` remains permitted.

## Deferred Scope for S00_L17 and Later

- PID.
- Feedforward.
- FusedCANcoder closed-loop feedback.
- Motion Magic.
- Closed-loop drive/steer requests.
- Four-module state actuation.
- Kinematics.
- Odometry.

The completion commit reference is recorded, but the freeze commit and final clean-working-tree
verification remain user-owned steps.
