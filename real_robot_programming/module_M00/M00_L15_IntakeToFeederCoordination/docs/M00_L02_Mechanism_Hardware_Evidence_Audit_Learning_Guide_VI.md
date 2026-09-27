# M00_L02 - Hướng dẫn học Kiểm tra Bằng chứng Phần cứng Cơ cấu

Tiếng Anh là bản quy phạm. Hướng dẫn tiếng Việt có tính giải thích và phải giữ nguyên cấu trúc, ý nghĩa của bản tiếng Anh.

## 1. Danh tính và Mục đích Bài học

- Mô-đun: `M00 - Competition Mechanism Foundations`
- Bài học: `M00_L02 - Mechanism Hardware Evidence Audit`
- Khái niệm mới duy nhất: phân loại dữ kiện cơ cấu bằng bằng chứng có kỷ luật.
- Phạm vi runtime: không có. Bài học này không triển khai hoặc vận hành cơ cấu.

Bài học dạy người mới tách điều đã biết khỏi điều phỏng đoán trước khi bắt đầu công việc cơ cấu sau này. Một bảng đẹp là chưa đủ: mỗi mục phải có trạng thái, nguồn, hành động xác minh, ý nghĩa an toàn và phụ thuộc tương lai có thể bảo vệ được.

## 2. Mục tiêu Học tập

Sau bài học, học sinh có thể:

1. phân biệt dữ kiện phần cứng với giả định;
2. gán đúng một trạng thái: `VERIFIED`, `PROVISIONAL`, `UNKNOWN` hoặc `NOT APPLICABLE`;
3. chỉ ra nguồn bằng chứng mà không nói quá điều nguồn đó chứng minh;
4. giải thích vì sao trạng thái đã chọn là hợp lý;
5. ghi hành động còn thiếu để tăng độ mạnh bằng chứng;
6. giải thích liên quan an toàn và phụ thuộc bài học tương lai;
7. tránh biến kiểm tra bằng chứng thành chọn phần cứng hoặc triển khai; và
8. giữ nguyên Frozen Backbone khi chuẩn bị các cơ cấu tương lai.

## 3. Phạm vi và Nội dung Không thuộc Bài học

Trong phạm vi: định nghĩa bằng chứng, chất lượng nguồn, tính áp dụng, quy trình kiểm tra, lập luận an toàn, phụ thuộc tương lai và ma trận khởi đầu cho Intake, Feeder, Flywheel và Elevator.

Ngoài phạm vi: chọn thiết bị cuối cùng, bịa giá trị, sửa Java hoặc cấu hình, gán CAN ID, định nghĩa tỷ số hoặc giới hạn, thêm thư viện hãng, tạo API cơ cấu, tinh chỉnh vòng điều khiển, commissioning phần cứng hoặc kích hoạt M00_L03.

Mọi ví dụ chỉ để minh họa. Chúng không phải tuyên bố về robot hiện tại.

## 4. Dữ kiện Kiến trúc và Dữ kiện Phần cứng

Dữ kiện kiến trúc cho biết trách nhiệm thuộc về đâu. Ví dụ, quản trị repository xác lập năng lực Intake tương lai được sở hữu qua `IntakeSubsystem + IntakeIO`, và API hãng nằm trong adapter IO cụ thể. Điều này **không** chứng minh một motor, controller, sensor, tỷ số, CAN ID hay cụm cơ khí cụ thể tồn tại.

Dữ kiện phần cứng mô tả robot hoặc thiết bị thực tế đã chọn. Nó cần bằng chứng áp dụng cho đúng robot, thiết bị, dây nối, cách lắp, cấu hình hoặc điều kiện kiểm tra đó. Không dùng quy tắc kiến trúc làm bằng chứng cho một giá trị vật lý.

## 5. Quan hệ với Frozen Backbone

Việc kiểm tra phải giữ nguyên hai luồng đóng băng:

```text
CONTROL: Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware
OBSERVATION: hardware -> IOInputs -> subsystem / estimator -> immutable Observation -> telemetry -> NT4 / Glass / log
```

- `RobotContainer` chỉ là composition root.
- Subsystem sở hữu hành vi, trạng thái và safe stop của cơ cấu.
- Interface IO không phụ thuộc hãng; adapter IO cụ thể cô lập API hãng.
- `Constants.java` vẫn là thẩm quyền cấu hình mặc định.
- Command yêu cầu hoặc phối hợp hành vi subsystem.
- Telemetry chỉ đọc immutable Observation.
- Ma trận bằng chứng cung cấp thông tin cho thiết kế sau này; nó không vượt qua IO, chuyển hành vi vào telemetry hay cho phép viết mã.

Bằng chứng thu thập trong M00_L02 không tự cho phép thêm hoặc thay đổi giá trị trong `Constants.java`. M00_L02 chỉ ghi nhận bằng chứng. Mọi thay đổi `Constants.java` trong tương lai đều cần đúng phạm vi và sự cho phép của bài học tương ứng sau này. Ngay cả khi một dữ kiện phần cứng trở thành `VERIFIED`, không được thay đổi mã cấu hình trong M00_L02.

Các mục sau là dữ kiện quyền sở hữu kiến trúc `VERIFIED` cho những bài học tương lai:

```text
Intake:   IntakeSubsystem + IntakeIO
Feeder:   FeederSubsystem + FeederIO
Flywheel: FlywheelSubsystem + FlywheelIO
Elevator: ElevatorSubsystem + ElevatorIO

Phối hợp bắn:
FlywheelSubsystem
+ FeederSubsystem
+ ShootCommand requiring both
```

Thiết kế quyền sở hữu M00 đã khóa không có `ShooterSubsystem` và không có `ShooterIO`. Các dữ kiện quyền sở hữu này không chứng minh motor vật lý nào tồn tại, cũng không xác lập model motor-controller, CAN ID, sensor, dây điện, tỷ số, inversion, giới hạn dòng điện hoặc hành vi safe-stop vật lý. Mỗi dữ kiện vật lý vẫn cần bằng chứng áp dụng riêng.

## 6. Bốn Trạng thái Bằng chứng Cấp Dữ kiện

Mỗi dữ kiện nhận đúng một trạng thái.

