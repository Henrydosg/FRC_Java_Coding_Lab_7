# V00_L07 Exceptional Swerve Integrity Repair — Step by Step

## Document state

- **Lesson:** V00_L07 - Vision Timestamp and Latency Contract
- **Record type:** Current exceptional-repair transition guide
- **State:** `FINAL / RE-FROZEN / PUBLICATION PENDING`
- **Implementation:** `COMPLETE / AUTHORIZED R1/R2/R3 BOUNDARY`
- **Authoritative pre-repair publication:** `d58bef0`
- **Current lesson state:** `COMPLETE / FROZEN / READ-ONLY`
- **Scope:** R1/R2/R3 only

This guide explains why V00_L07 was selected as the controlled repair point,
how the repair was derived from the published V00_L07 snapshot, and what
remains before corrected publication. It is a student-facing lifecycle record. The original
V00_L07 timing lesson remains valid; the reopen addressed inherited Swerve
architecture and integrity findings in the descendant snapshot.

The authoritative governance decision is
[`ADR_V00_L07_Inherited_Swerve_Architecture_Robustness_Integrity_Reopen.md`](../../../../docs/architecture_decisions/ADR_V00_L07_Inherited_Swerve_Architecture_Robustness_Integrity_Reopen.md).

## Step 1 — Establish the historical frozen V00_L07 parent

**Objective:** Identify the exact snapshot from which the repair lifecycle
starts.

**Why:** A frozen lesson must not be repaired by guessing which copy is current.
The latest authoritative published V00_L07 snapshot is the reproducible parent.

**Action:** Confirm V00_L07's historical publication identity:

```text
d58bef0 Complete V00_L07 vision timestamp and latency contract
d58bef0d17d202ce1dd0b8645635a8c35095dd3f
```

Treat that snapshot as the pre-repair baseline. Preserve its original vision
timestamp and latency concept, its historical verification record, and its
provenance.

**Files Changed:** None in this step.

**Verification:** Repository lifecycle evidence identifies `d58bef0` as the
published V00_L07 snapshot; the original timing lesson is not identified as the
defect. The fresh reopened baseline recorded 593/593 tests PASS and clean build
PASS before repair work.

**Expected Result:** There is one clearly identified historical parent, and no
older S00 or A01 lesson is reopened.

## Step 2 — Record the Architect and User decision

**Objective:** Establish authority for the exceptional lifecycle before any
repair work.

**Why:** A completed lesson is frozen. Reopening it requires explicit
governance approval and an exact scope.

**Action:** Record that the Architect selected V00_L07 as the narrowest
authoritative repair point and that the User approved an exceptional reopen for
exactly R1, R2, and R3. The preceding governance gates
`AUDIT-REV-03B`, `S00-REPAIR-GATE-01`, and `V00-L07-REOPEN-GOV-01` are preserved
as provenance. The later Design Lock and separate implementation authorization
approved the exact repair boundary; those results are recorded below. Record
the governance decision in the reopen ADR.

**Files Changed:**

- `docs/architecture_decisions/ADR_V00_L07_Inherited_Swerve_Architecture_Robustness_Integrity_Reopen.md`

**Verification:** The ADR preserves the historical governance-only decision,
and the later records preserve the separate implementation authorization.

**Expected Result:** Governance approval and implementation authorization remain
distinct. No configuration, asset, or Git operation is authorized by this
guide.

## Step 3 — Perform the controlled lifecycle transition

**Objective:** Make the repository's current lifecycle metadata agree with the
approved exceptional reopen.

**Why:** The old `COMPLETE / FROZEN / READ-ONLY` metadata described the
pre-repair snapshot. Future work cannot be safely controlled while the current
lesson state is ambiguous.

**Action:** The historical reopen transition changed the current V00_L07 state
to:

```text
Status: IN_PROGRESS
Active State: REOPENED / IN_PROGRESS / EDITABLE
Active lesson count: 1
Design Lock: PASS / R1/R2/R3 REPAIR DESIGN LOCK
Implementation authorization: PASS / EXACT AUTHORIZED REPAIR BOUNDARY
Implementation: PASS / R1/R2/R3 COMPLETE
Verification: PASS / AUTOMATED REPAIR GATES
Simulation: PASS
Real Robot: DEFERRED — ROBOT UNAVAILABLE
Re-freeze: BLOCKED / BOUNDED HARDWARE GATE
Repair publication: NOT AUTHORIZED BEFORE RE-FREEZE
```

Keep the original `d58bef0` publication, original test/build results, and prior
closure records explicitly marked as historical. Update repository governance
and lesson-local metadata only. The current reconciliation records the later
implementation and verification evidence. The later final closure review and
explicit re-freeze authorization return the repaired lesson to frozen state.

**Files Changed:**

