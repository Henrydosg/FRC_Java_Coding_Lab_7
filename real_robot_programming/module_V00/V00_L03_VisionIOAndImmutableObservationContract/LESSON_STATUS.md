# LESSON_STATUS

## Identity

- **Lesson:** `V00_L03_VisionIOAndImmutableObservationContract`
- **Title:** `V00_L03 - Vision IO and Immutable Observation Contract`
- **Previous Lesson:** `V00_L02_AprilTagFieldLayoutContract @ 53e9b9f`
- **Earlier Lineage:** `V00_L01 @ 7d52ebf` and `A01_L09 @ 6b243bb`
- **Status:** `COMPLETE`
- **Active State:** `COMPLETE / FROZEN / READ-ONLY`
- **Lifecycle Qualifier:** `IMPLEMENTATION COMPLETE / USER-VERIFIED / DOCUMENTATION COMPLETE / FINAL ARCHITECTURE AUDIT PASS / PREDECESSOR PROVENANCE PASS / FINAL CLOSURE REVIEW PASS`
- **Freeze State:** `FROZEN / READ-ONLY`
- **Lesson Goal:** vendor-neutral one-cycle VisionIO transport plus immutable Vision Observation contract

## Required status fields

- **Architecture Review:** `PASS`
- **Baseline Build:** `PASS / USER VERIFIED / WPILib Java 17`
- **Build:** `PASS / USER VERIFIED`
- **Simulation:** `NOT APPLICABLE / DEFERRED TO V00_L04`
- **Driver Station / Glass:** `NOT APPLICABLE`
- **Real Robot:** `NOT APPLICABLE / DEFERRED TO V00_L08`
- **Transition Guide:** `FINAL / PASS`
- **Git Commit:** `PENDING / USER OWNED`
- **Git Push:** `PENDING / USER OWNED`
- **Known Issues:** No L03 implementation defect; runtime/camera verification
  is outside this contract-only lesson.

## Current lifecycle gates

| Gate | Result | Evidence / meaning |
| --- | --- | --- |
| Predecessor Publication | PASS | V00_L02 is User-verified published at `53e9b9f`. |
| User Copy/Rename Preparation | PASS | User prepared V00_L03 from authoritative V00_L02. |
| Generated-Artifact Handling | PASS | Generated outputs were handled separately from authoritative source inheritance. |
| Baseline Build | PASS | User-supplied WPILib Java 17 baseline evidence. |
| Inheritance Audit | PASS | 219 comparable non-generated files per lesson; zero differences before L03 work. |
| Architecture Audit | PASS | Frozen Backbone, Frozen Interface Contract, Documents A/B/C, and V00 roadmap preserved. |
| Design Lock | PASS / APPROVED | Exact L03 transport, Observation, semantics, tests, and deferred scope were locked. |
| Controlled Activation | PASS | V00_L03 became the sole `IN_PROGRESS / EDITABLE` lesson. |
| Implementation Authorization | PASS | Separate Architect/User authorization covered exactly two production and two focused-test files. |
| Production Implementation | PASS | `VisionIO.java` and `VisionObservation.java` only. |
| Focused Test Implementation | PASS | `VisionIOTest.java` and `VisionObservationTest.java` only. |
| Focused Verification | PASS | `VisionIOTest` and `VisionObservationTest` passed. |
| Inherited Regression | PASS | V00_L01 `VisionFrameTransformTest` and V00_L02 `AprilTagFieldLayoutContractTest` passed. |
| Full Regression | PASS | Authoritative User verification of the full `512/512` test suite. |
| Clean Build | PASS | Authoritative User verification of the clean full build. |
| Documentation Completion | PASS | README, status, plan, checklist, transition guide, and bilingual learning guides reconciled. |
| Transition Guide | FINAL / PASS | Chronology is complete through implementation, false-oracle diagnosis, verification, and pre-closure review. |
| English Learning Guide | FINAL / PASS | Final contract, implementation, forensic, and verification evidence recorded. |
| Vietnamese Learning Guide | FINAL / PASS | Explanatory guide aligned with the English guide. |
| Final Architecture Audit | PASS | Read-only audit found no architecture, inheritance, layering, contract, oracle, scope, or documentation blocker. |
| Final Closure Review | PASS | Architect-authorized final closure review passed; lesson content/state is frozen and read-only. |
| Freeze Metadata | PASS | V00_L03 is recorded as `COMPLETE / FROZEN / READ-ONLY`. |

