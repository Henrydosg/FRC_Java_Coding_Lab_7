# Lesson Status

## Identity

- Module: `V00 - AprilTag Vision Observation and Pose Fusion`
- Lesson: `V00_L01_VisionCoordinateFramesAndCameraExtrinsics`
- Title: `V00_L01 - Vision Coordinate Frames and Camera Extrinsics`
- Previous Lesson: `A01_L09_PathPlannerNamedCommandsAndEventMarkers`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Lesson Goal: define the vendor-neutral WPILib field, robot, camera, and
  AprilTag coordinate-frame contract and the direction of `robotToCamera`.

## Governance and Activation

- Governance: `PASS` - AGENTS.md, root README, the approved V00 ADR,
  authoritative English Documents A/B/C, Frozen Backbone, Frozen Interface
  Contract, frozen A01_L09 documentation, architecture, source, and tests, and
  the copied V00_L01 project were reviewed.
- Authoritative Roadmap Entry: `PASS` - V00_L01 is the first lesson in the
  approved V00 roadmap.
- Source Lesson: `PASS` - A01_L09 remains COMPLETE / FROZEN / READ-ONLY.
- Inheritance: `PASS` - 125 inherited source, test, deploy, Gradle-wrapper, and
  vendor-dependency files are byte-identical to A01_L09.
- Generated-Artifact Cleanup: `PASS` - the copied project predates the current
  `build/` and `.gradle/` directories; those directories were recreated by the
  inherited baseline build after the required cleanup.
- Directory Identity: `PASS` -
  `V00_L01_VisionCoordinateFramesAndCameraExtrinsics`.
- Single Active Lesson: `PASS` - V00_L01 was the only editable lesson during
  implementation and is now closed; no V00 lesson is IN_PROGRESS.
- Architecture Review: `PASS` - the design lock preserves the Frozen Backbone,
  observation flow, Swerve localization ownership, autonomous pose-consumer
  boundary, and V00 lesson boundaries.
- Design Lock: `PASS / IMPLEMENTED AS APPROVED`.
- Production Implementation: `PASS` - one pure vendor-neutral helper with no
  runtime integration or physical camera values.

## Build and Automated Verification

- Baseline Build: `PASS / USER-VERIFIED`.
- Baseline Evidence: `BUILD SUCCESSFUL in 1m 4s`; `6 actionable tasks: 6
  executed`.
- Baseline Tests: `PASS / USER-VERIFIED`; current inherited XML reports record
  `446/446 PASS`, zero failures, errors, or skips.
- compileJava: `PASS / CODEX-VERIFIED` using the WPILib 2026 Java 17 runtime.
- compileTestJava: `PASS / CODEX-VERIFIED`.
- Focused Tests: `18/18 PASS / CODEX-VERIFIED`.
- Inherited Regression: `446/446 PASS / CODEX-VERIFIED`, zero failures,
  errors, or skips.
- Full Test Suite: `464/464 PASS / CODEX-VERIFIED`, zero failures, errors, or
  skips.
- Clean Build: `PASS / CODEX-VERIFIED`; `BUILD SUCCESSFUL in 29s`; seven
  actionable tasks executed.
- WPILib VS Code Build Robot Code: `PASS / USER-VERIFIED`;
  `BUILD SUCCESSFUL`.

## Runtime Verification

- Simulation: `NOT APPLICABLE` - L01 contains only pure deterministic
  coordinate-frame/Transform3d geometry and no runtime Vision/camera behavior.
- Driver Station / Glass: `NOT APPLICABLE` - no runtime Vision observation or
  telemetry exists in L01.
- Real Robot: `NOT APPLICABLE` - no real Vision adapter or physical
  `robotToCamera` calibration values exist. No camera-hardware PASS is claimed.

## Locked Architecture

- Frozen control flow and immutable observation flow remain unchanged.
- `RobotContainer` remains the composition root only.
- `SwerveSubsystem` remains the sole owner of `SwerveDrivePoseEstimator`,
  localization state, EstimatedPose, and the future vision-fusion entry point.
- Autonomous and AutoBuilder continue consuming only
  `SwerveSubsystem.getEstimatedPose()` and never camera data, VisionIO, or a
  vendor API.
- L04 remains the sole alliance-transform owner. Vision geometry uses the
  canonical WPILib field frame and is never alliance-flipped.
- L01 introduces coordinate-frame and camera-extrinsic semantics only.
- No camera/vendor is selected in V00_L01-L07.
- No VisionIO, AprilTag detection, field layout, pose estimation, measurement
  quality, timing, real adapter, simulation adapter, or fusion is implemented.

## Camera Extrinsic Gate

- Permanent configuration authority: `Constants.VisionConstants`, containing
  one immutable `Transform3d kRobotToCamera` only after all six physical mounting
  measurements are supplied and validated.
- Required measurements: X, Y, and Z translation plus roll, pitch, and yaw of
  the camera relative to the robot frame.
- Physical values: `TBD / USER MEASUREMENT REQUIRED`.
- No zero, identity, estimated, or example value may be installed as production
  calibration.

## Documentation and Git

- Transition Guide: `FINAL / PASS` -
  `docs/A01_L09_to_V00_L01_Step_by_Step.md` records activation, design lock,
  implementation, verification, changed-file audit, and closure.
- Git Commit: `NOT TESTED` - user-owned; Codex ran no Git operations.
- Git Push: `NOT TESTED` - user-owned; Codex ran no Git operations.

## Known Issues and Unclaimed Scope

- Actual camera mounting translation and orientation are not yet measured.
- No vendor, field layout, target observation, pose estimate, quality decision,
  timestamp policy, simulation, real-camera result, or fusion result is claimed.
- Exact localization accuracy, final covariance/rejection thresholds, final
  tuning, physical characterization, and competition readiness are unclaimed.
- A sandboxed compiler run could not read generated classpath entries; the
  same verification commands and the final clean build succeeded with normal
  filesystem access. This was an execution-environment constraint, not a
  repository blocker.

## Exact Changed-File Reconciliation

- Intended production delta: one new `VisionFrameTransform.java`.
- Intended test delta: one new `VisionFrameTransformTest.java`.
- Intended lesson documentation/activation delta: seven files.
- The implementation tool's 20-file report consisted of these nine intended
  files plus eleven temporary authoritative-PDF text-extraction files under
  `tmp/pdfs/`; all eleven scratch files and the temporary directory were
  removed before closure and are not publication content.
- Generated/IDE outputs in `build/`, `.gradle/`, and `bin/` are excluded by
  repository ignore rules and are not publication content.
- All inherited production and test files, Gradle configuration, vendordeps,
  PathPlanner assets, `.wpilib`, and `.vscode` remain byte-identical to frozen
  A01_L09.
- Unexpected files or modifications: `NONE`.

## Final Closure

- Final lesson state: `COMPLETE / FROZEN / READ-ONLY`.
- Frozen inheritance source for:
  `V00_L02_AprilTagFieldLayoutContract`.
- V00_L02: `NOT CREATED / NOT STARTED`.
