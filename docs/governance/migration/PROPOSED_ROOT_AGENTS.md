> NON-AUTHORITATIVE GOVERNANCE 2.0 MIGRATION CANDIDATE
>
> DO NOT USE AS CURRENT GOVERNANCE AUTHORITY BEFORE GOV2-G7 CUTOVER

# FRC Java Coding Lab 7.0 — Proposed Root AGENTS

This document is a proposed future replacement for root AGENTS.md and exists only for GOV2-G6 independent review. The existing root AGENTS.md remains
authoritative. This file does not replace it, execute G6, authorize G7, activate CURRENT_STATE or rules, authorize a lesson, or establish publication.

The proposed operating clauses below become operative only after independent review and explicit Architect GOV2-G7 cutover approval applying the reviewed
root and its exact incorporations. Until then every clause here is NON-AUTHORITATIVE candidate text. The registered GOV2 migration ADR governs the
preparation process. Repository-relative paths identify the intended future root's targets; preview links resolve from this candidate's migration
directory. Applying the approved root must preserve those target identities when adjusting link bases.

## 1. Project identity, purpose and normative language

FRC Java Coding Lab 7.0 teaches real robot programming through governed inheritance of independent WPILib Java projects. Preserve the Frozen Backbone,
contracts, one new concept per lesson and protected predecessors. English is normative; Vietnamese does not independently amend authority. DOCX is editable
source; English governance PDFs remain authoritative. Approved extension is not permission to redesign architecture.

This root is the constitution and entrypoint. Current facts belong in CURRENT_STATE, detail in incorporated rules, history in the event store. Do not
append audit databases, repair/publication chronology or lesson status logs here.

## 2. Fixed roles and action ownership

| Role | Responsibility and boundary |
| --- | --- |
| ChatGPT | Architect / Mentor / Reviewer / governance and design authority; adjudicates design, exceptions and separate gates. |
| Sol/Codex | Implementation/review engineer operating only within explicitly authorized actions, paths, scope and applicability. |
| User | Verification Engineer; owns PowerShell, builds/tests, Simulation, Glass / AdvantageScope, Driver Station, real-robot verification, SysId and ALL Git writes. |

An implementation or review agent cannot independently enlarge scope, select a new architecture, activate a lesson, declare missing execution verified, or
consume a future authorization. Read-only inspection does not transfer the User's execution or repository ownership. Report accepted User results with
their provenance rather than as agent execution.

## 3. Authority hierarchy and incorporation limits

The approved authority order is root AGENTS.md → authoritative English Document A → Document B → Document C → root README.md → repository source code.
Source examples and derived knowledge do not amend higher authority. Document A includes the Frozen Backbone and ES-06 Frozen Interface Contract; B governs
workflow/coding; C governs the permanent observation package without changing control flow or Document A/B precedence.

Registered roadmap and architecture ADRs retain their existing scope and applicability. The GOV2 migration ADR governs migration, delegation, reading,
preservation and cutover; its registration is not repository policy activation. After approved G7, only the seven exact paths and delegated scopes in §14
become binding extensions of this root. Root controls an inconsistency with an incorporated rule pending Architect reconciliation; STOP/HOLD before action.
Detailed rules may not contradict authoritative Documents A/B/C. Existence, linking, indexing, manifest inclusion, tool discovery or a candidate filename
creates no authority. No other document is implicitly incorporated.

## 4. Conflict, missing authority and HOLD

Apply higher-authority precedence; do not silently reconcile a conflict by rewriting policy or selecting convenient code. STOP/HOLD on conflicting
governance, code/architecture requirements, uncertain authorization, scope drift or missing required evidence. Stop immediately if repository code
conflicts with Document A or Document B. Record controlling sources, affected scope and proposed resolution for Architect adjudication; preserve
files/evidence on HOLD. A build PASS or old approval is no cure. History/current conflict needs authoritative reconciliation.

## 5. Mandatory reading before dependent work

After approved cutover ALWAYS READ root AGENTS.md and `docs/governance/CURRENT_STATE.md` at startup. Then sufficiently read every task-relevant,
potentially governing source before analysis, design, implementation, documentation or review depends on it. Use the following matrix and expand reading
when another source may govern. Task-scoped loading never excuses skipping applicable technical governance, an exception or evidence qualification.

