# A01_L08 - Autonomous Routine Selection and Safe Composition

## Learning guide

This lesson teaches how a robot selects one safe autonomous routine at the
composition root and turns that selection into a fresh scheduler-owned command.
It is a repository learning guide, not a competition-tuning report.

## 1. What problem L08 solves

L07 proved that the existing pose, speed, output, alliance, requirement, and
safety contracts can cross the AutoBuilder boundary. L08 adds the next small
concept: choosing between more than one approved autonomous behavior without
storing stale commands, bypassing readiness, or losing the drivetrain stop
authority.

| Concern | L07 | L08 |
| --- | --- | --- |
| Main concept | AutoBuilder contract integration | Routine selection and safe composition |
| Selection | One known autonomous path | `SAFE_STOP` or explicit `ONE_METER_PATH` |
| Owner | L07 adapter boundary | `RobotContainer` and `AutonomousRoutineFactory` |
| Command lifetime | Existing path command boundary | Fresh command for each autonomous request |
| Safety | Existing L07 stop and requirement contracts | Same contracts plus fail-closed selection |

L08 does not add NamedCommands, event markers, mechanism actions, pathfinding,
vision, or competition strategy. Those remain outside this lesson.

## 2. What the chooser means

`SendableChooser` is a WPILib dashboard selection object. It exposes named
options to the operator while the robot is being prepared. In this repository
`RobotContainer`, the composition root, owns the chooser and publishes it as
`Autonomous Routine`.

The chooser contains routine identities, not persistent command instances:

- `SAFE_STOP` is the safe/default routine. It owns Swerve and holds the
  drivetrain stopped through `AutonomousSafetyHoldCommand`.
- `ONE_METER_PATH` is the explicit driving option. It uses the known path
  through the frozen L07 AutoBuilder contract.

SAFE_STOP is the default because an unknown, missing, invalid, or unprepared
selection must never create motion. A driving routine is opt-in and requires a
valid accepted starting context.

## 3. Snapshot and fresh-command concepts

At `Robot.autonomousInit()`, `RobotContainer.getAutonomousCommand()` reads the
chooser once. That identity is the selection snapshot for the autonomous
session. The factory then consumes the accepted starting context once and
creates a new command.

The command is not reused on a later request. A fresh instance matters because
commands have scheduler state, timers, requirements, and cancellation history.
Changing the dashboard selection after a command has been created cannot mutate
that command or automatically restart the robot. A later autonomous session
must request a new snapshot and a fresh command.

## 4. Readiness: BACK and Reset Known Starting Pose

This repository has a Disabled-only preparation contract. The Xbox Back/View
button is bound to capture the field-heading reference. In this repository,
BACK is a heading-reference action; it is not a universal FRC requirement.

`Reset Known Starting Pose` then validates the current Disabled alliance/session
state and stores one accepted `AutonomousStartContext`. The context contains the
alliance, field variant, and execution start pose. `getAutonomousCommand()`
consumes that accepted context once. A later request without a new accepted
reset receives SAFE_STOP and cannot restart driving.

The correct manual preparation sequence is:

```text
DISABLED
  -> establish correct alliance/session state
  -> press BACK / capture heading reference
  -> Reset Known Starting Pose
  -> select the autonomous routine
  -> Autonomous
  -> Enable
```

The reset is not a motion command and must not be moved into enabled operation.

## 5. Alliance and path ownership

The path geometry is canonical Blue geometry. A Blue execution uses that
canonical geometry. A Red execution is produced by the single
`FieldAllianceTransform` authority from L04.

AutoBuilder vendor flipping remains disabled:

```text
shouldFlipPath = false
execution PathPlannerPath.preventFlipping = true
```

The two protections are intentional. L04 owns the alliance transform, while the
PathPlanner execution path is told not to mirror it again. Applying both an L04
transform and a vendor flip would double-transform the path and produce the
wrong field geometry. L08 does not create separate Blue and Red routines.

## 6. Requirements, stopping, and failure behavior

`SwerveSubsystem` remains the drivetrain requirement owner. The scheduler, not
manual locks, arbitrates command ownership. Both a driving command and the
SAFE_STOP command retain the Swerve requirement at the safe terminal boundary.

The following conditions fail closed to SAFE_STOP or stop through the existing
centralized authority:

- null or unknown routine selection;
- missing or invalid accepted readiness;
- unknown alliance;
- missing or malformed path asset;
- adapter or factory construction failure;
- cancellation, interruption, Disable, or mode loss.

Normal completion and every terminal path preserve `SwerveSubsystem.stop()`.
There is no automatic restart. Re-enabling the robot without a fresh accepted
reset does not recreate an autonomous command.

## 7. Architecture flow

```text
Driver Station / Autonomous Init
        |
        v
Autonomous Routine Chooser
        |
        v
Selection Snapshot
        |
        v
AutonomousRoutineFactory
        |
        +--> SAFE_STOP
        |
        +--> ONE_METER_PATH
                    |
                    v
          L07 AutoBuilder Contract
                    |
                    v
          L04 Alliance Transform Authority
                    |
                    v
              SwerveSubsystem
                    |
                    v
                    IO
                    |
                    v
                 Hardware
```

The normal mechanism backbone remains Driver -> controls -> commands ->
subsystems -> IO -> hardware. L08 only adds a bounded autonomous selection
boundary at the composition root.

## 8. Simulation verification procedure

Before enabling, confirm the chooser is visible and contains SAFE_STOP and
ONE_METER_PATH. Use the Disabled preparation sequence, select SAFE_STOP, enter
Autonomous, and Enable. SAFE_STOP must produce no drivetrain motion. Repeat
with a fresh Disabled preparation and explicit ONE_METER_PATH selection.

Verify Blue and Red separately. Confirm that the path starts from the accepted
pose, that Red is the single L04 transform of canonical Blue geometry, and that
the robot stops on Disable, cancellation, or mode loss. Re-enable without a new
accepted reset and confirm that motion does not restart.

The user verified these Simulation behaviors, including successful Blue and Red
execution. The dashboard UI did not permit switching to ONE_METER_PATH while
Autonomous was already enabled, so no manual runtime chooser-change result is
claimed. Automated tests and implementation review cover snapshot immutability.

## 9. Real Robot verification procedure

Only after Simulation passes, use conservative speeds, a physical stop/rollback
plan, Driver Station Disable control, and a clear workspace. Repeat the same
Disabled preparation and test SAFE_STOP before ONE_METER_PATH. Confirm Blue and
Red behavior, Disable/mode-loss stop, cancellation, and no restart without new
readiness. The user confirmed A01_L08 real-robot verification PASS.

## 10. What L08 proves and does not prove

L08 proves that this repository can expose two bounded routine identities,
snapshot a selection, create fresh commands, preserve one-shot readiness,
preserve Swerve requirement ownership, stop safely, and execute the known path
through the frozen L07/L04 contracts in Blue and Red contexts.

L08 intentionally does not prove exact endpoint accuracy, exact one-meter
precision, final PID or feedforward tuning, final RobotConfig physical
characterization, mass/MOI/COF characterization, or competition-ready
autonomous accuracy.

## 11. Knowledge check

1. Why is SAFE_STOP the default?  
   Because invalid or unprepared input must fail closed without motion.

2. When is the chooser read?  
   Once when `getAutonomousCommand()` snapshots the autonomous request.

3. Why create a fresh command?  
   A command carries scheduler, timer, requirement, and cancellation state.

4. Who owns the Red transform?  
   L04 `FieldAllianceTransform`, exactly once.

5. Why are both flipping protections required?  
   `shouldFlipPath=false` and `preventFlipping=true` prevent a second mirror.

6. What makes a new autonomous start valid?  
   A new Disabled-only heading/reset preparation and accepted start context.

7. What happens on mode loss?  
   The active command is canceled and centralized Swerve stop is preserved.

## 12. Final lesson state

A01_L08 is `COMPLETE / FROZEN / READ-ONLY`. The post-repair WPILib VS Code
build was user-verified as `BUILD SUCCESSFUL in 1s` with `6 actionable tasks: 1
executed, 5 up-to-date`; the full test result is 430/430 PASS, and Simulation
and Real Robot evidence are PASS. A01_L08 is the frozen inheritance source for
A01_L09, which remains `NOT CREATED / NOT STARTED`.

This closure does not claim exact endpoint accuracy, final PID/feedforward
tuning, or final physical characterization.
