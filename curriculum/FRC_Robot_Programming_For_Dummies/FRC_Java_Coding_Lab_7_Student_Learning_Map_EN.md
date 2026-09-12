# FRC Java Coding Lab 7.0
## Student Learning Map / Course Progression Guide

This guide is a student-facing map of the repository's current learning progression. It explains the authoritative S00 -> A00 -> A01 -> V00 chain and records D00 -> D01 as a separate parallel track. It does not authorize source changes, lesson reordering, or new roadmap scope.

### 1. How to Use This Guide

This is a cumulative course. A lesson is not an unrelated mini-project. Each new lesson is an inherited copy of the previous completed lesson, followed by one tightly bounded learning increment.

```text
previous lesson: COMPLETE / FROZEN / READ-ONLY
        |
        v
copied inherited baseline
        |
        v
one new architectural concept
        |
        v
focused verification + regression
        |
        v
new lesson: COMPLETE / FROZEN / READ-ONLY
```

Use the guide in this order:

1. Learn the ownership vocabulary before reading a large class.
2. Read the predecessor and identify what is already frozen.
3. Name the one new concept before looking for implementation details.
4. Predict what should happen.
5. Run the applicable test or Simulation, then explain the observation.
6. Treat real-robot evidence as a separate proof boundary.
7. Do not skip ahead because a later lesson looks more exciting. Skipping breaks the reasoning chain and makes it harder to tell whether a behavior came from the inherited baseline or the new concept.

Frozen lessons matter because they are the known starting point for the next experiment. `COMPLETE / FROZEN / READ-ONLY` does not mean that the code is perfect for every future robot; it means that this curriculum snapshot has passed its defined gates and must not be casually rewritten.

One lesson introduces **ONE ARCHITECTURAL CONCEPT**. A lesson may contain tests, diagrams, safety checks, and supporting classes, but those are evidence and support for the one concept. Dense repository lessons such as S00_L24 and A01_L08 are kept as one source snapshot even when a teacher divides their mental model into several classroom sessions.

Evidence has levels. `THEORY VERIFIED` means the architecture, source, tests, or governance support the explanation. `SIMULATION VERIFIED` means the repository records applicable Simulation or deterministic runtime evidence. `REAL HARDWARE VERIFIED` means the User supplied physical evidence for a stated scope. `REAL HARDWARE DEFERRED` means the claim needs a physical boundary or is intentionally outside that lesson. `NOT APPLICABLE` means the lesson has no such runtime surface. A build is useful evidence, but a build alone does not prove wiring, calibration, traction, latency, or safe physical motion.

### 2. Course Architecture Philosophy

The robot program is a set of owners with clear questions:

| Part | Beginner question | Responsibility |
| --- | --- | --- |
| `Robot.java` | When is the framework calling us? | WPILib runtime lifecycle, mode hooks, scheduler boundary, and runtime fault boundary. |
| `RobotContainer.java` | Which objects make up this robot? | Composition Root: create, select, inject, configure, and bind. |
| Commands | What behavior should happen now? | Coordinate actions and declare subsystem requirements. |
| Subsystems | What robot capability owns this behavior and state? | Own mechanism/domain behavior, safety, and the high-level API. |
| IO | How do we talk to this device? | Define a vendor-neutral hardware contract and move data through Inputs snapshots. |
| Observations | What is known about the robot? | Immutable, vendor-neutral read models and pure evaluators. |
| Telemetry | How can people inspect the robot? | Publish read-only observations to NT4, Glass, or logs. |
| Autonomous | How can behavior run without a driver? | Use commands, pose/readiness contracts, scheduler ownership, and safe mode boundaries. |
| Vision | What measurement does the camera provide? | Supply qualified information to localization; never directly command Swerve. |

The two principal flows are separate:

```text
CONTROL
Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware
```

```text
OBSERVATION
hardware -> IOInputs -> subsystem / estimator -> immutable Observation
          -> telemetry -> NT4 / Glass / log
```

The broader student picture has two separate paths:

```text
Driver / autonomous intent
  -> Command / coordinator
  -> Subsystem
  -> IO
  -> hardware

hardware
  -> IOInputs
  -> subsystem / estimator processing
  -> immutable Observation
  -> read-only telemetry
```

These paths meet at subsystem ownership but must not be treated as one linear data flow. The command path asks for action; the observation path reports facts; telemetry is read-only. This is a learning summary, not permission to connect every box directly to every other box.

`RobotContainer` is the **Composition Root**, not a data-flow stage. It assembles the graph. `Robot.java` is the **WPILib runtime lifecycle and scheduling boundary**, not the home of drivetrain math or hardware configuration.

### 3. Lesson Inheritance Model

Every authoritative main-course lesson follows this controlled evolution:

```text
previous COMPLETE/FROZEN lesson
        -> copy
        -> rename to the approved lesson identity
        -> clean generated build/.gradle artifacts
        -> baseline build
        -> inheritance and architecture audit
        -> Design Lock
        -> one-concept implementation
        -> focused tests
        -> inherited regression
        -> clean build
        -> Simulation where applicable
        -> real hardware where required and authorized
        -> documentation and transition guide
        -> final review
        -> freeze
```

Think of the previous lesson as a carefully labeled laboratory sample. The student does not throw it away and start a new experiment in an empty room. The student copies the sample, changes one controlled variable, and records what the change means. This makes failures easier to localize and prevents a new library or convenience class from silently redesigning the robot.

The repository's frozen workflow also gives a practical stop rule: if the architecture review fails, do not implement; if the build fails, do not advance; if verification fails, record the cause and repair one change at a time. The User owns build, Simulation, Driver Station / Glass, real-robot verification, and Git publication. Codex documentation must not upgrade missing evidence.

### 4. High-Level Course Map

The authoritative primary progression is:

```text
S00 Swerve Foundation
  -> A00 Autonomous Command Foundation
  -> A01 Autonomous Navigation and Path Following
  -> V00 AprilTag Vision Observation and Pose Fusion
```

| Module | Student question | Lessons | What students know before | What students know after |
| --- | --- | ---: | --- | --- |
| S00 - Swerve Foundation | How do we build a safe, testable Swerve drivetrain? | 24 | Basic WPILib project ownership and Java structure. | How a Swerve request and measurement move through IO, subsystems, observations, telemetry, kinematics, actuation, driver frames, odometry, and readiness. |
| A00 - Autonomous Command Foundation | How can autonomous commands run safely? | 4 | S00_L24 pose, measured-speed, stop, and readiness contracts. | Scheduler ownership, bounded robot-relative motion, terminal holding, and the Autonomous Enabled safety gate. |
| A01 - Autonomous Navigation and Path Following | How does the robot navigate to positions and follow paths? | 9 | A00 safety and S00 localization contracts. | Starting pose, pose feedback, trajectories, alliance transforms, holonomic following, PathPlanner, AutoBuilder, safe routine selection, and event markers. |
| V00 - AprilTag Vision Observation and Pose Fusion | How can vision improve localization without becoming a hidden control path? | 9 | A01 field-frame/autonomous boundaries and Swerve estimator ownership. | Frames, AprilTag references, VisionIO, immutable observations, deterministic simulation, pose candidates, quality, timing, one real adapter, and the guarded future/current fusion boundary. |

S00_L01 begins from a new WPILib Command Robot foundation. A00_L01 inherits frozen S00_L24. A01_L01 inherits frozen A00_L04. V00_L01 inherits the final frozen A01_L09. Those predecessor facts are why the arrows are meaningful.

## Student Checkpoint

- Can you explain why the main chain is S00 -> A00 -> A01 -> V00?
- Can you name the inherited baseline and the one new concept for a lesson you have studied?
- Can you distinguish a theory explanation, a deterministic test, a Simulation result, and real-hardware proof?

Self-check: draw the two arrows `Driver -> ... -> hardware` and `hardware -> ... -> telemetry`. Put a red mark wherever a class would be violating the Frozen Backbone if it crossed the boundary directly.

### 5. Module S00 - Swerve Foundation

S00 grows one Swerve architecture from a project skeleton into a measured, field-relative, pose-aware drivetrain. It deliberately delays autonomous and vision. The 24 lesson snapshots are grouped pedagogically below, but the repository lesson order remains one lesson at a time.

**Module goal:** build a Swerve drivetrain whose ownership, units, module identities, transformations, safety, and evidence are explainable before autonomous navigation or vision fusion is attempted.

**Before S00:** a WPILib Command Robot project and Java object/interface vocabulary.

**After S00:** a User-verified Swerve foundation with four-module actuation, coherent driver input, robot-relative and field-relative teleop, odometry, an estimator, reset/readiness contracts, and read-only observation/telemetry boundaries.

**Evidence profile:** theory is verified throughout; Simulation is verified where the lesson status records it; physical commissioning and driving are verified only for the stated lessons and scopes. Final tuning, competition accuracy, and unresolved hardware maintenance are not silently promoted to curriculum truth.

#### S00_L01 - Swerve Architecture Foundation

**Previous Knowledge:** A new WPILib Command Robot project.

**One New Concept:** A governed Swerve package and dependency skeleton.

**Why This Lesson Exists:** A four-module drivetrain has many responsibilities. Students need the map before they add devices, otherwise `RobotContainer`, commands, or subsystems become accidental owners of everything.

**What Changes:** The project establishes the Swerve-oriented package structure, frozen control/observation vocabulary, and composition-root expectations without placeholder mechanism code.

**What Does NOT Change:** No hardware, vendor adapter, kinematics, motor output, or mechanism behavior is added.

**Architecture Flow:** `RobotContainer -> future SwerveSubsystem -> future IO`; the observation boundary is reserved, not yet populated.

**Student Outcome:** Explain where a future Swerve class belongs and reject a direct button-to-motor design.

**Verification Meaning:** Theory and architecture are `THEORY VERIFIED`; build and user Simulation evidence are `SIMULATION VERIFIED`; Driver Station / Glass and real robot are `NOT APPLICABLE` to this architecture-only scope.

**Connection to Next Lesson:** Once the map exists, the next question is which physical facts are known and which are still assumptions.

#### S00_L02 - Swerve Hardware Audit

**Previous Knowledge:** S00_L01 architecture-only baseline.

**One New Concept:** Evidence-based hardware inventory before implementation.

**Why This Lesson Exists:** Guessed CAN IDs, ratios, offsets, or axis signs can become false authority when copied into code. An audit separates verified facts from unknowns.

**What Changes:** Hardware documentation and an explicit list of unresolved device identity, gearing, inversion, IMU, and calibration questions are recorded.

**What Does NOT Change:** Java source, vendor dependencies, IO implementations, and robot behavior remain unchanged.

**Architecture Flow:** `hardware evidence -> audit record -> later Constants/IO decisions`.

**Student Outcome:** Label a hardware statement as verified, provisional, or unresolved without filling the gap from memory.

**Verification Meaning:** Audit theory is `THEORY VERIFIED`; Simulation, Driver Station / Glass, and robot runtime are `NOT APPLICABLE`; physical facts not established by the audit remain `REAL HARDWARE DEFERRED`.

