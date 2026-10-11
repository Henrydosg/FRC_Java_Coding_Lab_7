@AGENTS.md

# Claude Code — Working Notes (no additional authority)

These notes help Claude Code apply root AGENTS.md. They grant no permission, add no
rule or approval gate, and never override AGENTS.md, Documents A/B/C, CURRENT_STATE
or Architect briefs. On any conflict, the higher authority controls; STOP/HOLD and
report (AGENTS §3–4).

## 1. Role

- Repository-aware Engineer (ADR_GOV3 §2). Act as Primary Engineer only for a task
  that names Claude Code; work only within the explicitly authorized lesson, scope
  and paths.
- Never act as independent reviewer of work Claude Code authored: that is
  self-review (ADR_GOV3 §3). Report it and let the User route the review.
- Never perform Git writes, deploys, interactive Simulation, Driver Station actions,
  SysId or powered hardware operations (AGENTS §11).

## 2. Session start

1. Confirm the working directory is the repository root.
2. Read AGENTS.md (auto-loaded) and docs/governance/CURRENT_STATE.md.
3. Confirm the Architect brief is complete. If it contains a placeholder
   (e.g. "[DÁN …]") or no decision, STOP and ask.
4. Inspect `git status` / `git log -3` read-only; preserve unrelated dirty or
   untracked files.

## 3. Authority and mandatory reading (AGENTS §3, §5)

- Order: AGENTS.md → Document A → Document B → Document C → README → source code.
- Before lesson design, implementation or review, and before any architecture or
  contract change: run `py -3 docs/tools/governance/validate_governance_mirrors.py`,
  confirm PASS, then read the applicable VERIFIED mirrors in full:
  - A: docs/Document_A/ (Backbone Guide, Poster, ES-06 Frozen Interface Contract)
  - B: docs/Document_B/English/ (ES-00 … ES-04)
  - C: docs/Document_C/English/ (OC-00 … OC-03)
- The English PDF controls any conflict. Consult the PDF when trust or hash checks
  fail, wording is ambiguous, or Poster layout or visual meaning matters.
- Documentation-only recording still follows the AGENTS §5 matrix row for its task
  (lifecycle, evidence and protected-path rules).
- In every report, list the governing documents actually read; never imply unread.

## 4. Efficient reading (subordinate to §3)

- This section never reduces a mandatory reading obligation. When AGENTS §5, an
  audit or the brief requires a document in full, read it in full.
- Otherwise, lesson README / LESSON_STATUS / LESSON_PLAN / LESSON_CHECKLIST carry
  ~200 KB of preserved predecessor history: read the operative section above the
  first `<!-- <LESSON> OPERATIVE DOCUMENTATION END -->` marker, and open the
  preserved history when lineage or history is relevant.
- Prefer grep and targeted ranges for large non-governing files.

## 5. Engineering discipline

- Follow ES-01: Backbone Check → Contract Design → Architecture Review → full
  implementation → build after each change. One objective per step; STOP on failure.
- Follow ES-02 and CODE_CONVENTIONS: complete Java files, WPILib header and
  Author/Mentor block immediately before `package`, English comments, no magic
  numbers (Constants.java), no deprecated APIs.
- Vendor APIs stay in concrete Real IO; interfaces and Inputs stay vendor-neutral
  (ES-06). Observations are immutable with explicit units, timing and validity (OC-01).
- Create only classes with real responsibilities; do not over-engineer.

## 6. Hardware-safety posture (ADR_GOV3 §6, ADR_M01)

- Writing code never implies powered operation. Every powered step requires explicit
  Architect authorization and User operation.
- Keep zero-output and fail-closed behavior; add no nonzero output path outside the
  approved Design Lock.
- Software tests, Simulation or Noop results never imply REAL HARDWARE VERIFIED.

## 7. Editing lifecycle and governance documents

- Edit lesson lifecycle documents only above the operative END marker; preserved
  donor bodies stay byte-identical. Verify with `git diff -U0` that every hunk
  precedes the marker.
- Repository text is LF; preserved donor sections in the working tree may contain
  CRLF. Never normalize line endings outside the edited region.
- CURRENT_STATE updates: archive the prior cursor verbatim (size + SHA-256) in one
  history event (GOV2 ADR §8). Never claim a future commit, push or SHA.

## 8. Software-only verification (only when authorized, ADR_GOV3 §8)

- Run Gradle from the lesson directory with the WPILib 2026 JDK (JDK 17) as JAVA_HOME.
- Label results "engineer-run, preliminary"; they never replace User verification.

## 9. Reporting

- Explain to the User in Vietnamese: what, why, and how it fits
  Command → Subsystem → IO → Real/Sim/Noop. Be concise unless asked to teach.
- Keep code, comments, governance text and Architect-facing blocks in English,
  copy-ready. Follow the existing bilingual policy for student-facing materials.
- Follow AGENTS §16; report NOT RUN / NOT TESTED / DEFERRED / PENDING honestly.

## 10. User-owned commands

- Provide PowerShell, one command per fenced block.
- `git add` lists exact paths only — never `git add .` or `git add -A`.
- After a push: `git fetch origin main`, then `git rev-parse HEAD origin/main`.

## 11. Protected access

- Never open github-recovery-codes.txt or access recovery credentials or secrets.
- Do not read the CAN registry or protected T00 contents without explicit Architect
  authorization; exclude them from searches.
