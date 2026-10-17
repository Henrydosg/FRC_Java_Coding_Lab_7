# M00_L02 - Mechanism Hardware Evidence Audit Learning Guide

English is normative. The Vietnamese guide is explanatory and must preserve this guide's structure and meaning.

## 1. Lesson Identity and Purpose

- Module: `M00 - Competition Mechanism Foundations`
- Lesson: `M00_L02 - Mechanism Hardware Evidence Audit`
- Sole new concept: classify mechanism facts using disciplined evidence.
- Runtime scope: none. This lesson does not implement or operate a mechanism.

This lesson teaches a beginner how to separate a known fact from an assumption before later mechanism work begins. A tidy spreadsheet is not enough: every entry needs a defensible state, source, verification action, safety meaning, and future dependency.

## 2. Learning Objectives

By the end of the lesson, a student can:

1. distinguish a hardware fact from an assumption;
2. assign exactly one state: `VERIFIED`, `PROVISIONAL`, `UNKNOWN`, or `NOT APPLICABLE`;
3. identify an evidence source without overstating what it proves;
4. explain why the selected state is justified;
5. record the unresolved action needed to strengthen the evidence;
6. explain the safety relevance and future lesson dependency;
7. avoid turning an evidence audit into hardware selection or implementation; and
8. preserve the Frozen Backbone while preparing future mechanisms.

## 3. Scope and Non-Goals

In scope: evidence definitions, source quality, applicability, audit workflow, safety reasoning, future dependencies, and a starter matrix for Intake, Feeder, Flywheel, and Elevator.

Out of scope: selecting final devices, inventing values, editing Java or configuration, assigning CAN IDs, defining ratios or limits, adding vendor libraries, creating mechanism APIs, tuning control loops, commissioning hardware, or activating M00_L03.

All examples are illustrative. They are not claims about the current robot.

## 4. Architecture Facts Versus Hardware Facts

An architecture fact says where a responsibility belongs. For example, repository governance establishes that a future Intake capability is owned through `IntakeSubsystem + IntakeIO`, and that vendor APIs belong inside a concrete IO adapter. This does **not** prove that a particular motor, controller, sensor, ratio, CAN ID, or physical assembly exists.

A hardware fact describes the actual robot or selected device. It needs evidence applicable to that exact robot, device, wiring, mounting, configuration, or test condition. Never use an architecture rule as proof of a physical value.

## 5. Frozen Backbone Relationship

The audit must preserve both frozen flows:

```text
CONTROL: Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware
OBSERVATION: hardware -> IOInputs -> subsystem / estimator -> immutable Observation -> telemetry -> NT4 / Glass / log
```

- `RobotContainer` remains the composition root only.
- Subsystems own mechanism behavior, state, and safe stop.
- IO interfaces remain vendor-neutral; concrete IO adapters isolate vendor APIs.
- `Constants.java` remains the default configuration authority.
- Commands request or coordinate subsystem behavior.
- Telemetry consumes immutable Observations and remains read-only.
- An evidence matrix informs later design; it does not bypass IO, move behavior into telemetry, or authorize code.

Evidence gathered in M00_L02 does not itself authorize adding or changing values in `Constants.java`. M00_L02 records evidence only. Any future `Constants.java` change requires the authorization and scope of the appropriate later lesson. Even when a hardware fact becomes `VERIFIED`, configuration code must not be changed in M00_L02.

The following are `VERIFIED` architecture-ownership facts for future lessons:

```text
Intake:   IntakeSubsystem + IntakeIO
Feeder:   FeederSubsystem + FeederIO
Flywheel: FlywheelSubsystem + FlywheelIO
Elevator: ElevatorSubsystem + ElevatorIO

Shooting coordination:
FlywheelSubsystem
+ FeederSubsystem
+ ShootCommand requiring both
```

There is no `ShooterSubsystem` and no `ShooterIO` in the locked M00 ownership design. These ownership facts do not prove that any physical motor exists, or establish a motor-controller model, CAN ID, sensor, wiring, ratio, inversion, current limit, or physical safe-stop behavior. Each physical fact still requires its own applicable evidence.

## 6. The Four Fact-Level Evidence States

Every fact receives exactly one state.

| State | Meaning | Minimum record |
| --- | --- | --- |
| `VERIFIED` | Identified acceptable evidence establishes the fact for the stated scope. | Exact source, applicability, and any scope limit. |
| `PROVISIONAL` | A real supporting basis exists, but an identified limitation prevents verification. | Basis, limitation, and unresolved verification action. |
| `UNKNOWN` | Acceptable evidence has not established the fact. | What is missing and how it could later be established. |
| `NOT APPLICABLE` | The category genuinely does not apply. | A specific rationale showing why it does not apply. |

