# L03 to L04 to L05 Data Flow

## The Data Progression

```text
L03: canonical plan data only
Start Pose + interior Translation2d waypoint(s) + Goal Pose
        ↓ TrajectoryGenerator
time-parameterized canonical Blue-origin Trajectory
        ↓ sample(t)
Trajectory.State (pose rotation = path tangent)

L04: frame ownership only
canonical pose / vector / trajectory
        ↓ explicit FieldVariant + definite Alliance
one alliance-derived reference representation

L05: future following only
trajectory sample + estimated pose feedback
        ↓ future holonomic follower/controller
future field-relative drivetrain request
        ↓ existing SwerveSubsystem conversion and output boundary
```

The final L05 portion is a future design boundary, not L04 behavior. L04 has no command, scheduler registration, `ChassisSpeeds`, or `SwerveSubsystem` dependency.

## Ownership at Each Arrow

- L03 owns pure trajectory generation and leaves sampling native to WPILib.
- L04 owns the canonical-frame definition and the pure mathematical transform contract.
- Future L05 owns follower/controller responsibilities and must use `getEstimatedPose()` under the inherited L01/L02 validity contracts.
- `SwerveSubsystem` continues to own field-relative conversion, localization state mutation, and centralized `stop()`.

## What Must Survive the Transition

L01 field-heading reference and known start, L02 accepted-start one-shot readiness, EstimatedPose contract, immediate Disable stop, centralized stop, and no automatic restart all remain unchanged. The L03 trajectory still has path-tangent rotation, not a holonomic heading profile. L04 adds only an explicit answer to which field/alliance frame that canonical plan represents.

## Before the First Real-Robot Trajectory Autonomous

L05 must first complete its architecture, implementation, deterministic tests, and Simulation gates. Its real-robot procedure must select the actual field variant, require a known alliance and valid starting pose, confirm safe speed and Disable readiness, and apply exactly one documented alliance transform. L04 cannot claim that verification because it contains no physical actuation.
