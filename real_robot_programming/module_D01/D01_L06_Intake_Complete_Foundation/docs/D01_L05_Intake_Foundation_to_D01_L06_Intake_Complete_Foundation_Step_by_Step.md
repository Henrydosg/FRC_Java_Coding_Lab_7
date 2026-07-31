# D01_L05 Intake Foundation to D01_L06 Intake Complete Foundation

## Transition Summary / Tóm tắt chuyển đổi

**English:** D01_L06 inherits the frozen D01_L05 drivetrain and manual Intake control path. It
completes the Intake hardware foundation by adding TalonFX safety configuration, grouped
electrical observations, deterministic simulation values, and typed read-only telemetry.

**Tiếng Việt:** D01_L06 kế thừa hệ truyền động và luồng điều khiển Intake thủ công đã đóng băng
của D01_L05. Bài học hoàn thiện nền tảng phần cứng Intake bằng cách thêm cấu hình an toàn TalonFX,
observation điện theo nhóm, giá trị mô phỏng xác định và telemetry chỉ đọc có kiểu dữ liệu.

## Step 1 - Inherit and Activate D01_L06 / Kế thừa và kích hoạt D01_L06

### Objective / Mục tiêu

**English:** Create D01_L06 from the completed D01_L05 project.

**Tiếng Việt:** Tạo D01_L06 từ project D01_L05 đã hoàn thành.

### Why / Tại sao

**English:** Direct inheritance preserves the verified drivetrain, Intake controls, commands,
subsystem, IO implementations, simulation path, and telemetry path.

**Tiếng Việt:** Kế thừa trực tiếp bảo toàn hệ truyền động, điều khiển Intake, command, subsystem,
IO implementation, luồng mô phỏng và luồng telemetry đã được xác minh.

### Action / Thao tác

**English:** Copy D01_L05, rename the project to
`D01_L06_Intake_Complete_Foundation`, remove generated artifacts, and run the baseline clean
build before lesson-specific implementation.

**Tiếng Việt:** Sao chép D01_L05, đổi tên project thành
`D01_L06_Intake_Complete_Foundation`, xóa artifact được tạo tự động và chạy baseline clean build
trước khi implementation nội dung riêng của bài học.

### Files Changed / Tệp thay đổi

- `README.md`
- `LESSON_STATUS.md`

### Verification / Xác minh

**English:** The D01_L06 lesson record reports the inheritance baseline and baseline clean build
as PASS.

**Tiếng Việt:** Hồ sơ D01_L06 ghi nhận inheritance baseline và baseline clean build là PASS.

### Expected Result / Kết quả mong đợi

**English:** D01_L06 starts with the completed D01_L05 architecture and behavior.

**Tiếng Việt:** D01_L06 bắt đầu với kiến trúc và hành vi đã hoàn thành của D01_L05.

## Step 2 - Approve the Intake Completion Architecture / Phê duyệt kiến trúc hoàn thiện Intake

### Objective / Mục tiêu

**English:** Extend Intake observations and real-hardware configuration without changing the
Frozen Backbone.

**Tiếng Việt:** Mở rộng observation Intake và cấu hình phần cứng thật mà không thay đổi Frozen
Backbone.

### Why / Tại sao

**English:** Hardware configuration belongs in the real IO implementation, hardware observations
must cross the IO inputs snapshot, and telemetry must remain read-only.

**Tiếng Việt:** Cấu hình phần cứng thuộc real IO implementation, observation phần cứng phải đi qua
IO inputs snapshot và telemetry phải luôn chỉ đọc.

### Action / Thao tác

**English:** Preserve the approved control and observation paths:

```text
CONTROL
Driver
-> Xbox Controller
-> IntakeInputProcessor
-> ManualIntakeCommand
-> IntakeSubsystem
-> IntakeIO
-> IntakeIOTalonFX or IntakeIOSim

OBSERVATION
IntakeIO
-> IntakeIOInputs
-> IntakeSubsystem
-> IntakeObservation
-> RobotTelemetry
-> IntakeTelemetryFacade
-> NetworkTables / Glass
```

Keep Phoenix 6 types inside `IntakeIOTalonFX`. Keep `RobotContainer` as the composition root.

**Tiếng Việt:** Bảo toàn hai luồng điều khiển và observation ở trên. Giữ kiểu Phoenix 6 bên trong
`IntakeIOTalonFX`. Giữ `RobotContainer` chỉ là composition root.

### Files Changed / Tệp thay đổi

- No production files / Không có tệp production

### Verification / Xác minh

