# A00_L02 - Autonomous Mode Scheduling

## Lesson State

- Module: `A00 - Autonomous Command Foundation`
- Lesson: `A00_L02_AutonomousModeScheduling`
- Previous lesson: `A00_L01_AutonomousCommandLifecycleFoundation` - `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE / FROZEN / READ-ONLY`
- Architecture review: `PASS`
- Transition guide: `FINAL / PASS`
- Freeze state: `FROZEN`
- Real robot: `HOLD`
- Git: user-owned; not run by Codex

This lesson inherits directly from frozen A00_L01 and introduces one new
concept: autonomous mode composition and scheduler requirement ownership.

## Learning Objective

Compose the existing zero-motion safety-hold command into the autonomous
selection path so it retains ownership of `SwerveSubsystem` for the entire
scheduled autonomous interval. This prevents the default teleoperated drive
command from becoming eligible during Autonomous, without adding autonomous
motion.

## Inherited Architecture

The Frozen Backbone remains:

```text
XboxController -> controls -> commands -> SwerveSubsystem -> SwerveModuleIO -> hardware
```

The observation path remains:

```text
hardware or simulation IOInputs
-> SwerveSubsystem
-> immutable observation
-> RobotTelemetry
-> SwerveTelemetryFacade
-> NT4 / Glass / Field2d
```

`SwerveSubsystem` remains the owner of actuation and centralized `stop()`.
`RobotContainer` remains the composition root: it constructs dependencies,
selects implementations, configures the default command, and composes the
autonomous command. It contains no hardware logic, input processing,
telemetry calculations, or drivetrain business logic.

## Single New Concept

### Autonomous composition and scheduler requirement ownership

Before A00_L02, `RobotContainer.getAutonomousCommand()` returned
`Commands.none()`. That command completed immediately and required no
subsystem. Consequently, the default `FieldRelativeTeleopDriveCommand` could
remain eligible during Autonomous and potentially own the Swerve subsystem.

A finite safety hold by itself would not be sufficient: after it finished,
the default command could become eligible again while Autonomous was still
active.

The solution is:

```text
AutonomousSafetyHoldCommand.repeatedly()
```

The repeating composition retains the wrapped command's
`SwerveSubsystem` requirement until it is externally canceled or interrupted.
Each repeated lifecycle is still zero-motion because the frozen hold command
only stops the subsystem and performs no drivetrain request.

## Production Implementation

`RobotContainer` now:

- constructs `AutonomousSafetyHoldCommand` with `SwerveSubsystem`;
- injects `Timer::getFPGATimestamp`;
- injects the named `Constants.AutonomousConstants.kSafetyHoldLifecycleDurationSeconds` value;
- wraps the hold with `.repeatedly()`; and
- returns the composition from `getAutonomousCommand()`.

The `1.0` second constant is one bounded lifecycle repeat interval. It is not
the autonomous ownership duration and does not determine when autonomous mode
ends. Requirement ownership belongs to the repeating composition until the
command is externally canceled or interrupted.

`Robot.java` is unchanged. Its existing autonomous scheduling and Teleop/Test
cancellation behavior is sufficient for this increment.

No nonzero autonomous `acceptChassisSpeeds(...)` request exists. A00_L02 is
strictly zero-motion. A00_L03 remains the first lesson permitted to generate a
nonzero autonomous drivetrain request.

## Verification Evidence

- inherited directly from frozen A00_L01: `PASS`;
- Java 17 baseline build before implementation: `PASS`;
- `RobotContainerAutonomousModeSchedulingTest`: `PASS`;
- `AutonomousSafetyHoldCommandTest` regression: `PASS`;
- full Java 17 regression: `PASS`;
- final clean Java 17 build: `PASS`;
- Simulation Disabled baseline: `PASS`;
- Autonomous Enabled zero-motion: `PASS`;
- repeated hold across multiple 1.0 second lifecycle intervals: `PASS`;
- nonzero joystick input during Autonomous does not move Swerve: `PASS`;
- Autonomous to Disabled safe stop: `PASS`;
- Autonomous to Teleop fresh-input recovery: `PASS`;
- real-robot verification: `HOLD`.

The test fixture was corrected to use the current mechanism-specific
`SwerveModuleIOInputs` health fields. This was a test-compile correction only;
it introduced no new production concept.

## Non-Blocking Technical Debt

- Inherited commissioning tests still use `Thread.sleep` timing.
- The focused scheduler test could add a stronger explicit assertion that the
  default command was scheduled before autonomous scheduling.
- Real-robot verification remains `HOLD`.
- Test-mode global motion gating is deferred to a later lesson.

## Safety Boundary

A00_L01 and A00_L02 are zero-motion lessons. A00_L03 is the first A00 lesson
permitted to issue a nonzero autonomous drivetrain request. Test-mode global
motion gating is outside this lesson and is not redefined here.

## Explicitly Out of Scope

A00_L02 does not add:

- nonzero autonomous motion;
- PathPlanner or AutoBuilder;
- trajectory generation, path following, or pose targeting;
- field or alliance transforms;
- vision or AprilTag integration;
- multi-step autonomous routines;
- hardware calibration, drive tuning, or gain changes;
- Robot.java mode logic;
- changes to A00_L01, S00_L24, L22, the Frozen Backbone, or IO contracts.

## Final State

A00_L02 is `COMPLETE / FROZEN / READ-ONLY`. The locked zero-motion scope,
supplied Java/Simulation evidence, architecture review, and transition guide
are complete. Real-robot verification remains `HOLD` and is not claimed as
hardware PASS. A00_L03 remains the first lesson permitted to issue nonzero
autonomous drivetrain motion.
