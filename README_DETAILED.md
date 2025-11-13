# 🏥 Hospital Design Patterns - Hệ Thống Quản Lý Bệnh Viện Với 12 Design Patterns

## 📋 Tóm Tắt Dự Án

Dự án này là một **ứng dụng quản lý bệnh viện** triển khai **12 Design Patterns** khác nhau từ Gang of Four. Mỗi pattern được áp dụng vào các tình huống thực tế trong bối cảnh bệnh viện (quản lý nhân sự, lịch hẹn, thanh toán, v.v.).

### 🎯 Mục Đích
- **Học tập**: Hiểu rõ cách hoạt động của từng design pattern
- **Thực hành**: Triển khai patterns vào ứng dụng thực tế
- **Tham khảo**: Dùng làm template cho các dự án khác

---

## 🚀 Công Nghệ Sử Dụng

| Công Nghệ | Phiên Bản | Vai Trò |
|-----------|---------|--------|
| Java | **21 LTS** (vừa nâng cấp) | Ngôn ngữ lập trình chính |
| Maven | 3.x | Build tool, dependency management |
| PostgreSQL | 12+ | Database |
| JUnit | 5.10.0 | Testing framework |
| Mockito | 5.5.0 | Mocking library |
| AssertJ | 3.24.2 | Assertion library |

---

## 📦 12 Design Patterns

### **Creational Patterns** (Tạo Object)

#### 1. 🔒 **Singleton Pattern** - Quản Lý Kết Nối Database
```
Mục đích: Đảm bảo chỉ một instance của DatabaseConnection
Ứng dụng: Kết nối database duy nhất cho toàn ứng dụng
Kỹ thuật: Double-checked locking, volatile keyword
Lợi ích: Tiết kiệm tài nguyên, thread-safe
```
📁 File: `DatabaseConnection.java`
🧪 Test: `SingletonTest.java`

#### 2. 🏭 **Factory Method Pattern** - Tạo Nhân Sự
```
Mục đích: Tạo Staff (Doctor, Nurse, Admin) mà không biết class cụ thể
Ứng dụng: Tạo các loại nhân sự khác nhau
Kỹ thuật: Abstract creator, concrete creators
Lợi ích: Decoupling, dễ thêm loại nhân sự mới
```
📁 File: `StaffCreator.java`, `DoctorCreator.java`, ...
🧪 Test: `FactoryTest.java`

#### 3. 🏭🏭 **Abstract Factory Pattern** - Tạo DAO Families
```
Mục đích: Tạo họ DAO (Standard, Optimized) tương thích với nhau
Ứng dụng: StandardDAOFactory vs OptimizedDAOFactory
Kỹ thuật: Factory producer, multiple factories
Lợi ích: Tương thích đảm bảo, dễ chuyển đổi family
```
📁 File: `DAOFactoryProducer.java`, `StandardDAOFactory.java`, ...
🧪 Test: `AbstractFactoryTest.java`

#### 4. 🏗️ **Builder Pattern** - Xây Dựng Bệnh Nhân
```
Mục đích: Xây dựng Patient từng bước một (fluent interface)
Ứng dụng: Tạo Patient với attributes tuỳ ý
Kỹ thuật: Method chaining, fluent interface
Lợi ích: Dễ đọc, linh hoạt, tránh constructor dài
```
📁 File: `PatientBuilder.java`, `StandardPatientBuilder.java`
🧪 Test: `BuilderTest.java`

---

### **Structural Patterns** (Kết Hợp Object)

#### 5. 🔌 **Adapter Pattern** - Chuyển Đổi Payment System
```
Mục đích: Chuyển đổi interface cũ (LegacyPaymentSystem) sang mới (PaymentSystem)
Ứng dụng: Tích hợp hệ thống thanh toán cũ
Kỹ thuật: Wrapper, type conversion, method mapping
Lợi ích: Không phá vỡ code cũ, tích hợp mượt mà
```
📁 File: `PaymentAdapter.java`, `LegacyPaymentSystem.java`
🧪 Test: `AdapterTest.java`

