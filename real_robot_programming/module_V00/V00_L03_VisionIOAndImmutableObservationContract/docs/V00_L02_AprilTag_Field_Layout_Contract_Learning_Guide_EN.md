# V00_L02 Learning Guide — AprilTag Field Layout Contract

## Guide status

- **Lesson:** `V00_L02_AprilTagFieldLayoutContract`
- **State:** `COMPLETE / FROZEN / READ-ONLY`
- **Implementation:** `COMPLETE / USER VERIFIED`
- **Documentation:** `COMPLETE`
- **Final Architecture Review / Closure:** `PASS`
- **Language authority:** English is normative.

This guide explains the implemented reference-geometry contract. V00_L02 passed
final architecture and closure review and is complete, frozen, and read-only.

## 1. Why this lesson follows V00_L01

V00_L01 established frame mathematics:

- `robotToCamera`: where a camera is mounted on the robot;
- `fieldToCamera`: where the camera is in the canonical field frame; and
- rigid composition/inversion using WPILib geometry.

That mathematics does not tell us where numbered AprilTags are fixed on the
field. A future camera measurement needs two different categories of
information:

1. measured camera-to-target information from a camera system; and
2. authoritative field-to-tag reference geometry from the official field
   layout.

V00_L02 adds only the second category.

## 2. AprilTag identity

Every official field AprilTag has a positive integer ID. The ID is the stable
key that connects a future camera detection to one official field pose.

The V00_L02 query contract distinguishes:

- known positive ID -> `Optional<Pose3d>`;
- unknown positive ID -> `Optional.empty()`; and
- zero or negative ID -> `IllegalArgumentException`.

An unknown positive ID is normal absence: the request is structurally valid,
but that ID is not in the selected official layout. A nonpositive ID is an
invalid request.

## 3. Meaning of fieldToTag

The returned `Pose3d` is `fieldToTag`: the pose of the tag frame relative to
the canonical field frame.

Read the name from left to right:

```text
field frame -> tag frame
```

Translation locates the tag center in the field frame. Rotation describes the
tag frame orientation relative to the field frame.

It is not `tagToField`. Inverting the pose would answer a different question
and would break later frame composition.

## 4. Canonical WPILib Blue-origin frame

Official WPILib field layouts load with the Blue Alliance wall/right-side
origin:

- +X points from Blue toward Red;
- +Y points left when viewed from Blue;
- +Z points upward;
- translations use meters;
- rotations use radians; and
- axes and rotations follow the right-handed NWU convention.

The field reference does not change when the robot is assigned Red. Physical
tags do not move with alliance assignment.

Therefore V00_L02 performs no alliance flip, mirroring, origin reset, or A01_L04
autonomous transform. A01_L04 remains the sole autonomous alliance-transform
owner.

## 5. Explicit 2026 physical-field variants

The 2026 Rebuilt field has two official construction variants with slightly
different dimensions and tag positions:

| Repository variant | Official WPILib 2026.2.1 definition | Field dimensions |
| --- | --- | --- |
| `REBUILT_WELDED` | `AprilTagFields.k2026RebuiltWelded` | 16.541 m x 8.069 m |
| `REBUILT_ANDYMARK` | `AprilTagFields.k2026RebuiltAndymark` | 16.518 m x 8.043 m |

The physical field must be selected explicitly. The implementation does not use
`AprilTagFields.kDefaultField` as authority because a default alias can hide
which physical construction was chosen.

## 6. Implemented production API

Package and class:

`frc.robot.vision.AprilTagFieldLayoutContract`

Public methods:

```java
public static AprilTagFieldLayoutContract loadOfficial2026(
    Constants.FieldTransformConstants.FieldVariant fieldVariant)

public Optional<Pose3d> getTagPose(int tagId)
```

The class is `final`, its constructor is private, and no broader public API was
added.

## 7. Loading and validation sequence

`loadOfficial2026(...)` performs this sequence:

```text
explicit FieldVariant
    -> matching AprilTagFields enum
    -> AprilTagFieldLayout.loadField(...)
    -> validate field dimensions
    -> validate reachable tag IDs and poses
    -> deep-snapshot ID -> Pose3d
    -> immutable private map
    -> discard raw mutable layout/tag objects
```

Validation fails closed for malformed reachable data:

- field dimensions must be finite, positive, and match the selected variant;
- tag IDs must be positive;
- poses must be non-null;
- translations and quaternion components must be finite;
- quaternion norm must be finite and nonzero;
- duplicate IDs are rejected if reachable; and
- an empty layout is rejected.

