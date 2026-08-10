# S00_L18 to S00_L19: Driver Input Processing

Status: `FINAL / PASS`

This guide is the finalized transition record for the `COMPLETE / FROZEN / READ-ONLY` L19 lesson.
The Architect confirmed the established closure-workflow and Git evidence as authoritative during
governance reconciliation. This record does not invent test counts, commit hashes, or remote
revision identifiers that are not present in the lesson history.

## Step 1 - Copy the frozen S00_L18 lesson

- Step: 1
- Objective: Start L19 from the completed S00_L18 foundation.
- Why: The lesson lifecycle requires inheritance from the previous frozen lesson.
- Action: Copy `S00_L18_FourModuleStateActuation` to an independent
  `S00_L19_DriverInputProcessing` project and update the lesson identity.
- Files Changed: New S00_L19 project copy and lesson metadata.
- Verification: PASS; S00_L18 remains `COMPLETE / FROZEN / READ-ONLY`.
- Expected Result: L19 inherits the verified S00_L18 architecture without changing the source lesson.

## Step 2 - Remove copied generated artifacts

- Step: 2
- Objective: Remove inherited transient build output from the new lesson copy.
- Why: Generated output must not serve as evidence for the new lesson.
- Action: Remove the copied L19 `build/` and `.gradle/` artifacts before baseline verification.
- Files Changed: Generated artifacts in the new L19 copy only.
- Verification: PASS; recorded transition evidence confirms the cleanup.
- Expected Result: L19 verification starts from regenerated output.

## Step 3 - Establish the inherited baseline

- Step: 3
- Objective: Prove the copied lesson is healthy before L19 implementation.
- Why: A baseline separates inherited behavior from L19 changes.
- Action: Run the baseline `clean build` from the L19 project.
- Files Changed: None.
- Verification: PASS; recorded baseline evidence.
- Expected Result: A known-good S00_L18-derived starting point.

## Step 4 - Complete the architecture audit

- Step: 4
- Objective: Approve an observable driver-input lesson without expanding into teleop actuation.
- Why: External operator input is not mechanism hardware observation, and the composition ownership
  must be explicit before implementation.
- Action: Review AGENTS.md v1.2, Documents A/B/C, the Frozen Backbone, the operator-input Observation
  exception, and the approved `ADR_S00_L19_L20_Driver_Input_Ownership.md`.
- Files Changed: Governance ADR outside the lesson was already approved; no Frozen Backbone document
  was changed by this lesson step.
- Verification: PASS; the ADR approves the L19-only synchronous telemetry pull and the L20 migration
  constraint.
- Expected Result: L19 is limited to a non-actuating observable pipeline.

## Step 5 - Implement deterministic axis processing

- Step: 5
- Objective: Convert semantic raw axes into finite, bounded processed intent.
- Why: Driver-input transformation belongs in `frc.robot.controls` and must be deterministic.
- Action: Add `DriverInputConstants` and `DriverInputProcessor`; apply finite-value safety, WPILib
  deadband rescaling with deadband `0.10`, signed-square shaping, and clamp to `[-1.0, +1.0]`.
- Files Changed: `src/main/java/frc/robot/Constants.java` and
  `src/main/java/frc/robot/controls/DriverInputProcessor.java`.
- Verification: Implementation confirmed by source inspection; `compileJava` PASS. At this
  implementation step, the expanded 14-test processor run remained NOT TESTED; final closure
  verification is recorded in Step 12 and `LESSON_STATUS.md`.
- Expected Result: Each semantic axis produces a finite normalized value without creating robot motion.

## Step 6 - Acquire one coherent Xbox sample and map semantics

- Step: 6
- Objective: Convert controller-specific axis reads into vendor-neutral driver-input meaning.
- Why: Controller acquisition and semantic mapping must be isolated from commands and mechanisms.
- Action: Add `XboxDriverInputSource`; read LeftY, LeftX, and RightX once per `read()` call and map
  `forward = -LeftY`, `strafe = -LeftX`, and `rotation = -RightX` before processing.
- Files Changed: `src/main/java/frc/robot/controls/XboxDriverInputSource.java`.
- Verification: `XboxDriverInputSourceTest`: 2/2 PASS from current user-supplied evidence.
- Expected Result: One coherent sample contains stable raw, semantic raw, and processed values.

## Step 7 - Add the immutable external-input Observation

- Step: 7
- Objective: Provide one immutable, vendor-neutral driver-input snapshot.
- Why: Read-only telemetry must not depend on a live controller or mutable sample.
- Action: Add `DriverInputObservation` with raw controller axes, semantic raw axes, and processed axes.
- Files Changed: `src/main/java/frc/robot/observation/DriverInputObservation.java`.
- Verification: Source inspection confirms an immutable Java record with no vendor, hardware,
  NetworkTables, command, or subsystem dependency.
- Expected Result: Telemetry can consume external human input as immutable data. This does not permit
  controls to create mechanism Observations.

## Step 8 - Publish the observation through read-only telemetry

- Step: 8
- Objective: Make raw, semantic raw, and processed driver input observable over NT4.
- Why: L19 must be verifiable without actuating the drivetrain.
- Action: Add `DriverInputTelemetryFacade` with typed publishers under `/DriverInput/Raw`,
  `/DriverInput/SemanticRaw`, and `/DriverInput/Processed`.
- Files Changed:
  `src/main/java/frc/robot/telemetry/driver/DriverInputTelemetryFacade.java`.
