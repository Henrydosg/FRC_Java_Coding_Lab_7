# ADR: GOV2 Governance 2.0 Agent Instructions, State, and History Migration

- Record status: **REGISTERED / ACTIVE GOVERNANCE 2.0 MIGRATION DECISION**.
- Date: 2026-10-03.
- Decision authority: ChatGPT / Architect / Mentor / Reviewer / Governance Design Authority.
- Recording engineer: Sol / Codex, authorized for this ADR registration recording only.
- Verification Engineer: User; the User owns PowerShell, project execution and verification, and ALL Git writes.
- Accepted inventory/design review: `PASS_GOVERNANCE_2_0_AGENTS_MIGRATION_DESIGN_READY_FOR_ARCHITECT_ADJUDICATION`.
- Architect design decision being recorded: `PASS_GOVERNANCE_2_0_ARCHITECT_MIGRATION_DESIGN_ADJUDICATION`.
- Independent review: **PASS**, accepted token `PASS_GOVERNANCE_2_0_INDEPENDENT_ADR_REVIEW_READY_FOR_ARCHITECT_REGISTRATION`.
- Architect registration: **PASS**, decision token `PASS_ARCHITECT_GOVERNANCE_2_0_ADR_REGISTRATION`.
- Authorized action in this recording: modify this one existing ADR to record registration; no other file change.
- GOV2-G0: **COMPLETE**; GOV2-G1: **COMPLETE**; GOV2-G2: **NOT YET AUTHORIZED**.
- Migration execution: **NOT STARTED**; repository operating-policy activation / root cutover: **NOT PERFORMED / NOT AUTHORIZED BY THIS RECORDING**.
- Exact next recommended gate: **GOV2-G2 DIRTY/UNTRACKED RECOVERY AND EXACT MIGRATION SCOPE AUTHORIZATION**.

## 1. Decision status, scope, and effect

The Architect accepted the completed Governance 2.0 inventory/design review subject to the exact decisions recorded here. This ADR was created as a candidate, independently reviewed with PASS, and is now formally registered under `PASS_ARCHITECT_GOVERNANCE_2_0_ADR_REGISTRATION`. GOV2-G0 and GOV2-G1 are COMPLETE. GOV2-G2 is NOT YET AUTHORIZED; migration execution is NOT STARTED. Registration is separate from migration execution, repository operating-policy activation, and cutover.

**Until GOV2-G7 explicit Architect cutover, the existing root AGENTS.md remains authoritative.** This registered ADR governs HOW the future Governance 2.0 migration must be conducted: authority delegation, reading model, CURRENT_STATE, historical preservation, Git policy, Real/Simulation/Noop applicability, gate sequence, and cutover requirements. It does not replace root AGENTS as the repository operating entrypoint, incorporate new rule files, activate the future operating reading model, or establish a competing current-state authority. No destination governance files are created or made authoritative by this registration. Future destination documents remain non-authoritative migration candidates unless an explicit Architect decision says otherwise. Merely creating, linking, reviewing, registering, or publishing this ADR or a candidate does not perform cutover or authorize later gates.

Existing registered ADR authority, scope, and applicability remain unchanged. No roadmap, lesson order, technical architecture, Frozen Backbone, frozen predecessor, verification result, or publication model is amended by this recording.

The exact authorized path is:

```text
docs/architecture_decisions/ADR_GOV2_Governance_2_0_Agent_Instructions_State_and_History_Migration.md
```

No second file is authorized. No root AGENTS shortening, README reconciliation, destination folder creation, CURRENT_STATE, LEARNING_FLOW_MAP, rule file, history file, migration file, validator implementation, or recovery capture is authorized in this turn.

## 2. Baseline and accepted repository context

Repository:

```text
C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7
```

| Baseline item | Accepted identity / meaning |
| --- | --- |
| Branch | `main` |
| Verified pre-Governance-2.0 recovery commit | `e6a3a69e9274ba951e1eec507f3e367ebc4ada2a` |
| Checkpoint verification gate | `PASS_PRE_GOVERNANCE_2_0_CHECKPOINT_REMOTE_VERIFIED` |
| Reviewed root AGENTS physical lines | 5,625 |
| Reviewed root AGENTS bytes | 405,773 |
| Approximate root AGENTS size | 396.3 KiB |
| Reviewed root AGENTS SHA-256 | `01f981be0dfedd08484f9c19e67c6b4743e5cd5b2f7de150937fc877e95441ab` |

