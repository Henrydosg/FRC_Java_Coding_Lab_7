# V00_L04 - Deterministic Vision Simulation

## Current lesson state

- **Directory:** `V00_L04_DeterministicVisionSimulation`
- **Authoritative predecessor:** `V00_L03_VisionIOAndImmutableObservationContract @ cc20d62`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`
- **Status:** `COMPLETE`
- **Active state:** `COMPLETE / FROZEN / READ-ONLY`
- **Active lesson count:** `0`
- **Design Lock:** `APPROVED BY CHATGPT ARCHITECT`
- **Implementation:** `IMPLEMENTED / VERIFIED`
- **Automated verification:** `PASS / USER VERIFIED / WPILib Java 17`
- **Post-implementation architecture review:** `PASS`
- **Artifact cleanup:** `PASS / USER REPORTED`
- **Documentation:** `COMPLETE / PASS`
- **Closure:** `AUTHORIZED / PASS`
- **Freeze:** `COMPLETE / FROZEN / READ-ONLY`
- **Git publication:** `PUBLISHED @ 5461555 / USER VERIFIED`

V00_L04 is no longer active or editable. Its lesson content and lifecycle state
are complete, frozen, and read-only. User-owned Git publication is confirmed
at `5461555`.

## What L04 adds

L04 adds exactly one concept: a deterministic, vendor-neutral
`VisionIOSim` implementation of the frozen `VisionIO` contract.

For a target-present frame, the simulator combines known robot ground truth,
the fixed camera mounting extrinsic, and official field-to-tag geometry:

```text
V00_L02 official fieldToTag geometry
                +
known fieldToRobot ground truth
                +
fixed robotToCamera
                |
                v
fieldToCamera = fieldToRobot.transformBy(robotToCamera)
                |
                v
cameraToTarget = Transform3d(fieldToCamera, fieldToTag)
                |
                v
VisionIOInputs
```

This is forward measurement synthesis. L04 starts with a known robot field
pose and generates what the camera would observe. It does not estimate robot
pose from camera measurements. V00_L05 will later consume vision measurements
in the opposite conceptual direction to create robot-pose candidates; that
work is deferred and is not implemented here.

## Deterministic state contract

`VisionIOSim` provides five explicit frame factories: `unavailable()`,
`disconnected()`, `invalidSample()`, `noTargets()`, and
`targetsPresent(Pose3d, List<Integer>)`.

The initial state is unavailable. Progression occurs only when the caller uses
`setFrame(...)`; there is no automatic sequence, clock, randomness, thread,
Driver Station dependency, NetworkTables dependency, vendor API, or hidden
runtime dependency.

Every `updateInputs(...)` call overwrites `available`, `connected`,
`sampleValid`, and `targets` as one complete cycle. This prevents targets from
a previous frame remaining visible after a no-target, invalid, disconnected,
or unavailable transition.

Validation is fail-atomic: null or non-finite geometry, nonpositive or
duplicate tag IDs, and tag IDs unknown to the selected official field are
rejected before the current valid frame is replaced. Caller collections and
geometry are defensively owned, and target order is preserved.

## Inherited architecture preserved

- Frozen Backbone: `PASS / PRESERVED`.
- Frozen Interface Contract: `PASS / PRESERVED`.
- Document C Observation architecture: `PASS / PRESERVED`.
- `VisionIO`, `VisionIOInputs`, `VisionObservation`,
  `VisionFrameTransform`, and `AprilTagFieldLayoutContract`: `UNCHANGED`.
- RobotContainer, Swerve, autonomous, PathPlanner, telemetry, Gradle,
  vendordeps, configuration, resources, and deploy assets: `UNCHANGED`.
- V00_L01, V00_L02, and V00_L03: `PROTECTED / PUBLISHED / UNCHANGED`.

The post-implementation read-only review confirmed the API, state mappings,
geometry direction, independent numeric oracle, determinism, validation,
atomicity, test quality, and V00_L05-L09 scope isolation. The independent
geometry expectation of `-2.5 m` is correct for a robot located `2.0 m` beyond
an aligned tag with the camera another `0.5 m` robot-forward.

## Authoritative verification evidence

The User reran the repository-standard WPILib Java 17 workflow and supplied:

- `compileTestJava`: `PASS / exit code 0`;
- `VisionIOSimTest`: `PASS / exit code 0`;
- required inherited vision regressions: `PASS / exit code 0`;
- full test suite: `PASS / exit code 0`; and
- clean build: `PASS / exit code 0`.

The earlier Codex-local `compileTestJava` classpath failure is retained only as
`RESOLVED / SUPERSEDED / NON-REPRODUCIBLE`. It is not the current lesson state
and does not authorize a Gradle repair.

Runtime Robot/HALSIM wiring, Driver Station / Glass, physical camera, and real
robot are not applicable to this unwired vendor-neutral adapter lesson. Its
behavior is verified through automated deterministic contract tests.

## Cleanup evidence

The User deleted the audited temporary forensic log and the accidental
untracked V00_L03 path copy. At that historical cleanup stage, V00_L04 was the
active uncommitted lesson; subsequent closure and publication are recorded
below. The User-reported post-cleanup state contained no remaining V00_L03
modification.

## Deferred scope

- V00_L05: AprilTag robot-pose candidate estimation and multi-tag solving.
- V00_L06: quality, ambiguity, uncertainty, and acceptance policy.
- V00_L07: timestamps, latency, freshness, and temporal semantics.
- V00_L08: real-camera/vendor integration and physical calibration.
- V00_L09: vision fusion through `SwerveDrivePoseEstimator`.

No V00_L05 lesson has been created. A01 ends at A01_L09, and A01_L10 remains
prohibited.

## Closure record

```text
final read-only architecture/documentation review PASS
    -> Architect closure authorization PASS
    -> COMPLETE / FROZEN / READ-ONLY
    -> User-owned Git add/commit/push confirmed @ 5461555
```

The User confirmed Git publication at `5461555` with `PUBLISHED @ 5461555 /
USER VERIFIED`. V00_L05 remains deferred and has not been created.
