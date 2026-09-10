# LESSON_STATUS

## Identity

- **Module:** V00 - AprilTag Vision Observation and Pose Fusion
- **Lesson:** V00_L08_RealVisionAdapterIntegration
- **Title:** V00_L08 - Real Vision Adapter Integration
- **Previous Lesson:** V00_L07_VisionTimestampAndLatencyContract @ 4704cfc
- **Previous Lesson State:** COMPLETE / FROZEN / READ-ONLY / PUBLISHED
- **Status:** COMPLETE
- **Active State:** COMPLETE / FROZEN / READ-ONLY
- **Freeze State:** FROZEN
- **Active Lesson Count:** 0
- **Lesson Goal:** integrate one real Limelight NetworkTables adapter into the frozen VisionIO boundary without fusion or vendor leakage

## Required status fields

- **Architecture Review:** PASS / BOUNDED REPAIR SCOPE AND OBSERVATION-ONLY
  RUNTIME OWNER REVIEW
- **Baseline Build:** PASS / PRESERVED CHECKPOINT AND CORRECTED-DONOR RECONCILIATION
- **Build:** PASS / User-verified `compileJava`, `compileTestJava`, and clean
  build under Java 17
- **Automated Verification:** PASS / User-verified focused adapter tests
  37/37 PASS and full regression 642/642 PASS. The earlier 637 passed / 5
  failed result is preserved as historical pre-repair evidence.
- **Simulation:** PASS / WPILib Simulation startup and Teleop runtime passed;
  the inherited deterministic `VisionIOSim` default correctly reported no
  target. This is not a claim that the real Limelight adapter was simulated.
- **Driver Station / Glass:** PASS / DS attached and Java vision telemetry
  visible through Glass.
- **Real Robot:** PASS / Limelight 4 acquisition, target loss, and
  reacquisition were observed through Java telemetry for AprilTag 32.
- **Transition Guide:** PASS / FINAL
- **Final Architecture Review:** PASS / `PASS_V00_L08_FINAL_CLOSURE_REVIEW`
- **Documentation:** PASS / LESSON-LOCAL REPAIR AND VERIFICATION RECORDS
  RECONCILED FOR FINAL CLOSURE
- **Closure:** COMPLETE / FINAL READ-ONLY ARCHITECTURE AND CLOSURE REVIEW PASS;
  LESSON FROZEN
- **Git Commit:** PENDING USER / CODEX DID NOT RUN GIT
- **Git Push:** PENDING USER / CODEX DID NOT RUN GIT
- **Known Issues:** Rotation remains **PROVISIONAL COMMISSIONING LOCK** and is
  not promoted to official or proven vendor semantics. A historical Codex-local
  Windows/Javac classpath-resolution anomaly remains non-authoritative because
  the User supplied official focused, full, and clean-build PASS evidence.
  Timing and quality evaluators remain inherited contracts; this adapter does
  not fabricate timing or quality data because the frozen VisionIO inputs do
  not contain those fields.

## Governance and provenance gates

| Gate | Result | Evidence / meaning |
| --- | --- | --- |
| Required English governance reading | PASS | AGENTS.md, README.md, applicable verified Document A/B/C mirrors, and active lesson source were read. |
| Governance mirror validation | PASS | `py -3 docs/tools/governance/validate_governance_mirrors.py` passed; manifest entries and SHA-256 values matched. |
| V00_L07 predecessor protection | PASS | Corrected V00_L07 donor remains published at `4704cfc`; no predecessor source was edited. |
| Candidate preservation checkpoint | PASS | External checkpoint `C:\Users\xps7350i7\Desktop\FRC_L08_Preservation_Checkpoint_2026-09-07_VERIFIED` was compared read-only. |
| Seven-file forward-port reconciliation | PASS / HISTORICAL | The preserved candidate differed from the checkpoint in exactly the seven ADR-authorized inherited Swerve repair files; those files matched the corrected V00_L07 donor before this repair turn. |
| Active lesson scope | PASS | Authorized L08 production/test files plus L08, repository-root, and governance metadata were reconciled; no V00_L09, fusion, autonomous, Swerve tuning, configuration, asset, or Git changes were made. |

## Design Lock

The real adapter consumes only NetworkTables data from `/limelight`:

- `/limelight/tv`
- `/limelight/tid`
- `/limelight/hb`
- `/limelight/targetpose_cameraspace`

