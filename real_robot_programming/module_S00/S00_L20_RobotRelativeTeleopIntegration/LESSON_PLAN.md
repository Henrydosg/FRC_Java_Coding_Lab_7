# S00_L20 Robot-Relative Teleop Integration - Lesson Plan

## Metadata

- Framework Version: 2.1
- Lesson: `S00_L20_RobotRelativeTeleopIntegration`
- Previous Lesson: `S00_L19_DriverInputProcessing`
- Source Lesson Status: `COMPLETE / FROZEN / READ-ONLY`
- Status: `IN_PROGRESS`
- Architecture Decision: `ADR_S00_L19_L20_Driver_Input_Ownership.md`
- Next Roadmap Lesson: `S00_L21_FirstFloorDriveValidation` (`OUT OF SCOPE`)

## Lesson Goal

Integrate processed driver intent with the existing production Swerve request path using
robot-relative `ChassisSpeeds`:

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

The command owns exactly one authoritative sample per execution. Control scaling and driver-input
telemetry use that same immutable sample. The approved baseline limits are `1.0 m/s` translation and
`1.0 rad/s` rotation.

## Locked Architecture

- Preserve the Frozen Backbone and mechanism observation flow.
- Keep `RobotContainer` as composition root only.
- Keep telemetry read-only and prevent independent Xbox polling by `RobotTelemetry`.
- Keep vendor APIs inside IO implementations.
- Use robot-relative `ChassisSpeeds`; do not add field-relative conversion.
- Do not add production-request Observation or requested chassis/module telemetry.
- Do not add `SwerveModuleIOSim`, odometry, pose, L21, or L22 behavior.
- Keep L19 Java and tests frozen.

## Completed Work

1. Inherited and renamed the completed/frozen L19 lesson.
2. Removed copied generated artifacts and confirmed `build/` was absent.
3. Recorded the user-supplied baseline clean-build PASS and initialization scope evidence.
4. Completed the pre-implementation audit and governance reconciliation.
5. Received the Increment 1 architecture lock.
6. Implemented one command-owned Xbox sample per execution, same-sample telemetry, robot-relative
   scaling, fail-safe command lifecycle, and default-command wiring.
7. Added and verified 11 focused command tests.
8. Completed Simulation, HALSIM joystick, and Glass / DriverInput verification.
9. Completed the governance review rejecting production-request Observation/telemetry.
10. Added the test-only `RobotRelativeTeleopProductionPathTest`, which traverses the real production
    kinematics, optimization, and desaturation path with recording module IO.
11. User explicitly reran the end-to-end suite with 10/10 PASS.
12. User explicitly reran the current full regression with PASS.
13. User ran `gradlew clean build`; result `BUILD SUCCESSFUL`, 7 actionable tasks executed.
14. Production architecture was confirmed locked for final Architect review.

## Remaining Work

1. Perform required real-robot actuation and safety verification when hardware becomes available.
2. Obtain ChatGPT Architect lesson-closure approval after reviewing the explicit verification debt.
3. Finalize the transition-guide status only when all required verification is complete.
4. Leave Git commit and push to the user.

## Explicitly Out of Scope

- L21 First Floor Drive Validation or tuning claims.
- L22 field-relative drive or gyro-based frame conversion.
- L23 odometry or pose visualization.
- L24 pose estimation, trajectory, or autonomous-readiness work.
- Production request telemetry, simulated physics, or Frozen Backbone changes.

## Closure Position

The software and simulation portion of L20 is verified. L20 remains `IN_PROGRESS`; real-robot
verification is `NOT TESTED - hardware unavailable`, and Simulation is not a substitute. Do not begin
L21.