Words such as *probably*, *likely*, *expected*, *common*, *typical*, *assumed*, or *convenient* do not make a fact `VERIFIED`.

## 7. Evidence Strength and Robot Applicability

Evidence strength and applicability are separate questions. A manufacturer datasheet may strongly establish a device's rated capability, but it does not establish that the device is installed on this robot. A clear wiring photo may establish a physical connection, but not the firmware version or applied configuration. A successful simulation can establish software behavior in the model, but not physical direction, CAN identity, friction, loading, temperature, or current margins.

Ask both:

1. Is the source trustworthy for this kind of fact?
2. Does it apply to this exact robot, device, revision, configuration, and test condition?

## 8. Evidence Source Discipline

Good sources are named precisely enough that another reviewer can find and inspect them. Examples include an approved ADR section, a device label photo tied to the robot, an electrical drawing revision, a purchase record with part number, a vendor manual/version, a configuration readback captured from the device, a measured tooth count, or a bounded commissioning record.

Weak entries such as “team knowledge,” “usual setup,” “looks right,” or an unlinked screenshot are not self-verifying. If no applicable source has been identified, write `No applicable evidence identified` and use `UNKNOWN`.

## 9. Audit Workflow

Use this sequence for every row:

```text
Question -> Evidence Search -> Applicability -> State -> Safety
         -> Verification Action -> Dependency -> Review
```

The process is iterative, but promotion is evidence-driven:

```text
UNKNOWN -> PROVISIONAL -> VERIFIED
```

A row may skip `PROVISIONAL` when decisive evidence directly establishes it. A row may also remain `UNKNOWN`. Review never promotes a fact merely because a later lesson wants the value.

## 10. Hardware Evidence Categories

The audit covers five connected categories:

- Mechanical: purpose, mounting, ratios, conversions, direction, and travel.
- Electrical: controller, bus, CAN identity, follower arrangement, and limits.
- Control-related: sensor meaning, phase, inversion, neutral behavior, and safe stop.
- Software compatibility: firmware/library compatibility, readback, and simulation support.
- Commissioning and safety: physical checks, bounded energization, emergency stop, and acceptance evidence.

## 11. Mechanical Evidence

Mechanical evidence should connect drawings or measurements to the assembled robot. Tooth counts, pulley diameters, drum circumference, chain stages, travel range, and mounting orientation affect unit conversion and direction. A catalog ratio is not automatically the total mechanism ratio. Record uncertainty rather than multiplying assumed values.

## 12. Electrical Evidence

Electrical evidence must identify the installed controller, motor arrangement, CAN bus, unique CAN ID, wiring, power path, and applicable limits. A software constant alone does not prove the physical device or wire is correct. Later commissioning should compare documentation, device discovery, labels, and configuration readback.

## 13. Control-Related Evidence

Control-related evidence defines what positive command and positive measurement mean, whether inversion is needed, whether sensor phase agrees with motion, how neutral behavior works, and what safe stop must do. These facts affect closed-loop stability and safe command ownership. They must be established before later feedback work.

## 14. Software Compatibility Evidence

Compatibility evidence binds exact device firmware, vendor library, WPILib version, API behavior, and configuration/readback support. “The library supports this family” is not enough when the installed model or firmware is unknown. Simulation support must also name what is modeled and what remains unproven physically.

## 15. Commissioning and Safety Evidence

Commissioning should move from power-off inspection to constrained, low-energy checks, with an emergency-stop plan and clear observers. Record the exact scope tested. A mechanism that moved once is not automatically verified for direction, limits, load, thermal behavior, repeated starts, fault response, or safe stop.

## 16. The PROVISIONAL Entry Rule

Use `PROVISIONAL` only when all three items exist:

1. a named supporting basis;
2. a named limitation that prevents `VERIFIED`; and
3. a concrete unresolved verification action.

Example: a purchase record identifies a Talon FX, but the robot label and device discovery have not been checked. That may support a provisional controller-family entry. Without the purchase record or another real basis, the correct state is `UNKNOWN`. Do not manufacture a provisional state to make the matrix look more complete.

## 17. Safety Relevance

Every row should answer, “What could go wrong if this fact is wrong?” Possible consequences include unexpected motion, opposing motors, unstable feedback, hard-stop impact, current or thermal damage, loss of braking, invalid position, unsafe restart, or an ineffective stop. Safety relevance does not prove the fact; it sets review priority and the caution level of the verification method.

