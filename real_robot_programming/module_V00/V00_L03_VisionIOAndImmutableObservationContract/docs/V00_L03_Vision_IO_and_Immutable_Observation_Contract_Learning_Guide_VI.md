# V00_L03 - Hướng dẫn Vision IO và Immutable Observation Contract

Bản tiếng Việt này dùng để giải thích. Bản tiếng Anh là tài liệu có tính quy
phạm. Đây là phiên bản cuối của lesson: nó ghi lại contract đã implement,
evidence verification và trạng thái lesson đã frozen. Git publication vẫn là
gate riêng do User sở hữu.

## 1. Vì sao cần VisionIO

Không nên để thư viện của hãng camera lan sang subsystem, command, telemetry và
pose estimation. `VisionIO` tạo một ranh giới nhỏ cho hardware/source. Adapter
real hoặc simulation trong tương lai có thể thay đổi mà không làm thay đổi
domain contract.

```text
camera / simulation adapter tương lai
    -> VisionIO
    -> VisionIOInputs
    -> domain owner tương lai
    -> immutable VisionObservation
    -> read-only consumers tương lai
```

## 2. Từ vendor adapter đến Inputs

Boundary đã implement được thiết kế để adapter tương lai đọc source một lần
trong mỗi cycle và cập nhật đầy đủ `VisionIOInputs`. Mọi vendor result object
phải dừng bên trong adapter tương lai đó. Capability công khai duy nhất là:

```java
void updateInputs(VisionIOInputs inputs)
```

Không có output hoặc `stop()` vì contract này chỉ quan sát camera source, không
điều khiển mechanism.

## 3. Mutable transport và immutable meaning

`VisionIOInputs` là dữ liệu transport mutable đã được implement cho một cycle.
Adapter tương lai phải ghi đè đầy đủ mỗi cycle.

`VisionObservation` là domain meaning immutable đã được implement. Test,
diagnostics, log và read-only consumer tương lai có thể sử dụng nó mà không
thấy mutable Inputs.

Hai loại dữ liệu này không được coi là giống nhau.

## 4. Vì sao vendor object không được vượt qua VisionIO

Limelight, PhotonVision hoặc vendor khác có model riêng. Nếu vendor type đi qua
VisionIO, mọi consumer sẽ phụ thuộc vendor và simulation khó thay thế. Vì vậy
L03 chỉ dùng JDK collection và WPILib geometry value đã được duyệt.

Không chọn camera vendor trước V00_L08.

## 5. Vì sao Observation không có NetworkTables

NetworkTables là công nghệ acquisition/publication, không phải domain meaning
immutable. `VisionObservation` không chứa topic, key, publisher, update loop
hoặc NetworkTables entry. Telemetry là read-only consumer tương lai và không
được thêm chỉ để hiển thị một contract-only lesson.

## 6. Các field của one-cycle Inputs

Các field đã implement và được khóa:

- `available`: implementation đã chọn có khả năng cung cấp vision data;
- `connected`: source được hỗ trợ hiện đang kết nối;
- `sampleValid`: sample của cycle này nhất quán về cấu trúc;
- `targets`: toàn bộ target của cycle này.

Mỗi lần update phải thay thế cả bốn fact. Khi target biến mất, collection mới
phải rỗng; dữ liệu target cũ không được còn lại.

## 7. available, connected và sampleValid khác nhau

| Ý nghĩa | available | connected | sampleValid |
| --- | --- | --- | --- |
| Implementation không khả dụng | false | false | false |
| Source được hỗ trợ nhưng mất kết nối | true | false | false |
| Đã kết nối nhưng sample sai cấu trúc | true | true | false |
| Sample nhất quán | true | true | true |

`sampleValid` không có nghĩa là target tốt, ambiguity thấp, pose được chấp nhận
hoặc estimator đã chấp nhận measurement. Các quyết định đó thuộc lesson sau.

## 8. Các Observation state

State đã khóa:

- `UNAVAILABLE`
- `DISCONNECTED`
- `INVALID_SAMPLE`
- `NO_TARGETS`
- `TARGETS_PRESENT`

Chỉ `TARGETS_PRESENT` được chứa target. Không có target được biểu diễn bằng
collection rỗng, không dùng ID 0, ID âm, NaN hoặc zero transform.

