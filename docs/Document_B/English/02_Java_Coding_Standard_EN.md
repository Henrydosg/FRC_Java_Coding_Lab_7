---
document_id: "ES-02-JAVA-CODING-STANDARD"
document_title: "JAVA CODING STANDARD"
document_class: "Document B"
language: "English"
mirror_status: "VERIFIED"
mirror_fidelity_class: "TEXTUAL"
authoritative_source: "02_Java_Coding_Standard_EN.pdf"
authoritative_source_sha256: "1e5a15cad105668b92e2b8577ca5aa45cee7d5d3ea56cb7dda1320a1dfade58f"
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

## JAVA CODING STANDARD

Source-code rules for FRC Java Coding Lab 7.0

| Document | Version | Status | Language |
| --- | --- | --- | --- |
| ES-02 | 1.0 | FROZEN | English |

SSIS FRC Team 10951

Author: SSIS | Mentor: SSIS

## Code Delivery

- Always provide the FULL FILE when creating or modifying Java.
- Do not use diffs, ellipses, or omitted lines.
- Make one clear change per step.

## Headers and Comments

- Preserve the WPILib copyright header at the top.
- Place the Author: SSIS / Mentor: SSIS block immediately before package.
- Use concise technical English comments only.

## Design

- No magic numbers; configuration belongs in Constants.java.
- Do not use deprecated APIs.
- RobotContainer performs composition and wiring only.
- Commands do not access hardware directly.
- Telemetry observes only and never controls.
- IO does not know NetworkTables.

## Naming

- Classes and interfaces use PascalCase.
- Methods and fields use camelCase.
- Constants use lowerCamelCase following the current WPILib project convention.
- Names describe responsibility, not temporary lesson context.

## Dependency Rules

controls → commands → subsystems → io → hardware

Telemetry sits beside the control pipeline and receives snapshots only from
commands or subsystems.

## Revision History

| Version | Date | Status | Notes |
| --- | --- | --- | --- |
| 1.0 | 2026-07-18 | FROZEN | Initial release |
