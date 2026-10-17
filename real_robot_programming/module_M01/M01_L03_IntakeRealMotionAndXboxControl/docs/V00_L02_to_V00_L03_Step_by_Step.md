# V00_L02 to V00_L03 - Step-by-Step Transition Guide

## Purpose and current status

This guide explains, in student-facing chronological form, how
`V00_L03_VisionIOAndImmutableObservationContract` was derived from the final
authoritative `V00_L02_AprilTagFieldLayoutContract @ 53e9b9f`.

The guide separates inherited behavior, new L03 behavior, test-oracle repair,
verification evidence, and the remaining closure/publication decisions. It does
not treat the copied V00_L02 metadata as implementation evidence and does not
invent commands, test counts, measurements, hardware results, or Git results.

Current lesson state:

`COMPLETE / FROZEN / READ-ONLY / IMPLEMENTATION COMPLETE / USER-VERIFIED /
DOCUMENTATION COMPLETE / FINAL ARCHITECTURE AUDIT PASS / PREDECESSOR
PROVENANCE PASS / FINAL CLOSURE REVIEW PASS`

The lesson content/state is complete, frozen, and read-only. No active editable
lesson remains. User-owned Git add/commit/push publication remains pending and
is intentionally separate from lesson closure.

## What was inherited and what was added

### Inherited from final V00_L02

- the complete V00_L02 project baseline;
- V00_L01 coordinate-frame and camera-extrinsic semantics;
- V00_L02 official AprilTag field-layout contract;
- the final inherited A01 safety and event architecture;
- Frozen Backbone and Frozen Interface Contract;
- Document A/B/C observation boundaries;
- RobotContainer composition-root role;
- Swerve ownership, centralized stop, terminal ownership, and no-restart
  behavior;
- Gradle, vendordeps, configuration, source resources, and deploy assets; and
- all inherited production, test, and learning documentation except the
  L03-specific metadata and guides.

### New in V00_L03

- `frc.robot.io.vision.VisionIO` with mutable one-cycle transport types;
- `frc.robot.observation.vision.VisionObservation` with immutable state and
  target values;
- focused contract tests for those two boundaries;
- the L03 implementation/verification records; and
- the final L03 transition and bilingual learning-guide updates.

### Explicitly not added

No vendor adapter, Limelight, PhotonVision, NetworkTables acquisition, runtime
producer, telemetry, deterministic camera simulation, field-layout consumer,
pose estimation, quality/ambiguity policy, timestamp/latency policy, fusion,
Swerve, autonomous, PathPlanner, Robot, RobotContainer, command, subsystem,
scheduler, configuration, dependency, asset, or predecessor change was added.

## Step 1 - Confirm the final V00_L02 predecessor

- **Objective:** Establish the only authoritative source lesson.
- **Why:** A new V00 lesson must inherit the immediately preceding lesson only
  after that predecessor is complete, frozen, and read-only.
- **Action:** Confirm V00_L02 at `53e9b9f` is
  `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`.
- **Files Changed:** None.
- **Verification:** User publication evidence and V00_L02 metadata identify
  `53e9b9f` as the published predecessor.
- **Expected Result:** V00_L02 is the stable source snapshot for L03.
- **Status:** `COMPLETE / PASS`.

## Step 2 - Copy the authoritative predecessor

- **Objective:** Preserve accepted behavior before introducing the L03 concept.
- **Why:** Inheritance development prevents accidental loss of V00 and A01
  safety, frame, field-layout, and testing architecture.
- **Action:** The User copied the published V00_L02 project into the approved
  V00_L03 lesson identity.
- **Files Changed:** The prepared V00_L03 lesson directory; V00_L02 remained
  protected.
- **Verification:** The later no-Git comparison found the inherited baseline
  intact.
- **Expected Result:** L03 begins as a genuine V00_L02-derived project.
- **Status:** `COMPLETE / USER OWNED`.

## Step 3 - Rename the lesson identity

- **Objective:** Apply the exact roadmap-approved directory and lesson name.
- **Why:** Lesson order and identity are governed; a copied directory must not
  remain labeled as V00_L02.
- **Action:** The User named the copy
  `V00_L03_VisionIOAndImmutableObservationContract`.
- **Files Changed:** Copied lesson identity and its activation metadata.
- **Verification:** The approved V00_L03 directory exists under `module_V00`.
- **Expected Result:** A distinct L03 candidate exists without changing L02.
- **Status:** `COMPLETE / USER OWNED`.

## Step 4 - Handle generated artifacts separately

