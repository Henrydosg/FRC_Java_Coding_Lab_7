# M00_L01 - Mechanism Architecture Reuse

- **Khóa học:** `FRC Java Coding Lab 7.0`
- **Bài học:** `M00_L01 - Mechanism Architecture Reuse`
- **Ngôn ngữ:** Tiếng Việt — bản giải thích thân thiện với học sinh, giữ nguyên ý nghĩa kỹ thuật của bản tiếng Anh

## 1. Vì sao có bài học này?

Trong các module trước, học sinh đã học kiến trúc thông qua những trách nhiệm thật trên robot. Swerve giúp chúng ta hiểu quyền sở hữu của subsystem và IO, đầu ra an toàn và cách quan sát trạng thái. Autonomous giúp chúng ta hiểu quyền sở hữu của command và scheduler. Vision giúp chúng ta hiểu cách cô lập thư viện vendor, tạo Observation bất biến, giữ telemetry chỉ-đọc và chuyển phép đo đến đúng chủ sở hữu của estimator.

M00 áp dụng lại những ý tưởng đó cho các cơ cấu thi đấu không thuộc drivetrain trong tương lai. Nguyên tắc quan trọng là:

> **CƠ CẤU MỚI KHÔNG CÓ NGHĨA LÀ KIẾN TRÚC MỚI.**

Một cơ cấu mới có mục đích vật lý mới, nhưng vẫn cần quyền sở hữu rõ ràng, IO độc lập với vendor, dữ liệu đo đầu vào, ý nghĩa bất biến, telemetry chỉ-đọc, command điều phối và khả năng dừng an toàn. Tái sử dụng kỷ luật này giúp giảm logic trùng lặp và làm cho lỗi dễ tìm hơn. Các bài sau cũng dễ hiểu hơn vì học sinh chỉ cần tập trung vào một hành vi mới thay vì học lại toàn bộ cấu trúc dự án.

Bài này chỉ dạy bản đồ kiến trúc. Bài này không tạo mã cơ cấu và không chọn phần cứng.

## 2. Mục tiêu học tập

Sau bài học này, học sinh có thể:

- xác định ranh giới sở hữu của subsystem;
- phân biệt Subsystem với IO;
- giải thích mục đích của IOInputs;
- giải thích vì sao Observation phải bất biến;
- giải thích vì sao telemetry chỉ-đọc;
- giải thích RobotContainer là Composition Root;
- giải thích quyền sở hữu của command và scheduler;
- xác định trách nhiệm dừng an toàn;
- giải thích việc cô lập vendor; và
- ánh xạ kiến trúc đã học sang các cơ cấu tương lai.

## 3. Những gì được tái sử dụng từ các module trước

M00 tái sử dụng các mẫu đã được thiết lập:

- **Composition Root:** RobotContainer tạo và kết nối các đối tượng.
- **Quyền sở hữu subsystem:** một subsystem sở hữu một năng lực cơ cấu và trạng thái lâu dài của nó.
- **Trừu tượng IO:** subsystem phụ thuộc vào một hợp đồng phần cứng độc lập với vendor.
- **IOInputs:** các dữ kiện đo từ phần cứng đi lên trong một snapshot truyền dữ liệu của một chu kỳ.
- **Adapter phần cứng cụ thể:** API vendor và truy cập thiết bị nằm tại biên phần cứng.
- **Observation bất biến:** ý nghĩa đã được diễn giải của cơ cấu có thể chia sẻ an toàn cho các bên đọc.
- **Telemetry:** công bố Observation mà không thay đổi hành vi robot.
- **Quyền sở hữu command:** command yêu cầu hoặc điều phối hành động qua API của subsystem.
- **Quyền sở hữu scheduler:** scheduler quản lý vòng đời và requirements của command.
- **Trách nhiệm dừng an toàn:** subsystem cung cấp đường dừng an toàn cho cơ cấu do nó sở hữu.

