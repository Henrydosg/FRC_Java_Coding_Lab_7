# M00_L05 - Feeder Foundation

## Lesson identity

- **Status:** `COMPLETE / FROZEN / READ-ONLY`
- **Freeze state:** `FROZEN / READ-ONLY`
- **Active lesson:** `NO / NONE`
- **Active lesson count:** `0`
- **Predecessor:** `M00_L04 - Intake Command Ownership`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Baseline build:** `PASS / BUILD SUCCESSFUL IN 41s / JAVA 17.0.16`
- **Inheritance:** `PASS / 285 OF 285 GOVERNED FILES BYTE-IDENTICAL`
- **Architecture audit:** `PASS_M00_L05_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`
- **Final Design Lock:** `PASS_M00_L05_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION`
- **Controlled activation:** `COMPLETE`
- **Implementation:** `COMPLETE / VERIFIED`
- **Focused tests:** `PASS / VERIFIED`
- **Full regression:** `PASS / VERIFIED / 682 TESTS`
- **Simulation:** `SIMULATION VERIFIED / BOUNDED`
- **Simulated Driver Station:** `VERIFIED / BOUNDED VIA HALSIM ROBOT STATE`
- **Independent implementation review:** `PASS`
- **Documentation:** `COMPLETE / INDEPENDENT DOCUMENTATION REREVIEW PASS`
- **Final closure build:** `PASS / BUILD SUCCESSFUL IN 41s / 7 OF 7 TASKS EXECUTED / EXIT CODE 0`
- **Final closure review:** `PASS`
- **Primary publication:** `COMPLETE / PUSHED / REMOTE-ALIGNED`
- **Primary commit:** `5709f1d74b3318303bcc56779315b243dd81770b`
- **Primary commit subject:** `Complete M00_L05 feeder foundation`
- **Publication metadata reconciliation:** `COMPLETE`
- **Metadata publication:** `PENDING / NOT YET COMMITTED`
- **Final publication verification:** `PENDING`
- **Real hardware:** `REAL HARDWARE DEFERRED`
- **M00_L06:** `NOT ACTIVE / NOT CREATED`

## Sole new concept

```text
FEEDER AS ONE INDEPENDENTLY OWNED TRANSPORT MECHANISM CAPABILITY
```

M00_L05 establishes a vendor-neutral Feeder capability using the same
Frozen Backbone pattern already demonstrated by Intake. It does not introduce
manual command ownership, operator controls, coordination, shooting, staging,
jam behavior, sensing policy, or autonomous events.

## Implemented architecture

```text
CONTROL
future caller
-> FeederSubsystem.requestFeed()
-> FeederIO
-> FeederIONoop

OBSERVATION
FeederIONoop
-> mutable FeederIOInputs
-> FeederSubsystem
-> immutable FeederObservation
-> read-only FeederTelemetryFacade
-> RobotTelemetry
```

The requested states are exactly `STOPPED` and `FEED_REQUESTED`.
`FEED_REQUESTED` means software requests transport; it does not prove a
motor turns or a game piece moves.

## Runtime and hardware boundary

M00_L05 uses `FeederIONoop` only. Dynamic Feeder simulation, a real adapter,
CTRE/REV APIs, physical CAN configuration, and `Constants.java` changes are
not authorized. CAN 45-49 remains a planning reservation, not a physical
assignment. Motor/controller, motor count, bus, ID, ratio, inversion, sensors,
and electrical limits remain `UNKNOWN`. Real hardware is
`REAL HARDWARE DEFERRED`.

## Implemented file boundary

The authorized implementation created:

- `FeederIO.java`
- `FeederIONoop.java`
- `FeederSubsystem.java`
- `FeederObservation.java`
- `FeederTelemetryFacade.java`

and modified only:

- `RobotContainer.java`
- `RobotTelemetry.java`

The focused-test boundary is:

- `FeederIONoopTest.java`
- `FeederSubsystemTest.java`
- `FeederObservationTest.java`
- `FeederTelemetryFacadeTest.java`
- `RobotContainerFeederCompositionTest.java`
- `FeederArchitectureBoundaryTest.java`

No production or test file outside this boundary changed.

## Safe-stop contract

`FeederSubsystem.stop()` sets requested state to `STOPPED`, refreshes the
immutable observation, and invokes `FeederIO.stop()` exactly once. If IO stop
throws, software intent remains `STOPPED`.

## Periodic and Noop behavior

`periodic()` updates the mutable one-cycle `FeederIOInputs` and rebuilds the
immutable `FeederObservation`. It does not issue `requestFeed()` or `stop()`,
publish NetworkTables directly, read controller state, or use vendor APIs.

`FeederIONoop` is the only runtime selection. It reports `available=false` and
`connected=false`; `requestFeed()` and `stop()` are deterministic no-ops. No
physical output occurs.

## Accepted verification evidence

- `THEORY / ARCHITECTURE VERIFIED`
- `FOCUSED TESTS VERIFIED`: all six authorized classes passed after one
  architecture-test false-positive repair; `BUILD SUCCESSFUL in 26s`, exit code 0.
- `FULL CLEAN BUILD REGRESSION VERIFIED`: 682/682 tests passed after one
  `FeederSubsystemTest` scheduler-isolation repair; `BUILD SUCCESSFUL in 51s`, exit code 0.
- `SIMULATION VERIFIED / BOUNDED`: stable Noop composition, observation flow,
  telemetry, and software state.
- `SIMULATED DRIVER-STATION VERIFIED / BOUNDED`: HALSIM Robot State passed in
  Disabled and Teleoperated Enabled states with false/false/`STOPPED` telemetry.
- `REAL HARDWARE DEFERRED`: no motor, controller, CAN bus/ID, ratio, inversion,
  sensor, current-limit, or output-limit claim is made.

The architecture-test failure came from searching comments for `current`; it
was repaired with reflection over actual instance fields. The full-suite
failure came from singleton `CommandScheduler` subsystem registration surviving
intentional constructor rejection; `@AfterEach` now calls
`unregisterAllSubsystems()`. Neither incident was a production defect.

## Protected later lessons

- M00_L06 owns Feeder command and scheduler ownership.
- M00_L14 owns future shooting coordination.
- M00_L15 owns Intake-to-Feeder coordination.
- M00_L16 owns mechanism autonomous-event integration.

M00_L04 remains frozen and published. M00_L06 remains
`NOT ACTIVE / NOT CREATED`.

M00_L06 may teach scheduler-managed Feeder command ownership only after
separate preparation and activation authorization. No Feeder command or
controller binding exists in M00_L05.

Current lifecycle is `COMPLETE / FROZEN / READ-ONLY`; active lesson count is
`0`, and no M00 lesson is active. Independent documentation rereview, final
closure build, final closure review, and lifecycle freeze are complete. Primary
Git publication is complete at `5709f1d74b3318303bcc56779315b243dd81770b`,
and accepted evidence confirms primary remote alignment. Publication metadata
reconciliation is complete. Metadata publication and final publication
verification remain pending and User-owned; no metadata commit is claimed.

See [M00_L04 to M00_L05 transition guide](docs/M00_L04_to_M00_L05_Step_by_Step.md).
