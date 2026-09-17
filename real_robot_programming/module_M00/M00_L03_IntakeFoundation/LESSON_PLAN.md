# M00_L03 Lesson Plan and Lifecycle Record

## Current state

- **Lesson:** `M00_L03 - Intake Foundation`
- **Predecessor:** `M00_L02 - Mechanism Hardware Evidence Audit`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Status:** `COMPLETE / FROZEN / READ-ONLY`
- **Freeze state:** `FROZEN`
- **Active lesson count:** `0`
- **Implementation:** `COMPLETE FOR THE AUTHORIZED M00_L03 SCOPE`
- **Student documentation:** `IMPLEMENTED / VERIFIED AFTER BOUNDED REPAIR AND REREVIEW`
- **Final closure build:** `VERIFIED / BUILD SUCCESSFUL IN 42s / EXIT CODE 0`
- **Final closure review:** `PASS`
- **Lifecycle reconciliation:** `COMPLETE`
- **Independent reconciliation review:** `PASS`
- **Architect freeze authorization:** `PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION`
- **Publication:** `PENDING USER GIT`
- **M00_L04:** `NOT ACTIVE / NOT CREATED`

## Sole concept

```text
AN INDEPENDENTLY OWNED, VENDOR-NEUTRAL INTAKE MECHANISM FOUNDATION
```

M00_L03 establishes Intake as an independently owned mechanism capability
while preserving the Frozen Backbone. It does not claim that physical Intake
hardware has been selected, configured, commissioned, or verified.

## Completed authorized implementation

- vendor-neutral `IntakeIO` and owned input snapshot;
- deterministic `IntakeIONoop`;
- `IntakeSubsystem` ownership with `requestIntake()` and centralized `stop()`;
- requested states limited to `STOPPED` and `INTAKE_REQUESTED`;
- immutable, vendor-neutral `IntakeObservation`;
- read-only Intake telemetry consuming the immutable observation;
- RobotContainer composition only, with no Intake Commands or bindings; and
- focused Fake/Noop, subsystem, observation, telemetry, and composition tests.

`IntakeSubsystem.stop()` records `STOPPED` and calls `IntakeIO.stop()`. This
verifies the software request and IO-call contract only. It does not prove that
a physical mechanism stopped.

## Preserved boundaries

- No vendor-specific Intake adapter was added.
- `Constants.java` was unchanged.
- Unsupported motor, controller, CAN, inversion, gearing, current, output,
  sensor, direction, and PID facts remain `UNKNOWN`.
- RobotContainer remains the composition root and contains no Intake behavior.
- Telemetry remains read-only.
- M00_L04 owns Commands, requirements, bindings, interruption behavior, and
  default/manual mechanism ownership.

## Verification evidence

The initial focused run executed 16 tests: 15 passed and one failed in
`IntakeSubsystemTest.periodicBuildsFreshImmutableSnapshotsFromCurrentInputsAndIntent`
because an optional callback was dereferenced at line 69. The failure was
classified as a test defect. The bounded repair guarded the optional callback
in `IntakeSubsystemTest.java` and removed no assertions.

- Focused retest: `PASS / BUILD SUCCESSFUL in 4s / exit code 0`.
- Full clean regression: `PASS / BUILD SUCCESSFUL in 20s / exit code 0 / 7 of 7 actionable tasks executed`.
- Simulation: `PASS` for startup, Noop composition, subsystem integration, and
  Intake NetworkTables/telemetry presence while the robot remained Disabled.
- Independent implementation review: `PASS / ACCEPTED`.
- Initial independent documentation review: `HOLD` because transition-guide
  Step 16 described mutable `IntakeIOInputs` as immutable.
- Bounded documentation repair: `PASS`; Step 16 now says `mutable one-cycle
  input snapshot`.
- Independent documentation rereview: `PASS / ACCEPTED`.
- Final User closure build: `PASS / BUILD SUCCESSFUL in 42s / 7 of 7
  actionable tasks executed / exit code 0`.
- Final closure review: `PASS / ACCEPTED`.
- Lifecycle reconciliation: `COMPLETE / READY FOR INDEPENDENT RECONCILIATION
  REVIEW`.
- Driver Station / Glass: `NOT APPLICABLE` as completion gates.
- Real hardware: `REAL HARDWARE DEFERRED`.

Simulation is not proof of wiring, CAN identity, controller configuration,
motor direction, mechanism motion, current behavior, loading, force, or
physical stopping.

## Completed documentation work

- English guide: `docs/M00_L03_Intake_Foundation_Learning_Guide_EN.md`
- Vietnamese guide: `docs/M00_L03_Intake_Foundation_Learning_Guide_VI.md`
- Lifecycle/status records reconciled to accepted implementation evidence.
- Transition guide updated with implementation, test-repair, retest,
  regression, Simulation, documentation HOLD and repair, documentation
  rereview, final closure build, final closure review, and reconciliation
  history.
- Final course-level evidence is `THEORY VERIFIED`, `FOCUSED TESTS VERIFIED`,
  `FULL REGRESSION VERIFIED`, `FINAL CLOSURE BUILD VERIFIED`, `SIMULATION
  VERIFIED` for bounded software/Noop/composition only, Driver Station / Glass
  `NOT APPLICABLE`, and `REAL HARDWARE DEFERRED`.
- Independent reconciliation review passed.
- Architect freeze authorization passed at
  `PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION`.
- The lifecycle transition to `COMPLETE / FROZEN / READ-ONLY` is recorded,
  and the active lesson count is now `0`.

## Remaining publication-only sequence

1. User-owned primary Git staging.
2. User-owned primary Git commit.
3. User-owned primary Git push.
4. Remote alignment verification.
5. Publication metadata reconciliation if governance requires it.
6. User-owned metadata Git commit and push if required.
7. Final publication verification.
8. Final publication completion.

No new technical feature is authorized by these remaining steps. M00_L03 is
no longer editable. M00_L04 remains inactive and uncreated pending separate
future authorization.
