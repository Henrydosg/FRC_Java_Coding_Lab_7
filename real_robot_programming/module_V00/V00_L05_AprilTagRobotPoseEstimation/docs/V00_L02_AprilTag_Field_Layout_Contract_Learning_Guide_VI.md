# V00_L02 — Hướng dẫn học: Hợp đồng bố trí AprilTag trên sân

## Trạng thái tài liệu

- **Lesson:** `V00_L02_AprilTagFieldLayoutContract`
- **Trạng thái:** `COMPLETE / FROZEN / READ-ONLY`
- **Triển khai:** `COMPLETE / USER VERIFIED`
- **Tài liệu:** `COMPLETE`
- **Final Architecture Review / Closure:** `PASS`
- **Thẩm quyền ngôn ngữ:** Bản tiếng Anh là tài liệu chuẩn; bản tiếng Việt này
  dùng để giải thích.

Hướng dẫn này mô tả hợp đồng reference geometry đã được triển khai. V00_L02
đã `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 53e9b9f` sau khi hoàn tất
final review và closure; User đã xác nhận publication.

## 1. Vì sao V00_L02 đi sau V00_L01?

V00_L01 xây dựng toán học frame:

- `robotToCamera`: camera được gắn ở đâu trên robot;
- `fieldToCamera`: camera nằm ở đâu trong frame sân chuẩn; và
- phép composition/inversion rigid-body bằng WPILib geometry.

Nhưng V00_L01 chưa cho biết các AprilTag mang ID cụ thể được gắn ở đâu trên sân.
Một phép đo camera trong tương lai cần hai nhóm thông tin khác nhau:

1. thông tin camera-to-target đo được từ hệ thống camera; và
2. reference geometry field-to-tag chính thức từ bố trí sân.

V00_L02 chỉ thêm nhóm thứ hai.

## 2. Identity của AprilTag

Mỗi AprilTag chính thức trên sân có một ID nguyên dương. ID là khóa ổn định để
liên kết một detection tương lai với pose chính thức trên sân.

Hợp đồng lookup phân biệt:

- ID dương đã biết -> `Optional<Pose3d>`;
- ID dương không tồn tại -> `Optional.empty()`; và
- ID bằng 0 hoặc âm -> `IllegalArgumentException`.

ID dương không tồn tại là normal absence. ID không dương là request không hợp
lệ.

## 3. Ý nghĩa fieldToTag

`Pose3d` trả về là `fieldToTag`: pose của frame tag so với frame sân chuẩn.

```text
field frame -> tag frame
```

Translation cho biết tâm tag nằm ở đâu trong frame sân. Rotation cho biết frame
tag xoay như thế nào so với frame sân.

Đây không phải `tagToField`. Lấy inverse sẽ trả lời câu hỏi khác và làm sai
composition trong các lesson sau.

## 4. Frame sân WPILib gốc Blue

Official WPILib field layout dùng origin tại Blue Alliance wall/right side:

- +X từ Blue hướng sang Red;
- +Y sang trái khi nhìn từ Blue;
- +Z hướng lên;
- translation dùng mét;
- rotation dùng radian; và
- trục/rotation theo right-handed NWU.

Tag vật lý không di chuyển khi robot đổi alliance. Vì vậy V00_L02 không
alliance-flip, mirror, đổi origin sang Red, hoặc dùng A01_L04 autonomous
transform. A01_L04 vẫn là owner duy nhất của alliance transform cho autonomous.

## 5. Hai biến thể sân Rebuilt 2026

| Variant trong repository | Định nghĩa WPILib 2026.2.1 | Kích thước sân |
| --- | --- | --- |
| `REBUILT_WELDED` | `AprilTagFields.k2026RebuiltWelded` | 16.541 m x 8.069 m |
| `REBUILT_ANDYMARK` | `AprilTagFields.k2026RebuiltAndymark` | 16.518 m x 8.043 m |

Hai biến thể có kích thước và vị trí tag hơi khác nhau. Vì vậy phải chọn rõ sân
vật lý. Implementation không dùng `AprilTagFields.kDefaultField` làm authority
vì default alias có thể che giấu loại sân thật.

## 6. API production đã triển khai

Package và class:

`frc.robot.vision.AprilTagFieldLayoutContract`

Public methods:

```java
public static AprilTagFieldLayoutContract loadOfficial2026(
    Constants.FieldTransformConstants.FieldVariant fieldVariant)

public Optional<Pose3d> getTagPose(int tagId)
```

Class là `final`, constructor là private và không có public API rộng hơn.

## 7. Quy trình load và validation

```text
FieldVariant rõ ràng
    -> AprilTagFields tương ứng
    -> AprilTagFieldLayout.loadField(...)
    -> validate kích thước sân
    -> validate ID và pose có thể truy cập
    -> deep-snapshot ID -> Pose3d
    -> private immutable map
    -> bỏ raw mutable layout/tag
```

Validation fail-closed:

