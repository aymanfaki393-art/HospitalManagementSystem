package hms.model;

public class MedicalRecord {
    private String recordId;
    private String patientId;
    private String doctorId;
    private String recordDate;
    private String bloodPressure;
    private String heartRate;
    private String temperature;
    private String notes;

    public MedicalRecord(String recordId, String patientId, String doctorId,
                         String recordDate, String bloodPressure, String heartRate,
                         String temperature, String notes) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.recordDate = recordDate;
        this.bloodPressure = bloodPressure;
        this.heartRate = heartRate;
        this.temperature = temperature;
        this.notes = notes;
    }

    public MedicalRecord(String recordId, String patientId, String doctorId,
                         String recordDate, VitalSigns vitalSigns, String notes) {
        this(recordId, patientId, doctorId, recordDate,
                vitalSigns.getBloodPressure(), vitalSigns.getHeartRate(),
                vitalSigns.getTemperature(), notes);
    }

    public VitalSigns getVitalSigns() {
        return new VitalSigns(bloodPressure, heartRate, temperature);
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String toFileString() {
        return recordId + "," + patientId + "," + doctorId + "," + recordDate + ","
                + bloodPressure + "," + heartRate + "," + temperature + "," + notes;
    }

    public static MedicalRecord fromFileString(String line) {
        String[] parts = line.split(",", 8);
        if (parts.length < 8) {
            return null;
        }
        return new MedicalRecord(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]);
    }
}
