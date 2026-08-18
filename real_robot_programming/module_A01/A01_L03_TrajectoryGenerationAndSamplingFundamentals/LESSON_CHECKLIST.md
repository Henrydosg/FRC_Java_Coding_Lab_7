# A01_L03 - Trajectory Generation and Sampling Fundamentals - Checklist

Status: COMPLETE / FROZEN / READ-ONLY  
Freeze State: FROZEN / READ-ONLY  
Previous lesson: A01_L02_PoseTargetedAutonomousMotion - COMPLETE / FROZEN / READ-ONLY  
Git: user-owned; not run by Codex

## Governance and Completion

- [x] A01 ADR identity and lesson order reviewed.
- [x] Corrected L03 directory identity verified.
- [x] Frozen L02 inheritance baseline preserved.
- [x] Architecture Audit and Design Lock passed.
- [x] Implemented source delta is limited to Constants, LearningTrajectoryFactory, and LearningTrajectoryFactoryTest.
- [x] Transition guide finalized.
- [x] L03 status is COMPLETE / FROZEN / READ-ONLY.
- [ ] User Git commit.
- [ ] User Git push.

## Implemented L03 Contract

- [x] Existing L01 learning starting pose is reused.
- [x] Interior waypoint is (0.50 m, 0.25 m).
- [x] Goal pose is (1.00 m, 0.00 m, 0 deg).
- [x] Maximum velocity is 1.0 m/s.
- [x] Maximum acceleration is 1.0 m/s².
- [x] WPILib TrajectoryGenerator produces a fresh native Trajectory.
- [x] Sampling remains direct through trajectory.sample(t).
- [x] Generated trajectory validation rejects unusable null, empty,
  non-finite, non-positive-duration, decreasing-time, or invalid-state output.
- [x] State pose rotation is documented as path geometry, not holonomic heading.

## Verification

- [x] User-supplied compileTestJava PASS.
- [x] User-supplied LearningTrajectoryFactoryTest PASS.
- [x] User-supplied full regression PASS.
- [x] User-supplied clean build PASS: BUILD SUCCESSFUL in 44s; 7 actionable tasks.
- [x] Non-actuating local deterministic generation/sampling verification PASS.
- [x] Driver Station / Glass is NOT APPLICABLE; no runtime behavior or telemetry was added.
- [x] Real Robot is HOLD by A01 ADR; no physical actuation was authorized.

## Preserved Boundaries and Exclusions

- [x] A01_L01 and A01_L02 remain frozen and untouched.
- [x] Field-heading reference, known starting pose, one-shot readiness,
  EstimatedPose, Disable stop, centralized stop, and no-automatic-restart
  behavior remain unchanged.
- [x] No follower, holonomic controller, ChassisSpeeds, drivetrain motion,
  RobotContainer, Robot, or SwerveSubsystem change was added.
- [x] No PathPlanner, AutoBuilder, alliance transform, vision, replanning,
  NamedCommands, event marker, mechanism event, telemetry, IO, hardware,
  drivetrain tuning, Gradle, vendordep, or Frozen Backbone/interface change
  was added.