- kích thước sân phải finite, dương và đúng variant;
- ID tag phải dương;
- pose không được null;
- translation và quaternion phải finite;
- quaternion norm phải finite và khác 0;
- duplicate ID bị từ chối nếu có thể xuất hiện; và
- layout rỗng bị từ chối.

Lỗi load resource chính thức được propagate; implementation không tạo default
giả.

## 8. Vì sao không giữ raw WPILib layout?

`AprilTagFieldLayout` có origin mutable. `setOrigin(...)` làm thay đổi frame
của kết quả `getTagPose(...)`. Raw `AprilTag` cũng có public ID và pose
mutable.

Nếu giữ hoặc expose các object đó, caller có thể làm thay đổi reference contract
sau khi tạo. V00_L02 copy từng pose đã validate vào owned geometry và chỉ giữ
immutable map.

Class không expose:

- raw layout;
- raw tag list;
- mutable map;
- origin setter; hoặc
- mutable `AprilTag`.

## 9. Vì sao không có fromLayout test seam?

Synthetic seam `fromLayout(AprilTagFieldLayout)` đã được xem xét trước
implementation và bị từ chối rõ ràng.

Focused tests chỉ dùng:

- `loadOfficial2026(FieldVariant)`;
- `getTagPose(int)`; và
- numeric values độc lập từ official resources đã cài đặt.

Không mở rộng production API chỉ để inject malformed synthetic layout. Các
validation branch không thể chạm tới qua official loading được static-audit
thay vì ép test bằng seam bị cấm.

## 10. Ví dụ tag 1 từ WPILib 2026.2.1

Các số dưới đây lấy trực tiếp từ official JSON resources của WPILib 2026.2.1.
Đây là field-reference examples, không phải robot measurement, camera
calibration hoặc estimated pose.

| Variant | Tag ID | X (m) | Y (m) | Z (m) | Yaw |
| --- | ---: | ---: | ---: | ---: | ---: |
| Rebuilt Welded | 1 | 11.8779798 | 7.4247756 | 0.889 | pi rad |
| Rebuilt AndyMark | 1 | 11.8639590 | 7.4114914 | 0.889 | pi rad |

X/Y khác nhau cho thấy vì sao phải chọn field construction rõ ràng. Các giá trị
này cũng là independent fixed test oracles; test không gọi production helper để
tính expected value.

## 11. Focused verification

`AprilTagFieldLayoutContractTest` kiểm tra:

- load welded và AndyMark rõ ràng;
- reject null variant;
- ID known, unknown-positive, zero và negative;
- numeric oracle độc lập cho hai resources;
- đúng chiều `fieldToTag`;
- không inversion hoặc Red mirroring;
- mét và right-handed NWU radians;
- deterministic repeated lookup;
- caller không mutate stored reference;
- hai variant vẫn phân biệt;
- public API chính xác; và
- không có `fromLayout(...)`.

Authoritative User verification bằng VS Code với WPILib Java 17:

- focused AprilTag test: PASS;
- inherited `VisionFrameTransformTest`: PASS;
- full test suite: PASS; và
- clean full build: PASS (`BUILD SUCCESSFUL in 24s`; 7 actionable tasks,
  7 executed).

Đây là kết quả do User chạy, không phải Codex-executed PASS.

## 12. Quan hệ với camera measurement tương lai

Sau này có thể kết hợp:

```text
fieldToTag                 reference chính thức từ V00_L02
cameraToTag                measurement tương lai từ camera
robotToCamera              fixed extrinsic từ V00_L01
    -> candidate fieldToRobot estimate
```

V00_L02 chưa thực hiện phép kết hợp này. Nó chỉ cung cấp fixed
`fieldToTag` reference.

## 13. Vì sao đây không phải Observation?

Observation là sampled fact bất biến do subsystem/estimator tạo từ IOInputs tại
một thời điểm xác định. Field layout là static domain reference geometry từ
official resource.

Vì vậy contract thuộc `frc.robot.vision`, không thuộc
`frc.robot.observation`. Nó không có timestamp, age/validity, hardware sample,
telemetry topic, scheduler behavior hoặc mechanism state.

## 14. Các trách nhiệm để dành cho V00 sau

V00_L03 và các lesson sau sẽ review riêng:

- VisionIO và immutable Vision Observation;
- deterministic camera simulation;
- AprilTag robot-pose estimation;
- measurement quality;
- timestamp và latency;
- real camera adapter/vendor integration; và
- accepted measurement fusion vào Swerve pose estimator.

V00_L02 không thêm camera vendor, NetworkTables, telemetry, RobotContainer,
autonomous, PathPlanner, Swerve hoặc hardware behavior.

## 15. Trạng thái verification và closure

Vì lesson chỉ thêm immutable deterministic reference geometry:

- Simulation: `NOT APPLICABLE`;
- Driver Station / Glass: `NOT APPLICABLE`;
- Real Robot: `NOT APPLICABLE`; và
- Physical Camera: `NOT APPLICABLE`.

Implementation và documentation đã complete/verified. Lesson là
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 53e9b9f`; User đã xác nhận
publication và vẫn là người sở hữu các thao tác Git.
