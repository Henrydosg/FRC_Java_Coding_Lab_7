# Governance 2.0 Delegated Rule — CODE CONVENTIONS

> ACTIVE DELEGATED GOVERNANCE 2.0 RULE
>
> Incorporated by [root AGENTS.md](../../../AGENTS.md) within its exact delegated scope. Root controls conflicts pending HOLD and Architect reconciliation.

Delegated scope: Complete Java delivery, headers/comments/naming, approved configuration authority and vendor configuration boundary. This file creates no lesson, repair, publication or later-gate authorization.

Registered migration decision: [GOV2 ADR](../../architecture_decisions/ADR_GOV2_Governance_2_0_Agent_Instructions_State_and_History_Migration.md).

## Active code/configuration contract

Use complete Java files with all lines present; no partial/omitted code, deprecated APIs, magic numbers or non-English comments. Preserve WPILib headers and the SSIS author/mentor header before package as required below. Use approved package/dependency responsibilities, not file proliferation or layer-per-lesson counting.

Constants.java remains the default configuration authority, organized by mechanism/concern. Do not split it during ordinary lessons; a constants package requires formal architecture review. Concrete IO owns vendor apply/readback behavior; algorithms obtain approved vendor-neutral configuration. Hardware identity, safety configuration, calibration, provisional tuning and final verified tuning are different evidence classes. Do not copy historical CAN/offset/inversion/model values as new physical authority.

No Constants cleanup, tuning change or code modification is authorized by these rules. Existing hardware-evidence and future hardware-selection gates remain binding through applicable roadmaps; no new tuning policy is introduced.

## Traceable source families

These reviewed clauses retain full accepted A/H/C families. Normalization is limited to LF presentation, relocated section pointers, scoped User cache ownership and explicit registered GOV2 Git/physical-scope/future-reading decisions. Exact original bytes remain in the archive. Named historical lesson exclusions retain their original applicability and do not authorize work.

### Family A-L1944-L1965 — Java/code conventions

Source: AGENTS L1944–L1965; accepted classification A; Detailed `rules/CODE_CONVENTIONS.md`; preserve configuration-authority qualification

## 7. Java Rules

- Complete Java files only.
- No partial code.
- No omitted lines.
- No deprecated APIs.
- No magic numbers.
- English comments only.
- Preserve WPILib header.
- Add

/**
 * Author: SSIS
 * Mentor: SSIS
 */

before package.

Keep Constants.java as the default configuration authority.

---
