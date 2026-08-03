# D00_L05 Drive Input Processing to D00_L06 Simulation IO Layer

## 1. Source Lesson

`D00_L05_Drive_Input_Processing`

D00_L05 remains unchanged and acts as the frozen inheritance snapshot for D00_L06.

## 2. Target Lesson

`D00_L06_Simulation_IO_Layer`

## 3. Architectural Objective

D00_L06 adds a minimal simulation implementation of the existing `DriveIO` contract and selects the real or simulated implementation only at the composition root.

```text
Driver
-> Xbox Controller
-> controls
-> commands
-> subsystems
-> DriveIO
-> DriveIOSparkMax or DriveIOSim
```

Real path:

```text
RobotContainer
-> DriveIOSparkMax
-> DriveSubsystem
```

Simulation path:

```text
RobotContainer
-> DriveIOSim
-> DriveSubsystem
```

## 4. Why This Lesson Exists

D00_L05 could run in desktop simulation, but `RobotContainer` always created `DriveIOSparkMax`. D00_L06 separates runtime implementation selection from mechanism behavior so `DriveSubsystem` can operate with real hardware or minimal simulation state through the same `DriveIO` dependency.

## 5. Frozen Backbone Reminder

The Frozen Backbone remains:

```text
Driver
-> Xbox Controller
-> controls
-> commands
-> subsystems
-> io
-> Hardware or Simulation State
```

- `RobotContainer` remains the composition root.
- Runtime selection appears only in `RobotContainer`.
- `DriveSubsystem` depends only on `DriveIO`.
- Commands and controls do not know the runtime environment.
- Telemetry remains observer-only.

## 6. Starting State

The inherited D00_L05 baseline provided:

- A complete WPILib project.
- `DriveIO` with `DriveIOInputs`, `updateInputs()`, `setTankOutputs()`, and `stop()`.
- `DriveIOSparkMax` as the real implementation.
- `DriveSubsystem` receiving `DriveIO` through dependency injection.
- `RobotContainer` directly creating `DriveIOSparkMax`.
- A successful baseline build.

No `DriveIOSim` or Real/Simulation selection existed.

## 7. Final Architecture

`DriveIOSim` implements `DriveIO` using only two internal applied-output values. It contains no REVLib, Spark MAX, CAN, physics, encoder, gyro, pose, odometry, telemetry, NetworkTables, or WPILib simulation-device dependency.

`RobotContainer.createDriveIO()` makes one runtime decision:

- `RobotBase.isReal()` returns `true`: create `DriveIOSparkMax`.
- Otherwise: create `DriveIOSim`.

The selected `DriveIO` is injected into `DriveSubsystem`. No runtime mode is passed into commands, controls, or the subsystem.

## 8. Step-by-Step Development

### Step 1 - Baseline Architecture Audit

- **Objective:** Confirm D00_L05 is a valid inheritance source.
- **Why:** D00_L06 must begin from a buildable, frozen lesson with a sufficient IO contract.
- **Action:** Inspect the source tree, `DriveIO`, `DriveIOSparkMax`, dependency injection, Gradle configuration, vendordeps, simulation configuration, and generated folders.
- **Files Changed:** None.
- **Verification:** D00_L05 baseline build completed successfully; D00_L06 did not yet exist.
- **Expected Result:** The current `DriveIO` contract can support a second implementation without changing subsystem logic.

### Step 2 - Inheritance Copy

- **Objective:** Create an independent D00_L06 WPILib project from D00_L05.
- **Why:** Repository lessons advance by inheritance, not recreation.
- **Action:** Copy the complete D00_L05 directory, remove inherited `build/` and `.gradle/` only from D00_L06, update lesson identity, and run the baseline build.
- **Files Changed:** D00_L06 project copy and `LESSON_STATUS.md`.
- **Verification:** All 10 inherited Java files were byte-for-byte identical to D00_L05 before implementation; baseline build passed.
- **Expected Result:** D00_L06 starts as an architecture-identical, buildable baseline.

### Step 3 - Create DriveIOSim

- **Objective:** Add a minimal simulation implementation of `DriveIO`.
- **Why:** Simulation must use the same subsystem-facing contract without creating vendor hardware.
- **Action:** Create `DriveIOSim.java` with internal left/right applied-output state. Implement `updateInputs()`, `setTankOutputs()`, and `stop()`.
- **Files Changed:** `src/main/java/frc/robot/io/drive/DriveIOSim.java`.
- **Verification:** Confirm no REVLib, Spark MAX, CAN, physics, telemetry, or simulation-device API is used.
- **Expected Result:** `DriveIOSim` represents the current drivetrain output state through the frozen `DriveIO` contract.

