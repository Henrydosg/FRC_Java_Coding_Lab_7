# A01_L07 AutoBuilder Contract Integration - Pre-Activation Design Record

## 1. Record Identity and Authority

- Record status: **PRE-ACTIVATION ONLY**
- Authoritative future lesson: `A01_L07 - AutoBuilder Contract Integration`
- Authoritative future directory: `A01_L07_AutoBuilderContractIntegration`
- Current active lesson: `A01_L06 - PathPlanner Path and Runtime Integration`
- Current lesson state: `IN_PROGRESS / EDITABLE`
- A01_L06 Real Robot state: `DEFERRED / PENDING / NOT VERIFIED`
- A01_L07 state: `NOT ACTIVE / NOT CREATED / NOT IMPLEMENTED`

This record is design evidence stored with the active A01_L06 lesson. It is not an A01_L07
`LESSON_STATUS.md`, does not activate A01_L07, does not authorize implementation, and does not
change any verification result. The authority order remains `AGENTS.md`, Document A, Document B,
Document C, `README.md`, then repository source. The approved A01 roadmap ADR fixes A01_L07 after
frozen A01_L06 and prohibits creating it from an unfinished predecessor.

## 2. Purpose and Non-Goals

The future lesson will add exactly one architectural concept: configure PathPlanner
`AutoBuilder` against the established swerve contracts, then obtain a path-following command
through `AutoBuilder.followPath(...)` without moving mechanism ownership out of
`SwerveSubsystem`.

This record does not:

- implement or configure `AutoBuilder`;
- create an A01_L07 project;
- approve an alliance-transform ownership change;
- select or compose autonomous routines, which belongs to A01_L08;
- add `NamedCommands` or event markers, which belongs to A01_L09;
- alter A01_L06 source, tests, status, verification evidence, or real-robot gate.

## 3. Frozen Architecture Flow

The future implementation must preserve both frozen flows:

```text
Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware

hardware -> IOInputs -> subsystem / estimator -> immutable Observation
         -> telemetry -> NT4 / Glass / log

Future AutoBuilder command -> SwerveSubsystem public contract -> io -> hardware
```

`AutoBuilder` is a command-construction/integration facility. It must not access vendor hardware,
mutable IO inputs, telemetry publishers, or `RobotContainer` state. Telemetry remains a read-only
consumer of immutable Observations.

## 4. Pose Supplier Contract

The configured pose supplier maps to `SwerveSubsystem.getEstimatedPose()`, whose established
return type is `Optional<Pose2d>`. PathPlanner requires a non-null `Supplier<Pose2d>`; therefore a
missing pose must never be silently replaced with an invented origin or used to authorize motion.

The future bridge must enforce this policy:

1. Before an AutoBuilder path command may start, require a present, finite estimated pose.
2. Cache only a pose that was actually supplied by the subsystem.
3. If pose availability is lost during execution, latch the session fault, inhibit all subsequent
   outputs, command `SwerveSubsystem.stop()`, and expose only the last verified pose to satisfy the
   library callback while the command terminates.
4. If no verified pose has ever existed, do not initialize the PathPlanner command.

This is a safety adapter around an `Optional` contract, not permission to fake missing state.

## 5. Pose Reset Consumer Contract

The reset consumer maps to `SwerveSubsystem.resetKnownFieldPose(Pose2d)`. That method is the sole
known-field-pose authority and retains its disabled-only safety rule. The future adapter must:

- validate the requested pose as finite before forwarding it;
- call the subsystem method rather than touching gyro, odometry, estimator, or IO directly;
- treat a `false` return as a rejected reset, latch a fault, inhibit output, and stop;
- never reinterpret AutoBuilder configuration as permission to reset while enabled.

Because the PathPlanner callback type is `Consumer<Pose2d>`, the bridge must retain the boolean
result internally and make rejection observable through its fault contract. Any generated-auto
flow that attempts its initial reset after enable is incompatible with the current reset contract
until a separately reviewed design resolves the timing; L07 must not weaken the subsystem rule.

## 6. Robot-Relative Speed Supplier Contract

The speed supplier maps to `SwerveSubsystem.getMeasuredRobotRelativeSpeeds()`, returning
`Optional<ChassisSpeeds>`. It must supply measured robot-relative speeds, not commanded speeds and
not field-relative speeds.

The future adapter must require present, finite measured speeds before path execution. Loss or
non-finite data during execution latches the session fault, inhibits output, stops the subsystem,
and terminates the wrapper. A zero-speed substitution may be used only as a non-actuating library
callback value after the output gate is already closed; it must never clear the fault or permit
continued path following.

## 7. Output Consumer Contract

The output callback maps to `SwerveSubsystem.acceptChassisSpeeds(ChassisSpeeds)`. The subsystem
continues to own kinematics, module optimization, actuation, and safe stop. The bridge must:

- accept robot-relative `ChassisSpeeds` only;
- reject null or non-finite output before it reaches the subsystem;
- forward output only when the preconditions are valid and the session fault latch is clear;
- call `SwerveSubsystem.stop()` instead of forwarding motion after any fault;
- avoid direct IO, module-state, motor-controller, or vendor API access.

