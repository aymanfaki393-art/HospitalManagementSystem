package hms.service;

import hms.dao.MedicalRecordDAO;
import hms.dao.PatientProfileDAO;
import hms.dao.PrescriptionDAO;
import hms.dao.UserDAO;
import hms.model.MedicalRecord;
import hms.model.Patient;
import hms.model.PatientProfile;
import hms.model.Prescription;
import hms.model.User;
import hms.util.AppLogger;
import hms.util.Validator;
import java.util.List;

/**
 * Business operations a patient performs on their own account.
 *
 * <p>Covers the profile-editing and history-viewing entries of Table 1.0 in the coursework
 * specification. Every rule is enforced here rather than in the graphical layer, so the same
 * validation applies however the operation is invoked.</p>
 *
 * @author Patient module
 */
public class PatientService {

    /** Access to the patient's extended personal details. */
    private final PatientProfileDAO profileDAO = new PatientProfileDAO();

    /** Access to the shared credential file. */
    private final UserDAO userDAO = new UserDAO();

    /** Access to clinical records written by doctors. */
    private final MedicalRecordDAO recordDAO = new MedicalRecordDAO();

    /** Access to prescriptions written by doctors. */
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();

    /**
     * Loads the personal details of a patient.
     *
     * @param patientId identifier of the patient
     * @return the stored profile, or an empty one when the patient has not saved details yet
     */
    public PatientProfile getProfile(String patientId) {
        return profileDAO.findByPatientId(patientId);
    }

    /**
     * Validates and saves a patient's personal details.
     *
     * <p>The contact number, e-mail address and date of birth are all checked before anything
     * is written, so a rejected update leaves the stored profile untouched.</p>
     *
     * @param patientId   identifier of the patient
     * @param contact     telephone contact number
     * @param email       e-mail address
     * @param dateOfBirth date of birth in {@code yyyy-MM-dd} form
     * @param address     residential address
     * @return the outcome of the operation
     */
    public ServiceResult updateProfile(String patientId, String contact, String email,
                                       String dateOfBirth, String address) {
        if (!Validator.isValidContactNumber(contact)) {
            return ServiceResult.fail("Contact number must be 9 to 15 digits, optionally starting with '+'.");
        }
        if (!Validator.isValidEmail(email)) {
            return ServiceResult.fail("Please enter a valid e-mail address, for example name@example.com.");
        }
        if (!Validator.isValidDateOfBirth(dateOfBirth)) {
            return ServiceResult.fail("Date of birth must be a real past date in yyyy-MM-dd form.");
        }
        if (Validator.isBlank(address)) {
            return ServiceResult.fail("Address cannot be left empty.");
        }

        PatientProfile profile = new PatientProfile(patientId, contact.trim(), email.trim(),
                address.trim(), dateOfBirth.trim());
        if (!profileDAO.saveOrUpdate(profile)) {
            return ServiceResult.fail("The profile could not be saved. Please try again.");
        }
        AppLogger.info("PatientService", "Profile updated for " + patientId);
        return ServiceResult.ok("Your profile has been updated successfully.");
    }

    /**
     * Changes the patient's login password.
     *
     * <p>The current password must be supplied correctly, the new password must satisfy the
     * length policy and the confirmation must match, before the credential file is rewritten.</p>
     *
     * @param patient         the logged-in patient
     * @param currentPassword the password the patient believes is in force
     * @param newPassword     the requested new password
     * @param confirmPassword repeat of the requested new password
     * @return the outcome of the operation
     */
    public ServiceResult changePassword(Patient patient, String currentPassword,
                                        String newPassword, String confirmPassword) {
        if (!patient.checkPassword(currentPassword)) {
            return ServiceResult.fail("Your current password is not correct.");
        }
        if (!Validator.isValidPassword(newPassword)) {
            return ServiceResult.fail("The new password must be at least 4 characters long.");
        }
        if (!newPassword.equals(confirmPassword)) {
            return ServiceResult.fail("The new password and its confirmation do not match.");
        }
        if (newPassword.equals(currentPassword)) {
            return ServiceResult.fail("The new password must be different from the current one.");
        }

        patient.setPassword(newPassword);
        userDAO.updateUser(patient);
        AppLogger.info("PatientService", "Password changed for " + patient.getUserId());
        return ServiceResult.ok("Your password has been changed successfully.");
    }

    /**
     * Updates the patient's display name in the shared credential file.
     *
     * @param patient the logged-in patient
     * @param newName the requested new name
     * @return the outcome of the operation
     */
    public ServiceResult updateName(Patient patient, String newName) {
        if (Validator.isBlank(newName)) {
            return ServiceResult.fail("Your name cannot be left empty.");
        }
        if (newName.contains(",")) {
            return ServiceResult.fail("Your name cannot contain a comma, as it would corrupt the data file.");
        }
        patient.setName(newName.trim());
        userDAO.updateUser(patient);
        AppLogger.info("PatientService", "Name updated for " + patient.getUserId());
        return ServiceResult.ok("Your name has been updated successfully.");
    }

    /**
     * Lists the clinical records written about a patient.
     *
     * @param patientId identifier of the patient
     * @return the patient's medical history
     */
    public List<MedicalRecord> getMedicalHistory(String patientId) {
        return recordDAO.findByPatient(patientId);
    }

    /**
     * Lists the prescriptions issued to a patient.
     *
     * @param patientId identifier of the patient
     * @return the patient's prescriptions
     */
    public List<Prescription> getPrescriptions(String patientId) {
        return prescriptionDAO.findByPatient(patientId);
    }

    /**
     * Resolves a user identifier into a display name, used when showing which doctor
     * an appointment or record belongs to.
     *
     * @param userId identifier to resolve
     * @return the matching user's name, or the identifier itself when no user matches
     */
    public String resolveUserName(String userId) {
        for (User user : userDAO.loadAllUsers()) {
            if (user.getUserId().equals(userId)) {
                return user.getName();
            }
        }
        return userId;
    }
}
