# A01_L04 - Field and Alliance Transform Contract

## Lesson State

- Module: A01 - Autonomous Navigation and Path Following
- Previous lesson: A01_L03_TrajectoryGenerationAndSamplingFundamentals - COMPLETE / FROZEN / READ-ONLY
- Status: COMPLETE
- Freeze State: FROZEN / READ-ONLY
- Architecture Audit: PASS
- Design-Lock Verification: PASS
- Design-Lock HOLD Resolution: PASS
- Implementation: COMPLETE - pure field/alliance transform data contract only
- Independent user Java verification: PASS - compileJava, compileTestJava,
  FieldAllianceTransformTest, LearningTrajectoryFactoryTest, full regression,
  and clean build.

## Locked Concept

L04 establishes canonical Blue-origin field-frame ownership and one explicit
Blue/Red alliance transform contract. It adds no drivetrain motion.

```text
canonical Blue-origin pose / trajectory
        -> explicit FieldVariant + Alliance transform
        -> alliance-specific reference data
```

- `REBUILT_WELDED`: 16.541 m x 8.069 m.
- `REBUILT_ANDYMARK`: 16.518 m x 8.043 m.
- Blue is identity.
- Red is a 180-degree rotation about the selected field centre.
- Unknown alliance is handled fail closed by a future composition boundary;
  there is no implicit Blue fallback.
- The planned transform utility is pure, receives explicit `FieldVariant` and
  `Alliance` inputs, and never calls `DriverStation.getAlliance()`.
- Exactly one alliance-transform owner is permitted in every future execution
  path.

## Preserved and Deferred Scope

L01 field-heading reference and known starting-pose contract; L02 one-shot
readiness, EstimatedPose, centralized stop, Disable stop, and no automatic
restart; and L03 generation/sampling remain frozen and unchanged.

L04 excludes trajectory following, controllers, ChassisSpeeds, RobotContainer,
SwerveSubsystem, drivetrain actuation, PathPlanner, AutoBuilder, vision,
telemetry, IO, hardware, and tuning changes.

The detailed learning documents listed in LESSON_CHECKLIST.md are mandatory
before L04 can become COMPLETE / FROZEN.

## Completion Record

The required L04 learning guides document the L01-to-L05 progression,
WPILib field coordinates, both official 2026 field variants, Blue/Red
mathematics, trajectory-state semantics, transform ownership, unknown-alliance
safety, and the L03-to-L05 data flow. Independent user verification supplied
PASS for Java compilation, focused/regression tests, clean build, and the
non-actuating Simulation gate. L04 is frozen; Real Robot remains HOLD because
the lesson authorizes no actuation. Git publication remains user-owned.