This hash identifies the reviewed migration source, not a semantic-fidelity certification. Before any future migration, confirm the actual source identity; unexpected drift requires reconciliation rather than silently using stale ranges or replacing the recorded hash.

The checkpoint is the committed recovery reference immediately before Governance 2.0. Do not modify, reset, restore, or clean working state to that reference. It does not capture existing unrelated dirty/untracked material; Section 19 defines the separate future recovery gate.

The repository learning-flow audit is formally closed under `PASS_ARCHITECT_REPOSITORY_LEARNING_FLOW_AUDIT_FINAL_CLOSURE`. LF-01 and LF-02 are closed. The accepted main line contains 62 lessons, the historical/parallel line contains 17, and the repository represents 79 lessons. D00 -> D01 is the historical/parallel Tank Drive lineage, not M00's predecessor.

Governance 2.0 is the current next major phase. M00 is the accepted software/architecture mechanism foundation, with no currently authorized editable lesson. Its accepted scope does not establish physical mechanism commissioning or competition readiness.

| Future-work item | Preserved boundary |
| --- | --- |
| T00 | PARKED / NOT REGISTERED / NOT ACTIVATED |
| M01 | NOT AUTHORIZED / NOT CREATED |
| Constants.java / configuration-authority cleanup | DEFERRED UNTIL Governance 2.0 IS COMPLETE |

No future work is activated by this ADR. Existing Phase-2 recorded limits, Phase-3 formal closure, ACM-01 through ACM-12 closure, and historical lesson/repair/publication qualifications remain preserved; Governance 2.0 does not authorize Phase 4 or M00_L17.

## 3. Problem, purpose, and transformation model

The reviewed root AGENTS mixes permanent rules, current cursors, navigation, audit evidence, repair chronology, closure/publication evidence, superseded states, and consumed authorizations. A heading labeled "Current" or an old approval token cannot by itself identify today's state or permission. Root context must make essential constraints visible without requiring reconstruction of all historical stages on every task.

**Governance 2.0 is a semantic-preservation migration. It is not a cleanup-by-deletion exercise.**

The approved transformation model is:

```text
CLASSIFY
-> SEPARATE
-> MOVE
-> LINK
-> VALIDATE
-> INDEPENDENTLY REVIEW
-> EXPLICIT CUTOVER
```

Preserve all operative governance meaning, current accepted state, historical evidence, lifecycle truth, chronology, and evidence limits. Classify by semantic purpose and accepted chronology rather than heading names or file position. The accepted A-J inventory distinguishes permanent rules, current state, current references, historical audits, historical repairs, historical closures/publications, superseded state, duplicated rules, derived knowledge, and uncertain material requiring adjudication.

Uncertain wording must not be silently resolved during extraction. A summary does not replace the source evidence. Relocating text is not automatically authority-neutral.

## 4. Future root AGENTS role and size

After an eventual approved cutover, root AGENTS is a **short, high-signal governance constitution and entrypoint**. It is not an audit database, publication log, lifecycle transcript store, test-result archive, or historical current-state stack.

Planning ranges are approximately **220-300 lines** and **18-28 KiB UTF-8**. They are not hard success criteria. Determine semantic completeness first; do not reduce protections to achieve a line or byte target.

Future root order should make identity/roles, authority/reading, current-state navigation, Frozen Backbone, curriculum/lifecycle, evidence, execution/Git ownership, protected scope, change control/HOLD, relevant document pointers, and truthful reporting directly discoverable.

Detailed event histories, transcripts, complete matrices, superseded cursors, publication sequences, consumed approvals, and revision-event logs must not again accumulate in root AGENTS. Root changes should concern the constitution, mandatory reading, or essential navigation.

## 5. Root semantic minimum — 40 required protections

The future root must directly expose enough information to protect an agent when only the entrypoint has initially loaded. Detailed references may expand obligations but cannot replace these minimum protections with bare links.