- `AGENTS.md`
- `README.md`
- `LESSON_STATUS.md`
- `LESSON_PLAN.md`
- `LESSON_CHECKLIST.md`
- `real_robot_programming/module_V00/V00_L07_VisionTimestampAndLatencyContract/README.md`
- this transition guide

**Verification:** Exactly one lesson is current and editable: V00_L07. V00_L08
remains unactivated, non-authoritative, read-only, and not started.

**Expected Result:** Lifecycle metadata is consistent, while no production or
test implementation is changed by this documentation reconciliation.

## Step 4 — Define the exact inherited repair objective

**Objective:** State the three defects precisely enough for later design review
without inventing implementation mechanics.

**Why:** A controlled repair must not expand into a general Swerve cleanup or a
new vision feature.

**Action:** Preserve exactly these repair objectives and their completed
semantics:

1. **R1 — Type ownership:** remove the command-layer dependency on the
   IO-owned `SwerveModuleIO.StaticFrictionStopReason` type.
2. **R2 — Stop robustness:** make `SwerveSubsystem` all-module stop fanout
   best-effort so one throwing module stop does not prevent attempts on the
   remaining modules.
3. **R3 — Physical-forward integrity:** make measured drive position and
   measured drive velocity coherent when `physicalForwardSign = -1`.

The authoritative drive ratio remains `6.75:1`.

The authorized implementation moved command-facing
`StaticFrictionStopReason` ownership to `SwerveSubsystem`, privately
translating to the unchanged IO reason. The characterization command no longer
depends on `frc.robot.io`; the IO contract and commissioning lifecycle
semantics remain equivalent. It made `SwerveSubsystem.stop()` clear
actuation state before attempting FL → FR → BL → BR, preserving and
suppressing `RuntimeException`s in that encounter order, and rethrowing the
first after all attempts; and applied the same physical-forward sign to
measured drive position and velocity at the subsystem conversion boundary while
leaving IO/Observation values raw; `SwerveSubsystem` owns that physical-forward
normalization.
CTRE and Sim production IO remain unchanged. CTRE drive/steer module-local
exception isolation was not part of this repair.

**Files Changed:** The authorized implementation boundary consisted of
`SwerveFrontLeftDriveStaticFrictionCharacterizationCommand.java` and
`SwerveSubsystem.java`; the authorized focused tests covered the command,
subsystem, measured position, measured speed, and Sim raw-sign behavior.

**Verification:** The three objectives match the approved audit evidence and
the reopen ADR; no additional defect is added to the repair list.

**Expected Result:** A student can distinguish the completed inherited Swerve
repair work from the original V00_L07 timing concept.

## Step 5 — Preserve the exclusions and frozen architecture

**Objective:** Prevent scope drift while the lesson is temporarily editable.

**Why:** Reopening a lesson does not unfreeze the repository architecture.

**Action:** Keep the Frozen Backbone, Frozen Interface Contract, observation
flow, RobotContainer composition-root role, telemetry read-only rule, and V00
roadmap unchanged. Do not modify or redesign live module-health policy,
transient CAN policy, `setControl` status handling, nonfinite/general IO
cleanup, telemetry, tuning, calibration, CAN IDs, ratios, wheel diameter,
Driver Input, field-relative behavior, odometry, pose estimation, autonomous,
PathPlanner, vision, Limelight, AprilTag, pose fusion, or unrelated code.

**Files Changed:** None by this transition step.

**Verification:** A later changed-file audit must show no edits outside the
approved documentation now and the separately authorized minimum repair
boundary later.

**Expected Result:** The reopen is a narrow integrity correction, not a second
implementation of V00_L07 or a redesign of the robot.

## Step 6 — Record the completed repair and re-freeze lifecycle

**Objective:** Record the gated order that was followed and identify the
remaining User-owned publication gate.

**Why:** Each transition must have an independently verifiable gate, old PASS
results cannot substitute for fresh evidence after reopening, and functional
hardware usability must not be overstated as quantitative drivetrain proof.

**Action:** The lifecycle proceeded in this order:

1. User established a fresh Java 17 inherited baseline: 593/593 tests PASS.
2. Codex completed the focused read-only Architecture Audit.
3. The Architect approved the exact R1/R2/R3 Design Lock, including ownership,
   exception reporting, and physical-forward semantics.
4. Architect and User separately authorized the exact minimum production and
   test files.
5. Only the authorized repair and tests were implemented.
6. Focused tests, inherited regressions, the 600/600 full suite, and the clean
   build passed with 0 failures, 0 errors, and 0 skipped tests.
7. User-supplied runtime WPILib Simulation passed. At that historical stage,
   real-robot verification was deferred because the robot was unavailable.
8. The post-implementation read-only Architecture Review and Frozen Backbone
   review passed.
9. The User later verified Teleop and Autonomous usability. The BL quantitative
   drivetrain anomaly remains `KNOWN / DEFERRED HARDWARE MAINTENANCE`; no
   quantitative drivetrain PASS or issue resolution is claimed.
