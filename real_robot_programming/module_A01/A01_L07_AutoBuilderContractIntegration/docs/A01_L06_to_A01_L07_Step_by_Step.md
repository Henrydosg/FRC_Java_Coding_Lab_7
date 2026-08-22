# A01_L06 to A01_L07 - Step-by-Step Activation Record

## Activation Identity

- Source lesson: `A01_L06_PathPlannerPathAndRuntimeIntegration`.
- Source status: `COMPLETE / FROZEN / READ-ONLY`.
- Active lesson: `A01_L07_AutoBuilderContractIntegration`.
- Active status: `IN_PROGRESS / EDITABLE`.
- Authoritative title: `A01_L07 - AutoBuilder Contract Integration`.
- Activation scope: documentation and lesson identity only; AutoBuilder is not
  implemented.
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

## Step 9 - Future Implementation Gate

### Objective

Define the boundary for the next authorized L07 implementation phase.

### Why

Activation is not implementation. The single new concept must be added only in
independently verifiable steps after design review.

### Action

Deferred AutoBuilder adapter/configuration, transformed execution-path factory,
focused contract tests, full regression, Simulation, Driver Station / Glass,
and real-robot verification to the next phase. Chooser, multiple routines,
NamedCommands, event markers, mechanisms, vision, AprilTags, pathfinding,
replanning, CTRE/CAN changes, and frozen predecessor changes remain forbidden.

### Files Changed

None beyond the activation documentation listed above.

### Verification

No `AutoBuilder.configure`, `AutoBuilder.followPath`, or
`AutoBuilderContractAdapter` implementation exists in L07 Java source.

### Expected Result

L07 is ready for implementation design, but not yet complete or frozen.

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
