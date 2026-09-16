# M00_L02 Lesson Plan and Lifecycle Record

## Current state

- **Lesson:** `M00_L02 - Mechanism Hardware Evidence Audit`
- **Directory:** `M00_L02_MechanismHardwareEvidenceAudit`
- **Predecessor:** `M00_L01 - Mechanism Architecture Reuse`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Status:** `COMPLETE`
- **Active state:** `COMPLETE / FROZEN / READ-ONLY`
- **Freeze state:** `FROZEN / READ-ONLY`
- **Active lesson count:** `0`
- **Preparation:** `PASS`
- **Baseline build:** `PASS / BUILD SUCCESSFUL IN 58s / 637 TESTS PASS`
- **Architecture / Inheritance Audit:** `PASS`
- **Design Lock:** `PASS_M00_L02_FINAL_DESIGN_LOCK`
- **Production code authorization:** `NONE`
- **Test implementation authorization:** `NONE`
- **Configuration authorization:** `NONE`
- **Runtime behavior authorization:** `NONE`
- **Documentation implementation authorization:** `PASS_M00_L02_DOCUMENTATION_IMPLEMENTATION_AUTHORIZED`
- **Documentation implementation:** `COMPLETE`
- **Initial independent documentation review:** `HOLD / RESOLVED BY BOUNDED REPAIR / HOLD_M00_L02_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_REQUIRED_OWNERSHIP_CONSTANTS_AND_KNOWLEDGE_CHECK_COVERAGE`
- **Minimal documentation repair:** `PASS`
- **Independent documentation rereview:** `PASS`
- **Final User build / regression:** `PASS / BUILD SUCCESSFUL IN 33s / 7 ACTIONABLE TASKS EXECUTED`
- **Final closure review:** `PASS`
- **Documentation reconciliation:** `COMPLETE / RECORDED / PASS_M00_L02_DOCUMENTATION_RECONCILED_READY_FOR_INDEPENDENT_REVIEW`
- **Independent reconciliation review:** `PASS`
- **Freeze authorization:** `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION`
- **COMPLETE / FROZEN / READ-ONLY:** `AUTHORIZED / RECORDED`
- **Primary Git publication:** `COMPLETE / PUSHED`
- **Primary publication commit:** `65a92a4a5806fd5134e0114e851c4e4cc093c58e`
- **Publication metadata reconciliation:** `COMPLETE`
- **Metadata Git commit / push:** `PENDING USER COMMIT / PENDING USER PUSH`
- **Final remote verification:** `PENDING`
- **Final publication confirmation:** `PENDING`
- **M00_L03:** `NOT ACTIVE / NOT CREATED`

## One-concept objective

Teach a disciplined method for deciding what is known, what is temporarily supported, what remains unknown, and what does not apply when preparing future mechanism hardware work.

```text
MECHANISM HARDWARE EVIDENCE AUDIT
```

This is a documentation and evidence-method lesson. It does not select final hardware and does not implement a mechanism.

## Locked fact-level semantics

| State | Required meaning |
| --- | --- |
| `VERIFIED` | An identified acceptable source establishes the fact. |
| `PROVISIONAL` | A stated supporting basis exists, but a limitation and unresolved verification action remain. |
| `UNKNOWN` | Acceptable evidence has not established the fact. |
| `NOT APPLICABLE` | The category genuinely does not apply and the reason is stated. |

Convenience, familiarity, probability, expectation, or a typical FRC value is not verification.

## Documentation implementation record

Documentation was separately authorized and completed. The paired English and Vietnamese guides cover:

1. evidence-state semantics;
2. hardware evidence categories;
3. source and citation discipline;
4. handling of unknown and provisional facts;
5. safety relevance;
6. dependency on future M00 lessons; and
7. a structured evidence matrix.

The paired guides must have identical structure, lesson identity, technical meaning, architecture boundaries, evidence classifications, and equivalent matrix rows/states.

Recommended matrix fields:

```text
Mechanism
Fact / Parameter
Current Value
Unit
Evidence State
Evidence Source
Verification Method
Safety Relevance
Notes / Unknowns
Future Lesson Dependency
```

Both guides contain 25 sections, 15 knowledge-check questions, 15 answers, and equivalent 80-row matrices with 8 `VERIFIED`, 0 `PROVISIONAL`, and 72 `UNKNOWN` rows. The initial independent review HOLD was resolved by the authorized minimal two-guide repair, and the independent rereview passed.

## Preserved architecture

```text
hardware
-> concrete IO adapter
-> vendor-neutral IO / IOInputs
-> subsystem / processing
-> immutable Observation
-> read-only telemetry
-> NT4 / Glass / log
```

- RobotContainer remains the composition root only.
- Constants.java remains the default configuration authority.
- Subsystems own mechanism state, behavior, and safe stop.
- Commands request or coordinate behavior.
- Telemetry remains read-only.
- Prior completed lessons remain read-only.

## Future ownership map

```text
IntakeSubsystem + IntakeIO
FeederSubsystem + FeederIO
FlywheelSubsystem + FlywheelIO
ElevatorSubsystem + ElevatorIO
```

Shooting remains `FlywheelSubsystem + FeederSubsystem + ShootCommand requiring both`. No `ShooterSubsystem` or `ShooterIO` is authorized.

## Locked change boundary

- Production Java: `NONE`.
- Tests: `NONE`.
- Configuration: `NONE`.
- Vendordeps: `NONE`.
- Deploy assets: `NONE`.
- Runtime behavior: `NONE`.
- Mechanism APIs: `NONE`.
- Documentation implementation: `COMPLETE / AUTHORIZED`.

## Verification plan

- Theory/repository evidence: `THEORY VERIFIED` required.
- Focused new tests: `NOT APPLICABLE`.
- Simulation completion gate: `NOT APPLICABLE`.
- Driver Station / Glass: `NOT APPLICABLE`.
- Real hardware: `REAL HARDWARE DEFERRED`.
- Final inherited regression/build: `PASS / BUILD SUCCESSFUL IN 33s`.
- Initial independent documentation review: `COMPLETE / HOLD FOUND / RESOLVED`.
- Independent documentation rereview: `PASS`.
- Final closure review: `PASS`.
- Documentation reconciliation: `COMPLETE / RECORDED`.
- Independent reconciliation review: `PASS`.
- Freeze authorization: `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION`.
- All authorized lesson work: `COMPLETE`.
- No technical implementation was introduced.
- Primary Git publication: `COMPLETE` at `65a92a4a5806fd5134e0114e851c4e4cc093c58e`.
- Publication metadata reconciliation: `COMPLETE`.
- Remaining work is User metadata staging/commit/push, remote verification, and final publication confirmation only.
- No technical work remains.

## Lifecycle sequence

1. Architecture / Inheritance Audit: `PASS`.
2. Architect Final Design Lock: `PASS_M00_L02_FINAL_DESIGN_LOCK`.
3. Controlled lifecycle activation: `COMPLETE` through this record.
4. Documentation implementation authorization: `PASS`.
5. EN/VI guides and evidence matrix: `COMPLETE`.
6. Initial independent documentation review: `COMPLETE / HOLD FOUND`.
7. Minimal documentation repair: `COMPLETE`.
8. Independent documentation rereview: `PASS`.
9. Final User clean build/regression: `PASS`.
10. Final closure review: `PASS`.
11. Documentation reconciliation: `COMPLETE / RECORDED`.
12. Independent reconciliation review: `PASS`.
13. Architect freeze authorization: `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION`.
14. `COMPLETE / FROZEN / READ-ONLY`: `COMPLETE`.
15. Active lesson count transition from `1` to `0`: `COMPLETE`.
16. Primary User Git publication: `COMPLETE / PUSHED` at `65a92a4a5806fd5134e0114e851c4e4cc093c58e`.
17. Publication metadata reconciliation: `COMPLETE`.
18. User metadata commit and push: `PENDING`.
19. Final remote verification and publication confirmation: `PENDING`.