## 18. Future Lesson Dependencies

- `M00_L03` depends on Intake evidence for the Intake Foundation.
- `M00_L05` depends on Feeder evidence for the Feeder Foundation.
- `M00_L07` depends on Flywheel evidence for the Flywheel Foundation.
- `M00_L10` depends on Elevator evidence for position-reference semantics.
- Later closed-loop and safety lessons depend on verified units, direction, sensors, limits, configuration behavior, and safe-stop expectations.

An unresolved row is not hidden. It becomes an explicit entry condition or deferred action for the future lesson.

## 19. Hardware Evidence Starter Matrix

Matrix rules:

- All physical or device-specific values below remain `UNKNOWN` because no applicable evidence was identified in this lesson.
- The eight `VERIFIED` rows establish architecture purpose or safe-stop ownership only. They do not prove hardware existence, installation, wiring, configuration, or physical behavior.
- No `PROVISIONAL` row is used because no row currently has both a real supporting basis and a documented limitation.
- No `NOT APPLICABLE` row is used because the current evidence does not establish that any listed category genuinely cannot apply.

| ID | Mechanism | Fact / Parameter | Current Value | Unit | Evidence State | Evidence Source | Verification Method | Safety Relevance | Notes / Unknowns | Future Lesson Dependency |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| INT-01 | Intake | Mechanism purpose | Independently owned Intake capability (architecture purpose only) | - | VERIFIED | M00 ADR, Locked 16-Lesson Roadmap, M00_L03 | Confirm ADR wording during review | Prevents ownership ambiguity | Does not prove hardware exists | M00_L03 |
| INT-02 | Intake | Motor/controller model | Not established | - | UNKNOWN | No applicable evidence identified | Inspect installed labels and records | Wrong API or ratings | Candidate not selected | M00_L03 |
| INT-03 | Intake | CAN bus | Not established | bus name | UNKNOWN | No applicable evidence identified | Inspect wiring and device discovery | Missed or conflicting device | Physical bus unknown | M00_L03 |
| INT-04 | Intake | CAN ID | Not established | integer | UNKNOWN | No applicable evidence identified | Device discovery plus label cross-check | ID collision or wrong output | Do not assign here | M00_L03 |
| INT-05 | Intake | Motor/follower arrangement | Not established | count/layout | UNKNOWN | No applicable evidence identified | Inspect assembly and electrical drawing | Motors may oppose or overload | Count and follower mode unknown | M00_L03 |
| INT-06 | Intake | Sensor type | Not established | - | UNKNOWN | No applicable evidence identified | Inspect assembly and part records | Invalid state or automation input | Presence itself unknown | M00_L03 and later sensing |
| INT-07 | Intake | Sensor mounting | Not established | location/orientation | UNKNOWN | No applicable evidence identified | Inspect and measure mounted geometry | False or unreachable detection | Mounting unknown | M00_L03 and later sensing |
| INT-08 | Intake | Mechanical ratio | Not established | ratio | UNKNOWN | No applicable evidence identified | Count stages, teeth, and pulleys | Wrong speed or torque model | Total ratio unknown | M00_L03 |
| INT-09 | Intake | Unit conversion | Not established | mechanism unit/motor unit | UNKNOWN | No applicable evidence identified | Derive from verified geometry and ratio | Misleading telemetry or control | Cannot derive before ratio | M00_L03 and later control |
| INT-10 | Intake | Positive direction | Not established | sign convention | UNKNOWN | No applicable evidence identified | Define then low-energy physical check | Unexpected intake/eject motion | Direction semantics unknown | M00_L03 |
| INT-11 | Intake | Motor inversion | Not established | boolean | UNKNOWN | No applicable evidence identified | Low-output check against positive direction | Unexpected or opposing motion | Depends on wiring/mounting | M00_L03 |
| INT-12 | Intake | Sensor direction/phase | Not established | sign/phase | UNKNOWN | No applicable evidence identified | Compare measurement with hand/low-energy motion | Unstable feedback or false state | Sensor unknown | Later sensing/control |
| INT-13 | Intake | Neutral behavior | Not established | brake/coast | UNKNOWN | No applicable evidence identified | Review requirement and bounded stop test | Roll-on or abrupt stop | Required mode unknown | M00_L03 and safety |
| INT-14 | Intake | Current/output limit | Not established | A / V / percent | UNKNOWN | No applicable evidence identified | Use device/mechanism evidence then bounded test | Electrical, thermal, mechanical damage | No value authorized | M00_L03 and safety |
| INT-15 | Intake | Physical travel limit where applicable | Not established | mechanism unit | UNKNOWN | No applicable evidence identified | Inspect geometry and interference | Collision, pinch, or damage | Applicability not yet established | M00_L03 and safety |
| INT-16 | Intake | Firmware/library compatibility | Not established | versions | UNKNOWN | No applicable evidence identified | Match exact device, firmware, vendordep, WPILib | Missing or unsafe API behavior | Exact stack unknown | M00_L03 |
| INT-17 | Intake | Configuration readback capability | Not established | fields/status | UNKNOWN | No applicable evidence identified | Review exact API and bench readback | Silent configuration mismatch | Device/API unknown | M00_L03 and commissioning |
| INT-18 | Intake | Simulation support | Not established | model scope | UNKNOWN | No applicable evidence identified | Review library and project model support | False confidence from incomplete model | Physical facts remain unproven | M00_L03 |
| INT-19 | Intake | Safe-stop expectation | Subsystem/IO safe stop owns safe output (architecture expectation only) | - | VERIFIED | AGENTS.md IO Contract; M00 ADR Mechanism Ownership | Architecture review, then future implementation/test | Prevents retained unsafe output | Physical stopping behavior unverified | M00_L03 and later safety |
| INT-20 | Intake | Commissioning verification method | Not established | procedure | UNKNOWN | No applicable evidence identified | Write reviewed power-off and low-energy procedure | Uncontrolled first motion | Needs exact hardware and hazards | M00_L03 |
| FED-01 | Feeder | Mechanism purpose | Independently owned transport mechanism (architecture purpose only) | - | VERIFIED | M00 ADR, Locked 16-Lesson Roadmap, M00_L05 | Confirm ADR wording during review | Prevents ownership ambiguity | Does not prove hardware exists | M00_L05 |
| FED-02 | Feeder | Motor/controller model | Not established | - | UNKNOWN | No applicable evidence identified | Inspect installed labels and records | Wrong API or ratings | Candidate not selected | M00_L05 |
| FED-03 | Feeder | CAN bus | Not established | bus name | UNKNOWN | No applicable evidence identified | Inspect wiring and device discovery | Missed or conflicting device | Physical bus unknown | M00_L05 |
| FED-04 | Feeder | CAN ID | Not established | integer | UNKNOWN | No applicable evidence identified | Device discovery plus label cross-check | ID collision or wrong output | Do not assign here | M00_L05 |
| FED-05 | Feeder | Motor/follower arrangement | Not established | count/layout | UNKNOWN | No applicable evidence identified | Inspect assembly and electrical drawing | Motors may oppose or overload | Count and follower mode unknown | M00_L05 |
| FED-06 | Feeder | Sensor type | Not established | - | UNKNOWN | No applicable evidence identified | Inspect assembly and part records | Invalid staging information | Presence itself unknown | M00_L05 and later coordination |
| FED-07 | Feeder | Sensor mounting | Not established | location/orientation | UNKNOWN | No applicable evidence identified | Inspect and measure mounted geometry | False or unreachable detection | Mounting unknown | M00_L05 and later coordination |
| FED-08 | Feeder | Mechanical ratio | Not established | ratio | UNKNOWN | No applicable evidence identified | Count stages, teeth, and pulleys | Wrong transport speed/torque | Total ratio unknown | M00_L05 |
| FED-09 | Feeder | Unit conversion | Not established | mechanism unit/motor unit | UNKNOWN | No applicable evidence identified | Derive from verified geometry and ratio | Misleading telemetry or control | Cannot derive before ratio | M00_L05 and later control |
| FED-10 | Feeder | Positive direction | Not established | sign convention | UNKNOWN | No applicable evidence identified | Define then low-energy physical check | Feeds in unsafe/wrong direction | Direction semantics unknown | M00_L05 |
| FED-11 | Feeder | Motor inversion | Not established | boolean | UNKNOWN | No applicable evidence identified | Low-output check against positive direction | Unexpected or opposing motion | Depends on wiring/mounting | M00_L05 |
| FED-12 | Feeder | Sensor direction/phase | Not established | sign/phase | UNKNOWN | No applicable evidence identified | Compare measurement with hand/low-energy motion | Unstable feedback or false staging | Sensor unknown | Later sensing/control |
| FED-13 | Feeder | Neutral behavior | Not established | brake/coast | UNKNOWN | No applicable evidence identified | Review requirement and bounded stop test | Game piece continues moving | Required mode unknown | M00_L05 and safety |
| FED-14 | Feeder | Current/output limit | Not established | A / V / percent | UNKNOWN | No applicable evidence identified | Use device/mechanism evidence then bounded test | Jam damage or overheating | No value authorized | M00_L05 and safety |
| FED-15 | Feeder | Physical travel limit where applicable | Not established | mechanism unit | UNKNOWN | No applicable evidence identified | Inspect geometry and interference | Collision, pinch, or jam damage | Applicability not yet established | M00_L05 and safety |
| FED-16 | Feeder | Firmware/library compatibility | Not established | versions | UNKNOWN | No applicable evidence identified | Match exact device, firmware, vendordep, WPILib | Missing or unsafe API behavior | Exact stack unknown | M00_L05 |
| FED-17 | Feeder | Configuration readback capability | Not established | fields/status | UNKNOWN | No applicable evidence identified | Review exact API and bench readback | Silent configuration mismatch | Device/API unknown | M00_L05 and commissioning |
| FED-18 | Feeder | Simulation support | Not established | model scope | UNKNOWN | No applicable evidence identified | Review library and project model support | False confidence from incomplete model | Physical facts remain unproven | M00_L05 |
| FED-19 | Feeder | Safe-stop expectation | Subsystem/IO safe stop owns safe output (architecture expectation only) | - | VERIFIED | AGENTS.md IO Contract; M00 ADR Mechanism Ownership | Architecture review, then future implementation/test | Prevents retained feeding output | Physical stopping behavior unverified | M00_L05 and later safety |
| FED-20 | Feeder | Commissioning verification method | Not established | procedure | UNKNOWN | No applicable evidence identified | Write reviewed power-off and low-energy procedure | Uncontrolled first transport | Needs exact hardware and hazards | M00_L05 |
| FLY-01 | Flywheel | Mechanism purpose | Independently owned rotational-speed mechanism (architecture purpose only) | - | VERIFIED | M00 ADR, Locked 16-Lesson Roadmap, M00_L07 | Confirm ADR wording during review | Prevents ownership ambiguity | Does not prove hardware exists | M00_L07 |
| FLY-02 | Flywheel | Motor/controller model | Not established | - | UNKNOWN | No applicable evidence identified | Inspect installed labels and records | Wrong API or ratings | Candidate not selected | M00_L07 |
| FLY-03 | Flywheel | CAN bus | Not established | bus name | UNKNOWN | No applicable evidence identified | Inspect wiring and device discovery | Missed or conflicting device | Physical bus unknown | M00_L07 |
| FLY-04 | Flywheel | CAN ID | Not established | integer | UNKNOWN | No applicable evidence identified | Device discovery plus label cross-check | ID collision or wrong output | Do not assign here | M00_L07 |
| FLY-05 | Flywheel | Motor/follower arrangement | Not established | count/layout | UNKNOWN | No applicable evidence identified | Inspect assembly and electrical drawing | Motors may oppose at high energy | Count and follower mode unknown | M00_L07 |
| FLY-06 | Flywheel | Sensor type | Not established | - | UNKNOWN | No applicable evidence identified | Inspect assembly and part records | Invalid speed measurement | Presence/source unknown | M00_L07-L09 |
| FLY-07 | Flywheel | Sensor mounting | Not established | location/orientation | UNKNOWN | No applicable evidence identified | Inspect sensor and measured shaft relationship | Wrong measured speed | Mounting unknown | M00_L07-L09 |
| FLY-08 | Flywheel | Mechanical ratio | Not established | ratio | UNKNOWN | No applicable evidence identified | Count stages, teeth, and pulleys | Overspeed or wrong torque model | Total ratio unknown | M00_L07-L08 |
| FLY-09 | Flywheel | Unit conversion | Not established | rpm or rad/s per motor unit | UNKNOWN | No applicable evidence identified | Derive from verified sensor and ratio | Unsafe or false speed setpoint | Cannot derive before ratio | M00_L07-L09 |
| FLY-10 | Flywheel | Positive direction | Not established | sign convention | UNKNOWN | No applicable evidence identified | Define then guarded low-energy check | Unexpected projectile direction | Direction semantics unknown | M00_L07 |
| FLY-11 | Flywheel | Motor inversion | Not established | boolean | UNKNOWN | No applicable evidence identified | Guarded low-output check | Opposing wheels or unsafe motion | Depends on arrangement | M00_L07 |
| FLY-12 | Flywheel | Sensor direction/phase | Not established | sign/phase | UNKNOWN | No applicable evidence identified | Compare speed sign with guarded motion | Unstable closed-loop control | Measurement source unknown | M00_L08 |
| FLY-13 | Flywheel | Neutral behavior | Not established | brake/coast | UNKNOWN | No applicable evidence identified | Review requirement and spin-down test | Unexpected coast or harsh stop | Required mode unknown | M00_L07 and safety |
| FLY-14 | Flywheel | Current/output limit | Not established | A / V / percent | UNKNOWN | No applicable evidence identified | Use device/mechanism evidence then guarded test | High-energy, thermal, electrical damage | No value authorized | M00_L07-L08 and safety |
| FLY-15 | Flywheel | Physical travel limit where applicable | Not established | mechanism unit | UNKNOWN | No applicable evidence identified | Inspect all moving geometry and guards | Contact or projectile hazard | Applicability not yet established | M00_L07 and safety |
| FLY-16 | Flywheel | Firmware/library compatibility | Not established | versions | UNKNOWN | No applicable evidence identified | Match exact device, firmware, vendordep, WPILib | Incorrect control or signal behavior | Exact stack unknown | M00_L07-L08 |
| FLY-17 | Flywheel | Configuration readback capability | Not established | fields/status | UNKNOWN | No applicable evidence identified | Review exact API and bench readback | Silent gains/limits mismatch | Device/API unknown | M00_L07-L08 and commissioning |
| FLY-18 | Flywheel | Simulation support | Not established | model scope | UNKNOWN | No applicable evidence identified | Review library and project model support | False readiness confidence | Physical speed remains unproven | M00_L07-L09 |
| FLY-19 | Flywheel | Safe-stop expectation | Subsystem/IO safe stop owns safe output (architecture expectation only) | - | VERIFIED | AGENTS.md IO Contract; M00 ADR Mechanism Ownership | Architecture review, then future implementation/test | Prevents retained high-energy output | Physical spin-down unverified | M00_L07 and later safety |
| FLY-20 | Flywheel | Commissioning verification method | Not established | procedure | UNKNOWN | No applicable evidence identified | Write guarded, exclusion-zone, low-energy procedure | High-energy first-motion hazard | Needs exact hardware and containment | M00_L07 |
| ELV-01 | Elevator | Mechanism purpose | Independently owned position mechanism with explicit reference meaning (architecture purpose only) | - | VERIFIED | M00 ADR, Locked 16-Lesson Roadmap, M00_L10 | Confirm ADR wording during review | Prevents ownership/reference ambiguity | Does not prove hardware exists | M00_L10 |
| ELV-02 | Elevator | Motor/controller model | Not established | - | UNKNOWN | No applicable evidence identified | Inspect installed labels and records | Wrong API, load, or ratings | Candidate not selected | M00_L10 |
| ELV-03 | Elevator | CAN bus | Not established | bus name | UNKNOWN | No applicable evidence identified | Inspect wiring and device discovery | Missed or conflicting device | Physical bus unknown | M00_L10 |
| ELV-04 | Elevator | CAN ID | Not established | integer | UNKNOWN | No applicable evidence identified | Device discovery plus label cross-check | ID collision or wrong output | Do not assign here | M00_L10 |
| ELV-05 | Elevator | Motor/follower arrangement | Not established | count/layout | UNKNOWN | No applicable evidence identified | Inspect assembly and electrical drawing | Motors may oppose or drop load | Count and follower mode unknown | M00_L10 |
| ELV-06 | Elevator | Sensor type | Not established | - | UNKNOWN | No applicable evidence identified | Inspect assembly and part records | Invalid position/reference | Relative/absolute unknown | M00_L10-L12 |
| ELV-07 | Elevator | Sensor mounting | Not established | location/orientation | UNKNOWN | No applicable evidence identified | Inspect and measure sensor coupling | Slip or wrong position | Mounting/coupling unknown | M00_L10-L12 |
| ELV-08 | Elevator | Mechanical ratio | Not established | ratio | UNKNOWN | No applicable evidence identified | Count stages, teeth, sprockets, drum | Wrong force, speed, or position | Total ratio unknown | M00_L10-L11 |
| ELV-09 | Elevator | Unit conversion | Not established | distance/motor unit | UNKNOWN | No applicable evidence identified | Derive from verified geometry and ratio | Wrong position or limit | Cannot derive before geometry | M00_L10-L13 |
| ELV-10 | Elevator | Positive direction | Not established | sign convention | UNKNOWN | No applicable evidence identified | Define then restrained low-energy check | Unexpected rise/drop | Direction semantics unknown | M00_L10 |
| ELV-11 | Elevator | Motor inversion | Not established | boolean | UNKNOWN | No applicable evidence identified | Restrained low-output check | Load drop or opposing motors | Depends on arrangement | M00_L10 |
| ELV-12 | Elevator | Sensor direction/phase | Not established | sign/phase | UNKNOWN | No applicable evidence identified | Compare position sign with restrained motion | Unstable feedback or invalid limits | Sensor unknown | M00_L10-L11 |
| ELV-13 | Elevator | Neutral behavior | Not established | brake/coast | UNKNOWN | No applicable evidence identified | Review load holding and bounded disable test | Gravity-driven motion | Required mode unknown | M00_L10 and safety |
| ELV-14 | Elevator | Current/output limit | Not established | A / V / percent | UNKNOWN | No applicable evidence identified | Use load/device evidence then restrained test | Drop, stall, thermal, structural damage | No value authorized | M00_L10-L13 and safety |
| ELV-15 | Elevator | Physical travel limit where applicable | Not established | distance | UNKNOWN | No applicable evidence identified | Measure hard/soft limits and clearances | Hard-stop collision or entrapment | Limits/reference unknown | M00_L10-L13 |
| ELV-16 | Elevator | Firmware/library compatibility | Not established | versions | UNKNOWN | No applicable evidence identified | Match exact device, firmware, vendordep, WPILib | Incorrect position/control behavior | Exact stack unknown | M00_L10-L11 |
| ELV-17 | Elevator | Configuration readback capability | Not established | fields/status | UNKNOWN | No applicable evidence identified | Review exact API and bench readback | Silent limits/feedback mismatch | Device/API unknown | M00_L10-L13 and commissioning |
| ELV-18 | Elevator | Simulation support | Not established | model scope | UNKNOWN | No applicable evidence identified | Review library and project model support | False confidence in gravity/limits | Physical load remains unproven | M00_L10-L13 |
| ELV-19 | Elevator | Safe-stop expectation | Subsystem/IO safe stop owns safe output (architecture expectation only) | - | VERIFIED | AGENTS.md IO Contract; M00 ADR Mechanism Ownership | Architecture review, then future implementation/test | Prevents retained motion command | Load-holding behavior unverified | M00_L10 and later safety |
| ELV-20 | Elevator | Commissioning verification method | Not established | procedure | UNKNOWN | No applicable evidence identified | Write restrained, supported-load, low-energy procedure | Drop, crush, or hard-stop hazard | Needs exact mechanics and hazards | M00_L10 |

