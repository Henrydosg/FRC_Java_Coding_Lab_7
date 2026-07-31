# D01_L06 Intake Complete Foundation

## English

### Lesson Purpose

D01_L06 completes the Intake hardware foundation by adding safe TalonFX configuration and read-only electrical telemetry.

The lesson inherits the complete Intake implementation from:

```text
D01_L05_Intake_Foundation
```

D01_L05 remains complete, frozen, and unchanged.

D01_L06 does not add autonomous Intake behavior, game-piece detection, state-machine logic, or jam recovery.

Its purpose is to make the inherited manual Intake mechanism safer, more observable, and ready for future intelligent behavior.

---

### Development Model

This lesson follows Inheritance Development.

```text
D01_L05_Intake_Foundation
→ Copy
→ Rename
→ D01_L06_Intake_Complete_Foundation
→ Baseline Build
→ Controlled Lesson Changes
```

Before lesson-specific implementation:

- D01_L05 was copied.
- The copied directory was renamed.
- No inherited Java file was intentionally modified.
- No architectural boundary was changed.
- The baseline clean build passed.

Baseline result:

```text
BUILD SUCCESSFUL
```

---

### Previous Lesson

```text
D01_L05_Intake_Foundation
```

The previous lesson completed:

- Manual Intake control.
- Manual Outtake control.
- Right Trigger and Left Trigger input processing.
- IntakeSubsystem integration.
- IntakeIO abstraction.
- TalonFX real-hardware implementation.
- Deterministic simulation implementation.
- Read-only Intake telemetry.
- NetworkTables publishing.
- Glass verification.
- WPILib Simulation verification.
- Real robot Intake verification.

---

### Lesson Objective

Complete the Intake hardware foundation by implementing approved:

- TalonFX safety configuration.
- Supply current limiting.
- Stator current limiting.
- Neutral mode.
- Open-loop ramp.
- Peak forward and reverse output limits.
- Configuration application verification.
- Read-only electrical telemetry.
- Simulation-compatible telemetry values.
- Glass verification.
- Real robot verification.

The inherited Intake control behavior must remain unchanged.

---

### Frozen Architecture

The project preserves the frozen dependency direction:

```text
Driver
→ Xbox Controller
→ controls
→ commands
→ subsystems
→ io
→ Hardware / Simulation
```

The Intake control flow remains:

```text
Driver
→ Xbox Controller
→ IntakeInputProcessor
→ ManualIntakeCommand
→ IntakeSubsystem
→ IntakeIO
    ├── IntakeIOTalonFX
    └── IntakeIOSim
→ Hardware / Simulation
```

The Intake telemetry flow remains read-only:

```text
IntakeIO
→ IntakeIOInputs
→ IntakeSubsystem
→ IntakeObservation
→ RobotTelemetry
→ IntakeTelemetryFacade
→ NetworkTables / Glass
```

---

### RobotContainer Responsibility

`RobotContainer` remains the composition root.

It may perform:

- Object creation.
- Real or simulation implementation selection.
- Dependency injection.
- Default command configuration.
- Controller bindings.
- Telemetry dependency wiring.

It must not contain:

- Driver-input processing.
- Intake business logic.
- Hardware configuration.
- Electrical calculations.
- Fault evaluation.
- Current-limit decisions.
- Periodic telemetry calculations.

---

### Confirmed Hardware

| Item | Value |
| --- | --- |
| Mechanism | Intake |
| Motor | Kraken X60 |
| Motor Controller | CTRE TalonFX |
| CAN ID | 12 |
| Encoder | Integrated TalonFX Encoder |
| Control Type | Open-loop output |
| Real IO | IntakeIOTalonFX |
| Simulation IO | IntakeIOSim |
| Intake Input | Xbox Right Trigger |
| Outtake Input | Xbox Left Trigger |

---

### Inherited Intake Behavior

The inherited manual behavior remains unchanged:

```text
Right Trigger
→ Intake

Left Trigger
→ Outtake

No Trigger
→ Stop

Both Triggers
→ Inherited safe conflict result
```

