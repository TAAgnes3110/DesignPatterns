# 🚀 Quick Start Guide - Hospital Design Patterns

## 📊 Tóm Tắt Nhanh

| Pattern | File | Ứng Dụng |
|---------|------|---------|
| **Singleton** | `DatabaseConnection.java` | 1 database connection |
| **Factory Method** | `StaffCreator.java` | Tạo Doctor/Nurse/Admin |
| **Abstract Factory** | `DAOFactoryProducer.java` | Standard vs Optimized DAO |
| **Builder** | `StandardPatientBuilder.java` | Xây dựng Patient |
| **Adapter** | `PaymentAdapter.java` | Legacy payment system |
| **Decorator** | `MedicalRecordDecorator.java` | Encrypted + Signed + AuditLog |
| **Facade** | `HospitalFacade.java` | API đơn giản |
| **Command** | `CommandInvoker.java` | Undo/Redo appointment |
| **Observer** | `AppointmentObservable.java` | Thông báo tự động |
| **State** | `AppointmentContext.java` | State machine |
| **Strategy** | `PaymentProcessor.java` | Cash/Card/Insurance |
| **Template Method** | `MedicalReport.java` | Report generation |

---

## 💻 Lệnh Hay Dùng

### Build & Test
```bash
# Build project
mvn clean compile

# Chạy tất cả test
mvn test

# Chạy test của 1 pattern
mvn test -Dtest=SingletonTest
mvn test -Dtest=FactoryTest
```

### Xem Test Results
```bash
# Xem tất cả report
open target/surefire-reports/  (MacOS)
start target\surefire-reports  (Windows)

# Xem XML report
cat target/surefire-reports/TEST-*.xml
```

---

## 🎯 Ví Dụ Sử Dụng Code

### 1️⃣ Singleton - Database Connection
```java
// Lần 1: Tạo instance
DatabaseConnection db = DatabaseConnection.getInstance();
Connection conn = db.getConnection();

// Lần 2: Trả về instance cùng
DatabaseConnection db2 = DatabaseConnection.getInstance();
// db == db2 ✅ (cùng instance)
```

### 2️⃣ Factory Method - Tạo Staff
```java
// Tạo doctor
StaffCreator doctorCreator = new DoctorCreator();
Staff doctor = doctorCreator.createStaff("Vân Anh", "Trần", "Cardiology");
System.out.println(doctor.getRole());  // "Doctor"

// Tạo nurse
StaffCreator nurseCreator = new NurseCreator();
Staff nurse = nurseCreator.createStaff("Minh Hương", "Trần", "ICU", "Day");
System.out.println(nurse.getRole());  // "Nurse"
```

### 3️⃣ Builder - Xây Dựng Patient
```java
Patient patient = new StandardPatientBuilder()
    .setFirstName("Vân Anh")
    .setLastName("Trần")
    .setEmail("vananh@example.com")
    .setAddress("Thái Bình")
    .build();

// Các trường không set sẽ là null
```

### 4️⃣ Decorator - Thêm Tính Năng
```java
// Tạo base record
MedicalRecord record = new BasicMedicalRecord();

// Thêm mã hóa
record = new EncryptedMedicalRecordDecorator(record, "key123");

// Thêm ký số
record = new SignedMedicalRecordDecorator(record, "Dr. Smith");

// Thêm ghi log
record = new AuditLogMedicalRecordDecorator(record);

// Sử dụng: tự động có 3 tính năng
record.save();
// Output:
// [AUDIT LOG] Record saved
// Saving signed record by: Dr. Smith
// Saving encrypted medical record with key: key123
// Saving basic medical record
```

