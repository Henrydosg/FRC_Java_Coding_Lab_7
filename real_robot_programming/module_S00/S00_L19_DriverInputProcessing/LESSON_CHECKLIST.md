# S00_L19 Driver Input Processing Checklist

Status: `IN_PROGRESS`

Source lesson: `S00_L18_FourModuleStateActuation` - `COMPLETE / FROZEN / READ-ONLY`

## Scope and Implementation

| Item | State | Evidence |
| --- | --- | --- |
| Inherit frozen S00_L18 | PASS | L19 is an independent copy; L18 remains read-only. |
| Rename lesson and remove copied generated artifacts | PASS | Recorded transition evidence. |
| Baseline clean build | PASS | Recorded pre-implementation baseline evidence. |
| Architecture audit | PASS | Approved L19 non-actuating pipeline and ADR. |
| Driver-input constants | PASS | Deadband and normalized bounds are in `Constants.java`. |
| Pure controls processor | PASS | Current source implements finite safety, deadband, signed square, and clamp. |
| Xbox acquisition and semantic mapping | PASS | Current source maps `-LeftY`, `-LeftX`, and `-RightX`. |
| Immutable `DriverInputObservation` | PASS | Current record contains raw, semantic raw, and processed values. |
| Read-only driver-input telemetry | PASS | Current facade publishes typed values under `/DriverInput`. |
| L19-only `RobotTelemetry.read()` composition | PASS | Approved by the ADR for this strictly non-actuating lesson. |
| Drive/default command | NOT APPLICABLE | Explicitly excluded from L19. |
| `ChassisSpeeds` | NOT APPLICABLE | Explicitly excluded from L19. |
| `SwerveSubsystem` drive request | NOT APPLICABLE | Explicitly excluded from L19. |
| Module-state generation | NOT APPLICABLE | Explicitly excluded from L19. |
| Drivetrain actuation | NOT APPLICABLE | Explicitly excluded from L19. |

## Verification

| Item | State | Evidence / pending action |
| --- | --- | --- |
| `compileJava` | PASS | Current user-supplied evidence. |
| `DriverInputProcessorTest` current 14-test run | NOT TESTED | Previous 11/11 PASS predates three added boundary tests. |
| `XboxDriverInputSourceTest` | PASS | Current user-supplied evidence: 2/2 PASS. |
| `DriverInputTelemetryFacadeTest` | PASS | Current user-supplied evidence. |
| Full regression current run | NOT TESTED | Previous PASS predates the processor-test additions. |
| Clean build current run | NOT TESTED | Current post-addition result has not been supplied. |
| Simulation | PASS | Current user-supplied evidence. |
| Glass | PASS | Current user-supplied evidence. |
| AdvantageScope | PASS | Current user-supplied evidence. |
| `/DriverInput` visible and updating in Simulation | PASS | Current user-supplied evidence. |
| Real roboRIO, Disabled and non-actuating | NOT TESTED | Required closure verification is pending. |
| Actuation verification | NOT APPLICABLE | L19 must not actuate Swerve. |

## Required Real-roboRIO Checklist

- [ ] Keep the robot Disabled for the entire verification.
- [ ] Confirm `/DriverInput` exists before the Xbox controller is connected.
- [ ] Connect the Xbox controller on USB port `0`.
- [ ] Verify `/DriverInput/Raw` values.
- [ ] Verify `/DriverInput/SemanticRaw` values and axis signs.
- [ ] Verify `/DriverInput/Processed` values and processing behavior.
- [ ] Verify processed values remain zero near controller center and inside the deadband.
- [ ] Confirm absolutely no drivetrain actuation occurs.

## Documentation and Closure

| Item | State | Evidence / pending action |
| --- | --- | --- |
| Transition guide maintained during lesson | IN PROGRESS | The guide reflects the current implementation and evidence. |
| Transition guide finalization | NOT TESTED | Finalize after all required verification. |
| Transition guide final PASS | NOT TESTED | Do not mark PASS until the guide is final. |
| Lesson closure review | NOT TESTED | Current regression, clean build, and real-roboRIO evidence are pending. |
| Lesson freeze | NOT TESTED | Status must remain IN_PROGRESS. |
| Git commit / push | USER-OWNED / NOT RUN | User-owned after closure; Codex does not run Git. |

## L20 Migration Gate

Before any driver input may actuate Swerve, L20 must establish exactly one authoritative
driver-input sample per control cycle. Telemetry and drive control must not independently poll Xbox;
telemetry must publish the same sample or a documented immutable projection. This is an ADR migration
constraint, not a roadmap or Frozen Backbone change.
