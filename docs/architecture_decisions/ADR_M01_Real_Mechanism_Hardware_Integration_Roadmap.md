# ADR: M01 Real Mechanism Hardware Integration Roadmap

- Status: **APPROVED / REGISTERED**, subject to successful Architect review of this registration recording.
- Date: 2026-10-04.
- Decision authority: ChatGPT / Architect / Mentor / Reviewer.
- Recording engineer: Sol / Codex, within bounded documentation registration only.
- Approval and recording provenance: Architect-approved registration brief, attachment **0628bd70-e0a1-447d-804a-a5210f72d7c7**, supplied by the User.
- Content basis: the accepted revised 18-lesson M01 proposal following bounded revision brief **05d64653-7d92-43fd-80cd-b4c35e40177d**. The earlier seven-lesson proposal was accepted in part; its Intake/Feeder-only exit was superseded by the approved four-mechanism exit.
- Authority: registered navigation in [root AGENTS](../../AGENTS.md), within existing roadmap applicability and the unchanged authority hierarchy. English Documents A/B/C remain authoritative; this ADR cannot amend the Frozen Backbone.
- Recording review: **PENDING ARCHITECT REVIEW**. Static self-review is not independent review or Architect acceptance of this recording.
- Publication of this recording: not performed by the recording engineer; Git publication remains User-owned. No future commit or push identity is claimed.

## 1. Decision, purpose and applicability

Register **M01 — Real Mechanism Hardware Integration** as the approved successor roadmap after the completed M00 software/mechanism architecture foundation.

Its future repository location is real_robot_programming/module_M01/. This location is a curriculum boundary, not permission to create a directory, prepare a lesson or implement code.

M01 extends the completed M00 architecture into qualified real physical mechanism integration while preserving the Frozen Backbone. The approved mechanism order is **Intake → Feeder → Flywheel → Elevator**.

This registration records the approved roadmap only. Module preparation, lesson preparation, inherited baseline verification, architecture audit, Design Lock, implementation, physical verification, closure, freeze and publication require their applicable separate gates. Registration does not execute or authorize those actions.

[CURRENT_STATE](../governance/CURRENT_STATE.md) remains the sole operational state/action cursor. This ADR defines the approved roadmap boundary and does not maintain a competing current-state table.

## 2. Entry state and donor

The entry contract is:

- M00_L16_MechanismAutonomousEventIntegration is COMPLETE / FROZEN / READ-ONLY and is the approved full-architecture donor.
- The roadmap is approved/registered, with review of this recording still required.
- Separate lesson/module preparation authorization remains required.
- Completed physical mechanism commissioning or tuning is not required merely for module entry or authorized preparation.
- Blocking hardware facts must be established before the dependent Real implementation or powered gate; UNKNOWN facts must not be represented as verified.

M01_L01 inherits from the complete frozen project at:

real_robot_programming/module_M00/M00_L16_MechanismAutonomousEventIntegration/

The exact first lesson is **M01_L01_MechanismHardwareReadinessAndIOContract**, titled **Mechanism Hardware Readiness and IO Contract**.

D00/D01 is a separate historical/parallel lineage and is not the M01 donor. M00 remains complete through L16; M00_L17 is not authorized.

## 3. Required module exit

The final inherited M01 project must contain:

- Qualified Real Intake: verified configuration, measured health, bounded actuation, stop behavior and manual command use.
- Qualified Real Feeder: equivalent independent qualification and the retained bounded Intake–Feeder coordination.
- Qualified Real Flywheel: trustworthy mechanism RPM, verified direction/conversion, conservative velocity limits, bounded closed-loop regulation, physically supported readiness policy and manual velocity-command use.
- Qualified Real Elevator: trustworthy position measurements, physical reference acquisition, qualified minimum/maximum travel protection, conservative motion, bounded position control and manual position/homing command use.
- Correct Real/non-real composition, immutable observations, read-only telemetry and scheduler ownership.
- Accepted individual interruption, mode-loss, unavailable-input and applicable fault/stop evidence within declared devices, snapshots, loading and operating conditions.
- Required documentation, transition guides and independent closure gates.

No mechanism may remain REAL HARDWARE DEFERRED at the completed four-mechanism exit. Missing required physical evidence prevents module completion; it does not retroactively invalidate M00's accepted software foundation.

M01 completion does not automatically establish competition readiness, maximum performance, autonomous mechanism actuation, unrestricted payload operation, shooting accuracy or SysId completion.

