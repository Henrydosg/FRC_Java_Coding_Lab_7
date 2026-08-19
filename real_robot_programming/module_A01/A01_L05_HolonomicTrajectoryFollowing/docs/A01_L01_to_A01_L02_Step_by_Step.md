# A01_L01 to A01_L02 - Step-by-Step Transition Guide

## Guide Status

- Previous lesson: `A01_L01_AutonomousStartingPoseAndFieldFrameContract` - `COMPLETE / FROZEN / READ-ONLY`
- Current lesson: `A01_L02_PoseTargetedAutonomousMotion` - `COMPLETE / FROZEN / READ-ONLY`
- Guide state: `FINAL / PASS`
- Git commit and push: user-owned; `NOT TESTED`

## Step 1 - Inherit the Frozen Baseline

- Objective: start L02 from the immediate frozen L01 predecessor.
- Why: preserve the validated localization, field-reference, readiness, IO,
  telemetry, and centralized-stop contracts.
- Action: retain A01_L01 unchanged and use its project as the L02 baseline.
- Files Changed: no A01_L01 files.
- Verification: pre-activation source/test comparison found the inherited L02
  Java/test tree byte-identical to A01_L01.
- Expected Result: L02 has one controlled place to add its one new concept.

## Step 2 - Lock the Minimum Design

- Objective: define one pose-target command without entering L03+ scope.
- Why: pose error, tolerance, timeout, and safe stop must be independently
  understandable before trajectories or vendor path tooling.
- Action: lock `getEstimatedPose()` feedback, one finite field target,
  field-frame X/Y/wrapped-heading error, bounded P control, per-cycle
  suppression, simultaneous tolerance completion, timeout, and fail-closed
  stop.
- Files Changed: documentation only.
- Verification: Architecture Audit and Design Lock passed.
- Expected Result: no subsystem, IO, telemetry, hardware, or frozen-interface
  redesign is needed.

## Step 3 - Implement the Command and Composition Delta

- Objective: add closed-loop pose-target autonomous motion.
- Why: provide the smallest controllable autonomous primitive after the L01
  starting-pose contract.
- Action: add `PoseTargetedAutonomousMotionCommand`; add conservative L02
  constants; replace only the inherited bounded autonomous-motion payload in
  `RobotContainer`; add focused command tests and extend scheduler/simulation
  tests.
- Files Changed: `Constants.java`, `RobotContainer.java`, the new command,
  and the three L02 test files.
- Verification: 10/10 focused command tests, 17/17 scheduler tests, 2/2
  simulation integration tests, 373/373 full regression tests, and a clean
  build passed in the user's WPILib Java 17 environment.
- Expected Result: one scheduler-owned bounded pose-target session followed by
  the inherited repeating safety hold.

## Step 4 - Verify the Disabled Preparation Workflow

- Objective: establish the correct field frame and one-shot starting-pose
  authorization before motion.
- Why: autonomous motion must not begin from an unknown or stale pose.
- Action: while Disabled, capture field heading with Xbox Back/View, wait for
  valid pose/estimate telemetry, then invoke `Reset Known Starting Pose`.
- Files Changed: none.
- Verification: Simulation and real-robot evidence confirmed valid pose and
  estimated pose, measurement validity, accepted reset, and no motion without
  a fresh accepted reset.
- Expected Result: one accepted reset authorizes exactly one autonomous
  session.

## Step 5 - Verify Motion, Tolerance, and Mode-Loss Safety

- Objective: demonstrate bounded motion and all L02 terminal safety behavior.
- Why: the target controller must stop safely under normal and interrupted
  operation.
- Action: run the `(0.40 m,0,0 deg)` target in Simulation and on the real
  robot; interrupt one run by disabling; re-enable without reset; then perform
  a fresh reset and repeat.
- Files Changed: none.
- Verification: normal runs converged near X=`0.370 m`; Y and heading stayed
  approximately zero; disable stopped immediately near X=`0.204 m`; no reset
  meant no motion; a fresh reset permitted one new repeatable session.
- Expected Result: X=`0.370 m` is accepted because the remaining 0.030 m
  translation error equals the locked tolerance.

## Step 6 - Finalize Documentation and Freeze L02

- Objective: record verified scope evidence without claiming Git actions.
- Why: a completed lesson must have a final transition guide and accurate
  status before freezing.
- Action: update L02 status, plan, checklist, README, and verification guide;
  preserve all L01 files as read-only.
- Files Changed: L02 documentation and the repository roadmap state only.
- Verification: all non-Git implementation, build, test, Simulation, Driver
  Station / Glass, and real-robot evidence is recorded as PASS.
- Expected Result: L02 is `COMPLETE / FROZEN / READ-ONLY`; Git commit and push
  remain `NOT TESTED` and user-owned.

## Scope Preserved for Later Lessons

L02 does not add trajectory generation/sampling/following, PathPlanner,
AutoBuilder, alliance transforms, vision/AprilTags, multi-waypoint logic,
mechanism events, drivetrain retuning, hardware changes, or new localization
architecture. Those concepts remain deferred to separately authorized L03+
lessons.
