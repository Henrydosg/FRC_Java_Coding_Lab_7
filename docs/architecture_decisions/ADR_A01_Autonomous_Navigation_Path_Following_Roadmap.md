# ADR: A01 Autonomous Navigation and Path Following Roadmap

- Status: APPROVED
- Date: 2026-08-16
- Amendment: APPROVED 2026-08-23 - A01_L09 demonstration-binding boundary
- Scope: Post-A00 autonomous-navigation module and lesson sequence
- Authority: Approved successor ADR to
  `ADR_A00_Autonomous_Command_Foundation_Roadmap.md`. The repository authority
  order remains unchanged. Governance registration in `AGENTS.md` remains a
  separate follow-up.

## Context

`S00_L24_PoseEstimationAndAutonomousReadiness` and
`A00_L04_AutonomousMotionSafetyGating` are complete, frozen, and read-only.
S00 ends at L24. A00 ends at L04 under its approved ADR. No A00_L05 is
authorized.

The frozen baseline now provides the following foundation:

- WPILib command lifecycle and scheduler ownership;
- bounded, robot-relative autonomous motion;
- centralized drivetrain stop authority;
- autonomous-enabled mode gating with fail-closed behavior;
- odometry and a subsystem-owned pose estimator;
- known-pose reset under the existing Disabled-only contract;
- measured robot-relative `ChassisSpeeds`; and
- existing field-relative conversion support.

The baseline does not provide a competition-specific starting-pose contract,
pose-targeted control, trajectory generation or following, alliance transforms,
PathPlanner, AutoBuilder, routine selection, event-marker coordination, or
vision timestamp/latency handling.

The repository learning rules require inheritance development, one independent
project per lesson, one new concept per lesson, Simulation before real-robot
verification, and preservation of frozen predecessors. The existing A00 ADR
explicitly excludes the subjects proposed here. Continuing A00 would therefore
expand a closed ADR rather than create the required new architectural boundary.

## Architecture Challenge

### One-concept review

The proposed sequence is educationally sound after the following scope
adjustments:

1. Starting pose and reference-frame ownership remain together in the first
   lesson because they define one autonomous initialization contract. That
   lesson shall not implement alliance mirroring or path following.
2. Pose-targeted motion is retained. It is not a duplicate of trajectory
   following: it teaches pose error, target tolerances, timeout, and terminal
   behavior before time-parameterized motion is introduced.
3. Trajectory generation and sampling remain separate from the follower. L03
   is a bounded WPILib-native trajectory learning/control foundation for
   generation, constraints, time ordering, sampling, start/end state, and
   terminal semantics. It is not a permanent competing trajectory or path
   framework after PathPlanner/AutoBuilder integration.
4. Field and alliance transforms remain before the follower and before
   PathPlanner. This makes coordinate ownership explicit before a path asset
   or vendor integration can conceal it.
5. The custom holonomic follower is retained as educational/reference
   infrastructure. It exposes sample timing, pose feedback, field-relative
   output, and safe stop behavior before PathPlanner integration. It shall use
   WPILib-supported primitives and remain small; it is not a general
   path-planning framework. L06/L07 shall transition production path execution
   to PathPlanner/AutoBuilder. After L07, no new production capability may be
   added to the L05 follower; it may remain frozen as a learning reference and
   deterministic test oracle.
6. PathPlanner path/runtime integration and AutoBuilder configuration are
   separated. Loading and executing a PathPlanner asset is distinct from
   configuring the adapter contracts that connect PathPlanner to the robot.
7. Routine selection and safe composition remain separate from AutoBuilder.
   Selection/composition has independent chooser, requirement, cancellation,
   and failure semantics.
8. NamedCommands and event markers are retained as the final A01 boundary, but
   only as a controlled event-dispatch and coordination concept. New mechanism
   architecture, mechanism contracts, or broad autonomous mechanism strategy
   require a later ADR or a separately approved integration scope. D01 owns
   mechanism subsystems, IO, commands, and mechanism contracts; A01 may only
   coordinate existing D01 command contracts.

### Missing prerequisite review

No prerequisite is missing for the proposed navigation sequence after the
starting-pose contract is added. The existing S00_L24 estimator and measured
speed contracts are inherited rather than recreated. Vision is intentionally
not a prerequisite for the first PathPlanner capability.

### Sequence decision

The proposed sequence is retained with PathPlanner/AutoBuilder split into two
lessons. The resulting roadmap contains nine core lessons. This ADR amendment
does not implement production code or tests; lesson activation remains a
separate documentation workflow.

## Proposed Decision

Authorize the following new module only after this ADR is approved:

`A01 - Autonomous Navigation and Path Following`

The proposed repository location is:

`real_robot_programming/module_A01/`

The module shall inherit from the published, frozen
`A00_L04_AutonomousMotionSafetyGating` project. Each later A01 lesson shall
inherit only from the immediately preceding A01 lesson after that predecessor
is `COMPLETE / FROZEN / READ-ONLY`.

This approved ADR authorizes roadmap scope only. It does not authorize
creation of `module_A01`, any lesson directory, any vendordep, or any
production change until the required governance updates are approved.

## Module Purpose and End State

A01 shall teach the smallest understandable progression from the frozen
autonomous command foundation to deterministic field-based trajectory
execution and safe PathPlanner integration.

The intended end state is:

- a validated autonomous starting pose and field-frame contract;
- a tested pose-target control primitive;
- deterministic trajectory generation and sampling knowledge;
- explicit Blue/Red alliance and field-frame transforms;
- a tested holonomic trajectory follower;
- one safe PathPlanner path executed through a verified AutoBuilder contract;
- deterministic routine selection and safe command composition; and
- a separately testable event-marker dispatch boundary.

A01 does not imply real-robot PASS. Real-robot verification remains user-owned
and HOLD until explicitly performed and recorded.

### Scope Deferred Beyond A01_L09

A01_L09 is the completion point for the autonomous-navigation and
path-following foundation. It is not the final competition autonomous
architecture.

The following remain outside A01 and require later governance and lessons as
appropriate:

- vision and AprilTags;
- timestamp- and latency-aware vision fusion;
- advanced localization;
- dynamic replanning;
- obstacle avoidance;
- game-specific autonomous optimization;
- final real-robot competition tuning; and
- new mechanism architecture.

Real-robot verification or tuning explicitly authorized by an individual A01
lesson does not make A01 the final competition-tuning authority. Any broader
competition tuning or autonomous architecture requires a later approved scope.

## Authorized Lesson Sequence

The following lessons are proposed for authorization after ADR approval.

### A01_L01 - Autonomous Starting-Pose and Field-Frame Contract

- One concept: authoritative autonomous reference-frame initialization from a
  validated starting pose and heading.
- Prerequisite: frozen A00_L04 and inherited S00_L24 pose infrastructure.
- Production value: prevents autonomous motion from beginning with an unknown,
  stale, or frame-incompatible pose.
- Expected impact: starting-pose availability/validation contract and
  composition-root wiring in the new project.
- Deterministic verification: valid pose, invalid pose, unavailable pose,
  Disabled-only reset, and refusal to start without a usable pose.
- Simulation: required.
- Real robot: HOLD pending a documented field-pose procedure.
- Exclusions: alliance mirroring, trajectories, PathPlanner, vision,
  mechanism events, and multi-step routines.

### A01_L02 - Pose-Targeted Autonomous Motion

- One concept: closed-loop movement toward a field-relative target pose.
- Prerequisite: A01_L01.
- Production value: teaches pose error, translation/rotation tolerance,
  timeout, terminal stop, and cancellation before trajectory timing is added.
- Expected impact: one small pose-target command/controller and deterministic
  tests.
- Deterministic verification: target pose convergence, tolerance, timeout,
  mode interruption, requirement ownership, and centralized stop.
- Simulation: required.
- Real robot: HOLD.
- Exclusions: trajectory generation, PathPlanner, AutoBuilder, vision,
  alliance transforms, and mechanism events.

### A01_L03 - Trajectory Generation and Sampling Fundamentals

- One concept: finite time-parameterized trajectory data and deterministic
  sampling.
- Prerequisite: A01_L02.
- Production value: makes trajectory constraints, timing, samples, and terminal
  states understandable before a path-planning integration is introduced.
- Expected impact: a bounded WPILib-native trajectory learning/control
  foundation with pure deterministic tests. It may support the educational
  L05 reference follower, but it shall not become a permanent competing
  trajectory or path framework after PathPlanner/AutoBuilder integration.
- Deterministic verification: finite samples, monotonic time, start/end pose,
  constraint compliance, and terminal sample behavior.
- Simulation: required for sample validation; drivetrain motion is not required.
- Real robot: HOLD.
- Exclusions: PathPlanner, AutoBuilder, vision, event markers, and mechanism
  routines.

### A01_L04 - Field and Alliance Transform Contract

- One concept: canonical path-frame ownership and Blue/Red alliance
  transformation.
- Prerequisite: A01_L01 and A01_L03.
- Production value: prevents incorrect, missing, or double application of
  alliance transforms.
