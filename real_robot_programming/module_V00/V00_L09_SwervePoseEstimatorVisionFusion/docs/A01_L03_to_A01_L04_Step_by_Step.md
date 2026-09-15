# A01_L03 to A01_L04 - Step-by-Step Transition Guide

## Guide State

- Previous lesson: A01_L03_TrajectoryGenerationAndSamplingFundamentals - COMPLETE / FROZEN / READ-ONLY
- Current lesson: A01_L04_FieldAndAllianceTransformContract - COMPLETE / FROZEN / READ-ONLY
- Guide state: FINAL / PASS
- Git commit and push: user-owned; NOT TESTED

## Step 1 - Inherit Frozen L03

- Objective: begin L04 from the immediate frozen trajectory-generation predecessor.
- Why: keep the established field-heading, readiness, estimator, stop, and
  trajectory sampling contracts unchanged.
- Action: copy L03 and preserve L03 as read-only.
- Files Changed: inherited L04 project only.
- Verification: user-supplied source comparison found the inherited L04 `src`
  tree byte-identical to L03; inherited baseline build reported BUILD SUCCESSFUL.
- Expected Result: any future delta is attributable solely to L04.

## Step 2 - Verify Identity and Architecture Scope

- Objective: confirm the authorized L04 identity and isolate its one concept.
- Why: the ADR places field/alliance transforms after sampling and before
  trajectory following.
- Action: verify `A01_L04_FieldAndAllianceTransformContract`; complete the
  Architecture Audit.
- Files Changed: none.
- Verification: identity PASS; Architecture Audit PASS.
- Expected Result: L04 owns canonical path-frame and alliance-transform
  definition, not following or motion.

## Step 3 - Verify and Resolve the Design Lock

- Objective: lock both official 2026 field variants without assuming one field.
- Why: Red transformation requires the selected field dimensions.
- Action: verify WPILib coordinate semantics; retain explicit
  `REBUILT_WELDED` and `REBUILT_ANDYMARK` variants.
- Files Changed: none.
- Verification: Design-Lock Verification PASS; Design-Lock HOLD Resolution PASS.
- Expected Result: Blue is identity; Red is one 180-degree field-centre
  rotation; unknown alliance has no implicit fallback.

## Step 4 - Activate L04

- Objective: establish the controlled editable lesson before implementation.
- Why: governance allows implementation changes only within one active lesson.
- Action: normalize L04 governance documentation and repository active-lesson
  status while preserving L03.
- Files Changed: repository README; L04 README, LESSON_STATUS, LESSON_PLAN,
  LESSON_CHECKLIST, and this guide.
- Verification: pending repository-wide single-active-lesson check.
- Expected Result: L04 is IN_PROGRESS / NOT FROZEN; all predecessor lessons
  remain frozen.

## Step 5 - Implement the Pure Transform Contract

- Objective: add the locked FieldVariant and pure transform utility.
- Why: make canonical Blue data and its one Red transform explicit before a
  follower exists.
- Action: add explicit official-2026 field variants; add the pure
  `FieldAllianceTransform`; and add deterministic transform tests.
- Files Changed: `Constants.java`; `util/FieldAllianceTransform.java`; and
  `util/FieldAllianceTransformTest.java` only.
- Verification: independent user verification supplied PASS for compileJava,
  compileTestJava, FieldAllianceTransformTest, LearningTrajectoryFactoryTest,
  full regression, and clean build. Non-actuating both-alliance Simulation
  remains pending.
- Expected Result: canonical Blue data can be transformed exactly once with an
  explicit field variant and definite alliance, without a drivetrain request,
  scheduler behavior, or autonomous motion.

## Step 6 - Complete Required Learning Documentation

- Objective: preserve the field-frame, transform mathematics, and ownership
  decisions for later L05 through L07 work.
- Why: future vendor integration must not reapply the transform.
- Action: create all eight L04 detailed learning documents covering the L01-L05
  learning map, WPILib coordinates, official field variants, transform
  mathematics, trajectory semantics, transform ownership, unknown-alliance
  safety, and L03-L05 data flow.
- Files Changed: the eight required learning documents in `docs/`, plus this
  transition guide and active L04 governance documentation.
- Verification: all eight documents exist and agree with the locked source and
  architecture. Independent user-supplied Java/build gates passed.
- Expected Result: L04 has durable learning documentation before freeze.

## Step 7 - Verify Without Actuation

- Objective: verify the pure transform contract and its no-motion boundary.
- Why: L04 establishes field/alliance data correctness, not drivetrain motion.
- Action: run Java compilation, focused transform and L03 regression tests,
  full regression, clean build, and non-actuating both-alliance Simulation.
- Files Changed: none.
- Verification: independent user supplied PASS for compileJava,
  compileTestJava, FieldAllianceTransformTest, LearningTrajectoryFactoryTest,
  full regression, clean build, and the non-actuating Simulation gate.
- Expected Result: frame and transform behavior is verified without a
  follower, `ChassisSpeeds`, or drivetrain request.

## Step 8 - Finalize and Freeze L04

- Objective: preserve the completed field/alliance contract as the L05 baseline.
- Why: the next lesson must inherit an exact read-only snapshot.
- Action: finalize L04 status, plan, checklist, README, and this guide after
  final architecture and documentation review.
- Files Changed: repository README; L04 README, status, plan, checklist, and
  this transition guide.
- Verification: final audit confirms the exact three-file implementation delta,
  all eight learning documents, supplied Java/build/Simulation PASS evidence,
  and preserved frozen boundaries.
- Expected Result: L04 is COMPLETE / FROZEN / READ-ONLY. Real Robot remains
  HOLD because L04 authorizes no actuation; Git commit and push remain
  user-owned.
