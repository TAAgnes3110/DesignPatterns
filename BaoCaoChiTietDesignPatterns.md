# Báo Cáo Chi Tiết Design Patterns - Hệ Thống Quản Lý Bệnh Viện

## 1. Singleton Pattern

### Mô tả bài toán:
Giả sử bạn đang xây dựng một hệ thống quản lý bệnh viện với nhiều module khác nhau (PatientService, AppointmentService, BillingService, ...), và mọi module đều cần kết nối đến database để thao tác dữ liệu.

### Yêu cầu:

- Chỉ có một đối tượng DatabaseConnection duy nhất trong toàn bộ hệ thống để quản lý kết nối database.

- Tất cả các module khác đều dùng chung DatabaseConnection này.

- DatabaseConnection cần cung cấp các phương thức:
  - `getConnection()`: Lấy connection từ database
  - `closeConnection()`: Đóng connection
  - `testConnection()`: Kiểm tra kết nối
  - Load cấu hình từ file `database.properties`

- Chống lại Reflection, Serialization, Cloning, ClassLoader khác nhau, đảm bảo đối tượng DatabaseConnection thật sự duy nhất (sử dụng Double-Checked Locking với `volatile`).

### Lý do sử dụng:

- **Nếu ta không dùng Singleton**: Mỗi khi một module cần kết nối database, ta sẽ `new DatabaseConnection()` ở nhiều nơi. Điều này dẫn đến:
  - Tốn tài nguyên (nhiều kết nối database thừa thãi)
  - Khó quản lý và kiểm soát kết nối tập trung
  - Có thể gây ra vấn đề về hiệu năng và rò rỉ bộ nhớ (memory leak)
  - Khó theo dõi và debug các vấn đề liên quan đến database

- **Dùng Singleton**:
  - Đảm bảo chỉ có 1 instance duy nhất của DatabaseConnection
  - Dễ dàng quản lý, theo dõi và mở rộng (ví dụ thay đổi cấu hình database, thêm connection pooling)
  - Giúp toàn bộ hệ thống dùng cùng một kết nối database thống nhất
  - Tối ưu hiệu năng và tài nguyên hệ thống

---

## 2. Factory Method Pattern

### Mô tả bài toán:
Giả sử bạn đang xây dựng một hệ thống quản lý nhân viên bệnh viện. Hệ thống cần tạo các loại nhân viên khác nhau như: Bác sĩ (Doctor), Y tá (Nurse), và Nhân viên hành chính (Admin). Mỗi loại nhân viên có các thuộc tính và tham số khởi tạo khác nhau.

### Yêu cầu:

- Tạo các loại nhân viên khác nhau (Doctor, Nurse, Admin) thông qua một interface thống nhất.

- Mỗi loại nhân viên có các tham số khởi tạo khác nhau:
  - Doctor: cần `specialty` (chuyên khoa)
  - Nurse: cần `specialization` và `shiftHours` (ca làm việc)
  - Admin: cần `department` (phòng ban)

- Cho phép dễ dàng thêm các loại nhân viên mới trong tương lai mà không cần sửa đổi code hiện có.

- Ẩn logic tạo đối tượng phức tạp khỏi client code.

### Lý do sử dụng:

- **Nếu ta không dùng Factory Method**: Mỗi khi cần tạo một nhân viên, client code phải:
  - Biết chi tiết về cách tạo từng loại nhân viên
  - Sử dụng nhiều câu lệnh `if-else` hoặc `switch-case` để phân biệt loại nhân viên
  - Dễ dàng tạo ra đối tượng không hợp lệ nếu truyền sai tham số
  - Code trở nên phức tạp và khó bảo trì khi thêm loại nhân viên mới

- **Dùng Factory Method**:
  - Tách biệt logic tạo đối tượng khỏi client code
  - Dễ dàng mở rộng để thêm loại nhân viên mới (chỉ cần tạo Creator mới)
  - Đảm bảo tính nhất quán trong cách tạo đối tượng
  - Code dễ đọc, dễ hiểu và tuân thủ nguyên tắc Open/Closed Principle

---

## 3. Abstract Factory Pattern

