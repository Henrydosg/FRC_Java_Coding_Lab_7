# ADR — V00_L07 Exceptional Reopen for Inherited Swerve Architecture and Robustness Integrity Repair

- Status: `APPROVED FOR GOVERNANCE/LIFECYCLE ONLY`
- Date: `2026-08-31`
- Decision owners: Architect and User
- Implementation at ADR issuance: `NOT AUTHORIZED` (historical)
- Current repair disposition: `R1/R2/R3 COMPLETE; FINAL CLOSURE REVIEW PASS; COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 4704cfc; USER VERIFIED`
- Change type: exceptional inherited-defect repair lifecycle

## 1. Status

This ADR authorizes the documentation-only exceptional reopen of the published
V00_L07 snapshot. It does not authorize production Java, test Java, Design Lock
implementation, or Git operations.

## 2. Context

V00_L07 — Vision Timestamp and Latency Contract is a valid completed vision
timing lesson. Post-freeze audit evidence found three inherited Swerve defects
in the current descendant lineage. The timing concept itself is not defective
and is not being redesigned.

## 3. Trigger Evidence

AUDIT-REV-03B confirmed:

- R1: a command depends on the IO-owned
  `SwerveModuleIO.StaticFrictionStopReason` type;
- R2: `SwerveSubsystem.stop()` is not exception-isolated across all module
  stop attempts; and
- R3: `physicalForwardSign = -1` produces asymmetric measured position and
  measured-velocity semantics.

These findings are inherited Swerve integrity defects, not V00_L07 timing
implementation defects.

The related governance gates `S00-REPAIR-GATE-01` and
`V00-L07-REOPEN-GOV-01` passed before this documentation-only transition and
are preserved as authorization provenance.

## 4. Authority

This ADR supplements, but does not replace, AGENTS.md, Documents A/B/C, the
Frozen Interface Contract, the approved V00 roadmap ADR, and the existing
A01_L08 exceptional-reopen precedent. English governance remains normative.

## 5. Architect Decision

The Architect selected V00_L07 as the exceptional repair location because it is
the latest authoritative published predecessor of V00_L08 and the narrowest
selected lineage correction point.

## 6. User Approval

The User explicitly approved: exceptional reopen of V00_L07 for exactly R1,
R2, and R3. This approval covers governance planning and the formally gated
repair lifecycle only.

## 7. Why V00_L07 Is the Repair Location

V00_L07 was published at `d58bef0` and contains the latest inherited Swerve
snapshot before the unactivated V00_L08 candidate. Reopening this point avoids
editing historical S00 or A01 lessons and gives future V00 inheritance one
corrected authoritative parent.

## 8. Why Historical S00 Is Not Reopened

Historical S00 lessons remain frozen. Reopening S00 would require a broader
downstream reconstruction across A00, A01, and V00 and is not authorized by
this ADR. The S00 lineage remains preserved historical evidence.

## 9. Why V00_L08 Is Not the Repair Workspace

V00_L08 owns real-camera adapter integration. Its current directory is an
unactivated inherited candidate, not an editable lesson. It must not combine
an unrelated Swerve repair with the single V00_L08 vision concept.

## 10. Exact Repair Scope R1/R2/R3

The exceptional repair unit contains exactly:

1. R1 — remove the command-layer dependency on the IO-owned
   `SwerveModuleIO.StaticFrictionStopReason` type.
2. R2 — make all-module stop fanout attempt the remaining module stops when one
   module stop throws an unchecked exception.
3. R3 — make supported `physicalForwardSign = -1` produce coherent physical-
   forward measured position and measured velocity semantics.

## 11. Explicit Exclusions

Excluded are live module-health actuation policy, transient CAN policy,
`setControl` redesign, nonfinite parity cleanup, general IO cleanup, telemetry
redesign, simulation-fidelity cleanup, tuning, calibration, CAN IDs, ratios,
wheel diameter, Driver Input, field-relative behavior, odometry, pose
estimation, autonomous, PathPlanner, vision, Limelight, AprilTag, pose fusion,
unrelated refactoring, and roadmap changes beyond this exceptional record.

The authoritative drive ratio remains `6.75:1`.

## 12. Frozen Backbone / Interface Impact

The Frozen Backbone, Frozen Interface Contract, observation flow, RobotContainer
composition-root role, telemetry read-only rule, and Swerve ownership remain
unchanged. Any interface ownership or public-type migration must pass a later
Design Lock and architecture review.

## 13. Maximum Candidate Production Boundary

The maximum inventory for later Design Lock review is:

