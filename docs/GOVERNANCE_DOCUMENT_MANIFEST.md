# Governance Document Manifest

> This file is a verification and integrity index for governance source and
> mirror identity. Authoritative English PDFs remain authoritative. VERIFIED
> Markdown mirrors have no independent or equal authority, and the PDF controls
> any conflict. SHA-256 values prove file identity and integrity only; they do
> not establish semantic fidelity. Semantic fidelity requires independent
> PDF-to-Markdown review and Architect authorization. G7 policy activation is
> controlled by AGENTS.md and does not change this manifest's verification-index-only role.

Mirror trust-state definitions and reconciliation remain governed by
`docs/architecture_decisions/ADR_Governance_PDF_Verified_Markdown_Mirrors.md`.

## Integrity Failure Rule

If the current authoritative PDF SHA-256 differs from its recorded source hash,
or the current Markdown SHA-256 differs from its recorded mirror hash, the
manifest entry fails integrity verification and must not be consumed as trusted.
No hash or trust state may be rewritten automatically. Direct PDF consultation
and governed reconciliation are required, including `STALE` or `HOLD`
classification through the existing governance process as applicable. This
manifest does not transition mirror states.

## Document A

### A-BACKBONE-GUIDE

- Document ID: `A-BACKBONE-GUIDE`
- Document title: `FINAL FROZEN PACKAGE BACKBONE`
- Document group: `Document A`
- Authoritative PDF path: `docs/Document_A/FRC_Final_Frozen_Backbone_Guide_EN.pdf`
- Authoritative PDF SHA-256: `1b71842692255da6cb21b0924634b2fbe1ad028a6f9aae5ce08d410dd879ecc0`
- Markdown mirror path: `docs/Document_A/FRC_Final_Frozen_Backbone_Guide_EN.md`
- Final Markdown SHA-256: `2563765ea91f0027d466c5417f960a3ed0f8fbcc448c800593b6314d3233e525`
- Mirror status: `VERIFIED`
- Fidelity class: `TEXTUAL`
- Verified on: `2026-08-29`
- Verification method: `Independent PDF-to-Markdown semantic fidelity review`

### A-BACKBONE-POSTER

- Document ID: `A-BACKBONE-POSTER`
- Document title: `FINAL FROZEN BACKBONE ARCHITECTURE`
- Document group: `Document A`
- Authoritative PDF path: `docs/Document_A/FRC_Final_Frozen_Backbone_Architecture_Poster.pdf`
- Authoritative PDF SHA-256: `0bb243082f32a77f4e1671404c283bdc6de498e6cf1402717b8910a4595a7cf5`
- Markdown mirror path: `docs/Document_A/FRC_Final_Frozen_Backbone_Architecture_Poster.md`
- Final Markdown SHA-256: `e8cf1b184ee13e6c5da890b7b03febcbe84dac393be60d0354e20db61b63ce5c`
- Mirror status: `VERIFIED`
- Fidelity class: `SEMANTIC_WITH_VISUAL_REFERENCE`
- Verified on: `2026-08-29`
- Verification method: `Independent PDF-to-Markdown semantic fidelity review`
- Visual-reference note: Consult the authoritative PDF whenever original spatial arrangement, adjacency, shared boxes, color emphasis, grouping, or relative prominence matters.

### ES-06

- Document ID: `ES-06`
- Document title: `FROZEN INTERFACE CONTRACT`
- Document group: `Document A`
- Authoritative PDF path: `docs/Document_A/ES-06_Frozen_Interface_Contract_EN.pdf`
- Authoritative PDF SHA-256: `cc93b04a3f4242aaea66323d11388def6a75980b9a4865e99ee5e27d0f881d41`
- Markdown mirror path: `docs/Document_A/ES-06_Frozen_Interface_Contract_EN.md`
- Final Markdown SHA-256: `dfde2846cc9fd21b49d8e59e44d1e8c7f2169e203fc7140eb4e98b34ef89e2fd`
- Mirror status: `VERIFIED`
- Fidelity class: `TEXTUAL`
- Verified on: `2026-08-29`
- Verification method: `Independent PDF-to-Markdown semantic fidelity review`