Official resource-loading failures propagate rather than fabricating defaults.

## 8. Why the raw WPILib layout is not retained

`AprilTagFieldLayout` has a mutable origin. Calling `setOrigin(...)` changes
the frame used by later `getTagPose(...)` calls. Raw `AprilTag` objects also
have mutable public ID and pose fields.

Retaining or exposing those objects would let caller activity alter the
reference contract after construction. V00_L02 instead copies every validated
pose into owned geometry and stores only an immutable map.

The class exposes no:

- raw layout getter;
- raw tag list;
- mutable map;
- origin setter; or
- mutable `AprilTag` object.

## 9. Why there is no fromLayout test seam

A synthetic `fromLayout(AprilTagFieldLayout)` constructor seam was considered
before implementation and explicitly rejected.

The focused tests use only:

- `loadOfficial2026(FieldVariant)`;
- `getTagPose(int)`; and
- independent numeric values taken from installed official resources.

No production API was added merely to make malformed synthetic layouts easier
to inject. Validation that is unreachable through official loading remains
statically audited rather than forced through a prohibited seam.

## 10. WPILib 2026.2.1 tag-1 examples

These examples come directly from the installed official WPILib 2026.2.1 JSON
resources. They are field-reference examples, not robot measurements, camera
calibration, or estimated poses.

| Variant | Tag ID | X (m) | Y (m) | Z (m) | Yaw |
| --- | ---: | ---: | ---: | ---: | ---: |
| Rebuilt Welded | 1 | 11.8779798 | 7.4247756 | 0.889 | pi rad |
| Rebuilt AndyMark | 1 | 11.8639590 | 7.4114914 | 0.889 | pi rad |

The different X/Y values prove why explicit construction selection matters.
They also provide independent fixed test oracles without calling the production
helper to calculate expected values.

## 11. Focused verification

`AprilTagFieldLayoutContractTest` covers:

- explicit welded and AndyMark loading;
- null variant rejection;
- known, unknown-positive, zero, and negative IDs;
- independent numeric oracles for both official resources;
- canonical `fieldToTag` direction;
- no inversion or Red mirroring;
- meters and right-handed NWU radians;
- deterministic repeated lookup;
- caller operations cannot mutate stored reference state;
- welded/AndyMark distinction;
- exact public API; and
- absence of `fromLayout(...)`.

Authoritative User verification under VS Code with WPILib Java 17 records:

- focused AprilTag test: PASS;
- inherited `VisionFrameTransformTest`: PASS;
- full test suite: PASS; and
- clean full build: PASS (`BUILD SUCCESSFUL in 24s`; 7 actionable tasks,
  7 executed).

These are User results, not Codex-executed PASS claims.

## 12. Relationship to future camera measurements

Later vision work may eventually combine:

```text
fieldToTag                 official reference from V00_L02
cameraToTag                future measured camera observation
robotToCamera              fixed extrinsic from V00_L01
    -> candidate fieldToRobot estimate
```

V00_L02 performs none of that combination. It supplies only the fixed
`fieldToTag` reference.

## 13. Why this is not an Observation

An Observation is an immutable sampled fact produced by a subsystem or
estimator from IOInputs at a defined time. The field layout is static domain
reference geometry loaded from an official resource.

Therefore this contract belongs in `frc.robot.vision`, not
`frc.robot.observation`. It has no timestamp, validity age, hardware sample,
telemetry topic, scheduler behavior, or mechanism state.

## 14. Deferred V00 responsibilities

V00_L03 and later lessons remain responsible for separately reviewed concepts,
including:

- VisionIO and immutable Vision Observation;
- deterministic camera simulation;
- AprilTag robot-pose estimation;
- measurement quality;
- timestamps and latency;
- real camera adapter/vendor integration; and
- accepted measurement fusion into the Swerve pose estimator.

V00_L02 adds no camera vendor, NetworkTables, telemetry, RobotContainer,
autonomous, PathPlanner, Swerve, or hardware behavior.

## 15. Verification and closure state

Because this lesson adds deterministic immutable reference geometry only:

- Simulation: `NOT APPLICABLE`;
- Driver Station / Glass: `NOT APPLICABLE`;
- Real Robot: `NOT APPLICABLE`; and
- Physical Camera: `NOT APPLICABLE`.

The implementation and documentation are complete and verified. The lesson is
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 53e9b9f`; the User confirmed the
publication while retaining ownership of Git operations.
