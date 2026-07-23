# REPOSITORY_WORKFLOW.md

# FRC Java Coding Lab 7.0 — Lesson Lifecycle

## 1. Purpose

This file defines the operational workflow for developing one lesson by inheritance from the previous completed lesson.

## 2. Standard Lifecycle

```text
Select latest completed lesson
→ Copy lesson folder
→ Rename new lesson
→ Delete build/ and .gradle/
→ Create or reset LESSON_STATUS.md
→ Baseline build
→ Review architecture
→ Implement one concept
→ Clean build
→ Required runtime verification
→ Create transition guide
→ Review documentation
→ Update LESSON_STATUS.md
→ User review
→ Commit
→ Push
```

## 3. Lesson Inheritance

- Never create a disconnected demonstration project.
- Preserve the previous lesson architecture unless the new lesson explicitly extends it.
- Do not reintroduce an older architecture.
- Keep earlier lessons unchanged as historical snapshots.
- Delete only generated build artifacts from the copied lesson.

## 4. Pre-Implementation Gate

Before editing Java:

1. confirm the active lesson;
2. read all required governance documents;
3. inspect active source;
4. identify the single lesson objective;
5. identify files that must change;
6. confirm the frozen backbone remains valid;
7. update `LESSON_STATUS.md` to `IN_PROGRESS`.

## 5. Implementation Gate

For every step:

```text
Objective
→ Why
→ Before
→ After
→ Action
→ Verification
→ Expected Result
```

Rules:

- One step makes one conceptual change.
- Do not bundle unrelated refactors.
- Preserve verified hardware behavior unless the lesson explicitly changes it.
- Stop and report architecture conflicts that cannot be resolved within scope.

## 6. Verification Gate

Recommended order:

```text
Baseline build
→ Clean build
→ Simulation
→ Driver Station / Glass
→ Real robot
```

Only required and available tests must run, but unavailable tests remain `NOT TESTED`.

## 7. Documentation Gate

After implementation and technical verification:

1. create the transition guide;
2. document architecture before and after;
3. list created and modified files;
4. document each implementation step;
5. record behavior preservation;
6. record exact verification evidence;
7. list known issues and deferred work;
8. review documentation against source and Git diff.

## 8. Completion Gate

A lesson may be `COMPLETE` only when:

- required implementation is complete;
- clean build passes;
- all mandatory runtime tests pass;
- transition guide exists;
- documentation review passes;
- `LESSON_STATUS.md` is accurate;
- user review is complete;
- commit succeeds;
- push succeeds when required.

## 9. Git Gate

Codex stops before commit or push unless the user explicitly authorizes the action.

Recommended checks:

```powershell
git status --short
git diff --check
git diff --name-only
```

Before commit, verify that only approved files are staged.
