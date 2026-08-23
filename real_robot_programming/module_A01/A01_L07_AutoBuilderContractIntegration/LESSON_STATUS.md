# Lesson Status

## Identity

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L07_AutoBuilderContractIntegration`
- Title: `A01_L07 - AutoBuilder Contract Integration`
- Previous Lesson: `A01_L06_PathPlannerPathAndRuntimeIntegration`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Lesson Goal: integrate AutoBuilder against the existing pose, reset, measured-speed, output, controller, RobotConfig, requirement, alliance, and safety contracts.

## Activation and Baseline Gates

- Governance: `PASS` - AGENTS.md, README, Documents A/B/C, the approved A01 ADR,
  frozen L01-L06, the pre-activation record, and the alliance-transform Design
  Lock were reviewed.
- Directory Identity: `PASS` - `A01_L07_AutoBuilderContractIntegration`.
- Strict Inheritance: `PASS` - copied from complete/frozen A01_L06; production
  Java and tests matched the L06 inheritance baseline before the authorized L07
  delta.
- Generated Artifact Removal: `PASS` - inherited generated artifacts were
  removed from the copied L07 only.
- Baseline Build: `PASS` - user supplied Java 17 baseline evidence.
- `compileJava`: `PASS` - user supplied.
- `compileTestJava`: `PASS` - user supplied.
- Tests: `PASS` - user supplied.
- Clean Build: `PASS` - user supplied.
- Architecture Review: `PASS` - approved A01 ADR and alliance-transform Design
  Lock; ADR change not required.
- Documentation Activation: `PASS` - L07 identity and activation documents
  are present; transition record created.

## Implementation and Verification Gates

- Production Implementation: `PASS` - approved adapter, execution-path factory,
  trajectory-adapter exposure, and RobotContainer wiring only.
- Focused L07 Tests: `PASS` - execution-path factory, AutoBuilder configuration,
  and autonomous lifecycle/requirement coverage.
- Full Regression After L07 Delta: `PASS` - 424 tests, 0 failures.
- Build After L07 Delta: `PASS` - compileJava, compileTestJava, and clean build.
- Build Toolchain Evidence: `PASS` - Java 17 with `-PteamNumber=0`; the final
  clean build includes production compilation, test compilation, and tests.
- Simulation: `PASS / USER-SUPPLIED` - Blue and Red known one-meter
  AutoBuilder execution, pose validity, heading stability, exactly-one
  alliance transform, disable stop, and no automatic restart.
- Driver Station / Glass: `PASS / USER-SUPPLIED SIMULATION EVIDENCE` - supplied
  EstimatedPose and heading observations are recorded; this is not real-robot
  evidence.
- Real Robot: `PASS / USER-CONFIRMED` - the user explicitly confirmed physical
  execution of the current L07 AutoBuilder Contract Integration lesson.
- Transition Guide: `FINAL / PASS` - implementation, Simulation, and
  user-confirmed Real Robot evidence are recorded.
- Closure Audit: `PASS` - all required A01_L07 implementation, build/test,
  Simulation, documentation, and user-owned Real Robot gates passed.
- Git Commit: `NOT TESTED` - user-owned; Codex does not run Git.
- Git Push: `NOT TESTED` - user-owned; Codex does not run Git.

## Locked Architecture

- Transform owner: `A01/L04 FieldAllianceTransform`.
- AutoBuilder vendor flipping: `DISABLED`.
- `shouldFlipPath`: `false`.
- Execution path: fresh transformed `PathPlannerPath` with
  `preventFlipping = true`.
- Canonical path: Blue-frame and unchanged.
- Double transformation: forbidden.
- Requirement owner: existing `SwerveSubsystem`.
- Terminal safety: centralized `SwerveSubsystem.stop()` on every terminal and
  fault path, with no automatic restart.
- RobotConfig: reuse the existing named, provisional learning configuration.
- RobotContainer: composition root only.

## Current Swerve Authority

- Drive ratio: `6.75:1`.
- FL CANcoder offset: `+0.068603515625`.
- FR CANcoder offset: `+0.014404296875`.
- BL CANcoder offset: `+0.46240234375`.
- BR CANcoder offset: `-0.057373046875`.

## User-Owned L07 Simulation Evidence

- Blue autonomous: `PASS` - Disabled heading reference established, known
  starting pose reset accepted, valid EstimatedPose observed, and the known
  one-meter autonomous executed successfully.
- Red autonomous: `PASS` - final EstimatedPose was `X=15.535553 m`,
  `Y=8.069000 m`, `Heading=-180.000000 deg`.
- Exactly-one alliance transform: `PASS` - the Red result is consistent with
  the locked `REBUILT_WELDED` transform using `L=16.541 m` and `W=8.069 m`.
- Disable/mode-loss stop: `PASS` - Blue stopped near `X=0.400765 m`,
  `Y=0.000000 m`, `Heading=0.000000 deg` after Disable during motion.
- No automatic restart: `PASS` - re-enable without BACK, a new known-pose
  reset, or fresh readiness did not resume motion.
- Pose validity and heading stability: `PASS`.

The expected Red endpoint is approximately `(15.541 m, 8.069 m, +/-180 deg)`.
The observed `-180 deg` is equivalent to `+180 deg` modulo 360 degrees. The
small X difference is Simulation geometry evidence only, not precision
characterization.

## Known Issues and Deferred Scope

- L07 Simulation, supplied telemetry evidence, and user-confirmed Real Robot
  evidence pass. L07 is COMPLETE / FROZEN / READ-ONLY.
- Exact real-robot endpoint accuracy is not claimed. Final PID/feedforward and
  physical-model characterization remain deferred.
- The copied pre-activation design record remains historical evidence; its
  unresolved pre-activation state is superseded by the approved Design Lock and
  implementation recorded above.
- L06 post-recalibration Blue and Red one-meter autonomous execution is now
  recorded as user-supplied PASS evidence. Exact endpoint precision and final
  PID/feedforward or physical-model tuning remain deferred; L06 remains frozen
  and is not modified by this L07 implementation state.
- Chooser, multiple routines, NamedCommands, event markers, mechanisms, vision,
  AprilTags, pathfinding, replanning, CTRE changes, CAN changes, and source
  changes to L01-L06 remain excluded.
