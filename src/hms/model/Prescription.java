package hms.model;

/**
 * A medication prescribed to a patient during a consultation.
 *
 * <p>Prescriptions are written by the Doctor module and are read-only from the patient's
 * point of view. Persisted to {@code Prescriptions.txt} in the form:</p>
 *
 * <pre>prescriptionId,patientId,doctorId,dateIssued,medication,dosage,instructions</pre>
 *
 * @author Patient module
 */
public class Prescription implements Describable {

    /** Unique prescription identifier, e.g. {@code PRE001}. */
    private final String prescriptionId;

    /** Identifier of the patient the medication was prescribed to. */
    private final String patientId;

    /** Identifier of the prescribing doctor. */
    private final String doctorId;

    /** Date the prescription was issued, in {@code yyyy-MM-dd} form. */
    private final String dateIssued;

    /** Name of the medication. */
    private final String medication;

    /** Dosage, e.g. {@code 500mg twice daily}. */
    private final String dosage;

    /** Additional instructions for the patient. */
    private final String instructions;

    /**
     * Creates a prescription.
     *
     * @param prescriptionId unique prescription identifier
     * @param patientId      identifier of the patient
     * @param doctorId       identifier of the prescribing doctor
     * @param dateIssued     date of issue in {@code yyyy-MM-dd} form
     * @param medication     name of the medication
     * @param dosage         dosage instructions
     * @param instructions   additional guidance for the patient
     */
    public Prescription(String prescriptionId, String patientId, String doctorId, String dateIssued,
                        String medication, String dosage, String instructions) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateIssued = dateIssued;
        this.medication = medication;
        this.dosage = dosage;
        this.instructions = instructions;
    }

    /** @return the unique prescription identifier */
    public String getPrescriptionId() {
        return prescriptionId;
    }

    /** @return the identifier of the patient */
    public String getPatientId() {
        return patientId;
    }

    /** @return the identifier of the prescribing doctor */
    public String getDoctorId() {
        return doctorId;
    }

    /** @return the date of issue in {@code yyyy-MM-dd} form */
    public String getDateIssued() {
        return dateIssued;
    }

    /** @return the name of the medication */
    public String getMedication() {
        return medication;
    }

    /** @return the dosage instructions */
    public String getDosage() {
        return dosage;
    }

    /** @return additional guidance for the patient */
    public String getInstructions() {
        return instructions;
    }

    /** {@inheritDoc} */
    @Override
    public String getSummary() {
        return dateIssued + "  " + medication + " (" + dosage + ")";
    }
}