- `SwerveFrontLeftDriveStaticFrictionCharacterizationCommand.java` — R1;
- `SwerveSubsystem.java` — R1/R2/R3;
- `SwerveModuleIO.java` — R1 review candidate;
- `SwerveModuleIOCTRE.java` — R1 review candidate; and
- `SwerveModuleIOSim.java` — R3 review candidate.

This list is not implementation authorization. The later Design Lock must
select the exact minimum subset.

## 14. Maximum Candidate Test Boundary

Later focused review may consider the existing characterization, IO-contract,
Swerve stop, module-position, measured-speed, simulation, odometry, and
integration tests. A new focused test may be authorized only if the Design
Lock establishes that it is necessary. No test file is authorized by this ADR.

## 15. Lifecycle Transition

The formal transition is:

```text
COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ d58bef0
    -> REOPENED / IN_PROGRESS / EDITABLE
```

After successful repair and fresh verification, the lesson may transition to
`COMPLETE / FROZEN / READ-ONLY` again. The historical d58bef0 publication is
retained; a later repair publication requires a new User-owned publication
identity.

## 16. One-Editable-Lesson Rule

During the reopen V00_L07 is the sole `IN_PROGRESS / EDITABLE` lesson. No
other lesson may be activated or edited as part of this ADR.

## 17. Downstream Protection

V00_L08 was never activated and is not formally suspended. It remains
`NOT STARTED / NOT ACTIVATED / NOT IMPLEMENTED / NON-AUTHORITATIVE / READ-ONLY`
throughout the repair. Its physical directory may remain present but must not
be modified or treated as an authoritative lesson.

## 18. Baseline Requirements

After the documentation-only reopen and before implementation, the User must
provide fresh Java 17, source/provenance, focused inherited-test, full-suite,
clean-build, and changed-file-isolation evidence. The historical 593-test
result is preserved but does not satisfy the reopened baseline.

## 19. Architecture Audit Requirement

Codex must perform a focused read-only audit after the baseline. Architect
review must confirm Frozen Backbone preservation, exact R1/R2/R3 scope, and no
unauthorized downstream or historical edits.

## 20. Design-Lock Requirement

The Architect must approve the exact ownership, contract, exception-reporting,
and physical-forward semantic decisions before implementation. This ADR does
not choose implementation mechanics.

## 21. Implementation Authorization Requirement

Production and test changes require separate explicit Architect/User
implementation authorization naming the exact files. Governance approval alone
does not authorize Java changes.

## 22. Verification Gates

The reopened repair requires focused tests, inherited regression, full suite,
clean build, changed-file audit, documentation reconciliation, final
architecture review, and explicit re-freeze approval. No historical PASS may be
reused as fresh reopened evidence.

## 23. Simulation / Real-Robot Requirements

User-owned Simulation and bounded real-robot verification are required where
applicable because the repair concerns production drivetrain safety and
measurement semantics. No unsafe fault injection on the real robot is implied.

## 24. Re-Freeze Requirements

Re-freeze requires all applicable implementation, test, build, Simulation,
real-robot, scope, documentation, and final-review gates to PASS, followed by
explicit Architect/User approval. Any failed or missing gate leaves V00_L07 on
HOLD and not frozen.

## 25. Publication Requirements

Git publication remains User-owned. Codex must not add, commit, push, or claim
publication. The original d58bef0 remains historical pre-repair evidence.

## 26. V00_L08 Reconstruction Requirement — HISTORICAL / SUPERSEDED PROSPECTIVELY

Before the later forensic comparison of the existing candidate, the authorized
procedure was to replace the current V00_L08 candidate with a fresh
copy/reconstruction from the corrected V00_L07 predecessor, followed by cleanup,
baseline build, Architecture Audit, Design Lock, and the independent Limelight
evidence gate. That was the correct plan for the evidence then available and is
retained as historical context. Section 33 now supersedes that prospective
replacement/reconstruction procedure only for the exact existing V00_L08
candidate named there; it creates no precedent for other lessons.

## 27. Existing Independent Limelight HOLD

The V00_L08 physical-evidence HOLD remains independent. `t6t_cs` rotation
semantics, camera model, LimelightOS version, frame identity, and timing still
require first-party/hardware confirmation. This repair does not resolve that
HOLD.

## 28. Rollback / HOLD Conditions

HOLD applies if scope expands, a frozen boundary is edited without approval,
another lesson becomes editable, baseline/build/verification fails, the
Limelight evidence remains unresolved for V00_L08, or the final review and
re-freeze approval are missing. The original frozen V00_L07 evidence remains
preserved in history.

## 29. Decision Summary at ADR Issuance

At ADR issuance, the V00_L07 exceptional reopen was approved for
governance/lifecycle documentation only. At that time R1/R2/R3 implementation
remained unauthorized. Roadmap identities and order remain unchanged, V00_L08
remains unactivated, and all Git operations remain User-owned.