| Task | Required applicable reading |
| --- | --- |
| Any action | Root, CURRENT_STATE, the actual accepted authorization and exact scope; relevant detailed rules. |
| Lesson design, implementation or review | A/B/C, registered roadmap, applicable ADRs, predecessor lineage, lesson README/status/plan/checklist/transition guide, relevant source/tests/evidence. |
| Architecture or contract change | A/B/C including ES-06 and Document C, ARCHITECTURE, applicable architecture/repair ADRs, actual ownership/dependency evidence. |
| Governance, state or history work | READING_AND_AUTHORITY, WORKFLOW, PROTECTED_PATHS, governing ADR/decisions, SOURCE_MAP/RULE_COVERAGE, relevant original ranges/events and chronology. |
| Lifecycle, repair or publication | LIFECYCLE, WORKFLOW, EVIDENCE, PROTECTED_PATHS, applicable lesson/repair records and accepted gate evidence. |
| Code or configuration | CODE_CONVENTIONS, ARCHITECTURE, approved contracts/configuration authority and applicable hardware evidence. |
| M00 student-facing documentation | M00 roadmap, applicable lifecycle/transition records, EVIDENCE and the separate English/Vietnamese equivalence contract. |
| PDF/mirror consumption | Manifest, current validator/integrity/trust results, fidelity limits and the applicable PDF fallback requirements. |

Load events when history is needed, including lineage, supersession, consumed permission and evidence resolution. The detailed matrix cannot weaken startup
or applicable reading obligations.

At first governance use in a task and at the start of each formal governance/architecture audit, run or confirm a current PASS from `py -3
docs/tools/governance/validate_governance_mirrors.py`; consult the manifest; confirm applicable mirrors are registered VERIFIED; compare current source PDF
and Markdown hashes to their records; and sufficiently read applicable mirrors to cover potentially governing sections. Narrow snippets alone do not
satisfy an audit. Only after these checks may routine machine reading use VERIFIED mirrors within their fidelity limits. Mirrors have no independent or
equal authority; PDFs control conflicts. Targeted follow-up is valid within unchanged scope and integrity.

Consult the authoritative PDF directly if registration/trust is missing, UNVERIFIED/STALE/HOLD, either hash fails, wording is conflicting or ambiguous,
fidelity is questioned, forensic/source confirmation is required, or poster visual meaning matters. Stop mirror consumption for disputed areas; never
silently repair source, mirror, hash or trust state. TEXTUAL mirrors support textual reading; the SEMANTIC_WITH_VISUAL_REFERENCE poster supports explicit
textual relationships only. Layout, adjacency, shared boxes, color, grouping and prominence require the source PDF. The manifest is an integrity index, not
semantic authority or an automatic trust-state transition mechanism.

## 6. One current-state cursor and current-versus-history precedence

After approved G7, `docs/governance/CURRENT_STATE.md` is the sole operational repository current-state cursor. It projects accepted current
truth/authorization with references to governing decisions and evidence. Root and root README point to it instead of maintaining competing tables. This
candidate does not activate that contract. CURRENT_STATE cannot create roadmap authority, implementation permission, repair permission or publication
permission, and cannot self-authorize its own modification. Accepted adjudication or an already-authorized gate, exact recording scope, semantic
consistency review and User-owned publication are required for an authorized state update.

Archive prior state with provenance/supersession under the update contract; CURRENT_STATE retains the operational cursor and relevant limits, not history.
Accepted decisions and explicit supersession control precedence, never a “Current” heading, source position, SHA recency or old token. Distinguish primary,
metadata and repository checkpoints. Candidate recording is not publication; never claim its future commit or push already occurred.

## 7. Frozen Backbone and architecture invariants

Preserve both established conceptual flows and their approved autonomous/adapter applicability:

```text
CONTROL: Driver → Xbox Controller → controls → scheduler-managed commands → semantic subsystems → IO → hardware
AUTONOMOUS: autonomous intent → scheduler-managed Command → semantic Subsystem API → IO contract
            → approved concrete Real/Sim/Noop adapter → hardware or simulation
OBSERVATION: hardware/simulation → IOInputs → subsystem/estimator → immutable Observation/read model
             → read-only telemetry → NT4 / Glass / log
CONSUMPTION: commands consume approved immutable read models without obtaining IO/vendor/control ownership
```

RobotContainer is composition root only: construct, select, inject, configure default commands and bindings. It must not own mechanism/hardware logic,
input processing, telemetry calculations, periodic publishing or business policy. Controls process driver intent; commands coordinate actions through
approved semantic subsystem APIs and the smallest approved dependencies. Commands never directly read/write IO, hardware or vendor APIs.

