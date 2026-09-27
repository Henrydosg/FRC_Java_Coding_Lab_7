# Hướng dẫn học M00_L04 - Quyền sở hữu lệnh Intake

Tiếng Anh là bản quy chuẩn. Tài liệu này dạy đúng phạm vi phần mềm đã được kiểm chứng của M00_L04; tài liệu không khẳng định hành vi phần cứng Intake thực tế.

## 1. Danh tính bài học

- Khóa học: FRC Java Coding Lab 7.0
- Module: M00 - Competition Mechanism Foundations
- Bài học: M00_L04 - Intake Command Ownership
- Vòng đời: `IN_PROGRESS / EDITABLE`
- Một khái niệm mới: quyền sở hữu Intake thủ công do scheduler quản lý trên capability Intake đã có

## 2. Vì sao cần bài học này

M00_L03 đã tạo một capability Intake hoàn chỉnh và độc lập với vendor, nhưng chưa quyết định cách người điều khiển tạm thời nắm quyền sử dụng capability đó. M00_L04 bổ sung command và controller binding để scheduler của WPILib quản lý quyền sở hữu này một cách an toàn.

## 3. Kiến thức kế thừa từ M00_L03

M00_L03 trả lời câu hỏi: “Intake sở hữu capability nào?” `IntakeSubsystem` sở hữu hành vi Intake, trạng thái yêu cầu phần mềm, `IntakeIO`, safe stop và việc tạo Observation bất biến. Các ranh giới IO và telemetry không thay đổi trong bài này.

## 4. Một khái niệm mới

M00_L04 trả lời câu hỏi: “Ai được phép sở hữu capability Intake khi người điều khiển đang yêu cầu nó?” `CommandScheduler` là bộ quản lý quyền sở hữu, còn `RunIntakeCommand` là chủ sở hữu được schedule trong thời gian command hoạt động.

## 5. Luồng điều khiển tổng thể

```text
Driver
-> Xbox Controller
-> Right Bumper Trigger
-> RunIntakeCommand
-> IntakeSubsystem
-> IntakeIO
```

Command điều phối một capability đã tồn tại. Nó không truy cập phần cứng và không tạo thêm một mô hình trạng thái Intake thứ hai.

## 6. Luồng quan sát không thay đổi

```text
IntakeIO
-> mutable IntakeIOInputs
-> IntakeSubsystem
-> immutable IntakeObservation
-> read-only telemetry
```

Quyền sở hữu command chỉ thay đổi trạng thái yêu cầu phần mềm thông qua subsystem. Nó không chuyển trách nhiệm Observation sang command.

## 7. WPILib Command là gì

Command biểu diễn một hành động robot có thể được schedule. Nó khai báo tài nguyên subsystem cần dùng và cung cấp các callback vòng đời để bắt đầu, chạy, kết thúc và dừng. Command không phải motor controller, IO adapter hay khóa phần cứng.

## 8. CommandScheduler sở hữu trách nhiệm gì

Scheduler khởi động command, gọi các lifecycle method, thực thi quy tắc requirement của subsystem, ngắt chủ sở hữu xung đột và hủy command khi Trigger yêu cầu. Nó quản lý quyền sở hữu phần mềm, không phải thread lock hay phần cứng điện.

## 9. Requirement của subsystem

`addRequirements(intakeSubsystem)` báo cho scheduler rằng `RunIntakeCommand` cần quyền sử dụng độc quyền `IntakeSubsystem`. Hai command cùng yêu cầu subsystem này không thể đồng thời là chủ sở hữu.

## 10. Vì sao requirement là IntakeSubsystem

Subsystem là ranh giới hành vi công khai của Intake. Yêu cầu IO, telemetry, RobotContainer hay thiết bị vendor sẽ đi vòng qua Frozen Backbone. Chỉ yêu cầu `IntakeSubsystem` diễn đạt đúng ranh giới điều phối thực sự.

## 11. Cấu trúc RunIntakeCommand