**Connection to Next Lesson:** After the device map is explicit, the vendor can be contained behind the IO contract.

#### S00_L03 - CTRE IO Foundation

**Previous Knowledge:** Audited device identities and unresolved calibration boundaries from S00_L02.

**One New Concept:** Vendor-neutral Swerve module/gyro IO contracts with CTRE real adapters.

**Why This Lesson Exists:** The robot must use TalonFX, CANcoder, and Pigeon2 devices without forcing Phoenix types into every upper layer.

**What Changes:** `SwerveModuleIO` and gyro IO contracts, their Inputs snapshots, and CTRE implementations provide the hardware boundary.

**What Does NOT Change:** Swerve subsystem ownership, observations, telemetry, kinematics, and driver behavior are still deferred.

**Architecture Flow:** `CTRE devices -> CTRE IO adapter -> vendor-neutral IOInputs`; upper layers see the contract.

**Student Outcome:** Point to the only layer that should know a Phoenix API and explain why a subsystem should not store a `TalonFX` object.

**Verification Meaning:** Theory, build, Simulation, and scoped real-robot IO evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`; Driver Station / Glass is `NOT APPLICABLE` to this IO-only lesson.

**Connection to Next Lesson:** The contracts now need one robot-domain owner to refresh all module and gyro snapshots coherently.

#### S00_L04 - Swerve Subsystem Foundation

**Previous Knowledge:** S00_L03 module and gyro IO contracts/adapters.

**One New Concept:** One `SwerveSubsystem` owner for IO snapshots and safe stop delegation.

**Why This Lesson Exists:** Four independent adapters should not coordinate robot behavior. One subsystem must own the mechanism boundary.

**What Changes:** The subsystem receives injected IOs, refreshes Inputs in `periodic()`, keeps fixed module identity, and delegates `stop()` safely.

**What Does NOT Change:** No public Observation, telemetry, kinematics, actuation pipeline, or composed runtime is added.

**Architecture Flow:** `module/gyro IO -> SwerveSubsystem.periodic() -> coherent internal snapshots`.

**Student Outcome:** Trace one refresh and one stop fan-out and explain why the subsystem depends on interfaces only.

**Verification Meaning:** Architecture and build are `THEORY VERIFIED`; Simulation, Driver Station / Glass, and real robot are `NOT APPLICABLE` because the subsystem is not composed into a runtime path.

**Connection to Next Lesson:** Once the subsystem owns mutable transport snapshots, it can turn them into stable meaning.

#### S00_L05 - Observation Foundation

**Previous Knowledge:** Subsystem-owned module and gyro Inputs snapshots.

**One New Concept:** An immutable, vendor-neutral `SwerveObservation` produced from the snapshot boundary.

**Why This Lesson Exists:** Diagnostics and later consumers need robot-domain facts without retaining mutable vendor transport data.

**What Changes:** The subsystem copies the relevant values into one coherent immutable observation with explicit units and validity meaning.

**What Does NOT Change:** Telemetry publishing, kinematics, driver intent, and actuation remain separate later concepts.

**Architecture Flow:** `IOInputs -> SwerveSubsystem -> immutable SwerveObservation`.

**Student Outcome:** Explain why retaining a mutable `IOInputs` reference is not the same as publishing an Observation.

**Verification Meaning:** Model and architecture are `THEORY VERIFIED`; runtime Simulation, Driver Station / Glass, and real robot are `NOT APPLICABLE` in this unwired lesson.

**Connection to Next Lesson:** A stable Observation is now safe for a read-only telemetry facade.

#### S00_L06 - Telemetry Foundation

**Previous Knowledge:** Immutable Swerve observations.

**One New Concept:** A read-only telemetry facade over Observation data.

**Why This Lesson Exists:** Operators need visibility, but a dashboard must not become a second control path.

**What Changes:** Typed publishers and a public facade define how Swerve facts can be exposed without giving telemetry hardware or command authority.

**What Does NOT Change:** Runtime ordering, IO access, control behavior, and Swerve actuation remain untouched.

**Architecture Flow:** `SwerveObservation -> SwerveTelemetryFacade -> typed NT4 topics`.

**Student Outcome:** Identify a telemetry-only dependency and reject telemetry code that calls a motor or schedules a command.

**Verification Meaning:** Architecture and build are `THEORY VERIFIED`; runtime Simulation, Driver Station / Glass, and real robot are `NOT APPLICABLE` because the facade is not yet wired into the runtime.

**Connection to Next Lesson:** The facade becomes useful only when the robot loop calls it after the newest subsystem snapshot exists.

#### S00_L07 - Runtime Telemetry Integration

**Previous Knowledge:** S00_L06 telemetry facade and Observation boundary.

**One New Concept:** Scheduler/subsystem update before read-only telemetry publication.

**Why This Lesson Exists:** Publishing before the subsystem refreshes can show the previous cycle's state and confuse debugging.

**What Changes:** `RobotTelemetry` is composed and invoked after scheduler/subsystem work in the periodic ordering.

**What Does NOT Change:** Telemetry still cannot read vendor devices, calculate control, or command Swerve.

**Architecture Flow:** `CommandScheduler.run() -> subsystem periodic -> Observation -> RobotTelemetry -> NT4/Glass`.

**Student Outcome:** Predict the difference between publish-before-update and publish-after-update.

**Verification Meaning:** Runtime Simulation and Driver Station / Glass telemetry are `SIMULATION VERIFIED`; no new real-hardware behavior is claimed, so real robot is `NOT APPLICABLE`.

**Connection to Next Lesson:** With state visible at the right time, the course can give one wheel state a precise meaning.

#### S00_L08 - Swerve Module State Foundation

**Previous Knowledge:** Runtime observations and fixed module identities.

**One New Concept:** Interpreting one measured wheel as a `SwerveModuleState`.

**Why This Lesson Exists:** Swerve calculations are unsafe when speed and steering angle have no explicit ownership or units.

**What Changes:** Subsystem state is expressed as measured speed in metres per second plus measured angle in `Rotation2d`, for a known module identity.

**What Does NOT Change:** Chassis conversion, optimization, output, and motor actuation are deferred.

**Architecture Flow:** `module IOInputs -> measured SwerveModuleState(velocity, angle)`.

**Student Outcome:** Explain why a `SwerveModuleState` can be measured or desired and why context matters.

**Verification Meaning:** Theory is `THEORY VERIFIED`; the deterministic contract is `SIMULATION VERIFIED` where the status records it; runtime Driver Station / Glass and real robot are `NOT APPLICABLE` to this non-actuating consumer.

**Connection to Next Lesson:** A wheel state describes one module; the next lesson describes the desired motion of the whole chassis.

#### S00_L09 - ChassisSpeeds Foundation

**Previous Knowledge:** Measured module-state meaning.

**One New Concept:** A defensive robot-relative `ChassisSpeeds` request boundary.

**Why This Lesson Exists:** Commands need a compact description of desired chassis motion without naming individual wheels.

**What Changes:** The subsystem accepts/copies `vx` (+X forward, m/s), `vy` (+Y left, m/s), and `omega` (+ counterclockwise, rad/s) as intent.

**What Does NOT Change:** No kinematics, module target generation, motor output, or measured-speed claim is added.

**Architecture Flow:** `robot-relative chassis intent(vx, vy, omega) -> subsystem-owned request copy`.

**Student Outcome:** Distinguish a requested velocity from a sensor-measured velocity and explain why a mutable caller object is copied.

**Verification Meaning:** Theory is `THEORY VERIFIED`; deterministic request tests are `SIMULATION VERIFIED` where recorded; runtime and real hardware are `NOT APPLICABLE`.

**Connection to Next Lesson:** The three-number request can now be converted using the four module locations.

#### S00_L10 - SwerveDriveKinematics Foundation

**Previous Knowledge:** Robot-relative chassis intent and measured module-state vocabulary.

**One New Concept:** Pure chassis-to-module conversion with fixed FL/FR/BL/BR ordering.

**Why This Lesson Exists:** The robot must turn one desired translation/rotation into four coordinated wheel targets.

**What Changes:** `SwerveDriveKinematics` uses module locations from the configuration authority to calculate desired module speeds and angles.

**What Does NOT Change:** Optimization, desaturation, IO output, and physical motion are deferred.

**Architecture Flow:** `ChassisSpeeds -> module locations -> four desired SwerveModuleStates`.

**Student Outcome:** Predict the pattern for forward, left, and counterclockwise requests and preserve module order.

**Verification Meaning:** Pure calculation and focused tests are `THEORY VERIFIED` and `SIMULATION VERIFIED`; Driver Station / Glass and real robot are `NOT APPLICABLE`.

**Connection to Next Lesson:** Kinematics may ask a wheel to steer a long way; optimization makes that request shorter.

#### S00_L11 - SwerveModuleState Optimization Foundation

**Previous Knowledge:** Four desired module states from kinematics.

**One New Concept:** Short-path optimization against the measured module angle.

**Why This Lesson Exists:** Rotating a module nearly 180 degrees is slower and less smooth than reversing its wheel-speed sign and steering a short distance.

**What Changes:** A pure optimizer compares desired and measured angle, possibly reverses desired speed, and avoids mutating caller data.

**What Does NOT Change:** Desaturation, cosine compensation, output dispatch, and closed-loop hardware control are deferred.

**Architecture Flow:** `desired state + measured angle -> optimized desired state`.

**Student Outcome:** Explain why a negative optimized wheel speed does not automatically mean the robot should travel backward.

**Verification Meaning:** Theory and deterministic tests are `THEORY VERIFIED` and `SIMULATION VERIFIED`; runtime and real hardware are `NOT APPLICABLE`.

**Connection to Next Lesson:** The optimizer is one stage; the next lesson defines the complete order of validation, conversion, optimization, and speed limiting.

#### S00_L12 - Swerve Output Pipeline Foundation

**Previous Knowledge:** Kinematics and per-module optimization.

**One New Concept:** A stateless validate -> calculate -> optimize -> desaturate pipeline.

**Why This Lesson Exists:** A safe output is easier to audit when each transformation has one clear job and the order is fixed.

**What Changes:** The pipeline copies/validates intent, calculates states, optimizes them against measured angles, and scales all wheels together when needed.

**What Does NOT Change:** The subsystem has not yet integrated the pipeline into output ownership or commanded hardware.

**Architecture Flow:** `ChassisSpeeds -> validate -> kinematics -> optimize -> desaturate -> final targets`.

**Student Outcome:** Calculate a common desaturation ratio and explain why independent wheel clamping changes the requested motion shape.

**Verification Meaning:** Theory and focused deterministic tests are `THEORY VERIFIED` and `SIMULATION VERIFIED`; runtime, Driver Station / Glass, and real robot are `NOT APPLICABLE`.

**Connection to Next Lesson:** A pure pipeline now needs the subsystem to own the final four target states.

#### S00_L13 - Swerve Module Control Foundation

**Previous Knowledge:** The complete pure output pipeline.

**One New Concept:** Subsystem ownership of four final desired module states.

**Why This Lesson Exists:** Someone must join current measured angles with the chassis request and own the result before any IO adapter can act.

**What Changes:** `SwerveSubsystem` integrates the pipeline and stores ordered FL/FR/BL/BR final targets.

**What Does NOT Change:** Motor actuation, PID, commissioning, and hardware output are deliberately deferred.

**Architecture Flow:** `IO measurements + chassis request -> subsystem pipeline -> final module targets`.

**Student Outcome:** Trace a cycle and say which values are measurements and which are targets.

**Verification Meaning:** Architecture, build, and focused tests are `THEORY VERIFIED`; Simulation, Driver Station / Glass, and real robot are `NOT APPLICABLE` because no runtime hardware path was added.

**Connection to Next Lesson:** Before commanding those targets, the robot must prove device presence and health while Disabled.

#### S00_L14 - Hardware Configuration Commissioning Foundation

**Previous Knowledge:** S00_L13 output targets that are not yet dispatched.

**One New Concept:** Disabled-only read-only hardware commissioning evidence.

**Why This Lesson Exists:** Connectivity and signal health should be observed before configuration writes or motion are attempted.

**What Changes:** A 14-device CTRE commissioning matrix records connection/health evidence with no unintended actuation.

**What Does NOT Change:** No new motor command, offset guess, inversion guess, or final tuning is introduced.

**Architecture Flow:** `Disabled -> read-only IO signals -> commissioning evidence -> permission for next bounded step`.

**Student Outcome:** Explain why online does not mean calibrated and why this gate is different from a motion test.

**Verification Meaning:** Theory is `THEORY VERIFIED`; the commissioning evidence is `REAL HARDWARE VERIFIED` for the recorded 14-device Disabled scope; Simulation is `NOT APPLICABLE`; remaining calibration claims are `REAL HARDWARE DEFERRED`.

**Connection to Next Lesson:** After presence is known, the first physical movement can be limited to one module, one mode, one action, and one watchdog.

#### S00_L15 - Single-Module Open-Loop Commissioning

**Previous Knowledge:** Verified device presence and centralized stop.

**One New Concept:** Test-mode, low-duty, watchdog-bounded single-module open-loop motion.

**Why This Lesson Exists:** The first movement should have the smallest possible blast radius and an obvious stop path.

**What Changes:** Four explicit Test-mode dashboard commands operate only the front-left drive/steer at +/-0.05 duty with a 0.25 second watchdog.

**What Does NOT Change:** Other modules, closed-loop control, chassis motion, and production teleop remain untouched.

**Architecture Flow:** `explicit Test action -> command requirement -> SwerveSubsystem -> one FL module -> automatic stop`.

**Student Outcome:** Predict cancellation, timeout, mode exit, and competing-request behavior.

**Verification Meaning:** Theory and scoped real commissioning are `THEORY VERIFIED` and `REAL HARDWARE VERIFIED`; Driver Station / Glass is `REAL HARDWARE VERIFIED`; Simulation is `NOT TESTED`; other module behavior is `REAL HARDWARE DEFERRED`.

**Connection to Next Lesson:** A bounded motion test is useful, but production output still needs an explicit configuration apply/readback contract.

#### S00_L16 - Module Hardware Configuration Contract

**Previous Knowledge:** Bounded FL open-loop commissioning.

**One New Concept:** Apply/read back module configuration and fail closed when unhealthy.

**Why This Lesson Exists:** A desired configuration is not proof that the device received it, especially with wrapped and quantized sensor values.

**What Changes:** TalonFX/CANcoder settings, calibrated offsets, health status, readback comparison, and nonzero-output rejection are formalized in the IO boundary.

**What Does NOT Change:** Four-module production drive, final tuning, and broad autonomous behavior remain out of scope.

**Architecture Flow:** `Constants desired configuration -> real IO apply -> readback -> health gate -> bounded output`.

**Student Outcome:** Explain desired configuration versus measured readback and why wrapped offset comparison needs a tolerance.

**Verification Meaning:** Theory, Simulation, Driver Station / Glass, and apply/readback real-robot evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED` for the recorded scope.