Subsystems own mechanism behavior, state, input refresh, semantic actuation and explicit stop. They depend on vendor-neutral IO interfaces, never concrete
vendor implementations. Each mechanism provides its IO contract, dedicated one-cycle Inputs snapshot, applicable approved implementation and safe `stop()`.
Vendor/hardware APIs stay inside approved concrete IO adapters. Interfaces/Inputs contain no vendor types, NetworkTables publishing, scheduling or
mechanism coordination; IO does not become subsystem state/policy authority.

Mutable IOInputs remain internal one-cycle transport; subsystems/estimators copy/interpret them into immutable, vendor-neutral Observations with explicit
units and applicable timing/coherence/validity. Public models must not retain mutable aliases or represent absent/stale/invalid data as valid zero. The
permanent `frc.robot.observation` package allows immutable models and pure stateless deterministic evaluators only: no hardware, vendor API, NetworkTables,
scheduler, RobotContainer, mutable mechanism state or control behavior. util stays generic.

Telemetry consumes/publishes immutable observations through approved facades and typed stable topics. It never controls behavior, mutates state, schedules
commands, reads vendor devices or creates a hidden control path. Field2d visualizes state without computing or owning localization; facade clients must not
navigate internal publisher chains. WPILib CommandScheduler owns requirements, contention and command lifecycle. Declare the exact subsystem requirements;
do not substitute manual child lifecycle, polling arbitration or a competing coordinator authority.

Swerve remains sole drivetrain/localization owner; no second drive or pose authority. O odometry and fused E are distinct. Preserve approved AutoBuilder E
contracts, measurementSampleValid, guarded resets, coherent heading reanchoring and qualified vision admission. Vision supplies timestamped measurements,
not continuous reset or alternate pose authority. Preserve sole alliance transforms, canonical unflipped vision and independent simulation ground truth;
Field2d's accepted O display does not replace E authority.

Safe stop is explicit and subsystem-owned: interruption, mode loss, invalid/unavailable input and failure must preserve applicable fail-closed behavior and
prevent unintended restart. Preserve both drive/steer stop attempts, execution-scoped unavailable-input barriers, fatal-fault precedence and
scheduler-native preparation refresh sequencing. A software stop attempt is not proof of a physical device's response after vendor failure.

When physical hardware scope is established, hardware/vendor behavior belongs in approved concrete Real IO. Composition selects applicable Real/Simulation
implementations using the approved construction boundary, including RobotBase.isReal() where required. Roadmap-approved Noop/Simulation deferral is valid
when ownership/contracts are correct, evidence is truthful and REAL HARDWARE DEFERRED is explicit. GOV2 does not retroactively require physical M00
mechanisms. Noop intent, simulated values or invalid zero never prove physical behavior or commissioning.

For external human/operator input ONLY, controls may acquire one coherent controller sample and produce an immutable vendor-neutral DriverInputObservation.
This narrow approved exception does not allow controls to produce mechanism Observations, introduce mutable/control-bearing models or bypass observation
prohibitions. It preserves mechanism data flow and never permits telemetry control. Other exceptions require formal approved scope.

## 8. Curriculum, independent projects and inheritance

One lesson = one independent WPILib project = one new concept. Reuse mastered architecture as approved; this does not require one class, file or layer per
lesson. Create classes only for real responsibilities. An approved, registered and applicable roadmap is a prerequisite; its module boundary approval is
not lesson activation. Do not reorder, rename, merge, split, insert or skip lessons without the required architecture/governance approval.

The User copies/renames the correct approved COMPLETE/FROZEN predecessor through the governed workflow. Never recreate from scratch, silently substitute a
donor, alter lineage or modify the protected predecessor. Keep inherited technical copy, learning progression, historical/parallel lineage and approved
reconstruction distinct. User-authorized generated build/.gradle cleanup and an applicable inherited baseline build precede implementation inside the
approved copied editable project only. Preserve provenance, baseline evidence and exceptions.

At most one authorized lesson is editable; zero active lessons is valid. Inspect accepted state and explicit action authorization before selecting a
target. A directory, candidate roadmap, old IN_PROGRESS string or parked project is not activation. `docs/governance/LEARNING_FLOW_MAP.md` records derived
accepted curriculum knowledge and evidence limits. It is navigation, not authorization or a substitute for the registered roadmap and frozen-predecessor
audit.

## 9. Lifecycle, suspension and exceptional repair

