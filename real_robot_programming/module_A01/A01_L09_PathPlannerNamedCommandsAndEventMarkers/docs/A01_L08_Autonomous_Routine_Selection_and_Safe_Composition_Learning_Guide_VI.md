# A01_L08 - Chọn và Kết hợp Autonomous An toàn

## Hướng dẫn học

Lesson này giải thích cách robot chọn một autonomous routine an toàn tại
composition root và tạo một command mới do scheduler quản lý. Đây là tài liệu
học của repository, không phải báo cáo tuning thi đấu.

## 1. L08 giải quyết vấn đề gì?

L07 đã chứng minh rằng các contract về pose, tốc độ, output, alliance,
requirement và safety có thể đi qua ranh giới AutoBuilder. L08 thêm đúng một
khái niệm: chọn giữa nhiều autonomous behavior mà không dùng lại command cũ,
không bỏ qua readiness và không làm mất quyền stop của drivetrain.

| Nội dung | L07 | L08 |
| --- | --- | --- |
| Khái niệm chính | Tích hợp contract AutoBuilder | Chọn và kết hợp routine an toàn |
| Selection | Một autonomous path đã biết | `SAFE_STOP` hoặc `ONE_METER_PATH` rõ ràng |
| Owner | Ranh giới adapter L07 | `RobotContainer` và `AutonomousRoutineFactory` |
| Vòng đời command | Ranh giới path command hiện có | Command mới cho mỗi autonomous request |
| Safety | Contract stop/requirement của L07 | Giữ contract và fail-closed khi selection lỗi |

L08 không thêm NamedCommands, event markers, hành động mechanism, pathfinding,
vision hay competition strategy. Những nội dung đó nằm ngoài lesson này.

## 2. SendableChooser là gì?

`SendableChooser` là object của WPILib để đưa các lựa chọn lên dashboard cho
operator chọn trong lúc chuẩn bị robot. Trong repository này, `RobotContainer`
là composition root và sở hữu chooser có tên `Autonomous Routine`.

Chooser chứa routine identity, không chứa các command instance dùng lại:

- `SAFE_STOP` là routine an toàn mặc định. Nó giữ requirement của Swerve và
  giữ drivetrain dừng bằng `AutonomousSafetyHoldCommand`.
- `ONE_METER_PATH` là lựa chọn chạy có chủ ý. Nó dùng path đã biết thông qua
  contract AutoBuilder của L07 đang frozen.

SAFE_STOP là mặc định vì selection bị thiếu, sai hoặc chưa chuẩn bị đúng không
được phép tạo motion. Routine chạy phải là lựa chọn rõ ràng và phải có starting
context hợp lệ.

## 3. Snapshot và command mới

Trong `Robot.autonomousInit()`, `RobotContainer.getAutonomousCommand()` đọc
chooser một lần. Identity đó là selection snapshot của autonomous session.
Factory sau đó consume accepted starting context một lần và tạo command mới.

Command không được dùng lại cho request sau. Command chứa trạng thái scheduler,
timer, requirement và lịch sử cancellation. Vì vậy thay đổi dashboard sau khi
command được tạo không thể sửa command đang chạy hoặc tự động restart robot.
Một autonomous session mới phải lấy snapshot mới và command mới.

## 4. Readiness: BACK và Reset Known Starting Pose

Repository này có contract chuẩn bị chỉ cho Disabled. Nút Xbox Back/View được
bind để capture field-heading reference. Ở repository này, BACK có vai trò
heading-reference; đây không phải yêu cầu chung của mọi robot FRC.

Sau đó `Reset Known Starting Pose` kiểm tra alliance/session hiện tại khi đang
Disabled và lưu một `AutonomousStartContext` được chấp nhận. Context chứa
alliance, field variant và execution start pose. `getAutonomousCommand()` consume
context này đúng một lần. Nếu request sau không có reset mới, factory trả về
SAFE_STOP và không thể restart motion.

Trình tự chuẩn bị thủ công là:

```text
DISABLED
  -> xác lập đúng alliance/session
  -> nhấn BACK / capture heading reference
  -> Reset Known Starting Pose
  -> chọn autonomous routine
  -> Autonomous
  -> Enable
```

Reset không phải motion command và không được chuyển sang enabled operation.

## 5. Alliance và quyền sở hữu path

Path geometry là canonical Blue geometry. Blue chạy geometry canonical đó. Red
được tạo bởi đúng một authority `FieldAllianceTransform` của L04.

Vendor flipping của AutoBuilder vẫn bị tắt:

```text
shouldFlipPath = false
execution PathPlannerPath.preventFlipping = true
```

Hai bảo vệ này có chủ ý. L04 sở hữu alliance transform, còn execution path của
PathPlanner được yêu cầu không mirror lần nữa. Nếu vừa transform ở L04 vừa để
vendor flip, path sẽ bị double flip và sai geometry trên field. L08 không tạo
routine Blue và Red riêng biệt.

## 6. Requirement, stop và fail-closed

`SwerveSubsystem` vẫn là owner của drivetrain requirement. Scheduler quản lý
ownership; không dùng manual lock. Cả driving command và SAFE_STOP đều giữ
Swerve requirement ở terminal safety boundary.

