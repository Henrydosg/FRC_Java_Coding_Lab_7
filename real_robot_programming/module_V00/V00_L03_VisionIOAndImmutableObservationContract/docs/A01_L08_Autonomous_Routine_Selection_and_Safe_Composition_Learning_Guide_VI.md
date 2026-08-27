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

## 12. Trạng thái closure lịch sử

A01_L08 trước đây là `COMPLETE / FROZEN / READ-ONLY`. User đã xác nhận build WPILib VS
Code sau repair: `BUILD SUCCESSFUL in 1s`, `6 actionable tasks: 1 executed, 5
up-to-date`; kết quả test đầy đủ là 430/430 PASS, và bằng chứng Simulation
cùng Real Robot là PASS. A01_L08 là frozen inheritance source cho A01_L09;
A01_L09 là `NOT CREATED / NOT STARTED` tại thời điểm closure đó.

Closure này không tuyên bố exact endpoint accuracy, final PID/feedforward
tuning, hoặc final physical characterization.

## 13. Repair an toàn / độ bền sau khi mở lại - ghi nhận lịch sử

A01_L08 từng ở trạng thái tạm thời `REOPENED / IN_PROGRESS / EDITABLE`. Quy trình operator hiện
tại: Disabled, chọn routine, đặt robot đúng vị trí vật lý, nhấn
`Prepare Autonomous`, xác nhận READY, rồi mới enable Autonomous. Prepare capture
heading, chờ một chu kỳ refresh subsystem, reset known start pose, và preflight
pose, measured speeds, path, RobotConfig cùng AutoBuilder mà không schedule
motion.

Driving READY là một attempt dùng một lần, có provenance gồm alliance, routine,
field variant, expected start pose, heading-capture attempt và path identity.
SAFE_STOP không cần và không consume driving READY. Fresh driving command được
construct trước khi đúng attempt được atomic claim.

Validation dùng tolerance tạm thời `0.03 m` và `2.0 degree`. Heading error dùng
`MathUtil.angleModulus`; đây không phải claim về endpoint, tuning hay physical
characterization. Điều kiện recoverable có thể được sửa rồi Prepare lại mà
không restart. Fatal software/configuration fault giữ nguyên first reason và
fail closed trong suốt process.

Evidence local ở checkpoint trung gian: compileJava PASS, compileTestJava PASS,
45/45 focused/integration tests PASS, 445/445 full tests PASS và clean build
PASS. Final Simulation và Real Robot do User xác nhận được ghi ở Section 15.
Tại checkpoint lịch sử đó, A01_L09 được xem là frozen và chưa sửa đổi. Sau khi
triển khai event L09 và final closure review, các record L09 hiện tại xác định
lesson là `COMPLETE / FROZEN / READ-ONLY`. Git publication vẫn chờ User
commit/push.

## 14. Repair an toàn về terminal ownership

Scheduler lifecycle sau repair là:

`CONSUMED -> RUNNING -> path completion -> centralized stop -> HOLDING ->
Autonomous exit -> COMPLETE`.

`HOLDING` là state mới duy nhất. Nó có nghĩa path motion đã kết thúc và Swerve
đã stop, nhưng autonomous command vẫn giữ Swerve requirement. Vì vậy default
Teleop command không thể lấy ownership trước khi Autonomous kết thúc. SAFE_STOP
dùng cùng session-long ownership và không consume driving readiness.

WPILib command composition hiện sở hữu toàn bộ child lifecycle callback. Custom
wrapper trước đây không còn tự gọi `initialize()`, `execute()`, `isFinished()`
hoặc `end()` của child. Outer Autonomous-enabled lifetime guard thực hiện
cleanup ngay khi đổi mode, còn `kCancelIncoming` bảo vệ active session.

Lớp phòng vệ thứ hai là `FieldRelativeTeleopDriveCommand` kiểm tra
`DriverStation.isTeleopEnabled()` trước khi đọc Xbox controller. Ngoài Teleop,
command gọi centralized stop rồi return, không publish hay submit intent từ
controller. Hành vi bình thường trong Teleop không đổi.

Evidence local: focused terminal/Teleop 32/32 PASS, preparation regression
12/12 PASS, autonomous scheduling 29/29 PASS, full suite 442/442 PASS và clean
build PASS. Final Simulation và Real Robot do User xác nhận đều PASS. Không có
thay đổi PID/feedforward, CANcoder calibration, SwerveSubsystem, CTRE/IO,
RobotConfig hoặc PathPlanner asset; không tuyên bố rằng mọi one-time physical
steering transient đã bị loại bỏ.

## 15. Vì sao L06 và L08 dừng khác nhau?

Sự khác nhau là có chủ ý vì hai lesson dùng hai contract hoàn thành chuyển động
khác nhau.

Ở L06, follower học theo endpoint-tolerance và pose-correction. Nó so sánh pose
hiện tại với target, vì vậy robot có thể overshoot endpoint rồi reverse-correct
để giảm pose error còn lại. Autonomous safety hold vẫn giữ quyền sở hữu Swerve
sau khi follower dừng để command khác không thể lấy drivetrain trong cùng
autonomous session.

Ở L08, `ONE_METER_PATH` hoàn tất theo command có thời gian của PathPlanner.
Composition sau repair vì vậy có terminal ownership rõ ràng:
path completion -> centralized stop -> `HOLDING`. `HOLDING` giữ Swerve cho đến
khi Autonomous thoát. Defensive Teleop-enabled gate ngăn controller input lọt
vào pipeline drivetrain khi Autonomous vẫn đang enabled. Đây là khác biệt về
command ownership, không phải thay đổi tuning drivetrain.

