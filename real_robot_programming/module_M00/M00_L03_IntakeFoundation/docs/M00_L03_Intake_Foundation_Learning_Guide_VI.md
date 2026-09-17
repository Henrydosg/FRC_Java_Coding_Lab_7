# M00_L03 - Hướng dẫn học Nền tảng Intake

Tiếng Anh là bản quy chuẩn. Hướng dẫn này chỉ mô tả phần triển khai và bằng chứng đã được xác minh của M00_L03.

## 1. Danh tính bài học

- Khóa học: FRC Java Coding Lab 7.0
- Module: M00 - Competition Mechanism Foundations
- Bài học: M00_L03 - Intake Foundation
- Thư mục: `M00_L03_IntakeFoundation`
- Bài trước: M00_L02 - Mechanism Hardware Evidence Audit
- Vòng đời: `IN_PROGRESS / EDITABLE`

## 2. Vì sao cần bài học này

Robot cần một ranh giới phần mềm Intake rõ ràng trước khi Command hoặc nút điều khiển có thể sở hữu cơ cấu. Bài này xây dựng nền tảng có thể tái sử dụng, đồng thời trung thực giữ các dữ kiện phần cứng chưa có bằng chứng ở trạng thái chưa biết.

## 3. Mục tiêu học tập

Sau bài này, học sinh có thể giải thích ranh giới IO của Intake, quyền sở hữu của subsystem, trạng thái yêu cầu phần mềm, luồng Observation bất biến, telemetry chỉ đọc, cách ghép nối Noop an toàn, kiểm thử tập trung, và ranh giới giữa M00_L03 với M00_L04.

## 4. Kiến thức đã có từ M00_L01 và M00_L02

M00_L01 dạy cách tái sử dụng Frozen Backbone. M00_L02 dạy rằng một dữ kiện không trở thành đã xác minh chỉ vì nó có vẻ hợp lý. M00_L03 áp dụng cả hai: tái sử dụng kiến trúc và giữ các dữ kiện chưa được hỗ trợ về motor, dây điện, chiều, tỉ số truyền và cảm biến ở trạng thái `UNKNOWN`.

## 5. Khái niệm mới duy nhất

```text
AN INDEPENDENTLY OWNED, VENDOR-NEUTRAL INTAKE MECHANISM FOUNDATION
```

Mọi thành phần thêm trong M00_L03 đều phục vụ một khả năng này. Quyền sở hữu bằng Command do scheduler quản lý thuộc M00_L04.

## 6. Tổng quan kiến trúc Nền tảng Intake

```text
RobotContainer
    -> IntakeIONoop
    -> IntakeSubsystem
    -> IntakeObservation bất biến
    -> IntakeTelemetryFacade
    -> RobotTelemetry
```

Trách nhiệm điều khiển và quan sát vẫn tách biệt. `IntakeSubsystem + IntakeIO` là ranh giới sở hữu Intake.

## 7. IntakeIO — Vì sao cần ranh giới IO

`IntakeIO` tách ý nghĩa của cơ cấu khỏi cách triển khai phần cứng. Nó định nghĩa `updateInputs(...)`, `requestIntake()` theo ngữ nghĩa, và `stop()`. Nó cố ý không cung cấp API phần trăm motor thô, kiểu của vendor, danh tính CAN, loại controller, đảo chiều, tỉ số truyền, dòng điện, vận tốc, vị trí, PID hoặc trạng thái cảm biến.

## 8. IntakeIOInputs

Snapshot mutable cho một chu kỳ chỉ có đúng hai trường:

- `available`: implementation được chọn có thể cung cấp dịch vụ Intake.
- `connected`: implementation khả dụng báo nguồn đang kết nối.

Đây là dữ liệu vận chuyển, không phải domain model bất biến công khai.

## 9. IntakeIONoop

`IntakeIONoop` là implementation an toàn khi không có phần cứng. Nó luôn báo unavailable và disconnected một cách xác định; các hàm request và stop không tạo tác động vật lý. Nó cho phép ghép nối và Simulation giới hạn mà không giả vờ mô phỏng motor.

