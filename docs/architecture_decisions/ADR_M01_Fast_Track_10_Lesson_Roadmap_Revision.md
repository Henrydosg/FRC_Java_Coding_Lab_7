# ADR: M01 Fast-Track 10-Lesson Roadmap Revision

- Status: **APPROVED** — Architect decision APPROVE_M01_FAST_TRACK_10_LESSON_ROADMAP; User-approved curriculum. Registration recording authorized by AUTH_M01_FAST_TRACK_ROADMAP_REVISION_EXACT_8_FILES.
- Date: 2026-10-15.
- Decision authority: ChatGPT / Architect / Mentor / Gate Authority. User approval of the curriculum supplied in chat.
- Recording engineer: Claude Code (Primary Documentation Engineer), bounded documentation registration only.
- Predecessor decision: [ADR_M01 — M01 Real Mechanism Hardware Integration Roadmap](ADR_M01_Real_Mechanism_Hardware_Integration_Roadmap.md) (registered 2026-10-04). It is preserved unchanged as history.
- Recording review and publication: **PENDING**. Self-review is not independent review. Git publication is User-owned; no future commit or push identity is claimed.

## 1. Decision and supersession scope

Replace the future part of the registered 18-lesson M01 sequence with a 10-lesson Fast-Track sequence that keeps the same mechanism order (**Intake → Feeder → Flywheel → Elevator**) and the same module exit.

This ADR supersedes ADR_M01 only for:

- §7 approved lineage (lesson count and identifiers);
- §8 approved 18-lesson sequence;
- the lesson-number boundaries and lesson-count rationale in §9.

All other ADR_M01 content remains binding by reference and is not restated: §1 purpose and applicability, §2 entry state and donor, §3 required four-mechanism module exit, §4 Frozen Backbone and ownership, §5 configuration authority and hardware facts, §6 evidence, verification and progression gates, the mechanism dependency qualifications of §9, and §10 exclusions and change control. Where the two documents differ on lesson numbering or count, this ADR controls; on every other matter ADR_M01 controls.

[CURRENT_STATE](../governance/CURRENT_STATE.md) remains the sole operational state/action cursor. Registration does not prepare, activate, implement, verify, close or publish any lesson.

## 2. Unchanged lessons

| New | Identifier | Title | State |
| --- | --- | --- | --- |
| L01 | M01_L01_MechanismHardwareReadinessAndIOContract | Hardware Readiness & IO Contract | COMPLETE / FROZEN — unchanged (= old L01) |
| L02 | M01_L02_IntakeRealIOConfigurationAndSafeStop | Intake Real IO & Safe Stop | IN_PROGRESS — unchanged until formally closed (= old L02) |

Their accepted identities, scope, evidence and lifecycle records are not modified by this revision.

## 3. Old-to-new mapping

| New | Title | Old lessons absorbed |
| --- | --- | --- |
| L01 | Hardware Readiness & IO Contract | L01 |
| L02 | Intake Real IO & Safe Stop | L02 |
| L03 | Intake Real Motion & Xbox Control | L03 Intake Measured Inputs and Validity; L04 Intake Bounded Open-Loop Commissioning; L05 Intake Command Hardware Integration |
| L04 | Feeder Real Integration & Coordination | L06 Feeder Real Hardware Integration; L07 Intake–Feeder Physical Coordination |
| L05 | Flywheel Real IO & Commissioning | L08 Flywheel Real IO and Measured Velocity; L09 Flywheel Bounded Open-Loop Commissioning |
| L06 | Flywheel Velocity & Manual Control | L10 Flywheel Closed-Loop Velocity Control; L11 Flywheel Readiness and Manual Command Integration |
| L07 | Elevator Real IO & Protection | L12 Elevator Real IO and Position Measurement; L13 Elevator Motion Protection Readiness |
| L08 | Elevator Motion & Travel Limits | L14 Elevator Bounded Open-Loop Commissioning; L15 Elevator Continuous Travel Protection Verification |
| L09 | Elevator Physical Homing | L16 Elevator Physical Reference and Homing |
| L10 | Elevator Position & Manual Control | L17 Elevator Closed-Loop Position Control; L18 Elevator Manual Command Integration |

Approved lineage: M00_L16 → M01_L01 → M01_L02 → M01_L03 → M01_L04 → M01_L05 → M01_L06 → M01_L07 → M01_L08 → M01_L09 → M01_L10. Each lesson inherits only after its predecessor's accepted COMPLETE / FROZEN / READ-ONLY gate; at most one lesson is editable.

Identifiers for L03–L10 are derived from the approved titles: M01_L03_IntakeRealMotionAndXboxControl, M01_L04_FeederRealIntegrationAndCoordination, M01_L05_FlywheelRealIOAndCommissioning, M01_L06_FlywheelVelocityAndManualControl, M01_L07_ElevatorRealIOAndProtection, M01_L08_ElevatorMotionAndTravelLimits, M01_L09_ElevatorPhysicalHoming, M01_L10_ElevatorPositionAndManualControl. The exact directory name is confirmed at each lesson's separate preparation authorization.

## 4. Gates carried inside merged lessons

A merged lesson keeps one principal concept per lesson, but every dependent gate of the old lessons it absorbs remains a **separate, ordered gate** inside it. A gate is not skipped because it now shares a lesson, and a later gate cannot pass before the earlier one. Each old lesson's simulation and real-hardware requirements in ADR_M01 §8 still apply to the corresponding gate.

### L03 — Intake Real Motion & Xbox Control

Principal concept: qualified first Intake motion and manual control. Required order:

1. Measurement validity — truthful measured Intake inputs with units, freshness and validity; powered stationary observation, actuation inhibited (old L03).
2. **AUD-P1-02 satisfied** — all-actuator scheduler-fatal stop coverage and exception-safe command cleanup (including the known IntakeToFeederCommand.end() rethrow) before any nonzero mechanism output.
3. Safe bounded first motion — scheduler-managed, output/time-bounded, Test-enabled commissioning through subsystem/IO; direction/inversion and observed stopping (old L04).
4. Xbox held-forward and release-stop — qualify the existing manual command/binding without unintended restart (old L05).
5. Simple manual reverse only if safely supported by the accepted evidence; otherwise it is excluded.
6. User hardware verification of the gates above.

No new state machine, umbrella subsystem or abstraction; reuse the existing semantic IntakeSubsystem, IntakeIO and Real/Noop adapters.

### L04 — Feeder Real Integration & Coordination

Principal concept: a second qualified physical capability and its bounded coordination. Order: Feeder readiness → zero-output configuration → measured inputs → bounded motion → manual gates (old L06); then bounded manual use of the existing IntakeToFeederCommand with contention, interruption, partial failure and both-stop handling (old L07). Coordination begins only after independent Feeder qualification. Transfer claims need separate observed evidence.

### L05 — Flywheel Real IO & Commissioning

Principal concept: trustworthy rotational feedback and bounded motor response. Order: Flywheel Real adapter and zero-output measured RPM with sensor source and conversion (old L08); then durable, formally reviewed open-loop commissioning with direction, inversion, dynamic sign/scale and deceleration evidence (old L09). Closed-loop velocity and normal operator use remain inhibited in this lesson.

### L06 — Flywheel Velocity & Manual Control

Principal concept: bounded velocity regulation and qualified manual use. Order: bounded closed-loop velocity over a conservative range with configuration provenance (old L10); then manual velocity command/binding with evidence-supported readiness tolerance (old L11). The provisional 50 RPM readiness tolerance must be resolved by evidence or an approved replacement before manual acceptance. No new readiness algorithm or shooting coordination.

### L07 — Elevator Real IO & Protection

Principal concept: trustworthy position measurement and protection before trusted reference. Order: Elevator Real adapter and position measurement with unsupported reference explicitly false, gravity/load-safe stationary condition before power (old L12); then subsystem-owned motion admission and continuous protection policy verified while stationary, motion and homing inhibited (old L13).

### L08 — Elevator Motion & Travel Limits

Principal concept: first bounded movement and moving travel-protection qualification. Order: first limited interior-region movement under L07 protections, no endpoints approached (old L14); then a separate gate demonstrating minimum/maximum protective response and stopping margins while moving (old L15). Target admission alone cannot pass the travel-protection gate.

### L09 — Elevator Physical Homing

Principal concept: trusted physical reference acquisition (old L16). Requires qualified motion and continuous protection. No fabricated success on timeout or logical zero; a supported absolute-reference procedure may replace motion-based homing when the hardware supports it.

### L10 — Elevator Position & Manual Control

Principal concept: bounded position regulation and qualified manual operation. Order: referenced closed-loop position control within the accepted envelope with gravity-safe failure behavior (old L17); then manual position and reference-acquisition commands, final composition checks and the four-mechanism module exit audit (old L18).

## 5. Preserved safety prerequisites and invariants

- AUD-P1-02 is a hard prerequisite inside L03 before any nonzero mechanism output (gate 2 above).
- Measurement trust precedes motion; protection precedes Elevator movement; moving protection precedes homing; trusted reference precedes position control (ADR_M01 §9).
- If the actual assembly cannot provide safe protection before reference acquisition, dependent implementation must HOLD for Architect hardware/contract adjudication. No limit switch, encoder, brake or other device is presumed.
- Four-mechanism module exit unchanged; no mechanism may remain REAL HARDWARE DEFERRED at module exit (ADR_M01 §3).
- Simulation precedes powered verification; powered steps require explicit Architect authorization and User operation (ADR_GOV3 §6).
- Frozen Backbone, Frozen Interface Contract, configuration authority and contract change control unchanged (ADR_M01 §4–§5, §10).
- Existing M01_L02 gates are not changed by this revision. As projected in CURRENT_STATE at registration: powered hardware NOT AUTHORIZED; H1.2 AWAITING REAL ROBOT ACCESS / NOT VERIFIED; H2/H3 NOT AUTHORIZED; E2-F01 Disabled-only four-CANcoder MagnetHealth pre-check PENDING before any powered Swerve motion. These are records of that point in time; they do not override later accepted User hardware evidence. This roadmap revision neither adjudicates hardware verification nor authorizes powered motion; those remain separate Architect gates.
- No final all-four-mechanism coordination lesson, autonomous mechanism actuation, SysId by default, or competition optimization (ADR_M01 §10).

## 6. Explicit exclusions

This registration does not authorize: creating, copying or preparing M01_L03 or any later lesson; closing M01_L02; Java, test, configuration or dependency changes; Gradle, Simulation, Glass/Driver Station, deployment or powered hardware; edits to ADR_M01 or any frozen lesson; any Git write by an agent.

## 7. Registration evidence and next boundary

The prior CURRENT_STATE is preserved verbatim in [the roadmap revision history event](../governance/history/events/M01_FAST_TRACK_ROADMAP_REVISION_2026-10-15.md) under root AGENTS §6 and ADR_GOV2 §8. Root AGENTS §15 and root README provide navigation only.

Next boundary: Architect review, independent review if authorized, User verification and User-owned publication of this recording. M01_L02 closure and M01_L03 preparation each require separate authorization.
