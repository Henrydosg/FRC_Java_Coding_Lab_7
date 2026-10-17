# A01_L09 to V00_L01 - Step-by-Step Reconstruction and Transition Guide

## Purpose and Current State

This guide records why and how the canonical
`V00_L01_VisionCoordinateFramesAndCameraExtrinsics` was reconstructed from the
final authoritative A01 lesson. It intentionally does not repeat the stale
historical V00_L01 story as though that project were still the implementation
baseline.

Authoritative predecessor:

`A01_L09 @ 6b243bb - Complete reconstructed A01_L09 named commands and event markers`

Final V00_L01 state:

`COMPLETE / FROZEN / READ-ONLY`

The implementation and canonical User verification pass. Documentation has
been reconciled, and the separate Final Architecture Review and Final Closure
Review are PASS. User-owned Git publication remains pending.

## What Is Inherited and What Is New

### Inherited from final A01_L09

- The complete Driver-to-hardware Frozen Backbone.
- The IOInputs-to-Observation-to-telemetry flow.
- RobotContainer's composition-root role.
- Autonomous preparation, scheduler-native path composition, Robot-level
  scheduler exception handling, fatal-fault bridge, terminal `HOLDING`,
  centralized Swerve stop, Teleop mode gate, and no automatic restart.
- `frc.robot.autonomous.AutonomousEventId`, `Commands.defer(...)` fresh event
  construction, NamedCommands, event markers, immutable event observation,
  and read-only event telemetry.
- All Gradle, vendordep, deploy, and inherited documentation material.

The canonical comparison found 73 production and 56 test files hash-identical
to final A01_L09.

### New in V00_L01

- `src/main/java/frc/robot/vision/VisionFrameTransform.java`.
- `src/test/java/frc/robot/vision/VisionFrameTransformTest.java`.
- This transition guide and the V00_L01 English/Vietnamese learning guides.
- Reconciled lesson-local README, status, plan, and checklist metadata.

The Vision helper is pure geometry. It does not connect to a camera, IO,
Observation, telemetry, Swerve, autonomous, or hardware.

## Step 1 - Establish final A01_L09 as the authoritative predecessor

**Step:** 1

**Objective:** Identify the only valid inheritance source for V00_L01.

**Why:** Repository governance requires every lesson to inherit from its
immediately preceding completed lesson. A01 ends at L09, and A01_L10 is
prohibited. The final reconstructed L09 contains safety and event architecture
that earlier downstream copies did not contain.

