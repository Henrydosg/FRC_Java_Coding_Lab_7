# Hướng dẫn học M00_L05 - Feeder Foundation

Tiếng Anh là bản quy chuẩn. Tài liệu này dạy kiến trúc phần mềm M00_L05 đã được kiểm chứng và không khẳng định hành vi phần cứng Feeder thực tế.

## 1. Danh tính bài học

- Khóa học: FRC Java Coding Lab 7.0
- Module: M00 - Competition Mechanism Foundations
- Bài học: M00_L05 - Feeder Foundation
- Vòng đời: `IN_PROGRESS / EDITABLE`
- Một khái niệm mới: Feeder là một capability vận chuyển do một cơ cấu độc lập sở hữu

## 2. Mục tiêu học tập

Sau bài học, học sinh cần giải thích được quyền sở hữu Feeder, lần theo control path và observation path, phân biệt transport IO mutable với ý nghĩa semantic immutable, mô tả Noop và safe stop, phân loại đúng bằng chứng đã chấp nhận, và xác định nội dung nào thuộc M00_L06.

## 3. Kiến thức tiên quyết

Học sinh cần hiểu Frozen Backbone, IO và IOInputs, quyền sở hữu của subsystem, Observation bất biến, telemetry chỉ đọc, vai trò composition của RobotContainer, và các bài Intake M00_L03/M00_L04.

## 4. Quan hệ với M00_L03 và M00_L04

M00_L03 thiết lập Intake như một cơ cấu được sở hữu độc lập. M00_L04 bổ sung quyền sở hữu command thủ công do scheduler quản lý cho capability Intake đã có. M00_L05 quay lại mức Foundation để tạo capability Feeder riêng. Bài này không sao chép hành vi Intake và không đưa quyền sở hữu command của Feeder vào sớm.

## 5. Tái sử dụng kiến trúc, không tái sử dụng hành vi

Feeder tuân theo cùng mẫu kiến trúc đã được chứng minh ở Intake: IO contract, inputs một chu kỳ, quyền sở hữu của subsystem, observation bất biến, telemetry chỉ đọc và wiring tại composition root. Tái sử dụng nghĩa là giữ đúng các ranh giới trách nhiệm này. Điều đó không có nghĩa Intake và Feeder dùng chung trạng thái, method, command, giả định phần cứng hay chính sách cơ cấu.

## 6. Feeder có nghĩa gì trong bài này

Feeder là một capability cơ cấu vận chuyển với một yêu cầu semantic: feed. M00_L05 chỉ mô hình hóa ranh giới phần mềm. Bài không định nghĩa motor, chiều quay, tốc độ, đường đi game piece, sensor, chính sách xử lý kẹt hay việc vận chuyển vật lý thành công.

## 7. Quyền sở hữu cơ cấu

`FeederSubsystem` là owner duy nhất của hành vi phần mềm Feeder và trạng thái yêu cầu. Nó sở hữu một `FeederIO` được inject, một `FeederIOInputs` mutable, trạng thái yêu cầu hiện tại và `FeederObservation` immutable mới nhất.

## 8. Control path

```text
future caller
-> FeederSubsystem
-> FeederIO
-> FeederIONoop trong M00_L05
```

Hiện chưa có Feeder command hay controller binding. “Future caller” đánh dấu ranh giới subsystem công khai mà không triển khai M00_L06 trước thời điểm cho phép.

## 9. Observation path

```text
FeederIONoop
-> mutable FeederIOInputs
-> FeederSubsystem
-> immutable FeederObservation
-> FeederTelemetryFacade
-> RobotTelemetry
```

RobotContainer chọn và kết nối implementation. Telemetry chỉ làm nhiệm vụ quan sát.

## 10. FeederIO contract

`FeederIO` độc lập với vendor và khai báo đúng ba operation semantic:

- `updateInputs(FeederIOInputs inputs)`
- `requestFeed()`
- `stop()`

Interface không chứa kiểu vendor, NetworkTables publisher, dependency vào scheduler hay giá trị phần cứng vật lý.

## 11. FeederIOInputs

`FeederIOInputs` chứa đúng hai instance field boolean: `available` và `connected`. Đây là transport/input snapshot mutable cho một chu kỳ, do IO implementation đã chọn điền dữ liệu. Nó không phải observation semantic công khai và không được giữ lại hoặc expose như một Observation.

## 12. FeederIONoop

`FeederIONoop` là Feeder implementation runtime duy nhất trong M00_L05. Mỗi lần update đều ghi `available=false` và `connected=false`. `requestFeed()` và `stop()` là các no-op an toàn, xác định. Vì vậy luồng phần mềm có thể chạy nhưng không có output vật lý.

