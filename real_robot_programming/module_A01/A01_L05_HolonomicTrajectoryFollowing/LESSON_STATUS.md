# Lesson Status

## Identity

- Module: A01 - Autonomous Navigation and Path Following
- Lesson: A01_L05_HolonomicTrajectoryFollowing
- Previous Lesson: A01_L04_FieldAndAllianceTransformContract
- Previous Lesson State: COMPLETE / FROZEN / READ-ONLY
- Status: COMPLETE
- Active State: COMPLETE / FROZEN / READ-ONLY
- Freeze State: FROZEN
- Lesson Goal: follow one sampled trajectory with bounded holonomic control while preserving explicit alliance/start-pose provenance and centralized safety stop.

## Gates and Evidence

- Inheritance Baseline: PASS - user verified L05 `src` byte-identical to frozen L04.
- Directory Identity: PASS - `A01_L05_HolonomicTrajectoryFollowing` matches the A01 ADR.
- Baseline Build: PASS - user supplied inherited baseline BUILD SUCCESSFUL.
- Architecture Audit: PASS.
- Design-Lock Blocker Resolution: PASS - approved alliance-aware start-context design.
- Implementation: COMPLETE - the approved L05 follower, alliance-aware start context/reset, composition, and focused tests are present.
- Java Verification: PASS - `compileJava` and `compileTestJava` passed under the installed WPILib Java 17 runtime.
- Focused L05 Tests: PASS - 32/32 passed.
- Inherited L01-L04 Regression: PASS - 57/57 focused inherited tests passed.
- Full Tests: PASS - 401/401 passed; zero failures, errors, or skips.
- Build: PASS - final clean build completed with all seven reported tasks executed.
- Simulation: PASS - user supplied Blue and Red autonomous trajectory evidence; integration tests also passed.
- Driver Station / Glass: PASS - user supplied communication/health, localization, field-heading, and Blue/Red reset evidence.
- Real Robot: PASS - user supplied robot-on-blocks actuation, Disable-stop, radio communication, and expected Blue and Red real-field trajectory evidence.
- Transition Guide: FINAL / PASS - `docs/A01_L04_to_A01_L05_Step_by_Step.md`.
- Git Commit: NOT TESTED - user-owned; not run by Codex.
- Git Push: NOT TESTED - user-owned; not run by Codex.
- Known Issues: no L05 source, architecture, simulation, or real-robot defect is established. Git publication remains user-owned and unverified.

## Locked Architecture

- `AutonomousStartContext` binds explicit `FieldVariant`, definite `Alliance`, and execution-start `Pose2d` after a Disabled-only accepted reset.
- Unknown alliance fails closed: no accepted context and no autonomous motion.
- RobotContainer is the exactly-one L04-transform owner. The L04 utility stays pure and does not read Driver Station state.
- `HolonomicDriveController` creates robot-relative `ChassisSpeeds`; output is bounded and sent only to `SwerveSubsystem.acceptChassisSpeeds(...)`.
- Fixed canonical holonomic heading is `0°`, transformed once for Red. Trajectory tangent remains path geometry.
- Completion requires elapsed trajectory time plus simultaneous translation and heading tolerances; hard timeout is trajectory duration plus three seconds.
- Mode loss, invalid runtime data, timeout, interruption, and output-submission failure fail closed through centralized `SwerveSubsystem.stop()`.
- L02 one-shot readiness and no automatic restart semantics remain mandatory.

## Locked Execution Contract

| Item | Value |
|---|---:|
| Selected field variant | `REBUILT_WELDED` (`16.541 m x 8.069 m`) |
| Canonical Blue start | `(0.000 m, 0.000 m, 0 deg)` |
| Canonical Blue end | `(1.000 m, 0.000 m, 0 deg)` |
| Generated trajectory duration | `2.140401179598 s` |
| Blue execution start / end | `(0.000, 0.000, 0 deg)` / `(1.000, 0.000, 0 deg)` |
| Red execution start / end | `(16.541, 8.069, 180 deg)` / `(15.541, 8.069, 180 deg)` |
| Desired holonomic heading | Blue `0 deg`; Red `180 deg` |
| Maximum translation speed | `0.50 m/s` |
| Maximum angular speed | `0.75 rad/s` |
| Hard timeout | trajectory duration + `3.0 s` = `5.140401179598 s` |
| Final translation tolerance | `0.05 m` |
| Final heading tolerance | `3.0 deg` |

## Frozen Boundaries and Exclusions

- A01_L01 through A01_L04 remain COMPLETE / FROZEN / READ-ONLY.
- No SwerveSubsystem, IO, observation, telemetry, hardware, Gradle, vendordep, Frozen Backbone, or Frozen Interface Contract change is authorized by activation.
- PathPlanner, AutoBuilder, replanning, vision, AprilTags, NamedCommands, event markers, mechanisms, and drivetrain retuning remain outside L05.

## Final User-Supplied Runtime Evidence

- Simulation Blue autonomous: PASS.
- Simulation Red autonomous: PASS.
- Real robot USB communication and health: PASS.
- Blue alliance/reset: PASS.
- Red alliance/reset: PASS.
- Robot-on-blocks autonomous actuation: PASS.
- Disable/stop safety: PASS.
- Radio communication: PASS.
- Real-field Blue autonomous trajectory: PASS; behavior as expected.
- Real-field Red autonomous trajectory: PASS; behavior as expected.
- One-shot/no-automatic-restart and field-heading lifecycle behavior: PASS from
  the supplied interactive verification record.
- Installed drive ratio: `6.75:1`, established by repeated physical
  20-motor-rotation / wheel-rotation tests.

The evidence-dependent L05 Real Robot HOLD is cleared. A01_L05 is `COMPLETE /
FROZEN / READ-ONLY`; A01_L06 has not been started.
