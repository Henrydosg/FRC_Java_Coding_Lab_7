# FRC Java Coding Lab 7.0
## Bản đồ học tập sinh viên / Hướng dẫn tiến trình khóa học

Đây là bản đồ dành cho sinh viên, được xây dựng từ trạng thái hiện tại của repository. Bản đồ giải thích chuỗi học tập chính có thẩm quyền `S00 -> A00 -> A01 -> V00` và ghi nhận `D00 -> D01` như một nhánh song song riêng. Tài liệu này không cho phép sửa source, đổi thứ tự bài, hay mở rộng roadmap.

### 1. Cách sử dụng hướng dẫn này

Đây là một khóa học tích lũy. Mỗi lesson mới không phải là một mini-project tách rời. Lesson mới được tạo từ bản sao của lesson trước đã hoàn tất, sau đó thêm đúng một bước học tập có phạm vi rõ ràng.

```text
lesson trước: COMPLETE / FROZEN / READ-ONLY
        |
        +--> sao chép, đổi tên, dọn artifact tạo tự động
              |
              +--> baseline build và inheritance audit
                    |
                    +--> Design Lock: đúng một concept mới
                          |
                          +--> implement -> test -> regression
                                |
                                +--> Simulation trước, robot thật sau
                                      |
                                      +--> docs -> review -> freeze
```

Hãy dùng mỗi block lesson để trả lời năm câu hỏi: mình đã biết gì, concept mới là gì, concept đó thuộc package nào, bằng chứng nào được phép gọi là đã kiểm chứng, và lesson tiếp theo sẽ kế thừa phần nào. `PASS` chỉ có nghĩa trong đúng phạm vi đã được kiểm chứng; nó không tự động chứng minh tuning thi đấu, độ chính xác tuyệt đối, hay mọi cấu hình phần cứng.

### 2. Triết lý kiến trúc của khóa học

| Thành phần | Trách nhiệm sinh viên phải học | Ranh giới không được vượt qua |
| --- | --- | --- |
| Driver / Xbox Controller | Tạo ý định điều khiển của người vận hành. | Không nói chuyện trực tiếp với motor hay sensor. |
| `controls` | Lấy một mẫu input nhất quán và biến đổi input của driver. | Không chứa cơ chế điều khiển mechanism. |
| `commands` | Điều phối hành động và yêu cầu subsystem. | Không sở hữu hardware state. |
| `subsystems` | Sở hữu hành vi mechanism, state và luật an toàn. | Không truy cập vendor hardware trực tiếp. |
| `io` | Cô lập adapter hardware và snapshot `Inputs`. | Không quyết định policy của robot. |
| `observation` | Cung cấp read model bất biến, vendor-neutral và evaluator thuần. | Không có hardware, NetworkTables, scheduler hay mutable mechanism state. |
| `telemetry` | Đọc và publish `Observation` một cách read-only. | Không điều khiển robot. |
| Autonomous | Dùng command, pose, target và readiness đã được cung cấp. | Không gọi camera, VisionIO hay vendor API trực tiếp. |
| Vision | Cung cấp measurement có quality và timestamp để subsystem xem xét. | Không reset pose liên tục và không tự sở hữu `SwerveDrivePoseEstimator`. |

Hai luồng nền tảng phải luôn được giữ nguyên:

```text
Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware
hardware -> IOInputs -> subsystem / estimator -> immutable Observation
          -> telemetry -> NT4 / Glass / log
```

`RobotContainer` là **Composition Root**: tạo object, chọn implementation, inject dependency, đăng ký default command và button binding. Nó không xử lý hardware, không tính business logic, không phân tích input thay cho `controls`, và không tính telemetry. `Robot.java` sở hữu vòng đời TimedRobot và thứ tự gọi; nó cũng phải giữ boundary an toàn khi scheduler gặp runtime exception.

### 3. Mô hình kế thừa lesson

Một lesson mới phải được đọc như một diff nhỏ trên snapshot trước đó:

1. Copy lesson trước đã frozen; đổi tên theo roadmap chính thức.
2. Xóa `build/`, `.gradle/` và artifact sinh tự động; giữ source, docs và provenance cần thiết.
3. Chạy baseline build trước khi thêm concept. Nếu baseline fail, dừng và sửa inheritance/environment trước.
4. Đọc `LESSON_STATUS.md`, source, transition guide và các contract liên quan.
5. Chốt đúng một concept mới trong Design Lock. Không dùng lesson để refactor tùy ý.
6. Implement đầy đủ file Java, test focused, rồi chạy inherited regression và clean build.
7. Chạy Simulation trước. Sau đó User mới kiểm chứng Driver Station / Glass và real robot trong phạm vi phù hợp.
8. Cập nhật transition guide, evidence classification và final review. Lesson chỉ được frozen khi các gate bắt buộc đã có evidence.

Khi kế thừa, code cũ không phải là “mẫu tham khảo” để viết lại tùy thích; nó là contract đã được kiểm chứng. Một lesson chỉ được hoàn thành concept của chính nó, còn các vấn đề ngoài scope phải được ghi nhận là deferred hoặc known issue.

### 4. Bản đồ khóa học cấp cao

| Nhánh | Phạm vi | Số lesson | Quan hệ kế thừa | Vai trò trong khóa học |
| --- | --- | ---: | --- | --- |
| `S00` | Swerve Foundation | 24 | `S00_L01` bắt đầu một nền tảng WPILib mới; các lesson sau kế thừa lesson trước | Trục cơ khí, IO, observation, telemetry và teleop chính |
| `A00` | Autonomous Command Foundation | 4 | Kế thừa snapshot frozen của `S00_L24` | Đưa command lifecycle và autonomous safety vào robot |
| `A01` | Autonomous Navigation and Path Following | 9 | Kế thừa snapshot frozen của `A00_L04` | Từ pose/trajectory đến PathPlanner và event markers |
| `V00` | AprilTag Vision Observation and Pose Fusion | 9 | Kế thừa snapshot frozen của `A01_L09` | Từ coordinate frame và camera adapter đến fusion có kiểm soát |
| `D00 -> D01` | Tank drive và mechanisms | 17 | `D00_L01` là imported baseline; `D01_L01` kế thừa `D00_L06` | Nhánh lịch sử/song song cho tank drive, observation và mechanism |

Chuỗi chính có **46 lesson** (`24 + 4 + 9 + 9`). Nhánh D có **17 lesson** (`6 + 11`), nên repository hiện có **63 lesson directory**. D00/D01 không được tính là prerequisite của S00: đây là hai topology khác nhau, dù chúng cùng dạy những nguyên tắc ownership tương tự.

## Student Checkpoint

Trước khi sang module tiếp theo, sinh viên phải nói được: lesson trước là snapshot nào; concept mới là gì; package nào sở hữu nó; luồng control và observation đi qua đâu; evidence nào đã PASS, evidence nào NOT TESTED hoặc DEFERRED; và tại sao code mới không làm vỡ contract cũ.

### 5. Module S00 - Nền tảng Swerve

**Mục tiêu module:** xây dựng một nền tảng Swerve có ownership rõ ràng từ architecture, IO, subsystem, observation, telemetry đến teleop và localization. S00 không bắt đầu bằng “điều khiển motor ngay”; nó bắt đầu bằng boundary và evidence.

**Đầu vào / đầu ra:** đầu vào là một WPILib Command Robot foundation và các lesson S00 trước; đầu ra là một Swerve snapshot có teleop, odometry, pose và autonomous-readiness boundary. S00 không bao gồm vision fusion.

**Hồ sơ evidence:** lý thuyết được kiểm tra xuyên suốt; Simulation chỉ được gọi là PASS khi lesson status ghi nhận; commissioning và driving thực tế chỉ có ý nghĩa trong phạm vi được nêu. Tuning thi đấu và bảo trì hardware chưa giải quyết không được nâng cấp thành sự thật của curriculum.

#### S00_L01 - Nền tảng kiến trúc Swerve

**Kiến thức trước đó:** Một project WPILib Command Robot mới.

**Một concept mới:** Skeleton package và dependency được quản trị cho Swerve.

**Vì sao có lesson này:** Drivetrain bốn module có nhiều trách nhiệm. Sinh viên cần có bản đồ trước khi thêm device, nếu không `RobotContainer`, command hoặc subsystem sẽ vô tình sở hữu tất cả.

**Điều thay đổi:** Tạo package structure hướng Swerve, vocabulary control/observation frozen và kỳ vọng về Composition Root, nhưng chưa có mechanism placeholder.

**Điều không thay đổi:** Chưa thêm hardware, vendor adapter, kinematics, motor output hay hành vi mechanism.

**Luồng kiến trúc:** `RobotContainer -> SwerveSubsystem` tương lai `-> IO` tương lai; observation boundary được dành chỗ nhưng chưa có dữ liệu.

**Kết quả sinh viên:** Chỉ ra class Swerve tương lai phải nằm ở đâu và bác bỏ thiết kế button gọi thẳng motor.

**Ý nghĩa kiểm chứng:** Lý thuyết/architecture là `THEORY VERIFIED`; Simulation theo status là `SIMULATION VERIFIED`; Driver Station / Glass và robot thật không áp dụng cho scope architecture-only.

**Nối sang lesson sau:** Khi đã có bản đồ, câu hỏi tiếp theo là physical fact nào đã biết và fact nào vẫn chỉ là assumption.

#### S00_L02 - Audit phần cứng Swerve

**Kiến thức trước đó:** Baseline architecture-only của S00_L01.

**Một concept mới:** Hardware inventory dựa trên evidence trước khi implement.

**Vì sao có lesson này:** CAN ID, ratio, offset hoặc dấu trục đoán sai có thể trở thành false authority khi chép vào code. Audit tách verified fact khỏi điều chưa biết.

**Điều thay đổi:** Ghi nhận tài liệu hardware và danh sách rõ ràng các câu hỏi còn thiếu về device identity, gearing, inversion, IMU và calibration.

**Điều không thay đổi:** Java source, vendor dependency, IO implementation và robot behavior không đổi.

**Luồng kiến trúc:** `hardware evidence -> audit record -> quyết định Constants/IO sau này`.

**Kết quả sinh viên:** Gắn nhãn một phát biểu hardware là verified, provisional hoặc unresolved mà không điền khoảng trống bằng trí nhớ.

**Ý nghĩa kiểm chứng:** Audit theory là `THEORY VERIFIED`; Simulation, Driver Station / Glass và runtime không áp dụng; physical fact chưa được audit xác lập là `REAL HARDWARE DEFERRED`.

