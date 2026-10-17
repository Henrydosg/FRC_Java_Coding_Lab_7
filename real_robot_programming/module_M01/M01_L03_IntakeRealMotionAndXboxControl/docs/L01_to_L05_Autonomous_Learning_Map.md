# L01 to L05 Autonomous Learning Map

## Why This Sequence Exists

Autonomous motion is learned in layers. Each lesson makes one question answerable before the next question depends on it.

```text
L01: Where am I? -> validated known starting pose and field reference
        ↓
L02: Can I drive to a pose? -> bounded feedback control to one field-relative target
        ↓
L03: How do I generate/sample a motion plan? -> Trajectory -> sample(t) -> Trajectory.State
        ↓
L04: Which field/alliance frame does that plan belong to? -> one Blue/Red contract
        ↓
L05: How will the robot FOLLOW that plan? -> future holonomic trajectory following
        ↓
First real-robot trajectory autonomous, after applicable L05 safety and verification gates
```

## What Each Lesson Owns

| Lesson | Learning question | Owned concept | Deliberate boundary |
|---|---|---|---|
| L01 | Where am I? | Validated starting pose and field-frame initialization | No motion plan or controller |
| L02 | Can I drive to a pose? | One bounded pose-target command | No trajectory generation or alliance handling |
| L03 | How do I generate/sample a plan? | Pure WPILib generation and `sample(t)` | No follower or drivetrain output |
| L04 | Which frame belongs to the plan? | Canonical Blue frame and one alliance transform | No motion or controller |
| L05 | How will the robot follow it? | Future holonomic follower boundary | Must preserve prior frame and stop contracts |

L04 does not alter L01's known start `(0.0 m, 0.0 m, 0 deg)`, L02's one-shot readiness/no-restart and centralized stop behavior, or L03's native trajectory generation and sampling.

## The Important Distinction Before L05

An L03 `Trajectory.State` says where the *path geometry* is at time `t`. Its `poseMeters` rotation is the path tangent. It does **not** yet say what holonomic robot heading a future swerve follower should command. L04 preserves that distinction while making the path's field frame explicit.

## Safety Thread Through the Lessons

The A00_L04 Autonomous+Enabled invariant, `SwerveSubsystem.stop()` authority, and no-automatic-restart behavior are inherited throughout. L04 is pure data transformation: it creates no command, scheduler action, `ChassisSpeeds`, or drivetrain request. It therefore cannot itself authorize motion.
