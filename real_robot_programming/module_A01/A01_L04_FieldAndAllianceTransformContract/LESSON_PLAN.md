# A01_L04 - Field and Alliance Transform Contract - Active Plan

## Activation State

- Lesson: A01_L04_FieldAndAllianceTransformContract
- Previous Lesson: A01_L03_TrajectoryGenerationAndSamplingFundamentals - COMPLETE / FROZEN / READ-ONLY
- Status: COMPLETE / FROZEN / READ-ONLY
- Inheritance baseline, identity, Architecture Audit, Design-Lock Verification,
  and Design-Lock HOLD Resolution: PASS.
- Implementation: COMPLETE - pure `FieldVariant` and
  `FieldAllianceTransform` data contract only.
- Independent user Java verification: PASS - compileJava, compileTestJava,
  FieldAllianceTransformTest, LearningTrajectoryFactoryTest, full regression,
  and clean build.

## Locked Single Concept

Define the canonical Blue-origin field frame and a pure, explicit transform
from canonical data to Blue or Red alliance-specific reference data. The
transform must receive both `FieldVariant` and `Alliance` explicitly.

| FieldVariant | Length | Width |
|---|---:|---:|
| REBUILT_WELDED | 16.541 m | 8.069 m |
| REBUILT_ANDYMARK | 16.518 m | 8.043 m |

Blue is identity. Red is a 180-degree rotation about the selected field centre.
Unknown alliance is not transformed and must fail closed at a future composition
boundary. One execution path has exactly one transform owner.

## Planned Minimum Delta

- Constants: add the explicit official-2026 `FieldVariant` data model.
- util: add one pure `FieldAllianceTransform` utility for pose, heading,
  field-relative vector, angular-rate, and trajectory reference data.
- tests: add deterministic transform coverage only.

No command, scheduler, drivetrain, ChassisSpeeds, RobotContainer,
SwerveSubsystem, telemetry, IO, hardware, dependency, or vendor integration
change belongs to L04.

## Required Documentation Before Freeze

- `L01_to_L05_Autonomous_Learning_Map.md`
- `WPILib_Field_Coordinate_System.md`
- `Blue_Red_Transform_Mathematics.md`
- `Official_2026_Field_Variants.md`
- `Trajectory_Transform_Semantics.md`
- `Transform_Ownership_and_Double_Transform_Prevention.md`
- `Unknown_Alliance_Safety_Contract.md`
- `L03_to_L04_to_L05_Data_Flow.md`
- All eight detailed L04 learning documents: COMPLETE. Independent user
  verification supplied PASS for the Java/build gates and non-actuating
  Simulation; the transition guide is FINAL / PASS. L04 is frozen. Real Robot
  remains HOLD because no actuation is authorized.
- Final `docs/A01_L03_to_A01_L04_Step_by_Step.md`
