# Phân Tích Chi Tiết Dự Án Hospital Design Patterns

## Tổng Quan Dự Án
Dự án này triển khai **12 Design Patterns** khác nhau trong một hệ thống quản lý bệnh viện. Mỗi pattern được áp dụng vào các trường hợp sử dụng thực tế trong bối cảnh bệnh viện.

**Công nghệ:**
- Java 21 (vừa nâng cấp)
- Maven
- PostgreSQL
- JUnit 5 + Mockito + AssertJ

---

## 1. SINGLETON PATTERN - Quản Lý Kết Nối Database

### 📂 File: `DatabaseConnection.java`

**Mục đích:** Đảm bảo chỉ có duy nhất một kết nối tới database trong toàn bộ ứng dụng.

**Các Function Chính:**

```java
// Double-Checked Locking Pattern
public static DatabaseConnection getInstance()
```
- Sử dụng **volatile** và **synchronized** để thread-safe
- Kiểm tra 2 lần (double-check) để giảm khóa không cần thiết
- Lần đầu: kiểm tra mà không khóa
- Lần thứ hai: khóa rồi kiểm tra lại

```java
public Connection getConnection() throws SQLException
```
- Trả về connection từ database
- Nếu connection đã đóng, tạo connection mới
- Tái sử dụng connection nếu vẫn mở

```java
private void loadDatabaseProperties()
```
- Đọc file `database.properties`
- Lấy URL, username, password từ file cấu hình

**Lý Do Sử Dụng:**
- ✅ Tiết kiệm tài nguyên: Chỉ 1 connection thay vì nhiều
- ✅ Quản lý trung tâm: Dễ theo dõi kết nối
- ✅ Thread-safe: Hoạt động đúng trong môi trường multi-threaded
- ✅ Linh hoạt: Hỗ trợ cấu hình động

**Sơ Đồ Hoạt Động:**
```
Lần gọi 1: Khi instance = null → tạo mới
Lần gọi 2: Khi instance ≠ null → trả về cùng instance
```

### 🔍 Cơ Chế Hoạt Động Chi Tiết

**Bảng Luồng Singleton:**

| Bước | Mô Tả | Chi Tiết |
|---|---|---|
| 1 | Client gọi `getInstance()` | Lần đầu tiên |
| 2 | Check `instance == null` | Không khóa, nhanh |
| 3 | Vào `synchronized block` | Khóa class |
| 4 | Check lại `instance == null` | Có thể đã được tạo bởi thread khác |
| 5 | Tạo `new DatabaseConnection()` | Gọi constructor private |
| 6 | Constructor gọi `loadDatabaseProperties()` | Đọc file config |
| 7 | Constructor gọi `initializeDataSource()` | Test kết nối DB |
| 8 | Lưu vào `instance` | Tất cả thread sau dùng cùng instance |
| 9 | Trả về `instance` | Kết nối database |

**Tại sao Double-Checked Locking quan trọng:**

```java
// ❌ Không tốt: Khóa tất cả các lần gọi
public static synchronized DatabaseConnection getInstance() {
    if(instance == null) {
        instance = new DatabaseConnection();
    }
    return instance;
}
// Vấn đề: Lần gọi 2, 3, 4, ... đều phải chờ lock (chậm)

// ✅ Tốt: Double-checked locking
public static DatabaseConnection getInstance() {
    DatabaseConnection result = instance;        // Lần 1: không khóa
    if(result == null) {                         // Fast path
        synchronized(DatabaseConnection.class) { // Lần 2: khóa khi cần
            result = instance;
            if(result == null) {
                result = new DatabaseConnection();
                instance = result;
            }
        }
    }
    return result;
}
// Lợi ích: Lần gọi 2, 3, 4, ... không cần lock, nhanh hơn 10x
```

**Trình Tự Khởi Tạo:**
```
DatabaseConnection.getInstance()
    ↓
Check instance != null (Fast check, không lock)
    ├─ Nếu có → trả về immediately
    └─ Nếu null → vào synchronized block
        ↓
    Khóa class (chỉ 1 thread vào)
    Check lại instance != null
    Tạo instance mới:
        ├─ loadDatabaseProperties() → đọc database.properties
        ├─ initializeDataSource() → test kết nối
        ├─ Class.forName(driver) → load PostgreSQL driver
        └─ DriverManager.getConnection(...) → kết nối DB
    ↓
Trả về cùng instance cho tất cả request
```

**Mối Liên Hệ Giữa Các File:**

```
DatabaseConnection.java
├─ Sử dụng: database.properties
├─ Kết nối: PostgreSQL Driver
└─ Được sử dụng bởi: Tất cả DAO classes
    ├─ StandardPatientDAO
    ├─ OptimizedPatientDAO
    ├─ StandardAppointmentDAO
    └─ OptimizedAppointmentDAO
```

---

## 2. FACTORY METHOD PATTERN - Tạo Nhân Sự

### 📂 Files: `Staff.java`, `StaffCreator.java`, `DoctorCreator.java`, `NurseCreator.java`, `AdminCreator.java`

**Mục đích:** Tạo các đối tượng nhân sự (Bác sĩ, Điều dưỡng, Quản lý) mà không cần biết lớp cụ thể.

**Các Interface & Class:**

```java
// Interface định nghĩa hành vi của nhân sự
public interface Staff {
    String getFullName();
    String getRole();
    String getJobDescription();
    String performTask(String task);
}

// Abstract class định nghĩa cách tạo Staff
public abstract class StaffCreator {
    public abstract Staff createStaff(String firstName, String lastName, Object... params);
    public abstract String getStaffType();
}

// Cụ thể hóa: Tạo Bác sĩ
public class DoctorCreator extends StaffCreator {
    public Staff createStaff(String firstName, String lastName, Object... params) {
        // params[0] = specialty (chuyên khoa)
        return new DoctorStaff(firstName, lastName, (String) params[0]);
    }

    public String getStaffType() {
        return "Doctor";
    }
}
```

**Các Concrete Creator:**
- `DoctorCreator` → tạo `DoctorStaff` (cần specialty)
- `NurseCreator` → tạo `NurseStaff` (cần ward, shift)
- `AdminCreator` → tạo `AdminStaff` (cần department)

**Lý Do Sử Dụng:**
- ✅ Linh hoạt: Thêm nhân sự mới chỉ cần thêm Creator mới
- ✅ Decoupling: Client không biết chi tiết tạo từng loại nhân sự
- ✅ Quản lý tập trung: Tất cả logic tạo trong các Creator
- ✅ Dễ mở rộng: Thêm EmployeeCreator, SpecialistCreator, v.v.

**Quy Trình Tạo:**
```
Client → DoctorCreator → createStaff() → DoctorStaff object
              ↓
        Thêm specialty
              ↓
        Tạo bác sĩ hoàn chỉnh
```

---

### 🔍 Cơ Chế Hoạt Động Chi Tiết

**Sơ Đồ Class Diagram:**

```
┌─────────────────────┐
│   Staff (Interface) │
├─────────────────────┤
│ + getFullName()     │
│ + getRole()         │
│ + getJobDescription()│
│ + performTask()     │
└─────────────────────┘
    ↑       ↑       ↑
    │       │       │
┌───┴──┐ ┌──┴──┐ ┌──┴───┐
│Doctor│ │Nurse│ │Admin  │
└──┬───┘ └──┬──┘ └──┬────┘
   ↓        ↓       ↓
┌──────────────────────┐
│  StaffCreator        │
│  (Abstract Class)    │
├──────────────────────┤
│ + createStaff()      │
│ + getStaffType()     │
└──────────────────────┘
    ↑       ↑       ↑
    │       │       │
┌───┴──────┐ ├──────┤ ┌───┴──────┐
│DoctorCreator│ NurseCreator│ AdminCreator│
└────────────┘ └──────────┘ └────────────┘
```

**Chi Tiết Quá Trình Tạo:**

```java
// Bước 1: Client quyết định loại nhân sự
String staffType = "Doctor";

// Bước 2: Chọn Creator phù hợp
StaffCreator creator;
if("Doctor".equals(staffType)) {
    creator = new DoctorCreator();
} else if("Nurse".equals(staffType)) {
    creator = new NurseCreator();
}

// Bước 3: Creator tạo Staff cụ thể
Staff staff = creator.createStaff("Vân Anh", "Trần", "Cardiology");
//                                 firstName  lastName  specialty (param)

// Bước 4: Sử dụng Staff
String fullName = staff.getFullName();  // "Vân Anh Trần"
String role = staff.getRole();          // "Doctor"
String specialty = staff.getJobDescription(); // "Cardiology"
```

**Bảng So Sánh Các Creator:**

| Creator | Staff Type | Bắt buộc param | Optional param | Ví dụ |
|---|---|---|---|---|
| DoctorCreator | DoctorStaff | specialty | - | `creator.createStaff("Vân", "Anh", "Cardiology")` |
| NurseCreator | NurseStaff | ward, shift | - | `creator.createStaff("Minh", "Hương", "ICU", "Day")` |
| AdminCreator | AdminStaff | department | - | `creator.createStaff("Thanh", "Tùng", "Manager")` |

