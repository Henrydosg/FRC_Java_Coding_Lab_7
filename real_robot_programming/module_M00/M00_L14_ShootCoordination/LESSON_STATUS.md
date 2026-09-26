# LESSON_STATUS — M00_L14 Shoot Coordination

## Current identity and frozen lifecycle — 2026-09-26

- Lesson: M00_L14 — Shoot Coordination
- Previous Lesson: M00_L13 — Elevator Travel-Limit Safety
- Previous Lesson State: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
- Previous primary SHA: 5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704
- Previous metadata SHA: 658d1e44c417763df3689b9b52e409161446c593
- Status: COMPLETE
- Active State: FROZEN / READ-ONLY
- Lifecycle: COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED
- Active Lesson Count: 0
- Current Active M00 Lesson: NONE
- Untouched-copy Baseline Build: PASS_M00_L14_UNTOUCHED_COPY_BASELINE_BUILD; 6 actionable tasks, 6 executed; BASELINE_BUILD_EXIT_CODE=0
- Build: PASS_M00_L14_USER_CLEAN_REGRESSION (accepted User evidence); `gradlew clean test`; BUILD SUCCESSFUL in 30s; 5 actionable tasks (5 executed); CLEAN_REGRESSION_EXIT_CODE=0
- Architecture / Inheritance Audit: PASS_M00_L14_ARCHITECTURE_INHERITANCE_AUDIT
- Final Design Lock: PASS_M00_L14_FINAL_DESIGN_LOCK
- Controlled Activation: PASS_M00_L14_CONTROLLED_ACTIVATION
- Independent Activation Review: PASS_M00_L14_INDEPENDENT_ACTIVATION_REREVIEW
- Implementation Authorization: PASS_M00_L14_IMPLEMENTATION_AUTHORIZATION
- Implementation Handoff: PASS_M00_L14_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW
- Implementation: COMPLETE; one ShootCommand.java added; existing production files unchanged
- Final Independent Static Review: PASS_M00_L14_FINAL_INDEPENDENT_STATIC_REVIEW
- Focused Tests: PASS_M00_L14_USER_FOCUSED_TESTS; `gradlew test --tests "*ShootCommandTest"`; BUILD SUCCESSFUL in 10s; 4 actionable tasks (3 executed, 1 up-to-date); FOCUSED_TEST_EXIT_CODE=0
- Clean Regression: PASS_M00_L14_USER_CLEAN_REGRESSION; `gradlew clean test`; BUILD SUCCESSFUL in 30s; 5 actionable tasks (5 executed); CLEAN_REGRESSION_EXIT_CODE=0
- Simulation: PASS_M00_L14_USER_BOUNDED_SIMULATION; Disabled startup (not enabled, DS attached, not E-stopped) -> Teleoperated enabled (DS attached, not E-stopped) -> Disabled (not enabled, DS attached, not E-stopped); mechanism requested states STOPPED; normal shutdown; LAST_NATIVE_EXIT_CODE=0
- Driver Station / Glass: WPILib Simulation mode and DS attachment states observed; separate Glass / AdvantageScope verification is not claimed
- Real Robot: DEFERRED
- Evidence: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
- Documentation Reconciliation: COMPLETE; documentation proof repair and Freeze Reconciliation recorded
- Transition Guide: PASS; docs/M00_L13_to_M00_L14_Step_by_Step.md finalized through Freeze Reconciliation
- Initial Independent Closure Review: HOLD_M00_L14_INDEPENDENT_CLOSURE_REVIEW_DOCUMENTATION_PROOF_RECONCILIATION_REQUIRED
- Documentation Proof Reconciliation: PASS_M00_L14_DOCUMENTATION_PROOF_RECONCILIATION
- Independent Closure Rereview: PASS_M00_L14_INDEPENDENT_CLOSURE_REREVIEW / INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION
- Freeze Reconciliation: COMPLETE
- Independent Freeze Review: PENDING
- Freeze: COMPLETE / FROZEN / READ-ONLY
- Publication: NOT PUBLISHED / USER-OWNED / PENDING
- Final Publication Verification: PENDING
- Git Commit: PENDING / USER-OWNED
- Git Push: PENDING / USER-OWNED
- Known Issues: Flywheel and Feeder runtime adapters are Noop. RobotContainer has no ShootCommand binding, so Simulation did not execute this command through a robot binding. Physical shooting values and behavior remain unverified.

## Accepted static-review chronology

The initial and subsequent independent static-review HOLDs identified
test-only architecture-guard defects. Each was repaired within the test file,
and the final independent static review passed. No production defect was found.
The accepted sequence is:

