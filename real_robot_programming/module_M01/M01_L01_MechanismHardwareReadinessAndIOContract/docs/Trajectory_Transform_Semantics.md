# Trajectory Transform Semantics

## Why a Trajectory Needs Special Care

An L03 `Trajectory` is time-parameterized path data. Transforming it for Red must preserve its timing and motion scalars while moving every path pose into the corresponding field geometry. It must not silently invent a future holonomic-heading profile.

```text
canonical Trajectory
  State(t, v, a, poseMeters, curvature)
             ↓ L04 Red transform
alliance-derived Trajectory
  State(t, v, a, rotated poseMeters, curvature)
```

## State-by-State Contract

For **both** alliances L04 returns a fresh `Trajectory` containing fresh `Trajectory.State` objects. It never mutates or aliases caller-owned states.

For **Blue**, state geometry is equivalent to canonical geometry. For **Red**, each `poseMeters` is rotated using the selected field length/width and its rotation receives `+pi` (normalized). The following are preserved exactly:

- `timeSeconds`;
- `velocityMetersPerSecond`;
- `accelerationMetersPerSecondSq`;
- `curvatureRadPerMeter`;
- state order and total duration.

L04 does not use `Trajectory.transformBy()` for the field-centre operation. The selected-field 180-degree operation is defined explicitly and is applied directly to copied state geometry.

## Tangent Is Not Holonomic Heading

`Trajectory.State.poseMeters.getRotation()` describes the direction of the path geometry, its tangent. After Red transformation, that tangent also rotates by 180 degrees. It is **not** a desired independent robot heading for a swerve drive. A future L05 holonomic follower must define robot-heading policy separately; L04 intentionally does not add a controller, `ChassisSpeeds`, or drivetrain output.

## Example

At `t = 0.75 s`, canonical pose `(4.200, 2.300, -58 deg)` becomes `(12.341, 5.769, 122 deg)` on the Welded field and `(12.318, 5.743, 122 deg)` on the AndyMark field. Its time, scalar velocity, acceleration, and curvature are identical before and after the transform.