## 20. Common Mistakes

- Treating an architecture owner as proof that hardware exists.
- Copying a candidate model or common team choice into “Current Value.”
- Marking a fact `VERIFIED` because code contains a constant.
- Using `PROVISIONAL` without a real supporting basis.
- Using `NOT APPLICABLE` merely because evidence is missing.
- Treating simulation as proof of wiring, direction, current, temperature, or physical limits.
- Omitting units, source identity, limitation, or verification method.
- Hiding an unknown because a later lesson needs the answer.
- Combining Intake, Feeder, and Flywheel into an unauthorized `ShooterSubsystem`.
- Turning the audit into implementation, commissioning, or final hardware selection.

## 21. Practical Exercise

The following six cases are hypothetical. For each one, record: (a) the state, (b) the reason, (c) evidence needed to upgrade it, (d) safety relevance, and (e) future lesson dependency.

1. A teammate says the Intake probably uses a 4:1 reduction, but no drawing, tooth count, or measurement is available.
2. A purchase record names a controller model, but nobody has matched it to the installed label.
3. A dated photo clearly shows a Feeder device label and robot location, and device discovery matches the same serial identity.
4. A Flywheel simulation reaches a target RPM, but the real motor, ratio, inertia, and loading are unknown.
5. An Elevator design has no follower motor by reviewed design, and the assembled mechanism matches that drawing.
6. A sensor-phase row is proposed as `NOT APPLICABLE`, but the sensor type itself is still unknown.