1. **Project identity and normative language:** identify FRC Java Coding Lab 7.0 and retain English normative governance; translations do not independently amend authority.
2. **Fixed roles:** ChatGPT is Architect/Mentor/Reviewer/Governance Design Authority; Sol/Codex acts within explicitly authorized implementation/review scope; the User is Verification Engineer and execution owner.
3. **Git-write ownership:** ALL Git writes remain User-owned; read-only inspection does not transfer that ownership.
4. **Authority order:** preserve root AGENTS -> Document A -> Document B -> Document C -> root README -> repository source code, with the delegated-rule contract in Section 6 and unchanged registered ADR applicability.
5. **Conflict / HOLD behavior:** apply existing higher-authority precedence; STOP/HOLD on conflicting governance or code/architecture requirements, and obtain governed reconciliation instead of silently rewriting authority.
6. **Mandatory reading contract:** root entrypoint and current state first after approved cutover, then the root reading matrix's required task-relevant documents before dependent work.
7. **PDF / VERIFIED-mirror trust and fallback:** English PDFs remain authoritative; only registered VERIFIED mirrors passing required integrity checks may satisfy permitted machine reading within fidelity limits; PDF fallback remains required for conflicts, ambiguity, hash/trust failures, fidelity/forensic requirements, and applicable poster visual meaning.
8. **Single current-state pointer:** identify `docs/governance/CURRENT_STATE.md` as the sole operational repository cursor after approved cutover, under Section 8's non-self-authorizing contract.
9. **Current versus historical precedence:** use accepted governing decisions and explicit supersession, not document position, a "Current" heading, or an old token; preserve earlier states as history.
10. **Both Frozen Backbone flows:** preserve Driver -> Xbox Controller -> controls -> commands -> subsystems -> IO -> hardware; and hardware -> IOInputs -> subsystem/estimator -> immutable Observation -> read-only telemetry -> NT4/Glass/log.
11. **RobotContainer:** composition root only; construction, selection, injection, bindings, and wiring do not confer control-policy or vendor-behavior ownership.
12. **Subsystem ownership:** subsystems retain their approved behavior, state, localization where applicable, semantic actuation, and stop ownership; no competing owner is introduced.
13. **Vendor-neutral IO contracts:** subsystems depend on approved IO contracts rather than concrete hardware adapters; vendor types do not cross the approved seam.
14. **Vendor API confinement:** hardware/vendor APIs remain inside approved concrete IO adapters, with physical-scope and Noop/Simulation applicability as stated in Section 13.
15. **IOInputs to immutable read models:** mutable one-cycle IOInputs transport remains subsystem-owned and must not escape as public retained state; project consumers receive approved immutable Observations/read models.
16. **Read-only telemetry:** telemetry publishes approved observations and cannot own control, schedule commands, mutate mechanism state, or become hidden policy authority.
17. **Scheduler-owned requirements:** command contention, requirements, and lifecycle remain scheduler-owned; history does not authorize manual child lifecycle or competing ownership arbitration.
18. **Semantic subsystem access:** commands use approved subsystem-semantic APIs; direct IO/vendor access or alternate hardware authority is forbidden outside an explicitly approved contract.
19. **Centralized safe stop:** preserve explicit subsystem stop/fail-safe obligations, interruption/mode-loss behavior, and no unintended restart; a software stop attempt is not proof of physical response after a device failure.
20. **One lesson / one project / one concept:** each lesson is an independent project with one new concept; approved reuse of mastered architecture does not mean one class/file/layer per lesson.
21. **Registered roadmap:** an approved, registered, applicable roadmap is required; a candidate filename or roadmap scope approval does not activate a lesson.
22. **Frozen-predecessor inheritance:** copy the correct approved complete/frozen predecessor through the governed workflow; do not rebuild from scratch, change lineage, or modify protected predecessors.
23. **Editable-lesson count:** at most one authorized lesson is editable; zero is valid when no lesson is active, including the present Governance 2.0 design stage.
24. **COMPLETE / FROZEN / READ-ONLY:** preserve frozen technical/content protection and separate narrowly authorized metadata or exceptional changes; an old frozen declaration does not prove a later repaired state is frozen.
25. **SUSPENDED:** preserve unfinished work exactly, neither COMPLETE nor FROZEN nor editable; resume only with explicit governance approval from preserved state unless separate reconciliation is approved.
26. **REOPENED / bounded repair:** REOPENED is provenance for IN_PROGRESS, not a generic lifecycle status; material post-freeze repair requires explicit Architect/User approval, exact scope, historical preservation, applicable verification, and explicit re-freeze.
27. **Separate authorizations:** roadmap, preparation/activation, implementation, repair, closure, freeze, publication, and subsequent work gates remain distinct; completion of one does not implicitly authorize another.
28. **Evidence vocabulary and applicability:** retain governed verification statuses and applicable THEORY VERIFIED, SIMULATION VERIFIED, REAL HARDWARE VERIFIED, REAL HARDWARE DEFERRED, and NOT APPLICABLE classifications; preserve freshness, provenance, limits, and missing evidence honestly.
29. **Real / Simulation / physical boundary:** tests, builds, Simulation, Noop observations, and software intent do not prove physical hardware behavior, commissioning, tuning, or safety margins.
30. **Exact authorized scope:** operate only within named actions/paths and approved boundaries; STOP/HOLD before expansion, including documentation or directory changes.
31. **Protected/unrelated worktree:** preserve existing drafts, candidates, practice projects, untracked material, and unrelated modifications; no unauthorized staging, clean, restore, move, delete, or normalization.
32. **Sensitive files:** NEVER open/read `github-recovery-codes.txt`; avoid the protected hardware registry unless strictly required and authorized; retain truthful access reporting and historical incident disclosure.
33. **Change control / exceptions:** retain reason, scope, impact, decision, authority, and migration/verification requirements; scope exceptions do not create a general refactor or architecture permission.
34. **Required lesson records:** maintain applicable lifecycle documents and the required transition guide before closure; documentation/evidence gates cannot be replaced by a build PASS.
35. **Stop on failure:** preserve ordered fail-stop verification/workflow discipline; failed/missing required gates leave the dependent claim on HOLD.
36. **Truthful final reporting:** distinguish observed facts, accepted User execution, historical evidence, inference, applicability, and actions actually performed; do not invent counts, exit codes, hashes, publication, or hardware PASS.
37. **Relevant roadmap / ADR / lesson reading:** read the matrix-required applicable Documents A/B/C, registered roadmap, ADRs, lifecycle records, and relevant source/tests/evidence before lesson design, implementation, or review.
38. **No historical permission reuse:** history, consumed approvals, candidates, parked work, and superseded states cannot supply fresh activation, implementation, repair, or publication permission.
39. **External-input exception:** preserve and make discoverable the approved coherent external human/operator sample -> immutable vendor-neutral DriverInputObservation exception; it does not permit mechanism observations from controls, mutable/control-bearing models, or telemetry control.
40. **M00 bilingual documentation:** preserve applicable separate English/Vietnamese student-facing files with equivalent structure, identity, meaning, evidence, and architecture rules; English is normative and the rule remains discoverable after relocation.

