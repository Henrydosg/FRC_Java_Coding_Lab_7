# AGENTS.md

# FRC Java Coding Lab 7.0 — Repository Rules  
# FRC Java Coding Lab 7.0 — Quy tắc Repository

This file combines Document A and Document B into one operating standard for Codex.  
File này kết hợp Document A và Document B thành một tiêu chuẩn vận hành duy nhất cho Codex.

English is the normative rule. Vietnamese is the faithful explanation.  
Tiếng Anh là quy tắc chuẩn. Tiếng Việt là phần giải thích tương đương.

---

## 1. Frozen Architecture / Kiến trúc đóng băng

MUST preserve this control flow:

```text
Driver
  ↓
Xbox Controller
  ↓
controls
  ↓
commands
  ↓
subsystems
  ↓
IO interface
  ↓
IO implementation
  ↓
Hardware
```

PHẢI giữ nguyên luồng điều khiển trên.

Telemetry observes the system but MUST NOT control robot behavior.  
Telemetry chỉ quan sát hệ thống và KHÔNG ĐƯỢC PHÉP điều khiển robot.

---

## 2. Package Responsibilities / Trách nhiệm package

### `controls`

Converts raw driver input into processed robot intent.  
Chuyển input thô của người lái thành yêu cầu điều khiển đã xử lý.

Allowed: deadband, inversion, scaling, curves, slew-rate limiting.  
Được phép: deadband, inversion, scaling, curves, slew-rate limiting.

MUST NOT contain hardware or command scheduling logic.  
KHÔNG ĐƯỢC PHÉP chứa phần cứng hoặc logic lập lịch command.

### `commands`

Coordinates robot actions through subsystem APIs.  
Điều phối hành động robot thông qua API của subsystem.

MUST NOT call vendor hardware APIs directly.  
KHÔNG ĐƯỢC PHÉP gọi trực tiếp API phần cứng của vendor.

### `subsystems`

Owns mechanism behavior, safety, and current mechanism state.  
Sở hữu hành vi, an toàn và trạng thái hiện tại của mechanism.

MUST depend on IO interfaces, not vendor implementations.  
PHẢI phụ thuộc vào IO interface, không phụ thuộc implementation của vendor.

### `io`

Defines hardware contracts and contains real or simulation implementations.  
Định nghĩa contract phần cứng và chứa implementation cho robot thật hoặc simulation.

IO implementations MAY use vendor APIs.  
IO implementation CÓ THỂ sử dụng API của vendor.

IO MUST NOT publish NetworkTables, schedule commands, or process driver intent.  
IO KHÔNG ĐƯỢC PHÉP publish NetworkTables, schedule command hoặc xử lý driver intent.

### `telemetry`

Publishes observations and diagnostics only.  
Chỉ publish dữ liệu quan sát và chẩn đoán.

Telemetry MUST NOT access vendor hardware directly or change robot behavior.  
Telemetry KHÔNG ĐƯỢC PHÉP truy cập trực tiếp phần cứng vendor hoặc thay đổi hành vi robot.

### `util`

Contains only generic helpers reused by multiple mechanisms.  
Chỉ chứa helper dùng chung cho nhiều mechanism.

Mechanism-specific helpers stay with that mechanism.  
Helper riêng của mechanism phải đặt cùng mechanism đó.

---

## 3. RobotContainer / Vai trò RobotContainer

`RobotContainer` is the Composition Root.

It MAY:

- create objects;
- select real or simulation implementations;
- inject dependencies;
- configure default commands;
- configure button bindings.

`RobotContainer` là Composition Root.

Nó CÓ THỂ:

- tạo object;
- chọn implementation cho robot thật hoặc simulation;
- inject dependency;
- cấu hình default command;
- cấu hình button binding.

It MUST NOT contain:

- input-processing logic;
- mechanism behavior;
- vendor hardware logic;
- periodic telemetry publishing;
- subsystem business logic.

