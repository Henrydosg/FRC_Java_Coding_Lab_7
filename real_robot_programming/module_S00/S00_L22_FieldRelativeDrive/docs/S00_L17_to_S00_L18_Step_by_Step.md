# S00_L17 to S00_L18: Four Module State Actuation

## Step 1 - Inherit the frozen lesson

- Objective: Start S00_L18 from the completed S00_L17 snapshot.
- Why: S00_L17 is the frozen single-module closed-loop foundation and remains read-only.
- Action: Confirm S00_L17 is `COMPLETE / FROZEN / READ-ONLY`. Copy and rename it to
  `S00_L18_FourModuleStateActuation`; remove copied `build/` and `.gradle/` artifacts before editing.
- Files Changed: New S00_L18 project only.
- Verification: From the new project directory, run:

  ```powershell
  .\gradlew clean build
  ```

  The inherited baseline build and tests passed before the S00_L18 changes.
- Expected Result: An independent S00_L18 project is ready for one new concept.

## Step 2 - Preserve the architecture

- Objective: Expand dispatch without changing ownership boundaries.
- Why: Four-module actuation belongs after the existing pipeline, not inside IO or RobotContainer.
- Action: Preserve `RobotContainer` as composition root, `SwerveModuleIO` as vendor-neutral boundary,
  and `SwerveOutputPipeline` as the owner of kinematics, optimization, and desaturation.
- The exact L17 -> L18 delta is all-module closed-loop readiness, the global wheel-speed clamp for
  production dispatch, FL/FR/BL/BR dispatch of the existing four-state array, the
  `productionIntentArmed` lifecycle gate, and fixed Test-mode producer/dashboard commands.
- These changes are required for safe simultaneous state actuation and explicit verification. CTRE
  configuration, public IO methods, telemetry, observations, RobotContainer ownership, and S00_L17
  behavior remain frozen.
- Files Changed: None for the architecture audit.
- Verification: Architecture and Frozen Backbone review PASS.
- Expected Result: No joystick, teleop, field-relative, odometry, pose, autonomous, or fault logic.

Final flow:

```text
ChassisSpeeds
  -> SwerveOutputPipeline
  -> optimized/desaturated SwerveModuleState[4]
  -> SwerveSubsystem
  -> FL / FR / BL / BR vendor-neutral IO
  -> CTRE hardware
```

## Step 3 - Verify all-module readiness and lifecycle ownership

- Objective: Make FL, FR, BL, and BR ready for closed-loop state dispatch without weakening safety.
- Why: The producer must be able to use the shared CTRE configuration/readback path for every module.
- Action: Enable the existing closed-loop configuration path for the remaining modules, preserve the
  global wheel-speed clamp, and require Enabled, no Front Left commissioning ownership, and
  `productionIntentArmed` before production dispatch. Arm intent only through `acceptChassisSpeeds()`;
  clear it through `stop()` and commissioning ownership.
- Files Changed: `SwerveModuleIOCTRE.java`, `SwerveSubsystem.java`,
  `SwerveSubsystemFourModuleActuationTest.java`, and the affected commissioning/subsystem lifecycle
  tests.
- Verification: All four modules ready, lifecycle gates, ordering, and commissioning isolation PASS.
- Expected Result: Disabled and commissioning-owned cycles observe and compute but do not actuate.

## Step 4 - Add fixed verification constants

- Objective: Define bounded, named values for safe four-module verification.
- Why: Fixed values avoid teleop scope and make the real-robot procedure auditable.
- Action: Add translation `0.30 m/s`, rotation `0.75 rad/s`, and command duration `1.0 s`.
- Files Changed: `Constants.java`.
- Verification: Focused producer tests PASS.
- Expected Result: Every producer request is finite, fixed, and bounded.

## Step 5 - Add the Test-mode producer

- Objective: Submit one fixed chassis intent through the production path.
- Why: Direct module IO would bypass kinematics, optimization, desaturation, ordering, and health gates.
- Action: Add `SwerveFourModuleTestCommand` with subsystem requirements, Test + Enabled guards, one
  `acceptChassisSpeeds()` call in `initialize()`, a `1.0 s` timer, and stop cleanup for timeout,
  interruption, disable, mode exit, exception, and command end.
- Files Changed: `SwerveFourModuleTestCommand.java` and its focused tests.
- Verification: Focused lifecycle, mutual-exclusion, fixed-value, and no-direct-IO tests PASS.
- Expected Result: Four-module states are dispatched only by the normal subsystem periodic path.

## Step 6 - Publish four dashboard commands

- Objective: Expose only the approved verification actions.
- Why: Glass/SmartDashboard provides explicit user-triggered Test-mode commands without joystick logic.
- Action: Add `SwerveFourModuleTestDashboard` and publish `Four Module Forward`, `Four Module Robot
  Left`, `Four Module Rotate CCW`, and `Four Module Stop`. Construct it from `RobotContainer` only.
