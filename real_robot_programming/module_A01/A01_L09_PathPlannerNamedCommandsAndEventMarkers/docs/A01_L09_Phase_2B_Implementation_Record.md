# A01_L09 Phase 2B Implementation Record

Date: 2026-08-26  
Parent: `A01_L08 @ 135272c`  
Status: `COMPLETE / FROZEN / READ-ONLY`

## Governance

Phase 2A is closed by user-authoritative evidence: the reconstructed final-L08
baseline passed `compileJava`, `compileTestJava`, the full inherited test suite,
and clean build. Phase 2B is authorized to implement only PathPlanner
NamedCommands and event markers. A01_L08 remains frozen and V00_L02 remains
suspended and read-only.

The original Phase 2B verification checkpoint recorded nine failures. Forensic
classification identified four DriverStationSim Autonomous-enable fixture
defects and five NamedCommands static-state contamination defects. The
authorized repairs were test-only: the fixtures establish the intended
Autonomous mode and clear the static registry between tests. The production
duplicate-registration guard and event architecture were not changed.

## Implemented Boundary

The implementation adds the neutral event ID
`frc.robot.autonomous.AutonomousEventId`, immutable event observation, binding
validation, scheduler-native `Commands.defer(...)` registration, a fresh
deterministic demonstration command, read-only event telemetry, and one
`ONE_METER_WITH_EVENT` routine. The existing event path remains unchanged with
one `LEARNING_EVENT` marker at relative position `0.5`.

The event command requires no subsystem. Observation does not depend on
commands. Telemetry consumes observations only. `PrepareAutonomousCommand` is
exactly inherited, `SafeAutoBuilderCommand` is absent, and no manual child
lifecycle delegation exists.

The final-L08 Robot scheduler exception boundary, coordinator fatal latch and
first-fault semantics, centralized Swerve stop, terminal HOLDING, SAFE_STOP,
Teleop-enabled output gate, and no-automatic-restart contract remain inherited.

## Changed-File Audit

Compared with final L08, the L09 source boundary contains exactly:

- nine authorized production merge files;
- six new production event files;
- six authorized shared-test merge files; and
- four new event tests.

No Gradle, vendordep, Swerve, IO, CTRE, CANcoder, PID/feedforward, gyro,
PathPlanner asset, or V00_L02 file was changed by this implementation. `.Glass`
is operator-view configuration outside the production/test architecture
boundary.

## Historical Intermediate Verification Checkpoint

The initial Phase 2B checkpoint passed `compileJava` but encountered the
documented Windows Gradle/Javac classpath hold during `compileTestJava`. The
remaining focused tests, inherited regressions, full suite, clean build, and
user-owned runtime gates were not claimed at that intermediate checkpoint.
This record is preserved as history and is not the current lesson result.

## Historical Closure Verification Record

The closure-era documentation recorded `compileJava`, `compileTestJava`, the
focused L09 event/path/routine/integration tests, 384 unchanged inherited
regression tests, the full `446/446` suite, and an isolated clean build as
PASS. This statement is retained as historical evidence. A later reverse audit
proved that `446/446` did not describe the present source/test snapshot, so it
is not the current verification count.

User-owned runtime evidence also passed Simulation, Driver Station / Glass,
and Real Robot verification for SAFE_STOP, `ONE_METER_PATH`,
`ONE_METER_WITH_EVENT`, LEARNING_EVENT behavior, terminal HOLDING, no automatic
restart, Disabled-to-Teleop recovery, marker preservation, event/path
coexistence, and both Blue and Red alliances.

## Current-Snapshot Re-verification - 2026-08-31

The User independently reran the present A01_L09 snapshot under Java 17:

- `gradlew clean`: exit `0`, `BUILD SUCCESSFUL`;
- `gradlew test --rerun-tasks`: exit `0`, `BUILD SUCCESSFUL`;
- final JUnit XML: `460` tests, `0` failures, `0` errors, `0` skipped; and
- `gradlew clean build`: exit `0`, `BUILD SUCCESSFUL`.

Therefore `A01_L09 RE-VERIFICATION: PASS`, and `460/460 PASS` supersedes the
historical `446/446` statement for the current snapshot. No production source,
test source, build configuration, dependency, or asset repair was required.

## Final Closure Gate

Technical verification, documentation reconciliation, the transition guide,
final architecture review, and final closure review are PASS. A01_L09 is now
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED`. User-owned publication is verified
at `6b243bb` (`Complete reconstructed A01_L09 named commands and event markers`).
