# A00_L03 - Bounded Robot-Relative Autonomous Motion

## Lesson State

- Module: `A00 - Autonomous Command Foundation`
- Lesson: `A00_L03_BoundedRobotRelativeAutonomousMotion`
- Previous lesson: `A00_L02_AutonomousModeScheduling` - `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE / FROZEN / READ-ONLY`
- Architecture review: `PASS`
- Transition guide: `FINAL / PASS`
- Freeze state: `FROZEN`
- Real robot: `HOLD`
- Git: user-owned; not run by Codex

A00_L03 is the first A00 lesson authorized to issue a nonzero autonomous
drivetrain request. It inherits the frozen A00_L02 project directly.

## Single Learning Concept

One bounded, finite, robot-relative autonomous motion request followed by the
inherited repeating zero-motion safety hold.

This lesson is the first controlled transition from autonomous lifecycle and
mode ownership into actual autonomous drivetrain actuation. It does not add
pose-targeting, trajectories, or autonomous routine composition.

## Production Design

`BoundedRobotRelativeAutonomousDriveCommand`:

- requires `SwerveSubsystem`;
- accepts a defensively copied finite `ChassisSpeeds` request;
- uses an injected monotonic clock;
- uses a finite positive duration;
- calls `SwerveSubsystem.stop()` before motion begins;
- submits exactly one robot-relative request during `initialize()`;
- performs no direct IO, telemetry, pose, estimator, or gyro work;
- fails closed on invalid, nonfinite, backward, or throwing clock behavior;
- stops on completion, interruption, cancellation, and mode/disabled failure; and
- returns `runsWhenDisabled() == false`.

The autonomous composition is:

```text
BoundedRobotRelativeAutonomousDriveCommand
    -> AutonomousSafetyHoldCommand.repeatedly()
```

The repeating safety hold retains the `SwerveSubsystem` requirement after the
bounded motion ends. It prevents the default field-relative Teleop command from
becoming eligible during the remainder of Autonomous. `RobotContainer` remains
composition-only and `Robot.java` is unchanged.

## Robot-Relative Versus Field-Relative

The autonomous command calls `acceptChassisSpeeds(...)` directly. Its baseline
request is robot-relative:

```text
vx = +0.30 m/s
vy =  0.00 m/s
omega = 0.00 rad/s
```

The existing `FieldRelativeTeleopDriveCommand` remains a separate Teleop path
and continues to use field-relative conversion and the existing heading
semantics. A00_L03 does not alter that path.

## Simulation Learning Baseline

The following named `AutonomousConstants` values are Simulation learning
baselines only, not approved real-robot commissioning values:

- forward speed: `+0.30 m/s`;
- lateral speed: `0.00 m/s`;
- angular speed: `0.00 rad/s`; and
- bounded duration: `1.0 s`.

Expected behavior is bounded forward motion, automatic stop, and a repeating
zero-motion hold that does not restart motion.

## Verification Evidence

User-supplied Java 17 verification: `PASS`.

Simulation and Driver Station evidence supplied by the user:

- Disabled baseline remained stationary: `PASS`;
- bounded `+0.30 m/s` robot-relative forward motion for approximately `1.0 s`: `PASS`;
- motion stopped automatically and did not restart through the repeating hold: `PASS`;
- joystick input during Autonomous did not move Swerve: `PASS`;
- Autonomous to Disabled performed a safe stop: `PASS`; and
- Teleop fresh-input recovery worked after Autonomous: `PASS`.

No real-robot PASS is claimed. Real-robot verification remains `HOLD`.

## Preserved Architecture

```text
Driver -> Xbox controller -> controls -> commands -> SwerveSubsystem
       -> SwerveModuleIO -> hardware or simulation
```

The observation flow, IO contracts, centralized stop authority, finite-request
fail-closed behavior, disabled-transition disarm, immutable observations, and
read-only telemetry remain inherited. No vendor API is added outside real IO.

## Explicitly Out of Scope

- A00_L04 Test-mode/global autonomous motion gating;
- PathPlanner or AutoBuilder;
- trajectories or path following;
- pose-targeted autonomous behavior;
- field/alliance transforms;
- vision or AprilTags;
- multi-step autonomous routines;
- hardware calibration, drive tuning, or gain changes;
- Robot.java changes;
- IO, observation, or telemetry contract changes; and
- changes to frozen A00_L01, A00_L02, or S00.

## Deferred / Non-Blocking Items

- real-robot verification remains `HOLD`;
- Test-mode global motion gating belongs to A00_L04;
- final drivetrain tuning remains outside this lesson; and
- inherited commissioning timing tests remain technical debt; and
- no new Glass-specific behavior or evidence was introduced; separate Glass
  evidence remains `NOT TESTED`.

A00_L03 is `COMPLETE / FROZEN / READ-ONLY`. The final architecture review is
`PASS` and the transition guide is `FINAL / PASS`.
A00_L04 is the next authorized roadmap lesson.
