# Hướng dẫn học V00_L01 - Hệ tọa độ Vision và ngoại tại camera

Trạng thái: `FINAL / PASS`  
Trạng thái lesson: `COMPLETE / FROZEN / READ-ONLY`  
Lesson tiền nhiệm có thẩm quyền: `A01_L09 @ 6b243bb`

Tài liệu tiếng Anh là tài liệu chuẩn khi có khác biệt về ý nghĩa.

## 1. Mục tiêu học tập

V00_L01 xây dựng nền tảng toán học cần có trước khi robot có thể sử dụng quan
sát từ camera một cách an toàn. Lesson dạy cách đặt tên hệ tọa độ, cách mô tả
camera được gắn cố định trên robot, và cách ghép/nghịch đảo phép biến đổi cứng
3D.

Lesson này chưa thu nhận measurement từ camera, chưa nhận diện AprilTag, chưa
ước lượng pose robot và chưa fusion vision vào localization. Các thao tác đó
đều phụ thuộc vào contract hệ tọa độ được thiết lập ở đây.

## 2. Vision nằm ở đâu trong kiến trúc robot

Kiến trúc điều khiển kế thừa vẫn là:

```text
Autonomous Command
    -> drivetrain subsystem
    -> IO
    -> hardware
```

Frozen Backbone tổng quát vẫn là:

```text
Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware
```

V00_L01 không thêm mũi tên mới vào các luồng điều khiển này. Lesson chỉ cung
cấp toán học thuần để các lesson vision sau sử dụng.

Nền tảng vision trong tương lai phát triển theo hướng observation:

```text
camera measurement
    -> quan hệ tọa độ camera/robot
    -> biểu diễn vision độc lập vendor
    -> lớp observation/localization
    -> pose-estimator fusion ở lesson sau
```

Trong L01 chỉ bắt đầu phần toán học của **quan hệ tọa độ camera/robot**. Việc
thu nhận camera measurement, contract VisionIO/Observation độc lập vendor,
localization, thời gian, chất lượng và fusion thuộc các lesson V00 sau. Luồng
mechanism observation hiện có không thay đổi:

```text
hardware -> IOInputs -> subsystem/estimator -> immutable Observation
         -> read-only telemetry -> NT4 / Glass / log
```

## 3. Pose3d và Transform3d trả lời hai câu hỏi khác nhau

`Pose3d` trả lời:

> Một hệ tọa độ đang ở đâu so với hệ tham chiếu?

Vì vậy `fieldToRobot` là vị trí và hướng của robot trong hệ field chuẩn.

`Transform3d` trả lời:

> Cần tịnh tiến và quay cứng như thế nào để đi từ hệ A sang hệ B?

Vì vậy `robotToCamera` là quan hệ lắp đặt cố định từ hệ robot đến hệ camera.

Các từ trước và sau `To` là một phần của contract. Không thể dùng
`robotToCamera` theo chiều ngược lại nếu chưa lấy inverse.

## 4. Ba hệ tọa độ của L01

### Hệ field chuẩn

Hệ field là hệ thế giới/tham chiếu ổn định của WPILib. Pose field-relative như
`fieldToRobot` hoặc `fieldToCamera` được biểu diễn trong hệ này. Quyền sở hữu
alliance transform của A01_L04 không thay đổi; V00_L01 không thêm hoặc áp dụng
alliance flip.

### Hệ thân robot

Hệ robot gắn cố định với mốc chassis được kiến trúc localization Swerve kế
thừa sử dụng:

- +X hướng về phía trước robot;
- +Y hướng về bên trái robot; và
- +Z hướng lên.

Translation lắp camera được biểu diễn theo các trục robot này.

### Hệ camera

Hệ camera gắn cố định với thân camera. V00 dùng hệ camera đã được chuẩn hóa theo
WPILib để làm toán hình học. Adapter vendor trong tương lai có thể phải đổi từ
quy ước optical-axis riêng của vendor trước khi tạo dữ liệu độc lập vendor,
nhưng L01 chưa có adapter vendor.

