# M00_L03 - Intake Foundation

## Lesson identity

- **Status:** `COMPLETE / FROZEN / READ-ONLY`
- **Freeze state:** `FROZEN`
- **Active lesson count:** `0`
- **Predecessor:** `M00_L02 - Mechanism Hardware Evidence Audit`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Implementation:** `COMPLETE FOR THE AUTHORIZED M00_L03 SCOPE`
- **Student documentation:** `VERIFIED AFTER ONE BOUNDED TERMINOLOGY REPAIR AND REREVIEW`
- **Final closure build:** `VERIFIED / BUILD SUCCESSFUL IN 42s / EXIT CODE 0`
- **Final closure review:** `PASS`
- **Lifecycle reconciliation:** `COMPLETE`
- **Independent reconciliation review:** `PASS`
- **Architect freeze authorization:** `PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION`
- **Primary publication:** `COMPLETE`
- **Primary commit:** `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`
- **Primary remote alignment:** `PASS`
- **Publication metadata reconciliation:** `COMPLETE`
- **Metadata Git publication:** `PENDING USER GIT`
- **Final publication verification:** `PENDING`
- **M00_L04:** `NOT ACTIVE / NOT CREATED`

## Sole new concept

```text
AN INDEPENDENTLY OWNED, VENDOR-NEUTRAL INTAKE MECHANISM FOUNDATION
```

M00_L03 implements the Intake foundation through vendor-neutral `IntakeIO` and
its owned input snapshot, deterministic `IntakeIONoop`, `IntakeSubsystem`,
requested states `STOPPED` and `INTAKE_REQUESTED`, immutable
`IntakeObservation`, read-only telemetry, RobotContainer composition, and
focused deterministic tests.

This lesson is complete, frozen, read-only, and no longer editable. Its primary
publication is complete; metadata publication and final verification remain.

The software flow is:

```text
hardware / Noop
-> IntakeIO
-> IntakeIOInputs
-> IntakeSubsystem
-> immutable IntakeObservation
-> read-only telemetry
```

`IntakeSubsystem.stop()` records `STOPPED` and invokes `IntakeIO.stop()`. That
contract verifies software ownership and forwarding; it is not evidence that
physical hardware stopped.

## Verification record

The initial focused run executed 16 tests: 15 passed and one failed because
`IntakeSubsystemTest.periodicBuildsFreshImmutableSnapshotsFromCurrentInputsAndIntent`
dereferenced an optional callback at line 69. The failure was classified as a
test defect. The minimal repair guarded that callback in
`IntakeSubsystemTest.java` and removed no assertions.

- Focused retest: `PASS / BUILD SUCCESSFUL in 4s / exit code 0`.
- Full clean regression: `PASS / BUILD SUCCESSFUL in 20s / exit code 0 / 7 of 7 actionable tasks executed`.
- Simulation: `PASS` for startup, Noop composition, subsystem integration, and
  Intake NetworkTables/telemetry presence while Disabled.
- Independent implementation review: `PASS / ACCEPTED`.
- Initial independent documentation review: `HOLD` for the transition-guide
  Step 16 mutable/immutable terminology defect.
- Step 16 repair: `PASS`; the corrected wording is `mutable one-cycle input
  snapshot`.
- Independent documentation rereview: `PASS / ACCEPTED`.
- Final User closure build: `VERIFIED / BUILD SUCCESSFUL in 42s / 7 of 7
  actionable tasks executed / exit code 0`.
- Final closure review: `PASS / ACCEPTED`.
- Lifecycle reconciliation: `COMPLETE`.
- Driver Station / Glass: `NOT APPLICABLE` as completion gates.
- Real hardware: `REAL HARDWARE DEFERRED`.

Simulation did not verify wiring, CAN identity, controller model, motor
direction, physical motion, current, loading, force, or physical stopping.

## Explicit boundaries

- No vendor-specific Intake adapter was added.
- `Constants.java` was unchanged.
- Unsupported hardware facts remain `UNKNOWN`.
- RobotContainer remains composition-only.
- Telemetry consumes immutable observations and remains read-only.
- M00_L04 exclusively owns scheduler-managed Intake Commands, command
  requirements, bindings, interruption behavior, and default/manual ownership.

## Student learning guides

- [English learning guide](docs/M00_L03_Intake_Foundation_Learning_Guide_EN.md)
- [Vietnamese learning guide](docs/M00_L03_Intake_Foundation_Learning_Guide_VI.md)
- [M00_L02 to M00_L03 transition guide](docs/M00_L02_to_M00_L03_Step_by_Step.md)

The English and Vietnamese guides each contain 34 numbered sections, 15 review
questions, and 15 answers. They are verified after the bounded transition-guide
terminology repair and independent documentation rereview. The student guides
were not modified by this reconciliation.

## Publication metadata gate

M00_L03 is `COMPLETE / FROZEN / READ-ONLY`, with freeze state `FROZEN` and
active lesson count `0`. Technical implementation, automated verification,
full regression, final closure build, bounded Simulation, student
documentation, final closure review, lifecycle reconciliation, independent
reconciliation review, and Architect freeze authorization are complete or
verified. Real hardware remains `REAL HARDWARE DEFERRED`. The User-owned
primary publication is complete at
`3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`; primary remote alignment is
`PASS`, and publication metadata reconciliation is complete. Independent
metadata review, User-owned metadata Git publication, metadata remote
verification, and final publication completion remain pending. M00_L04 remains
`NOT ACTIVE / NOT CREATED`.
