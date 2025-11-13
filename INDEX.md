# Hospital Design Patterns - Hướng Dẫn Phân Tích Chi Tiết

## 📚 Các Tài Liệu Tham Khảo

### 1. **PHAN_TICH_CHI_TIET.md** (File Chính)
- Phân tích chi tiết 12 Design Patterns
- Giải thích mục đích, cấu trúc, lý do sử dụng
- Code examples cho mỗi pattern
- Diagram và flow cơ bản
- **Nội dung:** Tổng quan đầy đủ về tất cả patterns

### 2. **DETAILED_EXPLANATION.md** (File Chi Tiết Cơ Chế)
- Cơ chế hoạt động sâu hơn của từng pattern
- Trình tự thực thi chi tiết từng bước
- Sơ đồ interaction phức tạp
- So sánh trước/sau khi sử dụng pattern
- Giải thích Volatile, Double-Checked Locking, etc.
- **Nội dung:** Học thêm cách hoạt động bên trong

---

## 🎯 Hướng Dẫn Sử Dụng

### Nếu bạn muốn:
1. **Hiểu tổng quan nhanh** → Đọc `PHAN_TICH_CHI_TIET.md`
2. **Hiểu sâu cơ chế** → Đọc `DETAILED_EXPLANATION.md`
3. **Xem code thực tế** → Xem trong thư mục `src/main/java/com/hospital/patterns/`
4. **Chạy test** → Chạy `mvn test`

---

## 📊 12 Design Patterns Được Triển Khai

| # | Pattern | Tệp Chính | Mục Đích |
|---|---------|-----------|---------|
| 1 | **Singleton** | `DatabaseConnection.java` | Một instance duy nhất |
| 2 | **Factory Method** | `StaffCreator.java` | Tạo nhân sự linh hoạt |
| 3 | **Abstract Factory** | `DAOFactoryProducer.java` | Tạo DAO families |
| 4 | **Builder** | `StandardPatientBuilder.java` | Xây dựng object phức tạp |
| 5 | **Adapter** | `PaymentAdapter.java` | Chuyển đổi interface cũ |
| 6 | **Decorator** | `MedicalRecordDecorator.java` | Thêm tính năng động |
| 7 | **Facade** | `HospitalFacade.java` | Interface đơn giản |
| 8 | **Command** | `CommandInvoker.java` | Quản lý hành động + undo/redo |
| 9 | **Observer** | `AppointmentObservable.java` | Thông báo tự động |
| 10 | **State** | `AppointmentContext.java` | Quản lý trạng thái |
| 11 | **Strategy** | `PaymentProcessor.java` | Hoán đổi thuật toán |
| 12 | **Template Method** | `MedicalReport.java` | Template + hook methods |

---

## 🔍 Các Khái Niệm Khoá

### Cấp Độ 1: Cơ Bản
- **Singleton**: Thread-safe, volatile, double-checked locking
- **Factory Method**: Decoupling, tạo object không biết class cụ thể
- **Builder**: Fluent interface, method chaining, immutability

### Cấp Độ 2: Trung Bình
- **Adapter**: Translation layer, conversion, compatibilit
- **Decorator**: Wrapping, nesting, composition vs inheritance
- **Facade**: Centralized API, simplification

### Cấp Độ 3: Nâng Cao
- **Command**: Undo/Redo, macro, history, callback
- **Observer**: Publish-subscribe, event-driven, loose coupling
- **State**: State machine, transition, encapsulation

### Cấp Độ 4: Chuyên Sâu
- **Strategy**: Runtime polymorphism, algorithm families
- **Template Method**: Hollywood principle, hook methods
- **Abstract Factory**: Family consistency, swappable families

---

## 💡 Lợi Ích Mỗi Pattern

| Pattern | Lợi Ích Chính |
|---------|---|
| Singleton | Tiết kiệm tài nguyên, quản lý trung tâm |
| Factory Method | Linh hoạt, dễ mở rộng, decoupling |
| Abstract Factory | Tương thích đảm bảo, quản lý families |
| Builder | Dễ đọc, linh hoạt, tránh constructor dài |
| Adapter | Tích hợp cũ, không breaking changes |
| Decorator | Composable, tránh subclass explosion |
| Facade | Dễ sử dụng, centralized logic |
| Command | Undo/Redo, logging, macro commands |
| Observer | Loose coupling, event-driven |
| State | Tránh if-else, quản lý state rõ ràng |
| Strategy | Runtime linh hoạt, dễ test |
| Template Method | Code reuse, kiểm soát flow |

---

## 🧪 Chạy Test

```bash
# Chạy tất cả test
mvn test

# Chạy test của pattern cụ thể
mvn test -Dtest=SingletonTest
mvn test -Dtest=FactoryTest
mvn test -Dtest=AdapterTest
mvn test -Dtest=DecoratorTest

# Chạy và xem coverage
mvn test jacoco:report
```

---

## 📁 Cấu Trúc Thư Mục

