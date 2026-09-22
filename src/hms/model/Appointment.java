package hms.model;

public class Appointment {
    private String appointmentId;
    private String doctorId;
    private String patientId;
    private String appointmentDate;
    private String appointmentTime;
    private String status;
    private String reason;

    public Appointment(String appointmentId, String doctorId, String patientId,
                       String appointmentDate, String appointmentTime,
                       String status, String reason) {
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.reason = reason;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String toFileString() {
        return appointmentId + "," + doctorId + "," + patientId + ","
                + appointmentDate + "," + appointmentTime + "," + status + "," + reason;
    }

    public static Appointment fromFileString(String line) {
        String[] parts = line.split(",", 7);
        if (parts.length < 7) {
            return null;
        }
        return new Appointment(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
    }
}