| Trạng thái | Ý nghĩa | Hồ sơ tối thiểu |
| --- | --- | --- |
| `VERIFIED` | Bằng chứng chấp nhận được và đã định danh xác lập dữ kiện trong phạm vi đã nêu. | Nguồn chính xác, tính áp dụng và mọi giới hạn phạm vi. |
| `PROVISIONAL` | Có cơ sở hỗ trợ thật, nhưng một giới hạn đã nêu ngăn việc xác minh hoàn toàn. | Cơ sở, giới hạn và hành động xác minh chưa hoàn tất. |
| `UNKNOWN` | Bằng chứng chấp nhận được chưa xác lập dữ kiện. | Điều còn thiếu và cách có thể xác lập sau này. |
| `NOT APPLICABLE` | Hạng mục thực sự không áp dụng. | Lý do cụ thể chứng minh vì sao không áp dụng. |

Các từ như *có lẽ*, *có khả năng*, *dự kiến*, *phổ biến*, *thông thường*, *giả định* hoặc *tiện lợi* không làm dữ kiện thành `VERIFIED`.

## 7. Độ mạnh Bằng chứng và Tính Áp dụng cho Robot

Độ mạnh và tính áp dụng là hai câu hỏi riêng. Datasheet hãng có thể xác lập tốt khả năng định mức của thiết bị, nhưng không chứng minh thiết bị đó được lắp trên robot này. Ảnh dây rõ có thể chứng minh kết nối vật lý, nhưng không chứng minh firmware hoặc cấu hình đã áp dụng. Simulation thành công có thể chứng minh hành vi trong mô hình, nhưng không chứng minh chiều vật lý, CAN identity, ma sát, tải, nhiệt độ hoặc biên dòng điện.

Luôn hỏi:

1. Nguồn có đáng tin cho loại dữ kiện này không?
2. Nguồn có áp dụng cho đúng robot, thiết bị, revision, cấu hình và điều kiện kiểm tra này không?

## 8. Kỷ luật Nguồn Bằng chứng

Nguồn tốt được ghi đủ chính xác để người khác tìm và kiểm tra. Ví dụ: mục ADR được duyệt, ảnh nhãn thiết bị gắn với vị trí robot, revision sơ đồ điện, hồ sơ mua có part number, manual/version hãng, configuration readback từ thiết bị, số răng đo được hoặc biên bản commissioning có phạm vi rõ.

Các mục yếu như “đội biết,” “cách lắp thường dùng,” “trông đúng” hoặc ảnh chụp không có liên kết không tự xác minh. Nếu chưa tìm thấy nguồn áp dụng, ghi `No applicable evidence identified` và dùng `UNKNOWN`.

## 9. Quy trình Kiểm tra

Dùng chuỗi sau cho từng hàng:

```text
Câu hỏi -> Tìm bằng chứng -> Tính áp dụng -> Trạng thái -> An toàn
        -> Hành động xác minh -> Phụ thuộc -> Rà soát
```

Quá trình có thể lặp, nhưng nâng trạng thái phải dựa trên bằng chứng:

```text
UNKNOWN -> PROVISIONAL -> VERIFIED
```

Một hàng có thể bỏ qua `PROVISIONAL` nếu bằng chứng quyết định xác lập trực tiếp. Hàng cũng có thể giữ `UNKNOWN`. Rà soát không được nâng trạng thái chỉ vì bài học sau cần giá trị đó.

## 10. Các Nhóm Bằng chứng Phần cứng

Kiểm tra bao gồm năm nhóm liên kết:

- Cơ khí: mục đích, lắp đặt, tỷ số, chuyển đổi, chiều và hành trình.
- Điện: controller, bus, CAN identity, bố trí follower và giới hạn.
- Liên quan điều khiển: ý nghĩa sensor, phase, inversion, neutral behavior và safe stop.
- Tương thích phần mềm: firmware/library, readback và hỗ trợ simulation.
- Commissioning và an toàn: kiểm tra vật lý, cấp năng lượng có giới hạn, emergency stop và bằng chứng chấp nhận.

## 11. Bằng chứng Cơ khí

Bằng chứng cơ khí phải nối bản vẽ hoặc phép đo với robot đã lắp. Số răng, đường kính pulley, chu vi drum, tầng chain, phạm vi hành trình và hướng lắp ảnh hưởng chuyển đổi đơn vị và chiều. Tỷ số catalog không tự động là tổng tỷ số cơ cấu. Hãy ghi sự chưa chắc chắn thay vì nhân các giá trị giả định.

## 12. Bằng chứng Điện

Bằng chứng điện phải định danh controller đã lắp, bố trí motor, CAN bus, CAN ID duy nhất, dây điện, đường nguồn và giới hạn áp dụng. Một constant trong phần mềm không tự chứng minh thiết bị hoặc dây vật lý là đúng. Commissioning sau này phải đối chiếu tài liệu, device discovery, nhãn và configuration readback.

## 13. Bằng chứng Liên quan Điều khiển

Bằng chứng liên quan điều khiển định nghĩa lệnh dương và phép đo dương nghĩa là gì, có cần inversion không, sensor phase có cùng chiều chuyển động không, neutral behavior hoạt động thế nào và safe stop phải làm gì. Các dữ kiện này ảnh hưởng ổn định closed-loop và quyền sở hữu command an toàn. Chúng phải được xác lập trước công việc feedback sau này.

## 14. Bằng chứng Tương thích Phần mềm

Bằng chứng tương thích liên kết chính xác firmware thiết bị, vendor library, phiên bản WPILib, hành vi API và hỗ trợ cấu hình/readback. “Library hỗ trợ dòng này” là chưa đủ khi chưa biết model hoặc firmware đã lắp. Hỗ trợ simulation cũng phải nêu mô hình hóa điều gì và điều gì vật lý vẫn chưa được chứng minh.

## 15. Bằng chứng Commissioning và An toàn

Commissioning nên đi từ kiểm tra không cấp điện đến thử năng lượng thấp có ràng buộc, kèm kế hoạch emergency stop và người quan sát rõ ràng. Ghi đúng phạm vi đã thử. Cơ cấu chạy một lần không tự động được xác minh về chiều, giới hạn, tải, nhiệt, lặp khởi động, phản ứng lỗi hoặc safe stop.

## 16. Quy tắc Ghi PROVISIONAL

