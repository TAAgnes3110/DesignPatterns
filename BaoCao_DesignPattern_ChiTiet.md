## Báo cáo Design Pattern Chi Tiết

Tài liệu này nhằm giải thích từng design pattern trong dự án `HospitalDesignPatterns`, đối chiếu với slide `BaigiangDactaPhanmem.pdf` và sơ đồ UML (tập tin `.drawio` hoặc ảnh xuất từ thư mục `uml/`). Mỗi phần gồm:

1. Mục đích pattern.
2. Ý nghĩa mũi tên/quan hệ trong sơ đồ UML.
3. Lớp source code tương ứng (đường dẫn).
4. Bảng CSDL mà pattern thao tác.
5. Cách mở rộng/thêm chức năng.

> Khi trình bày với giảng viên: mở slide lý thuyết → hiển thị sơ đồ UML → tham chiếu mục tương ứng bên dưới.

---

### Abstract Factory

- **Mục đích:** Tạo “gia đình” DAO (PatientDAO, AppointmentDAO, …) mà client có thể hoán đổi giữa bản chuẩn và bản tối ưu.
- **Giải thích sơ đồ UML (file `uml/AbstractFactory.drawio` / `image/AbstractFactory.png`):**
  - Tam giác rỗng từ `StandardDAOFactory`/`OptimizedDAOFactory` lên `DAOFactory`: quan hệ kế thừa/implements.
  - Mũi tên nét liền từ `DAOFactory` tới `PatientDAO`/`AppointmentDAO`: factory trả về instance của các DAO này (association).
  - Mũi tên nét đứt từ `DAOFactoryProducer` tới `DAOFactory`: dependency – producer tạo factory cụ thể.
- **Code chính:**
  - `AbstractFactory/DAOFactoryProducer.java`
  - `AbstractFactory/DAOFactory.java`
  - `AbstractFactory/StandardDAOFactory.java`, `OptimizedDAOFactory.java`
  - `AbstractFactory/PatientDAO.java`, `StandardPatientDAO.java`, `OptimizedPatientDAO.java`
  - `AbstractFactory/AppointmentDAO.java`, `StandardAppointmentDAO.java`, `OptimizedAppointmentDAO.java`
- **Bảng CSDL liên quan:** `Patients`, `Appointments` (và các bảng bổ sung nếu thêm DAO mới).
- **Cách thêm mới:** Ví dụ thêm quản lý phòng (`Rooms`)
  1. Tạo `RoomDAO` + `StandardRoomDAO` + `OptimizedRoomDAO`.
  2. Cập nhật `StandardDAOFactory`/`OptimizedDAOFactory` để trả về `RoomDAO` khi `daoType="room"`.
  3. Không cần chỉnh client code vì đã gọi qua `DAOFactory`.

---

### Adapter

- **Mục đích:** Chuẩn hóa interface thanh toán mới (`PaymentSystem`) khi vẫn phải gọi hệ thống thanh toán cũ (`LegacyPaymentSystem`).
- **Giải thích sơ đồ UML (`uml/Adapter.drawio`):**
  - Tam giác rỗng: `PaymentAdapter` implements `PaymentSystem`.
  - Mũi tên nét liền từ `PaymentAdapter` tới `LegacyPaymentSystem`: association – adapter chứa tham chiếu đến hệ thống cũ.
  - Hình thoi rỗng (nếu có): thể hiện quan hệ aggregation/composition (adapter “sở hữu” legacy).
- **Code liên quan:**
  - `Adapter/PaymentSystem.java`
  - `Adapter/LegacyPaymentSystem.java`
  - `Adapter/PaymentAdapter.java`
- **Bảng CSDL:** `Billing` (ghi nhận kết quả thanh toán).
- **Mở rộng:** Khi đổi cổng thanh toán khác, viết adapter mới implement `PaymentSystem`, inject vào Facade mà không đổi luồng nghiệp vụ.

---

### Builder