## 13. Trạng thái ban đầu của FeederSubsystem

Một `FeederSubsystem` hợp lệ bắt đầu với trạng thái yêu cầu `STOPPED` và một observation bất biến chứa unavailable, disconnected và `STOPPED`. Constructor không tự động yêu cầu feed hoặc gửi stop output.

## 14. Từ vựng trạng thái yêu cầu

Từ vựng có đúng hai giá trị:

- `STOPPED`: phần mềm không yêu cầu Feeder vận chuyển.
- `FEED_REQUESTED`: phần mềm yêu cầu Feeder vận chuyển.

Các giá trị này mô tả ý định phần mềm, không phải chuyển động phần cứng đã đo được.

## 15. requestFeed()

`FeederSubsystem.requestFeed()` trước hết ghi `FEED_REQUESTED`, sau đó cập nhật observation bất biến, rồi gọi `FeederIO.requestFeed()` đúng một lần cho mỗi lần method được gọi. Vì vậy callback IO nhìn thấy ý định phần mềm đã cập nhật.

`FEED_REQUESTED` không chứng minh motor quay, vận chuyển thành công, game piece di chuyển hay phần cứng khả dụng.

## 16. periodic()

`periodic()` yêu cầu IO cập nhật inputs mutable rồi tạo lại một observation immutable mới. Nó không gọi `requestFeed()`, không gọi `stop()`, không publish NetworkTables trực tiếp, không đọc controller và không dùng vendor API.

## 17. stop() và thứ tự safe stop

Thứ tự đã được chấp nhận là:

```text
requestedState = STOPPED
-> tạo lại FeederObservation
-> gọi FeederIO.stop() đúng một lần
```

Nếu `FeederIO.stop()` ném exception, ý định phần mềm và observation mới nhất vẫn là `STOPPED`. Đây là invariant safe stop phần mềm; nó không chứng minh phần cứng chưa biết đã dừng vật lý.

## 18. FeederObservation

`FeederObservation` là record immutable, độc lập với vendor, có đúng ba component:

- `available`
- `connected`
- `requestedState`

Nó sao chép các giá trị semantic và không bao giờ expose object `FeederIOInputs` mutable.

## 19. available, connected và requestedState

`available` trả lời implementation IO đã chọn có thể cung cấp dịch vụ Feeder hay không. `connected` trả lời implementation khả dụng đó có báo nguồn kết nối hay không. `requestedState` trả lời phần mềm hiện đang yêu cầu điều gì. Không được gộp ba sự thật này thành một ý nghĩa.

## 20. FeederTelemetryFacade

`FeederTelemetryFacade` nhận một observation immutable và chỉ publish các topic có kiểu `Available`, `Connected` và `RequestedState`. Nó không có method điều khiển Feeder, không phụ thuộc IO, vendor hay scheduler.

## 21. RobotTelemetry

`RobotTelemetry` giữ nguyên telemetry hiện có của Swerve, Vision, Intake, autonomous và driver. Phần bổ sung Feeder lấy observation immutable từ subsystem rồi ủy quyền publish cho `FeederTelemetryFacade`. Nó không request, stop, schedule hay configure hành vi Feeder.

## 22. Composition trong RobotContainer

RobotContainer chỉ thực hiện công việc composition cần thiết cho bài này:

```text
new FeederIONoop()
-> new FeederSubsystem(...)
-> new FeederTelemetryFacade(Feeder table)
-> RobotTelemetry
```

Nó không thêm `FeederIOSim`, real adapter, CAN ID, Feeder command, controller binding hay Feeder default command.

## 23. Vì sao chưa có command hay binding

M00_L05 dạy quyền sở hữu capability, không dạy quyền sở hữu scheduler. Feeder command, subsystem requirement, hành vi hold và controller binding thuộc M00_L06. Thêm chúng ngay bây giờ sẽ vi phạm quy tắc một khái niệm mới.

## 24. Vì sao chưa có real adapter

Chưa có bằng chứng xác thực về motor/controller Feeder, số thiết bị, bus, ID, ratio, inversion, sensor, current limit hay output limit. Real adapter sẽ buộc phải bịa đặt dữ kiện phần cứng. Lựa chọn kiến trúc an toàn là `FeederIONoop` và `REAL HARDWARE DEFERRED`.

## 25. CAN reservation và assignment khác nhau thế nào

Registry quy hoạch chuẩn dành CAN 45-49 cho Feeder trong tương lai. Planning reservation bảo vệ dung lượng namespace; nó không xác định một thiết bị đã lắp. M00_L05 không gán CAN ID vật lý cho Feeder, và không được mô tả CAN 45 như một thiết bị Feeder thực tế.