Chỉ dùng `PROVISIONAL` khi có đủ ba mục:

1. một cơ sở hỗ trợ được nêu tên;
2. một giới hạn được nêu tên khiến chưa thể `VERIFIED`; và
3. một hành động xác minh cụ thể còn chưa hoàn tất.

Ví dụ: hồ sơ mua xác định Talon FX, nhưng chưa đối chiếu nhãn robot và device discovery. Điều đó có thể hỗ trợ mục controller-family ở trạng thái provisional. Nếu không có hồ sơ mua hoặc cơ sở thật khác, trạng thái đúng là `UNKNOWN`. Không tạo trạng thái provisional giả để ma trận trông đầy hơn.

## 17. Liên quan An toàn

Mỗi hàng cần trả lời: “Nếu dữ kiện này sai thì chuyện gì có thể xảy ra?” Hậu quả có thể gồm chuyển động bất ngờ, motor đối nghịch, feedback mất ổn định, va hard stop, hỏng do dòng/nhiệt, mất braking, vị trí không hợp lệ, tự khởi động lại không an toàn hoặc stop không hiệu quả. Liên quan an toàn không chứng minh dữ kiện; nó đặt ưu tiên rà soát và mức thận trọng của phương pháp xác minh.

## 18. Phụ thuộc Bài học Tương lai

- `M00_L03` phụ thuộc bằng chứng Intake cho Intake Foundation.
- `M00_L05` phụ thuộc bằng chứng Feeder cho Feeder Foundation.
- `M00_L07` phụ thuộc bằng chứng Flywheel cho Flywheel Foundation.
- `M00_L10` phụ thuộc bằng chứng Elevator cho position-reference semantics.
- Các bài closed-loop và an toàn sau này phụ thuộc đơn vị, chiều, sensor, giới hạn, hành vi cấu hình và kỳ vọng safe-stop đã xác minh.

Một hàng chưa giải quyết không bị che giấu. Nó trở thành điều kiện vào hoặc hành động hoãn rõ ràng cho bài tương lai.

## 19. Ma trận Khởi đầu Bằng chứng Phần cứng

Quy tắc ma trận:

- Mọi giá trị vật lý hoặc theo thiết bị dưới đây giữ `UNKNOWN` vì bài học này chưa tìm thấy bằng chứng áp dụng.
- Tám hàng `VERIFIED` chỉ xác lập mục đích kiến trúc hoặc quyền sở hữu safe-stop. Chúng không chứng minh phần cứng tồn tại, đã lắp, đã nối dây, đã cấu hình hoặc có hành vi vật lý.
- Không có hàng `PROVISIONAL` vì hiện không hàng nào đồng thời có cơ sở hỗ trợ thật và giới hạn đã ghi.
- Không có hàng `NOT APPLICABLE` vì bằng chứng hiện tại chưa xác lập rằng hạng mục liệt kê nào chắc chắn không thể áp dụng.