**Luồng Tạo DoctorStaff:**

```
DoctorCreator.createStaff("Vân Anh", "Trần", "Cardiology")
    ↓
Kiểm tra params.length >= 1 (cần specialty)
    ↓
new DoctorStaff("Vân Anh", "Trần", "Cardiology")
    ├─ firstName = "Vân Anh"
    ├─ lastName = "Trần"
    ├─ specialty = "Cardiology"
    └─ role = "Doctor"
    ↓
Trả về Staff object
```

**Lợi Ích So Với Constructor Trực Tiếp:**

```java
// ❌ Cách cũ (Tight coupling)
public void hireStaff(String type) {
    Staff staff;
    if("Doctor".equals(type)) {
        staff = new DoctorStaff("Vân", "Anh", "Cardiology");
    } else if("Nurse".equals(type)) {
        staff = new NurseStaff("Minh", "Hương", "ICU", "Day");
    } else {
        staff = new AdminStaff("Thanh", "Tùng", "Manager");
    }
    employeeDAO.save(staff);
}
// Vấn đề: Nếu thêm SecurityStaff, phải sửa hàm này

// ✅ Cách mới (Loose coupling)
public void hireStaff(StaffCreator creator, String firstName,
                      String lastName, Object... params) {
    Staff staff = creator.createStaff(firstName, lastName, params);
    employeeDAO.save(staff);
}

// Thêm type mới: chỉ tạo SecurityCreator, không sửa code này
public class SecurityCreator extends StaffCreator {
    public Staff createStaff(String firstName, String lastName, Object... params) {
        return new SecurityStaff(firstName, lastName, (String) params[0]);
    }
}
```

**Mối Liên Hệ Giữa Các File:**

```
Factory.java/
├── Staff.java (Interface)
├── StaffCreator.java (Abstract Factory)
├── DoctorCreator.java (Concrete Creator)
│   └─→ DoctorStaff.java (Concrete Product)
├── NurseCreator.java (Concrete Creator)
│   └─→ NurseStaff.java (Concrete Product)
└── AdminCreator.java (Concrete Creator)
    └─→ AdminStaff.java (Concrete Product)
```

---

## 3. ABSTRACT FACTORY PATTERN - Tạo DAO Families

### 📂 Files: `DAOFactory.java`, `DAOFactoryProducer.java`, `StandardDAOFactory.java`, `OptimizedDAOFactory.java`

**Mục đích:** Tạo các họ đối tượng (DAO) liên quan mà đảm bảo tương thích với nhau.

**Cấu Trúc:**

```java
// Interface Factory
public interface DAOFactory {
    Object getDAOInstance(String daoType);
}

// Producer chọn factory đúng
public class DAOFactoryProducer {
    public static DAOFactory getFactory(String factoryType) {
        switch(factoryType.toLowerCase()) {
            case "standard":  return new StandardDAOFactory();  // Đơn giản
            case "optimized": return new OptimizedDAOFactory(); // Tối ưu
            default: return null;
        }
    }
}

// Standard Factory tạo DAO thông thường
public class StandardDAOFactory implements DAOFactory {
    public Object getDAOInstance(String daoType) {
        if("PatientDAO".equals(daoType))
            return new StandardPatientDAO();
        if("AppointmentDAO".equals(daoType))
            return new StandardAppointmentDAO();
    }
}

// Optimized Factory tạo DAO tối ưu hóa
public class OptimizedDAOFactory implements DAOFactory {
    public Object getDAOInstance(String daoType) {
        if("PatientDAO".equals(daoType))
            return new OptimizedPatientDAO(); // Cache, indexing
        if("AppointmentDAO".equals(daoType))
            return new OptimizedAppointmentDAO();
    }
}
```

**Cây Đối Tượng:**
```
DAOFactory
├─ StandardDAOFactory
│   ├─ StandardPatientDAO
│   └─ StandardAppointmentDAO
└─ OptimizedDAOFactory
    ├─ OptimizedPatientDAO
    └─ OptimizedAppointmentDAO
```

**Lý Do Sử Dụng:**
- ✅ Tương thích đảm bảo: DAO từ cùng factory compatible
- ✅ Dễ chuyển đổi: Từ Standard sang Optimized chỉ cần 1 dòng code
- ✅ Quản lý phức tạp: Xử lý các họ object liên quan
- ✅ Chính sách cô lập: Mỗi factory chịu trách nhiệm các DAO của nó

---

### 🔍 Cơ Chế Hoạt Động Chi Tiết

**Sơ Đồ Class Hierarchy:**

```
┌──────────────────────┐
│    DAOFactory        │
│    (Interface)       │
├──────────────────────┤
│ + getDAOInstance()   │
└──────────────────────┘
        ↑       ↑
        │       │
   ┌────┴──┐ ┌──┴─────────┐
   │Standard│ │Optimized   │
   │DAOFactory│ DAOFactory │
   └────┬──┘ └──┬─────────┘
        │       │
   ┌────▼──────────────┐
   │ DAO Products      │
   ├───────────────────┤
   │ PatientDAO        │
   │ AppointmentDAO    │
   │ BillingDAO        │
   └───────────────────┘
```

**Quá Trình Chọn Factory và Tạo DAO:**

```
Client
    ↓
DAOFactoryProducer.getFactory("standard")
    ├─ Kiểm tra factoryType
    ├─ "standard" → return new StandardDAOFactory()
    └─ "optimized" → return new OptimizedDAOFactory()
    ↓
DAOFactory factory (có type đã xác định)
    ↓
factory.getDAOInstance("PatientDAO")
    ├─ StandardDAOFactory:
    │   └─ new StandardPatientDAO() (query đơn giản)
    └─ OptimizedDAOFactory:
        └─ new OptimizedPatientDAO() (query tối ưu, cache)
    ↓
DAO được trả về
```

**Sự Khác Biệt StandardDAOFactory vs OptimizedDAOFactory:**

| Tính Năng | StandardDAOFactory | OptimizedDAOFactory |
|---|---|---|
| Query | Đơn giản, SELECT * | Chỉ select cột cần |
| Performance | Tốt cho dữ liệu nhỏ | Tốt cho dữ liệu lớn |
| Cache | Không | Có local cache |
| Index | Không yêu cầu | Yêu cầu index |
| Connection | Cố định | Connection pooling |
| Kích thước | Nhỏ | Lớn |
| Phức tạp | Thấp | Cao |

**Chi Tiết Code:**

```java
// DAOFactoryProducer: Chọn factory đúng
public class DAOFactoryProducer {
    public static DAOFactory getFactory(String factoryType) {
        if(factoryType == null) return null;

        switch(factoryType.toLowerCase()) {
            case "standard":
                return new StandardDAOFactory();  // Tạo Standard factory
            case "optimized":
                return new OptimizedDAOFactory(); // Tạo Optimized factory
            default:
                return null;
        }
    }
}

// StandardDAOFactory: Tạo Standard DAO
public class StandardDAOFactory implements DAOFactory {
    @Override
    public Object getDAOInstance(String daoType) {
        switch(daoType) {
            case "PatientDAO":
                return new StandardPatientDAO();      // DAO đơn giản
            case "AppointmentDAO":
                return new StandardAppointmentDAO();  // DAO đơn giản
            default:
                return null;
        }
    }
}

// OptimizedDAOFactory: Tạo Optimized DAO
public class OptimizedDAOFactory implements DAOFactory {
    @Override
    public Object getDAOInstance(String daoType) {
        switch(daoType) {
            case "PatientDAO":
                return new OptimizedPatientDAO();      // DAO tối ưu
            case "AppointmentDAO":
                return new OptimizedAppointmentDAO();  // DAO tối ưu
            default:
                return null;
        }
    }
}
```

**Sử Dụng Cả Hai Factories:**

```java
// Sử dụng Standard (nhanh, đơn giản)
DAOFactory standardFactory = DAOFactoryProducer.getFactory("standard");
PatientDAO patientDAO1 = (PatientDAO) standardFactory.getDAOInstance("PatientDAO");
AppointmentDAO appointmentDAO1 = (AppointmentDAO) standardFactory.getDAOInstance("AppointmentDAO");

patientDAO1.findById(101);       // Simple query
appointmentDAO1.findById(1);     // Simple query

System.out.println("=== Using Standard Factory ===");
patientDAO1.save(new Patient(...));


// Sử dụng Optimized (phức tạp, tối ưu)
DAOFactory optimizedFactory = DAOFactoryProducer.getFactory("optimized");
PatientDAO patientDAO2 = (PatientDAO) optimizedFactory.getDAOInstance("PatientDAO");
AppointmentDAO appointmentDAO2 = (AppointmentDAO) optimizedFactory.getDAOInstance("AppointmentDAO");

patientDAO2.findById(101);       // Optimized query with cache
appointmentDAO2.findById(1);     // Optimized query with index

System.out.println("=== Using Optimized Factory ===");
patientDAO2.save(new Patient(...));
```

**Đặc Điểm Abstract Factory vs Factory Method:**

