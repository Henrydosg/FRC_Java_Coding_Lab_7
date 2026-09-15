# M00_L01 Lesson Plan and Lifecycle Record

## Current state

- **Lesson:** `M00_L01 - Mechanism Architecture Reuse`
- **Directory:** `M00_L01_MechanismArchitectureReuse`
- **Predecessor:** `V00_L09_SwervePoseEstimatorVisionFusion @ 5d36529`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Status:** `COMPLETE`
- **Active state:** `COMPLETE / FROZEN / READ-ONLY`
- **Freeze state:** `FROZEN / READ-ONLY`
- **Active lesson count:** `0`
- **Architecture / Inheritance Audit:** `PASS`
- **Baseline build:** `PASS / BUILD SUCCESSFUL IN 20s / 7 ACTIONABLE TASKS EXECUTED`
- **Design Lock:** `PASS_M00_L01_FINAL_DESIGN_LOCK`
- **Implementation authorization:** `NONE`
- **Production-code authorization:** `NONE`
- **Student documentation:** `COMPLETE / REVIEWED`
- **Initial documentation review:** `HOLD_M00_L01_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_CONSTANTS_AUTHORITY / RESOLVED`
- **Constants authority repair:** `PASS_M00_L01_CONSTANTS_AUTHORITY_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`
- **Independent documentation rereview:** `PASS_M00_L01_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD`
- **Architect documentation review:** `PASS_M00_L01_DOCUMENTATION_REVIEW_COMPLETE_READY_FOR_FINAL_BUILD`
- **Final inherited clean build / regression:** `PASS / BUILD SUCCESSFUL IN 23s / 7 ACTIONABLE TASKS EXECUTED`
- **Final closure review:** `PASS_M00_L01_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION`
- **Architect closure acceptance:** `PASS_M00_L01_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`
- **Documentation reconciliation:** `PASS_M00_L01_DOCUMENTATION_RECONCILED_READY_FOR_FREEZE_AUTHORIZATION`
- **Architect reconciliation acceptance:** `PASS_M00_L01_DOCUMENTATION_RECONCILIATION_ACCEPTED_READY_FOR_INDEPENDENT_REVIEW`
- **Independent reconciliation review:** `PASS_M00_L01_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`
- **Technical / content closure readiness:** `PASS`
- **Freeze authorization:** `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION`
- **Publication:** `NOT YET PUBLISHED / PENDING USER GIT`
- **Git commit:** `PENDING USER COMMIT`
- **Git push:** `PENDING USER PUSH`

## One-concept objective

Teach how the already-mastered drivetrain, autonomous, vision, IO,
Observation, telemetry, composition-root, and safe-stop architecture patterns
apply to future independently owned non-drivetrain competition mechanisms.

The sole new concept is:

```text
MECHANISM ARCHITECTURE REUSE
```

## Learning plan

1. Reuse subsystem ownership so each mechanism owns its behavior, state, and safe stop.
2. Reuse vendor-neutral IO and one-cycle IOInputs transport boundaries.
3. Reuse subsystem/estimator production of immutable, vendor-neutral Observations.
4. Reuse read-only telemetry that consumes Observations without controlling behavior.
5. Reuse RobotContainer only for construction, implementation selection, dependency injection, bindings, and telemetry composition.
6. Reuse safe-stop contracts without moving safety or hardware behavior into RobotContainer or telemetry.
7. Map the inherited drivetrain, vision, and autonomous boundaries onto future mechanisms without redesigning those inherited systems.
8. Explain why Intake, Feeder, Flywheel, and Elevator remain independently owned capabilities.
9. Explain why shooting is command-level coordination through `FlywheelSubsystem + FeederSubsystem + ShootCommand requiring both`.

## Preserved architecture

```text
hardware
-> IO implementation
-> mechanism IOInputs
-> subsystem / processing
-> immutable Observation
-> read-only telemetry
-> NT4 / Glass / log
```

- Vendor APIs remain inside concrete IO adapters.
- Subsystems depend on IO interfaces rather than concrete hardware.
- Constants.java remains the default configuration authority.
- RobotContainer remains the composition root only.
- Swerve, autonomous, PathPlanner, Vision, estimator, fusion, and alliance-transform ownership remain unchanged.
- V00_L09 remains complete, frozen, read-only, published, and verified.

## Future ownership map

