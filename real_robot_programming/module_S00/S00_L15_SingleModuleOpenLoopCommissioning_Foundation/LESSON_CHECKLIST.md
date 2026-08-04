# S00_L15 Lesson Checklist

Status: COMPLETE — FROZEN / READ-ONLY

| Step | State |
| --- | --- |
| Inherit frozen S00_L14 | PASS |
| Baseline Build | PASS - user-reported inherited baseline |
| Architecture Review | PASS |
| Four dashboard command identities | PASS - four exact SmartDashboard command entries |
| Test-mode acceptance | PASS - command and subsystem both require Test mode |
| Disabled rejection | PASS - scheduler default gate and explicit mode checks |
| Teleop rejection | PASS - command and subsystem reject non-Test mode |
| Autonomous rejection | PASS - command and subsystem reject non-Test mode |
| Private/factory-only construction | PASS - command constructor is private |
| Fixed duty/duration | PASS - 0.05 duty and 0.25-second duration only |
| Subsystem duty clamp | PASS - absolute output bounded by subsystem |
| Drive/steer mutual exclusion | PASS - both Front Left outputs clear before selection |
| Subsystem watchdog | PASS - Front Left watchdog stops at or before 0.25 seconds |
| Cancellation/interruption cleanup | PASS - command end stops Front Left |
| Mode-exit cleanup | PASS - command and subsystem stop outside Test mode |
| Failure cleanup | PASS - output failure stops Front Left before rethrow |
| FR/BL/BR isolation | PASS - commissioning path never references other module outputs |
| HAL initialization before DriverStationSim | PASS |
| DriverStationSim.resetData() before each test | PASS |
| Duplicate-stop defect removed | PASS |
| Subsystem-owned output-failure cleanup | PASS |
| Inactive-state finally cleanup | PASS |
| Suppressed exception preservation | PASS |
| Focused Tests | PASS - 12/12 |
| Full Test Suite | PASS - 48/48 |
| Full Build | PASS |
| Simulation | NOT TESTED - no result supplied |
| Glass Commissioning | PASS |
| Driver Station | PASS |
| Real Robot | PASS |
| Documentation | PASS |
| Commit | NOT TESTED - user-owned |
| Push | NOT TESTED - user-owned |
| Freeze | NOT TESTED - user-owned; lesson is recorded FROZEN / READ-ONLY |

## Real-Robot Safety Checklist

- Keep the robot physically secured and use only the approved Test mode procedure.
- Confirm exactly one Glass/SmartDashboard button is selected: FL Drive Positive, FL Drive
  Negative, FL Steer Positive, or FL Steer Negative.
- Confirm no personnel or obstructions are near the Front Left module.
- Verify the command is limited to 0.05 duty and 0.25 seconds before testing.
- Confirm output stops on completion, cancellation, interruption, mode exit, or fault.
- Stop immediately for unexpected motion, noise, current, heat, fault, or signal behavior.