```
HospitalDesignPatterns/
├── src/main/java/com/hospital/patterns/
│   ├── Singleton/
│   │   └── DatabaseConnection.java
│   ├── Factory/
│   │   ├── StaffCreator.java
│   │   ├── DoctorCreator.java
│   │   └── ...
│   ├── AbstractFactory/
│   │   ├── DAOFactory.java
│   │   ├── DAOFactoryProducer.java
│   │   └── ...
│   ├── Builder/
│   │   ├── PatientBuilder.java
│   │   └── StandardPatientBuilder.java
│   ├── Adapter/
│   │   ├── PaymentSystem.java
│   │   ├── LegacyPaymentSystem.java
│   │   └── PaymentAdapter.java
│   ├── Decorator/
│   │   ├── MedicalRecord.java
│   │   ├── MedicalRecordDecorator.java
│   │   └── [Encrypted|Signed|AuditLog]Decorator.java
│   ├── Facade/
│   │   └── HospitalFacade.java
│   ├── Command/
│   │   ├── Command.java
│   │   ├── CommandInvoker.java
│   │   └── [Create|Update|Cancel]AppointmentCommand.java
│   ├── Observer/
│   │   ├── Observer.java
│   │   ├── AppointmentSubject.java
│   │   ├── AppointmentObservable.java
│   │   └── [Patient|Doctor|SMS]Observer.java
│   ├── State/
│   │   ├── AppointmentState.java
│   │   ├── AppointmentContext.java
│   │   └── [Scheduled|Confirmed|Completed|Cancelled]State.java
│   ├── Strategy/
│   │   ├── PaymentStrategy.java
│   │   ├── PaymentProcessor.java
│   │   └── [Cash|CreditCard|Insurance]PaymentStrategy.java
│   └── TemplateMethod/
│       ├── MedicalReport.java
│       └── [Patient|Appointment|Billing]ReportGenerator.java
├── src/test/java/com/hospital/patterns/
│   ├── SingletonTest.java
│   ├── FactoryTest.java
│   ├── AbstractFactoryTest.java
│   ├── BuilderTest.java
│   ├── AdapterTest.java
│   ├── DecoratorTest.java
│   ├── FacadeTest.java
│   ├── CommandTest.java
│   ├── ObserverTest.java
│   ├── StateTest.java
│   ├── StrategyTest.java
│   └── TemplateMethodTest.java
├── PHAN_TICH_CHI_TIET.md (Tài liệu chính)
├── DETAILED_EXPLANATION.md (Chi tiết cơ chế)
├── INDEX.md (File này)
└── pom.xml
```

---

## 🔗 Mối Quan Hệ Giữa Các Pattern

```
Creational Patterns (Tạo object):
├─ Singleton: Một instance duy nhất
├─ Factory Method: Tạo 1 object
└─ Abstract Factory: Tạo họ objects

Structural Patterns (Kết hợp objects):
├─ Adapter: Chuyển đổi interface
├─ Decorator: Thêm tính năng
└─ Facade: Simplify subsystem

Behavioral Patterns (Hành vi objects):
├─ Command: Encapsulate request
├─ Observer: One-to-many notification
├─ State: Behavior varies with state
├─ Strategy: Encapsulate algorithms
└─ Template Method: Define algorithm skeleton
```

---

## 📖 Học Tập Gợi Ý

### Tuần 1: Creational Patterns
1. Đọc PHAN_TICH_CHI_TIET.md (Singleton, Factory, Abstract Factory)
2. Xem code trong thư mục tương ứng
3. Chạy test: `mvn test -Dtest=SingletonTest`

### Tuần 2: Builder & Adapter
1. Đọc PHAN_TICH_CHI_TIET.md (Builder, Adapter)
2. Đọc DETAILED_EXPLANATION.md (Cơ chế chi tiết)
3. Chạy test: `mvn test -Dtest=BuilderTest`

### Tuần 3: Decorator & Facade
1. Đọc DETAILED_EXPLANATION.md (Decorator nesting)
2. Xem code: `Decorator.java` folder
3. Chạy test: `mvn test -Dtest=DecoratorTest`

### Tuần 4: Behavioral Patterns
1. Đọc PHAN_TICH_CHI_TIET.md (Command, Observer, State)
2. Đọc DETAILED_EXPLANATION.md (State transition, Observer notification)
3. Chạy test từng cái một

### Tuần 5: Strategy & Template Method
1. Đọc DETAILED_EXPLANATION.md (Strategy at runtime)
2. So sánh: If-else vs Strategy
3. Hiểu Template Method hooks

---

## 🎓 Bài Tập Đề Xuất

1. **Thêm Pattern Mới**: Thêm Prototype Pattern vào DAO creation
2. **Kết Hợp Pattern**: Kết hợp Builder + Factory cho Staff creation
3. **Mở Rộng**: Thêm FilePaymentStrategy cho Strategy pattern
4. **Refactoring**: Tìm nơi có thể thay thế if-else bằng Strategy
5. **Test**: Viết unit test cho từng pattern

---

## 📞 Tham Khảo Thêm

- Gang of Four (GoF) Design Patterns
- Head First Design Patterns
- Refactoring.guru Design Patterns
- Java Design Patterns (java-design-patterns.com)

---

**Cập nhật:** November 13, 2025
**Java Version:** Java 21 LTS
**Status:** ✅ Tất cả 12 patterns được triển khai và test