## 5. Quy ước NWU của WPILib

Hình học 3D của WPILib dùng quy ước north-west-up (NWU) theo bàn tay phải:

```text
             +Z lên
               |
               |
               o------ +X trước
              /
           +Y trái
```

- Đơn vị tịnh tiến là mét.
- Đơn vị góc là radian.
- Roll quay quanh +X.
- Pitch quay quanh +Y.
- Yaw quay quanh +Z.
- Chiều quay dương theo quy tắc bàn tay phải.

Helper không âm thầm đổi inch sang mét hoặc độ sang radian. Input phải dùng
đúng đơn vị và quy ước đã khóa.

## 6. Ngoại tại lắp đặt cố định

`robotToCamera` mô tả vị trí/hướng gắn camera so với robot:

```text
FIELD
  | fieldToRobot
  v
ROBOT
  | robotToCamera
  v
CAMERA
```

Translation cho biết gốc camera theo các trục robot. Rotation cho biết hướng
camera so với robot.

Quan hệ nghịch đảo là:

```text
cameraToRobot = inverse(robotToCamera)
```

Inverse là bắt buộc. Nó đổi cả rotation và translation như một rigid
transform; không phải chỉ đổi tên hoặc đổi dấu ba số.

## 7. Ghép theo chiều tiến và dựng lại theo chiều ngược

Để tìm pose field của camera từ pose field đã biết của robot:

```java
Pose3d fieldToCamera = fieldToRobot.transformBy(robotToCamera);
```

Theo toán học:

```text
fieldToCamera = fieldToRobot * robotToCamera
```

Để dựng lại pose field của robot từ pose field đã biết của camera:

```java
Transform3d cameraToRobot = robotToCamera.inverse();
Pose3d fieldToRobot = fieldToCamera.transformBy(cameraToRobot);
```

Theo toán học:

```text
fieldToRobot = fieldToCamera * cameraToRobot
```

Pose/hệ bắt đầu đứng trước; transform từ hệ bắt đầu đến hệ đích đứng sau.

## 8. Vì sao thứ tự ghép quan trọng

Rigid transform nói chung không có tính giao hoán. Translation của camera được
đo theo trục robot, và các trục này quay cùng robot.

Xét ví dụ số độc lập đã khóa:

```text
Vị trí field của robot: (1 m, 2 m, 0 m)
Yaw robot:               +90 độ
Vị trí gắn camera:       1 m về phía trước robot
```

Trước khi quay, robot-forward là trục +X của robot. Sau khi robot yaw
`+90 độ`, robot +X trùng hướng field +Y. Vì vậy offset camera một mét làm tăng
tọa độ Y của field:

```text
Vị trí field camera mong đợi: (1 m, 3 m, 0 m)
```

Camera **không** đi đến `(2 m, 2 m, 0 m)`. Kết quả sai đó cộng offset lắp đặt
trực tiếp theo field +X và bỏ qua việc robot đã quay.

Đây là lý do `fieldToRobot.transformBy(robotToCamera)` có ý nghĩa và việc đổi
thứ tự hoặc cộng trực tiếp tọa độ là sai.

## 9. Helper đã triển khai và quyền sở hữu package

Lesson triển khai đúng một production class:

`frc.robot.vision.VisionFrameTransform`

API public đã khóa:

```java
public static Pose3d fieldToCamera(
    Pose3d fieldToRobot,
    Transform3d robotToCamera)

public static Transform3d cameraToRobot(
    Transform3d robotToCamera)

public static Pose3d fieldToRobotFromCamera(
    Pose3d fieldToCamera,
    Transform3d robotToCamera)
```

Class là final, không thể khởi tạo, không có state, xác định và độc lập vendor.
Nó chỉ dùng hình học WPILib. Nó từ chối input null, translation không hữu hạn,
rotation không hữu hạn và kết quả tính không hữu hạn. Nó không thay đổi geometry
do caller sở hữu.