| Đặc Điểm | Factory Method | Abstract Factory |
|---|---|---|
| Tạo | 1 object | Họ objects |
| Ví dụ | 1 loại Staff | Nhiều DAO liên quan |
| Mục đích | Đơn giản hóa tạo object | Đảm bảo tương thích |
| Khi dùng | Thêm loại | Thêm cả họ |

**Mối Liên Hệ Giữa Các File:**

```
AbstractFactory.java/
├── DAOFactory.java (Interface)
├── DAOFactoryProducer.java (Producer)
├── StandardDAOFactory.java (Concrete Factory)
│   ├─→ StandardPatientDAO.java
│   └─→ StandardAppointmentDAO.java
├── OptimizedDAOFactory.java (Concrete Factory)
│   ├─→ OptimizedPatientDAO.java
│   └─→ OptimizedAppointmentDAO.java
├── Patient.java (Model)
└── Appointment.java (Model)
```

---

## 4. BUILDER PATTERN - Xây Dựng Bệnh Nhân

### 📂 Files: `PatientBuilder.java`, `StandardPatientBuilder.java`, `PatientDirector.java`

**Mục đích:** Xây dựng các đối tượng phức tạp (Patient) từng bước một, có thể bỏ qua một số thuộc tính.

**Cấu Trúc:**

```java
// Builder Interface
public interface PatientBuilder {
    PatientBuilder setFirstName(String firstName);
    PatientBuilder setLastName(String lastName);
    PatientBuilder setDateOfBirth(Date dateOfBirth);
    PatientBuilder setGender(String gender);
    PatientBuilder setAddress(String address);
    PatientBuilder setEmail(String email);
    // ... (7 setter khác)
    Patient build();
}

// Concrete Builder
public class StandardPatientBuilder implements PatientBuilder {
    private final Patient patient = new Patient();

    public PatientBuilder setFirstName(String firstName) {
        this.patient.setFirstName(firstName);
        return this;  // Fluent interface: có thể chain
    }

    public Patient build() {
        return patient;
    }
}

// Sử dụng:
Patient p = new StandardPatientBuilder()
    .setFirstName("Vân Anh")
    .setLastName("Trần")
    .setAddress("Thái Bình")
    .setEmail("vananh@example.com")
    .build();
```

**Quy Trình Xây Dựng:**
```
Builder tạo patient rỗng
    ↓
setFirstName("Vân Anh") → return builder
    ↓
setLastName("Trần") → return builder
    ↓
setAddress("Thái Bình") → return builder
    ↓
build() → trả về Patient hoàn chỉnh
```

**Lý Do Sử Dụng:**
- ✅ Xây dựng linh hoạt: Có thể bỏ qua thuộc tính không cần
- ✅ Dễ đọc: Fluent interface dễ hiểu
- ✅ Tránh Constructor dài: Thay vì `Patient(f, l, d, g, a, e, m)`
- ✅ Immutable friendly: Có thể tạo object bất biến
- ✅ Validation: Kiểm tra dữ liệu từng bước

---

### 🔍 Cơ Chế Hoạt Động Chi Tiết

**Sơ Đồ Interaction:**

```
┌──────────────────────────┐
│   PatientBuilder         │
│   (Interface)            │
├──────────────────────────┤
│ + setFirstName()         │
│ + setLastName()          │
│ + setEmail()             │
│ + ... (8 setter)         │
│ + build()                │
└──────────────────────────┘
            ↑
            │
┌──────────────────────────────┐
│  StandardPatientBuilder      │
│  (Concrete Builder)          │
├──────────────────────────────┤
│ - patient: Patient           │
├──────────────────────────────┤
│ + setFirstName(String)       │
│ + setLastName(String)        │
│ + setEmail(String)           │
│ + ... (return this)          │
│ + build() → Patient          │
└──────────────────────────────┘
            │
            └─→ Patient.java (được xây dựng)
```

**Fluent Interface (Method Chaining):**

```java
// Builder không return Patient, mà return this (chính nó)
public class StandardPatientBuilder implements PatientBuilder {
    private final Patient patient = new Patient();

    @Override
    public PatientBuilder setFirstName(String firstName) {
        this.patient.setFirstName(firstName);
        return this;  // Trả về builder → có thể gọi method tiếp
    }

    @Override
    public PatientBuilder setLastName(String lastName) {
        this.patient.setLastName(lastName);
        return this;  // Trả về builder → có thể gọi method tiếp
    }

    @Override
    public PatientBuilder setEmail(String email) {
        this.patient.setEmail(email);
        return this;  // Trả về builder → có thể gọi method tiếp
    }

    @Override
    public Patient build() {
        // Chỉ build() mới trả về Patient cuối cùng
        Patient builtPatient = new Patient();
        builtPatient.setFirstName(this.patient.getFirstName());
        builtPatient.setLastName(this.patient.getLastName());
        builtPatient.setEmail(this.patient.getEmail());
        // ... copy tất cả properties
        return builtPatient;
    }
}

// Sử dụng:
Patient p = new StandardPatientBuilder()
    .setFirstName("Vân")          // setFirstName() return this
    .setLastName("Anh")            // setLastName() return this
    .setEmail("vananh@example.com") // setEmail() return this
    .build();                       // build() return Patient
```

**Trình Tự Thực Thi:**

```
new StandardPatientBuilder()
    ↓
    │ return this
    ├─→ .setFirstName("Vân Anh")
    │
    │ return this
    ├─→ .setLastName("Trần")
    │
    │ return this
    ├─→ .setDateOfBirth(date)
    │
    │ return this
    ├─→ .setGender("Female")
    │
    │ return this
    ├─→ .setAddress("Thái Bình")
    │
    │ return this
    ├─→ .setEmail("vananh@example.com")
    │
    │ return Patient
    └─→ .build()

// Kết quả: Patient object đầy đủ
```

**So Sánh Constructor vs Builder:**

```java
// ❌ Constructor dài dòng (telescoping)
Patient p = new Patient(
    "Vân Anh",      // firstName
    "Trần",         // lastName
    new Date(),     // dateOfBirth
    "Female",       // gender
    "0123456789",   // contactNumber
    "Thái Bình",    // address
    "vananh@example.com", // email
    "Hypertension"  // medicalHistory
);
// Vấn đề: Khó nhớ thứ tự, khó sửa, dễ nhầm lẫn

// ✅ Builder (fluent, rõ ràng)
Patient p = new StandardPatientBuilder()
    .setFirstName("Vân Anh")
    .setLastName("Trần")
    .setDateOfBirth(new Date())
    .setGender("Female")
    .setContactNumber("0123456789")
    .setAddress("Thái Bình")
    .setEmail("vananh@example.com")
    .setMedicalHistory("Hypertension")
    .build();
// Lợi ích: Rõ ràng, dễ đọc, dễ sửa, không dễ nhầm lẫn

// ✅ Builder (bỏ qua không cần)
Patient p = new StandardPatientBuilder()
    .setFirstName("Vân Anh")
    .setLastName("Trần")
    .setEmail("vananh@example.com")
    .build();
// Các trường khác = null, không vấn đề
```

**PatientDirector (Optional):**

```java
// Director: Định nghĩa cách xây dựng tiêu chuẩn
public class PatientDirector {
    private PatientBuilder builder;

    public PatientDirector(PatientBuilder builder) {
        this.builder = builder;
    }

    // Xây dựng patient đầy đủ (tiêu chuẩn)
    public Patient buildStandardPatient(String firstName, String lastName, Date dateOfBirth) {
        return builder
            .setFirstName(firstName)
            .setLastName(lastName)
            .setDateOfBirth(dateOfBirth)
            .setGender("Unknown")           // Giá trị mặc định
            .setAddress("Unknown")          // Giá trị mặc định
            .setEmail("")                   // Giá trị mặc định
            .build();
    }

    // Xây dựng patient với toàn bộ thông tin
    public Patient buildCompletePatient(String firstName, String lastName,
                                       Date dateOfBirth, String gender,
                                       String address, String email) {
        return builder
            .setFirstName(firstName)
            .setLastName(lastName)
            .setDateOfBirth(dateOfBirth)
            .setGender(gender)
            .setAddress(address)
            .setEmail(email)
            .build();
    }
}

// Sử dụng Director:
PatientDirector director = new PatientDirector(new StandardPatientBuilder());
Patient standard = director.buildStandardPatient("Vân Anh", "Trần", new Date());
Patient complete = director.buildCompletePatient("Vân Anh", "Trần", date, "Female", "Thái Bình", "email@example.com");
```

**Mối Liên Hệ Giữa Các File:**

```
Builder.java/
├── PatientBuilder.java (Interface)
├── StandardPatientBuilder.java (Concrete Builder)
├── PatientDirector.java (Optional Director)
└── Patient.java (Product - được xây dựng)
```

---

## 5. ADAPTER PATTERN - Chuyển Đổi Hệ Thống Thanh Toán

### 📂 Files: `PaymentSystem.java`, `LegacyPaymentSystem.java`, `PaymentAdapter.java`

**Mục đích:** Chuyển đổi interface cũ (LegacyPaymentSystem) sang interface mới (PaymentSystem).

**Cấu Trúc:**