| ID | Cơ cấu | Dữ kiện / Tham số | Giá trị Hiện tại | Đơn vị | Trạng thái Bằng chứng | Nguồn Bằng chứng | Phương pháp Xác minh | Liên quan An toàn | Ghi chú / Điều chưa biết | Phụ thuộc Bài học Tương lai |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| INT-01 | Intake | Mục đích cơ cấu | Năng lực Intake được sở hữu độc lập (chỉ là mục đích kiến trúc) | - | VERIFIED | M00 ADR, Locked 16-Lesson Roadmap, M00_L03 | Xác nhận câu chữ ADR khi rà soát | Tránh mơ hồ quyền sở hữu | Không chứng minh phần cứng tồn tại | M00_L03 |
| INT-02 | Intake | Model motor/controller | Chưa xác lập | - | UNKNOWN | No applicable evidence identified | Kiểm tra nhãn lắp và hồ sơ | Sai API hoặc định mức | Chưa chọn candidate | M00_L03 |
| INT-03 | Intake | CAN bus | Chưa xác lập | tên bus | UNKNOWN | No applicable evidence identified | Kiểm tra dây và device discovery | Mất hoặc xung đột thiết bị | Bus vật lý chưa biết | M00_L03 |
| INT-04 | Intake | CAN ID | Chưa xác lập | số nguyên | UNKNOWN | No applicable evidence identified | Device discovery và đối chiếu nhãn | Trùng ID hoặc điều khiển sai | Không gán tại đây | M00_L03 |
| INT-05 | Intake | Bố trí motor/follower | Chưa xác lập | số lượng/bố trí | UNKNOWN | No applicable evidence identified | Kiểm tra cụm lắp và sơ đồ điện | Motor có thể đối nghịch/quá tải | Chưa biết số lượng và follower mode | M00_L03 |
| INT-06 | Intake | Loại sensor | Chưa xác lập | - | UNKNOWN | No applicable evidence identified | Kiểm tra cụm lắp và hồ sơ part | Trạng thái hoặc input tự động sai | Chưa biết có sensor không | M00_L03 và sensing sau này |
| INT-07 | Intake | Cách lắp sensor | Chưa xác lập | vị trí/hướng | UNKNOWN | No applicable evidence identified | Kiểm tra và đo hình học lắp | Phát hiện sai hoặc không tới được | Cách lắp chưa biết | M00_L03 và sensing sau này |
| INT-08 | Intake | Tỷ số cơ khí | Chưa xác lập | tỷ số | UNKNOWN | No applicable evidence identified | Đếm tầng, răng và pulley | Mô hình tốc độ/mô-men sai | Tổng tỷ số chưa biết | M00_L03 |
| INT-09 | Intake | Chuyển đổi đơn vị | Chưa xác lập | đơn vị cơ cấu/đơn vị motor | UNKNOWN | No applicable evidence identified | Suy ra từ hình học và tỷ số đã xác minh | Telemetry hoặc điều khiển sai | Chưa thể suy ra trước tỷ số | M00_L03 và điều khiển sau này |
| INT-10 | Intake | Chiều dương | Chưa xác lập | quy ước dấu | UNKNOWN | No applicable evidence identified | Định nghĩa rồi thử vật lý năng lượng thấp | Chuyển động intake/eject bất ngờ | Semantics chiều chưa biết | M00_L03 |
| INT-11 | Intake | Motor inversion | Chưa xác lập | boolean | UNKNOWN | No applicable evidence identified | Thử output thấp theo chiều dương | Chuyển động bất ngờ/đối nghịch | Phụ thuộc dây và cách lắp | M00_L03 |
| INT-12 | Intake | Chiều/phase sensor | Chưa xác lập | dấu/phase | UNKNOWN | No applicable evidence identified | So phép đo với chuyển động tay/năng lượng thấp | Feedback mất ổn định hoặc trạng thái sai | Sensor chưa biết | Sensing/điều khiển sau này |
| INT-13 | Intake | Neutral behavior | Chưa xác lập | brake/coast | UNKNOWN | No applicable evidence identified | Rà yêu cầu và thử stop có giới hạn | Trôi tiếp hoặc dừng gắt | Mode yêu cầu chưa biết | M00_L03 và an toàn |
| INT-14 | Intake | Giới hạn dòng/output | Chưa xác lập | A / V / phần trăm | UNKNOWN | No applicable evidence identified | Dùng bằng chứng thiết bị/cơ cấu rồi thử giới hạn | Hỏng điện, nhiệt, cơ khí | Không giá trị nào được cho phép | M00_L03 và an toàn |
| INT-15 | Intake | Giới hạn hành trình vật lý nếu áp dụng | Chưa xác lập | đơn vị cơ cấu | UNKNOWN | No applicable evidence identified | Kiểm tra hình học và giao thoa | Va chạm, kẹp hoặc hỏng | Chưa xác lập tính áp dụng | M00_L03 và an toàn |
| INT-16 | Intake | Tương thích firmware/library | Chưa xác lập | phiên bản | UNKNOWN | No applicable evidence identified | Khớp thiết bị, firmware, vendordep, WPILib chính xác | Hành vi API thiếu/không an toàn | Stack chính xác chưa biết | M00_L03 |
| INT-17 | Intake | Khả năng configuration readback | Chưa xác lập | field/status | UNKNOWN | No applicable evidence identified | Rà API chính xác và bench readback | Sai cấu hình bị che giấu | Thiết bị/API chưa biết | M00_L03 và commissioning |
| INT-18 | Intake | Hỗ trợ simulation | Chưa xác lập | phạm vi mô hình | UNKNOWN | No applicable evidence identified | Rà hỗ trợ library và model dự án | Tự tin sai do model thiếu | Dữ kiện vật lý chưa chứng minh | M00_L03 |
| INT-19 | Intake | Kỳ vọng safe-stop | Subsystem/IO safe stop sở hữu output an toàn (chỉ là kỳ vọng kiến trúc) | - | VERIFIED | AGENTS.md IO Contract; M00 ADR Mechanism Ownership | Rà kiến trúc, rồi triển khai/test tương lai | Ngăn output không an toàn còn giữ | Hành vi dừng vật lý chưa xác minh | M00_L03 và an toàn sau này |
| INT-20 | Intake | Phương pháp xác minh commissioning | Chưa xác lập | quy trình | UNKNOWN | No applicable evidence identified | Viết quy trình không cấp điện và năng lượng thấp được rà soát | Chuyển động đầu tiên mất kiểm soát | Cần phần cứng và hazard chính xác | M00_L03 |
| FED-01 | Feeder | Mục đích cơ cấu | Cơ cấu vận chuyển được sở hữu độc lập (chỉ là mục đích kiến trúc) | - | VERIFIED | M00 ADR, Locked 16-Lesson Roadmap, M00_L05 | Xác nhận câu chữ ADR khi rà soát | Tránh mơ hồ quyền sở hữu | Không chứng minh phần cứng tồn tại | M00_L05 |
| FED-02 | Feeder | Model motor/controller | Chưa xác lập | - | UNKNOWN | No applicable evidence identified | Kiểm tra nhãn lắp và hồ sơ | Sai API hoặc định mức | Chưa chọn candidate | M00_L05 |
| FED-03 | Feeder | CAN bus | Chưa xác lập | tên bus | UNKNOWN | No applicable evidence identified | Kiểm tra dây và device discovery | Mất hoặc xung đột thiết bị | Bus vật lý chưa biết | M00_L05 |
| FED-04 | Feeder | CAN ID | Chưa xác lập | số nguyên | UNKNOWN | No applicable evidence identified | Device discovery và đối chiếu nhãn | Trùng ID hoặc điều khiển sai | Không gán tại đây | M00_L05 |
| FED-05 | Feeder | Bố trí motor/follower | Chưa xác lập | số lượng/bố trí | UNKNOWN | No applicable evidence identified | Kiểm tra cụm lắp và sơ đồ điện | Motor có thể đối nghịch/quá tải | Chưa biết số lượng và follower mode | M00_L05 |
| FED-06 | Feeder | Loại sensor | Chưa xác lập | - | UNKNOWN | No applicable evidence identified | Kiểm tra cụm lắp và hồ sơ part | Thông tin staging sai | Chưa biết có sensor không | M00_L05 và phối hợp sau này |
| FED-07 | Feeder | Cách lắp sensor | Chưa xác lập | vị trí/hướng | UNKNOWN | No applicable evidence identified | Kiểm tra và đo hình học lắp | Phát hiện sai hoặc không tới được | Cách lắp chưa biết | M00_L05 và phối hợp sau này |
| FED-08 | Feeder | Tỷ số cơ khí | Chưa xác lập | tỷ số | UNKNOWN | No applicable evidence identified | Đếm tầng, răng và pulley | Tốc độ/mô-men vận chuyển sai | Tổng tỷ số chưa biết | M00_L05 |
| FED-09 | Feeder | Chuyển đổi đơn vị | Chưa xác lập | đơn vị cơ cấu/đơn vị motor | UNKNOWN | No applicable evidence identified | Suy ra từ hình học và tỷ số đã xác minh | Telemetry hoặc điều khiển sai | Chưa thể suy ra trước tỷ số | M00_L05 và điều khiển sau này |
| FED-10 | Feeder | Chiều dương | Chưa xác lập | quy ước dấu | UNKNOWN | No applicable evidence identified | Định nghĩa rồi thử vật lý năng lượng thấp | Feed sai hoặc không an toàn | Semantics chiều chưa biết | M00_L05 |
| FED-11 | Feeder | Motor inversion | Chưa xác lập | boolean | UNKNOWN | No applicable evidence identified | Thử output thấp theo chiều dương | Chuyển động bất ngờ/đối nghịch | Phụ thuộc dây và cách lắp | M00_L05 |
| FED-12 | Feeder | Chiều/phase sensor | Chưa xác lập | dấu/phase | UNKNOWN | No applicable evidence identified | So phép đo với chuyển động tay/năng lượng thấp | Feedback mất ổn định hoặc staging sai | Sensor chưa biết | Sensing/điều khiển sau này |
| FED-13 | Feeder | Neutral behavior | Chưa xác lập | brake/coast | UNKNOWN | No applicable evidence identified | Rà yêu cầu và thử stop có giới hạn | Game piece tiếp tục chạy | Mode yêu cầu chưa biết | M00_L05 và an toàn |
| FED-14 | Feeder | Giới hạn dòng/output | Chưa xác lập | A / V / phần trăm | UNKNOWN | No applicable evidence identified | Dùng bằng chứng thiết bị/cơ cấu rồi thử giới hạn | Hỏng do jam hoặc quá nhiệt | Không giá trị nào được cho phép | M00_L05 và an toàn |
| FED-15 | Feeder | Giới hạn hành trình vật lý nếu áp dụng | Chưa xác lập | đơn vị cơ cấu | UNKNOWN | No applicable evidence identified | Kiểm tra hình học và giao thoa | Va chạm, kẹp hoặc hỏng do jam | Chưa xác lập tính áp dụng | M00_L05 và an toàn |
| FED-16 | Feeder | Tương thích firmware/library | Chưa xác lập | phiên bản | UNKNOWN | No applicable evidence identified | Khớp thiết bị, firmware, vendordep, WPILib chính xác | Hành vi API thiếu/không an toàn | Stack chính xác chưa biết | M00_L05 |
| FED-17 | Feeder | Khả năng configuration readback | Chưa xác lập | field/status | UNKNOWN | No applicable evidence identified | Rà API chính xác và bench readback | Sai cấu hình bị che giấu | Thiết bị/API chưa biết | M00_L05 và commissioning |
| FED-18 | Feeder | Hỗ trợ simulation | Chưa xác lập | phạm vi mô hình | UNKNOWN | No applicable evidence identified | Rà hỗ trợ library và model dự án | Tự tin sai do model thiếu | Dữ kiện vật lý chưa chứng minh | M00_L05 |
| FED-19 | Feeder | Kỳ vọng safe-stop | Subsystem/IO safe stop sở hữu output an toàn (chỉ là kỳ vọng kiến trúc) | - | VERIFIED | AGENTS.md IO Contract; M00 ADR Mechanism Ownership | Rà kiến trúc, rồi triển khai/test tương lai | Ngăn output feed còn giữ | Hành vi dừng vật lý chưa xác minh | M00_L05 và an toàn sau này |
| FED-20 | Feeder | Phương pháp xác minh commissioning | Chưa xác lập | quy trình | UNKNOWN | No applicable evidence identified | Viết quy trình không cấp điện và năng lượng thấp được rà soát | Vận chuyển đầu tiên mất kiểm soát | Cần phần cứng và hazard chính xác | M00_L05 |
| FLY-01 | Flywheel | Mục đích cơ cấu | Cơ cấu tốc độ quay được sở hữu độc lập (chỉ là mục đích kiến trúc) | - | VERIFIED | M00 ADR, Locked 16-Lesson Roadmap, M00_L07 | Xác nhận câu chữ ADR khi rà soát | Tránh mơ hồ quyền sở hữu | Không chứng minh phần cứng tồn tại | M00_L07 |
| FLY-02 | Flywheel | Model motor/controller | Chưa xác lập | - | UNKNOWN | No applicable evidence identified | Kiểm tra nhãn lắp và hồ sơ | Sai API hoặc định mức | Chưa chọn candidate | M00_L07 |
| FLY-03 | Flywheel | CAN bus | Chưa xác lập | tên bus | UNKNOWN | No applicable evidence identified | Kiểm tra dây và device discovery | Mất hoặc xung đột thiết bị | Bus vật lý chưa biết | M00_L07 |
| FLY-04 | Flywheel | CAN ID | Chưa xác lập | số nguyên | UNKNOWN | No applicable evidence identified | Device discovery và đối chiếu nhãn | Trùng ID hoặc điều khiển sai | Không gán tại đây | M00_L07 |
| FLY-05 | Flywheel | Bố trí motor/follower | Chưa xác lập | số lượng/bố trí | UNKNOWN | No applicable evidence identified | Kiểm tra cụm lắp và sơ đồ điện | Motor có thể đối nghịch ở năng lượng cao | Chưa biết số lượng và follower mode | M00_L07 |
| FLY-06 | Flywheel | Loại sensor | Chưa xác lập | - | UNKNOWN | No applicable evidence identified | Kiểm tra cụm lắp và hồ sơ part | Đo tốc độ không hợp lệ | Nguồn/sự hiện diện chưa biết | M00_L07-L09 |
| FLY-07 | Flywheel | Cách lắp sensor | Chưa xác lập | vị trí/hướng | UNKNOWN | No applicable evidence identified | Kiểm tra sensor và quan hệ trục đo | Tốc độ đo sai | Cách lắp chưa biết | M00_L07-L09 |
| FLY-08 | Flywheel | Tỷ số cơ khí | Chưa xác lập | tỷ số | UNKNOWN | No applicable evidence identified | Đếm tầng, răng và pulley | Quá tốc hoặc mô hình mô-men sai | Tổng tỷ số chưa biết | M00_L07-L08 |
| FLY-09 | Flywheel | Chuyển đổi đơn vị | Chưa xác lập | rpm hoặc rad/s trên đơn vị motor | UNKNOWN | No applicable evidence identified | Suy ra từ sensor và tỷ số đã xác minh | Setpoint tốc độ sai/không an toàn | Chưa thể suy ra trước tỷ số | M00_L07-L09 |
| FLY-10 | Flywheel | Chiều dương | Chưa xác lập | quy ước dấu | UNKNOWN | No applicable evidence identified | Định nghĩa rồi thử năng lượng thấp có che chắn | Hướng bắn bất ngờ | Semantics chiều chưa biết | M00_L07 |
| FLY-11 | Flywheel | Motor inversion | Chưa xác lập | boolean | UNKNOWN | No applicable evidence identified | Thử output thấp có che chắn | Bánh quay đối nghịch/chuyển động nguy hiểm | Phụ thuộc bố trí | M00_L07 |
| FLY-12 | Flywheel | Chiều/phase sensor | Chưa xác lập | dấu/phase | UNKNOWN | No applicable evidence identified | So dấu tốc độ với chuyển động có che chắn | Closed-loop mất ổn định | Nguồn đo chưa biết | M00_L08 |
| FLY-13 | Flywheel | Neutral behavior | Chưa xác lập | brake/coast | UNKNOWN | No applicable evidence identified | Rà yêu cầu và thử spin-down | Coast bất ngờ hoặc dừng gắt | Mode yêu cầu chưa biết | M00_L07 và an toàn |
| FLY-14 | Flywheel | Giới hạn dòng/output | Chưa xác lập | A / V / phần trăm | UNKNOWN | No applicable evidence identified | Dùng bằng chứng thiết bị/cơ cấu rồi thử có che chắn | Hỏng năng lượng cao, nhiệt, điện | Không giá trị nào được cho phép | M00_L07-L08 và an toàn |
| FLY-15 | Flywheel | Giới hạn hành trình vật lý nếu áp dụng | Chưa xác lập | đơn vị cơ cấu | UNKNOWN | No applicable evidence identified | Kiểm tra mọi hình học chuyển động và guard | Tiếp xúc hoặc projectile hazard | Chưa xác lập tính áp dụng | M00_L07 và an toàn |
| FLY-16 | Flywheel | Tương thích firmware/library | Chưa xác lập | phiên bản | UNKNOWN | No applicable evidence identified | Khớp thiết bị, firmware, vendordep, WPILib chính xác | Hành vi điều khiển/tín hiệu sai | Stack chính xác chưa biết | M00_L07-L08 |
| FLY-17 | Flywheel | Khả năng configuration readback | Chưa xác lập | field/status | UNKNOWN | No applicable evidence identified | Rà API chính xác và bench readback | Sai gain/limit bị che giấu | Thiết bị/API chưa biết | M00_L07-L08 và commissioning |
| FLY-18 | Flywheel | Hỗ trợ simulation | Chưa xác lập | phạm vi mô hình | UNKNOWN | No applicable evidence identified | Rà hỗ trợ library và model dự án | Tự tin readiness sai | Tốc độ vật lý chưa chứng minh | M00_L07-L09 |
| FLY-19 | Flywheel | Kỳ vọng safe-stop | Subsystem/IO safe stop sở hữu output an toàn (chỉ là kỳ vọng kiến trúc) | - | VERIFIED | AGENTS.md IO Contract; M00 ADR Mechanism Ownership | Rà kiến trúc, rồi triển khai/test tương lai | Ngăn output năng lượng cao còn giữ | Spin-down vật lý chưa xác minh | M00_L07 và an toàn sau này |
| FLY-20 | Flywheel | Phương pháp xác minh commissioning | Chưa xác lập | quy trình | UNKNOWN | No applicable evidence identified | Viết quy trình che chắn, exclusion-zone, năng lượng thấp | Hazard chuyển động đầu tiên năng lượng cao | Cần phần cứng và containment chính xác | M00_L07 |
| ELV-01 | Elevator | Mục đích cơ cấu | Cơ cấu vị trí sở hữu độc lập với ý nghĩa reference rõ (chỉ là mục đích kiến trúc) | - | VERIFIED | M00 ADR, Locked 16-Lesson Roadmap, M00_L10 | Xác nhận câu chữ ADR khi rà soát | Tránh mơ hồ ownership/reference | Không chứng minh phần cứng tồn tại | M00_L10 |
| ELV-02 | Elevator | Model motor/controller | Chưa xác lập | - | UNKNOWN | No applicable evidence identified | Kiểm tra nhãn lắp và hồ sơ | Sai API, tải hoặc định mức | Chưa chọn candidate | M00_L10 |
| ELV-03 | Elevator | CAN bus | Chưa xác lập | tên bus | UNKNOWN | No applicable evidence identified | Kiểm tra dây và device discovery | Mất hoặc xung đột thiết bị | Bus vật lý chưa biết | M00_L10 |
| ELV-04 | Elevator | CAN ID | Chưa xác lập | số nguyên | UNKNOWN | No applicable evidence identified | Device discovery và đối chiếu nhãn | Trùng ID hoặc điều khiển sai | Không gán tại đây | M00_L10 |
| ELV-05 | Elevator | Bố trí motor/follower | Chưa xác lập | số lượng/bố trí | UNKNOWN | No applicable evidence identified | Kiểm tra cụm lắp và sơ đồ điện | Motor có thể đối nghịch hoặc làm rơi tải | Chưa biết số lượng và follower mode | M00_L10 |
| ELV-06 | Elevator | Loại sensor | Chưa xác lập | - | UNKNOWN | No applicable evidence identified | Kiểm tra cụm lắp và hồ sơ part | Vị trí/reference không hợp lệ | Relative/absolute chưa biết | M00_L10-L12 |
| ELV-07 | Elevator | Cách lắp sensor | Chưa xác lập | vị trí/hướng | UNKNOWN | No applicable evidence identified | Kiểm tra và đo coupling sensor | Trượt hoặc sai vị trí | Mounting/coupling chưa biết | M00_L10-L12 |
| ELV-08 | Elevator | Tỷ số cơ khí | Chưa xác lập | tỷ số | UNKNOWN | No applicable evidence identified | Đếm tầng, răng, sprocket, drum | Sai lực, tốc độ hoặc vị trí | Tổng tỷ số chưa biết | M00_L10-L11 |
| ELV-09 | Elevator | Chuyển đổi đơn vị | Chưa xác lập | khoảng cách/đơn vị motor | UNKNOWN | No applicable evidence identified | Suy ra từ hình học và tỷ số đã xác minh | Sai vị trí hoặc giới hạn | Chưa thể suy ra trước hình học | M00_L10-L13 |
| ELV-10 | Elevator | Chiều dương | Chưa xác lập | quy ước dấu | UNKNOWN | No applicable evidence identified | Định nghĩa rồi thử năng lượng thấp có giữ | Nâng/hạ bất ngờ | Semantics chiều chưa biết | M00_L10 |
| ELV-11 | Elevator | Motor inversion | Chưa xác lập | boolean | UNKNOWN | No applicable evidence identified | Thử output thấp có giữ | Rơi tải hoặc motor đối nghịch | Phụ thuộc bố trí | M00_L10 |
| ELV-12 | Elevator | Chiều/phase sensor | Chưa xác lập | dấu/phase | UNKNOWN | No applicable evidence identified | So dấu vị trí với chuyển động có giữ | Feedback mất ổn định hoặc limit sai | Sensor chưa biết | M00_L10-L11 |
| ELV-13 | Elevator | Neutral behavior | Chưa xác lập | brake/coast | UNKNOWN | No applicable evidence identified | Rà giữ tải và thử disable có giới hạn | Chuyển động do trọng lực | Mode yêu cầu chưa biết | M00_L10 và an toàn |
| ELV-14 | Elevator | Giới hạn dòng/output | Chưa xác lập | A / V / phần trăm | UNKNOWN | No applicable evidence identified | Dùng bằng chứng tải/thiết bị rồi thử có giữ | Rơi, stall, nhiệt, hỏng kết cấu | Không giá trị nào được cho phép | M00_L10-L13 và an toàn |
| ELV-15 | Elevator | Giới hạn hành trình vật lý nếu áp dụng | Chưa xác lập | khoảng cách | UNKNOWN | No applicable evidence identified | Đo hard/soft limit và clearance | Va hard stop hoặc kẹp | Limit/reference chưa biết | M00_L10-L13 |
| ELV-16 | Elevator | Tương thích firmware/library | Chưa xác lập | phiên bản | UNKNOWN | No applicable evidence identified | Khớp thiết bị, firmware, vendordep, WPILib chính xác | Hành vi vị trí/điều khiển sai | Stack chính xác chưa biết | M00_L10-L11 |
| ELV-17 | Elevator | Khả năng configuration readback | Chưa xác lập | field/status | UNKNOWN | No applicable evidence identified | Rà API chính xác và bench readback | Sai limit/feedback bị che giấu | Thiết bị/API chưa biết | M00_L10-L13 và commissioning |
| ELV-18 | Elevator | Hỗ trợ simulation | Chưa xác lập | phạm vi mô hình | UNKNOWN | No applicable evidence identified | Rà hỗ trợ library và model dự án | Tự tin sai về gravity/limit | Tải vật lý chưa chứng minh | M00_L10-L13 |
| ELV-19 | Elevator | Kỳ vọng safe-stop | Subsystem/IO safe stop sở hữu output an toàn (chỉ là kỳ vọng kiến trúc) | - | VERIFIED | AGENTS.md IO Contract; M00 ADR Mechanism Ownership | Rà kiến trúc, rồi triển khai/test tương lai | Ngăn lệnh chuyển động còn giữ | Hành vi giữ tải chưa xác minh | M00_L10 và an toàn sau này |
| ELV-20 | Elevator | Phương pháp xác minh commissioning | Chưa xác lập | quy trình | UNKNOWN | No applicable evidence identified | Viết quy trình có giữ, đỡ tải, năng lượng thấp | Hazard rơi, kẹp, hard stop | Cần cơ khí và hazard chính xác | M00_L10 |

