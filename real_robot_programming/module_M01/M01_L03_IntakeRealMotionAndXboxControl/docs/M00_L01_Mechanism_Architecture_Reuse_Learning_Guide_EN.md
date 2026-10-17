# M00_L01 - Mechanism Architecture Reuse

- **Course:** `FRC Java Coding Lab 7.0`
- **Lesson:** `M00_L01 - Mechanism Architecture Reuse`
- **Language:** English — normative technical version

## 1. Why This Lesson Exists

Earlier modules taught architecture through real robot responsibilities. Swerve taught subsystem and IO ownership, safe output, and state observation. Autonomous taught command and scheduler ownership. Vision taught vendor isolation, immutable observations, read-only telemetry, and a guarded handoff to the estimator owner.

M00 now applies those same ideas to future non-drivetrain competition mechanisms. The important principle is:

> **NEW MECHANISM DOES NOT MEAN NEW ARCHITECTURE.**

A new mechanism has a new physical purpose, but it still needs clear ownership, vendor-neutral IO, measured inputs, immutable meaning, read-only telemetry, command coordination, and safe stop. Reusing this discipline reduces duplicated logic and makes failures easier to locate. It also makes later lessons easier: students can concentrate on one new behavior instead of relearning the whole project structure.

This lesson teaches the architecture map only. It does not create mechanism code or select hardware.

## 2. Learning Objectives

By the end of this lesson, students should be able to:

- identify subsystem ownership boundaries;
- distinguish a Subsystem from IO;
- explain the purpose of IOInputs;
- explain why an Observation is immutable;
- explain why telemetry is read-only;
- explain RobotContainer as the Composition Root;
- explain command and scheduler ownership;
- identify safe-stop responsibility;
- explain vendor isolation; and
- map mastered architecture onto future mechanisms.

## 3. What Is Reused From Earlier Modules?

M00 reuses these established patterns:

- **Composition Root:** RobotContainer creates and connects objects.
- **Subsystem ownership:** one subsystem owns one mechanism capability and its persistent state.
- **IO abstraction:** the subsystem depends on a vendor-neutral hardware contract.
- **IOInputs:** measured hardware facts move upward in a one-cycle transport snapshot.
- **Concrete hardware adapters:** vendor APIs and device access stay at the hardware boundary.
- **Immutable Observations:** interpreted mechanism meaning is safe to share with readers.
- **Telemetry:** observations are published without changing robot behavior.
- **Command ownership:** commands request or coordinate actions through subsystem APIs.
- **Scheduler ownership:** the scheduler manages command lifecycle and requirements.
- **Safe-stop responsibility:** the subsystem provides the safe path to stop its owned mechanism.

Architecture reuse is not source-code copy/paste. We reuse responsibilities, dependency direction, and reasoning. We do not copy Swerve calculations into an Intake or invent mechanism-specific behavior before its lesson.

```text
ARCHITECTURE REUSE = reuse the ownership pattern
SOURCE-CODE COPY/PASTE = duplicate an implementation, often with the wrong meaning
```

## 4. The Core Mechanism Architecture

The conceptual construction relationship is:

```text
RobotContainer
    |
    +--> MechanismSubsystem
            |
            +--> MechanismIO
                    |
                    +--> MechanismIOHardware
                            |
                            +--> Physical Hardware
```

The observation direction is:

```text
Physical Hardware
        ↓
MechanismIOInputs
        ↓
MechanismSubsystem processing
        ↓
Immutable Observation
        ↓
Read-only Telemetry
```

- **RobotContainer** selects implementations and connects dependencies.
- **MechanismSubsystem** owns mechanism behavior and state.
- **MechanismIO** describes only the hardware capabilities the subsystem needs.
- **MechanismIOHardware** is a conceptual concrete adapter that talks to the selected device API.
- **Physical Hardware** is the actual actuator or sensor boundary.
- **MechanismIOInputs** carries measured facts from one update cycle.
- **Immutable Observation** gives those facts stable, vendor-neutral meaning.
- **Read-only Telemetry** publishes that meaning for people and tools to inspect.

These names describe a generic pattern. This lesson does not request generic Java classes with these names.

## 5. Control Flow vs Observation Flow

Control flow requests action:

```text
Driver / Autonomous intent
        ↓
Command
        ↓
Subsystem
        ↓
IO
        ↓
Hardware
```

Observation flow reports knowledge:

```text
Hardware
    ↓
IOInputs
    ↓
Subsystem processing
    ↓
Immutable Observation
    ↓
Telemetry
```

Control asks the robot to do something. Observation reports what the mechanism currently knows. Telemetry displays or records the observation; it never commands the mechanism.

RobotContainer assembles this object graph. It is not a normal runtime stage in either flow. After construction, commands, subsystems, IO, observations, and telemetry perform their own approved responsibilities.

## 6. Subsystem Ownership

Use one-owner thinking: every persistent mechanism responsibility must have one clear owner.

The M00 roadmap authorizes these future owners:

- `IntakeSubsystem` owns Intake responsibility.
- `FeederSubsystem` owns Feeder responsibility.
- `FlywheelSubsystem` owns Flywheel responsibility.
- `ElevatorSubsystem` owns Elevator responsibility.

A subsystem owns its mechanism state and behavior and exposes a high-level capability boundary to commands. Other packages should not create a second copy of that state or control the same hardware independently.

This lesson does not define final methods, fields, control algorithms, or device behavior for any future subsystem. Those decisions belong to later lessons.

## 7. Why There Is No ShooterSubsystem

The approved future shooting architecture is:

```text
FlywheelSubsystem
+
FeederSubsystem
+
ShootCommand requiring both
```

`FlywheelSubsystem` owns flywheel state and behavior. `FeederSubsystem` owns feeder state and behavior. A future `ShootCommand` coordinates the two existing owners for one shooting operation and declares both requirements so the scheduler can protect ownership.

Coordination does not create another hardware owner. A `ShooterSubsystem` wrapping Flywheel and Feeder would blur which subsystem owns their state, safe stop, and hardware. A `ShooterIO` would likewise combine two independently governed capabilities into an unauthorized hardware boundary.

M00_L01 does not implement `ShootCommand`, `ShooterSubsystem`, or `ShooterIO`.

## 8. IO and Vendor Isolation

The hardware dependency direction is:

```text
MechanismSubsystem
        ↓
vendor-neutral MechanismIO
        ↓
concrete hardware IO adapter
        ↓
vendor API
        ↓
physical device
```

Vendor APIs belong only inside concrete IO adapters. Commands, observations, telemetry, and generic subsystem contracts must not directly depend on a motor-controller or sensor vendor API.

This isolation keeps the subsystem focused on mechanism capability. A different device implementation can replace the adapter without forcing commands or telemetry to learn a new vendor library.

This lesson does not select motors, motor controllers, sensors, CAN IDs, gear ratios, current limits, PID constants, hardware models, geometry, or control modes. Those require later evidence and Design Locks.

`Constants.java` remains the default configuration authority; M00_L01 does not define mechanism configuration values.

## 9. IOInputs and Measurements

IOInputs transports measured hardware facts upward for one logical update cycle. It is mutable transport data populated by the concrete IO implementation; it is not the public meaning of the mechanism.

**CONCEPTUAL EXAMPLES ONLY:**

- whether a device is connected;
- a measured position;
- a measured velocity;
- a measured current; or
- a limit-state reading.

These examples are not final Intake, Feeder, Flywheel, or Elevator IO contracts. M00_L01 does not choose their fields, units, sensors, validity rules, or record types.

The subsystem reads a coherent IOInputs snapshot, interprets only what its mechanism contract needs, and creates stable meaning for consumers.

## 10. Immutable Observation

The learned pattern is:

```text
hardware fact
    ↓
IOInputs
    ↓
subsystem interpretation
    ↓
immutable Observation
```

An Observation is stable after construction, vendor-neutral, and read-only to consumers. It can express mechanism meaning, explicit units, validity, and timing when those are relevant. Telemetry, logging, tests, and approved higher-level readers can consume it without receiving a mutable hardware transport object.

Immutability prevents a reader from silently changing the state seen by another reader. Vendor neutrality prevents device-library details from leaking upward.

An Observation reports meaning. It does not command hardware, schedule commands, read devices, or publish itself.

## 11. Read-Only Telemetry

The publication path is:

```text
Observation
    ↓
Telemetry
    ↓
NT4 / Glass / logging
```