Suggested reasoning: Case 1 is `UNKNOWN`. Case 2 may be `PROVISIONAL` if the purchase record is applicable enough to be a genuine basis and the label check is the named limitation/action. Case 3 may be `VERIFIED` for the bounded identity claim. Case 4 verifies only modeled behavior, not real hardware facts. Case 5 may verify a single-motor arrangement; a “follower configuration” subfact can be `NOT APPLICABLE` only with the stated rationale. Case 6 remains `UNKNOWN` because applicability has not been established.

## 22. Knowledge Check

1. What is the difference between `VERIFIED` and `PROVISIONAL`?
2. If a value is only probable, typical, or commonly used and has no applicable evidence, which state must it receive?
3. What does `REAL HARDWARE DEFERRED` mean for M00_L02?
4. Does `REAL HARDWARE DEFERRED` allow a physical fact to be marked `VERIFIED` without evidence?
5. Why can Simulation not verify physical wiring, CAN identity, direction, gearing, current margin, or travel limits?
6. Why does M00_L02 implement no mechanism code?
7. Why does `Constants.java` remain the default configuration authority?
8. Does evidence gathered in M00_L02 authorize changing `Constants.java`?
9. Why does architecture ownership not prove physical hardware existence?
10. State the locked future ownership pair for Intake, Feeder, Flywheel, and Elevator.
11. State the locked shooting composition and the two prohibited umbrella types.
12. Does missing evidence justify `NOT APPLICABLE`?
13. Which component keeps vendor APIs?
14. Which four Foundation lessons directly depend on this audit method?
15. What is the difference between a fact-level state and a course-level evidence classification?

