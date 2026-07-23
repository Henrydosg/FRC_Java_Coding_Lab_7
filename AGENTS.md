# AGENTS.md

# FRC Java Coding Lab 7.0 — Repository Rules

English is normative. Vietnamese is explanatory.

---

## 1. Required Reading

Before any analysis, coding, documentation, commit, or push, Codex MUST read:

1. AGENTS.md
2. README.md
3. docs/Document_A/FRC_Final_Frozen_Backbone_Guide_EN.pdf
4. docs/Document_A/ES-06_Frozen_Interface_Contract_EN.pdf
5. docs/Document_B/English/00_Engineering_Standard_Overview_EN.pdf
6. docs/Document_B/English/01_Frozen_Development_Workflow_EN.pdf
7. docs/Document_B/English/02_Java_Coding_Standard_EN.pdf
8. docs/Document_B/English/03_Architecture_Review_Checklist_EN.pdf
9. docs/Document_B/English/04_Lesson_Module_Checklist_EN.pdf
10. Active lesson LESSON_STATUS.md
11. Active lesson source code

Only the English PDF documents are authoritative.
DOCX files are editable source documents.
Vietnamese documents are reference translations and are not required reading.

### Authority Order

1. AGENTS.md
2. Document A
3. Document B
4. README.md
5. Repository Source Code

If documents conflict, the higher priority document wins.

Stop immediately if repository code conflicts with Document A or Document B.

---

## 2. Repository Structure

Repository layout is fixed.

```
FRC_Java_Coding_Lab_7/
├── AGENTS.md
├── README.md
├── docs/
│   ├── Document_A/
│   └── Document_B/
└── real_robot_programming/
    └── <LESSON_NAME>/
        ├── docs/
        ├── src/
        ├── build.gradle
        ├── settings.gradle
        └── LESSON_STATUS.md
```

Rules

- One lesson = One independent WPILib project.
- Every lesson has its own docs.
- Every lesson has LESSON_STATUS.md.
- Do not create folders outside the approved structure.

---

## 3. Frozen Backbone

Always preserve

Driver
→ Xbox Controller
→ controls
→ commands
→ subsystems
→ io
→ hardware

Telemetry flow

Subsystem
→ Telemetry Coordinator
→ Telemetry Facade
→ NetworkTables / Glass

Telemetry is read-only.

---

## 4. Package Responsibilities

controls
- Driver input processing only.

commands
- Coordinate subsystem actions.

subsystems
- Own mechanism behavior and state.

io
- Hardware abstraction only.

telemetry
- Observation only.

util
- Shared reusable helpers.

---

## 5. RobotContainer

RobotContainer is the Composition Root.

Allowed

- object creation
- dependency injection
- implementation selection
- default commands
- button bindings

Forbidden

- hardware logic
- mechanism logic
- input processing
- telemetry calculations
- business logic

---

## 6. IO Contract

Every mechanism must provide

- IO interface
- Inputs snapshot
- Real implementation
- Simulation or Noop implementation when required by the current lesson
- Safe stop()

Flow

Hardware
→ IO
→ Inputs
→ Subsystem
→ Telemetry

Subsystems never access vendor hardware directly.

---

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

## 8. Lesson Lifecycle

Copy previous completed lesson
→ Rename
→ Delete build/ and .gradle/
→ Baseline Build
→ Add ONE concept
→ Build
→ Simulation
→ Real Robot
→ Documentation
→ Commit
→ Push

Rules

- Never recreate from scratch.
- Never modify the source code of completed lessons.
- Documentation or metadata may be updated only with explicit user approval.
- Only the lesson with Status = IN_PROGRESS is editable.
- COMPLETE lessons are frozen snapshots.

---

## 9. LESSON_STATUS.md

Required fields

- Lesson
- Previous Lesson
- Status
- Architecture Review
- Baseline Build
- Build
- Simulation
- Driver Station / Glass
- Real Robot
- Transition Guide
- Git Commit
- Git Push
- Known Issues

Lesson status

- IN_PROGRESS
- COMPLETE

Verification

- PASS
- FAIL
- NOT TESTED
- NOT APPLICABLE

Never report PASS without evidence.

---

## 10. Development Workflow

Before coding

- Read required documents.
- Confirm active lesson.
- Review Backbone.
- Review architecture.
- Confirm lesson objective.

During coding

- Each implementation step shall have one objective and one independently verifiable result.
- Preserve architecture.
- Build frequently.

After coding

- Build.
- Verify.
- Record issues.
- Update LESSON_STATUS.md.

Stop when

- required documents missing
- architecture conflict
- Document A/B conflict
- build fails
- verification fails

### Repository Safety

Never

- rename repository folders
- move repository folders
- delete repository folders
- overwrite repository folders
- reorganize repository structure

unless explicitly approved by the user.

### Self Review

Before reporting success verify

- Documents read
- Document A reviewed
- Document B reviewed
- Backbone preserved
- Architecture preserved
- RobotContainer preserved
- Build reported
- Verification reported
- Documentation reported
- No unsupported claims

---

## 11. Documentation

Every completed lesson contains

real_robot_programming/<LESSON>/docs/

Required guide

<PREVIOUS>_to_<CURRENT>_Step_by_Step.md

Documentation is created only after implementation and verification.

Each step contains

- Step
- Objective
- Why
- Action
- Files Changed
- Verification
- Expected Result

One step = One change.

---

## 12. Built-in Commands

### Make step by step docs

Codex shall

- Read AGENTS
- Read Document A
- Read Document B
- Read README
- Read LESSON_STATUS
- Compare previous and current lesson
- Generate guide
- Save guide
- Update LESSON_STATUS
- Review git status
- Commit
- Push
- Report results

Stop if

- lesson incomplete
- build failed
- verification missing
- git failure

### Reserved Commands

Start next lesson

Finish lesson

Publish lesson

Reserved for future repository automation.

---

## 13. Git Rules

Workflow

git status
→ git add
→ git commit
→ git push

Never claim GitHub was updated unless push succeeds.

---

## 14. Change Control

Formal review required before changing

- Frozen Backbone
- Package responsibilities
- Dependency direction
- IO contracts
- RobotContainer role
- Constants architecture
- Completed lessons

The review shall document:

- Reason
- Scope
- Impact
- Decision (APPROVED / REJECTED)

---

## 15. Final Report

Always report

- Required Documents
- Architecture Check
- Source Lesson
- Active Lesson
- Files Inspected
- Files Changed
- Baseline Build
- Build Result
- Simulation Result
- Driver Station / Glass Result
- Real Robot Result
- Documentation Result
- Git Commit Result
- Git Push Result
- Known Issues
- Lesson Status

Only report verified facts.
