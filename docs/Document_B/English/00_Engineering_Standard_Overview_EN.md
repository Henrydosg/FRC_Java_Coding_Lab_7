---
document_id: "ES-00-ENGINEERING-STANDARD-OVERVIEW"
document_title: "ENGINEERING STANDARD"
document_class: "Document B"
language: "English"
mirror_status: "VERIFIED"
mirror_fidelity_class: "TEXTUAL"
authoritative_source: "00_Engineering_Standard_Overview_EN.pdf"
authoritative_source_sha256: "5d548cf91703446d0321c9d769397846f980024de1e220957bf8c75663c282a4"
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

## ENGINEERING STANDARD

Robot Software Development Standards

| Document | Version | Status | Language |
| --- | --- | --- | --- |
| ES-00 | 1.0 | FROZEN | English |

SSIS FRC Team 10951

Author: SSIS | Mentor: SSIS

## Purpose

This document standardizes architecture, development workflow, coding rules, and
verification for the entire FRC Java Coding Lab 7.0.

## Document Set

| Document | Role |
| --- | --- |
| ES-01 - Frozen Development Workflow | Mandatory workflow for every module. |
| ES-02 - Coding Standard | Rules for writing and delivering Java code. |
| ES-03 - Architecture Review Checklist | Pre-implementation architecture checklist. |
| ES-04 - Lesson and Module Checklist | Implementation and completion checklist. |

## Frozen Principle

- The Frozen Backbone defines package responsibilities and dependency direction.
- The Frozen Development Workflow defines how every module is developed.
- Do not change the standard during a lesson without a formal architecture review.
- RobotContainer is the composition root and must remain concise.

## Standard Flow

Frozen Backbone → Contract Design → Architecture Review → Full Implementation →
BUILD SUCCESSFUL → Simulation → Real Robot → Git Commit

## Revision History

| Version | Date | Status | Notes |
| --- | --- | --- | --- |
| 1.0 | 2026-07-18 | FROZEN | Initial release |