**Connection to Next Lesson:** With one module healthy and configured, it can accept physical-unit velocity and angle targets.

#### S00_L17 - Single-Module Closed-Loop Control

**Previous Knowledge:** Healthy configured FL module and open-loop safety tools.

**One New Concept:** A single module's velocity and steering-position closed-loop contract.

**Why This Lesson Exists:** A physical-unit setpoint is a request that must be compared with sensor feedback; it is not merely a voltage percentage.

**What Changes:** FL drive velocity in metres per second and steer position in `Rotation2d` are sent through the vendor adapter's closed-loop controls and bounded characterization commands.

**What Does NOT Change:** Four-module coordination and production-final gain tuning remain deferred.

**Architecture Flow:** `velocity/angle target -> IO closed-loop request -> measured feedback -> module output`.

**Student Outcome:** Distinguish a target from feedback and explain why one successful movement does not prove final PID/feedforward tuning.

**Verification Meaning:** Theory, Simulation, Driver Station / Glass, and scoped real-robot closed-loop evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`; competition-final tuning is `REAL HARDWARE DEFERRED`.

**Connection to Next Lesson:** The same contract must now be dispatched to four fixed modules as one drivetrain.

#### S00_L18 - Four-Module State Actuation

**Previous Knowledge:** Single-module closed-loop control and the output pipeline.

**One New Concept:** Fixed-order four-module state actuation with bounded ownership.

**Why This Lesson Exists:** Four modules are separate devices but one robot capability. Dispatch must preserve order, requirements, and stop behavior.

**What Changes:** Optimized/desaturated states reach all FL/FR/BL/BR modules through fixed Test-mode commands and armed production intent.

**What Does NOT Change:** Driver input, field-relative conversion, and normal teleop are deferred.

**Architecture Flow:** `one chassis request -> four ordered module targets -> four IO outputs -> centralized stop`.

**Student Outcome:** Explain how one Swerve request becomes four outputs without letting each module invent its own policy.

**Verification Meaning:** Theory, Simulation, Driver Station / Glass, and real-robot direction/stop evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED` for the recorded bounded cases.

**Connection to Next Lesson:** Once the robot can actuate four modules, it needs one coherent interpretation of the driver's controller state.

#### S00_L19 - Driver Input Processing

**Previous Knowledge:** Four-module actuation and read-only telemetry patterns.

**One New Concept:** One coherent external-operator sample becomes a semantic immutable `DriverInputObservation`.

**Why This Lesson Exists:** Telemetry and future drive control must not observe two different controller samples in one cycle. Human input is the repository's narrow, explicitly approved controls-produced Observation exception.

**What Changes:** Xbox acquisition, semantic axis mapping, deterministic processing, and read-only driver-input telemetry are added without actuation.

**What Does NOT Change:** No `ChassisSpeeds`, no Swerve request, no module generation, and no drivetrain output are added in L19.

**Architecture Flow:** `Xbox sample -> controls processing -> DriverInputObservation -> read-only telemetry`.

**Student Outcome:** Explain why L19 may produce a driver-input Observation but may not produce a mechanism Observation or command a motor.

**Verification Meaning:** Theory, Simulation, Glass/driver-input telemetry, and Disabled real-roboRIO evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`; drivetrain actuation is `NOT APPLICABLE`.

**Connection to Next Lesson:** L20 must move sampling ownership into the control cycle before the observation can drive Swerve.

#### S00_L20 - Robot-Relative Teleop Integration

**Previous Knowledge:** L19 semantic driver input and four-module actuation.

**One New Concept:** Coherent driver intent becomes robot-relative `ChassisSpeeds` teleop control.

**Why This Lesson Exists:** The robot must connect forward/strafe/rotate intent to the existing Swerve pipeline while preserving one authoritative controller sample.

**What Changes:** The default teleop command uses the authoritative driver-input value, creates robot-relative chassis requests, and sends them through the existing output pipeline.

**What Does NOT Change:** Field-relative conversion, odometry, pose, autonomous, and vision remain later concepts.

**Architecture Flow:** `one controller sample -> controls -> teleop command -> robot-relative ChassisSpeeds -> SwerveSubsystem`.

**Student Outcome:** Drive or simulate forward, left, right, and rotation relative to the robot and explain the frame.

**Verification Meaning:** Theory, Simulation, Glass, and bounded real-robot teleop evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED` for the recorded scope.

**Connection to Next Lesson:** After teleop works, the next lesson asks whether the existing architecture behaves correctly under real floor load.

#### S00_L21 - First Floor Drive Validation

**Previous Knowledge:** Robot-relative teleop with the inherited safety and stop contracts.

**One New Concept:** Real-floor validation as an evidence boundary, not a new architecture.

**Why This Lesson Exists:** Passing calculations and stand tests do not prove the complete robot behaves correctly under load.

**What Changes:** A controlled floor-validation procedure records motion, stopping, communication, and confidence for the existing robot-relative drivetrain.

**What Does NOT Change:** No new control feature, tuning redesign, or architecture migration is introduced.

**Architecture Flow:** `existing teleop architecture -> bounded floor procedure -> evidence record`.

**Student Outcome:** Separate “the code path ran” from “the robot behaved correctly on the floor.”

**Verification Meaning:** Theory and recorded real-floor evidence are `THEORY VERIFIED` and `REAL HARDWARE VERIFIED`; separate Driver Station / Glass closure evidence is `NOT TESTED`; final tuning remains `REAL HARDWARE DEFERRED`.

**Connection to Next Lesson:** A robot-relative driver can drive, but the same stick direction should remain meaningful after the robot rotates.

#### S00_L22 - Field-Relative Drive

**Previous Knowledge:** Robot-relative teleop and a known physical heading procedure.

**One New Concept:** Capture a software heading reference and convert field intent exactly once.

**Why This Lesson Exists:** “Push forward” should mean the chosen field direction rather than whichever way the robot nose currently points.

**What Changes:** A Disabled heading-capture workflow and field-relative conversion are added while the existing robot-relative output pipeline remains the actuation path.

**What Does NOT Change:** Odometry, pose estimation, autonomous, vision, and a second alliance transform are deferred.

**Architecture Flow:** `field-relative driver intent + heading reference -> robot-relative ChassisSpeeds -> existing Swerve pipeline`.

**Student Outcome:** Explain the difference between a captured software reference and resetting a physical gyro.

**Verification Meaning:** Theory, Simulation, Driver Station / Glass, and scoped field-relative real-robot evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`; intermittent BL drift and final tuning remain `REAL HARDWARE DEFERRED`.

**Connection to Next Lesson:** Field-relative control needs a record of where the robot believes it is, so the next lesson introduces odometry.

#### S00_L23 - Odometry and Pose Visualization

**Previous Knowledge:** Field-relative Swerve, measured module positions, and gyro heading.

**One New Concept:** Subsystem-owned Swerve odometry exposed as an immutable pose observation and read-only Field2d/NT4 telemetry.

**Why This Lesson Exists:** Autonomous and students need a continuously updated estimate of field pose, not just wheel requests.

**What Changes:** Module positions and heading update `SwerveDriveOdometry`; pose is copied into observation/telemetry, and Field2d visualizes the odometry source.

**What Does NOT Change:** Field-relative control remains the control path; vision, estimator fusion, and autonomous commands are deferred.

**Architecture Flow:** `module/gyro measurements -> odometry -> SwerveObservation -> NT4/Glass/Field2d`.

**Student Outcome:** Explain why Field2d is a visualization of an estimate, not proof of absolute accuracy.

**Verification Meaning:** Theory, deterministic/runtime Simulation, Driver Station / Glass, and real odometry evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED` for the recorded scope.

