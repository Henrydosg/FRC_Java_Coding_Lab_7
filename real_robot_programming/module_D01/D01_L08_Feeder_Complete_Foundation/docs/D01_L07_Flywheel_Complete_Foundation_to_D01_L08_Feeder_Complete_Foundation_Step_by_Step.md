# D01_L07 Flywheel Complete Foundation to D01_L08 Feeder Complete Foundation

## Purpose

This guide records the verified inheritance transition from the completed and frozen
`D01_L07_Flywheel_Complete_Foundation` project to
`D01_L08_Feeder_Complete_Foundation`.

D01_L08 preserves the inherited Drivebase, Intake, and Flywheel implementations. It adds one
Feeder foundation without coordinating or merging existing mechanism responsibilities.

## Step 1 - Inherit the Frozen Source Lesson

**Objective**

Use the completed D01_L07 project as the D01_L08 starting point.

**Why**

The repository development model requires each lesson to inherit the previous completed lesson
instead of recreating the project.

**Action**

Copy the frozen D01_L07 project into the D01_L08 lesson and keep all inherited mechanism
responsibilities unchanged.

**Files Changed**

- Lesson project copy only.

**Verification**

- Confirm D01_L07 remains `COMPLETE` and `FROZEN`.
- Confirm Git reports no changes inside D01_L07.

**Expected Result**

D01_L08 begins with the exact frozen D01_L07 source architecture.

## Step 2 - Define Feeder Constants

**Objective**

Record approved Feeder hardware, safety, and manual-control values.

**Why**

Mechanism configuration must remain centralized and must not use magic numbers.

**Action**

Add Feeder CAN ID, inversion, brake mode, current limit, open-loop ramp, peak outputs, manual
forward and reverse outputs, safe stop, and telemetry topic keys to `Constants.java`.

**Files Changed**

- `src/main/java/frc/robot/Constants.java`

**Verification**

- Confirm all approved values match the final configuration.
- Confirm Feeder constants are grouped in `FeederConstants`.

**Expected Result**

The approved Feeder configuration has one default authority.

## Step 3 - Add the Vendor-Neutral Feeder IO Contract

**Objective**

Define the hardware capabilities and one-cycle observation snapshot required by the Feeder.

**Why**

The subsystem must depend on a vendor-neutral interface rather than REVLib.

**Action**

Add `FeederIO` with `FeederIOInputs`, `updateInputs`, `setOutput`, and `stop`.

**Files Changed**

- `src/main/java/frc/robot/io/feeder/FeederIO.java`

**Verification**

- Confirm the interface contains no REVLib or NetworkTables types.
- Confirm observation names include explicit units.

**Expected Result**

Real, simulation, and noop implementations can replace one another without subsystem changes.

## Step 4 - Add the Real Spark MAX Adapter

**Objective**

Control and observe the physical Feeder through the approved IO boundary.

**Why**

Vendor APIs and hardware configuration belong only in the real IO implementation.

**Action**

Add `FeederIOSparkMax` for the NEO Brushless motor and Spark MAX on CAN ID 19. Configure inversion
`false`, Brake mode, a 30 A smart current limit, a 0.20 s open-loop ramp, output clamping, the
integrated encoder, configuration health, connection state, and safe stop.

Report unsupported Spark MAX stator current deterministically as `0.0`.

**Files Changed**

- `src/main/java/frc/robot/io/feeder/FeederIOSparkMax.java`

**Verification**

- Confirm all REVLib imports remain in this adapter.
- Confirm the adapter starts and stops safely.
- Confirm integrated encoder position and velocity populate `FeederIOInputs`.

**Expected Result**

The real Feeder is accessible through `FeederIO` without leaking vendor dependencies.

## Step 5 - Add Simulation and Noop IO

**Objective**

Provide deterministic non-hardware Feeder implementations.

**Why**

Simulation and safe fallback behavior must use the same frozen IO contract.

**Action**

Add `FeederIOSim` to store bounded commanded output and publish deterministic observations. Add
`FeederIONoop` to ignore output requests and report stopped, disconnected values.

**Files Changed**

- `src/main/java/frc/robot/io/feeder/FeederIOSim.java`
- `src/main/java/frc/robot/io/feeder/FeederIONoop.java`

**Verification**

- Confirm simulation output is clamped to `-0.40` through `+0.40`.
- Confirm noop output remains stopped.
- Confirm stator current is `0.0`.

**Expected Result**

The Feeder subsystem can run without physical hardware and has a deterministic safe fallback.

## Step 6 - Add Feeder State and Behavior

**Objective**

Own Feeder behavior, safety clamping, and mechanism state in the subsystem.

**Why**

Subsystems own mechanism behavior and state while remaining vendor-independent.

