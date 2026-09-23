# M00_L06 — Hướng dẫn học về quyền sở hữu lệnh Feeder

Bản tiếng Anh là chuẩn quy phạm. Bản tiếng Việt này giải thích cùng cấu trúc và cùng ý nghĩa kỹ thuật.

## 1. Danh tính bài học và trạng thái hiện tại

M00_L06 bổ sung quyền sở hữu lệnh Feeder vào nền tảng Feeder đã đóng băng của M00_L05. Đây là bài học duy nhất ở trạng thái `IN_PROGRESS / EDITABLE`. Phần triển khai và bằng chứng xác minh được chấp nhận đã hoàn tất; việc rà soát đóng bài độc lập, đóng băng và xuất bản vẫn đang chờ.

Phân loại bằng chứng:

- `THEORY VERIFIED`
- `SIMULATION VERIFIED`
- `REAL HARDWARE DEFERRED`

## 2. Mục tiêu học tập

Sau bài học này, học sinh cần có thể:

- giải thích vì sao command sở hữu yêu cầu của người lái nhưng không sở hữu hành vi cơ cấu;
- mô tả việc lập lịch bằng `whileTrue(...)`, nhả nút, ngắt lệnh và vô hiệu hóa robot;
- giải thích vì sao requirement của subsystem ngăn quyền sở hữu command xung đột;
- phân biệt xác minh command với xác minh phần cứng thật; và
- xác định các chủ đề tích hợp cơ cấu nào vẫn nằm ngoài M00_L06.

## 3. Nền tảng kế thừa từ M00_L05

M00_L05 đã thiết lập interface Feeder IO, ảnh chụp inputs, triển khai Noop, subsystem, observation bất biến, telemetry consumer, constants và đường dừng an toàn. M00_L06 không thiết kế lại các lớp này. Luồng cơ cấu kế thừa vẫn là:

`hardware -> FeederIO -> FeederIOInputs -> FeederSubsystem -> FeederObservation -> FeederTelemetry`

Telemetry vẫn chỉ đọc. Subsystem vẫn sở hữu trạng thái và các yêu cầu hành vi của cơ cấu.

## 4. Khái niệm mới duy nhất

Khái niệm kiến trúc mới duy nhất là quyền sở hữu yêu cầu Feeder dựa trên command. `RunFeederCommand` chuyển trạng thái giữ bumper trái của người lái thành một yêu cầu gửi tới subsystem hiện có. Command không truy cập IO, inputs khả biến, API nhà cung cấp, NetworkTables hoặc telemetry.

## 5. Kiến trúc điều khiển

Đường điều khiển là:

`Bumper trái của người lái -> Trigger binding -> RunFeederCommand -> FeederSubsystem`

Đường observation vẫn tách biệt và chỉ đọc. Command yêu cầu hành vi; subsystem quyết định và báo cáo trạng thái cơ cấu thông qua contract hiện có.

## 6. Lập lịch khi giữ trigger

`whileTrue(...)` lập lịch command khi điều kiện bumper trái chuyển thành đúng và giữ command được lập lịch trong khi điều kiện còn đúng. Khi điều kiện thành sai, WPILib hủy command. Việc hủy dẫn đến `end(true)`, nơi yêu cầu dừng an toàn được gửi.

Như vậy binding biểu diễn khoảng thời gian sở hữu. Nó không tạo logic điều khiển lặp lại trong `RobotContainer`.

## 7. Constructor và requirement của subsystem

Constructor của command nhận `FeederSubsystem` qua dependency injection và gọi `addRequirements(feederSubsystem)`. Requirement báo cho scheduler biết command này sở hữu Feeder trong lúc được lập lịch. Một command khác cần cùng subsystem không thể đồng thời sở hữu nó.

## 8. Initialize một lần

`initialize()` gọi phương thức yêu cầu cấp liệu của subsystem một lần khi bắt đầu lập lịch. Như vậy là đủ vì Feeder subsystem giữ trạng thái được yêu cầu. Lặp lại cùng một yêu cầu ở mỗi vòng scheduler chỉ tạo nhiễu mà không thay đổi hành vi của bài học.

## 9. Ngữ nghĩa execute và hoàn tất

`execute()` được để trống có chủ đích. `isFinished()` trả về `false`, vì vậy command không tự kết thúc. Vòng đời của nó do trigger, việc ngắt, robot bị vô hiệu hóa hoặc scheduler hủy lệnh kiểm soát.

`execute()` trống là một quyết định thiết kế, không phải thiếu triển khai.

## 10. End có nghĩa là dừng an toàn

`end(boolean interrupted)` luôn yêu cầu Feeder dừng. Cùng một hành động kết thúc an toàn được áp dụng khi bumper trái được nhả, command khác ngắt quyền sở hữu, scheduler hủy command hoặc robot bị vô hiệu hóa.

Command không cần hành vi dừng riêng cho kết thúc bình thường và kết thúc do ngắt.

## 11. Hành vi khi robot bị vô hiệu hóa

Command không cho phép chạy khi robot bị vô hiệu hóa. Vì vậy WPILib ngăn nó tiếp tục khi robot disabled. Đường `end(...)` yêu cầu dừng, và Simulation đã xác nhận rằng vô hiệu hóa robot khi bumper vẫn được giữ làm cả Feeder và Intake dừng.

## 12. Vì sao Feeder không có default command

Default command sẽ tự động giành lại subsystem mỗi khi subsystem rảnh. M00_L06 không cần hành vi Feeder chạy nền, nên không cài default command. Khi không có command được lập lịch, trạng thái dừng hiện có vẫn có hiệu lực.

## 13. RobotContainer vẫn là composition root

