# D01_L03 Drive Observation Publishing to D01_L04 Robot Telemetry Runtime Integration

## Transition Summary / Tóm tắt chuyển đổi

**English:** D01_L04 inherits the frozen D01_L03 drivetrain, IO, immutable observation, and typed
publishing facade. It adds the smallest robot-level runtime connection: `RobotTelemetry` reads
`DriveSubsystem.getObservation()` and publishes it through `DriveTelemetryFacade` after the
command scheduler runs.

**Tiếng Việt:** D01_L04 kế thừa hệ truyền động, IO, observation bất biến và facade publishing có
kiểu dữ liệu đã đóng băng của D01_L03. Bài học thêm kết nối runtime cấp robot nhỏ nhất:
`RobotTelemetry` đọc `DriveSubsystem.getObservation()` và publish qua `DriveTelemetryFacade` sau
khi command scheduler chạy.

## Step 1 - Inherit and Activate D01_L04 / Kế thừa và kích hoạt D01_L04

### Objective / Mục tiêu

**English:** Create the active D01_L04 project from completed D01_L03.

**Tiếng Việt:** Tạo project D01_L04 đang hoạt động từ D01_L03 đã hoàn thành.

### Why / Tại sao

**English:** Direct inheritance preserves the verified control pipeline, IO abstraction,
simulation implementation, observation boundary, and telemetry facade.

**Tiếng Việt:** Kế thừa trực tiếp bảo toàn control pipeline, IO abstraction, simulation
implementation, observation boundary và telemetry facade đã được xác minh.

### Action / Thao tác

**English:** Copy D01_L03, update the lesson identity and objective, mark the lesson
`IN_PROGRESS`, remove generated artifacts, and run the baseline clean build.

**Tiếng Việt:** Sao chép D01_L03, cập nhật tên và mục tiêu bài học, đặt trạng thái
`IN_PROGRESS`, xóa artifact được sinh tự động và chạy baseline clean build.

### Files Changed / Tệp thay đổi

- `README.md`
- `LESSON_STATUS.md`

### Verification / Xác minh

**English:** The baseline clean build passed and D01_L03 remained unchanged.

**Tiếng Việt:** Baseline clean build đã PASS và D01_L03 không thay đổi.

### Expected Result / Kết quả mong đợi

**English:** D01_L04 starts from the exact completed D01_L03 architecture.

**Tiếng Việt:** D01_L04 bắt đầu từ đúng kiến trúc D01_L03 đã hoàn thành.

## Step 2 - Approve the Runtime Architecture / Phê duyệt kiến trúc runtime

### Objective / Mục tiêu

**English:** Define the smallest valid runtime architecture for publishing drive observations.

**Tiếng Việt:** Xác định kiến trúc runtime hợp lệ nhỏ nhất để publish drive observation.

### Why / Tại sao

**English:** Runtime integration requires a telemetry coordinator, but telemetry must remain
read-only and `RobotContainer` must remain a composition root.

**Tiếng Việt:** Tích hợp runtime cần một telemetry coordinator, nhưng telemetry phải luôn chỉ đọc
và `RobotContainer` phải tiếp tục chỉ là composition root.

### Action / Thao tác

**English:** Approve Architecture `D01_L04_A1` with this order:

```text
Robot.robotPeriodic()
1. CommandScheduler.run()
2. RobotTelemetry.periodic()
```

Approve constructor injection from `RobotContainer`, observation reads only through
`DriveSubsystem.getObservation()`, and publication only through `DriveTelemetryFacade`.

**Tiếng Việt:** Phê duyệt Architecture `D01_L04_A1` với thứ tự trên. Dependency được inject từ
`RobotContainer`; telemetry chỉ đọc qua `DriveSubsystem.getObservation()` và chỉ publish qua
`DriveTelemetryFacade`.

### Files Changed / Tệp thay đổi

- No production files / Không có tệp production

### Verification / Xác minh

**English:** Frozen Backbone, Frozen Interface Contract, ownership, dependency direction, periodic
caller, and exact file scope were reviewed and approved.