**Connection to Next Lesson:** Odometry alone is not the complete future localization contract; L24 separates estimated pose, reset, measured speed, and readiness.

#### S00_L24 - Pose Estimation and Autonomous Readiness

**Previous Knowledge:** L23 odometry, pose observation, and visualization.

**One New Concept:** A Swerve-owned localization/readiness boundary that future autonomous code can consume safely.

**Why This Lesson Exists:** Autonomous needs a clear owner for estimated pose, validated measured speeds, finite requests, reset rules, and stop behavior before it can own motion.

**What Changes:** `SwerveDrivePoseEstimator` coexists with L23 odometry; known-field-pose reset, measured robot-relative speeds, nonfinite request rejection, Disabled stale-intent disarm, and readiness contracts are added. These are supporting parts of one readiness boundary.

**What Does NOT Change:** `currentPose()` remains the L23 odometry meaning; no camera, PathPlanner, AutoBuilder, trajectory, alliance transform, or autonomous command is added.

**Architecture Flow:** `IO measurements -> odometry + estimator -> immutable SwerveObservation -> future autonomous consumer`.

**Student Outcome:** Explain why Swerve owns the estimator, why EstimatedPose is not automatically vision truth, and what a future autonomous command must require.

**Verification Meaning:** Theory, Simulation, Driver Station / Glass, and ten-case scoped localization/reset real-robot evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`; vision fusion, competition accuracy, and final tuning are `REAL HARDWARE DEFERRED`.

**Connection to Next Lesson:** The readiness boundary is the frozen predecessor that permits A00 to teach autonomous command lifecycle without rewriting Swerve.

## Student Checkpoint

- Can you draw `ChassisSpeeds -> kinematics -> optimization -> output` and mark which values are targets?
- Can you draw `hardware -> IOInputs -> SwerveSubsystem -> Observation -> telemetry` and mark which values are measurements?
- Why is S00_L19 allowed to produce `DriverInputObservation`, while a mechanism Observation must come from a subsystem or estimator?
- Why does S00_L24 not yet mean that vision has corrected the pose?

Self-checks:

1. Given a robot-relative request `(vx=1.0 m/s, vy=0.0 m/s, omega=0.0 rad/s)`, predict the qualitative module pattern before looking at a test.
2. Explain what a passing S00_L23 Simulation can prove and what it cannot prove about wheel slip or calibration.

### 6. Module A00 - Autonomous Command Foundation

A00 starts only after S00_L24 is frozen. Its teaching decision is intentional: motion is delayed until command lifecycle and scheduler ownership are understandable.

**Module goal:** teach safe autonomous ownership before teaching field navigation.

**Before A00:** Swerve owns stop, pose/readiness, measured speeds, finite requests, and Disabled disarm.

**After A00:** the student can explain a command's lifecycle, why Autonomous must retain Swerve ownership, how one bounded robot-relative request is stopped, and why nonzero motion requires Autonomous Enabled truth.

**Progression:** `lifecycle -> scheduler ownership -> first bounded motion -> Autonomous Enabled safety gate`.

**Evidence profile:** all four lessons are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and supported by user-supplied real-robot evidence for their stated lifecycle/motion scope. Exact distance, final tuning, and broader navigation are not claimed.

#### A00_L01 - Autonomous Command Lifecycle Foundation

**Previous Knowledge:** Frozen S00_L24 readiness and centralized stop.

**One New Concept:** The autonomous command lifecycle and stop skeleton.

**Why This Lesson Exists:** A command must stop on completion, interruption, cancellation, timeout, invalid state, and mode loss even if it never commands nonzero motion.

**What Changes:** A requirement-owning lifecycle boundary is introduced and connected to centralized Swerve stop semantics.

**What Does NOT Change:** This is strictly zero-motion; no autonomous selection, path, or nonzero request is allowed.

**Architecture Flow:** `autonomous command lifecycle -> Swerve requirement -> centralized stop`.

**Student Outcome:** Trace initialize/execute/end and predict the safe result when the command is interrupted.

**Verification Meaning:** Theory, zero-motion Simulation, Driver Station / Glass, and user lifecycle/zero-motion robot evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`.

**Connection to Next Lesson:** A safe command object is not enough; the full Autonomous interval must retain scheduler ownership.

#### A00_L02 - Autonomous Mode Scheduling

**Previous Knowledge:** A00_L01 lifecycle and stop skeleton.

**One New Concept:** Full-interval Autonomous scheduling and requirement ownership.

**Why This Lesson Exists:** A short safety command cannot represent ownership for an entire mode if another command can reacquire the drivetrain.

**What Changes:** Autonomous composition preserves Swerve ownership for the mode while remaining strictly zero-motion.

**What Does NOT Change:** No nonzero drivetrain request, pose targeting, trajectory, PathPlanner, or vision is introduced.

**Architecture Flow:** `autonomousInit -> schedule one owner -> scheduler runs -> Swerve remains required -> safe end`.

**Student Outcome:** Explain the difference between a command that exists and a command that owns the subsystem throughout Autonomous.

**Verification Meaning:** Theory, zero-motion Simulation/Driver Station evidence, and user zero-motion robot evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`.

**Connection to Next Lesson:** Only after ownership is stable does A00 permit the first deliberately bounded nonzero request.

#### A00_L03 - Bounded Robot-Relative Autonomous Motion

**Previous Knowledge:** A00_L02 full-interval scheduler ownership.

**One New Concept:** One finite bounded robot-relative autonomous request followed by a safe zero-motion hold.

**Why This Lesson Exists:** Students need to see a controlled motion request without jumping to pose feedback or path planning.

**What Changes:** A00 permits one finite nonzero robot-relative request and then retains Swerve ownership with repeating zero motion.

**What Does NOT Change:** No field-relative target, trajectory, PathPlanner, AutoBuilder, or exact travel-distance claim is introduced.

**Architecture Flow:** `Autonomous Enabled -> one finite request -> centralized stop -> repeating zero-motion hold`.

**Student Outcome:** Explain why bounded motion, stop-on-exit, and retained ownership are separate from endpoint accuracy.

**Verification Meaning:** Theory, Simulation, and scoped real-robot bounded-motion evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`; separate Glass evidence is `NOT TESTED`.

**Connection to Next Lesson:** The robot can move, so the final A00 question is which mode is allowed to authorize that motion.

#### A00_L04 - Autonomous Motion Safety Gating

**Previous Knowledge:** Bounded motion and the centralized stop/hold contract.

**One New Concept:** Autonomous Enabled mode gating for nonzero autonomous motion.

**Why This Lesson Exists:** A command can be scheduled while the robot is not in the correct enabled mode. Motion permission must be checked at the output boundary.

**What Changes:** Test/global Autonomous motion truth gates nonzero requests and fails closed on mode loss, while terminal ownership and stop semantics remain intact.

**What Does NOT Change:** No field navigation, PathPlanner, vision, new Swerve ownership, or S00 source change is authorized.

**Architecture Flow:** `request -> Autonomous Enabled gate -> nonzero output OR safe stop/hold`.

**Student Outcome:** Predict output for Disabled, Test, Autonomous Disabled, and Autonomous Enabled states.

**Verification Meaning:** Theory, Simulation, and scoped user real-robot autonomous safety-gating evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`; separate Driver Station / Glass closure is `NOT TESTED`.

**Connection to Next Lesson:** A01 can now add field-frame meaning and navigation on top of an explicit safety gate.

## Student Checkpoint

- Why are A00_L01 and A00_L02 strictly zero-motion?
- What does a subsystem requirement protect during Autonomous?
- What is the difference between a one-time stop and terminal `HOLDING` ownership?
- Why must the Autonomous Enabled gate be near the output boundary?

Self-check: draw a timeline for `autonomousInit -> command scheduled -> bounded request -> motion complete -> HOLDING -> teleopInit`. Mark who owns Swerve at every point.

### 7. Module A01 - Autonomous Navigation and Path Following

A01 is the approved successor after A00_L04 and ends at A01_L09. It deliberately teaches the concepts in dependency order:

```text
starting pose / field frame
  -> finite pose target
  -> trajectory generation and sampling
  -> field/alliance transform
  -> holonomic following
  -> PathPlanner path integration
  -> AutoBuilder integration
  -> safe routine selection/composition
  -> NamedCommands and event markers
```

**Module goal:** navigate a field while preserving localization, frame, scheduler, Swerve, and stop ownership.

**Before A01:** A00 safety, S00_L24 estimated pose/readiness, measured speed, and field-relative foundations.

**After A01:** a safe, testable navigation foundation that can select and run known paths and dispatch a controlled non-mechanism learning event. A01 does not become the final competition-tuning authority and does not add vision.

**Evidence profile:** theory and Simulation are verified for the recorded lessons; real-robot evidence is verified only for stated Blue/Red, reset, stop, and event scopes. Exact endpoint accuracy and final PID/feedforward tuning remain deferred.

#### A01_L01 - Autonomous Starting-Pose and Field-Frame Contract

**Previous Knowledge:** A00_L04 safety gate and S00_L24 pose/reset infrastructure.

**One New Concept:** Validated autonomous starting-pose and field-frame initialization.

**Why This Lesson Exists:** Feedback is meaningless if the robot begins with an unknown or frame-incompatible pose.

**What Changes:** A starting-pose availability/validation contract and composition-root wiring are added before target motion.

**What Does NOT Change:** No alliance mirroring, trajectory, PathPlanner, vision, or multi-step routine is added.

**Architecture Flow:** `Disabled reset/start context -> validated field pose -> autonomous readiness`.

**Student Outcome:** Explain why raw gyro yaw is not automatically a complete field pose.

**Verification Meaning:** Theory, Simulation, Driver Station / Glass, and scoped real-robot starting-pose evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`.

**Connection to Next Lesson:** Once the robot knows its starting pose, it can close the loop toward one finite target.

#### A01_L02 - Pose-Targeted Autonomous Motion

**Previous Knowledge:** Validated starting pose and EstimatedPose feedback.

**One New Concept:** Closed-loop movement toward one finite field-relative target pose.

**Why This Lesson Exists:** Students should learn pose error, tolerance, timeout, and terminal stop before a time-parameterized path adds another moving target.

**What Changes:** A bounded pose-target command uses field-relative translation/heading error and EstimatedPose feedback.

**What Does NOT Change:** No trajectory generation, PathPlanner, AutoBuilder, alliance transform, or vision is introduced.