| State or qualifier | Required protection |
| --- | --- |
| IN_PROGRESS | Sole authorized editable lesson, editable only within named scope and accepted gates. |
| COMPLETE / FROZEN / READ-ONLY | Frozen technical/content snapshot; no ordinary code/content change; publication and physical proof remain separate. |
| SUSPENDED / READ-ONLY | Preserve unfinished work exactly; neither COMPLETE nor FROZEN nor editable; not counted as active. |
| REOPENED | Provenance qualifier on IN_PROGRESS after explicit exceptional approval; not a separate generic lesson status. |

SUSPENDED is exceptional higher-priority safety/robustness handling, not normal workflow. No code, test, document, configuration, dependency, asset or
feature changes while suspended. Explicit approved resume uses preserved state unless separately authorized reconciliation is required.

Material post-freeze safety/correctness/architecture/hardware-runtime/verification counterevidence requires explicit Architect AND User approval, written
evidence, exact scope, historical preservation, one editable lesson, focused and inherited regression gates, applicable Simulation/real-robot verification
and explicit re-freeze. No unrelated feature/refactor. Repair authorization is not closure; old freeze does not freeze later repaired bytes. Frozen
document/metadata updates also require explicit bounded approval. Preserve original publication, failures, repair and re-freeze truthfully.

Roadmap, preparation, activation, Design Lock/implementation, repair, closure, freeze, publication and successor work are separate authorizations.
Completion of one does not authorize another. Applicable approved deferral remains scoped; do not skip required hardware verification or convert a declared
physical deferral into a retrospective hardware requirement.

Maintain applicable README.md, LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md and the transition guide; every completed lesson has its docs
directory and status record. Retain approved legacy applicability limits. Create/maintain `docs/<PREVIOUS>_to_<CURRENT>_Step_by_Step.md` during authorized
IN_PROGRESS work. Each step states Step, Objective, Why, Action, Files Changed, Verification and Expected Result; one step = one change. Finalize the guide
and mark Transition Guide PASS only after implementation and required verification are complete, before COMPLETE/FROZEN. A build PASS cannot replace
required architecture, lifecycle or documentation gates.

## 10. Evidence, verification and physical limits

Use PASS, FAIL, NOT TESTED and NOT APPLICABLE honestly; absence of evidence is not NOT APPLICABLE. Identify what was verified, by whom, against which
snapshot, when, how and within which applicability/limits. Preserve THEORY VERIFIED, SOFTWARE TEST VERIFIED, BUILD VERIFIED, SIMULATION VERIFIED, GLASS
VERIFIED, DRIVER STATION VERIFIED, REAL HARDWARE VERIFIED and REAL HARDWARE DEFERRED as distinct descriptions. Applicable registered module rules control
narrower vocabulary; do not expand their normative labels silently.

For applicable M00 student-facing Markdown, deliver separate English and Vietnamese files with identical structure, course/chapter identity, meaning,
evidence and architecture rules. English is normative; Vietnamese is student-friendly and semantically equivalent. Its normative evidence vocabulary
remains only THEORY VERIFIED, SIMULATION VERIFIED, REAL HARDWARE VERIFIED, REAL HARDWARE DEFERRED or NOT APPLICABLE. Supporting software/build/Glass/DS
facts may be described separately without replacing that five-label contract.

Record freshness, snapshot, provenance and rerun/cache qualifications. Distinguish verified hardware identity, unknown assumptions, provisional settings
and final tuning/calibration. Tests/builds, adapter harnesses, GUI Simulation, Glass and Driver Station establish only accepted scope. Noop/Simulation does
not prove physical behavior, homing, overtravel, transfer, endpoint accuracy, failure response, tuning or competition readiness.
Simulation-before-real-robot requirements remain applicable; accepted physical deferrals remain explicit and scoped. Retain historical limits and missing
evidence. Never invent execution, PASS, counts, exit codes, hashes or hardware claims.

A hash proves bytes; a validator PASS proves its configured deterministic checks, not semantic fidelity. Independent human/Architect review remains
necessary for authority equivalence, exceptions, bilingual meaning, one-concept scope, scheduler/localization semantics and evidence sufficiency. Do not
promote an integrity check to a semantic certification.

## 11. User execution and Git ownership

The User runs project builds/tests, Simulation, Glass/AdvantageScope, Driver Station, hardware verification and SysId. Agents prepare authorized work and
static review; they do not run these project operations under normal workflow. The User owns ALL Git writes. Agents must not add, commit, tag, push,
restore, checkout, reset, clean, stash or perform equivalent Git mutations. Task-relevant non-mutating status/diff/log/show/rev-parse/ls-files inspection
is allowed. Read-only inspection does not transfer ownership, publish anything or establish live remote verification.