Tái sử dụng kiến trúc không phải là sao chép/dán mã nguồn. Chúng ta tái sử dụng trách nhiệm, chiều phụ thuộc và cách suy luận. Chúng ta không chép phép tính Swerve vào Intake, cũng không tự nghĩ ra hành vi cơ cấu trước bài học phù hợp.

```text
TÁI SỬ DỤNG KIẾN TRÚC = tái sử dụng mẫu quyền sở hữu
SAO CHÉP/DÁN MÃ NGUỒN = nhân bản một implementation, thường mang sai ý nghĩa
```

## 4. Kiến trúc cơ cấu cốt lõi

Quan hệ khởi tạo mang tính khái niệm là:

```text
RobotContainer
    |
    +--> MechanismSubsystem
            |
            +--> MechanismIO
                    |
                    +--> MechanismIOHardware
                            |
                            +--> Physical Hardware
```

Chiều quan sát là:

```text
Physical Hardware
        ↓
MechanismIOInputs
        ↓
MechanismSubsystem processing
        ↓
Immutable Observation
        ↓
Read-only Telemetry
```

- **RobotContainer** chọn implementation và nối các dependency.
- **MechanismSubsystem** sở hữu hành vi và trạng thái cơ cấu.
- **MechanismIO** chỉ mô tả những năng lực phần cứng mà subsystem thật sự cần.
- **MechanismIOHardware** là adapter cụ thể mang tính khái niệm, giao tiếp với API của thiết bị đã chọn.
- **Physical Hardware** là biên actuator hoặc sensor thật.
- **MechanismIOInputs** mang dữ kiện đo của một chu kỳ cập nhật.
- **Immutable Observation** chuyển dữ kiện đó thành ý nghĩa ổn định và độc lập với vendor.
- **Read-only Telemetry** công bố ý nghĩa để con người và công cụ quan sát.

Các tên trên mô tả một mẫu tổng quát. Bài này không yêu cầu tạo các class Java tổng quát mang những tên đó.

## 5. Luồng điều khiển và luồng quan sát

Luồng điều khiển yêu cầu hành động:

```text
Driver / Autonomous intent
        ↓
Command
        ↓
Subsystem
        ↓
IO
        ↓
Hardware
```

Luồng quan sát báo cáo điều robot biết:

```text
Hardware
    ↓
IOInputs
    ↓
Subsystem processing
    ↓
Immutable Observation
    ↓
Telemetry
```

Control yêu cầu robot làm một việc. Observation báo cáo điều cơ cấu hiện đang biết. Telemetry hiển thị hoặc ghi lại Observation; telemetry không bao giờ ra lệnh cho cơ cấu.

RobotContainer lắp ráp đồ thị đối tượng này. Nó không phải là một bước chạy bình thường trong bất kỳ luồng nào. Sau khi khởi tạo, command, subsystem, IO, Observation và telemetry thực hiện đúng trách nhiệm đã được phê duyệt của mình.

## 6. Quyền sở hữu subsystem

Hãy suy nghĩ theo nguyên tắc một chủ sở hữu: mỗi trách nhiệm lâu dài của cơ cấu phải có một chủ sở hữu rõ ràng.

Roadmap M00 cho phép các chủ sở hữu tương lai sau:

- `IntakeSubsystem` sở hữu trách nhiệm Intake.
- `FeederSubsystem` sở hữu trách nhiệm Feeder.
- `FlywheelSubsystem` sở hữu trách nhiệm Flywheel.
- `ElevatorSubsystem` sở hữu trách nhiệm Elevator.

Subsystem sở hữu trạng thái và hành vi của cơ cấu, đồng thời cung cấp một biên năng lực cấp cao cho command. Các package khác không được tạo bản sao thứ hai của trạng thái đó hoặc điều khiển cùng phần cứng một cách độc lập.

Bài này không định nghĩa method, field, thuật toán điều khiển hoặc hành vi thiết bị cuối cùng cho bất kỳ subsystem tương lai nào. Những quyết định đó thuộc các bài sau.

## 7. Vì sao không có ShooterSubsystem

Kiến trúc bắn được phê duyệt cho tương lai là:

```text
FlywheelSubsystem
+
FeederSubsystem
+
ShootCommand requiring both
```

