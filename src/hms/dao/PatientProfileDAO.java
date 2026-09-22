package hms.dao;

import hms.model.PatientProfile;
import java.util.List;

/**
 * Data-access object for patient personal details, stored in {@code PatientProfiles.txt}.
 *
 * <p>Line format: {@code patientId,contactNumber,email,dateOfBirth,address}. The address is
 * written last because residential addresses commonly contain commas.</p>
 *
 * @author Patient module
 */
public class PatientProfileDAO extends TextFileDAO<PatientProfile> {

    /** Number of fields on a well-formed line. */
    private static final int FIELD_COUNT = 5;

    /** Creates a data-access object bound to {@code PatientProfiles.txt}. */
    public PatientProfileDAO() {
        super("PatientProfiles.txt");
    }

    /** {@inheritDoc} */
    @Override
    protected String toLine(PatientProfile profile) {
        return String.join(DELIMITER,
                clean(profile.getPatientId()),
                clean(profile.getContactNumber()),
                clean(profile.getEmail()),
                clean(profile.getDateOfBirth()),
                clean(profile.getAddress(), true));
    }

    /** {@inheritDoc} */
    @Override
    protected PatientProfile fromLine(String line) {
        String[] parts = line.split(DELIMITER, FIELD_COUNT);
        if (parts.length < FIELD_COUNT) {
            return null;
        }
        return new PatientProfile(parts[0].trim(), parts[1].trim(), parts[2].trim(),
                parts[4].trim(), parts[3].trim());
    }

    /** {@inheritDoc} */
    @Override
    protected String getId(PatientProfile profile) {
        return profile.getPatientId();
    }

    /**
     * Finds the profile belonging to a patient.
     *
     * @param patientId identifier of the patient
     * @return the stored profile, or an empty profile when none has been saved yet
     */
    public PatientProfile findByPatientId(String patientId) {
        for (PatientProfile profile : loadAll()) {
            if (profile.getPatientId().equals(patientId)) {
                return profile;
            }
        }
        return PatientProfile.empty(patientId);
    }

    /**
     * Saves a profile, replacing any profile already held for the same patient.
     *
     * @param profile the profile to store
     * @return {@code true} when the file was updated successfully
     */
    public boolean saveOrUpdate(PatientProfile profile) {
        List<PatientProfile> profiles = loadAll();
        boolean replaced = false;
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).getPatientId().equals(profile.getPatientId())) {
                profiles.set(i, profile);
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            profiles.add(profile);
        }
        return saveAll(profiles);
    }
}
