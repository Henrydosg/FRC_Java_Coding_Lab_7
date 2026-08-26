# Lesson Status

## Identity

- Module: `V00 - AprilTag Vision Observation and Pose Fusion`
- Lesson: `V00_L01_VisionCoordinateFramesAndCameraExtrinsics`
- Title: `V00_L01 - Vision Coordinate Frames and Camera Extrinsics`
- Previous Lesson: `A01_L09_PathPlannerNamedCommandsAndEventMarkers`
- Authoritative Predecessor: `A01_L09 @ 6b243bb`
- Predecessor Commit Message: `Complete reconstructed A01_L09 named commands and event markers`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY / PUBLISHED` by authoritative User evidence
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN / READ-ONLY`
- Lesson Goal: pure canonical field/robot/camera frame and fixed camera-extrinsic mathematics

## Reconciliation Authority and History

- Controlled documentation reconciliation: `APPROVED` by Architect and User.
- Historical V00_L01 implementation baseline: `STALE / NON-AUTHORITATIVE`.
- Reason: it predated the final reconstructed A01_L09 predecessor and could not
  preserve the final accepted safety/event lineage.
- Reconstruction: `COMPLETE` through an isolated candidate derived from final
  A01_L09 and then transferred into the canonical lesson directory.
- Editable boundary: `NONE`; V00_L01 is `COMPLETE / FROZEN / READ-ONLY`.
- A01_L10: `PROHIBITED`.
- V00_L02: `SUSPENDED / READ-ONLY / UNMODIFIED`.

## Architecture and Inheritance Audit

- Frozen Backbone: `PASS / PRESERVED`.
- Frozen Interface Contract: `PASS / PRESERVED`.
- Documents A/B/C observation architecture: `PASS / PRESERVED`.
- RobotContainer composition-root role: `PASS / UNCHANGED`.
- Production inheritance: `PASS` - 73 files hash-identical to final A01_L09.
- Test inheritance: `PASS` - 56 files hash-identical to final A01_L09.
- Gradle, vendordeps, and deploy assets: `PASS / INHERITED UNCHANGED`.
- Final A01_L09 safety/event architecture: `PASS / INHERITED UNCHANGED`.
- Manual child-command lifecycle delegation: `ABSENT`.
- Historical stale Vision production/test locations: `ABSENT`.
- Historical `frc.robot.commands.AutonomousEventId`: `ABSENT`.
- Final `frc.robot.autonomous.AutonomousEventId`: `PRESENT / INHERITED`.

## V00_L01 Design Lock

- Production delta: one file,
  `src/main/java/frc/robot/vision/VisionFrameTransform.java`.
- Test delta: one file,
  `src/test/java/frc/robot/vision/VisionFrameTransformTest.java`.
- Package: `frc.robot.vision`.
- Role: stateless, non-instantiable, vendor-neutral WPILib geometry utility.
- Public API: `fieldToCamera(...)`, `cameraToRobot(...)`, and
  `fieldToRobotFromCamera(...)` only.
- Units/convention: meters, radians, right-handed WPILib NWU.
- Observation classification: `NOT AN OBSERVATION`.
- Runtime wiring: `NONE`.
- Deferred scope: VisionIO, vendors/hardware, AprilTag lookup, target quality,
  timing/latency, estimation/fusion, autonomous, PathPlanner, Swerve, and
  physical calibration values.

## Build and Verification

- Baseline Inheritance Audit: `PASS / CODEX READ-ONLY HASH AUDIT`.
- Baseline Build: `NOT SEPARATELY RECORDED` - no unsupported pre-feature build
  claim is added retroactively.
- Independent Mathematical Oracle Review: `PASS / CANDIDATE EVIDENCE`.
- Canonical Clean: `PASS / USER-VERIFIED / JAVA 17`.
- Focused `VisionFrameTransformTest`: `PASS / USER-VERIFIED`.
- Full Build: `PASS / USER-VERIFIED`.
- Accidental `-Recurse` artifact: `ABSENT / USER-VERIFIED`.
- Simulation: `NOT APPLICABLE` - pure deterministic geometry; no new runtime behavior.
- Driver Station / Glass: `NOT APPLICABLE` - no new telemetry or runtime observation.
- Real Robot: `NOT APPLICABLE` - no camera, adapter, fusion, drivetrain change, or actuation.

## Documentation and Closure

- README reconciliation: `COMPLETE`.
- LESSON_STATUS reconciliation: `COMPLETE`.
- LESSON_PLAN reconciliation: `COMPLETE`.
- LESSON_CHECKLIST reconciliation: `COMPLETE`.
- Transition Guide: `FINAL / PASS`.
- English Learning Guide: `FINAL / PASS`.
- Vietnamese Learning Guide: `FINAL / PASS`.
- Inherited documentation classification: `COMPLETE`; 61 inherited A01 files
  preserved, none deleted or rewritten.
- Final Architecture Review: `PASS`.
- Final Closure Review: `PASS / APPROVED`.
- Git Commit: `PENDING / USER OWNED`.
- Git Push: `PENDING / USER OWNED`.

## Known Issues and Unclaimed Scope

- Physical camera X/Y/Z/roll/pitch/yaw values remain unknown and explicitly
  deferred; no value is guessed.
- No camera/vendor, AprilTag lookup, target detection, pose accuracy, quality,
  latency, fusion, autonomous behavior, drivetrain behavior, endpoint accuracy,
  or competition-readiness result is claimed.
- Repository-level governance and lesson metadata record the final frozen state.
- Git publication remains pending and User-owned.

## Current Result

Implementation, canonical verification, documentation reconciliation, final
architecture review, and final closure review are PASS. V00_L01 is
`COMPLETE / FROZEN / READ-ONLY`. Git publication remains pending User
commit/push.
