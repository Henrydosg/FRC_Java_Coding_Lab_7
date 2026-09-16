# LESSON_STATUS

## Identity

- **Module:** `M00 - Competition Mechanism Foundations`
- **Lesson:** `M00_L02_MechanismHardwareEvidenceAudit`
- **Title:** `M00_L02 - Mechanism Hardware Evidence Audit`
- **Directory:** `M00_L02_MechanismHardwareEvidenceAudit`
- **Previous Lesson:** `M00_L01 - Mechanism Architecture Reuse`
- **Previous Lesson Directory:** `M00_L01_MechanismArchitectureReuse`
- **Previous Lesson State:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Previous Lesson Primary Publication:** `83907ab`
- **Previous Lesson Metadata Publication:** `f523118`
- **Status:** `COMPLETE`
- **Active State:** `COMPLETE / FROZEN / READ-ONLY`
- **Freeze State:** `FROZEN / READ-ONLY`
- **Active Lesson Count:** `0`
- **Lesson Goal:** teach a disciplined method for distinguishing verified mechanism hardware facts from provisional, unknown, and not-applicable facts without implementing a mechanism

## Required status fields

- **Preparation:** `PASS / POST-REPAIR PREPARATION ACCEPTED`
- **Baseline Build:** `PASS / USER-SUPPLIED WPILIB JAVA 17 INHERITED BUILD`
- **Baseline Evidence:** `BUILD SUCCESSFUL in 58s / 7 ACTIONABLE TASKS: 6 EXECUTED, 1 UP-TO-DATE / 637 TESTS, 0 FAILURES, 0 ERRORS, 0 SKIPPED`
- **Inheritance Integrity:** `PASS / 604 OF 604 COMPARABLE FILES / 0 MISSING / 0 EXTRA / 0 SHA-256 DIFFERENCES`
- **Protected Integrity:** `PASS / 173 OF 173 FILES / 0 MISSING / 0 EXTRA / 0 SHA-256 DIFFERENCES`
- **Architecture / Inheritance Audit:** `PASS / PASS_M00_L02_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`
- **Design Lock:** `PASS / PASS_M00_L02_FINAL_DESIGN_LOCK`
- **Production Code Authorization:** `NONE`
- **Test Implementation Authorization:** `NONE`
- **Configuration Authorization:** `NONE`
- **Vendordep Authorization:** `NONE`
- **Deploy Authorization:** `NONE`
- **Runtime Behavior Authorization:** `NONE`
- **Mechanism API Authorization:** `NONE`
- **Documentation Implementation Authorization:** `PASS / PASS_M00_L02_DOCUMENTATION_IMPLEMENTATION_AUTHORIZED`
- **Focused New Tests:** `NOT APPLICABLE`
- **Simulation:** `NOT APPLICABLE`
- **Driver Station / Glass:** `NOT APPLICABLE`
- **Real Hardware:** `REAL HARDWARE DEFERRED`
- **Controlled Activation:** `PASS / PASS_M00_L02_CONTROLLED_ACTIVATION_ACCEPTED`
- **Transition Guide:** `PASS / FREEZE RECORDED / PUBLICATION STEPS PENDING`
- **English Learning Guide:** `COMPLETE / 25 SECTIONS / 15 QUESTIONS / 15 ANSWERS / 80 MATRIX ROWS`
- **Vietnamese Learning Guide:** `COMPLETE / 25 SECTIONS / 15 QUESTIONS / 15 ANSWERS / 80 MATRIX ROWS`
- **Evidence Matrix:** `COMPLETE / 8 VERIFIED / 0 PROVISIONAL / 72 UNKNOWN`
- **Initial Independent Documentation Review:** `HOLD / RESOLVED BY BOUNDED REPAIR / HOLD_M00_L02_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_REQUIRED_OWNERSHIP_CONSTANTS_AND_KNOWLEDGE_CHECK_COVERAGE`
- **Minimal Documentation Repair:** `PASS / PASS_M00_L02_MINIMAL_DOCUMENTATION_REPAIR_ACCEPTED_READY_FOR_INDEPENDENT_REREVIEW`
- **Independent Documentation Rereview:** `PASS / PASS_M00_L02_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD`
- **Build:** `PASS / FINAL USER BUILD REGRESSION / BUILD SUCCESSFUL IN 33s / 7 ACTIONABLE TASKS EXECUTED`
- **Final Closure Review:** `PASS / PASS_M00_L02_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION`
- **Documentation Reconciliation:** `PASS / PASS_M00_L02_DOCUMENTATION_RECONCILED_READY_FOR_INDEPENDENT_REVIEW`
- **Independent Reconciliation Review:** `PASS / PASS_M00_L02_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`
- **Freeze Authorization:** `PASS / PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION`
- **COMPLETE:** `AUTHORIZED / RECORDED`
- **FROZEN:** `AUTHORIZED / RECORDED`
- **READ-ONLY:** `AUTHORIZED / RECORDED`
- **Git Commit:** `PENDING USER COMMIT`
- **Git Push:** `PENDING USER PUSH`
- **Remote Verification:** `PENDING`
- **Publication Metadata Reconciliation:** `PENDING`
- **Publication:** `PENDING USER GIT PUBLICATION`
- **M00_L03:** `NOT ACTIVE / NOT CREATED`
- **Known Issues:** `REAL HARDWARE EVIDENCE IS DEFERRED; UNKNOWN AND PROVISIONAL FACTS MUST REMAIN EXPLICIT`

