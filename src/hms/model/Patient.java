package hms.model;

/**
 * A patient of APU Medical Centre.
 *
 * <p>{@code Patient} <em>inherits</em> identity and authentication state from {@link User}
 * and adds the single attribute that the shared {@code Users.txt} record carries for this
 * role, namely the contact number. Richer personal details (e-mail, address, date of birth)
 * are held separately in {@link PatientProfile} so that the file format written by
 * {@code hms.dao.UserDAO}, which is shared with the other three roles, does not have to
 * change.</p>
 *
 * <p>The field is {@code private} and reachable only through accessors that validate their
 * input, which is the <em>encapsulation</em> principle applied at class level.</p>
 *
 * @author Patient module
 */
public class Patient extends User {

    /** Telephone number on which the patient can be reached. */
    private String contactNumber;

    /**
     * Creates a patient.
     *
     * @param userId        unique identifier, e.g. {@code PAT001}
     * @param username      login name
     * @param password      login password
     * @param name          full name of the patient
     * @param contactNumber telephone contact number
     */
    public Patient(String userId, String username, String password, String name, String contactNumber) {
        super(userId, username, password, name, "Patient");
        this.contactNumber = contactNumber;
    }

    /**
     * Returns the patient's contact number.
     *
     * @return the stored contact number
     */
    public String getContactNumber() {
        return contactNumber;
    }

    /**
     * Retained for compatibility with code written before the accessor was renamed.
     *
     * @return the stored contact number
     * @deprecated use {@link #getContactNumber()} instead
     */
    @Deprecated
    public String getContactnumber() {
        return contactNumber;
    }

    /**
     * Updates the contact number.
     *
     * @param contactNumber the new contact number; ignored when {@code null} or blank
     */
    public void setContactNumber(String contactNumber) {
        if (contactNumber != null && !contactNumber.trim().isEmpty()) {
            this.contactNumber = contactNumber.trim();
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Every concrete subclass of {@link User} must supply its own behaviour for this
     * abstract operation, so a single call made through a {@code User} reference executes a
     * different implementation depending on the runtime type. That is <em>polymorphism</em>
     * by method overriding.</p>
     */
    @Override
    public void editProfile() {
        System.out.println("Patient " + getName() + " (" + getUserId() + ") is editing their profile.");
    }

    /**
     * Returns a display label used by combo boxes and tables.
     *
     * @return the patient's name followed by their identifier
     */
    @Override
    public String toString() {
        return getName() + " (" + getUserId() + ")";
    }
}
