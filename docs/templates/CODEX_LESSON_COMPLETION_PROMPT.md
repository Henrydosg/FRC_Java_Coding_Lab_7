# CODEX LESSON COMPLETION PROMPT

Read all files required by `AGENTS.md`.

Then perform this workflow:

1. Confirm the active lesson and single lesson objective.
2. Inspect the frozen backbone and active source.
3. Update `LESSON_STATUS.md` to `IN_PROGRESS` before source edits.
4. Implement only the approved lesson scope.
5. Run the required build and runtime verification.
6. Record only verified evidence in `LESSON_STATUS.md`.
7. Create `docs/<PREVIOUS>_to_<CURRENT>_Step_by_Step.md`.
8. For every step include:
   - Objective
   - Why
   - Before
   - After
   - Action
   - Files Changed
   - Verification
   - Expected Result
9. Review the guide against the final source and Git diff.
10. Run `git status` and `git diff --check`.
11. Do not commit or push unless explicitly requested.
12. Report the exact next gate requiring user approval.

Never claim PASS without evidence.
Never treat a successful build as simulation or real-robot evidence.
Never mark the lesson COMPLETE before every mandatory gate passes.
