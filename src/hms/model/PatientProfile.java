package hms.model;

/**
 * The personal details a patient maintains about themselves.
 *
 * <p>These attributes are stored apart from the shared {@code Users.txt} credential record
 * so that the Patient module can extend the information it captures without altering the
 * file format relied upon by the Administrative Staff module.</p>
 *
 * @author Patient module
 */
public class PatientProfile {

    /** Identifier of the patient this profile belongs to; also the primary key of the file. */
    private final String patientId;

    /** Telephone contact number. */
    private String contactNumber;

    /** E-mail address. */
    private String email;

    /** Residential address. */
    private String address;

    /** Date of birth in ISO form, {@code yyyy-MM-dd}. */
    private String dateOfBirth;

    /**
     * Creates a profile.
     *
     * @param patientId     identifier of the owning patient
     * @param contactNumber telephone contact number
     * @param email         e-mail address
     * @param address       residential address
     * @param dateOfBirth   date of birth in {@code yyyy-MM-dd} form
     */
    public PatientProfile(String patientId, String contactNumber, String email,
                          String address, String dateOfBirth) {
        this.patientId = patientId;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Creates an empty profile for a patient who has not yet filled one in.
     *
     * @param patientId identifier of the owning patient
     * @return a profile whose detail fields are empty strings
     */
    public static PatientProfile empty(String patientId) {
        return new PatientProfile(patientId, "", "", "", "");
    }

    /** @return the identifier of the owning patient */
    public String getPatientId() {
        return patientId;
    }

    /** @return the telephone contact number */
    public String getContactNumber() {
        return contactNumber;
    }

    /** @param contactNumber the new telephone contact number */
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /** @return the e-mail address */
    public String getEmail() {
        return email;
    }

    /** @param email the new e-mail address */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @return the residential address */
    public String getAddress() {
        return address;
    }

    /** @param address the new residential address */
    public void setAddress(String address) {
        this.address = address;
    }

    /** @return the date of birth in {@code yyyy-MM-dd} form */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /** @param dateOfBirth the new date of birth in {@code yyyy-MM-dd} form */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