## 6. Delegated rule authority — approved future contract

After eventual approved cutover, specific files under `docs/governance/rules/` MAY become binding extensions of AGENTS governance **only when root AGENTS explicitly incorporates them by exact path and delegated scope**. A file does not obtain authority because it exists, is linked in navigation, or appears in an integrity index. The future root must identify each incorporated rule file and scope explicitly.

Within this Governance 2.0 instruction layer:

- Root AGENTS is controlling.
- If root and an incorporated detailed rule appear inconsistent, STOP/HOLD and treat root as controlling pending Architect reconciliation.
- Delegated rules must not contradict authoritative Documents A/B/C.
- Migration must preserve pre-Governance operative meaning, subject only to the explicit Architect decisions recorded here; no unrelated policy change is permitted.
- Existing registered ADR authority and applicability remain unchanged.

Do not silently change roadmap or technical architecture authority through delegation. Future rules are not activated by this registered migration ADR.

## 7. Mandatory reading model — approved future contract

After approved cutover, normal startup loads:

1. Root `AGENTS.md`.
2. `docs/governance/CURRENT_STATE.md`.
3. Task-relevant documents required by the root reading matrix.

Detailed rules are task-scoped rather than all loaded on every task. Before lesson design, implementation, or review, the applicable required set still includes Documents A/B/C, registered roadmap, applicable ADRs, active lesson lifecycle documents, and relevant source/tests/evidence. Expand reading when another section may govern the decision. Historical event bodies are required when the task needs their history, not on every ordinary task.

Mirror validation, registered VERIFIED status, current source/mirror hash checks, fidelity limits, and authoritative PDF fallback remain intact. Existing policy controls reading until cutover; this registered migration ADR does not waive current prerequisites. The future exact reading matrix requires candidate review and explicit cutover approval.

## 8. CURRENT_STATE authority and update contract

Future `docs/governance/CURRENT_STATE.md` answers:

```text
WHAT IS TRUE NOW?
WHAT ACTION IS CURRENTLY AUTHORIZED?
```

It is an operational projection backed by accepted governance evidence, not a history log or independent source of roadmap, implementation, repair, or publication permission. Every state/permission claim must reference accepted governing decisions/evidence. It must never self-authorize its own changes.

A current-state change requires accepted Architect adjudication or another already-authorized governance gate, authorized recording scope, semantic consistency review, and User-owned Git publication. Sol/Codex may write a candidate update only when explicitly authorized. Candidate recording and completed publication must remain distinguishable; no pre-publication text may claim that its own future commit or push occurred.

There must be only ONE canonical repository current-state cursor after cutover. Root AGENTS and root README point to it rather than maintain competing tables. Retain endpoint, active lesson/NONE, current major phase, current authorized gate/scope, parked/deferred/prohibited work, immediately relevant evidence limits, qualified worktree notes when operationally needed, and links to detailed accepted evidence.

Keep lesson-primary, lesson-metadata, and repository-checkpoint identities distinct. A SHA's role and accepted verification govern its use; neither lexical order nor apparent recency establishes authority.

At an authorized state update, archive the prior state with provenance in the preserved event store, retain supersession links, and check consistency with the governing decisions and references. Historical tables, repair narratives, old matrices, transcripts, and obsolete pending cursors do not accumulate in CURRENT_STATE. Stale/conflicting/unbacked state is a HOLD requiring governed reconciliation.

## 9. Initial CURRENT_STATE projection — later creation only

When a later gate authorizes creation, include at least the following accepted projection, backed by its governing records:

| Item | Initial projection required by this design |
| --- | --- |
| Repository learning-flow audit | COMPLETE / CLOSED |
| Verified curriculum | 62 main-line + 17 historical/parallel = 79 represented lessons |
| Main flow | New WPILib Project -> S00 -> A00 -> A01 -> V00 -> M00_L16 |
| D00 -> D01 | Historical / parallel; NOT M00 predecessor |
| M00 | Accepted software / architecture mechanism foundation |
| Current major phase | Governance 2.0 |
| Active editable lesson | NONE |
| T00 | PARKED / NOT REGISTERED / NOT ACTIVATED |
| M01 | NOT AUTHORIZED / NOT CREATED |
| Constants/configuration cleanup | DEFERRED UNTIL Governance 2.0 completion |
| Pre-Governance checkpoint | `e6a3a69e9274ba951e1eec507f3e367ebc4ada2a` |

These are required design inputs, not a current-state file created by this ADR. Later recording must cite accepted closure/publication evidence and preserve its qualifications; do not infer physical mechanism performance from M00's accepted software foundation.

## 10. LEARNING_FLOW_MAP contract

Future `docs/governance/LEARNING_FLOW_MAP.md` records **derived accepted knowledge**. It does not authorize new lessons or amend roadmap authority. Curriculum navigation may point to the same artifact instead of duplicating its semantic source.

| Lineage | Inventory | Total |
| --- | --- | ---: |
| Main | S00 24 -> A00 4 -> A01 9 -> V00 9 -> M00 16 | 62 |
| Historical / parallel | D00 6 -> D01 11 | 17 |
| Represented lessons | Main plus historical / parallel | 79 |

The map must distinguish technical copy/inheritance, student concept progression, architecture learning progression, verification progression, historical/parallel lineage, reconstructed canonical lineage, and evidence/confidence limits. Exact lesson/directory identities, predecessor relationships, roadmap references, concepts, and evidence qualifications must remain traceable to accepted sources.

**D01 is NOT M00's predecessor.** Preserve LF-01/LF-02 repair traceability and the D01_L11 qualification `FINAL CLEAN BUILD: NOT ESTABLISHED IN RETAINED EVIDENCE`. Neither its historical COMPLETE/FROZEN declaration nor the earlier baseline/generic Build PASS establishes final clean-build verification. Do not reopen the closed learning audit or upgrade evidence through the map.

No LEARNING_FLOW_MAP is created in this turn.

## 11. Historical preservation and non-permission contract

No historical AGENTS evidence may be destroyed. Before root cutover, preserve:

1. An exact-byte original pre-migration AGENTS copy.
2. Its original hash.
3. Encoding/newline information.
4. Its relationship to the pre-Governance checkpoint.
5. A complete source map covering the original content.
6. Event provenance.
7. Chronology.
8. Findings.
9. Authorizations.
10. Repair gates.
11. Verification limits.
12. Closure decisions.
13. Publication identities and their roles.
14. Supersession links.
15. Remaining qualifications.

Historical event bodies remain **VERBATIM**. New explanations, migration metadata, and annotations remain outside the preserved event body. A concise summary cannot replace source evidence. Retain markers, tables, tokens, failures, stage-specific pending states, and distinctions between source facts, User execution, historical evidence, and inference.

Historical records preserve evidentiary and decision history. They do not provide fresh current permission. A consumed activation, repair, implementation, freeze, or publication authorization remains consumed. Superseded HOLD/PENDING/current-state snapshots remain historical. An agent must not reactivate work because an old token exists.

Use both exact-byte/source-range checks and semantic rule-coverage review. Recoverable bytes alone cannot prove that an operative rule remains binding and discoverable after relocation.

## 12. Approved target direction and event-store design

The approved design direction is below. Exact path authorization is still required before creation; this listing authorizes no directory or file creation in this turn.

```text
AGENTS.md
docs/governance/
  README.md
  CURRENT_STATE.md
  LEARNING_FLOW_MAP.md
  rules/
    READING_AND_AUTHORITY.md
    ARCHITECTURE.md
    LIFECYCLE.md
    WORKFLOW.md
    EVIDENCE.md
    CODE_CONVENTIONS.md
    PROTECTED_PATHS.md
  history/
    README.md
    originals/
      AGENTS.pre-governance-2.0.md
    events/
      range/event-preserved source records
    indexes/
      AUDITS.md
      REPAIRS.md
      CLOSURES.md
      PUBLICATIONS.md
  migration/
    SOURCE_MAP.md
    RULE_COVERAGE.md
    DECISIONS.md
docs/architecture_decisions/
  existing ADRs remain in place
```

Use a single preserved event store. Category indexes may point to the same event; do not split one event's body among category directories or duplicate it to satisfy multiple indexes. Preserve authorization -> repair -> verification -> closure -> publication relationships and distinct events within their chain. Stable source-range/event identifiers, original hashes, related gates, and supersession links permit navigation without loading all historical bodies on every task.

## 13. Git and Real / Simulation / Noop decisions

### Git ownership

The User owns ALL Git writes. In the normal project workflow, Sol/Codex must not perform `git add`, `git commit`, `git tag`, `git push`, `git restore`, `git checkout`, `git reset`, `git clean`, or equivalent Git mutations.

Task-relevant read-only Git inspection is permitted: `git status`, `git diff`, `git log`, `git show`, `git rev-parse`, `git ls-files`, and equivalent non-mutating inspection. This does not transfer Git ownership or allow claims of completed remote publication without accepted evidence. This turn's read-only inspection is explicitly permitted by the Architect/User request; recording this decision does not rewrite the old AGENTS text.

### Physical scope and deferred adapters

The architecture continues to require real hardware/vendor APIs behind approved concrete Real IO adapters when physical hardware scope has been established. A lesson/module may intentionally use Noop or Simulation adapters when its approved roadmap/governance explicitly defers physical hardware.

That is not an architecture defect when the IO contract remains vendor-neutral, subsystem architecture remains correct, evidence is honestly labeled, REAL HARDWARE DEFERRED is explicit where applicable, and no physical behavior is claimed from Noop/Simulation evidence.

This preserves the accepted M00 software/mechanism foundation. Governance 2.0 must not retroactively require physical M00 mechanism implementation. Software tests, Simulation, requested-state telemetry, or an invalid Noop zero do not certify physical response, tuning, commissioning, or game-piece transfer.

## 14. Duplication and historical annotation policy

Do not mechanically eliminate every duplicated rule. Classify duplication before migration. Intentional safety duplication may remain to protect partially loaded agents. Detailed procedures should have one canonical delegated rule where appropriate, while root retains concise critical invariants.

Documents A/B/C remain authoritative under existing governance. Registered ADRs retain their paths, scope, and applicability. Derived maps and summaries do not amend those sources. Canonicalization requires semantic comparison, including exceptions and evidence limits; a shorter summary is not automatically equivalent.

Do not edit historical event bodies to "fix" old wording. Clarifications must preserve the body verbatim, attach separate metadata/annotation, identify the source range and superseding/current interpretation, and retain chronology. This particularly applies to uncertain historical wording. No annotation may quietly convert an old failure into PASS or an old permission into current authorization.