**Nối sang lesson sau:** Khi device map rõ ràng, vendor có thể được cô lập sau IO contract.

#### S00_L03 - Nền tảng CTRE IO

**Kiến thức trước đó:** Device identity đã audit và boundary calibration chưa biết từ S00_L02.

**Một concept mới:** IO interface và CTRE adapter tách vendor API khỏi subsystem.

**Vì sao có lesson này:** Vendor code phải có một nơi duy nhất. Nếu subsystem biết CTRE, việc thay hardware, chạy Simulation và review contract sẽ khó hơn.

**Điều thay đổi:** Tạo contract cho module/IMU IO, snapshot `Inputs`, real CTRE implementation và safe stop theo phạm vi lesson.

**Điều không thay đổi:** IO chưa sở hữu drive policy, kinematics, command hay telemetry; Constants vẫn là authority cấu hình.

**Luồng kiến trúc:** `CTRE hardware -> SwerveModuleIOCTRE -> Inputs -> future SwerveSubsystem`.

**Kết quả sinh viên:** Giải thích vì sao subsystem gọi interface thay vì gọi Phoenix API.

**Ý nghĩa kiểm chứng:** Contract/build và Simulation theo status được phân loại riêng; real CTRE evidence chỉ được công nhận khi status ghi rõ, còn giá trị chưa xác nhận vẫn deferred.

**Nối sang lesson sau:** Có IO boundary rồi mới có thể tạo subsystem sở hữu hành vi.

#### S00_L04 - Nền tảng SwerveSubsystem

**Kiến thức trước đó:** IO contract và adapter từ S00_L03.

**Một concept mới:** `SwerveSubsystem` là owner duy nhất của mechanism state và hành vi an toàn.

**Vì sao có lesson này:** Nhiều command không được tự điều khiển từng module. Một subsystem trung tâm là nơi hợp nhất input, state, stop và output.

**Điều thay đổi:** Subsystem đọc `Inputs`, cập nhật state và cung cấp API cấp mechanism; RobotContainer chỉ inject dependency.

**Điều không thay đổi:** Commands chưa trở thành nơi chứa hardware logic; telemetry chưa tự publish từ mutable IOInputs.

**Luồng kiến trúc:** `IO -> SwerveSubsystem -> module output`; read model sẽ được thêm ở lesson sau.

**Kết quả sinh viên:** Nhận diện một lời gọi trực tiếp từ command tới motor là vi phạm ownership.

**Ý nghĩa kiểm chứng:** Đây là lesson architecture/subsystem foundation; không tự suy ra rằng robot đã sẵn sàng chạy nhanh ngoài scope evidence.

**Nối sang lesson sau:** Subsystem cần xuất ra read model bất biến để phần còn lại quan sát mà không chiếm ownership.

#### S00_L05 - Nền tảng Observation

**Kiến thức trước đó:** Subsystem đọc IO nhưng chưa có read model công khai từ S00_L04.

**Một concept mới:** Immutable, vendor-neutral `SwerveObservation`.

**Vì sao có lesson này:** Telemetry và evaluator cần dữ liệu ổn định, không được giữ reference tới mutable IO snapshot.

**Điều thay đổi:** Copy dữ liệu cần thiết từ `IOInputs` sang Observation bất biến có validity/state rõ ràng.

**Điều không thay đổi:** Observation không truy cập hardware, không gọi NetworkTables, không chạy command và không sở hữu control behavior.

**Luồng kiến trúc:** `IOInputs -> SwerveSubsystem -> immutable SwerveObservation`.

**Kết quả sinh viên:** Phân biệt “snapshot sự thật” với “lệnh điều khiển”.

**Ý nghĩa kiểm chứng:** Contract và immutable-data test là evidence chính; runtime visualization chỉ được nêu khi status có evidence.

**Nối sang lesson sau:** Khi observation ổn định, telemetry có thể đọc nó mà không phá architecture.

#### S00_L06 - Nền tảng Telemetry

**Kiến thức trước đó:** `SwerveObservation` bất biến từ S00_L05.

**Một concept mới:** Read-only telemetry facade tiêu thụ Observation.

**Vì sao có lesson này:** NT4/Glass/log là nơi quan sát, không phải nơi quyết định robot phải làm gì.

**Điều thay đổi:** Tạo telemetry API publish field đã chọn từ Observation, theo kiểu và tên ổn định.

**Điều không thay đổi:** Telemetry không gọi IO, không tính hành vi mới và không ghi ngược vào subsystem.

**Luồng kiến trúc:** `SwerveObservation -> telemetry -> NT4 / Glass / log`.

**Kết quả sinh viên:** Tìm được vì sao publish trực tiếp từ mutable IOInputs là sai boundary.

**Ý nghĩa kiểm chứng:** Contract/build là evidence; chưa có runtime display thì phải ghi `NOT TESTED`, không suy đoán từ code.

**Nối sang lesson sau:** Có facade rồi, lesson tiếp theo đặt đúng thứ tự cập nhật và publish trong runtime.

#### S00_L07 - Tích hợp telemetry runtime

**Kiến thức trước đó:** Telemetry facade từ S00_L06.

**Một concept mới:** Runtime order: scheduler/subsystem update trước, telemetry publish sau.

**Vì sao có lesson này:** Nếu publish trước khi subsystem cập nhật, dashboard sẽ hiển thị frame cũ và làm sai chẩn đoán.

**Điều thay đổi:** Đưa telemetry vào vòng đời runtime với thứ tự deterministic sau update.

**Điều không thay đổi:** Telemetry vẫn read-only; không có đường từ NT4 trở lại control behavior.

**Luồng kiến trúc:** `Robot.robotPeriodic -> scheduler/subsystems -> Observation -> telemetry -> NT4/Glass`.

**Kết quả sinh viên:** Vẽ được một chu kỳ dữ liệu và giải thích vì sao thứ tự là một contract.

**Ý nghĩa kiểm chứng:** Runtime Simulation và Driver Station / Glass được coi là PASS chỉ theo evidence của lesson; real robot chưa tự động suy ra.

**Nối sang lesson sau:** Sau khi quan sát đúng thời điểm, ta cần một module state được đo lường để pipeline Swerve có dữ liệu thật.

#### S00_L08 - Nền tảng module state Swerve

**Kiến thức trước đó:** Runtime observation/telemetry từ S00_L07.

**Một concept mới:** Measured `SwerveModuleState` từ module Inputs.

**Vì sao có lesson này:** Chassis command cần phân biệt state mong muốn và state thực tế; nếu trộn hai khái niệm, dashboard và closed-loop sẽ không đáng tin.

**Điều thay đổi:** Đưa tốc độ/góc đo được vào model module với validity và đơn vị rõ ràng.

**Điều không thay đổi:** Chưa thêm kinematics, optimization hay driver command.

**Luồng kiến trúc:** `module hardware -> IOInputs -> SwerveSubsystem -> measured module state -> Observation`.

**Kết quả sinh viên:** Nói được module state “measured” khác “desired” như thế nào.

**Ý nghĩa kiểm chứng:** Contract/Simulation evidence chỉ bao phủ state model; không tuyên bố module đã tuned.

**Nối sang lesson sau:** Khi measured state rõ, lớp chassis có thể nhận request theo kiểu WPILib.

#### S00_L09 - Nền tảng ChassisSpeeds

**Kiến thức trước đó:** Measured module state và subsystem boundary từ S00_L08.

**Một concept mới:** Request chassis dạng `ChassisSpeeds` và defensive input boundary.

**Vì sao có lesson này:** Driver/autonomous nên nói bằng vx, vy, omega, không biết chi tiết bốn module.

**Điều thay đổi:** Tạo API nhận chassis request và lưu một bản copy an toàn của robot-relative intent.

**Điều không thay đổi:** Chưa chuyển chassis request thành module output; chưa có kinematics, field-relative, actuation hoặc hardware output.

**Luồng kiến trúc:** `robot-relative chassis request -> SwerveSubsystem -> copied ChassisSpeeds intent`.

**Kết quả sinh viên:** Hiểu subsystem lưu giữ robot-relative intent; kinematics thuộc S00_L10 và actuation thuộc các lesson sau.

**Ý nghĩa kiểm chứng:** Deterministic tests và Simulation theo status xác nhận boundary; behavior ngoài giới hạn test không được phóng đại.

**Nối sang lesson sau:** `ChassisSpeeds` sẽ được kinematics chuyển thành yêu cầu cho từng module.

#### S00_L10 - Nền tảng SwerveDriveKinematics

**Kiến thức trước đó:** Chassis request hợp lệ từ S00_L09.

**Một concept mới:** Ánh xạ `ChassisSpeeds` sang bốn `SwerveModuleState` bằng `SwerveDriveKinematics`.

**Vì sao có lesson này:** Đây là bước toán học biến ý định robot-level thành yêu cầu module-level.

**Điều thay đổi:** Tạo geometry/kinematics contract với module locations được cấu hình có authority.

**Điều không thay đổi:** Kinematics không biết vendor, không publish telemetry và không tự drive hardware.

**Luồng kiến trúc:** `ChassisSpeeds -> SwerveDriveKinematics -> desired module states`.

**Kết quả sinh viên:** Giải thích translation và rotation ảnh hưởng module state ra sao.

**Ý nghĩa kiểm chứng:** Mathematical/contract tests và Simulation theo status; không phải tuning wheelbase thực tế.

**Nối sang lesson sau:** Desired states cần optimization để giảm việc quay module không cần thiết.

#### S00_L11 - Nền tảng tối ưu SwerveModuleState

**Kiến thức trước đó:** Desired module states từ S00_L10.

**Một concept mới:** `SwerveModuleState.optimize(...)` để chọn góc gần hơn và đảo tốc độ khi phù hợp.

**Vì sao có lesson này:** Module không nên quay gần 180 độ nếu có thể đạt cùng hướng chuyển động bằng cách đảo vận tốc.

**Điều thay đổi:** Thêm measured angle làm reference cho optimization trước khi command hardware.

**Điều không thay đổi:** Optimization không đổi desired chassis intent và không tự chịu trách nhiệm cho calibration.

**Luồng kiến trúc:** `desired state + measured angle -> optimized state -> output pipeline`.

**Kết quả sinh viên:** Phân biệt biến đổi điều khiển tương đương với thay đổi mục tiêu robot.

**Ý nghĩa kiểm chứng:** Tests/Simulation xác nhận quy tắc tối ưu; physical steering quality vẫn phụ thuộc hardware và tuning.

**Nối sang lesson sau:** Optimized state được đưa qua pipeline output có thứ tự và boundary rõ ràng.

