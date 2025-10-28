CREATE TABLE Patients (
    patient_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender CHAR(1) CHECK (gender IN ('M', 'F', 'O')),
    contact_number VARCHAR(15),
    address VARCHAR(255),
    email VARCHAR(100),
    medical_history TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index for patient name
CREATE INDEX idx_patient_name ON Patients (last_name, first_name);

CREATE TABLE Doctors (
    doctor_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    specialty VARCHAR(100) NOT NULL,
    contact_number VARCHAR(15),
    email VARCHAR(100),
    available_schedule TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index for specialty
CREATE INDEX idx_specialty ON Doctors (specialty);

CREATE TABLE Departments (
    department_id SERIAL PRIMARY KEY,
    department_name VARCHAR(50),
    location VARCHAR(100)
);

-- Creating Doctor_Department Junction Table (Many-to-Many: Doctors & Departments)
CREATE TABLE Doctor_Department (
    doctor_id INTEGER,
    department_id INTEGER,
    PRIMARY KEY (doctor_id, department_id),
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES Departments(department_id) ON DELETE CASCADE
);

CREATE TABLE Appointments (
    appointment_id SERIAL PRIMARY KEY,
    patient_id INTEGER NOT NULL,
    doctor_id INTEGER,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    purpose VARCHAR(255),
    status VARCHAR(20) DEFAULT 'Scheduled',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE SET NULL
);

-- Create index for appointment date
CREATE INDEX idx_appointment_date ON Appointments (appointment_date, appointment_time);

CREATE TABLE Medical_Records (
    record_id SERIAL PRIMARY KEY,
    patient_id INTEGER NOT NULL,
    doctor_id INTEGER,
    appointment_id INTEGER,
    diagnosis TEXT,
    treatment TEXT,
    prescription TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id) ON DELETE SET NULL,
    FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id) ON DELETE NO ACTION
);

-- Create index for record patient
CREATE INDEX idx_record_patient ON Medical_Records (patient_id);

CREATE TABLE Billing (
    bill_id SERIAL PRIMARY KEY,
    patient_id INTEGER NOT NULL,
    appointment_id INTEGER,
    total_amount DECIMAL(10, 2) NOT NULL,
    payment_status VARCHAR(20) DEFAULT 'Pending',
    payment_date DATE,
    insurance_provider VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES Appointments(appointment_id) ON DELETE NO ACTION
);

-- Create index for payment status
CREATE INDEX idx_payment_status ON Billing (payment_status);

-- Creating Staff Table (General staff details, including nurses and workers)
CREATE TABLE Staff (
    staff_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    role VARCHAR(20) NOT NULL CHECK (role IN ('Nurse', 'Worker', 'Admin', 'Pharmacist', 'Technician', 'Lab Assistant', 'Driver')),
    department_id INTEGER,
    contact_number VARCHAR(15),
    email VARCHAR(50),
    address TEXT,
    hire_date DATE,
    FOREIGN KEY (department_id) REFERENCES Departments(department_id) ON DELETE SET NULL
);

-- Create index for staff role
CREATE INDEX idx_staff_role ON Staff (role);

CREATE TABLE Nurses (
    nurse_id SERIAL PRIMARY KEY,
    staff_id INTEGER NOT NULL,
    specialization VARCHAR(50),
    shift_hours TEXT,
    FOREIGN KEY (staff_id) REFERENCES Staff(staff_id) ON DELETE CASCADE
);

CREATE TABLE Workers (
    worker_id SERIAL PRIMARY KEY,
    staff_id INTEGER,
    job_title VARCHAR(50),
    work_schedule TEXT,
    FOREIGN KEY (staff_id) REFERENCES Staff(staff_id) ON DELETE CASCADE
);

