# 🏥 Hospital Design Patterns

Dự án Java minh họa 12 Design Patterns qua hệ thống quản lý bệnh viện.

## 📋 Yêu cầu

- Java 17+
- Maven 3.6+
- PostgreSQL 12+

## 🚀 Cài đặt nhanh

### 1. Tạo Database
```bash
createdb hospital_db
psql hospital_db -f database/schema.sql
psql hospital_db -f database/sample_data.sql
```

### 2. Cấu hình
Chỉnh file `src/main/resources/database.properties`:
```properties
db.url=jdbc:postgresql://localhost:5432/hospital_db
db.username=hospital_user
db.password=hospital_pass
```

### 3. Build & Test
```bash
mvn clean compile
mvn test
```

## 📦 12 Design Patterns

### Creational (5 patterns)
1. ✅ **Singleton** - DatabaseConnection
2. **Factory Method** - StaffFactory
3. **Abstract Factory** - DepartmentFactory
4. **Builder** - PatientBuilder
5. **Prototype** - PatientPrototype

### Structural (4 patterns)
6. **Adapter** - BillingAdapter
7. **Decorator** - MedicalRecordDecorator
8. **Facade** - HospitalFacade
9. **Proxy** - MedicalRecordProxy

### Behavioral (3 patterns)
10. **Observer** - AppointmentSubject
11. **Strategy** - PaymentStrategy
12. **Command** - AppointmentCommand

## 📁 Cấu trúc thư mục

```
src/
├── main/java/com/hospital/patterns/
│   ├── creational/     # 5 Creational patterns
│   ├── structural/     # 4 Structural patterns
│   ├── behavioral/     # 3 Behavioral patterns
│   ├── database/       # DatabaseConnection (Singleton)
│   └── models/         # Domain models
└── test/               # Test cases
```

## 🧪 Testing

Mỗi pattern có test riêng:
```bash
# Test tất cả
mvn test

# Test một pattern cụ thể
mvn test -Dtest=DatabaseConnectionTest
```

## 📝 License

MIT License