## 15. Root README and governance manifest policy

A later authorized gate may reconcile root README to emphasize project purpose, student navigation, curriculum orientation, concise safety/governance summaries, and links to canonical governance/current-state/map sources. It must not maintain a competing current-state cursor. Relocating historical README material requires separately authorized source preservation. No README change is authorized here.

`docs/GOVERNANCE_DOCUMENT_MANIFEST.md` remains dedicated to the controlled PDF / VERIFIED-mirror relationship unless a future approved policy explicitly expands its schema. This ADR does not turn it into a general governance-file registry or confer authority through manifest inclusion. Governance 2.0 initially uses its own explicit migration coverage/integrity records. No manifest, source PDF, VERIFIED mirror, hash/trust-state, or validator configuration change is authorized here.

## 16. Tool-neutral consumption

The semantic governance source is shared across ChatGPT as Architect/Mentor and authorized Codex/Sol or possible future implementation/review agents. A future tool-specific entry adapter, if separately approved, must point to the same authority, current state, reading matrix, and evidence; it must not contain competing policy or state.

No CLAUDE.md or other adapter is authorized or created by this recording. No delegated rule gains authority from a particular tool's discovery behavior. Candidate labeling and explicit incorporation must remain understandable outside one agent runtime.

## 17. Deterministic validation direction

Future validators should check machine-verifiable invariants including:

- Root/current-state/rule pointers resolve to approved paths.
- Current-state evidence references exist.
- Authorized editable-lesson count is valid; zero is allowed and suspended/candidate work is not silently counted as editable.
- Parked candidates are not registered.
- Roadmap identity/order/count matches registered authority.
- Required lifecycle documents and transition guides exist.
- Frozen changes correspond to approved exception scope.
- Migration source coverage is complete.
- Original AGENTS hash and byte recovery are complete.
- Rule coverage is complete.
- Protected paths do not enter a proposed staging set.
- Existing PDF/mirror validation remains intact.

Architecture scans may flag suspicious vendor placement or ownership patterns, but cannot replace semantic architecture judgment. Validators must distinguish historical/candidate content from current operative state rather than count every old IN_PROGRESS string or token as current authority.

No validator is implemented or amended by this ADR. Existing deterministic mirror preflight PASS is not semantic-fidelity certification or proof of all Governance 2.0 invariants.

## 18. Independent human / Architect review

Human/Architect review remains required for authority equivalence, exception applicability, one-concept scope, scheduler ownership semantics, sole drivetrain/localization authority, evidence sufficiency, current-versus-historical precedence, bilingual semantic equivalence, and semantic preservation of summarized rules.

Independent review must evaluate candidate destination documents and the proposed replacement root before old AGENTS is replaced. Exact byte preservation, hashes, pointer checks, and line-count reduction do not by themselves prove correct authority delegation, sufficient reading, or preserved operative meaning. Missing evidence, unexplained scope drift, or competing current authority requires HOLD and governed reconciliation.

## 19. Dirty / untracked recovery prerequisite

The committed checkpoint protects committed repository state, not existing unrelated modified/untracked material. **Before GOV2 file migration begins beyond ADR/design recording, the User must perform a separately authorized, non-destructive recovery capture.**

The recovery gate must preserve the tracked A01_L06 draft, curriculum drafts, practice projects, parked T00 candidate, and other unrelated untracked work. It must avoid opening the sensitive recovery-code file and must not alter, clean, restore, or normalize the worktree. A recovery plan must preserve applicable protected state without converting backup into unauthorized reading, publication, or activation.

Known protected examples include:

```text
real_robot_programming/module_A01/A01_L06_PathPlannerPathAndRuntimeIntegration/src/main/deploy/pathplanner/paths/A01_L06_OneMeter_Forward.path
PathPlanner_Practice_2026/
curriculum/FRC_Robot_Programming_For_Dummies/
docs/Robot_Hardware/FRC_Robot_CAN_Address_Allocation_Registry.md
docs/architecture_decisions/ADR_T00_Swerve_Characterization_and_Closed_Loop_Tuning_Roadmap.md
docs/tools/governance/__pycache__/
docs/tools/governance/tests/__pycache__/
org/
github-recovery-codes.txt
```

