# S00_L21 — First Floor Drive Validation

## Lesson State

- Status: `COMPLETE / FROZEN / READ-ONLY`
- Previous lesson: `S00_L20_RobotRelativeTeleopIntegration` — `COMPLETE / FROZEN / READ-ONLY`
- Next lesson: `S00_L22_FieldRelativeDrive` — `OUT OF SCOPE`

## Objective and Architecture

L21 validated the existing robot-relative production drivetrain on the real floor under load. It remained validation-only with architecture delta `NONE`; no production defect was found and no production correction was made.

The inherited path is:

`XboxController` → `XboxDriverInputSource` → `DriverInputProcessor` → immutable `DriverInputObservation` → `RobotRelativeTeleopDriveCommand` → robot-relative `ChassisSpeeds` → `SwerveSubsystem` → `SwerveOutputPipeline` → `SwerveModuleIO` → `SwerveModuleIOCTRE` → hardware.

Translation remains capped at `1.0 m/s`, rotation at `1.0 rad/s`, and exact zero demand holds current measured module angles while commanding zero drive.

## Final Verification Matrix

| Gate | Result |
|---|---|
| Centered Enable | PASS |
| Forward | 3/3 PASS |
| Backward | 3/3 PASS |
| Strafe Left/Right | PASS |
| Diagonal | PASS |
| Rotation CW/CCW | PASS |
| Translation + Rotation | PASS |
| Zero / Stop / Transition | PASS; release-to-zero 3/3, Enable/Disable 10/10, Motion → center → Disable 3/3 |
| Final Floor Confidence | PASS |
| Final clean build | PASS — `BUILD SUCCESSFUL in 19s`; 7/7 tasks |
| Full regression | PASS |
| Simulation / HALSIM runtime smoke | PASS |

No BL/FL jitter or drift was reproduced. No evidence justified tuning or production correction.

## Scope Result

Production Java delta: `NONE`. Test delta: `NONE`. IO/configuration/tuning delta: `NONE`. Frozen Backbone, robot-relative control, vendor-neutral boundaries, and RobotContainer composition-root role were preserved. No field-relative or L22 work entered L21.

## Remaining Technical Debt

Cosine compensation, steer-alignment gating, slew/acceleration limiting, PID/NeutralMode/current-limit changes, encoder-offset/gearing/inversion changes, simulated floor physics, signal-age policy, and mechanism telemetry remain deferred.

## Transition Guide

See [`docs/S00_L20_to_S00_L21_Step_by_Step.md`](docs/S00_L20_to_S00_L21_Step_by_Step.md): `FINAL / PASS`.

## Closure Boundary

L21 is `COMPLETE / FROZEN / READ-ONLY`. User-created commit `5d1cc1f` (`Complete S00_L21 first floor drive validation`) is recorded; the working tree was CLEAN before reconciliation. Git push evidence was not supplied; no unresolved L21 correctness blocker remains.