**Architecture Flow:** `target pose - EstimatedPose -> bounded correction -> Swerve request -> stop at tolerance/timeout`.

**Student Outcome:** Predict correction direction and explain why a target pose is a request while EstimatedPose is feedback.

**Verification Meaning:** Theory, end-to-end Simulation, Glass/NT4/Field2d, and scoped real-robot target runs are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`.

**Connection to Next Lesson:** A target is one endpoint; the next lesson asks what the robot should do at each time along a route.

#### A01_L03 - Trajectory Generation and Sampling Fundamentals

**Previous Knowledge:** Pose error and bounded target motion.

**One New Concept:** A finite time-parameterized trajectory and deterministic sampling.

**Why This Lesson Exists:** Students need to see constraints, time, samples, and terminal states before a path library hides them.

**What Changes:** A bounded WPILib-native trajectory factory exposes finite `Trajectory.State` samples without commanding the drivetrain.

**What Does NOT Change:** No PathPlanner, AutoBuilder, runtime follower, or physical actuation is added.

**Architecture Flow:** `start + waypoint + goal + constraints -> trajectory -> state(t)`.

**Student Outcome:** Distinguish path tangent rotation from an independent holonomic robot-heading target.

**Verification Meaning:** Theory and non-actuating deterministic Simulation are `THEORY VERIFIED` and `SIMULATION VERIFIED`; Driver Station / Glass is `NOT APPLICABLE`; physical motion is `REAL HARDWARE DEFERRED` by approved scope.

**Connection to Next Lesson:** A trajectory is authored in a frame; the next lesson makes Blue/Red transform ownership explicit.

#### A01_L04 - Field and Alliance Transform Contract

**Previous Knowledge:** Canonical trajectory data and field-frame vocabulary.

**One New Concept:** One explicit Blue-origin to Red alliance field transform.

**Why This Lesson Exists:** A single plan should be reusable without every library or subsystem flipping it again.

**What Changes:** Pure pose, heading, and velocity transformations are centralized for the approved 2026 field variants.

**What Does NOT Change:** No follower, PathPlanner, vision, or hidden Driver Station read is added to the pure transform utility.

**Architecture Flow:** `canonical Blue plan -> exactly one alliance transform -> execution reference`.

**Student Outcome:** Calculate a Red counterpart and reject both “mirror X only” and double-transform designs.

**Verification Meaning:** Theory and both-alliance non-actuating Simulation are `THEORY VERIFIED` and `SIMULATION VERIFIED`; Driver Station / Glass is `NOT APPLICABLE`; real robot is `REAL HARDWARE DEFERRED` by approved non-actuating scope.

**Connection to Next Lesson:** With frame ownership explicit, the robot can follow a moving target with feedforward plus feedback.

#### A01_L05 - Holonomic Trajectory Following

**Previous Knowledge:** Pose target, trajectory samples, one alliance transform, and safety stop.

**One New Concept:** Sampled trajectory plus pose feedback becomes holonomic chassis control.

**Why This Lesson Exists:** A transparent follower lets students understand timing and correction before a vendor/path library becomes the execution source.

**What Changes:** `HolonomicDriveController` combines plan feedforward, pose feedback, and an independent holonomic heading into robot-relative chassis speeds.

**What Does NOT Change:** PathPlanner, AutoBuilder, vision, mechanism events, and final physical tuning remain deferred.

**Architecture Flow:** `transformed trajectory state + EstimatedPose -> holonomic controller -> robot-relative request -> Swerve`.

**Student Outcome:** Explain the difference between path tangent and robot heading and predict a correction after a pose perturbation.

**Verification Meaning:** Theory, Blue/Red Simulation, Driver Station / Glass, and scoped real-robot trajectory/stop evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`; endpoint precision remains `REAL HARDWARE DEFERRED`.

**Connection to Next Lesson:** The semantics are understandable; the next lesson loads one PathPlanner asset through a narrow adapter.

#### A01_L06 - PathPlanner Path and Runtime Integration

**Previous Knowledge:** L05 follower, safety, start pose, and transform contracts.

**One New Concept:** Loading one validated PathPlanner path through the existing follower boundary.

**Why This Lesson Exists:** Students should learn what PathPlanner supplies without allowing it to become the owner of pose, transforms, requirements, or stop behavior.

**What Changes:** One known `.path` asset and a narrow adapter produce validated trajectory data for the frozen L05 execution contract.

**What Does NOT Change:** AutoBuilder, chooser/routines, events, vision, and final path-model tuning remain deferred.

**Architecture Flow:** `PathPlanner asset -> narrow adapter -> existing follower -> Swerve safety boundary`.

**Student Outcome:** Identify the PathPlanner input as a plan/target and EstimatedPose as feedback/measurement.

**Verification Meaning:** Theory, Simulation/Glass, and scoped Blue/Red real-path evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`; exact 1.000 metre endpoint accuracy is `REAL HARDWARE DEFERRED`.

**Connection to Next Lesson:** A path can execute through an adapter; the next lesson formalizes all callbacks AutoBuilder needs.

#### A01_L07 - AutoBuilder Contract Integration

**Previous Knowledge:** Known PathPlanner path, Swerve APIs, one transform owner, and safety contracts.

**One New Concept:** AutoBuilder configuration as an adapter to existing robot contracts.

**Why This Lesson Exists:** A library may call pose/reset/speed/output callbacks, but it must not become the owner of Swerve or localization.

**What Changes:** AutoBuilder is configured once with pose, reset, measured-speed, output, alliance, RobotConfig, requirement, and fault/stop bridges.

**What Does NOT Change:** Swerve estimator ownership, `RobotContainer` composition role, A01_L04 transform ownership, and vision exclusion remain unchanged.

**Architecture Flow:** `AutoBuilder callbacks -> existing Swerve/pose contracts -> adapter -> scheduler-managed path`.

**Student Outcome:** Explain why a callback adapter is different from handing the drivetrain to a library.

**Verification Meaning:** Theory, Blue/Red Simulation, Driver Station / Glass, and scoped real-robot execution are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`.

**Connection to Next Lesson:** With execution configured, the next lesson must decide how an operator selects and safely composes routines.

#### A01_L08 - Autonomous Routine Selection and Safe Composition

**Previous Knowledge:** AutoBuilder execution and the inherited A00 safety boundary.

**One New Concept:** Snapshot routine selection and safely compose a fresh autonomous command.

**Why This Lesson Exists:** A chooser can change while the robot is running, factories can fail, and a completed path can otherwise release ownership too early.

**What Changes:** The factory samples an immutable routine ID, creates a fresh `SAFE_STOP` or path command, fails closed when readiness/selection fails, retains terminal `HOLDING` ownership, applies the Teleop gate, and preserves a Robot-level scheduler exception boundary.

**What Does NOT Change:** No live chooser replacement, automatic restart, manual child lifecycle delegation, new mechanism architecture, vision, or Swerve redesign is added.

**Architecture Flow:** `chooser snapshot -> fresh factory command -> readiness/fallback -> scheduler-native path -> HOLDING/stop -> Teleop gate`.

**Student Outcome:** Predict what happens if the chooser changes after start, readiness disappears, a path fails, or the scheduler reports a fatal exception.

**Verification Meaning:** Theory, final Simulation, Driver Station / Glass, and user real-robot safety/recovery evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`. The historical bounded terminal steer transient remains a documented, accepted/deferred tuning issue, not a new feature claim.

**Connection to Next Lesson:** Safe routine selection makes it possible to add a non-Swerve side event without disturbing the path owner.

#### A01_L09 - PathPlanner NamedCommands and Event Markers

**Previous Knowledge:** Final L08 safe selection, terminal ownership, exception boundary, and PathPlanner execution.

**One New Concept:** Scheduler-managed NamedCommand/event-marker dispatch that can coexist with the path.

**Why This Lesson Exists:** A path may announce an event while Swerve continues to follow it, but the event must not steal Swerve or redesign D01 mechanisms.

**What Changes:** `LEARNING_EVENT`, a marker at relative position 0.5, `NamedCommands`, `Commands.defer(...)`, fresh event commands, immutable event Observation/telemetry, and `ONE_METER_WITH_EVENT` are added.

**What Does NOT Change:** D01 remains an independent Tank Drive project; no real mechanism integration, new mechanism IO, or A01_L10 is authorized.

**Architecture Flow:** `PathPlanner marker -> NamedCommand -> defer fresh command -> scheduler -> event Observation/telemetry`, in parallel with the Swerve path command.

**Student Outcome:** Explain why this learning event has no Swerve requirement and why one command instance is not reused for repeated marker dispatch.

**Verification Meaning:** Theory, focused/full tests, clean build, Blue/Red Simulation, Driver Station / Glass, and scoped real-robot path/event evidence are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`.

**Connection to Next Lesson:** A01 closes here. The next authorized module is V00, which adds vision as measurement rather than adding A01_L10.

## Student Checkpoint

- Why must starting pose come before pose-targeted motion?
- Why are trajectory generation and trajectory following separate lessons?
- Who owns the one alliance transform?
- Why does AutoBuilder use callbacks instead of owning the Swerve subsystem?
- Why can the L09 learning event coexist with the path?

Self-check: draw the A01 chain and label every item as a plan/target, a measurement/feedback value, an adapter, or an owner. Mark where exact endpoint accuracy is still deferred.

### 8. Module V00 - AprilTag Vision Observation and Pose Fusion

V00 teaches vision as a measurement pipeline. It does not let a camera command Swerve. The authorized progression is:

```text
coordinate frames / camera extrinsics
  -> official AprilTag field layout
  -> VisionIO + immutable observation
  -> deterministic forward simulation
  -> raw robot-pose candidate
  -> quality decision
  -> timestamp / latency semantics
  -> one reviewed real-camera adapter
  -> guarded Swerve estimator fusion
```

**Module goal:** turn camera information into explicit, vendor-neutral, timestamped measurement meaning and provide a guarded boundary for Swerve-owned localization.

**Before V00:** A01 field coordinates, S00_L24 estimator ownership, and the read-only observation architecture.

**After the current V00 snapshot:** L01-L08 are frozen and published; L09 is the sole current `IN_PROGRESS / EDITABLE` lesson with Design Lock PASS but implementation and runtime verification pending.

**Evidence profile:** pure contracts use theory/deterministic evidence and often have `NOT APPLICABLE` runtime surfaces. L08 has scoped Simulation, Glass, and real Limelight evidence. L09 currently has theory/design evidence, `NOT TESTED` Simulation and Glass, and `REAL HARDWARE DEFERRED`.

#### V00_L01 - Vision Coordinate Frames and Camera Extrinsics

**Previous Knowledge:** Final frozen A01_L09 field/autonomous architecture.

**One New Concept:** Canonical field, robot, camera, and fixed-extrinsic transform ownership.

**Why This Lesson Exists:** A camera offset is expressed in robot axes, so rotating the robot changes the camera's field position. Transform order changes the answer.

**What Changes:** Pure `Pose3d`/`Transform3d` composition, inversion, and field-to-robot reconstruction are defined in `frc.robot.vision`.