#### S00_L12 - Nền tảng output pipeline Swerve

**Kiến thức trước đó:** Kinematics và optimization từ S00_L10/L11.

**Một concept mới:** Pipeline hoàn chỉnh từ chassis request đến module output.

**Vì sao có lesson này:** Một request phải đi qua validation, kinematics, optimization, giới hạn và safe-stop theo thứ tự nhất quán.

**Điều thay đổi:** Hợp nhất các bước thành đường đi deterministic tới từng module, chưa coi đó là bằng chứng tuning.

**Điều không thay đổi:** Command vẫn không gọi vendor; telemetry vẫn chỉ đọc Observation.

**Luồng kiến trúc:** `ChassisSpeeds -> validation -> kinematics -> optimization -> module output -> IO`.

**Kết quả sinh viên:** Trace một driver request qua toàn bộ pipeline và chỉ ra chỗ fail-closed.

**Ý nghĩa kiểm chứng:** Contract/unit/Simulation evidence xác nhận flow; real motion chỉ được ghi theo lesson status.

**Nối sang lesson sau:** Khi pipeline có hình dạng đúng, lesson sau mới được phép nói về điều khiển module.

#### S00_L13 - Nền tảng điều khiển module Swerve

**Kiến thức trước đó:** Output pipeline từ S00_L12.

**Một concept mới:** Ownership của final desired state cho mỗi module trong `SwerveSubsystem`.

**Vì sao có lesson này:** `SwerveSubsystem` cần giữ final desired state sau pure output pipeline để state đó có thể được đọc và kiểm tra trước các lesson actuation.

**Điều thay đổi:** `SwerveSubsystem` sở hữu final desired state và read-only access phù hợp trong lesson; chưa thêm đường điều khiển IO hoặc hardware output.

**Điều không thay đổi:** Không đưa CTRE/REV API ra ngoài IO; chưa thêm hardware actuation, chưa gán input driver và chưa khẳng định physical tuning. Closed-loop và four-module actuation thuộc các lesson sau.

**Luồng kiến trúc:** `optimized state -> SwerveSubsystem final desired state -> read-only lesson boundary`.

**Kết quả sinh viên:** Giải thích được đâu là final desired state và đâu là measured state.

**Ý nghĩa kiểm chứng:** Contract/build và focused tests chỉ xác nhận final-state ownership; Simulation, Driver Station / Glass và real robot là `NOT APPLICABLE` vì lesson không thêm runtime hardware path.

**Nối sang lesson sau:** Trước khi cho module quay, cần một bước commissioning an toàn, read-only khi robot Disabled.

#### S00_L14 - Nền tảng commissioning cấu hình hardware

**Kiến thức trước đó:** Final module output và IO safe-stop từ S00_L13.

**Một concept mới:** Nền tảng hardware commissioning theo lifecycle và bằng chứng Disabled/read-only; configuration apply/readback contract thuộc S00_L16.

**Vì sao có lesson này:** Configuration sai có thể làm hỏng hardware. Lesson này dạy cách phân loại audit và bằng chứng read-only trước khi nói đến configuration contract hoặc actuation.

**Điều thay đổi:** Ghi nhận hardware audit và commissioning Disabled/read-only theo evidence boundary; không thêm configuration apply/readback contract.

**Điều không thay đổi:** Chưa cho phép driver điều khiển; không ghi configuration, không thực hiện apply/readback/health contract của S00_L16, không bỏ qua safe stop và không dùng dashboard làm control input.

**Luồng kiến trúc:** `hardware signals/status -> Disabled read-only commissioning evidence`.

**Kết quả sinh viên:** Phân biệt hardware audit/read-only evidence với configuration apply/readback contract, vốn được formalize ở S00_L16.

**Ý nghĩa kiểm chứng:** Disabled commissioning và evidence hardware chỉ được gọi PASS trong scope status; các giá trị chưa đo là `REAL HARDWARE DEFERRED`.

**Nối sang lesson sau:** Khi cấu hình được xác nhận an toàn, chỉ một module mới được phép chạy open-loop có giới hạn.

#### S00_L15 - Commissioning open-loop một module

**Kiến thức trước đó:** Hardware configuration commissioning từ S00_L14.

**Một concept mới:** Test một module ở open-loop với bounded request, watchdog và giới hạn an toàn.

**Vì sao có lesson này:** Một module riêng lẻ là boundary dễ quan sát hơn bốn module. Đây là bài học về evidence, không phải bài tuning thi đấu.

**Điều thay đổi:** Thêm command/test path có tốc độ nhỏ, thời lượng giới hạn và điều kiện stop rõ ràng.

**Điều không thay đổi:** Không mở rộng thành teleop; không cho phép request vô hạn, không chỉnh PID/feedforward.

**Luồng kiến trúc:** `test request -> bounded command -> SwerveSubsystem -> one module IO -> stop`.

**Kết quả sinh viên:** Biết thiết kế thử nghiệm có thể dừng được và phân biệt hướng trục với sức mạnh motor.

**Ý nghĩa kiểm chứng:** Disabled/config và open-loop evidence là PASS trong phạm vi ghi nhận; Sim có thể không được test riêng theo status, còn physical result phải giữ đúng scope.

**Nối sang lesson sau:** Sau open-loop, configuration contract cần apply/readback rõ hơn cho từng module.

#### S00_L16 - Contract cấu hình hardware module

**Kiến thức trước đó:** Open-loop commissioning có giới hạn từ S00_L15.

**Một concept mới:** One-module configuration contract với apply, readback và health/result handling.

**Vì sao có lesson này:** Cấu hình chỉ đáng tin khi có tiêu chí biết rằng nó được áp dụng đúng, không chỉ vì code chạy qua.

**Điều thay đổi:** Formalize các giá trị config, kết quả apply/readback và failure boundary.

**Điều không thay đổi:** Constants vẫn là authority; chưa đổi calibration, CAN ID hay tuning ngoài scope đã được phê duyệt.

**Luồng kiến trúc:** `Constants -> typed configuration -> IO apply -> readback -> subsystem readiness`.

**Kết quả sinh viên:** Đọc được configuration report và chỉ ra giá trị nào verified, provisional hoặc unknown.

**Ý nghĩa kiểm chứng:** Apply/readback trên Simulation/Glass/real robot là evidence theo status; không biến một lần readback thành chứng minh mọi match đều đúng.

**Nối sang lesson sau:** Có config contract, ta có thể điều khiển closed-loop một module.

#### S00_L17 - Closed-loop một module

**Kiến thức trước đó:** Configuration apply/readback từ S00_L16.

**Một concept mới:** Closed-loop speed/angle control cho một module.

**Vì sao có lesson này:** Closed-loop yêu cầu measured state đi vào controller và output phải đi qua IO boundary.

**Điều thay đổi:** Một module nhận desired state, dùng measurement để điều khiển và xuất output có giới hạn.

**Điều không thay đổi:** Chưa phối hợp bốn module, chưa thêm field-relative hay autonomous.

**Luồng kiến trúc:** `desired module state + measured Inputs -> SwerveSubsystem -> IO closed-loop output`.

**Kết quả sinh viên:** Phân biệt control error, desired state và hardware command.

**Ý nghĩa kiểm chứng:** Simulation/Glass và evidence tuning chỉ được gọi theo status; physical tuning boundary có thể vẫn deferred.

**Nối sang lesson sau:** Khi một module ổn định, cùng contract có thể được áp dụng cho bốn module.

#### S00_L18 - Actuation state cho bốn module

**Kiến thức trước đó:** Closed-loop module contract từ S00_L17.

**Một concept mới:** Điều phối bốn module bằng một state snapshot thống nhất.

**Vì sao có lesson này:** Swerve là một drivetrain; cập nhật rời rạc từng module có thể tạo output không nhất quán.

**Điều thay đổi:** SwerveSubsystem tính và gửi desired state cho FL/FR/BL/BR theo cùng một pipeline.

**Điều không thay đổi:** Không đổi ownership: subsystem vẫn là owner, IO vẫn là adapter, telemetry vẫn read-only.

**Luồng kiến trúc:** `validated chassis request -> four desired states -> four module IO adapters -> hardware`.

**Kết quả sinh viên:** Trace được một output tới cả bốn module và hiểu centralized stop.

**Ý nghĩa kiểm chứng:** Simulation, Glass và real evidence PASS chỉ trong recorded scope; drift, calibration hoặc bảo trì phần cứng chưa giải quyết không bị che giấu.

**Nối sang lesson sau:** Drivetrain đã có actuation; tiếp theo input của người vận hành cần được sở hữu đúng nơi.

#### S00_L19 - Xử lý input của driver

**Kiến thức trước đó:** Four-module actuation từ S00_L18.

**Một concept mới:** Một controller sample nhất quán và immutable `DriverInputObservation` cho external operator input.

**Vì sao có lesson này:** Nếu nhiều lớp đọc controller độc lập trong cùng vòng lặp, cùng một thao tác có thể tạo các giá trị khác nhau. Exception này chỉ dành cho external human/operator input, không phải mechanism Observation.

**Điều thay đổi:** `controls` đọc một coherent sample, deadband/scale/clamp theo contract và tạo observation vendor-neutral cho driver intent.

**Điều không thay đổi:** Controls không được tạo mechanism Observation; không gọi motor, không truy cập subsystem state tùy ý và không điều khiển telemetry.

**Luồng kiến trúc:** `raw controller input -> controls processing -> immutable DriverInputObservation -> read-only telemetry`.

**Kết quả sinh viên:** Nói rõ vì sao raw controller access phân tán làm mất tính nhất quán, và vì sao L19 chưa tạo command, Swerve request hay robot movement.

**Ý nghĩa kiểm chứng:** Sim/Glass và Disabled/no-actuation evidence được phân loại theo status; real teleop chỉ được khẳng định khi User evidence có trong status.

**Nối sang lesson sau:** Driver intent đã chuẩn hóa; command tiếp theo sẽ dùng nó cho robot-relative teleop.

#### S00_L20 - Tích hợp teleop robot-relative

**Kiến thức trước đó:** `DriverInputObservation` và Swerve actuation từ S00_L19.

**Một concept mới:** Default command robot-relative nối driver intent với chassis request.

**Vì sao có lesson này:** Đây là lần đầu ý định driver đi qua trọn control backbone để tạo chuyển động có ownership đúng.

**Điều thay đổi:** Tạo teleop command, inject subsystem/input dependency và gửi `ChassisSpeeds` robot-relative.

**Điều không thay đổi:** Chưa dùng heading field-relative; không đọc camera; command không gọi vendor API.

