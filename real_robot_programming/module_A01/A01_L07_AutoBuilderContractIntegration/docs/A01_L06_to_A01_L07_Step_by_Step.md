# A01_L06 to A01_L07 - Step-by-Step Activation and Implementation Record

## Activation Identity

- Source lesson: `A01_L06_PathPlannerPathAndRuntimeIntegration`.
- Source status: `COMPLETE / FROZEN / READ-ONLY`.
- Active lesson: `A01_L07_AutoBuilderContractIntegration`.
- Active status: `COMPLETE / FROZEN / READ-ONLY`.
- Authoritative title: `A01_L07 - AutoBuilder Contract Integration`.
- Activation scope: documentation and lesson identity, followed by the
  approved single AutoBuilder contract implementation.
- Git: user-owned; Codex ran no Git commands.

## Step 1 - Confirm the Frozen Predecessor

### Objective

Confirm that L06 is complete and frozen before inheritance.

### Why

Strict inheritance protects the completed predecessor and prevents an active
lesson from being created from an unfinished baseline.

### Action

Reviewed L06 identity, status, final README, plan, checklist, transition guide,
source, tests, PathPlanner asset, learning guides, Swerve authority, and safety
contracts.

### Files Changed

None.

### Verification

L06 records `COMPLETE / FROZEN / READ-ONLY`; its production Java and tests are
the inheritance baseline.

### Expected Result

L06 remains frozen and uneditable.

## Step 2 - Copy the Project

### Objective

Create the official L07 project through inheritance.

### Why

Every lesson is an independent WPILib project copied from the immediately
preceding completed lesson.

### Action

Copied L06 into:

`real_robot_programming/module_A01/A01_L07_AutoBuilderContractIntegration`

### Files Changed

The new L07 project was created by copy; L06 was not edited.

### Verification

The copied production Java and test files remain identical to the L06 baseline.

### Expected Result

L07 begins as a strict L06 inheritance snapshot.

## Step 3 - Rename Lesson Identity

### Objective

Set the official L07 project and lesson identity.

### Why

The active lesson must be unambiguous while Java packages and inherited runtime
contracts remain unchanged.

### Action

Updated only L07 documentation identity to
`A01_L07_AutoBuilderContractIntegration` and
`A01_L07 - AutoBuilder Contract Integration`.

### Files Changed

L07 `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`, and
`LESSON_CHECKLIST.md`.

### Verification

No Java package, production source, test, asset, or project architecture change
was made.

### Expected Result

L07 has the authoritative identity and remains editable.

## Step 4 - Remove Generated Artifacts

### Objective

Keep inherited generated outputs out of the new lesson baseline.

### Why

Generated outputs are not source, tests, assets, or documentation and must not
be inherited as lesson content.

### Action

Removed inherited generated directories and runtime outputs from L07 only,
including `build`, `.gradle`, `bin`, and other applicable generated artifacts.

### Files Changed

L07 generated artifacts only.

### Verification

L06 generated/source/documentation content was not targeted by this cleanup.

### Expected Result

L07 begins from clean inherited project content.

## Step 5 - Record the Inherited Baseline

### Objective

Record the user-owned Java 17 inherited baseline before implementation.

### Why

The baseline proves that the copied project is buildable before adding the one
new L07 concept.

### Action

Recorded the user-supplied results: `compileJava PASS`, `compileTestJava PASS`,
`tests PASS`, and `clean build PASS`.

### Files Changed

L07 activation documentation only.

### Verification

User supplied the baseline evidence; Codex does not claim Simulation, Glass, or
real-robot verification from this evidence.

### Expected Result

The inherited L07 baseline gate is PASS.

## Step 6 - Complete the Governance Audit

### Objective

Confirm that L07 activation is authorized by the frozen governance and roadmap.

### Why

Activation may not change lesson order, frozen boundaries, package ownership,
or the Frozen Backbone.

### Action

Re-read AGENTS.md, repository README, authoritative Documents A/B/C, the Frozen
Backbone and Interface Contract, A01 ADR, frozen L01-L06, L06 final documents,
the copied L07 project, and the pre-activation design record.

### Files Changed

None outside L07 activation documentation.

### Verification

Governance PASS; A01_L07 is the authorized successor after frozen A01_L06.

### Expected Result

L07 becomes the sole active lesson without reopening L06 or changing A01 order.

## Step 7 - Record the Alliance-Transform Design Lock

### Objective

Carry the approved exactly-once alliance ownership into active L07 documents.

### Why

AutoBuilder must not introduce a second transformation or replace the frozen
L04 field/alliance contract.

### Action

Recorded:

- owner: `A01/L04 FieldAllianceTransform`;
- AutoBuilder flipping: disabled;
- `shouldFlipPath = false`;
- fresh execution `PathPlannerPath.preventFlipping = true`;
- canonical Blue path unchanged; and
- Red execution as canonical path -> L04 transform -> fresh path -> direct
  AutoBuilder follow.