### Vì sao là `frc.robot.vision`, không phải `frc.robot.observation.vision`

Observation là mô tả bất biến về điều robot biết tại một sample time nhất quán,
thường do subsystem hoặc estimator tạo từ IOInputs. `VisionFrameTransform`
không lưu sample, timestamp, connection state, validity, target hay measurement.
Nó chỉ tính hình học từ các tham số tường minh.

Vì vậy:

```text
frc.robot.vision                    đúng: hình học miền vision thuần
frc.robot.observation.vision        sai: ngụ ý helper sở hữu Observation
```

Toán học thuần có thể hỗ trợ Observation trong tương lai mà bản thân nó không
trở thành Observation.

## 10. Test đóng vai trò mathematical oracle như thế nào

Các test tập trung bao phủ:

- identity, chỉ translation, chỉ rotation và transform 3D kết hợp;
- kết quả số độc lập cho translation sau rotation và phép inverse;
- round trip theo chiều tiến/ngược;
- thứ tự ghép không giao hoán;
- dấu trục NWU, mét và radian;
- từ chối null và giá trị không hữu hạn; và
- tính xác định và không sửa input của caller.

Một số test so sánh với phép toán hình học WPILib, trong khi các test độc lập
dùng tọa độ và góc mong đợi được tính tường minh. Cách kết hợp này kiểm tra tính
phù hợp API mà không hoàn toàn lặp lại biểu thức production.

## 11. Những gì vẫn kế thừa từ A01_L09

Vision helper không sửa kiến trúc autonomous cuối cùng. Project canonical vẫn
giữ nguyên:

- `AutonomousPreparationCoordinator`, `PrepareAutonomousCommand`, preparation
  observation bất biến và preparation telemetry chỉ đọc;
- AutoBuilder composition do scheduler sở hữu;
- Robot-level scheduler `RuntimeException` boundary và fatal-fault bridge;
- terminal `HOLDING`, `SwerveSubsystem.stop()` tập trung, SAFE_STOP, cổng
  Teleop-enabled phòng vệ và không tự động restart;
- `frc.robot.autonomous.AutonomousEventId`;
- NamedCommands event markers và tạo command mới bằng `Commands.defer(...)`;
  và
- không có manual child-command lifecycle delegation.

Đây là hành vi A01 kế thừa, không phải hành vi mới của V00_L01.

## 12. Phạm vi để dành

Các nội dung sau cố ý chưa có:

- VisionIO và VisionIOInputs;
- PhotonVision, Limelight hoặc vendor camera bất kỳ;
- camera hardware và giá trị calibration X/Y/Z/roll/pitch/yaw thật;
- tra cứu AprilTag field layout và chọn target;
- quyết định chất lượng target/measurement;
- timestamp và bù latency;
- ước lượng pose robot từ target;
- tích hợp Swerve pose estimator và vision fusion;
- thay đổi autonomous, PathPlanner hoặc hành vi drivetrain.

Không được đoán giá trị lắp đặt thật. Các lesson sau phải thêm đúng một concept
theo roadmap V00 và qua architecture review bình thường.

## 13. Xác minh và trạng thái lesson

Bằng chứng có thẩm quyền do User cung cấp cho project canonical Java 17:

- Clean: PASS.
- `VisionFrameTransformTest` tập trung: PASS.
- Full build: PASS.
- Artifact `-Recurse` ngoài ý muốn: không tồn tại.

Simulation, Driver Station / Glass và robot thật không áp dụng cho concept mới
này vì không có camera runtime, IO, telemetry, scheduler, drivetrain, lookup,
fusion hoặc actuation.

V00_L01 là `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 7d52ebf` sau khi Final
Architecture Review và Final Closure Review PASS. Git add, commit và push vẫn
do User sở hữu; User đã xác nhận publication tại commit này.
