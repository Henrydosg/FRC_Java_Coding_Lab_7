# S00_L19 to S00_L20: Robot-Relative Teleop Integration

Status: `IN_PROGRESS / NOT FINAL`

This guide contains the reconciled implementation and verification history for
`S00_L20_RobotRelativeTeleopIntegration`. Its content is current through the final architecture
review. It cannot be marked final or PASS while required real-robot verification remains
`NOT TESTED - hardware unavailable`.

## Final Implemented Production Path

```text
XboxController
-> XboxDriverInputSource
-> DriverInputProcessor
-> immutable DriverInputObservation
-> RobotRelativeTeleopDriveCommand
-> robot-relative ChassisSpeeds
-> SwerveSubsystem
-> SwerveOutputPipeline
-> SwerveModuleIO
```

Each command execution acquires exactly one authoritative driver-input sample. The same immutable
`DriverInputObservation` supplies control scaling and driver-input telemetry. Approved L20 baseline
limits are `1.0 m/s` translation and `1.0 rad/s` rotation.

## Step 1 - Copy the completed L19 lesson

- Step: 1
- Objective: Start L20 from the completed S00_L19 foundation.
- Why: The repository lifecycle requires inheritance from the previous frozen lesson.
- Action: Copy `S00_L19_DriverInputProcessing` into a new independent lesson project.
- Files Changed: New S00_L20 project copy.
- Verification: PASS; completed transition evidence was supplied by the user.
- Expected Result: L20 inherits the frozen L19 source, tests, configuration, and documentation.

## Step 2 - Rename the copied lesson to L20

- Step: 2
- Objective: Establish the new lesson identity.
- Why: One lesson must be one independently named WPILib project.
- Action: Rename the copied directory to `S00_L20_RobotRelativeTeleopIntegration` and reconcile its
  lesson identity metadata.
- Files Changed: New L20 directory and L20 Markdown identity files.
- Verification: PASS; the current directory and metadata identify L20, while L19 remains
  `COMPLETE / FROZEN / READ-ONLY`.
- Expected Result: Exactly one editable L19-derived lesson is identified as L20.

## Step 3 - Remove copied build artifacts

- Step: 3
- Objective: Remove inherited generated state before baseline verification.
- Why: Baseline evidence must be generated from the L20 copy rather than copied artifacts.
- Action: Delete copied `build/` and `.gradle/` directories and confirm `build/` is absent before
  the baseline run.
- Files Changed: Generated artifacts in L20 only.
- Verification: PASS; the user supplied cleanup and absence evidence.
- Expected Result: L20 begins baseline verification without copied build output.

## Step 4 - Establish the baseline build

- Step: 4
- Objective: Prove the inherited project builds before L20 implementation.
- Why: A known-good baseline separates inherited L19 behavior from L20 changes.
- Action: Run the L20 baseline clean build.
- Files Changed: Generated build output only.
- Verification: PASS; user-supplied baseline clean-build evidence.
- Expected Result: The inherited source is known to build before Robot-Relative Teleop Integration.

## Step 5 - Complete the architecture audit and lock

- Step: 5
- Objective: Bound L20 before production implementation.
- Why: Driver-input ownership must be coherent before input may actuate Swerve.
- Action: Audit the inherited input, telemetry, command, subsystem, output, IO, safety, and lesson
  boundaries; reconcile governance; then obtain the Increment 1 ChatGPT Architect lock.
- Files Changed: L19/L20 governance Markdown during reconciliation; no Java or tests in the audit.
- Verification: PASS; the audit, governance reconciliation, and explicit Increment 1 architecture
  lock were completed.
- Expected Result: L20 has one bounded robot-relative design preserving the Frozen Backbone.

## Step 6 - Implement Increment 1

- Step: 6
- Objective: Connect processed driver intent to the existing production Swerve path.
- Why: L20 introduces robot-relative teleop without redesigning subsystem or IO architecture.
- Action: Add `RobotRelativeTeleopDriveCommand`; add the approved `1.0 m/s` translation and
  `1.0 rad/s` rotation limits; move driver-input sample ownership into the command; publish the same
  immutable sample to driver telemetry; remove independent Xbox polling from `RobotTelemetry`; and
  install the command as the Swerve default command in `RobotContainer`.
- Files Changed: L20 `Constants.java`, `RobotContainer.java`, `RobotTelemetry.java`, and
  `RobotRelativeTeleopDriveCommand.java`.
- Verification: PASS at the implementation and subsequent verification gates recorded below.
- Expected Result: One Xbox sample drives robot-relative chassis intent and matching driver telemetry.

## Step 7 - Verify the focused command contract

- Step: 7
- Objective: Verify sampling, scaling, lifecycle, and exception safety.
- Why: The command must be independently verified before runtime validation.
- Action: Add and execute `RobotRelativeTeleopDriveCommandTest`.
- Files Changed: `RobotRelativeTeleopDriveCommandTest.java` and generated build output.
- Verification: PASS; the user supplied 11/11 PASS.
- Expected Result: Tests prove one sample per execution, same-sample control/telemetry, robot-relative
  signs and scaling, non-termination, end/interruption stop, and failure-path stop-before-rethrow.

## Step 8 - Verify Simulation, HALSIM joystick, and Glass

