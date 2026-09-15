# M00_L01 - Mechanism Architecture Reuse

## Lesson identity

- **Module:** `M00 - Competition Mechanism Foundations`
- **Lesson identity:** `M00_L01_MechanismArchitectureReuse`
- **Directory:** `M00_L01_MechanismArchitectureReuse`
- **Status:** `COMPLETE`
- **Active state:** `COMPLETE / FROZEN / READ-ONLY`
- **Freeze state:** `FROZEN / READ-ONLY`
- **Active lesson count:** `0`
- **Predecessor:** `V00_L09_SwervePoseEstimatorVisionFusion @ 5d36529`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Architecture / Inheritance Audit:** `PASS`
- **Baseline build:** `PASS`
- **Design Lock:** `PASS_M00_L01_FINAL_DESIGN_LOCK`
- **Implementation authorization:** `NONE`
- **Production-code authorization:** `NONE`
- **Student documentation:** `COMPLETE / REVIEWED`
- **Technical/content work:** `COMPLETE`
- **Technical/content closure readiness:** `PASS`
- **Freeze authorization:** `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION`
- **Publication:** `NOT YET PUBLISHED / PENDING USER GIT`
- **Git commit:** `PENDING USER COMMIT`
- **Git push:** `PENDING USER PUSH`

## Learning objective

Learn how the already-mastered drivetrain, autonomous, vision, IO,
Observation, telemetry, composition-root, and safe-stop patterns apply to
future independently owned non-drivetrain competition mechanisms.

The sole new concept is:

```text
MECHANISM ARCHITECTURE REUSE
```

## Reused architecture

Future mechanisms must reuse the established ownership and data flow:

```text
hardware
-> concrete mechanism IO adapter
-> vendor-neutral mechanism IO / IOInputs
-> mechanism subsystem / processing
-> immutable mechanism Observation
-> read-only telemetry
-> NT4 / Glass / log
```

- A subsystem owns its mechanism behavior, state, and safe stop.
- An IO interface defines capability and owns one mutable one-cycle IOInputs snapshot.
- Concrete IO adapters isolate vendor APIs and hardware access.
- A subsystem or approved processor produces immutable, vendor-neutral meaning.
- Telemetry consumes Observations and never controls behavior.
- RobotContainer creates, selects, injects, and binds components; it does not own mechanism logic.
- Constants.java remains the default configuration authority.

## Future independent mechanism owners

```text
IntakeSubsystem + IntakeIO
FeederSubsystem + FeederIO
FlywheelSubsystem + FlywheelIO
ElevatorSubsystem + ElevatorIO
```

Future shooting architecture remains:

```text
FlywheelSubsystem
+
FeederSubsystem
+
ShootCommand requiring both
```

No `ShooterSubsystem` or `ShooterIO` is authorized.

## Scope boundary

This lesson changes documentation and lifecycle state only. It does not add or
modify production Java, tests, configuration, dependencies, vendordeps, deploy
assets, runtime behavior, mechanism hardware APIs, or inherited robot
architecture.

M00_L02-L16 concepts remain excluded, including hardware-evidence auditing,
mechanism Foundations, manual ownership, closed-loop control, readiness,
position-reference semantics, homing, travel-limit safety, mechanism
coordination, and autonomous mechanism events.

Inherited Swerve, autonomous, PathPlanner, Vision, pose-estimator, fusion, and
alliance-transform ownership remain unchanged.

## Accepted evidence

- `PASS_V00_MODULE_FINAL_CLOSURE_CONFIRMED`
- `PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED`
- `PASS_M00_GOVERNANCE_PREPARATION_PUBLICATION_CONFIRMED`
- `PASS_M00_L01_PREPARATION_BASELINE_BUILD`
- `PASS_M00_L01_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_CONTROLLED_ACTIVATION`
- `PASS_M00_L01_FINAL_DESIGN_LOCK`
- `PASS_M00_L01_CONSTANTS_AUTHORITY_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`
- `PASS_M00_L01_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD`
- `PASS_M00_L01_DOCUMENTATION_REVIEW_COMPLETE_READY_FOR_FINAL_BUILD`
- `PASS_M00_L01_FINAL_INHERITED_CLEAN_BUILD_REGRESSION`
- `PASS_M00_L01_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION`
- `PASS_M00_L01_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`
- `PASS_M00_L01_DOCUMENTATION_RECONCILED_READY_FOR_FREEZE_AUTHORIZATION`
- `PASS_M00_L01_DOCUMENTATION_RECONCILIATION_ACCEPTED_READY_FOR_INDEPENDENT_REVIEW`
- `PASS_M00_L01_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`
- `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION`

The inherited baseline build reported:

```text
BUILD SUCCESSFUL in 20s
7 actionable tasks: 7 executed
```

The inheritance audit compared 601 non-generated files on each side and found
zero missing files, zero candidate-only files, and zero SHA-256 differences.

The distinct final inherited clean build/regression reported:

```text
BUILD SUCCESSFUL in 23s
7 actionable tasks: 7 executed
```

## Verification classification

- `THEORY VERIFIED`: governance, inheritance integrity, Frozen Backbone, M00-specific architecture, one-new-concept scope, Design Lock, bilingual documentation, Constants repair, independent rereview, final inherited regression, and final closure review.
- Simulation: `NOT APPLICABLE`.
- Driver Station / Glass: `NOT APPLICABLE`.
- Real hardware: `NOT APPLICABLE`.
- Focused new tests: `NOT APPLICABLE`.
- New test implementation: `NONE`.

The User-owned final inherited clean build/regression is `PASS`. No V00 runtime
result is reused as M00_L01 runtime evidence.

## Documentation rule

Any new student-facing M00 Markdown must exist as two separate files, English
and Vietnamese, with identical structure, course/chapter identity, technical
meaning, evidence classification, and architecture rules. English remains
normative where governance requires it.

The authorized English and Vietnamese student learning guides are complete and
reviewed. The initial independent review returned
`HOLD_M00_L01_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_CONSTANTS_AUTHORITY`.
A minimal two-guide repair added the missing `Constants.java` authority
statement without defining configuration values and passed at
`PASS_M00_L01_CONSTANTS_AUTHORITY_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`. The
independent rereview passed at
`PASS_M00_L01_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD`,
and the Architect documentation review passed at
`PASS_M00_L01_DOCUMENTATION_REVIEW_COMPLETE_READY_FOR_FINAL_BUILD`. The final
closure review and technical/content readiness are `PASS`.

## Current next step

M00_L01 is `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`.
Technical/content work is complete, evidence remains `THEORY VERIFIED`, and
runtime verification remains `NOT APPLICABLE`. Production implementation
remains unauthorized. M00_L02 is not active and is not created.

Only the User-owned publication lifecycle remains: precise Git staging, commit,
push, remote verification, and publication metadata reconciliation if required.
Publication is `NOT YET PUBLISHED / PENDING USER GIT`; Git commit is `PENDING
USER COMMIT`, Git push is `PENDING USER PUSH`, and remote verification is
`PENDING`.
