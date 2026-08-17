# A00_L03 Bounded Robot-Relative Autonomous Motion - Lesson Plan

## Lesson Metadata

- Module: `A00 - Autonomous Command Foundation`
- Lesson: `A00_L03_BoundedRobotRelativeAutonomousMotion`
- Previous lesson: `A00_L02_AutonomousModeScheduling` - `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE / FROZEN / READ-ONLY`
- Architecture review: `PASS`
- Transition guide: `FINAL / PASS`
- Freeze state: `FROZEN`
- Real robot: `PASS` for the user-supplied A00_L03 bounded-motion and
  transition evidence only

## Learning Objective

Introduce one bounded nonzero robot-relative autonomous drivetrain request while
preserving command requirements, centralized stop ownership, finite-request
fail-closed behavior, and post-motion Swerve ownership.

A00_L03 is the first A00 lesson permitted to generate nonzero autonomous
motion. A00_L01 and A00_L02 remain zero-motion lessons.

## Locked Command Contract

`BoundedRobotRelativeAutonomousDriveCommand` owns one bounded lifecycle:

1. require `SwerveSubsystem`;
2. validate and defensively copy a finite robot-relative `ChassisSpeeds`;
3. validate a finite positive duration and injected monotonic clock;
4. stop before beginning motion;
5. submit the request once through `acceptChassisSpeeds(...)`;
6. monitor only elapsed time;
7. fail closed on invalid, nonfinite, backward, or throwing clock behavior; and
8. call `stop()` from both normal and interrupted termination.

It does not access IO, publish telemetry, convert field-relative speeds, or use
pose/estimator/measured-speed feedback.

## Autonomous Composition

`RobotContainer.getAutonomousCommand()` returns bounded motion followed by:

```text
AutonomousSafetyHoldCommand.repeatedly()
```

The repeating hold retains the Swerve requirement after the bounded command
finishes. This is required ownership safety, not a multi-step autonomous
routine. `Robot.java` remains unchanged.

## Simulation Baseline Constants

Named `AutonomousConstants` values define the Simulation learning baseline:

- `vx = +0.30 m/s`;
- `vy = 0.00 m/s`;
- `omega = 0.00 rad/s`; and
- duration `= 1.0 s`.

These values are not approved real-robot commissioning values.

## Robot-Relative Semantics

Autonomous uses `acceptChassisSpeeds(...)` and therefore commands robot-frame
velocity directly. Existing Teleop continues through
`FieldRelativeTeleopDriveCommand` and its field-relative API. No heading
capture, pose reset, or estimator input is required to execute this bounded
robot-relative request.

## Verification Record

| Gate | Result | Evidence |
|---|---|---|
| Inherited from frozen A00_L02 | PASS | User-supplied inheritance baseline |
| Java 17 verification | PASS | User-supplied verification |
| Simulation Disabled baseline | PASS | User-supplied Simulation Case 1 |
| Bounded robot-relative motion | PASS | User-supplied Simulation Case 2 |
| Automatic stop and no hold restart | PASS | User-supplied Simulation Case 2 |
| Joystick isolation during Autonomous | PASS | User-supplied Simulation Case 3 |
| Autonomous to Disabled safe stop | PASS | User-supplied Simulation Case 4 |
| Teleop fresh-input recovery | PASS | User-supplied Simulation Case 5 |
| Real robot | PASS | User-supplied A00_L03 bounded-motion and transition evidence |

### Real-Robot Verification Amendment

The user supplied and verified these five A00_L03 real-robot cases:

1. **Disabled baseline:** `PASS`.
2. **Autonomous bounded real drivetrain motion on the floor:** `PASS`; the
   command completed, the drivetrain stopped, and motion did not restart
   while Autonomous remained enabled.
3. **Autonomous -> Disabled interruption:** `PASS`; the drivetrain stopped
   with no stale output.
4. **Autonomous -> Teleop transition:** `PASS`; autonomous ownership cleared
   and fresh Teleop control recovered normally.
5. **Autonomous -> Test transition:** `PASS`; no stale or restarted
   autonomous output appeared.

A temporary E-Stop occurred during testing. The robot was rebooted and Case 3
was rerun successfully. This event is test context and is not classified as
an A00_L03 defect.

This PASS is limited to A00_L03 bounded robot-relative motion and lifecycle
transitions. It does not claim PathPlanner, AutoBuilder, localization,
autonomous path following, or competition readiness.
| Architecture review | PASS | Completed final architecture review |

## Deterministic Test Scope

Focused tests cover command requirements, exact robot-relative request values,
defensive copying, bounded completion, normal/interrupted/canceled stop,
invalid configuration and clock failure, no pose/sensor mutation, default
command exclusion, repeating-hold ownership, Disabled cleanup, Teleop recovery,
and Test cancellation. New tests use deterministic clocks and no sleep-based
timing.

## Out of Scope

PathPlanner, AutoBuilder, trajectories, path following, pose targeting,
field/alliance transforms, vision, AprilTags, multi-step routines, A00_L04
Test-mode/global gating, hardware calibration, gain tuning, IO changes,
observation/telemetry changes, Robot.java changes, and frozen lesson changes.

## Final State

A00_L03 is `COMPLETE / FROZEN / READ-ONLY`. Architecture Review is `PASS`,
and the transition guide is `FINAL / PASS`. Java, Simulation, and the supplied
A00_L03 real-robot bounded-motion/lifecycle evidence are recorded as `PASS`.
A00_L04 is the next authorized roadmap lesson.
No new Glass-specific behavior or evidence was introduced; separate Glass
evidence remains `NOT TESTED`.