Telemetry may inspect and publish state. It must not:

- command motors;
- reset mechanisms;
- mutate subsystem state;
- own hardware; or
- bypass subsystem and command ownership.

> **TELEMETRY IS AN OBSERVER, NOT A CONTROLLER.**

If dashboard information suggests that an action is needed, a command or subsystem still owns that action. The telemetry layer remains read-only.

## 12. Safe Stop

Every mechanism subsystem must eventually provide a safe way for its owned mechanism to stop.

```text
Command finishes / is interrupted
        ↓
Subsystem safe-stop responsibility
        ↓
IO
        ↓
Hardware-safe output
```

The command requests or coordinates the end of an action. The subsystem owns the mechanism-level safe-stop responsibility and routes it through IO. This prevents unrelated callers from inventing different stop behavior.

M00_L01 does not define mechanism-specific stop outputs. It does not choose percentages, voltages, currents, brake/coast states, or control modes.

## 13. RobotContainer as Composition Root

RobotContainer creates and wires:

- IO implementations;
- subsystems;
- commands;
- bindings; and
- telemetry dependencies.

RobotContainer must not become the location for mechanism control mathematics, vendor configuration algorithms, sensor interpretation, state machines, or persistent mechanism behavior.

```text
RobotContainer assembles ownership.
It does not replace ownership.
```

Selecting a real, simulation, or no-op implementation is composition. Calculating how a mechanism should behave is not.

## 14. Mapping From Swerve to Mechanisms

| Architecture idea | Swerve example | Future mechanism equivalent |
| --- | --- | --- |
| Subsystem owner | `SwerveSubsystem` owns drivetrain capability and state | One independently owned Intake, Feeder, Flywheel, or Elevator subsystem |
| IO interface | Swerve module and gyro IO contracts | One vendor-neutral IO contract for the owned mechanism |
| Concrete IO adapter | Existing concrete Swerve/gyro hardware adapters | A later selected mechanism hardware adapter |
| IOInputs | Module and gyro one-cycle measurements | One-cycle mechanism measurements defined in its later lesson |
| Immutable observation | `SwerveObservation` | A future mechanism-specific immutable Observation |
| Safe stop | Centralized Swerve stop authority | The mechanism subsystem's own safe-stop boundary |
| Command ownership | Teleop/autonomous commands require Swerve | A command requires the mechanism subsystem it requests or coordinates |
| Telemetry | Swerve telemetry publishes immutable state | A mechanism telemetry facade publishes its Observation |
| Composition root | RobotContainer selects and wires Swerve dependencies | RobotContainer selects and wires the future mechanism dependencies |

This comparison is architectural only. Future mechanisms do not inherit Swerve kinematics, odometry, pose estimation, module states, or drivetrain mathematics.

## 15. Four Future Mechanism Owners

```text
RobotContainer
 ├── IntakeSubsystem
 ├── FeederSubsystem
 ├── FlywheelSubsystem
 └── ElevatorSubsystem
```

These are future ownership boundaries authorized by the M00 roadmap. RobotContainer will eventually compose them, but each subsystem will remain responsible for its own capability.

M00_L01 does not implement these classes, describe their hardware, or define APIs that later lessons have not authorized.

## 16. Common Architecture Mistakes

- **Vendor API directly inside a Command:** the command becomes tied to hardware and bypasses subsystem ownership.
- **Vendor API directly inside RobotContainer:** the composition root grows hardware behavior instead of only selecting and wiring implementations.
- **Telemetry controlling hardware:** an observer becomes a controller, making behavior difficult to reason about and test.
- **One giant subsystem for all mechanisms:** unrelated state, safety, and requirements become tangled.
- **ShooterSubsystem wrapping Flywheel and Feeder:** two independent owners are hidden behind an unnecessary third owner.
- **Command owning persistent hardware state:** command lifetime is temporary; persistent mechanism state belongs to the subsystem.
- **Multiple subsystems owning the same hardware:** two owners can issue conflicting requests and unsafe stops.
- **Duplicating hardware state in multiple layers:** copies can disagree and no longer have a clear source of truth.
- **Implementation before ownership is clear:** code decisions lock in a confused boundary before responsibilities are reviewed.
- **Bypassing IO because direct access looks easier:** short-term convenience creates vendor coupling and breaks replacement and simulation boundaries.

