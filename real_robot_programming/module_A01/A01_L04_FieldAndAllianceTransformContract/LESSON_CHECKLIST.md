# A01_L04 - Field and Alliance Transform Contract - Checklist

Status: COMPLETE / FROZEN / READ-ONLY  
Freeze State: FROZEN / READ-ONLY  
Previous lesson: A01_L03_TrajectoryGenerationAndSamplingFundamentals - COMPLETE / FROZEN / READ-ONLY  
Git: user-owned; not run by Codex

## Governance and Activation

- [x] A01 ADR identity and lesson order reviewed.
- [x] Directory identity matches the authorized L04 lesson.
- [x] Frozen L03 inheritance baseline is byte-identical in `src`.
- [x] User-supplied inherited baseline build passed.
- [x] Architecture Audit passed.
- [x] Design-Lock Verification passed.
- [x] Design-Lock HOLD Resolution passed.
- [x] L04 is activated as the sole IN_PROGRESS lesson.
- [x] L04 implementation is complete.
- [x] Transition guide is final.
- [ ] User Git commit.
- [ ] User Git push.

## Locked L04 Contract

- [x] Canonical field frame is always Blue-origin.
- [x] Explicit variants are REBUILT_WELDED (16.541 m x 8.069 m) and
  REBUILT_ANDYMARK (16.518 m x 8.043 m).
- [x] Blue is identity; Red is 180-degree field-centre rotation.
- [x] Unknown alliance has no implicit Blue fallback and fails closed at the
  future composition boundary.
- [x] Planned utility is pure with explicit FieldVariant and Alliance inputs.
- [x] Planned utility has no DriverStation lookup.
- [x] Exactly one transform owner is required.
- [x] Independent user-supplied compileJava and compileTestJava pass.
- [x] Independent user-supplied FieldAllianceTransformTest passes.
- [x] Independent user-supplied LearningTrajectoryFactoryTest regression passes.
- [x] Independent user-supplied full regression and clean build pass.
- [x] Independent user-supplied non-actuating both-alliance Simulation verification passes.

## Required Learning Documents Before Freeze

- [x] `L01_to_L05_Autonomous_Learning_Map.md`
- [x] `WPILib_Field_Coordinate_System.md`
- [x] `Blue_Red_Transform_Mathematics.md`
- [x] `Official_2026_Field_Variants.md`
- [x] `Trajectory_Transform_Semantics.md`
- [x] `Transform_Ownership_and_Double_Transform_Prevention.md`
- [x] `Unknown_Alliance_Safety_Contract.md`
- [x] `L03_to_L04_to_L05_Data_Flow.md`

## Frozen Boundaries and Exclusions

- [x] A01_L01 through A01_L03 remain frozen and untouched.
- [x] L01 field-heading/start-pose, L02 readiness/EstimatedPose/stop/no-restart,
  and L03 generation/sampling contracts remain unchanged.
- [x] No follower, controller, ChassisSpeeds, drivetrain motion, RobotContainer,
  Robot, SwerveSubsystem, PathPlanner, AutoBuilder, vision, telemetry, IO,
  hardware, tuning, Gradle, vendordep, or Frozen Backbone/interface change is
  authorized.
- [x] L04 is COMPLETE / FROZEN / READ-ONLY; Real Robot remains HOLD by the
  authorized no-actuation scope.