Các trường hợp sau fail-closed về SAFE_STOP hoặc đi qua stop authority hiện có:

- selection null hoặc không biết;
- readiness bị thiếu hoặc không hợp lệ;
- alliance không biết;
- path asset thiếu hoặc malformed;
- adapter/factory construction thất bại;
- cancellation, interruption, Disable hoặc mode loss.

Normal completion và mọi terminal path đều giữ `SwerveSubsystem.stop()`. Không
có automatic restart. Enable lại mà không có reset/readiness mới sẽ không tạo
autonomous command mới.

## 7. Sơ đồ kiến trúc

```text
Driver Station / Autonomous Init
        |
        v
Autonomous Routine Chooser
        |
        v
Selection Snapshot
        |
        v
AutonomousRoutineFactory
        |
        +--> SAFE_STOP
        |
        +--> ONE_METER_PATH
                    |
                    v
          L07 AutoBuilder Contract
                    |
                    v
          L04 Alliance Transform Authority
                    |
                    v
              SwerveSubsystem
                    |
                    v
                    IO
                    |
                    v
                 Hardware
```

Frozen backbone của mechanism vẫn là Driver -> controls -> commands ->
subsystems -> IO -> hardware. L08 chỉ thêm boundary chọn autonomous bị giới
hạn tại composition root.

## 8. Quy trình kiểm tra Simulation

Trước khi Enable, kiểm tra chooser hiển thị và có SAFE_STOP cùng
ONE_METER_PATH. Dùng trình tự Disabled ở trên, chọn SAFE_STOP, chuyển sang
Autonomous rồi Enable. SAFE_STOP phải không tạo drivetrain motion. Sau đó
chuẩn bị Disabled mới và chọn rõ ONE_METER_PATH.

Kiểm tra Blue và Red riêng. Xác nhận path bắt đầu từ pose đã accept, Red là
đúng một L04 transform của canonical Blue, và robot stop khi Disable,
cancellation hoặc mode loss. Enable lại mà không có reset/readiness mới thì
motion không được tự chạy lại.

User đã xác nhận các hành vi Simulation này, gồm execution Blue và Red. UI không
cho phép đổi sang ONE_METER_PATH khi Autonomous đã enabled, vì vậy không ghi
nhận kết quả manual runtime chooser-change. Automated tests và implementation
review vẫn bao phủ snapshot contract.

## 9. Quy trình kiểm tra Real Robot

Chỉ kiểm tra Real Robot sau khi Simulation PASS, dùng tốc độ bảo thủ, kế hoạch
rollback/stop vật lý, quyền Disable trên Driver Station và khu vực an toàn.
Lặp lại chuẩn bị Disabled, kiểm tra SAFE_STOP trước ONE_METER_PATH, rồi kiểm
tra Blue/Red, Disable/mode-loss stop, cancellation và không restart khi thiếu
readiness mới. User đã xác nhận A01_L08 Real Robot verification PASS.

## 10. L08 chứng minh và không chứng minh điều gì?

L08 chứng minh repository có thể expose hai routine identity bị giới hạn,
snapshot selection, tạo command mới, giữ readiness one-shot, giữ Swerve
requirement ownership, stop an toàn và chạy path đã biết qua contract L07/L04
trong ngữ cảnh Blue và Red.

L08 không chứng minh exact endpoint accuracy, exact one-meter precision, final
PID/feedforward tuning, final RobotConfig physical characterization,
mass/MOI/COF characterization hay autonomous accuracy sẵn sàng cho thi đấu.

## 11. Câu hỏi kiểm tra

1. Vì sao SAFE_STOP là mặc định?  
   Vì input sai hoặc preparation thiếu phải fail-closed mà không tạo motion.

2. Khi nào chooser được đọc?  
   Một lần khi `getAutonomousCommand()` tạo selection snapshot.

3. Vì sao cần command mới?  
   Vì command có scheduler state, timer, requirement và cancellation state.

4. Ai sở hữu Red transform?  
   L04 `FieldAllianceTransform`, đúng một lần.

5. Vì sao cần cả hai bảo vệ flipping?  
   `shouldFlipPath=false` và `preventFlipping=true` ngăn mirror lần thứ hai.

6. Điều gì làm autonomous start mới hợp lệ?  
   Preparation Disabled mới và accepted start context mới.

7. Khi mode loss xảy ra thì sao?  
   Command bị cancel và centralized Swerve stop vẫn được thực hiện.

## 12. Trạng thái lesson cuối cùng

A01_L08 là `COMPLETE / FROZEN / READ-ONLY`. User đã xác nhận build WPILib VS
Code sau repair: `BUILD SUCCESSFUL in 1s`, `6 actionable tasks: 1 executed, 5
up-to-date`; kết quả test đầy đủ là 430/430 PASS, và bằng chứng Simulation
cùng Real Robot là PASS. A01_L08 là frozen inheritance source cho A01_L09;
A01_L09 vẫn `NOT CREATED / NOT STARTED`.

Closure này không tuyên bố exact endpoint accuracy, final PID/feedforward
tuning, hoặc final physical characterization.
