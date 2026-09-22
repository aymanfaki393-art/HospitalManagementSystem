# Patient Module - Developer Guide

Hospital Management System (HMS), APU Medical Centre
Object-Oriented Programming in Java (OODJ) group coursework

This document describes the Patient section of the system: what it does, how it is
structured, which files it reads and writes, and the points at which it meets the work of
the other three modules. It is written for the team, not for the marker; the assessed
report is a separate document.

---

## 1. Scope

Table 1.0 of the coursework specification gives the Patient role four functionalities.
All four are implemented, together with the login path that reaches them:

| Requirement (Table 1.0) | Where it is implemented |
|---|---|
| Edit personal / individual profile | `PatientDashboard` "My Profile" tab, `PatientService.updateProfile` / `updateName` / `changePassword` |
| Browse available slots of doctors' consultations and book / reschedule / cancel booking | "Book Appointment" and "My Appointments" tabs, `AppointmentService.book` / `reschedule` / `cancel` |
| View personal medical history and prescriptions | "Medical History" and "Prescriptions" tabs, `PatientService.getMedicalHistory` / `getPrescriptions` |
| Submit ratings and comments to doctors and clinic visits | "Feedback" tab, `FeedbackService.submitFeedback` |

## 2. Package layout

```
src/hms/
├── model/                  domain classes
│   ├── User.java                (existing, abstract base - unchanged)
│   ├── Patient.java             (corrected and extended)
│   ├── PatientProfile.java      personal details held apart from the credential file
│   ├── DoctorSlot.java          one consultation slot on a doctor's roster
│   ├── Appointment.java         a booking made against a slot
│   ├── Prescription.java        medication issued by a doctor
│   ├── MedicalRecord.java       clinical entry written by a doctor
│   ├── Feedback.java            rating and comment about a completed consultation
│   ├── Describable.java         interface: "can be summarised in one line"
│   ├── AppointmentStatus.java   enum: BOOKED / COMPLETED / CANCELLED
│   └── SlotStatus.java          enum: AVAILABLE / BOOKED / UNAVAILABLE
├── dao/                    persistence, one text file per entity
│   ├── TextFileDAO.java         abstract generic base (template method)
│   ├── PatientProfileDAO.java
│   ├── DoctorSlotDAO.java
│   ├── AppointmentDAO.java
│   ├── PrescriptionDAO.java
│   ├── MedicalRecordDAO.java
│   └── FeedbackDAO.java
├── service/                business rules
│   ├── ServiceResult.java       success flag plus a message for the user
│   ├── PatientService.java
│   ├── AppointmentService.java
│   └── FeedbackService.java
├── util/
│   ├── Validator.java           shared input validation
│   └── AppLogger.java           append-only activity log
└── gui/Patient/
    └── PatientDashboard.java    the tabbed window opened after login
```

## 3. Data files

All storage is plain text, one record per line, comma separated, in the project working
directory. No database is used, as required by Section 6 of the specification.

| File | Format | Written by |
|---|---|---|
| `Users.txt` | `userId,username,password,name,role` | Admin module (shared) |
| `PatientProfiles.txt` | `patientId,contactNumber,email,dateOfBirth,address` | Patient module |
| `DoctorSlots.txt` | `slotId,doctorId,date,startTime,endTime,status` | Medical Manager module creates; Patient module flips status |
| `Appointments.txt` | `appointmentId,patientId,doctorId,slotId,date,time,status,reason` | Patient module |
| `MedicalRecords.txt` | `recordId,patientId,doctorId,date,vitals,diagnosis,notes` | Doctor module |
| `Prescriptions.txt` | `prescriptionId,patientId,doctorId,dateIssued,medication,dosage,instructions` | Doctor module |
| `Feedback.txt` | `feedbackId,patientId,doctorId,appointmentId,rating,date,comments` | Patient module |
| `hms_activity.log` | `timestamp [LEVEL] source - message` | all modules that call `AppLogger` |

**Free-text fields are always written last on the line.** A patient may legitimately type a
comma in an address, a reason for a visit or a feedback comment. Each DAO therefore splits
with a field limit (`line.split(",", N)`), so the final field keeps its commas while the
structured fields in front of it stay aligned. Fields that are not last have commas and line
breaks stripped by `TextFileDAO.clean`.

## 4. Integration points with the other modules

1. **Medical Manager -> Patient.** The manager's shift-roster screen should append rows to
   `DoctorSlots.txt` in the format above, with status `AVAILABLE`. They will appear in the
   patient's booking tab immediately; no change to the Patient module is needed.