Additional inherited safety behavior:

- Trigger release stops the motor.
- Command interruption stops the motor.
- Robot Disable stops the motor.
- Real and simulation implementations use the same IntakeIO contract.

---

### Lesson Scope

#### TalonFX Safety Configuration

The current `IntakeIOTalonFX` implementation will first be audited.

The approved configuration may include:

- Motor inversion.
- Neutral mode.
- Supply current limit.
- Stator current limit.
- Open-loop ramp.
- Peak forward output.
- Peak reverse output.
- Configuration application result.

All approved configuration values must be stored in `Constants.java`.

All Phoenix 6 configuration APIs must remain inside `IntakeIOTalonFX`.

The IntakeSubsystem must remain independent of vendor APIs.

#### Electrical Telemetry

The approved telemetry contract may include:

- Applied output.
- Supply voltage.
- Supply current.
- Stator current.
- Motor temperature.
- Motor velocity.
- Connected state.
- Configuration health.

The responsibility chain remains:

```text
IntakeIOTalonFX
→ Read hardware signals

IntakeIOInputs
→ Transport mutable IO data

IntakeSubsystem
→ Own the latest IO snapshot

IntakeObservation
→ Expose immutable robot-level values

IntakeTelemetryFacade
→ Publish typed NetworkTables topics

RobotTelemetry
→ Coordinate read-only publishing
```

Telemetry must never command the Intake mechanism.

#### Simulation Compatibility

`IntakeIOSim` must remain deterministic.

Simulation must:

- Preserve the same IntakeIO contract.
- Provide compatible values for approved inputs.
- Publish the same NetworkTables topic types.
- Preserve inherited manual control behavior.
- Avoid unnecessary electrical or mechanism physics.

---

### Architecture Rules

- Commands may call subsystem methods only.
- Commands must not access hardware objects.
- Commands must not access vendor APIs.
- Controls process Xbox input only.
- Controls must not access subsystems or IO.
- Subsystems own IO input snapshots.
- Subsystems must not configure hardware.
- Hardware configuration belongs inside real IO implementations.
- Phoenix 6 types must not escape the IO package.
- Observations must remain immutable and read-only.
- Telemetry facades publish values only.
- RobotTelemetry coordinates telemetry only.
- RobotContainer wires dependencies only.
- Constants contain configuration values.
- No magic numbers.
- No deprecated APIs.
- No unnecessary abstractions.
- No unrelated refactoring.

---

### Expected Files to Review

```text
src/main/java/frc/robot/Constants.java
src/main/java/frc/robot/io/intake/IntakeIO.java
src/main/java/frc/robot/io/intake/IntakeIOTalonFX.java
src/main/java/frc/robot/io/intake/IntakeIOSim.java
src/main/java/frc/robot/observation/intake/IntakeObservation.java
src/main/java/frc/robot/subsystems/IntakeSubsystem.java
src/main/java/frc/robot/telemetry/intake/IntakeTelemetryFacade.java
src/main/java/frc/robot/telemetry/RobotTelemetry.java
```

Not every listed file must be modified.

A file may be changed only when the approved lesson design requires it.

---

### Out of Scope

D01_L06 does not include:

- Game-piece detection.
- Beam-break sensor integration.
- CANcoder integration.
- Intake state machine.
- Automatic Intake control.
- Automatic hold.
- Automatic stop.
- Jam detection.
- Jam recovery.
- Autonomous Intake commands.
- Shooter coordination.
- Loader coordination.
- Closed-loop velocity control.
- PID tuning.
- Feedforward tuning.

---

### Current Status

```text
LESSON
D01_L06_Intake_Complete_Foundation

STATUS
COMPLETE

INHERITED BASELINE
PASS

BASELINE BUILD
PASS

ARCHITECTURE AUDIT
PASS

SAFETY CONFIGURATION
PASS

ELECTRICAL TELEMETRY
PASS

SIMULATION TEST
PASS

GLASS TEST
PASS

REAL ROBOT TEST
PASS

DRIVE REGRESSION
PASS

FREEZE STATUS
FROZEN
```

