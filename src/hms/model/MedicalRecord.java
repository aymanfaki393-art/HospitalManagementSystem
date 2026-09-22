package hms.model;

/**
 * A clinical entry created by a doctor after a consultation.
 *
 * <p>Medical records are written by the Doctor module and are read-only from the patient's
 * point of view. Persisted to {@code MedicalRecords.txt} in the form:</p>
 *
 * <pre>recordId,patientId,doctorId,date,vitals,diagnosis,notes</pre>
 *
 * @author Patient module
 */
public class MedicalRecord implements Describable {

    /** Unique record identifier, e.g. {@code REC001}. */
    private final String recordId;

    /** Identifier of the patient the record belongs to. */
    private final String patientId;

    /** Identifier of the attending doctor. */
    private final String doctorId;

    /** Date of the consultation in {@code yyyy-MM-dd} form. */
    private final String date;

    /** Recorded vital signs, e.g. {@code BP 120/80; Temp 36.8C}. */
    private final String vitals;

    /** The doctor's diagnosis. */
    private final String diagnosis;

    /** Free-text consultation notes. */
    private final String notes;

    /**
     * Creates a medical record.
     *
     * @param recordId  unique record identifier
     * @param patientId identifier of the patient
     * @param doctorId  identifier of the attending doctor
     * @param date      date of the consultation in {@code yyyy-MM-dd} form
     * @param vitals    recorded vital signs
     * @param diagnosis the doctor's diagnosis
     * @param notes     free-text consultation notes
     */
    public MedicalRecord(String recordId, String patientId, String doctorId, String date,
                         String vitals, String diagnosis, String notes) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.vitals = vitals;
        this.diagnosis = diagnosis;
        this.notes = notes;
    }

    /** @return the unique record identifier */
    public String getRecordId() {
        return recordId;
    }

    /** @return the identifier of the patient */
    public String getPatientId() {
        return patientId;
    }

    /** @return the identifier of the attending doctor */
    public String getDoctorId() {
        return doctorId;
    }

    /** @return the date of the consultation in {@code yyyy-MM-dd} form */
    public String getDate() {
        return date;
    }

    /** @return the recorded vital signs */
    public String getVitals() {
        return vitals;
    }

    /** @return the doctor's diagnosis */
    public String getDiagnosis() {
        return diagnosis;
    }

    /** @return the free-text consultation notes */
    public String getNotes() {
        return notes;
    }

    /** {@inheritDoc} */
    @Override
    public String getSummary() {
        return date + "  " + diagnosis + "  (Dr " + doctorId + ")";
    }
}
