package hms.dao;

import hms.model.Feedback;
import java.util.ArrayList;
import java.util.List;

/**
 * Data-access object for patient feedback, stored in {@code Feedback.txt}.
 *
 * <p>Line format: {@code feedbackId,patientId,doctorId,appointmentId,rating,date,comments}.
 * The patient's free-text comment is written last so that a comma inside it cannot shift the
 * other fields.</p>
 *
 * @author Patient module
 */
public class FeedbackDAO extends TextFileDAO<Feedback> {

    /** Number of fields on a well-formed line. */
    private static final int FIELD_COUNT = 7;

    /** Creates a data-access object bound to {@code Feedback.txt}. */
    public FeedbackDAO() {
        super("Feedback.txt");
    }

    /** {@inheritDoc} */
    @Override
    protected String toLine(Feedback feedback) {
        return String.join(DELIMITER,
                clean(feedback.getFeedbackId()),
                clean(feedback.getPatientId()),
                clean(feedback.getDoctorId()),
                clean(feedback.getAppointmentId()),
                String.valueOf(feedback.getRating()),
                clean(feedback.getDate()),
                clean(feedback.getComments(), true));
    }

    /** {@inheritDoc} */
    @Override
    protected Feedback fromLine(String line) {
        String[] parts = line.split(DELIMITER, FIELD_COUNT);
        if (parts.length < FIELD_COUNT) {
            return null;
        }
        int rating;
        try {
            rating = Integer.parseInt(parts[4].trim());
        } catch (NumberFormatException e) {
            return null;
        }
        return new Feedback(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                rating, parts[5].trim(), parts[6].trim());
    }

    /** {@inheritDoc} */
    @Override
    protected String getId(Feedback feedback) {
        return feedback.getFeedbackId();
    }

    /**
     * Lists every feedback entry submitted by one patient.
     *
     * @param patientId identifier of the patient
     * @return the patient's feedback entries, in file order
     */
    public List<Feedback> findByPatient(String patientId) {
        List<Feedback> result = new ArrayList<>();
        for (Feedback feedback : loadAll()) {
            if (feedback.getPatientId().equals(patientId)) {
                result.add(feedback);
            }
        }
        return result;
    }

    /**
     * Reports whether an appointment has already been rated.
     *
     * @param appointmentId identifier of the appointment
     * @return {@code true} when feedback already exists for that appointment
     */
    public boolean existsForAppointment(String appointmentId) {
        for (Feedback feedback : loadAll()) {
            if (feedback.getAppointmentId().equals(appointmentId)) {
                return true;
            }
        }
        return false;
    }
}