```java
// Interface mới (mong muốn)
public interface PaymentSystem {
    boolean processPayment(BigDecimal amount, int patientId);
    boolean refundPayment(String transactionId);
}

// Class cũ (legacy - không thể thay đổi)
public class LegacyPaymentSystem {
    public boolean pay(Double amount, String patientName) { ... }
    public void refund(String txnId) { ... }
}

// Adapter: "dịch" từ cũ sang mới
public class PaymentAdapter implements PaymentSystem {
    private final LegacyPaymentSystem legacyPaymentSystem;

    @Override
    public boolean processPayment(BigDecimal amount, int patientId) {
        // Dịch từ mới → cũ
        String patientName = "Patient " + patientId;
        Double amountDouble = amount.doubleValue();
        return legacyPaymentSystem.pay(amountDouble, patientName);
    }

    @Override
    public boolean refundPayment(String transactionId) {
        legacyPaymentSystem.refund(transactionId);
        return true;
    }
}
```

**Quy Trình:**
```
Client (dùng PaymentSystem)
    ↓
PaymentAdapter (cầu nối)
    ├─ BigDecimal → Double
    ├─ patientId → patientName
    └─ processPayment() → pay()
    ↓
LegacyPaymentSystem (hệ thống cũ)
```

**Lý Do Sử Dụng:**
- ✅ Tích hợp cũ: Sử dụng code cũ mà không sửa đổi
- ✅ Interface thống nhất: Client không biết sự khác biệt
- ✅ Tránh breaking changes: Không phá vỡ code cũ
- ✅ Dễ test: Có thể mock LegacyPaymentSystem
- ✅ Dễ chuyển đổi: Khi sẵn sàng, thay đổi implementation

---

### 🔍 Cơ Chế Hoạt Động Chi Tiết

**Sơ Đồ Adapter Pattern:**

```
┌─────────────────────┐
│  PaymentSystem      │
│  (Target Interface) │
├─────────────────────┤
│ + processPayment()  │
│   (BigDecimal, int) │
│ + refundPayment()   │
│   (String)          │
└─────────────────────┘
         ↑
         │ implements
         │
    ┌────┴──────────────────────┐
    │   PaymentAdapter           │
    │   (Adapter/Bridge)         │
    ├───────────────────────────┤
    │ - legacy: LegacyPayment   │
    ├───────────────────────────┤
    │ + processPayment()        │
    │ + refundPayment()         │
    └────┬──────────────────────┘
         │
         │ uses
         ↓
┌──────────────────────────┐
│ LegacyPaymentSystem      │
│ (Adaptee/Incompatible)   │
├──────────────────────────┤
│ + pay(Double, String)    │
│ + refund(String)         │
└──────────────────────────┘
```

**So Sánh Interface Cũ vs Mới:**

| Tiêu Chí | LegacyPaymentSystem | PaymentSystem |
|---|---|---|
| Amount type | Double | BigDecimal |
| Identifier | String patientName | int patientId |
| processPayment | pay(Double, String) | processPayment(BigDecimal, int) |
| refund | refund(String) | refundPayment(String) |
| Return type | boolean | boolean |

**Chi Tiết Code Adapter:**

```java
// Interface mới (mong muốn, modern)
public interface PaymentSystem {
    boolean processPayment(BigDecimal amount, int patientId);
    boolean refundPayment(String transactionId);
}

// Class cũ (legacy, không thể sửa)
public class LegacyPaymentSystem {
    public boolean pay(Double amount, String patientName) {
        System.out.println("Processing legacy payment: " + amount +
                         " for patient: " + patientName);
        return true;
    }

    public void refund(String txnId) {
        System.out.println("Refunding transaction: " + txnId);
    }
}

// Adapter: Dịch từ mới sang cũ
public class PaymentAdapter implements PaymentSystem {
    private final LegacyPaymentSystem legacyPaymentSystem;

    public PaymentAdapter(LegacyPaymentSystem legacyPaymentSystem) {
        this.legacyPaymentSystem = legacyPaymentSystem;
    }

    @Override
    public boolean processPayment(BigDecimal amount, int patientId) {
        // Dịch từ PaymentSystem → LegacyPaymentSystem
        String patientName = "Patient " + patientId;  // int → String
        Double amountDouble = amount.doubleValue();   // BigDecimal → Double
        return legacyPaymentSystem.pay(amountDouble, patientName);
    }

    @Override
    public boolean refundPayment(String transactionId) {
        // Gọi method cũ
        legacyPaymentSystem.refund(transactionId);
        return true;
    }
}
```

**Luồng Gọi:**

```
Client.processPayment(BigDecimal 100.00, int 101)
    ↓
PaymentAdapter.processPayment(BigDecimal, int)
    ├─ Dịch: patientId 101 → "Patient 101"
    ├─ Dịch: BigDecimal 100.00 → Double 100.00
    ↓
LegacyPaymentSystem.pay(Double 100.00, "Patient 101")
    ├─ System.out.println("Processing legacy payment: 100.0 for patient: Patient 101")
    └─ return true
    ↓
PaymentAdapter.processPayment() return true
    ↓
Client nhận kết quả
```

**Ví Dụ Sử Dụng:**

```java
// Khởi tạo
LegacyPaymentSystem legacySystem = new LegacyPaymentSystem();
PaymentSystem adapter = new PaymentAdapter(legacySystem);

// Sử dụng qua adapter
boolean success = adapter.processPayment(
    new BigDecimal("150.00"),  // amount
    101                        // patientId
);
// → Adapter dịch và gọi legacySystem.pay(150.00, "Patient 101")
// → Output: "Processing legacy payment: 150.0 for patient: Patient 101"

// Hoàn tiền
adapter.refundPayment("TXN123");
// → Adapter gọi legacySystem.refund("TXN123")
// → Output: "Refunding transaction: TXN123"
```

**Tại Sao Không Sửa LegacyPaymentSystem Trực Tiếp?**

```java
// ❌ Cách xấu: Sửa code cũ (rủi ro)
public class LegacyPaymentSystem {
    // Thêm method mới
    public boolean processPayment(BigDecimal amount, int patientId) {
        return pay(amount.doubleValue(), "Patient " + patientId);
    }
}
// Vấn đề:
// - Nếu sửa sai, có thể phá vỡ code cũ đang dùng
// - Phải test lại tất cả code gọi method pay()
// - Breaking changes cho các client khác

// ✅ Cách tốt: Tạo Adapter (an toàn)
public class PaymentAdapter implements PaymentSystem {
    private final LegacyPaymentSystem legacy;

    public boolean processPayment(BigDecimal amount, int patientId) {
        return legacy.pay(amount.doubleValue(), "Patient " + patientId);
    }
}
// Lợi ích:
// - LegacyPaymentSystem không bị sửa
// - Code cũ vẫn hoạt động bình thường
// - Code mới dùng qua adapter
// - Dễ test adapter riêng
```

**Mối Liên Hệ Giữa Các File:**

```
Adapter.java/
├── PaymentSystem.java (Target Interface - modern)
├── LegacyPaymentSystem.java (Adaptee - legacy, cũ)
└── PaymentAdapter.java (Adapter - cầu nối)
```

---

## 6. DECORATOR PATTERN - Tăng Cường Medical Record

### 📂 Files: `MedicalRecord.java`, `MedicalRecordDecorator.java`, `EncryptedMedicalRecordDecorator.java`, `SignedMedicalRecordDecorator.java`, `AuditLogMedicalRecordDecorator.java`

**Mục đích:** Thêm tính năng vào một đối tượng một cách động (runtime).

**Cấu Trúc:**

```java
// Base Interface
public interface MedicalRecord {
    String getRecord();
    boolean save();
}

// Base Implementation
public class BasicMedicalRecord implements MedicalRecord {
    public String getRecord() { return "Basic Record"; }
    public boolean save() { ... }
}

// Abstract Decorator
public abstract class MedicalRecordDecorator implements MedicalRecord {
    protected MedicalRecord medicalRecord;

    public String getRecord() {
        return medicalRecord.getRecord();
    }

    public boolean save() {
        return medicalRecord.save();
    }
}

// Concrete Decorator 1: Mã hóa
public class EncryptedMedicalRecordDecorator extends MedicalRecordDecorator {
    private String encryptionKey;

    public EncryptedMedicalRecordDecorator(MedicalRecord record, String key) {
        this.medicalRecord = record;
        this.encryptionKey = key;
    }

    @Override
    public String getRecord() {
        String original = medicalRecord.getRecord();
        return encrypt(original);
    }

    @Override
    public boolean save() {
        System.out.println("Saving encrypted record...");
        return medicalRecord.save();
    }
}

// Concrete Decorator 2: Ký số
public class SignedMedicalRecordDecorator extends MedicalRecordDecorator {
    private String signedBy;

    public SignedMedicalRecordDecorator(MedicalRecord record, String signedBy) {
        this.medicalRecord = record;
        this.signedBy = signedBy;
    }

    @Override
    public String getRecord() {
        return medicalRecord.getRecord() + " [Signed: " + signedBy + "]";
    }
}

// Concrete Decorator 3: Ghi log
public class AuditLogMedicalRecordDecorator extends MedicalRecordDecorator {
    @Override
    public boolean save() {
        System.out.println("[AUDIT LOG] Record saved");
        return medicalRecord.save();
    }
}
```