D01_L06 implementation and verification are complete.

---

### Build

Run from the D01_L06 lesson root:

```powershell
.\gradlew.bat clean build --no-daemon
```

Current baseline result:

```text
BUILD SUCCESSFUL
```

---

### Planned Verification

The lesson requires:

- Architecture audit.
- TalonFX configuration review.
- Configuration implementation review.
- Clean build.
- WPILib Simulation.
- Intake control regression.
- Existing telemetry regression.
- New electrical telemetry verification.
- Glass verification.
- Real robot verification.
- Inherited drivetrain verification.

---

### Safety

- Keep hands and loose objects away from the Intake.
- Use low output during initial verification.
- Test one configuration concept at a time.
- Verify motor direction before extended operation.
- Monitor supply current, stator current, and temperature.
- Disable the robot immediately after unexpected behavior.
- Do not bypass approved current limits.
- Do not intentionally jam the mechanism.
- Real robot operation remains the user's responsibility.

---

## Tiếng Việt

### Mục đích bài học

D01_L06 hoàn thiện nền tảng phần cứng Intake bằng cách bổ sung cấu hình TalonFX an toàn và telemetry điện chỉ đọc.

Bài học kế thừa toàn bộ implementation Intake đã hoàn thành từ:

```text
D01_L05_Intake_Foundation
```

D01_L05 tiếp tục ở trạng thái hoàn thành, đóng băng và không được sửa đổi.

D01_L06 không bổ sung hành vi Intake tự động, phát hiện game piece, state machine hoặc xử lý kẹt.

Mục đích của bài là làm cho cơ cấu Intake thủ công đã kế thừa trở nên an toàn hơn, dễ quan sát hơn và sẵn sàng cho các hành vi thông minh trong tương lai.

---

### Mô hình phát triển

Bài học tuân theo Inheritance Development.

```text
D01_L05_Intake_Foundation
→ Sao chép
→ Đổi tên
→ D01_L06_Intake_Complete_Foundation
→ Baseline Build
→ Thay đổi bài học có kiểm soát
```

Trước khi triển khai nội dung riêng của bài:

- D01_L05 đã được sao chép.
- Thư mục sao chép đã được đổi tên.
- Không có file Java kế thừa nào bị chủ động sửa đổi.
- Không có ranh giới kiến trúc nào bị thay đổi.
- Baseline clean build đã thành công.

Kết quả baseline:

```text
BUILD SUCCESSFUL
```

---

### Bài học trước

```text
D01_L05_Intake_Foundation
```

Bài học trước đã hoàn thành:

- Điều khiển Intake thủ công.
- Điều khiển Outtake thủ công.
- Xử lý Right Trigger và Left Trigger.
- Tích hợp IntakeSubsystem.
- IntakeIO abstraction.
- Implementation TalonFX cho robot thật.
- Implementation simulation xác định.
- Intake telemetry chỉ đọc.
- Publishing qua NetworkTables.
- Xác minh bằng Glass.
- Kiểm thử WPILib Simulation.
- Kiểm thử Intake trên robot thật.

---

### Mục tiêu bài học

Hoàn thiện nền tảng phần cứng Intake bằng cách triển khai các nội dung được phê duyệt:

- Cấu hình TalonFX an toàn.
- Supply current limit.
- Stator current limit.
- Neutral mode.
- Open-loop ramp.
- Giới hạn output thuận và nghịch.
- Xác minh kết quả áp dụng configuration.
- Electrical telemetry chỉ đọc.
- Các giá trị telemetry tương thích simulation.
- Xác minh bằng Glass.
- Xác minh trên robot thật.

Hành vi điều khiển Intake kế thừa phải giữ nguyên.

---

### Kiến trúc đóng băng

Dự án giữ nguyên hướng dependency:

```text
Driver
→ Xbox Controller
→ controls
→ commands
→ subsystems
→ io
→ Hardware / Simulation
```

Luồng điều khiển Intake giữ nguyên:

```text
Driver
→ Xbox Controller
→ IntakeInputProcessor
→ ManualIntakeCommand
→ IntakeSubsystem
→ IntakeIO
    ├── IntakeIOTalonFX
    └── IntakeIOSim
→ Hardware / Simulation
```

Luồng Intake telemetry tiếp tục chỉ đọc:

```text
IntakeIO
→ IntakeIOInputs
→ IntakeSubsystem
→ IntakeObservation
→ RobotTelemetry
→ IntakeTelemetryFacade
→ NetworkTables / Glass
```

---

### Trách nhiệm của RobotContainer

`RobotContainer` tiếp tục chỉ là composition root.

Được phép thực hiện:

- Tạo object.
- Chọn implementation real hoặc simulation.
- Dependency injection.
- Cấu hình default command.
- Controller bindings.
- Kết nối dependency telemetry.

Không được chứa:

- Xử lý input của người lái.
- Business logic của Intake.
- Cấu hình phần cứng.
- Tính toán điện.
- Đánh giá fault.
- Quyết định current limit.
- Tính toán telemetry định kỳ.

---

### Phần cứng đã xác nhận

| Hạng mục | Giá trị |
| --- | --- |
| Cơ cấu | Intake |
| Motor | Kraken X60 |
| Motor Controller | CTRE TalonFX |
| CAN ID | 12 |
| Encoder | Integrated TalonFX Encoder |
| Kiểu điều khiển | Open-loop output |
| Real IO | IntakeIOTalonFX |
| Simulation IO | IntakeIOSim |
| Intake | Xbox Right Trigger |
| Outtake | Xbox Left Trigger |

---

### Hành vi Intake kế thừa

Hành vi thủ công giữ nguyên:

```text
Right Trigger
→ Intake

Left Trigger
→ Outtake

Không nhấn trigger
→ Stop

Nhấn cả hai trigger
→ Giữ kết quả an toàn đã kế thừa
```

Các hành vi an toàn kế thừa:

- Thả trigger sẽ dừng motor.
- Command bị interrupt sẽ dừng motor.
- Robot Disable sẽ dừng motor.
- Real và simulation sử dụng cùng IntakeIO contract.

---

### Phạm vi bài học

#### Cấu hình an toàn TalonFX

Implementation `IntakeIOTalonFX` hiện tại sẽ được audit trước.

Cấu hình được phê duyệt có thể gồm:

- Motor inversion.
- Neutral mode.
- Supply current limit.
- Stator current limit.
- Open-loop ramp.
- Peak forward output.
- Peak reverse output.
- Kết quả áp dụng configuration.

Mọi giá trị configuration được phê duyệt phải nằm trong `Constants.java`.

Mọi Phoenix 6 configuration API phải nằm trong `IntakeIOTalonFX`.

IntakeSubsystem phải tiếp tục độc lập với vendor API.

#### Electrical Telemetry

Telemetry contract được phê duyệt có thể gồm:

- Applied output.
- Supply voltage.
- Supply current.
- Stator current.
- Motor temperature.
- Motor velocity.
- Connected state.
- Configuration health.

Chuỗi trách nhiệm giữ nguyên:

```text
IntakeIOTalonFX
→ Đọc tín hiệu phần cứng

IntakeIOInputs
→ Vận chuyển dữ liệu IO có thể cập nhật

IntakeSubsystem
→ Sở hữu snapshot IO mới nhất

IntakeObservation
→ Cung cấp giá trị robot-level bất biến

IntakeTelemetryFacade
→ Publish typed NetworkTables topics

RobotTelemetry
→ Điều phối publishing chỉ đọc
```

Telemetry không được điều khiển cơ cấu Intake.

#### Tương thích Simulation

`IntakeIOSim` phải giữ tính xác định.

Simulation phải:

- Giữ nguyên IntakeIO contract.
- Cung cấp giá trị tương thích cho các input được phê duyệt.
- Publish cùng kiểu NetworkTables topic.
- Giữ nguyên hành vi điều khiển thủ công kế thừa.
- Không thêm mô phỏng điện hoặc cơ khí không cần thiết.

