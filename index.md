# Hospital Management System Code Index

This file maps the main project features to their source-code locations.

## 1. Architecture

```text
GUI -> Service -> DAO -> Model -> Text files
```

- `src/hms/gui/`: Swing user interfaces
- `src/hms/service/`: business rules and workflows
- `src/hms/dao/`: text-file loading and saving
- `src/hms/model/`: domain objects
- `test/`: executable smoke tests
- `build/info/`: project documentation and assignment files

## 2. Application Entry and Login

- Main application entry: `src/hospitalmanagementsystem/HospitalManagementSystem.java`
- Login screen: `src/hms/gui/LoginFrame.java`
- Login business logic: `src/hms/service/LoginService.java`
- Shared user base class: `src/hms/model/User.java`
- User persistence: `src/hms/dao/UserDAO.java`

## 3. Doctor Part

### Doctor GUI

- Main dashboard: `src/hms/gui/Doctor/DoctorDashboard.java`
- Doctor profile editor: `src/hms/gui/Doctor/DoctorProfileFrame.java`
- Gray example text field: `src/hms/gui/Doctor/PlaceholderTextField.java`
- Gray example text area: `src/hms/gui/Doctor/PlaceholderTextArea.java`
- Gray example password field: `src/hms/gui/Doctor/PlaceholderPasswordField.java`

### Doctor services

- Doctor business logic: `src/hms/service/DoctorService.java`
- Medical grade calculation: `src/hms/service/BillingService.java`

### Doctor models

- Doctor account: `src/hms/model/Doctor.java`
- Appointment: `src/hms/model/Appointment.java`
- Patient: `src/hms/model/Patient.java`
- Medical record: `src/hms/model/MedicalRecord.java`
- Vital signs: `src/hms/model/VitalSigns.java`
- Prescription: `src/hms/model/Prescription.java`
- Medication: `src/hms/model/Medication.java`
- Lab/X-ray/imaging request: `src/hms/model/LabRequest.java`

### Doctor DAOs

- User data: `src/hms/dao/UserDAO.java`
- Appointment data: `src/hms/dao/AppointmentDAO.java`
- Medical records: `src/hms/dao/MedicalRecordDAO.java`
- Prescriptions: `src/hms/dao/PrescriptionDAO.java`
- Lab and imaging requests: `src/hms/dao/LabRequestDAO.java`

### Doctor request administration

- Admin request viewer and status controls: `src/hms/gui/admin/LabRequestsFrame.java`
- Admin dashboard button: `src/hms/gui/admin/AdminDashboard.java`

### Doctor data files

- Users and Doctor accounts: `user.txt`
- Appointments: `appointments.txt`
- Medical records: `medicalrecords.txt`
- Prescriptions: `prescriptions.txt`
- Lab/X-ray/imaging requests: `labrequests.txt`
- Medication catalog: `medication.txt`
- Test and imaging catalog: `test.txt`
- Doctor seed reference: `doctor_seed_data.txt`

### Doctor testing and documentation

- Doctor smoke test: `test/hms/DoctorFeatureSmokeTest.java`
- Doctor documentation and code explanation: `build/info/Doctor_Part_Documentation.md`
- Doctor task status: `build/info/Doctor_Module_Task_Status.md`
- Doctor requirements status: `build/info/Doctor_Part_Status.md`

## 4. Patient Part

- Patient dashboard: `src/hms/gui/Patient/PatientDashboard.java`
- Patient model: `src/hms/model/Patient.java`
- Patient data is loaded through: `src/hms/dao/UserDAO.java`
- Appointment data is handled through: `src/hms/dao/AppointmentDAO.java`
- Appointment-related rules are shared with: `src/hms/service/DoctorService.java`

## 5. Admin Staff Part

- Admin dashboard: `src/hms/gui/admin/AdminDashboard.java`
- User management: `src/hms/gui/admin/ManageUsersFrame.java`
- Doctor assignment screen: `src/hms/gui/admin/AssignDoctorFrame.java`
- Lab and imaging requests: `src/hms/gui/admin/LabRequestsFrame.java`
- Admin model: `src/hms/model/AdminStaff.java`
- User persistence: `src/hms/dao/UserDAO.java`
- Assignment model: `src/hms/model/Assignment.java`
- Assignment persistence: `src/hms/dao/AssignmentDAO.java`

## 6. Medical Manager Part

- Manager dashboard: `src/hms/gui/Manager/ManagerDashboard.java`
- Medical Manager model: `src/hms/model/MedicalManager.java`
- User persistence: `src/hms/dao/UserDAO.java`

## 7. Shared Domain and Persistence

### Models

- `src/hms/model/User.java`
- `src/hms/model/Doctor.java`
- `src/hms/model/Patient.java`
- `src/hms/model/AdminStaff.java`
- `src/hms/model/MedicalManager.java`
- `src/hms/model/Appointment.java`
- `src/hms/model/MedicalRecord.java`
- `src/hms/model/Prescription.java`
- `src/hms/model/LabRequest.java`
- `src/hms/model/Medication.java`
- `src/hms/model/VitalSigns.java`

### DAOs

- `src/hms/dao/UserDAO.java`
- `src/hms/dao/AppointmentDAO.java`
- `src/hms/dao/MedicalRecordDAO.java`
- `src/hms/dao/PrescriptionDAO.java`
- `src/hms/dao/LabRequestDAO.java`
- `src/hms/dao/AssignmentDAO.java`

### Text data files

- `user.txt`
- `Users.txt`
- `appointments.txt`
- `medicalrecords.txt`
- `medication.txt`
- `test.txt`
- `labrequests.txt`
- `doctor_seed_data.txt`

## 8. Build and Project Configuration

- Ant build file: `build.xml`
- NetBeans project configuration: `nbproject/`
- Application manifest: `manifest.mf`
- Compiled classes and generated build output: `build/`

Useful commands:

```bash
ant -q compile
javac -cp build/classes -d build/test/classes test/hms/DoctorFeatureSmokeTest.java
java -cp build/classes:build/test/classes hms.DoctorFeatureSmokeTest
```

## 9. Documentation and Assignment Files

- Assignment question sheet: `build/info/2607-OODJ-Assignment-QS (1).docx`
- Doctor code explanation: `build/info/Doctor_Part_Documentation.md`
- Doctor task checklist: `build/info/Doctor_Module_Task_Status.md`
- Project status: `build/info/Project_List.md`
- Testing notes: `build/info/TESTING.md`
- UML class diagram: `build/info/HMS_ClassDiagram.puml`
- UML use-case diagram: `build/info/HMS_UseCaseDiagram.puml`
- README files: `build/info/README.md`, `build/info/README_1.md`, and `build/info/huxog_README_1.md`

## 10. Quick Navigation

| Need to change | Start here |
|---|---|
| Doctor screen layout | `src/hms/gui/Doctor/DoctorDashboard.java` |
| Doctor business rule | `src/hms/service/DoctorService.java` |
| Medical grading | `src/hms/service/BillingService.java` |
| Save/load a Doctor record | Relevant DAO in `src/hms/dao/` |
| Change a data structure | Relevant model in `src/hms/model/` |
| Change medication choices | `medication.txt` |
| Change test choices | `test.txt` |
| Explain the Doctor code | `build/info/Doctor_Part_Documentation.md` |
| Run Doctor verification | `test/hms/DoctorFeatureSmokeTest.java` |
