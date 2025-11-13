# CHI TIẾT SÂU VỀ CƠ CHẾ HOẠT ĐỘNG DESIGN PATTERNS

## Observer Pattern - Cơ Chế Chi Tiết

### 🔍 Sơ Đồ Observer Pattern:

```
┌──────────────────────┐
│     Subject          │
│  (AppointmentObserv) │
├──────────────────────┤
│ - observers: List    │
│ - appointment        │
├──────────────────────┤
│ + attach()           │
│ + detach()           │
│ + notifyObservers()  │
└──────────────────────┘
           │
           │ maintains list of
           │
    ┌──────┼──────┬──────────────────┐
    │      │      │                  │
    ↓      ↓      ↓                  ↓
┌────────┐ ┌──────┐ ┌──────────┐ ┌──────┐
│Patient │ │Doctor│ │SMS       │ │...   │
│Obs     │ │Obs   │ │Obs       │ │Obs   │
└────────┘ └──────┘ └──────────┘ └──────┘
```

### Trình Tự Thực Thi Chi Tiết:

```
Step 1: Tạo Subject và Observers
--------------------------------
AppointmentObservable subject = new AppointmentObservable();
Observer patient = new PatientObserver(101);
Observer doctor = new DoctorObserver(201);
Observer sms = new SMSNotificationObserver("0123456789");

Step 2: Đăng ký Observers
--------------------------
subject.attach(patient);    // observers.add(patient)
subject.attach(doctor);     // observers = [PatientObserver, DoctorObserver]
subject.attach(sms);        // observers = [PatientObserver, DoctorObserver, SMSNotification]

Step 3: Thay đổi Subject
------------------------
subject.setAppointmentStatus("Confirmed")
    ├─ appointment.setStatus("Confirmed")
    └─ notifyObservers()
        ├─ for(Observer obs : observers) {
        │   obs.update(appointment);
        │ }
        ├─ observers[0].update() → PatientObserver.update()
        │   └─ print "Patient 101 notified: Appointment updated"
        ├─ observers[1].update() → DoctorObserver.update()
        │   └─ print "Doctor 201 notified: Appointment updated"
        └─ observers[2].update() → SMSNotificationObserver.update()
            └─ print "SMS sent to 0123456789: Appointment 1 updated"

Step 4: Hủy Đăng Ký
-------------------
subject.detach(patient);  // observers.remove(patient)
// observers = [DoctorObserver, SMSNotification]

subject.setAppointmentStatus("Completed")
    └─ notifyObservers()
        ├─ DoctorObserver.update()
        │   └─ print "Doctor 201 notified: Appointment updated"
        └─ SMSNotificationObserver.update()
            └─ print "SMS sent to 0123456789: Appointment 1 updated"
        (PatientObserver không được thông báo nữa)
```

---

## State Pattern - Cơ Chế Chi Tiết

### 🔍 Sơ Đồ State Transition:

```
┌────────────────┐
│ ScheduledState │
└────────┬───────┘
         │ (request/handle)
         ↓
┌────────────────┐
│ ConfirmedState │
└────────┬───────┘
         │ (request/handle)
         ↓
┌────────────────┐
│ CompletedState │
└────────────────┘

CancelledState ←← (từ bất kỳ state nào)
```

### Chi Tiết Transition:

```java
// Cách thực hiện transition
public class ScheduledState implements AppointmentState {
    @Override
    public void handle(AppointmentContext context) {
        System.out.println("Appointment is scheduled");
        // Chuyển state: ScheduledState → ConfirmedState
        context.setState(new ConfirmedState());
    }
}

public class ConfirmedState implements AppointmentState {
    @Override
    public void handle(AppointmentContext context) {
        System.out.println("Appointment is confirmed");
        // Chuyển state: ConfirmedState → CompletedState
        context.setState(new CompletedState());
    }
}
```

### Trình Tự Thực Thi:

```
Step 1: Khởi tạo Context với initial state
--------------------------------------------
AppointmentContext context = new AppointmentContext(appointment);
context.setState(new ScheduledState());
// context.state = ScheduledState object

Step 2: Gọi request() lần 1
----------------------------
context.request()
    ├─ state.handle(context)  // Gọi ScheduledState.handle()
    │   ├─ print "Appointment is scheduled"
    │   └─ context.setState(new ConfirmedState())
    │       └─ context.state = ConfirmedState object
    └─ return

Current state: ConfirmedState

Step 3: Gọi request() lần 2
----------------------------
context.request()
    ├─ state.handle(context)  // Gọi ConfirmedState.handle()
    │   ├─ print "Appointment is confirmed"
    │   └─ context.setState(new CompletedState())
    │       └─ context.state = CompletedState object
    └─ return

Current state: CompletedState

Step 4: Gọi request() lần 3
----------------------------
context.request()
    ├─ state.handle(context)  // Gọi CompletedState.handle()
    │   ├─ print "Appointment is completed"
    │   └─ (không thay đổi state, terminal state)
    └─ return

Current state: CompletedState (không thay đổi)
```

---

## Strategy Pattern - Cơ Chế Chi Tiết

### 🔍 Sơ Đồ Strategy Pattern:

```
┌──────────────────────┐
│  PaymentStrategy     │
│  (Interface)         │
├──────────────────────┤
│ + pay()              │
│ + getPaymentMethod() │
└──────────────────────┘
    ↑       ↑       ↑
    │       │       │
┌───┴────┐ ├───────┤ ┌───┴─────┐
│Cash    │ │Credit │ │Insurance│
│Payment │ │Card   │ │Payment  │
│Strategy│ │Payment│ │Strategy │
└────────┘ │Strategy│ └─────────┘
           └───────┘

┌──────────────────────┐
│  PaymentProcessor    │
│  (Context)           │
├──────────────────────┤
│ - strategy           │
├──────────────────────┤
│ + setPaymentStrategy()│
│ + processPayment()   │
└──────────────────────┘
```

### Trình Tự Runtime Thay Đổi Strategy:

```
Step 1: Tạo Context (PaymentProcessor)
---------------------------------------
PaymentProcessor processor = new PaymentProcessor();

Step 2: Set Strategy 1 - Cash Payment
--------------------------------------
processor.setPaymentStrategy(new CashPaymentStrategy());
// processor.strategy = CashPaymentStrategy object

processor.processPayment(BigDecimal("150.00"))
    ├─ strategy.pay(amount)
    │   ├─ CashPaymentStrategy.pay()
    │   └─ print "Paid 150.00 in cash"
    └─ return true

Step 3: Runtime Change - Credit Card Strategy
----------------------------------------------
processor.setPaymentStrategy(
    new CreditCardPaymentStrategy("5678", "Nguyễn Văn A")
);
// processor.strategy = CreditCardPaymentStrategy object

processor.processPayment(BigDecimal("500.00"))
    ├─ strategy.pay(amount)
    │   ├─ CreditCardPaymentStrategy.pay()
    │   └─ print "Paid 500.00 with credit card 5678 by Nguyễn Văn A"
    └─ return true

Step 4: Runtime Change - Insurance Strategy
---------------------------------------------
processor.setPaymentStrategy(
    new InsurancePaymentStrategy("Bảo Việt", "POL123")
);
// processor.strategy = InsurancePaymentStrategy object

processor.processPayment(BigDecimal("300.00"))
    ├─ strategy.pay(amount)
    │   ├─ InsurancePaymentStrategy.pay()
    │   └─ print "Paid 300.00 via insurance Bảo Việt (Policy: POL123)"
    └─ return true
```

### So Sánh: If-Else vs Strategy Pattern:

```java
// ❌ Cách cũ: If-Else (Mệnh lệnh)
public void processPayment(BigDecimal amount, String type) {
    if("cash".equals(type)) {
        // Logic thanh toán bằng tiền mặt
        System.out.println("Paid " + amount + " in cash");
    } else if("card".equals(type)) {
        // Logic thanh toán bằng thẻ
        System.out.println("Paid " + amount + " with credit card");
    } else if("insurance".equals(type)) {
        // Logic thanh toán qua bảo hiểm
        System.out.println("Paid " + amount + " via insurance");
    }
    // Vấn đề:
    // - Nếu thêm loại: "Bitcoin", phải sửa if-else
    // - Logic tất cả loại trong 1 method (violations of SRP)
    // - Khó test từng strategy
}

// ✅ Cách mới: Strategy Pattern
public class PaymentProcessor {
    private PaymentStrategy strategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;  // Runtime thay đổi
    }

    public boolean processPayment(BigDecimal amount) {
        return strategy.pay(amount);  // Một dòng, delegate cho strategy
    }
}

// Thêm loại mới: Bitcoin
public class BitcoinPaymentStrategy implements PaymentStrategy {
    public boolean pay(BigDecimal amount) {
        System.out.println("Paid " + amount + " via Bitcoin");
        return true;
    }
}

// Sử dụng:
processor.setPaymentStrategy(new BitcoinPaymentStrategy());
processor.processPayment(amount);
// Không cần sửa PaymentProcessor!
```

---

## Template Method Pattern - Cơ Chế Chi Tiết

### 🔍 Sơ Đồ Template Method:

```
┌────────────────────────────┐
│  MedicalReport             │
│  (Abstract Template)       │
├────────────────────────────┤
│ # generateReport() [final] │ ← Định sẵn thứ tự
│   1. collectData()         │ ← Abstract (subclass implement)
│   2. formatReport()        │ ← Abstract (subclass implement)
│   3. validateReport()      │ ← Abstract (subclass implement)
└────────────────────────────┘
    ↑       ↑       ↑
    │       │       │
┌───┴──────┐├───────┤ ┌───┴──────┐
│Patient   │ │Appt   │ │Billing   │
│Report    │ │Report │ │Report    │
│Generator │ │Generator│Generator│
└──────────┘ └───────┘ └──────────┘
```

### Trình Tự Thực Thi Template:

```
Step 1: Gọi generateReport()
-----------------------------
report = new PatientReportGenerator(101, date);
String result = report.generateReport();  // Gọi template method

Step 2: Template method điều khiển flow
----------------------------------------
public final String generateReport() {  // final → không thể override
    // Bước 1: Collect data (subclass implement)
    Map<String, Object> data = collectData();
    │
    ├─ PatientReportGenerator.collectData()
    │   └─ PatientDAO.findById(101)
    │       └─ return {patient, patientName, ...}

    // Bước 2: Format data (subclass implement)
    String report = formatReport(data);
    │
    ├─ PatientReportGenerator.formatReport()
    │   └─ return "Patient Report:\nName: Vân Anh\nDate: 2025-11-13"

    // Bước 3: Validate (subclass implement)
    if(validateReport(report)) {
        return report;
    }
    │
    ├─ PatientReportGenerator.validateReport()
    │   └─ check: report != null && contains("Patient Report")
    │       └─ return true

    return "Invalid report";
}

Step 3: Kết quả
----------------
Trả về report hoàn chỉnh với đầy đủ dữ liệu
```

### So Sánh: Không Template vs Template Method:

```java
// ❌ Cách cũ: Không Template (lặp lại code)
public class PatientReportGenerator {
    public String generateReport() {
        // Code lặp: Collect, format, validate
        Map<String, Object> data = collectData();  // Lặp lại
        String report = formatReport(data);        // Lặp lại
        if(validateReport(report)) {               // Lặp lại
            return report;
        }
        return "Invalid report";
    }
}

public class AppointmentReportGenerator {
    public String generateReport() {
        // Code lặp: Collect, format, validate (GIỐNG hệt!)
        Map<String, Object> data = collectData();  // Lặp lại
        String report = formatReport(data);        // Lặp lại
        if(validateReport(report)) {               // Lặp lại
            return report;
        }
        return "Invalid report";
    }
}

// Vấn đề:
// - Code lặp lại ở tất cả Generator
// - Nếu sửa flow (thêm step), phải sửa tất cả generator
// - Violations of DRY (Don't Repeat Yourself)

// ✅ Cách mới: Template Method
public abstract class MedicalReport {
    // Template method: flow chuẩn
    public final String generateReport() {
        Map<String, Object> data = collectData();
        String report = formatReport(data);
        if(validateReport(report)) {
            return report;
        }
        return "Invalid report";
    }

    // Hook methods: subclass implement
    protected abstract Map<String, Object> collectData();
    protected abstract String formatReport(Map<String, Object> data);
    protected abstract boolean validateReport(String report);
}

// Mỗi subclass chỉ implement chi tiết từng bước
public class PatientReportGenerator extends MedicalReport {
    @Override
    protected Map<String, Object> collectData() {
        // Chỉ implement chi tiết lấy patient data
        return patientDAO.findById(patientId);
    }

    @Override
    protected String formatReport(Map<String, Object> data) {
        // Chỉ implement chi tiết format patient
        return "Patient Report: ...";
    }

    @Override
    protected boolean validateReport(String report) {
        // Chỉ implement chi tiết validate patient
        return report.contains("Patient Report");
    }
}

// Lợi ích:
// - Không lặp lại: Flow chung định ở template
// - Dễ mở rộng: Thêm generator mới chỉ implement 3 hook
// - Dễ maintain: Sửa flow ở 1 chỗ, tất cả generator theo
```

---

## Decorator Pattern - Cơ Chế Chi Tiết (Wrapping Layers)

### 🔍 Sơ Đồ Nested Wrapping:

```
Layer 4: AuditLogMedicalRecordDecorator
        └─ wraps → Layer 3
                   │
        Layer 3: SignedMedicalRecordDecorator
                └─ wraps → Layer 2
                           │
                Layer 2: EncryptedMedicalRecordDecorator
                        └─ wraps → Layer 1
                                   │
                        Layer 1: BasicMedicalRecord (inti)
```

### Trình Tự Wrapping:

```
Step 1: Tạo Base Object
------------------------
MedicalRecord record = new BasicMedicalRecord();
// record.getRecord() → "Basic Medical Record"

Step 2: Wrap với Encrypted Decorator
--------------------------------------
record = new EncryptedMedicalRecordDecorator(record, "key123");
// record = {
//   medicalRecord: BasicMedicalRecord,
//   encryptionKey: "key123"
// }

Step 3: Wrap với Signed Decorator
----------------------------------
record = new SignedMedicalRecordDecorator(record, "Dr. Smith");
// record = {
//   medicalRecord: EncryptedMedicalRecordDecorator {
//     medicalRecord: BasicMedicalRecord,
//     ...
//   },
//   signedBy: "Dr. Smith"
// }

Step 4: Wrap với AuditLog Decorator
------------------------------------
record = new AuditLogMedicalRecordDecorator(record);
// record = {
//   medicalRecord: SignedMedicalRecordDecorator { ... }
// }
```

### Trình Tự Thực Thi save():

```
record.save()  // AuditLogMedicalRecordDecorator
    ├─ print "[AUDIT LOG] Record saved"
    ├─ medicalRecord.save()  // SignedMedicalRecordDecorator
    │   ├─ print "Saving signed record by: Dr. Smith"
    │   ├─ medicalRecord.save()  // EncryptedMedicalRecordDecorator
    │   │   ├─ print "Saving encrypted medical record with key: key123"
    │   │   ├─ medicalRecord.save()  // BasicMedicalRecord
    │   │   │   └─ print "Saving basic medical record"
    │   │   │   └─ return true
    │   │   └─ return true
    │   └─ return true
    └─ return true
```

### Output:
```
[AUDIT LOG] Record saved
Saving signed record by: Dr. Smith
Saving encrypted medical record with key: key123
Saving basic medical record
```

---

## Adapter Pattern - Cơ Chế Chi Tiết (Translation Layer)

### 🔍 Conversion Mapping:

```
Client Interface:  PaymentSystem
                   ├─ processPayment(BigDecimal, int)
                   └─ refundPayment(String)

                         ↓ (Adapter)
                   [Translation]
                   BigDecimal → Double
                   int → "Patient X"

Legacy Interface:  LegacyPaymentSystem
                   ├─ pay(Double, String)
                   └─ refund(String)
```

### Trình Tự Gọi qua Adapter:

```
Step 1: Client gọi qua PaymentSystem interface
----------------------------------------------
boolean success = paymentSystem.processPayment(
    new BigDecimal("100.00"),  // Modern type
    101                        // Patient ID as int
);

Step 2: Adapter nhận request
----------------------------
PaymentAdapter.processPayment(BigDecimal, int)
    ├─ Dịch: patientId (101) → patientName ("Patient 101")
    ├─ Dịch: BigDecimal (100.00) → Double (100.0)
    └─ Gọi legacy method: legacySystem.pay(100.0, "Patient 101")

Step 3: Legacy system xử lý
---------------------------
LegacyPaymentSystem.pay(Double 100.0, "Patient 101")
    ├─ print "Processing legacy payment: 100.0 for patient: Patient 101"
    └─ return true

Step 4: Adapter trả về kết quả
-------------------------------
PaymentAdapter.processPayment() → return true
Client nhận: boolean true
```

---

## Abstract Factory - Cơ Chế Chi Tiết (Family Creation)

### 🔍 Factory Family:

```
DAOFactoryProducer
    │
    ├─ getFactory("standard")
    │   └─ return StandardDAOFactory
    │       ├─ getDAOInstance("PatientDAO")
    │       │   └─ return new StandardPatientDAO()
    │       └─ getDAOInstance("AppointmentDAO")
    │           └─ return new StandardAppointmentDAO()
    │
    └─ getFactory("optimized")
        └─ return OptimizedDAOFactory
            ├─ getDAOInstance("PatientDAO")
            │   └─ return new OptimizedPatientDAO()
            └─ getDAOInstance("AppointmentDAO")
                └─ return new OptimizedAppointmentDAO()
```

### Trình Tự Sử Dụng Cả Hai Families:

```
Step 1: Chọn Standard Family
----------------------------
DAOFactory factory1 = DAOFactoryProducer.getFactory("standard");
// factory1 = StandardDAOFactory instance

PatientDAO patientDAO1 = (PatientDAO) factory1.getDAOInstance("PatientDAO");
// patientDAO1 = StandardPatientDAO instance (simple, no cache)

patientDAO1.findById(101);
    ├─ SELECT * FROM patients WHERE id = 101
    └─ return Patient object (basic query)

Step 2: Chọn Optimized Family
-----------------------------
DAOFactory factory2 = DAOFactoryProducer.getFactory("optimized");
// factory2 = OptimizedDAOFactory instance

PatientDAO patientDAO2 = (PatientDAO) factory2.getDAOInstance("PatientDAO");
// patientDAO2 = OptimizedPatientDAO instance (cache, indexing)

patientDAO2.findById(101);
    ├─ Check cache first
    ├─ If miss: SELECT id, firstName, lastName FROM patients WHERE id = 101 (optimized)
    ├─ Cache result
    └─ return Patient object (optimized query)

Step 3: Key Point - Tương thích trong Family
----------------------------------------------
// Standard family: PatientDAO + AppointmentDAO từ cùng StandardDAOFactory
PatientDAO p1 = (PatientDAO) factory1.getDAOInstance("PatientDAO");
AppointmentDAO a1 = (AppointmentDAO) factory1.getDAOInstance("AppointmentDAO");
// p1 và a1 cùng triết lý "đơn giản, không cache"
// Tương thích với nhau

// Optimized family: PatientDAO + AppointmentDAO từ cùng OptimizedDAOFactory
PatientDAO p2 = (PatientDAO) factory2.getDAOInstance("PatientDAO");
AppointmentDAO a2 = (AppointmentDAO) factory2.getDAOInstance("AppointmentDAO");
// p2 và a2 cùng triết lý "tối ưu, có cache"
// Tương thích với nhau

// Không nên mix:
PatientDAO p1 = (PatientDAO) factory1.getDAOInstance("PatientDAO");  // Simple
AppointmentDAO a2 = (AppointmentDAO) factory2.getDAOInstance("AppointmentDAO");  // Optimized
// p1 và a2 khác triết lý → có thể không tương thích
```