#### 6. ✨ **Decorator Pattern** - Tăng Cường Medical Record
```
Mục đích: Thêm tính năng vào MedicalRecord (mã hóa, ký, ghi log)
Ứng dụng: Kết hợp EncryptedMedicalRecord + SignedMedicalRecord + AuditLogMedicalRecord
Kỹ thuật: Wrapping, composition, nesting decorators
Lợi ích: Composable, tránh subclass explosion, runtime linh hoạt
```
📁 File: `MedicalRecordDecorator.java`, các decorator cụ thể
🧪 Test: `DecoratorTest.java`

#### 7. 📄 **Facade Pattern** - Giao Diện Đơn Giản
```
Mục đích: Cung cấp API đơn giản cho subsystem phức tạp
Ứng dụng: HospitalFacade che giấu PatientDAO, AppointmentDAO, BillingDAO
Kỹ thuật: Centralized API, delegation
Lợi ích: Dễ sử dụng, decoupling, centralized logic
```
📁 File: `HospitalFacade.java`
🧪 Test: `FacadeTest.java`

---

### **Behavioral Patterns** (Hành Vi Object)

#### 8. 📝 **Command Pattern** - Quản Lý Hành Động Appointment
```
Mục đích: Đóng gói hành động (request) thành object, hỗ trợ undo/redo
Ứng dụng: CreateAppointmentCommand, UpdateAppointmentCommand, CancelAppointmentCommand
Kỹ thuật: Command interface, invoker, history stack
Lợi ích: Undo/Redo, logging, macro commands, decoupling
```
📁 File: `Command.java`, `CommandInvoker.java`, các command cụ thể
🧪 Test: `CommandTest.java`

#### 9. 🔔 **Observer Pattern** - Thông Báo Tự Động
```
Mục đích: Thiết lập mối quan hệ one-to-many, thông báo tự động khi có thay đổi
Ứng dụng: PatientObserver, DoctorObserver, SMSNotificationObserver
Kỹ thuật: Subject-Observer, attach/detach, notify
Lợi ích: Loose coupling, event-driven, dynamic attachment
```
📁 File: `Observer.java`, `AppointmentObservable.java`, các observer cụ thể
🧪 Test: `ObserverTest.java`

#### 10. 🎭 **State Pattern** - Quản Lý Trạng Thái Appointment
```
Mục đích: Thay đổi hành vi theo trạng thái nội bộ (state machine)
Ứng dụng: ScheduledState → ConfirmedState → CompletedState
Kỹ thuật: State interface, context, state transitions
Lợi ích: Tránh if-else, quản lý state rõ ràng, encapsulation
```
📁 File: `AppointmentState.java`, `AppointmentContext.java`, các state cụ thể
🧪 Test: `StateTest.java`

#### 11. 🎯 **Strategy Pattern** - Chiến Lược Thanh Toán
```
Mục đích: Hoán đổi thuật toán (algorithm families) runtime
Ứng dụng: CashPaymentStrategy, CreditCardPaymentStrategy, InsurancePaymentStrategy
Kỹ thuật: Strategy interface, context, runtime switching
Lợi ích: Runtime linh hoạt, dễ test, tránh if-else
```
📁 File: `PaymentStrategy.java`, `PaymentProcessor.java`, các strategy cụ thể
🧪 Test: `StrategyTest.java`

#### 12. 📋 **Template Method Pattern** - Tạo Báo Cáo
```
Mục đích: Định sẵn skeleton của thuật toán, subclass điền chi tiết
Ứng dụng: PatientReportGenerator, AppointmentReportGenerator, BillingReportGenerator
Kỹ thuật: Abstract template method, hook methods
Lợi ích: Code reuse, kiểm soát flow, Hollywood principle
```
📁 File: `MedicalReport.java`, các report generator cụ thể
🧪 Test: `TemplateMethodTest.java`

---

## 📚 Tài Liệu & Hướng Dẫn

### Tài Liệu Chính
- **`PHAN_TICH_CHI_TIET.md`** ⭐⭐⭐ - Phân tích chi tiết 12 patterns với code examples
- **`DETAILED_EXPLANATION.md`** ⭐⭐ - Cơ chế hoạt động sâu hơn, trình tự thực thi chi tiết
- **`INDEX.md`** - Tài liệu tham khảo nhanh, hướng dẫn sử dụng