No final all-four-mechanism coordination lesson is required. Individual qualification and final composition/regression review are the module exit, not a new simultaneous-action or orchestration feature.

## 4. Frozen Backbone and ownership

Preserve the existing control and observation flows:

    Controls → scheduler-managed Commands → semantic Subsystems
             → vendor-neutral IO → concrete Real / Sim / Noop adapters

    hardware/simulation → typed IOInputs → subsystem/estimator
                        → immutable Observation → read-only telemetry

The following boundaries remain unchanged:

- RobotContainer constructs, selects, injects and binds as composition root only; it owns no mechanism, safety, calibration or control policy.
- Vendor APIs, hardware access, vendor configuration objects and apply/readback stay inside approved concrete Real IO.
- Every mechanism retains dedicated typed Inputs and updateInputs. Mutable transport does not escape as public mechanism state.
- Subsystems own behavior, state, safety/admission/reference policy and explicit stop.
- Observations remain immutable and vendor-neutral. Pure evaluators receive explicit inputs/configuration and remain stateless and deterministic.
- Telemetry only consumes/publishes immutable observations; it does not control, schedule or read vendor devices.
- WPILib CommandScheduler owns lifecycle, requirements and contention; commands use the smallest approved subsystem dependencies.
- Applicable implementation selection uses RobotBase.isReal() at the approved construction boundary.
- Swerve remains the sole drivetrain/localization owner. Preserve O/E distinctions, qualified feedback/reset contracts, vision admission, alliance ownership, independent simulation ground truth and existing autonomous safety boundaries.
- Every unqualified physical entrypoint remains inhibited, including inherited manual bindings and autonomous event paths. Autonomous mechanism actuation remains outside this roadmap.
- Unrelated outputs require an approved verification boundary; mechanism work does not enlarge Swerve or vision scope.

The inherited Flywheel and Elevator contracts do not expose open-loop commissioning output. The approved progression requires minimal, durable, formally reviewed subsystem/IO support for that capability in future authorized M01 copies. Existing velocity, position and homing methods must retain their meanings; commands never bypass IO or reinterpret a closed-loop request as open-loop output.

Inputs/Observation evolution and inherited exact-contract architecture assertions require explicit impact review. Breaking changes require the applicable version update and migration evidence. Temporary-demo-only IO methods, generic hidden control paths and unauthorized contract changes are prohibited.

## 5. Configuration authority and hardware facts

Constants.java remains the default configuration authority, organized by mechanism/concern. Future separately authorized M01 lesson copies may add evidence-supported configuration, as applicable:

- CAN IDs / ports and actual device identity;
- inversion and current limits;
- sensor conversions and mechanism ratios;
- safe output/time bounds;
- physical travel limits and operational envelopes;
- PID/feedforward gains and readiness tolerances.

Numeric robot policy/configuration remains vendor-neutral where applicable. Vendor configuration objects, controller-specific conversion/application and readback remain in concrete Real IO. Pure evaluators obtain explicit approved neutral policies.

No unknown hardware values are assigned by this roadmap. Hardware identity, safety configuration, calibration and tuning remain distinct evidence classes. Historical device identities and provisional values do not become physical authority for a new mechanism.

The inherited Flywheel 50 RPM readiness tolerance is explicitly provisional / not hardware validated. Future physical evidence must support it or an approved replacement in the authorized M01 copy. Registration does not select a tolerance, gain, controller, sensor, limit switch or brake.

Constants review remains CLOSED: CFG-A01 DEFER, CFG-A02 DO NOT CHANGE, CFG-A03 DEFER. This roadmap does not reopen cleanup or modify frozen M00_L16 Constants.java.

## 6. Evidence, verification and progression gates

Use THEORY VERIFIED, SIMULATION VERIFIED, REAL HARDWARE VERIFIED and REAL HARDWARE DEFERRED with explicit scope. NOT APPLICABLE requires an actual applicability judgment, not missing evidence.

Simulation evidence never substitutes for real-hardware evidence. Noop is not a physics simulation; deterministic fixtures/harnesses are reported separately from WPILib runtime Simulation. Model/controller fidelity limits remain explicit.

Powered hardware verification is User-owned. Every implemented lesson retains applicable architecture review, User-operated inherited baseline/build/tests, focused and inherited regression, Simulation before powered verification, Driver Station/Glass evidence, documentation, transition-guide and closure gates.

The lesson entries below are approved future requirements, not achieved execution results. Hardware readiness, units, freshness, limits, loading and physical stopping must be qualified before dependent gates. A software stop attempt is not proof of physical response after vendor failure.