## 10. Quyền sở hữu của IntakeSubsystem

`IntakeSubsystem` sở hữu đúng một `IntakeIO`, inputs mutable, trạng thái yêu cầu phần mềm và Observation bất biến hiện tại. `periodic()` làm mới inputs rồi tạo Observation mới. Subsystem không chứa binding tay điều khiển, scheduling, Intake tự động hoặc phối hợp với cơ cấu khác.

## 11. Trạng thái yêu cầu phần mềm

Chỉ có hai trạng thái:

- `STOPPED`
- `INTAKE_REQUESTED`

Chúng chỉ biểu diễn ý định phần mềm. Chúng không chứng minh motor quay, roller quay, game piece di chuyển hoặc trạng thái vật lý thật của cơ cấu.

## 12. Luồng requestIntake()

```text
IntakeSubsystem.requestIntake()
    -> trạng thái yêu cầu = INTAKE_REQUESTED
    -> làm mới Observation phần mềm bất biến
    -> IntakeIO.requestIntake()
```

Yêu cầu theo ngữ nghĩa giúp tránh bịa ra một giá trị output motor tùy ý.

## 13. Luồng stop() / Safe-Stop

```text
IntakeSubsystem.stop()
    -> trạng thái yêu cầu = STOPPED
    -> làm mới Observation phần mềm bất biến
    -> IntakeIO.stop()
```

Kiểm thử tập trung xác minh đường gọi phần mềm này. Chúng không chứng minh motor thật đã dừng. Việc xác minh dừng vật lý vẫn được hoãn đến khi có phần cứng thật.

## 14. IntakeObservation bất biến

`IntakeObservation` là Java record gồm `available`, `connected` và trạng thái yêu cầu phần mềm. Nó yêu cầu nguồn connected cũng phải available. Mỗi lần cập nhật định kỳ tạo một giá trị mới, nên việc thay đổi `IntakeIOInputs` sau đó không thể sửa một Observation đã trả về trước đó.

## 15. Observation và Control

Observation mô tả điều phần mềm hiện biết; nó không yêu cầu hành vi. `IntakeObservation` không chứa tham chiếu IO, API vendor, publisher NetworkTables, Command, lệnh scheduler hoặc trạng thái cơ cấu mutable.

## 16. Telemetry của Intake

`IntakeTelemetryFacade` chỉ publish `Available`, `Connected` và `RequestedState` từ `IntakeObservation` bất biến. `RobotTelemetry` nối nguồn chỉ đọc với facade. Telemetry không bao giờ điều khiển hoặc thay đổi Intake, schedule Command hay gọi API vendor.

## 17. Ghép nối trong RobotContainer

`RobotContainer` tạo `IntakeIONoop`, inject nó vào `IntakeSubsystem`, tạo telemetry facade và nối đường Observation của Intake vào `RobotTelemetry`. Nó không chứa logic cơ cấu Intake, Command, button binding, default command hoặc hành vi scheduling.

## 18. M00_L03 và M00_L04

| M00_L03 - Intake Foundation | M00_L04 - Intake Command Ownership |
| --- | --- |
| IO contract và Inputs | Command do scheduler quản lý |
| Subsystem và trạng thái yêu cầu | Requirement của Command |
| Stop ở mức kiến trúc | Binding controller và Trigger |
| Observation bất biến | Quyền sở hữu default/manual |
| Telemetry chỉ đọc | Hành vi interruption |
| Ghép nối Noop | Ngữ nghĩa sở hữu của scheduler |

Cột M00_L04 là công việc tương lai và chưa được triển khai ở đây.

## 19. Vì sao chưa có adapter phần cứng thật

Chưa có bằng chứng chấp nhận được để chọn motor, controller, CAN bus, CAN ID, dây điện, đảo chiều, chiều dương, tỉ số truyền, giới hạn dòng, cảm biến hoặc cấu hình vật lý khác. Vì vậy adapter CTRE, REV hay adapter thật khác sẽ phải bịa dữ kiện và chưa được cho phép.

## 20. Vì sao Constants.java không thay đổi