### Files Changed

L07 activation documentation only.

### Verification

Design Lock is PASS; ADR change is not required.

### Expected Result

Exactly one transform owner is explicit before implementation begins.

## Step 8 - Activate L07 Documentation

### Objective

Make L07 the single active lesson.

### Why

Only one lesson may be editable under the repository lifecycle.

### Action

Set L07 to `IN_PROGRESS / EDITABLE`, retained L06 as
`COMPLETE / FROZEN / READ-ONLY`, and added this transition record.

### Files Changed

L07 `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`,
`LESSON_CHECKLIST.md`, and this file.

### Verification

L07 documentation is present and identifies the active lesson; L06 documents
remain untouched.

### Expected Result

L07 is active and ready for a future implementation design review.

## Step 9 - Implementation Gate and Design Lock

### Objective

Define the boundary for the next authorized L07 implementation phase.

### Why

Activation is not implementation. The single new concept must be added only in
independently verifiable steps after design review.

### Action

Authorized the single AutoBuilder contract concept after implementation design
review. Chooser, multiple routines, NamedCommands, event markers, mechanisms,
vision, AprilTags, pathfinding, replanning, CTRE/CAN changes, and frozen
predecessor changes remain forbidden.

### Files Changed

None beyond the activation documentation listed above.

### Verification

The approved design lock preserves L04 transform ownership,
`shouldFlipPath = false`, execution `preventFlipping = true`, shared
RobotConfig, SwerveSubsystem requirement ownership, centralized stop, and no
automatic restart.

### Expected Result

L07 is authorized for the implementation steps below and remains IN_PROGRESS,
not COMPLETE or FROZEN.

## Step 10 - Implement the AutoBuilder Contract Boundary

### Objective

Add only the approved adapter/configuration and fresh execution-path factory.

### Why

AutoBuilder must integrate existing pose, reset, measured-speed, output,
controller, RobotConfig, requirement, lifecycle, and safety contracts without
becoming a drivetrain or transform owner.

### Action

Added `AutoBuilderContractAdapter` and
`PathPlannerExecutionPathFactory`; exposed the canonical path from
`PathPlannerTrajectoryAdapter`; and wired `RobotContainer` to configure once
and obtain the path command through `AutoBuilder.followPath(...)`.

### Files Changed

- `src/main/java/frc/robot/commands/AutoBuilderContractAdapter.java` (added)
- `src/main/java/frc/robot/commands/PathPlannerExecutionPathFactory.java` (added)
- `src/main/java/frc/robot/commands/PathPlannerTrajectoryAdapter.java` (modified)
- `src/main/java/frc/robot/RobotContainer.java` (modified)

### Verification

The exact verified PathPlannerLib Consumer overload is used once. Vendor
flipping is disabled, the fresh execution path is marked `preventFlipping`,
and L04 `FieldAllianceTransform` performs the only alliance transform.
Callback validation, monotonic fault latching, terminal stop, timeout, mode
loss handling, and SwerveSubsystem requirement ownership are implemented.

### Expected Result

The approved AutoBuilder contract is integrated without changing RobotConfig,
Swerve configuration, IO, assets, or frozen L01-L06 lessons.

## Step 11 - Add Focused L07 Contract Tests

### Objective

Independently verify path immutability, exactly-once alliance transformation,
AutoBuilder configuration, and autonomous lifecycle integration.

### Why

The new boundary must be testable without claiming Simulation or real-robot
evidence.

### Action

Added the execution-path factory tests and updated the L07 integration and
autonomous scheduling tests with static AutoBuilder reset isolation.

### Files Changed

- `src/test/java/frc/robot/commands/PathPlannerExecutionPathFactoryTest.java` (added)
- `src/test/java/frc/robot/RobotContainerPathPlannerIntegrationTest.java` (modified)
- `src/test/java/frc/robot/RobotContainerAutonomousModeSchedulingTest.java` (modified)

### Verification

Focused L07 tests pass, including Blue-copy immutability, Red L04 transformation,
unsupported-path rejection, `shouldFlipPath = false`, lifecycle, and
requirement behavior.

### Expected Result

The implementation contract is covered while chooser, events, and additional
autonomous routines remain absent.

## Step 12 - Build and Regression Verification

### Objective

Verify the authorized production and test delta under the project toolchain.

### Why

Compilation and inherited regression are required before user-owned runtime
verification.

### Action

Ran `compileJava`, `compileTestJava`, the focused L07 tests, the full test suite,
and `clean build` with Java 17 and `-PteamNumber=0`.

### Files Changed

Generated build outputs only; no additional source scope.

### Verification

All compile tasks passed. Focused tests passed. The full suite passed with 424
tests and 0 failures. The clean build passed.

