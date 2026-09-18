# M00_L04 - Intake Command Ownership

## Lesson identity

- **Status:** `COMPLETE / FROZEN / READ-ONLY`
- **Freeze state:** `FROZEN`
- **Editable boundary:** `NONE`
- **Active lesson:** `NO`
- **Current active M00 lesson:** `NONE`
- **Active lesson count:** `0`
- **Predecessor:** `M00_L03 - Intake Foundation`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Preparation:** `PASS`
- **Baseline build:** `PASS / BUILD SUCCESSFUL IN 48s / EXIT CODE 0`
- **Architecture / inheritance audit:** `PASS`
- **Final Design Lock:** `PASS_M00_L04_FINAL_DESIGN_LOCK`
- **Implementation:** `COMPLETE`
- **Implementation authorization:** `CONSUMED`
- **Focused tests:** `VERIFIED / 14 OF 14 PASS`
- **Full regression:** `VERIFIED`
- **Simulation:** `SIMULATION VERIFIED / BOUNDED`
- **Driver Station:** `VERIFIED / BOUNDED`
- **Independent implementation review:** `PASS`
- **Student documentation:** `COMPLETE / VERIFIED`
- **Final closure build:** `PASS / BUILD SUCCESSFUL IN 23s / 7 OF 7 TASKS EXECUTED / EXIT CODE 0`
- **Final closure review:** `PASS`
- **Lifecycle freeze:** `COMPLETE / FROZEN / READ-ONLY`
- **Primary publication:** `COMPLETE / 5c86be3`
- **Primary remote alignment:** `PASS`
- **Publication metadata reconciliation:** `COMPLETE IN WORKING TREE`
- **Metadata Git publication:** `PENDING USER ACTION`
- **Final publication verification:** `PENDING`
- **M00_L05:** `NOT ACTIVE / NOT CREATED`

## Sole new concept

```text
SCHEDULER-MANAGED MANUAL OWNERSHIP OF THE EXISTING INTAKE CAPABILITY
```

M00_L03 supplied the complete vendor-neutral Intake capability:
`IntakeSubsystem`, `IntakeIO`, requested states, centralized safe stop,
immutable `IntakeObservation`, and read-only telemetry. M00_L04 does not
redesign those responsibilities.

This lesson adds only the implemented scheduler-owned manual command path:

```text
Xbox Controller Right Bumper hold
-> RunIntakeCommand
-> requires IntakeSubsystem
-> existing requestIntake() / stop()
-> existing IntakeIO boundary
```

## Implemented lifecycle meaning

The command owns scheduler lifecycle and requirement declaration. When
scheduled, `initialize()` requests Intake once. The command remains scheduled
while the Right Bumper is held; `execute()` does not repeat the request and
`isFinished()` remains false. Release, cancellation, interruption, competing
ownership transfer, or defensive normal ending invokes `end(...)`, which
unconditionally delegates to `IntakeSubsystem.stop()`.

The subsystem continues to own Intake behavior and requested software state.
IO remains the vendor-neutral hardware/simulation boundary. Observation and
telemetry remain unchanged and read-only. No Intake default command is added.

## Implemented binding

The existing Xbox controller on port 0 uses the semantic accessor:

```java
driverController.rightBumper().whileTrue(runIntakeCommand);
```

The inherited Back/View Prepare Autonomous binding remains unchanged. The
Right Bumper was confirmed unbound before activation.

## Scope protection

No vendor adapter, motor/controller choice, CAN identity, inversion, gearing,
current limit, sensor, PID, physical direction, speed, motion, acquisition, or
physical stopping claim is introduced. `Constants.java` remains unchanged.
Real hardware remains `REAL HARDWARE DEFERRED`.

## Verified runtime software evidence

The observed bounded Driver Station sequence was:

```text
STOPPED
-> hold Right Bumper
-> INTAKE_REQUESTED
-> release Right Bumper
-> STOPPED
```

The selected implementation is deterministic, vendor-neutral `IntakeIONoop`.
It intentionally reports `Available=false` and `Connected=false` and produces
no physical output even while `RequestedState` is `INTAKE_REQUESTED`.
`RequestedState` is software intent; it is not proof that a motor moved.
Release, cancellation, or interruption reaches `end(true)` and delegates to
`IntakeSubsystem.stop()`.

## Evidence boundary

- Focused tests: `VERIFIED / 14 OF 14 PASS`
- Full regression: `VERIFIED`
- Simulation: `SIMULATION VERIFIED / BOUNDED`
- Driver Station: `VERIFIED / BOUNDED`
- Glass: `NOT APPLICABLE` as a distinct completion gate
- Real hardware: `REAL HARDWARE DEFERRED`

No motor/controller, CAN, wiring, inversion, gearing, speed, current, sensor,
game-piece, physical-motion, or physical-stop claim is made.

## Final lifecycle state

Preparation, architecture review, implementation, automated verification,
bounded runtime verification, independent implementation review, paired
student documentation, documentation repair/rereview, final closure build,
transition reconciliation, independent confirmation, and resumed final
closure review are complete. M00_L04 is `COMPLETE / FROZEN / READ-ONLY`, its
editable boundary is `NONE`, and active lesson count is `0`.

Primary M00_L04 publication is complete at commit `5c86be3`, and primary
remote alignment is `PASS`. This publication-metadata reconciliation is
complete in the working tree, but the separate User-owned metadata commit,
push, remote verification, and final publication verification remain pending.
No metadata SHA is claimed. M00_L05 remains `NOT ACTIVE / NOT CREATED`.

Student guides:

- [English learning guide](docs/M00_L04_Intake_Command_Ownership_Learning_Guide_EN.md)
- [Vietnamese learning guide](docs/M00_L04_Intake_Command_Ownership_Learning_Guide_VI.md)

See [M00_L03 to M00_L04 transition guide](docs/M00_L03_to_M00_L04_Step_by_Step.md).
