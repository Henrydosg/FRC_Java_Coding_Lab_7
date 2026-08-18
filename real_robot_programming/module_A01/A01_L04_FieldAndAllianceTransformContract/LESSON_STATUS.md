# Lesson Status

## Identity

- Module: A01 - Autonomous Navigation and Path Following
- Lesson: A01_L04_FieldAndAllianceTransformContract
- Previous Lesson: A01_L03_TrajectoryGenerationAndSamplingFundamentals
- Previous Lesson State: COMPLETE / FROZEN / READ-ONLY
- Status: COMPLETE
- Active State: COMPLETE / FROZEN / READ-ONLY
- Freeze State: FROZEN / READ-ONLY
- Lesson Goal: establish one explicit canonical Blue-origin field and alliance transform contract without autonomous motion.

## Gates and Evidence

- Inheritance Baseline: PASS - inherited L04 source tree was verified byte-identical to frozen L03.
- Directory Identity: PASS - `A01_L04_FieldAndAllianceTransformContract` matches the A01 ADR identity.
- Baseline Build: PASS - user-supplied inherited L04 baseline build reported BUILD SUCCESSFUL.
- Architecture Audit: PASS.
- Design-Lock Verification: PASS.
- Design-Lock HOLD Resolution: PASS - both official 2026 field variants are represented explicitly in the locked design.
- Implementation: COMPLETE - `FieldVariant` and pure `FieldAllianceTransform` are implemented.
- Build: PASS - independent user verification supplied compileJava,
  compileTestJava, focused L04 tests, full regression, and clean build PASS.
- Java Verification: PASS - independent user-supplied compileJava and compileTestJava PASS.
- Focused Tests: PASS - independent user-supplied FieldAllianceTransformTest
  and LearningTrajectoryFactoryTest PASS.
- Full Tests: PASS - independent user-supplied full regression PASS.
- Full Build: PASS - independent user-supplied clean build PASS.
- Simulation: PASS - independent user-supplied non-actuating both-alliance
  Simulation gate PASS; L04 adds no actuation.
- Driver Station / Glass: NOT APPLICABLE - no L04 runtime behavior or telemetry is authorized.
- Real Robot: HOLD - the A01 ADR authorizes no L04 physical actuation.
- Transition Guide: FINAL / PASS - `docs/A01_L03_to_A01_L04_Step_by_Step.md`.
- Git Commit: NOT TESTED - user-owned; not run by Codex.
- Git Push: NOT TESTED - user-owned; not run by Codex.
- Known Issues: no L04 source or architecture defect is established. Real
  Robot remains HOLD by authorized no-actuation scope; Git publication remains
  user-owned.

## Locked Architecture

- Canonical frame: always Blue-origin.
- Explicit `FieldVariant`: `REBUILT_WELDED` (16.541 m x 8.069 m) or
  `REBUILT_ANDYMARK` (16.518 m x 8.043 m).
- Blue transform: identity.
- Red transform: 180-degree rotation about the selected field centre.
- Unknown alliance: future composition fails closed; no implicit Blue fallback.
- Utility: pure; explicit `FieldVariant` and `Alliance` inputs; no Driver
  Station lookup inside the utility.
- Ownership: exactly one alliance transform owner; no hidden drivetrain,
  IO, or telemetry transform.

## Frozen Boundaries and Exclusions

- A01_L01 through A01_L03 remain COMPLETE / FROZEN / READ-ONLY.
- Frozen Backbone, Frozen Interface Contract, localization, observation,
  telemetry, safety-stop, and readiness contracts remain unchanged.
- No follower, controller, ChassisSpeeds, drivetrain motion, RobotContainer,
  Robot, SwerveSubsystem, PathPlanner, AutoBuilder, vision, IO, telemetry,
  hardware, tuning, Gradle, or vendordep change is authorized in L04.
