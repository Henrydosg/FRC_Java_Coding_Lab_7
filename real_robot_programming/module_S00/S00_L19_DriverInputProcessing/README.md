# S00_L19 Driver Input Processing

## Lesson State

`IN_PROGRESS`

Previous lesson: `S00_L18_FourModuleStateActuation` (`COMPLETE / FROZEN / READ-ONLY`)

## Approved L19 Scope

S00_L19 adds one non-actuating, observable driver-input pipeline:

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

The semantic mapping is `forward = -LeftY`, `strafe = -LeftX`, and
`rotation = -RightX`. Each semantic axis is processed independently through finite-value safety,
WPILib deadband rescaling with a `0.10` deadband, signed-square shaping, and clamping to
`[-1.0, +1.0]`.

`DriverInputObservation` is an immutable, vendor-neutral snapshot containing raw controller values,
semantic raw values, and processed values. The production telemetry table is `/DriverInput`, with
`Raw`, `SemanticRaw`, and `Processed` subtables.

## Non-Actuating Boundary

L19 does not add or use driver input for any of the following:

- a drive or default command;
- `ChassisSpeeds` generation;
- a `SwerveSubsystem` drive request;
- Swerve module-state generation; or
- drivetrain actuation.

Inherited Swerve code remains present, but the L19 driver-input pipeline only publishes observation
data. Actuation verification is therefore `NOT APPLICABLE` to L19.

## Approved Architecture Decision

The approved
`docs/architecture_decisions/ADR_S00_L19_L20_Driver_Input_Ownership.md` permits
`RobotTelemetry` to synchronously call `XboxDriverInputSource.read()` in S00_L19 only because this
lesson is strictly non-actuating. This is a lesson-specific composition decision, not a general
telemetry permission and not a change to the Frozen Backbone.

Before any driver input may actuate Swerve in S00_L20, L20 must establish exactly one authoritative
driver-input sample per control cycle. Telemetry and drive control must not independently poll Xbox;
telemetry must publish that same sample or a documented immutable projection of it.

## Current Verification Truth

| Item | Current state | Evidence |
| --- | --- | --- |
| Baseline clean build | PASS | Recorded inheritance baseline evidence. |
| Architecture review | PASS | Approved non-actuating scope and ADR; current source matches the boundary. |
| `compileJava` | PASS | Current user-supplied evidence. |
| `DriverInputProcessorTest` current 14-test run | NOT TESTED | The file now contains 14 tests. Earlier evidence covers 11/11 before the latest three additions. |
| `XboxDriverInputSourceTest` | PASS | Current user-supplied evidence: 2/2 PASS. |
| `DriverInputTelemetryFacadeTest` | PASS | Current user-supplied evidence. |
| Full regression current run | NOT TESTED | Earlier regression PASS predates the latest processor-test additions. |
| Clean build current run | NOT TESTED | No current post-addition clean-build result has been supplied. |
| Simulation | PASS | Current user-supplied evidence. |
| Glass | PASS | Current user-supplied evidence. |
| AdvantageScope | PASS | Current user-supplied evidence. |
| `/DriverInput` visible and updating in Simulation | PASS | Current user-supplied evidence. |
| Real roboRIO, Disabled and non-actuating | NOT TESTED | Required closure verification is pending. |
| Actuation verification | NOT APPLICABLE | L19 is strictly non-actuating. |

## Required Real-Robot Verification

The user must complete the following on the real roboRIO while the robot remains **Disabled**:

1. Confirm `/DriverInput` exists before an Xbox controller is connected.
2. Connect the Xbox controller on USB port `0`.
3. Confirm `/DriverInput/Raw`, `/DriverInput/SemanticRaw`, and `/DriverInput/Processed` update.
4. Verify forward, strafe, and rotation axis signs match the semantic mapping.
5. Verify deadband and shaping behavior, including zero output near controller center.
6. Confirm absolutely no drivetrain actuation occurs.

## Closure State

L19 is not ready for `COMPLETE / FROZEN`. The current 14-test processor run, current full
regression, current clean build, required Disabled real-roboRIO verification, and transition-guide
finalization remain pending. Git commit and push remain user-owned and have not been run by Codex.