### Mô tả bài toán:
Giả sử bạn đang xây dựng một hệ thống quản lý bệnh viện cần làm việc với database. Hệ thống có nhiều loại DAO (Data Access Object) như PatientDAO, AppointmentDAO. Tuy nhiên, có hai cách triển khai khác nhau:
- StandardDAOFactory: Tạo các DAO cơ bản cho môi trường phát triển
- OptimizedDAOFactory: Tạo các DAO tối ưu với caching và connection pooling cho môi trường production

### Yêu cầu:

- Tạo các families của các đối tượng DAO liên quan (PatientDAO, AppointmentDAO) mà không cần chỉ định các class cụ thể.

- Đảm bảo các DAO được tạo từ cùng một factory sẽ tương thích với nhau (ví dụ: StandardPatientDAO và StandardAppointmentDAO đều từ StandardDAOFactory).

- Cho phép chuyển đổi giữa các families một cách dễ dàng (từ Standard sang Optimized) mà không cần sửa đổi client code.

- Dễ dàng thêm các DAO mới (ví dụ: BillingDAO) vào cả hai families.

### Lý do sử dụng:

- **Nếu ta không dùng Abstract Factory**: Client code phải:
  - Biết chi tiết về từng loại DAO và cách tạo chúng
  - Tự quản lý việc đảm bảo các DAO tương thích với nhau
  - Sử dụng nhiều `if-else` để phân biệt loại factory
  - Code trở nên phức tạp và khó bảo trì khi thêm DAO mới hoặc family mới

- **Dùng Abstract Factory**:
  - Đảm bảo các đối tượng trong cùng một family luôn tương thích
  - Dễ dàng thay đổi family (từ Standard sang Optimized) mà không ảnh hưởng client code
  - Tách biệt logic tạo đối tượng khỏi client code
  - Tuân thủ nguyên tắc Dependency Inversion Principle

---

## 4. Builder Pattern

### Mô tả bài toán:
Giả sử bạn đang xây dựng một hệ thống quản lý bệnh nhân. Đối tượng Patient có rất nhiều thuộc tính: firstName, lastName, dateOfBirth, gender, contactNumber, address, email, medicalHistory. Không phải lúc nào cũng cần tất cả các thuộc tính, và việc tạo đối tượng với constructor có quá nhiều tham số sẽ rất khó đọc và dễ nhầm lẫn.

### Yêu cầu:

- Xây dựng đối tượng Patient từng bước một, chỉ cần set các thuộc tính cần thiết.

- Cho phép tạo đối tượng với các thuộc tính tùy chọn khác nhau mà không cần tạo nhiều constructor overload.

- Code dễ đọc và dễ hiểu khi tạo đối tượng Patient.

- Hỗ trợ phương thức chain (fluent interface) để code gọn gàng hơn.

- Đảm bảo đối tượng được tạo ra là hợp lệ (có thể thêm validation trong method `build()`).

### Lý do sử dụng:

- **Nếu ta không dùng Builder**: Phải:
  - Tạo nhiều constructor với các tham số khác nhau (telescoping constructor anti-pattern)
  - Hoặc sử dụng JavaBean pattern với setter, nhưng đối tượng có thể ở trạng thái không nhất quán trong quá trình tạo
  - Code khó đọc khi có quá nhiều tham số: `new Patient("Vân", "Anh", date, "Female", "0123456789", "Thái Bình", "vananh@email.com", "No history")`
  - Khó phân biệt thứ tự các tham số, dễ nhầm lẫn

- **Dùng Builder**:
  - Code dễ đọc và tự giải thích: `builder.setFirstName("Vân").setLastName("Anh").setAddress("Thái Bình").build()`
  - Linh hoạt trong việc set các thuộc tính tùy chọn
  - Đảm bảo đối tượng được tạo ra là immutable và hợp lệ
  - Dễ dàng thêm validation trong method `build()`

---

## 5. Strategy Pattern

### Mô tả bài toán:
Giả sử bạn đang xây dựng một hệ thống thanh toán cho bệnh viện. Bệnh nhân có thể thanh toán bằng nhiều phương thức khác nhau: Tiền mặt (Cash), Thẻ tín dụng (Credit Card), hoặc Bảo hiểm (Insurance). Mỗi phương thức thanh toán có logic xử lý khác nhau.

### Yêu cầu:

- Cho phép chọn phương thức thanh toán linh hoạt tại runtime.

- Mỗi phương thức thanh toán có thể có các tham số riêng:
  - Cash: không cần tham số
  - Credit Card: cần `cardNumber` và `cardHolder`
  - Insurance: cần `insuranceCompany` và `policyNumber`