`RunIntakeCommand` là final command với đúng một dependency được giữ lại: `IntakeSubsystem` được truyền vào. Nó không chứa controller, IO, telemetry, NetworkTables, vendor API, timer, chính sách sensor hay giá trị output vật lý.

## 12. Ý nghĩa constructor

Constructor từ chối subsystem null, lưu instance được truyền vào và gọi `addRequirements(intakeSubsystem)`. RobotContainer inject owner Intake đã có; command không tự tạo subsystem mới.

## 13. initialize()

`initialize()` chạy khi command vừa được schedule. Trong bài này, nó gọi `intakeSubsystem.requestIntake()` đúng một lần để ghi nhận yêu cầu Intake có ý nghĩa tại thời điểm command bắt đầu.

## 14. execute()

`execute()` được gọi lặp lại khi command đang được schedule, nhưng cố ý không gọi lại `requestIntake()`. Bài học dạy quyền sở hữu theo vòng đời scheduler, không dạy vòng lặp ghi output cơ cấu mỗi 20 ms.

## 15. isFinished()

`isFinished()` trả về `false`. Command không tự quyết định hoàn thành; Right Bumper Trigger quyết định vòng đời thông qua schedule và cancel.

## 16. end(...)

`end(boolean interrupted)` luôn gọi `intakeSubsystem.stop()`. Lệnh stop là vô điều kiện, nên mọi đường kết thúc đều hội tụ về ranh giới safe stop do subsystem sở hữu.

## 17. Kết thúc do ngắt và kết thúc bình thường

`end(false)` biểu diễn kết thúc bình thường và vẫn dừng để phòng vệ. `end(true)` biểu diễn cancel hoặc interruption và cũng dừng. Dù command này thường hoạt động đến khi bị cancel, cả hai đường vòng đời đều được thiết kế an toàn.

## 18. Ủy quyền safe stop

Command không bao giờ gọi trực tiếp `IntakeIO.stop()`. Nó ủy quyền cho `IntakeSubsystem.stop()`, nơi ghi trạng thái `STOPPED`, cập nhật Observation bất biến và chuyển tiếp yêu cầu stop có ý nghĩa xuống IO. Đây là bằng chứng kiến trúc safe stop phần mềm, không phải bằng chứng dừng vật lý.

## 19. Chuyển quyền cho command cạnh tranh

Khi scheduler chấp nhận một command khác cũng yêu cầu `IntakeSubsystem`, WPILib ngắt owner hiện tại. `RunIntakeCommand.end(true)` dừng Intake trước khi command mới initialize. Đây là mutual exclusion của scheduler, không phải thread locking hay hardware locking.

## 20. Ánh xạ Right Bumper

RobotContainer dùng semantic accessor `rightBumper()` trên controller hiện có tại port kế thừa. Không có số nút thô, vì vậy ý định của người điều khiển có thể được đọc và review trực tiếp từ source.

## 21. Vòng đời whileTrue

```java
driverController.rightBumper().whileTrue(runIntakeCommand);
```

Khi nút chuyển sang true, command được schedule. Khi nút vẫn true, command tiếp tục được schedule. Khi nút chuyển sang false, command bị cancel, `end(true)` chạy và ranh giới stop của subsystem được gọi.

## 22. Vì sao không có default command cho Intake

Default command của Intake sẽ chiếm subsystem mỗi khi subsystem rảnh, không đúng khái niệm đang học. `toggleOnTrue` có thể giữ quyền sau khi thả nút; tách `onTrue`/`onFalse` để gọi subsystem trực tiếp sẽ bỏ qua command ownership. `whileTrue` diễn đạt chính xác hành vi giữ nút để sở hữu.

## 23. Trách nhiệm composition của RobotContainer

RobotContainer tái sử dụng `IntakeSubsystem` hiện có, tạo đúng một `RunIntakeCommand` và cấu hình Right Bumper binding. Nó cũng giữ nguyên Back/View Prepare Autonomous binding kế thừa.

## 24. Những gì không được đặt trong RobotContainer

