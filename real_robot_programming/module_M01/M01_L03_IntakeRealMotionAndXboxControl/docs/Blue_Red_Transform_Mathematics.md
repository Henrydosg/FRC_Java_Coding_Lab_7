# Blue/Red Transform Mathematics

## Why the Red Operation Is a Rotation

L04 uses a 180-degree rotation about the selected field centre, not a mirror reflection. A rotation preserves coordinate handedness and maps both field axes to their opposites. A reflection would reverse handedness and has different rotation and curvature consequences.

For selected field length `L` and width `W`, the Red transform of canonical Blue geometry is:

```text
x' = L - x
y' = W - y
theta' = normalize(theta + pi)
vx' = -vx
vy' = -vy
omega' = omega
```

The centre is `(L / 2, W / 2)`. `omega` remains unchanged because a proper planar rotation preserves counterclockwise angular-rate sign.

## Official 2026 Numerical Examples

Take a canonical pose `(2.750 m, 1.600 m, 37 deg)` and a field-relative vector `(0.700, -1.100) m/s` with `omega = -0.450 rad/s`.

| Variant | Field L x W (m) | Field centre (m) | Red pose | Red vector | Red omega |
|---|---|---|---|---|---:|
| REBUILT_WELDED | 16.541 x 8.069 | (8.2705, 4.0345) | (13.791, 6.469, -143 deg) | (-0.700, 1.100) m/s | -0.450 rad/s |
| REBUILT_ANDYMARK | 16.518 x 8.043 | (8.2590, 4.0215) | (13.768, 6.443, -143 deg) | (-0.700, 1.100) m/s | -0.450 rad/s |

The heading shown as `-143 deg` is the normalized form of `37 + 180 = 217` degrees. A second Red rotation returns original geometry mathematically: `R(pi)(R(pi)(p)) = p`. That is a **double-transform misuse signature**, not a supported operational sequence.

## What the Implementation Does

`FieldAllianceTransform` receives an explicit `FieldVariant` and definite `Alliance`. Blue returns equivalent fresh geometry; Red applies the equations above. It rejects null or nonfinite inputs rather than choosing a default variant or silently treating unknown alliance as Blue.