## 20. Lỗi Thường gặp

- Xem chủ sở hữu kiến trúc là bằng chứng phần cứng tồn tại.
- Điền model candidate hoặc lựa chọn phổ biến của đội vào “Giá trị Hiện tại.”
- Đánh dấu `VERIFIED` vì code có một constant.
- Dùng `PROVISIONAL` mà không có cơ sở hỗ trợ thật.
- Dùng `NOT APPLICABLE` chỉ vì thiếu bằng chứng.
- Xem simulation là bằng chứng dây, chiều, dòng, nhiệt độ hoặc giới hạn vật lý.
- Bỏ đơn vị, danh tính nguồn, giới hạn hoặc phương pháp xác minh.
- Che một unknown vì bài sau cần câu trả lời.
- Gộp Intake, Feeder và Flywheel thành `ShooterSubsystem` chưa được cho phép.
- Biến kiểm tra thành triển khai, commissioning hoặc chọn phần cứng cuối cùng.

## 21. Bài tập Thực hành

Sáu tình huống sau là giả định. Với mỗi tình huống, ghi: (a) trạng thái, (b) lý do, (c) bằng chứng cần để nâng trạng thái, (d) liên quan an toàn và (e) phụ thuộc bài học tương lai.

1. Một thành viên nói Intake có lẽ dùng giảm tốc 4:1, nhưng không có bản vẽ, số răng hoặc phép đo.
2. Hồ sơ mua ghi model controller, nhưng chưa ai đối chiếu với nhãn đã lắp.
3. Ảnh có ngày chụp rõ nhãn thiết bị Feeder và vị trí robot, và device discovery khớp cùng serial identity.
4. Simulation Flywheel đạt target RPM, nhưng motor thật, tỷ số, quán tính và tải đều chưa biết.
5. Thiết kế Elevator đã rà soát không có follower motor, và cơ cấu lắp khớp bản vẽ.
6. Một hàng sensor-phase được đề xuất là `NOT APPLICABLE`, nhưng loại sensor vẫn chưa biết.

