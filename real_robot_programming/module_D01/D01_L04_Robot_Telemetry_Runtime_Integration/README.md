# D01_L04 Robot Telemetry Runtime Integration

## Lesson Objective / Mục tiêu bài học

**English:** Integrate the inherited read-only drive telemetry publisher into the WPILib robot
runtime through one `RobotTelemetry` coordinator without changing drivetrain behavior.

**Tiếng Việt:** Tích hợp bộ xuất telemetry chỉ đọc của hệ truyền động đã được kế thừa vào runtime
robot WPILib thông qua một bộ điều phối `RobotTelemetry` mà không thay đổi hành vi hệ truyền động.

## Previous Lesson / Bài học trước

`D01_L03_Drive_Observation_Publishing`

D01_L04 inherits the complete D01_L03 control, IO, observation, and publishing boundaries.
D01_L03 remains a frozen lesson.

D01_L04 kế thừa toàn bộ ranh giới điều khiển, IO, observation và publishing của D01_L03.
D01_L03 vẫn là một bài học đã đóng băng.

## Inherited Architecture / Kiến trúc kế thừa

Control flow remains unchanged:

```text
Driver
-> Xbox Controller
-> controls
-> commands
-> DriveSubsystem
-> DriveIO
-> Hardware or Simulation
```

Observation and publishing contracts remain unchanged:

```text
DriveIO
-> DriveIOInputs
-> DriveSubsystem
-> DriveObservation
-> DriveTelemetryFacade
-> typed NetworkTables publishers
```

Luồng điều khiển và các hợp đồng observation/publishing không thay đổi.

## RobotTelemetry Responsibility / Trách nhiệm của RobotTelemetry

`RobotTelemetry` is the robot-level, read-only telemetry coordinator. It:

- receives `DriveSubsystem` and `DriveTelemetryFacade` through constructor injection;
- reads the latest immutable value from `DriveSubsystem.getObservation()`;
- passes that value to `DriveTelemetryFacade.publish()`.

`RobotTelemetry` does not access IO or hardware, process driver input, command the drivetrain,
schedule commands, or change robot behavior.

`RobotTelemetry` là bộ điều phối telemetry chỉ đọc ở cấp robot. Lớp này nhận các dependency qua
constructor, đọc observation bất biến mới nhất từ subsystem và chuyển observation đó cho facade.
Lớp này không truy cập IO/phần cứng, không xử lý input, không điều khiển robot và không schedule
command.

## Final Runtime Flow / Luồng runtime cuối cùng

Every `Robot.robotPeriodic()` cycle executes in this exact order:

```text
Robot.robotPeriodic()
1. CommandScheduler.run()
   -> DriveSubsystem.periodic()
      -> DriveIO.updateInputs(DriveIOInputs)
2. RobotTelemetry.periodic()
   -> DriveSubsystem.getObservation()
   -> DriveTelemetryFacade.publish(DriveObservation)
   -> NetworkTables / Glass
```

Mỗi chu kỳ `Robot.robotPeriodic()` chạy `CommandScheduler` trước, sau đó mới chạy
`RobotTelemetry.periodic()`. Vì vậy telemetry xuất snapshot từ lần cập nhật subsystem đã hoàn tất
gần nhất.

`RobotContainer` remains the composition root. It creates the selected `DriveIO`, subsystem,
typed telemetry facade, and coordinator, then injects their dependencies. It contains no periodic
publishing or telemetry calculations.

`RobotContainer` vẫn chỉ là composition root: tạo object, chọn implementation và inject
dependency; không chứa publishing định kỳ hoặc phép tính telemetry.

## NetworkTables Contract / Hợp đồng NetworkTables

Approved table:

```text
Drive
```

Typed topics:

```text
/Drive/leftAppliedOutput
/Drive/rightAppliedOutput
```

Both topics are typed double publishers and contain the corresponding values from the latest
`DriveObservation`.

Cả hai topic là publisher kiểu `double` và chứa giá trị tương ứng từ `DriveObservation` mới nhất.

## Verification / Xác minh

- Architecture Approval `D01_L04_A1`: PASS
- Implementation Review `D01_L04_I1`: PASS
- Runtime Verification `D01_L04_V1`: PASS
- Clean build: PASS
- WPILib simulation startup: PASS
- Disabled and enabled simulation lifecycle: PASS; no lifecycle exceptions
- NetworkTables table and topic creation: PASS
- Observation-to-topic updates: PASS
- Telemetry read-only behavior: PASS
- Frozen D01_L03 Java comparison: PASS; zero mismatches
- Driver Station / Glass: PASS for verified WPILib simulation and NetworkTables evidence only
- Real robot: NOT TESTED

Observed enabled simulation sample:

```text
/Drive/leftAppliedOutput  = 0.456521739130
/Drive/rightAppliedOutput = 0.184782608696
```

Mẫu mô phỏng khi enabled đã xác nhận hai giá trị topic thay đổi theo observation. Việc gọi
telemetry riêng biệt không làm thay đổi output của hệ truyền động.

## Status / Trạng thái

`COMPLETE`

Git commit and Git push remain pending. Physical robot behavior has not been tested.

Git commit và Git push vẫn đang chờ. Hành vi trên robot thật chưa được kiểm thử.