Preparation may retain UNKNOWN facts truthfully. Hardware-ready acceptance and Real activation cannot pass blocking unknowns. Physical evidence is scoped to the actual devices, snapshot, conditions, method, date and Verification Engineer.

## 7. Inheritance and future start workflow

One lesson is one independent WPILib project with one principal new concept. Reuse mastered architecture as supporting implementation; do not create layers or classes merely for lesson counting.

The approved lineage is M00_L16 → M01_L01 → M01_L02 → M01_L03 → M01_L04 → M01_L05 → M01_L06 → M01_L07 → M01_L08 → M01_L09 → M01_L10 → M01_L11 → M01_L12 → M01_L13 → M01_L14 → M01_L15 → M01_L16 → M01_L17 → M01_L18.

Each later lesson inherits only after its predecessor's accepted COMPLETE / FROZEN / READ-ONLY gate. At most one explicitly authorized lesson is editable. Protected predecessors are not modified.

After separate preparation authorization, the User copies/renames the correct donor, removes only authorized generated artifacts in the copy, runs the inherited baseline and supplies Git status and BUILD SUCCESSFUL. Baseline PASS precedes inheritance/architecture audit; Design Lock precedes separate implementation authorization. Required User verification and finalized transition documentation precede COMPLETE/FROZEN. No step in this workflow is performed or authorized by registration alone.

## 8. Approved 18-lesson sequence

### L01 — Mechanism Hardware Readiness and IO Contract

**ID:** M01_L01_MechanismHardwareReadinessAndIOContract

- Objective: Establish Intake inventory, safety prerequisites and compatibility with inherited contracts.
- Principal new concept: Evidence-based hardware readiness.
- Architecture addition: Readiness/contract documentation; no runtime addition.
- Hardware state: Hardware unnecessary for preparation; actual inventory and unpowered inspection required for hardware-ready acceptance.
- Simulation requirement: New runtime behavior NOT APPLICABLE; inherited baseline gates remain.
- Real-hardware requirement: Scoped readiness evidence; powered behavior REAL HARDWARE DEFERRED.
- Primary gate: Accepted readiness and IO compatibility contract.
- Ordering reason: Establishes prerequisites before constructing a Real adapter.

### L02 — Intake Real IO Configuration and Safe Stop

**ID:** M01_L02_IntakeRealIOConfigurationAndSafeStop

- Objective: Connect Intake hardware while retaining zero output.
- Principal new concept: Qualified Real IO configuration.
- Architecture addition: Intake Real adapter, configuration/readback and direct implementation selection.
- Hardware state: Identified assembly; Disabled, controlled zero-output verification.
- Simulation requirement: Required for selection, configuration failure, unavailable input and stop paths.
- Real-hardware requirement: Configuration/readback and observed zero-output scope.
- Primary gate: Qualified configuration and safe stationary output.
- Ordering reason: Uses L01's accepted device identity and safety contract.

### L03 — Intake Measured Inputs and Validity

**ID:** M01_L03_IntakeMeasuredInputsAndValidity

- Objective: Separate measured device facts from requested intent.
- Principal new concept: Truthful measured Inputs.
- Architecture addition: Required measurement/validity fields and reviewed immutable observation/telemetry extensions.
- Hardware state: Powered stationary observation; actuation inhibited.
- Simulation requirement: Required for invalid, stale, unavailable and recovery cases.
- Real-hardware requirement: Actual readings and validity behavior.
- Primary gate: Accepted units, freshness and validity semantics.
- Ordering reason: Measurement trust precedes commissioning.

### L04 — Intake Bounded Open-Loop Commissioning

**ID:** M01_L04_IntakeBoundedOpenLoopCommissioning

- Objective: Establish conservative physical Intake response.
- Principal new concept: Bounded powered commissioning.
- Architecture addition: Scheduler-managed commissioning through subsystem/IO, with approved output/time admission.
- Hardware state: First limited Test-enabled Intake movement.
- Simulation requirement: Required for bounds, termination, interruption, mode loss and failures.
- Real-hardware requirement: Direction/inversion, bounded response and observed stopping.
- Primary gate: Accepted first-motion evidence.
- Ordering reason: Requires L02 configuration and L03 measurement qualification.

### L05 — Intake Command Hardware Integration

**ID:** M01_L05_IntakeCommandHardwareIntegration