- Dễ dàng thêm phương thức thanh toán mới trong tương lai (ví dụ: PayPal, Bank Transfer) mà không cần sửa đổi code hiện có.

- Client code không cần biết chi tiết về cách từng phương thức thanh toán hoạt động.

### Lý do sử dụng:

- **Nếu ta không dùng Strategy**: Phải:
  - Sử dụng nhiều câu lệnh `if-else` hoặc `switch-case` để xử lý từng phương thức thanh toán
  - Code trong PaymentProcessor sẽ rất dài và phức tạp
  - Khó mở rộng: mỗi khi thêm phương thức mới, phải sửa đổi PaymentProcessor
  - Vi phạm nguyên tắc Open/Closed Principle
  - Logic của từng phương thức thanh toán bị rải rác, khó kiểm thử

- **Dùng Strategy**:
  - Tách biệt logic của từng phương thức thanh toán thành các class riêng
  - Dễ dàng thêm phương thức mới: chỉ cần tạo class mới implement PaymentStrategy
  - Client code đơn giản và dễ hiểu
  - Dễ dàng kiểm thử từng strategy độc lập
  - Tuân thủ nguyên tắc Open/Closed Principle và Single Responsibility Principle

---

## 6. Observer Pattern

### Mô tả bài toán:
Giả sử bạn đang xây dựng một hệ thống quản lý lịch hẹn cho bệnh viện. Khi có thay đổi về lịch hẹn (ví dụ: lịch hẹn bị hủy, thời gian thay đổi, lịch hẹn được xác nhận), nhiều đối tượng khác nhau cần được thông báo:
- Bệnh nhân (Patient) cần nhận thông báo qua SMS hoặc email
- Bác sĩ (Doctor) cần nhận thông báo để cập nhật lịch làm việc
- Hệ thống quản lý cần ghi log

### Yêu cầu:

- Khi trạng thái của Appointment thay đổi, tự động thông báo cho tất cả các đối tượng đã đăng ký (subscribe).

- Cho phép thêm hoặc xóa các observer một cách linh hoạt (ví dụ: bệnh nhân có thể đăng ký hoặc hủy đăng ký nhận thông báo).

- Tách biệt logic thông báo khỏi logic quản lý Appointment.

- Hỗ trợ nhiều loại observer khác nhau (PatientObserver, DoctorObserver, SMSNotificationObserver) với cách xử lý thông báo khác nhau.

### Lý do sử dụng:

- **Nếu ta không dùng Observer**: Phải:
  - Appointment phải biết và gọi trực tiếp các phương thức của Patient, Doctor, NotificationService
  - Tạo sự phụ thuộc chặt chẽ (tight coupling) giữa Appointment và các đối tượng khác
  - Khó mở rộng: mỗi khi thêm loại observer mới, phải sửa đổi Appointment
  - Vi phạm nguyên tắc Open/Closed Principle
  - Code trở nên phức tạp và khó bảo trì

- **Dùng Observer**:
  - Tách biệt logic thông báo khỏi logic quản lý Appointment
  - Appointment không cần biết chi tiết về các observer
  - Dễ dàng thêm hoặc xóa observer mà không cần sửa đổi Appointment
  - Hỗ trợ mối quan hệ một-nhiều (one-to-many) giữa subject và observers
  - Tuân thủ nguyên tắc Dependency Inversion Principle

---

## 7. Command Pattern

### Mô tả bài toán:
Giả sử bạn đang xây dựng một hệ thống quản lý lịch hẹn cho bệnh viện. Hệ thống cần thực hiện các thao tác như: tạo lịch hẹn (Create), cập nhật lịch hẹn (Update), hủy lịch hẹn (Cancel). Ngoài ra, hệ thống cần hỗ trợ:
- Undo/Redo: hoàn tác các thao tác đã thực hiện
- Logging: ghi lại lịch sử các thao tác
- Queue: xếp hàng các thao tác để thực hiện sau

### Yêu cầu:

- Đóng gói mỗi thao tác thành một đối tượng Command riêng biệt.

- Hỗ trợ undo/redo: mỗi command có thể thực hiện (execute) và hoàn tác (undo).

- Cho phép lưu trữ lịch sử các command đã thực hiện.