- **Objective:** Keep generated output out of inheritance authority.
- **Why:** `build`, `.gradle`, `bin`, `.vscode`, and `.wpilib` state is
  reproducible machine output, not source architecture.
- **Action:** The User handled generated artifacts during preparation. The
  current working copy may contain regenerated outputs for verification; they
  are excluded from the source comparison and publication boundary.
- **Files Changed:** Generated outputs only.
- **Verification:** The no-Git comparison excluded generated directories and
  compared authoritative files directly.
- **Expected Result:** Source inheritance can be audited independently of local
  build state.
- **Status:** `COMPLETE / PASS`.

## Step 5 - Run the inherited baseline build

- **Objective:** Prove the copied project works before L03 implementation.
- **Why:** A failing inherited baseline must not be confused with an L03 defect.
- **Action:** The User ran the inherited baseline with WPILib Java 17.
- **Files Changed:** No authoritative source change; generated build output
  only.
- **Verification:** User-supplied baseline build result: `PASS`.
- **Expected Result:** The copied V00_L02 architecture is a viable L03 base.
- **Status:** `COMPLETE / PASS`.

## Step 6 - Read governance and authorities

- **Objective:** Establish the rules that bound the L03 change.
- **Why:** Document A/B/C, AGENTS.md, the ADRs, and the V00 roadmap have higher
  authority than convenience or implementation preference.
- **Action:** Read repository `AGENTS.md`, root `README.md`, all authoritative
  English Documents A/B/C, applicable A00/A01/A01_L08/V00 ADRs, V00_L01 and
  V00_L02 records, and the active L03 records/source.
- **Files Changed:** None.
- **Verification:** The read set confirmed the Frozen Backbone, immutable
  observation boundary, one-concept rule, V00 order, and User-owned Git rule.
- **Expected Result:** Implementation scope is reviewable before coding.
- **Status:** `COMPLETE / PASS`.

## Step 7 - Perform the inheritance and architecture audit

- **Objective:** Prove that preparation preserved the final lineage and frozen
  boundaries.
- **Why:** A copied project can silently contain stale or unexplained changes.
- **Action:** Compare V00_L02 and prepared V00_L03 without Git, excluding
  generated output; inspect package ownership, dependencies, configuration,
  resources, deploy assets, and inherited A01 safety/event behavior.
- **Files Changed:** None.
- **Verification:** There were 219 comparable non-generated files in each
  lesson and zero pre-implementation differences. The L01/L02 predecessor
  boundaries remained protected.
- **Expected Result:** L03 has a trustworthy inherited baseline.
- **Status:** `COMPLETE / PASS`.

## Step 8 - Approve the Design Lock

- **Objective:** Define one precise L03 concept before implementation.
- **Why:** A camera lesson can grow into vendor, telemetry, simulation,
  estimation, or fusion work unless the boundary is explicit.
- **Action:** Lock `VisionIO` in `frc.robot.io.vision`, immutable
  `VisionObservation` in `frc.robot.observation.vision`, the one-cycle fields,
  positive tag identity, camera-relative `Transform3d`, five states, defensive
  ownership, and all later-roadmap exclusions.
- **Files Changed:** None; this was a design decision.
- **Verification:** Design Lock was approved by the Architect/User process.
- **Expected Result:** Implementation has an exact, testable four-file boundary.
- **Status:** `COMPLETE / APPROVED`.

## Step 9 - Activate V00_L03

- **Objective:** Make L03 the sole editable lesson.
- **Why:** Only an active `IN_PROGRESS` lesson may receive implementation
  changes.
- **Action:** Reconcile activation metadata while preserving V00_L01 and V00_L02
  as frozen published lessons.
- **Files Changed:** Activation-era repository and L03 documentation.
- **Verification:** V00_L03 became the sole `IN_PROGRESS / EDITABLE` lesson.
- **Expected Result:** Implementation can proceed only within the approved lock.
- **Status:** `COMPLETE / PASS`.

## Step 10 - Obtain separate implementation authorization

- **Objective:** Confirm permission for the exact source and test files.
- **Why:** Design Lock and lifecycle activation alone do not authorize Java
  implementation.
- **Action:** Receive separate Architect/User authorization for the two
  production files and two focused-test files.
- **Files Changed:** None until authorization.
- **Verification:** Authorization was supplied for exactly
  `VisionIO.java`, `VisionObservation.java`, `VisionIOTest.java`, and
  `VisionObservationTest.java`.
- **Expected Result:** No unrelated Java or test file may change.
- **Status:** `COMPLETE / APPROVED`.

## Step 11 - Implement the VisionIO transport boundary

