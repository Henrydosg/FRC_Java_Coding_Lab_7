# S00_L19 to S00_L20: Robot-Relative Teleop Integration

Status: `FINAL / PASS`

This guide contains the reconciled implementation and verification history for
`S00_L20_RobotRelativeTeleopIntegration`. Its content is final through the post-fix clean build and
final closure audit. All required implementation and verification gates are complete.

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

An exact zero chassis request produces four independent zero-speed states at the corresponding
current measured FL/FR/BL/BR steer angles. The measured angles are refreshed each update; this is
not a persistent last-commanded-angle latch. Nonzero requests continue through the unchanged
kinematics, optimization, and desaturation path. At the CTRE IO boundary, zero drive velocity stops
only the drive motor; explicit and fail-closed full module stops still stop both drive and steer.

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

## Step 11 - Run the pre-correction full regression

- Step: 11
- Objective: Confirm L20 and inherited behavior remain coherent after both increments.
- Why: Focused tests do not replace repository-wide lesson regression.
- Action: Rerun the current full L20 regression suite.
- Files Changed: Generated test output only.
- Verification: PASS; the user explicitly supplied current full-regression PASS.
- Expected Result: No tested inherited behavior regresses under the then-current locked L20
  architecture.

## Step 12 - Run the historical clean build

- Step: 12
- Objective: Establish clean software-build evidence for the pre-correction implementation.
- Why: The then-current implementation required a clean build before hardware verification.
- Action: Run `gradlew clean build`.
- Files Changed: Regenerated build output only.
- Verification: PASS; user supplied `BUILD SUCCESSFUL`, 7 actionable tasks, 7 executed.
- Expected Result: The pre-correction L20 project builds successfully from a clean state. This
  evidence must not be represented as a clean build of the later post-fix source.

## Step 13 - Correct exact-zero measured-angle behavior

- Step: 13
- Objective: Prevent exact zero demand from creating a synthetic steer-angle jump.
- Why: Zero drive demand must not create unnecessary steering toward synthetic 0-degree or
  optimized targets.
- Action: In `SwerveOutputPipeline`, detect exact zero `ChassisSpeeds` before normal kinematics and
  return zero-speed states at the corresponding current measured module angles.
- Files Changed: `SwerveOutputPipeline.java`, `SwerveOutputPipelineTest.java`,
  `SwerveSubsystemFourModuleActuationTest.java`, and `RobotRelativeTeleopProductionPathTest.java`.
- Verification: Current repository test artifacts include the focused zero-demand coverage and
  record zero test failures.
- Expected Result: Exact zero demand commands zero drive and follows each current measured steer
  angle in FL/FR/BL/BR order; the nonzero pipeline remains unchanged.

## Step 14 - Separate zero drive stop from full module stop

- Step: 14
- Objective: Remove the CTRE cross-actuator side effect on zero drive velocity.
- Why: A zero drive request must not neutralize steer before the subsystem submits its steer target.
- Action: Change `SwerveModuleIOCTRE.setDriveVelocityMetersPerSecond(0.0)` to stop only the drive
  motor. Preserve full module stop for unhealthy/nonfinite requests and explicit safety/lifecycle
  paths.
- Files Changed: `SwerveModuleIOCTRE.java` and `SwerveModuleIOCTREStopSeparationTest.java`.
- Verification: The current repository artifacts record all 4 stop-separation tests passing as part
  of the 166/166 post-fix test result.
- Expected Result: Zero drive does not interrupt steer PositionVoltage control, while full module
  stop continues stopping both drive and steer.

## Step 15 - Investigate intermittent steer symptoms

- Step: 15
- Objective: Bound the intermittent BL/FL steer symptom without speculative software changes.
- Why: Closure must distinguish a reproducible production defect from a hardware or diagnostic
  symptom.
- Action: Audit zero-demand behavior, drive/steer stop separation, enable/disable lifecycle,
  nonzero motion, optimizer behavior, and steer feedback/configuration. The user mechanically
  reseated/tightened the encoder assembly before the final retest.
- Files Changed: None for the audits or mechanical user action.
- Verification: No absolute hardware root cause was established.
- Expected Result: Retain only the bounded diagnostic statement: probable mechanical
  encoder/mounting issue; symptom not reproduced after mechanical correction and post-fix
  verification.

