package hms;

import hms.model.Medication;
import hms.model.LabRequest;
import hms.model.MedicalRecord;
import hms.model.VitalSigns;
import hms.dao.LabRequestDAO;
import hms.dao.AppointmentDAO;
import hms.model.Appointment;
import hms.service.BillingService;
import hms.service.DoctorService;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

public class DoctorFeatureSmokeTest {
    public static void main(String[] args) {
        VitalSigns vitalSigns = new VitalSigns("120/80", "72", "36.8");
        MedicalRecord record = new MedicalRecord(
                "MR-TEST", "P001", "D999", "2026-09-22", vitalSigns, "Routine check-up");
        assertEquals("120/80", record.getVitalSigns().getBloodPressure(), "blood pressure");
        assertEquals("72", record.getVitalSigns().getHeartRate(), "heart rate");
        assertEquals("A", BillingService.getMedicalGrade("120/80", "72", "36.8"), "medical grade");

        Medication medication = new Medication("Paracetamol", "Panadol", "C8H9NO2", true);
        if (!medication.isAvailable() || !medication.toString().contains("Panadol")
                || !medication.toString().contains("C8H9NO2")) {
            throw new AssertionError("Medication details were not preserved.");
        }

        LabRequest legacyRequest = LabRequest.fromFileString(
            "LAB-OLD,P001,D999,CBC,Fast before test,2026-09-22");
        assertEquals("Pending", legacyRequest.getStatus(), "legacy request status");
        LabRequest completedRequest = LabRequest.fromFileString(
            "LAB-NEW,P001,D999,MRI,Scan required,2026-09-22,Completed");
        assertEquals("Completed", completedRequest.getStatus(), "request status");

        verifyRequestStatusTransitions();
        verifyAppointmentRules();

        DoctorService doctorService = new DoctorService();
        assertTrue(doctorService.canAccessPatient("D999", "P004"), "active patient access");
        assertTrue(!doctorService.canAccessPatient("D999", "P001"), "completed patient access rejection");
        assertTrue(!doctorService.canAccessPatient("D999", "P003"), "cancelled patient access rejection");

        System.out.println("DoctorFeatureSmokeTest passed.");
    }

    private static void assertEquals(String expected, String actual, String field) {
        if (!expected.equals(actual)) {
            throw new AssertionError(field + " expected " + expected + " but was " + actual);
        }
    }

    private static void assertTrue(boolean condition, String field) {
        if (!condition) {
            throw new AssertionError(field + " assertion failed.");
        }
    }

    private static void verifyRequestStatusTransitions() {
        try {
            File temporaryFile = File.createTempFile("doctor-lab-requests", ".txt");
            LabRequestDAO requestDAO = new LabRequestDAO(temporaryFile.getAbsolutePath());
            requestDAO.saveLabRequest(new LabRequest(
                    "LAB-TEST", "P004", "D999", "CBC", "Routine", "2026-09-22"));

            assertTrue(requestDAO.updateRequestStatus("LAB-TEST", "In Progress"), "pending to in progress");
            assertTrue(requestDAO.updateRequestStatus("LAB-TEST", "Completed"), "in progress to completed");
            assertTrue(!requestDAO.updateRequestStatus("LAB-TEST", "Pending"), "completed status reversal rejection");
            temporaryFile.delete();
        } catch (IOException ex) {
            throw new AssertionError("Unable to create temporary request test file.", ex);
        }
    }

    private static void verifyAppointmentRules() {
        try {
            File temporaryFile = File.createTempFile("doctor-appointments", ".txt");
            try (FileWriter writer = new FileWriter(temporaryFile, false)) {
                writer.write(new Appointment("A-TEST", "D999", "P004", "2026-09-25", "09:00", "Booked", "Check-up").toFileString());
                writer.write(System.lineSeparator());
                writer.write(new Appointment("A-TAKEN", "D999", "P005", "2026-09-25", "10:00", "Booked", "Follow-up").toFileString());
                writer.write(System.lineSeparator());
                writer.write(new Appointment("A-OTHER", "D999", "P006", "2026-09-25", "11:00", "Booked", "Consultation").toFileString());
                writer.write(System.lineSeparator());
            }

            DoctorService doctorService = new DoctorService(new AppointmentDAO(temporaryFile.getAbsolutePath()));
            assertTrue(!doctorService.completeAppointment("A-TEST", "D888"), "wrong doctor completion rejection");
            assertTrue(doctorService.completeAppointment("A-TEST", "D999"), "appointment completion");
            assertTrue(!doctorService.rescheduleAppointment("A-TAKEN", "D999", "2026-09-25", "11:00"), "occupied slot rejection");
            temporaryFile.delete();
        } catch (IOException ex) {
            throw new AssertionError("Unable to create temporary appointment test file.", ex);
        }
    }
}