**Action:** Use the User-supplied authoritative identity
`A01_L09 @ 6b243bb`, commit message
`Complete reconstructed A01_L09 named commands and event markers`, as the
parent boundary. Treat A01_L09 as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`.

**Files Changed:** None. The predecessor remains frozen and read-only.

**Verification:** Final A01_L09 documentation, the A01 and V00 roadmap ADRs,
and the supplied predecessor identity agree that A01 ends at L09 and V00_L01
must inherit from it.

**Expected Result:** Every later comparison and statement uses final A01_L09,
not a pre-reconstruction A01 or V00 snapshot.

## Step 2 - Identify why the historical V00_L01 became stale

**Step:** 2

**Objective:** Explain why an apparently completed historical lesson could no
longer serve as the implementation authority.

**Why:** The historical V00_L01 was produced before A01_L09 was reconstructed
from the final A01_L08 safety repair. It therefore reflected an older lineage,
including stale package placement and an older autonomous event-ID location.
Its old build and documentation evidence could remain educational history, but
could not prove inheritance from the new final predecessor.

**Action:** Classify the historical V00_L01 as
`STALE / NON-AUTHORITATIVE IMPLEMENTATION BASELINE`. Preserve useful
explanations only as evidence to review; do not restore its source or metadata
blindly.

**Files Changed:** None during diagnosis.

**Verification:** The historical guide identifies
`frc.robot.observation.vision.VisionFrameTransform` and a stale lesson lineage,
while the locked canonical design requires `frc.robot.vision` and final
A01_L09 inheritance.

**Expected Result:** Students understand that an old PASS can be historically
valid while still being unsuitable as the current parent.

## Step 3 - Prove that continuing from stale V00_L01 would violate inheritance

**Step:** 3

**Objective:** Connect the stale-baseline finding to the repository's
inheritance and safety rules.

**Why:** Continuing from the historical directory would skip the authoritative
immediate predecessor and could omit scheduler-native safety/event behavior.
That would create an unreviewed downstream architecture fork.

**Action:** Compare the required final A01_L09 contracts with the historical
V00 assumptions. Require the new baseline to include the final preparation
coordinator, scheduler exception boundary, fatal bridge, terminal ownership,
Teleop gate, event ID package, deferred NamedCommands architecture, and
no-manual-lifecycle rule.

**Files Changed:** None.

**Verification:** Governance requires frozen immediate-predecessor inheritance;
the final contracts named above are present in current A01_L09 and in the
canonical reconstructed V00_L01.

**Expected Result:** Reconstruction is recognized as a lineage correction, not
an optional refactor or a second V00 feature.

## Step 4 - Reconstruct V00_L01 from final A01_L09

**Step:** 4

**Objective:** Establish a clean lesson candidate containing the complete final
A01_L09 baseline.

**Why:** Reconstructing from the authoritative parent is the safest way to
avoid an incomplete manual forward-port and to preserve all inherited files
outside the one-concept V00 delta.

**Action:** Create an isolated V00_L01 reconstruction candidate from final
A01_L09, give it the approved
`V00_L01_VisionCoordinateFramesAndCameraExtrinsics` identity, and keep the
canonical lesson untouched until the candidate passes its audits and
verification.

**Files Changed:** The isolated candidate only. Frozen A01_L09 and protected
V00_L02 remain unchanged.

**Verification:** The candidate was available independently for inheritance,
design, source, test, and build review before controlled replacement.

**Expected Result:** One reviewable candidate starts with the exact final A01
architecture rather than accumulated historical patches.

## Step 5 - Use an isolated reconstruction-candidate workflow

**Step:** 5

**Objective:** Separate candidate engineering from canonical replacement.

**Why:** Isolation allows the inherited baseline and one V00 concept to be
audited without overwriting the canonical lesson prematurely or touching the
suspended successor.

**Action:** Perform the inheritance comparison, Design Lock review, Vision
implementation, test-oracle review, focused verification, and full build in the
candidate. Approve canonical transfer only after those gates pass.

**Files Changed:** Candidate source, candidate test, and candidate lesson-local
evidence only. No Git operation or protected-lesson change is implied.

**Verification:** Authoritative history records candidate independent oracle
review PASS, focused Vision test PASS, and full build PASS.

**Expected Result:** The canonical boundary receives a reviewed result, not an
untested partial reconstruction.

## Step 6 - Audit the inherited baseline

**Step:** 6

**Objective:** Prove that all non-Vision behavior remains the final A01_L09
behavior.

**Why:** A V00 geometry lesson must not silently change autonomous, drivetrain,
IO, event, dependency, or safety architecture.

**Action:** Compare production and test files by relative path and SHA-256,
then compare Gradle files, vendordeps, and deploy assets. Search for the final
event ID, NamedCommands/deferred event construction, scheduler exception
bridge, terminal `HOLDING`, central stop, Teleop gate, and any manual child
lifecycle calls.

**Files Changed:** None; this is a read-only audit.

**Verification:** The canonical audit finds 73 inherited production files and
56 inherited test files hash-identical to final A01_L09. Gradle, vendordeps,
and deploy assets match. Manual child lifecycle delegation is absent.

**Expected Result:** The only production/test differences are the locked
Vision helper and its focused test.

## Step 7 - Lock the V00_L01 design before implementation

**Step:** 7

**Objective:** Define exactly one new concept and prevent early expansion into
later V00 lessons.

**Why:** Coordinate-frame errors propagate into every future tag-pose,
measurement, estimation, and fusion feature. Conversely, introducing camera IO
or tag lookup now would violate the one-concept roadmap.

**Action:** Lock field, robot, and camera frames; WPILib NWU axes; meters;
radians; right-handed rotations; fixed `robotToCamera`; inverse
`cameraToRobot`; forward composition; and reverse robot-pose reconstruction.
Lock the helper to `frc.robot.vision` and the three public methods only.

**Files Changed:** Candidate design documentation only at this stage.

**Verification:** The V00 ADR assigns only coordinate frames and camera
extrinsics to L01. VisionIO, vendors, hardware, AprilTag lookup, quality,
timing, latency, fusion, Swerve, and autonomous changes remain deferred.

**Expected Result:** The implementation has a small, deterministic, and
reviewable boundary.

## Step 8 - Implement VisionFrameTransform

**Step:** 8

**Objective:** Encode the locked composition and inversion contract in one pure
utility.

**Why:** Future V00 work needs one explicit transform-direction authority, but
L01 needs no runtime service, mutable model, Observation, subsystem, or IO
abstraction.

**Action:** Add non-instantiable final class
`frc.robot.vision.VisionFrameTransform` with:

```java
fieldToCamera(Pose3d fieldToRobot, Transform3d robotToCamera)
cameraToRobot(Transform3d robotToCamera)
fieldToRobotFromCamera(Pose3d fieldToCamera, Transform3d robotToCamera)
```

Use WPILib geometry composition/inversion and reject null or nonfinite input
and nonfinite computed output.

**Files Changed:**
`src/main/java/frc/robot/vision/VisionFrameTransform.java` in the candidate,
later transferred unchanged to canonical V00_L01.

**Verification:** Source review confirms no state, vendor, camera, IO,
subsystem, command, telemetry, NetworkTables, DriverStation, scheduler, or
hardware dependency.

**Expected Result:** One deterministic geometry helper implements only the
Design-Locked API.

## Step 9 - Strengthen tests with independent mathematical oracles

**Step:** 9

**Objective:** Verify the math without merely duplicating the same expression
used by production.

**Why:** A test that computes its expected value only with
`transformBy(...)` could repeat the implementation's direction mistake.
Independent numeric expectations expose reversed transforms, axis mistakes,
and incorrect composition order.

**Action:** Add focused tests for identity, translation, rotation, combined 3D
composition, inverse, forward/reverse round trip, noncommutativity, NWU axis
signs, meters, radians, nulls, nonfinite values, determinism, and no mutation.
Include the independent oracle:

```text
fieldToRobot = (1 m, 2 m, 0 m), yaw +90 degrees
robotToCamera translation = (1 m, 0 m, 0 m)
expected fieldToCamera position = (1 m, 3 m, 0 m)
```

The result is not `(2 m, 2 m, 0 m)` because the camera offset is expressed in
the rotated robot axes.

**Files Changed:**
`src/test/java/frc/robot/vision/VisionFrameTransformTest.java` in the candidate,
later transferred unchanged to canonical V00_L01.

**Verification:** Independent mathematical-oracle review: `PASS`.

**Expected Result:** Tests can detect transform direction, composition order,
axis, unit, and validation defects independently.

## Step 10 - Run focused candidate verification

**Step:** 10

**Objective:** Verify the one new V00 concept in isolation.

**Why:** A focused test result gives a direct signal about the frame helper
before inherited project behavior is considered.

**Action:** Run the focused `VisionFrameTransformTest` in the isolated
candidate using the intended Java 17 environment.

**Files Changed:** Generated build output only; no source repair is recorded.

**Verification:** Candidate focused Vision test: `PASS` by authoritative
evidence.

**Expected Result:** The locked helper and independent oracles pass together.

## Step 11 - Run the candidate full build

**Step:** 11

**Objective:** Verify that the V00 addition coexists with the complete inherited
A01_L09 project.

**Why:** Focused geometry tests cannot alone prove that the inherited source,
tests, dependencies, and assets still form a valid independent WPILib lesson.

**Action:** Run the candidate's full build after the focused test passes.

**Files Changed:** Generated build output only.

**Verification:** Candidate full build: `PASS` by authoritative evidence.

**Expected Result:** The candidate is eligible for controlled canonical
replacement without a production repair or scope expansion.

## Step 12 - Perform the controlled canonical replacement

**Step:** 12

**Objective:** Make the verified reconstruction the canonical V00_L01 lesson.

**Why:** The stale historical baseline must no longer represent the active
implementation, but replacement must occur only after the isolated candidate
passes.

**Action:** Transfer the reviewed candidate into the canonical V00_L01 path
under the approved controlled replacement workflow. Keep final A01_L09 frozen
and V00_L02 suspended and untouched.

**Files Changed:** The canonical V00_L01 lesson boundary only. No Git
publication is performed by Codex or implied by the transfer.

**Verification:** The canonical path contains the final A01_L09 baseline plus
only the locked `frc.robot.vision` helper and focused test.

**Expected Result:** The canonical lesson now has correct predecessor lineage
and one V00 concept.

## Step 13 - Remove or exclude stale historical source locations

**Step:** 13

**Objective:** Prevent two competing owners for the same Vision mathematics or
event identity.

**Why:** Keeping both old and new package locations would create duplicate
responsibility and could cause imports to preserve the stale architecture.

**Action:** Ensure the reconstructed canonical lesson does not contain:

- `src/main/java/frc/robot/observation/vision/VisionFrameTransform.java`;
- `src/test/java/frc/robot/observation/vision/VisionFrameTransformTest.java`;
- `src/main/java/frc/robot/commands/AutonomousEventId.java`.

Retain the final event identity at
`src/main/java/frc/robot/autonomous/AutonomousEventId.java`.

**Files Changed:** Stale historical locations are absent from the reconstructed
canonical baseline; the final inherited event file remains unchanged.

**Verification:** Canonical filesystem audit records all three stale locations
`ABSENT` and the final autonomous event-ID location `PRESENT`.

**Expected Result:** Vision geometry has one owner in `frc.robot.vision`, and
autonomous event identity has one owner in `frc.robot.autonomous`.

## Step 14 - Verify the canonical focused test

**Step:** 14

**Objective:** Prove the transferred canonical lesson, not only the candidate,
passes the V00 contract test.

**Why:** A correct candidate result does not by itself prove that canonical
replacement preserved every intended file.

**Action:** The User ran the canonical focused Vision test with Java 17 after
replacement.

**Files Changed:** Generated verification output only.

**Verification:** Canonical focused `VisionFrameTransformTest`:
`PASS / USER-VERIFIED`.

**Expected Result:** The canonical helper and its independent oracles are
confirmed after transfer.

## Step 15 - Verify the canonical clean and full build

**Step:** 15

**Objective:** Prove the complete canonical lesson is clean and buildable.

**Why:** Closure evidence must describe the canonical directory rather than
relying only on a candidate result.

**Action:** The User independently verified the canonical Java 17 lesson's
clean state, full build, and absence of the accidental `-Recurse` artifact.

**Files Changed:** Generated verification output only.

**Verification:** `CLEAN: PASS`; `FULL BUILD: PASS`; accidental `-Recurse`
artifact: `ABSENT`, all supplied as authoritative User evidence.

**Expected Result:** Canonical implementation verification is complete without
reinterpreting the User-owned gates or inventing command details.

## Step 16 - Reconcile lesson documentation

**Step:** 16

**Objective:** Make the canonical documentation match the actual
reconstruction, package ownership, design scope, and verification state.

**Why:** The transferred directory intentionally inherited A01_L09 metadata
and lacked the new V00 transition and learning guides. Leaving those files
unchanged would falsely identify the lesson and hide the reconstruction
history.

**Action:** Reconcile `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`, and
`LESSON_CHECKLIST.md`; create this transition guide and the English/Vietnamese
learning guides; classify all inherited documents; preserve useful A01 history;
and, at that reconciliation stage, record V00_L01 as
`REOPENED / IN_PROGRESS / EDITABLE` pending final review.

**Files Changed:** Seven V00_L01 documentation files only.

**Verification:** The required files exist; they name A01_L09 at `6b243bb`,
separate inherited A01 behavior from new V00 behavior, record User verification,
defer later V00 concepts, protect V00_L02, and preserve the then-pending
final-review state as historical evidence.

**Expected Result:** A student can understand the current canonical lesson
without the chat history or stale historical documents.

## Step 17 - Perform the separate final architecture and closure sequence

**Step:** 17

**Objective:** Complete the separate review gates before freezing the lesson.

**Why:** Passing implementation, tests, build, and documentation does not
replace the required final Architect/Reviewer decision.

**Action:** Follow this sequence:

```text
reconciled documentation
    -> separate Final Architecture Review PASS
    -> separate Final Closure Review and explicit freeze approval PASS
    -> COMPLETE / FROZEN / READ-ONLY metadata update
