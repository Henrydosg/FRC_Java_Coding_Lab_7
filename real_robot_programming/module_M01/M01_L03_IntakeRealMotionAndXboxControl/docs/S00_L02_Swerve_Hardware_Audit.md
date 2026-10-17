# S00 L02 Swerve Hardware Audit

## Objective

Record only repository-verifiable Swerve and IMU hardware facts before any IO contract, vendor dependency, constants, or implementation is designed. Unknown values remain explicitly unresolved.

## Evidence Standard

- `VERIFIED` means an authoritative project record explicitly establishes the value.
- `UNKNOWN` means the reviewed project records do not establish the value.
- User-verified hardware information is recorded as `VERIFIED` when it is consistent with the designated Swerve hardware maps.
- A hardware vendor and a software vendor dependency are distinct facts.

## Sources Reviewed

- `docs/Swerve_Robot_Hardware_Map_v2.0.docx`
- `docs/Swerve_Robot_Hardware_Map_v2.0.pdf`
- User-verified hardware evidence supplied for this update
- S00_L02 `vendordeps` and `build.gradle`

## Audit Results

| Required information | Status | Verified value or evidence gap |
| --- | --- | --- |
| Module model | VERIFIED | West Coast Products WCP Legacy. |
| Drive motor/controller | VERIFIED | Kraken X60 with TalonFX. Drive CAN IDs: FL 21, FR 24, BL 27, BR 30. |
| Steer motor/controller | VERIFIED | Kraken X60 with TalonFX. Steer CAN IDs: FL 22, FR 25, BL 28, BR 31. |
| Absolute encoder | VERIFIED | CANcoder. Encoder CAN IDs: FL 23, FR 26, BL 29, BR 32. |
| IMU | VERIFIED | CTRE Pigeon2, CAN ID 20. |
| CAN bus / CAN IDs | UNKNOWN | The named CAN bus is not established. Verified CAN IDs: PDP 0; Pigeon2 20; FL 21/22/23; FR 24/25/26; BL 27/28/29; BR 30/31/32. |
| Wheel diameter | VERIFIED | 4.0 in. |
| Wheel radius | VERIFIED | 2.0 in. |
| Drive gear ratio | VERIFIED | 6.75:1 final measured/commissioned value. The initial provisional value was 7.85:1, superseded by repeated physical 20-motor-rotation / wheel-rotation measurement tests. |
| Steer gear ratio | UNKNOWN | No reviewed record provides the Swerve steering reduction. |
| Wheelbase | VERIFIED | 21.5 in. |
| Track width | VERIFIED | 21.5 in. |
| Module order | VERIFIED | FL, FR, BL, BR as identified by the hardware map CAN table. |
| Offsets | UNKNOWN | No reviewed record provides calibrated absolute steering offsets. |
| Inversions | UNKNOWN | No reviewed record defines drive, steer, encoder, or IMU inversion conventions. |
| Vendor dependencies | UNKNOWN | CTRE hardware is verified, but the required CTRE software dependency version is not specified. S00_L02 currently contains only `WPILibNewCommands.json`; no CTRE dependency is installed. |

## Drive Ratio Commissioning History

- Initial provisional value: `7.85:1` - **Initial provisional value - superseded by physical measurement.**
- Verification method: repeated physical tests comparing 20 motor rotations with wheel rotation, repeated multiple times.
- Final measured/commissioned value: `6.75:1`.
- Current status: `6.75:1` is authoritative and current. `7.85:1` is obsolete historical data only.

## Missing Evidence Required Before Contract Design

- Per-module absolute steering calibration offsets.
- Verified drive, steer, encoder, and IMU inversion conventions.
- Name of the CAN bus that carries the verified device IDs.
- Steer gear ratio.
- Exact CTRE Phoenix 6 dependency version required by the verified hardware.

## Architecture Decision

- Result: VERIFIED
- Scope: Documentation-only audit.
- Impact: No Java, package, dependency, hardware, RobotContainer, IO, Observation, telemetry, or control-flow change.
- Decision: Do not create `io/swerve`, `observation/swerve`, constants, vendor implementations, or vendor dependencies until the missing hardware evidence is verified and a separate architecture review approves the smallest viable contract.