- **Objective:** Add vendor-neutral one-cycle source transport.
- **Why:** Future adapters need one stable boundary without leaking vendor
  result objects to the rest of the robot.
- **Action:** Implement `VisionIO` with only
  `updateInputs(VisionIOInputs)`. Keep `VisionIOInputs` mutable with
  `available`, `connected`, `sampleValid`, and acquisition-ordered targets;
  keep `VisionTargetInputs` limited to positive identity transport and
  `cameraToTarget`.
- **Files Changed:** `src/main/java/frc/robot/io/vision/VisionIO.java` only.
- **Verification:** Source audit confirms no vendor, NetworkTables, scheduler,
  telemetry, or control dependency and only the locked public method.
- **Expected Result:** A future adapter can publish one complete transport
  snapshot per cycle.
- **Status:** `COMPLETE / PASS`.

## Step 12 - Implement the immutable Observation boundary

- **Objective:** Add immutable domain meaning for one coherent vision sample.
- **Why:** Consumers must not retain mutable transport state or vendor objects.
- **Action:** Implement `VisionObservation` with the five locked states,
  immutable target values, state/list consistency, positive IDs, finite
  observable transform values, defensive collection copying, and defensive
  transform ownership.
- **Files Changed:** `src/main/java/frc/robot/observation/vision/VisionObservation.java` only.
- **Verification:** Source audit confirms no hardware, vendor, NetworkTables,
  CommandScheduler, RobotContainer, telemetry, or control dependency.
- **Expected Result:** An immutable vendor-neutral observation can be safely
  passed to future read-only consumers.
- **Status:** `COMPLETE / PASS`.

## Step 13 - Add focused contract tests

- **Objective:** Verify transport and immutable-model semantics directly.
- **Why:** A one-cycle mutable snapshot and an immutable Observation have
  different ownership and validity obligations.
- **Action:** Add tests for defaults, complete refresh, stale-target clearing,
  state distinctions, acquisition order, defensive copying, target identity,
  transform semantics, equality, and the locked API/schema exclusions.
- **Files Changed:** `VisionIOTest.java` and `VisionObservationTest.java` only.
- **Verification:** The tests compile against the public contract and include
  focused boundary checks rather than vendor-specific assumptions.
- **Expected Result:** L03 behavior is independently testable before regressions.
- **Status:** `COMPLETE / PASS`.

## Step 14 - Run initial verification and record the failure historically

- **Objective:** Separate real contract failures from invalid test assumptions.
- **Why:** A failing test is evidence to investigate, not permission to widen
  the production API.
- **Action:** Run the initial focused verification and preserve the one failure
  that expected an effectively zero quaternion norm to be rejected.
- **Files Changed:** Generated test reports only.
- **Verification:** The failure was reproduced at the locked `Transform3d`
  boundary; no production contradiction was established.
- **Expected Result:** The failing expectation is recorded as historical while
  investigation proceeds.
- **Status:** `COMPLETE / HISTORICAL FAILURE`.

## Step 15 - Perform the false-oracle forensic diagnosis

- **Objective:** Determine whether the failure was in production or in the test
  oracle.
- **Why:** `Rotation3d` is constructed before `VisionObservation` receives a
  `Transform3d`; its canonicalization changes what the public boundary can see.
- **Action:** Inspect the actual WPILib behavior and compare the requested raw
  quaternion norm with the values observable from the constructed transform.
- **Files Changed:** None.
- **Verification:** WPILib canonicalization converted the raw zero or
  effectively-zero quaternion construction to a valid identity rotation before
  the Observation boundary. The original norm could not be recovered there.
- **Expected Result:** The defect is classified as a test-fixture/oracle defect,
  not a production contract defect.
- **Status:** `COMPLETE / PASS`.

## Step 16 - Apply the authorized TEST-ORACLE repair

- **Objective:** Make the test assert behavior observable through the locked
  public contract.
- **Why:** The lesson must keep `Transform3d`; adding raw quaternion transport
  solely to satisfy an unobservable test would expand L03 improperly.
- **Action:** Replace the unobservable zero-norm rejection expectation with a
  test that constructs and accepts a valid identity `Rotation3d` at the
  `Transform3d` boundary.
- **Files Changed:** `src/test/java/frc/robot/observation/vision/VisionObservationTest.java` only.
- **Verification:** The repaired test passes. No raw quaternion field/API,
  alternate schema, or production code change was made.
- **Expected Result:** The oracle validates the actual public contract and
  legitimate identity rotation remains valid.
- **Status:** `COMPLETE / TEST-ORACLE REPAIR ONLY`.