## 30. Post-Repair Verification Reconciliation — 2026-08-31 (Historical robot-unavailable stage)

The preceding sections preserve the governance-only authorization state at the
time this ADR was issued. A later separate Architect/User implementation
authorization permitted exactly the R1/R2/R3 production and focused-test
boundary. That authorized implementation is complete and does not expand the
scope of this ADR.

Evidence at that reconciliation stage was:

- Fresh pre-repair reopened baseline: 593 tests, 0 failures, 0 errors, 0
  skipped; clean build PASS.
- Focused R1/R2/R3 verification: PASS.
- Inherited Swerve regressions: PASS.
- Post-repair full suite: 600 tests, 0 failures, 0 errors, 0 skipped.
- Post-repair clean build: PASS.
- Runtime WPILib Simulation: PASS.
- Post-implementation read-only architecture review: PASS.
- Frozen Backbone review: PASS.

R1 moved command-facing `StaticFrictionStopReason` ownership to
`SwerveSubsystem`, with private translation to the unchanged IO reason. R2
made `SwerveSubsystem.stop()` clear actuation state, attempt FL → FR → BL →
BR, preserve the first `RuntimeException`, suppress later exceptions in
encounter order, and rethrow the first after all attempts. CTRE drive/steer
module-local exception isolation was not part of the repair. R3 kept IO and
Observation values raw while applying the same `physicalForwardSign` semantic
to measured position and velocity in `SwerveSubsystem`; the current robot
configuration and `6.75:1` drive ratio remain unchanged, and CTRE/Sim
production IO remain unchanged.

Real-robot verification is **DEFERRED — ROBOT UNAVAILABLE**; it is neither
PASS nor FAIL. The applicable bounded hardware gate remains required before
re-freeze. R1 needs no additional hardware verification for type ownership
alone. The later R2 check must use a safe restrained or raised robot, bounded
operation, Disable/mode transition, normal-stop confirmation, and no
unintended restart, without fault injection. The later R3 check must confirm
positive measured physical-forward motion at low speed for the current
calibrated configuration without changing `physicalForwardSign` merely to
force a `-1` case.

V00_L07 therefore remains `REOPENED / IN_PROGRESS / EDITABLE`. Re-freeze and
repair publication are not authorized before the bounded hardware gate;
V00_L08 remains unactivated,
non-authoritative, read-only, and protected for fresh reconstruction from the
corrected published V00_L07. The independent Limelight physical-evidence HOLD
is unchanged.

## 31. Pre-Closure Hardware-Evidence Reconciliation — 2026-09-07

The preceding post-repair section preserves the earlier stage at which the
robot was unavailable and the bounded hardware gate was deferred. Later
User-supplied evidence verifies that the robot remains usable in Teleop and
Autonomous. No stronger bounded stop/Disable/no-unintended-restart claim is
added beyond the supplied evidence.

The separately observed BL quantitative drivetrain anomaly remains `KNOWN /
DEFERRED HARDWARE MAINTENANCE`. It is unresolved and is not BL PASS,
quantitative drivetrain PASS, matched-module evidence, completed tuning,
completed calibration, or issue resolution. The Architect/User disposition
classifies this maintenance condition as non-blocking for continued Vision
curriculum closure. This does not authorize diagnosis, tuning, calibration, or
another Swerve change.

The fresh 593/593 reopened baseline, focused R1/R2/R3 tests, inherited Swerve
regressions, post-repair 600/600 suite, clean build, runtime WPILib Simulation,
post-implementation architecture review, and Frozen Backbone review remain
PASS. Documentation evidence is now reconciled. V00_L07 remains `REOPENED /
IN_PROGRESS / EDITABLE` but is `CLOSURE-READY / PENDING FINAL READ-ONLY CLOSURE
REVIEW`. Explicit Architect/User re-freeze approval and User-owned corrected
repair publication remain pending. Historical `d58bef0` remains the pre-repair
publication and is not the corrected repair publication.

V00_L08 remains unactivated, non-authoritative, read-only, and untouched. Its
future reconstruction from the corrected published V00_L07 remains a separate
governed action.

## 32. Final Re-Freeze Closure — 2026-09-07

The final read-only closure review returned
`READY_FOR_EXPLICIT_L07_REFREEZE_AUTHORIZATION`. The Architect and User then
explicitly authorized the lifecycle transition. V00_L07 is now `COMPLETE /
FROZEN / READ-ONLY`; no V00 lesson is active.