```

Do not activate or reconcile V00_L02 until corrected V00_L01 is explicitly
frozen and a separate successor action is authorized.

**Files Changed:** Final lifecycle metadata only after explicit authorization.

**Verification:** Current metadata records Final Architecture Review PASS,
Final Closure Review PASS, and `COMPLETE / FROZEN / READ-ONLY`.

**Expected Result:** V00_L01 is frozen and read-only; V00_L02 remains suspended
until a separate action is authorized.

## Step 18 - Keep Git User-owned

**Step:** 18

**Objective:** Preserve the repository's fixed ownership of publication.

**Why:** Codex may document and audit the lesson but may not stage, commit,
push, inspect, or claim completion of User-owned Git operations.

**Action:** After the completed closure and freeze authorization, the User alone
performs Git add, commit, and push. Record V00_L01 Git publication as pending
until those User-owned operations succeed.

**Files Changed:** None by this step.

**Verification:** Codex ran no Git command during reconstruction documentation
reconciliation. Target lesson commit and push remain `PENDING / USER OWNED`.

**Expected Result:** Documentation readiness and Git publication remain two
truthfully separate states.

## Verification and Scope Summary

| Area | Result | Evidence boundary |
| --- | --- | --- |
| Authoritative parent | PASS | User-supplied A01_L09 at `6b243bb`; A01 ends at L09. |
| Baseline inheritance | PASS | 73 production and 56 test files hash-identical; config/assets preserved. |
| V00 production delta | PASS | One `frc.robot.vision.VisionFrameTransform` file. |
| V00 test delta | PASS | One focused `frc.robot.vision.VisionFrameTransformTest` file. |
| Stale package removal | PASS | Historical Vision and commands event-ID paths absent. |
| Candidate verification | PASS | Independent oracle review, focused Vision test, and full build. |
| Canonical verification | PASS | User-verified Clean, focused Vision test, full build; `-Recurse` artifact absent. |
| Simulation / DS / Glass / real robot | NOT APPLICABLE | Pure deterministic geometry with no runtime or hardware path. |
| Documentation | FINAL / PASS | Seven V00_L01 documentation files reconciled/created and final closure recorded. |
| V00_L02 | PROTECTED | Suspended, read-only, unmodified, and not activated. |
| Final review/freeze | PASS | Final Architecture Review and Final Closure Review passed; lesson frozen. |
| Git | USER OWNED / PENDING | No Git operation performed by Codex. |

This guide makes no claim about physical camera mounting values, camera/vendor
operation, AprilTag lookup, target quality, timestamp/latency, pose-estimator
integration, vision fusion, autonomous changes, Swerve changes, or competition
readiness.