## Step 17 - Run focused and inherited verification

- **Objective:** Prove both new L03 behavior and inherited vision behavior.
- **Why:** Focused tests do not protect predecessor geometry or field-layout
  semantics by themselves.
- **Action:** Run `VisionObservationTest`, `VisionIOTest`, inherited
  `VisionFrameTransformTest`, inherited `AprilTagFieldLayoutContractTest`, the
  full suite, and the clean full build under WPILib Java 17.
- **Files Changed:** Generated reports and build output only.
- **Verification:** The User independently supplied PASS for both focused tests,
  both inherited regressions, the full test suite, and the clean full build.
  Codex's authorized rerun also passed these gates.
- **Expected Result:** New and inherited deterministic behavior pass together.
- **Status:** `COMPLETE / PASS`.

## Step 18 - Classify runtime verification surfaces

- **Objective:** Record what L03 does and does not verify physically.
- **Why:** A contract-only lesson must not claim runtime or camera evidence it
  did not produce.
- **Action:** Keep Simulation `NOT APPLICABLE / DEFERRED TO V00_L04`, Driver
  Station / Glass `NOT APPLICABLE`, and physical camera/Real Robot
  `NOT APPLICABLE / DEFERRED TO V00_L08`.
- **Files Changed:** L03 verification documentation only.
- **Verification:** L03 adds no simulation implementation, runtime telemetry,
  camera adapter, deployment, or actuation path.
- **Expected Result:** N/A classifications remain explicit and are not mistaken
  for unperformed PASS claims.
- **Status:** `COMPLETE / PASS`.

## Step 19 - Complete the documentation

- **Objective:** Make the lesson understandable months later without chat
  history.
- **Why:** Lifecycle state, inheritance, architecture, forensic reasoning, and
  verification evidence must agree across documents.
- **Action:** Reconcile root `AGENTS.md`, root `README.md`, L03 `README.md`,
  `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, this guide, and
  the English/Vietnamese learning guides. Preserve activation and initial
  failure as historical records.
- **Files Changed:** Documentation only.
- **Verification:** Cross-document review recorded implementation complete,
  User verification PASS, N/A runtime surfaces, and Git pending. The former
  closure-pending wording is historical pre-closure state.
- **Expected Result:** Documentation is final for the pre-closure review.
- **Status:** `COMPLETE / PASS`.

## Step 20 - Perform the final read-only architecture audit

- **Objective:** Confirm no technical or scope blocker remains before closure.
- **Why:** Documentation completion does not itself freeze a lesson.
- **Action:** Audit governance, inheritance, layering, contract, immutability,
  Transform3d semantics, test-oracle repair, scope containment, and mutual
  documentation consistency. Do not edit implementation during this review.
- **Files Changed:** None during the read-only audit.
- **Verification:** Final audit result: `PASS / READY FOR CHATGPT CLOSURE REVIEW`.
  The source boundary remains the four authorized L03 files; generated output
  remains excluded.
- **Expected Result:** ChatGPT can make the separate closure decision from a
  complete evidence record.
- **Status:** `COMPLETE / PASS`.

## Step 21 - Apply final closure and leave publication to the User

- **Objective:** Finish the lifecycle without confusing lesson completion with
  Git publication.
- **Why:** Closure/freeze is a governance decision, while Git add/commit/push is
  User-owned.
- **Action:** The Architect's final closure review returned `PASS`. The
  authorized documentation metadata now records the lesson as `COMPLETE /
  FROZEN / READ-ONLY`; the User remains responsible for Git add, commit, and
  push.
- **Files Changed:** The nine authorized documentation/status files in this
  closure task. User-owned Git history is not changed by Codex.
- **Verification:** Final closure review and freeze metadata are `PASS`.
  Git publication remains `PENDING / USER OWNED`.
- **Expected Result:** Lesson content/state is frozen and read-only, with no
  active lesson; publication is recorded only after User evidence.
- **Status:** `COMPLETE / PASS`.

## Final student summary

V00_L03 is a contract lesson, not a camera-integration lesson. It inherited the
final V00_L02 and A01 architecture, added exactly one vendor-neutral transport
and immutable Observation boundary, and kept all later vision responsibilities
deferred. The initial quaternion failure was resolved by correcting the test
oracle at the public `Transform3d` boundary. Focused tests, inherited
regressions, full regression, clean build, documentation, and final read-only
architecture audit are PASS. Simulation, Driver Station / Glass, physical
camera, and Real Robot are not applicable to this scope. Closure and freeze are
complete; the only remaining gate is User-owned Git publication.