**Tiếng Việt:** Frozen Backbone, Frozen Interface Contract, ownership, dependency direction,
periodic caller và phạm vi tệp chính xác đã được review và phê duyệt.

### Expected Result / Kết quả mong đợi

**English:** Implementation can proceed without changing any frozen mechanism contract.

**Tiếng Việt:** Implementation có thể tiếp tục mà không thay đổi bất kỳ hợp đồng mechanism đã
đóng băng nào.

## Step 3 - Add RobotTelemetry / Thêm RobotTelemetry

### Objective / Mục tiêu

**English:** Add one robot-level coordinator for read-only telemetry.

**Tiếng Việt:** Thêm một coordinator cấp robot cho telemetry chỉ đọc.

### Why / Tại sao

**English:** The Frozen Backbone requires the observation path
`Subsystem -> Telemetry Coordinator -> Telemetry Facade -> NetworkTables`.

**Tiếng Việt:** Frozen Backbone yêu cầu observation path
`Subsystem -> Telemetry Coordinator -> Telemetry Facade -> NetworkTables`.

### Action / Thao tác

**English:** Create `frc.robot.telemetry.RobotTelemetry`. Inject `DriveSubsystem` and
`DriveTelemetryFacade` through its constructor. In `periodic()`, obtain one immutable
`DriveObservation` from the subsystem and pass it to the facade.

**Tiếng Việt:** Tạo `frc.robot.telemetry.RobotTelemetry`. Inject `DriveSubsystem` và
`DriveTelemetryFacade` qua constructor. Trong `periodic()`, lấy một `DriveObservation` bất biến
từ subsystem và chuyển nó cho facade.

### Files Changed / Tệp thay đổi

- Added `src/main/java/frc/robot/telemetry/RobotTelemetry.java`

### Verification / Xác minh

**English:** Dependency inspection confirmed no IO, hardware, control, or scheduling access.

**Tiếng Việt:** Kiểm tra dependency xác nhận lớp không truy cập IO, phần cứng, điều khiển hoặc
scheduling.

### Expected Result / Kết quả mong đợi

**English:** One coordinator performs observation-to-facade forwarding and nothing else.

**Tiếng Việt:** Một coordinator chuyển observation tới facade và không thực hiện trách nhiệm nào
khác.

## Step 4 - Add the Stable Telemetry Table Constant / Thêm hằng số bảng telemetry ổn định

### Objective / Mục tiêu

**English:** Define the approved NetworkTables table name in the configuration authority.

**Tiếng Việt:** Định nghĩa tên bảng NetworkTables đã phê duyệt trong nơi quản lý cấu hình.

### Why / Tại sao

**English:** The table name is a stable runtime contract and should not be duplicated as an
unexplained literal in composition code.

**Tiếng Việt:** Tên bảng là hợp đồng runtime ổn định và không nên bị lặp lại dưới dạng literal
không giải thích trong composition code.

### Action / Thao tác

**English:** Add `TelemetryConstants.kDriveTableName` with the value `Drive` inside
`Constants.java`.

**Tiếng Việt:** Thêm `TelemetryConstants.kDriveTableName` với giá trị `Drive` trong
`Constants.java`.

### Files Changed / Tệp thay đổi

- Modified `src/main/java/frc/robot/Constants.java`

### Verification / Xác minh

**English:** `RobotContainer` uses the constant to select the approved table.

**Tiếng Việt:** `RobotContainer` dùng hằng số này để chọn bảng đã được phê duyệt.

### Expected Result / Kết quả mong đợi

**English:** The runtime publishes under the stable `Drive` table.

**Tiếng Việt:** Runtime publish dưới bảng `Drive` ổn định.

## Step 5 - Compose the Telemetry Graph / Ghép nối đồ thị telemetry

### Objective / Mục tiêu

**English:** Create and inject the runtime telemetry dependencies.

**Tiếng Việt:** Tạo và inject các dependency telemetry runtime.

### Why / Tại sao

**English:** Object creation and implementation selection belong in the composition root.

