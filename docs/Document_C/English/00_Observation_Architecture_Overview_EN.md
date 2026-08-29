---
document_id: "OC-00"
document_title: "OBSERVATION ARCHITECTURE OVERVIEW"
document_class: "Document C"
language: "English"
mirror_status: "VERIFIED"
mirror_fidelity_class: "TEXTUAL"
authoritative_source: "00_Observation_Architecture_Overview_EN.pdf"
authoritative_source_sha256: "df2402cbc2aeb942015129a8d366624ca32fe5f8515d1f35fb1d38089f68e5e0"
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

## OBSERVATION ARCHITECTURE OVERVIEW

Permanent architecture boundary for frc.robot.observation

| Document | Version | Status | Language |
| --- | --- | --- | --- |
| OC-00 | 1.0 | FROZEN | English |

SSIS FRC Team 10951

Author: SSIS | Mentor: SSIS

## 1. Purpose and Authority

Document C formally defines frc.robot.observation as a permanent top-level package. It extends the
Frozen Backbone without changing control flow. English is normative; Vietnamese is explanatory.
Document A remains higher authority, and Document B remains the governing workflow and coding
standard.

## 2. Frozen Architecture

CONTROL: Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware.

OBSERVATION: hardware -> IOInputs -> subsystem or estimator -> immutable Observation -> telemetry ->
NT4 / Glass / log.

The two flows are separate. Observation describes what is known; it never decides what the robot shall do.

## 3. Ownership and Boundaries

IO owns hardware access and populates a mutable one-cycle IOInputs transport snapshot. A subsystem
or dedicated estimator owns mechanism interpretation and produces an immutable Observation. Telemetry
consumes and publishes Observations. Commands and controls remain on the control path.

Observation is not util, IO, telemetry, command, or control logic. It may contain immutable value models,
enums, validity metadata, and pure evaluators only.

## 4. Dependency Direction

Allowed: observation may depend on the Java standard library and small vendor-neutral value types
approved by architecture review. Subsystems and estimators may create Observations. Telemetry may
depend on Observation types.

Forbidden: observation dependencies on vendor APIs, hardware devices, NetworkTables,
CommandScheduler, RobotContainer, telemetry publishers, controls, or commands. Observation must not
call back into subsystems or IO.

## 5. Stable Package Tree

```text
frc.robot/
  observation/
    drive/
      DriveObservation.java
      DriveObservationEvaluator.java
    intake/
      IntakeObservation.java
    flywheel/
      FlywheelObservation.java
    feeder/
      FeederObservation.java
  telemetry/
  subsystems/
  io/
```

Create one mechanism subpackage only when it has a real observation responsibility. Do not create empty
layers.

## 6. L10 Reviewed Alignment

D01_L10 demonstrates mechanism-specific immutable records and telemetry facades that consume them.
Drive uses a separate evaluator. These are reviewed examples, not a source of authority.

Migration is required where an Observation or evaluator lacks explicit time/validity semantics needed by a
future feature, or where telemetry publishes directly from mutable IOInputs. Completed L10 remains frozen
and is not modified.

## 7. Change Policy

Version 1.0 is FROZEN. Moving this responsibility, reversing dependencies, adding behavior control, or
changing the immutable model contract requires formal architecture review, version update, impact
analysis, and a migration plan.

## Revision History

1.0 | 2026-08-01 | FROZEN | Initial release
