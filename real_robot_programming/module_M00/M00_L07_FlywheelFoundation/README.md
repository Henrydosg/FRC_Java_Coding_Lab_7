# M00_L07 — Flywheel Foundation

M00_L07 is `COMPLETE / FROZEN / READ-ONLY`. It introduces exactly one
concept: Flywheel is one independently owned rotational-speed mechanism.

## Current lifecycle

- Previous lesson: `M00_L06 - Feeder Command Ownership`
- Previous lesson state: `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- Preparation and inherited baseline: `PASS`
- Architecture / Inheritance Audit: `PASS_M00_L07_ARCHITECTURE_INHERITANCE_AUDIT`
- Final Design Lock: `PASS_M00_L07_FINAL_DESIGN_LOCK`
- Controlled Activation: `PASS_M00_L07_CONTROLLED_ACTIVATION`
- Governance Adjudication: `PASS_M00_L07_GOVERNANCE_ADJUDICATION`
- Independent Activation Review: `PASS_M00_L07_INDEPENDENT_ACTIVATION_REVIEW_AFTER_ADJUDICATION`
- Implementation: `COMPLETE / PASS_M00_L07_IMPLEMENTATION_REPORT_ACCEPTED`
- Final Independent Static Rereview: `PASS_M00_L07_FINAL_INDEPENDENT_STATIC_REREVIEW`
- Focused Tests: `PASS / BUILD SUCCESSFUL`
- Clean Regression: `PASS / BUILD SUCCESSFUL IN 36s / 7 OF 7 ACTIONABLE TASKS EXECUTED`
- Bounded Simulation: `PASS_M00_L07_BOUNDED_SIMULATION`
- Evidence: `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`
- Documentation Reconciliation: `COMPLETE / ACCEPTED`
- Independent Closure Review: `PASS / PASS_M00_L07_INDEPENDENT_CLOSURE_REVIEW`
- Freeze: `COMPLETE / FROZEN / READ-ONLY`
- Publication: `PENDING / NOT YET PUBLISHED`
- Active lesson count: `0`
- Current active M00 lesson: `NONE`
- M00_L08: `INACTIVE / NOT CREATED`

The controlled freeze transition protects M00_L07 as a completed snapshot.
Publication remains a separate User-owned gate; no commit, push, remote
alignment, or publication verification is claimed.

## Implemented foundation

The accepted implementation provides vendor-neutral `FlywheelIO` and
`FlywheelIOInputs`, `FlywheelIONoop`, immutable `FlywheelObservation`,
`FlywheelSubsystem`, read-only `FlywheelTelemetryFacade`, and bounded
composition through `RobotContainer` and `RobotTelemetry`.

The IO input snapshot contains `available`, `connected`, `velocityValid`, and
`velocityRpm`. Software intent is exactly `STOPPED` or `SPIN_REQUESTED`.
`FlywheelIONoop` is the only runtime adapter. There is no physical Flywheel
adapter or operational CAN assignment; CAN 50–54 remains planning-only.

## Verification and interpretation

The six focused test classes passed. The full inherited regression and M00_L07
tests passed in the accepted clean build. Bounded Simulation passed across
Disabled initial state, Teleop Enabled idle with no Flywheel controller action,
and return to Disabled. Every checkpoint reported unavailable, disconnected,
invalid velocity, `velocityRpm = 0.0`, and `STOPPED`.

Because `velocityValid = false`, the reported `0.0` RPM is the canonical
invalid `FlywheelIONoop` representation. It is not a verified physical
zero-speed measurement. Simulation verifies composition, observation
transport, telemetry publication, Noop semantics, stopped software intent, no
automatic Teleop request, and safe Disabled → Teleop → Disabled persistence.
It does not verify any physical motor, sensor, CAN, direction, RPM accuracy,
ratio, current limit, physical stop, closed-loop velocity, or ready-at-speed
behavior.

## Protected future scope

- M00_L08 retains target RPM, closed-loop velocity, regulation, PID/PIDF,
  feedforward, convergence, and measured-versus-target error.
- M00_L09 retains at-speed/readiness tolerance, dwell, debounce, and policy.
- Flywheel command ownership, Feeder/Flywheel coordination, automatic firing or
  staging, NamedCommands, and autonomous mechanism integration remain later
  roadmap scope.
- M00_L14 retains Shoot Coordination and M00_L16 retains autonomous mechanism
  event integration.

## Lesson records

- [M00_L06 to M00_L07 transition guide](docs/M00_L06_to_M00_L07_Step_by_Step.md)
- [Lesson status](LESSON_STATUS.md)
- [Lesson plan](LESSON_PLAN.md)
- [Lesson checklist](LESSON_CHECKLIST.md)