**Luồng kiến trúc:** `DriverInputObservation -> teleop command -> ChassisSpeeds -> SwerveSubsystem -> IO`.

**Kết quả sinh viên:** Trace một joystick request từ mẫu input tới module output mà không có shortcut.

**Ý nghĩa kiểm chứng:** Simulation, Glass và real teleop được gọi PASS theo evidence; mọi claim về tốc độ/độ chính xác ngoài scope là không hợp lệ.

**Nối sang lesson sau:** Teleop đã có, nhưng cần kiểm chứng vật lý bounded trên sàn trước khi thêm semantics cao hơn.

#### S00_L21 - Xác nhận drive đầu tiên trên sàn

**Kiến thức trước đó:** Robot-relative teleop từ S00_L20.

**Một concept mới:** Quy trình floor validation có người quan sát, vùng an toàn và tiêu chí dừng.

**Vì sao có lesson này:** Simulation không thể thay thế mọi rủi ro vật lý. Người học phải học cách chuyển từ code PASS sang commissioning có trách nhiệm.

**Điều thay đổi:** Ghi nhận kiểm tra hướng, stop, response và phạm vi chuyển động trên sàn.

**Điều không thay đổi:** Không tuyên bố tuning hoàn tất; không bypass mode gate, watchdog hoặc centralized stop.

**Luồng kiến trúc:** `Driver -> controls -> command -> SwerveSubsystem -> IO -> floor response -> stop`.

**Kết quả sinh viên:** Viết được checklist test nhỏ và biết dừng khi evidence mâu thuẫn.

**Ý nghĩa kiểm chứng:** Physical floor evidence là PASS trong scope recorded; nếu chỉ có build/Sim thì phải ghi thiếu real evidence.

**Nối sang lesson sau:** Khi robot-relative behavior đã có evidence, heading IMU mới được đưa vào field-relative transform.

#### S00_L22 - Điều khiển field-relative

**Kiến thức trước đó:** Robot-relative teleop và floor validation từ S00_L20/L21.

**Một concept mới:** Chuyển driver translation theo heading robot để tạo field-relative drive.

**Vì sao có lesson này:** Driver có thể giữ hướng di chuyển trên field trong khi robot quay, nhưng phép biến đổi phải dùng đúng reference frame.

**Điều thay đổi:** Đưa heading đã đo vào `ChassisSpeeds.fromFieldRelativeSpeeds` hoặc contract tương đương.

**Điều không thay đổi:** A01 vẫn là owner của alliance transform; V00 vision không được tự alliance-flip measurement.

**Luồng kiến trúc:** `DriverInputObservation + measured heading -> field transform -> ChassisSpeeds -> SwerveSubsystem`.

**Kết quả sinh viên:** Phân biệt robot frame, field frame và alliance transform.

**Ý nghĩa kiểm chứng:** Sim/Glass/real field-relative evidence theo status; drift hoặc BL anomaly được ghi là known/deferred nếu status yêu cầu.

**Nối sang lesson sau:** Có chassis pose logic, lesson sau mới hiển thị odometry và Field2d.

#### S00_L23 - Odometry và hiển thị pose

**Kiến thức trước đó:** Field-relative drive và measured module/IMU state.

**Một concept mới:** Cập nhật odometry/pose và publish `Field2d` như một read model.

**Vì sao có lesson này:** Pose là cầu nối giữa drivetrain sensing và autonomous/vision, nhưng hiển thị pose không đồng nghĩa với fusion vision.

**Điều thay đổi:** `SwerveSubsystem` sở hữu pose update; telemetry đưa pose và Field2d ra NT4/Glass.

**Điều không thay đổi:** Telemetry không điều khiển; camera chưa cung cấp measurement; pose-estimator ownership và vision fusion được defer đến S00_L24/V00_L09.

**Luồng kiến trúc:** `module/IMU Inputs -> SwerveSubsystem odometry -> immutable pose Observation -> Field2d/telemetry`.

**Kết quả sinh viên:** Nói được odometry dùng measurement nào và vì sao pose là subsystem state.

**Ý nghĩa kiểm chứng:** Simulation, Glass và real odometry evidence chỉ có ý nghĩa trong scope; drift không tự biến thành lỗi vision.

**Nối sang lesson sau:** Pose và readiness boundary đã rõ, tạo nền cho autonomous command ở A00.

#### S00_L24 - Pose estimation và autonomous readiness

**Kiến thức trước đó:** Odometry, pose và Field2d từ S00_L23.

**Một concept mới:** Boundary read-only để autonomous dùng `getEstimatedPose()` và readiness mà không chiếm pose ownership.

**Vì sao có lesson này:** Các module sau cần pose nhưng không được gọi IO hoặc reset estimator. S00 phải đóng lại bằng một contract rõ ràng.

**Điều thay đổi:** Chuẩn hóa pose accessor/readiness và điều kiện reset/stop thuộc SwerveSubsystem.

**Điều không thay đổi:** Vision fusion chưa được implement; autonomous không truy cập camera; `SwerveDrivePoseEstimator` vẫn chỉ có một owner.

**Luồng kiến trúc:** `IOInputs -> SwerveSubsystem estimator -> getEstimatedPose/readiness -> autonomous/telemetry`.

**Kết quả sinh viên:** Giải thích vì sao A00/A01 được phép dùng pose nhưng không được sở hữu estimator.

**Ý nghĩa kiểm chứng:** Localization/reset evidence được phân loại theo status; vision fusion được để dành cho V00.

**Nối sang lesson sau:** S00 đóng tại đây; A00 bắt đầu với autonomous command lifecycle trên snapshot frozen này.

## Student Checkpoint

Bạn chỉ nên rời S00 khi có thể vẽ cả hai backbone, chỉ ra `SwerveSubsystem` là owner, giải thích IO/Inputs/Observation/telemetry khác nhau thế nào, và nói rõ `getEstimatedPose()` là read-only boundary. Bạn cũng phải biết phần nào đã được Simulation hoặc User kiểm chứng và phần nào vẫn deferred.

### 6. Module A00 - Nền tảng Autonomous Command

**Mục tiêu module:** đưa robot từ teleop foundation sang autonomous có lifecycle và safety. A00 chỉ có bốn lesson; nó đóng ở A00_L04. `A00_L05` không thuộc roadmap được phép.

**Tiến trình concept:** lifecycle -> scheduler ownership -> bounded robot-relative motion -> Autonomous-enabled safety gate.

**Nguyên tắc:** A00 không nhảy thẳng vào PathPlanner. PathPlanner chỉ xuất hiện từ A01_L06, còn AutoBuilder từ A01_L07.

#### A00_L01 - Nền tảng autonomous command lifecycle

**Kiến thức trước đó:** Frozen S00_L24 với pose/readiness boundary.

**Một concept mới:** Command `initialize/execute/isFinished/end` trong autonomous lifecycle.

**Vì sao có lesson này:** Sinh viên cần hiểu command sống và kết thúc thế nào trước khi robot được phép chuyển động.

**Điều thay đổi:** Tạo autonomous command không chuyển động, thể hiện lifecycle log/state.

**Điều không thay đổi:** Không thêm PathPlanner, không thêm motion, không bypass scheduler và không đổi Swerve ownership.

**Luồng kiến trúc:** `autonomousInit -> scheduler -> command lifecycle -> no-motion Swerve state`.

**Kết quả sinh viên:** Dự đoán được callback nào xảy ra khi mode bắt đầu, kết thúc hoặc bị cancel.

**Ý nghĩa kiểm chứng:** Simulation, Driver Station và real evidence PASS theo status cho zero-motion lifecycle.

**Nối sang lesson sau:** Khi lifecycle rõ, scheduler mới được giao quyền schedule autonomous.

#### A00_L02 - Scheduling autonomous mode

**Kiến thức trước đó:** Command lifecycle từ A00_L01.

**Một concept mới:** Scheduler là owner của việc schedule/cancel command theo mode.

**Vì sao có lesson này:** Gọi `initialize()` bằng tay hoặc tự gọi `execute()` sẽ phá scheduler contract và dễ tạo duplicate lifecycle.

**Điều thay đổi:** Autonomous command được chọn/schedule qua WPILib scheduler; mode transition được kiểm soát.

**Điều không thay đổi:** Chưa tạo motion và không cho RobotContainer thành business-logic owner.

**Luồng kiến trúc:** `Robot.autonomousInit -> selected command -> CommandScheduler -> lifecycle callbacks`.

**Kết quả sinh viên:** Nhận diện manual lifecycle delegation là lỗi kiến trúc.

**Ý nghĩa kiểm chứng:** Zero-motion Simulation/Driver Station/real evidence theo status; scheduler requirement vẫn là contract trung tâm.

**Nối sang lesson sau:** Scheduler đã đúng, lesson sau thêm bounded motion đầu tiên.

#### A00_L03 - Autonomous motion robot-relative có giới hạn

**Kiến thức trước đó:** Scheduler-owned autonomous từ A00_L02 và Swerve request contract.

**Một concept mới:** Command tạo robot-relative motion bounded bằng timeout/điều kiện kết thúc rõ ràng.

**Vì sao có lesson này:** Đây là bước nhỏ nhất để chứng minh autonomous có thể drive mà không cần PathPlanner.

**Điều thay đổi:** Autonomous command phát `ChassisSpeeds` giới hạn, yêu cầu Swerve và stop khi hoàn tất.

**Điều không thay đổi:** Chưa có trajectory, alliance transform, PathPlanner hay event marker.

**Luồng kiến trúc:** `autonomous command -> bounded ChassisSpeeds -> SwerveSubsystem -> centralized stop`.

**Kết quả sinh viên:** Thiết kế được motion có terminal condition và không restart ngoài ý muốn.

**Ý nghĩa kiểm chứng:** Simulation PASS và bounded real motion PASS theo status; Glass có thể chưa được test riêng.

**Nối sang lesson sau:** Motion đã có, nên safety gate phải chặn autonomous khi robot không Enabled.

#### A00_L04 - Gating an toàn cho autonomous motion

**Kiến thức trước đó:** Bounded autonomous motion từ A00_L03.

**Một concept mới:** Autonomous + Enabled safety invariant và centralized `SwerveSubsystem.stop()` authority.

**Vì sao có lesson này:** Chọn đúng command chưa đủ; output phải bị chặn khi mode hoặc enabled state không cho phép.

**Điều thay đổi:** Thêm gate cho autonomous output, cancel/stop behavior và safe terminal handling.

**Điều không thay đổi:** RobotContainer vẫn là Composition Root; chưa đưa PathPlanner vào; không đổi IO/tuning.

