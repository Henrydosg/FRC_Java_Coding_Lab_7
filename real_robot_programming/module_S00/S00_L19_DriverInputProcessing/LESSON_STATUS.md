# Lesson Status

## Identity

- Lesson: `S00_L19_DriverInputProcessing`
- Previous Lesson: `S00_L18_FourModuleStateActuation`
- Status: `IN_PROGRESS`
- Architecture decision: `ADR_S00_L19_L20_Driver_Input_Ownership.md` (`APPROVED`)

## Verification Record

| Required Field | State | Evidence |
| --- | --- | --- |
| Architecture Review | PASS | Approved non-actuating pipeline and L19-only telemetry composition; current source matches the ADR. |
| Baseline Build | PASS | Recorded L18 -> L19 baseline `clean build` evidence. |
| Build | NOT TESTED | `compileJava` is PASS, but the current closure clean build has not been supplied. |
| Simulation | PASS | Current user-supplied evidence. |
| Driver Station / Glass | PASS | Glass PASS is supplied; real-roboRIO Driver Station verification remains pending. |
| Real Robot | NOT TESTED | Disabled, non-actuating real-roboRIO verification is required and pending. |
| Transition Guide | NOT TESTED | Maintained during the lesson, but not final and not eligible for PASS. |
| Git Commit | USER-OWNED / NOT RUN | User-owned; Codex did not run Git. |
| Git Push | USER-OWNED / NOT RUN | User-owned; Codex did not run Git. |

## Approved L19 Scope

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

`RobotTelemetry` may synchronously call `XboxDriverInputSource.read()` in L19 only because this
lesson is strictly non-actuating, as approved by the ADR. L19 contains no drive/default command,
`ChassisSpeeds`, `SwerveSubsystem` drive request, module-state generation, or drivetrain actuation.
Actuation verification is `NOT APPLICABLE`.

## Detailed Current Evidence

| Item | Current state | Evidence |
| --- | --- | --- |
| `compileJava` | PASS | Current user-supplied evidence. |
| Processor current 14-test run | NOT TESTED | The test file now contains 14 tests; only the earlier 11/11 run is verified. |
| Xbox source tests | PASS | Current user-supplied evidence: 2/2 PASS. |
| Telemetry facade test | PASS | Current user-supplied evidence. |
| Full regression current run | NOT TESTED | Earlier PASS predates the latest processor-test additions. |
| Clean build current run | NOT TESTED | No current result has been supplied. |
| Simulation | PASS | Current user-supplied evidence. |
| Glass | PASS | Current user-supplied evidence. |
| AdvantageScope | PASS | Current user-supplied evidence. |
| `/DriverInput` visible and updating in Simulation | PASS | Current user-supplied evidence. |
| Real roboRIO | NOT TESTED | Required Disabled verification is pending. |

## Required Real-Robot Closure Verification

With the robot Disabled, confirm `/DriverInput` exists before an Xbox is connected; connect the
Xbox controller on USB port `0`; inspect Raw, SemanticRaw, and Processed values; verify axis signs,
processing, and zero near center/deadband; and confirm absolutely no drivetrain actuation.

## L20 Boundary

Before actuation in L20, establish exactly one authoritative driver-input sample per control cycle.
Telemetry and drive control must not independently poll Xbox. Telemetry must publish that same sample
or a documented immutable projection. L20 remains Robot-Relative Teleop Integration; this requirement
does not change the roadmap or Frozen Backbone.

## Known Issues / Pending Work

- Run all 14 current processor tests and record the result.
- Rerun the full regression after the latest test additions.
- Run and record the current clean build.
- Complete the required Disabled, non-actuating real-roboRIO verification.
- Finalize the transition guide and mark it PASS only after required verification is complete.
- Keep the lesson `IN_PROGRESS`; do not close or freeze it yet.
