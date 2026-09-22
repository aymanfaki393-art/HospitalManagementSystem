package hms.model;

/**
 * A consultation booked by a patient with a doctor.
 *
 * <p>Persisted to {@code Appointments.txt} in the form:</p>
 *
 * <pre>appointmentId,patientId,doctorId,slotId,date,time,status,reason</pre>
 *
 * <p>The free-text reason is written last so that a comma typed by the patient cannot
 * corrupt the surrounding fields when the line is split.</p>
 *
 * @author Patient module
 */
public class Appointment implements Describable {

    /** Unique appointment identifier, e.g. {@code APT001}. */
    private final String appointmentId;

    /** Identifier of the patient who made the booking. */
    private final String patientId;

    /** Identifier of the doctor who will hold the consultation. */
    private String doctorId;

    /** Identifier of the roster slot this appointment occupies. */
    private String slotId;

    /** Date of the consultation in {@code yyyy-MM-dd} form. */
    private String date;

    /** Start time of the consultation in {@code HH:mm} form. */
    private String time;

    /** Current lifecycle state of the appointment. */
    private AppointmentStatus status;

    /** Reason for the visit, as described by the patient. */
    private String reason;

    /**
     * Creates an appointment.
     *
     * @param appointmentId unique appointment identifier
     * @param patientId     identifier of the booking patient
     * @param doctorId      identifier of the doctor
     * @param slotId        identifier of the roster slot occupied
     * @param date          date in {@code yyyy-MM-dd} form
     * @param time          start time in {@code HH:mm} form
     * @param status        current lifecycle state
     * @param reason        patient's stated reason for the visit
     */
    public Appointment(String appointmentId, String patientId, String doctorId, String slotId,
                       String date, String time, AppointmentStatus status, String reason) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.slotId = slotId;
        this.date = date;
        this.time = time;
        this.status = status;
        this.reason = reason;
    }

    /** @return the unique appointment identifier */
    public String getAppointmentId() {
        return appointmentId;
    }

    /** @return the identifier of the booking patient */
    public String getPatientId() {
        return patientId;
    }

    /** @return the identifier of the doctor */
    public String getDoctorId() {
        return doctorId;
    }

    /** @param doctorId the identifier of the doctor now holding the consultation */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    /** @return the identifier of the roster slot occupied */
    public String getSlotId() {
        return slotId;
    }

    /** @param slotId the identifier of the roster slot now occupied */
    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    /** @return the date in {@code yyyy-MM-dd} form */
    public String getDate() {
        return date;
    }

    /** @param date the new date in {@code yyyy-MM-dd} form */
    public void setDate(String date) {
        this.date = date;
    }

    /** @return the start time in {@code HH:mm} form */
    public String getTime() {
        return time;
    }

    /** @param time the new start time in {@code HH:mm} form */
    public void setTime(String time) {
        this.time = time;
    }

    /** @return the current lifecycle state */
    public AppointmentStatus getStatus() {
        return status;
    }

    /** @param status the new lifecycle state */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    /** @return the patient's stated reason for the visit */
    public String getReason() {
        return reason;
    }

    /** @param reason the patient's revised reason for the visit */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Reports whether this appointment may still be rescheduled or cancelled.
     *
     * @return {@code true} only while the appointment is {@link AppointmentStatus#BOOKED}
     */
    public boolean isActive() {
        return status == AppointmentStatus.BOOKED;
    }

    /** {@inheritDoc} */
    @Override
    public String getSummary() {
        return appointmentId + "  " + date + " " + time + "  with " + doctorId + "  [" + status + "]";
    }
}
