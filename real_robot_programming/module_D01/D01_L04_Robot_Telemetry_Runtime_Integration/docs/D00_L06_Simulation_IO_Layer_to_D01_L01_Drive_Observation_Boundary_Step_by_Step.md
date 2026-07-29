# D00_L06 Simulation IO Layer to D01_L01 Drive Observation Boundary

## Lesson Objective

Add one read-only observation boundary for the drivetrain without exposing mutable
`DriveIOInputs`, changing the control path, or introducing a publishing mechanism.

Source lesson:

`D00_L06_Simulation_IO_Layer`

Target lesson:

`D01_L01_Drive_Observation_Boundary`

## Architecture Contract

The frozen control path remains unchanged:

```text
Driver
-> Xbox Controller
-> controls
-> commands
-> DriveSubsystem
-> DriveIO
-> Hardware or Simulation
```

The lesson adds this parallel read-only observation path:

```text
DriveIO
-> DriveIOInputs
-> DriveSubsystem
-> DriveObservation
-> future read-only consumer
```

`DriveObservation` is an immutable domain snapshot. It describes the latest values copied
from the subsystem-owned inputs after a completed periodic update.

Telemetry is a separate future publishing mechanism. This lesson does not implement
NetworkTables, SmartDashboard, Glass publishing, telemetry coordination, encoders, gyro,
pose, odometry, or physics.

## Approved Production Changes

Created:

- `src/main/java/frc/robot/observation/drive/DriveObservation.java`

Modified:

- `src/main/java/frc/robot/subsystems/DriveSubsystem.java`

No other production Java file changed from D00_L06.

## Step 1 - Inherit D00_L06

**Objective:** Establish D01_L01 from the completed D00_L06 project.

**Why:** Inheritance preserves the previously verified WPILib project and frozen architecture.

**Action:** Copy the complete D00_L06 lesson project and activate the D01_L01 identity.

**Files Changed:** Lesson metadata only; production Java initially remains identical.

**Expected Result:** D01_L01 begins as a complete inherited project with status `IN_PROGRESS`.

**Verification:** Compare inherited Java file paths and hashes against D00_L06 before implementation.

## Step 2 - Verify the inherited baseline

**Objective:** Prove the inherited project builds before adding the lesson concept.

**Why:** A successful baseline separates inherited behavior from later D01_L01 changes.

**Action:** Run the repository Gradle wrapper using the WPILib 2026 Java 17 toolchain.

**Files Changed:** None.

**Expected Result:** The inherited project reports `BUILD SUCCESSFUL`.

**Verification:** Baseline Build is recorded as PASS in `LESSON_STATUS.md`.

## Step 3 - Review ownership of DriveIOInputs

**Objective:** Keep mutable hardware observations inside `DriveSubsystem`.

**Why:** Returning `DriveIOInputs` would expose a mutable internal object and break ownership.

**Action:** Confirm that `DriveSubsystem` owns one private `DriveIOInputs` instance and that
`periodic()` is the production update boundary.

**Files Changed:** None.

**Expected Result:** Mutable IO data remains private to the subsystem.

**Verification:** `io.updateInputs(inputs)` remains in `DriveSubsystem.periodic()` and is not
called by observation access.

## Step 4 - Approve the immutable observation design

**Objective:** Define the smallest safe public drive observation.

**Why:** Consumers need a cohesive read-only value without references to mutable IO state.

**Action:** Approve a Java record containing only left and right applied outputs.

**Files Changed:** None.

**Expected Result:** The design has no behavior, publishing, hardware access, or reverse dependency.

**Verification:** Architecture Review and package review both report PASS.

## Step 5 - Create DriveObservation

**Objective:** Add the immutable domain snapshot.

**Why:** A Java record provides final components and value semantics without custom mutable state.

**Action:** Create `DriveObservation` in `frc.robot.observation.drive` with two `double`
components: `leftAppliedOutput` and `rightAppliedOutput`.

**Files Changed:** Created
`src/main/java/frc/robot/observation/drive/DriveObservation.java`.

**Expected Result:** Callers can receive a detached immutable drivetrain observation.

**Verification:** Confirm the type is a public record, has exactly two components, and imports no
robot, vendor, command, or publishing API.

## Step 6 - Expose the subsystem copy-out API

**Objective:** Return the latest completed observation without exposing internal state.

**Why:** The subsystem owns the mutable inputs and is the correct boundary for copying them into a
public immutable value.

**Action:** Add `getObservation()` to `DriveSubsystem`; construct a new `DriveObservation` from
the current `inputs.leftAppliedOutput` and `inputs.rightAppliedOutput`.

**Files Changed:** Modified
`src/main/java/frc/robot/subsystems/DriveSubsystem.java`.

**Expected Result:** Every call returns an independent immutable snapshot of the latest completed
periodic update.

**Verification:** Confirm `getObservation()` does not call `updateInputs()`, output methods,
hardware APIs, or publishing APIs.

## Step 7 - Verify architecture preservation

**Objective:** Confirm the observation boundary does not change robot control.

**Why:** D01_L01 adds a read-only path and must not redesign the Frozen Backbone.

**Action:** Inspect RobotContainer, controls, commands, subsystem ownership, DriveIO, real IO, and
simulation IO.

**Files Changed:** None.

**Expected Result:** Control still flows through the existing controller, controls, command,
subsystem, IO, and selected hardware or simulation implementation.

**Verification:** RobotContainer remains byte-identical to D00_L06; no control or publishing
dependency originates from `DriveObservation`.