**English:** D01_L06 records the Frozen Architecture, RobotContainer boundary, dependency
direction, vendor isolation, read-only telemetry, and architecture audit as PASS.

**Tiếng Việt:** D01_L06 ghi nhận Frozen Architecture, ranh giới RobotContainer, hướng dependency,
vendor isolation, telemetry chỉ đọc và architecture audit là PASS.

### Expected Result / Kết quả mong đợi

**English:** The lesson can extend Intake capability without moving responsibilities between
packages.

**Tiếng Việt:** Bài học có thể mở rộng khả năng Intake mà không di chuyển trách nhiệm giữa các
package.

## Step 3 - Add Approved Intake Configuration Constants / Thêm hằng số cấu hình Intake

### Objective / Mục tiêu

**English:** Store TalonFX safety configuration and telemetry topic names in the configuration
authority.

**Tiếng Việt:** Lưu cấu hình an toàn TalonFX và tên topic telemetry trong nơi quản lý cấu hình.

### Why / Tại sao

**English:** Approved hardware values and stable NetworkTables keys must not be unexplained
literals inside implementation code.

**Tiếng Việt:** Giá trị phần cứng đã phê duyệt và key NetworkTables ổn định không được là literal
không giải thích trong implementation.

### Action / Thao tác

**English:** Extend `IntakeConstants` with inversion, brake mode, supply and stator current
limits, enable flags, open-loop ramp, and peak duty-cycle limits. Set the recorded manual Intake
and Outtake outputs to `0.5` and `-0.5`, matching the configured peak limits. Extend
`TelemetryConstants` with keys for supply voltage, supply current, stator current, temperature,
position, velocity, and configuration health.

**Tiếng Việt:** Mở rộng `IntakeConstants` với inversion, brake mode, giới hạn supply và stator
current, enable flag, open-loop ramp và giới hạn peak duty cycle. Đặt output Intake và Outtake đã
ghi nhận thành `0.5` và `-0.5`, khớp với peak limit đã cấu hình. Mở rộng
`TelemetryConstants` với key cho supply voltage, supply current, stator current, temperature,
position, velocity và configuration health.

### Files Changed / Tệp thay đổi

- Modified `src/main/java/frc/robot/Constants.java`

### Verification / Xác minh

**English:** Source comparison confirms the constants are named and centralized. D01_L06 records
all safety-configuration items as PASS.

**Tiếng Việt:** So sánh source xác nhận các hằng số được đặt tên và quản lý tập trung. D01_L06 ghi
nhận tất cả mục safety configuration là PASS.

### Expected Result / Kết quả mong đợi

**English:** Intake hardware configuration and telemetry names have one explicit authority.

**Tiếng Việt:** Cấu hình phần cứng Intake và tên telemetry có một nơi quản lý rõ ràng.

## Step 4 - Extend the Intake IO Snapshot / Mở rộng Intake IO snapshot

### Objective / Mục tiêu

**English:** Transport approved electrical and configuration observations through the
vendor-independent IO contract.

**Tiếng Việt:** Vận chuyển electrical observation và configuration observation đã phê duyệt qua
IO contract độc lập vendor.

### Why / Tại sao

**English:** Subsystems and telemetry must receive hardware observations through
`IntakeIOInputs`; they must not read the TalonFX directly.

**Tiếng Việt:** Subsystem và telemetry phải nhận hardware observation qua `IntakeIOInputs`;
chúng không được đọc TalonFX trực tiếp.

### Action / Thao tác

**English:** Add supply voltage, supply current, stator current, temperature, and configuration
health fields to `IntakeIOInputs`. Retain applied output, rotor position, rotor velocity, and
connected state.

**Tiếng Việt:** Thêm field supply voltage, supply current, stator current, temperature và
configuration health vào `IntakeIOInputs`. Giữ applied output, rotor position, rotor velocity và
connected state.

### Files Changed / Tệp thay đổi

- Modified `src/main/java/frc/robot/io/intake/IntakeIO.java`

### Verification / Xác minh

**English:** Source inspection confirms the interface contains only mechanism operations and a
mutable one-cycle inputs snapshot. It exposes no Phoenix 6 or NetworkTables type.

**Tiếng Việt:** Kiểm tra source xác nhận interface chỉ chứa operation của mechanism và inputs
snapshot mutable cho một chu kỳ. Interface không lộ kiểu Phoenix 6 hoặc NetworkTables.

### Expected Result / Kết quả mong đợi

**English:** Real and simulation implementations can provide equivalent observations without
changing the subsystem dependency.

**Tiếng Việt:** Real implementation và simulation implementation có thể cung cấp observation
tương đương mà không thay đổi dependency của subsystem.