- Verification: `DriverInputTelemetryFacadeTest`: PASS from current user-supplied evidence.
- Expected Result: One immutable observation is published without behavior control.

## Step 9 - Compose the L19-only runtime path

- Step: 9
- Objective: Sample and publish driver input during the robot periodic loop.
- Why: The observable pipeline requires runtime composition while remaining non-actuating.
- Action: Construct the Xbox controller, input source, and telemetry facade in `RobotContainer`; inject
  them into `RobotTelemetry`; synchronously call `XboxDriverInputSource.read()` and publish its result
  from `RobotTelemetry.periodic()`.
- Files Changed: `src/main/java/frc/robot/RobotContainer.java` and
  `src/main/java/frc/robot/telemetry/RobotTelemetry.java`. The unchanged inherited
  `Robot.robotPeriodic()` call continues to invoke the now-expanded `RobotTelemetry.periodic()`.
- Verification: Architecture review PASS under the approved ADR. This permission is L19-only and
  depends on the absence of driver-input actuation.
- Expected Result: The pipeline ends at NT4 and never creates a Swerve request.

## Step 10 - Add focused verification

- Step: 10
- Objective: Cover processing boundaries, semantic mapping, snapshot stability, and typed publication.
- Why: The pipeline has pure, hardware-boundary, and telemetry responsibilities that require focused
  evidence.
- Action: Add `DriverInputProcessorTest`, `XboxDriverInputSourceTest`, and
  `DriverInputTelemetryFacadeTest`.
- Files Changed: The three focused test files under `src/test/java/frc/robot`.
- Verification: At this test-authoring step, processor evidence covered 11/11 before expansion to
  14 tests; Xbox source 2/2 PASS and telemetry facade PASS were recorded. Final closure verification
  is recorded in Step 12 and `LESSON_STATUS.md`.
- Expected Result: No 14/14 or current regression claim is made until the latest suite is run.

## Step 11 - Verify Simulation and telemetry tools

- Step: 11
- Objective: Confirm the non-actuating pipeline is visible and live in desktop verification tools.
- Why: Simulation provides observable evidence before the required real-roboRIO check.
- Action: Run Simulation and inspect `/DriverInput` in Glass and AdvantageScope while changing
  controller input.
- Files Changed: None.
- Verification: Simulation PASS; Glass PASS; AdvantageScope PASS; `/DriverInput` visible and updating
  PASS, all from current user-supplied evidence.
- Expected Result: Raw, SemanticRaw, and Processed data are observable without drivetrain actuation.

## Step 12 - Complete automated closure verification

- Step: 12
- Objective: Validate the exact current test tree and a clean build.
- Why: Earlier full regression evidence predates the latest three processor-test additions.
- Action: Complete the required automated verification and clean-build workflow.
- Files Changed: None expected; generated build output only.
- Verification: PASS; established L19 closure-workflow evidence was confirmed as authoritative by
  the Architect. Detailed command output and counts are not restated without a repository record.
- Expected Result: The completed lesson has accepted automated and clean-build closure evidence.

## Step 13 - Complete Disabled real-roboRIO verification

- Step: 13
- Objective: Verify the external-input observation pipeline on the real roboRIO without actuation.
- Why: Approved governance requires real-roboRIO verification before L19 closure.
- Action: Keep the robot Disabled; confirm `/DriverInput` exists before Xbox connection; connect the
  Xbox controller on USB port `0`; inspect Raw, SemanticRaw, and Processed values; verify axis signs,
  processing, and zero near center/deadband; confirm absolutely no drivetrain actuation.
- Files Changed: None.
- Verification: PASS from user-supplied real-roboRIO evidence. The robot remained Disabled; Glass
  connected; `/DriverInput` existed before Xbox connection; Raw `LeftX`, `LeftY`, and `RightX`
  existed and updated; SemanticRaw `Forward`, `Strafe`, and `Rotation` existed and updated;
  Processed `Forward`, `Strafe`, and `Rotation` existed and updated; the Xbox operated on USB port
  `0`; axis signs/mapping and center/deadband behavior were correct; and no drivetrain actuation
  occurred.
- Expected Result: The real target published driver-input observations while the drivetrain remained
  completely inactive.

## Step 14 - Finalize documentation and close L19

- Step: 14
- Objective: Reconcile final evidence and prepare the lesson for closure.
- Why: The transition guide may be marked PASS only when final, and it must be complete before the
  lesson becomes `COMPLETE / FROZEN`.
- Action: Reconcile the established closure evidence, finalize L19 documentation, record the
  Architect-confirmed Git completion, and freeze the lesson.
- Files Changed: L19 documentation only unless a failed verification separately authorizes a source
  correction.
- Verification: PASS; all required gates, including explicit Disabled real-roboRIO verification,
  are recorded. The guide is `FINAL / PASS`, and L19 is `COMPLETE / FROZEN / READ-ONLY`.
- Expected Result: L19 remains a frozen snapshot and L20 becomes the sole active lesson.

## L19 Boundary

L19 contains no drive/default command, `ChassisSpeeds`, `SwerveSubsystem` drive request, Swerve
module-state generation, or drivetrain actuation. Actuation verification is `NOT APPLICABLE`.

## L20 Migration Requirement

L20 remains Robot-Relative Teleop Integration. Before any driver input can actuate Swerve, establish
exactly one authoritative driver-input sample per control cycle. Telemetry and drive control must not
independently poll Xbox. Telemetry must publish the same sample or a documented immutable projection.
This ADR requirement does not change the Frozen Backbone or the S00_L15-S00_L24 roadmap.