**What Does NOT Change:** No camera vendor, VisionIO, tag lookup, quality, timing, telemetry, Swerve, autonomous, or physical extrinsic calibration is added.

**Architecture Flow:** `fieldToRobot + robotToCamera -> fieldToCamera`; `fieldToCamera + cameraToRobot -> fieldToRobot`.

**Student Outcome:** Use NWU axes (+X forward, +Y left, +Z up), metres, and radians to predict a simple rotated-camera result.

**Verification Meaning:** Theory and pure deterministic geometry tests are `THEORY VERIFIED`; Simulation, Driver Station / Glass, and real robot are `NOT APPLICABLE`; physical mounting calibration is `REAL HARDWARE DEFERRED`.

**Connection to Next Lesson:** Once frames are named, the robot needs fixed world landmarks: field-to-AprilTag poses.

#### V00_L02 - AprilTag Field Layout Contract

**Previous Knowledge:** Canonical frame/extrinsic mathematics.

**One New Concept:** Immutable official 2026 field-variant lookup for canonical Blue-origin `fieldToTag` poses.

**Why This Lesson Exists:** A seen tag is useful only when its identity and field pose are known in a stable world frame.

**What Changes:** The selected official welded or AndyMark layout is loaded, validated, snapshotted, and exposed through positive tag IDs and immutable poses.

**What Does NOT Change:** No camera sample, VisionIO, robot-pose solver, telemetry, alliance flip, runtime wiring, or Swerve behavior is added.

**Architecture Flow:** `official field layout -> validated tag ID -> canonical fieldToTag reference`.

**Student Outcome:** Explain why canonical Blue-origin tag poses are not alliance-flipped by the vision contract.

**Verification Meaning:** Theory and contract tests are `THEORY VERIFIED`; runtime Simulation, Driver Station / Glass, and real robot are `NOT APPLICABLE`.

**Connection to Next Lesson:** A fixed tag reference still needs a safe way for one camera acquisition cycle to enter robot code.

#### V00_L03 - VisionIO and Immutable Observation Contract

**Previous Knowledge:** Frames and field/tag references.

**One New Concept:** Vendor-neutral one-cycle `VisionIOInputs` transport plus immutable `VisionObservation` meaning.

**Why This Lesson Exists:** Hardware adapters need a mutable carrier they can overwrite, but downstream code needs a stable value that cannot change behind its back.

**What Changes:** `VisionIO`, `VisionIOInputs`, explicit availability states, positive tag IDs, immutable target lists, and validated `cameraToTarget` records are defined.

**What Does NOT Change:** No runtime producer, vendor adapter, Simulation implementation, pose estimation, quality, timing, telemetry, or fusion is added.

**Architecture Flow:** `camera/vendor adapter -> VisionIO.updateInputs -> VisionSubsystem/estimator -> immutable VisionObservation`.

**Student Outcome:** Distinguish `sampleValid`/structural validity from quality-approved-for-fusion.

**Verification Meaning:** Theory and the recorded focused/full contract tests are `THEORY VERIFIED`; runtime Simulation and real camera are `NOT APPLICABLE` and deferred to later V00 lessons.

**Connection to Next Lesson:** The contract exists; the next lesson supplies a deterministic Simulation IO implementation without selecting a vendor.

#### V00_L04 - Deterministic Vision Simulation

**Previous Knowledge:** VisionIO schema, immutable observation states, frames, and tag layout.

**One New Concept:** Forward deterministic `VisionIOSim` synthesis from explicit simulation ground truth.

**Why This Lesson Exists:** Students need to predict what a camera should report before asking a pose estimator to solve backward.

**What Changes:** A vendor-neutral simulator produces the five explicit observation states and synthesizes `cameraToTarget` from a separate known `fieldToRobot` truth source.

**What Does NOT Change:** EstimatedPose is not camera ground truth; no pose estimation, quality, timing, vendor, network, or fusion is added.

**Architecture Flow:** `explicit sim ground truth + fieldToTag + robotToCamera -> VisionIOSim -> cameraToTarget observation`.

**Student Outcome:** Derive a simple camera-to-target transform and explain why a simulator using EstimatedPose would create circular evidence.

**Verification Meaning:** Theory and the recorded focused/inherited/full deterministic tests are `THEORY VERIFIED`; deterministic `VisionIOSim` model coverage is documented, while runtime WPILib Simulation is `NOT APPLICABLE`; real optics/calibration are `REAL HARDWARE DEFERRED`.

**Connection to Next Lesson:** The simulator creates a camera measurement; the next lesson solves a raw robot-pose candidate from it.

#### V00_L05 - AprilTag Robot Pose Estimation

**Previous Knowledge:** Field-to-tag reference, camera-to-target measurement, fixed robot-to-camera extrinsic, and forward Simulation.

**One New Concept:** Pure inverse-transform calculation of a canonical field-to-robot pose candidate.

**Why This Lesson Exists:** Students need to solve the geometry backward before deciding whether a candidate deserves trust or should enter an estimator.

**What Changes:** `fieldToTag + inverse(cameraToTarget) + inverse(robotToCamera) -> fieldToRobot` is implemented as a stateless vendor-neutral calculator.

**What Does NOT Change:** No quality acceptance, timing, camera runtime, alliance transform, Swerve reset, estimator fusion, or telemetry is added.

**Architecture Flow:** `known tag pose + measured transform + fixed extrinsic -> raw pose candidate`.

**Student Outcome:** Explain why a mathematically valid candidate is not automatically accepted or fused.

**Verification Meaning:** Theory and pure deterministic focused/full tests are `THEORY VERIFIED`; runtime Simulation, Driver Station / Glass, and real robot are `NOT APPLICABLE`; camera calibration/quality are `REAL HARDWARE DEFERRED`.

**Connection to Next Lesson:** A candidate needs an explicit policy that asks whether its measurement quality is usable.

#### V00_L06 - Vision Measurement Quality Contract

**Previous Knowledge:** Immutable target data and raw robot-pose candidate geometry.

**One New Concept:** Deterministic distance-based acceptance and qualitative uncertainty classification.

**Why This Lesson Exists:** A valid-looking target can still be too far away for the lesson's explicit trust policy.

**What Changes:** A pure evaluator classifies target distance as accepted/rejected and assigns LOW/MEDIUM/HIGH/UNUSABLE qualitative uncertainty according to immutable thresholds.

**What Does NOT Change:** No ambiguity model, covariance tuning, timestamp, runtime camera, Swerve, NetworkTables, or fusion is added.

**Architecture Flow:** `VisionObservation target transform -> pure quality policy -> immutable quality result`.

**Student Outcome:** Distinguish a valid but rejected sample from malformed program configuration.

**Verification Meaning:** Theory and deterministic contract tests are `THEORY VERIFIED`; runtime Simulation, Driver Station / Glass, and real camera are `NOT APPLICABLE`; a complete physical uncertainty model is `REAL HARDWARE DEFERRED`.

**Connection to Next Lesson:** Quality says whether a sample deserves trust; timing says when that sample actually happened.

#### V00_L07 - Vision Timestamp and Latency Contract

**Previous Knowledge:** Structural validity, pose candidates, and quality meaning.

**One New Concept:** Canonical measurement time, receive time, latency, freshness, and ordering semantics.

**Why This Lesson Exists:** The time code receives a frame is not necessarily the time the image was captured. Estimation must reason about delayed, duplicate, stale, future, or out-of-order samples.

**What Changes:** Immutable timing values and pure evaluators define `measurement = receive - latency` plus freshness and duplicate/order decisions. The current frozen snapshot also records an exceptional inherited Swerve robustness repair; that history does not redefine the vision timing concept.

**What Does NOT Change:** No vendor time conversion, real adapter, camera network schema, or estimator fusion is added by the timing contract.

**Architecture Flow:** `camera timing facts -> immutable VisionTiming -> timing evaluator -> eligible/ineligible measurement meaning`.

**Student Outcome:** Classify zero-latency, stale, duplicate, out-of-order, and impossible-future samples using one robot-time basis.

**Verification Meaning:** Timing theory, focused/full regression, clean build, and runtime WPILib Simulation are `THEORY VERIFIED` and `SIMULATION VERIFIED`. User evidence verifies Teleop/Autonomous usability for the scoped repair, while quantitative drivetrain/timing hardware proof remains `REAL HARDWARE DEFERRED`.

**Connection to Next Lesson:** Once timing meaning is explicit, one reviewed camera can be translated into the frozen VisionIO contract.

#### V00_L08 - Real Vision Adapter Integration

**Previous Knowledge:** Frames, tag layout, VisionIO, immutable observation, quality, and timing contracts.

**One New Concept:** One real Limelight NetworkTables adapter behind the frozen VisionIO boundary.

**Why This Lesson Exists:** The real device must enter the same architecture without leaking vendor types, publishing from IO, fusing pose, or controlling Swerve.

**What Changes:** The adapter reads `/limelight/tv`, `/limelight/tid`, `/limelight/hb`, and `/limelight/targetpose_cameraspace`, applies bounded freshness/coherence checks, maps Limelight axes to WPILib axes, and returns `cameraToTarget` through VisionIO. Real/simulation selection stays in RobotContainer; observation-only runtime ownership and read-only telemetry are wired.

**What Does NOT Change:** No estimator fusion, continuous pose reset, autonomous camera dependency, or vendor API above the real IO adapter is added. The H1 rotation convention remains a `PROVISIONAL COMMISSIONING LOCK`, not official vendor semantics.

**Architecture Flow:** `Limelight NetworkTables -> VisionIOLimelight -> VisionIOInputs -> VisionSubsystem -> immutable VisionObservation -> read-only telemetry`.

**Student Outcome:** Explain why `tv == 0` is an invalid acquisition sample, why independent NetworkTables topics are not atomic, and why a reconnect needs fresh evidence.

