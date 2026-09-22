package hms.model;

/**
 * The lifecycle states an appointment can occupy.
 *
 * <p>Using an enumeration instead of free-text status strings prevents invalid values from
 * ever entering the data files and allows the compiler to check every comparison.</p>
 *
 * @author Patient module
 */
public enum AppointmentStatus {

    /** The appointment has been reserved by the patient and has not yet taken place. */
    BOOKED,

    /** The consultation has taken place; the doctor may attach records and prescriptions. */
    COMPLETED,

    /** The patient (or the clinic) cancelled the appointment and the slot was released. */
    CANCELLED;

    /**
     * Converts text loaded from a data file into a status, defaulting safely when the
     * stored value is missing or unrecognised.
     *
     * @param value the raw text read from the data file
     * @return the matching status, or {@link #BOOKED} when the text cannot be interpreted
     */
    public static AppointmentStatus fromText(String value) {
        if (value == null) {
            return BOOKED;
        }
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return BOOKED;
        }
    }
}