## Authoritative implementation and verification

The separately authorized production boundary is exactly:

- `src/main/java/frc/robot/io/vision/VisionIO.java`;
- `src/main/java/frc/robot/observation/vision/VisionObservation.java`.

The separately authorized focused-test boundary is exactly:

- `src/test/java/frc/robot/io/vision/VisionIOTest.java`;
- `src/test/java/frc/robot/observation/vision/VisionObservationTest.java`.

The User independently verified the focused tests, inherited L01 and L02
regressions, full test suite, and clean build under WPILib Java 17. These are
User-supplied results and are not attributed to an unverified environment.

## Implemented L03 contract

- `VisionIO` is a vendor-neutral interface with only
  `void updateInputs(VisionIOInputs inputs)`.
- `VisionIOInputs` is mutable one-cycle transport containing `available`,
  `connected`, `sampleValid`, and acquisition-ordered `targets`.
- `VisionTargetInputs` carries only positive-tag identity transport and a
  WPILib `Transform3d cameraToTarget` value.
- `VisionObservation` is an immutable record with explicit states
  `UNAVAILABLE`, `DISCONNECTED`, `INVALID_SAMPLE`, `NO_TARGETS`, and
  `TARGETS_PRESENT`.
- Only `TARGETS_PRESENT` may contain targets; target collections and transforms
  are defensively owned.
- Target transforms retain camera-relative WPILib NWU semantics in meters and
  radians. L03 does not invert transforms or estimate robot pose.
- There is no target ranking, ambiguity, quality, timestamp, latency,
  covariance, estimator acceptance, telemetry, or runtime producer.

## Historical false-oracle diagnosis and authorized repair

The initial automated run exposed one failing expectation that an effectively
zero quaternion norm would be rejected by `VisionObservation`. For the locked
public contract, this was a test-fixture/oracle defect rather than a production
defect: WPILib `Rotation3d` canonicalization had already converted that raw
construction to the valid identity rotation before the `Transform3d` reached
the Observation boundary. The original raw norm was therefore not observable
by L03.

The authorized TEST-ORACLE repair replaced that unobservable expectation with a
test that constructs and accepts a valid identity `Rotation3d` at the
`Transform3d` boundary. No raw quaternion field, raw quaternion API, alternate
schema, or production-contract weakening was added. No production repair was
required. Raw pre-normalization validation, if needed by a future adapter,
belongs before construction of `Rotation3d` and is outside L03.

## Verification surfaces and exclusions

- **Simulation:** `NOT APPLICABLE / DEFERRED TO V00_L04`; L03 defines a contract
  and immutable model but adds no simulation implementation.
- **Driver Station / Glass:** `NOT APPLICABLE`; L03 adds no runtime telemetry.
- **Physical camera / Real Robot:** `NOT APPLICABLE / DEFERRED TO V00_L08`;
  no camera adapter, deployment, or actuation path was added.

No Limelight, PhotonVision, vendor result object, NetworkTables acquisition,
field-layout dependency, robot-pose estimator, quality/ambiguity policy,
timestamp/latency policy, fusion, Swerve, autonomous, PathPlanner, Robot,
RobotContainer, command, subsystem, scheduler, Gradle, vendordep,
configuration, source-resource, or deploy-asset change belongs to this L03
implementation.

## Protected boundaries and lifecycle

- V00_L01 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 7d52ebf`.
- V00_L02 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 53e9b9f`.
- The Frozen Backbone, Frozen Interface Contract, Document C observation
  boundary, and V00 roadmap remain unchanged.
- A01 ends at A01_L09; `A01_L10` remains prohibited.
- V00_L04 has not been started.
- Git add, commit, and push remain User-owned and pending.
- Codex performed no Git operation.

## Current result

`COMPLETE / FROZEN / READ-ONLY / IMPLEMENTATION COMPLETE / USER-VERIFIED /
DOCUMENTATION COMPLETE / FINAL ARCHITECTURE AUDIT PASS / PREDECESSOR
PROVENANCE PASS / FINAL CLOSURE REVIEW PASS`

The lesson content/state is complete, frozen, and read-only. User-owned Git
add/commit/push publication remains pending; Codex performed no Git operation.
