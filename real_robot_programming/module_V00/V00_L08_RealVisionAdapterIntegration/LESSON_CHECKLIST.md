# V00_L08 Lesson Checklist - Real Vision Adapter Integration

Status: COMPLETE / FROZEN / READ-ONLY  
Freeze state: FROZEN  
Predecessor: V00_L07 @ 4704cfc - COMPLETE / FROZEN / READ-ONLY / PUBLISHED  
Implementation result: COMPLETE / VERIFIED / DOCUMENTATION COMPLETE / FINAL
ARCHITECTURE AND CLOSURE REVIEW PASS  
Git commit/push: PUBLISHED @ f34b210 / USER VERIFIED

## Governance and provenance

- [x] AGENTS.md, README.md, and required English Document A/B/C material read.
- [x] Governance mirror validator passed and manifest hashes matched.
- [x] Corrected V00_L07 donor remains protected and unchanged.
- [x] Existing V00_L08 candidate was preserved and compared read-only with the
      external checkpoint.
- [x] Exactly the seven ADR-authorized inherited repair files differ from the
      checkpoint, and those seven match the corrected donor.
- [x] No completed or suspended lesson source was modified.
- [x] No Git command was run.

## Design Lock

- [x] One-concept boundary is real Limelight adapter integration.
- [x] NetworkTables-only transport; no LimelightHelpers runtime dependency.
- [x] Required topics are `tv`, `tid`, `hb`, and `targetpose_cameraspace`.
- [x] `tv == 1` is required for a target measurement.
- [x] Missing, malformed, nonfinite, stale/unacceptable, or invalid data fails
      closed without fabricating a target.
- [x] Repeated unchanged heartbeats invalidate immediately and fail closed after
      two repeated cycles.
- [x] Heartbeat reset/reconnect requires fresh target-pose evidence beyond both
      prior freshness boundaries; stable current `tv`/`tid` remain structural,
      partial changes and changes observed during a stalled heartbeat are
      rejected as recovery evidence.
- [x] Independent NetworkTables topics are documented as non-atomic; the
      adapter uses stable-read and heartbeat-recheck coherence guards.
- [x] `tv == 0` is an invalid acquisition sample, not a valid no-target sample.
- [x] `cameraToTarget` direction is preserved; no adapter inversion.
- [x] Translation basis conversion is explicit and tested.
- [x] Preserved robot-to-camera extrinsic is unchanged.
- [x] **ROTATION STATUS: PROVISIONAL COMMISSIONING LOCK** recorded.
- [x] H1 is implemented as `Rx(-a3) * Ry(+a4) * Rz(+a5)`.
- [x] No V00_L09 fusion or estimator wiring added.

## Implementation

- [x] Added `VisionIOLimelight` inside the IO package.
- [x] Kept Limelight schema names private to the real adapter; public constants
      remain vendor-neutral.
- [x] Added real/simulation VisionIO selection in RobotContainer.
- [x] Added the periodic observation-only runtime owner:
      `VisionIO -> VisionSubsystem -> immutable VisionObservation`.
- [x] Added read-only diagnostic telemetry without widening the observation
      contract or adding heartbeat/vendor fields.
- [x] Preserved public vendor-neutral IO and observation contracts.
- [x] Added focused deterministic adapter tests.
- [x] Preserved `VisionIOSim` independence from the real adapter.

## Automated verification

- [x] Historical pre-latest-tightening direct Java 17/JUnit result: 29 passed.
- [x] Added regressions for stalled-heartbeat recovery and same-value
      timestamp-change rejection; focused post-repair execution is 37/37 PASS.
- [x] User-verified `compileTestJava`: PASS.
- [x] Focused post-repair tests: 37/37 PASS; the five historical isolated
      freshness/coherence failures are repaired.
- [x] Full suite: 642/642 PASS after repair. The earlier 637/5 result remains
      historical pre-repair evidence.
- [x] Clean build: PASS under the User's Java 17 toolchain.

## User-owned runtime gates

- [x] WPILib Simulation run and evidence recorded; inherited `VisionIOSim`
      default no-target output is acceptable and is not real-adapter simulation.
- [x] Driver Station / Glass / NT4 inspection completed; Java telemetry is
      visible.
- [x] Real Limelight heartbeat and target acquisition validated for AprilTag 32.
- [x] Target loss and reacquisition validated; loss is `INVALID_SAMPLE` while
      the connection remains available, and reacquisition returns
      `TARGETS_PRESENT`.
- [x] Translation signs and `cameraToTarget` direction validated on hardware.
- [x] Physical runtime evidence supplied to the Architect/Reviewer; H1 remains
      explicitly provisional rather than promoted.

## Closure gates

- [x] Provisional rotation decision recorded after real validation; H1 remains
      a commissioning lock and is not claimed official or proven.
- [x] Final architecture and closure review PASS:
      `PASS_V00_L08_FINAL_CLOSURE_REVIEW`.
- [x] Transition guide finalized and marked PASS.
- [x] Lesson marked COMPLETE / FROZEN / READ-ONLY.
- [x] User commits and pushes the lesson; publication is verified at
      `f34b210` on `origin/main`.