The future baseline should use the `Consumer<ChassisSpeeds>` configure overload. Feedforward
handling is not introduced unless the lesson design and tests explicitly require the
`BiConsumer<ChassisSpeeds, DriveFeedforwards>` overload.

## 8. Subsystem Requirements and Scheduler Ownership

The `AutoBuilder.configure(...)` requirements varargs must contain the existing
`SwerveSubsystem` instance. Commands returned by `AutoBuilder.followPath(...)` must therefore own
the drivetrain scheduler requirement and interrupt conflicting drivetrain commands normally.
Any safety wrapper must preserve the same requirement and must not create a second subsystem
instance. `RobotContainer` supplies the single composed instance.

## 9. RobotConfig and Physical Configuration Source

`RobotConfig` must be built from named values owned by `Constants.java`, with no magic numbers and
no duplicated competing authority. The audited baseline values for future implementation are:

- drive gearing: **6.75**;
- wheel radius: **0.0508 m**;
- drive current limit: **70 A**;
- robot mass: **45 kg, provisional and requiring validation**;
- robot moment of inertia: **5 kg*m^2, provisional and requiring validation**;
- maximum drive velocity: **4 m/s, provisional and requiring validation**;
- wheel coefficient of friction: **1.0, provisional and requiring validation**;
- module locations: derive from the existing swerve geometry constants, not new coordinates.

The drive gearing must be **6.75**, not the stale or incorrect **6.538...** value. Provisional
physical values must remain visibly provisional in code and documentation until measured or
otherwise verified; they may not be reported as real-robot verified facts.

## 10. Verified PathPlanner 2026 API Surface

The locally resolved dependency is PathPlannerLib Java `2026.1.2`. Its source confirms these
relevant static entry points:

```java
AutoBuilder.configure(
    Supplier<Pose2d> poseSupplier,
    Consumer<Pose2d> resetPose,
    Supplier<ChassisSpeeds> robotRelativeSpeedsSupplier,
    Consumer<ChassisSpeeds> output,
    PathFollowingController controller,
    RobotConfig robotConfig,
    BooleanSupplier shouldFlipPath,
    Subsystem... driveRequirements);

AutoBuilder.configure(
    Supplier<Pose2d> poseSupplier,
    Consumer<Pose2d> resetPose,
    Supplier<ChassisSpeeds> robotRelativeSpeedsSupplier,
    BiConsumer<ChassisSpeeds, DriveFeedforwards> output,
    PathFollowingController controller,
    RobotConfig robotConfig,
    BooleanSupplier shouldFlipPath,
    Subsystem... driveRequirements);

AutoBuilder.followPath(PathPlannerPath path);
```

Implementation must compile against the resolved API rather than memory, examples for another
release, or deprecated signatures.

## 11. Exactly-Once Configuration Strategy

`AutoBuilder` stores process-global static configuration. The future lesson must configure it
exactly once per robot-program session through one dedicated integration object created by
`RobotContainer`.

The integration object must own a one-way local configuration guard. A second configuration
attempt is a configuration fault: report it, latch the session fault, stop the subsystem, and do
not treat the library's internal overwrite behavior as recovery. Tests must isolate static state
and prove that normal construction invokes configuration once. Configuration must not occur in a
command constructor, button binding, periodic method, telemetry class, or subsystem.

## 12. Alliance Transform Ownership Blocker

Alliance transform design remains **UNRESOLVED / BLOCKED FOR IMPLEMENTATION**. A01_L04 remains the
current field/alliance transform contract owner. PathPlanner also offers runtime path flipping,
and the existing A01_L06 adapter deliberately sets `preventFlipping` to avoid hidden double
transforms. Enabling AutoBuilder's `shouldFlipPath` from alliance color without an ownership
decision could transform a path twice or apply dimensions inconsistent with the A01_L04 field
contract.

No transform-ownership change is approved by this record. Until formal architecture review
selects exactly one owner, future implementation must keep automatic AutoBuilder flipping
disabled (`shouldFlipPath` returns `false`) and must not claim red-alliance equivalence. The final
L07 design cannot be locked while this blocker is open.

## 13. Terminal Stop Policy

The audited PathPlanner `FollowPathCommand.end(...)` does not guarantee zero output for every
termination path; its normal-finish zero output is conditional on a low goal-end velocity.
Therefore the future command returned to scheduling must be enclosed by an explicit safety
boundary that calls `SwerveSubsystem.stop()` unconditionally on:

- successful completion;
- interruption;
- cancellation;
- precondition failure;
- reset rejection;
- pose or measured-speed loss;
- invalid controller output;
- any latched integration fault.

Library end behavior is supplemental and must not be the terminal-stop authority.

## 14. Session-Level Fault Latch

The future integration object must own a monotonic fault latch for the robot-program session.
Once set, it must:

- remain set for the rest of that process session;
- reject new AutoBuilder path commands or keep them non-actuating;
- gate every output callback to `SwerveSubsystem.stop()`;
- preserve the first fault reason for diagnostics;
- never be cleared by telemetry, command rescheduling, alliance changes, or a second configure
  attempt.

A process restart is the baseline reset boundary. Any future operator-reset mechanism requires a
separate safety review and is not authorized by A01_L07.

## 15. RobotContainer Composition-Root Role

`RobotContainer` may construct the future integration object, inject the existing
`SwerveSubsystem`, select the PathPlanner controller implementation, invoke the object's
exactly-once configuration entry point, and bind/select commands. It must not calculate robot
physics, unwrap optional state, decide transform mathematics, validate callback data, implement
fault behavior, or command hardware. Those policies belong in the dedicated command/integration
boundary and the subsystem's existing public contracts.

## 16. Planned Future Source Delta

Only after A01_L06 is verified, completed, frozen, and copied according to the lifecycle should
A01_L07 propose its production delta. The anticipated minimal delta is:

- add one dedicated AutoBuilder contract integration class under `frc.robot.commands`;
- add named PathPlanner physical/configuration values to `Constants.java`;
- update `RobotContainer.java` only for construction, dependency injection, exactly-once
  activation, and command exposure;
- retain `PathPlannerTrajectoryAdapter` unless a reviewed migration makes a narrow replacement
  necessary;
- do not change IO interfaces, hardware implementations, Observation models, telemetry control
  direction, estimator ownership, or the subsystem's centralized `stop()` authority.

Exact files and code are subject to the future activation audit. This forecast is not
implementation authorization.

## 17. Planned Future Test Delta

The future L07 test plan must independently verify at least:

1. the exact PathPlanner 2026 configure contract and selected overload;
2. configuration occurs exactly once and a duplicate attempt faults safely;
3. pose supplier forwards a real finite subsystem estimate;
4. missing initial pose prevents command start without fabricated motion state;
5. pose loss during execution latches fault and stops;
6. reset consumer forwards only a finite pose and preserves disabled-only rejection;
7. measured robot-relative speeds are used, with missing/non-finite data faulting safely;
8. finite robot-relative outputs reach `acceptChassisSpeeds(...)` and invalid outputs do not;
9. the returned command requires the existing `SwerveSubsystem`;
10. terminal stop occurs on success, interruption, cancellation, and every fault path;
11. the fault latch is monotonic for the process session;
12. `RobotConfig` uses drive gearing 6.75 and derives module locations from existing constants;
13. provisional physical values remain explicitly provisional;
14. automatic alliance flipping stays disabled while transform ownership is unresolved;
15. frozen backbone, Observation, telemetry, IO, and `RobotContainer` boundaries remain intact.

Build, simulation, Driver Station / Glass, and real-robot evidence remain user-owned gates and may
be recorded as PASS only from supplied evidence.

## 18. Current Verification and Status Boundary

At creation of this record:

- A01_L06 is the sole active lesson and remains `IN_PROGRESS / EDITABLE`.
- A01_L06 Real Robot verification remains `DEFERRED / PENDING / NOT VERIFIED`.
- A01_L07 is `NOT ACTIVE / NOT CREATED / NOT IMPLEMENTED`.
- alliance-transform ownership is unresolved;
- no AutoBuilder implementation, source change, test change, build result, simulation result,
  Driver Station / Glass result, or real-robot result is created by this document.

This record may inform a later architecture review, but it cannot close a gate or freeze a design
by itself.

## 19. Future Activation Procedure

When and only when A01_L06 has completed all required user verification and is marked
`COMPLETE / FROZEN`, the future activation sequence is:

1. The user verifies the Git/worktree state and confirms the intended frozen A01_L06 snapshot.
2. Re-read all required governance documents, the A01 roadmap ADR, A01_L06 status/source/docs, and
   this pre-activation record.
3. Confirm A01_L06 Real Robot and all other required gates have verified evidence; stop if any
   required gate is missing or failed.
4. Copy the completed A01_L06 lesson into exactly
   `real_robot_programming/module_A01/A01_L07_AutoBuilderContractIntegration`.
5. Remove copied generated directories such as `build/` and `.gradle/` from the new lesson only.
6. Rename lesson identity and create its `LESSON_STATUS.md` lifecycle state without modifying the
   frozen A01_L06 source snapshot.
7. Run the user-owned baseline build and record only supplied evidence.
8. Resolve the alliance-transform ownership blocker through formal architecture review before
   enabling any runtime flip behavior.
9. Implement one concept in independently verifiable steps: AutoBuilder contract integration.
10. Add and run the planned tests, then complete user-owned build, simulation, Driver Station /
    Glass, and real-robot verification in the required order.
11. Finalize the transition guide and lesson status only after all required evidence exists.
12. The user alone performs Git commit and push.

If A01_L06 is not frozen, the target directory already exists, the source snapshot is not clean,
the API differs, or the transform blocker is unresolved for the proposed behavior, activation
must stop rather than rename, merge, or reuse an invalid preparation directory.