The adapter requires a finite nonnegative heartbeat, accepts a target only when
`tv == 1`, requires a positive integer tag id and an exact six-element finite
pose tuple, and fails closed for missing, malformed, nonfinite,
stale/unacceptable, or invalid data. `tv == 0` is an invalid acquisition
sample, not a valid target-bearing observation. Repeated unchanged heartbeat
values invalidate the current sample immediately and become unavailable after
two repeated cycles. Heartbeat reset/reconnect requires fresh target-pose
evidence after both the prior target-refresh boundary and the prior
heartbeat-change boundary. The current stable `tv` and `tid` values remain
required structural fields; a changed tag id or visibility value alone is not
recovery evidence. Target changes observed while the heartbeat is stalled are
not recovery evidence. Independent NetworkTables topics are not
atomic; the adapter reads the target tuple twice, rejects disagreement, and
uses heartbeat rechecks as a bounded coherence guard. A zero pose with no
target never creates a measurement. The adapter returns `cameraToTarget` and
does not invert it.

The preserved robot-to-camera extrinsic is:

```text
Translation3d(-0.038, +0.050, +0.114) metres
Rotation3d(0, -20 degrees, 0)
```

Frame conversion is locked as:

```text
x_wpilib = +z_limelight
y_wpilib = -x_limelight
z_wpilib = -y_limelight
```

**ROTATION STATUS: PROVISIONAL COMMISSIONING LOCK**

The newly authorized H1 convention is implemented as column-vector matrix
multiplication:

```text
R = Rx(-a3) * Ry(+a4) * Rz(+a5)
```

where `a3`, `a4`, and `a5` are `targetpose_cameraspace[3..5]` in degrees.
This is a commissioning lock, not a lab-grade or vendor-official proof. The
real robot must validate it before any future promotion.

The evidence basis is preserved: controlled signed +Z testing reduced the
hypotheses; controlled signed +Y testing confirmed positive-Y behavior; H1 was
the only candidate classified STRONG in both earlier matrix tests; the real-
robot Pigeon yaw reference was `+0.104370 deg`, the negative endpoint was
`-24.798889 deg`, and the physical delta was `-24.903259 deg`. H1 decoded to
approximately `25.166 deg`, with approximately `0.263 deg` magnitude
disagreement. The positive-yaw test was less consistent, so physical validation
remains required.

## Implementation and automated verification

- **Production:** `VisionIOLimelight`, `VisionSubsystem`,
  `VisionTelemetryFacade`, `RobotTelemetry` wiring, vendor-boundary cleanup in
  `Constants`, and real-versus-simulation VisionIO selection in
  `RobotContainer`.
- **Runtime path:** `VisionIO.updateInputs(...) -> VisionSubsystem -> immutable
  VisionObservation -> read-only Vision telemetry`; no fusion or pose reset.
- **Focused tests:** User evidence confirms 37/37 PASS after the bounded
  freshness/coherence repair, including the isolated regressions that had
  failed before repair.
- **Official Gradle tests:** User evidence confirms the full 642/642 regression
  suite PASS, with `compileJava` and `compileTestJava` PASS.
- **Clean build:** User evidence confirms PASS under Java 17.
- **Runtime integration:** no estimator fusion, pose reset, autonomous access,
  control behavior, or vendor helper dependency was added. WPILib Simulation,
  Driver Station/Glass, and real Limelight acquisition/loss/reacquisition
  evidence are PASS.

## User-owned verification evidence

- WPILib Simulation confirms the real/simulation selection boundary and the
  inherited no-target `VisionIOSim` default.
- Driver Station/Glass shows Java telemetry. The simulated default is
  `Available=false`, `Connected=false`, `FirstTargetId=-1`, `SampleValid=false`,
  and `State=UNAVAILABLE`; no real-adapter simulation claim is made.
- Real Limelight evidence shows heartbeat progression, AprilTag 32 acquisition,
  target loss as `INVALID_SAMPLE` while connection remains available, and
  reacquisition as `TARGETS_PRESENT` with the expected pose telemetry.
- The target-pose translation and `cameraToTarget` direction were observed on
  the physical path. H1 remains provisional; no claim of atomic multi-topic
  NetworkTables reads is made.

## Final state for this documentation freeze

```text
COMPLETE / FROZEN / READ-ONLY
```

The final architecture and closure review is recorded as
`PASS_V00_L08_FINAL_CLOSURE_REVIEW`. The implementation, automated
verification, Simulation, Driver Station/Glass, real-robot evidence, and
transition documentation are reconciled and PASS. H1 remains the
**PROVISIONAL COMMISSIONING LOCK**; the bounded NetworkTables checks are not an
atomic multi-topic read claim; and V00_L09 estimator fusion remains out of
scope. Git publication remains pending the User's commit/push workflow. No Git
operation was run.
