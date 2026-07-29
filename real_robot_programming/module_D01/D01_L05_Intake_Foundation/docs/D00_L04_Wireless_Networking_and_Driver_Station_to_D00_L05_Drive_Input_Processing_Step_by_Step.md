# D00_L04 Wireless Networking and Driver Station to D00_L05 Drive Input Processing

## 1. Lesson Objective

D00_L05 introduces one concept: normalize each signed driver joystick request inside `DriveInputProcessor` before the default drive command sends it to the drivetrain subsystem.

The completed scalar pipeline is:

```text
Raw joystick value
-> deadband
-> driver-axis inversion
-> maximum driver-output scaling
-> final processor clamp
-> drivetrain request
```

The lesson preserves the existing tank-drive command flow, hardware abstraction, subsystem safety boundary, and composition-root responsibilities inherited from D00_L04.

## 2. Architecture

No package responsibility, dependency direction, interface contract, or composition-root role changed.

```text
Driver
-> Xbox Controller
-> controls
-> commands
-> subsystems
-> io
-> Hardware
```

`DriveInputProcessor` remains in `controls` and owns driver-intent normalization. `DefaultDriveCommand` continues to coordinate the two axis suppliers, processor, and subsystem. `DriveSubsystem` retains its independent output-safety clamp. `DriveIO` and `DriveIOSparkMax` remain unchanged.

Architecture review result: PASS.

## 3. Files Modified

### Engineering files

- `src/main/java/frc/robot/Constants.java`
- `src/main/java/frc/robot/controls/DriveInputProcessor.java`

### Lesson tracking files

- `LESSON_STATUS.md`
- `docs/D00_L04_Wireless_Networking_and_Driver_Station_to_D00_L05_Drive_Input_Processing_Step_by_Step.md`

No other Java file changed from D00_L04.

## 4. Step-by-Step Implementation

### Step 1 - Inherit D00_L04

- **Objective:** Establish D00_L05 as an independent WPILib project.
- **Why:** Repository lessons advance through inheritance rather than recreation.
- **Action:** Copy the complete D00_L04 project, remove generated `build/` and `.gradle/` directories from the new copy, and set the new lesson identity.
- **Files Changed:** `LESSON_STATUS.md`.
- **Verification:** Confirm the target exists, generated directories are absent before building, and all inherited Java files are byte-identical.
- **Expected Result:** A clean D00_L05 baseline with no engineering changes.

### Step 2 - Review the Existing Drive Pipeline

- **Objective:** Confirm the correct owner for driver-input processing.
- **Why:** Input transformation must not leak into `RobotContainer`, commands, subsystems, or IO.
- **Action:** Trace the Xbox axis suppliers through `DefaultDriveCommand`, `DriveInputProcessor`, `DriveSubsystem`, and `DriveIO`.
- **Files Changed:** None.
- **Verification:** Compare the path with the Frozen Backbone and architecture checklist.
- **Expected Result:** `DriveInputProcessor` is confirmed as the sole owner of driver normalization.

### Step 3 - Approve the Minimal Processing Contract

- **Objective:** Define the smallest valid D00_L05 behavior.
- **Why:** One lesson must add one concept without introducing unrelated infrastructure.
- **Action:** Approve a stateless scalar pipeline ordered as deadband, inversion, scaling, and clamp.
- **Files Changed:** None.
- **Verification:** Review input/output ranges, constant ownership, API stability, and deferred features.
- **Expected Result:** The existing `process(double)` API remains sufficient.

### Step 4 - Add Deadband

- **Objective:** Remove centered-stick noise while preserving the usable axis range.
- **Why:** Small raw joystick values should not produce unintended drive intent.
- **Action:** Add `kDriverDeadband = 0.08` and apply `MathUtil.applyDeadband()`.
- **Files Changed:** `Constants.java`, `DriveInputProcessor.java`.
- **Verification:** Clean build and deterministic checks for centered, inside-deadband, and outside-deadband inputs.
- **Expected Result:** Values inside the deadband return zero; values outside it are rescaled.

### Step 5 - Add Driver-Axis Inversion

- **Objective:** Convert the Xbox Y-axis convention into the approved drive-intent direction.
- **Why:** Joystick-axis direction is a controls concern and is distinct from physical motor inversion.
- **Action:** Add `kDriverAxisSign = -1.0` and multiply the deadband result by that sign.
- **Files Changed:** `Constants.java`, `DriveInputProcessor.java`.
- **Verification:** Confirm `-1.0` becomes `+1.0` and `+1.0` becomes `-1.0`.
- **Expected Result:** Driver intent has the approved sign while motor-controller inversion remains unchanged.

### Step 6 - Add Maximum Driver Output Scaling

- **Objective:** Make maximum driver authority explicit.
- **Why:** Scaling belongs after the request has been normalized and direction-corrected.
- **Action:** Add `kDriverMaximumOutput = 1.0` and multiply the direction-corrected value by it.
- **Files Changed:** `Constants.java`, `DriveInputProcessor.java`.
- **Verification:** Confirm full-scale behavior is preserved and an input of `0.50` produces approximately `-0.456522`.
- **Expected Result:** Maximum driver output is explicit without adding nonlinear behavior.

### Step 7 - Add the Final Processor Clamp