## 26. Bounded Simulation chứng minh điều gì

WPILib Simulation cho thấy runtime ổn định và các topic:

```text
Feeder/Available      = false
Feeder/Connected      = false
Feeder/RequestedState = STOPPED
```

Bằng chứng này xác nhận composition, dữ kiện Noop, observation flow, telemetry publication và trạng thái phần mềm. Phân loại: `SIMULATION VERIFIED / BOUNDED`.

## 27. Bounded Simulation không chứng minh điều gì

Simulation không chứng minh hành vi motor thật, vận chuyển game piece, dòng điện, gear ratio, inversion, tốc độ vật lý, hành vi sensor hay cấu hình CAN. Bài này không có mô hình vật lý Feeder động.

## 28. Bằng chứng Driver Station qua HALSIM

Việc kiểm chứng dùng HALSIM Robot State bên trong Robot Simulation, không phải kết nối Driver Station/roboRIO vật lý. Ở Disabled, Robot Enabled là No và DS Attached là Yes. Ở Teleoperated Enabled, Robot Enabled là Yes và DS Attached là Yes. Feeder giữ false/false/`STOPPED` ở cả hai trạng thái. Phân loại: `SIMULATED DRIVER-STATION VERIFIED / BOUNDED`.

## 29. Bằng chứng focused test và bài học từ architecture test

Lần chạy focused đầu tiên có 19 test: 18 pass và một architecture test fail. Assertion cũ tìm chuỗi `current` trong source và vô tình khớp với Javadoc hợp lệ `current cycle`. `FeederIOInputs` production vốn đã đúng.

Repair dùng reflection trên các field non-static, non-synthetic và yêu cầu đúng hai field boolean tên `available` và `connected`. Sau đó cả sáu focused test class được phép đều pass với `BUILD SUCCESSFUL in 26s`, exit code 0. Phân loại: `FOCUSED TESTS VERIFIED`.

Bài học kỹ thuật: architecture test nên kiểm tra cấu trúc và semantic Java, không nên kiểm tra từ ngữ không liên quan trong comment.

## 30. Bằng chứng full regression và scheduler isolation

Lần full clean regression đầu tiên chạy 682 test: 681 pass và test kế thừa `SwerveSubsystemKnownFieldPoseResetTest.scheduledPersistentResetPreservesBaselineAndCanResetAgainAfterMotion()` fail vì `FeederSubsystem.periodic()` được gọi trên object chưa hoàn chỉnh có IO null. Đây là `TEST ISOLATION / SHARED GLOBAL STATE DEFECT`; production Feeder code và inherited Swerve test đều đúng. Null-constructor test cố ý gọi `new FeederSubsystem(null)`. Java chạy constructor của `SubsystemBase` trước nên object được đăng ký với singleton `CommandScheduler`; sau đó null rejection của subclass ném exception nhưng registration toàn cục vẫn còn.

`FeederSubsystemTest` bổ sung cleanup `@AfterEach` bằng `CommandScheduler.getInstance().unregisterAllSubsystems()` và vẫn giữ nguyên kiểm tra null rejection. `cancelAll()` hủy các command đang schedule nhưng không tương đương với việc xóa đăng ký subsystem. Full clean regression cuối cùng pass 682/682 test với `BUILD SUCCESSFUL in 51s`, bảy task executed và exit code 0. Phân loại: `FULL CLEAN BUILD REGRESSION VERIFIED`.

## 31. Các lỗi học sinh thường gặp

- Xem `FEED_REQUESTED` như bằng chứng chuyển động hoặc vận chuyển.
- Expose `FeederIOInputs` mutable như Observation.
- Publish trực tiếp từ IO hoặc điều khiển Feeder từ telemetry.
- Thêm Feeder command hoặc controller binding trong M00_L05.
- Bịa motor, CAN ID, inversion, ratio hoặc current limit.
- Nhầm CAN reservation 45-49 với phần cứng đã xác thực.
- Viết architecture test tìm từ tùy ý trong comment.
- Chỉ cancel command mà quên trạng thái subsystem đã đăng ký trong test.

## 32. Checklist debug

1. Xác nhận `FeederIONoop` là runtime implementation được chọn.
2. Xác nhận inputs trở thành false/false sau mỗi update.
3. Xác nhận subsystem sở hữu requested state và observation mới nhất.
4. Xác nhận `requestFeed()` cập nhật ý định trước khi forward đúng một lần.
5. Xác nhận `periodic()` không phát output call.
6. Xác nhận `stop()` ghi và publish `STOPPED` trước IO stop.
7. Xác nhận telemetry chỉ publish các field của observation immutable.
8. Xác nhận không có Feeder command, binding, Constants entry hay physical CAN assignment.
9. Nếu full suite khác focused suite, kiểm tra cleanup singleton scheduler.

