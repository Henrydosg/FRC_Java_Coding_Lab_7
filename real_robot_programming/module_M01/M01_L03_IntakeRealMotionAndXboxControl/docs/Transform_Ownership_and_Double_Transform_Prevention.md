# Transform Ownership and Double-Transform Prevention

## The Rule

Every production execution path has **exactly one** alliance-transform owner. The A01 ADR forbids a hidden transform in IO, Swerve module code, telemetry, or multiple autonomous layers.

```text
canonical Blue trajectory
        │
        ├── A01 owner: L04 transform once -> vendor flip disabled/not reapplied
        │
        └── vendor owner: canonical data unchanged -> vendor flip once
```

The two branches are alternatives. They are never combined.

## Why Applying It Twice Is Dangerous

For a Red alliance, two 180-degree transforms happen to return canonical geometry mathematically. This can look plausible in a unit test while being wrong for the Red field. `FieldAllianceTransformTest` therefore documents the second Red transform as a **DOUBLE-TRANSFORM MISUSE SIGNATURE**, not a feature.

## Ownership by Lesson Boundary

L04 defines the pure transform and no execution path uses it yet. L05 must select and document its execution-owner decision before following a trajectory. PathPlanner remains prohibited until L06 and AutoBuilder until L07. When those lessons are reached, their actual compatible API/version must be verified as required by the ADR; this documentation does not assume a future vendor configuration.

If a future A01 autonomous layer owns the transformation, it invokes the pure L04 utility exactly once and configured vendor flipping must be disabled or otherwise absent. If a future PathPlanner/AutoBuilder integration owns the transformation, data handed to it stays canonical/unflipped and no A01 call is made. The chosen owner must be recorded with the routine and its frame label.

## What Does Not Own This Transform

`RobotContainer` composes future objects but contains no transform business logic. `SwerveSubsystem` owns existing field-relative actuator conversion, not alliance policy. IO, observations, and telemetry do not apply an alliance transform. These boundaries preserve the Frozen Backbone and make one transform owner auditable.