## Step 5 - Configure the Intake TalonFX / Cấu hình Intake TalonFX

### Objective / Mục tiêu

**English:** Apply the approved TalonFX safety settings inside the real IO implementation.

**Tiếng Việt:** Áp dụng cấu hình an toàn TalonFX đã phê duyệt bên trong real IO implementation.

### Why / Tại sao

**English:** Vendor configuration is hardware-specific and therefore belongs only in
`IntakeIOTalonFX`.

**Tiếng Việt:** Cấu hình vendor phụ thuộc phần cứng nên chỉ thuộc `IntakeIOTalonFX`.

### Action / Thao tác

**English:** Build a `TalonFXConfiguration` from `IntakeConstants`, configure inversion, neutral
mode, peak forward and reverse duty cycle, supply and stator current limits, and duty-cycle
open-loop ramp. Apply the configuration through the TalonFX configurator and retain whether the
application status is healthy.

**Tiếng Việt:** Tạo `TalonFXConfiguration` từ `IntakeConstants`, cấu hình inversion, neutral mode,
peak forward và reverse duty cycle, giới hạn supply và stator current, cùng duty-cycle open-loop
ramp. Apply cấu hình qua TalonFX configurator và lưu trạng thái application có healthy hay không.

### Files Changed / Tệp thay đổi

- Modified `src/main/java/frc/robot/io/intake/IntakeIOTalonFX.java`

### Verification / Xác minh

**English:** Source inspection confirms all Phoenix 6 configuration remains in the real IO class.
D01_L06 records configuration application and real-robot safety configuration as PASS.

**Tiếng Việt:** Kiểm tra source xác nhận mọi cấu hình Phoenix 6 nằm trong real IO class. D01_L06
ghi nhận configuration application và safety configuration trên robot thật là PASS.

### Expected Result / Kết quả mong đợi

**English:** The Intake TalonFX starts stopped and uses the approved safety limits.

**Tiếng Việt:** TalonFX Intake khởi động ở trạng thái dừng và dùng các giới hạn an toàn đã phê
duyệt.

## Step 6 - Read Grouped TalonFX Signals / Đọc signal TalonFX theo nhóm

### Objective / Mục tiêu

**English:** Populate the complete Intake snapshot from Phoenix 6 status signals.

**Tiếng Việt:** Điền đầy đủ Intake snapshot từ Phoenix 6 status signal.

### Why / Tại sao

**English:** One grouped refresh provides a consistent periodic observation boundary and a
connection result without exposing hardware outside IO.

**Tiếng Việt:** Một grouped refresh cung cấp observation boundary nhất quán theo chu kỳ và kết quả
connection mà không lộ phần cứng ra ngoài IO.

### Action / Thao tác

**English:** Cache duty cycle, rotor position, rotor velocity, supply voltage, supply current,
stator current, and device temperature signals. Refresh them together in `updateInputs()`,
convert values to explicit units, set `connected` from the refresh status, and publish the stored
configuration-health result into the inputs snapshot.

**Tiếng Việt:** Lưu duty cycle, rotor position, rotor velocity, supply voltage, supply current,
stator current và device temperature signal. Refresh chúng cùng nhau trong `updateInputs()`,
chuyển giá trị sang unit rõ ràng, đặt `connected` từ refresh status và đưa kết quả
configuration-health đã lưu vào inputs snapshot.

### Files Changed / Tệp thay đổi

- Modified `src/main/java/frc/robot/io/intake/IntakeIOTalonFX.java`

### Verification / Xác minh

**English:** Source inspection confirms the IO implementation fully updates every approved input.
D01_L06 records TalonFX connection, electrical telemetry, and configuration health as PASS.

**Tiếng Việt:** Kiểm tra source xác nhận IO implementation cập nhật đầy đủ mọi input đã phê duyệt.
D01_L06 ghi nhận TalonFX connection, electrical telemetry và configuration health là PASS.

### Expected Result / Kết quả mong đợi

**English:** The subsystem receives real applied output, rotor, electrical, temperature,
connection, and configuration observations through `IntakeIOInputs`.

**Tiếng Việt:** Subsystem nhận applied output, rotor, electrical, temperature, connection và
configuration observation thật qua `IntakeIOInputs`.

## Step 7 - Preserve Deterministic Simulation Compatibility / Bảo toàn mô phỏng xác định

### Objective / Mục tiêu

**English:** Make `IntakeIOSim` populate the extended IO snapshot without adding mechanism
physics.

