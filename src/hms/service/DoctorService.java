package hms.service;

import hms.dao.AppointmentDAO;
import hms.dao.LabRequestDAO;
import hms.dao.MedicalRecordDAO;
import hms.dao.PrescriptionDAO;
import hms.dao.UserDAO;
import hms.model.Appointment;
import hms.model.Doctor;
import hms.model.LabRequest;
import hms.model.MedicalRecord;
import hms.model.Patient;
import hms.model.Prescription;
import hms.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DoctorService {
    private final AppointmentDAO appointmentDAO;
    private final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();
    private final LabRequestDAO labRequestDAO = new LabRequestDAO();
    private final UserDAO userDAO = new UserDAO();

    public DoctorService() {
        this(new AppointmentDAO());
    }

    public DoctorService(AppointmentDAO appointmentDAO) {
        this.appointmentDAO = appointmentDAO;
    }

    public ArrayList<Appointment> getAppointmentsForDoctor(String doctorId) {
        return appointmentDAO.getAppointmentsForDoctor(doctorId);
    }

    public ArrayList<Patient> getPatientsForDoctor(String doctorId) {
        ArrayList<Patient> patients = new ArrayList<>();
        ArrayList<Appointment> appointments = getAppointmentsForDoctor(doctorId);
        Set<String> patientIds = new HashSet<>();

        for (Appointment appointment : appointments) {
            if (!isActiveAppointment(appointment) || !patientIds.add(appointment.getPatientId())) {
                continue;
            }
            for (User user : userDAO.loadAllUsers()) {
                if (user instanceof Patient && user.getUserId().equals(appointment.getPatientId())) {
                    patients.add((Patient) user);
                }
            }
        }

        return patients;
    }

    public boolean canAccessPatient(String doctorId, String patientId) {
        for (Appointment appointment : getAppointmentsForDoctor(doctorId)) {
            if (patientId.equals(appointment.getPatientId()) && isActiveAppointment(appointment)) {
                return true;
            }
        }
        return false;
    }

    public void saveMedicalRecord(MedicalRecord record) {
        medicalRecordDAO.saveMedicalRecord(record);
    }

    public void createPrescription(Prescription prescription) {
        prescriptionDAO.savePrescription(prescription);
    }

    public void requestLabTest(LabRequest request) {
        labRequestDAO.saveLabRequest(request);
    }

    public ArrayList<MedicalRecord> getMedicalRecordsForPatient(String patientId) {
        ArrayList<MedicalRecord> result = new ArrayList<>();
        for (MedicalRecord record : medicalRecordDAO.loadAllRecords()) {
            if (patientId.equals(record.getPatientId())) {
                result.add(record);
            }
        }
        return result;
    }

    public int countMedicalRecordsForDoctor(String doctorId) {
        int count = 0;
        for (MedicalRecord record : medicalRecordDAO.loadAllRecords()) {
            if (doctorId.equals(record.getDoctorId())) {
                count++;
            }
        }
        return count;
    }

    public int countPrescriptionsForDoctor(String doctorId) {
        int count = 0;
        for (Prescription prescription : prescriptionDAO.loadAllPrescriptions()) {
            if (doctorId.equals(prescription.getDoctorId())) {
                count++;
            }
        }
        return count;
    }

    public int countLabRequestsForDoctor(String doctorId) {
        int count = 0;
        for (LabRequest request : labRequestDAO.loadAllLabRequests()) {
            if (doctorId.equals(request.getDoctorId())) {
                count++;
            }
        }
        return count;
    }

    public boolean completeAppointment(String appointmentId, String doctorId) {
        ArrayList<Appointment> appointments = appointmentDAO.loadAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)
                    && appointment.getDoctorId().equals(doctorId)
                    && !"Completed".equalsIgnoreCase(appointment.getStatus())
                    && !"Cancelled".equalsIgnoreCase(appointment.getStatus())) {
                appointment.setStatus("Completed");
                appointmentDAO.updateAppointment(appointment);
                return true;
            }
        }
        return false;
    }

    public void cancelAppointment(String appointmentId, String doctorId) {
        updateAppointmentStatus(appointmentId, doctorId, "Cancelled");
    }

    public boolean rescheduleAppointment(String appointmentId, String doctorId, String date, String time) {
        ArrayList<Appointment> appointments = appointmentDAO.loadAppointments();
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)
                    && appointment.getDoctorId().equals(doctorId)
                    && !"Completed".equalsIgnoreCase(appointment.getStatus())
                    && !"Cancelled".equalsIgnoreCase(appointment.getStatus())) {
                for (Appointment otherAppointment : appointments) {
                    if (!otherAppointment.getAppointmentId().equals(appointmentId)
                            && doctorId.equals(otherAppointment.getDoctorId())
                            && date.equals(otherAppointment.getAppointmentDate())
                            && time.equals(otherAppointment.getAppointmentTime())
                            && !"Completed".equalsIgnoreCase(otherAppointment.getStatus())
                            && !"Cancelled".equalsIgnoreCase(otherAppointment.getStatus())) {
                        return false;
                    }
                }
                appointment.setAppointmentDate(date);
                appointment.setAppointmentTime(time);
                appointment.setStatus("Rescheduled");
                appointmentDAO.updateAppointment(appointment);
                return true;
            }
        }
        return false;
    }

    private void updateAppointmentStatus(String appointmentId, String doctorId, String status) {
        for (Appointment appointment : appointmentDAO.loadAppointments()) {
            if (appointment.getAppointmentId().equals(appointmentId)
                    && appointment.getDoctorId().equals(doctorId)
                    && !"Completed".equalsIgnoreCase(appointment.getStatus())
                    && !"Cancelled".equalsIgnoreCase(appointment.getStatus())) {
                appointment.setStatus(status);
                appointmentDAO.updateAppointment(appointment);
                return;
            }
        }
    }

    private boolean isActiveAppointment(Appointment appointment) {
        return !"Completed".equalsIgnoreCase(appointment.getStatus())
                && !"Cancelled".equalsIgnoreCase(appointment.getStatus());
    }

    public static String generateId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String today() {
        return LocalDate.now().toString();
    }

    public static Doctor getDoctorFromUser(User user) {
        if (user instanceof Doctor) {
            return (Doctor) user;
        }
        return null;
    }
}