**Luồng kiến trúc:** `mode/enabled gate -> autonomous command -> SwerveSubsystem -> stop on unsafe/terminal state`.

**Kết quả sinh viên:** Kiểm tra được tình huống Disabled, Teleop và Autonomous chuyển mode mà không để output trái phép.

**Ý nghĩa kiểm chứng:** Simulation và safety/real evidence PASS theo status; đây là snapshot frozen predecessor của A01.

**Nối sang lesson sau:** A00 đóng ở đây; A01 bắt đầu bằng starting pose và field-frame contract.

## Student Checkpoint

Trước A01, bạn phải giải thích được vì sao lifecycle thuộc scheduler, vì sao bounded motion cần terminal condition, và vì sao mọi autonomous output phải đi qua Enabled gate và centralized stop. PathPlanner/AutoBuilder vẫn chưa được dùng.

### 7. Module A01 - Autonomous Navigation và Path Following

**Mục tiêu module:** phát triển autonomous từ motion thủ công sang navigation có field frame, trajectory, follower và PathPlanner runtime integration. A01 có đúng chín lesson và đóng tại A01_L09.

**Tiến trình concept:** starting pose -> pose target -> trajectory -> alliance transform -> holonomic follower -> PathPlanner -> AutoBuilder -> safe composition -> NamedCommands/event markers.

**Boundary:** A01_L06 là compatibility entry gate cho PathPlanner; A01_L07 mới được dùng AutoBuilder. A01 không sở hữu mechanism architecture của D01.

#### A01_L01 - Contract starting pose và field frame

**Kiến thức trước đó:** Frozen A00_L04 với pose accessor và safety gate.

**Một concept mới:** Starting pose và canonical field-frame contract cho autonomous.

**Vì sao có lesson này:** Autonomous cần biết robot bắt đầu ở đâu và pose dùng hệ tọa độ nào trước khi target hay path xuất hiện.

**Điều thay đổi:** Chuẩn hóa starting pose, reset/initialize pose boundary và quy ước field coordinates.

**Điều không thay đổi:** Chưa có trajectory, PathPlanner, vision và chưa alliance-flip tùy tiện.

**Luồng kiến trúc:** `autonomous start -> starting pose contract -> Swerve pose owner -> autonomous read-only pose`.

**Kết quả sinh viên:** Đặt robot vào field frame đúng cách và chỉ ra ai được reset pose.

**Ý nghĩa kiểm chứng:** Theory, Simulation, Glass và real evidence PASS theo status.

**Nối sang lesson sau:** Có starting pose, autonomous có thể nhắm tới một pose target.

#### A01_L02 - Autonomous chuyển động tới pose target

**Kiến thức trước đó:** Starting pose/field frame từ A01_L01.

**Một concept mới:** Pose-targeted motion dùng error vị trí/hướng để tạo bounded chassis request.

**Vì sao có lesson này:** Đây là cầu nối giữa “đi một đoạn” và “đi tới trạng thái hình học xác định”.

**Điều thay đổi:** Autonomous command đọc estimated pose và tính request hướng tới target.

**Điều không thay đổi:** Chưa có trajectory sampling, PathPlanner hay event marker.

**Luồng kiến trúc:** `target pose - estimated pose -> controller/evaluator -> ChassisSpeeds -> SwerveSubsystem`.

**Kết quả sinh viên:** Phân biệt pose target, pose estimate và motor output.

**Ý nghĩa kiểm chứng:** Theory, Simulation, Glass và real evidence PASS theo status; không khẳng định accuracy ngoài scope.

**Nối sang lesson sau:** Một target duy nhất chưa mô tả cả lịch trình; trajectory sẽ cung cấp các sample theo thời gian.

#### A01_L03 - Nền tảng sinh và sample trajectory

**Kiến thức trước đó:** Pose-targeted motion từ A01_L02.

**Một concept mới:** Trajectory generation và time-based sampling.

**Vì sao có lesson này:** Follower cần reference state theo thời gian, không chỉ target cuối cùng.

**Điều thay đổi:** Tạo trajectory và lấy sample có pose/velocity tương ứng tại thời điểm chạy.

**Điều không thay đổi:** Chưa đưa PathPlanner vào; chưa actuation thêm; đây vẫn là concept non-actuating/deterministic.

**Luồng kiến trúc:** `trajectory -> time sample -> reference pose/velocity -> future follower`.

**Kết quả sinh viên:** Đọc được vì sao cùng một path ở thời điểm khác nhau cho reference khác nhau.

**Ý nghĩa kiểm chứng:** Theory và deterministic Simulation PASS; Driver Station/real motion là NOT APPLICABLE hoặc deferred theo scope.

**Nối sang lesson sau:** Sample cần đúng field và alliance semantics trước khi follower dùng nó.

#### A01_L04 - Contract field và alliance transform

**Kiến thức trước đó:** Canonical field frame và trajectory sample từ A01_L03.

**Một concept mới:** Một owner duy nhất cho alliance/field transformation.

**Vì sao có lesson này:** Transform lặp lại ở nhiều package gây double flip, pose sai và khó debug.

**Điều thay đổi:** Đưa alliance transform vào boundary A01 được chỉ định và quy định input/output frame.

**Điều không thay đổi:** Vision measurement dùng canonical WPILib field coordinates và không tự alliance-flip.

**Luồng kiến trúc:** `canonical field data -> A01 alliance transform owner -> autonomous reference -> Swerve`.

**Kết quả sinh viên:** Chỉ ra nơi transform được phép xảy ra và phát hiện double-transform.

**Ý nghĩa kiểm chứng:** Theory và cả hai alliance trong nonactuating Simulation PASS; real motion deferred theo status.

**Nối sang lesson sau:** Reference đã đúng frame; follower holonomic mới được phép biến error thành request.

#### A01_L05 - Holonomic trajectory following

**Kiến thức trước đó:** Time samples và alliance contract từ A01_L03/L04.

**Một concept mới:** Holonomic follower kết hợp translational và rotational reference.

**Vì sao có lesson này:** Swerve có thể điều khiển x, y và heading độc lập; follower phải giữ ba chiều này nhất quán.

**Điều thay đổi:** Follower đọc sample + pose estimate và tạo `ChassisSpeeds` field/robot-relative đúng boundary.

**Điều không thay đổi:** Chưa có vendor PathPlanner runtime; vẫn giữ Swerve ownership và safety gate.

**Luồng kiến trúc:** `trajectory sample + pose -> holonomic follower -> ChassisSpeeds -> SwerveSubsystem`.

**Kết quả sinh viên:** Mô tả được follower khác trajectory generator thế nào.

**Ý nghĩa kiểm chứng:** Theory, Simulation, Glass và real evidence PASS theo status; exact tuning/endpoint accuracy không được suy ra.

**Nối sang lesson sau:** Follower đã có contract; lesson sau mới đưa PathPlanner path vào runtime qua compatibility gate.

#### A01_L06 - PathPlanner path và runtime integration

**Kiến thức trước đó:** Holonomic follower và alliance transform contract từ A01_L05.

**Một concept mới:** Đọc/chạy PathPlanner path qua compatibility entry gate.

**Vì sao có lesson này:** Một path asset và runtime library có version/contract riêng; phải kiểm tra tương thích trước khi dùng.

**Điều thay đổi:** Thêm PathPlanner path loading/runtime boundary và mapping tới follower đã học.

**Điều không thay đổi:** Chưa có AutoBuilder; không tự động thêm mechanism event; không bypass safety.

**Luồng kiến trúc:** `PathPlanner asset -> compatibility boundary -> trajectory/reference -> follower -> Swerve`.

**Kết quả sinh viên:** Kiểm tra được library/version/asset assumption thay vì copy config mù quáng.

**Ý nghĩa kiểm chứng:** Theory, Simulation, Glass và real path evidence PASS theo status; tuning endpoint vẫn là boundary riêng.

**Nối sang lesson sau:** Khi path runtime tương thích, AutoBuilder mới được phép đóng gói lifecycle và callbacks.

#### A01_L07 - Tích hợp AutoBuilder contract

**Kiến thức trước đó:** PathPlanner path/runtime integration từ A01_L06.

**Một concept mới:** AutoBuilder composition contract với pose supplier, reset, speeds, output và requirements.

**Vì sao có lesson này:** AutoBuilder nối nhiều callback; nếu chọn sai owner, RobotContainer sẽ biến thành nơi chứa autonomous business logic.

**Điều thay đổi:** Cấu hình AutoBuilder trong composition root, inject Swerve-owned capabilities và giữ scheduler-native requirements.

**Điều không thay đổi:** Swerve vẫn sole pose-estimator owner; không gọi thủ công lifecycle của child command.

**Luồng kiến trúc:** `RobotContainer composition -> AutoBuilder callbacks -> scheduler-native command -> SwerveSubsystem`.

**Kết quả sinh viên:** Phân biệt wiring dependency với việc RobotContainer tự chạy autonomous.

**Ý nghĩa kiểm chứng:** Theory, Simulation, Glass và real path evidence PASS theo status.

**Nối sang lesson sau:** AutoBuilder đã được cấu hình, cần chọn routine và compose an toàn qua mode lifecycle.

#### A01_L08 - Chọn autonomous routine và safe composition

**Kiến thức trước đó:** AutoBuilder contract và safety gate từ A00/A01_L07.

**Một concept mới:** Routine selection, terminal ownership và fail-closed composition qua scheduler-native lifecycle.

**Vì sao có lesson này:** Autonomous thật có nhiều routine và failure mode; robot phải dừng an toàn, không restart vô ý hoặc để Teleop giành ownership sai thời điểm.

**Điều thay đổi:** Cho chooser/factory tạo routine, giữ `SwerveSubsystem` requirement, xử lý fault/holding/stop theo contract đã được phê duyệt.

**Điều không thay đổi:** Không đổi Swerve IO/tuning/config; không để child command lifecycle bị gọi thủ công; RobotContainer vẫn là Composition Root.

**Luồng kiến trúc:** `chooser -> routine factory -> scheduler-native composition -> Swerve -> centralized stop/terminal state`.

**Kết quả sinh viên:** Giải thích `CONSUMED -> RUNNING -> HOLDING -> COMPLETE`, safe stop và không automatic restart.

**Ý nghĩa kiểm chứng:** Theory, Simulation, Driver Station/Glass, real robot và recovery evidence PASS theo final status; transient đã được phân loại bounded/deferred.

**Nối sang lesson sau:** Routine đã an toàn; lesson cuối thêm NamedCommands/event markers mà không phá boundary mechanism.

