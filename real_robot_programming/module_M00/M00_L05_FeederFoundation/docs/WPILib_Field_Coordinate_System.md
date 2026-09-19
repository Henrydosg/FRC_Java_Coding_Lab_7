# WPILib Field Coordinate System

## Why a Canonical Frame Is Needed

The same physical point must not mean different coordinates depending on who created a trajectory. L04 therefore defines **one canonical frame** for A01: the WPILib Blue-origin field frame. Trajectories, poses, and field-relative vectors are created canonically first; an alliance-specific reference is only derived later and explicitly.

## WPILib 2026 Convention Used by This Lesson

The installed WPILib 2026.2.1 AprilTag field-layout source defines the normal FRC origin at the right-hand corner of the Blue alliance wall in the NWU field coordinate convention. In that canonical Blue-origin frame:

```text
             +Y (toward the left when viewed from Blue)
              ^
              |
Blue origin --+------------------------------> +X (toward Red)
              |
              +-- positive rotation is counterclockwise about +Z
```

The exact source is the installed WPILib resource set used by `AprilTagFieldLayout`: `2026-rebuilt-welded.json` and `2026-rebuilt-andymark.json`. L04 copies only their published field length and width into `Constants.FieldTransformConstants`; it does not add an AprilTag or vision runtime dependency.

## Blue and Red Meaning

- **Blue** reference data has identity geometry in the canonical frame.
- **Red** reference data is the same physical geometry after a 180-degree rotation about the selected field centre.
- **Unknown** is not Blue. L04 accepts only a definite `DriverStation.Alliance`; future composition must resolve an unknown `Optional<Alliance>` fail closed before it calls the utility.

This is a field-coordinate decision, not a drivetrain-coordinate conversion. `SwerveSubsystem` remains the owner of its existing field-relative to robot-relative conversion and actuation boundary.

## Frame Labels Prevent Confusion

Every future pose or trajectory should state whether it is canonical Blue-origin data, an explicit L04 alliance-derived reference, or a future vendor-owned representation. That label prevents a later caller from applying an alliance operation twice.
