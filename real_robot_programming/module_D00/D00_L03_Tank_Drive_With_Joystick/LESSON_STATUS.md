# Lesson Status

- Lesson: D00_L03_Tank_Drive_With_Joystick
- Previous Lesson: D00_L01_Competition_Robot_Foundation
- Status: COMPLETE

## Architecture

- Architecture Review: PASS
- Source Implementation: PASS
- Frozen Pipeline: Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> Hardware
- RobotContainer: Composition root only

## Verification

| Item | Status | Evidence |
| --- | --- | --- |
| Architecture | PASS | Frozen pipeline and package responsibilities preserved |
| Baseline Build | PASS | Baseline build completed |
| Build | PASS | `.\gradlew.bat clean build` |
| Simulation | PASS | HAL Simulation verified with keyboard input |
| Control Flow | PASS | Keyboard -> HAL Simulation -> CommandXboxController -> DefaultDriveCommand -> DriveSubsystem -> DriveIO |
| Documentation | PASS | Transition guide completed |
| Driver Station / Glass | NOT REQUIRED | Not required for this lesson |
| Real Robot | DEFERRED | Physical robot verification deferred |
| Git Commit | PASS | Lesson commits completed |
| Git Push | PASS | Lesson commits pushed to `origin/main` |

## Known Issues

- Brake mode, 60 A current limiting, 12.0 V voltage compensation,
  0.25-second open-loop ramping, and leader/follower hardware behavior
  remain physically unverified.
- Desktop simulation used `DriveIOSparkMax`; it did not exercise physical
  SPARK MAX firmware or hardware.
- Real deployment failed only because the roboRIO was unavailable.