- Expected impact: explicit pose, heading, and velocity transform utilities
  with no hidden transformation in the drivetrain subsystem.
- Deterministic verification: Blue/Red symmetry, reflected pose, heading and
  velocity transformation, and no-double-transform cases.
- Simulation: required for both alliance states.
- Real robot: HOLD.
- Exclusions: PathPlanner, vision, event markers, and dynamic replanning.

### A01_L05 - Holonomic Trajectory Following

- One concept: trajectory sample to holonomic control output to field-relative
  and robot-relative drivetrain request.
- Prerequisite: A01_L02, A01_L03, and A01_L04.
- Production value: establishes a transparent follower before vendor/path
  integration and proves the existing A00 stop and mode-gate contracts remain
  effective.
- Expected impact: one bounded follower command/controller and integration
  tests using deterministic pose and clock fixtures.
- Role boundary: L05 is educational/reference infrastructure that validates
  trajectory-following semantics before vendor integration. L06/L07 transition
  production path execution to PathPlanner/AutoBuilder. After L07, no new
  production capability may be added to this follower. It may remain frozen as
  a learning reference and deterministic test oracle.
- Deterministic verification: sample timing, pose error, bounded output,
  terminal tolerance, cancellation, requirement ownership, mode loss, and
  centralized stop.
- Simulation: required; this is the first end-to-end trajectory-motion
  simulation.
- Real robot: HOLD.
- Exclusions: PathPlanner, AutoBuilder, vision, event markers, and mechanism
  coordination.

### A01_L06 - PathPlanner Path and Runtime Integration

- One concept: loading and executing one PathPlanner path asset through the
  already-understood follower and safety contracts.
- Prerequisite: A01_L01 through A01_L05.
- Production value: introduces the practical path-authoring/runtime boundary
  without making PathPlanner the first source of trajectory knowledge.
- Expected impact: approved PathPlanner vendordep, one path asset, path loading
  adapter, and integration tests.
- Deterministic verification: valid path selection, missing path, invalid path
  data, finite output, requirement ownership, mode cancellation, and fail-safe
  stop.
- Simulation: required with one known path and known starting pose.
- Real robot: HOLD.
- Exclusions: AutoBuilder configuration, vision, mechanism events, multiple
  routines, and dynamic replanning.

### A01_L07 - AutoBuilder Contract Integration

- One concept: configure AutoBuilder against the repository's pose, reset,
  output, alliance, requirement, and safety contracts.
- Prerequisite: A01_L04, A01_L05, and A01_L06.
- Production value: teaches AutoBuilder as an adapter/configuration boundary
  rather than treating it as an opaque replacement for the robot architecture.
- Expected impact: composition-root AutoBuilder configuration and focused
  contract tests; no SwerveSubsystem ownership transfer.
- Deterministic verification: pose supplier/reset wiring, output callback,
  alliance supplier, requirement ownership, missing configuration, mode loss,
  and centralized stop.
- Simulation: required using the same known path and start-pose contract.
- Real robot: HOLD.
- Exclusions: vision, event markers, mechanism strategy, and dynamic replanning.

### A01_L08 - Autonomous Routine Selection and Safe Composition

- One concept: selecting and composing autonomous routines with explicit
  cancellation, requirements, and failure behavior.
- Prerequisite: A01_L07.
- Production value: supports more than one competition option without losing
  scheduler ownership or safe cancellation.
- Expected impact: chooser and routine composition in RobotContainer as the
  composition root.
- Deterministic verification: routine selection, unavailable routine, path
  failure, interruption, requirement conflicts, no automatic restart, and
  centralized stop.
- Simulation: required with at least two routines.
- Real robot: HOLD.
- Exclusions: vision, event-marker mechanism coordination, and dynamic
  replanning.

### A01_L09 - PathPlanner NamedCommands and Event Markers

- One concept: event-marker dispatch and safe coordination of named commands
  during a PathPlanner autonomous execution.
- Prerequisite: A01_L08 and approved D01 mechanism command interfaces where
  applicable.
- Production value: establishes the navigation-to-mechanism integration point
  needed for competition routines without changing the Frozen Backbone.
- Ownership boundary: D01 owns mechanism subsystems, IO, commands, and
  mechanism contracts. A01_L09 may only register, dispatch, and coordinate
  existing mechanism commands. A01 must not redesign or extend mechanism
  architecture.
- Failure boundary: missing commands, unsafe requirement conflicts, and
  invalid dispatch must fail safely. Broader autonomous-mechanism architecture
  requires a future ADR or amendment.
- Expected impact: named-command registry, event dispatch, requirement and
  timeout tests, and a narrowly scoped composition-root integration.
