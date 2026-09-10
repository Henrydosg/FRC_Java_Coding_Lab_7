# Official 2026 Field Variants

## Why One Generic “2026 Field” Is Not Enough

WPILib 2026.2.1 provides two official rebuilt FRC field layouts. Their dimensions differ, so the Red field-centre rotation cannot be correct without an explicit construction choice. L04 deliberately models that choice with the small `FieldVariant` enum rather than assuming a universal 2026 size.

| FieldVariant | Installed WPILib 2026.2.1 source resource | Length | Width |
|---|---|---:|---:|
| `REBUILT_WELDED` | `2026-rebuilt-welded.json` | 16.541 m | 8.069 m |
| `REBUILT_ANDYMARK` | `2026-rebuilt-andymark.json` | 16.518 m | 8.043 m |

The authoritative source is the installed WPILib 2026.2.1 AprilTag field-layout resources. The source attribution is retained in `Constants.FieldTransformConstants`; L04 does not load layouts at runtime and does not introduce an AprilTag or vision dependency.

## Selection Contract

The caller must explicitly supply `FieldVariant` with every L04 transform. There is no default. This makes field selection visible in a future L05 composition or real-robot procedure instead of silently baking a possibly wrong geometry into a path.

```text
known field construction + definite alliance
                 ↓
FieldAllianceTransform(canonical Blue data)
```

For future real-robot trajectory testing, the team must select the physical field construction used for the test before creating alliance-derived reference data. That selection belongs to the future composition/test procedure, not to `FieldAllianceTransform`, `SwerveSubsystem`, vision, or a NetworkTables chooser.

## What This Does Not Mean

Field variant is not a localization source, an AprilTag layout request, or a mechanism/hardware configuration. It is immutable geometry used solely for the explicit field-centre transform. L01's known starting-pose contract and L03's canonical trajectory definition remain unchanged.
