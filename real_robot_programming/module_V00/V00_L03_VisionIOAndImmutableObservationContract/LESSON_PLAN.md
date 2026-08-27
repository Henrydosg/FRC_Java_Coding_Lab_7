# V00_L03 Lesson Plan - Vision IO and Immutable Observation Contract

## Current state

- **Status:** `COMPLETE / FROZEN / READ-ONLY`
- **Predecessor:** `V00_L02 @ 53e9b9f`
- **Preparation:** `COMPLETE / USER OWNED`
- **Baseline Build:** `PASS / USER VERIFIED / WPILib JAVA 17`
- **Inheritance Audit:** `PASS`
- **Architecture Audit:** `PASS`
- **Design Lock:** `PASS / APPROVED`
- **Implementation:** `COMPLETE / USER VERIFIED`
- **Focused Tests:** `PASS / USER VERIFIED`
- **Full Regression:** `PASS / USER VERIFIED`
- **Clean Build:** `PASS / USER VERIFIED`
- **Documentation:** `COMPLETE / PASS`
- **Final Architecture Audit:** `PASS`
- **Final Closure Review:** `PASS`
- **Freeze State:** `FROZEN / READ-ONLY`
- **Git publication:** `PENDING / USER OWNED`

## One-concept objective

Define and implement a vendor-neutral one-cycle `VisionIO` transport and an
immutable `VisionObservation` read-model contract without adding runtime camera
acquisition, interpretation, telemetry, simulation, vendor integration, pose
estimation, or fusion.

## Completed phase 1 - Preparation and inherited baseline

- User copied published V00_L02 to the approved V00_L03 identity.
- User handled generated artifacts separately from source inheritance.
- User used WPILib Java 17.
- User ran the inherited baseline build: PASS.
- No-Git filesystem comparison found 219 comparable non-generated files in each
  lesson and zero differences before L03 work.

**Result:** `PASS`.

## Completed phase 2 - Architecture audit and Design Lock

Locked:

- `frc.robot.io.vision` transport ownership;
- `VisionIO.updateInputs(VisionIOInputs)` as the only IO behavior;
- `available`, `connected`, `sampleValid`, and multiple acquisition-ordered
  `targets`;
- positive AprilTag identity and `cameraToTarget` direction;
- `frc.robot.observation.vision` immutable model ownership;
- five explicit availability/connection/validity states;
- immutable collection and transform ownership;
- no best-target policy;
- no runtime producer in L03;
- no field-layout dependency, telemetry, simulation implementation, or vendor
  implementation; and
- strict V00_L04-L09 deferral.

**Result:** `PASS / APPROVED`.

## Completed phase 3 - Controlled activation

- V00_L03 became the sole `IN_PROGRESS / EDITABLE` lesson.
- V00_L01 and V00_L02 remained frozen and published.
- Activation-era documentation preserved the then-correct
  `IMPLEMENTATION NOT YET AUTHORIZED` state as historical context.

**Result:** `PASS`.

## Completed phase 4 - Authorized exact implementation

The separate Architect/User authorization covered exactly two production files:

- `src/main/java/frc/robot/io/vision/VisionIO.java`;
- `src/main/java/frc/robot/observation/vision/VisionObservation.java`.

The implementation provides the locked mutable one-cycle transport, ordered
multi-target values, explicit observation states, positive identity validation,
camera-relative transform semantics, state/list consistency, finite observable
values, and defensive immutable ownership. No runtime producer or later-roadmap
feature was added.

**Result:** `PASS / IMPLEMENTED`.

## Completed phase 5 - Authorized focused tests

The separate focused-test authorization covered exactly:

- `src/test/java/frc/robot/io/vision/VisionIOTest.java`;
- `src/test/java/frc/robot/observation/vision/VisionObservationTest.java`.

The tests cover defaults, complete-cycle refresh, stale-target clearing,
connection/absence/invalid-sample distinctions, acquisition order, immutable
collection and transform ownership, state consistency, identity validation,
observable finite-value validation, equality, and deferred-field boundaries.

**Result:** `PASS / IMPLEMENTED`.

## Completed phase 6 - Initial verification and forensic diagnosis