- Objective: Qualify inherited manual Intake operation.
- Principal new concept: Physical manual-command qualification.
- Architecture addition: Reviewed admission of the existing command/binding.
- Hardware state: Individually commissioned Intake.
- Simulation requirement: Required for held input, release, contention, disable and recovery.
- Real-hardware requirement: Manual lifecycle and applicable stop/fault behavior.
- Primary gate: Accepted operator-command evidence without unintended restart.
- Ordering reason: Normal operator use follows bounded commissioning.

### L06 — Feeder Real Hardware Integration

**ID:** M01_L06_FeederRealHardwareIntegration

- Objective: Independently qualify Feeder using the established pattern.
- Principal new concept: Qualification of a second physical capability.
- Architecture addition: Feeder Real adapter and necessary configuration/measurement extensions.
- Hardware state: Ordered readiness → zero/configuration → Inputs → bounded motion → manual gates.
- Simulation requirement: Required before powered stages.
- Real-hardware requirement: Independent Feeder configuration, motion, manual and stop evidence.
- Primary gate: Individually qualified Real Feeder.
- Ordering reason: Reuses the completed Intake pattern before coordination.

### L07 — Intake–Feeder Physical Coordination

**ID:** M01_L07_IntakeFeederPhysicalCoordination

- Objective: Qualify bounded coordination of two proven capabilities.
- Principal new concept: Physical two-mechanism coordination.
- Architecture addition: Bounded manual use of existing IntakeToFeederCommand.
- Hardware state: Intake and Feeder independently qualified.
- Simulation requirement: Required for contention, interruption, partial failure and both-stop handling.
- Real-hardware requirement: Coordinated response and stopping; transfer claims require separate observed evidence.
- Primary gate: Accepted bounded coordination evidence.
- Ordering reason: Depends on both individual qualifications.

### L08 — Flywheel Real IO and Measured Velocity

**ID:** M01_L08_FlywheelRealIOAndMeasuredVelocity

- Objective: Establish trustworthy physical Flywheel RPM.
- Principal new concept: Qualified rotational-feedback boundary.
- Architecture addition: Flywheel Real adapter, configuration/readback and required freshness/validity support.
- Hardware state: Actual hardware identified; stationary zero-output observation. Other Flywheel actuation remains inhibited.
- Simulation requirement: Required for RPM units, validity, connection/configuration failure and observation paths.
- Real-hardware requirement: Device/configuration evidence, sensor source and supported mechanism-RPM conversion.
- Primary gate: Accepted zero-output feedback contract; dynamic sign/scale confirmation remains pending.
- Ordering reason: Reuses the hardware-integration pattern established through L07.

### L09 — Flywheel Bounded Open-Loop Commissioning

**ID:** M01_L09_FlywheelBoundedOpenLoopCommissioning

- Objective: Establish physical motor response before velocity regulation.
- Principal new concept: Bounded rotational commissioning.
- Architecture addition: Durable subsystem-owned commissioning capability with minimal formally reviewed neutral IO support.
- Hardware state: Conservative Test-enabled motion; closed-loop velocity and normal operator use inhibited.
- Simulation requirement: Required for output/time bounds, stop, invalid feedback and fault disarm.
- Real-hardware requirement: Direction, inversion, dynamic RPM sign/scale, deceleration and conservative operating constraints.
- Primary gate: Accepted bounded response with trustworthy dynamic feedback.
- Ordering reason: Uses L08's measured-input contract; precedes the first velocity loop.

### L10 — Flywheel Closed-Loop Velocity Control

**ID:** M01_L10_FlywheelClosedLoopVelocityControl

- Objective: Qualify regulation over a conservative RPM range.
- Principal new concept: Bounded physical velocity regulation.
- Architecture addition: Real implementation of existing velocity requests; target admission and approved PID/feedforward configuration where applicable.
- Hardware state: Qualified feedback and open-loop response; controlled limited velocity requests.
- Simulation requirement: Required for target admission, feedback loss, saturation/stop behavior and declared controller/model scope.
- Real-hardware requirement: Measured tracking, stability and applicable stop/failure response under stated conditions.
- Primary gate: Accepted bounded velocity-control evidence and configuration provenance.
- Ordering reason: Requires physically confirmed response and feedback from L09.

### L11 — Flywheel Readiness and Manual Command Integration

**ID:** M01_L11_FlywheelReadinessAndManualCommandIntegration

