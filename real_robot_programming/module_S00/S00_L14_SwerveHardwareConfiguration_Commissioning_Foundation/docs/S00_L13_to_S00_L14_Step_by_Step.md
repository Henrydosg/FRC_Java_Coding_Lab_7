# S00_L13 to S00_L14 Step by Step

## Step 1

- Objective: Audit the inherited S00_L13 hardware boundary.
- Why: Commissioning must begin with evidence and ownership boundaries before any hardware configuration is written.
- Action: Inspect `Constants.java`, `SwerveModuleIO`, `SwerveModuleIOCTRE`, `GyroIO`, `GyroIOPigeon2`, `SwerveSubsystem`, observations, telemetry, tests, and inherited documentation.
- Files Changed: None.
- Verification: The inherited IOInputs, immutable observation, and read-only telemetry path was reviewed.
- Expected Result: Vendor APIs remain in IO adapters, and subsystem/telemetry behavior remains read-only.

## Step 2

- Objective: Classify the four-module and Pigeon2 hardware values.
- Why: Unverified configuration must not be silently promoted into robot behavior.
- Action: Create the hardware commissioning matrix with VERIFIED, PROVISIONAL, and UNRESOLVED classifications.
- Files Changed: `docs/S00_L14_Swerve_Hardware_Commissioning_Matrix.md`.
- Verification: CAN identifiers, drive ratio, and wheel diameter match the inherited hardware map and Constants; live evidence verifies `rio`, connectivity, configuration health, and the recorded commissioning checks. Unsupported configuration values remain UNRESOLVED.
- Expected Result: The matrix is complete without inventing steer ratios, inversions, offsets, neutral modes, current limits, ramp rates, mounting, or yaw values.

## Step 3

- Objective: Confirm the smallest safe commissioning implementation.
- Why: Existing observation and telemetry contracts already expose connectivity and configuration health.
- Action: Retain the inherited read-only code path and add no output method, configuration write, command, or new abstraction.
- Files Changed: None.
- Verification: `SwerveModuleIOCTRE` and `GyroIOPigeon2` only refresh signals into IOInputs; `SwerveSubsystem.periodic()` produces observations; existing telemetry publishes them.
- Expected Result: Commissioning data can be observed without drive or steer output calls.

## Step 4

- Objective: Record user-owned verification boundaries.
- Why: Repository evidence cannot replace Phoenix Tuner X or disabled real-robot verification.
- Action: Record the supplied Phoenix Tuner X and disabled real-robot verification evidence.
- Files Changed: S00_L14 status and checklist documentation.
- Verification: 14/14 CTRE devices were detected on `rio`; all TalonFX, CANcoder, and Pigeon2 devices were online, with no duplicate IDs or unexpected faults. The robot remained Disabled, no unintended actuation occurred, CANcoder signals updated correctly, FL direction was verified, FR/BL/BR checks passed, and Pigeon2 communication passed.
- Expected Result: Hardware Audit and Hardware Commissioning are PASS, while the evidence remains explicitly limited to Disabled commissioning.

## Step 5

- Objective: Record the final documentation state.
- Why: The supplied verification completes the lesson evidence while Git lifecycle actions remain user-owned.
- Action: Update only the S00_L14 README, lesson plan, status, checklist, transition guide, and commissioning matrix.
- Files Changed: S00_L14 documentation files only.
- Verification: Architecture Review PASS, Implementation PASS, Hardware Audit PASS, Hardware Commissioning PASS, Baseline Build PASS, Full Build PASS, Focused Tests NOT TESTED, Simulation NOT APPLICABLE, Glass NOT APPLICABLE, Driver Station PASS (Disabled commissioning only), Real Robot PASS (Disabled commissioning only), Documentation PASS, and Commit/Push/Freeze NOT TESTED.
- Expected Result: Status is `COMPLETE`; final lesson state is `FROZEN / READ-ONLY`; unresolved hardware values remain explicitly unresolved.
