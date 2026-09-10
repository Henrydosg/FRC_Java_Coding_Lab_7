# V00_L03 - Vision IO and Immutable Observation Contract Learning Guide

English is normative. This final edition teaches the approved contract and
records the implemented boundary, verification evidence, and final frozen
lifecycle state. Git publication remains a separate User-owned gate.

## 1. Why VisionIO exists

A robot program should not allow a camera vendor library to spread through
subsystems, commands, telemetry, and pose estimation. `VisionIO` creates one
small hardware/source boundary. Future real and simulation adapters can change
without changing the domain contract.

```text
future camera / simulation adapter
    -> VisionIO
    -> VisionIOInputs
    -> future domain owner
    -> immutable VisionObservation
    -> future read-only consumers
```

## 2. Hardware/vendor adapter to Inputs

The implemented boundary is designed for a future adapter to read one source
once per cycle and write a complete `VisionIOInputs` snapshot. Vendor result
objects remain inside that future adapter. The public capability is only:

```java
void updateInputs(VisionIOInputs inputs)
```

There is no output or `stop()` method because this L03 contract observes a
camera source; it does not actuate a mechanism.

## 3. Mutable transport versus immutable meaning

`VisionIOInputs` is implemented mutable transport. A future adapter overwrites
it every cycle. It may describe unavailable hardware, a disconnect, an invalid
sample, no targets, or multiple targets.

`VisionObservation` is implemented immutable domain meaning. It is safe for
tests, diagnostics, logs, and future read-only consumers because it does not
expose a mutable Inputs object.

The two types must never be treated as interchangeable.

## 4. Why vendor objects stop at VisionIO

Limelight, PhotonVision, or another vendor may represent targets differently.
If vendor result types crossed `VisionIO`, every consumer would depend on that
vendor and simulation replacement would become difficult. L03 therefore uses
only JDK collections and approved WPILib geometry values.

No camera vendor is selected before V00_L08.

## 5. Why NetworkTables is absent

NetworkTables is a publication/acquisition technology, not immutable domain
meaning. `VisionObservation` contains no topics, keys, publishers, update
loops, or NetworkTables entry objects. Telemetry remains a future read-only
consumer and is not added merely to visualize a contract-only lesson.

## 6. The one-cycle Inputs fields

The implemented Inputs fields are:

- `available`: the selected implementation can supply vision data;
- `connected`: the supported source is currently connected;
- `sampleValid`: this acquisition cycle is structurally coherent;
- `targets`: all targets from this acquisition cycle.

Every future call must replace all four facts. If a target disappears, the new
target collection must be empty. Old target data must not remain visible.

## 7. available, connected, and sampleValid are different

The three booleans answer different questions:

| Meaning | available | connected | sampleValid |
| --- | --- | --- | --- |
| Implementation unavailable | false | false | false |
| Supported source disconnected | true | false | false |
| Connected but structurally invalid sample | true | true | false |
| Connected coherent sample | true | true | true |

`sampleValid` does not mean good target quality, low ambiguity, accepted pose,
or accepted estimator measurement. Those decisions belong to later lessons.

## 8. Observation states

The approved immutable states are:

- `UNAVAILABLE`
- `DISCONNECTED`
- `INVALID_SAMPLE`
- `NO_TARGETS`
- `TARGETS_PRESENT`

Only `TARGETS_PRESENT` may contain target observations. No-target absence is an
empty collection, not ID zero, a negative sentinel, NaN, or a zero transform.

## 9. Why multiple targets are retained

A camera may see several AprilTags in one cycle. Choosing one target would
silently introduce a selection policy. L03 retains all targets in acquisition
order so V00_L04 can simulate them and later lessons can interpret them.

There is no "best target" in L03 because quality and ambiguity do not become a
lesson responsibility until V00_L06.

## 10. Target identity

Each target carries a positive `int tagId`. Positive identity lets a future
pose estimator join the camera measurement with authoritative field-layout
data. No arbitrary maximum ID is frozen. Empty targets, rather than a sentinel
ID, represent absence.

## 11. cameraToTarget direction

Each target also carries:

```text
Transform3d cameraToTarget
```

This means the target relative to the camera. It does not mean
`targetToCamera`, `robotToTarget`, `fieldToTag`, or `fieldToRobot`.

Frame direction is part of the contract because inversion errors can produce
mathematically valid but physically wrong results.

## 12. Coordinate convention and units

L03 uses WPILib right-handed NWU geometry:

- +X forward;
- +Y left;
- +Z up;
- translation in meters;
- rotation in radians.

Immutable targets reject nulls and invalid values observable through the
accepted `Transform3d` boundary, including nonfinite observable components.
Legitimate identity rotation is valid.

WPILib `Rotation3d` canonicalizes a supplied quaternion. If raw input has a
zero or effectively-zero norm, that construction can replace it with identity
`(1, 0, 0, 0)` before `VisionObservation` receives the transform. The original
raw norm is then unavailable and cannot be reconstructed by the Observation.
Any future raw or vendor pre-normalization check belongs in the applicable
adapter before `Rotation3d` construction; L03 does not add that adapter or raw
quaternion transport.

## 13. Immutability and defensive ownership

The implemented `VisionObservation` contains a state and an immutable list of
immutable target values. Its construction:

- rejects null state, collection, entries, and transforms;
- enforces state/list consistency;
- requires positive IDs;
- validates transform values observable after `Rotation3d` canonicalization;
- defensively copies the target collection and geometry; and
- preserves deterministic value equality.