### Code Examples
- Tất cả code trong `src/main/java/com/hospital/patterns/`
- Test case trong `src/test/java/com/hospital/patterns/`
- Database schema trong `database/schema.sql`

---

## 🏃 Bắt Đầu Nhanh

### 1. Clone & Setup
```bash
git clone https://github.com/TAAgnes3110/DesignPatterns.git
cd HospitalDesignPatterns
```

### 2. Build
```bash
mvn clean compile
```

### 3. Chạy Test
```bash
# Chạy tất cả test
mvn test

# Chạy test của 1 pattern
mvn test -Dtest=SingletonTest
mvn test -Dtest=FactoryTest
mvn test -Dtest=BuilderTest
# ... v.v.
```

### 4. Xem Report
```bash
# Kết quả test sẽ ở: target/surefire-reports/
# Hoặc xem chi tiết output ở console
```

---

## 🧪 Test Coverage

```
Tất cả 12 pattern được test bằng:
✅ Unit Tests (JUnit 5)
✅ Mocking (Mockito)
✅ Assertions (AssertJ)

Test Results:
- Total Tests: 38
- Passed: 38 ✅
- Failed: 0
- Skipped: 0
```

Chạy lại test:
```bash
mvn clean test
```

---

## 📊 Ứng Dụng Thực Tế

### Singleton
```java
// Một database connection duy nhất cho toàn ứng dụng
DatabaseConnection db = DatabaseConnection.getInstance();
Connection conn = db.getConnection();
```

### Factory + Abstract Factory
```java
// Tạo nhân sự (linh hoạt)
StaffCreator creator = new DoctorCreator();
Staff doctor = creator.createStaff("Vân", "Anh", "Cardiology");

// Tạo DAO family (tương thích)
DAOFactory factory = DAOFactoryProducer.getFactory("optimized");
PatientDAO dao = (PatientDAO) factory.getDAOInstance("PatientDAO");
```

### Builder
```java
// Xây dựng patient linh hoạt
Patient patient = new StandardPatientBuilder()
    .setFirstName("Vân Anh")
    .setLastName("Trần")
    .setEmail("vananh@example.com")
    .build();
```

### Decorator
```java
// Thêm tính năng vào record
MedicalRecord record = new BasicMedicalRecord();
record = new EncryptedMedicalRecordDecorator(record, "key123");
record = new SignedMedicalRecordDecorator(record, "Dr. Smith");
record = new AuditLogMedicalRecordDecorator(record);
record.save();  // Tự động mã hóa + ký + ghi log
```

### Strategy
```java
// Chuyển đổi strategy thanh toán runtime
PaymentProcessor processor = new PaymentProcessor();

processor.setPaymentStrategy(new CashPaymentStrategy());
processor.processPayment(amount);  // Thanh toán tiền mặt

processor.setPaymentStrategy(new CreditCardPaymentStrategy("1234", "Name"));
processor.processPayment(amount);  // Thanh toán thẻ
```

### Observer + State
```java
// Thông báo tự động + quản lý state
AppointmentObservable observable = new AppointmentObservable();
observable.attach(new PatientObserver(101));
observable.attach(new DoctorObserver(201));
observable.setAppointmentStatus("Confirmed");  // Tất cả observer được thông báo
```

---

## 📈 Cấp Độ Khó Độc Lập

### Level 1: Beginner (Dễ)
- Singleton: Hiểu đơn giản là 1 instance duy nhất
- Factory Method: Tạo object mà không biết class cụ thể
- Builder: Xây dựng object từng bước

### Level 2: Intermediate (Trung Bình)
- Abstract Factory: Tạo họ object tương thích
- Adapter: Chuyển đổi interface không tương thích
- Decorator: Thêm tính năng bằng wrapping

### Level 3: Advanced (Khó)
- Facade: Simplify complex subsystem
- Command: Undo/Redo + history management
- Observer: Publish-subscribe pattern

