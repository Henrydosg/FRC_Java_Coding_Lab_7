---
document_id: "OC-02"
document_title: "OBSERVATION PACKAGE STANDARD"
document_class: "Document C"
language: "English"
mirror_status: "VERIFIED"
mirror_fidelity_class: "TEXTUAL"
authoritative_source: "02_Observation_Package_Standard_EN.pdf"
authoritative_source_sha256: "5886555e246811d23efa06c7aa8180b82f4538f0bdd6db343d3fe4ffae546315"
source_version: "1.0"
source_status: "FROZEN"
verified_on: "2026-08-28"
verification_method: "Independent PDF-to-Markdown semantic fidelity review"
manifest: "../../GOVERNANCE_DOCUMENT_MANIFEST.md"
---

> This is a VERIFIED machine-readable mirror that has passed independent
> semantic fidelity review. The English PDF remains authoritative, and this
> mirror has no independent or equal authority rank. If a conflict exists, the
> PDF controls.

# FRC JAVA CODING LAB 7.0

## OBSERVATION PACKAGE STANDARD

Rules for models, evaluators, dependencies, and migration

| Document | Version | Status | Language |
| --- | --- | --- | --- |
| OC-02 | 1.0 | FROZEN | English |

SSIS FRC Team 10951

Author: SSIS | Mentor: SSIS

## 1. Package Responsibility

frc.robot.observation contains immutable observation models and pure interpretation helpers.
Mechanism-specific types belong in `frc.robot.observation.<mechanism>`. Cross-mechanism aggregation belongs in a
clearly reviewed observation subpackage only when it is genuinely observation, not command
coordination.

## 2. Allowed Contents

`<Mechanism>Observation` records or immutable final classes; observation-only enums and vendor-neutral
value objects; `<Mechanism>ObservationEvaluator` pure helpers; concise JavaDoc defining source, units,
timing, and validity.

## 3. Forbidden Contents

Vendor API imports; motor controllers and sensors; IO interfaces or IOInputs definitions; NetworkTables,
Glass, DataLog, or publisher code; WPILib Command or CommandScheduler; RobotContainer; Xbox or
DriverStation reads; mutable caches; timers; control setpoints; safety interlocks; command decisions;
subsystem lifecycle methods.

## 4. Evaluator Contract

Evaluators are pure, stateless, deterministic, and side-effect free. Their output depends only on explicit
parameters. They do not read clocks, environment, singletons, hardware, NetworkTables, DriverStation, or
mutable global state.

Use static methods in a non-instantiable final class or an immutable callable object only when configuration
is passed explicitly and remains immutable. The same inputs and configuration must produce equal
outputs.

## 5. Dependency Rules

subsystem/estimator -> observation is allowed for construction. telemetry -> observation is allowed for
consumption. observation -> telemetry, io, subsystem, commands, controls, RobotContainer, hardware, or
vendor libraries is forbidden.

A pure evaluator may accept primitive values, immutable Observation inputs, or a deliberately copied
vendor-neutral snapshot. It must not accept a live subsystem, IO implementation, publisher, supplier that
hides side effects, or mutable IOInputs by retained reference.

## 6. Creation and Publication Sequence

1. IO implementation updates IOInputs.
2. Subsystem periodic reads the complete snapshot.
3. Subsystem or estimator copies/selects values and invokes any pure evaluator.
4. It creates one immutable Observation.
5. It atomically replaces its latest Observation reference or returns the new value.
6. Telemetry reads the Observation and publishes typed stable topics.
7. Logging may record the same Observation without changing it.

## 7. Tests

Model tests cover immutability, equality, units, invalid values, and boundary values. Evaluator tests cover
truth tables, thresholds, NaN/infinity policy where applicable, and repeatability. Architecture checks reject
forbidden imports and dependency cycles.

## 8. Migration Notes

Do not modify completed lessons. Apply Document C to the next editable lesson by copying the frozen
baseline under the normal lesson lifecycle. Inventory current observation types; preserve public meaning;
add explicit validity/time only where required; move publication concerns to telemetry; move hardware
transport to IOInputs; replace stateful evaluators with pure functions; build and verify after each change.

L10 aligns in package placement, immutable records, subsystem production, and telemetry consumption.
Its DriveObservationEvaluator is a useful pattern. A future lesson should formally review whether all
mechanism Observations need explicit sample timestamps or validity beyond connected/configuration
flags.

## 9. Change Control

Because this package is FROZEN, responsibility moves or breaking model changes require Reason,
Scope, Impact, Decision, version update, and migration evidence.

## Revision History

1.0 | 2026-08-01 | FROZEN | Initial release