Gợi ý lập luận: Tình huống 1 là `UNKNOWN`. Tình huống 2 có thể là `PROVISIONAL` nếu hồ sơ mua đủ áp dụng làm cơ sở thật và việc kiểm tra nhãn là giới hạn/hành động đã nêu. Tình huống 3 có thể `VERIFIED` cho tuyên bố identity có phạm vi. Tình huống 4 chỉ xác minh hành vi mô hình, không xác minh dữ kiện phần cứng thật. Tình huống 5 có thể xác minh bố trí một motor; dữ kiện con “follower configuration” chỉ có thể `NOT APPLICABLE` khi nêu lý do. Tình huống 6 giữ `UNKNOWN` vì tính áp dụng chưa được xác lập.

## 22. Câu hỏi Kiểm tra Kiến thức

1. `VERIFIED` khác `PROVISIONAL` như thế nào?
2. Nếu một giá trị chỉ là có khả năng, thường gặp hoặc thường được dùng mà không có bằng chứng áp dụng, nó phải nhận trạng thái nào?
3. `REAL HARDWARE DEFERRED` có ý nghĩa gì đối với M00_L02?
4. `REAL HARDWARE DEFERRED` có cho phép đánh dấu một dữ kiện vật lý là `VERIFIED` khi chưa có bằng chứng không?
5. Vì sao Simulation không thể xác minh dây vật lý, CAN identity, chiều, gearing, biên dòng hoặc giới hạn hành trình?
6. Vì sao M00_L02 không triển khai mã cơ cấu?
7. Vì sao `Constants.java` vẫn là thẩm quyền cấu hình mặc định?
8. Bằng chứng thu thập trong M00_L02 có cho phép thay đổi `Constants.java` không?
9. Vì sao quyền sở hữu kiến trúc không chứng minh phần cứng vật lý tồn tại?
10. Nêu cặp quyền sở hữu tương lai đã khóa cho Intake, Feeder, Flywheel và Elevator.
11. Nêu cấu trúc phối hợp bắn đã khóa và hai kiểu umbrella bị cấm.
12. Thiếu bằng chứng có biện minh cho `NOT APPLICABLE` không?
13. Thành phần nào giữ API hãng?
14. Bốn bài Foundation nào phụ thuộc trực tiếp phương pháp kiểm tra này?
15. Trạng thái cấp dữ kiện khác phân loại bằng chứng cấp khóa học thế nào?