## 9. Vì sao giữ nhiều target

Camera có thể thấy nhiều AprilTag trong một cycle. Chọn một target sẽ vô tình
tạo selection policy. L03 giữ toàn bộ target theo acquisition order để L04 có
thể simulation và lesson sau có thể diễn giải.

L03 không có "best target" vì quality và ambiguity thuộc V00_L06.

## 10. Target identity

Mỗi target có `int tagId` dương. Identity giúp estimator tương lai kết hợp
camera measurement với field-layout data. L03 không khóa maximum ID tùy ý.
Collection rỗng, không phải sentinel ID, biểu diễn absence.

## 11. Hướng cameraToTarget

Mỗi target có:

```text
Transform3d cameraToTarget
```

Nó có nghĩa target relative to camera. Nó không phải `targetToCamera`,
`robotToTarget`, `fieldToTag` hoặc `fieldToRobot`. Frame direction là một phần
của contract vì đảo transform có thể cho kết quả toán học hợp lệ nhưng sai vật
lý.

## 12. Quy ước tọa độ và đơn vị

L03 dùng WPILib right-handed NWU:

- +X forward;
- +Y left;
- +Z up;
- translation dùng mét;
- rotation dùng radian.

Immutable target phải reject null và các giá trị không hợp lệ còn quan sát được
qua boundary `Transform3d`, gồm các component không hữu hạn còn quan sát được.
Identity rotation hợp lệ phải được chấp nhận.

WPILib `Rotation3d` canonicalize quaternion đầu vào. Nếu raw input có norm bằng
zero hoặc gần zero, quá trình construction có thể thay nó bằng identity
`(1, 0, 0, 0)` trước khi `VisionObservation` nhận transform. Khi đó Observation
không còn quan sát hoặc khôi phục được raw norm ban đầu. Việc kiểm tra raw hoặc
vendor data trước normalization, nếu cần trong tương lai, thuộc adapter tương
ứng trước khi tạo `Rotation3d`; L03 không thêm adapter hoặc raw quaternion
transport đó.

## 13. Immutability và defensive ownership

`VisionObservation` đã implement chứa một state và immutable list của immutable
target values. Construction:

- reject null state, collection, entry và transform;
- bảo đảm state/list consistency;
- yêu cầu ID dương;
- validate các giá trị transform còn quan sát được sau khi `Rotation3d`
  canonicalize;
- defensive copy collection và geometry; và
- giữ deterministic value equality.

Thay đổi Inputs hoặc list cũ không được làm thay đổi Observation.

## 14. Vì sao L03 không dùng fieldToTag

V00_L02 cung cấp canonical Blue-origin `fieldToTag`. L03 chỉ định nghĩa
acquisition fact trong camera frame. Kết hợp `fieldToTag`, camera extrinsics và
`cameraToTarget` để tạo robot-pose candidate thuộc V00_L05.

Tách field layout khỏi L03 giúp acquisition không biến thành estimation.

## 15. Vì sao runtime producer được để dành

Document C yêu cầu subsystem hoặc dedicated estimator tạo mechanism
Observation từ IOInputs. L03 chưa có runtime camera hoặc simulation adapter.
Tạo subsystem hoặc mapper lúc này sẽ tạo một layer chưa có trách nhiệm thực.
Vì vậy producer decision được để dành.

IO và telemetry không được trở thành Observation owner.

## 16. Vì sao simulation thuộc V00_L04

L03 khóa interface và immutable value semantics. V00_L04 mới thêm deterministic
implementation đầu tiên của `VisionIO`. Vì vậy Simulation không phải gate của
L03 activation.

## 17. Vì sao pose estimation thuộc V00_L05

Robot pose cần field layout, camera mounting extrinsics, target identity và
camera-relative geometry. L03 chỉ mang vendor-neutral measurement boundary;
V00_L05 sở hữu phép tính pose candidate.

## 18. Vì sao quality và timing thuộc lesson sau

V00_L06 sở hữu ambiguity, quality, confidence và acceptance classification.
V00_L07 sở hữu timestamp, latency, freshness, ordering và duplicate semantics.

Do đó L03 không thêm ambiguity, quality, timestamp, latency, covariance,
freshness hoặc acceptance field.

