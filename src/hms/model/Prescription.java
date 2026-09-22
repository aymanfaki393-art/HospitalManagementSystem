package hms.model;

public class Prescription {
    private String prescriptionId;
    private String patientId;
    private String doctorId;
    private String medicationName;
    private String dosage;
    private String instructions;
    private String prescriptionDate;

    public Prescription(String prescriptionId, String patientId, String doctorId,
                       String medicationName, String dosage, String instructions,
                       String prescriptionDate) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.medicationName = medicationName;
        this.dosage = dosage;
        this.instructions = instructions;
        this.prescriptionDate = prescriptionDate;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String toFileString() {
        return prescriptionId + "," + patientId + "," + doctorId + ","
                + medicationName + "," + dosage + "," + instructions + "," + prescriptionDate;
    }

    public static Prescription fromFileString(String line) {
        String[] parts = line.split(",", 7);
        if (parts.length < 7) {
            return null;
        }
        return new Prescription(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
    }
}