### Step 3A - Correct Incremental Compilation State

- **Objective:** Resolve the initial compilation failure without changing the approved source design.
- **Why:** The initial incremental compile selected only the newly added source and did not resolve existing `DriveIO` output correctly.
- **Action:** Inspect package declarations and source locations, confirm they were already correct, remove the generated compilation state with Gradle `clean`, and rebuild all sources.
- **Files Changed:** None.
- **Verification:** Clean and normal builds completed successfully with the original package and contract design.
- **Expected Result:** `DriveIOSim` compiles without unnecessary imports or architecture changes.

### Step 4 - Select Real or Simulation IO

- **Objective:** Select the drivetrain IO implementation at the composition root.
- **Why:** Runtime branching must not enter commands, controls, subsystem logic, or the IO contract.
- **Action:** Add `RobotContainer.createDriveIO()`. Use `RobotBase.isReal()` once to select `DriveIOSparkMax`; otherwise select `DriveIOSim`. Inject the returned `DriveIO` into `DriveSubsystem`.
- **Files Changed:** `src/main/java/frc/robot/RobotContainer.java`.
- **Verification:** Clean build passed; static search confirmed the runtime decision appears only once in `RobotContainer`.
- **Expected Result:** Real and simulation runtimes use different IO implementations while all downstream logic remains unchanged.

### Step 5 - Verify Simulation Runtime

- **Objective:** Prove that simulation selects and exercises `DriveIOSim`.
- **Why:** A successful compile alone does not prove runtime implementation selection or IO behavior.
- **Action:** Start `simulateJava`, inspect the live HAL simulation process, and run a temporary external HAL harness through `DriveInputProcessor`, `DefaultDriveCommand`, `DriveSubsystem`, and `DriveIOSim`.
- **Files Changed:** None in production source. The external harness was temporary and removed.
- **Verification:** Runtime type was `DriveIOSim`; no drivetrain Spark MAX/CAN initialization occurred; neutral, forward, independent, and stop states matched expected values.
- **Expected Result:** The simulation path operates without vendor drivetrain hardware and preserves the existing command pipeline.

### Step 6 - Source, Architecture, and Repository Closure Review

- **Objective:** Confirm the final lesson is clean and limited to its approved objective.
- **Why:** Closure requires evidence that the Frozen Backbone and inherited behavior remain intact.
- **Action:** Inspect final source, compare Java files with D00_L05, verify architecture ownership, run final clean/build commands, and remove generated artifacts.
- **Files Changed:** None.
- **Verification:** Only `RobotContainer.java` and new `DriveIOSim.java` differ from D00_L05; D00_L05 remains unchanged; final build passed.
- **Expected Result:** Engineering, simulation, architecture, and regression checks are ready for documentation closure.

### Documentation Closure - Create the Guide and Complete Status

- **Objective:** Record the verified D00_L05-to-D00_L06 transition and close the lesson.
- **Why:** Every completed lesson requires a reproducible transition guide and accurate lesson status.
- **Action:** Create this guide and update `LESSON_STATUS.md` to `COMPLETE`.
- **Files Changed:** `LESSON_STATUS.md` and this transition guide.
- **Verification:** Validate naming, required sections, evidence accuracy, lesson status, Java integrity, D00_L05 integrity, final build, and Git state.
- **Expected Result:** D00_L06 documentation is complete and ready for mentor review, commit, and push.

## 9. Files Created

- `src/main/java/frc/robot/io/drive/DriveIOSim.java`
- `docs/D00_L05_Drive_Input_Processing_to_D00_L06_Simulation_IO_Layer_Step_by_Step.md`

## 10. Files Modified

- `src/main/java/frc/robot/RobotContainer.java`
- `LESSON_STATUS.md`

No other Java file changed from D00_L05.

## 11. Complete Change Summary

- Added a minimal `DriveIOSim` implementation.
- Added a single Real/Simulation selection point in `RobotContainer`.
- Preserved `DriveIO`, `DriveSubsystem`, commands, controls, constants, and the package structure.
- Preserved the D00_L05 frozen source.
- Added no physics, sensors, pose estimation, telemetry redesign, or new dependency.

## 12. Build Verification

Verified evidence:

- D00_L06 inherited baseline build: PASS.
- Initial build after adding `DriveIOSim` failed because of stale incremental compilation state.
- Package declarations and source locations were already correct.
- Gradle `clean` removed the stale generated state.
- Build after clean: PASS.
- Step 4 clean and build: PASS.
- Step 5 final clean and build: PASS.
- Documentation-closure build: required after this guide and status update.