2. **Patient -> Doctor.** A confirmed booking is a row in `Appointments.txt` with status
   `BOOKED`. The doctor's screen can read this file filtered by `doctorId` to build a
   consultation list.
3. **Doctor -> Patient.** Rows appended to `MedicalRecords.txt` and `Prescriptions.txt` for a
   given `patientId` appear in the patient's history and prescription tabs on the next refresh.
4. **Patient -> Medical Manager.** `Feedback.txt` holds per-doctor ratings, which the
   manager's "hospital metrics" report can aggregate.
5. **Doctor specialty.** `UserDAO.loadAllUsers` currently constructs every `Doctor` with the
   placeholder specialty `"Unknown"`, because `Users.txt` has only five fields. The patient's
   booking list therefore shows doctors by name alone. Once the shared user record carries a
   specialty, adding it back to the slot table is a one-line change in
   `PatientDashboard.refreshSlots`.

## 5. Defects corrected outside the Patient module

These were blocking the Patient module and were fixed with the smallest change that would do.
No feature belonging to another module was altered.

| File | Problem | Fix |
|---|---|---|
| `dao/UserDAO.java` | Users were written to `Users.txt` but read back from `users.txt`. Windows ignores the difference; on any case-sensitive file system every login failed. | A single `USERS_FILE` constant is now used by all four methods. |
| `dao/UserDAO.java` | A short or blank line in the file threw `ArrayIndexOutOfBoundsException` at `parts[4]` and aborted the whole load, making login impossible for every role. | Lines with fewer than five fields are skipped; a missing file returns an empty list. |
| `dao/UserDAO.java` | The `Scanner` in `loadAllUsers` was never closed. | Closed after the read loop. |
| `gui/LoginFrame.java` | An unsuccessful login showed no message at all: the `else` branch belonged to the chain of role tests, not to the null check. | The null check now has its own branch and clears the password field. |
| `gui/LoginFrame.java` | Managers were routed on the role string `"Manager"`, but `MedicalManager` stores `"MedicalManager"`, so a manager who signed in correctly saw nothing happen. | Role routing rewritten as a `switch` using the correct strings, with a default branch for unknown roles. |
| `gui/LoginFrame.java` | `PatientDashboard` was constructed with no argument, so it had no way of knowing who had logged in. | The authenticated `Patient` is now passed to the constructor. |
| `model/Patient.java` | The constructor assigned the field to itself (`this.ContactNumber = ContactNumber`), so every patient's contact number was `null`. | Parameter renamed and assigned correctly; a validating setter and a `toString()` were added. |

`src/hms/gui/Patient/PatientDashboard.form` was deleted. It described the one-label
placeholder window and is incompatible with the hand-written tabbed interface that replaced
it. Please edit `PatientDashboard.java` in the source editor rather than the design view.

## 6. Running the module

1. Open the project in NetBeans (it is an Ant-based Java project, source and target level 21).
2. `Users.txt` is deliberately not tracked by Git, because it holds passwords in clear text.
   Create one in the project root before the first run. The demo accounts used for the
   screenshots in the report are:

   ```
   AD001,ad_omar,1234,Omar Hassan,AdminStaff
   MG001,mg_bilal,1234,Sayyed Bilal,MedicalManager
   D001,drAli,1234,Dr. Ali Rahman,Doctor
   D002,drSiti,1234,Dr. Siti Nurhaliza,Doctor
   D003,drChen,1234,Dr. Chen Wei,Doctor
   PAT001,pat_eve,1234,Evelyn Tan,Patient
   PAT002,pat_ahmed,1234,Ahmed Salim,Patient
   ```

3. Run `hms.gui.LoginFrame` (or the project's main class) and sign in as `pat_eve` / `1234`.

## 7. Known limitations

- Passwords are stored in clear text in `Users.txt`. This matches the existing design of the
  shared credential file; hashing would change a format three other modules depend on and was
  therefore not introduced unilaterally.
- Records are re-read from disk on every operation rather than cached. With coursework-sized
  files this is fast enough and keeps the modules from holding stale copies of each other's data.
- There is no file locking, so two copies of the application running at once could overwrite
  each other's changes. Single-user operation is assumed, as in the rest of the project.
- `ManageUsersFrame` (Admin module) calls the deprecated `JPasswordField.getText()`. It was
  left alone because it belongs to another team member's section.