- **Mục đích:** Tách quá trình xây dựng `Patient` thành các bước nhỏ, tạo được nhiều biến thể (nhập viện khẩn, đăng ký online,...).
- **Giải thích UML (`uml/Builder.drawio`):**
  - Tam giác rỗng: `StandardPatientBuilder` implements `PatientBuilder`.
  - Hình thoi rỗng từ `PatientDirector` tới `PatientBuilder`: director sử dụng builder (aggregation).
  - Mũi tên từ `PatientBuilder` tới `Patient`: builder tạo ra đối tượng `Patient`.
- **Code tương ứng:**
  - `Builder/PatientBuilder.java`
  - `Builder/StandardPatientBuilder.java`
  - `Builder/PatientDirector.java`
  - `AbstractFactory/Patient.java` (Product sử dụng chung)
- **Bảng liên quan:** `Patients`.
- **Mở rộng:** Thêm `EmergencyPatientBuilder` để bỏ qua một số bước, hoặc `ImportedPatientBuilder` đọc dữ liệu từ file CSV. Director không cần đổi.

---

### Command

- **Mục đích:** Đóng gói các thao tác lịch hẹn (create/update/cancel) thành đối tượng lệnh, dễ log/undo/queue.
- **Giải thích UML (`uml/Command.drawio`):**
  - Tam giác rỗng: `CreateAppointmentCommand`, `Update...`, `Cancel...` implements `Command`.
  - Association từ các command tới `AppointmentDAO` (receiver) – lệnh gọi DAO thực thi.
  - `CommandInvoker` chứa stack command (association + multiplicity).
- **Code chính:**
  - `Command/Command.java`
  - `Command/CreateAppointmentCommand.java`, `UpdateAppointmentCommand.java`, `CancelAppointmentCommand.java`
  - `Command/CommandInvoker.java`
- **Bảng liên quan:** `Appointments` (thao tác CRUD), có thể liên quan `Medical_Records`.
- **Mở rộng:** Thêm `RescheduleAppointmentCommand` hoặc `BulkCancelCommand`. Có thể ghi lịch sử lệnh để audit.

---

### Decorator

- **Mục đích:** Mở rộng chức năng hồ sơ y tế (mã hóa, ký số, audit) mà không đổi lớp cơ bản.
- **Giải thích UML (`uml/Decorator.drawio`):**
  - Tam giác rỗng: `EncryptedMedicalRecordDecorator`, `Signed...`, `AuditLog...` extends `MedicalRecordDecorator`.
  - `MedicalRecordDecorator` giữ reference `MedicalRecord` (hình thoi rỗng → composition).
  - Chuỗi Decorator: mũi tên thể hiện việc bọc lẫn nhau.
- **Code:**
  - `Decorator/MedicalRecord.java`
  - `Decorator/MedicalRecordDecorator.java`
  - `Decorator/BasicMedicalRecord.java`
  - `Decorator/EncryptedMedicalRecordDecorator.java`
  - `Decorator/SignedMedicalRecordDecorator.java`
  - `Decorator/AuditLogMedicalRecordDecorator.java` + `AuditLogService.java`
- **Bảng liên quan:** `Medical_Records`, `Medical_Records_Medicine` (nếu lưu thuốc).
- **Mở rộng:** Thêm decorator `PIIRedactionMedicalRecordDecorator` để che dữ liệu nhạy cảm; hoặc `CompressionDecorator` nếu muốn giảm dung lượng lưu trữ.

---

### Facade

- **Mục đích:** Cung cấp API nghiệp vụ đơn giản (đăng ký bệnh nhân, đặt lịch, thanh toán) thay vì để client gọi nhiều lớp con.
- **Giải thích UML (`uml/Facade.drawio`):**
  - `HospitalFacade` có association tới `PatientDAO`, `AppointmentDAO`, `BillingDAO`.
  - Facade sử dụng các DTO `Patient`, `Appointment`, `Billing` trong cùng package để trao đổi dữ liệu.
- **Code:**
  - `Facade/HospitalFacade.java`
  - `Facade/PatientDAO.java`, `AppointmentDAO.java`, `BillingDAO.java`
  - `Facade/Patient.java`, `Appointment.java`, `Billing.java`