A caller must not be able to change an Observation by changing an old Inputs
object or list.

## 14. Why fieldToTag is not used in L03

V00_L02 provides canonical Blue-origin `fieldToTag` reference poses. L03 only
defines acquisition facts in the camera frame. Combining `fieldToTag`, camera
extrinsics, and `cameraToTarget` to derive a robot-pose candidate is V00_L05's
responsibility.

Keeping field layout out of L03 prevents acquisition from becoming estimation.

## 15. Why the runtime producer is deferred

Document C requires a subsystem or dedicated estimator to produce mechanism
Observations from IOInputs. L03 has no runtime camera or simulation adapter.
Creating a subsystem or mapper now would add an empty layer without a real
runtime responsibility. The producer decision is therefore deferred.

IO and telemetry must never become the Observation owner.

## 16. Why simulation is deferred to V00_L04

L03 freezes interfaces and immutable value semantics. V00_L04 introduces the
first deterministic implementation of `VisionIO`. Simulation is therefore not
an L03 activation or verification gate.

## 17. Why pose estimation is deferred to V00_L05

Robot pose requires field layout, camera mounting extrinsics, target identity,
and camera-relative target geometry. L03 carries only the raw vendor-neutral
measurement boundary; V00_L05 owns the mathematical pose candidate.

## 18. Why quality and time are later

V00_L06 owns ambiguity, quality, confidence, and acceptance classification.
V00_L07 owns timestamps, latency, freshness, ordering, and duplicates.

Therefore L03 must not add ambiguity, quality, timestamp, latency, covariance,
freshness, or acceptance fields.

## 19. Why real cameras wait until V00_L08

V00_L08 may select exactly one real implementation only after hardware,
WPILib-version, vendor-library, dependency, timestamp, and simulation-support
review. L03 cannot guess Limelight, PhotonVision, or any other camera.

Physical camera, Driver Station / Glass, and real-robot checks are not L03
verification surfaces.

## 20. Why fusion waits until V00_L09

Vision fusion requires an accepted, timestamped, quality-classified robot-pose
measurement. Those prerequisites do not exist in L03. V00_L09 alone may feed
accepted measurements into the Swerve-owned `SwerveDrivePoseEstimator` through
the approved fusion boundary.

Autonomous continues to consume `getEstimatedPose()` and never reads raw
camera data.

## 21. Implementation record

The separate Architect/User authorization covered exactly four Java files:

- `src/main/java/frc/robot/io/vision/VisionIO.java`;
- `src/main/java/frc/robot/observation/vision/VisionObservation.java`;
- `src/test/java/frc/robot/io/vision/VisionIOTest.java`; and
- `src/test/java/frc/robot/observation/vision/VisionObservationTest.java`.

`VisionIO` exposes only `updateInputs(VisionIOInputs)`. The inputs snapshot is
mutable and is refreshed as a complete one-cycle transport. `VisionObservation`
and its target values are immutable, retain acquisition order, defend their
collection and transform ownership, and expose only the locked states and
fields. No runtime producer was added.

## 22. The false test-oracle diagnosis

The initial automated verification exposed one failing expectation that an
effectively zero quaternion norm would be rejected. That raw norm is not
observable through the locked public `Transform3d` contract: WPILib
`Rotation3d` canonicalizes the quaternion during construction and can convert
that raw input to the valid identity rotation `(1, 0, 0, 0)` before the
Observation receives it.

This was a test-fixture/oracle defect, not a production-contract defect. The
authorized test-only repair replaced the unobservable rejection expectation
with a test that accepts a valid identity `Rotation3d` at the `Transform3d`
boundary. No raw quaternion field or API was added, and no production repair
was required. If a future vendor adapter needs pre-normalization validation, it
must perform it before constructing `Rotation3d`.

## 23. Verification evidence

Under WPILib Java 17, the User independently verified:

- `VisionObservationTest`: PASS;
- `VisionIOTest`: PASS;
- inherited V00_L01 `VisionFrameTransformTest`: PASS;
- inherited V00_L02 `AprilTagFieldLayoutContractTest`: PASS;
- full test suite: PASS; and
- clean full build: PASS.

The final documentation reconciliation, read-only architecture audit, and
final closure review are also PASS. These results do not claim camera,
Simulation, Driver Station / Glass, or real-robot behavior.

## 24. Verification surfaces and deferred work

Simulation is `NOT APPLICABLE / DEFERRED TO V00_L04` because L03 adds no
simulation implementation. Driver Station / Glass is `NOT APPLICABLE` because
L03 adds no runtime telemetry. Physical camera and Real Robot verification are
`NOT APPLICABLE / DEFERRED TO V00_L08` because no camera adapter, deployment,
or actuation path was added.

Pose estimation, quality and ambiguity, timestamps and latency, real vendor
integration, and Swerve estimator fusion remain later V00 responsibilities.

## 25. Current lifecycle state

Completed:

- User preparation and Java 17 baseline build;
- read-only inheritance and architecture audits;
- approved Design Lock and controlled activation;
- authorized implementation and focused tests;
- focused and inherited verification, full regression, and clean build;
- final documentation reconciliation; and
- final read-only architecture audit.

Remaining gate:

- User-owned Git add, commit, and push.

V00_L03 is `COMPLETE / FROZEN / READ-ONLY`. No active editable lesson remains.
Git publication is pending and User-owned. V00_L04 has not been started, and
`A01_L10` remains prohibited.