## Document B

### ES-00

- Document ID: `ES-00`
- Document title: `ENGINEERING STANDARD`
- Document group: `Document B`
- Authoritative PDF path: `docs/Document_B/English/00_Engineering_Standard_Overview_EN.pdf`
- Authoritative PDF SHA-256: `5d548cf91703446d0321c9d769397846f980024de1e220957bf8c75663c282a4`
- Markdown mirror path: `docs/Document_B/English/00_Engineering_Standard_Overview_EN.md`
- Final Markdown SHA-256: `e4f5365b4c0cd45c73ea009f812a9c8c16501f3c666e0660951304be71777747`
- Mirror status: `VERIFIED`
- Fidelity class: `TEXTUAL`
- Verified on: `2026-08-28`
- Verification method: `Independent PDF-to-Markdown semantic fidelity review`

### ES-01

- Document ID: `ES-01`
- Document title: `FROZEN DEVELOPMENT WORKFLOW`
- Document group: `Document B`
- Authoritative PDF path: `docs/Document_B/English/01_Frozen_Development_Workflow_EN.pdf`
- Authoritative PDF SHA-256: `d0772532928de3e26e6afe318ceada9d35607679b89d91dacff36fab91acad3c`
- Markdown mirror path: `docs/Document_B/English/01_Frozen_Development_Workflow_EN.md`
- Final Markdown SHA-256: `3be6f13386b3bd0bcc1136ea4188f79c5d50c3fbe7cb8f88141ab028d17d3f90`
- Mirror status: `VERIFIED`
- Fidelity class: `TEXTUAL`
- Verified on: `2026-08-28`
- Verification method: `Independent PDF-to-Markdown semantic fidelity review`

### ES-02

- Document ID: `ES-02`
- Document title: `JAVA CODING STANDARD`
- Document group: `Document B`
- Authoritative PDF path: `docs/Document_B/English/02_Java_Coding_Standard_EN.pdf`
- Authoritative PDF SHA-256: `1e5a15cad105668b92e2b8577ca5aa45cee7d5d3ea56cb7dda1320a1dfade58f`
- Markdown mirror path: `docs/Document_B/English/02_Java_Coding_Standard_EN.md`
- Final Markdown SHA-256: `d31dee11c0798a5bc2bdae5d07ca1040404d500d56431e600d97d410affbb247`
- Mirror status: `VERIFIED`
- Fidelity class: `TEXTUAL`
- Verified on: `2026-08-28`
- Verification method: `Independent PDF-to-Markdown semantic fidelity review`

### ES-03

- Document ID: `ES-03`
- Document title: `ARCHITECTURE REVIEW CHECKLIST`
- Document group: `Document B`
- Authoritative PDF path: `docs/Document_B/English/03_Architecture_Review_Checklist_EN.pdf`
- Authoritative PDF SHA-256: `d4201d4380842003a5aa72ce4f13f836ed279642e8d882ce5fbbff9fa34eab9c`
- Markdown mirror path: `docs/Document_B/English/03_Architecture_Review_Checklist_EN.md`
- Final Markdown SHA-256: `9afb37737fd1d4725df1037a4daa07d497283c8f2c97247658874db49ac56e3b`
- Mirror status: `VERIFIED`
- Fidelity class: `TEXTUAL`
- Verified on: `2026-08-28`
- Verification method: `Independent PDF-to-Markdown semantic fidelity review`

### ES-04