### Expected Result

The implementation is ready for ChatGPT implementation review.

## Step 13 - User-Owned Runtime Boundary

### Objective

Preserve the governance boundary for runtime evidence and lesson closure.

### Why

Codex may not claim Simulation, Driver Station / Glass, or real-robot results
without user-supplied evidence.

### Action

Recorded Simulation, Driver Station / Glass, and real-robot verification as
not run by Codex and still user-owned.

### Files Changed

L07 documentation only.

### Verification

L07 remains `IN_PROGRESS / EDITABLE`; L06 remains
`COMPLETE / FROZEN / READ-ONLY`.

### Expected Result

The implementation stops at the requested review boundary and does not mark
the lesson COMPLETE / FROZEN.

## Preserved Swerve Authority

- Drive ratio: `6.75:1`.
- FL CANcoder offset: `+0.068603515625`.
- FR CANcoder offset: `+0.014404296875`.
- BL CANcoder offset: `+0.46240234375`.
- BR CANcoder offset: `-0.057373046875`.

## Preserved Pre-Activation Knowledge

The copied pre-activation design record remains in L07 documentation. Its
Optional-to-vendor callback bridge, pose/reset/speed/output contracts,
exact-once configuration guard, RobotConfig reuse, SwerveSubsystem requirement,
session fault latch, terminal stop guarantee, and alliance ownership are all
carried into the active L07 design. The active Design Lock above resolves its
former pre-activation alliance blocker.

## Step 14 - Reconcile User-Owned Simulation Evidence

### Objective

Record the supplied L07 Simulation and telemetry evidence without closing or
freezing the lesson.

### Why

Simulation verifies the integrated architecture and lifecycle behavior before
the user performs the separate real-robot gate.

### Action

Recorded Blue and Red autonomous PASS, exactly-one alliance-transform PASS,
pose-validity and heading-stability PASS, disable/mode-loss stop PASS, and
no-automatic-restart PASS.

### Files Changed

L07 documentation only: `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`,
`LESSON_CHECKLIST.md`, and this transition record.

### Verification

The user supplied Blue readiness and one-meter execution evidence. The user
supplied Red final EstimatedPose `(15.535553 m, 8.069000 m, -180.000000 deg)`.
The user also supplied the Blue disable stop near `(0.400765 m, 0 m, 0 deg)`
and confirmed that re-enable without fresh readiness did not restart motion.

### Expected Result

L07 Simulation is recorded as `PASS / USER-SUPPLIED`; L07 remains
`IN_PROGRESS / EDITABLE` because real-robot verification is not yet tested.

## Step 15 - Preserve the Real-Robot Boundary

### Objective

Keep the lesson open after Simulation while documenting the remaining gate.

### Why

Simulation cannot establish real motor response, traction, wiring, CAN behavior,
physical geometry, or final dynamics.

### Action

Recorded L07 real-robot verification as `DEFERRED / NOT TESTED`, planned by the
user for Monday. No COMPLETE, FROZEN, or READ-ONLY claim was added.

### Files Changed

L07 documentation only; no Java, tests, assets, configuration, or frozen L06
files were changed.

### Verification

The transition guide remains `IN_PROGRESS`, and the real-robot gate remains
user-owned.

### Expected Result

The active L07 lesson remained editable and ready for the user-owned real-robot
verification step at this point in the recorded workflow. The later closure step
supersedes this temporary open state.

## Step 16 - Reconcile Final User-Owned Real-Robot Evidence and Freeze L07

### Objective

Close L07 only after the user confirms the current AutoBuilder implementation
passed physical real-robot execution.

### Why

The A01 ADR requires explicit user-owned Real Robot evidence before a lesson can
be marked COMPLETE / FROZEN / READ-ONLY. Simulation and builds alone are not
hardware evidence.

### Action

Recorded the user's explicit confirmation that the current A01_L07
AutoBuilderContractIntegration physical-robot execution passed. Updated the
lesson identity, status, plan, checklist, README, learning guides, and this
transition guide to close the lesson.

### Files Changed

Documentation/status artifacts only: repository README, L07 README,
LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md, this transition guide,
and the English/Vietnamese learning guides.

### Verification

Implementation, compileJava, compileTestJava, focused L07 tests, AutoBuilder and
PathPlanner tests, inherited L01-L06 regression, full 424-test suite with zero
failures, clean build, Blue and Red Simulation, exactly-one L04 transform,
disable/mode-loss stop, no automatic restart, documentation, and user-confirmed
Real Robot evidence are all PASS.

### Expected Result

A01_L07 is `COMPLETE / FROZEN / READ-ONLY`. No exact real-robot endpoint
accuracy, final PID/feedforward tuning, or final physical-model
characterization is claimed. A01_L08 is not created or implemented.
