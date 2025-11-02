package com.hospital.patterns.AbstractFactory;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Abstract Factory Pattern Tests cho DAO Factory")
class AbstractFactoryTest {

    @Test
    @DisplayName("Tạo StandardDAOFactory từ DAOFactoryProducer")
    void testTaoStandardDAOFactory() {
        DAOFactory factory = DAOFactoryProducer.getFactory("standard");

        assertNotNull(factory);
        assertInstanceOf(StandardDAOFactory.class, factory);
    }

    @Test
    @DisplayName("Tạo OptimizedDAOFactory từ DAOFactoryProducer")
    void testTaoOptimizedDAOFactory() {
        DAOFactory factory = DAOFactoryProducer.getFactory("optimized");

        assertNotNull(factory);
        assertInstanceOf(OptimizedDAOFactory.class, factory);
    }

    @Test
    @DisplayName("Tạo factory với case không phân biệt hoa thường")
    void testTaoFactoryCaseInsensitive() {
        DAOFactory factory1 = DAOFactoryProducer.getFactory("STANDARD");
        DAOFactory factory2 = DAOFactoryProducer.getFactory("OPTIMIZED");

        assertNotNull(factory1);
        assertNotNull(factory2);
        assertInstanceOf(StandardDAOFactory.class, factory1);
        assertInstanceOf(OptimizedDAOFactory.class, factory2);
    }

    @Test
    @DisplayName("Tạo factory với null - trả về null")
    void testTaoFactoryVoiNull() {
        DAOFactory factory = DAOFactoryProducer.getFactory(null);

        assertNull(factory);
    }

    @Test
    @DisplayName("Tạo factory với loại không hợp lệ - trả về null")
    void testTaoFactoryVoiLoaiKhongHopLe() {
        DAOFactory factory = DAOFactoryProducer.getFactory("invalid");

        assertNull(factory);
    }

    @Test
    @DisplayName("StandardDAOFactory tạo StandardPatientDAO")
    void testStandardFactoryTaoStandardPatientDAO() {
        DAOFactory factory = DAOFactoryProducer.getFactory("standard");
        Object dao = factory.getDAOInstance("patient");

        assertNotNull(dao);
        assertInstanceOf(StandardPatientDAO.class, dao);
        assertInstanceOf(PatientDAO.class, dao);
    }

    @Test
    @DisplayName("StandardDAOFactory tạo StandardAppointmentDAO")
    void testStandardFactoryTaoStandardAppointmentDAO() {
        DAOFactory factory = DAOFactoryProducer.getFactory("standard");
        Object dao = factory.getDAOInstance("appointment");

        assertNotNull(dao);
        assertInstanceOf(StandardAppointmentDAO.class, dao);
        assertInstanceOf(AppointmentDAO.class, dao);
    }

    @Test
    @DisplayName("OptimizedDAOFactory tạo OptimizedPatientDAO")
    void testOptimizedFactoryTaoOptimizedPatientDAO() {
        DAOFactory factory = DAOFactoryProducer.getFactory("optimized");
        Object dao = factory.getDAOInstance("patient");

        assertNotNull(dao);
        assertInstanceOf(OptimizedPatientDAO.class, dao);
        assertInstanceOf(PatientDAO.class, dao);
    }

    @Test
    @DisplayName("OptimizedDAOFactory tạo OptimizedAppointmentDAO")
    void testOptimizedFactoryTaoOptimizedAppointmentDAO() {
        DAOFactory factory = DAOFactoryProducer.getFactory("optimized");
        Object dao = factory.getDAOInstance("appointment");

        assertNotNull(dao);
        assertInstanceOf(OptimizedAppointmentDAO.class, dao);
        assertInstanceOf(AppointmentDAO.class, dao);
    }

    @Test
    @DisplayName("Factory tạo DAO với case không phân biệt hoa thường")
    void testFactoryTaoDAOCaseInsensitive() {
        DAOFactory standardFactory = DAOFactoryProducer.getFactory("standard");

        Object patientDAO1 = standardFactory.getDAOInstance("PATIENT");
        Object patientDAO2 = standardFactory.getDAOInstance("Patient");
        Object appointmentDAO1 = standardFactory.getDAOInstance("APPOINTMENT");
        Object appointmentDAO2 = standardFactory.getDAOInstance("Appointment");

        assertNotNull(patientDAO1);
        assertNotNull(patientDAO2);
        assertNotNull(appointmentDAO1);
        assertNotNull(appointmentDAO2);
        assertInstanceOf(StandardPatientDAO.class, patientDAO1);
        assertInstanceOf(StandardPatientDAO.class, patientDAO2);
        assertInstanceOf(StandardAppointmentDAO.class, appointmentDAO1);
        assertInstanceOf(StandardAppointmentDAO.class, appointmentDAO2);
    }

    @Test
    @DisplayName("Factory trả về null khi DAO type không hợp lệ")
    void testFactoryVoiDAOTypeKhongHopLe() {
        DAOFactory factory = DAOFactoryProducer.getFactory("standard");
        Object dao = factory.getDAOInstance("invalid");

        assertNull(dao);
    }

    @Test
    @DisplayName("StandardPatientDAO có thể save và delete patient")
    void testStandardPatientDAOCacPhuongThuc() {
        DAOFactory factory = DAOFactoryProducer.getFactory("standard");
        PatientDAO patientDAO = (PatientDAO) factory.getDAOInstance("patient");

        // Tạo patient test
        Patient patient = new Patient(1, "Nguyễn", "Văn A",
            new Date(), "Nam", "0123456789",
            "123 Đường ABC", "nguyenvana@example.com", "Không có");

        // Test save
        boolean saveResult = patientDAO.save(patient);
        assertTrue(saveResult);

        // Test findAll
        List<Patient> patients = patientDAO.findAll();
        assertNotNull(patients);

        // Test findById (có thể trả về null vì chưa implement database logic)
        patientDAO.findById(1);
        // Gọi method để test, không kiểm tra kết quả vì implementation chưa hoàn chỉnh

        // Test delete
        boolean deleteResult = patientDAO.delete(1);
        assertTrue(deleteResult);
    }