### 5️⃣ Strategy - Thanh Toán Runtime
```java
PaymentProcessor processor = new PaymentProcessor();

// Cách 1: Thanh toán tiền mặt
processor.setPaymentStrategy(new CashPaymentStrategy());
processor.processPayment(new BigDecimal("100.00"));
// Output: Paid 100.00 in cash

// Cách 2: Đổi sang thẻ (runtime!)
processor.setPaymentStrategy(
    new CreditCardPaymentStrategy("1234", "Nguyễn Văn A")
);
processor.processPayment(new BigDecimal("200.00"));
// Output: Paid 200.00 with credit card 1234 by Nguyễn Văn A

// Cách 3: Đổi sang bảo hiểm
processor.setPaymentStrategy(
    new InsurancePaymentStrategy("Bảo Việt", "POL123")
);
processor.processPayment(new BigDecimal("300.00"));
// Output: Paid 300.00 via insurance Bảo Việt (Policy: POL123)
```

### 6️⃣ Observer - Thông Báo Tự Động
```java
AppointmentObservable observable = new AppointmentObservable();

// Đăng ký observer
observable.attach(new PatientObserver(101));
observable.attach(new DoctorObserver(201));
observable.attach(new SMSNotificationObserver("0987654321"));

// Thay đổi appointment → tất cả observer tự động được thông báo
observable.setAppointmentStatus("Confirmed");
// Output:
// Patient 101 notified: Appointment updated
// Doctor 201 notified: Appointment updated
// SMS sent to 0987654321: Appointment 1 updated

// Hủy đăng ký bệnh nhân
observable.detach(observer1);
```

### 7️⃣ State - State Machine
```java
AppointmentContext context = new AppointmentContext(appointment);
context.setState(new ScheduledState());

// Transition 1: Scheduled → Confirmed
context.request();
// Output: Appointment is scheduled

// Transition 2: Confirmed → Completed
context.request();
// Output: Appointment is confirmed

// Terminal state
context.request();
// Output: Appointment is completed
```

### 8️⃣ Command - Undo/Redo
```java
CommandInvoker invoker = new CommandInvoker();

// Execute command 1
invoker.setCommand(new CreateAppointmentCommand(dao, apt));
invoker.executeCommand();
// State: Database có appointment

// Execute command 2
invoker.setCommand(new UpdateAppointmentCommand(dao, apt));
invoker.executeCommand();
// State: Appointment status = Confirmed

// Undo command 2
invoker.undo();
// State: Appointment status = Scheduled

// Undo command 1
invoker.undo();
// State: Database không có appointment
```

---

## 🧪 Chạy Test

### Chạy từng pattern
```bash
mvn test -Dtest=SingletonTest           # ✅ Singleton
mvn test -Dtest=FactoryTest             # ✅ Factory Method
mvn test -Dtest=AbstractFactoryTest     # ✅ Abstract Factory
mvn test -Dtest=BuilderTest             # ✅ Builder
mvn test -Dtest=AdapterTest             # ✅ Adapter
mvn test -Dtest=DecoratorTest           # ✅ Decorator
mvn test -Dtest=FacadeTest              # ✅ Facade
mvn test -Dtest=CommandTest             # ✅ Command
mvn test -Dtest=ObserverTest            # ✅ Observer
mvn test -Dtest=StateTest               # ✅ State
mvn test -Dtest=StrategyTest            # ✅ Strategy
mvn test -Dtest=TemplateMethodTest      # ✅ Template Method
```

### Chạy tất cả
```bash
mvn clean test -DfailIfNoTests=false
```

### Xem coverage
```bash
mvn test jacoco:report
# Report sẽ ở: target/site/jacoco/index.html
```

---

## 📁 Cấu Trúc Thư Mục

