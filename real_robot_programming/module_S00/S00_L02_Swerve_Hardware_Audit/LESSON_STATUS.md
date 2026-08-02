# Lesson Status

- Lesson: S00_L02_Swerve_Hardware_Audit
- Previous Lesson: S00_L01_Swerve_Architecture_Foundation
- Source: S00_L01_Swerve_Architecture_Foundation
- Status: IN_PROGRESS

## Architecture

- Architecture Review: PASS - Documentation-only hardware audit; S00_L01 Java remains byte-for-byte unchanged and no package, dependency, vendor, or behavior change was introduced.

## Verification

- Baseline Build: PASS - User verified the inherited S00_L01 baseline build before the S00_L02 audit.
- Build: NOT TESTED
- Simulation: NOT TESTED
- Driver Station / Glass: NOT TESTED
- Real Robot: NOT TESTED
- Transition Guide: PASS - `docs/S00_L01_to_S00_L02_Step_by_Step.md` created.
- Git Commit: NOT TESTED
- Git Push: NOT TESTED

## Known Issues

- Swerve module model is unknown.
- Drive and steer motors/controllers are unknown.
- Absolute encoder and IMU are unknown.
- Swerve CAN bus and CAN IDs are unknown.
- Wheel diameter, drive and steer gear ratios, wheelbase, and track width are unknown.
- Module order, offsets, and inversions are unknown.
- No Swerve or IMU hardware vendor dependency is present.