`FlywheelSubsystem` sở hữu trạng thái và hành vi Flywheel. `FeederSubsystem` sở hữu trạng thái và hành vi Feeder. Một `ShootCommand` trong tương lai sẽ điều phối hai chủ sở hữu có sẵn cho một thao tác bắn và khai báo cả hai requirements để scheduler bảo vệ quyền sở hữu.

Điều phối không tạo ra một chủ sở hữu phần cứng mới. Một `ShooterSubsystem` bao quanh Flywheel và Feeder sẽ làm mờ subsystem nào sở hữu trạng thái, dừng an toàn và phần cứng của chúng. Tương tự, `ShooterIO` sẽ gom hai năng lực độc lập vào một biên phần cứng chưa được phê duyệt.

M00_L01 không triển khai `ShootCommand`, `ShooterSubsystem` hoặc `ShooterIO`.

## 8. IO và cô lập vendor

Chiều phụ thuộc phần cứng là:

```text
MechanismSubsystem
        ↓
vendor-neutral MechanismIO
        ↓
concrete hardware IO adapter
        ↓
vendor API
        ↓
physical device
```

API vendor chỉ được nằm trong adapter IO cụ thể. Command, Observation, telemetry và hợp đồng subsystem tổng quát không được phụ thuộc trực tiếp vào API của vendor motor controller hoặc sensor.

Sự cô lập này giúp subsystem tập trung vào năng lực của cơ cấu. Một implementation thiết bị khác có thể thay thế adapter mà không buộc command hoặc telemetry phải học thư viện vendor mới.

Bài này không chọn motor, motor controller, sensor, CAN ID, gear ratio, current limit, hằng số PID, model phần cứng, geometry hoặc control mode. Các lựa chọn đó cần bằng chứng và Design Lock ở bài sau.

`Constants.java` vẫn là nguồn cấu hình mặc định; M00_L01 không định nghĩa bất kỳ giá trị cấu hình cơ cấu nào.

## 9. IOInputs và dữ liệu đo

IOInputs chuyển các dữ kiện đo từ phần cứng lên trên trong một chu kỳ logic. Đây là dữ liệu truyền mutable do implementation IO cụ thể điền vào; nó không phải là ý nghĩa công khai của cơ cấu.

**CHỈ LÀ VÍ DỤ KHÁI NIỆM:**

- thiết bị có đang kết nối hay không;
- một vị trí đo được;
- một vận tốc đo được;
- một dòng điện đo được; hoặc
- trạng thái giới hạn đo được.

Các ví dụ này không phải hợp đồng IO cuối cùng của Intake, Feeder, Flywheel hoặc Elevator. M00_L01 không chọn field, đơn vị, sensor, quy tắc validity hoặc record type cho chúng.

Subsystem đọc một snapshot IOInputs nhất quán, chỉ diễn giải điều hợp đồng cơ cấu cần và tạo ra ý nghĩa ổn định cho bên sử dụng.

## 10. Observation bất biến

Mẫu đã học là:

```text
hardware fact
    ↓
IOInputs
    ↓
subsystem interpretation
    ↓
immutable Observation
```

Observation ổn định sau khi được tạo, độc lập với vendor và chỉ-đọc đối với bên sử dụng. Nó có thể biểu diễn ý nghĩa cơ cấu, đơn vị rõ ràng, validity và timing khi cần. Telemetry, logging, test và bên đọc cấp cao đã được phê duyệt có thể sử dụng nó mà không nhận một đối tượng truyền dữ liệu phần cứng mutable.

Tính bất biến ngăn một bên đọc âm thầm thay đổi trạng thái mà bên khác đang thấy. Tính độc lập với vendor ngăn chi tiết thư viện thiết bị rò rỉ lên các tầng trên.

Observation báo cáo ý nghĩa. Nó không ra lệnh phần cứng, schedule command, đọc thiết bị hoặc tự công bố chính nó.

## 11. Telemetry chỉ-đọc

Đường công bố là:

```text
Observation
    ↓
Telemetry
    ↓
NT4 / Glass / logging
```

