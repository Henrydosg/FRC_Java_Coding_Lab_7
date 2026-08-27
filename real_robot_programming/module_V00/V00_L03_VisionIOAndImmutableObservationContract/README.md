# V00_L03 - Vision IO and Immutable Observation Contract

## Current lesson state

- **Status:** `COMPLETE / FROZEN / READ-ONLY`
- **Active State:** `COMPLETE / FROZEN / READ-ONLY`
- **Lifecycle Qualifier:** `IMPLEMENTATION COMPLETE / USER-VERIFIED / DOCUMENTATION COMPLETE / FINAL ARCHITECTURE AUDIT PASS / PREDECESSOR PROVENANCE PASS / FINAL CLOSURE REVIEW PASS`
- **Implementation:** `COMPLETE / USER VERIFIED`
- **Focused Tests:** `PASS / USER VERIFIED`
- **Full Regression:** `PASS / USER VERIFIED`
- **Clean Build:** `PASS / USER VERIFIED`
- **Documentation:** `COMPLETE / PASS`
- **Final Architecture Audit:** `PASS`
- **Final Closure:** `PASS`
- **Freeze State:** `FROZEN / READ-ONLY`
- **Git publication:** `PENDING / USER OWNED`
- **Authoritative predecessor:** `V00_L02_AprilTagFieldLayoutContract @ 53e9b9f`
- **Earlier lineage:** `V00_L01 @ 7d52ebf` and `A01_L09 @ 6b243bb`

The activation and pre-closure records are preserved as historical context.
Lesson content/state is now complete, frozen, and read-only. No active editable
lesson remains. Git publication is a separate User-owned gate and is still
pending.

## Inheritance and scope

The User prepared this lesson by copying the published V00_L02 snapshot,
renaming the copy, handling generated artifacts, and running the inherited
baseline build under WPILib Java 17. A no-Git filesystem audit found 219
comparable non-generated files in each lesson and zero differences before L03
work. V00_L01 frame semantics, V00_L02 field-layout semantics, the inherited
A01 safety/event architecture, Gradle, vendordeps, configuration, source
resources, and deploy/PathPlanner assets remain preserved.

The exact L03 implementation boundary is four files:

- production: `src/main/java/frc/robot/io/vision/VisionIO.java`;
- production: `src/main/java/frc/robot/observation/vision/VisionObservation.java`;
- focused test: `src/test/java/frc/robot/io/vision/VisionIOTest.java`; and
- focused test: `src/test/java/frc/robot/observation/vision/VisionObservationTest.java`.

No other production or test Java file was added for L03.

## One learning objective

V00_L03 defines and implements one concept:

```text
camera/vendor adapter boundary
    -> vendor-neutral VisionIO
    -> mutable one-cycle VisionIOInputs
    -> future subsystem or estimator owner
    -> immutable VisionObservation / target observations
```

The lesson establishes transport and immutable meaning. It does not create the
runtime producer that will connect those two boundaries in a later lesson.

## Implemented VisionIO contract

Package: `frc.robot.io.vision`

`VisionIO` exposes only:

```java
void updateInputs(VisionIOInputs inputs)
```

`VisionIOInputs` is mutable one-cycle transport with:

```text
boolean available
boolean connected
boolean sampleValid
List<VisionTargetInputs> targets
```

Every update replaces all four facts. An empty target collection represents no
targets; stale target data and sentinel IDs are not used. Multiple targets are
retained in acquisition order. `sampleValid` means structurally coherent for
this cycle, not quality-approved or estimator-accepted.

Each `VisionTargetInputs` value carries only a positive `tagId` and a
camera-relative WPILib `Transform3d cameraToTarget`. The transform uses
right-handed NWU geometry, meters, and radians. It is not `targetToCamera`,
`robotToTarget`, `fieldToTag`, or `fieldToRobot`.

## Implemented immutable Observation contract

Package: `frc.robot.observation.vision`

`VisionObservation` is an immutable record with nested `State` and
`TargetObservation` values. Its states are:

- `UNAVAILABLE`;
- `DISCONNECTED`;
- `INVALID_SAMPLE`;
- `NO_TARGETS`; and
- `TARGETS_PRESENT`.

Only `TARGETS_PRESENT` may contain targets. Construction rejects nulls,
inconsistent state/target combinations, nonpositive IDs, and nonfinite values
observable through the accepted `Transform3d` boundary. Target collections and
transforms are defensively owned and preserve deterministic value equality.

## Transform3d boundary and false-oracle result

`cameraToTarget` remains a WPILib `Transform3d`. Legitimate identity
`Rotation3d` is valid. WPILib `Rotation3d` canonicalizes quaternion input
before the Observation receives the transform. Consequently, a raw zero or
effectively-zero quaternion can be converted to the valid identity rotation
`(1, 0, 0, 0)` before L03 can observe the original norm.

The initial automated verification exposed one failing expectation that this
unobservable raw quaternion norm would be rejected. For the locked public
contract this was a test-fixture/oracle defect, not a production defect. The
authorized repair changed `VisionObservationTest` to verify valid identity
rotation at the `Transform3d` boundary. No raw quaternion field/API, alternate
schema, or production-contract weakening was added, and no production repair
was required. A future raw or vendor pre-normalization check belongs inside the
applicable adapter before constructing `Rotation3d`.

## Explicit exclusions

L03 does not add:

- Limelight, PhotonVision, or any other vendor implementation;
- vendor result objects outside a future adapter;
- NetworkTables acquisition or telemetry;
- a runtime camera or simulation adapter;
- a field-layout dependency or field-to-robot estimate;
- best-target selection, ambiguity, quality, confidence, covariance, or
  acceptance policy;
- timestamps, latency, freshness, ordering, or duplicate policy;
- pose estimation or Swerve estimator fusion;
- alliance mirroring;
- autonomous, PathPlanner, Robot, RobotContainer, command, subsystem, or
  scheduler changes; or
- Gradle, vendordep, configuration, source-resource, deploy-asset, or
  predecessor changes.

Those responsibilities remain deferred to the governed V00 roadmap lessons.

## Authoritative User verification

Under WPILib Java 17, the User independently verified:

- `VisionObservationTest`: PASS;
- `VisionIOTest`: PASS;
- inherited V00_L01 `VisionFrameTransformTest`: PASS;
- inherited V00_L02 `AprilTagFieldLayoutContractTest`: PASS;
- full test suite: PASS (`512/512`); and
- clean full build: PASS.

These are User-supplied results. Simulation, Driver Station / Glass, physical
camera, and real-robot behavior are not claimed for this contract-only lesson.

## Verification classification

- focused unit tests: `REQUIRED / PASS`;
- inherited L01 and L02 regressions: `REQUIRED / PASS`;
- full regression suite: `REQUIRED / PASS`;
- clean full build: `REQUIRED / PASS`;
- Simulation: `NOT APPLICABLE / DEFERRED TO V00_L04`;
- Driver Station / Glass: `NOT APPLICABLE`;
- physical camera and Real Robot: `NOT APPLICABLE / DEFERRED TO V00_L08`.

The N/A classifications are scope decisions, not unperformed PASS claims.

## Protected lessons and roadmap

- V00_L01 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 7d52ebf`.
- V00_L02 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 53e9b9f`.
- The Frozen Backbone, Frozen Interface Contract, and Document C observation
  boundary remain unchanged.
- A01 ends at A01_L09; `A01_L10` remains prohibited.
- V00_L04 has not been started.
- Git add, commit, and push remain User-owned and pending.
- Codex performed no Git operation.

## Current closure position

Final documentation reconciliation and the final read-only architecture audit
are PASS. The Architect's final closure review is also PASS. V00_L03 is
recorded as `COMPLETE / FROZEN / READ-ONLY`. This lesson's content/state is
complete and frozen; only User-owned Git add/commit/push publication remains
pending. Codex performed no Git operation.
