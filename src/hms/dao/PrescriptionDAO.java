package hms.dao;

import hms.model.Prescription;
import java.util.ArrayList;
import java.util.List;

/**
 * Data-access object for prescriptions, stored in {@code Prescriptions.txt}.
 *
 * <p>Line format: {@code prescriptionId,patientId,doctorId,dateIssued,medication,dosage,instructions}.
 * Records are created by the Doctor module; the Patient module reads them only.</p>
 *
 * @author Patient module
 */
public class PrescriptionDAO extends TextFileDAO<Prescription> {

    /** Number of fields on a well-formed line. */
    private static final int FIELD_COUNT = 7;

    /** Creates a data-access object bound to {@code Prescriptions.txt}. */
    public PrescriptionDAO() {
        super("Prescriptions.txt");
    }

    /** {@inheritDoc} */
    @Override
    protected String toLine(Prescription prescription) {
        return String.join(DELIMITER,
                clean(prescription.getPrescriptionId()),
                clean(prescription.getPatientId()),
                clean(prescription.getDoctorId()),
                clean(prescription.getDateIssued()),
                clean(prescription.getMedication()),
                clean(prescription.getDosage()),
                clean(prescription.getInstructions(), true));
    }

    /** {@inheritDoc} */
    @Override
    protected Prescription fromLine(String line) {
        String[] parts = line.split(DELIMITER, FIELD_COUNT);
        if (parts.length < FIELD_COUNT) {
            return null;
        }
        return new Prescription(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                parts[4].trim(), parts[5].trim(), parts[6].trim());
    }

    /** {@inheritDoc} */
    @Override
    protected String getId(Prescription prescription) {
        return prescription.getPrescriptionId();
    }

    /**
     * Lists every prescription issued to one patient.
     *
     * @param patientId identifier of the patient
     * @return the patient's prescriptions, in file order
     */
    public List<Prescription> findByPatient(String patientId) {
        List<Prescription> result = new ArrayList<>();
        for (Prescription prescription : loadAll()) {
            if (prescription.getPatientId().equals(patientId)) {
                result.add(prescription);
            }
        }
        return result;
    }
}