**Verification Meaning:** Theory, 37/37 focused tests, 642/642 regression, clean build, Java Simulation path, Glass, and scoped Limelight 4 acquisition/loss/reacquisition are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE VERIFIED`. H1 promotion, physical calibration beyond the recorded scope, and fusion are `REAL HARDWARE DEFERRED`.

**Connection to Next Lesson:** The adapter now supplies real observations. L09 asks whether one qualified, timestamped measurement may enter the Swerve-owned estimator.

#### V00_L09 - Swerve Pose Estimator Vision Fusion

**Previous Knowledge:** Frozen V00_L08 real adapter, quality/timing contracts, and S00_L24 Swerve estimator ownership.

**One New Concept:** Guarded admission of one qualified timestamped vision measurement into the Swerve-owned estimator.

**Why This Lesson Exists:** A camera measurement should correct localization through one explicit owner, not continuously reset pose or steer the drivetrain.

**What Changes:** The approved Design Lock defines `VisionFusionCoordinator`, guarded Swerve-owned admission, and the `SwerveDrivePoseEstimator.addVisionMeasurement(...)` boundary.

**What Does NOT Change:** Vision remains vendor-neutral above IO; Swerve remains the sole estimator owner; canonical field measurements are not alliance-flipped; MegaTag migration, dynamic covariance, Constants tuning, and autonomous redesign are excluded.

**Architecture Flow:** `qualified timestamped measurement -> post-scheduler VisionFusionCoordinator requirement -> guarded Swerve admission -> addVisionMeasurement(...) -> getEstimatedPose()`.

**Student Outcome:** Explain the difference between measurement fusion and continuous pose reset and identify which invalid/stale/out-of-order measurements must be rejected.

**Verification Meaning:** Design Lock and architecture are `THEORY VERIFIED`; implementation and L09 Simulation/Driver Station / Glass are `NOT TESTED`; real fusion verification is `REAL HARDWARE DEFERRED`. This active lesson is not a completion claim.

**Connection to Next Lesson:** V00_L09 is the current roadmap endpoint; any later localization or strategy work requires new governance rather than an invented V00_L10.

## Student Checkpoint

- Why does V00 begin with frames instead of a camera vendor?
- What is the difference between `VisionIOInputs`, `VisionObservation`, a pose candidate, a quality result, and a timestamp?
- Why must Simulation use explicit ground truth rather than EstimatedPose?
- Why does L08 stop at an observation-only adapter?
- Why is L09 `addVisionMeasurement(...)` fusion rather than pose reset?

Self-check: label this diagram with the first V00 lesson that owns each arrow:

```text
camera -> IOInputs -> VisionObservation -> pose candidate
       -> quality -> timestamp -> real adapter -> Swerve estimator
```

### 9. Historical / Parallel Learning Track: D00 and D01

Repository evidence shows that D00 and D01 are not predecessors of S00. D00_L01 starts from an imported competition baseline and D00_L02 through D00_L06 inherit within D00. D01_L01 explicitly inherits D00_L06, and D01 continues its own Tank Drive and mechanism line. By contrast, S00_L01 is a new WPILib Command Robot foundation and S00_L02 inherits S00_L01. A01_L09 also records that D01 has no approved shared command boundary with A01.

Therefore D00 -> D01 is a **legacy/parallel architecture-practice track**. It is useful for beginners because it teaches the same ownership ideas with a simpler tank drive and several mechanisms. It is not an authoritative prerequisite in the S00 -> A00 -> A01 -> V00 main progression unless an instructor separately chooses it as preparation.

Students may use D00/D01 as examples of:

- lifecycle, safe tank hardware, Driver Station communication, input processing, and Real-versus-Simulation IO;
- immutable observations, pure evaluation, read-only NT4 publishing, and runtime telemetry;
- complete intake, flywheel, feeder, shooter, and scheduler-requirement slices; and
- the difference between a simpler teaching robot and the highly coupled Swerve lineage.

| Track lesson | One-concept learning map | Evidence boundary |
| --- | --- | --- |
| D00_L01 Competition Robot Foundation | Where WPILib starts and who owns the basic robot objects. | Theory/build verified; Simulation, Driver Station / Glass, and real robot are `NOT TESTED` in the lesson status. |
| D00_L02 Drivebase Safety Configuration | Safe real Spark MAX configuration, limits, inversion, and startup stop. | Theory, Simulation, Driver Station / Glass, and real hardware are verified for the recorded scope. |
| D00_L03 Tank Drive with Joystick | Independent left/right driver requests through a default command. | Theory, Simulation, and real tank-drive evidence are verified. |
| D00_L04 Wireless Networking and Driver Station | Diagnose mode, USB, network, and Driver Station communication layers. | Theory and HAL Simulation/Driver Station evidence are verified; closure/Git history is incomplete in the older status. |
| D00_L05 Drive Input Processing | Deadband, sign correction, scaling, and clamping in controls. | Theory, Simulation/Driver Station, and real robot input evidence are verified for the recorded scope. |
| D00_L06 Simulation IO Layer | Select deterministic `DriveIOSim` while preserving upper contracts. | Theory, deterministic Simulation, and implementation-selection evidence are verified; physical dynamics remain deferred. |
| D01_L01 Drive Observation Boundary | Copy mutable drive facts into an immutable `DriveObservation`. | Theory, deterministic tests, and recorded hardware regression evidence are verified. |
| D01_L02 Drive Observation Evaluation | Apply explicit tolerance through a pure stopped/not-stopped evaluator. | Theory and deterministic evaluator evidence are verified; older README lifecycle text is stale. |
| D01_L03 Drive Observation Publishing | Publish immutable drive facts through typed telemetry. | Theory and contract tests are verified; runtime Driver Station / Glass and real robot are `NOT TESTED` for this unwired snapshot. |
| D01_L04 Robot Telemetry Runtime Integration | Invoke read-only telemetry after scheduler/subsystem updates. | Theory, Simulation, NT4/Glass, and real runtime evidence are verified by status. |
| D01_L05 Intake Foundation | Build one complete intake command -> subsystem -> IO -> Observation -> telemetry slice. | Theory, Simulation, Glass, and real intake evidence are verified for the recorded scope. |
| D01_L06 Intake Complete Foundation | Add safe TalonFX configuration/readback and electrical observations. | Theory, Simulation, Glass, and real configuration/electrical evidence are recorded as verified. |
| D01_L07 Flywheel Complete Foundation | Reuse the architecture for a flywheel with safe open-loop behavior. | Theory, Simulation, Glass, and real flywheel evidence are recorded as verified. |
| D01_L08 Feeder Complete Foundation | Contain REV-specific feeder details behind its IO contract. | Theory, tests/build, Simulation, Glass, and real feeder evidence are recorded as verified; some historical baseline fields were not tested. |
| D01_L09 Shooter Complete Foundation | Coordinate separate shooter and feeder capabilities through a command. | Theory, tests/build, Simulation, Glass, and real evidence are recorded as verified; Git fields remain user-owned/not tested in the status. |
| D01_L10 Basic Integrated Robot | Combine independent mechanisms into a predictable timed operator workflow. | Theory, Simulation, Glass, real robot, and rendered documentation evidence are recorded as verified. |
| D01_L11 Intake Feeder Coordination | Use scheduler requirements as an ownership lock for a shared feeder. | Theory/build are verified; Simulation, Driver Station / Glass, and real hardware are `NOT TESTED` in the lesson status and remain deferred. |

### 10. Architecture Concept Explanations

| Concept | Plain-English explanation and FRC example | What it is not | First encounter |
| --- | --- | --- | --- |
| Hardware | Physical motors, sensors, controllers, wiring, and mechanisms. Example: a TalonFX driving a Swerve module. | A Java interface or a simulated request. | D00_L02, S00_L02/L03 |
| IO | The contract and adapter that talk to a device. Example: `SwerveModuleIO` exposes operations while CTRE code stays in `SwerveModuleIOCTRE`. | A command or robot-domain policy class. | D00_L06, S00_L03 |
| IO Inputs | A mutable one-cycle transport snapshot populated by IO. Example: measured position, velocity, current, and connection. | An immutable public Observation. | D01_L01, S00_L04 |
| Subsystem | The robot capability that owns domain behavior/state. Example: `SwerveSubsystem.stop()` and pose ownership. | Just one motor variable or a vendor object holder. | D00_L01, S00_L04 |
| Observation | An immutable, vendor-neutral description of what was known at a defined time. Example: `VisionObservation`. | A target, command, publisher, or control decision. | D01_L01, S00_L05 |
| Telemetry | Read-only publication of Observations to NT4, Glass, or logs. Example: `SwerveTelemetryFacade`. | A second control loop or hardware reader. | D01_L03, S00_L06 |
| Command | A behavior with lifecycle and subsystem requirements. Example: a bounded autonomous drive command. | Direct vendor API code. | D00_L01, A00_L01 |
| Coordinator | A narrow owner that orders or bridges existing responsibilities. Example: `RobotTelemetry` or the approved `VisionFusionCoordinator`. | A place to hide arbitrary business logic. | D01_L04, V00_L09 design |
| RobotContainer | The Composition Root that creates, selects, injects, and binds. Example: choose real Limelight IO or `VisionIOSim`. | The robot's all-purpose business-logic class. | D00_L01, every main lesson |
| Robot.java | The TimedRobot lifecycle class that lets WPILib call mode hooks and runs the scheduler boundary. | A place for vendor configuration or controller math. | D00_L01, current V00_L09 source |
| Composition Root | The one assembly point where object dependencies are wired. Here it is `RobotContainer`. | A runtime data-flow stage. | D00_L01 |
| Vendor Adapter | A concrete IO implementation containing vendor API details. Example: `VisionIOLimelight`. | A vendor type exposed to Observation or commands. | S00_L03, V00_L08 |
| Simulation IO | An implementation of an IO contract for a declared fidelity. Example: `VisionIOSim` or `SwerveModuleIOSim`. | Automatic proof of wiring, traction, or calibration. | D00_L06, V00_L04 |
| Frozen Backbone | The protected control and observation dependency directions. | A ban on all future extensions. | S00_L01 |
| Immutable data | A value that cannot change after construction and cannot retain mutable aliases. Example: a record containing a copied target list. | A mutable Inputs object passed around by reference. | D01_L01, V00_L03 |
| Read-only telemetry | Publication that observes and serializes facts without changing robot behavior. | A dashboard-controlled actuator. | D01_L03, S00_L06 |
| Scheduler requirements | WPILib ownership locks that prevent conflicting commands from using one subsystem at once. | A guarantee that two unrelated mechanisms are automatically coordinated. | D01_L11, A00_L02 |
| Pose | Position plus orientation in a stated coordinate frame. Example: `Pose2d` in the field frame. | A motor request or guaranteed ground truth. | S00_L23, A01_L01 |
| Odometry | Pose estimated by integrating measured module travel and heading over time. | Absolute localization or a camera measurement. | S00_L23 |
| Pose Estimator | A subsystem-owned estimator that combines available localization inputs and can accept future measurements. Example: `SwerveDrivePoseEstimator`. | Permission for camera code to reset pose continuously. | S00_L24 |
| Vision Fusion | Guarded admission of a qualified, timestamped vision measurement into the Swerve-owned estimator. | Direct camera-to-motor control or unqualified pose reset. | V00_L09 design |
| Autonomous | Scheduler-managed robot behavior during Autonomous mode. Example: a selected safe PathPlanner routine. | A special pathway that bypasses requirements or mode gates. | A00_L01 |
| Vision measurement | Camera-derived observation/pose information with explicit frame, quality, and time. Example: a qualified AprilTag pose. | A drivetrain command. | V00_L03, V00_L05-L07 |

### 11. Robot.java and RobotContainer.java

#### Robot.java: runtime lifecycle owner

`Robot.java` extends WPILib `TimedRobot`. WPILib calls its lifecycle methods at the appropriate mode boundaries. The class connects framework timing to robot-specific owners; it does not replace the framework with a custom infinite loop.

Typical ordering is:

```text
Main.main()
  -> RobotBase.startRobot(Robot::new)
  -> new Robot()
  -> new RobotContainer()
  -> repeated robotPeriodic()
       -> CommandScheduler.run()
       -> post-scheduler observation coordinators
       -> read-only RobotTelemetry.periodic()