## Step 8 - Run the production build

**Objective:** Verify the complete production source compiles after the two approved Java changes.

**Why:** Compilation proves the new type and subsystem API integrate with the inherited project.

**Action:** Run `.\gradlew.bat build --no-daemon`.

**Files Changed:** None.

**Expected Result:** `BUILD SUCCESSFUL`.

**Verification:** Production Build is recorded as PASS.

## Step 9 - Verify observation values in simulation

**Objective:** Confirm values move from `DriveIOSim` through `DriveIOInputs` to
`DriveObservation`.

**Why:** The lesson contract requires evidence that the observation boundary reflects simulated IO
state.

**Action:** Exercise the subsystem with the previously approved temporary external verification
harness.

**Files Changed:** None in production; the temporary harness is removed after verification.

**Expected Result:** Initial, asymmetric, mixed, and stopped values match the expected snapshots.

**Verification:**

| Case | Expected | Result |
| --- | --- | --- |
| Initial state | `(0.0, 0.0)` | PASS |
| Positive asymmetric outputs | `(0.25, 0.60)` | PASS |
| Negative asymmetric outputs | `(-0.40, -0.75)` | PASS |
| Mixed outputs | `(0.50, -0.30)` | PASS |
| Stop behavior | `(0.0, 0.0)` | PASS |

## Step 10 - Verify immutability

**Objective:** Prove an earlier observation cannot change after later IO updates.

**Why:** A snapshot must represent one completed state and remain stable for its lifetime.

**Action:** Retain snapshot A, change simulated outputs, update the subsystem, and create snapshot B.

**Files Changed:** None.

**Expected Result:** Snapshot A retains its original values while snapshot B contains the new values.

**Verification:** Snapshot immutability result is PASS.

## Step 11 - Verify side-effect-free access

**Objective:** Prove observation access does not perform an immediate hardware or simulation read.

**Why:** IO updates belong to `periodic()`; a getter must not create a second update path.

**Action:** Read observations repeatedly between periodic updates while tracking
`updateInputs()` calls.

**Files Changed:** None.

**Expected Result:** Returned values remain stable and the IO update count does not increase.

**Verification:** Update count remains `4 -> 4`; No Immediate Hardware Read is PASS.

## Step 12 - Document and clean the lesson

**Objective:** Preserve verified engineering evidence without generated artifacts.

**Why:** A lesson must contain maintainable documentation and no temporary build or harness output.

**Action:** Create the lesson guides, record verification results, and remove generated artifacts.

**Files Changed:** Documentation and `LESSON_STATUS.md` only.

**Expected Result:** Documentation matches production source; lesson remains `IN_PROGRESS` pending
final freeze review.

**Verification:** Documentation review is PASS; `build/`, `.gradle/`, temporary harnesses, class
files, and logs are absent.

## Complete Source Code

### DriveObservation.java

```java
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.drive;

/**
 * Provides an immutable drivetrain observation from the latest completed subsystem periodic
 * update.
 *
 * @param leftAppliedOutput normalized, dimensionless left-side applied output
 * @param rightAppliedOutput normalized, dimensionless right-side applied output
 */
public record DriveObservation(
    double leftAppliedOutput,
    double rightAppliedOutput) {}
```

### DriveSubsystem.java

```java
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.io.drive.DriveIO;
import frc.robot.io.drive.DriveIO.DriveIOInputs;
import frc.robot.observation.drive.DriveObservation;

/**
 * Provides high-level drivetrain behavior.
 */
public class DriveSubsystem extends SubsystemBase {
  private final DriveIO io;
  private final DriveIOInputs inputs = new DriveIOInputs();

  /**
   * Creates the drive subsystem.
   *
   * @param io real or simulated drivetrain hardware
   */
  public DriveSubsystem(DriveIO io) {
    this.io = io;
    stop();
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
  }

  /**
   * Returns an immutable observation from the latest completed periodic update.
   *
   * <p>Before the first periodic update, the observation may contain default values.
   *
   * @return latest drivetrain observation
   */
  public DriveObservation getObservation() {
    return new DriveObservation(
        inputs.leftAppliedOutput,
        inputs.rightAppliedOutput);
  }

  /**
   * Drives the left and right sides independently.
   *
   * @param leftOutput left-side output
   * @param rightOutput right-side output
   */
  public void tankDrive(
      double leftOutput,
      double rightOutput) {
    double safeLeftOutput =
        MathUtil.clamp(
            leftOutput,
            DriveConstants.kMinimumDriveOutput,
            DriveConstants.kMaximumDriveOutput);

    double safeRightOutput =
        MathUtil.clamp(
            rightOutput,
            DriveConstants.kMinimumDriveOutput,
            DriveConstants.kMaximumDriveOutput);

    io.setTankOutputs(
        safeLeftOutput,
        safeRightOutput);
  }

  /**
   * Stops the complete drivetrain.
   */
  public void stop() {
    io.stop();
  }
}
```

## Final Verification Summary

- Production build: PASS
- Initial state: PASS
- Positive asymmetric outputs: PASS
- Negative asymmetric outputs: PASS
- Mixed outputs: PASS
- Stop behavior: PASS
- Snapshot immutability: PASS
- No immediate hardware read: PASS
- Real robot: NOT TESTED

The lesson implements no publishing mechanism. `DriveObservation` is the immutable domain
snapshot; telemetry remains a future read-only publishing responsibility.