The initial failure was a generated incremental-state issue, not a package architecture failure.

## 13. Simulation Verification

Verified results:

- `simulateJava` started successfully.
- HAL simulation and GUI native modules loaded.
- Runtime `DriveIO` type was `DriveIOSim`.
- No drivetrain Spark MAX or CAN initialization occurred.
- Neutral output: `0.0 / 0.0`.
- Forward output: `1.0 / 1.0`.
- Independent output: approximately `0.456522 / -0.456522`.
- Stop output: `0.0 / 0.0`.
- The temporary external harness was removed.
- No diagnostic code remains in production source.

No physical robot behavior is claimed.

## 14. Architecture Verification

- Frozen Backbone: PASS.
- RobotContainer composition-root responsibility: PASS.
- Runtime selection only in RobotContainer: PASS.
- `RobotBase.isReal()` occurrence count: one.
- Real path creates `DriveIOSparkMax`: PASS.
- Simulation path creates `DriveIOSim`: PASS.
- DriveSubsystem depends only on `DriveIO`: PASS.
- Commands and controls are runtime-independent: PASS.
- `DriveIO` contract unchanged: PASS.
- Telemetry redesign absent: PASS.
- Physics and sensor additions absent: PASS.

## 15. Inheritance Verification

Compared with D00_L05:

- Modified Java: `src/main/java/frc/robot/RobotContainer.java`.
- New Java: `src/main/java/frc/robot/io/drive/DriveIOSim.java`.
- Other Java differences: none.
- D00_L05 working-tree changes: none.
- D00_L05 remains the frozen source snapshot.

## 16. Expected Results

After completion:

- Real runtime uses `DriveIOSparkMax`.
- Simulation runtime uses `DriveIOSim`.
- Both implementations satisfy `DriveIO`.
- `DriveSubsystem`, commands, and controls require no runtime-specific logic.
- Simulation can verify command flow and output state without drivetrain vendor hardware.
- The lesson builds successfully and remains limited to one architectural objective.

## 17. Troubleshooting

### DriveIO or DriveIOInputs cannot be resolved after adding DriveIOSim

1. Confirm `DriveIO.java` and `DriveIOSim.java` both declare `package frc.robot.io.drive;`.
2. Confirm both files remain in `src/main/java/frc/robot/io/drive/`.
3. Do not add unnecessary same-package imports.
4. Run:

```powershell
.\gradlew.bat clean --no-daemon
.\gradlew.bat build --no-daemon
```

The verified D00_L06 issue was resolved by cleaning generated incremental state.

### Simulation creates Spark MAX or CAN errors

1. Confirm `RobotBase.isReal()` appears only in `RobotContainer`.
2. Confirm the real branch creates `DriveIOSparkMax`.
3. Confirm the non-real branch creates `DriveIOSim`.
4. Confirm commands and subsystem still receive only `DriveIO`.

## 18. Final Checklist

- [x] D00_L06 inherited from completed D00_L05
- [x] Baseline architecture audit PASS
- [x] Baseline build PASS
- [x] `DriveIOSim` created
- [x] `DriveIOSim` implements the complete `DriveIO` contract
- [x] Real/Simulation selection occurs only in RobotContainer
- [x] Real path selects `DriveIOSparkMax`
- [x] Simulation path selects `DriveIOSim`
- [x] Commands, controls, and subsystem remain runtime-independent
- [x] Runtime simulation verification PASS
- [x] Temporary harness removed
- [x] Java regression comparison PASS
- [x] D00_L05 integrity PASS
- [x] Final clean build PASS
- [x] Transition guide complete
- [x] Lesson status COMPLETE
- [ ] Real robot test not performed
- [ ] Git commit pending
- [ ] Git push pending

## 19. Lesson Completion Evidence

- Engineering implementation: PASS.
- Architectural objective: COMPLETE.
- Architecture review: PASS.
- Build: PASS.
- Simulation: PASS.
- Runtime IO selection: PASS.
- `DriveIOSim` behavior: PASS.
- Regression: PASS.
- Previous lesson integrity: PASS.
- Temporary artifact cleanup: PASS.
- Real robot test: NOT PERFORMED.
- Transition guide: COMPLETE.
- Git commit: NOT COMPLETED.
- Git push: NOT COMPLETED.

## 20. Lessons Learned

- Hardware and simulation implementations can share one small IO contract.
- Runtime selection belongs in the composition root.
- Subsystem logic remains reusable when it depends only on an interface.
- Minimal simulation state can verify command flow before a physics model exists.
- Generated incremental compilation state should be cleaned before diagnosing a confirmed-correct package structure as an architecture failure.
