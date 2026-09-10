# Unknown Alliance Safety Contract

## Why Unknown Is Not Blue

`DriverStation.getAlliance()` is optional because alliance information may be unavailable. Treating an unavailable value as Blue silently selects a field frame and can place a Red autonomous routine in the wrong geometry. L04 therefore has no implicit Blue fallback.

## L04 Utility Boundary

`FieldAllianceTransform` accepts only a definite `DriverStation.Alliance` and an explicit `FieldVariant`. It does not call `DriverStation.getAlliance()`, accept `Optional<Alliance>`, retain mutable alliance state, or select a default field variant. Null inputs are rejected.

```text
future composition boundary
    DriverStation optional alliance
              │
     present + valid field variant? -- no --> fail closed; do not dispatch motion
              │ yes
              v
FieldAllianceTransform(definite Alliance, explicit FieldVariant)
```

The exact scheduler/completion/stop behavior belongs to the future L05 composition design. It must preserve the existing Autonomous+Enabled gate, centralized `SwerveSubsystem.stop()`, and no-automatic-restart semantics.

## Operator and Real-Robot Implication

Before any future real-robot trajectory autonomous test, the team must confirm both the definite Driver Station alliance and the physical field variant. If either is unknown, the safe result is no autonomous trajectory dispatch, not a guess, manual mirroring, or a new telemetry/hardware dependency.

L04 itself produces no robot motion, so it has no Driver Station chooser or Glass/NetworkTables configuration. The contract is documented now so later composition cannot weaken this safety decision accidentally.
