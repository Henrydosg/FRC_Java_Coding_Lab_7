---
document_id: "FRC-FINAL-FROZEN-BACKBONE-ARCHITECTURE-POSTER"
document_title: "FINAL FROZEN BACKBONE ARCHITECTURE"
document_class: "Document A"
language: "English"
mirror_status: "VERIFIED"
mirror_fidelity_class: "SEMANTIC_WITH_VISUAL_REFERENCE"
authoritative_source: "FRC_Final_Frozen_Backbone_Architecture_Poster.pdf"
authoritative_source_sha256: "0bb243082f32a77f4e1671404c283bdc6de498e6cf1402717b8910a4595a7cf5"
source_version: "1.1"
source_status: "FROZEN"
verified_on: "2026-08-29"
verification_method: "Independent PDF-to-Markdown semantic fidelity review"
manifest: "../GOVERNANCE_DOCUMENT_MANIFEST.md"
---

> This is a VERIFIED machine-readable mirror that has passed independent
> semantic fidelity review. The English PDF remains authoritative, and this
> mirror has no independent or equal authority rank. If a conflict exists, the
> PDF controls.
> Routine governance use of this VERIFIED mirror is controlled by AGENTS.md.
> The canonical manifest remains an integrity and verification index only.
> Trust requires VERIFIED status and valid integrity checks. If either fails,
> direct consultation of the authoritative PDF and governed reconciliation are required.

> Fidelity class: SEMANTIC_WITH_VISUAL_REFERENCE. This mirror preserves
> semantic text and explicit relationships. Consult the authoritative PDF for
> the original landscape arrangement, adjacency, shared boxes, color emphasis,
> spatial grouping, and relative prominence.

# FRC ROBOT PROJECT - FINAL FROZEN BACKBONE ARCHITECTURE

FRC Java Coding Lab 7.0 - Permanent Inheritance Development Contract

## Top Overview Bands

```text
CONTROL: Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware
OBSERVATION: hardware -> IOInputs -> subsystem / estimator -> immutable Observation -> telemetry -> NT4 / Glass / log
```

## Shared Architecture Region

The authoritative poster presents PACKAGE TREE, RESPONSIBILITY MAP and SOURCE OF TRUTH, and FINAL FROZEN RULES as parallel views within one shared architecture region.

### PACKAGE TREE

```text
frc.robot
|- Main / Robot / RobotContainer / Constants
|- commands/  controls/  subsystems/
|- io/
|  `- <mechanism>/
|     |- <Mechanism>IO
|     |- <Mechanism>IOReal
|     `- <Mechanism>IOSim
|- telemetry/
|  |- RobotTelemetry
|  `- <mechanism>/
|     |- <Mechanism>TelemetryFacade
|     `- focused telemetry modules
`- util/

deploy/pathplanner/
|- paths/
`- autos/
```

### RESPONSIBILITY MAP

```text
controls = Driver intent
commands = Robot actions / coordination
subsystems = Mechanism API and state
io = Hardware contract + vendor APIs
telemetry = Observation and diagnostics
util = Generic shared helpers only
```

### SOURCE OF TRUTH

- Hardware values originate in IO.
- Mechanism state originates in subsystems/estimators.
- Telemetry only publishes snapshots.
- Each IO owns a mechanism-specific Inputs snapshot.

### FINAL FROZEN RULES

1. RobotContainer: create, select, inject, configure.
2. Commands receive the smallest dependency needed.
3. Subsystems depend on IO interfaces only.
4. IO never publishes NetworkTables.
5. Telemetry never reads vendor devices or controls behavior.
6. Facade hides internal telemetry modules.
7. Constants.java is default; constants/ requires formal review.
8. commands/auto coordinates; PathPlanner files live in deploy/.
9. New mechanisms follow responsibility patterns, not file-by-file copying.
10. Normal lessons extend the backbone; they do not redesign it.

## APPROVED FLOWS

The authoritative poster presents CONTROL, OBSERVATION, and FORBIDDEN as parallel lower flow regions under APPROVED FLOWS.

### CONTROL

```text
Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware
```

### OBSERVATION

```text
hardware -> IOInputs -> subsystem / estimator -> immutable Observation -> telemetry -> typed NT4 / Glass / log
```

### FORBIDDEN

The following directed relationships are prohibited in the authoritative poster's FORBIDDEN region.

```text
Observation -> hardware / vendor / NT / scheduler / RobotContainer / mutable state / control
Telemetry -> control
IO -> NT
```

## Status

STATUS: VERSION 1.1 FROZEN - APPROVED 2026-08-01

## Revision History

REVISION HISTORY: 1.0 (2026-07-18) Initial frozen poster | 1.1 (2026-08-01) APPROVED observation package governance migration
