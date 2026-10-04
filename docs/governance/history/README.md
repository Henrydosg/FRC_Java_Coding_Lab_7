# Governance 2.0 Candidate History Store

> NON-AUTHORITATIVE GOVERNANCE 2.0 MIGRATION CANDIDATE
>
> DO NOT USE AS CURRENT GOVERNANCE AUTHORITY BEFORE GOV2-G7 CUTOVER

GOV2-G4 population candidate; IN PROGRESS / PENDING ARCHITECT ACCEPTANCE. GOV2-G5 is NOT AUTHORIZED. The existing root AGENTS.md remains authoritative until explicit Architect GOV2-G7 cutover. This document grants no current operating, lesson, repair, publication, or next-gate permission.

Registered migration decision: [GOV2 ADR](../../architecture_decisions/ADR_GOV2_Governance_2_0_Agent_Instructions_State_and_History_Migration.md).

## Original archive

[AGENTS.pre-governance-2.0.md](originals/AGENTS.pre-governance-2.0.md) is the exact original 5,625-line / 405,773-byte source, SHA-256 01f981be0dfedd08484f9c19e67c6b4743e5cd5b2f7de150937fc877e95441ab. It is an immutable NON-AUTHORITATIVE historical archive, not an operating entrypoint. Its warning exemption is intentional: adding a header would break byte identity. Encoding/newline facts and committed-checkpoint association are in [SOURCE_MAP](../migration/SOURCE_MAP.md).

## Events and indexes

The 138 accepted D/E/F/G inventory ranges become one event file per accepted range, named AGENTS-L<four-digit-start>-L<four-digit-end>.md. No new historical boundary is invented. Preserved bodies retain exact source bytes, spelling, tokens, SHAs, matrices, failures, pending states and chronology; annotations and delimiters remain outside them.

Indexes are views only: [AUDITS](indexes/AUDITS.md), [REPAIRS](indexes/REPAIRS.md), [CLOSURES](indexes/CLOSURES.md), [PUBLICATIONS](indexes/PUBLICATIONS.md). Multiple indexes can reference one event; never fragment or duplicate its body. Source-neighbour links are not assumed chronological predecessors/successors. Where chronological links are not explicitly established, metadata says so.

Historical records preserve evidence but do not grant fresh permission. Consumed approvals remain consumed; superseded states remain historical; accepted decisions retain their recorded applicability, not continuing edit authority. Source position or the word Current cannot establish current authorization. The root AGENTS remains current operating authority until G7; later accepted decisions inform the candidate projection without rewriting old event bodies.

Exact byte coverage and semantic operative-rule coverage are separate: original recovery/events are checked against source bytes, while [RULE_COVERAGE](../migration/RULE_COVERAGE.md) accounts for binding meaning, scoped exceptions and discoverability. Hashes alone do not certify semantics.
