# M00_L06 — Feeder Command Ownership

M00_L06 adds exactly one architectural concept to frozen M00_L05: a command owns the driver's request to run the Feeder. The lesson is `COMPLETE / FROZEN / READ-ONLY`; implementation, accepted verification, documentation, final independent closure rereview, lifecycle freeze, primary publication, and publication metadata reconciliation are complete. The separate metadata Git publication and final publication verification remain pending.

## Learning objective

Students learn how a WPILib command translates a held driver control into subsystem requests without moving mechanism behavior into the command or into `RobotContainer`.

The implemented lifecycle is:

1. the driver holds the left bumper;
2. `RunFeederCommand.initialize()` requests `FEED_REQUESTED` once;
3. the command stays scheduled while the trigger is true;
4. `execute()` performs no repeated mechanism calculation;
5. releasing the bumper or disabling the robot interrupts the command; and
6. `end(...)` requests `STOPPED`.

`RunFeederCommand` requires only `FeederSubsystem`, never finishes on its own, and cannot run while disabled. The Feeder has no default command. `RobotContainer` creates dependencies and declares the binding; it does not own feeder behavior.

## Verification evidence

- Focused test set: PASS — four named test classes, `BUILD SUCCESSFUL in 7s`, four tasks up-to-date, exit code 0.
- Full clean regression: PASS — `gradlew clean build`, `BUILD SUCCESSFUL in 37s`, seven tasks executed, exit code 0.
- Bounded Simulation: PASS — checkpoints A–G, including release-stop and disable-while-held-stop behavior.
- Theory: `VERIFIED`.
- Simulation: `VERIFIED`.
- Real hardware: `DEFERRED`.

The inherited `FeederArchitectureBoundaryTest` update is `EXPECTED INHERITED TEST CONTRACT EVOLUTION`: its earlier frozen assumption correctly changed when the roadmap-authorized command class was introduced. It is not evidence of a production defect.

## Hardware limits

This lesson uses `FeederIONoop`. Therefore `available = false` and `connected = false` are expected. CAN IDs 45–49 are planning reservations only. No physical Feeder, motor controller, sensor, wiring, direction, current limit, tuning, or real-robot behavior is verified here.

Manual simultaneous bumper input is not a mechanism-coordination contract. Intake/Feeder coordination belongs to protected future scope. M00_L14, M00_L15, and M00_L16 are not pulled into this lesson.

## Lifecycle closure

The final Independent Closure Rereview returned `READY_FOR_FREEZE` with no
remaining findings. The Architect accepted
`PASS_M00_L06_INDEPENDENT_CLOSURE_REREVIEW_ACCEPTED` and authorized
`AUTHORIZED_FOR_FREEZE`. Active lesson count is `0`; no M00 lesson is active,
and M00_L07 is not active or created. The later primary publication is recorded
below; metadata Git publication and final verification remain pending and
User-owned.

## Publication state

Accepted gate `PASS_M00_L06_PRIMARY_PUBLICATION` records User-owned primary
publication commit `f102a5e662877f8cb49eb63f2cfd888ac356bea4` with subject
`Complete M00_L06 Feeder command ownership`. Primary push is `PASS`, and
accepted remote evidence records `HEAD = origin/main =
f102a5e662877f8cb49eb63f2cfd888ac356bea4`; primary remote alignment is
`PASS`.

Publication metadata reconciliation is `COMPLETE / PREPARED FOR USER COMMIT`.
The metadata Git commit/push and final publication verification remain
`PENDING`. Final `PUBLISHED / VERIFIED` status is not yet claimed, and M00_L07
remains `NOT ACTIVE / NOT CREATED`.

## Student documents

- [English learning guide](docs/M00_L06_Feeder_Command_Ownership_Learning_Guide_EN.md)
- [Vietnamese learning guide](docs/M00_L06_Feeder_Command_Ownership_Learning_Guide_VI.md)
- [M00_L05 to M00_L06 transition guide](docs/M00_L05_to_M00_L06_Step_by_Step.md)
- [Lesson status](LESSON_STATUS.md)
- [Lesson plan](LESSON_PLAN.md)
- [Lesson checklist](LESSON_CHECKLIST.md)