**Sử Dụng:**
```java
// Thêm tính năng lần lượt
MedicalRecord record = new BasicMedicalRecord();
record = new EncryptedMedicalRecordDecorator(record, "key123");
record = new SignedMedicalRecordDecorator(record, "Dr. Smith");
record = new AuditLogMedicalRecordDecorator(record);

// Kết quả: record vừa mã hóa, vừa ký, vừa ghi log
record.save();
```

**Cây Kết Hợp:**
```
BasicMedicalRecord
    ↓
EncryptedMedicalRecordDecorator (thêm mã hóa)
    ↓
SignedMedicalRecordDecorator (thêm ký số)
    ↓
AuditLogMedicalRecordDecorator (thêm ghi log)
```

**Lý Do Sử Dụng:**
- ✅ Tính năng động: Thêm/bỏ tính năng runtime mà không sửa code
- ✅ Linh hoạt: Kết hợp tùy ý (mã hóa + ký, hoặc chỉ ghi log)
- ✅ Single Responsibility: Mỗi decorator chỉ làm một việc
- ✅ Tránh explosion: Thay vì tạo 8 subclass, tạo 3 decorator
- ✅ Composable: Có thể chain nhiều decorator

---

### 🔍 Cơ Chế Hoạt Động Chi Tiết

**Sơ Đồ Class Diagram:**

```
┌──────────────────────┐
│   MedicalRecord      │
│   (Interface)        │
├──────────────────────┤
│ + getRecord()        │
│ + save()             │
└──────────────────────┘
    ↑           ↑
    │           │
┌───┴──────┐    │
│BasicMR   │    │
└───┬──────┘    │
    │           │
    │     ┌─────┴──────────────┐
    │     │ MedicalRecord      │
    │     │ Decorator          │
    │     │ (Abstract)         │
    │     ├──────────────────┤
    │     │ - medicalRecord  │
    │     │                  │
    │     └──────────────────┘
    │               ↑ extends
    │               │
    │     ┌─────────┼─────────┬──────────────────┐
    │     │         │         │                  │
    ├─→──┴─→──┬─────┴──┬──┬──┴──────┬────────────┤
    │       ┌─┴┐ ┌────┘  │  │ ┌────┘             │
    └─→─────┤E│ │       │  └─┤S│                 │
            └─┘ │      │   └──┘                 │
                │ E=Encrypted                    │
                │ S=Signed                       │
                │ A=AuditLog                     │
                │                                │
            ┌───┴────────┐                       │
            │   Audit    │                       │
            │   Log      │                       │
            └────────────┘
```

**Inheritance vs Composition Comparison:**

```java
// ❌ Cách cũ: Inheritance (Explosion Problem)
// Để có đủ các tổ hợp: Encrypted, Signed, AuditLog
// Cần tạo: 2^3 = 8 class

public class BasicMedicalRecord implements MedicalRecord { }
public class EncryptedMedicalRecord extends BasicMedicalRecord { }
public class SignedMedicalRecord extends BasicMedicalRecord { }
public class AuditLogMedicalRecord extends BasicMedicalRecord { }
public class EncryptedSignedMedicalRecord extends BasicMedicalRecord { } // Combination
public class EncryptedAuditLogMedicalRecord extends BasicMedicalRecord { }
public class SignedAuditLogMedicalRecord extends BasicMedicalRecord { }
public class EncryptedSignedAuditLogMedicalRecord extends BasicMedicalRecord { }
// Vấn đề: Quá nhiều class, khó maintain, dễ nhầm lẫn

// ✅ Cách mới: Decorator (Composition)
// Chỉ cần tạo: Basic + 3 Decorator = 4 class

public class BasicMedicalRecord implements MedicalRecord { }
public class EncryptedMedicalRecordDecorator extends MedicalRecordDecorator { }
public class SignedMedicalRecordDecorator extends MedicalRecordDecorator { }
public class AuditLogMedicalRecordDecorator extends MedicalRecordDecorator { }

// Tổ hợp:
MedicalRecord record = new BasicMedicalRecord();
record = new EncryptedMedicalRecordDecorator(record);
record = new SignedMedicalRecordDecorator(record);
record = new AuditLogMedicalRecordDecorator(record);
// Lợi ích: Linh hoạt, ít code, dễ maintain
```

**Chi Tiết Code Decorator:**

```java
// Base Interface
public interface MedicalRecord {
    String getRecord();    // Lấy nội dung record
    boolean save();        // Lưu record
}

// Base Implementation
public class BasicMedicalRecord implements MedicalRecord {
    private int recordId;
    private int patientId;
    private int doctorId;

    @Override
    public String getRecord() {
        return "Basic Medical Record";
    }

    @Override
    public boolean save() {
        System.out.println("Saving basic medical record");
        return true;
    }
}

// Abstract Decorator (Wrapper)
public abstract class MedicalRecordDecorator implements MedicalRecord {
    protected MedicalRecord medicalRecord;  // Wrap object cần decorate

    public MedicalRecordDecorator(MedicalRecord medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    @Override
    public String getRecord() {
        return medicalRecord.getRecord();  // Forward to wrapped object
    }

    @Override
    public boolean save() {
        return medicalRecord.save();       // Forward to wrapped object
    }
}

// Concrete Decorator 1: Mã hóa
public class EncryptedMedicalRecordDecorator extends MedicalRecordDecorator {
    private String encryptionKey;

    public EncryptedMedicalRecordDecorator(MedicalRecord record, String key) {
        super(record);
        this.encryptionKey = key;
    }

    @Override
    public String getRecord() {
        String original = medicalRecord.getRecord();
        return encrypt(original);  // Thêm tính năng
    }

    @Override
    public boolean save() {
        System.out.println("Saving encrypted medical record with key: " + encryptionKey);
        return medicalRecord.save();
    }

    private String encrypt(String data) {
        // Mã hóa data
        return "[ENCRYPTED]" + data;
    }
}

// Concrete Decorator 2: Ký số
public class SignedMedicalRecordDecorator extends MedicalRecordDecorator {
    private String signedBy;

    public SignedMedicalRecordDecorator(MedicalRecord record, String signedBy) {
        super(record);
        this.signedBy = signedBy;
    }

    @Override
    public String getRecord() {
        String original = medicalRecord.getRecord();
        return original + " [Signed: " + signedBy + "]";  // Thêm tính năng
    }

    @Override
    public boolean save() {
        System.out.println("Saving signed record by: " + signedBy);
        return medicalRecord.save();
    }
}

// Concrete Decorator 3: Ghi log
public class AuditLogMedicalRecordDecorator extends MedicalRecordDecorator {

    public AuditLogMedicalRecordDecorator(MedicalRecord record) {
        super(record);
    }

    @Override
    public String getRecord() {
        System.out.println("[AUDIT LOG] Record accessed");
        return medicalRecord.getRecord();
    }

    @Override
    public boolean save() {
        System.out.println("[AUDIT LOG] Record saved");
        return medicalRecord.save();
    }
}
```

**Trình Tự Wrapping (Nesting):**

```
Client
    ↓
new AuditLogMedicalRecordDecorator(
    new SignedMedicalRecordDecorator(
        new EncryptedMedicalRecordDecorator(
            new BasicMedicalRecord()
        )
    )
)
    ↓
record.save()
    ↓
AuditLogMedicalRecordDecorator.save()
    ├─ System.out.println("[AUDIT LOG] Record saved")
    ↓
SignedMedicalRecordDecorator.save()
    ├─ System.out.println("Saving signed record...")
    ↓
EncryptedMedicalRecordDecorator.save()
    ├─ System.out.println("Saving encrypted medical record...")
    ↓
BasicMedicalRecord.save()
    ├─ System.out.println("Saving basic medical record")
    └─ return true
    ↓
Kết quả: Tất cả decorator được thực thi
```

**Ví Dụ Sử Dụng:**

```java
// Tạo basic record
MedicalRecord record = new BasicMedicalRecord();

// Thêm tính năng: Mã hóa
record = new EncryptedMedicalRecordDecorator(record, "key123");

// Thêm tính năng: Ký số
record = new SignedMedicalRecordDecorator(record, "Dr. Smith");

// Thêm tính năng: Ghi log
record = new AuditLogMedicalRecordDecorator(record);

// Sử dụng
record.save();
// Output:
// [AUDIT LOG] Record saved
// Saving signed record by: Dr. Smith
// Saving encrypted medical record with key: key123
// Saving basic medical record

String content = record.getRecord();
// Lấy content đã đi qua tất cả decorator
```

**So Sánh Các Decorator Khác Nhau:**

| Decorator | Tính Năng | Mục Đích |
|---|---|---|
| EncryptedMedicalRecordDecorator | Mã hóa nội dung | Bảo mật dữ liệu |
| SignedMedicalRecordDecorator | Thêm chữ ký | Xác minh người tạo |
| AuditLogMedicalRecordDecorator | Ghi log hành động | Kiểm toán, truy vết |

**Mối Liên Hệ Giữa Các File:**