**Action**

Add `FeederObservation` with `FEEDING`, `REVERSING`, and `STOPPED` modes. Add `FeederSubsystem` to
consume `FeederIOInputs`, clamp output, update mode, expose immutable observations, and stop
safely.

**Files Changed**

- `src/main/java/frc/robot/observation/feeder/FeederObservation.java`
- `src/main/java/frc/robot/subsystems/FeederSubsystem.java`

**Verification**

- Positive output selects `FEEDING`.
- Negative output selects `REVERSING`.
- Zero output and `stop()` select `STOPPED`.

**Expected Result**

Feeder state and safety behavior remain inside the Feeder subsystem boundary.

## Step 7 - Add Manual Input Processing and Command Coordination

**Objective**

Convert Xbox bumper requests into safe manual Feeder behavior.

**Why**

Driver intent processing belongs in `controls`, while command coordination belongs in
`commands`.

**Action**

Add `FeederInputProcessor` using the inherited Intake conflict pattern:

- Right bumper only returns `+0.20`.
- Left bumper only returns `-0.20`.
- Both or neither returns `0.0`.

Add `ManualFeederCommand` with both button suppliers, the Feeder subsystem requirement, and safe
stop in `end`.

**Files Changed**

- `src/main/java/frc/robot/controls/FeederInputProcessor.java`
- `src/main/java/frc/robot/commands/feeder/ManualFeederCommand.java`

**Verification**

- Confirm the four input combinations produce the approved outputs.
- Confirm interruption invokes `FeederSubsystem.stop()`.

**Expected Result**

Manual Feeder control is deterministic, conflict-safe, and interruption-safe.

## Step 8 - Add Read-Only Feeder Telemetry

**Objective**

Publish Feeder observations without changing robot behavior.

**Why**

Telemetry must observe subsystem snapshots and must never access or control hardware.

**Action**

Add `FeederTelemetryFacade` with typed NetworkTables publishers. Extend `RobotTelemetry` to
publish the latest immutable `FeederObservation`.

**Files Changed**

- `src/main/java/frc/robot/telemetry/feeder/FeederTelemetryFacade.java`
- `src/main/java/frc/robot/telemetry/RobotTelemetry.java`

**Verification**

- Confirm `/Feeder` publishes applied output, encoder position and velocity, current,
  temperature, connection, configuration health, and mode.
- Confirm telemetry has no output or scheduling methods.

**Expected Result**

Glass can observe the Feeder through the existing read-only telemetry flow.

## Step 9 - Compose and Bind the Feeder

**Objective**

Select implementations, inject dependencies, and bind both hold-to-run controls.

**Why**

`RobotContainer` is the approved composition root.

**Action**

Create the Feeder input processor, runtime-selected IO implementation, subsystem, telemetry
facade, and manual command binding in `RobotContainer`.

Bind the logical OR of right and left bumpers to one `ManualFeederCommand`, passing both current
button states to the input processor.

**Files Changed**

- `src/main/java/frc/robot/RobotContainer.java`

**Verification**

- Confirm `RobotContainer` contains composition and bindings only.
- Confirm there is no Feeder hardware logic, input processing, or telemetry calculation.

**Expected Result**

Either bumper schedules the command, simultaneous requests resolve to stop, and releasing both
bumpers ends the command safely.

## Step 10 - Verify and Freeze the Lesson

**Objective**

Record verified results and close the lesson as a frozen inheritance snapshot.

**Why**

A lesson may be frozen only after implementation, build, simulation, Glass, and real-robot
verification are complete.

**Action**

Record the final hardware, configuration, controls, telemetry, safety behavior, verification
evidence, inheritance source, and next-lesson boundary in `README.md` and `LESSON_STATUS.md`.

**Files Changed**

- `README.md`
- `LESSON_STATUS.md`
- `docs/D01_L07_Flywheel_Complete_Foundation_to_D01_L08_Feeder_Complete_Foundation_Step_by_Step.md`

**Verification**

- Build: PASS.
- Simulation: PASS.
- Driver Station / Glass: PASS.
- Real Robot: PASS.
- Right bumper feed: PASS.
- Left bumper reverse: PASS.
- Simultaneous request stop: PASS.
- Release safe stop: PASS.
- Motor direction: PASS.

**Expected Result**

`D01_L08_Feeder_Complete_Foundation` is `COMPLETE` and `FROZEN`, ready to be inherited by the
next Shooter lesson.

## Next Lesson Boundary

The next lesson is Shooter integration. It may coordinate the existing Flywheel and Feeder
subsystems through commands, but it must not merge their subsystem responsibilities, IO
contracts, state ownership, or telemetry boundaries.

D01_L09 is not created by this transition.
