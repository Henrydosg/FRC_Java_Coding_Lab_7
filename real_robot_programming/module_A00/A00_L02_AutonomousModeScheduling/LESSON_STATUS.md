# Lesson Status

## Identity

- Module: `A00 - Autonomous Command Foundation`
- Lesson: `A00_L02_AutonomousModeScheduling`
- Previous Lesson: `A00_L01_AutonomousCommandLifecycleFoundation`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Lesson Goal: establish autonomous mode composition and scheduler requirement ownership without issuing nonzero autonomous drivetrain motion.
- Architecture Review: `PASS`
- Baseline Build: `PASS`
- Build: `PASS`
- Simulation: `PASS`
- Driver Station / Glass: `PASS`
- Real Robot: `HOLD`
- Transition Guide: `FINAL / PASS`
- Next Lesson: `A00_L03_BoundedRobotRelativeAutonomousMotion`
- Git Commit: `NOT TESTED` - user-owned; Git not run by Codex
- Git Push: `NOT TESTED` - user-owned; Git not run by Codex

## Verification Record

| Gate | Status | Evidence |
|---|---|---|
| Direct inheritance from frozen A00_L01 | PASS | User-supplied inheritance evidence |
| Generated artifacts cleaned | PASS | User-supplied inheritance evidence |
| Java 17 baseline build | PASS | User-supplied baseline evidence |
| Focused A00_L02 scheduler/composition test | PASS | User-supplied Java 17 evidence |
| AutonomousSafetyHoldCommand regression | PASS | User-supplied Java 17 evidence |
| Full Java 17 regression | PASS | User-supplied Java 17 evidence |
| Final clean Java 17 build | PASS | User-supplied Java 17 clean-build evidence |
| Simulation Disabled baseline | PASS | User-supplied Simulation evidence |
| Autonomous Enabled zero-motion | PASS | User-supplied Simulation evidence |
| Repeated hold beyond multiple lifecycle intervals | PASS | User-supplied test/Simulation evidence |
| Nonzero joystick during Autonomous does not move Swerve | PASS | User-supplied Simulation evidence |
| Autonomous to Disabled safe stop | PASS | User-supplied Simulation evidence |
| Autonomous to Teleop fresh-input recovery | PASS | User-supplied Simulation evidence |
| Real Robot | HOLD | A00_L02 hardware evidence remains pending |
| Transition Guide | PASS | Final guide records the complete A00_L01 -> A00_L02 evolution |

## Implemented Concept

The inherited `Commands.none()` autonomous selection did not require
`SwerveSubsystem`, so the default `FieldRelativeTeleopDriveCommand` could be
eligible during Autonomous. A finite hold would also release ownership after
its duration expired.

`RobotContainer` now constructs the frozen `AutonomousSafetyHoldCommand` with
the Swerve subsystem, the named
`Constants.AutonomousConstants.kSafetyHoldLifecycleDurationSeconds` interval,
and `Timer::getFPGATimestamp`, then returns
`AutonomousSafetyHoldCommand.repeatedly()` from `getAutonomousCommand()`.

The repeated composition retains Swerve ownership until external cancellation
or interruption. The 1.0 second constant is a bounded lifecycle repeat
interval, not the autonomous ownership duration.

RobotContainer remains composition-only. `Robot.java` remains unchanged.

## Zero-Motion Safety Boundary

A00_L02 contains no nonzero autonomous chassis-speed request. The safety-hold
command performs no actuation in `execute()` and stops the subsystem at
lifecycle boundaries. The default teleop command cannot regain Swerve while
the repeated autonomous composition is scheduled.

A00_L01 and A00_L02 are zero-motion lessons. A00_L03 remains the first A00
lesson permitted to issue nonzero autonomous drivetrain motion. Test-mode
global motion gating remains outside this lesson.

## Test Compile Correction

The A00_L02 scheduler test fixture was corrected to populate the current
mechanism-specific `SwerveModuleIOInputs` health fields:
`driveConnected`, `steerConnected`, `encoderConnected`,
`driveConfigurationHealthy`, `steerConfigurationHealthy`, and
`encoderConfigurationHealthy`. No production behavior changed.

## Scope Exclusions and Deferred Work

PathPlanner, AutoBuilder, trajectories, path following, pose targeting,
field/alliance transforms, vision, AprilTags, multi-step autonomous routines,
Test-mode global motion gating, hardware calibration, gain tuning, and all
changes to frozen A00_L01/S00/L22 remain out of scope.

Real-robot verification remains `HOLD`; no hardware PASS is claimed.

The following remain non-blocking technical debt:

- inherited commissioning tests still use `Thread.sleep` timing;
- an optional stronger default-command precondition assertion;
- real-robot verification HOLD; and
- Test-mode global motion gating deferred to a later lesson.

## Final State

A00_L02 is `COMPLETE / FROZEN / READ-ONLY`. The final architecture review is
`PASS`, the transition guide is `FINAL / PASS`, and the supplied Java and
Simulation evidence is recorded accurately. Real-robot verification remains
`HOLD`; no hardware PASS is claimed. A00_L03 remains the first lesson
permitted to issue nonzero autonomous drivetrain motion.

## Known Issues

- Real-robot verification remains `HOLD`; this is non-blocking for the locked
  zero-motion A00_L02 scope.
- Inherited commissioning tests still use `Thread.sleep` timing.
- An optional stronger default-command precondition assertion remains deferred.
- Test-mode global motion gating is deferred to a later lesson.