Nó KHÔNG ĐƯỢC PHÉP chứa:

- logic xử lý input;
- hành vi mechanism;
- logic phần cứng vendor;
- telemetry publishing định kỳ;
- business logic của subsystem.

---

## 4. IO Interface Contract / Contract của IO interface

Every mechanism MUST use its own IO interface and Inputs Snapshot.

Examples:

- `DriveIO` and `DriveIOInputs`
- `IntakeIO` and `IntakeIOInputs`
- `ShooterIO` and `ShooterIOInputs`

Mỗi mechanism PHẢI có IO interface và Inputs Snapshot riêng.

Required pattern:

```text
Hardware
  ↓
IO implementation
  ↓ updateInputs(...)
Inputs Snapshot
  ↓
Subsystem
  ↓
Telemetry
```

Rules:

- Interfaces define capability, not implementation.
- Interfaces MUST remain vendor-independent.
- Inputs represent hardware observations for one periodic cycle.
- Inputs MUST contain no control logic or vendor API calls.
- The IO implementation fully updates Inputs.
- The subsystem MUST NOT read hardware outside the IO contract.
- Every IO interface MUST provide a safe `stop()` operation.
- Real and simulation implementations MUST be replaceable without changing the subsystem.

Quy tắc:

- Interface định nghĩa khả năng, không định nghĩa implementation.
- Interface PHẢI độc lập với vendor.
- Inputs biểu diễn dữ liệu phần cứng trong một chu kỳ periodic.
- Inputs KHÔNG ĐƯỢC PHÉP chứa control logic hoặc vendor API.
- IO implementation phải cập nhật đầy đủ Inputs.
- Subsystem KHÔNG ĐƯỢC PHÉP đọc phần cứng ngoài IO contract.
- Mỗi IO interface PHẢI có operation `stop()` an toàn.
- Implementation robot thật và simulation PHẢI thay thế được mà không sửa subsystem.

---

## 5. Constants / Constants

`Constants.java` is the default configuration authority.  
`Constants.java` là nơi cấu hình mặc định.

MUST NOT use magic numbers in Java code.  
KHÔNG ĐƯỢC PHÉP sử dụng magic number trong code Java.

Use nested static classes by mechanism or concern.  
Sử dụng nested static class theo mechanism hoặc nhóm chức năng.

Do not split `Constants.java` unless a formal architecture review approves it.  
Không tách `Constants.java` nếu chưa được architecture review chính thức phê duyệt.

---

## 6. Java Coding Rules / Quy tắc code Java

Every created or modified Java file MUST be delivered as a complete file.  
Mỗi file Java được tạo hoặc sửa PHẢI được cung cấp đầy đủ toàn bộ file.

MUST NOT use:

- diffs;
- ellipses;
- omitted lines;
- deprecated APIs;
- Vietnamese identifiers;
- long Vietnamese comments inside Java.

KHÔNG ĐƯỢC PHÉP sử dụng:

- diff;
- dấu ba chấm thay nội dung;
- dòng bị lược bỏ;
- API deprecated;
- định danh tiếng Việt;
- comment tiếng Việt dài trong Java.

Every Java file MUST preserve the WPILib copyright header and include:

```java
/**
 * Author: SSIS
 * Mentor: SSIS
 */
```

immediately before the `package` declaration.

Mỗi file Java PHẢI giữ header bản quyền WPILib và khối trên ngay trước khai báo `package`.

Naming:

- classes and interfaces: `PascalCase`;
- methods and fields: `camelCase`;
- constants: current WPILib project convention;
- names describe responsibility, not temporary lesson context.

Comments MUST be concise technical English only.  
Comment PHẢI chỉ sử dụng tiếng Anh kỹ thuật ngắn gọn.

---

## 7. Development Workflow / Quy trình phát triển

Codex MUST follow this order:

```text
1. Inspect the current project
2. Check the Frozen Backbone
3. Design or confirm the contract
4. Perform architecture review
5. Implement one clear change
6. Run build
7. Verify in simulation when applicable
8. Verify on the real robot when available
9. Record known issues and lessons learned
10. Commit the completed lesson
```

Codex PHẢI thực hiện đúng thứ tự trên.

Stop rules:

- Backbone Check FAIL → stop and redesign.
- Architecture Review FAIL → do not implement.
- Build FAIL → do not continue.
- Verification FAIL → record the cause, fix one change, and retest.

Quy tắc dừng:

- Backbone Check KHÔNG ĐẠT → dừng và thiết kế lại.
- Architecture Review KHÔNG ĐẠT → không implement.
- Build KHÔNG ĐẠT → không tiếp tục.
- Verification KHÔNG ĐẠT → ghi nguyên nhân, sửa một thay đổi và kiểm tra lại.

---

## 8. Inheritance Development / Phát triển kế thừa

Each lesson MUST inherit the latest completed lesson.  
Mỗi lesson PHẢI kế thừa lesson hoàn thành gần nhất.

Required sequence:

```text
Copy previous lesson
  ↓
Rename project
  ↓
Remove generated build folders
  ↓
Build inherited baseline
  ↓
Add one lesson concept
  ↓
Build
  ↓
Simulation
  ↓
Real robot
```

MUST NOT redesign earlier architecture or create disposable demo code.  
KHÔNG ĐƯỢC PHÉP thiết kế lại kiến trúc cũ hoặc tạo code demo dùng một lần.

---

## 9. Architecture Review / Kiểm tra kiến trúc

Before implementation, confirm:

- package responsibility is correct;
- dependency direction is correct;
- control and observation flows are correct;
- `RobotContainer` remains composition-only;
- commands do not access hardware;
- subsystems own mechanism state;
- IO owns hardware access and Inputs updates;
- telemetry is observer-only;
- no responsibility is duplicated;
- no unnecessary abstraction is introduced;
- build and verification plans exist.

Trước khi implement, phải xác nhận đầy đủ các mục trên.

---

## 10. Verification / Kiểm chứng

Build, simulation, Driver Station/Glass, and real robot results MUST be reported separately.  
Kết quả build, simulation, Driver Station/Glass và robot thật PHẢI được báo cáo riêng.

Allowed statuses:

- `PASS`
- `FAIL`
- `NOT TESTED`
- `NOT APPLICABLE`

Codex MUST NOT fabricate:

- build results;
- simulation results;
- Driver Station results;
- real robot results.

Codex KHÔNG ĐƯỢC PHÉP bịa kết quả kiểm tra.

A lesson is complete only when:

- architecture review passes;
- implementation is complete;
- build passes;
- applicable verification is completed;
- safety is reviewed;
- known issues are recorded;
- Git commit is completed.

Lesson chỉ hoàn thành khi đáp ứng đầy đủ các điều kiện trên.

---

## 11. Change Control / Kiểm soát thay đổi

Normal lessons MAY extend the frozen architecture but MUST NOT redesign it.  
Lesson thông thường CÓ THỂ mở rộng kiến trúc đóng băng nhưng KHÔNG ĐƯỢC PHÉP thiết kế lại.

A formal architecture review is required before:

- changing package responsibilities;
- reversing dependency direction;
- changing IO contracts incompatibly;
- allowing telemetry to control behavior;
- moving logic into `RobotContainer`;
- splitting `Constants.java`;
- changing the Frozen Backbone.

Phải thực hiện architecture review chính thức trước các thay đổi trên.

---

## 12. Codex Final Report / Báo cáo cuối của Codex

After each code task, report:

```text
Architecture Check
Files Inspected
Files Changed
Build Result
Simulation Result
Real Robot Result
Regression Result
Known Issues
Lesson Status
```

Use exact evidence.  
Sử dụng bằng chứng chính xác.

Never claim a test passed unless it was actually performed.  
Không được báo kiểm tra đạt nếu chưa thực hiện.