```
Decorator.java/
├── MedicalRecord.java (Interface)
├── BasicMedicalRecord.java (Base Implementation)
├── MedicalRecordDecorator.java (Abstract Decorator)
├── EncryptedMedicalRecordDecorator.java (Concrete Decorator)
├── SignedMedicalRecordDecorator.java (Concrete Decorator)
└── AuditLogMedicalRecordDecorator.java (Concrete Decorator)
```

---

## 7. FACADE PATTERN - Giao Diện Đơn Giản Cho Bệnh Viện

### 📂 File: `HospitalFacade.java`

**Mục đích:** Cung cấp interface đơn giản cho một hệ thống phức tạp.

**Cấu Trúc:**

```java
public class HospitalFacade {
    // Giữu các dependencies phức tạp
    private final PatientDAO patientDAO;
    private final AppointmentDAO appointmentDAO;
    private final BillingDAO billingDAO;

    // Method đơn giản che giấu độ phức tạp
    public Patient registerPatient(Map<String, Object> patientData) {
        Patient patient = new Patient(...);
        patientDAO.save(patient);
        return patient;
    }

    public Appointment bookAppointment(int patientId, int doctorId,
                                       Date date, Time time) {
        Appointment appointment = new Appointment(...);
        appointmentDAO.save(appointment);
        return appointment;
    }

    public Billing processBilling(int appointmentId) {
        // Các bước phức tạp bên trong
        Appointment appointment = appointmentDAO.findById(appointmentId);
        Billing billing = new Billing(...);
        billingDAO.save(billing);
        return billing;
    }

    public Map<String, Object> getPatientRecords(int patientId) {
        Map<String, Object> records = new HashMap<>();
        records.put("patient", patientDAO.findById(patientId));
        return records;
    }
}

// Sử dụng:
HospitalFacade facade = new HospitalFacade(...);
facade.registerPatient(data);
facade.bookAppointment(101, 201, date, time);
facade.processBilling(1);
```

**So Sánh:**

| Trước (Phức Tạp) | Sau (Facade) |
|---|---|
| patientDAO.save() | facade.registerPatient() |
| appointmentDAO.save() | facade.bookAppointment() |
| appointmentDAO.findById() | (bên trong facade) |
| billingDAO.save() | |
| Phải biết 3 DAO | Chỉ biết 1 Facade |

**Lý Do Sử Dụng:**
- ✅ Đơn giản hóa: Client không cần biết DAO phức tạp
- ✅ Decoupling: Thay đổi DAO bên trong không ảnh hưởng client
- ✅ Centralized logic: Tất cả business logic trong Facade
- ✅ API sạch: Dễ sử dụng cho developer khác
- ✅ Kiểm soát thực thi: Có thể log, validate, v.v.

---

## 8. COMMAND PATTERN - Quản Lý Hành Động Appointment

### 📂 Files: `Command.java`, `CommandInvoker.java`, `CreateAppointmentCommand.java`, `UpdateAppointmentCommand.java`, `CancelAppointmentCommand.java`

**Mục đích:** Đóng gói một hành động (request) thành một đối tượng, cho phép undo/redo.

**Cấu Trúc:**

```java
// Command Interface
public interface Command {
    boolean execute();  // Thực hiện
    boolean undo();     // Hoàn tác
}

// Concrete Command 1: Tạo lịch hẹn
public class CreateAppointmentCommand implements Command {
    private Appointment appointment;
    private AppointmentDAO appointmentDAO;

    public CreateAppointmentCommand(AppointmentDAO dao, Appointment apt) {
        this.appointmentDAO = dao;
        this.appointment = apt;
    }

    @Override
    public boolean execute() {
        return appointmentDAO.save(appointment);
    }

    @Override
    public boolean undo() {
        return appointmentDAO.delete(appointment.getId());
    }
}

// Concrete Command 2: Cập nhật lịch hẹn
public class UpdateAppointmentCommand implements Command {
    private Appointment appointment;
    private String oldStatus;

    @Override
    public boolean execute() {
        oldStatus = appointment.getStatus();
        appointment.setStatus("Confirmed");
        return appointmentDAO.update(appointment);
    }

    @Override
    public boolean undo() {
        appointment.setStatus(oldStatus);
        return appointmentDAO.update(appointment);
    }
}

// Invoker: Quản lý các command
public class CommandInvoker {
    private Command command;
    private final Stack<Command> commandHistory;

    public boolean executeCommand() {
        if(command == null) return false;
        boolean result = command.execute();
        if(result) commandHistory.push(command);  // Lưu vào lịch sử
        return result;
    }

    public boolean undo() {
        if(commandHistory.isEmpty()) return false;
        return commandHistory.pop().undo();  // Lấy command cuối cùng và undo
    }
}

// Sử dụng:
CommandInvoker invoker = new CommandInvoker();
invoker.setCommand(new CreateAppointmentCommand(dao, apt));
invoker.executeCommand();  // Tạo appointment

invoker.undo();  // Hoàn tác: xóa appointment
invoker.executeCommand();  // Redo: tạo lại
```

**Quy Trình:**
```
Client
    ↓
setCommand(CreateAppointmentCommand)
    ↓
executeCommand()
    ├─ command.execute() → appointmentDAO.save()
    └─ push vào Stack
    ↓
undo()
    ├─ pop từ Stack
    └─ command.undo() → appointmentDAO.delete()
```

**Lý Do Sử Dụng:**
- ✅ Undo/Redo: Dễ dàng hoàn tác hành động
- ✅ Queuing: Có thể queue các command để thực hiện sau
- ✅ Logging: Ghi lại tất cả hành động (audit trail)
- ✅ Decoupling: Sender (invoker) không cần biết receiver
- ✅ Macro commands: Có thể tổ hợp các command lớn

---

### 🔍 Cơ Chế Hoạt Động Chi Tiết

**Sơ Đồ Interaction:**

```
┌──────────────────┐
│  Command         │
│  (Interface)     │
├──────────────────┤
│ + execute()      │
│ + undo()         │
└──────────────────┘
    ↑       ↑       ↑
    │       │       │
┌───┴──┐ ┌──┴──┐ ┌──┴───┐
│Create│ │Update│ │Cancel│
│Appt  │ │Appt  │ │Appt  │
└──────┘ └──────┘ └──────┘

┌──────────────────────┐
│  CommandInvoker      │
│  (Invoker)           │
├──────────────────────┤
│ - command: Command   │
│ - history: Stack     │
├──────────────────────┤
│ + setCommand()       │
│ + executeCommand()   │
│ + undo()             │
└──────────────────────┘
```

**Trình Tự Thực Thi:**

```
Step 1: Tạo Command
-------------------
Command cmd = new CreateAppointmentCommand(dao, appointment);

Step 2: Đặt Command vào Invoker
-------------------------------
invoker.setCommand(cmd);

Step 3: Thực Thi Command
------------------------
invoker.executeCommand()
    ├─ command.execute()
    │   └─ appointmentDAO.save(appointment)
    │       └─ INSERT appointment vào DB
    ├─ push command vào Stack
    └─ return true

State: commandHistory = [CreateAppointmentCommand]

Step 4: Undo Command
--------------------
invoker.undo()
    ├─ pop command từ Stack
    │   └─ CreateAppointmentCommand
    └─ command.undo()
        └─ appointmentDAO.delete(appointment.getId())
            └─ DELETE appointment từ DB

State: commandHistory = []
```

**Chi Tiết Code Invoker:**

```java
public class CommandInvoker {
    private Command command;
    private final Stack<Command> commandHistory;

    public CommandInvoker() {
        this.commandHistory = new Stack<>();
    }

    public void setCommand(Command command) {
        this.command = command;  // Set command để execute
    }

    public boolean executeCommand() {
        if(command == null) return false;

        boolean result = command.execute();  // Thực hiện command
        if(result) {
            commandHistory.push(command);    // Lưu vào history nếu thành công
        }
        return result;
    }

    public boolean undo() {
        if(commandHistory.isEmpty()) return false;

        Command lastCommand = commandHistory.pop();  // Lấy command cuối
        return lastCommand.undo();                   // Gọi undo trên command đó
    }

    public boolean hasHistory() {
        return !commandHistory.isEmpty();
    }

    public void clearHistory() {
        commandHistory.clear();
    }
}
```

**Ví Dụ Sử Dụng:**

```java
// Tạo invoker
CommandInvoker invoker = new CommandInvoker();

// Command 1: Tạo appointment
Appointment apt = new Appointment(0, 101, 201);
invoker.setCommand(new CreateAppointmentCommand(dao, apt));
invoker.executeCommand();  // Save appointment
// Lịch sử: [CreateAppointmentCommand]

// Command 2: Cập nhật status
apt.setId(1);
invoker.setCommand(new UpdateAppointmentCommand(dao, apt));
invoker.executeCommand();  // Update status → Confirmed
// Lịch sử: [CreateAppointmentCommand, UpdateAppointmentCommand]

// Undo Command 2
invoker.undo();  // Status → Scheduled (quay lại trạng thái cũ)
// Lịch sử: [CreateAppointmentCommand]

// Undo Command 1
invoker.undo();  // Delete appointment
// Lịch sử: []
```