## 23. Đáp án Kiểm tra Kiến thức

1. `VERIFIED` có bằng chứng chấp nhận được xác lập dữ kiện trong phạm vi đã nêu. `PROVISIONAL` có cơ sở thật, nhưng đồng thời có giới hạn được nêu tên và hành động xác minh chưa hoàn tất.
2. `UNKNOWN`. Khả năng, sự quen thuộc hoặc giá trị thường gặp không phải là xác minh và không được đoán để điền vào ma trận.
3. Việc xác minh phần cứng thật được hoãn; các tuyên bố vật lý vẫn bị giới hạn bởi bằng chứng hiện có và phải được xác minh sau này khi cần.
4. Không. Việc hoãn xác minh không bao giờ biến một tuyên bố vật lý không có bằng chứng thành dữ kiện đã xác minh.
5. Simulation chỉ xác minh hành vi mô hình đã nêu. Nó không thể xác lập thiết bị, dây, cách lắp, cấu hình, tải hoặc phản ứng vật lý của robot thật.
6. Khái niệm mới duy nhất của bài là phương pháp kiểm tra bằng chứng. Việc triển khai cơ cấu thuộc các bài học sau được cho phép riêng.
7. Frozen Backbone giao thẩm quyền cấu hình mặc định cho `Constants.java`; M00_L02 chỉ ghi nhận bằng chứng mà không thay đổi kiến trúc đó.
8. Không. Một bài học sau phải cho phép riêng và xác định phạm vi mọi thay đổi `Constants.java`, kể cả sau khi dữ kiện phần cứng trở thành `VERIFIED`.
9. Kiến trúc xác định trách nhiệm và ranh giới phụ thuộc. Nó không chứng minh motor, controller, sensor, dây, tỷ số hoặc vật thể vật lý khác tồn tại.
10. Intake dùng `IntakeSubsystem + IntakeIO`; Feeder dùng `FeederSubsystem + FeederIO`; Flywheel dùng `FlywheelSubsystem + FlywheelIO`; Elevator dùng `ElevatorSubsystem + ElevatorIO`.
11. Cấu trúc bắn vẫn là `FlywheelSubsystem + FeederSubsystem + ShootCommand requiring both`. Không có `ShooterSubsystem` và không có `ShooterIO`.
12. Không. Thiếu bằng chứng nghĩa là `UNKNOWN`; `NOT APPLICABLE` cần lý do chứng minh hạng mục không áp dụng.
13. Adapter IO cụ thể, phía sau interface IO không phụ thuộc hãng.
14. `M00_L03`, `M00_L05`, `M00_L07` và `M00_L10`.
15. Trạng thái cấp dữ kiện phân loại từng tuyên bố trong ma trận. Phân loại cấp khóa học báo miền xác minh nào được yêu cầu và hoàn tất cho bài học.

