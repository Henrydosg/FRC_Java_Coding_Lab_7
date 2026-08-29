---
document_id: "ES-01-FROZEN-DEVELOPMENT-WORKFLOW"
document_title: "FROZEN DEVELOPMENT WORKFLOW"
document_class: "Document B"
language: "English"
mirror_status: "VERIFIED"
mirror_fidelity_class: "TEXTUAL"
authoritative_source: "01_Frozen_Development_Workflow_EN.pdf"
authoritative_source_sha256: "d0772532928de3e26e6afe318ceada9d35607679b89d91dacff36fab91acad3c"
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

## FROZEN DEVELOPMENT WORKFLOW

Mandatory process for every lesson and module

| Document | Version | Status | Language |
| --- | --- | --- | --- |
| ES-01 | 1.0 | FROZEN | English |

SSIS FRC Team 10951

Author: SSIS | Mentor: SSIS

## Scope

Applies to every change involving commands, subsystems, IO, telemetry, controls,
autonomous, sensors, and utilities.

## Step 0 - Backbone Check

Verify package responsibility, dependency direction, control flow, observation
flow, RobotContainer role, telemetry rules, and IO rules.

## Step 1 - Contract Design

Define interfaces, data snapshots, method signatures, units, ownership, and data
sources before implementation.

## Step 2 - Architecture Review

Confirm the design preserves the Frozen Backbone, avoids duplicate
responsibility, and avoids over-engineering.

## Step 3 - Full Implementation

Write code only after review PASS. Every Java change must be delivered as a FULL
FILE, never as a diff or ellipsis.

## Step 4 - BUILD SUCCESSFUL

Run a build after each clear change. Do not continue while the build is failing.

## Step 5 - Simulation / Real Robot Verification

Validate in simulation when applicable, then test on the real robot using safety
controls, Glass/Driver Station, and logs.

## Stop Rules

- Backbone Check FAIL → stop and redesign.
- Architecture Review FAIL → do not implement.
- Build FAIL → do not advance.
- Verification FAIL → record the cause, fix one change at a time, and retest.

## Module Closure

1. Build PASS.
2. Simulation or real robot PASS.
3. Clear Git commit.
4. Record Known Issues and Lessons Learned.
5. Confirm dependencies for the next module.

## Revision History

| Version | Date | Status | Notes |
| --- | --- | --- | --- |
| 1.0 | 2026-07-18 | FROZEN | Initial release |