- **Objective:** Guarantee a valid normalized driver request.
- **Why:** The controls boundary should return a safe percent-output range while the subsystem retains its independent safety boundary.
- **Action:** Clamp the processed result using `MathUtil.clamp()` and the existing minimum and maximum drive-output constants.
- **Files Changed:** `DriveInputProcessor.java`.
- **Verification:** Confirm normal inputs are unchanged and out-of-range inputs remain within `[-1.0, 1.0]`.
- **Expected Result:** The clamp is the final operation and the subsystem clamp remains unchanged.

### Step 8 - Audit Scope and Regression

- **Objective:** Prove the implementation contains only the approved concept.
- **Why:** Sensitivity curves, squared inputs, slew limiting, telemetry, and simulation infrastructure were explicitly deferred.
- **Action:** Inspect the processor API and processing order, search for deferred features, and compare all Java files against D00_L04.
- **Files Changed:** None.
- **Verification:** Confirm ten Java files in both lessons, with differences limited to `Constants.java` and `DriveInputProcessor.java`.
- **Expected Result:** Static audit, architecture, scope-control, and regression results are PASS.

### Step 9 - Verify Runtime Simulation Stability

- **Objective:** Verify the existing robot application remains stable under simulated driver input.
- **Why:** Runtime verification complements the deterministic processor audit without adding observability infrastructure.
- **Action:** Start HAL Simulation, connect the simulated Driver Station, assign HID controls to port `0`, exercise both Y axes in Teleop, return them to zero, and disable safely.
- **Files Changed:** None.
- **Verification:** User confirmed HAL startup, Driver Station connectivity, HID input injection, stable Teleop operation, stable disable transition, and no runtime or scheduler errors.
- **Expected Result:** Runtime command flow and lifecycle remain stable.

### Step 10 - Close and Freeze the Lesson

- **Objective:** Record verified completion and preserve D00_L05 as an inheritance source.
- **Why:** Completed lessons are frozen snapshots for future lessons.
- **Action:** Complete the transition guide and set `LESSON_STATUS.md` to `COMPLETE`.
- **Files Changed:** `LESSON_STATUS.md`, this transition guide.
- **Verification:** Final clean warning-enabled build, documentation review, Java regression comparison, and Git-status inspection.
- **Expected Result:** D00_L05 is complete and frozen; commit and push remain explicit user actions.

## 5. Verification Summary

| Verification | Result |
| --- | --- |
| Engineering | PASS |
| Architecture | PASS |
| Implementation | PASS |
| Static Audit | PASS |
| Regression | PASS |
| Simulation Preparation | PASS |
| Interactive Simulation | PASS - user confirmed |
| Build | PASS |
| Build Warnings | NONE |
| Real Robot | NOT TESTED |

Deterministic processor examples:

| Raw input | Processed output |
| ---: | ---: |
| `0.00` | `0.0` |
| `0.05` | `0.0` |
| `0.081` | approximately `-0.001087` |
| `0.50` | approximately `-0.456522` |
| `-1.0` | `+1.0` |
| `+1.0` | `-1.0` |
| `-2.0` | `+1.0` after clamp |
| `+2.0` | `-1.0` after clamp |

## 6. Simulation Summary

The interactive verification used the existing WPILib HAL Simulation and simulated Driver Station. The user confirmed:

- HAL Simulation started.
- The simulated Driver Station connected.
- HID controls were assigned to joystick port `0`.
- Left-Y and right-Y raw axes responded and returned near zero.
- Teleop enabled and remained stable while both axes were exercised.
- No uncaught exception, scheduler error, or application instability appeared.
- The robot returned safely to Disabled mode.
- Simulation shut down normally.

The simulation did not directly expose numeric processor values, subsystem requests, IO motor outputs, or physical drivetrain behavior. The Phase 4 deterministic audit remains the evidence for the numeric processor contract.

## 7. Final Checklist

- [x] Required engineering standards reviewed
- [x] Lesson inherited from D00_L04
- [x] Baseline build PASS
- [x] Frozen Backbone preserved
- [x] RobotContainer preserved
- [x] `DriveInputProcessor` owns normalization
- [x] Deadband implemented
- [x] Driver-axis inversion implemented
- [x] Maximum driver output implemented
- [x] Final processor clamp implemented
- [x] Deferred features remain absent
- [x] Static audit PASS
- [x] Regression PASS
- [x] Interactive simulation PASS
- [x] Final build PASS
- [x] Build warnings NONE
- [x] Transition guide complete
- [x] Lesson status COMPLETE
- [ ] Git commit pending user execution
- [ ] Git push pending user execution
- [ ] Physical robot verification not performed

## 8. Lessons Learned

- Driver-input normalization belongs in `controls`, not in the composition root, command, subsystem, or IO layer.
- `MathUtil.applyDeadband()` removes center noise and rescales the remaining range.
- Driver-axis inversion represents controller intent; SPARK MAX inversion represents physical drivetrain configuration.
- Explicit maximum-output scaling keeps driver authority configurable without changing the processor API.
- A processor-level clamp validates driver intent, while the subsystem clamp independently protects every drivetrain caller.
- Deterministic calculation proves the numeric scalar contract; runtime simulation proves lifecycle and scheduler stability within the signals the current project exposes.
- Deferring curves, slew limiting, telemetry, and simulation IO kept the lesson focused on one concept.