- Objective: Qualify normal manual velocity operation with truthful readiness reporting.
- Principal new concept: Qualified manual Flywheel operation.
- Architecture addition: Scheduler-managed manual velocity command/binding; evidence-supported existing readiness policy.
- Hardware state: Bounded velocity loop qualified.
- Simulation requirement: Required for readiness boundaries, invalid measurements, release/interruption, disable and fresh-request recovery.
- Real-hardware requirement: Readiness tolerance evidence and manual command/stop behavior.
- Primary gate: Individually qualified Real Flywheel; provisional readiness tolerance resolved for the declared range.
- Ordering reason: Normal use follows L10 regulation. Readiness adjudication precedes manual acceptance; no new readiness algorithm or shooting coordination is introduced.

### L12 — Elevator Real IO and Position Measurement

**ID:** M01_L12_ElevatorRealIOAndPositionMeasurement

- Objective: Establish trustworthy position transport without claiming a physical reference.
- Principal new concept: Qualified linear-position feedback.
- Architecture addition: Elevator Real adapter, configuration/readback and required measurement/validity support.
- Hardware state: Actual assembly identified; gravity/load-safe stationary condition established before power.
- Simulation requirement: Required for units, unavailable/stale measurements and valid-versus-referenced distinctions.
- Real-hardware requirement: Configuration, sensor meaning, conversion and stationary stop/Disabled conditions.
- Primary gate: Accepted position-measurement contract; unsupported reference remains explicitly false.
- Ordering reason: Extends the learned Real-feedback pattern to the higher-risk linear mechanism.

### L13 — Elevator Motion Protection Readiness

**ID:** M01_L13_ElevatorMotionProtectionReadiness

- Objective: Establish protection sufficient to permit first bounded movement.
- Principal new concept: Protection before trusted reference.
- Architecture addition: Subsystem-owned motion admission and continuous protection policy; actual hardware enforcement remains in Real IO.
- Hardware state: Stationary verification; ordinary motion and homing inhibited.
- Simulation requirement: Required for both directions, boundary states, invalid sensors, unknown reference and fault disarm.
- Real-hardware requirement: Actual protective-device/strategy evidence, stationary output inhibits and gravity/load-safe failure conditions.
- Primary gate: Accepted protection for a conservative first-motion region, including unreferenced operation.
- Ordering reason: Uses L12's measurement meanings before allowing movement.

### L14 — Elevator Bounded Open-Loop Commissioning

**ID:** M01_L14_ElevatorBoundedOpenLoopCommissioning

- Objective: Establish conservative motion and coordinate direction.
- Principal new concept: First bounded Elevator movement.
- Architecture addition: Reviewed subsystem/IO commissioning capability and bounded scheduler command.
- Hardware state: Limited interior-region movement under L13 protections; no homing or closed-loop positioning.
- Simulation requirement: Required for bounds, validity loss, interruption, mode loss and stop behavior.
- Real-hardware requirement: Physical direction/sign, conversion, bounded displacement and stop/load behavior.
- Primary gate: Accepted first-motion evidence without approaching endpoints.
- Ordering reason: Motion follows protection readiness.

### L15 — Elevator Continuous Travel Protection Verification

**ID:** M01_L15_ElevatorContinuousTravelProtectionVerification

- Objective: Demonstrate protective response while the mechanism is moving.
- Principal new concept: Physical continuous travel-protection qualification.
- Architecture addition: No new owner; verification of L13 protection with L14's qualified motion capability.
- Hardware state: Controlled movement under a reviewed boundary-verification procedure.
- Simulation requirement: Required for moving boundary crossings, margins and applicable failure cases.
- Real-hardware requirement: Minimum/maximum protective response, stopping margins and applicable sensor-loss/disable behavior.
- Primary gate: Accepted continuous protection evidence; target admission alone cannot pass.
- Ordering reason: Separates moving protection verification from first movement and precedes homing.

### L16 — Elevator Physical Reference and Homing

**ID:** M01_L16_ElevatorPhysicalReferenceAndHoming

- Objective: Establish a physically justified position origin.
- Principal new concept: Trusted physical reference acquisition.
- Architecture addition: Actual reference procedure and Real IO operations under subsystem policy and the inherited bounded homing lifecycle.
- Hardware state: Motion and continuous protection qualified; actual reference source/procedure identified.
- Simulation requirement: Required for success, timeout, unavailable reference, interruption and reference invalidation/recovery.
- Real-hardware requirement: Actual reference acquisition, repeatability within declared limits and safe unsuccessful termination.
- Primary gate: Trusted reference without fabricated success on timeout or logical zero.
- Ordering reason: Homing follows qualified motion/protection. A supported absolute-reference procedure may avoid motion-based homing; hardware determines applicability.

### L17 — Elevator Closed-Loop Position Control

