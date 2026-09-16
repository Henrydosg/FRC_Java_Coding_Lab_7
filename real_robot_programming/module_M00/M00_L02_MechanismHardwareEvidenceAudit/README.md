# M00_L02 - Mechanism Hardware Evidence Audit

## Lesson identity

- **Module:** `M00 - Competition Mechanism Foundations`
- **Lesson identity:** `M00_L02_MechanismHardwareEvidenceAudit`
- **Directory:** `M00_L02_MechanismHardwareEvidenceAudit`
- **Status:** `COMPLETE`
- **Active state:** `COMPLETE / FROZEN / READ-ONLY`
- **Freeze state:** `FROZEN / READ-ONLY`
- **Active lesson count:** `0`
- **Predecessor:** `M00_L01 - Mechanism Architecture Reuse`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Preparation:** `PASS`
- **Baseline build:** `PASS`
- **Architecture / Inheritance Audit:** `PASS`
- **Design Lock:** `PASS_M00_L02_FINAL_DESIGN_LOCK`
- **Production code authorization:** `NONE`
- **Test implementation authorization:** `NONE`
- **Configuration authorization:** `NONE`
- **Runtime behavior authorization:** `NONE`
- **Documentation implementation authorization:** `PASS_M00_L02_DOCUMENTATION_IMPLEMENTATION_AUTHORIZED`
- **Documentation implementation:** `COMPLETE`
- **Independent documentation rereview:** `PASS`
- **Final User build:** `PASS / BUILD SUCCESSFUL IN 33s / 7 ACTIONABLE TASKS EXECUTED`
- **Final closure review:** `PASS`
- **Documentation reconciliation:** `COMPLETE / RECORDED`
- **Independent reconciliation review:** `PASS`
- **Freeze authorization:** `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION`
- **COMPLETE / FROZEN / READ-ONLY:** `AUTHORIZED / RECORDED`
- **Primary publication commit:** `65a92a4a5806fd5134e0114e851c4e4cc093c58e`
- **Primary push:** `PASS`
- **Publication metadata:** `RECONCILED / COMPLETE`
- **Metadata commit / push / final remote verification:** `PENDING`
- **Final publication completion:** `PENDING`
- **M00_L03:** `NOT ACTIVE / NOT CREATED`

## Learning objective

This lesson is about distinguishing what is known from what is assumed when preparing future mechanism hardware work.

The sole new concept is:

```text
MECHANISM HARDWARE EVIDENCE AUDIT
```

M00_L02 teaches evidence discipline. It does not implement mechanism code, select final hardware, define mechanism constants, or add runtime behavior.

## Fact-level evidence states

Every audited fact receives exactly one state:

- `VERIFIED`: identified acceptable evidence establishes the fact.
- `PROVISIONAL`: a stated basis exists, but a limitation and unresolved verification action remain.
- `UNKNOWN`: acceptable evidence has not established the fact.
- `NOT APPLICABLE`: the category genuinely does not apply and the rationale is stated.

Words such as probably, likely, expected, common, typical, assumed, or convenient are not verification. No hardware value may be invented.

## Evidence classification

- Theory/repository evidence: `THEORY VERIFIED` required.
- Focused new tests: `NOT APPLICABLE`.
- Simulation: `NOT APPLICABLE` as a lesson-completion gate.
- Driver Station / Glass: `NOT APPLICABLE`.
- Real hardware: `REAL HARDWARE DEFERRED`.

`REAL HARDWARE DEFERRED` does not permit unsupported physical facts to be labeled `VERIFIED`.

## Architecture boundary

```text
hardware
-> IOInputs
-> subsystem / processing
-> immutable Observation
-> read-only telemetry
```

- RobotContainer remains the composition root only.
- Vendor APIs remain inside concrete IO adapters.
- Constants.java remains the default configuration authority.
- Subsystems own state, behavior, and safe stop.
- Commands request or coordinate behavior.
- Telemetry remains read-only.

Future conceptual ownership remains:

```text
IntakeSubsystem + IntakeIO
FeederSubsystem + FeederIO
FlywheelSubsystem + FlywheelIO
ElevatorSubsystem + ElevatorIO
```

Shooting remains `FlywheelSubsystem + FeederSubsystem + ShootCommand requiring both`. No `ShooterSubsystem` or `ShooterIO` is authorized.

## Preparation evidence

- Inherited baseline: `BUILD SUCCESSFUL in 58s`; 637 tests passed.
- Full comparison: 604 predecessor files and 604 candidate files; zero missing, extra, or different files.
- Protected comparison: 173 predecessor files and 173 candidate files; zero missing, extra, or different files.
- The accidental nested M00_L01 project was identified, removed through controlled repair, and independently verified absent.
- Architecture / Inheritance Audit: `PASS_M00_L02_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`.
- Final Design Lock: `PASS_M00_L02_FINAL_DESIGN_LOCK`.

## Documentation and review record

Separate documentation authorization was issued, and the English and Vietnamese student-facing guides were implemented with identical 25-section structure, lesson identity, technical meaning, architecture boundaries, evidence classifications, and equivalent evidence-matrix rows and states.

Each guide contains 15 knowledge-check questions, 15 answers, and an 80-row matrix containing 8 `VERIFIED`, 0 `PROVISIONAL`, and 72 `UNKNOWN` rows.

The initial independent review recorded `HOLD_M00_L02_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_REQUIRED_OWNERSHIP_CONSTANTS_AND_KNOWLEDGE_CHECK_COVERAGE` for three bounded documentation defects: the explicit `Constants.java` authorization boundary, the complete M00 ownership/shooting lock, and required knowledge-check coverage. The authorized two-guide repair resolved them, and the independent rereview passed. The User final build/regression and the final closure review also passed. Documentation reconciliation is complete and recorded at `PASS_M00_L02_DOCUMENTATION_RECONCILED_READY_FOR_INDEPENDENT_REVIEW`.

## Post-primary-publication state

M00_L02 is `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`. No
further technical or student-guide edits are authorized. The primary publication
is complete at `65a92a4a5806fd5134e0114e851c4e4cc093c58e`, and its push is
`PASS`. Publication metadata is reconciled. Evidence remains `THEORY VERIFIED`;
Simulation and Driver Station / Glass remain `NOT APPLICABLE`; real hardware
remains `REAL HARDWARE DEFERRED`. The separate metadata commit, metadata push,
final remote verification, and final publication completion remain pending.
M00_L03 remains `NOT ACTIVE / NOT CREATED`.