## 19. Vì sao camera thật chờ đến V00_L08

V00_L08 chỉ được chọn đúng một real implementation sau khi review hardware,
WPILib version, vendor library, dependency, timestamp và simulation support.
L03 không được đoán Limelight, PhotonVision hoặc camera khác.

Physical camera, Driver Station / Glass và real robot không phải verification
surface của L03.

## 20. Vì sao fusion chờ đến V00_L09

Vision fusion cần robot-pose measurement đã có quality classification,
timestamp và acceptance. Các điều kiện đó chưa tồn tại trong L03. Chỉ V00_L09
mới được đưa accepted measurement vào `SwerveDrivePoseEstimator` do Swerve sở
hữu qua fusion boundary đã duyệt.

Autonomous tiếp tục dùng `getEstimatedPose()` và không đọc raw camera data.

## 21. Record implementation

Architect/User authorization riêng đã bao phủ đúng bốn Java file:

- `src/main/java/frc/robot/io/vision/VisionIO.java`;
- `src/main/java/frc/robot/observation/vision/VisionObservation.java`;
- `src/test/java/frc/robot/io/vision/VisionIOTest.java`; và
- `src/test/java/frc/robot/observation/vision/VisionObservationTest.java`.

`VisionIO` chỉ expose `updateInputs(VisionIOInputs)`. Inputs snapshot là
mutable và được refresh như một one-cycle transport hoàn chỉnh.
`VisionObservation` và target values là immutable, giữ acquisition order, bảo
vệ collection/transform ownership và chỉ expose state/field đã khóa. Không có
runtime producer được thêm.

## 22. Chẩn đoán false test-oracle

Verification tự động ban đầu có một expectation thất bại: effectively-zero
quaternion norm phải bị reject. Raw norm đó không observable qua public
contract `Transform3d` đã khóa: WPILib `Rotation3d` canonicalize quaternion
trong construction và có thể đổi raw input thành identity rotation hợp lệ
`(1, 0, 0, 0)` trước khi Observation nhận transform.

Đây là lỗi của test-fixture/oracle, không phải lỗi production contract. Test
được sửa theo authorization chỉ để accept và verify identity `Rotation3d` tại
boundary `Transform3d`. Không thêm raw quaternion field/API, không đổi schema
và không cần production repair. Nếu adapter vendor tương lai cần kiểm tra
trước normalization, adapter phải làm trước khi tạo `Rotation3d`.

## 23. Verification evidence

User đã độc lập verify dưới WPILib Java 17:

- `VisionObservationTest`: PASS;
- `VisionIOTest`: PASS;
- inherited V00_L01 `VisionFrameTransformTest`: PASS;
- inherited V00_L02 `AprilTagFieldLayoutContractTest`: PASS;
- full test suite: PASS; và
- clean full build: PASS.

Final documentation reconciliation, read-only architecture audit và final
closure review cũng PASS. Các kết quả này không claim camera, Simulation,
Driver Station / Glass hoặc real-robot behavior.

## 24. Verification surfaces và phần deferred

Simulation là `NOT APPLICABLE / DEFERRED TO V00_L04` vì L03 không thêm
simulation implementation. Driver Station / Glass là `NOT APPLICABLE` vì L03
không thêm runtime telemetry. Physical camera và Real Robot là
`NOT APPLICABLE / DEFERRED TO V00_L08` vì không có camera adapter, deployment
hoặc actuation path.

Pose estimation, quality/ambiguity, timestamp/latency, real vendor integration
và Swerve estimator fusion vẫn thuộc các V00 lesson sau.

## 25. Trạng thái lifecycle hiện tại

Đã hoàn thành:

- User preparation và Java 17 baseline build;
- read-only inheritance và architecture audit;
- Design Lock được duyệt và controlled activation;
- implementation và focused tests đã được authorize;
- focused/inherited verification, full regression và clean build;
- final documentation reconciliation; và
- final read-only architecture audit.

Còn lại một gate:

- User-owned Git add, commit và push.

V00_L03 hiện là `COMPLETE / FROZEN / READ-ONLY`. Không còn lesson editable đang
active. Git publication vẫn pending và thuộc User. V00_L04 chưa bắt đầu và
`A01_L10` vẫn bị cấm.