RobotContainer không được gọi `requestIntake()` hoặc `stop()` trực tiếp từ Trigger, sở hữu trạng thái Intake, truy cập hành vi phần cứng, diễn giải sensor hay chứa giá trị motor. Nó vẫn là composition root, không phải owner cơ cấu.

## 25. IntakeIOInputs và IntakeObservation

`IntakeIOInputs` là mutable one-cycle transport/input snapshot do IO cập nhật. `IntakeObservation` là immutable vendor-neutral observation/value snapshot do subsystem tạo. Dữ liệu vận chuyển mutable không được lộ ra như hợp đồng Observation công khai.

## 26. Available, Connected và RequestedState

`Available` và `Connected` mô tả sự sẵn sàng của IO hoặc phần cứng. `RequestedState` mô tả ý định phần mềm do `IntakeSubsystem` sở hữu. Các giá trị này trả lời những câu hỏi khác nhau và không được xem là tương đương.

## 27. Ý nghĩa IntakeIONoop

Implementation đang chọn là `IntakeIONoop`, một implementation xác định và độc lập với vendor dành cho bài học phần mềm có phạm vi giới hạn này. Nó chủ ý báo `Available=false` và `Connected=false`, nhận các semantic method call nhưng không tạo output vật lý, nhờ đó có thể kiểm chứng kiến trúc phần mềm và hành vi scheduler an toàn.

## 28. Bằng chứng focused test

Mười bốn focused test đã PASS với `BUILD SUCCESSFUL in 28s`, bốn task được thực thi và exit code 0. Chúng kiểm tra null, requirement chính xác, một request lúc bắt đầu, không request lặp trong execute, command tiếp tục hoạt động, normal stop, cancellation stop, thứ tự chuyển owner, disabled behavior, binding và ranh giới kiến trúc.

Phân loại bằng chứng: `FOCUSED TESTS: VERIFIED`.

## 29. Bằng chứng full regression

Full clean regression đã PASS với `BUILD SUCCESSFUL in 47s`, bảy task được thực thi và exit code 0. Kết quả cho thấy command boundary mới không làm hỏng bộ kiểm thử kế thừa.

Phân loại bằng chứng: `FULL REGRESSION: VERIFIED`.

## 30. Bằng chứng Simulation

WPILib Simulation khởi động thành công, chạy hơn hai phút, hiển thị controller tại `Joystick[0]`, có kết nối NetworkTables và node Intake, đồng thời không quan sát thấy crash của Intake command. Intake vẫn là Noop nên không mong đợi chuyển động.

Phân loại bằng chứng: `SIMULATION VERIFIED / BOUNDED`.

## 31. Bằng chứng Driver Station

Khi Teleop được enable, chuỗi trạng thái phần mềm quan sát được là:

```text
STOPPED
-> giữ Right Bumper
-> INTAKE_REQUESTED
-> thả Right Bumper
-> STOPPED
```

Trong toàn bộ chuỗi, `Available=false` và `Connected=false` vẫn đúng như mong đợi vì implementation là `IntakeIONoop`. Phân loại: `DRIVER STATION: VERIFIED / BOUNDED`.

## 32. Những gì chưa được chứng minh

Không có bằng chứng cho loại motor/controller, CAN ID hoặc bus, wiring, inversion, gearing, current limit, tốc độ, chiều quay, torque, sensor, khả năng lấy game piece, chuyển động vật lý hay dừng vật lý. Glass chỉ hỗ trợ quan sát read-only và là `NOT APPLICABLE` như một completion gate riêng. Phần cứng thật là `REAL HARDWARE DEFERRED`.

## 33. Lỗi thường gặp và cách kiểm tra

- Gọi lặp `requestIntake()` trong `execute()` sẽ nhầm quyền sở hữu scheduler với điều khiển output lặp.
- Gọi subsystem trực tiếp từ Trigger sẽ bỏ qua command ownership.
- Bỏ `addRequirements(...)` sẽ cho phép các owner phần mềm xung đột.
- Stop có điều kiện có thể để trạng thái yêu cầu tiếp tục hoạt động.
- Xem `RequestedState` như bằng chứng motor chạy sẽ biến ý định phần mềm thành khẳng định phần cứng sai.
- Xem `Available=false` là lỗi Driver Station sẽ bỏ qua việc implementation hiện tại là Noop.