## 16. Quan hệ giữa các layer kiến trúc

Autonomous request đi qua các layer như sau:

```text
Autonomous Routine
    -> PathPlanner / command composition
    -> SwerveSubsystem
    -> Swerve module state/output pipeline
    -> IO
    -> CTRE hardware
```

Routine và PathPlanner layer quyết định và kết hợp command. `SwerveSubsystem`
vẫn là owner duy nhất của hành vi drivetrain, state và centralized stop. Module
output pipeline đổi chassis intent thành module state/output. IO chuyển output
đó thành lời gọi tới vendor hardware. Vì vậy repair L08 nằm ở command
composition và Teleop command boundary, không nằm ở `SwerveSubsystem`, CTRE
configuration hay module calibration.

## 17. Vì sao phải điều tra steering twitch?

Steering twitch ở terminal được điều tra vì path completion đã release Swerve
requirement trước khi Autonomous kết thúc. Điều đó tạo ownership gap: default
Teleop command có thể reacquire Swerve trong lúc Driver Station vẫn báo
Autonomous Enabled. Source review cũng phát hiện preparation wrapper tự gọi
manual child-command lifecycle, trái với contract scheduler-native của A01.

Repair giữ Swerve qua terminal `HOLDING` và SAFE_STOP, thêm Teleop mode gate tối
thiểu, và để WPILib sở hữu child lifecycle callback. User quan sát trên real
robot rằng steering twitch biến mất sau repair. Đây là validation phù hợp với
ownership repair, nhưng không chứng minh mọi physical cause có thể xảy ra.

PID/feedforward tuning và CANcoder recalibration không được dùng vì evidence
chỉ ra lỗi phần mềm về ownership/mode boundary, không phải lỗi gain, encoder
offset hay hardware đã được chứng minh. Scheduler-native composition được ưu
tiên hơn manual lifecycle delegation vì scheduler sở hữu requirement,
interruption, initialize, execute, completion và cleanup trong một lifecycle
thống nhất.

## 18. Ghi nhận verification cuối cùng

User đã xác nhận Blue và Red execution, Prepare -> READY, recoverable
`RESET_REJECTED` rồi Prepare READY lần hai không restart, final Blue pose gần
`1.005 m`, terminal hold, không có drivetrain movement khi joystick mô phỏng
sau path completion, Autonomous -> Disabled -> Teleop, Teleop bình thường,
SAFE_STOP, recovery không restart và không automatic restart. Real Robot còn
bao phủ repaired deployment, preparation telemetry, repeat Blue, Blue -> Red
không restart, mode-loss stop và steering twitch không còn sau repair. A01_L08
vẫn là `REOPENED / IN_PROGRESS / EDITABLE`; Git publication vẫn thuộc User.
Final re-freeze hiện là `HOLD`: `AutoBuilderContractAdapter.SafeAutoBuilderCommand`
vẫn tự delegate các child lifecycle callback, nên gate scheduler-native không
manual lifecycle chưa được giải quyết. Audit documentation-only này không cho
phép sửa production hoặc test.

## 19. Ghi nhận re-freeze cuối cùng

Phần trước giữ nguyên bằng chứng lịch sử của architecture HOLD. Repair được phê
duyệt sau đó đã loại bỏ `SafeAutoBuilderCommand` và toàn bộ manual child
lifecycle delegation khỏi active path. WPILib scheduler hiện sở hữu PathPlanner
child lifecycle; Robot-level exception boundary, coordinator fault bridge,
centralized stop, immutable `FAULTED`, terminal `HOLDING`, SAFE_STOP, Teleop
mode gate và nguyên tắc không automatic restart đều được giữ nguyên.

Verification cuối cùng đã PASS `compileJava`, `compileTestJava`,
`RobotSchedulerExceptionBoundaryTest`, toàn bộ 449 test và clean build. User đã
xác nhận Simulation và real-robot retest cuối cùng, gồm Blue/Red path,
preparation/recovery, terminal ownership, SAFE_STOP, chuyển từ Autonomous sang
Teleop và không automatic restart.

Steer event ngắn, chỉ xảy ra một lần gần path completion được phân loại là
`KNOWN / BOUNDED TERMINAL STEER TRANSIENT`, `ACCEPTED FOR CURRENT LESSON` và
`DEFERRED FOR FUTURE DRIVETRAIN / PATH-FOLLOWING TUNING`. Root cause vật lý
chính xác chưa được chứng minh đầy đủ. Không có sustained oscillation, PID
instability, CANcoder defect, hardware defect, PathPlanner defect, Swerve
architecture defect, ownership gap hoặc uncontrolled drivetrain motion nào đã
được xác minh, vì vậy không có production change tương ứng nào được biện minh.
Một sample desktop khoảng 5.9 ms của periodic không phải bằng chứng đại diện
cho roboRIO và không chặn closure.

Frozen Backbone và Frozen Interface Contract được giữ nguyên. RobotContainer
vẫn chỉ là composition root; SwerveSubsystem vẫn sở hữu drivetrain/output/
localization; A01_L04 vẫn là owner duy nhất của alliance transform;
`shouldFlipPath = false`; và `preventFlipping = true`. A01_L08 được đặt thành
`COMPLETE / FROZEN / READ-ONLY` sau exceptional reopen. V00_L02 vẫn
`SUSPENDED / READ-ONLY` cho đến khi có reconciliation và resume riêng.