Do not stage, clean, restore, move, delete, or normalize these items. Avoid opening/reading the hardware registry unless strictly required; it is not needed for this recording. NEVER open/read `github-recovery-codes.txt`. Preserve the previous registry search-scope incident truthfully as historical evidence; do not rewrite it as "never accessed."

No recovery capture or inspection of sensitive/registry content occurs in this turn.

## 20. Exact migration gate sequence

| Gate | Required action / exit condition | State at this registration recording |
| --- | --- | --- |
| GOV2-G0 | Complete inventory/design review | COMPLETE under the accepted inventory/design token |
| GOV2-G1 | Architect architecture/delegation/state-policy adjudication | COMPLETE; independent ADR review PASS and Architect registration PASS under the accepted tokens recorded above |
| GOV2-G2 | Exact migration-path/scope authorization plus non-destructive User recovery capture of existing dirty/untracked state | NOT YET AUTHORIZED / NOT PERFORMED; separate authorization/evidence required |
| GOV2-G3 | Create NON-AUTHORITATIVE destination candidates while old AGENTS remains authoritative | NOT PERFORMED / NOT AUTHORIZED BY THIS RECORDING |
| GOV2-G4 | Populate archive/event store/source map/rules/current-state/learning-map candidates | NOT PERFORMED / NOT AUTHORIZED BY THIS RECORDING |
| GOV2-G5 | Verify complete byte recovery, complete source-range coverage, semantic rule coverage, chronology, references, and evidence qualifications | NOT PERFORMED; candidate evidence required |
| GOV2-G6 | Independent review of all candidate destinations and proposed replacement root AGENTS | NOT PERFORMED; must precede cutover |
| GOV2-G7 | Explicit Architect CUTOVER authorization; only here apply approved root replacement and approved README reconciliation | NOT PERFORMED / NOT AUTHORIZED; no competing current-state authorities after cutover |
| GOV2-G8 | Post-cutover integrity/loading/semantic review | NOT PERFORMED |
| GOV2-G9 | User-owned Git checkpoint/publication | NOT PERFORMED; ALL Git writes remain User-owned |
| GOV2-G10 | Independent external/post-publication verification | NOT PERFORMED |

No later gate is implicitly authorized by completion of an earlier one. Independent review and Architect registration of this ADR are complete. The immediate next gate is GOV2-G2 DIRTY/UNTRACKED RECOVERY AND EXACT MIGRATION SCOPE AUTHORIZATION; identifying that gate does not authorize its execution or destination creation.

## 21. Recording boundary and next gate

This registered ADR records approved future architecture and migration policy, the accepted independent-review PASS, and Architect registration PASS. Old AGENTS remains authoritative. GOV2-G0 and GOV2-G1 are COMPLETE; GOV2-G2 is NOT YET AUTHORIZED; migration execution is NOT STARTED. No Governance 2.0 migration, destination creation, root cutover, README/manifest reconciliation, history extraction, backup, validator implementation, lesson activation, Constants cleanup, T00 advancement, M01 creation, or repository operating-policy activation occurs.

The candidate ADR was created at the preceding recording gate. Exactly this existing ADR is modified for registration; its migration architecture and GOV2-G0 through GOV2-G10 gate definitions remain unchanged. AGENTS, root README, governance manifest, Documents A/B/C, other ADRs, lesson documents, source/tests, Constants.java, T00 candidate, curriculum drafts, hardware registry, and all unrelated/protected material remain unchanged by this recording. Active editable lesson remains NONE; T00 remains PARKED / NOT REGISTERED / NOT ACTIVATED; M01 remains NOT AUTHORIZED / NOT CREATED; Constants cleanup remains DEFERRED UNTIL Governance 2.0 COMPLETE.

No Gradle, tests, build, Simulation, Glass, Driver Station, hardware, or SysId execution occurs. Only permitted static documentation inspection, required non-writing mirror validation/integrity checks, bounded ADR registration recording, and read-only Git inspection are performed. No Git write, commit, tag, push, reset, restore, checkout, or clean occurs. No new publication identity or new independent review performed during registration is claimed; the preceding accepted independent-review PASS is recorded.

Hardware registry: NOT ACCESSED / NOT MODIFIED in this recording.

github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED

**Exact next recommended gate: GOV2-G2 DIRTY/UNTRACKED RECOVERY AND EXACT MIGRATION SCOPE AUTHORIZATION.**
