@AGENTS.md

## Claude-specific working notes (no additional authority)

- Explain work to the User in Vietnamese: what, why, and how it fits
  Command → Subsystem → IO → Real/Sim/Noop.
- Keep code, comments and governance in English. Follow the existing
  bilingual policy for student-facing course materials.
- Work only within the explicitly authorized lesson, scope and paths.
- Never perform Git writes, deploys, interactive Simulation,
  Driver Station actions, SysId or powered hardware operations.
- Run local software-only builds/unit tests only when explicitly
  authorized by the task and permitted by GOV3. These do not
  replace User-owned verification.
- Provide PowerShell commands for User-owned verification and Git
  publication instead of executing those operations.
- Never access recovery credentials or secrets.
- Do not read protected T00 or CAN-registry contents without explicit
  Architect authorization consistent with protected-path governance.
