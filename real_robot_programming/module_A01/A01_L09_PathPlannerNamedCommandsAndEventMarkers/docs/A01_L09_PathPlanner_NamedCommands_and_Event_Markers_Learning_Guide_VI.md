# A01_L09 - PathPlanner NamedCommands và Event Markers - Tài liệu học tập

## 1. Mục tiêu học tập

A01_L09 hướng dẫn cách một event marker của PathPlanner kích hoạt một WPILib
Command mới thông qua NamedCommands, trong khi command bám đường vẫn sở hữu và
điều khiển hệ truyền động Swerve.

Lesson này chứng minh ranh giới event-dispatch, không phải tích hợp mechanism.
Event duy nhất là `LEARNING_EVENT`, một demonstration phi-mechanism có tính xác
định.

## 2. Nền tảng kế thừa

L09 kế thừa các contract đã đóng băng từ L08 và các lesson trước:

- `SAFE_STOP` là lựa chọn mặc định của chooser;
- `ONE_METER_PATH` là routine một mét không có event;
- routine chỉ được chụp một lần khi Autonomous bắt đầu;
- readiness từ starting-pose chỉ được sử dụng một lần;
- L04 sở hữu phép biến đổi alliance Blue/Red duy nhất;
- chức năng flipping của AutoBuilder bị tắt;
- mọi execution path sử dụng `preventFlipping = true`;
- `SwerveSubsystem` sở hữu localization, drivetrain requirement và stop;
- Disabled/mode loss dừng chuyển động; và
- enable lại mà không có readiness mới không khởi động lại routine cũ.

## 3. Luồng dispatch

```text
event path của A01_L09
    -> marker đạt vị trí tương đối 0.5
    -> PathPlanner yêu cầu LEARNING_EVENT
    -> NamedCommands tìm deferred command đã đăng ký
    -> AutonomousEventBinding kiểm tra identity và requirements
    -> Commands.defer(...) gọi Supplier<Command>
    -> AutonomousEventDemonstrationCommand mới được chạy
```

Việc đăng ký diễn ra trong `RobotContainer` trước khi event path được load.
`RobotContainer` chỉ tạo và inject các thành phần; nó không thực hiện event
timing, hành vi drivetrain hay phép tính telemetry.

## 4. Vì sao phải tạo Command mới

WPILib Command có trạng thái vòng đời. Tái sử dụng cùng một command instance
cho nhiều marker hoặc nhiều phiên Autonomous có thể giữ trạng thái cũ hoặc vi
phạm yêu cầu của scheduler. L09 đăng ký một deferred wrapper và gọi
`Supplier<Command>` cho mỗi lần dispatch.

Ranh giới đăng ký cũng kiểm tra requirements của command được trả về phải khớp
chính xác với requirements đã khai báo trong binding. Command null, supplier
ném exception, requirements không khớp, tên trùng hoặc Swerve requirement đều
fail closed.

## 5. Quyền sở hữu requirement và chạy đồng thời

Command bám đường yêu cầu `SwerveSubsystem`. `LEARNING_EVENT` có requirement
rỗng. Vì vậy scheduler có thể chạy đồng thời hai command mà không xung đột quyền
sở hữu drivetrain:

```text
Path follower: yêu cầu SwerveSubsystem -> chuyển động drivetrain
Learning event: không yêu cầu subsystem -> demonstration vòng đời có giới hạn
```

Event không được gửi chassis speeds, dừng module, truy cập IO hoặc giả lập
Intake, Feeder, Flywheel hay mechanism khác.

## 6. Observation và telemetry

Demonstration command tạo các giá trị `AutonomousEventObservation` bất biến.
Telemetry facade nhận các Observation đó và sở hữu việc publish NetworkTables.

```text
Command
    -> AutonomousEventObservation
    -> AutonomousEventTelemetryFacade
    -> /Autonomous/Event/*
```

Các topic được publish:

- `LastEvent`: tên event ổn định;
- `State`: `STARTED`, `ACTIVE`, `COMPLETED`, `CANCELLED` hoặc
  `FACTORY_FAILURE`;
- `Active`: event hiện có đang active hay không; và
- `DispatchCount`: số lần quan sát thấy transition `STARTED`.

Command không gọi NetworkTables trực tiếp và telemetry không điều khiển hành vi
robot.

## 7. Contract của path và alliance

`A01_L09_OneMeter_With_Learning_Event.path` là dữ liệu canonical Blue-frame với
một named-command marker tại vị trí tương đối `0.5`. Phép biến đổi L04 tạo một
execution path Blue hoặc Red mới đúng một lần. Callback `shouldFlipPath` của
AutoBuilder trả về `false`, và execution path đặt `preventFlipping = true`.

Thiết kế này ngăn event-enabled path tạo phép biến đổi thứ hai. Identity và vị
trí tương đối của marker vẫn đúng cho cả hai alliance.

## 8. Hành vi lỗi và an toàn

L09 fail closed trong các trường hợp:

- readiness thiếu hoặc không hợp lệ;
- alliance không xác định;
- dữ liệu path thiếu, sai hoặc không được hỗ trợ;
- đăng ký event trùng hoặc không hợp lệ;
- supplier lỗi hoặc trả về Command không hợp lệ;
- xung đột requirement của event;
- thời gian event không hữu hạn hoặc chạy lùi;
- cancellation, Disabled hoặc mode loss; và
- lỗi AutoBuilder/path execution.

Hành vi kết thúc drivetrain vẫn tập trung tại `SwerveSubsystem.stop()`. Event
không thay thế hoặc bỏ qua quyền sở hữu này.

## 9. Bằng chứng xác minh cuối cùng

Bằng chứng tự động:

- compileJava PASS;
- compileTestJava PASS;
- focused event, path, routine và integration tests PASS;
- 384 inherited regression tests không thay đổi PASS; và
- full suite 446/446 PASS cùng isolated clean build PASS.

Bằng chứng runtime do User sở hữu:

- Blue Simulation PASS;
- Red Simulation PASS;
- `ONE_METER_WITH_EVENT` PASS;
- path tiếp tục chạy trong khi `LEARNING_EVENT` thực thi;
- telemetry kết thúc tại `Active=false`, `DispatchCount=1`,
  `LastEvent="LEARNING_EVENT"` và `State="COMPLETED"`;
- Disable/mode-loss stop PASS;
- không tự động restart PASS; và
- Real Robot PASS trên robot Swerve thật.

## 10. Ranh giới phạm vi và đóng lesson

Bằng chứng cuối cùng không khẳng định độ chính xác endpoint tuyệt đối, tuning
PID/feedforward cuối cùng, physical characterization cuối cùng, mức sẵn sàng
thi đấu hoặc hành vi mechanism thật. D01 vẫn là project Tank Drive độc lập và
không có mechanism contract D01 nào được đưa vào A01.

A01_L09 là `COMPLETE / FROZEN / READ-ONLY`. Đây là điểm hoàn tất được phê duyệt
của A01. Không có A01_L10 hoặc lesson/module tiếp theo nào được tạo hay bắt đầu.