---

### Quy tắc kiến trúc

- Command chỉ được gọi phương thức subsystem.
- Command không được truy cập hardware object.
- Command không được truy cập vendor API.
- Controls chỉ xử lý Xbox input.
- Controls không được truy cập subsystem hoặc IO.
- Subsystem sở hữu snapshot IO inputs.
- Subsystem không được cấu hình hardware.
- Hardware configuration chỉ nằm trong real IO implementation.
- Phoenix 6 types không được thoát khỏi IO package.
- Observation phải bất biến và chỉ đọc.
- Telemetry facade chỉ publish giá trị.
- RobotTelemetry chỉ điều phối telemetry.
- RobotContainer chỉ kết nối dependency.
- Constants chứa các giá trị configuration.
- Không dùng magic number.
- Không dùng deprecated API.
- Không tạo abstraction không cần thiết.
- Không refactor nội dung ngoài phạm vi.

---

### Các file dự kiến cần kiểm tra

```text
src/main/java/frc/robot/Constants.java
src/main/java/frc/robot/io/intake/IntakeIO.java
src/main/java/frc/robot/io/intake/IntakeIOTalonFX.java
src/main/java/frc/robot/io/intake/IntakeIOSim.java
src/main/java/frc/robot/observation/intake/IntakeObservation.java
src/main/java/frc/robot/subsystems/IntakeSubsystem.java
src/main/java/frc/robot/telemetry/intake/IntakeTelemetryFacade.java
src/main/java/frc/robot/telemetry/RobotTelemetry.java
```

Không phải mọi file trong danh sách đều bắt buộc phải sửa.

Chỉ sửa file khi thiết kế bài học đã được phê duyệt yêu cầu.

---

### Ngoài phạm vi

D01_L06 không bao gồm:

- Phát hiện game piece.
- Beam-break sensor.
- CANcoder.
- Intake state machine.
- Điều khiển Intake tự động.
- Automatic hold.
- Automatic stop.
- Jam detection.
- Jam recovery.
- Autonomous Intake command.
- Shooter coordination.
- Loader coordination.
- Closed-loop velocity control.
- PID tuning.
- Feedforward tuning.

---

### Trạng thái hiện tại

```text
LESSON
D01_L06_Intake_Complete_Foundation

STATUS
COMPLETE

INHERITED BASELINE
PASS

BASELINE BUILD
PASS

ARCHITECTURE AUDIT
PASS

SAFETY CONFIGURATION
PASS

ELECTRICAL TELEMETRY
PASS

SIMULATION TEST
PASS

GLASS TEST
PASS

REAL ROBOT TEST
PASS

DRIVE REGRESSION
PASS

FREEZE STATUS
FROZEN
```

D01_L06 đã hoàn thành implementation và verification.

---

### Build

Chạy từ thư mục gốc của D01_L06:

```powershell
.\gradlew.bat clean build --no-daemon
```

Kết quả baseline hiện tại:

```text
BUILD SUCCESSFUL
```

---

### Kế hoạch xác minh

Bài học yêu cầu:

- Architecture audit.
- Kiểm tra configuration TalonFX.
- Review implementation configuration.
- Clean build.
- WPILib Simulation.
- Kiểm tra hồi quy điều khiển Intake.
- Kiểm tra hồi quy telemetry hiện tại.
- Xác minh electrical telemetry mới.
- Xác minh bằng Glass.
- Xác minh trên robot thật.
- Xác minh drivetrain kế thừa.

---

### An toàn

- Giữ tay và vật lỏng tránh xa Intake.
- Dùng output thấp trong lần kiểm thử đầu tiên.
- Mỗi lần chỉ kiểm thử một khái niệm configuration.
- Xác minh chiều motor trước khi chạy lâu.
- Theo dõi supply current, stator current và temperature.
- Disable robot ngay khi có hành vi bất thường.
- Không bỏ qua current limit đã được phê duyệt.
- Không chủ động làm kẹt cơ cấu.
- Việc vận hành robot thật thuộc trách nhiệm của người dùng.