**Tiếng Việt:** Làm cho `IntakeIOSim` điền extended IO snapshot mà không thêm physics của
mechanism.

### Why / Tại sao

**English:** Simulation must remain substitutable for real IO and publish the same typed
observation fields.

**Tiếng Việt:** Simulation phải thay thế được real IO và publish cùng các observation field có
kiểu dữ liệu.

### Action / Thao tác

**English:** Retain commanded-output clamping and safe stop. Add deterministic values for supply
voltage, supply current, stator current, temperature, and configuration health while retaining
the existing deterministic position, velocity, and connected values.

**Tiếng Việt:** Giữ việc clamp commanded output và safe stop. Thêm giá trị xác định cho supply
voltage, supply current, stator current, temperature và configuration health, đồng thời giữ các
giá trị position, velocity và connected xác định hiện có.

### Files Changed / Tệp thay đổi

- Modified `src/main/java/frc/robot/io/intake/IntakeIOSim.java`

### Verification / Xác minh

**English:** Source inspection confirms every extended field is assigned on each update. D01_L06
records simulation startup, IO selection, manual control, stop behavior, telemetry, and
deterministic values as PASS.

**Tiếng Việt:** Kiểm tra source xác nhận mọi extended field được gán trong mỗi lần update. D01_L06
ghi nhận simulation startup, IO selection, manual control, stop behavior, telemetry và giá trị
xác định là PASS.

### Expected Result / Kết quả mong đợi

**English:** Simulation remains deterministic and contract-compatible with the real Intake IO.

**Tiếng Việt:** Simulation vẫn xác định và tương thích contract với real Intake IO.

## Step 8 - Extend the Immutable Intake Observation / Mở rộng Intake observation bất biến

### Objective / Mục tiêu

**English:** Expose the extended Intake snapshot as one immutable robot-level value.

**Tiếng Việt:** Cung cấp extended Intake snapshot dưới dạng một giá trị bất biến ở cấp robot.

### Why / Tại sao

**English:** Telemetry should consume an immutable observation rather than mutable IO inputs.

**Tiếng Việt:** Telemetry nên dùng observation bất biến thay vì IO inputs mutable.

### Action / Thao tác

**English:** Add supply voltage, supply current, stator current, temperature, rotor position,
rotor velocity, and configuration health to `IntakeObservation`. Update
`IntakeSubsystem.getObservation()` to copy the latest input values and subsystem-owned Intake
mode into the record.

**Tiếng Việt:** Thêm supply voltage, supply current, stator current, temperature, rotor position,
rotor velocity và configuration health vào `IntakeObservation`. Cập nhật
`IntakeSubsystem.getObservation()` để sao chép input mới nhất và Intake mode do subsystem sở hữu
vào record.

### Files Changed / Tệp thay đổi

- Modified `src/main/java/frc/robot/observation/intake/IntakeObservation.java`
- Modified `src/main/java/frc/robot/subsystems/IntakeSubsystem.java`

### Verification / Xác minh

**English:** Source inspection confirms the subsystem remains vendor-independent and telemetry
receives an immutable record.

**Tiếng Việt:** Kiểm tra source xác nhận subsystem vẫn độc lập vendor và telemetry nhận một record
bất biến.

### Expected Result / Kết quả mong đợi

**English:** The observation boundary carries all approved Intake values without exposing mutable
IO state.

**Tiếng Việt:** Observation boundary mang mọi giá trị Intake đã phê duyệt mà không lộ IO state
mutable.

## Step 9 - Publish Typed Intake Electrical Telemetry / Publish telemetry điện Intake có kiểu

### Objective / Mục tiêu

**English:** Publish the extended immutable observation through the existing Intake telemetry
facade.

**Tiếng Việt:** Publish extended immutable observation qua Intake telemetry facade hiện có.

### Why / Tại sao

**English:** Typed publishers provide stable NetworkTables contracts while keeping telemetry
read-only.

**Tiếng Việt:** Typed publisher cung cấp NetworkTables contract ổn định trong khi giữ telemetry
chỉ đọc.

### Action / Thao tác

**English:** Add typed publishers for:

```text
/Intake/SupplyVoltageVolts
/Intake/SupplyCurrentAmps
/Intake/StatorCurrentAmps
/Intake/TemperatureCelsius
/Intake/PositionRotations
/Intake/VelocityRPM
/Intake/ConfigurationHealthy
```

Publish the matching `IntakeObservation` values and close every publisher in `close()`. Retain
the inherited applied-output, mode, and connected topics.