## 34. Liên hệ với M00_L05

M00_L04 chỉ nói về quyền sở hữu Intake. M00_L05 sẽ giới thiệu Feeder như một mechanism foundation riêng sau khi M00_L04 được review, đóng, freeze và publish đầy đủ. Bài này không tạo Feeder code và không điều phối Intake với Feeder.

## Câu hỏi cho học sinh

1. M00_L04 giới thiệu một khái niệm mới nào?
2. Thành phần nào quản lý command ownership?
3. Vì sao `RunIntakeCommand` yêu cầu `IntakeSubsystem`?
4. `initialize()` được gọi khi nào và làm gì trong bài này?
5. Vì sao `requestIntake()` không xuất hiện trong `execute()`?
6. Vì sao `isFinished()` trả về `false`?
7. `end(...)` phải làm gì ở cả kết thúc bình thường và bị ngắt?
8. Điều gì xảy ra khi một Intake command cạnh tranh được schedule?
9. `whileTrue` có nghĩa gì khi nhấn, giữ và thả nút?
10. Vì sao không cấu hình default command cho Intake?
11. RobotContainer được phép làm gì trong bài này?
12. `IntakeIOInputs` khác `IntakeObservation` như thế nào?
13. Vì sao `RequestedState` có thể là `INTAKE_REQUESTED` trong khi `Available` và `Connected` là false?
14. Bằng chứng Driver Station bounded chứng minh điều gì?
15. Những dữ kiện phần cứng Intake nào vẫn chưa được kiểm chứng?

## Đáp án

1. Quyền sở hữu Intake thủ công do scheduler quản lý trên capability Intake đã có.
2. `CommandScheduler` của WPILib.
3. Subsystem là ranh giới hành vi và quyền sở hữu công khai của Intake, nên nó là tài nguyên scheduler cần loại trừ lẫn nhau.
4. Nó được gọi khi command bắt đầu được schedule và yêu cầu Intake một lần qua `IntakeSubsystem`.
5. Subsystem đã sở hữu requested state; command dạy quyền sở hữu theo vòng đời chứ không ghi output lặp.
6. Thời gian giữ nút và thao tác cancel quyết định vòng đời, không phải tự động hoàn thành.
7. Nó phải luôn gọi `IntakeSubsystem.stop()`.
8. Command hiện tại bị ngắt, dừng qua `end(true)`, rồi quyền sở hữu mới được chuyển giao.
9. Nhấn sẽ schedule, giữ sẽ duy trì command và thả sẽ cancel rồi đi qua stop path.
10. Trong bài này, Intake chỉ nên được sở hữu bởi hold command rõ ràng.
11. Nó được tái sử dụng và inject subsystem, tạo command và cấu hình binding.
12. IOInputs là dữ liệu vận chuyển mutable một chu kỳ; Observation là ý nghĩa bất biến và độc lập với vendor.
13. `RequestedState` là ý định phần mềm, còn hai trường kia báo tình trạng IO/phần cứng; `IntakeIONoop` không có phần cứng thật.
14. Nó chứng minh chuỗi bounded từ controller đến scheduler rồi đến trạng thái phần mềm, không chứng minh chuyển động vật lý.
15. Mọi dữ kiện về motor, CAN, wiring, chiều, tốc độ, dòng điện, sensor, lấy game piece và dừng vật lý vẫn chưa được kiểm chứng và được hoãn lại.

## Tóm tắt bằng chứng

- Theory: `VERIFIED`
- Focused tests: `VERIFIED`
- Full regression: `VERIFIED`
- Simulation: `SIMULATION VERIFIED / BOUNDED`
- Driver Station: `VERIFIED / BOUNDED`
- Glass: `NOT APPLICABLE` như một completion gate riêng
- Real hardware: `REAL HARDWARE DEFERRED`
- Vòng đời bài học: `IN_PROGRESS / EDITABLE`