Telemetry có thể quan sát và công bố trạng thái. Nó không được:

- ra lệnh cho motor;
- reset cơ cấu;
- thay đổi trạng thái subsystem;
- sở hữu phần cứng; hoặc
- đi vòng qua quyền sở hữu của subsystem và command.

> **TELEMETRY LÀ BÊN QUAN SÁT, KHÔNG PHẢI BỘ ĐIỀU KHIỂN.**

Nếu thông tin trên dashboard cho thấy cần thực hiện một hành động, command hoặc subsystem vẫn là nơi sở hữu hành động đó. Tầng telemetry luôn chỉ-đọc.

## 12. Dừng an toàn

Mỗi subsystem cơ cấu cuối cùng phải cung cấp một cách an toàn để dừng cơ cấu mà nó sở hữu.

```text
Command finishes / is interrupted
        ↓
Subsystem safe-stop responsibility
        ↓
IO
        ↓
Hardware-safe output
```

Command yêu cầu hoặc điều phối việc kết thúc hành động. Subsystem sở hữu trách nhiệm dừng an toàn ở cấp cơ cấu và chuyển yêu cầu đó qua IO. Cách này ngăn các bên gọi khác nhau tự nghĩ ra những hành vi dừng khác nhau.

M00_L01 không định nghĩa đầu ra dừng cụ thể cho cơ cấu. Bài này không chọn phần trăm, điện áp, dòng điện, trạng thái brake/coast hoặc control mode.

## 13. RobotContainer là Composition Root

RobotContainer tạo và nối:

- các IO implementation;
- các subsystem;
- các command;
- các binding; và
- các dependency của telemetry.

RobotContainer không được trở thành nơi chứa phép toán điều khiển cơ cấu, thuật toán cấu hình vendor, diễn giải sensor, state machine hoặc hành vi cơ cấu lâu dài.

```text
RobotContainer lắp ráp quyền sở hữu.
Nó không thay thế quyền sở hữu.
```

Chọn implementation real, simulation hoặc no-op là composition. Tính toán cơ cấu phải hoạt động như thế nào không phải là composition.

## 14. Ánh xạ từ Swerve sang cơ cấu

| Ý tưởng kiến trúc | Ví dụ Swerve | Tương đương ở cơ cấu tương lai |
| --- | --- | --- |
| Chủ sở hữu subsystem | `SwerveSubsystem` sở hữu năng lực và trạng thái drivetrain | Một subsystem Intake, Feeder, Flywheel hoặc Elevator được sở hữu độc lập |
| IO interface | Các hợp đồng IO của module Swerve và gyro | Một hợp đồng IO độc lập với vendor cho cơ cấu được sở hữu |
| Adapter IO cụ thể | Các adapter phần cứng Swerve/gyro hiện có | Một adapter phần cứng cơ cấu được chọn trong bài sau |
| IOInputs | Dữ liệu đo một chu kỳ của module và gyro | Dữ liệu đo một chu kỳ được định nghĩa trong bài cơ cấu tương ứng |
| Observation bất biến | `SwerveObservation` | Một Observation bất biến riêng cho cơ cấu tương lai |
| Dừng an toàn | Quyền dừng Swerve tập trung | Biên dừng an toàn thuộc subsystem của cơ cấu |
| Quyền sở hữu command | Command teleop/autonomous require Swerve | Command require subsystem mà nó yêu cầu hoặc điều phối |
| Telemetry | Telemetry Swerve công bố trạng thái bất biến | Telemetry facade cơ cấu công bố Observation của nó |
| Composition Root | RobotContainer chọn và nối dependency Swerve | RobotContainer chọn và nối dependency của cơ cấu tương lai |

So sánh này chỉ nói về kiến trúc. Các cơ cấu tương lai không kế thừa Swerve kinematics, odometry, pose estimation, module state hoặc phép toán drivetrain.

## 15. Bốn chủ sở hữu cơ cấu tương lai

```text
RobotContainer
 ├── IntakeSubsystem
 ├── FeederSubsystem
 ├── FlywheelSubsystem
 └── ElevatorSubsystem
```

