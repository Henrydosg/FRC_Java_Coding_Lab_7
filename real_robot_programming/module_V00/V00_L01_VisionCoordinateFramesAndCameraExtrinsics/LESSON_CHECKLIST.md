# V00_L01 - Vision Coordinate Frames and Camera Extrinsics - Checklist

Status: `COMPLETE / FROZEN / READ-ONLY`  
Previous lesson: `A01_L09_PathPlannerNamedCommandsAndEventMarkers - COMPLETE / FROZEN / READ-ONLY`  
Git: user-owned; not run by Codex

## Activation and Inheritance

- [x] AGENTS.md, root README, and V00 ADR reviewed.
- [x] Authoritative English Documents A/B/C reviewed.
- [x] Frozen Backbone and Frozen Interface Contract reviewed.
- [x] Frozen A01_L09 documentation, architecture, source, and tests reviewed.
- [x] Exact V00_L01 roadmap identity verified.
- [x] Copied project identity verified.
- [x] Generated build artifacts were removed before the baseline and recreated
      by it.
- [x] User-verified baseline build PASS: `BUILD SUCCESSFUL in 1m 4s`.
- [x] User-verified inherited tests PASS; XML reports show `446/446 PASS`.
- [x] Inherited source, tests, deploy assets, and dependencies are byte-identical.
- [x] V00_L01 is the only IN_PROGRESS / EDITABLE lesson.

## Architecture and Design Lock

- [x] Frozen control and immutable observation flows are preserved.
- [x] RobotContainer remains composition root only.
- [x] SwerveSubsystem remains sole `SwerveDrivePoseEstimator` owner.
- [x] SwerveSubsystem retains localization, EstimatedPose, stop, and future
      fusion-entry ownership.
- [x] Autonomous and AutoBuilder consume only `getEstimatedPose()`.
- [x] L04 remains the sole alliance-transform owner.
- [x] Vision geometry uses the canonical field frame and is not alliance-flipped.
- [x] Field, robot, camera, and AprilTag frames are defined in WPILib NWU.
- [x] `robotToCamera` direction and inverse are unambiguous.
- [x] Axis, sign, angle, and unit conventions are explicit.
- [x] Camera extrinsic configuration has one smallest future authority.
- [x] Unknown physical X/Y/Z and roll/pitch/yaw remain TBD; no values invented.
- [x] No vendor is selected.
- [x] L02, L03, L04, L05, L06, L07, L08, and L09 boundaries remain deferred.

## Current Task Scope

- [x] Documentation activation and approved implementation completed.
- [x] Transition guide created, maintained, and finalized PASS.
- [x] One pure `VisionFrameTransform` production helper added.
- [x] One focused `VisionFrameTransformTest` class added.
- [x] No configuration or PathPlanner asset modified.
- [x] No frozen A01_L09 or earlier lesson modified.
- [x] No vision simulation, camera integration, deployment, or fusion performed.
- [x] No Git/GitHub operation performed.

## Future Implementation and Verification

- [x] ChatGPT-approved implementation design followed.
- [x] Pure vendor-neutral frame-transform production delta implemented.
- [x] Focused deterministic frame/transform tests implemented.
- [x] compileJava PASS / Codex direct verification.
- [x] compileTestJava PASS / Codex direct verification.
- [x] Focused tests `18/18 PASS` / Codex direct verification.
- [x] Full inherited regression `446/446 PASS` / Codex direct verification.
- [x] Full suite `464/464 PASS` / Codex direct verification.
- [x] Clean build PASS / Codex direct verification: `BUILD SUCCESSFUL in 29s`;
      seven actionable tasks executed.
- [x] WPILib VS Code Build Robot Code PASS / user verified:
      `BUILD SUCCESSFUL`.
- [x] Simulation marked NOT APPLICABLE for pure L01 geometry scope.
- [x] Driver Station / Glass marked NOT APPLICABLE; no runtime observation.
- [x] Real Robot marked NOT APPLICABLE; no camera hardware/calibration claim.
- [x] English learning guide created.
- [x] Vietnamese explanatory learning guide created.
- [x] Transition guide maintained through implementation and verification.
- [x] Exact 20-file implementation report reconciled: nine intended final
      files plus eleven removed PDF text-extraction scratch files.
- [x] Generated `build/`, `.gradle/`, and `bin/` outputs excluded from
      publication by repository ignore rules.
- [x] Inherited source/tests, Gradle files, vendordeps, PathPlanner assets,
      `.wpilib`, and `.vscode` verified unchanged.
- [x] Final architecture review PASS.
- [x] V00_L01 completion/freeze explicitly approved.
- [x] V00_L02 remains NOT CREATED / NOT STARTED.
