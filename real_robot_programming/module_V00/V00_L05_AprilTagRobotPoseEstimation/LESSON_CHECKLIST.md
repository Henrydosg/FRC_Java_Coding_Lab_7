# V00_L05 Lesson Checklist - AprilTag Robot Pose Estimation

Status: `COMPLETE / FROZEN / READ-ONLY`
Predecessor: `V00_L04 @ 5461555 - COMPLETE / FROZEN / READ-ONLY / PUBLISHED`
Design Lock: `APPROVED / CHATGPT ARCHITECT`
Implementation: `COMPLETE / AUTHORIZED BOUNDARY`
Post-repair user verification: `PASS / WPILib Java 17`
Final closure: `APPROVED / COMPLETE`
Active lesson count: `0`
Git publication: `PENDING USER GIT PUBLICATION`

## Governance and predecessor

- [x] Repository governance and authoritative English Documents A/B/C read.
- [x] Applicable ADRs reviewed.
- [x] V00_L04 confirmed complete, frozen, read-only, and published at
      `5461555`.
- [x] V00_L04 publication metadata reconciliation recorded at `96dcb4d`.
- [x] V00_L01 remains published and frozen at `7d52ebf`.
- [x] V00_L02 remains published and frozen at `53e9b9f`.
- [x] V00_L03 remains published and frozen at `cc20d62`.
- [x] A01_L04 remains the sole alliance-transform owner.
- [x] A01_L10 remains prohibited.

## Preparation and inheritance

- [x] User copied final V00_L04 to the ADR-approved V00_L05 identity.
- [x] Copied generated artifacts were removed before baseline verification.
- [x] User-supplied inherited WPILib Java 17 baseline build PASS recorded.
- [x] 229 comparable non-generated files confirmed.
- [x] Zero inheritance differences confirmed.
- [x] 74 production Java files confirmed identical.
- [x] 61 test Java files confirmed identical.
- [x] Build/configuration/wrapper inherited unchanged.
- [x] Vendordeps inherited unchanged.
- [x] Deploy/resources/PathPlanner content inherited unchanged.
- [x] Inherited documentation preserved until this activation.
- [x] The inherited pre-implementation baseline contained no L05-specific implementation.
- [x] V00_L01-L04 predecessor protection passed.

## Identity and activation

- [x] Initial candidate naming HOLD recorded as historical evidence.
- [x] Architect decision retained the frozen ADR identity without amendment.
- [x] User renamed the untracked candidate to
      `V00_L05_AprilTagRobotPoseEstimation`.
- [x] Naming re-audit PASS; ADR amendment not required.
- [x] Design-Lock planning audit PASS.
- [x] Architect-approved Design Lock recorded.
- [x] V00_L05 activated as the sole `IN_PROGRESS / EDITABLE` lesson.
- [x] Active V00 lesson count is `1`.
- [x] Historical activation began before implementation authorization.

## Approved Design Lock

- [x] Pure deterministic vendor-neutral pose-candidate responsibility recorded.
- [x] Canonical inputs recorded: `fieldToTag`, `cameraToTarget`,
      `robotToCamera`.
- [x] Canonical output recorded: Blue-origin `fieldToRobot` pose candidate.
- [x] WPILib Blue-origin/NWU, meters, and radians recorded.
- [x] Exact package recorded: `frc.robot.vision`.
- [x] Exact class recorded: `AprilTagRobotPoseEstimator`.
- [x] Stateless final non-instantiable form recorded.
- [x] Exact single public method recorded:
      `estimateFieldToRobotCandidate(Pose3d, Transform3d, Transform3d)`.
- [x] `VisionFrameTransform` production reuse boundary recorded.
- [x] `VisionIOSim` test-only round-trip/reference boundary recorded.
- [x] No vendor, IO, Observation, layout, runtime, telemetry, or fusion
      dependency added to the estimator boundary.
- [x] Structural validation boundary recorded.
- [x] L06 measurement-quality boundary recorded as deferred.
- [x] Independent numeric-oracle requirement recorded.

## Implementation and verification - completed

- [x] Separate production implementation authorization.
- [x] Create `AprilTagRobotPoseEstimator.java`.
- [x] Create `AprilTagRobotPoseEstimatorTest.java`.
- [x] Focused identity, translation, rotation, and combined-geometry tests.
- [x] Noncommutative-order proof, independent 3D numeric oracle, and narrow
      test-only fixture repair.
- [x] L04 forward to L05 reverse round-trip test.
- [x] Meters, radians, NWU signs, multiple positions, and nonzero extrinsic tests.
- [x] Determinism, fresh result, and caller-ownership tests.
- [x] Null and observable nonfinite validation tests.
- [x] Exact public API/reflection test, including final-class and exactly-one-
      public-declared-method protection.
- [x] Inherited vision regression tests.
- [x] Full test suite.
- [x] Clean build.
- [x] Post-implementation documentation reconciliation.
- [x] Post-implementation architecture review.
- [x] API reflection-test hardening implemented.
- [x] Post-hardening User verification rerun: Java 17, clean compileTestJava,
      focused tests, inherited regressions, full suite, and clean build PASS.
- [x] Final Architect closure authorization.
- [x] Freeze metadata update.
- [ ] User-owned Git add/commit/push.

## Verification surfaces

- Focused deterministic unit tests: `PASS / USER VERIFIED / WPILib Java 17`.
- Inherited regression tests: `PASS / USER VERIFIED / WPILib Java 17`.
- Full test suite: `PASS / USER VERIFIED / WPILib Java 17`.
- Clean build: `PASS / USER VERIFIED / WPILib Java 17`.
- WPILib Simulation: `NOT REQUIRED / NOT APPLICABLE`.
- HALSIM: `NOT REQUIRED / NOT APPLICABLE`.
- Glass: `NOT REQUIRED / NOT APPLICABLE`.
- Driver Station: `NOT REQUIRED / NOT APPLICABLE`.
- Limelight/physical camera: `NOT REQUIRED / NOT APPLICABLE`.
- Real robot: `NOT REQUIRED / NOT APPLICABLE`.

## Protected boundaries

- [x] Frozen Backbone preserved.
- [x] Frozen Interface Contract preserved.
- [x] Document C Observation architecture preserved.
- [x] V00_L01 unchanged.
- [x] V00_L02 unchanged.
- [x] V00_L03 unchanged.
- [x] V00_L04 unchanged.
- [x] No production Java changed by activation.
- [x] No test Java changed by activation.
- [x] No build/configuration/dependency/deploy asset changed by activation.
- [x] No ADR changed.
- [x] No V00_L06+ lesson activated.
- [x] No Git operation performed by Codex.

## Current result

`COMPLETE / FROZEN / READ-ONLY / DESIGN LOCK APPROVED / IMPLEMENTATION COMPLETE /
POST-REPAIR USER VERIFICATION PASS / POST-HARDENING USER VERIFICATION PASS /
FINAL ARCHITECT CLOSURE APPROVED / COMPLETE / DOCUMENTATION COMPLETE /
ACTIVE V00 LESSON COUNT 0 / GIT PUBLICATION PENDING USER ACTION`