Đây là các ranh giới sở hữu tương lai được roadmap M00 phê duyệt. Sau này RobotContainer sẽ compose chúng, nhưng mỗi subsystem vẫn chịu trách nhiệm cho năng lực riêng của mình.

M00_L01 không triển khai các class này, không mô tả phần cứng của chúng và không định nghĩa API chưa được các bài sau phê duyệt.

## 16. Các lỗi kiến trúc thường gặp

- **API vendor nằm trực tiếp trong Command:** command bị gắn chặt với phần cứng và đi vòng qua quyền sở hữu subsystem.
- **API vendor nằm trực tiếp trong RobotContainer:** Composition Root chứa hành vi phần cứng thay vì chỉ chọn và nối implementation.
- **Telemetry điều khiển phần cứng:** bên quan sát trở thành bộ điều khiển, khiến hành vi khó suy luận và kiểm thử.
- **Một subsystem khổng lồ cho mọi cơ cấu:** trạng thái, an toàn và requirements không liên quan bị trộn vào nhau.
- **ShooterSubsystem bao quanh Flywheel và Feeder:** hai chủ sở hữu độc lập bị che bởi một chủ sở hữu thứ ba không cần thiết.
- **Command sở hữu trạng thái phần cứng lâu dài:** vòng đời command là tạm thời; trạng thái lâu dài của cơ cấu thuộc subsystem.
- **Nhiều subsystem sở hữu cùng phần cứng:** hai chủ sở hữu có thể gửi yêu cầu xung đột và dừng không an toàn.
- **Nhân đôi trạng thái phần cứng ở nhiều tầng:** các bản sao có thể khác nhau và không còn source of truth rõ ràng.
- **Triển khai trước khi quyền sở hữu rõ ràng:** quyết định mã nguồn khóa một ranh giới rối trước khi trách nhiệm được review.
- **Bỏ qua IO vì truy cập trực tiếp trông dễ hơn:** sự tiện lợi ngắn hạn tạo coupling với vendor và phá vỡ biên thay thế/simulation.

## 17. Ranh giới một bài học - một khái niệm

M00_L01 không dạy:

- audit bằng chứng phần cứng;
- lựa chọn phần cứng;
- triển khai Intake;
- quyền sở hữu command Intake;
- triển khai Feeder;
- quyền sở hữu command Feeder;
- triển khai Flywheel;
- điều khiển vận tốc vòng kín Flywheel;
- ready-at-speed;
- nền tảng Elevator;
- điều khiển vị trí vòng kín Elevator;
- homing;
- an toàn giới hạn hành trình;
- điều phối bắn;
- điều phối Intake-to-Feeder; hoặc
- tích hợp autonomous event cho cơ cấu.

Các khái niệm đó thuộc M00_L02 đến M00_L16. Dạy sớm sẽ trộn nhiều trách nhiệm vào một bài và loại bỏ các cổng bằng chứng bảo vệ công việc sau này.

## 18. Phân loại bằng chứng

```text
THEORY VERIFIED

Simulation:
NOT APPLICABLE

Driver Station / Glass:
NOT APPLICABLE

Real Hardware:
NOT APPLICABLE
```

M00_L01 chỉ bổ sung hiểu biết kiến trúc và tài liệu. Bài này không thêm hành vi cơ cấu có thể thực thi. Vì vậy không có hành vi mới để mô phỏng, không có telemetry runtime mới để hiển thị và không có phần cứng mới để commissioning.

Không dùng `REAL HARDWARE DEFERRED` cho M00_L01 vì bài này không đưa ra tuyên bố nào về phần cứng.

## 19. Bài tập kiến trúc cho học sinh

Hãy tưởng tượng một cơ cấu tương lai giả định tên là **Panel Handler**. Hành vi chi tiết và phần cứng của nó được cố ý để chưa biết. Hãy tạo bản đồ sở hữu kiến trúc—không viết code—và xác định:

1. subsystem sở hữu;
2. IO interface độc lập với vendor;
3. adapter IO phần cứng cụ thể;
4. các dữ liệu đo IOInputs có thể có ở mức khái niệm;
5. Observation bất biến;
6. chủ sở hữu dừng an toàn;
7. command sở hữu hoặc điều phối;
8. bên telemetry sử dụng dữ liệu; và
9. trách nhiệm wiring của RobotContainer.

Không chọn motor, controller, sensor, CAN ID, gearing hoặc thuật toán điều khiển.

### Hướng dẫn tự kiểm tra

- Panel Handler có một subsystem sở hữu.
- Subsystem đó phụ thuộc vào IO interface độc lập với vendor.
- Adapter cụ thể chứa mọi API vendor trong tương lai.
- **Chỉ là ví dụ khái niệm:** trạng thái kết nối hoặc một giá trị chuyển động đo được có thể đi qua IOInputs, nhưng bài này không định nghĩa field cuối cùng.
- Subsystem diễn giải input và cung cấp Observation bất biến, độc lập với vendor.
- Subsystem sở hữu trách nhiệm dừng an toàn.
- Command yêu cầu hoặc điều phối hành vi và khai báo requirement của subsystem; command không sở hữu trạng thái cơ cấu lâu dài.
- Telemetry sử dụng Observation theo cách chỉ-đọc.
- RobotContainer tạo và kết nối các đối tượng này mà không triển khai hành vi Panel Handler.

## 20. Kiểm tra kiến thức

### Câu hỏi

1. Composition Root là gì?
2. Thành phần nào sở hữu trạng thái lâu dài của cơ cấu?
3. Vì sao chúng ta dùng IO interface?
4. API vendor được phép xuất hiện ở đâu?
5. IOInputs dùng để làm gì?
6. Vì sao Observation phải bất biến?
7. Telemetry có thể ra lệnh cho phần cứng không?
8. Thành phần nào sở hữu trách nhiệm dừng an toàn của cơ cấu?
9. Vì sao Intake, Feeder, Flywheel và Elevator là các chủ sở hữu riêng biệt?
10. Vì sao thao tác bắn dùng `FlywheelSubsystem + FeederSubsystem + ShootCommand` thay vì `ShooterSubsystem`?

### Câu trả lời

1. Đây là nơi các implementation được tạo, chọn và kết nối; trong dự án này RobotContainer đảm nhiệm vai trò đó.
2. Subsystem sở hữu năng lực cơ cấu.
3. IO tách hành vi cơ cấu khỏi truy cập phần cứng riêng của thiết bị và cho phép thay implementation.
4. Chỉ bên trong adapter IO cụ thể.
5. IOInputs mang dữ kiện đo phần cứng của một chu kỳ nhất quán từ IO lên subsystem.
6. Để các bên đọc nhận được ý nghĩa ổn định, an toàn, độc lập với vendor và không thể thay đổi nó.
7. Không. Telemetry chỉ quan sát và công bố.
8. Subsystem của cơ cấu; subsystem chuyển thao tác dừng qua biên IO của nó.
9. Vì chúng là các năng lực độc lập với trạng thái, hành vi, an toàn và scheduler requirements khác nhau.
10. Flywheel và Feeder vẫn là hai chủ sở hữu phần cứng; command chỉ điều phối chúng tạm thời mà không tạo chủ sở hữu thứ ba.

## 21. Những điểm cần nhớ

> **Cơ cấu mới, nhưng kỷ luật kiến trúc vẫn giữ nguyên.**

- Xác định quyền sở hữu trước tiên.
- Cô lập API vendor trong adapter IO cụ thể.
- Dùng IOInputs để truyền dữ kiện đo.
- Giữ Observation bất biến và độc lập với vendor.
- Giữ telemetry chỉ-đọc.
- Command yêu cầu hoặc điều phối hành vi.
- Subsystem sở hữu hành vi và trạng thái lâu dài.
- RobotContainer compose đồ thị đối tượng.
- Dừng an toàn thuộc trách nhiệm subsystem.
- Các bài sau thêm hành vi theo từng khái niệm đã được quản trị.