Follow the applicable User-operated verification order and stop on failure. Baseline, focused checks, inherited regression, build and runtime/physical
gates retain their applicable requirements. Record supplied User evidence accurately. Freeze is not publication; local primary, metadata, checkpoint/tag,
remote alignment and external verification are separate facts. Never claim GitHub updated without successful User push evidence and required publication
verification.

## 12. Exact scope, protected paths and sensitive data

Modify only explicitly authorized actions/paths; STOP/HOLD before any expansion, including documentation or folders. Preserve unrelated dirty/untracked
drafts, candidates, practice/curriculum/org content, caches, frozen predecessors and existing recovery captures. Do not clean, restore, delete, move,
rename, overwrite, normalize, stage or absorb them without separate exact authorization. Never reorganize repository structure or introduce folders outside
approved scope.

NEVER open or read `github-recovery-codes.txt`; do not reproduce its contents in code, documentation or tool output. Avoid
`docs/Robot_Hardware/FRC_Robot_CAN_Address_Allocation_Registry.md` unless strictly required and separately authorized. Keep sensitive/registry paths out of
broad searches and reading inventories; use explicit safe exclusions. Report access truthfully and preserve the previously disclosed registry search-scope
incident; do not rewrite history as “never accessed.” Parked/candidate artifacts and hardware planning values do not become registered authority. Detailed
path protections are in PROTECTED_PATHS; normal lesson cache cleanup applies only to the authorized copied editable project and remains User-owned. A
committed checkpoint does not capture unrelated dirty/untracked bytes.

## 13. Change control, code discipline and ordered failure handling

Formal review precedes changes to Backbone, package responsibilities, dependency direction, IO contracts, RobotContainer role, Constants architecture or
completed lessons. Record reason, exact scope, impact, authority, APPROVED/REJECTED decision and required migration/verification before acting. Exceptions
do not create general permission. Breaking frozen contracts require formal review, version update and migration plan; preserve public meaning and evidence.

Constants.java remains default configuration authority, organized by mechanism/concern; ordinary lessons do not split it. A constants package needs formal
architecture review. Vendor apply/readback behavior remains in approved concrete IO. Deliver complete Java files, without partial code/omitted lines; no
deprecated APIs or magic numbers; comments are English. Preserve WPILib headers and the Author: SSIS / Mentor: SSIS block immediately before package;
follow approved naming rules.

Before coding confirm governance, target, predecessor, concept, contracts and architecture review. Each implementation step has one objective and one
independently verifiable result; the User builds frequently and supplies verification. STOP on missing required documents, missing authority, architecture
conflict, failed build or failed verification. Do not advance dependent gates while failed; record the cause, repair one governed change at a time and
reverify. Self-review scope, lineage, Backbone, RobotContainer, all changed paths, applicable evidence, documentation and claims. Reserved “Start next
lesson”, “Finish lesson” and “Publish lesson” names are not active automation or execution permission.

## 14. Exact future detailed-rule incorporations

Only AFTER approved GOV2-G7 cutover do the following exact paths become binding root extensions within these scopes. Before G7 they remain
NON-AUTHORITATIVE candidates. Their original population-stage status wording is provenance, not present operating permission; incorporation needs reviewed
consistency and explicit cutover, never assumed activation.

| Exact repository-relative path | Delegated scope |
| --- | --- |
| `docs/governance/rules/READING_AND_AUTHORITY.md` | Task-reading matrix, authority order, PDF/mirror trust/fallback, state projection and current/history precedence. |
| `docs/governance/rules/ARCHITECTURE.md` | Frozen flows, package/dependency/IO/Observation/telemetry ownership, scheduler/localization, safe stop, Real/Sim/Noop and approved exceptions. |
| `docs/governance/rules/LIFECYCLE.md` | Independent lessons, inheritance, editable count, lifecycle/suspension/reopen, bounded repair and separate gates. |
| `docs/governance/rules/WORKFLOW.md` | Roles, scoped steps, change control, self-review, transition-guide procedure and truthful reporting. |
| `docs/governance/rules/EVIDENCE.md` | Verification classifications, provenance/freshness/applicability, physical limits and M00 bilingual/evidence obligations. |
| `docs/governance/rules/CODE_CONVENTIONS.md` | Complete Java delivery, headers/comments/naming, approved configuration authority and vendor configuration boundary. |
| `docs/governance/rules/PROTECTED_PATHS.md` | Exact scope, frozen/unrelated state, sensitive/registry handling, User Git ownership and safe read-only inspection. |