**Mối Liên Hệ Giữa Các File:**

```
Command.java/
├── Command.java (Interface)
├── CommandInvoker.java (Invoker)
├── CreateAppointmentCommand.java (Concrete Command)
├── UpdateAppointmentCommand.java (Concrete Command)
├── CancelAppointmentCommand.java (Concrete Command)
└── Appointment.java (Receiver - được tác động)
```

---
- ✅ Decoupling: Sender (invoker) không cần biết receiver
- ✅ Macro commands: Có thể tổ hợp các command lớn

---

## 9. OBSERVER PATTERN - Thông Báo Về Thay Đổi Appointment

### 📂 Files: `Observer.java`, `AppointmentSubject.java`, `AppointmentObservable.java`, `PatientObserver.java`, `DoctorObserver.java`, `SMSNotificationObserver.java`

**Mục đích:** Thiết lập mối quan hệ one-to-many sao cho khi một đối tượng thay đổi, tất cả phụ thuộc được thông báo.

**Cấu Trúc:**

```java
// Observer Interface
public interface Observer {
    void update(Appointment appointment);  // Được gọi khi appointment thay đổi
}

// Subject Interface
public interface AppointmentSubject {
    void attach(Observer observer);    // Đăng ký observer
    void detach(Observer observer);    // Hủy đăng ký
    void notifyObservers();            // Thông báo tất cả observer
}

// Concrete Subject
public class AppointmentObservable implements AppointmentSubject {
    private Appointment appointment;
    private List<Observer> observers = new ArrayList<>();

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(Observer observer : observers) {
            observer.update(appointment);  // Thông báo từng observer
        }
    }

    public void setAppointmentStatus(String newStatus) {
        appointment.setStatus(newStatus);
        notifyObservers();  // Tự động thông báo khi status thay đổi
    }
}

// Concrete Observer 1: Thông báo cho bệnh nhân
public class PatientObserver implements Observer {
    private int patientId;

    @Override
    public void update(Appointment appointment) {
        System.out.println("Patient " + patientId +
                         " notified: Appointment updated");
    }
}

// Concrete Observer 2: Thông báo cho bác sĩ
public class DoctorObserver implements Observer {
    private int doctorId;

    @Override
    public void update(Appointment appointment) {
        System.out.println("Doctor " + doctorId +
                         " notified: Appointment updated");
    }
}

// Concrete Observer 3: Gửi SMS
public class SMSNotificationObserver implements Observer {
    private String phoneNumber;

    @Override
    public void update(Appointment appointment) {
        System.out.println("SMS sent to " + phoneNumber +
                         ": Appointment " + appointment.getId() + " updated");
    }
}

// Sử dụng:
AppointmentObservable observable = new AppointmentObservable();
observable.attach(new PatientObserver(101));
observable.attach(new DoctorObserver(201));
observable.attach(new SMSNotificationObserver("0123456789"));

// Khi appointment thay đổi
observable.setAppointmentStatus("Confirmed");
// → Tất cả observer được thông báo tự động
```

**Luồng Thông Báo:**
```
setAppointmentStatus("Confirmed")
    ↓
notifyObservers()
    ├─ PatientObserver.update() → "Patient 101 notified"
    ├─ DoctorObserver.update() → "Doctor 201 notified"
    └─ SMSNotificationObserver.update() → "SMS sent"
```

**Lý Do Sử Dụng:**
- ✅ Loose coupling: Subject không biết chi tiết observer
- ✅ Dynamic: Có thể attach/detach observer runtime
- ✅ Automatic: Khi thay đổi, tự động thông báo tất cả
- ✅ Broadcast: Nhiều subscriber nhận thông báo cùng lúc
- ✅ Event-driven: Model hệ thống sự kiện

---

## 10. STATE PATTERN - Quản Lý Trạng Thái Appointment

### 📂 Files: `AppointmentState.java`, `AppointmentContext.java`, `ScheduledState.java`, `ConfirmedState.java`, `CompletedState.java`, `CancelledState.java`

**Mục đích:** Cho phép một đối tượng thay đổi hành vi khi trạng thái nội bộ thay đổi.

**Cấu Trúc:**

```java
// State Interface
public interface AppointmentState {
    void handle(AppointmentContext context);
    String getStatus();
}

// Context: Giữ state hiện tại
public class AppointmentContext {
    private AppointmentState state;
    private Appointment appointment;

    public void setState(AppointmentState state) {
        this.state = state;
    }

    public void request() {
        if(state != null) {
            state.handle(this);  // Delegate cho state
        }
    }
}

// Concrete State 1: Scheduled (Chưa xác nhận)
public class ScheduledState implements AppointmentState {
    @Override
    public void handle(AppointmentContext context) {
        System.out.println("Appointment is scheduled");
        context.setState(new ConfirmedState());  // Chuyển sang Confirmed
    }

    @Override
    public String getStatus() {
        return "Scheduled";
    }
}

// Concrete State 2: Confirmed (Đã xác nhận)
public class ConfirmedState implements AppointmentState {
    @Override
    public void handle(AppointmentContext context) {
        System.out.println("Appointment is confirmed");
        context.setState(new CompletedState());  // Chuyển sang Completed
    }

    @Override
    public String getStatus() {
        return "Confirmed";
    }
}

// Concrete State 3: Completed (Hoàn thành)
public class CompletedState implements AppointmentState {
    @Override
    public void handle(AppointmentContext context) {
        System.out.println("Appointment is completed");
        // Không chuyển state nữa (terminal state)
    }

    @Override
    public String getStatus() {
        return "Completed";
    }
}

// Concrete State 4: Cancelled (Hủy)
public class CancelledState implements AppointmentState {
    @Override
    public void handle(AppointmentContext context) {
        System.out.println("Appointment is cancelled");
    }

    @Override
    public String getStatus() {
        return "Cancelled";
    }
}

// Sử dụng:
AppointmentContext context = new AppointmentContext(appointment);
context.setState(new ScheduledState());

context.request();  // "Appointment is scheduled" → Scheduled → Confirmed
context.request();  // "Appointment is confirmed" → Confirmed → Completed
context.request();  // "Appointment is completed"
```

**State Machine Diagram:**
```
Scheduled
    ↓ (request)
Confirmed
    ↓ (request)
Completed

CancelledState (có thể vào từ bất kỳ state nào)
```

**Lý Do Sử Dụng:**
- ✅ Quản lý trạng thái phức tạp: Tránh if-else dài dòng
- ✅ Encapsulation: Mỗi state tự chịu trách nhiệm hành vi của nó
- ✅ Dễ thêm state: Chỉ cần tạo class mới implement AppointmentState
- ✅ Transition logic: Logic chuyển state được định nghĩa rõ ràng
- ✅ Follows Single Responsibility: Mỗi state làm một việc

---

## 11. STRATEGY PATTERN - Chiến Lược Thanh Toán

### 📂 Files: `PaymentStrategy.java`, `CashPaymentStrategy.java`, `CreditCardPaymentStrategy.java`, `InsurancePaymentStrategy.java`, `PaymentProcessor.java`

**Mục đích:** Định nghĩa một họ các thuật toán, gói gọn từng cái, và làm cho chúng có thể hoán đổi.

**Cấu Trúc:**

```java
// Strategy Interface
public interface PaymentStrategy {
    boolean pay(BigDecimal amount);
    String getPaymentMethod();
}

// Concrete Strategy 1: Thanh toán bằng tiền mặt
public class CashPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean pay(BigDecimal amount) {
        System.out.println("Paid " + amount + " in cash");
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "Cash";
    }
}

// Concrete Strategy 2: Thanh toán bằng thẻ tín dụng
public class CreditCardPaymentStrategy implements PaymentStrategy {
    private String cardNumber;
    private String cardHolder;

    public CreditCardPaymentStrategy(String cardNumber, String cardHolder) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
    }

    @Override
    public boolean pay(BigDecimal amount) {
        System.out.println("Paid " + amount + " with credit card " +
                         cardNumber + " by " + cardHolder);
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "Credit Card";
    }
}

// Concrete Strategy 3: Thanh toán qua bảo hiểm
public class InsurancePaymentStrategy implements PaymentStrategy {
    private String insuranceProvider;
    private String policyNumber;

    @Override
    public boolean pay(BigDecimal amount) {
        System.out.println("Paid " + amount + " via insurance " +
                         insuranceProvider + " (Policy: " + policyNumber + ")");
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "Insurance";
    }
}

// Context: Sử dụng Strategy
public class PaymentProcessor {
    private PaymentStrategy strategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;  // Có thể thay đổi strategy runtime
    }

    public boolean processPayment(BigDecimal amount) {
        if(strategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }
        return strategy.pay(amount);
    }
}

// Sử dụng:
PaymentProcessor processor = new PaymentProcessor();

// Thanh toán bằng tiền mặt
processor.setPaymentStrategy(new CashPaymentStrategy());
processor.processPayment(new BigDecimal("150.00"));

// Đổi sang thẻ tín dụng (runtime!)
processor.setPaymentStrategy(
    new CreditCardPaymentStrategy("5678", "Nguyễn Văn A")
);
processor.processPayment(new BigDecimal("500.00"));

// Đổi sang bảo hiểm
processor.setPaymentStrategy(
    new InsurancePaymentStrategy("Bảo Việt", "POL123")
);
processor.processPayment(new BigDecimal("300.00"));
```