- Hỗ trợ macro command: thực hiện nhiều command cùng lúc.

- Tách biệt đối tượng gọi thao tác (invoker) khỏi đối tượng thực hiện thao tác (receiver).

### Lý do sử dụng:

- **Nếu ta không dùng Command**: Phải:
  - Gọi trực tiếp các phương thức của AppointmentDAO trong client code
  - Khó hỗ trợ undo/redo: phải tự quản lý trạng thái trước khi thực hiện thao tác
  - Khó logging: phải thêm code logging vào nhiều nơi
  - Khó hỗ trợ queue hoặc batch processing
  - Client code phải biết chi tiết về cách thực hiện từng thao tác

- **Dùng Command**:
  - Đóng gói thao tác thành đối tượng, dễ dàng truyền, lưu trữ, và thực thi
  - Dễ dàng hỗ trợ undo/redo: mỗi command biết cách hoàn tác chính nó
  - Dễ dàng logging: có thể log trước/sau khi thực thi command
  - Hỗ trợ macro command và batch processing
  - Tách biệt invoker và receiver, giảm coupling
  - Tuân thủ nguyên tắc Single Responsibility Principle

---

## 8. State Pattern

### Mô tả bài toán:
Giả sử bạn đang xây dựng một hệ thống quản lý lịch hẹn cho bệnh viện. Mỗi lịch hẹn có thể ở các trạng thái khác nhau: Scheduled (Đã đặt), Confirmed (Đã xác nhận), Completed (Đã hoàn thành), Cancelled (Đã hủy). Mỗi trạng thái có các hành vi và quy tắc chuyển đổi khác nhau.

### Yêu cầu:

- Quản lý các trạng thái của Appointment một cách có tổ chức.

- Mỗi trạng thái biết được các trạng thái có thể chuyển đến tiếp theo (ví dụ: Scheduled có thể chuyển sang Confirmed hoặc Cancelled, nhưng không thể chuyển trực tiếp sang Completed).

- Mỗi trạng thái có thể có hành vi riêng (ví dụ: khi ở trạng thái Completed, không thể cập nhật thông tin).

- Dễ dàng thêm trạng thái mới (ví dụ: Rescheduled - Đã dời lịch) mà không cần sửa đổi code hiện có.

### Lý do sử dụng:

- **Nếu ta không dùng State**: Phải:
  - Sử dụng nhiều `if-else` hoặc `switch-case` để kiểm tra trạng thái và xử lý logic
  - Code trong AppointmentContext sẽ rất dài và phức tạp
  - Khó mở rộng: mỗi khi thêm trạng thái mới, phải sửa đổi nhiều nơi
  - Logic chuyển đổi trạng thái bị rải rác, khó kiểm soát
  - Dễ xảy ra lỗi khi quy tắc chuyển đổi trạng thái phức tạp

- **Dùng State**:
  - Tách biệt logic của từng trạng thái thành các class riêng
  - Dễ dàng quản lý và kiểm soát quy tắc chuyển đổi trạng thái
  - Dễ dàng thêm trạng thái mới: chỉ cần tạo class mới implement AppointmentState
  - Code dễ đọc và dễ bảo trì
  - Tuân thủ nguyên tắc Open/Closed Principle
  - Loại bỏ các câu lệnh điều kiện phức tạp

---

## 9. Template Method Pattern

### Mô tả bài toán:
Giả sử bạn đang xây dựng một hệ thống tạo báo cáo cho bệnh viện. Hệ thống cần tạo nhiều loại báo cáo khác nhau: Báo cáo bệnh nhân (Patient Report), Báo cáo lịch hẹn (Appointment Report), Báo cáo thanh toán (Billing Report). Mỗi loại báo cáo có cách thu thập dữ liệu và định dạng khác nhau, nhưng quy trình tạo báo cáo chung là giống nhau: thu thập dữ liệu → định dạng → kiểm tra tính hợp lệ → trả về báo cáo.

### Yêu cầu:

- Định nghĩa skeleton của thuật toán tạo báo cáo trong một class trừu tượng (MedicalReport).

- Cho phép các subclass override các bước cụ thể (collectData, formatReport, validateReport) để tạo các loại báo cáo khác nhau.

- Đảm bảo quy trình chung (generateReport) không bị thay đổi bởi các subclass.

