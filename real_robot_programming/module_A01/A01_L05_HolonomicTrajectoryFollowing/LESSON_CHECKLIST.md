# A01_L05 - Holonomic Trajectory Following - Checklist

Status: COMPLETE / FROZEN / READ-ONLY  
Freeze State: FROZEN  
Previous lesson: A01_L04_FieldAndAllianceTransformContract - COMPLETE / FROZEN / READ-ONLY  
Git: user-owned; not run by Codex

## Governance and Activation

- [x] A01 ADR identity and lesson order reviewed.
- [x] Directory identity matches the authorized L05 lesson.
- [x] Frozen L04 inheritance baseline is byte-identical in `src`.
- [x] User-supplied inherited baseline build passed.
- [x] Architecture Audit passed.
- [x] Design-Lock Blocker Resolution passed.
- [x] L05 was the sole IN_PROGRESS lesson before closure; no lesson is active after freeze.
- [x] Transition guide is FINAL / PASS.
- [x] L05 implementation is complete.
- [x] `compileJava` and `compileTestJava` pass under WPILib Java 17.
- [x] Focused L05 tests pass: 32/32.
- [x] Focused inherited L01-L04 regression passes: 57/57.
- [x] Full regression passes: 401/401; zero failures, errors, or skips.
- [x] Clean build passes with all seven reported tasks executed.
- [x] End-to-end Blue and Red Simulation evidence is supplied.
- [x] Real robot USB communication/health verification passes.
- [x] Blue and Red alliance/reset verification passes.
- [x] Robot-on-blocks autonomous actuation passes.
- [x] Disable/stop safety behavior passes.
- [x] Radio communication verification passes.
- [x] Blue real-field autonomous trajectory passes as expected.
- [x] Red real-field autonomous trajectory passes as expected.
- [x] One-shot/no-automatic-restart and field-heading lifecycle behavior pass.
- [x] Authoritative installed drive ratio is `6.75:1`, established by repeated physical 20-rotation tests.
- [ ] User Git commit.
- [ ] User Git push.

## Locked L05 Contract

- [x] `AutonomousStartContext` binds explicit `FieldVariant`, definite `Alliance`, and execution start pose.
- [x] Disabled-only alliance-aware reset/readiness is one-shot.
- [x] Unknown alliance fails closed without a reset context or autonomous motion.
- [x] RobotContainer owns exactly one L04 transform; the L04 utility remains pure.
- [x] WPILib `HolonomicDriveController` output is robot-relative and uses `acceptChassisSpeeds(...)`.
- [x] Fixed canonical holonomic heading is `0°`; path tangent remains independent geometry.
- [x] Output bounds, final tolerance completion, timeout, centralized stop, and no automatic restart are locked.

## Frozen Boundaries and Exclusions

- [x] A01_L01 through A01_L04 remain frozen and untouched.
- [x] No activation-time Java, test, SwerveSubsystem, IO, telemetry, hardware, Gradle, vendordep, or dependency change occurred.
- [x] PathPlanner, AutoBuilder, replanning, vision, AprilTags, NamedCommands, event markers, mechanisms, and drivetrain retuning remain deferred.

## Closure Result

- [x] Every L05 acceptance criterion is satisfied.
- [x] The evidence-dependent ADR Real Robot HOLD is cleared.
- [x] L05 documentation is complete and reconciled.
- [x] A01_L05 is COMPLETE / FROZEN / READ-ONLY.
- [x] A01_L06 has not been started.
- [x] No production Java, tests, Gradle, Frozen Backbone, or Frozen Interface Contract files were changed during closure.