**Tiếng Việt:** Thêm typed publisher cho các topic ở trên, publish giá trị
`IntakeObservation` tương ứng và đóng mọi publisher trong `close()`. Giữ các topic applied
output, mode và connected đã kế thừa.

### Files Changed / Tệp thay đổi

- Modified `src/main/java/frc/robot/telemetry/intake/IntakeTelemetryFacade.java`

### Verification / Xác minh

**English:** Source inspection confirms the facade only publishes immutable values. D01_L06
records NetworkTables and Glass publishing as PASS.

**Tiếng Việt:** Kiểm tra source xác nhận facade chỉ publish giá trị bất biến. D01_L06 ghi nhận
NetworkTables và Glass publishing là PASS.

### Expected Result / Kết quả mong đợi

**English:** Glass and NetworkTables receive the complete approved Intake observation without any
control side effect.

**Tiếng Việt:** Glass và NetworkTables nhận đầy đủ Intake observation đã phê duyệt mà không có
control side effect.

## Step 10 - Verify and Freeze D01_L06 / Xác minh và đóng băng D01_L06

### Objective / Mục tiêu

**English:** Record the verified Intake foundation as the frozen baseline for D01_L07.

**Tiếng Việt:** Ghi nhận Intake foundation đã xác minh thành frozen baseline cho D01_L07.

### Why / Tại sao

**English:** Inheritance Development requires a successful build, architecture review, runtime
verification, documentation, and explicit completion state before the next lesson inherits.

**Tiếng Việt:** Inheritance Development yêu cầu build thành công, architecture review, runtime
verification, documentation và trạng thái hoàn thành rõ ràng trước khi bài học tiếp theo kế thừa.

### Action / Thao tác

**English:** Review the final source scope, run the final clean build, verify simulation and Glass,
perform the recorded real-robot and regression checks, update the lesson records, and freeze the
lesson.

**Tiếng Việt:** Review phạm vi source cuối cùng, chạy final clean build, xác minh simulation và
Glass, thực hiện real-robot và regression check đã ghi nhận, cập nhật hồ sơ bài học và đóng băng
bài học.

### Files Changed / Tệp thay đổi

- `README.md`
- `LESSON_STATUS.md`
- `docs/D01_L05_Intake_Foundation_to_D01_L06_Intake_Complete_Foundation_Step_by_Step.md`

### Verification / Xác minh

**English:** The D01_L06 verification record reports:

- Architecture audit: PASS
- Implementation review: PASS
- Clean build: PASS
- WPILib Simulation: PASS
- Glass verification: PASS
- Real robot verification: PASS
- Drive regression: PASS
- Intake regression: PASS

This guide does not add or reinterpret test evidence.

**Tiếng Việt:** Hồ sơ xác minh D01_L06 ghi nhận:

- Architecture audit: PASS
- Implementation review: PASS
- Clean build: PASS
- WPILib Simulation: PASS
- Glass verification: PASS
- Real robot verification: PASS
- Drive regression: PASS
- Intake regression: PASS

Hướng dẫn này không thêm hoặc diễn giải lại bằng chứng kiểm thử.

### Expected Result / Kết quả mong đợi

**English:** D01_L06 is documented as `COMPLETE` and `FROZEN` and can serve as the inherited
baseline for D01_L07.

**Tiếng Việt:** D01_L06 được ghi nhận `COMPLETE` và `FROZEN` và có thể làm inherited baseline cho
D01_L07.

## Final Architecture / Kiến trúc cuối cùng

```text
CONTROL
Driver
-> Xbox Controller
-> IntakeInputProcessor
-> ManualIntakeCommand
-> IntakeSubsystem
-> IntakeIO
-> IntakeIOTalonFX or IntakeIOSim
-> Hardware or Simulation

OBSERVATION
IntakeIO
-> IntakeIOInputs
-> IntakeSubsystem
-> IntakeObservation
-> RobotTelemetry
-> IntakeTelemetryFacade
-> NetworkTables / Glass
```

**English:** Source comparison identifies changes only in `Constants.java`, the Intake IO
contract and implementations, `IntakeObservation`, `IntakeSubsystem`, and
`IntakeTelemetryFacade`. The inherited drivetrain, controls, commands, `RobotContainer`, and
`RobotTelemetry` source remain unchanged.

**Tiếng Việt:** So sánh source xác định thay đổi chỉ nằm trong `Constants.java`, Intake IO
contract và implementation, `IntakeObservation`, `IntakeSubsystem` và
`IntakeTelemetryFacade`. Source hệ truyền động, controls, commands, `RobotContainer` và
`RobotTelemetry` được kế thừa vẫn không thay đổi.