| Capability | Subsystem owner | IO owner | M00_L01 implementation status |
| --- | --- | --- | --- |
| Intake | `IntakeSubsystem` | `IntakeIO` | Not implemented; later roadmap scope |
| Feeder | `FeederSubsystem` | `FeederIO` | Not implemented; later roadmap scope |
| Flywheel | `FlywheelSubsystem` | `FlywheelIO` | Not implemented; later roadmap scope |
| Elevator | `ElevatorSubsystem` | `ElevatorIO` | Not implemented; later roadmap scope |

No `ShooterSubsystem` or `ShooterIO` is authorized.

## Explicit exclusions through M00_L16

M00_L01 does not introduce the later roadmap concepts:

- M00_L02 mechanism hardware evidence classification;
- M00_L03 Intake implementation/Foundation;
- M00_L04 Intake command ownership;
- M00_L05 Feeder implementation/Foundation;
- M00_L06 Feeder command ownership;
- M00_L07 Flywheel implementation/Foundation;
- M00_L08 closed-loop flywheel velocity;
- M00_L09 ready-at-speed logic;
- M00_L10 Elevator implementation and position-reference semantics;
- M00_L11 Elevator closed-loop position;
- M00_L12 Elevator homing;
- M00_L13 Elevator travel-limit safety;
- M00_L14 shooting coordination;
- M00_L15 Intake-to-Feeder coordination; or
- M00_L16 mechanism autonomous-event integration.

It also excludes new mechanism hardware selection, new vendor APIs, inherited
system redesign, roadmap reorder, lesson merge/split, and M00_L17.

## Locked change boundary

- Production Java: `NONE`.
- Test code: `NONE`.
- Configuration: `NONE`.
- Vendordeps: `NONE`.
- Deploy assets: `NONE`.
- Runtime behavior: `NONE`.
- Mechanism hardware API: `NONE`.
- Documentation: paired bilingual learning guides plus lifecycle and transition records only.

No production implementation plan is authorized.

## Verification plan

- `THEORY VERIFIED` is established for governance, inheritance, architecture, Design Lock, bilingual documentation, the Constants repair, independent rereview, final inherited regression, and final closure review.
- Focused new tests are `NOT APPLICABLE`.
- New test implementation is `NONE`.
- Simulation is `NOT APPLICABLE`.
- Driver Station / Glass is `NOT APPLICABLE`.
- Real hardware is `NOT APPLICABLE`.
- The final inherited clean build/regression passed with `BUILD SUCCESSFUL in 23s`; 7 actionable tasks were executed; gate `PASS_M00_L01_FINAL_INHERITED_CLEAN_BUILD_REGRESSION`.
- Documentation review passed after the preserved Constants-authority HOLD, minimal repair, and independent rereview.
- Final architecture/documentation closure review passed at `PASS_M00_L01_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION`.
- Technical/content closure readiness is `PASS`; explicit Architect freeze authorization passed at `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION`.

No V00 runtime result is reused as M00_L01 runtime evidence.

## Documentation plan

- `V00_L09_to_M00_L01_Step_by_Step.md` is complete for the pre-freeze historical record.
- The authorized English and Vietnamese student learning guides are complete and reviewed.
- The guides retain identical structure, course/chapter identity, technical meaning, evidence classification, and architecture rules.
- English remains normative where governance requires it.
- The initial Constants-authority omission, minimal repair, and independent rereview PASS remain preserved as verification history.

## Closure sequence

1. Controlled documentation reconciliation: `COMPLETE` through the current bounded reconciliation.
2. Architect reconciliation acceptance: `COMPLETE` through `PASS_M00_L01_DOCUMENTATION_RECONCILIATION_ACCEPTED_READY_FOR_INDEPENDENT_REVIEW`.
3. Independent reconciliation review: `COMPLETE` through `PASS_M00_L01_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`.
4. Explicit Architect freeze authorization: `COMPLETE` through `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION`.
5. Lifecycle transition to `COMPLETE / FROZEN / READ-ONLY`: `COMPLETE`; active lesson count is `0`.
6. User-owned precise Git staging, commit, push, and remote verification: `PENDING`.
7. Publication metadata reconciliation if required: `PENDING`.

No technical implementation work remains. M00_L02 is not active and is not
created; remaining work is limited to the User-owned publication lifecycle.