## Locked lesson boundary

The sole new concept is:

```text
MECHANISM HARDWARE EVIDENCE AUDIT
```

Every audited hardware fact must receive exactly one state:

- `VERIFIED`: supported by identified acceptable evidence.
- `PROVISIONAL`: temporary engineering value or belief with a stated basis, limitation, and unresolved verification action.
- `UNKNOWN`: acceptable evidence has not established the fact.
- `NOT APPLICABLE`: the fact category genuinely does not apply, with a stated rationale.

Words such as probably, likely, expected, common, typical, assumed, or convenient do not independently establish `VERIFIED`. No hardware value may be invented.

## Architecture and ownership protection

- RobotContainer remains the composition root only.
- Vendor APIs remain inside concrete IO adapters.
- Observation flow remains `hardware -> IOInputs -> subsystem / processing -> immutable Observation -> read-only telemetry`.
- Constants.java remains the default configuration authority.
- Safe-stop ownership remains in subsystem / IO architecture.
- Future ownership remains `IntakeSubsystem + IntakeIO`, `FeederSubsystem + FeederIO`, `FlywheelSubsystem + FlywheelIO`, and `ElevatorSubsystem + ElevatorIO`.
- Shooting remains `FlywheelSubsystem + FeederSubsystem + ShootCommand requiring both`.
- `ShooterSubsystem` and `ShooterIO` remain unauthorized.

## Evidence classification

- **THEORY / REPOSITORY:** `THEORY VERIFIED` is required for the evidence method, architecture boundaries, and documented repository evidence.
- **FOCUSED NEW TESTS:** `NOT APPLICABLE`.
- **SIMULATION:** `NOT APPLICABLE` as a lesson-completion gate.
- **DRIVER STATION / GLASS:** `NOT APPLICABLE`.
- **REAL HARDWARE:** `REAL HARDWARE DEFERRED`.

`REAL HARDWARE DEFERRED` does not permit an unsupported physical fact to be marked `VERIFIED`.

## Final pre-publication lifecycle gate

```text
STATUS: COMPLETE
ACTIVE STATE: COMPLETE / FROZEN / READ-ONLY
FREEZE STATE: FROZEN / READ-ONLY
ACTIVE LESSON COUNT: 0
DESIGN LOCK: PASS_M00_L02_FINAL_DESIGN_LOCK
PRODUCTION CODE AUTHORIZATION: NONE
TEST IMPLEMENTATION AUTHORIZATION: NONE
CONFIGURATION AUTHORIZATION: NONE
RUNTIME BEHAVIOR AUTHORIZATION: NONE
DOCUMENTATION IMPLEMENTATION AUTHORIZATION: PASS_M00_L02_DOCUMENTATION_IMPLEMENTATION_AUTHORIZED
DOCUMENTATION IMPLEMENTATION: COMPLETE
INDEPENDENT DOCUMENTATION REREVIEW: PASS
FINAL USER BUILD: PASS
FINAL CLOSURE REVIEW: PASS
DOCUMENTATION RECONCILIATION: PASS
INDEPENDENT RECONCILIATION REVIEW: PASS
FREEZE AUTHORIZATION: PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION
COMPLETE: AUTHORIZED / RECORDED
FROZEN: AUTHORIZED / RECORDED
READ-ONLY: AUTHORIZED / RECORDED
PUBLICATION: PENDING USER GIT PUBLICATION
GIT COMMIT: PENDING USER COMMIT
GIT PUSH: PENDING USER PUSH
REMOTE VERIFICATION: PENDING
PUBLICATION METADATA RECONCILIATION: PENDING
M00_L03: NOT ACTIVE / NOT CREATED
```
