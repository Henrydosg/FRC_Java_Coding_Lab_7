# Governance 2.0 Delegated Rule — ARCHITECTURE

> ACTIVE DELEGATED GOVERNANCE 2.0 RULE
>
> Incorporated by [root AGENTS.md](../../../AGENTS.md) within its exact delegated scope. Root controls conflicts pending HOLD and Architect reconciliation.

Delegated scope: Frozen flows, package/dependency/IO/Observation/telemetry ownership, scheduler/localization, safe stop, Real/Sim/Noop and approved exceptions. This file creates no lesson, repair, publication or later-gate authorization.

Registered migration decision: [GOV2 ADR](../../architecture_decisions/ADR_GOV2_Governance_2_0_Agent_Instructions_State_and_History_Migration.md).

## Active architecture contract

CONTROL: Driver -> Xbox Controller -> controls -> commands -> subsystems -> IO -> hardware.
OBSERVATION: hardware -> IOInputs -> subsystem/estimator -> immutable Observation -> read-only telemetry -> NT4/Glass/log.

RobotContainer constructs, selects, injects and binds; it does not own processing, hardware behavior or periodic publishing. Main/Robot retain approved lifecycle duties. Commands use semantic subsystem APIs and declare exact WPILib requirements. Scheduler owns contention and lifecycle; no manual child lifecycle, polling-based owner arbitration, or hidden coordinator replaces it.

Subsystems own mechanism behavior, state, input refresh and explicit stop. Vendor APIs remain in approved concrete IO adapters; subsystem dependencies use vendor-neutral contracts. Mutable one-cycle IOInputs do not escape as retained public models; consumers use defensive immutable observations. Telemetry neither controls nor schedules nor reads vendor devices; Field2d visualizes localization without owning it. IO does not publish NetworkTables or coordinate commands. util stays generic.

Swerve remains the sole drivetrain/localization owner. Odometry O and estimated pose E have distinct meaning; field-heading reanchoring, known-pose reset and vision admission preserve existing guards. Finite held E alone is not current valid path feedback; the existing measurementSampleValid qualification remains required. AutoBuilder feedback/reset/output consume approved Swerve contracts. Vision candidates are qualified measurements, not a second pose authority. Canonical field vision is not alliance-flipped; simulation ground truth must not be estimator feedback.

O is Swerve-owned wheel/gyro odometry; E is the Swerve-owned authoritative fused field pose, including qualified vision admission through the existing estimator contract. O and E may differ and retain their independently preserved values through coherent field-heading reanchoring. AutoBuilder consumes E; the accepted Field2d display uses O. Neither telemetry nor an autonomous adapter computes a competing authoritative pose. Source: the accepted ACM-09 ownership record, AGENTS L0638-L0714.

Composition selects Real/Simulation with RobotBase.isReal() where applicable; selection does not leak vendor APIs across seams. Physical-scope lessons require approved concrete Real IO; an approved roadmap may defer hardware with Noop/Simulation when contracts/ownership remain correct, evidence is honest and REAL HARDWARE DEFERRED explicit. No physical behavior follows from software tests, invalid Noop zero or intent telemetry. No retroactive M00 real-mechanism requirement is imposed.

Safe stop/interruption/mode loss use subsystem-owned stop semantics and preserve fail-closed/no-unintended-restart behavior. Preserve both CTRE drive/steer stop attempts and execution-scoped unavailable-input barriers, permanent fatal-fault precedence, and scheduler-native preparation refresh sequencing. A software stop attempt does not establish physical device response after vendor failure.

The external human/operator-input exception uses one coherent vendor-neutral immutable DriverInputObservation; it cannot create mechanism observations or control-bearing telemetry. Module-specific restrictions below retain their named lesson/module applicability and do not become new implementation permission. Original M00_L01 exclusions apply to L01; later accepted M00 capabilities remain valid. D01 retains separate historical Tank ownership; its hardware identities do not transfer to M00.

## Traceable source families

These reviewed clauses retain full accepted A/H/C families. Normalization is limited to LF presentation, relocated section pointers, scoped User cache ownership and explicit registered GOV2 Git/physical-scope/future-reading decisions. Exact original bytes remain in the archive. Named historical lesson exclusions retain their original applicability and do not authorize work.

### Family A-L1835-L1862 — Frozen Backbone/control and observation flows

Source: AGENTS L1835–L1862; accepted classification A; Keep both flows and read-only telemetry inline

## 3. Frozen Backbone

Always preserve

Driver
→ Xbox Controller
→ controls
→ commands
→ subsystems
→ io
→ hardware

Observation flow

hardware
→ IOInputs
→ subsystem / estimator
→ immutable Observation
→ telemetry
→ NT4 / Glass / log

Telemetry is read-only.

This mechanism observation flow remains unchanged. The narrowly approved external human/operator
input exception is defined in this file's Approved External Operator-Input Observation Exception family and does not apply to mechanism Observations.

---

### Family A-L1863-L1897 — Package responsibility rules

Source: AGENTS L1863–L1897; accepted classification A; Inline boundary summary; full detail in `rules/ARCHITECTURE.md`; Documents A/C

## 4. Package Responsibilities

controls
- Driver input processing only.
- For external human/operator input only, controls may acquire one coherent controller sample and
  produce an immutable, vendor-neutral DriverInputObservation.
- This exception does not permit controls to produce mechanism Observations.

commands
- Coordinate subsystem actions.

subsystems
- Own mechanism behavior and state.

io
- Hardware abstraction only.

observation
- Immutable, vendor-neutral read models and pure evaluators only.
- Subsystems or dedicated estimators produce mechanism Observations.
- The approved external human/operator input exception in this file is the only approved controls-produced
  Observation exception.
