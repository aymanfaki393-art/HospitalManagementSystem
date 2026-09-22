package hms.dao;

import hms.model.MedicalRecord;
import java.util.ArrayList;
import java.util.List;

/**
 * Data-access object for clinical records, stored in {@code MedicalRecords.txt}.
 *
 * <p>Line format: {@code recordId,patientId,doctorId,date,vitals,diagnosis,notes}.
 * Records are created by the Doctor module; the Patient module reads them only.</p>
 *
 * @author Patient module
 */
public class MedicalRecordDAO extends TextFileDAO<MedicalRecord> {

    /** Number of fields on a well-formed line. */
    private static final int FIELD_COUNT = 7;

    /** Creates a data-access object bound to {@code MedicalRecords.txt}. */
    public MedicalRecordDAO() {
        super("MedicalRecords.txt");
    }

    /** {@inheritDoc} */
    @Override
    protected String toLine(MedicalRecord record) {
        return String.join(DELIMITER,
                clean(record.getRecordId()),
                clean(record.getPatientId()),
                clean(record.getDoctorId()),
                clean(record.getDate()),
                clean(record.getVitals()),
                clean(record.getDiagnosis()),
                clean(record.getNotes(), true));
    }

    /** {@inheritDoc} */
    @Override
    protected MedicalRecord fromLine(String line) {
        String[] parts = line.split(DELIMITER, FIELD_COUNT);
        if (parts.length < FIELD_COUNT) {
            return null;
        }
        return new MedicalRecord(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                parts[4].trim(), parts[5].trim(), parts[6].trim());
    }

    /** {@inheritDoc} */
    @Override
    protected String getId(MedicalRecord record) {
        return record.getRecordId();
    }

    /**
     * Lists every clinical record belonging to one patient.
     *
     * @param patientId identifier of the patient
     * @return the patient's records, in file order
     */
    public List<MedicalRecord> findByPatient(String patientId) {
        List<MedicalRecord> result = new ArrayList<>();
        for (MedicalRecord record : loadAll()) {
            if (record.getPatientId().equals(patientId)) {
                result.add(record);
            }
        }
        return result;
    }
}
