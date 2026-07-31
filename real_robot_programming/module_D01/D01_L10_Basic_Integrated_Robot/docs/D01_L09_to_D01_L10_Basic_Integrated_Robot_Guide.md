# D01_L09 to D01_L10 Basic Integrated Robot Guide

## 1. Lesson Objective / Mục tiêu bài học

**EN:** Integrate the inherited drivetrain, intake, flywheel, feeder, and shooter into one tested
Xbox controller layout. Correct physical drivetrain identity and complete the timed Y-button
shooter sequence without changing the frozen architecture.

**VI:** Tích hợp hệ truyền động, intake, flywheel, feeder và shooter kế thừa vào một bố cục tay cầm
Xbox đã được kiểm thử. Sửa đúng nhận dạng vật lý trái/phải của hệ truyền động và hoàn thiện chuỗi
bắn có thời gian bằng nút Y mà không thay đổi kiến trúc đóng băng.

## 2. Inherited Baseline / Trạng thái kế thừa

**EN:** D01_L10 was copied from the current approved D01_L09 working state. Generated build state
was excluded, D01_L09 was restored to its committed frozen HEAD, and the inherited L10 project
passed a clean baseline build.

**VI:** D01_L10 được sao chép từ trạng thái làm việc D01_L09 hiện tại đã được phê duyệt. Các tệp
sinh ra khi build bị loại trừ, D01_L09 được khôi phục về HEAD đóng băng đã commit, và dự án L10 kế
thừa đã vượt qua clean baseline build.

## 3. Files Changed / Các tệp đã thay đổi

### Implementation / Triển khai

- `src/main/java/frc/robot/Constants.java`
- `src/main/java/frc/robot/RobotContainer.java`
- `src/main/java/frc/robot/commands/shooter/ManualShootCommand.java`
- `src/main/java/frc/robot/controls/FlywheelInputProcessor.java`

### Documentation / Tài liệu

- `README.md`
- `LESSON_STATUS.md`
- `drivebase_hardware_map.docx`
- `drivebase_hardware_map.pdf`
- `docs/D01_L09_to_D01_L10_Basic_Integrated_Robot_Guide.md`
- `docs/D01_L09_to_D01_L10_Basic_Integrated_Robot_Guide.docx`
- `docs/D01_L09_to_D01_L10_Basic_Integrated_Robot_Guide.pdf`

## 4. Step-by-Step Implementation / Triển khai từng bước

### Step 1 - Inherit the tested baseline / Kế thừa trạng thái đã kiểm thử

- **Objective / Mục tiêu:** Start from the approved D01_L09 working state.
- **Why / Lý do:** Preserve incremental inheritance and the tested shooter-delay work.
- **Action / Thao tác:** Copy D01_L09 to D01_L10 without generated build state; restore frozen
  D01_L09 to committed HEAD.
- **Files Changed / Tệp thay đổi:** Complete inherited D01_L10 project.
- **Verification / Kiểm tra:** D01_L09 clean; D01_L10 clean baseline build PASS.
- **Expected Result / Kết quả mong đợi:** A buildable L10 project with the approved inherited
  behavior.

### Step 2 - Standardize Xbox controls / Chuẩn hóa điều khiển Xbox

- **Objective / Mục tiêu:** Put all existing mechanisms on one driver layout.
- **Why / Lý do:** Provide predictable integrated robot operation.
- **Action / Thao tác:** Reserve A/B/Start/Back; bind X to flywheel-only and retain Y for the full
  shooter sequence. Keep tank sticks, triggers, and bumpers assigned to their mechanisms.
- **Files Changed / Tệp thay đổi:** `RobotContainer.java`, `FlywheelInputProcessor.java`.
- **Verification / Kiểm tra:** Source inspection, simulation, Driver Station / Glass, real robot.
- **Expected Result / Kết quả mong đợi:** Every requested input controls only its assigned action.

### Step 3 - Correct drivetrain hardware identity / Sửa nhận dạng phần cứng truyền động

- **Objective / Mục tiêu:** Make software Left/Right match physical Left/Right.
- **Why / Lý do:** Previous lessons labeled the two physical CAN pairs on the wrong sides.
- **Action / Thao tác:** Assign Left to leader/follower CAN `10 -> 7` with leader inversion `true`;
  assign Right to `11 -> 8` with leader inversion `false`.
- **Files Changed / Tệp thay đổi:** `Constants.java`, lesson hardware-map DOCX/PDF.
- **Verification / Kiểm tra:** Isolated-side tests, straight forward/reverse tests, Drive telemetry.
- **Expected Result / Kết quả mong đợi:** Left stick moves only physical left; right stick moves
  only physical right; equal forward inputs drive straight forward.

### Step 4 - Complete the shooter sequence / Hoàn thiện chuỗi shooter

- **Objective / Mục tiêu:** Produce the final safe Y-button shooting sequence.
- **Why / Lý do:** The flywheel must reach speed before feeding a game piece.
- **Action / Thao tác:** Set flywheel output to `0.60`, spin-up delay to `1.0 s`, and feeder output
  to `0.40`. Stop both motors and reset the timer on release or interruption.