## Step 16 - Execute post-fix real-robot verification

- Step: 16
- Objective: Verify L20 production behavior and safety on hardware.
- Why: Simulation and unit tests do not replace physical actuation verification.
- Action: Verify the robot on stands, then verify robot-relative driving on the floor.
- Files Changed: None; user-operated verification only.
- Verification: PASS from user-supplied evidence: Enable/Disable 10/10; Forward/Backward; Strafe
  Left/Right; Diagonal; Rotation CW/CCW; transition stress 3/3; floor driving; and no unintended
  module actuation. BL drift/jitter and FL jitter were not reproduced.
- Expected Result: Required real-robot actuation and safety evidence is complete without claiming
  an absolute cause for the prior intermittent symptom.

## Step 17 - Inspect current post-fix test results

- Step: 17
- Objective: Confirm the repository's current test artifacts cover the corrective changes.
- Why: The historical clean build predates those production changes.
- Action: Read the generated JUnit XML results without rerunning Gradle.
- Files Changed: None.
- Verification: PASS; 166/166 tests, zero failures, zero errors, and zero skips. Included are 11
  command tests, 10 production-path tests, 15 output-pipeline tests, and 4 CTRE stop-separation
  tests.
- Expected Result: The current post-fix test suite has recorded passing evidence.

## Step 18 - Complete the final closure architecture audit

- Step: 18
- Objective: Confirm the delivered L20 design remains inside its locked architecture and scope.
- Why: Final review must ensure verification work did not introduce forbidden production behavior.
- Action: Confirm robot-relative-only control; one authoritative immutable sample; exact-zero
  measured-angle behavior; drive-only zero-stop correction; full-stop safety; production telemetry
  and Observation boundaries unchanged; test-only recording IO; and no L21/L22,
  `SwerveModuleIOSim`, odometry, pose, or Frozen Backbone expansion.
- Files Changed: L20 documentation was reconciled; Java and tests were not changed by this audit.
- Verification: PASS for architecture preservation. No unresolved production correctness defect was
  found. Technical closure remains subject only to the final post-fix clean build recorded in the
  next step.
- Expected Result: Production architecture is locked and documentation is ready for closure review,
  with the final clean-build gate explicitly identified.

## Step 19 - Run the final post-fix clean build

- Step: 19
- Objective: Establish clean-build evidence for the final corrected L20 source.
- Why: The previous clean build predated the exact-zero measured-angle and CTRE drive/steer stop
  separation corrections.
- Action: Run `gradlew clean build` after both final production corrections.
- Files Changed: Regenerated build output only.
- Verification: PASS; user supplied `BUILD SUCCESSFUL in 35s`, 7 actionable tasks, 7 executed, and
  confirmed that all tests executed by the clean build passed.
- Expected Result: The final L20 production source and tests build successfully from a clean state.

## Verification Summary

| Gate | State |
| --- | --- |
| Focused command tests | PASS - user supplied 11/11 |
| Focused production-path tests | PASS - user supplied 10/10 |
| Current post-fix tests | PASS - repository artifacts, 166/166 |
| Historical clean build | PASS - user supplied before final corrections |
| Final post-fix clean build | PASS - user supplied |
| Simulation | PASS - user supplied |
| HALSIM joystick | PASS - user supplied |
| Glass / DriverInput | PASS - user supplied |
| Robot on stands | PASS - user supplied |
| Floor verification | PASS - user supplied |
| Git | NOT RUN - user-owned |

## Closure and Scope Boundary

Architecture, implementation, current post-fix tests, final post-fix clean build,
Simulation/HALSIM/Glass, real-robot safety and motion verification, diagnostic reconciliation, and
documentation are complete. This guide is `FINAL / PASS`.

Git remains user-owned and has not run. Under Document B, the clear Git commit is still required
before the lesson itself may be marked `COMPLETE / FROZEN / READ-ONLY`.

- L21 First Floor Drive Validation: not included.
- L22 Field-Relative Drive: not included.
- L23 Odometry and Pose Visualization: not included.
- L24 Pose Estimation and Autonomous Readiness: not included.
- Production-request Observation/telemetry: rejected and not included.