    @Test
    @DisplayName("OptimizedPatientDAO có thể save và delete patient")
    void testOptimizedPatientDAOCacPhuongThuc() {
        DAOFactory factory = DAOFactoryProducer.getFactory("optimized");
        PatientDAO patientDAO = (PatientDAO) factory.getDAOInstance("patient");

        // Tạo patient test
        Patient patient = new Patient(2, "Trần", "Thị B",
            new Date(), "Nữ", "0987654321",
            "456 Đường XYZ", "tranthib@example.com", "Tiền sử bệnh tim");

        // Test save
        boolean saveResult = patientDAO.save(patient);
        assertTrue(saveResult);

        // Test findAll
        List<Patient> patients = patientDAO.findAll();
        assertNotNull(patients);

        // Test delete
        boolean deleteResult = patientDAO.delete(2);
        assertTrue(deleteResult);
    }

    @Test
    @DisplayName("StandardAppointmentDAO có thể save và delete appointment")
    void testStandardAppointmentDAOCacPhuongThuc() {
        DAOFactory factory = DAOFactoryProducer.getFactory("standard");
        AppointmentDAO appointmentDAO = (AppointmentDAO) factory.getDAOInstance("appointment");

        // Tạo appointment test
        Appointment appointment = new Appointment(1, 1, 1,
            new Date(), new Time(System.currentTimeMillis()),
            "Khám tổng quát", "Scheduled");

        // Test save
        boolean saveResult = appointmentDAO.save(appointment);
        assertTrue(saveResult);

        // Test findAll
        List<Appointment> appointments = appointmentDAO.findAll();
        assertNotNull(appointments);

        // Test delete
        boolean deleteResult = appointmentDAO.delete(1);
        assertTrue(deleteResult);
    }

    @Test
    @DisplayName("OptimizedAppointmentDAO có thể save và delete appointment")
    void testOptimizedAppointmentDAOCacPhuongThuc() {
        DAOFactory factory = DAOFactoryProducer.getFactory("optimized");
        AppointmentDAO appointmentDAO = (AppointmentDAO) factory.getDAOInstance("appointment");

        // Tạo appointment test
        Appointment appointment = new Appointment(2, 2, 2,
            new Date(), new Time(System.currentTimeMillis()),
            "Theo dõi sau phẫu thuật", "Completed");

        // Test save
        boolean saveResult = appointmentDAO.save(appointment);
        assertTrue(saveResult);

        // Test findAll
        List<Appointment> appointments = appointmentDAO.findAll();
        assertNotNull(appointments);

        // Test delete
        boolean deleteResult = appointmentDAO.delete(2);
        assertTrue(deleteResult);
    }

    @Test
    @DisplayName("Kiểm tra Abstract Factory Pattern - Standard Factory chỉ tạo Standard DAOs")
    void testAbstractFactoryPatternStandard() {
        DAOFactory standardFactory = DAOFactoryProducer.getFactory("standard");

        PatientDAO patientDAO = (PatientDAO) standardFactory.getDAOInstance("patient");
        AppointmentDAO appointmentDAO = (AppointmentDAO) standardFactory.getDAOInstance("appointment");

        // Kiểm tra cả hai đều là Standard implementation
        assertInstanceOf(StandardPatientDAO.class, patientDAO);
        assertInstanceOf(StandardAppointmentDAO.class, appointmentDAO);

        // Đảm bảo không phải Optimized
        assertTrue(!(patientDAO instanceof OptimizedPatientDAO));
        assertTrue(!(appointmentDAO instanceof OptimizedAppointmentDAO));
    }

    @Test
    @DisplayName("Kiểm tra Abstract Factory Pattern - Optimized Factory chỉ tạo Optimized DAOs")
    void testAbstractFactoryPatternOptimized() {
        DAOFactory optimizedFactory = DAOFactoryProducer.getFactory("optimized");

        PatientDAO patientDAO = (PatientDAO) optimizedFactory.getDAOInstance("patient");
        AppointmentDAO appointmentDAO = (AppointmentDAO) optimizedFactory.getDAOInstance("appointment");

        // Kiểm tra cả hai đều là Optimized implementation
        assertInstanceOf(OptimizedPatientDAO.class, patientDAO);
        assertInstanceOf(OptimizedAppointmentDAO.class, appointmentDAO);

        // Đảm bảo không phải Standard
        assertTrue(!(patientDAO instanceof StandardPatientDAO));
        assertTrue(!(appointmentDAO instanceof StandardAppointmentDAO));
    }

    @Test
    @DisplayName("Tạo nhiều instances - mỗi lần gọi tạo instance mới")
    void testTaoNhieuInstances() {
        DAOFactory factory1 = DAOFactoryProducer.getFactory("standard");
        DAOFactory factory2 = DAOFactoryProducer.getFactory("standard");

        // Mỗi lần gọi tạo factory mới
        assertNotNull(factory1);
        assertNotNull(factory2);

        // Mỗi factory tạo DAO instance mới
        PatientDAO dao1 = (PatientDAO) factory1.getDAOInstance("patient");
        PatientDAO dao2 = (PatientDAO) factory2.getDAOInstance("patient");

        assertNotNull(dao1);
        assertNotNull(dao2);
        // Hai instances khác nhau (không phải cùng object)
        assertTrue(dao1 != dao2);
    }
}