#### A01_L09 - PathPlanner NamedCommands và event markers

**Kiến thức trước đó:** Routine selection và safe composition từ A01_L08.

**Một concept mới:** NamedCommands/event markers để phát tín hiệu scheduler-native tại mốc path.

**Vì sao có lesson này:** Path cần có thể phát event mà không biến path follower thành mechanism owner.

**Điều thay đổi:** Đăng ký/tiêu thụ named event trong boundary đã chỉ định, giữ requirement và lifecycle của WPILib.

**Điều không thay đổi:** D01 mechanism project là độc lập, không có shared command boundary được phê duyệt; không mở A01_L10.

**Luồng kiến trúc:** `PathPlanner marker -> NamedCommand -> scheduler -> approved command/subsystem boundary`.

**Kết quả sinh viên:** Phân biệt event request với quyền truy cập trực tiếp intake/shooter.

**Ý nghĩa kiểm chứng:** Theory, tests/build, Simulation, Glass và real path/event evidence PASS theo final status; mechanism integration của D01 không được claim.

**Nối sang lesson sau:** A01 đóng; V00 bắt đầu từ vision coordinate frames sau khi A01_L09 đã frozen.

## Student Checkpoint

Trước V00, bạn phải giải thích starting pose, target pose, trajectory sample, alliance transform owner, follower, PathPlanner entry gate, AutoBuilder callbacks và NamedCommands. Bạn cũng phải biết scheduler-native composition, Swerve sole ownership và safe terminal state không phải là chi tiết tùy chọn.

### 8. Module V00 - Observation AprilTag và Pose Fusion

**Mục tiêu module:** xây dựng vision theo đúng Observation Architecture: coordinate frame -> field layout -> VisionIO -> immutable observation -> deterministic simulation -> pose estimation -> quality -> timestamp/latency -> real adapter -> Swerve-owned fusion. V00 có đúng chín lesson.

**Luồng chính:** `camera/hardware -> VisionIO -> VisionObservation -> estimator/evaluator -> QualifiedVisionMeasurement -> VisionFusionCoordinator -> SwerveDrivePoseEstimator`.

**Boundary quan trọng:** vendor API chỉ được phép bên trong real `VisionIO`; model/evaluator phải immutable và vendor-neutral; telemetry chỉ đọc; `SwerveSubsystem` là owner duy nhất của `SwerveDrivePoseEstimator`; fusion dùng `addVisionMeasurement(...)`, không reset pose liên tục. Measurement dùng canonical WPILib field coordinates và không bị alliance-flip bởi vision.

#### V00_L01 - Vision coordinate frames và camera extrinsics

**Kiến thức trước đó:** A01_L09 frozen với canonical field-frame contract và Swerve pose owner.

**Một concept mới:** Coordinate frame chain và camera extrinsics.

**Vì sao có lesson này:** Một AprilTag observation chỉ có nghĩa khi biết frame của field, tag, camera và robot. Sai frame sẽ làm pose sai dù phép toán không báo lỗi.

**Điều thay đổi:** Định nghĩa frame, transform direction và robot-to-camera/camera-to-target contract bằng model vendor-neutral.

**Điều không thay đổi:** Chưa chọn camera vendor; chưa gọi camera API; chưa fusion vào estimator.

**Luồng kiến trúc:** `canonical field/tag frame -> camera target -> camera extrinsics -> robot-frame candidate`.

**Kết quả sinh viên:** Đọc được một transform chain và phát hiện inverse/axis/sign sai.

**Ý nghĩa kiểm chứng:** Theory/contract evidence theo status; chưa có real camera nên vendor runtime không được claim.

**Nối sang lesson sau:** Frame chain cần field tag layout chính thức để biến tag-relative data thành field pose.

#### V00_L02 - Contract field layout AprilTag

**Kiến thức trước đó:** Frame và extrinsics từ V00_L01.

**Một concept mới:** Immutable canonical AprilTag field layout.

**Vì sao có lesson này:** Tag ID, vị trí và hướng phải là dữ liệu chuẩn dùng chung; mỗi adapter không được tự tạo một field map khác.

**Điều thay đổi:** Cung cấp immutable field layout snapshot, tag lookup và canonical Blue-origin coordinates.

**Điều không thay đổi:** Chưa chọn camera, chưa tạo observation từ hardware và không alliance-flip layout tại vision layer.

**Luồng kiến trúc:** `official field layout -> immutable tag lookup -> reference cho các lesson pose-estimation sau này`.

**Kết quả sinh viên:** Phân biệt field model với một camera measurement.

**Ý nghĩa kiểm chứng:** Layout/contract tests và build evidence là cơ sở; real camera/Glass chỉ PASS khi có status evidence.

**Nối sang lesson sau:** Có tag map rồi, VisionIO có thể xuất observation bất biến.

#### V00_L03 - VisionIO và immutable Observation contract

**Kiến thức trước đó:** Canonical field layout từ V00_L02 và IO rules.

**Một concept mới:** VisionIO interface, `Inputs` snapshot và `VisionObservation` immutable.

**Vì sao có lesson này:** Camera vendor có thể thay đổi, nhưng subsystem/evaluator không nên đổi theo vendor API.

**Điều thay đổi:** Định nghĩa VisionIO contract, `VisionIOInputs` và cách copy sample vào observation với state, targets, validity mà không giữ mutable reference; chưa thêm real/sim adapter.

**Điều không thay đổi:** Chưa chọn hoặc wire real Limelight/VisionIOSim runtime; Observation không truy cập hardware, NT4, scheduler, RobotContainer hay control behavior.

**Luồng kiến trúc:** `VisionIO contract -> VisionIOInputs -> immutable VisionObservation boundary`.

**Kết quả sinh viên:** Giải thích vendor-neutral read model và vì sao Input snapshot không được publish trực tiếp.

**Ý nghĩa kiểm chứng:** Contract/build/focused tests theo status; real adapter chưa được lựa chọn trong các lesson trước L08.

**Nối sang lesson sau:** Khi contract bất biến đã có, Simulation có thể tạo sample lặp lại mà không cần EstimatedPose làm camera truth.

#### V00_L04 - Vision Simulation deterministic

**Kiến thức trước đó:** VisionIO và Observation contract từ V00_L03.

**Một concept mới:** `VisionIOSim` deterministic với ground truth riêng của camera.

**Vì sao có lesson này:** Test fusion trước real hardware cần sample ổn định, nhưng nếu lấy `EstimatedPose` làm camera truth thì test sẽ tự xác nhận chính nó.

**Điều thay đổi:** Tạo deterministic simulated tags/targets, timing và validity từ nguồn ground truth độc lập.

**Điều không thay đổi:** Không thêm vendor camera; không reset estimator để làm ground truth; không bypass Swerve owner.

**Luồng kiến trúc:** `independent sim field/robot truth -> VisionIOSim -> VisionObservation -> evaluator`.

**Kết quả sinh viên:** Chỉ ra vì sao Simulation có thể sai nếu feedback loop dùng estimated pose làm input.

**Ý nghĩa kiểm chứng:** `compileTestJava`, focused tests, vision regressions, full suite và clean build cung cấp automated deterministic test evidence; runtime WPILib Simulation là `NOT APPLICABLE`, còn real optics/calibration được defer đến các lesson hardware phù hợp.

**Nối sang lesson sau:** Sample hợp lệ có thể được estimator biến thành robot pose candidate từ tag geometry.

#### V00_L05 - AprilTag robot pose estimation

**Kiến thức trước đó:** Frame chain, field layout và deterministic observation từ V00_L01-L04.

**Một concept mới:** Tính field-to-robot pose candidate bằng AprilTag geometry.

**Vì sao có lesson này:** Measurement của camera thường là camera-to-target; robot cần field-to-robot để so sánh với pose estimator.

**Điều thay đổi:** Pure estimator dùng field-to-tag, inverse camera-to-target và inverse robot-to-camera để tạo candidate.

**Điều không thay đổi:** Estimator không publish, không fusion, không tự chấm quality và không gọi vendor API.

**Luồng kiến trúc:** `VisionObservation + field layout + extrinsics -> pure AprilTagRobotPoseEstimator -> pose candidate`.

**Kết quả sinh viên:** Trình bày được vì sao thứ tự transform và inverse quan trọng.

**Ý nghĩa kiểm chứng:** Geometry/math và pure deterministic tests là `THEORY VERIFIED`; runtime Simulation, Driver Station / Glass và real robot đều `NOT APPLICABLE` trong lesson này; camera calibration và quality được defer đến các lesson sau. Pose candidate chưa phải accepted measurement.

**Nối sang lesson sau:** Candidate cần quality gate trước khi được phép đi gần estimator.

#### V00_L06 - Contract chất lượng vision measurement

**Kiến thức trước đó:** Pose candidate từ V00_L05.

**Một concept mới:** Explicit quality evaluation và `QualifiedVisionMeasurement`.

**Vì sao có lesson này:** Không phải target nào cũng đủ tốt để làm measurement; distance, ambiguity, tag count và validity phải thành contract minh bạch.

**Điều thay đổi:** Evaluator thuần loại sample không hợp lệ và tạo immutable qualified measurement cho sample đạt điều kiện.

**Điều không thay đổi:** Quality không sở hữu estimator, không ghi NetworkTables và không điều khiển robot.

**Luồng kiến trúc:** `VisionObservation -> pure quality evaluator -> QualifiedVisionMeasurement or reject`.

**Kết quả sinh viên:** Phân biệt detection, candidate và accepted measurement.

**Ý nghĩa kiểm chứng:** Deterministic quality tests và inherited vision regressions được ghi theo status; không claim mọi camera sample đều được nhận.

**Nối sang lesson sau:** Measurement đã có quality nhưng còn cần timestamp/latency trước khi fusion.

#### V00_L07 - Contract timestamp và latency vision

**Kiến thức trước đó:** Qualified measurement từ V00_L06.

**Một concept mới:** Timestamp semantics và latency compensation cho vision measurement.

**Vì sao có lesson này:** Một pose đúng nhưng gắn thời điểm sai sẽ làm estimator sửa lịch sử sai và tạo apparent jump.

**Điều thay đổi:** Chuẩn hóa acquisition/measurement timestamp, latency, freshness và admission semantics.

**Điều không thay đổi:** Không chọn vendor; không reset estimator; không đưa camera vào autonomous trực tiếp.

**Luồng kiến trúc:** `vision sample -> timestamp/latency contract -> QualifiedVisionMeasurement -> future fusion admission`.

**Kết quả sinh viên:** Tính được vì sao timestamp phải gắn với lúc measurement có hiệu lực, không chỉ lúc telemetry được publish.

