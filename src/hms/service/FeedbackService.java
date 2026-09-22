package hms.service;

import hms.dao.AppointmentDAO;
import hms.dao.FeedbackDAO;
import hms.model.Appointment;
import hms.model.AppointmentStatus;
import hms.model.Feedback;
import hms.util.AppLogger;
import hms.util.Validator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Business rules governing the ratings and comments a patient leaves about a consultation.
 *
 * <p>This class implements the fourth row of the Patients section of Table 1.0: submitting
 * ratings and comments about doctors and clinic visits. Feedback is tied to a specific
 * appointment, which keeps it honest: a patient can only rate a consultation that actually
 * took place, and can only rate it once.</p>
 *
 * @author Patient module
 */
public class FeedbackService {

    /** Access to stored feedback entries. */
    private final FeedbackDAO feedbackDAO = new FeedbackDAO();

    /** Access to stored appointments, used to validate what may be rated. */
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    /**
     * Lists the patient's consultations that have taken place and have not yet been rated.
     *
     * @param patientId identifier of the patient
     * @return the appointments still awaiting feedback
     */
    public List<Appointment> getRateableAppointments(String patientId) {
        List<Appointment> rateable = new ArrayList<>();
        for (Appointment appointment : appointmentDAO.findByPatient(patientId)) {
            if (appointment.getStatus() == AppointmentStatus.COMPLETED
                    && !feedbackDAO.existsForAppointment(appointment.getAppointmentId())) {
                rateable.add(appointment);
            }
        }
        return rateable;
    }

    /**
     * Lists the feedback a patient has already submitted.
     *
     * @param patientId identifier of the patient
     * @return the patient's feedback entries
     */
    public List<Feedback> getFeedbackByPatient(String patientId) {
        return feedbackDAO.findByPatient(patientId);
    }

    /**
     * Validates and stores a rating and comment for a completed consultation.
     *
     * @param patientId     identifier of the patient submitting the feedback
     * @param appointmentId identifier of the consultation being rated
     * @param ratingText    the star rating, as entered
     * @param comments      free-text comments
     * @return the outcome of the operation
     */
    public ServiceResult submitFeedback(String patientId, String appointmentId,
                                        String ratingText, String comments) {
        if (Validator.isBlank(appointmentId)) {
            return ServiceResult.fail("Please select the consultation you wish to rate.");
        }
        if (!Validator.isValidRating(ratingText)) {
            return ServiceResult.fail("Please give a rating between 1 and 5.");
        }
        if (Validator.isBlank(comments)) {
            return ServiceResult.fail("Please write a short comment about your visit.");
        }
        if (comments.trim().length() < 5) {
            return ServiceResult.fail("Please write a slightly longer comment about your visit.");
        }

        Appointment appointment = appointmentDAO.findById(appointmentId);
        if (appointment == null) {
            return ServiceResult.fail("That consultation could not be found.");
        }
        if (!appointment.getPatientId().equals(patientId)) {
            return ServiceResult.fail("You may only rate your own consultations.");
        }
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            return ServiceResult.fail("You can only rate a consultation once it has taken place.");
        }
        if (feedbackDAO.existsForAppointment(appointmentId)) {
            return ServiceResult.fail("You have already submitted feedback for that consultation.");
        }

        Feedback feedback = new Feedback(
                feedbackDAO.nextId("FBK", 3), patientId, appointment.getDoctorId(), appointmentId,
                Integer.parseInt(ratingText.trim()), LocalDate.now().toString(), comments.trim());

        if (!feedbackDAO.append(feedback)) {
            return ServiceResult.fail("Your feedback could not be saved. Please try again.");
        }
        AppLogger.info("FeedbackService",
                "Patient " + patientId + " rated appointment " + appointmentId
                        + " as " + feedback.getRating() + "/5");
        return ServiceResult.ok("Thank you. Your feedback has been recorded.");
    }
}
