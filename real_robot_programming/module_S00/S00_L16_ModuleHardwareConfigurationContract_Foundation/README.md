# Framework Version

2.1

# Lesson

S00_L16_ModuleHardwareConfigurationContract_Foundation

# Previous Lesson

S00_L15_SingleModuleOpenLoopCommissioning_Foundation

# Source Lesson

S00_L15_SingleModuleOpenLoopCommissioning_Foundation - COMPLETE / FROZEN / READ-ONLY

# Status

IN_PROGRESS

# Concept

This lesson defines one deterministic CTRE module hardware configuration contract for the drive
TalonFX, steer TalonFX, and CANcoder through the existing Constants and IO boundaries.

The contract will define desired configuration ownership, configuration-result handling, and
configuration-health reporting without moving vendor APIs outside IO implementations.

# Architecture Audit

PASS. The completed lesson preserves the Frozen Backbone:

Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware

hardware -> IOInputs -> subsystem / estimator -> immutable Observation -> telemetry -> NT4 / Glass / log

RobotContainer remains the composition root. Telemetry remains read-only. The existing SwerveModuleIO
interface remains vendor-neutral. Configuration ownership and health reporting remain inside the
existing IO boundary.

# Scope

- Deterministic configuration for one CTRE Swerve module.
- Drive TalonFX configuration.
- Steer TalonFX configuration.
- CANcoder configuration.
- Configuration-result handling and health semantics inside the IO boundary.
- Preservation of the existing single-module commissioning safety boundary.

## Final Implementation Record

- `Constants.java` remains the configuration authority for the approved gear ratios, inversions,
  CANcoder directions, four calibrated offsets, and confirmed current limits.
- `SwerveModuleIOCTRE.java` constructs and applies TalonFX and CANcoder configurations, refreshes
  readback, compares required fields, reports configuration health, and rejects nonzero output when
  the module configuration is unhealthy.
- Configuration verification requires apply `StatusCode.OK`, refresh `StatusCode.OK`, exact
  sensor-direction equality, and a CANcoder magnet-offset comparison modulo one rotation.
- The accepted offset tolerance is one observed CANcoder quantization step:
  `1 / 4096 = 0.000244140625` rotations.
- Failure-only startup diagnostics report module, CANcoder ID, statuses, expected/actual fields,
  raw and wrapped differences, and final health.

## Final Hardware Values

| Module | CANcoder offset (rotations) |
| --- | ---: |
| Front Left | `0.067138671875` |
| Front Right | `0.02099609375` |
| Back Left | `0.464599609375` |
| Back Right | `-0.052978515625` |

## Final Verification Record

- Architecture Audit: PASS.
- Frozen Backbone: PASS.
- Interface Contract: PASS.
- Focused tests: `10/10 PASS`.
- Full test suite: `58/58 PASS`.
- Clean full build: `BUILD SUCCESSFUL`.
- Simulation: PASS.
- Glass: PASS.
- Driver Station: PASS.
- Real-robot configuration apply/readback: PASS.
- All four modules reported drive, steer, and encoder connectivity true.
- All four modules reported drive, steer, and encoder configuration health true.
- Front Left drive-positive, drive-negative, steer-positive, steer-negative, and safe-stop checks:
  PASS.

# Locked Roadmap and Exclusions

- Preserve inheritance from the frozen S00_L15 source lesson.
- Preserve the Frozen Backbone and package responsibilities.
- Do not add closed-loop control, PID, feedforward, Motion Magic, kinematics, odometry, driver input,
  or four-module actuation.
- Do not modify previous lessons.
- Do not add commissioning commands for other modules.
- Implementation and verification are complete. Git, push, and freeze remain user-owned pending
  steps.

# Current State

- Baseline build: PASS, user-reported inherited baseline.
- Architecture audit: PASS.
- Implementation: COMPLETE.
- Verification: COMPLETE.
- Documentation finalization: COMPLETE.
- Hardware configuration values: synchronized to the approved calibration record.
- User owns build, tests, simulation, Driver Station / Glass, real-robot verification, and Git.

## Deferred Scope

The following remain outside S00_L16 and are reserved for later lessons:

- PID and feedforward.
- FusedCANcoder closed-loop feedback.
- Motion Magic.
- Closed-loop drive or steer requests.
- Four-module state actuation.
- Kinematics and odometry.