10. The pre-closure documentation reconciliation, final read-only closure
    review, and explicit Architect/User re-freeze authorization are complete.
11. User-owned corrected repair publication remains pending.

**Files Changed:** The implementation and test files were changed only under
the separate explicit authorization. This guide records their result; Git
publication remains User-owned and pending.

**Verification:** Items 1 through 10 are complete for the reopened cycle. Item
11 remains pending User publication. The
593-test baseline is pre-repair evidence for this cycle, while the 600-test
result and clean build are post-repair evidence. Teleop and Autonomous usability
are User-verified. This does not establish quantitative drivetrain PASS.

**Expected Result:** V00_L07 is `COMPLETE / FROZEN / READ-ONLY`; corrected
publication remains pending User commit and push.

## Step 7 — Re-freeze and publish the corrected lesson

**Objective:** Define the conditions for returning V00_L07 to a trustworthy
frozen state.

**Why:** Implementation completion alone does not close an exceptional reopen.

**Action:** The final read-only closure review passed and explicit
Architect/User re-freeze authorization was recorded. The User now owns the
add/commit/push operation and supplies the new repair publication identity. Do
not claim the new publication before the User confirms it.

**Files Changed:** Final lifecycle metadata only; Git publication is
User-owned.

**Verification:** Current result is `COMPLETE / FROZEN / READ-ONLY`; no repaired
publication identity has been created or claimed.

**Expected Result:** V00_L07 remains `COMPLETE / FROZEN / READ-ONLY` while the
corrected repair publication remains pending User commit and push.

### Current hardware evidence and deferred maintenance

Real-robot verification was **DEFERRED — ROBOT UNAVAILABLE** at the earlier
post-repair stage. That statement remains historical. The User later verified
Teleop and Autonomous usability. This guide does not add a stronger bounded
stop/Disable/no-unintended-restart claim beyond the supplied evidence.

The BL quantitative drivetrain anomaly remains **KNOWN / DEFERRED HARDWARE
MAINTENANCE**. It is unresolved, is not BL PASS or quantitative drivetrain
PASS, and does not establish matched module measurements, tuning completion, or
calibration completion. Under the explicit Architect/User disposition, it does
not block the Vision curriculum closure sequence. Do not change
`physicalForwardSign` merely to force a `-1` case.

This repair excludes tuning, recalibration, autonomous redesign, vision
validation, Limelight validation, and PathPlanner validation.

## Step 8 — Reconstruct V00_L08 only from the corrected parent

**Objective:** Protect downstream inheritance from the old, unrepaired copy.

**Why:** V00_L08 owns real vision-adapter integration and must inherit the
corrected V00_L07 architecture, not silently preserve the pre-repair snapshot.

**Action:** After corrected V00_L07 re-freeze and User publication, replace or
reconstruct the current unactivated V00_L08 candidate through the normal
copy/rename, generated-artifact cleanup, baseline-build, transition-guide,
Architecture Audit, and Design-Lock workflow. Keep V00_L08 unactivated until
that separate process is authorized. The independent Limelight physical-
evidence HOLD remains in force; it is not resolved by the V00_L07 repair.

**Files Changed:** No V00_L08 file is changed by the current transition.

**Verification:** Current result is `PENDING`; V00_L08 remains
`NOT STARTED / NOT ACTIVATED / NOT IMPLEMENTED / NOT PUBLISHED / NON-AUTHORITATIVE / READ-ONLY`.

**Expected Result:** The next active lesson, if separately authorized, starts
from the corrected V00_L07 parent and preserves the V00 roadmap and lesson
identities.

## Current closure statement

This guide records the final re-frozen documentation state. The original
V00_L07 timing lesson is not invalidated. R1/R2/R3 implementation, automated
verification, Simulation, later User functional hardware evidence, and the
deferred BL maintenance condition are reconciled. No Git operation has been
performed. The current lesson has passed closure and is re-frozen:

```text
V00_L07: COMPLETE / FROZEN / READ-ONLY
R1/R2/R3 implementation: COMPLETE / AUTHORIZED BOUNDARY
AUTOMATED VERIFICATION: PASS / 600 TESTS / 0 FAILURES / 0 ERRORS / 0 SKIPPED
SIMULATION: PASS
POST-IMPLEMENTATION ARCHITECTURE REVIEW: PASS
REAL ROBOT: USER VERIFIED / TELEOP AND AUTONOMOUS USABILITY
BL QUANTITATIVE ANOMALY: KNOWN / DEFERRED HARDWARE MAINTENANCE
QUANTITATIVE DRIVETRAIN PASS: NOT CLAIMED
FINAL READ-ONLY CLOSURE REVIEW: PASS
RE-FREEZE: PASS / EXPLICIT ARCHITECT/USER AUTHORIZATION
Repair publication: PENDING USER PUBLICATION
```