---

## Builder Pattern - Cơ Chế Chi Tiết (Step-by-Step Construction)

### 🔍 Trình Tự Xây Dựng:

```
Step 1: Tạo Builder
-------------------
PatientBuilder builder = new StandardPatientBuilder();
// builder.patient = new Patient() (rỗng)

Step 2: Set thuộc tính 1
------------------------
builder.setFirstName("Vân Anh");
// builder.patient.firstName = "Vân Anh"
// return builder (this)

Step 3: Set thuộc tính 2
------------------------
.setLastName("Trần");
// builder.patient.lastName = "Trần"
// return builder (this)

Step 4: Set thuộc tính 3
------------------------
.setEmail("vananh@example.com");
// builder.patient.email = "vananh@example.com"
// return builder (this)

...

Step N: Build (Final)
---------------------
.build();
// Tạo Patient mới
// Copy tất cả properties từ builder.patient
// return Patient object (hoàn chỉnh)
```

### Chaining (Method Fluent Interface):

```java
Patient p = new StandardPatientBuilder()
    .setFirstName("Vân Anh")      // return this
    .setLastName("Trần")          // return this
    .setEmail("...")              // return this
    .setAddress("...")            // return this
    .build();                      // return Patient

// Tương đương:
StandardPatientBuilder builder = new StandardPatientBuilder();
builder = builder.setFirstName("Vân Anh");
builder = builder.setLastName("Trần");
builder = builder.setEmail("...");
builder = builder.setAddress("...");
Patient p = builder.build();
```

---

## Singleton Pattern - Double-Checked Locking Chi Tiết

### 🔍 Vì sao Double-Checked Locking quan trọng:

```
❌ Single-Checked (Always Lock):
public synchronized static DatabaseConnection getInstance() {
    if(instance == null) {
        instance = new DatabaseConnection();
    }
    return instance;
}

Các lần gọi:
1st call:  lock → create → unlock (hơi chậm)
2nd call:  lock → check → unlock (chậm vô cớ!)
3rd call:  lock → check → unlock (chậm vô cớ!)
...
100th call: lock → check → unlock (chậm vô cớ!)
// Vấn đề: Mỗi lần gọi đều lock, dù instance đã tồn tại

✅ Double-Checked (Smart Lock):
public static DatabaseConnection getInstance() {
    DatabaseConnection result = instance;      // 1st check (NO LOCK)
    if(result == null) {                       // Fast path
        synchronized(DatabaseConnection.class) {  // LOCK chỉ nếu cần
            result = instance;                 // 2nd check (WITH LOCK)
            if(result == null) {
                result = new DatabaseConnection();
                instance = result;
            }
        }
    }
    return result;
}

Các lần gọi:
1st call:  check (no lock) → null → lock → create → unlock
2nd call:  check (no lock) → not null → return (NO LOCK!)
3rd call:  check (no lock) → not null → return (NO LOCK!)
...
100th call: check (no lock) → not null → return (NO LOCK!)
// Lợi ích: Chỉ lock lần đầu, sau đó không lock (nhanh 10x!)
```

### Volatile Keyword:

```java
private static volatile DatabaseConnection instance;
//             ^^^^^^^^
// Volatile đảm bảo:
// 1. Mỗi thread luôn đọc value mới nhất từ main memory
// 2. Prevents compiler optimizations
// 3. Cần thiết cho double-checked locking pattern

// Nếu không volatile:
private static DatabaseConnection instance;  // ❌ Unsafe
// - Compiler có thể cache value trong register
// - Một thread thấy non-null, nhưng value chưa được initialize đầy đủ
// - Race condition xảy ra
```

---