- Files Changed: `SwerveFourModuleTestDashboard.java`, `RobotContainer.java`.
- Verification: Dashboard publication and Glass verification PASS.
- Expected Result: Exactly four new fixed command widgets are available.

## Step 7 - Complete software verification

- Objective: Prove the producer and inherited behavior before hardware verification.
- Why: Software lifecycle and pipeline errors must be eliminated before enabling the robot.
- Action: From the S00_L18 project directory, run exactly:

  ```powershell
  .\gradlew test --tests "frc.robot.commands.SwerveFourModuleTestCommandTest"
  .\gradlew test --tests "frc.robot.subsystems.SwerveSubsystemFourModuleActuationTest"
  .\gradlew test
  .\gradlew clean build
  ```
- Files Changed: No additional production behavior.
- Verification: Focused tests PASS; full suite `114/114 PASS`; clean build `BUILD SUCCESSFUL`.
- Expected Result: Software verification is complete.

## Step 8 - Verify Simulation, Glass, and Driver Station

- Objective: Verify publication, scheduling, Test + Enabled guards, timeout, and stop behavior.
- Why: HALSIM and Glass validate command lifecycle without relying on mechanical motion.
- Action:
  1. Start the project with `simulateJava` (for example, `.\gradlew simulateJava`).
  2. Open Glass and connect to the simulation NetworkTables instance.
  3. Set Driver Station to Test mode and Enable.
  4. Locate `Four Module Forward`, `Four Module Robot Left`, `Four Module Rotate CCW`, and
     `Four Module Stop`.
  5. Run Forward, wait for its one-second timeout, then run Stop.
  6. Run Robot Left, wait for its one-second timeout, then run Stop.
  7. Run Rotate CCW, wait for its one-second timeout, then run Stop.
  8. Confirm Disabled or Test-mode exit stops commands and prevents production dispatch.
- `SwerveModuleIONoop` does not simulate real CTRE motor motion. Simulation PASS means command
  publication, Test + Enabled gating, fixed `ChassisSpeeds`, pipeline dispatch calls, timeout, and
  stop lifecycle are correct; it does not prove wheel movement, torque, direction, or mechanics.
- Files Changed: None.
- Verification: Simulation, Glass, and Driver Station PASS.
- Expected Result: Commands run only under approved mode ownership and return to stopped state.

## Step 9 - Verify the real robot

- Objective: Confirm simultaneous four-module actuation and safe stopping.
- Why: Only the robot can prove wiring, direction, synchronized state response, and mechanical behavior.
- Action:
  1. Securely support the robot with all four wheels off the floor.
  2. Keep personnel, loose clothing, tools, and CAN/power wires clear of rotating parts.
  3. Use Test mode only, with one Driver Station operator and immediate Disable/E-stop access.
  4. Confirm communications, connectivity, configuration health, and the correct dashboard artifact.
  5. Disable immediately for unexpected motion, wrong direction, abnormal vibration/noise, a module
     not responding, output continuing after Stop/timeout, exposed wiring, or any person entering the
     hazard area. Use E-stop if Disable is insufficient.
  6. Run Forward -> Stop, Robot Left -> Stop, and Rotate CCW -> Stop, one command at a time.
- Files Changed: None.
- Verification: Forward PASS; Robot Left PASS; Rotate CCW PASS; automatic `1.0 s` stop PASS; explicit
  Stop PASS; no abnormal vibration observed.
- Expected Result: All four modules actuate coherently and stop safely.

## Step 10 - Run the real-robot commands one at a time

Use the fixed values: translation `0.30 m/s`, rotation `0.75 rad/s`, and timeout `1.0 s`. Stop and
re-check the robot between every command.

### A. Four Module Forward

- Command/setpoint: `Four Module Forward`; `ChassisSpeeds(+0.30, 0.0, 0.0)`.
- Expected steer: All four modules move to the optimized forward-translation states.
- Expected drive: All four drives respond coherently in the forward direction.
- Timeout: All outputs stop automatically after `1.0 s`.
- PASS: Four modules actuate in the expected pattern, no abnormal vibration occurs, and timeout stops
  every output.
- FAIL: Any module is absent, reversed, incoherent, unhealthy, or continues after timeout.
- Disable immediately: Unexpected motion, wrong direction, vibration/noise, wiring movement, or failure
  to stop.

### B. Four Module Stop

- Command/setpoint: `Four Module Stop`; explicit zero-output stop.
- Expected steer: No new steering actuation.
- Expected drive: All four drive outputs return to zero immediately.
- Timeout: Not applicable; Stop is immediate.
- PASS: All modules are stopped and `productionIntentArmed` is false.
- FAIL: Any output remains active or stale intent re-arms dispatch.
- Disable immediately: Any motor remains energized or moves after Stop.

### C. Four Module Robot Left

- Command/setpoint: `Four Module Robot Left`; `ChassisSpeeds(0.0, +0.30, 0.0)`.
- Expected steer: All four modules move to the optimized robot-left translation states.
- Expected drive: All four drives respond coherently for robot-left translation.
- Timeout: All outputs stop automatically after `1.0 s`.
- PASS: The state pattern and physical direction correspond to robot-left translation, with no abnormal
  vibration and a clean timeout stop.