**So Sánh Strategy vs If-Else:**

| If-Else (Cũ) | Strategy (Mới) |
|---|---|
| `if(type == "cash") pay_cash()` | `setPaymentStrategy(new CashPaymentStrategy())` |
| `else if(type == "card") pay_card()` | `setPaymentStrategy(new CreditCardPaymentStrategy())` |
| `else if(type == "insurance") pay_insurance()` | `setPaymentStrategy(new InsurancePaymentStrategy())` |
| Thêm loại → sửa if-else | Thêm loại → tạo class mới |
| Khó test từng case | Dễ test từng strategy |

**Lý Do Sử Dụng:**
- ✅ Runtime linh hoạt: Thay đổi strategy khi ứng dụng đang chạy
- ✅ Tránh if-else: Thay vì 10 dòng điều kiện, chỉ 1 dòng setPaymentStrategy
- ✅ Dễ mở rộng: Thêm InsurancePaymentStrategy không ảnh hưởng code cũ
- ✅ Dễ test: Mock từng strategy riêng lẻ
- ✅ Single Responsibility: Mỗi strategy chỉ xử lý một loại thanh toán

---

## 12. TEMPLATE METHOD PATTERN - Tạo Báo Cáo

### 📂 Files: `MedicalReport.java`, `PatientReportGenerator.java`, `AppointmentReportGenerator.java`, `BillingReportGenerator.java`

**Mục đích:** Định nghĩa skeleton (khung) của một thuật toán trong method, để subclass định nghĩa chi tiết từng bước.

**Cấu Trúc:**

```java
// Abstract class định nghĩa template
public abstract class MedicalReport {
    protected int patientId;
    protected Date reportDate;

    // Template method: Định nghĩa skeleton
    public final String generateReport() {
        Map<String, Object> data = collectData();      // Bước 1
        String report = formatReport(data);             // Bước 2
        if(validateReport(report)) {                    // Bước 3
            return report;
        }
        return "Invalid report";
    }

    // Hook methods: Subclass sẽ implement
    protected abstract Map<String, Object> collectData();
    protected abstract String formatReport(Map<String, Object> data);
    protected abstract boolean validateReport(String report);
}

// Concrete Implementation 1: Báo cáo bệnh nhân
public class PatientReportGenerator extends MedicalReport {
    public PatientReportGenerator(int patientId, Date reportDate) {
        this.patientId = patientId;
        this.reportDate = reportDate;
    }

    @Override
    protected Map<String, Object> collectData() {
        // Lấy dữ liệu bệnh nhân từ DB
        PatientDAO dao = new StandardPatientDAO();
        Patient patient = dao.findById(patientId);

        Map<String, Object> data = new HashMap<>();
        data.put("patient", patient);
        data.put("patientName", patient.getFirstName() + " " + patient.getLastName());
        return data;
    }

    @Override
    protected String formatReport(Map<String, Object> data) {
        // Format theo định dạng báo cáo bệnh nhân
        Patient patient = (Patient) data.get("patient");
        return "Patient Report:\n" +
               "Name: " + data.get("patientName") + "\n" +
               "Date: " + reportDate;
    }

    @Override
    protected boolean validateReport(String report) {
        // Kiểm tra dữ liệu bệnh nhân hợp lệ
        return report != null && !report.isEmpty() && report.contains("Patient Report");
    }
}

// Concrete Implementation 2: Báo cáo lịch hẹn
public class AppointmentReportGenerator extends MedicalReport {
    public AppointmentReportGenerator(int patientId, Date reportDate) {
        this.patientId = patientId;
        this.reportDate = reportDate;
    }

    @Override
    protected Map<String, Object> collectData() {
        // Lấy dữ liệu lịch hẹn
        AppointmentDAO dao = new StandardAppointmentDAO();
        Appointment appointment = dao.findById(patientId);

        Map<String, Object> data = new HashMap<>();
        data.put("appointment", appointment);
        return data;
    }

    @Override
    protected String formatReport(Map<String, Object> data) {
        // Format theo định dạng báo cáo lịch hẹn
        Appointment apt = (Appointment) data.get("appointment");
        return "Appointment Report:\n" +
               "Patient ID: " + apt.getPatientId() + "\n" +
               "Doctor ID: " + apt.getDoctorId();
    }

    @Override
    protected boolean validateReport(String report) {
        return report != null && !report.isEmpty() && report.contains("Appointment Report");
    }
}

// Concrete Implementation 3: Báo cáo hóa đơn
public class BillingReportGenerator extends MedicalReport {
    @Override
    protected Map<String, Object> collectData() {
        // Lấy dữ liệu hóa đơn
        ...
    }

    @Override
    protected String formatReport(Map<String, Object> data) {
        // Format theo định dạng hóa đơn
        ...
    }

    @Override
    protected boolean validateReport(String report) {
        // Kiểm tra hóa đơn hợp lệ
        ...
    }
}

// Sử dụng:
// Tạo báo cáo bệnh nhân
MedicalReport report = new PatientReportGenerator(101, new Date());
String result = report.generateReport();
// → Tự động gọi: collectData() → formatReport() → validateReport()

// Tạo báo cáo lịch hẹn - cùng quy trình nhưng logic khác
MedicalReport aptReport = new AppointmentReportGenerator(102, new Date());
String aptResult = aptReport.generateReport();
```

**Quy Trình:**
```
generateReport() [Template method]
    ↓
collectData() [Subclass implement]
    ↓
formatReport() [Subclass implement]
    ↓
validateReport() [Subclass implement]
    ↓
return report
```

**Các Bước So Sánh:**

| PatientReportGenerator | AppointmentReportGenerator | BillingReportGenerator |
|---|---|---|
| collectData: từ PatientDAO | collectData: từ AppointmentDAO | collectData: từ BillingDAO |
| formatReport: format bệnh nhân | formatReport: format lịch hẹn | formatReport: format hóa đơn |
| validateReport: kiểm tra bệnh nhân | validateReport: kiểm tra lịch hẹn | validateReport: kiểm tra hóa đơn |

**Lý Do Sử Dụng:**
- ✅ Code reuse: Skeleton chung không lặp lại
- ✅ Kiểm soát thứ tự: Bước nào trước, bước nào sau được định nghĩa rõ
- ✅ Dễ thêm báo cáo mới: Chỉ implement 3 abstract method
- ✅ Tránh break flow: Subclass không thể phá vỡ quy trình chính
- ✅ Hollywood Principle: "Don't call us, we'll call you"

---

## 📊 Bảng Tóm Tắt Các Pattern

| Pattern | Mục Đích | Lợi Ích |
|---|---|---|
| **Singleton** | Một instance duy nhất | Tiết kiệm tài nguyên |
| **Factory Method** | Tạo object mà không biết class cụ thể | Linh hoạt, dễ mở rộng |
| **Abstract Factory** | Tạo họ đối tượng liên quan | Đảm bảo tương thích |
| **Builder** | Xây dựng object phức tạp từng bước | Dễ đọc, linh hoạt |
| **Adapter** | Chuyển đổi interface không tương thích | Tích hợp code cũ |
| **Decorator** | Thêm tính năng động | Tránh explosion, composable |
| **Facade** | Interface đơn giản cho hệ thống phức tạp | Dễ sử dụng |
| **Command** | Đóng gói request thành object | Undo/Redo, logging |
| **Observer** | Thông báo tự động khi thay đổi | Loose coupling, event-driven |
| **State** | Thay đổi hành vi theo trạng thái | Tránh if-else, quản lý state |
| **Strategy** | Hoán đổi thuật toán runtime | Linh hoạt, dễ test |
| **Template Method** | Define skeleton, subclass fill detail | Code reuse, kiểm soát flow |

---

## 🔧 Cách Chạy & Test

```bash
# Build dự án
mvn clean compile

# Chạy test
mvn test

# Test một pattern cụ thể
mvn test -Dtest=SingletonTest

# Build JAR
mvn package
```

---

## 📝 Kết Luận

Dự án này là một ví dụ tuyệt vời về cách áp dụng Design Patterns vào thực tế. Mỗi pattern được sử dụng để giải quyết một vấn đề cụ thể:

- **Singleton**: Kết nối database
- **Factory Method**: Tạo nhân sự
- **Abstract Factory**: Tạo DAO families
- **Builder**: Xây dựng bệnh nhân
- **Adapter**: Tích hợp thanh toán cũ
- **Decorator**: Mã hóa, ký, ghi log record
- **Facade**: API đơn giản
- **Command**: Undo appointment
- **Observer**: Thông báo tự động
- **State**: Quản lý trạng thái appointment
- **Strategy**: Nhiều cách thanh toán
- **Template Method**: Tạo nhiều loại báo cáo

Học cách sử dụng chúng sẽ giúp bạn viết code tốt hơn, dễ maintain hơn, và dễ mở rộng hơn!