**Ý nghĩa kiểm chứng:** Timestamp/latency repair và inherited regression evidence PASS theo snapshot hiện hành; drivetrain anomaly không được gán nhầm cho vision.

**Nối sang lesson sau:** Chỉ sau khi timing contract ổn định mới được chọn đúng một real vision adapter.

#### V00_L08 - Tích hợp real Vision adapter

**Kiến thức trước đó:** Vision contract, quality và timestamp từ V00_L03/L06/L07.

**Một concept mới:** Đưa đúng một camera implementation thực vào VisionIO adapter.

**Vì sao có lesson này:** Real camera có vendor library, version, timestamp semantics và connectivity behavior; tất cả phải nằm sau interface.

**Điều thay đổi:** Current snapshot tích hợp Limelight 4 acquisition/loss/reacquisition qua `VisionIOLimelight`; vendor details bị giữ trong adapter.

**Điều không thay đổi:** Vision model/evaluator vendor-neutral; Swerve vẫn owner pose estimator; H1 rotation chỉ là `PROVISIONAL COMMISSIONING LOCK`, không phải official vendor semantics.

**Luồng kiến trúc:** `Limelight 4 -> VisionIOLimelight -> VisionObservation -> quality/timing pipeline`; không có shortcut tới Swerve.

**Kết quả sinh viên:** Biết cách chứng minh một adapter real không làm rò vendor API vào subsystem/observation.

**Ý nghĩa kiểm chứng:** Focused 37/37, full 642/642, clean build, Simulation path/Glass và real camera evidence đã được status ghi nhận PASS cho phạm vi đó.

**Nối sang lesson sau:** Adapter đã cung cấp accepted timestamped measurement; lesson cuối mới được phép admission vào Swerve estimator.

#### V00_L09 - Fusion vision vào Swerve pose estimator

**Kiến thức trước đó:** Real Vision adapter từ V00_L08 và toàn bộ quality/timestamp contract.

**Một concept mới:** Qualified timestamped AprilTag admission qua `VisionFusionCoordinator` vào `SwerveDrivePoseEstimator.addVisionMeasurement(...)`.

**Vì sao có lesson này:** Vision chỉ có giá trị khi được fusion đúng boundary; continuous pose reset sẽ phá odometry/autonomous contract.

**Điều thay đổi:** Current active lesson thêm coordinator chạy sau scheduler, thực hiện guarded Swerve-owned admission cho accepted measurement.

**Điều không thay đổi:** V00_L09 vẫn `IN_PROGRESS / EDITABLE`; implementation, final verification và closure chưa hoàn tất. Không claim Simulation, Driver Station / Glass hay real fusion PASS.

**Luồng kiến trúc:** `VisionObservation -> estimator -> quality/timestamp -> VisionFusionCoordinator -> SwerveSubsystem -> addVisionMeasurement`.

**Kết quả sinh viên:** Giải thích vì sao autonomous chỉ dùng `getEstimatedPose()`, còn vision không được gọi estimator trực tiếp.

**Ý nghĩa kiểm chứng:** Design Lock PASS và lesson activation evidence có sẵn; implementation/verification/closure/publish vẫn `NOT TESTED` hoặc pending theo status hiện hành.

**Nối sang lesson sau:** Đây là lesson cuối của V00. Chỉ sau khi User chạy đủ gate và closure được phê duyệt, snapshot mới có thể frozen.

## Student Checkpoint

Trước khi nói “đã học vision fusion”, bạn phải phân biệt frame, field layout, IO, Observation, candidate, quality, timestamp, adapter và accepted measurement. Bạn phải chỉ ra rằng `VisionFusionCoordinator` không làm Swerve mất ownership, và current V00_L09 chưa phải lesson đã hoàn tất.

### 9. Nhánh học lịch sử / song song: D00 và D01

Evidence từ repository cho thấy D00 và D01 không phải predecessor của S00. `D00_L01` bắt đầu từ imported competition baseline; `D00_L02` đến `D00_L06` kế thừa trong D00. `D01_L01` ghi rõ predecessor là `D00_L06`, sau đó D01 tiếp tục nhánh Tank Drive và mechanisms. Ngược lại, `S00_L01` là WPILib Command Robot foundation mới và `S00_L02` kế thừa S00_L01. `A01_L09` cũng ghi rằng D01 không có shared command boundary được phê duyệt với A01.

Vì vậy `D00 -> D01` là **nhánh legacy/song song để luyện architecture**. Nhánh này hữu ích cho người mới vì dạy ownership tương tự bằng tank drive đơn giản hơn và nhiều mechanism. Nó không phải prerequisite có thẩm quyền của chuỗi `S00 -> A00 -> A01 -> V00`, trừ khi instructor chọn riêng nó làm preparation.

| Lesson nhánh | Một concept chính | Ranh giới evidence |
| --- | --- | --- |
| D00_L01 Competition Robot Foundation | Bắt đầu WPILib từ đâu và ai sở hữu object robot cơ bản. | Theory/build verified; Simulation, Driver Station / Glass và real robot là `NOT TESTED`. |
| D00_L02 Drivebase Safety Configuration | Spark MAX configuration, limit, inversion và startup stop an toàn. | Theory, Simulation, Driver Station / Glass và real hardware verified trong scope. |
| D00_L03 Tank Drive with Joystick | Hai driver request trái/phải qua default command. | Theory, Simulation và real tank-drive evidence verified. |
| D00_L04 Wireless Networking and Driver Station | Diagnose mode, USB, network và Driver Station communication. | Theory và HAL Simulation/Driver Station verified; closure/Git history cũ chưa đầy đủ. |
| D00_L05 Drive Input Processing | Deadband, sign correction, scaling và clamping trong controls. | Theory, Simulation/Driver Station và real input evidence verified theo scope. |
| D00_L06 Simulation IO Layer | Chọn `DriveIOSim` deterministic mà vẫn giữ upper contract. | Theory và deterministic Simulation verified; physical dynamics deferred. |
| D01_L01 Drive Observation Boundary | Copy mutable drive facts thành immutable `DriveObservation`. | Theory, deterministic tests và hardware regression evidence verified. |
| D01_L02 Drive Observation Evaluation | Pure evaluator dùng tolerance để đánh giá stopped/not-stopped. | Theory/evaluator evidence verified; README lifecycle cũ stale. |
| D01_L03 Drive Observation Publishing | Publish immutable drive facts qua typed telemetry. | Contract tests verified; runtime Driver Station / Glass và real robot `NOT TESTED`. |
| D01_L04 Robot Telemetry Runtime Integration | Gọi telemetry read-only sau scheduler/subsystem update. | Theory, Simulation, NT4/Glass và real runtime verified theo status. |
| D01_L05 Intake Foundation | Một slice hoàn chỉnh command -> subsystem -> IO -> Observation -> telemetry. | Theory, Simulation, Glass và real intake evidence verified. |
| D01_L06 Intake Complete Foundation | TalonFX configuration/readback và electrical observations an toàn. | Theory, Simulation, Glass và real configuration/electrical evidence recorded verified. |
| D01_L07 Flywheel Complete Foundation | Tái sử dụng architecture cho flywheel open-loop an toàn. | Theory, Simulation, Glass và real flywheel evidence recorded verified. |
| D01_L08 Feeder Complete Foundation | Giấu chi tiết REV feeder sau IO contract. | Theory/build, Simulation, Glass và real feeder evidence recorded; một số baseline field cũ NOT TESTED. |
| D01_L09 Shooter Complete Foundation | Điều phối shooter và feeder riêng qua command. | Theory/build, Simulation, Glass và real evidence recorded; Git vẫn user-owned/not tested. |
| D01_L10 Basic Integrated Robot | Ghép mechanism độc lập thành workflow operator theo thời gian. | Theory, Simulation, Glass, real robot và rendered docs verified. |
| D01_L11 Intake Feeder Coordination | Scheduler requirement làm ownership lock cho feeder dùng chung. | Theory/build verified; Simulation, Driver Station / Glass và real hardware `NOT TESTED`. |

### 10. Giải thích các concept kiến trúc

| Concept | Giải thích dễ hiểu và ví dụ FRC | Nó không phải là | Gặp lần đầu |
| --- | --- | --- | --- |
| Hardware | Motor, sensor, controller, dây và mechanism vật lý; ví dụ TalonFX của một module. | Java interface hay request mô phỏng. | D00_L02, S00_L02/L03 |
| IO | Contract và adapter nói chuyện với device; ví dụ `SwerveModuleIO` và `SwerveModuleIOCTRE`. | Command hoặc policy của robot. | D00_L06, S00_L03 |
| IO Inputs | Snapshot facts đọc từ hardware trong một chu kỳ. | Observation bất biến hoặc lệnh output. | S00_L03/L05, V00_L03 |
| Subsystem | Owner của mechanism behavior, state và safety. | Một wrapper mỏng để command gọi motor. | S00_L04 |
| Observation | Read model immutable, vendor-neutral của sự thật đã được subsystem/estimator tạo ra. | Mutable state, hardware API hay command. | D01_L01, S00_L05 |
| Telemetry | Kênh read-only đưa Observation ra NT4/Glass/log. | Nút điều khiển robot hoặc source của business logic. | D01_L03, S00_L06 |
| Command | Đơn vị điều phối có lifecycle và requirement do scheduler quản lý. | Nơi chứa vendor IO trực tiếp. | A00_L01 |
| Coordinator | Thành phần phối hợp nhiều contract đã có; ví dụ vision admission sau scheduler. | Owner mới của Swerve pose estimator. | V00_L09 |
| RobotContainer | Composition Root tạo object, inject dependency, default command, binding. | Nơi tính input/hardware/business logic. | S00_L01; rõ nhất A01_L07 |
| Robot.java | Owner của TimedRobot lifecycle và thứ tự runtime gọi. | Nơi chứa mechanism policy. | A00_L01; current V00_L09 |
| Composition Root | Một nơi lắp các implementation và dependency để runtime chạy. | Một lớp được phép làm mọi trách nhiệm. | S00_L01, A01_L07 |
| Vendor Adapter | Lớp duy nhất giữ Phoenix/REV/Limelight API sau IO. | Model domain vendor-specific lan ra toàn hệ thống. | S00_L03, V00_L08 |
| Simulation IO | IO implementation deterministic mô phỏng device contract. | Bằng chứng real hardware hoặc camera thật. | D00_L06, V00_L04 |
| Frozen Backbone | Luồng control và observation cố định của repository. | Gợi ý có thể đổi tùy lesson. | S00_L01 |
| Immutable data | Giá trị không thể bị sửa sau khi tạo; an toàn để truyền qua boundary. | Mutable `Inputs` dùng chung. | S00_L05, V00_L03 |
| Read-only telemetry | Publish data, không ra lệnh robot. | Feedback control path. | S00_L06 |
| Scheduler requirements | Lock ownership để không có hai command cùng điều khiển resource. | Một callback tự viết thay cho scheduler. | A00_L02, A01_L08/D01_L11 |
| Pose | Vị trí và hướng robot trong một frame. | Raw encoder reading. | S00_L23, A01_L01 |
| Odometry | Tích lũy pose từ drivetrain/IMU measurements. | Vision fusion hoặc ground truth. | S00_L23 |
| Pose estimator | Component hợp nhất/duy trì pose estimate do Swerve sở hữu. | Camera vendor API hay telemetry facade. | S00_L24, V00_L09 |
| Vision fusion | Admission measurement vision có quality/timestamp vào estimator. | Continuous pose reset. | V00_L09 |
| Autonomous | Command-driven robot behavior khi không có driver trực tiếp. | Một vòng code riêng bỏ qua scheduler. | A00_L01 |
| Vision measurement | Pose candidate đã được qualify, có timestamp và quality. | Một raw target detection. | V00_L05/L06/L07 |

