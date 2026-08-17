# A00_L04 Autonomous Motion Safety Gating - Checklist

Status: `COMPLETE / FROZEN / READ-ONLY`  
Freeze State: `FROZEN`  
Architecture Review: `PASS`  
Implementation: `COMPLETE`  
Predecessor: `A00_L03_BoundedRobotRelativeAutonomousMotion - COMPLETE / FROZEN / READ-ONLY`  
Git: user-owned; not run by Codex

## Activation and Inheritance

- [x] AGENTS.md, repository README, authoritative Documents A/B/C, Frozen Backbone, and Frozen Interface Contract reviewed.
- [x] A00 roadmap ADR and locked lesson order reviewed.
- [x] A00_L03 is recorded as the complete, frozen predecessor.
- [x] A00_L04 was copied from the published frozen A00_L03 project.
- [x] Generated build artifacts were cleaned.
- [x] The `.wpilib` removal and team-number baseline failure are recorded.
- [x] `.wpilib` was restored from frozen A00_L03.
- [x] `.wpilib` is documented as required configuration, not disposable build output.
- [x] User-supplied inherited Java 17 baseline: `BUILD SUCCESSFUL`.
- [x] A00_L04 is the final lesson currently authorized by the existing A00 roadmap ADR.
- [x] No A00_L05 is authorized by the existing roadmap ADR.

## Authorized L04 Concept

- [x] The single concept is Test/global autonomous-motion mode gating.
- [x] The invariant is recorded exactly: nonzero autonomous drivetrain motion is permitted only while `DriverStation.isAutonomousEnabled() == true`.
- [x] Invalid mode fails closed through centralized drivetrain stop.
- [x] The bounded motion and repeating zero-motion hold use scheduler-managed WPILib composition.
- [x] The frozen A00_L03 command remains unchanged.
- [x] No out-of-scope safety policy was added.

## Disabled Scheduler Expectation

- [x] The focused Disabled initial-scheduling test expects `stopCount == 0`.
- [x] The reason is recorded: WPILib rejects a command with `runsWhenDisabled() == false` before initialization while already Disabled.
- [x] Existing safety assertions remain: command unscheduled, `acceptCount == 0`, no motion, and final module states zero.

## Scope Preservation

- [x] A00_L03, A00_L02, A00_L01, and S00 remain frozen and unchanged.
- [x] The active L04 RobotContainer autonomous composition is the only authorized production implementation change.
- [x] The focused L04 scheduling test contains only the authorized Disabled expectation correction.
- [x] Gradle is unchanged.
- [x] Robot.java, SwerveSubsystem, IO, observation, telemetry, and hardware configuration are unchanged.
- [x] Health-policy, CAN/configuration fault, pose, odometry, estimator, and observation-freshness gating are out of scope.
- [x] PathPlanner, AutoBuilder, trajectories, vision, AprilTags, alliance transforms, and multi-step routines are out of scope.
- [x] Drivetrain tuning, hardware changes, and Frozen Interface Contract changes are out of scope.

## Verification Record

| Gate | Result | Evidence |
|---|---|---|
| Inheritance from frozen A00_L03 | PASS | User-supplied inheritance evidence |
| Generated artifact cleanup | PASS | User-supplied inheritance evidence |
| `.wpilib` restoration | PASS | User-supplied inheritance evidence |
| Java 17 inherited baseline | PASS | User-supplied `BUILD SUCCESSFUL` |
| L04 implementation | PASS | Locked standard WPILib composition; user verification supplied |
| Focused Java 17 regression | PASS | User-supplied result |
| Full Java 17 regression | PASS | User-supplied result |
| Clean build | PASS | User-supplied result |
| L04 Simulation | PASS | User-supplied five-scenario evidence |
| L04 Driver Station / Glass | NOT SEPARATELY TESTED | No separate evidence supplied |
| Real robot | PASS | Supplied A00_L04 autonomous-mode safety-gating and lifecycle evidence |
| Transition Guide | FINAL / PASS | Final guide completed |
| Final Architecture Review | PASS | User authorization supplied |
| Git Commit | NOT TESTED | User-owned; Git not run by Codex |
| Git Push | NOT TESTED | User-owned; Git not run by Codex |

## Simulation Evidence

- [x] Disabled baseline: zero drive/steer output and velocity.
- [x] Autonomous + Enabled: inherited bounded `+0.30 m/s` robot-relative motion, stops after approximately `1.0 s`, and does not restart during the repeating safety hold.
- [x] Autonomous -> Teleop during motion: motion terminates immediately; neutral Teleop remains stopped.
- [x] Autonomous -> Disabled during motion: motion stops/disarms; no stale request remains.
- [x] Test gating: Test permits no autonomous motion; Autonomous -> Test terminates motion and does not restart.

## Real-Robot Evidence Amendment

- [x] Disabled baseline: PASS.
- [x] Valid Autonomous bounded motion: PASS; motion completed, the drivetrain
  stopped, and motion did not restart while Autonomous remained enabled.
- [x] Autonomous -> Disabled: PASS; motion terminated safely with no stale
  output.
- [x] Autonomous -> Teleop: PASS; autonomous output cleared and fresh Teleop
  control recovered normally.
- [x] Autonomous -> Test: PASS; no stale or restarted autonomous output
  appeared.
- [x] Test initial gate: PASS; Test Enabled did not permit autonomous motion.
- [x] Teleop initial gate: PASS; Teleop Enabled with neutral input did not
  permit autonomous motion, while normal Teleop remained available with fresh
  input.
- [x] A temporary CommandScheduler loop-overrun observation occurred during
  Teleop testing; it requires further evidence and is not classified as an
  A00_L04 defect.
- [x] Real-robot PASS is limited to A00_L04 autonomous-mode safety gating and
  lifecycle behavior.
- [x] No claim is made for pose/localization, PathPlanner, AutoBuilder,
  trajectory following, or competition readiness.

## Completion and Freeze Record

- [x] Locked design completed.
- [x] L04 production implementation completed for the locked scope.
- [x] Focused and full L04 Java tests completed.
- [x] L04 clean build completed.
- [x] L04 Simulation verification completed.
- [x] Supplied L04 real-robot safety-gating/lifecycle verification is recorded
  as `PASS`.
- [x] Transition guide finalized as `FINAL / PASS`.
- [x] Final Architecture Review passed.
- [x] A00_L04 transitioned to `COMPLETE / FROZEN / READ-ONLY`.
- [ ] User Git commit.
- [ ] User Git push.

## Remaining Non-Blocking Debt

- [x] Broader real-robot capability remains outside the A00_L04 scope.
- [x] Driver Station / Glass has no separate verification evidence.
- [x] Inherited commissioning tests retain sleep-based timing; those tests
  were not added by A00_L04.
