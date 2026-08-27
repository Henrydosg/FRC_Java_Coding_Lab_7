# V00_L01 - Documentation Reconciliation Checklist

Status: `COMPLETE / FROZEN / READ-ONLY`  
Predecessor: `A01_L09 @ 6b243bb - COMPLETE / FROZEN / READ-ONLY / PUBLISHED`  
Git: User-owned; Codex must not run Git commands

## Governance and Authority

- [x] AGENTS.md and repository README read.
- [x] All authoritative English Documents A, B, and C read.
- [x] Relevant A00, A01, A01_L08, S00 input-ownership, and V00 ADRs read.
- [x] Final A01_L09 metadata, transition guide, implementation record, and
      bilingual learning guides read.
- [x] Canonical V00_L01 source, tests, metadata, and inherited docs audited.
- [x] Historical V00_L01 docs treated only as stale historical evidence.
- [x] Protected V00_L02 status/docs inspected only as needed for protection.
- [x] Controlled V00_L01 documentation-only reconciliation authority confirmed.
- [x] A01 ends at L09; A01_L10 is prohibited.

## Authoritative Inheritance

- [x] Authoritative predecessor recorded as A01_L09 at `6b243bb`.
- [x] Historical V00_L01 classified as stale and non-authoritative.
- [x] Reconstruction from final A01_L09 recorded truthfully.
- [x] Isolated candidate, candidate verification, controlled canonical transfer,
      and canonical User verification recorded without invented commands.
- [x] 73 inherited production files are hash-identical to final A01_L09.
- [x] 56 inherited test files are hash-identical to final A01_L09.
- [x] Gradle, vendordeps, and deploy assets match final A01_L09.
- [x] Exactly one V00 production helper and one focused V00 test are additive.
- [x] Stale `frc.robot.observation.vision` helper/test paths are absent.
- [x] Stale `frc.robot.commands.AutonomousEventId` is absent.
- [x] Final `frc.robot.autonomous.AutonomousEventId` is present.

## Frozen Architecture

- [x] Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware preserved.
- [x] Hardware -> IOInputs -> subsystem/estimator -> immutable Observation -> telemetry preserved.
- [x] Frozen Interface Contract and vendor boundary preserved.
- [x] RobotContainer remains composition root only.
- [x] AutonomousPreparationCoordinator and PrepareAutonomousCommand preserved.
- [x] AutonomousPreparationObservation and its read-only telemetry facade preserved.
- [x] Scheduler-native AutoBuilder composition preserved.
- [x] Robot-level scheduler exception boundary and fatal-fault bridge preserved.
- [x] Terminal HOLDING and centralized Swerve stop ownership preserved.
- [x] Defensive Teleop-enabled output gate preserved.
- [x] Commands.defer(...) fresh events and NamedCommands/event markers preserved.
- [x] Manual child-command lifecycle delegation remains absent.

## V00_L01 Design Lock

- [x] Only field, robot, and camera frames are taught.
- [x] WPILib NWU, meters, radians, and right-handed rotations are explicit.
- [x] `robotToCamera` direction and inverse are explicit.
- [x] Composition order and reverse reconstruction are explicit.
- [x] Locked three-method API is unchanged.
- [x] Helper is stateless, non-instantiable, deterministic, and vendor-neutral.
- [x] Helper is correctly located in `frc.robot.vision`.
- [x] Helper is explicitly not an Observation, IO, subsystem, command,
      telemetry component, or hardware adapter.
- [x] Independent numeric oracle `(1, 2, 0) + yaw 90 deg + 1 m forward -> (1, 3, 0)` is taught.
- [x] VisionIO, vendors, hardware, AprilTag lookup, quality, timing, latency,
      fusion, autonomous, PathPlanner, Swerve, and calibration values remain deferred.

## Verification Evidence

- [x] Candidate independent mathematical oracle review PASS recorded.
- [x] Candidate focused Vision test PASS recorded.
- [x] Candidate full build PASS recorded.
- [x] Canonical Clean PASS / User verified under Java 17.
- [x] Canonical focused Vision test PASS / User verified.
- [x] Canonical full build PASS / User verified.
- [x] Accidental `-Recurse` artifact ABSENT / User verified.
- [x] Simulation classified NOT APPLICABLE with design-scope reason.
- [x] Driver Station / Glass classified NOT APPLICABLE with design-scope reason.
- [x] Real Robot classified NOT APPLICABLE with design-scope reason.

## Documentation Classification and Reconciliation

- [x] Category A required inherited learning/history preserved.
- [x] Category B required transition evidence preserved.
- [x] Category C copied lesson metadata reconciled in README, status, plan, and checklist.
- [x] Category D unnecessary duplicates: none identified.
- [x] All 61 inherited A01 docs remain unchanged.
- [x] `docs/A01_L09_to_V00_L01_Step_by_Step.md` created.
- [x] English V00_L01 learning guide created.
- [x] Vietnamese V00_L01 learning guide created.
- [x] Historical stale V00_L01 docs were not blindly restored.

## Scope Protection

- [x] Production Java changed by this reconciliation: NONE.
- [x] Test Java changed by this reconciliation: NONE.
- [x] Gradle, vendordeps, and PathPlanner assets changed: NONE.
- [x] A01 changed: NONE.
- [x] Root AGENTS.md and README stale V00_L01 lifecycle metadata reconciled
      under separate explicit authorization; no governance rule changed.
- [x] V00_L02 was later reconciled as COMPLETE / FROZEN / READ-ONLY / PUBLISHED
      at `53e9b9f`; it was not modified by the V00_L01 closure.
- [x] Git commands performed by Codex: NONE.

## Final Closure Gates

- [x] Documentation reconciliation complete.
- [x] Separate Final Architecture Review PASS.
- [x] Separate Final Closure Review / freeze authorization PASS.
- [x] Change V00_L01 to COMPLETE / FROZEN / READ-ONLY under that authorization.
- [x] User Git add/commit/push confirmed at `7d52ebf`.
- [x] Later V00_L02 publication reconciliation was separately authorized after
      V00_L01 freeze and completed at `53e9b9f`.

V00_L01 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 7d52ebf`. This closure
did not authorize work in V00_L02; V00_L02 was later completed and published
under separate authorization.
