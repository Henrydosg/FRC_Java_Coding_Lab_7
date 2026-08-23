# ADR: V00 AprilTag Vision Observation and Pose Fusion Roadmap

- Status: APPROVED
- Date: 2026-08-23
- Scope: Post-A01 vision-observation, AprilTag pose-estimation, and Swerve pose-fusion roadmap
- Authority: Approved successor ADR to
  `ADR_A01_Autonomous_Navigation_Path_Following_Roadmap.md`. The repository
  authority order and Frozen Backbone remain unchanged.

## Context

`S00_L24_PoseEstimationAndAutonomousReadiness` and the complete A01 roadmap
through `A01_L09_PathPlannerNamedCommandsAndEventMarkers` are complete, frozen,
and read-only. A01 ends at L09. `A01_L10` is not authorized.

The frozen A01_L09 baseline provides:

- Swerve module and gyro IO boundaries;
- deterministic odometry and subsystem-owned `SwerveDrivePoseEstimator`;
- defensive `getEstimatedPose()` access;
- Disabled-only known-field-pose reset;
- immutable observation and read-only telemetry patterns;
- deterministic simulation boundaries;
- field-relative autonomous and PathPlanner/AutoBuilder execution using
  EstimatedPose; and
- A01_L04 ownership of the sole alliance transform.

The baseline does not provide a VisionIO contract, AprilTag camera
observations, vision-derived robot poses, measurement-quality evaluation,
timestamp/latency handling, a real camera adapter, or vision measurement
fusion. The A01 ADR explicitly defers those subjects to a separately governed
scope.

## Decision

Authorize the successor module:

`V00 - AprilTag Vision Observation and Pose Fusion`

Authorize `real_robot_programming/module_V00/` as the future independent module
location. This ADR and its governance registration authorize the roadmap and
location only. They do not create `module_V00`, activate V00_L01, select a
camera vendor, or authorize production implementation.

V00_L01 shall inherit directly from the frozen
`A01_L09_PathPlannerNamedCommandsAndEventMarkers` project through the standard
workflow:

```text
copy frozen A01_L09
-> rename to the approved V00_L01 identity
-> remove generated build/ and .gradle/ artifacts from the copy only
-> run the inherited baseline build
-> create and maintain the transition guide
-> add one approved concept
```

Each later V00 lesson shall inherit only from the immediately preceding V00
lesson after that predecessor is `COMPLETE / FROZEN / READ-ONLY`.

## Authorized Lesson Sequence

The exact locked roadmap is:

1. `V00_L01 - Vision Coordinate Frames and Camera Extrinsics`
   - Directory identity: `V00_L01_VisionCoordinateFramesAndCameraExtrinsics`
   - One concept: canonical field, robot, camera, and AprilTag transform ownership.
2. `V00_L02 - AprilTag Field Layout Contract`
   - Directory identity: `V00_L02_AprilTagFieldLayoutContract`
   - One concept: authoritative AprilTag identities and poses in the canonical field frame.
3. `V00_L03 - Vision IO and Immutable Observation Contract`
   - Directory identity: `V00_L03_VisionIOAndImmutableObservationContract`
   - One concept: vendor-neutral one-cycle vision acquisition and immutable observation boundary.
4. `V00_L04 - Deterministic Vision Simulation`
   - Directory identity: `V00_L04_DeterministicVisionSimulation`
   - One concept: deterministic simulation implementation of the VisionIO contract.
5. `V00_L05 - AprilTag Robot Pose Estimation`
   - Directory identity: `V00_L05_AprilTagRobotPoseEstimation`
   - One concept: derive a canonical field-relative robot-pose candidate from approved inputs.
6. `V00_L06 - Vision Measurement Quality Contract`
   - Directory identity: `V00_L06_VisionMeasurementQualityContract`
   - One concept: deterministic acceptance/rejection and uncertainty classification.
7. `V00_L07 - Vision Timestamp and Latency Contract`
   - Directory identity: `V00_L07_VisionTimestampAndLatencyContract`
   - One concept: canonical capture-time, latency, freshness, ordering, and duplicate semantics.