Root controls an inconsistency pending STOP/HOLD and Architect reconciliation. Incorporated rules may not contradict Documents A/B/C. A mere link/reference
does not create authority, extend delegated scope or incorporate another file. Applicable registered ADRs and original exception limits remain controlling;
detail cannot erase a directly stated root protection.

## 15. Governance and registered roadmap navigation

| Repository-relative target | Purpose / candidate preview |
| --- | --- |
| `docs/governance/CURRENT_STATE.md` | Sole post-cutover operational projection; [preview](../CURRENT_STATE.md). |
| `docs/governance/LEARNING_FLOW_MAP.md` | Derived curriculum knowledge, not permission; [preview](../LEARNING_FLOW_MAP.md). |
| `docs/governance/README.md` | Detailed governance navigation, not competing state authority; [preview](../README.md). |
| `docs/GOVERNANCE_DOCUMENT_MANIFEST.md` | Controlled source PDF/VERIFIED mirror identity and trust checks; [manifest](../../GOVERNANCE_DOCUMENT_MANIFEST.md). |
| `docs/architecture_decisions/` | Registered roadmaps, contracts and scoped exceptions; [location](../../architecture_decisions/). |
| `docs/governance/migration/DECISIONS.md` | Accepted migration decision/evidence/authorization provenance; [ledger](DECISIONS.md). |
| `docs/governance/migration/SOURCE_MAP.md`, `RULE_COVERAGE.md` | Source preservation and operative coverage; [source map](SOURCE_MAP.md), [coverage](RULE_COVERAGE.md). |
| `docs/governance/history/README.md` | Exact archive, verbatim events and indexes; historical evidence, not permission; [store](../history/README.md). |

Registered roadmap pointers remain at their existing locations; consult the applicable actual decision and its scope: [S00_L19/L20 driver-input
ownership](../../architecture_decisions/ADR_S00_L19_L20_Driver_Input_Ownership.md),
[A00](../../architecture_decisions/ADR_A00_Autonomous_Command_Foundation_Roadmap.md),
[A01](../../architecture_decisions/ADR_A01_Autonomous_Navigation_Path_Following_Roadmap.md),
[V00](../../architecture_decisions/ADR_V00_AprilTag_Vision_Observation_and_Pose_Fusion_Roadmap.md),
[M00](../../architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md), [PDF/mirror
policy](../../architecture_decisions/ADR_Governance_PDF_Verified_Markdown_Mirrors.md) and [GOV2
migration](../../architecture_decisions/ADR_GOV2_Governance_2_0_Agent_Instructions_State_and_History_Migration.md).

Read applicable reopen, configuration and package-repair ADRs through registered navigation before dependent work; their historical scopes are not renewed.
Read task-relevant lesson lifecycle documents and source/evidence from the accepted target directory. Do not open parked candidate ADRs as substitutes for
registered roadmap authority.

## 16. Required truthful report

Report documents read, source/active lesson or NONE, inspected/changed paths, architecture/Backbone/RobotContainer, supplied baseline/build/tests,
Simulation, Glass/Driver Station, real robot, documentation, Git commit/push, issues and status. Distinguish observed facts, accepted User execution,
history, inference, applicability and actual actions. Report NOT RUN, NOT TESTED, DEFERRED and justified NOT APPLICABLE honestly; invent no counts, exit
codes, hashes, SHAs, tags, pushes, semantic certification or hardware PASS. Disclose limitations, failures, protected access and HOLD. Self-review is not
independent review, Architect acceptance, closure, cutover or external publication verification.

## 17. Historical, candidate and consumed-permission prohibition

Historical records preserve evidence and accepted decisions within their original stage/applicability; they do not provide fresh activation,
implementation, repair, freeze, publication or successor permission. Consumed approvals remain consumed. Superseded HOLD/PENDING/current-state snapshots
remain historical, including original failures. Parked artifacts, candidate rules, candidate roadmaps and migration review files are not current authority.
Preserve original archive bytes, event bodies, provenance, chronology and supersession links; annotations stay outside verbatim bodies. Summaries, indexes
and recovered bytes cannot replace operative semantic coverage or accepted decisions. No permission follows merely from a file existing, a historical PASS,
a closed gate or this proposed root being reviewed.
