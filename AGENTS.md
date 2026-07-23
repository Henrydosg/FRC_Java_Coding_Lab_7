# AGENTS.md

# FRC Java Coding Lab 7.0 — Repository Rules

English documents are authoritative. Vietnamese may be used for explanatory documentation.

## 1. Required Reading

Before analysis, implementation, documentation, commit, or push, Codex MUST read:

1. `AGENTS.md`
2. `README.md`
3. `REPOSITORY_WORKFLOW.md`
4. `docs/Document_A/FRC_Final_Frozen_Backbone_Guide_EN.pdf`
5. `docs/Document_A/ES-06_Frozen_Interface_Contract_EN.pdf`
6. `docs/Document_B/English/00_Engineering_Standard_Overview_EN.pdf`
7. `docs/Document_B/English/01_Frozen_Development_Workflow_EN.pdf`
8. `docs/Document_B/English/02_Java_Coding_Standard_EN.pdf`
9. `docs/Document_B/English/03_Architecture_Review_Checklist_EN.pdf`
10. `docs/Document_B/English/04_Lesson_Module_Checklist_EN.pdf`
11. Active lesson `LESSON_STATUS.md`
12. Active lesson source code

DOCX files are editable sources. Vietnamese files are reference translations.

## 2. Authority Order

1. `AGENTS.md`
2. Authoritative English Document A
3. Authoritative English Document B
4. `REPOSITORY_WORKFLOW.md`
5. `README.md`
6. Active lesson `LESSON_STATUS.md`
7. Active source code

When documents conflict, the higher authority wins.

## 3. Active Lesson Rule

- Only the latest active lesson may be modified.
- Earlier lessons are read-only snapshots.
- Never reorganize repository folders without explicit user approval.
- One lesson introduces one architectural concept.

## 4. Frozen Backbone

```text
Driver
→ Xbox Controller
→ controls
→ commands
→ subsystems
→ io
→ Hardware
```

Telemetry is read-only:

```text
Subsystem observations
→ Telemetry Coordinator
→ Telemetry Facade
→ NetworkTables / Glass
```

## 5. Package Responsibilities

- `controls`: driver-input processing only
- `commands`: action coordination and subsystem requirements
- `subsystems`: mechanism behavior and state
- `io`: vendor-independent hardware boundary
- `telemetry`: read-only observation and publishing
- `util`: proven shared helpers only

## 6. RobotContainer

`RobotContainer` is the composition root only.

Allowed:

- object creation
- implementation selection
- dependency injection
- default commands
- button bindings

Forbidden:

- input-processing logic
- mechanism behavior
- hardware configuration logic
- telemetry calculations
- business logic

## 7. IO Contract

Each mechanism IO design must provide, when required by the lesson:

- IO interface
- inputs snapshot
- real implementation
- simulation or no-op implementation
- safe stop method

Subsystems must not directly import vendor hardware APIs.

```text
Hardware
→ IO
→ Inputs
→ Subsystem
→ Telemetry
```

## 8. Java Rules

- Provide complete Java files only.
- Never use diffs, ellipses, or omitted lines as the final code deliverable.
- Preserve the WPILib copyright header.
- Add this block immediately before `package`:

```java
/**
 * Author: SSIS
 * Mentor: SSIS
 */
```

- Comments must be short, technical, and English-only.
- Do not use deprecated APIs.
- Do not use magic numbers or hardcoded configuration values.
- Prefer low coupling, high cohesion, dependency injection, composition, and single responsibility.
- Do not add unnecessary packages, classes, abstractions, or refactors.
- Keep `Constants.java` as the default configuration authority unless an approved lesson changes that rule.

## 9. Verification Truthfulness

Allowed verification results:

- `PASS`
- `FAIL`
- `NOT TESTED`
- `NOT APPLICABLE`

Rules:

- Never claim `PASS` without evidence.
- A successful build does not prove simulation or real-robot behavior.
- Use stable evidence: source inspection, Git diff, command output, simulation output, Driver Station/Glass output, or real-robot test results.
- Do not use a commit hash as the only evidence.
- Preserve untested items as `NOT TESTED`.

## 10. LESSON_STATUS.md

Codex maintains the active lesson `LESSON_STATUS.md`.

Allowed lesson statuses:

- `PLANNED`
- `IN_PROGRESS`
- `BUILD_PASSED`
- `SIMULATION_PASSED`
- `REAL_ROBOT_PASSED`
- `COMPLETE`

Rules:

- Before source edits: `IN_PROGRESS`
- After verified clean build: `BUILD_PASSED`
- After verified simulation: `SIMULATION_PASSED`
- After verified real-robot test: `REAL_ROBOT_PASSED`
- `COMPLETE` requires all mandatory gates to pass
- The file must show the current gate and next gate

## 11. Documentation

Each lesson transition must create:

```text
docs/<PREVIOUS>_to_<CURRENT>_Step_by_Step.md
```

Required sections:

1. Lesson Summary
2. Why This Lesson Exists
3. Starting Architecture
4. Target Architecture
5. Files Created
6. Files Modified
7. Step-by-Step Implementation
8. Behavior Preservation
9. Verification Results
10. Known Issues and Deferred Work
11. Final Checklist

Every implementation step must include:

- Objective
- Why
- Before
- After
- Action
- Files Changed
- Verification
- Expected Result

The guide must be understandable without opening Git history and must match the actual source and Git diff.

## 12. Completion Gate

```text
Implementation
→ Build PASS
→ Required runtime verification PASS
→ Documentation created
→ Documentation review PASS
→ LESSON_STATUS.md updated
→ User review
→ Commit
→ Push
```

Never skip a gate. Never mark `COMPLETE` early.

## 13. Git Rules

Codex must not commit or push unless explicitly requested in the current instruction.

Before commit:

```text
git status
→ git diff --check
→ review staged files
→ user approval
→ git commit
```

Before push:

```text
verified commit
→ explicit user approval
→ git push
```

Never claim GitHub was updated unless push succeeds.

## 14. Final Report

Report only verified facts:

- required documents read
- active lesson
- objective
- architecture before
- architecture after
- files inspected
- files created
- files modified
- baseline build
- final build
- simulation
- Driver Station / Glass
- real robot
- documentation
- documentation review
- Git commit
- Git push
- known issues
- current lesson status
- next required gate