**ID:** M01_L17_ElevatorClosedLoopPositionControl

- Objective: Qualify conservative referenced position requests.
- Principal new concept: Bounded physical position regulation.
- Architecture addition: Real implementation of existing position requests, evidence-based operational envelope and approved controller configuration.
- Hardware state: Trusted reference and continuous protection active; declared load conditions.
- Simulation requirement: Required for target admission, reference/feedback loss, protection response and declared regulation-model scope.
- Real-hardware requirement: Bounded tracking, stop/load behavior and protective response within the accepted envelope.
- Primary gate: Accepted position regulation; gravity-safe failure behavior remains mandatory.
- Ordering reason: Position control requires L16 reference and all preceding protection gates.

### L18 — Elevator Manual Command Integration

**ID:** M01_L18_ElevatorManualCommandIntegration

- Objective: Qualify normal manual position and reference-acquisition operations.
- Principal new concept: Qualified manual Elevator operation.
- Architecture addition: Minimal approved command/binding integration through existing semantic subsystem APIs.
- Hardware state: Individually qualified Elevator; all four Real adapters present in final composition.
- Simulation requirement: Required for requirements/contention, cancellation, disable, invalid reference and fresh-request recovery.
- Real-hardware requirement: Manual position/homing lifecycle, applicable stop behavior and final stationary composition checks.
- Primary gate: Individually qualified Real Elevator and accepted four-mechanism module exit audit.
- Ordering reason: Operator use follows protected position regulation; inherited regression closes the module without adding orchestration.

## 9. Mechanism progression and dependency qualifications

The approved boundaries are Intake L01–L05; Feeder L06; Intake–Feeder coordination L07; Flywheel L08–L11; Elevator L12–L18. L01–L07 retain their accepted IDs, titles, order and principal concepts.

Flywheel proceeds from zero-output measured RPM to bounded motor response, then bounded velocity regulation, then qualified manual operation with physically supported readiness. Sensor/conversion/sign evidence precedes closed loop. Readiness classification does not prove shooting performance.

Elevator proceeds from position measurement and stationary safety to protection before movement, first bounded movement, moving protection verification, physical reference/homing, bounded position regulation and manual operation. The inherited logical target-admission envelope is not continuous overtravel protection. Logical zero and homing timeout do not establish a physical reference. Zero electrical output does not establish gravity-safe stopping.

No limit switch, encoder, brake or other device is presumed present. If the actual assembly cannot provide safe protection before reference acquisition, dependent implementation must HOLD for Architect hardware/contract adjudication. The roadmap does not invent missing physical safeguards.

The 18-lesson length preserves seven accepted lessons and adds four Flywheel/seven Elevator lessons. Repeated inventory, vendor-object construction and telemetry wiring support the principal concept rather than each creating another lesson. First measured feedback, first powered motion, first closed loop, moving travel-protection validation and first homing retain distinct dependent gates. No symmetry-driven final coordination lesson is added.

## 10. Explicit exclusions and change control

This registration does not authorize:

- module_M01 or any M01 lesson directory/file creation, copying or preparation;
- Java, tests, configuration, dependency or Real IO implementation;
- T00 registration/activation or access to its protected candidate contents;
- drivetrain SysId or mechanism SysId merely by default;
- competition optimization, unrestricted operation or autonomous mechanism actuation;
- a new umbrella mechanism subsystem or a final all-four-mechanism coordination lesson;
- historical M00 or other frozen-project edits, M00_L17 or Constants cleanup;
- project build/tests, Simulation, Glass/Driver Station, powered hardware, tuning or SysId execution;
- any Git write, staging, commit, push, branch or tag mutation by an agent.

Lesson identities/order, ownership, contract evolution and exceptions remain subject to formal change control. Hardware uncertainty or a failed required gate causes HOLD; it cannot be bypassed through historical approvals, simulated intent, a build PASS or roadmap registration.

## 11. Registration evidence and next boundary

The approved content is recorded without redesign. The required prior current-state snapshot and supersession provenance are preserved in [the M01 registration history event](../governance/history/events/M01_ROADMAP_REGISTRATION_2026-10-04.md), under root AGENTS §6 and the registered GOV2 ADR §8.

Root AGENTS and root README provide roadmap navigation only. CURRENT_STATE records accepted approval/registration, pending review/publication of this recording and the absence of preparation/implementation authority. The PDF/mirror manifest is not a general ADR registry and receives no change.

Await Architect review of the registration evidence before any M01_L01 preparation authorization.
