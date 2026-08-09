# Lesson Status

## Identity

- Lesson: `S00_L20_RobotRelativeTeleopIntegration`
- Previous Lesson: `S00_L19_DriverInputProcessing`
- Status: `IN_PROGRESS`
- Lesson Goal: Robot-Relative Teleop Integration
- Architecture Decision: `ADR_S00_L19_L20_Driver_Input_Ownership.md` (`APPROVED`)
- Next Roadmap Lesson: `S00_L21_FirstFloorDriveValidation` (`OUT OF SCOPE`)

## Verification Record

| Required Field | State | Evidence |
| --- | --- | --- |
| Architecture Review | PASS | ChatGPT Architect locked the Increment 1 production architecture, rejected the proposed production-request Observation, approved test-only end-to-end verification, and confirmed the production architecture remains locked. |
| Baseline Build | PASS | User-supplied L19 to L20 baseline `clean build` evidence. |
| Focused Command Tests | PASS | User explicitly supplied `RobotRelativeTeleopDriveCommandTest`: 11/11 PASS. |
| Focused Production-Path Tests | PASS | User explicitly reran `RobotRelativeTeleopProductionPathTest`: 10/10 PASS. |
| Full Regression | PASS | User explicitly reran the current full regression and reported PASS. |
| Build | PASS | User ran `gradlew clean build`; result `BUILD SUCCESSFUL`, 7 actionable tasks, 7 executed. |
| Simulation | PASS | User supplied final Simulation PASS evidence. |
| Driver Station / Glass | PASS | User supplied HALSIM joystick PASS and Glass / DriverInput PASS evidence. |
| Real Robot | NOT TESTED | Hardware unavailable. Simulation does not substitute for real-hardware verification; the required real-robot verification debt remains open. |
| Transition Guide | NOT TESTED | The guide is reconciled through the final architecture review but remains `IN_PROGRESS / NOT FINAL` because required real-robot verification is missing. |
| Git Commit | USER-OWNED / NOT RUN | No L20 Git action was run by Codex. |
| Git Push | USER-OWNED / NOT RUN | No L20 Git action was run by Codex. |

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

`RobotRelativeTeleopDriveCommand.execute()` acquires exactly one authoritative driver-input sample.
The same immutable `DriverInputObservation` is used for control scaling and driver-input telemetry.
`RobotTelemetry` does not independently poll Xbox.

Approved L20 baseline limits:

- Translation: `1.0 m/s`
- Rotation: `1.0 rad/s`

The test-only `RobotRelativeTeleopProductionPathTest` verifies the real production command,
subsystem, kinematics, optimization, desaturation, module identity/order, lifecycle safety, and
four-module output path using recording test IO.

## Completed Transition Evidence

1. Copied the completed/frozen L19 lesson.
2. Renamed the copy to `S00_L20_RobotRelativeTeleopIntegration`.
3. Deleted copied `build/` and `.gradle/` artifacts and confirmed `build/` was absent.
4. Completed the baseline clean build with user-supplied PASS evidence.
5. Completed the pre-implementation architecture audit and governance reconciliation.
6. Implemented the locked Increment 1 production path.
7. Completed the observability governance review; production-request Observation/telemetry was rejected.
8. Added the approved test-only end-to-end production-path verification.
9. Recorded user-supplied focused, regression, clean-build, Simulation, HALSIM, and Glass evidence.

## Known Issues / Verification Debt

- Real Robot Verification: `NOT TESTED - hardware unavailable`.
- Required real-hardware actuation and safety verification must be executed later when hardware is
  available. Simulation is not recorded as a substitute.
- The copied Java comment describing the Xbox port as L19-specific remains stale. It does not define
  governance and was not changed because production Java is locked.
- User-owned Git commit and push have not run.

## Closure Gate

Software, focused-test, full-regression, clean-build, Simulation, HALSIM, and Glass verification are
complete based on user-supplied evidence. The lesson remains `IN_PROGRESS` because the L20 ADR keeps
required real-robot actuation safety gates in force, AGENTS places real-robot verification before
documentation finalization, and Architect closure has not been granted. Do not start L21.
