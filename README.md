# FRC Java Coding Lab 7.0
## Real Robot Programming

> Learn professional FRC robot software engineering through incremental inheritance development.

> Học lập trình robot FRC theo phương pháp phát triển kế thừa (Inheritance Development) với các dự án robot thật.

---

# Repository Purpose
# Mục tiêu Repository

This repository is a complete learning roadmap for developing FRC robots using Java and WPILib.

The goal is not only to build a working robot, but also to learn software architecture, engineering workflow, testing, documentation, and long-term project maintenance.

Repository này là lộ trình học hoàn chỉnh để phát triển robot FRC bằng Java và WPILib.

Mục tiêu không chỉ là làm robot chạy được, mà còn học kiến trúc phần mềm, quy trình phát triển, kiểm thử, tài liệu hóa và bảo trì dự án lâu dài.

---

# Core Principles
# Nguyên tắc cốt lõi

- Frozen Architecture
- Inheritance Development
- One Lesson = One Project
- One Lesson = One New Concept
- Step-by-Step Learning
- Build Before Continue
- Simulation Before Real Robot
- Complete Documentation

---

# Repository Structure
# Cấu trúc Repository

```text
FRC_Java_Coding_Lab_7/
│
├── AGENTS.md
├── README.md
├── .gitignore
│
├── docs/
│   ├── Document_A/
│   ├── Document_B/
│   └── Document_C/
│
└── real_robot_programming/
    ├── D00_L01_Competition_Robot_Foundation/
    ├── D00_L02_Drivebase_Safety_Configuration/
    ├── D00_L03_Tank_Drive_With_Joystick/
    └── ...
```

---

# Directory Description
# Giải thích thư mục

## AGENTS.md

Defines the operating rules for Codex.

Định nghĩa quy tắc vận hành của Codex.

---

## docs/

Contains the repository engineering standards.

Chứa các tiêu chuẩn kỹ thuật của toàn bộ repository.

Typical contents:

- Frozen Backbone
- Frozen Interface Contract
- Engineering Standard
- Architecture Documents
- Observation Architecture and Model Contract

`Document_C` formally defines `frc.robot.observation` as the permanent
top-level package for immutable, vendor-neutral read models.

`Document_C` chính thức định nghĩa `frc.robot.observation` là package
top-level cố định cho các read model bất biến và độc lập với vendor.

---

## real_robot_programming/

Contains every lesson project.

Mỗi lesson là một project WPILib độc lập.

Each lesson inherits from the previous completed lesson.

Mỗi lesson kế thừa lesson hoàn thành trước đó.

Example:

```text
D00_L01
    ↓ Copy
D00_L02
    ↓ Copy
D00_L03
```

## Approved Autonomous Roadmap

### A01 - Autonomous Navigation and Path Following

A01 is the approved successor roadmap after frozen
`A00_L04_AutonomousMotionSafetyGating`. Its purpose is to progress from the
autonomous command foundation to deterministic field-based trajectory
execution and PathPlanner/AutoBuilder integration.

Authority:
`docs/architecture_decisions/ADR_A01_Autonomous_Navigation_Path_Following_Roadmap.md`

Current implementation state: A01 is authorized and `module_A01` exists.
`A01_L01`, `A01_L02`, and
`A01_L03_TrajectoryGenerationAndSamplingFundamentals` are `COMPLETE /
FROZEN / READ-ONLY`.
`A01_L04` through `A01_L09` are authorized by the approved ADR but have not
yet been created or started.
A00 is closed at A00_L04; A00_L05 is not authorized.

Approved lesson sequence:

1. `A01_L01 - Autonomous Starting-Pose and Field-Frame Contract`
2. `A01_L02 - Pose-Targeted Autonomous Motion`
3. `A01_L03 - Trajectory Generation and Sampling Fundamentals`
4. `A01_L04 - Field and Alliance Transform Contract`
5. `A01_L05 - Holonomic Trajectory Following`
6. `A01_L06 - PathPlanner Path and Runtime Integration`
7. `A01_L07 - AutoBuilder Contract Integration`
8. `A01_L08 - Autonomous Routine Selection and Safe Composition`
9. `A01_L09 - PathPlanner NamedCommands and Event Markers`

---

# Lesson Structure
# Cấu trúc một Lesson

```text
D00_L02_Drivebase_Safety_Configuration/
│
├── docs/
├── src/
├── vendordeps/
├── gradle/
├── build.gradle
├── settings.gradle
├── WPILib-License.md
└── LESSON_STATUS.md
```

---

## docs/

Contains the learning documentation for this lesson.

Chứa tài liệu học của lesson.

Example:

```
D00_L01_to_D00_L02_Step_by_Step.md
```

---

## src/

Robot source code.

Mã nguồn robot.

---

## LESSON_STATUS.md

Records the lesson status.

Ghi lại trạng thái lesson.

Typical information:

- Current lesson
- Previous lesson
- Build result
- Simulation result
- Real robot result
- Git status

---

# Learning Workflow
# Quy trình học

```text
Previous Lesson Completed
            │
            ▼
Copy Previous Project
            │
            ▼
Rename New Lesson
            │
            ▼
Remove Generated Folders
            │
            ▼
Baseline Build
            │
            ▼
Learn One New Concept
            │
            ▼
Build
            │
            ▼
Simulation
            │
            ▼
Real Robot
            │
            ▼
Create Step-by-Step Guide
            │
            ▼
Git Commit
            │
            ▼
Git Push
```

---

# Required Documents
# Tài liệu bắt buộc

Every completed lesson contains:

- source code
- lesson status
- transition guide

Mỗi lesson hoàn thành bao gồm:

- mã nguồn
- trạng thái lesson
- tài liệu chuyển đổi

---

# Repository Rules
# Quy tắc Repository

Always read:

1. AGENTS.md
2. Engineering Documents
3. Current Lesson

before modifying code.

Luôn đọc:

1. AGENTS.md
2. Tài liệu kỹ thuật
3. Lesson hiện tại

trước khi sửa mã nguồn.

---

# Goal
# Mục tiêu

Learn professional robot software engineering through continuous inheritance development.

Học phát triển phần mềm robot chuyên nghiệp thông qua phát triển kế thừa liên tục.