- **Bảng liên quan:** `Patients`, `Appointments`, `Billing`.
- **Mở rộng:** Thêm phương thức `allocateRoom` để thao tác `Rooms`. Facade sẽ gọi `RoomDAO`, có thể phối hợp với Command/Observer để phát thông báo.

---

### Factory Method

- **Mục đích:** Tạo đúng loại `Staff` dựa trên role/department.
- **Giải thích UML (`uml/FactoryMethod.drawio`):**
  - Tam giác rỗng: `DoctorCreator`, `NurseCreator`, `AdminCreator` extends `StaffCreator`.
  - `StaffCreator.createStaff()` trả về subtype `DoctorStaff`, `NurseStaff`, `AdminStaff`.
- **Code:**
  - `Factory/StaffCreator.java`
  - `Factory/DoctorCreator.java`, `NurseCreator.java`, `AdminCreator.java`
  - `Factory/Staff.java`, `DoctorStaff.java`, `NurseStaff.java`, `AdminStaff.java`
- **Bảng liên quan:** `Staff`, `Nurses`, `Workers`, `Departments`.
- **Mở rộng:** Thêm `PharmacistCreator` + `PharmacistStaff` để hỗ trợ dược sĩ; update logic mapping department.

---

### Observer

- **Mục đích:** Thông báo tự động cho bệnh nhân/bác sĩ khi lịch hẹn thay đổi.
- **Giải thích UML (`uml/Observer.drawio`):**
  - `AppointmentObservable` implements `AppointmentSubject` (tam giác rỗng).
  - Subject giữ danh sách `Observer` (hình thoi rỗng thể hiện aggregation).
  - `PatientObserver`, `DoctorObserver`, `SMSNotificationObserver` implements `Observer`.
- **Code:**
  - `Observer/AppointmentSubject.java`, `Observer.java`
  - `Observer/AppointmentObservable.java`
  - `Observer/PatientObserver.java`, `DoctorObserver.java`, `SMSNotificationObserver.java`
- **Bảng liên quan:** `Appointments` (nguồn sự kiện), có thể thêm bảng log thông báo.
- **Mở rộng:** Thêm `EmailNotificationObserver`, `WebhookObserver`. Có thể dùng chung với State để trigger theo trạng thái.

---

### Singleton

- **Mục đích:** Đảm bảo toàn bộ ứng dụng dùng chung cấu hình/kết nối database.
- **Giải thích UML (`uml/Singleton.drawio`):**
  - Biểu tượng static (~) cho thấy phương thức `getInstance()` là static.
  - Không có tam giác vì không kế thừa; singleton là class duy nhất.
- **Code:**
  - `Singleton/DatabaseConnection.java`
  - `src/main/resources/database.properties`
- **Bảng liên quan:** Tất cả (DAO gọi qua singleton).
- **Mở rộng:** Nếu chuyển sang connection pool, vẫn bọc trong Singleton để API dao không đổi.

---

### State

- **Mục đích:** Quản lý vòng đời lịch hẹn (`Scheduled`, `Confirmed`, `Completed`, `Cancelled`) bằng các lớp trạng thái riêng.
- **Giải thích UML (`uml/State.drawio`):**
  - Tam giác rỗng: `ScheduledState`, `ConfirmedState`, `CompletedState`, `CancelledState` implements `AppointmentState`.
  - `AppointmentContext` có association tới `AppointmentState` (hình thoi – composition).
  - Mũi tên chuyển state thể hiện hành vi `handle()` cập nhật context.
- **Code:**
  - `State/AppointmentState.java`
  - `State/AppointmentContext.java`
  - `State/ScheduledState.java`, `ConfirmedState.java`, `CompletedState.java`, `CancelledState.java`
- **Bảng liên quan:** `Appointments` (cột `status`).
- **Mở rộng:** Thêm `NoShowState` cho trường hợp bệnh nhân vắng mặt; map sang giá trị mới trong DB.

---

### Strategy