- **Files Changed / Tệp thay đổi:** `Constants.java`, `ManualShootCommand.java`.
- **Verification / Kiểm tra:** Observe immediate flywheel output, delayed feeder output, continuous
  hold operation, safe stop, and full delay on the next press.
- **Expected Result / Kết quả mong đợi:** Y performs Flywheel -> Delay -> Feeder safely and
  repeatably.

### Step 5 - Verify and freeze / Kiểm tra và đóng băng

- **Objective / Mục tiêu:** Confirm the final lesson and freeze it.
- **Why / Lý do:** A completed lesson must be reproducible and documented.
- **Action / Thao tác:** Run clean build; record simulation, Driver Station / Glass, real-robot,
  architecture, and documentation results.
- **Files Changed / Tệp thay đổi:** `README.md`, `LESSON_STATUS.md`, transition guide files.
- **Verification / Kiểm tra:** Final build, user-confirmed tested source, DOCX/PDF visual QA.
- **Expected Result / Kết quả mong đợi:** D01_L10 is COMPLETE and FROZEN.

## 5. Final Architecture / Kiến trúc cuối cùng

```text
Driver
-> Xbox Controller
-> controls
-> commands
-> subsystems
-> io
-> hardware

Subsystems
-> RobotTelemetry
-> telemetry facades
-> NetworkTables / Glass
```

**EN:** RobotContainer remains the composition root. No new subsystem, IO contract, hardware
ownership, telemetry control path, or reversed dependency was introduced.

**VI:** RobotContainer vẫn là composition root. Không có subsystem, IO contract, quyền sở hữu phần
cứng, luồng điều khiển telemetry hoặc phụ thuộc ngược mới nào được thêm vào.

## 6. Final Controller Layout / Bố cục điều khiển cuối cùng

| Input | EN | VI |
| --- | --- | --- |
| Left Stick Y | Physical left tank drive | Truyền động bánh xích trái vật lý |
| Right Stick Y | Physical right tank drive | Truyền động bánh xích phải vật lý |
| RT | Intake | Thu bóng |
| LT | Outtake | Nhả bóng |
| RB | Feeder forward | Feeder tiến |
| LB | Feeder reverse | Feeder lùi |
| X | Flywheel only at `0.60` | Chỉ flywheel ở `0.60` |
| Y | Full timed shooter | Chuỗi shooter đầy đủ có thời gian |
| A, B, Start, Back | Reserved | Dự phòng |

## 7. Shooter Sequence / Chuỗi Shooter

1. **EN:** Hold Y; flywheel starts immediately at `0.60`.
   **VI:** Giữ Y; flywheel chạy ngay ở `0.60`.
2. **EN:** Wait `1.0 s` for spin-up.
   **VI:** Chờ `1.0 s` để flywheel tăng tốc.
3. **EN:** Feeder starts at `0.40`; both continue while Y remains held.
   **VI:** Feeder chạy ở `0.40`; cả hai tiếp tục chạy khi vẫn giữ Y.
4. **EN:** Release, disable, or interruption stops both immediately and resets the timer.
   **VI:** Nhả nút, disable hoặc ngắt lệnh sẽ dừng cả hai ngay và đặt lại bộ đếm thời gian.

## 8. Verification Checklist / Danh sách kiểm tra

- [x] Frozen Backbone preserved / Giữ nguyên Frozen Backbone.
- [x] RobotContainer remains composition-root only / RobotContainer chỉ là composition root.
- [x] No new subsystem or IO / Không thêm subsystem hoặc IO.
- [x] Left stick controls physical left / Cần trái điều khiển bên trái vật lý.
- [x] Right stick controls physical right / Cần phải điều khiển bên phải vật lý.
- [x] Equal forward inputs drive straight / Hai cần tiến làm robot đi thẳng.
- [x] Controller layout verified / Bố cục tay cầm đã kiểm tra.
- [x] Shooter `0.60 -> 1.0 s -> 0.40` verified / Chuỗi shooter đã kiểm tra.
- [x] Release/interruption safe stop verified / Dừng an toàn khi nhả hoặc ngắt đã kiểm tra.
- [x] Clean build PASS / Clean build PASS.
- [x] Simulation PASS / Simulation PASS.
- [x] Driver Station / Glass PASS.
- [x] Real robot PASS / Robot thật PASS.

## 9. Final Results / Kết quả cuối cùng

| Item / Hạng mục | Result / Kết quả | Evidence / Bằng chứng |
| --- | --- | --- |
| Architecture / Kiến trúc | PASS | Frozen responsibilities and dependency direction preserved |
| Baseline Build | PASS | Inherited L10 baseline |
| Final Clean Build | PASS | `BUILD SUCCESSFUL in 24s` |
| Simulation | PASS | User-confirmed current tested source |
| Driver Station / Glass | PASS | User-confirmed current tested source |
| Real Robot | PASS | User-confirmed current tested source |
| Documentation | PASS | Markdown, DOCX, and PDF completed and visually verified |
| Git Commit | NOT TESTED | Not requested |
| Git Push | NOT TESTED | Not requested |
| Known Issues | NONE | No known issue reported |

**Final status / Trạng thái cuối:** `COMPLETE` and `FROZEN`.