## 17. One-Lesson-One-Concept Boundary

M00_L01 does not teach:

- hardware evidence audit;
- hardware selection;
- Intake implementation;
- Intake command ownership;
- Feeder implementation;
- Feeder command ownership;
- Flywheel implementation;
- flywheel closed-loop velocity;
- ready-at-speed;
- Elevator foundation;
- Elevator closed-loop position;
- homing;
- travel-limit safety;
- shooting coordination;
- Intake-to-Feeder coordination; or
- mechanism autonomous-event integration.

Those concepts belong to M00_L02 through M00_L16. Teaching them early would mix multiple responsibilities into one lesson and remove the evidence gates that protect later work.

## 18. Evidence Classification

```text
THEORY VERIFIED

Simulation:
NOT APPLICABLE

Driver Station / Glass:
NOT APPLICABLE

Real Hardware:
NOT APPLICABLE
```

M00_L01 introduces architecture understanding and documentation only. It introduces no executable mechanism behavior. Therefore, there is nothing new to simulate, display as new runtime telemetry, or commission on physical hardware.

`REAL HARDWARE DEFERRED` is not used for M00_L01 because this lesson makes no hardware claim.

## 19. Student Architecture Exercise

Imagine a fictional future mechanism called the **Panel Handler**. Its detailed behavior and hardware are intentionally unknown. Create an architecture ownership map—not code—and identify:

1. the owner subsystem;
2. the vendor-neutral IO interface;
3. the concrete hardware IO adapter;
4. possible conceptual IOInputs measurements;
5. the immutable Observation;
6. the safe-stop owner;
7. the command owner or coordinator;
8. the telemetry consumer; and
9. RobotContainer's wiring responsibility.

Do not choose motors, controllers, sensors, CAN IDs, gearing, or control algorithms.

### Self-check guidance

- The Panel Handler has one subsystem owner.
- That subsystem depends on a vendor-neutral IO interface.
- A concrete adapter contains any future vendor API.
- **Conceptual examples only:** connection state or a measured motion value could travel through IOInputs, but final fields are not defined here.
- The subsystem interprets inputs and exposes an immutable, vendor-neutral Observation.
- The subsystem owns safe-stop responsibility.
- A command requests or coordinates behavior and declares the subsystem requirement; it does not own persistent mechanism state.
- Telemetry consumes the Observation read-only.
- RobotContainer creates and connects these objects without implementing Panel Handler behavior.

## 20. Knowledge Check

### Questions

1. What is a Composition Root?
2. Who owns persistent mechanism state?
3. Why do we use an IO interface?
4. Where may vendor APIs appear?
5. What is IOInputs for?
6. Why is an Observation immutable?
7. Can telemetry command hardware?
8. Who owns the mechanism's safe-stop responsibility?
9. Why are Intake, Feeder, Flywheel, and Elevator separate owners?
10. Why does shooting use `FlywheelSubsystem + FeederSubsystem + ShootCommand` instead of `ShooterSubsystem`?

### Answers

1. It is the place where implementations are created, selected, and connected; in this project, RobotContainer performs that role.
2. The subsystem that owns the mechanism capability.
3. It separates mechanism behavior from device-specific hardware access and permits implementation replacement.
4. Only inside concrete IO adapters.
5. It carries one coherent cycle of measured hardware facts from IO to the subsystem.
6. So readers receive stable, safe, vendor-neutral meaning that they cannot mutate.
7. No. Telemetry observes and publishes only.
8. The mechanism subsystem, which routes the stop through its IO boundary.
9. They are independent capabilities with different state, behavior, safety, and scheduler requirements.
10. Flywheel and Feeder remain the two hardware owners; the command coordinates them temporarily without inventing a third owner.

## 21. Key Takeaways

> **New mechanism, same architecture discipline.**

- Establish ownership first.
- Isolate vendor APIs inside concrete IO adapters.
- Use IOInputs for measured transport facts.
- Make Observations immutable and vendor-neutral.
- Keep telemetry read-only.
- Let commands request or coordinate behavior.
- Let the subsystem own behavior and persistent state.
- Let RobotContainer compose the object graph.
- Keep safe stop within subsystem responsibility.
- Add later behavior one governed concept at a time.
