package hms.dao;

import hms.model.DoctorSlot;
import hms.model.SlotStatus;
import java.util.ArrayList;
import java.util.List;

/**
 * Data-access object for consultation slots, stored in {@code DoctorSlots.txt}.
 *
 * <p>Line format: {@code slotId,doctorId,date,startTime,endTime,status}.</p>
 *
 * <p>This file is shared with the Medical Manager module, which creates slots when it builds
 * shift rosters. The Patient module only reads slots and changes their status between
 * {@link SlotStatus#AVAILABLE} and {@link SlotStatus#BOOKED}; it never deletes a slot that
 * another module created.</p>
 *
 * @author Patient module
 */
public class DoctorSlotDAO extends TextFileDAO<DoctorSlot> {

    /** Number of fields on a well-formed line. */
    private static final int FIELD_COUNT = 6;

    /** Creates a data-access object bound to {@code DoctorSlots.txt}. */
    public DoctorSlotDAO() {
        super("DoctorSlots.txt");
    }

    /** {@inheritDoc} */
    @Override
    protected String toLine(DoctorSlot slot) {
        return String.join(DELIMITER,
                clean(slot.getSlotId()),
                clean(slot.getDoctorId()),
                clean(slot.getDate()),
                clean(slot.getStartTime()),
                clean(slot.getEndTime()),
                slot.getStatus().name());
    }

    /** {@inheritDoc} */
    @Override
    protected DoctorSlot fromLine(String line) {
        String[] parts = line.split(DELIMITER);
        if (parts.length < FIELD_COUNT) {
            return null;
        }
        return new DoctorSlot(parts[0].trim(), parts[1].trim(), parts[2].trim(),
                parts[3].trim(), parts[4].trim(), SlotStatus.fromText(parts[5]));
    }

    /** {@inheritDoc} */
    @Override
    protected String getId(DoctorSlot slot) {
        return slot.getSlotId();
    }

    /**
     * Finds a slot by its identifier.
     *
     * @param slotId identifier to search for
     * @return the matching slot, or {@code null} when no slot carries that identifier
     */
    public DoctorSlot findById(String slotId) {
        for (DoctorSlot slot : loadAll()) {
            if (slot.getSlotId().equals(slotId)) {
                return slot;
            }
        }
        return null;
    }

    /**
     * Lists every slot belonging to one doctor.
     *
     * @param doctorId identifier of the doctor
     * @return the doctor's slots, in file order
     */
    public List<DoctorSlot> findByDoctor(String doctorId) {
        List<DoctorSlot> result = new ArrayList<>();
        for (DoctorSlot slot : loadAll()) {
            if (slot.getDoctorId().equals(doctorId)) {
                result.add(slot);
            }
        }
        return result;
    }

    /**
     * Changes the availability of one slot and writes the change to disk.
     *
     * @param slotId identifier of the slot to change
     * @param status the new availability
     * @return {@code true} when the slot was found and the file was updated
     */
    public boolean updateStatus(String slotId, SlotStatus status) {
        List<DoctorSlot> slots = loadAll();
        for (DoctorSlot slot : slots) {
            if (slot.getSlotId().equals(slotId)) {
                slot.setStatus(status);
                return saveAll(slots);
            }
        }
        return false;
    }
}
