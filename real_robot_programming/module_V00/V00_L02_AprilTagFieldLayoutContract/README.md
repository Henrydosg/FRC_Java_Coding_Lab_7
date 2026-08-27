# V00_L02 — AprilTag Field Layout Contract

## Current lesson state

- **Status:** `COMPLETE / FROZEN / READ-ONLY`
- **Implementation:** `COMPLETE / USER VERIFIED`
- **Technical Verification:** `PASS`
- **Documentation:** `COMPLETE`
- **Final Architecture Review:** `PASS`
- **Final Closure:** `PASS`
- **Freeze State:** `FROZEN / READ-ONLY`
- **Canonical predecessor:** `V00_L01_VisionCoordinateFramesAndCameraExtrinsics @ 7d52ebf`
- **A01 foundation:** `A01_L09_PathPlannerNamedCommandsAndEventMarkers @ 6b243bb`
- **Git publication:** `PENDING USER COMMIT/PUSH`

V00_L02 is a complete, frozen, read-only lesson snapshot. V00_L01 remains
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 7d52ebf`. A01 ends at A01_L09;
A01_L10 is prohibited. This closure does not start V00_L03, and Git publication
remains User-owned and pending.

## Authoritative reconstruction history

An earlier V00_L02 was implemented from a historical V00_L01 lineage. That
lineage became stale after final A01_L09 safety/event architecture was published
and V00_L01 was reconstructed. The stale lesson was backed up outside the
repository and was not resumed.

The canonical V00_L02 was reconstructed from final published V00_L01 at
`7d52ebf`. Generated artifacts were cleaned, WPILib Java 17 was used, and the
inherited baseline build and full inherited test suite passed before the new
concept was added. The inheritance and architecture audits found no unexpected
drift.

## One learning objective

V00_L02 implements one reference-geometry contract:

```text
official 2026 AprilTag identity
    -> canonical WPILib Blue-origin fieldToTag Pose3d
```

This gives later vision lessons authoritative tag poses without adding camera
acquisition, runtime observations, robot-pose estimation, or fusion.

## Exact Java delta

Exactly two Java files distinguish V00_L02 from frozen V00_L01:

- production:
  `src/main/java/frc/robot/vision/AprilTagFieldLayoutContract.java`;
- focused test:
  `src/test/java/frc/robot/vision/AprilTagFieldLayoutContractTest.java`.

All inherited production and test files, including `VisionFrameTransform.java`
and `VisionFrameTransformTest.java`, remain unchanged.

## Implemented contract

Package and class:

`frc.robot.vision.AprilTagFieldLayoutContract`

Approved public API:

```java
public static AprilTagFieldLayoutContract loadOfficial2026(
    Constants.FieldTransformConstants.FieldVariant fieldVariant)

public Optional<Pose3d> getTagPose(int tagId)
```

Explicit official resource mapping:

| Repository field variant | WPILib 2026.2.1 resource |
| --- | --- |
| `REBUILT_WELDED` | `AprilTagFields.k2026RebuiltWelded` |
| `REBUILT_ANDYMARK` | `AprilTagFields.k2026RebuiltAndymark` |

`AprilTagFields.kDefaultField` is not used as physical-field authority.

## Coordinate and lookup semantics

Returned poses are canonical WPILib Blue-origin `fieldToTag` values:

- +X points from Blue toward Red;
- +Y points left when viewed from Blue;
- +Z points upward;
- translations use meters;
- rotations use radians and right-handed NWU semantics;
- no alliance flip or Red mirroring occurs; and
- no `tagToField` inversion occurs.

Lookup behavior:

- null field variant -> `NullPointerException`;
- nonpositive tag ID -> `IllegalArgumentException`;
- unknown positive tag ID -> `Optional.empty()`;
- known positive tag ID -> `Optional<Pose3d>`.

## Immutable ownership

The implementation loads the selected official WPILib layout, validates its
field dimensions and reachable tag data, deep-snapshots tag ID to owned
`Pose3d` values, stores those values in an immutable private map, and discards
the raw layout.

It does not retain or expose:

- mutable `AprilTagFieldLayout`;
- mutable `AprilTag` objects;
- the raw tag list;
- a mutable map; or
- an origin-changing API.

The proposed `fromLayout(AprilTagFieldLayout)` test seam remains absent and
unapproved.

## Authoritative User verification

The User independently verified the canonical lesson in VS Code with WPILib
Java 17:

- `AprilTagFieldLayoutContractTest`: `PASS`;
- inherited `VisionFrameTransformTest`: `PASS`;
- full test suite: `PASS`;
- clean full build: `PASS`;
- clean build result: `BUILD SUCCESSFUL in 24s`;
- actionable tasks: `7 executed`.

These are User-supplied verification results. They are not claimed as
Codex-executed PASS results. The earlier Codex incremental classpath failure is
classified by the authoritative report as an environment/process discrepancy,
not an implementation defect.

## Architecture and runtime boundary

V00_L02 adds no runtime wiring and no change to:

- Frozen Backbone or Frozen Interface Contract;
- Robot, RobotContainer, commands, autonomous, or PathPlanner;
- Swerve, subsystems, IO, telemetry, or NetworkTables;
- Observation architecture;
- Gradle, vendordeps, source resources, or deploy assets; or
- camera/vendor selection.

The class is field-reference/domain geometry. It is not an Observation, IO
adapter, subsystem, telemetry component, mutable configuration object, or
runtime camera state.

## Verification classification

- Focused unit tests: `REQUIRED / PASS`
- Inherited Vision regression: `REQUIRED / PASS`
- Full regression suite: `REQUIRED / PASS`
- Clean full build: `REQUIRED / PASS`
- Simulation: `NOT APPLICABLE`
- Driver Station / Glass: `NOT APPLICABLE`
- Real Robot: `NOT APPLICABLE`
- Physical Camera: `NOT APPLICABLE`

Hardware/runtime gates are N/A because the lesson adds immutable deterministic
reference geometry only.

## Final closure sequence

```text
documentation complete
    -> final read-only architecture review PASS
    -> final closure authorization PASS
    -> freeze metadata complete
    -> COMPLETE / FROZEN / READ-ONLY
    -> User-owned Git add/commit/push pending
```

Current state: `COMPLETE / FROZEN / READ-ONLY / IMPLEMENTATION VERIFIED /
DOCUMENTATION COMPLETE / FINAL ARCHITECTURE REVIEW PASS / FINAL CLOSURE PASS /
GIT PUBLICATION PENDING USER COMMIT/PUSH`.