- **Mục đích:** Tách thuật toán thanh toán theo phương thức (tiền mặt, thẻ, bảo hiểm).
- **Giải thích UML (`uml/Strategy.drawio`):**
  - Tam giác rỗng: các strategy cụ thể implements `PaymentStrategy`.
  - `PaymentProcessor` giữ tham chiếu `PaymentStrategy` (aggregation).
- **Code:**
  - `Strategy/PaymentStrategy.java`
  - `Strategy/CashPaymentStrategy.java`, `CreditCardPaymentStrategy.java`, `InsurancePaymentStrategy.java`
  - `Strategy/PaymentProcessor.java`
  - `Strategy/Billing.java` (ví dụ minh họa)
- **Bảng liên quan:** `Billing` (thay đổi `total_amount`, `payment_status` tùy strategy).
- **Mở rộng:** Thêm `VoucherPaymentStrategy`, `SplitPaymentStrategy`, v.v.

---

### Template Method

- **Mục đích:** Định nghĩa skeleton chung cho việc tạo báo cáo y tế; các subclass tùy biến bước thu thập và định dạng dữ liệu.
- **Giải thích UML (`uml/TemplateMethod.drawio`):**
  - Tam giác rỗng: `PatientReportGenerator`, `AppointmentReportGenerator`, `BillingReportGenerator` extends `MedicalReport`.
  - Template method `generateReport()` gọi các hook `collectData()`, `formatReport()`, `validateReport()`.
- **Code:**
  - `TemplateMethod/MedicalReport.java`
  - `TemplateMethod/PatientReportGenerator.java`, `AppointmentReportGenerator.java`, `BillingReportGenerator.java`
- **Bảng liên quan:** `Patients`, `Appointments`, `Billing` (tùy report).
- **Mở rộng:** Thêm `RoomReportGenerator` cho bảng `Rooms`; override hook để lấy dữ liệu phù hợp.

---

## Checklist thêm chức năng mới (ví dụ “Quản lý phòng”)

1. **Model & DAO:** tạo `Room` (model), `RoomDAO`, `StandardRoomDAO`, `OptimizedRoomDAO`.
2. **Abstract Factory:** cập nhật `StandardDAOFactory`/`OptimizedDAOFactory` trả về `RoomDAO`.
3. **Facade:** thêm `allocateRoom()`, `releaseRoom()` trong `HospitalFacade`, tiêm `RoomDAO`.
4. **Command:** tạo `AllocateRoomCommand`, `ReleaseRoomCommand` để log/undo.
5. **State:** xây dựng `RoomState` (`Available`, `Occupied`, `Maintenance`).
6. **Observer:** khi state đổi sang `Maintenance`, notify `CleaningServiceObserver`.
7. **Strategy:** nếu có chiến lược gán phòng (ưu tiên ICU, gần bác sĩ), tạo `RoomAssignmentStrategy`.
8. **Template Method:** xuất `RoomReportGenerator` thống kê sử dụng phòng.
9. **DAO thực thi:** dùng `Singleton/DatabaseConnection`, viết SQL tương ứng bảng `Rooms`, `Room_Types`, `Room_Assignments`.

---

## Xuất báo cáo sang Word

Sau khi chỉnh sửa file Markdown này (`BaoCao_DesignPattern_ChiTiet.md`), có thể chuyển sang Word bằng `pandoc`:

```bash
pandoc -s -o BaoCao_DesignPattern_ChiTiet.docx BaoCao_DesignPattern_ChiTiet.md
```

Nếu chưa cài `pandoc` trên Windows:

```powershell
winget install --id JohnMacFarlane.Pandoc -e --source winget
```

---

## Gợi ý trình bày với giảng viên

1. Chọn pattern cần giải thích → mở slide tương ứng trong PDF.
2. Bật sơ đồ UML (ảnh hoặc `.drawio`) → dùng phần “Giải thích sơ đồ” để mô tả mối quan hệ (mũi tên, tam giác, hình thoi).
3. Trỏ đến file code (đường dẫn đã liệt kê) → mô tả cách các lớp liên kết với bảng CSDL.
4. Kết bằng “Cách mở rộng” để chứng minh hiểu rõ cách phát triển thêm tính năng.