## 23. Knowledge Check Answers

1. `VERIFIED` has acceptable evidence that establishes the fact for its stated scope. `PROVISIONAL` has a real basis, but also a named limitation and an unresolved verification action.
2. `UNKNOWN`. Probability, familiarity, or a typical value is not verification and must not be guessed into the matrix.
3. Real-hardware verification is postponed; physical claims remain limited to the evidence currently available and must be verified later where required.
4. No. Deferred verification never turns an unsupported physical claim into a verified fact.
5. Simulation verifies only the stated model behavior. It cannot establish the actual robot's devices, wiring, mounting, configuration, loading, or physical response.
6. Its one new concept is the evidence-audit method. Mechanism implementation belongs to separately authorized later lessons.
7. The Frozen Backbone assigns default configuration authority to `Constants.java`; M00_L02 records evidence without changing that architecture.
8. No. A later lesson must separately authorize and scope any `Constants.java` change, even after a hardware fact becomes `VERIFIED`.
9. Architecture identifies responsibility and dependency boundaries. It does not prove that a motor, controller, sensor, wire, ratio, or other physical item exists.
10. Intake uses `IntakeSubsystem + IntakeIO`; Feeder uses `FeederSubsystem + FeederIO`; Flywheel uses `FlywheelSubsystem + FlywheelIO`; Elevator uses `ElevatorSubsystem + ElevatorIO`.
11. Shooting remains `FlywheelSubsystem + FeederSubsystem + ShootCommand requiring both`. There is no `ShooterSubsystem` and no `ShooterIO`.
12. No. Missing evidence means `UNKNOWN`; `NOT APPLICABLE` needs a rationale proving the category does not apply.
13. A concrete IO adapter, behind a vendor-neutral IO interface.
14. `M00_L03`, `M00_L05`, `M00_L07`, and `M00_L10`.
15. Fact-level states classify individual matrix claims. Course-level classifications report which lesson verification domains were required and completed.