### 11. Robot.java và RobotContainer.java

#### Robot.java: owner của runtime lifecycle

Trong trạng thái V00_L09 hiện tại, `Robot.java` tạo `RobotContainer`, lấy telemetry và `VisionFusionCoordinator`, rồi giữ thứ tự `robotPeriodic`. Thứ tự ý nghĩa là:

1. WPILib gọi `CommandScheduler.getInstance().run()`.
2. Nếu scheduler ném `RuntimeException`, Robot báo fault qua bridge đã inject, giữ fail-closed behavior và không tự restart autonomous.
3. Chỉ khi scheduler hoàn tất hợp lệ, coordinator chạy bước vision admission sau scheduler.
4. Telemetry chạy trong `finally` để operator-visible state vẫn được publish read-only.
5. `autonomousInit()` lấy command đã chọn và schedule một lần; `teleopInit()` cancel autonomous; `testInit()` cancel all; `simulationPeriodic()` chạy simulation harness.

Đây là lifecycle ordering, không phải nơi để đặt kinematics, camera math, motor config hay business logic. Nếu một lesson thay đổi thứ tự này, đó là architecture change chứ không phải cleanup nhỏ.

#### RobotContainer.java: Composition Root

`RobotContainer.java` chọn real/simulation implementation, tạo VisionIO, Swerve IO, subsystem, coordinator, PathPlanner/AutoBuilder adapter, autonomous factory/chooser, telemetry và default teleop command. Nó inject dependency và đăng ký binding.

Nó không được:

- đọc vendor hardware để tự quyết định cơ chế;
- xử lý raw controller input thay cho `controls`;
- tính quality/timing/pose/telemetry business logic;
- tự gọi lifecycle child command;
- cho autonomous hoặc vision bypass `SwerveSubsystem`.

| Câu hỏi | Câu trả lời theo Frozen Backbone |
| --- | --- |
| Ai tạo object? | `RobotContainer`, ở Composition Root. |
| Ai sở hữu lifecycle? | `Robot.java` và WPILib `CommandScheduler`. |
| Ai sở hữu mechanism state? | Subsystem tương ứng; Swerve là `SwerveSubsystem`. |
| Ai nói chuyện với hardware? | IO implementation/vendor adapter. |
| Ai tạo mechanism Observation? | Subsystem hoặc estimator; controls chỉ có exception DriverInputObservation. |
| Ai publish? | Read-only telemetry từ immutable Observation. |
| Ai sở hữu pose estimator? | `SwerveSubsystem`, duy nhất. |
| Vision vào estimator bằng gì? | Qualified, timestamped measurement qua coordinator và `addVisionMeasurement(...)`. |
| User kiểm chứng gì? | Build, Simulation, Glass/Driver Station, real robot và Git publication. |

### 12. IO và Subsystem

| Tình huống | IO chịu trách nhiệm | Subsystem chịu trách nhiệm |
| --- | --- | --- |
| Swerve | CTRE/REV calls, device config, Inputs, safe stop. | Chassis request, kinematics, optimization, module state, odometry, pose, safety. |
| Vision | Camera API, connection, raw target Inputs, real/sim adapter. | Observation lifecycle, pose candidate/quality coordination và boundary với estimator. |
| Tank Drive D00/D01 | Spark/Talon calls, safe config, simulated device. | Left/right behavior, drive state, observation và command requirement. |

Quy tắc chẩn đoán nhanh: nếu một class biết CAN ID, vendor object hoặc `setVoltage`, class đó nhiều khả năng thuộc IO; nếu class quyết định “robot phải làm gì”, class đó thuộc subsystem/command. Nếu class chỉ copy immutable facts hoặc evaluate pure data, nó thuộc observation. Nếu class publish NT4, nó thuộc telemetry và không được ra lệnh ngược.

### 13. Student Checkpoints

Rubric tự kiểm tra cho toàn khóa:

- Tôi có thể chỉ ra lesson trước và đúng một concept mới.
- Tôi biết code mới nằm trong package nào và package đó không vượt responsibility.
- Tôi có thể vẽ control flow và observation flow mà không nối telemetry ngược vào behavior.
- Tôi biết `IOInputs` là mutable snapshot còn Observation là immutable read model.
- Tôi biết SwerveSubsystem là owner pose estimator và vision chỉ cung cấp accepted measurement.
- Tôi phân biệt `THEORY VERIFIED`, `BUILD VERIFIED`, `SIMULATION VERIFIED`, `DRIVER STATION / GLASS VERIFIED`, `REAL HARDWARE VERIFIED`, `NOT TESTED`, `NOT APPLICABLE`, `DEFERRED` và `HOLD`.
- Tôi không gọi build, Simulation, Glass hay real robot là PASS nếu không có evidence tương ứng.
- Tôi không dùng D00/D01 như prerequisite chính nếu instructor chưa xác nhận topology đó.
- Tôi hiểu lesson `IN_PROGRESS` không phải snapshot frozen và không được trích dẫn như final closure.

### 14. Phân loại evidence

| Nhãn | Ý nghĩa dành cho sinh viên |
| --- | --- |
| `THEORY VERIFIED` | Contract/architecture đã được review hoặc test ở mức lý thuyết. |
| `BUILD VERIFIED` | Build command tương ứng đã có evidence PASS. |
| `SIMULATION VERIFIED` | User/runtime Simulation đã chạy PASS trong scope ghi rõ. |
| `DRIVER STATION / GLASS VERIFIED` | Dashboard/mode/runtime evidence đã được User ghi nhận. |
| `REAL HARDWARE VERIFIED` | Real robot/camera evidence đã có, chỉ trong scope ghi rõ. |
| `NOT TESTED` | Không có evidence; không được suy ra PASS từ source. |
| `NOT APPLICABLE` | Scope lesson không yêu cầu loại test đó. |
| `DEFERRED` | Có chủ ý để lại cho hardware, tuning, lesson sau hoặc điều kiện khác. |
| `HOLD` | Không được đóng/freeze vì một gate bắt buộc chưa đạt. |
| `KNOWN / BOUNDED` | Hành vi đã quan sát, được giới hạn và disposition rõ; không xóa nó khỏi lịch sử. |

Simulation chứng minh behavior của implementation mô phỏng và contract tương ứng; nó không tự chứng minh CAN wiring, sensor offset, latency thực tế, camera mounting hay drivetrain calibration. Real PASS cũng không có nghĩa là mọi parameter đã tuned cho thi đấu.

### 15. Ghi chú về tài liệu curriculum

#### Nợ tài liệu curriculum

Một số curriculum notes cũ vẫn mô tả V00 chỉ đến L07, gọi L08 là candidate/L09 là conceptual, hoặc dùng inventory cũ `61 lessons`. Topology hiện tại cần đọc theo repository source và status mới: V00 có 9 lesson, L08 đã complete/frozen/published, L09 là lesson active duy nhất. Những ghi chú cũ được giữ như historical debt, không dùng làm authority.

D00 hiện thiếu `LESSON_PLAN.md` và `LESSON_CHECKLIST.md` ở các lesson được kiểm tra; D01 cũng thiếu hai companion file này. Đây là curriculum documentation debt, không phải bằng chứng rằng lesson source không tồn tại.

Một số README D01 có nội dung copy/stale so với status hiện hành, và một số transition guide được kế thừa/copy với lifecycle wording cũ. Các finding này cần được đọc như provenance/documentation debt; không được tự sửa lesson D00/D01 trong khi audit bản đồ.

#### Artifact lịch sử vô hại

Một số README/status cũ trong D01 hoặc các lesson kế thừa có field Git/closure chưa cập nhật, hoặc mô tả lifecycle trước khi safety repair được đóng. Đây là provenance/documentation finding. Không được tự đổi source frozen để “làm sạch” chúng; hãy ưu tiên status và evidence hiện hành trong đúng lesson.

#### Phân loại architecture/source defect

Bản đồ này không phát hiện và không tự tuyên bố một source defect mới. Findings được phân loại là topology reconciliation, stale curriculum prose, evidence qualification và current V00_L09 incompletion. Mọi thay đổi Frozen Backbone, lesson order, IO contract, Constants authority hoặc frozen lesson đều cần formal review riêng.

### 16. Góc nhìn cuối cùng dành cho sinh viên

Nếu học theo chuỗi chính, hãy đi theo:

```text
S00  Swerve Foundation (24)
  -> A00  Autonomous Command Foundation (4)
    -> A01  Autonomous Navigation and Path Following (9)
      -> V00  AprilTag Vision Observation and Pose Fusion (9)
```

Bạn sẽ học ownership trước, rồi mới học motion; học contract trước, rồi mới học integration; học Simulation trước, rồi mới xem real hardware; và chỉ gọi một concept là hoàn tất khi evidence, docs và closure gate phù hợp.

Nhánh `D00 -> D01 (17)` là đường tank-drive/mechanism song song để luyện những ý tưởng tương tự. Nó không thay đổi thứ tự hay authority của chuỗi chính.

Trạng thái hiện tại của khóa học: `V00_L09` là lesson duy nhất `IN_PROGRESS / EDITABLE`. Concept của lesson là qualified timestamped AprilTag admission vào Swerve-owned pose estimator. Implementation, verification và final closure của lesson đó còn pending; vì vậy bản đồ này không tuyên bố vision fusion đã hoàn thành.
