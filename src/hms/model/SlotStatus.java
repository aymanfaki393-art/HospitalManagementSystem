package hms.model;

/**
 * Availability states of a consultation slot on a doctor's roster.
 *
 * @author Patient module
 */
public enum SlotStatus {

    /** The slot is open and may be booked by a patient. */
    AVAILABLE,

    /** The slot has been reserved by a patient. */
    BOOKED,

    /** The slot has been withdrawn by the medical manager and cannot be booked. */
    UNAVAILABLE;

    /**
     * Converts text loaded from a data file into a slot status.
     *
     * @param value the raw text read from the data file
     * @return the matching status, or {@link #AVAILABLE} when the text cannot be interpreted
     */
    public static SlotStatus fromText(String value) {
        if (value == null) {
            return AVAILABLE;
        }
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return AVAILABLE;
        }
    }
}