`RobotContainer` tạo `RunFeederCommand`, inject Feeder subsystem và khai báo binding bumper trái của người lái. Nó không tính output Feeder, gọi IO, diễn giải inputs khả biến, xuất telemetry hoặc sở hữu trạng thái cơ cấu. Điều này giữ nguyên vai trò composition root.

## 14. Observation, telemetry và hành vi Noop

Command không xuất telemetry. Nó yêu cầu subsystem thực hiện hành vi; subsystem tạo Feeder observation bất biến; telemetry tiêu thụ observation đó.

Bài học này vẫn dùng `FeederIONoop`. Vì vậy `available = false` và `connected = false` là kết quả mong đợi. Việc trạng thái yêu cầu thay đổi trong Simulation chứng minh logic command/subsystem, không chứng minh phản ứng motor vật lý.

## 15. Nhấn đồng thời các bumper không phải là phối hợp

Simulation có giới hạn đã quan sát yêu cầu Feeder bằng bumper trái trong khi Intake vẫn dừng. Việc nhấn thủ công nhiều nút cùng lúc không phải thiết kế phối hợp Intake/Feeder được phê duyệt. Nó không thiết lập trình tự, phân xử, interlock, chuyển vật tự động hoặc quyền sở hữu command kết hợp.

## 16. Thiết kế test tập trung và cô lập scheduler

Bộ xác minh tập trung gồm:

- `RunFeederCommandTest`
- `RobotContainerFeederCommandBindingTest`
- `FeederCommandArchitectureBoundaryTest`
- `FeederArchitectureBoundaryTest`

Các test bao phủ vòng đời command, requirements, hành vi binding, an toàn khi disabled và các dependency bị cấm. Trạng thái scheduler phải được dọn giữa các test để command đã được lập lịch hoặc trạng thái enable của một test không làm nhiễm test khác.

## 17. Sự phát triển contract test kế thừa được mong đợi

`FeederArchitectureBoundaryTest` kế thừa đã mã hóa đúng giả định của M00_L05 rằng chưa có Feeder command. M00_L06 chủ đích đưa vào command đã được roadmap cho phép, nên giả định hẹp đó phải phát triển trong khi mọi ranh giới nền tảng Feeder còn hiệu lực vẫn được bảo vệ.

Việc này được phân loại là `EXPECTED INHERITED TEST CONTRACT EVOLUTION`. Đây không phải lỗi production và không cho phép làm yếu các test không liên quan.

## 18. Bằng chứng build và regression được chấp nhận

Bốn lớp test tập trung đã PASS với `BUILD SUCCESSFUL in 7s`, bốn task up-to-date và mã thoát 0.

Full clean regression đã PASS bằng `gradlew clean build`, `BUILD SUCCESSFUL in 37s`, bảy task executed và mã thoát 0.

Các kết quả này là bằng chứng được cung cấp và được chấp nhận. Giai đoạn tài liệu này không chạy lại build hoặc test.

## 19. Bằng chứng Simulation có giới hạn

Các checkpoint được chấp nhận là:

| Checkpoint | Điều kiện | Feeder | Intake |
|---|---|---|---|
| A | Robot disabled | `STOPPED` | — |
| B | Driver Station và Xbox đã kết nối khi robot disabled | `STOPPED` | — |
| C | Teleop enabled, không thao tác điều khiển | `STOPPED` | — |
| D | Giữ bumper trái | `FEED_REQUESTED` | `STOPPED` |
| E | Nhả bumper trái | `STOPPED` | — |
| F | Giữ lại bumper trái | `FEED_REQUESTED` | `STOPPED` |
| G | Vô hiệu hóa robot trong khi vẫn giữ bumper | `STOPPED` | `STOPPED` |

Các gate được chấp nhận:

- `PASS_M00_L06_SIMULATION_LEFT_BUMPER_RELEASE_STOPPED`
- `PASS_M00_L06_SIMULATION_DISABLE_WHILE_HELD_STOPPED`
- `PASS_M00_L06_BOUNDED_SIMULATION_COMPLETE`

## 20. Giới hạn bằng chứng và phạm vi tương lai được bảo vệ

Không có phần cứng Feeder thật nào được sử dụng hoặc xác minh. CAN ID 45–49 chỉ là vùng lập kế hoạch. Bài học này không đưa ra tuyên bố về lựa chọn motor controller, danh tính thiết bị, dây điện, chiều quay, tỉ số truyền, hành vi sensor, giới hạn dòng, PID, feedforward, hiệu chuẩn, tuning, an toàn vật lý hoặc vận hành robot thật.

M00_L14, M00_L15 và M00_L16 vẫn là phạm vi tương lai được bảo vệ. Không nội dung nào trong bài học này cho phép tích hợp adapter thật, hành vi phối hợp cơ cấu hoặc thay đổi thứ tự chương trình học.

## 21. Ôn tập cho học sinh

Hãy xác nhận rằng em có thể trả lời các câu hỏi sau:

1. Vì sao command yêu cầu `FeederSubsystem` thay vì `FeederIO`?
2. Vì sao `execute()` trống là đúng trong trường hợp này?
3. Callback nào bảo đảm gửi yêu cầu dừng sau khi nhả nút hoặc bị ngắt?
4. Vì sao Simulation với `FeederIONoop` không chứng minh hành vi phần cứng thật?
5. Vì sao thay đổi test kế thừa là sự phát triển contract được mong đợi thay vì một lỗi?

Tóm tắt đúng: driver binding kiểm soát khoảng thời gian command sở hữu yêu cầu; command yêu cầu hành vi; subsystem sở hữu trạng thái cơ cấu; ranh giới IO sở hữu quyền truy cập phần cứng; và telemetry chỉ quan sát trạng thái bất biến.
