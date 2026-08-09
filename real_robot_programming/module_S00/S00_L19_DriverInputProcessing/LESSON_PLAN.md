# S00_L19 Driver Input Processing - Lesson Plan

## Metadata

- Framework Version: 2.1
- Lesson: `S00_L19_DriverInputProcessing`
- Previous Lesson: `S00_L18_FourModuleStateActuation`
- Source Lesson Status: `COMPLETE / FROZEN / READ-ONLY`
- Status: `IN_PROGRESS`
- Architecture decision: `ADR_S00_L19_L20_Driver_Input_Ownership.md` (`APPROVED`)

## Lesson Concept

Build and verify a non-actuating observable driver-input pipeline:

```text
XboxController
  -> XboxDriverInputSource
  -> semantic axis mapping
  -> DriverInputProcessor
  -> immutable DriverInputObservation
  -> RobotTelemetry
  -> DriverInputTelemetryFacade
  -> NT4
```

The implemented semantic mapping is `forward = -LeftY`, `strafe = -LeftX`, and
`rotation = -RightX`. The processor applies finite-value safety, deadband `0.10` with WPILib
rescaling, signed-square shaping, and clamping to `[-1.0, +1.0]`.

## Architecture Boundary

- External human/operator input may produce the immutable, vendor-neutral
  `DriverInputObservation` from one coherent controller sample under AGENTS.md v1.2.
- This external-input rule does not authorize controls to create mechanism Observations.
- The mechanism path remains `hardware -> IOInputs -> subsystem/estimator -> immutable Observation`.
- `RobotTelemetry` may synchronously call `XboxDriverInputSource.read()` in L19 only under the
  approved ADR because this pipeline does not actuate anything.
- `RobotContainer` remains the composition root; it constructs and injects the input source and
  telemetry facade without performing input processing.
- Telemetry publishes the immutable observation and performs no behavior control.

## Explicitly Excluded from L19

- drive or default command;
- `ChassisSpeeds`;
- `SwerveSubsystem` drive request;
- Swerve module-state generation;
- drivetrain actuation; and
- actuation verification.

## Implemented Work

1. Inherited the frozen S00_L18 project, renamed it for L19, removed generated artifacts, and
   established the recorded baseline build.
2. Completed the architecture audit and approved the lesson-specific ownership decision in the ADR.
3. Added the normalized driver-input constants and pure `DriverInputProcessor`.
4. Added `XboxDriverInputSource` with explicit semantic axis mapping.
5. Added immutable `DriverInputObservation` for raw, semantic raw, and processed values.
6. Added `DriverInputTelemetryFacade` and the L19-only synchronous composition in
   `RobotTelemetry`.
7. Added focused processor, Xbox source, and telemetry facade tests.
8. Verified the observable pipeline in Simulation, Glass, AdvantageScope, and `/DriverInput` using
   the current user-supplied evidence.

## Remaining Work

1. Run the current 14-test `DriverInputProcessorTest` file and record the result.
2. Rerun the full regression after the latest processor-test additions.
3. Run and record the current clean build.
4. Complete the required Disabled, non-actuating real-roboRIO verification.
5. Reconcile final evidence, finalize the transition guide, and mark the guide PASS only when final.
6. Perform closure review before changing the lesson from `IN_PROGRESS` to `COMPLETE / FROZEN`.
7. Leave commit and push to the user after closure evidence is complete.

## Verification Matrix

| Item | Current state | Evidence / required action |
| --- | --- | --- |
| L18 source frozen | PASS | Previous lesson remains COMPLETE / FROZEN / READ-ONLY. |
| L18 -> L19 inheritance | PASS | Copy, rename, and generated-artifact removal are recorded. |
| Baseline clean build | PASS | Recorded pre-implementation baseline evidence. |
| Architecture review | PASS | Current scope and L19-only composition are approved by ADR. |
| `compileJava` | PASS | Current user-supplied evidence. |
| Processor current 14-test run | NOT TESTED | Previous 11/11 PASS does not cover the three latest tests. |
| Xbox source tests | PASS | Current user-supplied evidence: 2/2 PASS. |
| Telemetry facade test | PASS | Current user-supplied evidence. |
| Full regression current run | NOT TESTED | Must be rerun after the processor-test additions. |
| Clean build current run | NOT TESTED | Current closure run has not been supplied. |
| Simulation | PASS | Current user-supplied evidence. |
| Glass | PASS | Current user-supplied evidence. |
| AdvantageScope | PASS | Current user-supplied evidence. |
| `/DriverInput` in Simulation | PASS | Visible and updating under Raw/SemanticRaw/Processed. |
| Real roboRIO | NOT TESTED | Required Disabled, non-actuating verification is pending. |
| Actuation verification | NOT APPLICABLE | Actuation is outside L19. |
| Transition guide | NOT TESTED | Maintained during the lesson; not final and not PASS. |
| Git commit / push | USER-OWNED / NOT RUN | Codex does not run Git. |

## L20 Migration Constraint

L20 remains Robot-Relative Teleop Integration. Before actuation, it must establish exactly one
authoritative driver-input sample per control cycle. Telemetry and drive control must not
independently poll Xbox. Telemetry must publish that same sample or a documented immutable
projection. This constraint does not alter the roadmap or authorize a Frozen Backbone change.

## Required Real-Robot Plan

Keep the robot Disabled. Confirm `/DriverInput` exists before the controller is connected, connect
the Xbox controller on USB port `0`, inspect Raw/SemanticRaw/Processed values, verify axis signs and
processing, verify zero near center/deadband, and confirm absolutely no drivetrain actuation.
