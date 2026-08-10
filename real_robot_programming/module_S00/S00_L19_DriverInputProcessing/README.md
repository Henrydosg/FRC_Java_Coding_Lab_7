# S00_L19 Driver Input Processing

## Lesson State

`COMPLETE / FROZEN / READ-ONLY`

Previous lesson: `S00_L18_FourModuleStateActuation` (`COMPLETE / FROZEN / READ-ONLY`)

## Final Scope

S00_L19 provides a strictly non-actuating, observable driver-input pipeline:

```text
XboxController
  -> XboxDriverInputSource
  -> semantic axis mapping
  -> DriverInputProcessor
  -> immutable DriverInputObservation
  -> RobotTelemetry
  -> DriverInputTelemetryFacade
  -> NT4
```

The semantic mapping is `forward = -LeftY`, `strafe = -LeftX`, and
`rotation = -RightX`. Processing applies finite-value safety, WPILib deadband rescaling,
signed-square shaping, and normalized clamping.

## Non-Actuating Boundary

L19 does not connect driver input to a default drive command, physical `ChassisSpeeds`, a
driver-input Swerve request, or teleop drivetrain actuation. Its synchronous `RobotTelemetry` pull
is authorized only by the approved L19/L20 ADR and may not be generalized to actuating L20 code.

## Verification and Closure

The final verification record is maintained in `LESSON_STATUS.md`, and the finalized transition
record is `docs/S00_L18_to_S00_L19_Step_by_Step.md`. Established closure and Git evidence were
confirmed as authoritative by the Architect during governance reconciliation; no unsupported test
counts, commit hashes, or remote revision identifiers are asserted.

The user completed the required real-roboRIO verification while the robot remained Disabled. Glass
connected to the real roboRIO; `/DriverInput` existed before Xbox connection; every Raw,
SemanticRaw, and Processed topic existed and updated; USB port 0, axis signs/mapping, and
center/deadband behavior passed; and no drivetrain actuation occurred. The real-robot verification
debt is cleared without introducing actuation.

L19 Java and tests are frozen.