## 24. Phân loại Bằng chứng Cấp Khóa học

Không nhầm trạng thái ma trận với bằng chứng hoàn tất bài học.

| Miền bằng chứng khóa học | Phân loại M00_L02 | Ý nghĩa |
| --- | --- | --- |
| Bằng chứng lý thuyết/repository | `THEORY VERIFIED` | Bắt buộc cho bài tài liệu và phương pháp bằng chứng này. |
| Test mới tập trung | `NOT APPLICABLE` | Không triển khai test nào được cho phép hoặc yêu cầu. |
| Simulation | `NOT APPLICABLE` | Không phải gate hoàn tất cho khái niệm chỉ-tài-liệu này. |
| Driver Station / Glass | `NOT APPLICABLE` | Không có hành vi runtime mới. |
| Phần cứng thật | `REAL HARDWARE DEFERRED` | Dữ kiện vật lý vẫn bị giới hạn bởi bằng chứng hiện có và xác minh tương lai. |

`THEORY VERIFIED` không biến từng dữ kiện vật lý thành `VERIFIED`. `REAL HARDWARE DEFERRED` không cho phép bịa giá trị.

## 25. Điều Cần Ghi nhớ

- Ghi điều bằng chứng chứng minh, không ghi điều đội hy vọng là đúng.
- Dùng đúng một trạng thái cấp dữ kiện cho mỗi hàng.
- Giữ dữ kiện không được hỗ trợ là `UNKNOWN`; chỉ dùng `PROVISIONAL` khi có cơ sở, giới hạn và hành động.
- Chỉ dùng `NOT APPLICABLE` khi lý do xác lập rằng hạng mục không áp dụng.
- Tách dữ kiện kiến trúc khỏi dữ kiện phần cứng vật lý.
- Giữ Frozen Backbone và hoãn triển khai đến bài tương lai được cho phép.
- Làm bằng chứng chưa giải quyết trở nên nhìn thấy để công việc cơ cấu, closed-loop và an toàn tương lai bắt đầu trung thực.