The initial automated run had one failing test that expected an effectively
zero quaternion norm to be rejected. For the locked public `Transform3d`
contract, that expectation was not observable: WPILib `Rotation3d`
canonicalization had already converted the raw construction to a valid identity
rotation before the Observation boundary received it.

The failure was therefore diagnosed as a test-fixture/oracle defect, not a
production-contract defect. The authorized repair changed only the test oracle
to accept and verify a valid identity `Rotation3d` at the `Transform3d`
boundary. No raw quaternion API, alternate schema, or production repair was
required.

**Result:** `PASS / TEST-ORACLE REPAIR ONLY`.

## Completed phase 7 - Focused and inherited verification

Authoritative User verification under WPILib Java 17 records:

- `VisionObservationTest`: PASS;
- `VisionIOTest`: PASS;
- inherited V00_L01 `VisionFrameTransformTest`: PASS;
- inherited V00_L02 `AprilTagFieldLayoutContractTest`: PASS;
- full test suite: PASS; and
- clean full build: PASS.

These results are User-supplied evidence. The verification does not claim
physical camera, Simulation, Driver Station / Glass, or real-robot behavior.

**Result:** `PASS`.

## Completed phase 8 - Documentation completion

- Reconciled repository `AGENTS.md` and root `README.md` current-state text.
- Reconciled this lesson's README, status, plan, and checklist.
- Finalized the chronological transition guide.
- Reconciled the English and Vietnamese learning guides with the implementation,
  false-oracle diagnosis, and verification evidence.
- Preserved activation and initial-failure history as historical evidence.

**Result:** `PASS`.

## Completed phase 9 - Final read-only architecture audit

The final audit reviewed governance, inheritance, layering, the VisionIO and
Observation contract, immutability, Transform3d semantics, the test-oracle
repair, scope containment, and documentation consistency. The audit found no
unexpected production/test/configuration/dependency/asset delta and no
architecture blocker.

**Result:** `PASS / READY FOR CHATGPT CLOSURE REVIEW`.

## Completed phase 10 - Final closure and freeze

The Architect's final closure review passed. The documentation-only freeze
metadata transition records V00_L03 as `COMPLETE / FROZEN / READ-ONLY`.
Lesson content/state is complete and frozen; Git publication remains a separate
User-owned operation.

**Result:** `PASS`.

## Remaining User-owned publication gate

The User still owns Git add, commit, and push. No Git publication claim is made
until the User supplies publication evidence.

**Result:** `PENDING / USER OWNED`.

## Verification surfaces not applicable to L03

- **Simulation:** `NOT APPLICABLE / DEFERRED TO V00_L04`; L03 adds no simulation
  implementation.
- **Driver Station / Glass:** `NOT APPLICABLE`; L03 adds no runtime telemetry.
- **Physical camera / Real robot:** `NOT APPLICABLE / DEFERRED TO V00_L08`; L03
  adds no camera adapter, deployment, or actuation path.

These are scope classifications, not unperformed PASS claims.

## Explicit exclusions preserved

- no Limelight, PhotonVision, or vendor result object;
- no NetworkTables camera acquisition;
- no camera/runtime adapter;
- no field-layout consumption or field-to-robot estimate;
- no ambiguity, quality, confidence, covariance, timestamp, latency,
  freshness, ordering, or duplicate policy;
- no telemetry or runtime Observation producer;
- no Swerve fusion or estimator injection;
- no alliance mirroring;
- no autonomous, PathPlanner, Robot, RobotContainer, command, subsystem, or
  scheduler change; and
- no Gradle, vendordep, configuration, source-resource, deploy-asset, or
  predecessor change.

## Protected roadmap state

- V00_L01 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 7d52ebf`.
- V00_L02 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 53e9b9f`.
- A01 ends at A01_L09; `A01_L10` remains prohibited.
- V00_L04 has not been started.
- Git publication remains pending and User-owned.

## Current stopping point

`V00_L03` is `COMPLETE / FROZEN / READ-ONLY`. No active editable lesson remains.
V00_L04 has not been started, A01_L10 remains prohibited, and only User-owned
Git publication is pending.
