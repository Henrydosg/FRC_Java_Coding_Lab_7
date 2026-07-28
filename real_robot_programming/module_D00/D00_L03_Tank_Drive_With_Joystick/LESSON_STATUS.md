# Lesson Status

- Lesson: `D00_L03_Tank_Drive_With_Joystick`
- Previous Lesson: `D00_L02_Drivebase_Safety_Configuration`
- Status: `COMPLETE`

## Architecture

- Architecture Review: PASS
- Source Implementation: PASS
- Frozen Pipeline: Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> Hardware
- RobotContainer: Composition root only

## Verification

| Item | Status | Evidence |
| --- | --- | --- |
| Architecture | PASS | Frozen pipeline and package responsibilities preserved |
| Baseline Build | PASS | Inherited D00_L02 project built successfully |
| Build | PASS | `.\gradlew.bat clean build` |
| Simulation | PASS | HAL Simulation verified with independent left-Y and right-Y input |
| Driver Station | PASS | Xbox controller detected on USB port 0 |
| Deployment | PASS | D00_L03 deployed successfully to the roboRIO |
| Real Robot | PASS | Left joystick controlled the left drivetrain side; right joystick controlled the right drivetrain side |
| Independent Tank Control | PASS | Left and right drivetrain outputs responded independently |
| Neutral Input | PASS | Robot remained stationary when both joysticks were released |
| Command Interruption | PASS | Drivetrain stopped when the active drive command ended |
| Documentation | PASS | Transition guide completed |
| Git Commit | PENDING | Complete after final review |
| Git Push | PENDING | Complete after commit |

## Input Processing Scope

`DriveInputProcessor.process(...)` currently preserves the raw signed joystick value.

Not included in this lesson:

- Deadband
- Joystick-axis inversion
- Sensitivity scaling
- Nonlinear response curve
- Slew-rate limiting

These features remain deferred to later input-processing lessons.

## Known Limitations

- Small joystick-center drift may produce drivetrain movement because deadband is not yet implemented.
- Driver control currently uses direct tank drive: left Y controls the left drivetrain side and right Y controls the right drivetrain side.
- Physical SPARK MAX safety configuration was inherited from D00_L02 and was not changed by this lesson.