**Tiếng Việt:** Việc tạo object và chọn implementation thuộc trách nhiệm của composition root.

### Action / Thao tác

**English:** In `RobotContainer`, obtain the default NetworkTables instance, select the `Drive`
table, create `DriveTelemetryFacade`, create `RobotTelemetry`, and expose the composed
coordinator to `Robot`.

**Tiếng Việt:** Trong `RobotContainer`, lấy NetworkTables instance mặc định, chọn bảng `Drive`,
tạo `DriveTelemetryFacade`, tạo `RobotTelemetry` và cung cấp coordinator đã ghép nối cho `Robot`.

### Files Changed / Tệp thay đổi

- Modified `src/main/java/frc/robot/RobotContainer.java`

### Verification / Xác minh

**English:** `RobotContainer` contains only creation, selection, injection, commands, and
bindings. It contains no periodic publishing or telemetry calculations.

**Tiếng Việt:** `RobotContainer` chỉ chứa việc tạo object, lựa chọn, inject, command và binding;
không chứa publishing định kỳ hoặc phép tính telemetry.

### Expected Result / Kết quả mong đợi

**English:** All telemetry objects have robot-lifetime ownership and explicit dependencies.

**Tiếng Việt:** Tất cả object telemetry có vòng đời bằng vòng đời robot và dependency rõ ràng.

## Step 6 - Call Telemetry After the Scheduler / Gọi telemetry sau scheduler

### Objective / Mục tiêu

**English:** Execute telemetry once per robot loop after subsystem periodic updates.

**Tiếng Việt:** Chạy telemetry một lần mỗi vòng lặp robot sau khi subsystem periodic cập nhật.

### Why / Tại sao

**English:** `CommandScheduler.run()` invokes subsystem periodic processing. Publishing afterward
uses the latest completed subsystem observation.

**Tiếng Việt:** `CommandScheduler.run()` gọi subsystem periodic. Publish sau đó sẽ dùng
observation mới nhất đã được subsystem cập nhật hoàn tất.

### Action / Thao tác

**English:** Retain the coordinator in `Robot` and call:

```java
CommandScheduler.getInstance().run();
m_robotTelemetry.periodic();
```

**Tiếng Việt:** Giữ coordinator trong `Robot` và gọi scheduler trước, telemetry sau theo đúng hai
dòng trên.

### Files Changed / Tệp thay đổi

- Modified `src/main/java/frc/robot/Robot.java`

### Verification / Xác minh

**English:** Source inspection and runtime execution confirmed the approved order.

**Tiếng Việt:** Kiểm tra source và chạy runtime xác nhận đúng thứ tự đã phê duyệt.

### Expected Result / Kết quả mong đợi

**English:** Every robot loop publishes the latest completed drive observation.

**Tiếng Việt:** Mỗi vòng lặp robot publish drive observation đã hoàn tất mới nhất.

## Step 7 - Verify WPILib Simulation Runtime / Xác minh runtime mô phỏng WPILib

### Objective / Mục tiêu

**English:** Prove startup, lifecycle, publication, value updates, and read-only behavior.

**Tiếng Việt:** Chứng minh startup, lifecycle, publishing, cập nhật giá trị và hành vi chỉ đọc.

### Why / Tại sao

**English:** A successful build alone does not prove that the runtime graph executes or publishes
NetworkTables values.

**Tiếng Việt:** Build thành công không tự chứng minh rằng runtime graph thực thi hoặc publish giá
trị NetworkTables.

### Action / Thao tác

**English:** Start the WPILib Java simulation, exercise disabled, enabled teleop, and return to
disabled, observe the approved NetworkTables topics, apply simulated joystick input, and call
telemetry independently to check for output side effects.

**Tiếng Việt:** Khởi động mô phỏng Java WPILib, chạy disabled, enabled teleop rồi quay lại
disabled, quan sát các topic NetworkTables đã phê duyệt, áp dụng joystick input mô phỏng và gọi
telemetry riêng để kiểm tra side effect lên output.

### Files Changed / Tệp thay đổi