## 33. Tóm tắt kiến trúc

M00_L05 giữ nguyên Frozen Backbone: Noop IO cập nhật inputs mutable; subsystem sở hữu hành vi và tạo ý nghĩa immutable; telemetry chỉ publish ý nghĩa đó; RobotContainer composition object graph. Vendor API và phần cứng chưa biết vẫn nằm ngoài bài học.

## 34. Bàn giao sang M00_L06

M00_L06 đang `NOT ACTIVE / NOT CREATED`. Sau khi M00_L05 hoàn tất independent documentation review, closure, freeze và publication, bài tiếp theo mới có thể dạy quyền sở hữu command Feeder thủ công do scheduler quản lý. M00_L05 không triển khai và không tuyên bố công việc tương lai đó đã tồn tại.

## Câu hỏi ôn tập

1. M00_L05 đưa vào đúng một khái niệm mới nào?
2. “Tái sử dụng kiến trúc, không tái sử dụng hành vi” nghĩa là gì?
3. Class nào sở hữu requested state của Feeder?
4. `FeederIOInputs` có những field nào?
5. `FeederIOInputs` khác `FeederObservation` như thế nào?
6. `FEED_REQUESTED` có nghĩa gì và không chứng minh điều gì?
7. Thứ tự chính xác bên trong `stop()` là gì?
8. `periodic()` làm gì và phải tránh những output call nào?
9. Vì sao `FeederIONoop` là runtime implementation duy nhất?
10. Vì sao chưa có Feeder command hoặc controller binding?
11. CAN 45-49 có ý nghĩa gì trong bài này?
12. Bounded Simulation đã kiểm chứng điều gì?
13. Bằng chứng HALSIM Driver Station đã kiểm chứng điều gì?
14. Vì sao architecture-test failure là test defect?
15. Vì sao cần `unregisterAllSubsystems()` sau null-constructor test?

## Đáp án

1. Feeder là một capability vận chuyển do một cơ cấu độc lập sở hữu.
2. Tái sử dụng các ranh giới IO/subsystem/Observation/telemetry đã chứng minh mà không dùng chung hành vi Intake hay giả định phần cứng.
3. `FeederSubsystem`.
4. Đúng hai field boolean `available` và `connected`.
5. IOInputs là transport một chu kỳ mutable; Observation là ý nghĩa semantic immutable, độc lập với vendor.
6. Nó nghĩa là phần mềm yêu cầu feed; nó không chứng minh availability, motor quay, vận chuyển hay game piece di chuyển.
7. Ghi `STOPPED`, tạo lại observation, rồi gọi `FeederIO.stop()` đúng một lần.
8. Nó cập nhật inputs và tạo lại observation; nó không được request feed hoặc phát stop output.
9. Dữ kiện phần cứng Feeder chưa biết, nên Noop hỗ trợ học kiến trúc an toàn mà không bịa cấu hình.
10. Quyền sở hữu Feeder do scheduler quản lý là khái niệm của bài tiếp theo, M00_L06.
11. Đây là dung lượng quy hoạch được dành trước, không phải assignment thiết bị vật lý đã xác thực.
12. Composition, runtime stability, dữ kiện Noop, observation flow, telemetry và trạng thái phần mềm.
13. Hành vi phần mềm bounded ổn định trong trạng thái mô phỏng Disabled và Teleoperated Enabled, không phải hành vi DS/roboRIO vật lý.
14. Vì test khớp một từ trong Javadoc hợp lệ thay vì kiểm tra cấu trúc field Java thực tế.
15. `SubsystemBase` đăng ký object chưa hoàn chỉnh trước khi subclass từ chối null; chỉ cancel command không xóa đăng ký subsystem.

## Tóm tắt bằng chứng

- Implementation: `COMPLETE / VERIFIED`
- Lý thuyết / kiến trúc: `THEORY / ARCHITECTURE VERIFIED`
- Focused tests: `FOCUSED TESTS VERIFIED`
- Full regression: `FULL CLEAN BUILD REGRESSION VERIFIED`
- Simulation: `SIMULATION VERIFIED / BOUNDED`
- Simulated Driver Station: `SIMULATED DRIVER-STATION VERIFIED / BOUNDED`
- Phần cứng thật: `REAL HARDWARE DEFERRED`
- Independent implementation review: `PASS`
- Tài liệu: `IMPLEMENTED / READY FOR INDEPENDENT DOCUMENTATION REVIEW`
- Vòng đời bài học: `IN_PROGRESS / EDITABLE / NOT FROZEN / NOT PUBLISHED`