### Level 4: Expert (Rất Khó)
- State: State machine + transitions
- Strategy: Runtime polymorphism + algorithm families
- Template Method: Hook methods + skeleton control

---

## 🔑 Khái Niệm Khoá

### SOLID Principles
- **S**ingle Responsibility: Mỗi class làm 1 việc
- **O**pen/Closed: Mở rộng, không sửa đổi
- **L**iskov Substitution: Subtype có thể thay thế parent
- **I**nterface Segregation: Interface cụ thể
- **D**ependency Inversion: Depend on abstractions

### Design Principles
- **DRY** (Don't Repeat Yourself)
- **YAGNI** (You Aren't Gonna Need It)
- **KISS** (Keep It Simple, Stupid)
- **Composition over Inheritance**
- **Program to Interface, not Implementation**

---

## 📖 Học Tập Đề Xuất

### Tuần 1-2: Creational Patterns
1. Đọc `PHAN_TICH_CHI_TIET.md` - Singleton, Factory, Abstract Factory
2. Chạy test từng cái
3. Sửa code để thêm pattern

### Tuần 3: Builder & Structural
1. Đọc `DETAILED_EXPLANATION.md` - Builder cơ chế chi tiết
2. Hiểu Adapter + Decorator
3. Chạy test: BuilderTest, AdapterTest, DecoratorTest

### Tuần 4: Behavioral Patterns
1. Đọc Command, Observer, State patterns
2. Hiểu state machine
3. Chạy test từng cái

### Tuần 5: Strategy & Template Method
1. So sánh If-Else vs Strategy
2. Hiểu Template Method hooks
3. Làm bài tập mở rộng

---

## 💡 Bài Tập Mở Rộng

### Dễ
1. Thêm `AdminStaff` creator nếu chưa có
2. Thêm `EmailPaymentStrategy`
3. Tạo `WarningState` cho Appointment

### Trung Bình
1. Kết hợp Builder + Factory cho Staff
2. Thêm Composite Pattern cho Report hierarchy
3. Implement Prototype Pattern cho DAO cloning

### Khó
1. Thêm Chain of Responsibility cho command validation
2. Implement Observer Pattern với weak references
3. Thêm Memento Pattern cho State save/restore

---

## 🔗 Liên Kết & Tham Khảo

- [Gang of Four Design Patterns](https://refactoring.guru/design-patterns/catalog)
- [Head First Design Patterns](https://www.oreilly.com/library/view/head-first-design/9780596007124/)
- [Java Design Patterns](https://java-design-patterns.com/)
- [Effective Java by Joshua Bloch](https://www.oreilly.com/library/view/effective-java-3rd/9780134685991/)

---

## 📝 Thông Tin Dự Án

| Thông Tin | Chi Tiết |
|-----------|---------|
| **Dự Án** | Hospital Design Patterns |
| **GitHub** | github.com/TAAgnes3110/DesignPatterns |
| **Branch** | main |
| **Java** | 21 LTS (vừa nâng cấp) |
| **Maven** | 3.x |
| **Tests** | 38/38 ✅ |
| **Cập nhật** | November 13, 2025 |
| **License** | MIT |

---

## 🤝 Đóng Góp

Nếu bạn muốn:
- Thêm pattern mới
- Cải thiện code examples
- Sửa lỗi trong tài liệu
- Thêm test case

Vui lòng tạo Pull Request!

---

## ❓ FAQ

**Q: Tôi nên học pattern nào trước?**
A: Bắt đầu từ Singleton → Factory → Builder → rồi đến các pattern khác

**Q: Pattern này áp dụng khi nào?**
A: Xem `PHAN_TICH_CHI_TIET.md` - phần "Lý Do Sử Dụng" cho mỗi pattern

**Q: Làm sao để chạy code examples?**
A: Xem file test trong `src/test/java` hoặc run `mvn test`

**Q: Có thể sử dụng code này cho dự án thực tế không?**
A: Có, nhưng cần thêm error handling, validation, security features

---

**Happy Learning! 🎓**

Nếu có câu hỏi, vui lòng mở issue trên GitHub hoặc tham khảo tài liệu chi tiết.

