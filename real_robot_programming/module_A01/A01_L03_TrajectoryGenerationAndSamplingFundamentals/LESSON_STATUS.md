# Lesson Status

## Identity

- Module: A01 - Autonomous Navigation and Path Following
- Lesson: A01_L03_TrajectoryGenerationAndSamplingFundamentals
- Previous Lesson: A01_L02_PoseTargetedAutonomousMotion
- Previous Lesson State: COMPLETE / FROZEN / READ-ONLY
- Status: COMPLETE
- Active State: COMPLETE / FROZEN / READ-ONLY
- Freeze State: FROZEN / READ-ONLY
- Lesson Goal: create one finite time-parameterized trajectory and inspect deterministic Trajectory.State samples without commanding robot motion.
- Inheritance Baseline: PASS - L03 began from a source/test/deploy tree byte-identical to frozen L02.
- Corrected Directory Identity: PASS
- Baseline Build: PASS - user-supplied inherited baseline build reported BUILD SUCCESSFUL.
- Architecture Review: PASS
- Architecture Audit: PASS
- Design Lock: PASS
- Implementation: COMPLETE
- Build: PASS - user-supplied local verification complete.
- Java Verification: PASS - user-supplied compileTestJava PASS.
- Focused Tests: PASS - user-supplied LearningTrajectoryFactoryTest PASS.
- Full Tests: PASS - user-supplied full regression PASS; no test count supplied.
- Full Build: PASS - user-supplied clean build reported BUILD SUCCESSFUL in 44s with 7 actionable tasks executed.
- Simulation: PASS - non-actuating local deterministic generation and sampling verification; no drivetrain motion.
- Driver Station / Glass: NOT APPLICABLE - no runtime telemetry or Driver Station behavior entered L03.
- Real Robot: HOLD - the A01 ADR permits no physical actuation or real-robot test for this generation/sampling-only lesson.
- Transition Guide: FINAL / PASS - docs/A01_L02_to_A01_L03_Step_by_Step.md.
- Git Commit: NOT TESTED - user-owned; not run by Codex.
- Git Push: NOT TESTED - user-owned; not run by Codex.
- Known Issues: no L03 implementation defect is established. Real Robot remains HOLD by approved scope; Git commit and push remain user-owned.

## Implemented L03 Data Contract

Start Pose + interior Translation2d waypoint(s) + Goal Pose
→ TrajectoryGenerator
→ Trajectory
→ trajectory.sample(t)
→ Trajectory.State

| Item | Implemented value |
|---|---:|
| Start pose | existing L01 learning start: (0.00 m, 0.00 m, 0 deg) |
| Interior waypoint | (0.50 m, 0.25 m) |
| Goal pose | (1.00 m, 0.00 m, 0 deg) |
| Maximum velocity | 1.0 m/s |
| Maximum acceleration | 1.0 m/s² |

LearningTrajectoryFactory creates a fresh TrajectoryConfig, calls the WPILib
TrajectoryGenerator, validates null/empty/non-finite/unordered output, and
returns the native Trajectory. Sampling remains owned directly by
Trajectory.sample(t); no wrapper was introduced.

## Frozen Boundaries and Exclusions

- A01_L01 and A01_L02 remain COMPLETE / FROZEN / READ-ONLY.
- Frozen Backbone, Frozen Interface Contract, L01 field-frame/start-pose
  contract, L02 pose-target command, accepted-reset readiness, centralized
  stop, Disable stop, and no-automatic-restart behavior remain unchanged.
- RobotContainer, Robot, commands, SwerveSubsystem, IO, observation,
  telemetry, output pipeline, hardware configuration, drivetrain tuning,
  Gradle, and vendordeps are unchanged.
- No follower, holonomic controller, ChassisSpeeds, drivetrain output,
  PathPlanner, AutoBuilder, alliance transform, vision, replanning, event
  marker, or mechanism event enters L03.