- No production files / Không có tệp production
- Temporary verification source was removed / Source xác minh tạm thời đã được xóa

### Verification / Xác minh

**English:**

- Robot startup: PASS
- Disabled/enabled lifecycle exceptions: none
- Table: `Drive`
- Topics: `/Drive/leftAppliedOutput`, `/Drive/rightAppliedOutput`
- Disabled sample: `0.0`, `0.0`
- Enabled sample: `0.456521739130`, `0.184782608696`
- Telemetry read-only check: PASS
- Frozen D01_L03 Java mismatches: zero

**Tiếng Việt:**

- Robot startup: PASS
- Lỗi lifecycle disabled/enabled: không có
- Bảng: `Drive`
- Topic: `/Drive/leftAppliedOutput`, `/Drive/rightAppliedOutput`
- Mẫu disabled: `0.0`, `0.0`
- Mẫu enabled: `0.456521739130`, `0.184782608696`
- Kiểm tra telemetry chỉ đọc: PASS
- Sai khác Java đã đóng băng của D01_L03: bằng không

### Expected Result / Kết quả mong đợi

**English:** Runtime Verification `D01_L04_V1` passes without a production defect.

**Tiếng Việt:** Runtime Verification `D01_L04_V1` PASS mà không có defect production.

## Step 8 - Complete and Freeze the Lesson / Hoàn thành và đóng băng bài học

### Objective / Mục tiêu

**English:** Record the verified implementation as a completed lesson snapshot.

**Tiếng Việt:** Ghi nhận implementation đã xác minh thành snapshot bài học hoàn thành.

### Why / Tại sao

**English:** Repository governance requires accurate verification records, transition
documentation, a final clean build, and explicit freeze state before commit.

**Tiếng Việt:** Quy tắc repository yêu cầu hồ sơ xác minh chính xác, tài liệu chuyển đổi, final
clean build và trạng thái đóng băng rõ ràng trước commit.

### Action / Thao tác

**English:** Update README and `LESSON_STATUS.md`, create this bilingual guide, verify that all
production Java hashes still match the approved Step 2 implementation, and run the final clean
build.

**Tiếng Việt:** Cập nhật README và `LESSON_STATUS.md`, tạo hướng dẫn song ngữ này, xác minh mọi
hash Java production vẫn khớp implementation Step 2 đã phê duyệt và chạy final clean build.

### Files Changed / Tệp thay đổi

- `README.md`
- `LESSON_STATUS.md`
- `docs/D01_L03_Drive_Observation_Publishing_to_D01_L04_Robot_Telemetry_Runtime_Integration_Step_by_Step.md`

### Verification / Xác minh

**English:** Java integrity passed and `.\gradlew.bat clean build --no-daemon` completed
successfully.

**Tiếng Việt:** Java integrity PASS và `.\gradlew.bat clean build --no-daemon` hoàn thành thành
công.

### Expected Result / Kết quả mong đợi

**English:** D01_L04 is `COMPLETE` and frozen, with Git commit and push still `PENDING`.

**Tiếng Việt:** D01_L04 ở trạng thái `COMPLETE` và đã đóng băng; Git commit và push vẫn
`PENDING`.

## Final Architecture / Kiến trúc cuối cùng

```text
CONTROL
Driver
-> Xbox Controller
-> controls
-> commands
-> DriveSubsystem
-> DriveIO
-> Hardware or Simulation

OBSERVATION AND PUBLISHING
DriveIO
-> DriveIOInputs
-> DriveSubsystem
-> DriveObservation
-> RobotTelemetry
-> DriveTelemetryFacade
-> /Drive/leftAppliedOutput
-> /Drive/rightAppliedOutput
```

**English:** Telemetry observes only. It does not control, schedule, access IO, or access
hardware. Real robot verification remains `NOT TESTED`.

**Tiếng Việt:** Telemetry chỉ quan sát. Telemetry không điều khiển, không schedule, không truy cập
IO và không truy cập phần cứng. Xác minh trên robot thật vẫn là `NOT TESTED`.