## 24. Course-Level Evidence Classification

Do not confuse matrix states with lesson completion evidence.

| Course evidence domain | M00_L02 classification | Meaning |
| --- | --- | --- |
| Theory/repository evidence | `THEORY VERIFIED` | Required for this documentation and evidence-method lesson. |
| Focused new tests | `NOT APPLICABLE` | No test implementation is authorized or required. |
| Simulation | `NOT APPLICABLE` | Not a lesson-completion gate for this documentation-only concept. |
| Driver Station / Glass | `NOT APPLICABLE` | No runtime behavior is introduced. |
| Real hardware | `REAL HARDWARE DEFERRED` | Physical hardware facts remain bounded by available evidence and future verification. |

`THEORY VERIFIED` does not convert individual physical facts to `VERIFIED`. `REAL HARDWARE DEFERRED` does not excuse invented values.

## 25. Key Takeaways

- Record what the evidence proves, not what the team hopes is true.
- Use exactly one fact-level state per row.
- Keep unsupported facts `UNKNOWN`; use `PROVISIONAL` only with basis, limitation, and action.
- Use `NOT APPLICABLE` only when a rationale establishes non-applicability.
- Keep architecture facts separate from physical hardware facts.
- Preserve the Frozen Backbone and defer implementation to its authorized future lesson.
- Make unresolved evidence visible so future mechanism, closed-loop, and safety work begins honestly.