- No hardware access, vendor APIs, NetworkTables, CommandScheduler, RobotContainer, mutable mechanism state, or control behavior.

telemetry
- Consume and publish immutable Observations only.
- No behavior control or hardware access.
- Lesson-specific approved exceptions are recorded in the architecture decision records referenced
  by READING_AND_AUTHORITY.md and do not establish general package dependencies.

util
- Generic shared reusable helpers only.

---

### Family A-L1898-L1919 — RobotContainer responsibility

Source: AGENTS L1898–L1919; accepted classification A; Composition-root restriction inline

## 5. RobotContainer

RobotContainer is the Composition Root.

Allowed

- object creation
- dependency injection
- implementation selection
- default commands
- button bindings

Forbidden

- hardware logic
- mechanism logic
- input processing
- telemetry calculations
- business logic

---

### Family A-L1920-L1943 — IO contract and implementation rules

Source: AGENTS L1920–L1943; accepted classification A; Vendor neutrality and adapter boundary inline; Real/Noop applicability needs adjudication

The preceding note reproduces the accepted inventory's design-stage wording. Its applicability issue is resolved for the active contract by registered GOV2 ADR §13; it is not an outstanding hardware requirement or new authorization.

## 6. IO Contract

Every mechanism must provide

- IO interface
- Inputs snapshot
- Approved concrete Real implementation when physical hardware scope is established; roadmap-approved Noop/Simulation deferral remains valid
- Simulation or Noop implementation when required by the current lesson
- Safe stop()

Flow

Hardware
→ IO
→ IOInputs
→ Subsystem
→ immutable Observation
→ Telemetry

Subsystems never access vendor hardware directly.
Telemetry never publishes directly from mutable IOInputs when an Observation contract exists.

---

### Family A-L2216-L2228 — External-input exception

Source: AGENTS L2216–L2228; accepted classification A; Preserve exact exception inline; it does not permit telemetry control

### Approved External Operator-Input Observation Exception

For external human/operator input only, controls may produce an immutable, vendor-neutral
DriverInputObservation from one coherent controller sample.

This exception:

- does not change the mechanism observation flow;
- does not permit controls to produce mechanism Observations;
- does not permit Observation to contain hardware access, vendor APIs, NetworkTables,
  CommandScheduler, RobotContainer, mutable state, or control behavior; and
- does not permit telemetry to control robot behavior.

### Family H-L2273-L2279 — Repeated A01 architecture restrictions

Source: AGENTS L2273–L2279; accepted classification H; Canonical roadmap/architecture rule; retain any safety summary deliberately

A00_L04's Autonomous+Enabled safety invariant and centralized
`SwerveSubsystem.stop()` authority remain authoritative. `RobotContainer` remains the composition
root only, and Simulation-before-real-robot verification remains mandatory. PathPlanner is
prohibited before A01_L06, AutoBuilder is prohibited before A01_L07, and the A01_L06 mandatory
compatibility entry gate remains authoritative. Vision/AprilTags are outside the A01 baseline,
and D01 retains mechanism architecture ownership.

### Family H-L2376-L2391 — V00 restrictions

Source: AGENTS L2376–L2391; accepted classification H; Roadmap-specific restrictions; preserve single estimator and independent simulation truth

V00 preserves the Frozen Backbone and Observation Architecture. Vision vendor APIs may exist only
inside the selected real VisionIO adapter; Vision models and evaluators remain immutable and
vendor-neutral; telemetry remains read-only; and `SwerveSubsystem` remains the sole owner of
`SwerveDrivePoseEstimator`. Vision supplies accepted timestamped measurements only, and the
approved fusion boundary uses `addVisionMeasurement(...)` rather than continuous pose reset.
Autonomous continues to consume `getEstimatedPose()` and shall not access camera, VisionIO, or
vendor APIs directly. A01_L04 remains the sole alliance-transform owner; vision measurements use
canonical WPILib field coordinates and are not alliance-flipped. Simulation shall not use
EstimatedPose as camera ground truth and shall pass before real-robot fusion verification.

No camera or vendor is selected in V00_L01 through V00_L07. V00_L08 may select exactly one real
vision implementation only after explicit review of actual camera hardware, WPILib 2026
compatibility, the exact vendor library/version, timestamp semantics, dependency resolution, and
simulation support where applicable. Lessons shall not be reordered, renamed, merged, split,
inserted, or skipped without the architecture/governance approval required by the V00 ADR.

### Family H-L3447-L3464 — Repeated M00 architecture constraints

Source: AGENTS L3447–L3464; accepted classification H; M00 ADR; mastered architecture reuse and exclusions

M00 preserves the Frozen Backbone, Frozen Interface Contract, Constants as the
default configuration authority, frozen predecessor protection, and one lesson
per new concept. RobotContainer remains composition root only. Vendor APIs
remain confined to concrete IO adapters. Mechanism data continues to flow
`hardware -> IOInputs -> subsystem/processing -> immutable Observation ->
read-only telemetry`. Intake, Feeder, Flywheel, and Elevator retain independent
ownership. Shooting composition remains `FlywheelSubsystem + FeederSubsystem +
ShootCommand`; no `ShooterSubsystem` or `ShooterIO` is authorized absent a
later formal architecture change.

M00_L01's sole concept is Mechanism Architecture Reuse: how the mastered
drivetrain, vision, and autonomous architecture applies to non-drivetrain
mechanisms. It may teach subsystem ownership, IO, immutable Observations,
read-only telemetry, composition-root assembly, safe stop, and architecture
mapping. It must not implement Intake, Feeder, Flywheel, Elevator, closed-loop
control, readiness, homing, travel limits, coordination, autonomous events, or
a new hardware API.