8. `V00_L08 - Real Vision Adapter Integration`
   - Directory identity: `V00_L08_RealVisionAdapterIntegration`
   - One concept: adapt one approved real camera implementation into the frozen VisionIO contract.
9. `V00_L09 - Swerve Pose Estimator Vision Fusion`
   - Directory identity: `V00_L09_SwervePoseEstimatorVisionFusion`
   - One concept: fuse accepted timestamped vision measurements into the subsystem-owned estimator.

Lessons shall not be reordered, renamed, merged, split, inserted, or skipped
without the architecture/governance approval required by this ADR. One lesson
remains one new primary concept.

## Frozen Inheritance Boundaries

Every V00 lesson shall preserve:

- all frozen S00 projects;
- all frozen A00 and A01 projects;
- the Frozen Backbone and dependency direction;
- the Frozen Interface Contract;
- the mechanism observation flow;
- `RobotContainer` as a concise composition root only;
- read-only telemetry behavior;
- `SwerveSubsystem` ownership of drivetrain behavior, localization, actuation,
  requirements, and centralized `stop()`;
- A00_L04 Autonomous+Enabled fail-closed behavior;
- no automatic restart after mode loss;
- existing PathPlanner/AutoBuilder ownership; and
- A01_L04 as the sole alliance-transform owner.

No V00 lesson may modify a frozen S00, A00, or A01 project. All V00 changes
belong only in the independently inherited V00 lesson projects after the
applicable lesson is activated.

## Vision IO and Vendor Boundary

Vision shall follow the Frozen Interface Contract:

```text
camera / vendor library
-> real or simulation VisionIO implementation
-> VisionIOInputs
-> subsystem or dedicated estimator
-> immutable Vision Observation
-> read-only telemetry
```

- VisionIO and VisionIOInputs remain vendor-neutral.
- Vision observations, value models, and evaluators remain immutable and
  vendor-neutral.
- Vendor APIs and vendor result types may exist only inside the selected real
  VisionIO adapter.
- Subsystems, commands, autonomous code, observations, evaluators, and
  telemetry shall not import camera-vendor APIs.
- Vision IO shall not publish NetworkTables, schedule commands, or own
  mechanism behavior.
- Pure evaluators shall use only explicit inputs and shall not read clocks,
  DriverStation, hardware, NetworkTables, or hidden mutable state.
- Telemetry consumes immutable Observations only and never accepts, rejects,
  transforms, or fuses a measurement.

## Pose-Estimator and Fusion Ownership

`SwerveSubsystem` remains the sole owner of `SwerveDrivePoseEstimator` and its
localization state. Vision supplies accepted, immutable, timestamped
measurements only.

The approved V00_L09 fusion boundary may apply an accepted measurement through
`SwerveDrivePoseEstimator.addVisionMeasurement(...)`. This is measurement
fusion, not a pose reset. Vision shall never continuously call
`resetKnownFieldPose(...)`, replace the estimator, directly set the robot pose,
or mutate localization state outside the Swerve-owned fusion boundary.

Duplicate, stale, future, out-of-order, invalid, nonfinite, and rejected
measurements shall not be fused. Measurement time, latency, validity,
uncertainty, acceptance/rejection reason, and coordinate frame must be explicit
and deterministically testable.

## Autonomous and Alliance Contracts

Autonomous ownership remains unchanged:

```text
accepted vision measurement
-> SwerveSubsystem / SwerveDrivePoseEstimator
-> getEstimatedPose()
-> existing autonomous and AutoBuilder consumers
```

Autonomous commands and adapters shall not directly access a camera, VisionIO,
VisionIOInputs, raw target observation, or vendor API.

A01_L04 remains the sole alliance-transform owner. Vision-derived poses and
measurements use the canonical WPILib field coordinate system and are never
alliance-flipped. Vision shall not introduce a second transform in IO,
observation, Swerve, telemetry, PathPlanner, or AutoBuilder.

## Simulation Ground-Truth Contract

Deterministic Vision simulation shall use a separate, explicit simulation
ground-truth pose source. It shall not use `getEstimatedPose()`, an
EstimatedPose Observation, AutoBuilder pose feedback, or a vision-corrected
estimate as camera ground truth. Doing so would create circular evidence and
invalidate the simulation gate.

