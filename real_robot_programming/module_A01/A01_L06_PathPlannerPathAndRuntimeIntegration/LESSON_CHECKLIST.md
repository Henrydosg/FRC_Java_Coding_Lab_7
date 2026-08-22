# A01_L06 - PathPlanner Path and Runtime Integration - Checklist

Status: `COMPLETE / FROZEN / READ-ONLY`  
Previous lesson: `A01_L05_HolonomicTrajectoryFollowing - COMPLETE / FROZEN / READ-ONLY`  
Git: user-owned; not run by Codex

## Governance and Activation

- [x] A01 ADR identity and lesson order reviewed.
- [x] L06 directory identity is `A01_L06_PathPlannerPathAndRuntimeIntegration`.
- [x] Frozen L05 was inherited; L01-L05 remain COMPLETE / FROZEN / READ-ONLY.
- [x] L06 inherited baseline build was user-verified `BUILD SUCCESSFUL`.
- [x] Java 17 / PathPlannerLib `2026.1.2` compatibility gate passed.
- [x] Design-Lock Amendment passed for the learning-only RobotConfig baseline.
- [x] Final Design-Lock passed.
- [x] Existing official `PathplannerLib.json` `2026.1.2` vendordep is preserved.
- [x] Separate Architect authorization for implementation was supplied after
      activation.
- [x] L06 implementation complete for the approved narrow scope.
- [x] L06 focused tests complete: `18/18` PASS.
- [x] Full regression and clean build after implementation PASS from
      user-supplied evidence.
- [x] CAN hardware blocker closed by user after physical repair/replacement.
- [x] Final transition guide and lesson closure.

## Locked Path and Frame Contract

- [x] Create only `A01_L06_OneMeter_Forward.path` after implementation
  authorization.
- [x] Place it only at `src/main/deploy/pathplanner/paths/`.
- [x] Keep the asset in canonical Blue frame.
- [x] Use start `(0.000, 0.000, 0 deg)` and goal `(1.000, 0.000, 0 deg)`.
- [x] Use constraints `0.50 m/s`, `1.00 m/s^2`, `0.75 rad/s`, and
  `1.50 rad/s^2`.
- [x] Keep markers, rotation targets, zones, pathfinding, replanning, and
  multiple routines absent.
- [x] Apply the A01/L04 Blue/Red transform exactly once.
- [x] Prove PathPlanner flipping/mirroring is absent or disabled.

## RobotConfig Classification

Verified inherited inputs: wheel radius `0.0508 m`; drive gearing `6.75:1`;
Kraken X60; one drive motor/module; drive current limit `70 A`; wheelbase and
trackwidth `0.5461 m`; module offsets `+/-0.27305 m`; and FOC disabled in the
CTRE runtime path.

**PROVISIONAL — LEARNING/SIMULATION ONLY — NOT MEASURED — NOT FINAL**

- [ ] mass `45.0 kg`
- [ ] MOI `5.0 kg·m²`
- [ ] maximum drive velocity `4.0 m/s`
- [ ] wheel COF `1.0`

The four values above are not measured, verified, calibrated, final, or
competition-authoritative. They may be used only for conservative L06
learning/Simulation and require later revalidation.

## Adapter and Safety Gates

- [x] Load one path with the approved PathPlannerLib `2026.1.2` API.
- [x] Validate asset presence, schema, generated trajectory, states, and finite
  timing before scheduling motion.
- [x] Adapt validated states to the unchanged L05 follower boundary.
- [x] Preserve L05 command requirements and centralized `SwerveSubsystem.stop()`.
- [x] Fail closed for missing/malformed asset, generation failure, null or
  nonfinite state/timing, incompatible API, mode loss, cancellation, timeout,
  invalid pose/observation/output, and submission failure.
- [x] Preserve Autonomous+Enabled-only motion and no automatic restart.

## Required Tests and Simulation

- [x] Focused valid-load and deterministic state/endpoint tests.
- [x] Focused missing/malformed/generation-failure/finite-data tests.
- [x] Focused exactly-once transform/no-double-transform tests for Blue and Red.
- [x] Focused requirements, mode-loss, cancellation, timeout, stop, and
  no-restart tests.
- [x] Inherited L01-L05 regression and clean build pass.
- [x] Blue Simulation executes the one known path from the accepted start pose.
- [x] Red Simulation executes the transformed path from the accepted start
  pose with no double transform.
- [ ] Record exact endpoint tolerance and stop-safe evidence for the final
  Simulation acceptance item; current user evidence confirms approximately
  one-metre Blue and alliance-transformed motion, reset, and valid EstimatedPose.

The unchecked measurement item is retained as an evidence limit, not a failed
gate: no centimeter-level endpoint claim is made. Simulation is user-supplied
PASS for the governed learning behavior.

## Forbidden Scope

AutoBuilder, chooser/multiple autos, NamedCommands, event markers, dynamic
replanning, pathfinding, vision/AprilTags, mechanism coordination, subsystem
or IO redesign, telemetry architecture changes, and L01-L05 modifications are
forbidden.

## Verification State

- Simulation: `PASS` - user supplied Blue and alliance-transformed path motion,
  Glass reset verification, and valid EstimatedPose evidence.
- Driver Station / Glass: `PASS` for the supplied Simulation/Glass evidence.
- CAN hardware blocker: `CLOSED` by user after physical repair/replacement.
- Real-Robot Execution Before Recalibration: `PASS / USER-CONFIRMED` - the L06
  one-meter autonomous was run on both Blue and Red before the latest offset
  recalibration.
- Post-Recalibration Real-Robot Verification: `PASS / USER-CONFIRMED` - the
  user physically executed the one-meter autonomous after the latest Swerve
  zero-offset recalibration on both Blue and Red.
- [x] Record the pre-recalibration Blue and Red physical execution accurately.
- [x] Record post-recalibration Simulation PASS separately.
- [x] Record post-recalibration Blue and Red one-meter autonomous execution as
      user-supplied physical PASS evidence.
- [x] Record the Blue observation of slight endpoint overshoot followed by a
      small closed-loop reverse correction and settling near the intended
      endpoint.
- [ ] Measure exact physical endpoint accuracy and establish an exact `1.000 m`
      claim; this remains outside the supplied evidence.
- [ ] Perform final PID/feedforward and physical-model tuning; RobotConfig mass,
      MOI, wheel COF, and maximum drive velocity remain provisional.
- Git commit/push: user-owned and not run by Codex.
- [x] Implementation, required tests, Simulation, post-recalibration Blue/Red
      physical verification, documentation, and governance closure are complete;
      mark L06 COMPLETE/FROZEN without claiming exact endpoint precision.
