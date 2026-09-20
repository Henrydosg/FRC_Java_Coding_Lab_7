# S00 L01 to S00 L02 Step by Step

## Step 1 - Copy the completed architecture foundation

- Objective: Create S00_L02 from the completed S00_L01 foundation.
- Why: The lesson lifecycle requires inheritance from a completed project.
- Action: Copy `S00_L01_Swerve_Architecture_Foundation` to `S00_L02_Swerve_Hardware_Audit`.
- Files Changed: New S00_L02 lesson project files.
- Verification: S00_L02 Java source matches S00_L01 byte-for-byte.
- Expected Result: S00_L02 begins with the frozen architecture foundation intact.

## Step 2 - Delete generated build artifacts

- Objective: Remove inherited generated output before lesson work begins.
- Why: Generated `build/` and `.gradle/` directories must not be used as baseline evidence.
- Action: Delete the copied `build/` and `.gradle/` directories.
- Files Changed: Generated `build/` and `.gradle/` directories removed.
- Verification: The baseline build begins from source and Gradle configuration, not inherited output.
- Expected Result: The lesson has a clean build boundary.

## Step 3 - Verify the baseline build

- Objective: Confirm the inherited project is buildable before the hardware audit.
- Why: The audit must not conceal an inherited build failure.
- Action: Run the approved Gradle build before changing lesson documentation.
- Files Changed: No source files.
- Verification: User verified the inherited S00_L01 baseline build as PASS.
- Expected Result: `LESSON_STATUS.md` records Baseline Build as PASS.

## Step 4 - Initialize the S00_L02 lesson status

- Objective: Establish S00_L02 as the active audit lesson.
- Why: Lesson status identifies the editable lesson, its source, and its verification state.
- Action: Set the lesson name, previous lesson, source lesson, and `IN_PROGRESS` status in `LESSON_STATUS.md`.
- Files Changed: `LESSON_STATUS.md`.
- Verification: The status identifies `S00_L01_Swerve_Architecture_Foundation` as the previous and source lesson.
- Expected Result: S00_L02 has an auditable lesson lifecycle record.

## Step 5 - Inspect the Swerve and IMU hardware records

- Objective: Identify only evidence relevant to Swerve modules and the IMU.
- Why: Tank Drive records and unverified assumptions cannot define a Swerve contract.
- Action: Review the user-designated Swerve hardware map documents and user-verified hardware evidence.
- Files Changed: `docs/S00_L02_Swerve_Hardware_Audit.md`.
- Verification: The audit distinguishes `VERIFIED` values from `UNKNOWN` values.
- Expected Result: Hardware facts are recorded without guessing.

## Step 6 - Verify Phoenix Tuner device identities and CAN IDs

- Objective: Record the verified CAN identity plan for the electrical devices.
- Why: Future IO implementation requires confirmed device identity and module mapping.
- Action: Record PDP CAN 0, Pigeon2 CAN 20, FL 21/22/23, FR 24/25/26, BL 27/28/29, and BR 30/31/32.
- Files Changed: `docs/S00_L02_Swerve_Hardware_Audit.md`.
- Verification: The CAN assignments are recorded from user-verified evidence and the Swerve hardware map.
- Expected Result: The audit retains device IDs without installing or using vendor libraries.

## Step 7 - Record module and geometry data

- Objective: Record verified module, actuator, encoder, ratio, and geometry facts.
- Why: These facts are prerequisites for a later reviewed Swerve IO and kinematics design.
- Action: Record WCP Legacy modules, Kraken X60 with TalonFX, CANcoder, the initial provisional 7.85:1 drive ratio, 4.0 in wheel diameter, 2.0 in wheel radius, and 21.5 in wheelbase and track width. Retain the later commissioning correction: repeated physical 20-motor-rotation / wheel-rotation tests established the installed ratio as 6.75:1.
- Files Changed: `docs/S00_L02_Swerve_Hardware_Audit.md`.
- Verification: The geometry and device values match the user-verified hardware evidence and designated map. The initial 7.85:1 value is historical only; the final measured/commissioned 6.75:1 value is authoritative/current.
- Expected Result: Known mechanical and electrical facts are separated from unresolved steer ratio, offsets, inversions, CAN bus name, and dependency version.

## Step 8 - Add the DOCX and PDF hardware maps

- Objective: Preserve the designated hardware references with the lesson.
- Why: Future lessons need an auditable source for verified hardware facts.
- Action: Add `Swerve_Robot_Hardware_Map_v2.0.docx` and `Swerve_Robot_Hardware_Map_v2.0.pdf` to the lesson documentation folder.
- Files Changed: `docs/Swerve_Robot_Hardware_Map_v2.0.docx` and `docs/Swerve_Robot_Hardware_Map_v2.0.pdf`.
- Verification: Both files are present in the S00_L02 documentation folder and agree on the recorded module/geometry data.
- Expected Result: The hardware audit references its source documents locally.

## Step 9 - Review the documentation boundary

- Objective: Confirm this lesson remains an audit and does not introduce implementation.
- Why: FAR reserves Swerve-specific packages until verified requirements and an architecture review justify them.
- Action: Review the audit and source tree for Java, dependency, vendor-installation, package, and RobotContainer changes.
- Files Changed: No source files.
- Verification: S00_L02 Java remains identical to S00_L01; no Swerve IO, Observation, or vendor implementation is created.
- Expected Result: The frozen backbone remains unchanged.

## Step 10 - Perform the final build before lesson completion

- Objective: Verify the documented audit lesson builds after all approved work is complete.
- Why: A completed lesson requires current build evidence.
- Action: Run the approved Gradle build during lesson closure.
- Files Changed: No source files.
- Verification: NOT TESTED by this documentation-only update.
- Expected Result: Build evidence must be recorded as PASS before S00_L02 can be marked COMPLETE.