- Document ID: `ES-04`
- Document title: `LESSON AND MODULE CHECKLIST`
- Document group: `Document B`
- Authoritative PDF path: `docs/Document_B/English/04_Lesson_Module_Checklist_EN.pdf`
- Authoritative PDF SHA-256: `c906ce0f6d67822dcf7975b0faf9fe48528fac13e181c66f29ffe165bf44430f`
- Markdown mirror path: `docs/Document_B/English/04_Lesson_Module_Checklist_EN.md`
- Final Markdown SHA-256: `c97cd912235a92fdd0b637e118c2cb7e402ede9e6eabe03c79af0c4a4c6bb203`
- Mirror status: `VERIFIED`
- Fidelity class: `TEXTUAL`
- Verified on: `2026-08-28`
- Verification method: `Independent PDF-to-Markdown semantic fidelity review`

## Document C

### OC-00

- Document ID: `OC-00`
- Document title: `OBSERVATION ARCHITECTURE OVERVIEW`
- Document group: `Document C`
- Authoritative PDF path: `docs/Document_C/English/00_Observation_Architecture_Overview_EN.pdf`
- Authoritative PDF SHA-256: `df2402cbc2aeb942015129a8d366624ca32fe5f8515d1f35fb1d38089f68e5e0`
- Markdown mirror path: `docs/Document_C/English/00_Observation_Architecture_Overview_EN.md`
- Final Markdown SHA-256: `dfebd75a0e7a8bfe7abe356958d6d37915a3963036ac3a9fc5a4ca3a8b229c87`
- Mirror status: `VERIFIED`
- Fidelity class: `TEXTUAL`
- Verified on: `2026-08-28`
- Verification method: `Independent PDF-to-Markdown semantic fidelity review`

### OC-01

- Document ID: `OC-01`
- Document title: `OBSERVATION MODEL CONTRACT`
- Document group: `Document C`
- Authoritative PDF path: `docs/Document_C/English/01_Observation_Model_Contract_EN.pdf`
- Authoritative PDF SHA-256: `8de8e22f558621337616011286ae3f45a32bcbd5432e1bd45167f6c45f621220`
- Markdown mirror path: `docs/Document_C/English/01_Observation_Model_Contract_EN.md`
- Final Markdown SHA-256: `fc852f55806e709d70a0678bc88fcd1700bf9f68448b52a6f8640f4db26cd0a8`
- Mirror status: `VERIFIED`
- Fidelity class: `TEXTUAL`
- Verified on: `2026-08-28`
- Verification method: `Independent PDF-to-Markdown semantic fidelity review`

### OC-02

- Document ID: `OC-02`
- Document title: `OBSERVATION PACKAGE STANDARD`
- Document group: `Document C`
- Authoritative PDF path: `docs/Document_C/English/02_Observation_Package_Standard_EN.pdf`
- Authoritative PDF SHA-256: `5886555e246811d23efa06c7aa8180b82f4538f0bdd6db343d3fe4ffae546315`
- Markdown mirror path: `docs/Document_C/English/02_Observation_Package_Standard_EN.md`
- Final Markdown SHA-256: `074ddc0b1ffca067a74e2af4df8e76b1409b8a1f669981267af46eb5444be015`
- Mirror status: `VERIFIED`
- Fidelity class: `TEXTUAL`
- Verified on: `2026-08-28`
- Verification method: `Independent PDF-to-Markdown semantic fidelity review`

### OC-03

- Document ID: `OC-03`
- Document title: `OBSERVATION ARCHITECTURE CHECKLIST`
- Document group: `Document C`
- Authoritative PDF path: `docs/Document_C/English/03_Observation_Architecture_Checklist_EN.pdf`
- Authoritative PDF SHA-256: `09bd85cfa1cccc152ab877a859060bb0be7377efc52ff79f1eb8edc950d33a52`
- Markdown mirror path: `docs/Document_C/English/03_Observation_Architecture_Checklist_EN.md`
- Final Markdown SHA-256: `f35371bfc8faa6d1118c6d3b00307c3caf4c7f22dc39715852db4f7b4a894cbf`
- Mirror status: `VERIFIED`
- Fidelity class: `TEXTUAL`
- Verified on: `2026-08-28`
- Verification method: `Independent PDF-to-Markdown semantic fidelity review`
