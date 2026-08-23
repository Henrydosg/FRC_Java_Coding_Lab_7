# Hướng dẫn học V00_L01: Hệ tọa độ Vision và ngoại tại camera

Trạng thái: `FINAL / PASS` - bản giải thích tiếng Việt  
Bài học: `V00_L01_VisionCoordinateFramesAndCameraExtrinsics`  
Tài liệu tiếng Anh là chuẩn khi có khác biệt.

## 1. Mục tiêu học tập

Bài này thiết lập ngôn ngữ hình học cho toàn bộ module V00. Ta cần biết mỗi
vị trí/hướng quay đang được biểu diễn trong hệ tọa độ nào và phép biến đổi
gắn camera đi theo chiều nào.

L01 chưa nhận diện AprilTag, chưa chọn hãng camera, chưa ước lượng pose robot
và chưa fusion vào pose estimator.

## 2. Phân biệt Pose3d và Transform3d

`Pose3d` trả lời: “hệ tọa độ này đang ở đâu và quay thế nào so với hệ tham
chiếu?” Ví dụ, `fieldToRobot` là pose robot trong hệ field chuẩn.

`Transform3d` trả lời: “đi từ A đến B bằng độ dời và độ quay nào?” Ví dụ,
`robotToCamera` mô tả vị trí và hướng lắp camera so với robot.

```text
Pose3d:       Hệ này ở đâu?
Transform3d:  Đi từ A đến B như thế nào?
```

Tên biến khóa chiều biến đổi. Muốn đổi `robotToCamera` thành
`cameraToRobot`, phải lấy inverse.

## 3. Quy ước NWU của WPILib

Toàn bộ L01 dùng quy ước north-west-up:

```text
             +Z lên
               |
               |
               o------ +X trước
              /
           +Y trái
```

- Tịnh tiến dùng mét.
- Góc tính toán/lưu trữ dùng radian.
- Roll quanh +X, pitch quanh +Y, yaw quanh +Z.
- Chiều dương theo quy tắc bàn tay phải.

`VisionFrameTransform` không tự đổi inch sang mét hay độ sang radian.

## 4. Bốn hệ tọa độ

### Field

Field là hệ tọa độ WPILib chuẩn, luôn theo phía blue. Dữ liệu vision của cả hai
alliance đều giữ trong hệ này và không alliance-flip. V00_L02 sẽ sở hữu layout
AprilTag chính thức.

### Robot

Hệ robot gắn với chassis tại mốc localization/center of rotation hiện có của
Swerve: +X về trước robot, +Y về trái robot, +Z hướng lên.

### Camera

Hệ camera gắn với thân camera và được chuẩn hóa sang NWU: +X trước camera, +Y
trái camera, +Z lên. Adapter thật sau này phải đổi hệ trục riêng của vendor
trước khi dữ liệu rời khỏi IO.

### AprilTag

Gốc hệ tag ở tâm tag. +X vuông góc và hướng ra khỏi mặt tag, +Y là phía trái
của tag khi nhìn theo +X, +Z hướng lên. L01 chỉ định nghĩa ý nghĩa hình học;
V00_L02 định nghĩa ID và pose field chính thức.

## 5. Ý nghĩa chính xác của robotToCamera

`robotToCamera` là ngoại tại lắp đặt cố định của camera:

```text
FIELD
  | fieldToRobot
  v
ROBOT
  | robotToCamera
  v
CAMERA
```

Phần translation là vị trí gốc camera so với gốc robot, được biểu diễn theo
trục robot. Phần rotation là hướng camera so với robot.

Thứ tự tiến đúng:

```java
Pose3d fieldToCamera = fieldToRobot.transformBy(robotToCamera);
```

Chiều ngược:

```java
Transform3d cameraToRobot = robotToCamera.inverse();
Pose3d fieldToRobot = fieldToCamera.transformBy(cameraToRobot);
```

Không được dùng `robotToCamera` trực tiếp cho chiều ngược.

## 6. Vì sao thứ tự ghép quan trọng

Translation của transform được áp dụng theo trục của pose bắt đầu. Nếu robot
yaw +90 độ, camera lắp cách một mét về phía trước robot sẽ nằm theo +Y của
field, không phải +X của field.

Áp dụng transform hai lần, bỏ inverse, hoặc cộng trực tiếp theo trục field đều
cho kết quả sai.

## 7. API đã triển khai

`frc.robot.observation.vision.VisionFrameTransform` chỉ có ba phép công khai:

```java
fieldToCamera(Pose3d fieldToRobot, Transform3d robotToCamera)
cameraToRobot(Transform3d robotToCamera)
fieldToRobotFromCamera(Pose3d fieldToCamera, Transform3d robotToCamera)
```

Class không có trạng thái và không thể khởi tạo. Nó từ chối:

- pose/transform null;
- X, Y, Z không hữu hạn; và
- thành phần quaternion của rotation không hữu hạn.

Các phép toán xác định, không sửa input của caller và không phụ thuộc vendor,
hardware, subsystem, NetworkTables, Driver Station, clock hay telemetry.

## 8. Giá trị lắp camera thật vẫn chưa có

Calibration production cần sáu giá trị đo: X, Y, Z, roll, pitch và yaw của
camera so với robot. Hiện tại tất cả vẫn là
`TBD / USER MEASUREMENT REQUIRED`.

Không dùng identity, số 0, ví dụ, mặc định vendor hay phỏng đoán làm giá trị
production. Test chỉ dùng hình học giả lập được ghi rõ. Sau khi đo và review,
authority nhỏ nhất trong tương lai có thể là một
`Constants.VisionConstants.kRobotToCamera` bất biến.

## 9. Phạm vi để dành

`cameraToTarget` và phép ghép field-to-tag chưa được thêm vì L01 chưa có target
observation contract. Các bài sau lần lượt bổ sung field layout, VisionIO và
Observation, simulation, pose estimation, chất lượng measurement, timestamp/
latency, một adapter thật đã review, rồi fusion do Swerve sở hữu.

Chiều observation trong bài tương lai sẽ là:

```text
CAMERA
  | cameraToTarget
  v
TAG
```

Việc thu nhận `cameraToTarget` thuộc các bài sau; sơ đồ này chỉ khóa chiều hình
học, không tuyên bố detection hay hành vi camera trong L01.

Các ranh giới giữ nguyên:

- `RobotContainer` chỉ là composition root.
- Chỉ `SwerveSubsystem` sở hữu `SwerveDrivePoseEstimator` và fusion tương lai.
- Autonomous chỉ đọc `getEstimatedPose()`.
- Chỉ A01_L04 sở hữu alliance transform.
- Telemetry chỉ đọc.
- Không chọn vendor trước V00_L08.

## 10. Kết quả kiểm tra

- Test tập trung L01: 18/18 PASS.
- Regression kế thừa frozen: 446/446 PASS.
- Toàn bộ test suite: 464/464 PASS.
- Clean build: PASS - `BUILD SUCCESSFUL in 29s`.
- WPILib VS Code Build Robot Code: PASS / USER-VERIFIED.
- Simulation: không áp dụng vì L01 không có hành vi vision runtime.
- Robot thật: không áp dụng vì chưa có camera/adapter thật.

Không tuyên bố giá trị mount thật, độ chính xác detection/pose, tuning,
latency, ngưỡng chất lượng hoặc hành vi fusion.

V00_L01 là `COMPLETE / FROZEN / READ-ONLY` và là nguồn kế thừa frozen cho
V00_L02. V00_L02 vẫn `NOT CREATED / NOT STARTED`.