Simulation shall cover visible and unavailable targets, finite transforms,
connection state, delay, stale/duplicate/out-of-order measurements, accepted
and rejected quality cases, estimator correction, and failure recovery as the
applicable lessons introduce those concepts.

## Vendor-Selection Gate

No camera or vendor implementation is selected in V00_L01 through V00_L07.
Those lessons must remain implementation-neutral.

V00_L08 may select exactly one real vision implementation only after an
explicit compatibility review confirms:

- the actual camera hardware;
- compatibility with the repository's WPILib 2026 baseline;
- the exact vendor library and version;
- camera and robot timestamp semantics, conversion, and synchronization;
- dependency-resolution and build feasibility; and
- simulation support where applicable.

No vendor shall be guessed, selected from familiarity alone, or introduced
before this gate passes. Failure to establish compatibility places V00_L08 on
HOLD and requires architecture/governance review before proceeding.

## Verification and Real-Robot Gates

Each V00 lesson must pass the normal inheritance and verification workflow:

1. frozen-boundary and one-concept architecture review;
2. inherited baseline build;
3. focused deterministic tests;
4. full regression and clean build;
5. required Simulation verification;
6. Driver Station / Glass / telemetry verification when runtime observations
   are introduced;
7. explicit Real Robot status; and
8. final documentation and transition-guide audit.

Real-robot verification remains user-owned. V00_L08 real-camera verification
begins Disabled and read-only, proving camera health, target observations,
transforms, timestamps, latency, quality classification, and disconnect
recovery without estimator fusion.

V00_L09 fusion verification requires Simulation PASS before real-robot fusion.
Real-robot fusion shall progress from stationary observation to controlled
translation/rotation, invalid-target rejection, disconnect recovery, and only
then low-speed bounded autonomous verification with immediate Disable/E-stop
readiness.

No lesson may claim final endpoint accuracy, final covariance or rejection
thresholds, final PID/feedforward tuning, final physical characterization, or
competition readiness without explicit evidence supplied by the User.

## Explicit Exclusions

This ADR does not authorize:

- creation or activation of V00_L01 by this governance task;
- any camera/vendor selection before the V00_L08 compatibility gate;
- continuous pose reset from camera measurements;
- direct autonomous dependency on vision or vendor APIs;
- changes to the Frozen Backbone, package responsibilities, or dependency
  direction;
- changes to frozen S00, A00, or A01 projects;
- changes to PathPlanner, AutoBuilder, chooser, NamedCommands, or event-marker
  ownership;
- dynamic replanning, obstacle avoidance, game-specific strategy, or new
  mechanism architecture; or
- unsupported tuning, accuracy, characterization, or competition-readiness
  claims.

## Conditions Requiring an ADR Amendment or New ADR

Formal approval is required before:

- changing the V00 module boundary or lesson order;
- adding, removing, renaming, merging, splitting, inserting, or skipping a V00
  lesson;
- changing Swerve pose-estimator ownership;
- moving vendor APIs outside the real VisionIO adapter;
- changing the canonical field-frame or alliance-transform ownership;
- using EstimatedPose as simulation camera ground truth;
- selecting more than one real vision implementation in V00_L08;
- changing the Frozen Backbone, Frozen Interface Contract, observation flow,
  RobotContainer responsibility, or real-robot verification ownership; or
- adding advanced localization, dynamic replanning, obstacle avoidance, or
  game-specific vision strategy.

## Consequences

- A01 remains closed and frozen at A01_L09.
- A01_L10 remains unauthorized.
- V00 becomes the approved successor roadmap after A01.
- `module_V00` and V00_L01 remain not created and not started until a separate
  activation decision authorizes the normal inheritance workflow.
- Camera/vendor selection remains deferred to V00_L08 and its compatibility
  gate.
- The Frozen Backbone, observation architecture, Swerve estimator ownership,
  autonomous ownership, and alliance-transform ownership remain unchanged.

## Review Result

This ADR is `APPROVED / FROZEN` as the V00 roadmap authority. It authorizes the
successor roadmap and future repository location only. Module creation, lesson
activation, project copying, source implementation, tests, builds, Simulation,
real-robot verification, and Git/GitHub operations remain separate workflow
steps.