- FAIL: Any module is absent, reversed, incoherent, unhealthy, or continues after timeout.
- Disable immediately: Unexpected motion, wrong direction, vibration/noise, wiring movement, or failure
  to stop.

### D. Four Module Stop

- Command/setpoint: `Four Module Stop`; explicit zero-output stop.
- Expected steer: No new steering actuation.
- Expected drive: All four drive outputs return to zero.
- Timeout: Not applicable; Stop is immediate.
- PASS: All four modules are stopped before the next motion test.
- FAIL: Any residual drive or steer output remains.
- Disable immediately: Stop does not neutralize every module.

### E. Four Module Rotate CCW

- Command/setpoint: `Four Module Rotate CCW`; `ChassisSpeeds(0.0, 0.0, +0.75)`.
- Expected steer: Modules move to optimized tangential states for robot-relative CCW rotation; angles
  may differ by module position.
- Expected drive: Drive directions and speeds form the coherent CCW rotational pattern.
- Timeout: All outputs stop automatically after `1.0 s`.
- PASS: The four modules produce the expected CCW pattern, no abnormal vibration occurs, and timeout
  stops every output.
- FAIL: Any module is absent, wrong-way, incoherent, unhealthy, or continues after timeout.
- Disable immediately: Unexpected motion, wrong direction, vibration/noise, wiring movement, or failure
  to stop.

### F. Four Module Stop

- Command/setpoint: `Four Module Stop`; explicit zero-output stop.
- Expected steer: No new steering actuation.
- Expected drive: All four drive outputs remain neutral.
- Timeout: Not applicable; Stop is immediate.
- PASS: All modules remain stopped and production intent is disarmed.
- FAIL: Any output remains active or the robot does not remain stopped.
- Disable immediately: Any output persists after Stop.

## Step 11 - Troubleshoot the verified lifecycle regressions

- Commissioning ownership conflict: Production dispatch initially resumed while a Front Left
  commissioning command still owned the subsystem. The corrected gate suppresses production dispatch
  whenever `frontLeftCommissioningActive` is true.
- `productionIntentArmed` solution: Enabled alone is not actuation permission. The flag starts false,
  becomes true only after `acceptChassisSpeeds(valid intent)`, becomes false in `stop()`, and is false
  during commissioning ownership.
- Zero/stale dispatch prevention: `periodic()` still refreshes inputs, creates observations, and
  computes the pipeline, but dispatch requires Enabled, no commissioning ownership, and armed intent.
  This prevents automatic zero-state or stale-state reissue without adding repeated periodic stops.
- Test fixture aggregate drive/steer count issue: An inherited fixture combined drive and steer calls
  into one aggregate count, causing a misleading mismatch after four-module dispatch was added. The
  fixture was corrected to count drive and steer independently per module while retaining both checks.
- Architecture preservation: These corrections remain subsystem lifecycle state and test evidence
  changes. They do not add IO methods, bypass the pipeline, move vendor APIs, change CTRE configuration,
  alter the public contract, or modify S00_L17.

## Step 12 - Final verification matrix

| Verification item | Result |
| --- | --- |
| Architecture review | PASS |
| Frozen Backbone | PASS |
| Public IO contract | PASS |
| All four modules closed-loop ready | PASS |
| FL/FR/BL/BR dispatch order | PASS |
| `productionIntentArmed` lifecycle | PASS |
| Front Left commissioning isolation | PASS |
| Focused tests | PASS |
| Full suite | `114/114 PASS` |
| Clean build | `BUILD SUCCESSFUL` |
| Simulation command lifecycle | PASS |
| Glass / SmartDashboard publication | PASS |
| Driver Station workflow | PASS |
| Four Module Forward | PASS |
| Four Module Robot Left | PASS |
| Four Module Rotate CCW | PASS |
| Automatic `1.0 s` stop | PASS |
| Explicit Stop | PASS |
| Abnormal vibration | None observed |

## Step 13 - Technical debt and S00_L19 boundary

PID and feedforward values remain commissioning baselines, not production-final tuning. `kS` and full
SysId/characterization remain future work. The four fixed Test-mode commands are verification tools
only and must not become normal drive controls. Joystick and teleop integration belongs to S00_L19.
Field-relative control, odometry, pose estimation, autonomous behavior, and fault aggregation remain
outside S00_L18.

S00_L17 is unchanged and frozen. S00_L19 was not created or modified during this transition.

## Step 14 - Git closure procedure

Git is user-owned. After reviewing the active lesson and confirming only intended changes are present,
run these commands in order:

- `git status`
- `git add real_robot_programming/module_S00/S00_L18_FourModuleStateActuation`
- `git commit -m "Complete S00_L18 four-module state actuation"`
- `git push origin main`
- `git status`

The final `git status` must be clean. Do not claim commit or push completion until the user observes
successful command results.
