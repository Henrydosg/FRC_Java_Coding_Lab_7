# Lesson Status

## Identity

- Lesson: `S00_L20_RobotRelativeTeleopIntegration`
- Previous Lesson: `S00_L19_DriverInputProcessing`
- Status: `COMPLETE`
- Freeze State: `FROZEN / READ-ONLY`
- Lesson Goal: Robot-Relative Teleop Integration
- Architecture Decision: `ADR_S00_L19_L20_Driver_Input_Ownership.md` (`APPROVED`)
- Next Roadmap Lesson: `S00_L21_FirstFloorDriveValidation` (`OUT OF SCOPE`)

## Verification Record

| Required Field | State | Evidence |
| --- | --- | --- |
| Architecture Review | PASS | Final closure audit found the Frozen Backbone, composition-root boundary, robot-relative control path, single-sample ownership, telemetry boundary, IO boundary, and lifecycle safety preserved. No unresolved production correctness defect was found. |
| Baseline Build | PASS | User-supplied L19 to L20 baseline `clean build` evidence. |
| Focused Command Tests | PASS | User explicitly supplied `RobotRelativeTeleopDriveCommandTest`: 11/11 PASS. |
| Focused Production-Path Tests | PASS | User explicitly reran `RobotRelativeTeleopProductionPathTest`: 10/10 PASS. |
| Current Post-Fix Test Results | PASS | Repository test-result artifacts generated after both corrective changes record 166/166 tests passing with zero failures, errors, or skips, including 11 command tests, 10 production-path tests, 15 output-pipeline tests, and 4 CTRE stop-separation tests. |
| Historical Clean Build | PASS | User previously ran `gradlew clean build`; result `BUILD SUCCESSFUL`, 7 actionable tasks, 7 executed. This evidence predates the two final production corrections. |
| Final Post-Fix Clean Build | PASS | User ran the clean build after both final production corrections: `BUILD SUCCESSFUL in 35s`; 7 actionable tasks, 7 executed; all tests executed by the clean build passed. |
| Simulation | PASS | User supplied final Simulation PASS evidence. |
| Driver Station / Glass | PASS | User supplied HALSIM joystick PASS and Glass / DriverInput PASS evidence. |
| Real Robot | PASS | User supplied post-fix robot-on-stands and floor verification evidence: Enable/Disable 10/10, all requested robot-relative motion directions, transition stress 3/3, correct floor driving, and no unintended module actuation. |
| Transition Guide | PASS | `docs/S00_L19_to_S00_L20_Step_by_Step.md` is reconciled and finalized as `FINAL / PASS`. |
| Git Commit | PASS | User supplied commit `216ee4d` - `Complete S00_L20 robot-relative teleop integration`; working tree was CLEAN before this documentation reconciliation. |
| Git Push | USER-OWNED / NOT TESTED | No Git push evidence was supplied; Codex did not run Git. |

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

For an exact zero robot-relative chassis request, `SwerveOutputPipeline` returns four independent
zero-speed states whose angles are copied from the corresponding current measured module angles in
FL/FR/BL/BR order. This is a per-update measured-angle policy, not a retained last-commanded-angle
policy. Normal nonzero requests still use kinematics, optimization, and desaturation.

`SwerveModuleIOCTRE.setDriveVelocityMetersPerSecond(0.0)` now stops only the drive motor and does
not interrupt steer position control. Nonfinite/unhealthy drive requests and explicit module,
subsystem, Disabled, interruption, and commissioning stop paths remain fail-closed through the full
module stop that stops both drive and steer.

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
10. Corrected exact-zero output behavior to command zero drive while using each module's current
    measured steer angle.
11. Corrected CTRE zero-drive handling so zero drive velocity stops only drive, while full safety
    stops continue stopping drive and steer.
12. Recorded current post-fix repository test artifacts: 166/166 PASS, zero failures/errors/skips.
13. Completed post-fix robot-on-stands and floor verification with user-supplied PASS evidence.
14. Completed the final closure architecture audit with no unresolved production correctness defect.
15. Completed the final post-fix clean build with user-supplied PASS evidence: `BUILD SUCCESSFUL in
    35s`, 7 actionable tasks executed, and all clean-build tests passed.
16. Finalized the L19-to-L20 transition guide as `FINAL / PASS`.
17. Recorded user-owned Git commit `216ee4d` (`Complete S00_L20 robot-relative teleop integration`)
    and the user-supplied CLEAN working-tree state before final governance reconciliation.
18. Marked L20 `COMPLETE / FROZEN / READ-ONLY`.

## Known Issues / Verification Debt

- Final post-fix clean build: `PASS` using the user-supplied evidence recorded above.
- Real Robot Verification: `PASS` using only the user-supplied post-fix evidence recorded above.
- Diagnostic closure: "Probable mechanical encoder/mounting issue; symptom not reproduced after
  mechanical correction and post-fix verification." This is not an absolute hardware root-cause
  claim.
- FL jitter was not reproduced during the supplied post-fix verification.
- The copied Java comment describing the Xbox port as L19-specific remains stale. It does not define
  governance and was not changed because production Java is locked.
- Git push evidence was not supplied. No push is claimed.

## Closure Gate

Architecture, implementation, current regression, final post-fix clean build, Simulation, HALSIM,
Glass, required real-robot verification, safety review, diagnostic reconciliation, final transition
guide, and the required clear Git commit are complete based on recorded evidence. No unresolved L20
production correctness blocker remains. L20 is `COMPLETE / FROZEN / READ-ONLY`. Do not modify L20
Java/tests and do not start L21.
