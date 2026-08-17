# A01_L01 Autonomous Starting-Pose and Field-Frame Contract - Checklist

Status: `COMPLETE / FROZEN / READ-ONLY`  
Freeze State: `FROZEN`  
Architecture Review: `PASS`  
Lesson: `A01_L01_AutonomousStartingPoseAndFieldFrameContract`  
Previous lesson: `A00_L04_AutonomousMotionSafetyGating` - `COMPLETE / FROZEN / READ-ONLY`  
Git: user-owned; not run by Codex

## Governance and Inheritance

- [x] Approved A01 ADR and lesson order reviewed.
- [x] A01_L01 identity is normalized from copied A00_L04 documentation.
- [x] Frozen A00_L04 is recorded as the immediate predecessor.
- [x] A00_L04 remains frozen and unchanged.
- [x] Frozen Backbone and Frozen Interface Contract remain unchanged.
- [x] `RobotContainer` remains the composition root.

## Single Concept

- [x] One concept: authoritative autonomous reference-frame initialization
  from a validated starting pose and heading.
- [x] Starting-pose ownership and field-frame semantics are limited to the
  A01_L01 contract.

## Safety and Frozen Boundaries

- [x] A00_L04 Autonomous+Enabled safety invariant remains authoritative.
- [x] Centralized `SwerveSubsystem.stop()` remains authoritative.
- [x] Localization remains owned by `SwerveSubsystem`.
- [x] No A01_L02 or later capability is claimed.
- [x] No Java, tests, Gradle, vendordep, or hardware changes were made by
  this normalization.

## Verification Gates

- [x] Architecture audit and final architecture review completed.
- [x] Inherited Java 17 baseline verified for A01_L01.
- [x] Focused deterministic tests verified.
- [x] Full regression and clean build verified.
- [x] Simulation verified.
- [x] Driver Station / Glass verified where applicable.
- [x] Real-robot verification recorded as `PASS` for the A01_L01
  starting-pose and field/reference-frame scope.
- [x] Java 17 verification recorded as `VERIFIED`.
- [x] Full tests and clean full build recorded as `PASS`.
- [x] Disabled telemetry, valid poses, runtime pose update, visible reset, and
  safe zero drivetrain output recorded as `PASS` in Driver Station / Glass.

## Explicit Exclusions

- [x] No pose-targeted autonomous motion.
- [x] No trajectory generation, sampling, or following.
- [x] No alliance transforms or mirroring.
- [x] No PathPlanner or AutoBuilder.
- [x] No vision or AprilTags.
- [x] No mechanism events or multi-step routines.
- [x] No competition readiness or final tuning claim.

## Documentation State

- [x] A01_L01 is `COMPLETE / FROZEN / READ-ONLY`.
- [x] Real-robot evidence is recorded without changing the active lesson state.
- [x] Cases 1-7 are documented from supplied evidence only.
- [x] Disabled reset establishes available/valid Pose and EstimatedPose.
- [x] The exposed reset command is `ResetKnownFieldPoseCommand`.
- [x] Autonomous without a fresh accepted reset fails closed.
- [x] One-shot authorization is consumed and a second Autonomous enable does
  not start a second run.
- [x] Enabled reset attempts are safely rejected/blocked without pose
  corruption or drivetrain motion.
- [x] Field visualization, gyro health, and module health observations are
  recorded within scope.
- [x] Robot-mode transitions were tested and field visualization updated
  consistently.
- [x] Driver Station E-Stop and CommandScheduler loop-overrun observations
  are retained as test context only, not classified as defects.
- [x] Known Issues records both observations as non-blocking follow-up only.
- [x] A01_L01 is `COMPLETE / FROZEN / READ-ONLY`.
- [x] Real Robot: `PASS`, limited to the seven supplied A01_L01 cases.
- [x] Case 1: Disabled baseline and hardware health recorded.
- [x] Case 2: Autonomous without a fresh accepted reset fails closed.
- [x] Case 3: Disabled known starting-pose reset establishes valid Pose and
  EstimatedPose without drivetrain motion.
- [x] Case 4: Fresh accepted reset permits one bounded Autonomous run.
- [x] Case 5: Second Autonomous enable without another reset produces no
  second motion.
- [x] Case 6: A new Disabled reset permits one new Autonomous session.
- [x] Case 7: Enabled Teleop reset attempt is safely rejected/blocked.
- [x] Transition Guide is `FINAL / PASS`.
- [ ] User Git commit.
- [ ] User Git push.