- HOLD_M00_L14_STATIC_REVIEW_TEST_GUARD_DEFECT -> PASS_M00_L14_TEST_GUARD_REPAIR
- HOLD_M00_L14_STATIC_REREVIEW_BRANCH_CONDITION_GUARD_DEFECT -> PASS_M00_L14_BRANCH_CONDITION_GUARD_REPAIR
- HOLD_M00_L14_STATIC_REREVIEW_DATAFLOW_MUTATION_GUARD_DEFECT -> PASS_M00_L14_DATAFLOW_MUTATION_GUARD_REPAIR
- HOLD_M00_L14_STATIC_REREVIEW_CONTROL_FLOW_GATE_GUARD_DEFECT -> PASS_M00_L14_CONTROL_FLOW_GUARD_REPAIR
- HOLD_M00_L14_STATIC_REREVIEW_DIRECT_OUTPUT_CALL_REACHABILITY_GUARD_DEFECT -> PASS_M00_L14_DIRECT_CALL_REACHABILITY_GUARD_REPAIR
- PASS_M00_L14_FINAL_INDEPENDENT_STATIC_REVIEW / INDEPENDENT_STATIC_REREVIEW_PASS_READY_FOR_USER_FOCUSED_TESTS

These HOLDs are classified as TEST_ONLY_ARCHITECTURE_GUARD_DEFECT; they are not
production defects or runtime behavior failures.

## Accepted closure and freeze chronology

The initial Independent Closure Review HOLD concerned documentation and
evidence wording only; it identified no production, architecture, runtime,
test, or Simulation defect. The bounded proof repair changed only
LESSON_STATUS.md, LESSON_PLAN.md, and LESSON_CHECKLIST.md. Independent Closure
Rereview passed and authorized readiness for documentation-only Freeze
Reconciliation. The latter is complete; Independent Freeze Review is pending.

## Locked one-concept contract

One scheduler-managed frc.robot.commands.ShootCommand coordinates Flywheel
ready-at-speed semantics and Feeder request semantics. It requires exactly
FlywheelSubsystem and FeederSubsystem. FlywheelObservation.readyAtSpeed() is
the sole readiness authority. Feed admission additionally requires Feeder
available and connected. Feeder requestedState() is software request state
only; no command-local feedingRequested authority is allowed.

The constructor uses non-null subsystems and a finite, positive caller-supplied
RPM; invalid numeric values throw IllegalArgumentException. It performs no
output or subsystem mutation. The value is semantic configuration, not an
authoritative real shooting RPM. Constants.java remains unchanged and no shot,
feed, spin-up, or Elevator timing/position values are introduced.

Successful initialization stops Feeder once, then requests Flywheel velocity
once. Normal execute uses transition-only Feeder output and never reissues the
Flywheel request. The command is hold-style (isFinished() == false). Terminal
end attempts both stops in Feeder-then-Flywheel order, even if Feeder stop
throws; if both throw, Feeder remains primary and Flywheel is suppressed. All
approved failure paths preserve the original RuntimeException, perform their
specified one-time cleanup, suppress cleanup failures on the primary failure,
and do not retry.

## Accepted verification scope

The focused test suite contains 18 @Test methods and passed with the command
and result recorded above. The accepted clean regression passed. The bounded
Simulation verified Disabled startup (Robot Disabled, not enabled, DS attached,
not E-stopped), Teleoperated enable (Robot Teleoperated, enabled, DS attached,
not E-stopped), and return to Disabled (not enabled, DS attached, not
E-stopped) with Feeder, Flywheel, and Intake requested states STOPPED. No
application crash or scheduler/runtime exception was observed, and normal
shutdown completed with LAST_NATIVE_EXIT_CODE=0. It supports startup,
mode-transition, scheduler/integration stability, and safe semantic state
claims only. RobotContainer remains unchanged and has no ShootCommand binding;
Simulation did not directly execute ShootCommand or demonstrate a physical
shot. The focused test directly invokes `end(true)` and verifies interrupted-end
cleanup method behavior; actual scheduler-driven ShootCommand cancellation was
not tested. Real hardware remains deferred.

## Locked boundaries and remaining gates

RobotContainer, Constants, existing subsystems, IO, Observations, telemetry,
vendor adapters, and deploy files remain unchanged. No ShootCommand driver
binding, Elevator or Intake requirement, vision aiming, shot-completion
authority, M00_L15 transfer coordination, or M00_L16 autonomous event
integration is included. M00_L15 remains FUTURE / INACTIVE / NOT CREATED and
owns Intake-to-Feeder Coordination. M00_L16 remains FUTURE / INACTIVE / NOT
CREATED and owns Mechanism Autonomous Event Integration.

M00_L14 is COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED. Active Lesson Count
is 0 and Current Active M00 Lesson is NONE. Independent Freeze Review is the
next gate. User-owned publication and Final Publication Verification remain
pending; M00_L15 and M00_L16 have not been activated.
