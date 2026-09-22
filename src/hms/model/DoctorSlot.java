package hms.model;

/**
 * A single consultation slot on a doctor's roster.
 *
 * <p>Slots are the unit a patient books against. The Medical Manager module is responsible
 * for creating them when it builds shift rosters; the Patient module reads the available
 * ones and flips their status when a booking is made or released. Both modules share the
 * same {@code DoctorSlots.txt} file, so the format is documented here:</p>
 *
 * <pre>slotId,doctorId,date,startTime,endTime,status</pre>
 *
 * @author Patient module
 */
public class DoctorSlot implements Describable {

    /** Unique slot identifier, e.g. {@code SLT001}. */
    private final String slotId;

    /** Identifier of the doctor who will hold the consultation. */
    private final String doctorId;

    /** Date of the slot in {@code yyyy-MM-dd} form. */
    private final String date;

    /** Start time in {@code HH:mm} form. */
    private final String startTime;

    /** End time in {@code HH:mm} form. */
    private final String endTime;

    /** Whether the slot is open, taken or withdrawn. */
    private SlotStatus status;

    /**
     * Creates a slot.
     *
     * @param slotId    unique slot identifier
     * @param doctorId  identifier of the doctor
     * @param date      date in {@code yyyy-MM-dd} form
     * @param startTime start time in {@code HH:mm} form
     * @param endTime   end time in {@code HH:mm} form
     * @param status    current availability of the slot
     */
    public DoctorSlot(String slotId, String doctorId, String date,
                      String startTime, String endTime, SlotStatus status) {
        this.slotId = slotId;
        this.doctorId = doctorId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    /** @return the unique slot identifier */
    public String getSlotId() {
        return slotId;
    }

    /** @return the identifier of the doctor holding the slot */
    public String getDoctorId() {
        return doctorId;
    }

    /** @return the date of the slot in {@code yyyy-MM-dd} form */
    public String getDate() {
        return date;
    }

    /** @return the start time in {@code HH:mm} form */
    public String getStartTime() {
        return startTime;
    }

    /** @return the end time in {@code HH:mm} form */
    public String getEndTime() {
        return endTime;
    }

    /** @return the current availability of the slot */
    public SlotStatus getStatus() {
        return status;
    }

    /** @param status the new availability of the slot */
    public void setStatus(SlotStatus status) {
        this.status = status;
    }

    /**
     * Reports whether a patient may book this slot.
     *
     * @return {@code true} when the slot is {@link SlotStatus#AVAILABLE}
     */
    public boolean isBookable() {
        return status == SlotStatus.AVAILABLE;
    }

    /** {@inheritDoc} */
    @Override
    public String getSummary() {
        return date + "  " + startTime + "-" + endTime + "  (" + status + ")";
    }
}