- Step: 8
- Objective: Verify the runtime driver-input path in the approved simulated environment.
- Why: Runtime verification must confirm the command-owned sample and visible DriverInput topics.
- Action: Run Simulation, exercise the HALSIM joystick, and inspect DriverInput topics in Glass.
- Files Changed: None; runtime verification only.
- Verification: PASS; user supplied Simulation PASS, HALSIM joystick PASS, and Glass / DriverInput
  PASS.
- Expected Result: Simulated input follows the robot-relative command path and the same immutable
  sample is visible through approved driver-input telemetry.

## Step 9 - Review the production-request observability proposal

- Step: 9
- Objective: Resolve whether requested chassis/module values may be published as an Observation.
- Why: Verification needs must not weaken the frozen Observation and telemetry boundaries.
- Action: Review the proposed production-request Observation against AGENTS and Documents A/B/C.
- Files Changed: None; architecture review only.
- Verification: PASS; the proposal was rejected because Document C prohibits control setpoints,
  actuator requests, safety gates, and lifecycle state in mechanism Observations.
- Expected Result: No requested chassis/module telemetry, request DTO workaround, or Observation
  contract change is introduced.

## Step 10 - Add test-only end-to-end production-path verification

- Step: 10
- Objective: Verify the complete production request path without adding production observability.
- Why: Recording test IO can prove final module requests while preserving production architecture.
- Action: Add `RobotRelativeTeleopProductionPathTest` using the real input source/processor, command,
  subsystem, output pipeline, kinematics, optimization, and desaturation with four recording test IO
  modules.
- Files Changed: `RobotRelativeTeleopProductionPathTest.java` only, plus generated build output.
- Verification: PASS; the user explicitly reran the focused suite and supplied 10/10 PASS.
- Expected Result: Test evidence covers forward, strafe, rotation, combined motion, zero input,
  scaling, FL/FR/BL/BR identity/order, disabled gating, stop/interruption, commissioning ownership,
  robot-relative-only behavior, and final post-pipeline IO requests.

## Step 11 - Run the full regression

- Step: 11
- Objective: Confirm L20 and inherited behavior remain coherent after both increments.
- Why: Focused tests do not replace repository-wide lesson regression.
- Action: Rerun the current full L20 regression suite.
- Files Changed: Generated test output only.
- Verification: PASS; the user explicitly supplied current full-regression PASS.
- Expected Result: No tested inherited behavior regresses under the locked L20 architecture.

## Step 12 - Run the final clean build

- Step: 12
- Objective: Establish final clean software-build evidence.
- Why: Closure review requires a clean build after the completed implementation and tests.
- Action: Run `gradlew clean build`.
- Files Changed: Regenerated build output only.
- Verification: PASS; user supplied `BUILD SUCCESSFUL`, 7 actionable tasks, 7 executed.
- Expected Result: The current L20 project builds successfully from a clean state.

## Step 13 - Record real-robot verification debt

- Step: 13
- Objective: Preserve the unexecuted physical verification gate without inventing evidence.
- Why: Simulation does not establish real-hardware actuation, wiring, direction, or safety behavior.
- Action: Record Real Robot Verification as `NOT TESTED - hardware unavailable` and retain the
  requirement for later execution.
- Files Changed: L20 lesson documentation only.
- Verification: NOT TESTED; hardware was unavailable. No real-robot PASS is claimed.
- Expected Result: Closure reviewers can distinguish completed software/simulation evidence from
  outstanding physical verification.

## Step 14 - Complete the final architecture review

- Step: 14
- Objective: Confirm the delivered L20 design remains inside its locked architecture and scope.
- Why: Final review must ensure verification work did not introduce forbidden production behavior.
- Action: Confirm robot-relative-only control; one authoritative immutable sample; production
  telemetry and Observation boundaries unchanged; test-only recording IO; and no L21/L22,
  `SwerveModuleIOSim`, odometry, pose, IO/CTRE, or Frozen Backbone expansion.
- Files Changed: None by the architecture review; documentation was reconciled separately.
- Verification: PASS for architecture preservation. Lesson closure remains pending because required
  real-robot verification is `NOT TESTED` and ChatGPT Architect has not granted closure.
- Expected Result: Production architecture is locked and documentation is ready for closure review,
  without falsely marking L20 `COMPLETE / FROZEN`.

## Verification Summary

| Gate | State |
| --- | --- |
| Focused command tests | PASS - user supplied 11/11 |
| Focused production-path tests | PASS - user supplied 10/10 |
| Full regression | PASS - user supplied |
| Final clean build | PASS - user supplied |
| Simulation | PASS - user supplied |
| HALSIM joystick | PASS - user supplied |
| Glass / DriverInput | PASS - user supplied |
| Real robot | NOT TESTED - hardware unavailable |
| Git | NOT RUN - user-owned |

## Closure and Scope Boundary

The software and simulation evidence is complete. This guide remains `IN_PROGRESS / NOT FINAL`
because AGENTS requires all required verification before guide finalization and the L20 ADR keeps
real-robot actuation safety gates in force. Simulation is not a substitute for that debt.

- L21 First Floor Drive Validation: not included.
- L22 Field-Relative Drive: not included.
- L23 Odometry and Pose Visualization: not included.
- L24 Pose Estimation and Autonomous Readiness: not included.
- Production-request Observation/telemetry: rejected and not included.