- Deterministic verification: marker ordering, dispatch, missing command,
  requirement conflict, timeout, cancellation, and drivetrain stop behavior.
- Simulation: required with a path containing at least one controlled event.
- Real robot: HOLD.
- Exclusions: new mechanism architecture, new mechanism IO contracts, vision,
  AprilTags, and dynamic replanning. Any broader mechanism-autonomous strategy
  requires a future ADR or amendment.

### Approved A01_L09 Integration-Boundary Amendment

D01 is an independent Tank Drive WPILib project and has no approved shared
command boundary with A01; real D01 mechanism integration is therefore
unavailable to A01_L09.

A01_L09 may prove PathPlanner NamedCommands and event-marker dispatch using
safe, observable, deterministic non-mechanism demonstration Command bindings.

Demonstration bindings are not mechanism integration.

A future approved robot-integration layer may replace those demonstration
bindings with real mechanism Commands without changing the A01 event-dispatch
architecture.

A01 must not absorb mechanism subsystems, IO, vendor APIs, business logic, or
mechanism contracts.

The approved L09 implementation direction is a typed event binding carrying a
stable event name, a fresh `Supplier<Command>`, and explicit
`Set<Subsystem>` requirements. The composition root registers a deferred
command through `Commands.defer(...)`; no `AutonomousEventCommandProvider`
interface is authorized at this stage. This amendment does not add a lesson or
change the approved A01 lesson order.

## Dependency Order

```text
Frozen S00_L24 pose/estimator/measured-speed contracts
    + frozen A00_L04 lifecycle and autonomous safety gate
        -> A01_L01 starting pose and reference frame
        -> A01_L02 pose-target control
        -> A01_L03 trajectory generation and sampling
        -> A01_L04 field and alliance transforms
        -> A01_L05 holonomic trajectory following
        -> A01_L06 PathPlanner path/runtime integration
        -> A01_L07 AutoBuilder contract integration
        -> A01_L08 routine selection and safe composition
        -> A01_L09 NamedCommands and event markers
```

Alliance and field transforms must be understood before the follower and before
PathPlanner/AutoBuilder. The follower must pass before vendor integration so
path asset, adapter, frame, and control failures remain distinguishable.

## Frozen Inheritance Baseline

Every A01 lesson shall preserve:

- the Frozen Backbone and dependency direction;
- the Frozen Interface Contract;
- `SwerveSubsystem` ownership of localization and actuation;
- existing IO and observation contracts;
- read-only telemetry behavior;
- `RobotContainer` as the composition root;
- centralized `SwerveSubsystem.stop()` authority;
- finite-request fail-closed behavior; and
- A00_L04's autonomous-motion invariant.

No A01 lesson may modify S00, A00, the Frozen Interface Contract, hardware
configuration, or unrelated mechanism architecture.

## PathPlanner and AutoBuilder Boundary

PathPlanner shall not be added before A01_L06. AutoBuilder shall not be added
before A01_L07.

### Mandatory A01_L06 Entry Gate

Before any A01_L06 implementation begins or any PathPlanner dependency is
added, the implementation phase shall verify:

- compatibility with the repository's active WPILib 2026 release;
- compatibility with the actual installed/project API set;
- the exact PathPlanner vendordep/version compatibility; and
- successful dependency resolution and build feasibility in the intended
  inherited project.

The exact PathPlanner version shall be identified from the actual supported
compatibility evidence during implementation. It shall not be guessed or
selected from an unverified name alone.

If compatibility or dependency-resolution feasibility cannot be established,
A01_L06 implementation shall stop and require architecture/governance review
or an ADR amendment as appropriate.

The first PathPlanner lesson shall use one known path and the existing tested
pose, frame, follower, requirement, and safety contracts. It shall not introduce
vision, multi-routine selection, event markers, or dynamic replanning at the
same time.

L06/L07 are the production migration boundary: L06 introduces the PathPlanner
path/runtime boundary, and L07 establishes the approved AutoBuilder execution
configuration. After L07, no new production capability may be added to the
L05 follower or the L03 trajectory foundation. Those components may remain
frozen learning references and deterministic test oracles.

AutoBuilder shall remain an adapter configured from the composition root. It
shall not transfer localization or actuation ownership away from
`SwerveSubsystem`.

## Frame and Alliance Ownership

L04 owns the definition of the canonical field frame and the Blue/Red alliance
transform contract. The drivetrain subsystem remains responsible for its
existing field-relative conversion and actuation boundary.

Exactly one alliance transformation owner is permitted in any production path.
Before L05, L06, or L07 execution is accepted, the selected execution
architecture shall identify whether the transformation is performed by the
A01 autonomous layer or delegated to PathPlanner/AutoBuilder.

