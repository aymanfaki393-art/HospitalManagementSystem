package hms.model;

public class LabRequest {
    private String requestId;
    private String patientId;
    private String doctorId;
    private String testType;
    private String notes;
    private String requestDate;
    private String status;

    public LabRequest(String requestId, String patientId, String doctorId,
                      String testType, String notes, String requestDate) {
        this(requestId, patientId, doctorId, testType, notes, requestDate, "Pending");
    }

    public LabRequest(String requestId, String patientId, String doctorId,
                      String testType, String notes, String requestDate, String status) {
        this.requestId = requestId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.testType = testType;
        this.notes = notes;
        this.requestDate = requestDate;
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toFileString() {
        return requestId + "," + patientId + "," + doctorId + "," + testType + "," + notes + "," + requestDate + "," + status;
    }

    public static LabRequest fromFileString(String line) {
        String[] parts = line.split(",", 7);
        if (parts.length < 6) {
            return null;
        }
        String status = parts.length == 7 ? parts[6] : "Pending";
        return new LabRequest(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], status);
    }
}