```
src/main/java/com/hospital/patterns/
├── Singleton/
│   └── DatabaseConnection.java (⭐⭐⭐ Bắt đầu từ đây)
├── Factory/
│   └── DoctorCreator.java (⭐⭐ Pattern cơ bản)
├── AbstractFactory/
│   └── DAOFactoryProducer.java (⭐⭐⭐ Nâng cao từ Factory)
├── Builder/
│   └── StandardPatientBuilder.java (⭐⭐)
├── Adapter/
│   └── PaymentAdapter.java (⭐⭐⭐ Structural pattern)
├── Decorator/
│   └── MedicalRecordDecorator.java (⭐⭐⭐⭐ Nâng cao)
├── Facade/
│   └── HospitalFacade.java (⭐⭐)
├── Command/
│   └── CommandInvoker.java (⭐⭐⭐ Behavioral pattern)
├── Observer/
│   └── AppointmentObservable.java (⭐⭐⭐⭐ Publish-Subscribe)
├── State/
│   └── AppointmentContext.java (⭐⭐⭐⭐ State Machine)
├── Strategy/
│   └── PaymentProcessor.java (⭐⭐⭐ Algorithm families)
└── TemplateMethod/
    └── MedicalReport.java (⭐⭐⭐⭐ Hook methods)
```

---

## 🎯 Lộ Trình Học

### Ngày 1-2: Creational Patterns
```
Singleton → Factory Method → Abstract Factory → Builder
```

### Ngày 3: Structural Patterns
```
Adapter → Decorator → Facade
```

### Ngày 4-5: Behavioral Patterns
```
Command → Observer → State → Strategy → Template Method
```

---

## 🔍 So Sánh Pattern

### Tạo Object: Factory vs Builder
| Tiêu Chí | Factory | Builder |
|---------|---------|---------|
| Số bước | 1 bước | Nhiều bước |
| Tham số | Cụ thể | Optional |
| Dùng khi | Đơn giản | Phức tạp |

### Thêm Tính Năng: Inheritance vs Decorator
| Tiêu Chí | Inheritance | Decorator |
|---------|-------------|-----------|
| Số class | 2^n (explosion) | n + 1 |
| Runtime | Không | Có thể |
| Dùng khi | Ít loại | Nhiều tổ hợp |

### Chuyển Đổi: If-Else vs Strategy
| Tiêu Chí | If-Else | Strategy |
|---------|---------|---------|
| Thêm loại | Sửa code | Tạo class mới |
| Test | Khó | Dễ |
| Sạch sẽ | Không | Có |

---

## ⚠️ Lưu Ý

1. **Database**: Mẫu này chỉ là ví dụ, cần configure `database.properties` để chạy đầy đủ
2. **Test**: Tất cả test đã mock database, không cần DB thực tế để chạy
3. **Error Handling**: Code ví dụ đơn giản, dự án thực tế cần exception handling
4. **Logging**: Dùng System.out, thực tế nên dùng SLF4J

---

## 💡 Tips

### Hiểu Pattern
1. Đọc **Mục Đích**
2. Xem **Code Example**
3. Chạy **Test**
4. Sửa code, thêm functionality

### Debug
```bash
# Thêm logging chi tiết
mvn test -X

# Chạy test cụ thể với verbose
mvn test -Dtest=SingletonTest -e -X
```

### Extend
```java
// Thêm pattern mới cho Staff
public class SecurityCreator extends StaffCreator {
    public Staff createStaff(String firstName, String lastName, Object... params) {
        return new SecurityStaff(firstName, lastName, (String) params[0]);
    }
}
```

---

## 📖 Đọc Thêm

- `PHAN_TICH_CHI_TIET.md` - Chi tiết từng pattern
- `DETAILED_EXPLANATION.md` - Cơ chế hoạt động sâu
- `README_DETAILED.md` - Hướng dẫn chi tiết
- `INDEX.md` - Tài liệu tham khảo

---

## ✅ Checklist Học Tập

- [ ] Hiểu Singleton + double-checked locking
- [ ] Viết được Factory Method cho loại nhân sự mới
- [ ] Kết hợp 3+ decorator
- [ ] Thực hiện undo/redo command
- [ ] Triển khai observer pattern mới
- [ ] Tạo state transition phức tạp
- [ ] Thêm strategy thanh toán mới
- [ ] Viết report generator mới

---

**Chúc bạn học tập vui vẻ! 🚀**

Nếu có vấn đề, tham khảo tài liệu chi tiết hoặc chạy test để xem ví dụ.