If transformation is delegated to PathPlanner/AutoBuilder, autonomous data
shall remain canonical and unflipped. If transformation is performed by A01,
vendor alliance flipping shall be disabled or otherwise not reapplied. Double
transformation is forbidden.

No hidden alliance transform may be introduced in IO, Swerve module code, or
telemetry. Each path or pose shall have one documented frame, and each alliance
transformation shall be applied exactly once under the selected owner.

## Starting-Pose Ownership

The autonomous composition layer owns the competition starting-pose procedure,
availability decision, and request to reset localization. `SwerveSubsystem`
remains the owner of localization state mutation and validation under its
existing contract.

Starting-pose selection shall be explicit, finite, frame-labeled, and validated
before autonomous motion begins. The provisional learning pose from S00_L24 is
not an official competition pose database.

## Safety-Gate Preservation

The A00_L04 invariant remains mandatory:

> Nonzero autonomous drivetrain motion is permitted only while
> `DriverStation.isAutonomousEnabled() == true`.

If the condition becomes false, the active autonomous motion must fail closed,
reach centralized drivetrain stop, clear stale motion intent, and not restart
automatically when the mode later becomes valid.

All A01 motion commands and compositions shall preserve scheduler-managed
requirements and lifecycle behavior. Manual child-command lifecycle delegation
is prohibited.

## Verification Gates

Each lesson must pass, in order:

1. architecture review for the single new concept and frozen-boundary check;
2. inherited baseline Java 17 build;
3. focused deterministic tests;
4. full regression and clean build;
5. required Simulation verification;
6. Driver Station / Glass evidence where the lesson exposes runtime state;
7. documentation and transition-guide audit; and
8. explicit Real Robot status.

No lesson may be marked complete from a build result alone. A Real Robot
`HOLD` remains visible until the user supplies the applicable hardware evidence.

## Real-Robot HOLD Policy

Real-robot verification remains user-owned. No A01 lesson may claim a hardware
PASS from Simulation, Java tests, or a clean build.

Any real-robot motion verification shall be performed only after Simulation
passes, with Driver Station state, emergency-stop/disable readiness, safe speed
limits, and a documented rollback/stop procedure.

## Vision Boundary

AprilTag and vision integration are not prerequisites for the first successful
trajectory or PathPlanner capability. Vision shall be introduced only after a
deterministic path has been executed successfully with known starting pose,
odometry/estimator state, and alliance correctness.

Vision requires a later separately governed scope covering timestamped
measurements, latency, uncertainty, invalid measurements, estimator fusion,
and simulation evidence. It may be an optional later phase unless the selected
competition strategy makes it necessary.

## Explicit Prohibition of A00_L05

This ADR does not create, authorize, or reopen `A00_L05`.

A00 remains closed at:

`A00_L04_AutonomousMotionSafetyGating - COMPLETE / FROZEN / READ-ONLY`

All post-A00 autonomous navigation work, if approved, belongs to the new A01
module and must inherit from frozen A00_L04.

## Conditions Requiring a Future ADR Amendment

An amendment or new ADR is required before:

- changing the A01 module boundary or lesson order;
- adding or removing an A01 lesson;
- making vision or AprilTags a prerequisite for PathPlanner;
- adding dynamic replanning or obstacle avoidance;
- changing the Frozen Backbone, Frozen Interface Contract, IO ownership,
  observation flow, or RobotContainer responsibility;
- introducing new mechanism IO or autonomous mechanism contracts;
- changing the A00 closure boundary;
- changing the real-robot verification ownership or HOLD policy; or
- introducing a PathPlanner/AutoBuilder version or dependency that is not
  compatible with the approved WPILib 2026 project baseline.

## Consequences

As approved, this ADR provides a controlled successor boundary after A00,
while preserving all frozen S00 and A00 projects. It will authorize roadmap
scope only. Lesson creation, project copying, vendordep changes, source
implementation, tests, builds, Simulation, and real-robot verification remain
separate workflow steps.

The proposed A01 sequence intentionally delays vendor integration until the
underlying pose, frame, trajectory, follower, and safety concepts are
independently testable. It also delays broad mechanism coordination until the
navigation path is already selectable and safely composable.

## Review Result

This ADR is APPROVED and FROZEN as the A01 roadmap authority. It authorizes
roadmap scope only; governance registration in `AGENTS.md` and the root
`README.md`, module creation, lesson creation, vendordep changes, source
implementation, tests, builds, Simulation, and real-robot verification remain
separate workflow steps.
