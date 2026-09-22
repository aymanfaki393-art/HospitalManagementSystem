package hms.model;

/**
 * A rating and comment left by a patient about a completed consultation.
 *
 * <p>Persisted to {@code Feedback.txt} in the form:</p>
 *
 * <pre>feedbackId,patientId,doctorId,appointmentId,rating,date,comments</pre>
 *
 * <p>The free-text comment is written last so that a comma typed by the patient cannot
 * corrupt the surrounding fields when the line is split.</p>
 *
 * @author Patient module
 */
public class Feedback implements Describable {

    /** Unique feedback identifier, e.g. {@code FBK001}. */
    private final String feedbackId;

    /** Identifier of the patient who left the feedback. */
    private final String patientId;

    /** Identifier of the doctor the feedback concerns. */
    private final String doctorId;

    /** Identifier of the appointment the feedback concerns. */
    private final String appointmentId;

    /** Star rating from 1 to 5 inclusive. */
    private final int rating;

    /** Date the feedback was submitted, in {@code yyyy-MM-dd} form. */
    private final String date;

    /** Free-text comments left by the patient. */
    private final String comments;

    /**
     * Creates a feedback entry.
     *
     * @param feedbackId    unique feedback identifier
     * @param patientId     identifier of the patient
     * @param doctorId      identifier of the doctor
     * @param appointmentId identifier of the appointment being rated
     * @param rating        star rating from 1 to 5
     * @param date          submission date in {@code yyyy-MM-dd} form
     * @param comments      free-text comments
     */
    public Feedback(String feedbackId, String patientId, String doctorId, String appointmentId,
                    int rating, String date, String comments) {
        this.feedbackId = feedbackId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.rating = rating;
        this.date = date;
        this.comments = comments;
    }

    /** @return the unique feedback identifier */
    public String getFeedbackId() {
        return feedbackId;
    }

    /** @return the identifier of the patient */
    public String getPatientId() {
        return patientId;
    }

    /** @return the identifier of the doctor */
    public String getDoctorId() {
        return doctorId;
    }

    /** @return the identifier of the appointment being rated */
    public String getAppointmentId() {
        return appointmentId;
    }

    /** @return the star rating from 1 to 5 */
    public int getRating() {
        return rating;
    }

    /** @return the submission date in {@code yyyy-MM-dd} form */
    public String getDate() {
        return date;
    }

    /** @return the free-text comments */
    public String getComments() {
        return comments;
    }

    /**
     * Renders the rating as stars for display.
     *
     * @return a string of filled and empty stars representing the rating
     */
    public String getStars() {
        StringBuilder stars = new StringBuilder();
        for (int i = 1; i <= 5; i++) {
            stars.append(i <= rating ? '*' : '-');
        }
        return stars.toString();
    }

    /** {@inheritDoc} */
    @Override
    public String getSummary() {
        return date + "  " + getStars() + "  Dr " + doctorId;
    }
}
