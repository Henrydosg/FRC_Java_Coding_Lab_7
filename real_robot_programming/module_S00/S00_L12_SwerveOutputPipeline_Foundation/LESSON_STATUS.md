# Lesson Status

## Identity

- Framework Version: 2.1
- Lesson: S00_L12_SwerveOutputPipeline_Foundation
- Previous Lesson: S00_L11_SwerveModuleStateOptimization_Foundation
- Source: S00_L11_SwerveModuleStateOptimization_Foundation
- Status: COMPLETE
- Freeze: FROZEN / READ-ONLY
- Architecture Review: PASS

## Current-Lesson Evidence

| Item | State | Evidence |
| --- | --- | --- |
| Architecture Review | PASS | One stateless pipeline preserves the Frozen Backbone, reuses existing kinematics and optimization helpers, and delegates desaturation to WPILib. |
| Implementation | PASS | `SwerveOutputPipeline` and its focused test class were added; only the active lesson was changed. |
| Baseline Build | PASS | User reported the inherited S00_L11/S00_L12 baseline build as PASS. |
| Focused Tests | PASS | 13/13 focused tests passed. |
| Build | PASS | Full `gradlew build` passed. |
| Simulation | NOT APPLICABLE | No runtime wiring or hardware-output path was added. |
| Driver Station / Glass | NOT APPLICABLE | No runtime wiring or telemetry path was added. |
| Real Robot | NOT APPLICABLE | No IO, motor output, or hardware behavior was added. |
| Documentation | PASS | README, lesson plan, lesson status, checklist, and transition guide were updated for S00_L12. |
| Transition Guide | PASS | `docs/S00_L11_to_S00_L12_Step_by_Step.md` was created. |
| Commit | PASS | Implementation commit `0295ac0`. |
| Push | PASS | Pushed to `origin/main`. |
| Freeze | PASS | S00_L12 is complete, frozen, and read-only. |

## Architecture Boundary

The pipeline is pure, deterministic, hardware-independent, and subsystem-owned. It performs robot-relative chassis-speed kinematics, per-module optimization, and wheel-speed desaturation in that order. It accepts four current angles, produces states in FL/FR/BL/BR order, and uses WPILib desaturation for the final speed limit.

## Constant Decision

No existing maximum wheel-speed constant was available in the inherited S00_L11 source. `Constants.SwerveConstants.kMaxWheelSpeedMetersPerSecond = 4.0` was added as the smallest clearly named lesson configuration constant. The 4.0 m/s value is provisional software baseline data, not verified hardware capability, and requires later hardware validation.

## Known Issues

- The configured 4.0 m/s cap is not a verified real-robot capability and requires later hardware validation.

## Git State

- Commit: PASS - `0295ac0`
- Push: PASS - `origin/main`
- Freeze: PASS - FROZEN / READ-ONLY

## Post-Freeze Note

The provisional 4.0 m/s software baseline remains documented for later hardware validation. It does not prevent lesson closure.