```

The current V00_L09 inherited source illustrates the responsibility boundary:

- The constructor creates `RobotContainer` and obtains the telemetry/coordinator owners that the composition root built.
- `robotPeriodic()` runs `CommandScheduler.getInstance().run()`. In the current safety/fusion-ready structure, the post-scheduler vision coordinator is then called, and telemetry runs in a `finally` path so publication is not silently skipped by an earlier runtime failure.
- `autonomousInit()` asks `RobotContainer` for one freshly composed autonomous command and schedules it once for mode entry.
- `teleopInit()` cancels the stored autonomous command so Teleop can recover ownership.
- `disabledInit()` and `disabledPeriodic()` are lifecycle hooks. Disabled safety behavior belongs in the proper subsystem/command boundary; empty hooks do not authorize bypassing it.
- `testInit()` cancels running commands at the start of Test mode.
- `simulationPeriodic()` invokes the simulation harness when applicable.
- The current autonomous safety design gives `Robot.java` a narrow RuntimeException boundary around scheduler/coordinator work. The failure is bridged to the autonomous safety owner, centralized stop remains in Swerve, the first fault is latched, and automatic autonomous restart is not allowed.

`Robot.java` must not become:

- a hardware vendor adapter;
- a controller-input processor;
- a second `RobotContainer` or dependency graph;
- a home for drivetrain or vision math;
- a telemetry calculation class; or
- a place where a command's child lifecycle is manually reimplemented.

#### RobotContainer.java: Composition Root

`RobotContainer.java` builds the object graph. In the current vision-capable baseline it selects real or Simulation IO, constructs Swerve and Vision subsystems, creates command/adaptor/coordinator objects, configures PathPlanner/AutoBuilder contracts, installs the autonomous chooser, creates telemetry facades, sets the default drive command, and binds the controller.

Its allowed jobs are:

- create objects;
- choose Real IO, Simulation IO, or Noop IO;
- inject dependencies through constructors;
- create/configure command factories and chooser bindings;
- set default commands and controller bindings; and
- connect approved telemetry facades and runtime owners.

Its forbidden jobs are:

- reading a motor or camera for business logic;
- processing joystick axes inline;
- calculating vision acceptance or pose meaning inline;
- owning mutable mechanism state;
- publishing periodic telemetry directly as a second runtime loop; or
- deciding robot behavior merely because it is convenient to access a dependency there.

| Question | `Robot.java` | `RobotContainer.java` |
| --- | --- | --- |
| Who creates objects? | Receives the already-created composition root. | Creates and wires the robot object graph. |
| Who owns runtime lifecycle? | WPILib `TimedRobot` hooks and mode transitions. | No runtime lifecycle ownership. |
| Who runs the scheduler? | `robotPeriodic()` calls `CommandScheduler.run()`. | Does not run the scheduler. |
| Who wires dependencies? | Does not wire mechanism graph. | Injects IO, subsystems, commands, coordinators, and telemetry. |
| Who should contain hardware vendor APIs? | Neither as a general rule. | Neither; the concrete IO adapter owns them. |
| Who should contain vision math? | Neither; pure vision utilities/evaluators own it. | Only selects/wires those objects, not their business logic. |
| Who should contain drivetrain control math? | Neither; commands/subsystems/pure helpers own their defined parts. | Only constructs and connects those owners. |
| What happens on a scheduler fault? | Catches at the runtime boundary and routes it to the safety bridge. | Owns the bridge entry point and safety coordinator dependency. |

The shortest memory aid is: **Robot runs the framework lifecycle; RobotContainer assembles the robot.**

### 12. IO versus Subsystem

Use two questions:

```text
IO:        How do I talk to the device?
Subsystem: What should this robot capability do?
```

For Swerve:

```text
Kraken / CANcoder / Pigeon2
        -> SwerveModuleIO / GyroIO
        -> SwerveSubsystem
```

The CTRE implementation knows Phoenix configuration, device signals, and safe hardware stop. `SwerveSubsystem` knows that four modules form one robot capability, owns the coherent snapshot, controls the output pipeline, owns pose/estimator state, and exposes safe robot-domain methods. Commands ask the subsystem for behavior; they do not call the Kraken or CANcoder.

For Vision:

```text
Limelight
        -> VisionIOLimelight
        -> VisionIOInputs
        -> VisionSubsystem / estimator boundary
        -> immutable VisionObservation
```

The Limelight adapter owns topic names, decoding, axis conversion, freshness/coherence checks, and the provisional commissioning convention. It does not publish NT4, fuse pose, reset Swerve, or expose Limelight types above the adapter. Vision math and evaluators remain vendor-neutral.

Hypothetical Tank Drive example:

```text
left/right motors
        -> TankDriveIO
        -> TankDriveSubsystem
        -> command
```

This tank-drive example is educational only. It does not modify, replace, or become a prerequisite for the existing Swerve course architecture. Its point is to show the same ownership pattern with a simpler mechanism.

If an IO implementation changes from real hardware to Simulation, the subsystem-facing contract should remain understandable. The exact Simulation fidelity must be named: it may be a stored request, deterministic IO state, HALSIM lifecycle, or a physics model. None of these automatically proves wiring, calibration, traction, current draw, or physical safety.

### 13. Student Checkpoints

Use these questions after every module, not only at the end of the repository:

- Can you explain the architecture without code?
- Can you draw the command flow and the observation flow separately?
- Can you name what was inherited and what one concept was added?
- Can you explain why the next lesson depends on this one?
- Can you state which evidence surface proved a claim?
- Can you identify one thing the lesson deliberately did not teach?

For a short self-check, choose one lesson and complete this sentence aloud:

> “The previous lesson already owned ____. This lesson added ____. It deliberately did not add ____. The evidence proves ____, but it does not prove ____.”

### 14. Evidence Classification

Use evidence labels with a scope, not as a general feeling about the project.

| Classification | Meaning in this guide |
| --- | --- |
| `THEORY VERIFIED` | The authority, source, contract, tests, or architecture record supports the explanation. |
| `SIMULATION VERIFIED` | The lesson status records applicable deterministic or runtime Simulation evidence as PASS. |
| `REAL HARDWARE VERIFIED` | User-supplied physical evidence supports the stated, bounded claim. |
| `REAL HARDWARE DEFERRED` | A physical, calibration, timing, tuning, accuracy, or maintenance claim remains outside the supplied proof. |
| `NOT APPLICABLE` | The lesson deliberately has no runtime surface for that evidence type. |

The repository also uses `NOT TESTED`, `HOLD`, and `IN_PROGRESS` in lesson status files. Those are preserved when they describe the current state; they are not silently converted into PASS. Examples:

- D00_L01 Simulation and real robot are `NOT TESTED`, even though the architecture is teachable.
- A01_L03 is a non-actuating trajectory lesson, so real-robot motion is deferred by scope and Driver Station / Glass is not applicable.
- V00_L08 has real Limelight acquisition evidence, but its H1 rotation convention remains provisional and is not promoted to official vendor semantics.
- V00_L09 has an approved Design Lock but no completed implementation or L09 runtime verification; its real fusion gate is deferred.

Simulation-first means prediction -> run/inspect -> observe -> explain. A pure geometry test, a contract test, deterministic IO state, HALSIM lifecycle, and a physics model are different kinds of evidence. A Simulation result cannot prove:

- CAN identity, wiring, firmware, or configuration readback;
- motor/encoder/gyro sign, offset, gearing, or physical-forward direction;
- current, temperature, traction, static friction, vibration, or wheel slip;
- real Driver Station/network recovery;
- physical path endpoint accuracy; or
- camera mounting, optics, vendor timebase, exposure latency, or real estimator correction quality.

### 15. Curriculum Documentation Notes

These notes classify documentation findings without silently repairing them.

#### Curriculum documentation debt

- The older `00A_Curriculum_Scope_and_Sequence.md` and `00B_Repository_to_Curriculum_Architecture_Map.md` were written before the current V00_L08 closure and V00_L09 controlled activation. Their historical text still describes L08 as a candidate/future lesson and L09 as conceptual only. Current AGENTS.md, root README.md, and lesson-local status/README/plan records are the current evidence for L08/L09.
- The older inventory in 00B counts 61 analyzed lessons and stops the primary V00 chain at L07. The current repository inventory is 24 S00 + 4 A00 + 9 A01 + 9 V00 in the main chain, plus 6 D00 + 11 D01 in the separate track.
- Early S00 and D00 lesson directories preserve older lifecycle/documentation shapes. Some early snapshots have `LESSON_STATUS.md` but no lesson-local README, plan, or checklist in the current directory. Their status and transition records remain evidence; missing companion documents are debt, not permission to invent details.
- The checked D00 lessons are missing `LESSON_PLAN.md` and `LESSON_CHECKLIST.md`; the checked D01 lessons are also missing those companion files. This is curriculum documentation debt, not a source architecture defect.
- D01_L02 has older README wording that says `IN_PROGRESS` while its status records completion. D01_L04 retains older lifecycle/real-robot wording while its status records later evidence. D01_L11 is marked complete/frozen but retains `NOT TESTED` runtime gates and pending publication text. These are historical inconsistencies, not source authority.
- D01 also contains stale/copied transition-guide content and other copied README material whose lifecycle wording no longer matches the current status records. These remain documentation/provenance debt and are not repaired by this map.
- The root README and curriculum map contain older examples of a flat D00 layout even though the current repository uses module directories. This is a harmless navigation/documentation mismatch.

#### Harmless historical artifacts

- Inherited lesson folders contain copied guides, hardware documents, and predecessor learning material. Their presence is provenance, not proof that every copied lesson concept is newly implemented in the current snapshot.
- Historical reopen, reconstruction, checkpoint, and publication references are preserved so students can distinguish current authority from prior evidence.

#### Architecture/source defect classification

No architecture/source defect is asserted by this documentation map. The map preserves the Frozen Backbone, the Observation Architecture, the RobotContainer role, the vendor-adapter boundary, read-only telemetry, and the current V00_L09 pending state. A technical defect would require a separate evidence-based review and is outside this documentation-only artifact.

### 16. Final Student View

The complete learning story is controlled growth:

```text
S00: make Swerve ownership, measurement, actuation, and pose explicit
  -> A00: make autonomous scheduling and motion ownership safe
  -> A01: make field navigation and path execution understandable
  -> V00: make camera information explicit, qualified, timed, and safely consumable
```

If you can answer these four questions, you understand the map:

1. Who creates and wires the robot objects? `RobotContainer`, the Composition Root.
2. Who runs the runtime lifecycle and scheduler boundary? `Robot.java` under WPILib `TimedRobot`.
3. Where do vendor APIs live? In the appropriate concrete IO adapter.
4. Can telemetry or camera code directly control Swerve? No. Telemetry is read-only, and vision supplies measurements through the approved localization boundary.

The next lesson is earned by understanding the current frozen boundary, not by skipping it. The active V00_L09 snapshot is still `IN_PROGRESS / EDITABLE`; its Design Lock is approved, but implementation, L09 verification, final closure, and User Git publication remain pending.