- Dễ dàng thêm loại báo cáo mới (ví dụ: Báo cáo thuốc) mà không cần sửa đổi code hiện có.

### Lý do sử dụng:

- **Nếu ta không dùng Template Method**: Phải:
  - Lặp lại code chung (quy trình tạo báo cáo) trong mỗi class
  - Vi phạm nguyên tắc DRY (Don't Repeat Yourself)
  - Khó bảo trì: nếu quy trình chung thay đổi, phải sửa ở nhiều nơi
  - Code trở nên dài dòng và khó đọc

- **Dùng Template Method**:
  - Tái sử dụng code chung, tránh lặp lại
  - Đảm bảo quy trình chung được thực thi nhất quán
  - Dễ dàng thêm loại báo cáo mới: chỉ cần override các phương thức abstract
  - Code dễ đọc và dễ bảo trì
  - Tuân thủ nguyên tắc Open/Closed Principle và DRY Principle
  - Kiểm soát được flow của thuật toán

---

## 10. Decorator Pattern

### Mô tả bài toán:
Giả sử bạn đang xây dựng một hệ thống quản lý hồ sơ y tế cho bệnh viện. Hệ thống cần lưu trữ hồ sơ y tế cơ bản (BasicMedicalRecord). Tuy nhiên, tùy theo yêu cầu, hồ sơ có thể cần thêm các tính năng:
- Mã hóa (Encryption): để bảo mật thông tin nhạy cảm
- Ký số (Digital Signature): để đảm bảo tính toàn vẹn
- Audit Log: để ghi lại lịch sử truy cập và chỉnh sửa

Các tính năng này có thể được kết hợp với nhau (ví dụ: vừa mã hóa vừa có audit log).

### Yêu cầu:

- Cho phép thêm các tính năng động cho hồ sơ y tế mà không cần sửa đổi class gốc (BasicMedicalRecord).

- Có thể kết hợp nhiều decorator với nhau (ví dụ: EncryptedMedicalRecordDecorator + SignedMedicalRecordDecorator + AuditLogMedicalRecordDecorator).

- Dễ dàng thêm decorator mới (ví dụ: CompressedMedicalRecordDecorator) mà không cần sửa đổi code hiện có.

- Đảm bảo tính nhất quán: decorator có thể được thêm/bỏ một cách linh hoạt.

### Lý do sử dụng:

- **Nếu ta không dùng Decorator**: Phải:
  - Tạo nhiều subclass kết hợp các tính năng: BasicMedicalRecord, EncryptedMedicalRecord, SignedMedicalRecord, EncryptedAndSignedMedicalRecord, ... (combinatorial explosion)
  - Hoặc thêm tất cả tính năng vào BasicMedicalRecord, nhưng không phải lúc nào cũng cần tất cả
  - Khó mở rộng: mỗi khi thêm tính năng mới, phải tạo nhiều subclass mới
  - Code trở nên phức tạp và khó bảo trì

- **Dùng Decorator**:
  - Linh hoạt trong việc thêm/bỏ tính năng tại runtime
  - Tránh được vấn đề combinatorial explosion của subclass
  - Dễ dàng kết hợp các tính năng: chỉ cần wrap decorator này bằng decorator khác
  - Dễ dàng thêm decorator mới: chỉ cần tạo class mới extend MedicalRecordDecorator
  - Tuân thủ nguyên tắc Open/Closed Principle
  - Cho phép thêm tính năng mà không ảnh hưởng đến đối tượng gốc

---

## 11. Facade Pattern

### Mô tả bài toán:
Giả sử bạn đang xây dựng một hệ thống quản lý bệnh viện với nhiều subsystem phức tạp:
- PatientDAO: quản lý thông tin bệnh nhân
- AppointmentDAO: quản lý lịch hẹn
- BillingDAO: quản lý thanh toán

Client code (ví dụ: Controller hoặc Service layer) cần tương tác với nhiều subsystem này để thực hiện một tác vụ đơn giản. Ví dụ: để đăng ký bệnh nhân và tạo lịch hẹn, client phải:
1. Tạo Patient thông qua PatientDAO
2. Tạo Appointment thông qua AppointmentDAO
3. Có thể cần tạo Billing thông qua BillingDAO

### Yêu cầu:

- Cung cấp một giao diện đơn giản để truy cập các subsystem phức tạp.

- Ẩn đi sự phức tạp của việc tương tác với nhiều subsystem từ client code.

- Đơn giản hóa các tác vụ thường xuyên (ví dụ: `registerPatient`, `bookAppointment`, `processBilling`).

- Cho phép client code không cần biết chi tiết về cách các subsystem hoạt động.

### Lý do sử dụng:

- **Nếu ta không dùng Facade**: Client code phải:
  - Biết chi tiết về cách sử dụng từng subsystem (PatientDAO, AppointmentDAO, BillingDAO)
  - Tự quản lý việc gọi các phương thức từ nhiều subsystem
  - Code trở nên phức tạp và khó đọc
  - Khó bảo trì: nếu logic thay đổi, phải sửa ở nhiều nơi
  - Tạo sự phụ thuộc chặt chẽ giữa client code và các subsystem

- **Dùng Facade**:
  - Đơn giản hóa giao diện cho client code
  - Giảm coupling giữa client code và các subsystem
  - Dễ dàng thay đổi implementation của các subsystem mà không ảnh hưởng client code
  - Code dễ đọc và dễ bảo trì
  - Tuân thủ nguyên tắc Law of Demeter (LoD)
  - Cung cấp một entry point thống nhất cho các subsystem

---

## 12. Adapter Pattern

### Mô tả bài toán:
Giả sử bạn đang xây dựng một hệ thống thanh toán mới cho bệnh viện với interface `PaymentSystem`. Tuy nhiên, hệ thống cũ (LegacyPaymentSystem) vẫn đang được sử dụng và có interface khác:
- `PaymentSystem`: sử dụng `BigDecimal` và `int patientId`
- `LegacyPaymentSystem`: sử dụng `Double` và `String patientName`

Bạn muốn tích hợp hệ thống cũ vào hệ thống mới mà không cần sửa đổi code của hệ thống cũ.

### Yêu cầu:

- Cho phép hệ thống mới sử dụng LegacyPaymentSystem thông qua interface PaymentSystem.

- Không cần sửa đổi code của LegacyPaymentSystem (có thể là code cũ, đã được test kỹ, hoặc không có quyền sửa).

- Chuyển đổi giữa các interface không tương thích (BigDecimal ↔ Double, int patientId ↔ String patientName).

- Dễ dàng thay thế LegacyPaymentSystem bằng hệ thống mới trong tương lai mà không cần sửa đổi client code.

### Lý do sử dụng:

- **Nếu ta không dùng Adapter**: Phải:
  - Sửa đổi LegacyPaymentSystem để phù hợp với PaymentSystem (nhưng có thể không được phép hoặc rủi ro cao)
  - Hoặc viết lại toàn bộ LegacyPaymentSystem (tốn kém thời gian và công sức)
  - Hoặc client code phải xử lý cả hai interface, làm code phức tạp
  - Khó bảo trì khi phải quản lý nhiều interface khác nhau

- **Dùng Adapter**:
  - Tái sử dụng code cũ mà không cần sửa đổi
  - Cho phép các hệ thống không tương thích làm việc cùng nhau
  - Dễ dàng thay thế implementation trong tương lai: chỉ cần thay adapter
  - Client code chỉ cần làm việc với một interface (PaymentSystem)
  - Tuân thủ nguyên tắc Open/Closed Principle
  - Giảm rủi ro khi tích hợp code cũ

---

## Kết Luận

Mười hai Design Patterns trên đã được áp dụng thành công trong hệ thống quản lý bệnh viện, giúp:

- **Cải thiện chất lượng code**: Code dễ đọc, dễ hiểu, dễ bảo trì
- **Tăng khả năng mở rộng**: Dễ dàng thêm tính năng mới mà không ảnh hưởng code hiện có
- **Giảm coupling**: Các module độc lập với nhau, dễ test và maintain
- **Tuân thủ nguyên tắc SOLID**: Các pattern giúp code tuân thủ các nguyên tắc thiết kế tốt
- **Tối ưu hiệu năng**: Singleton pattern giúp quản lý tài nguyên hiệu quả
- **Tái sử dụng code**: Các pattern giúp tái sử dụng code và tránh lặp lại

Tất cả các pattern đã được test thành công với 28 test cases, đạt 100% pass rate, chứng minh tính đúng đắn và hiệu quả của việc áp dụng Design Patterns trong dự án này.