`Constants.java` vẫn là nguồn cấu hình mặc định, nhưng M00_L03 chưa có giá trị phần cứng Intake nào được xác minh để đưa vào đó. SHA-256 được chấp nhận vẫn không đổi và không có Intake constant nào được thêm.

## 21. Chiến lược kiểm thử tập trung

Sáu lớp test tập trung bao phủ Noop, value semantics của Observation, trạng thái và forwarding của subsystem, tính bất biến của snapshot mới, telemetry, ghép nối RobotContainer, cô lập vendor, thứ tự safe-stop và bảo vệ khỏi rò rỉ phạm vi M00_L04. Fake IO trong test mô hình hóa lời gọi theo ngữ nghĩa, không mô hình hóa output motor vật lý.

## 22. Lỗi kiểm thử ban đầu và bài học rút ra

Lần chạy đầu hoàn thành 16 test: 15 pass và 1 fail. `periodicBuildsFreshImmutableSnapshotsFromCurrentInputsAndIntent()` ném `NullPointerException` vì fake trong test dereference callback tùy chọn mà không kiểm tra. Đây là `TEST DEFECT`, không phải lỗi production. Chỉ `IntakeSubsystemTest.java` được sửa bằng cách guard callback khi nó được cấu hình; không assertion nào bị xóa và mục đích kiểm tra snapshot vẫn được giữ nguyên.

## 23. Xác minh hồi quy đầy đủ

Sau khi bộ focused test đã sửa chạy pass, User chạy clean build bằng Java 17.0.16 Temurin. Kết quả: `BUILD SUCCESSFUL in 20s`, cả 7 actionable task được thực thi, exit code 0. Phân loại: `FULL REGRESSION VERIFIED`.

## 24. Xác minh Simulation giới hạn

WPILib Simulation khởi chạy, ứng dụng tiếp tục hoạt động, robot giữ Disabled, NetworkTables hiển thị node Intake và ghép nối Noop an toàn đang hoạt động. Không cần Driver Station hoặc Glass. Phân loại: `SIMULATION VERIFIED` cho hành vi kiến trúc phần mềm giới hạn.

## 25. Điều Simulation chứng minh

Bằng chứng Simulation hỗ trợ việc ứng dụng khởi động, ghép nối Noop, tích hợp Intake subsystem, sự hiện diện của telemetry Intake và đường dữ liệu phần mềm giới hạn.

## 26. Điều Simulation KHÔNG chứng minh

Simulation không chứng minh danh tính CAN, dây điện, loại motor/controller, chiều vật lý, chuyển động vật lý, dòng điện hoặc tải, hiệu năng cơ cấu, xử lý game piece hay safe-stop vật lý.

## 27. Hoãn phần cứng thật

Phân loại phần cứng thật là `REAL HARDWARE DEFERRED`. Đây là trạng thái bằng chứng trung thực, không phải thất bại. Một adapter thật trong tương lai phải được review riêng và phải giữ nguyên các ranh giới IO, subsystem, Observation, telemetry và Command hiện có.

## 28. Lỗi thường gặp

- Xem `INTAKE_REQUESTED` là bằng chứng có chuyển động vật lý.
- Thêm hàm phần trăm motor vào `IntakeIO` khi chưa có nhu cầu được xác minh.
- Publish trực tiếp `IntakeIOInputs` mutable.
- Cho telemetry gọi hành vi của subsystem.
- Thêm Intake Command hoặc binding trong M00_L03.
- Bịa CAN ID hoặc constants để nền tảng trông có vẻ hoàn chỉnh.
- Gọi `IntakeIONoop` là mô phỏng motor.

## 29. Diễn giải kiến trúc từng bước

Luồng M00_L03 hiện tại:

```text
RobotContainer
    -> tạo IntakeIONoop
    -> inject vào IntakeSubsystem
    -> subsystem xử lý IntakeIOInputs
    -> subsystem tạo IntakeObservation
    -> IntakeTelemetryFacade nhận Observation
    -> RobotTelemetry điều phối publication
```

Luồng điều khiển M00_L04 tương lai, chưa được triển khai trong M00_L03:

```text
Xbox / Trigger
    -> Intake Command
    -> IntakeSubsystem
```

## 30. Câu hỏi kiểm tra kiến thức

1. Vì sao `IntakeIO` tồn tại?
2. Vì sao API vendor không có trong Intake foundation?
3. Vai trò của `IntakeIONoop` là gì?
4. `STOPPED` có nghĩa gì?
5. `INTAKE_REQUESTED` có nghĩa gì?
6. Vì sao requested state không phải bằng chứng hành vi vật lý?
7. `stop()` xác minh đường phần mềm nào?
8. Vì sao `IntakeObservation` phải bất biến?
9. Telemetry được phép làm gì?
10. `RobotContainer` được phép làm gì cho Intake?
11. Trách nhiệm Intake nào thuộc M00_L04?
12. Vì sao không có giá trị Intake trong `Constants.java`?
13. Focused tests xác minh điều gì?
14. Bounded Simulation xác minh điều gì?
15. Vì sao phần cứng thật vẫn được hoãn?

## 31. Đáp án kiểm tra kiến thức

1. Nó tách khả năng cơ cấu độc lập vendor khỏi cách triển khai phần cứng.
2. Chưa có lựa chọn vendor hoặc phần cứng nào có đủ bằng chứng và authorization.
3. Nó cung cấp ghép nối an toàn, xác định và không tạo output vật lý.
4. Phần mềm đang yêu cầu trạng thái dừng; đây không phải bằng chứng vật lý.
5. Phần mềm đang yêu cầu hành động Intake theo ngữ nghĩa.
6. Ý định phần mềm không quan sát motor, roller hoặc game piece thật.
7. Trạng thái chuyển sang `STOPPED`, rồi subsystem forward `IntakeIO.stop()`.
8. Snapshot ổn định không được thay đổi khi IO inputs mutable đổi sau đó.
9. Chỉ publish các trường Observation và không bao giờ điều khiển hành vi.
10. Chỉ tạo, chọn, inject và kết nối dependency.
11. Commands, requirements, bindings, quyền sở hữu default/manual, interruption và ngữ nghĩa scheduler.
12. Chưa có giá trị cấu hình phần cứng Intake nào được xác minh.
13. Trạng thái, forwarding, thứ tự, snapshot, bất biến, Noop, telemetry, composition và ranh giới kiến trúc.
14. Khởi động, tích hợp Noop, sự hiện diện subsystem và telemetry, cùng hành vi phần mềm giới hạn.
15. Chưa có adapter thật hoặc cấu hình vật lý được xác minh, và chưa thực hiện kiểm thử vật lý.

## 32. Tóm tắt bằng chứng bài học

| Bằng chứng | Phân loại |
| --- | --- |
| Lý thuyết | `VERIFIED` |
| Focused tests | `VERIFIED` — bộ đã sửa, 16/16, exit code 0 |
| Hồi quy đầy đủ | `VERIFIED` — clean build, exit code 0 |
| Simulation | `VERIFIED` — chỉ phần mềm / Noop / composition giới hạn |
| Driver Station | `NOT APPLICABLE` |
| Glass | `NOT APPLICABLE` |
| Phần cứng thật | `REAL HARDWARE DEFERRED` |

## 33. Điều kiện kết thúc

Phạm vi production/test đã được cho phép, focused retest, hồi quy đầy đủ, Simulation giới hạn, review implementation độc lập và việc tạo hai hướng dẫn song ngữ đã hoàn tất. Review tài liệu độc lập, mọi sửa chữa tài liệu được cho phép, final closure build/review, lifecycle reconciliation, freeze authorization và publication của User vẫn đang chờ. Bài học chưa ở trạng thái `COMPLETE / FROZEN / READ-ONLY`.

## 34. Bước tiếp theo

Gate tiếp theo ngay lập tức là independent documentation review. M00_L04 vẫn `NOT ACTIVE / NOT CREATED`; nó chỉ có thể bắt đầu sau khi M00_L03 hoàn tất closure, freeze, publication và nhận authorization riêng.
