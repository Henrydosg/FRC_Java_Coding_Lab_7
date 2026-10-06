# S00_L14 Swerve Hardware Commissioning Matrix

## Classification Rules

- VERIFIED: supported by the inherited repository hardware map and matching source constants, or by the supplied Phoenix Tuner X / Disabled real-robot verification record.
- PROVISIONAL: an inherited software baseline that is not a verified hardware configuration value.
- UNRESOLVED: no authoritative value exists in the inherited repository; leave it unset until user-owned commissioning evidence exists.

## Four Swerve Modules

| Item | FL | FR | BL | BR | Classification and evidence |
| --- | ---: | ---: | ---: | ---: | --- |
| Drive CAN ID | 21 | 24 | 27 | 30 | VERIFIED - hardware map v2.0 and `Constants.java` agree. |
| Steer CAN ID | 22 | 25 | 28 | 31 | VERIFIED - hardware map v2.0 and `Constants.java` agree. |
| CANcoder CAN ID | 23 | 26 | 29 | 32 | VERIFIED - hardware map v2.0 and `Constants.java` agree. |
| Drive ratio | 6.75:1 | 6.75:1 | 6.75:1 | 6.75:1 | VERIFIED - final measured/commissioned value; repeated physical 20-motor-rotation / wheel-rotation tests were repeated multiple times. The initial provisional 7.85:1 value is superseded historical data only. |
| Steer ratio | UNRESOLVED | UNRESOLVED | UNRESOLVED | UNRESOLVED | UNRESOLVED - no authoritative value found. |
| Wheel diameter | 4.0 in | 4.0 in | 4.0 in | 4.0 in | VERIFIED - hardware map v2.0 and `kWheelDiameterMeters`. |
| Drive inversion | UNRESOLVED | UNRESOLVED | UNRESOLVED | UNRESOLVED | UNRESOLVED - no configured direction found. |
| Steer inversion | UNRESOLVED | UNRESOLVED | UNRESOLVED | UNRESOLVED | UNRESOLVED - no configured direction found. |
| CANcoder direction | VERIFIED | UNRESOLVED | UNRESOLVED | UNRESOLVED | FL direction was verified on hardware; FR/BL/BR direction values were not individually supplied. |
| Absolute offset | UNRESOLVED | UNRESOLVED | UNRESOLVED | UNRESOLVED | UNRESOLVED - no calibration value found. |
| Neutral mode | UNRESOLVED | UNRESOLVED | UNRESOLVED | UNRESOLVED | UNRESOLVED - adapter only refreshes default configuration. |
| Current limits | UNRESOLVED | UNRESOLVED | UNRESOLVED | UNRESOLVED | UNRESOLVED - no current-limit configuration found. |
| Ramp rates | UNRESOLVED | UNRESOLVED | UNRESOLVED | UNRESOLVED | UNRESOLVED - no ramp configuration found. |
| Connectivity | VERIFIED | VERIFIED | VERIFIED | VERIFIED | All four drive, steer, and CANcoder devices were online in the supplied 14/14 CTRE-device verification. |
| Configuration health | VERIFIED | VERIFIED | VERIFIED | VERIFIED | No unexpected faults were reported during the supplied hardware verification. This does not define unverified configuration values. |

## Pigeon2

| Item | Value | Classification and evidence |
| --- | --- | --- |
| CAN ID | 20 | VERIFIED - hardware map v2.0 and `kPigeonCanId` agree. |
| CAN bus | `rio` | VERIFIED - supplied hardware verification identified the CAN bus as `rio`. |
| Mounting orientation | UNRESOLVED | UNRESOLVED - no authoritative physical mounting record found. |
| Yaw convention | UNRESOLVED | UNRESOLVED - adapter reports raw yaw without defining field-relative convention. |
| Connectivity | VERIFIED | VERIFIED - Pigeon2 online in the supplied hardware verification. |
| Configuration health | VERIFIED | VERIFIED - no unexpected faults reported; no mounting or yaw configuration is inferred. |

## Provisional Values

| Value | Classification and boundary |
| --- | --- |
| `kMaxWheelSpeedMetersPerSecond = 4.0` | PROVISIONAL - inherited software output-pipeline cap, not a hardware commissioning value. |
| Initial drive ratio `7.85:1` | HISTORICAL / SUPERSEDED - initial provisional value before repeated physical 20-rotation measurement tests; not current. |

No provisional current hardware identifiers, inversions, offsets, neutral modes, current limits,
ramp rates, bus values, or orientation values were invented. The only historical provisional ratio
is `7.85:1`; it is retained for traceability and is not a current configuration value.

## Drive Ratio Commissioning History

```text
7.85:1
  -> initial provisional assumption before physical measurement
  -> repeated 20-motor-rotation / wheel-rotation hardware tests
  -> measured installed ratio = 6.75:1
  -> Constants / CTRE conversion updated
  -> real-robot distance validation confirmed the commissioned configuration
```

`6.75:1` is authoritative and current. `7.85:1` is obsolete historical data only.

## Hardware Verification Record

- 14/14 CTRE devices detected.
- CAN bus identified as `rio`.
- All TalonFX, all CANcoders, and Pigeon2 online.
- No duplicate CAN IDs and no unexpected faults.
- Robot remained Disabled throughout commissioning; no unintended motor actuation occurred.
- CANcoder signals updated correctly.
- FL CANcoder sensor direction verified on hardware.
- FR, BL, and BR hardware checks passed.
- Pigeon2 communication passed.

This record verifies observed connectivity and commissioning behavior. It does not promote
unverified steer ratios, inversion settings, absolute offsets, neutral modes, current limits, ramp
rates, mounting orientation, or yaw convention into configuration values.

## Read-Only Commissioning Path Audit

1. `SwerveModuleIOCTRE.updateInputs()` refreshes drive, steer, and CANcoder status signals and copies connectivity/configuration health into `SwerveModuleIOInputs`.
2. `GyroIOPigeon2.updateInputs()` refreshes Pigeon2 signals and copies connectivity/configuration health into `GyroIOInputs`.
3. `SwerveSubsystem.periodic()` refreshes all five snapshots and creates an immutable `SwerveObservation`.
4. Existing telemetry publishes the observation fields without writing configuration or commanding outputs.

The inherited CTRE adapter constructor calls its existing safe-stop method. This audit adds no new
commissioning actuation and does not call `setDriveOutput()` or `setSteerOutput()`.