The authorized R1/R2/R3 implementation, focused and inherited regressions,
600/600 full suite, clean build, runtime WPILib Simulation, final architecture
review, Frozen Backbone review, Frozen Interface Contract review, and
documentation reconciliation remain PASS. Later User evidence verifies Teleop
and Autonomous usability. The BL quantitative drivetrain anomaly remains
`KNOWN / DEFERRED HARDWARE MAINTENANCE`; no BL PASS, quantitative drivetrain
PASS, completed tuning, completed calibration, or issue resolution is claimed.

The publication at `d58bef0d17d202ce1dd0b8645635a8c35095dd3f` with subject
`Complete V00_L07 vision timestamp and latency contract` remains historical
pre-repair evidence only. The corrected repair publication is
`PUBLISHED / USER VERIFIED` at
`4704cfc0801910e30c8abb7cffcc467e4f4df016` with subject `Complete corrected
V00_L07 Swerve integrity repair`; HEAD equals origin/main at that commit.

V00_L08 remains not started, unactivated, non-authoritative, read-only, and
untouched. The historical reconstruction procedure in Section 26 is
SUPERSEDED PROSPECTIVELY for this exact candidate by Section 33; its
preservation-based reconciliation remains a separate governed action. V00_L09
remains not started.

## 33. Authorized One-Time Preservation-Based V00_L08 Reconciliation — 2026-09-07

The prior forensic and governance reviews established the following complete
basis for a narrow, candidate-specific exception:

1. The existing V00_L08 contains valuable User hardware and experimental
   evidence.
2. No unexplained production-source divergence was found.
3. The inherited defect delta is exact, bounded, and understood.
4. Exactly seven Java/test files differ because this candidate predates the
   corrected V00_L07 R1/R2/R3 repair.
5. No V00_L08-specific Java implementation overlaps those seven files.
6. The immutable corrected donor is the published V00_L07 repair at
   `4704cfc0801910e30c8abb7cffcc467e4f4df016`.
7. Fresh post-forward-port verification is mandatory.
8. A post-reconciliation inheritance and architecture review is mandatory.

Accordingly, preservation-based reconciliation is an AUTHORIZED ONE-TIME
EXCEPTION for the existing
`real_robot_programming/module_V00/V00_L08_RealVisionAdapterIntegration`
candidate. It does not generally authorize forward-porting in future lessons
and does not weaken ordinary Inheritance Development, the Frozen Backbone, the
Frozen Interface Contract, one-active-lesson discipline, transition-document
requirements, baseline verification, architecture audit, Design Lock, or User
Git ownership.

Before any reconciliation modification, the User must create and verify a
byte-preserving filesystem checkpoint of the entire current L08 directory
outside the repository. The checkpoint must preserve all files, hidden files,
`.Glass` configuration, build output, `bin`, `.gradle`, test reports and
artifacts, deploy assets, configuration, and documentation. It must not be
staged or committed to this repository.

The separately authorized reconciliation may forward-port only these seven
files from the exact corrected donor above:

Production:

- `src/main/java/frc/robot/commands/SwerveFrontLeftDriveStaticFrictionCharacterizationCommand.java`
- `src/main/java/frc/robot/subsystems/SwerveSubsystem.java`

Tests:

- `src/test/java/frc/robot/commands/SwerveFrontLeftOpenLoopCommissioningCommandTest.java`
- `src/test/java/frc/robot/io/swerve/SwerveModuleIOSimTest.java`
- `src/test/java/frc/robot/subsystems/SwerveSubsystemMeasuredSpeedTest.java`
- `src/test/java/frc/robot/subsystems/SwerveSubsystemModulePositionTest.java`
- `src/test/java/frc/robot/subsystems/SwerveSubsystemTest.java`

L08 remains NOT ACTIVATED / READ-ONLY during reconciliation, except for files
explicitly authorized by each controlled reconciliation step. Fresh verification
after the forward-port must include focused R1/R2/R3 tests, inherited Swerve,
vision, and autonomous regressions, the full test suite, a clean build,
changed-file boundary verification, and the post-reconciliation inheritance /
architecture review. Only after those gates may L08 proceed to its normal Real
Vision Adapter Design Lock and controlled activation.

This amendment does not authorize a Limelight Java adapter, NetworkTables
acquisition by robot code, a new vendor dependency, camera pose normalization,
estimator fusion, `addVisionMeasurement(...)`, PathPlanner changes, drivetrain
tuning, BL hardware investigation, or any V00_L09 work.

The governance-relevant Limelight distinction remains:

- VERIFIED USER HARDWARE EVIDENCE: Limelight -> roboRIO NetworkTables server ->
  Glass
- NOT YET IMPLEMENTED: Limelight -> Java VisionIO -> immutable VisionObservation

Detailed experimental evidence must later be preserved in a dedicated L08
experiment/evidence document. Provisional physical measurements, including the
unresolved WPILib pitch sign, are not architecture authority here.