CREATE TABLE Medicine (
    medicine_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    brand VARCHAR(50),
    type VARCHAR(20) CHECK (type IN ('Tablet', 'Capsule', 'Liquid', 'Injection', 'Ointment')),
    dosage VARCHAR(50),
    stock_quantity INTEGER CHECK (stock_quantity >= 0),
    expiry_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Creating Pharmacy table for storing medication given to patients
CREATE TABLE Pharmacy (
    pharmacy_id SERIAL PRIMARY KEY,
    medicine_id INTEGER,
    patient_id INTEGER,
    quantity INTEGER,
    prescription_date DATE,
    FOREIGN KEY (medicine_id) REFERENCES Medicine(medicine_id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id) ON DELETE CASCADE
);

CREATE TABLE Blood_Bank (
    blood_id SERIAL PRIMARY KEY,
    blood_type VARCHAR(3) CHECK (blood_type IN ('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-')),
    stock_quantity INTEGER CHECK (stock_quantity >= 0),
    last_updated DATE
);

-- Create index for blood type
CREATE INDEX idx_blood_type ON Blood_Bank (blood_type);

CREATE TABLE Room_Types (
    room_type_id SERIAL PRIMARY KEY,
    room_type_name VARCHAR(50) NOT NULL, -- ICU, Laboratory, Cosmetic, Operating, Staff
    description VARCHAR(255)
);

CREATE TABLE Rooms (
    room_id SERIAL PRIMARY KEY,
    room_number VARCHAR(10) UNIQUE NOT NULL,
    room_type_id INTEGER,
    capacity INTEGER,
    status VARCHAR(20) CHECK (status IN ('Available', 'Occupied', 'Under Maintenance')),
    last_serviced DATE, -- Date when the room was last serviced or cleaned
    FOREIGN KEY (room_type_id) REFERENCES Room_Types(room_type_id) ON DELETE SET NULL
);

CREATE TABLE Room_Assignments (
    assignment_id SERIAL PRIMARY KEY,
    room_id INTEGER,
    staff_id INTEGER,
    patient_id INTEGER,
    assignment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    end_date TIMESTAMP, -- End date for the assignment (nullable)
    FOREIGN KEY (room_id) REFERENCES Rooms(room_id) ON DELETE CASCADE,
    FOREIGN KEY (staff_id) REFERENCES Staff(staff_id) ON DELETE SET NULL,
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id) ON DELETE SET NULL
);

CREATE TABLE Cleaning_Service (
    service_id SERIAL PRIMARY KEY,
    room_id INTEGER,
    service_date DATE DEFAULT CURRENT_DATE,
    service_time TIME DEFAULT CURRENT_TIME,
    staff_id INTEGER,
    notes VARCHAR(255),
    FOREIGN KEY (room_id) REFERENCES Rooms(room_id),
    FOREIGN KEY (staff_id) REFERENCES Staff(staff_id)
);

CREATE TABLE Prescription (
    prescription_id SERIAL PRIMARY KEY,
    patient_id INTEGER NOT NULL,
    doctor_id INTEGER NOT NULL,
    prescription_date DATE DEFAULT CURRENT_DATE,
    medication_name VARCHAR(100),
    dosage VARCHAR(100),
    frequency VARCHAR(50),
    duration VARCHAR(50),
    notes VARCHAR(255),
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES Doctors(doctor_id)
);

CREATE TABLE Ambulance (
    ambulance_id SERIAL PRIMARY KEY,
    ambulance_number VARCHAR(10) UNIQUE,
    availability VARCHAR(15) CHECK (availability IN ('Available', 'On Duty', 'Maintenance')),
    driver_id INTEGER, -- Make driver_id nullable
    last_service_date DATE,
    FOREIGN KEY (driver_id) REFERENCES Staff(staff_id) ON DELETE NO ACTION
);

-- Creating Ambulance_Log Table to track ambulance usage
CREATE TABLE Ambulance_Log (
    log_id SERIAL PRIMARY KEY,
    ambulance_id INTEGER,
    patient_id INTEGER,
    pickup_location VARCHAR(100),
    dropoff_location VARCHAR(100),
    pickup_time TIMESTAMP,
    dropoff_time TIMESTAMP,
    status VARCHAR(15) CHECK (status IN ('Completed', 'In Progress', 'Canceled')),
    FOREIGN KEY (ambulance_id) REFERENCES Ambulance(ambulance_id) ON DELETE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES Patients(patient_id) ON DELETE CASCADE
);

-- Create index for log status
CREATE INDEX idx_log_status ON Ambulance_Log (status);

-- Creating Medical_Records_Medicine Table (Junction table for many-to-many relationship between MedicalRecords and Medicine)
CREATE TABLE Medical_Records_Medicine (
    record_id INTEGER,
    medicine_id INTEGER,
    dosage VARCHAR(50),
    PRIMARY KEY (record_id, medicine_id),
    FOREIGN KEY (record_id) REFERENCES Medical_Records(record_id) ON DELETE CASCADE,
    FOREIGN KEY (medicine_id) REFERENCES Medicine(medicine_id) ON DELETE CASCADE
);

-- Creating additional indexes for optimization
CREATE INDEX idx_doctor_specialty ON Doctors(specialty);
CREATE INDEX idx_billing_status ON Billing(payment_status);
CREATE INDEX idx_medicine_type ON Medicine (type);
CREATE INDEX idx_medicine_expiry ON Medicine (expiry_date);
