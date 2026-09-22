package hms.service;

import hms.dao.AppointmentDAO;
import hms.dao.DoctorSlotDAO;
import hms.dao.UserDAO;
import hms.model.Appointment;
import hms.model.AppointmentStatus;
import hms.model.Doctor;
import hms.model.DoctorSlot;
import hms.model.SlotStatus;
import hms.model.User;
import hms.util.AppLogger;
import hms.util.Validator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Business rules governing the booking, rescheduling and cancellation of consultations.
 *
 * <p>This class implements the second row of the Patients section of Table 1.0: browsing the
 * available slots of doctors' consultations and booking, rescheduling or cancelling a booking.
 * Bookings are held against slots published by the Medical Manager module, and the two are kept
 * consistent: taking a slot marks it {@link SlotStatus#BOOKED}, releasing one returns it to
 * {@link SlotStatus#AVAILABLE}.</p>
 *
 * @author Patient module
 */
public class AppointmentService {

    /** Access to stored appointments. */
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    /** Access to the consultation slots published by the Medical Manager module. */
    private final DoctorSlotDAO slotDAO = new DoctorSlotDAO();

    /** Access to the shared credential file, used to list doctors. */
    private final UserDAO userDAO = new UserDAO();

    /**
     * Lists every doctor registered in the system.
     *
     * @return the registered doctors, sorted by name
     */
    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        for (User user : userDAO.loadAllUsers()) {
            if (user instanceof Doctor) {
                doctors.add((Doctor) user);
            }
        }
        doctors.sort(Comparator.comparing(Doctor::getName));
        return doctors;
    }

    /**
     * Lists every slot a patient may currently book, for all doctors.
     *
     * <p>This is the no-argument form of {@link #getAvailableSlots(String)}. Offering the same
     * operation under one name with two different parameter lists is <em>method overloading</em>,
     * or static polymorphism: the compiler decides which version runs from the arguments at the
     * call site, so a screen that wants every doctor's slots does not have to pass a null.</p>
     *
     * @return the bookable slots of every doctor, ordered by date and then start time
     */
    public List<DoctorSlot> getAvailableSlots() {
        return getAvailableSlots(null);
    }

    /**
     * Lists the slots a patient may currently book.
     *
     * <p>A slot qualifies when it is marked available and its start time has not already
     * passed. Passing {@code null} or an empty string as the doctor identifier returns the
     * available slots of every doctor.</p>
     *
     * @param doctorId identifier of a doctor to filter by, or {@code null} for all doctors
     * @return the bookable slots, ordered by date and then start time
     */
    public List<DoctorSlot> getAvailableSlots(String doctorId) {
        List<DoctorSlot> available = new ArrayList<>();
        for (DoctorSlot slot : slotDAO.loadAll()) {
            if (!slot.isBookable()) {
                continue;
            }
            if (doctorId != null && !doctorId.isEmpty() && !slot.getDoctorId().equals(doctorId)) {
                continue;
            }
            if (isInThePast(slot.getDate(), slot.getStartTime())) {
                continue;
            }
            available.add(slot);
        }
        available.sort(Comparator.comparing(DoctorSlot::getDate).thenComparing(DoctorSlot::getStartTime));
        return available;
    }

    /**
     * Lists a patient's own appointments, most recent first.
     *
     * @param patientId identifier of the patient
     * @return the patient's appointments, ordered by date descending
     */
    public List<Appointment> getAppointmentsForPatient(String patientId) {
        List<Appointment> appointments = appointmentDAO.findByPatient(patientId);
        appointments.sort(Comparator.comparing(Appointment::getDate)
                .thenComparing(Appointment::getTime).reversed());
        return appointments;
    }

    /**
     * Books a consultation for a patient in a published slot.
     *
     * <p>The operation is refused when the slot no longer exists, has already been taken, lies
     * in the past, or when the patient already holds an active booking at the same date and
     * time. Only once every rule passes are the appointment and the slot written to disk.</p>
     *
     * @param patientId identifier of the booking patient
     * @param slotId    identifier of the slot to reserve
     * @param reason    the patient's stated reason for the visit
     * @return the outcome of the operation
     */
    public ServiceResult book(String patientId, String slotId, String reason) {
        if (Validator.isBlank(slotId)) {
            return ServiceResult.fail("Please select a consultation slot first.");
        }
        if (Validator.isBlank(reason)) {
            return ServiceResult.fail("Please describe the reason for your visit.");
        }
        if (reason.trim().length() < 5) {
            return ServiceResult.fail("Please describe the reason for your visit in a little more detail.");
        }

        DoctorSlot slot = slotDAO.findById(slotId);
        if (slot == null) {
            return ServiceResult.fail("That consultation slot no longer exists.");
        }
        if (!slot.isBookable()) {
            return ServiceResult.fail("That slot has just been taken. Please refresh and choose another.");
        }
        if (isInThePast(slot.getDate(), slot.getStartTime())) {
            return ServiceResult.fail("That slot is in the past and can no longer be booked.");
        }
        if (hasClashingAppointment(patientId, slot.getDate(), slot.getStartTime(), null)) {
            return ServiceResult.fail("You already have an appointment booked at that date and time.");
        }

        Appointment appointment = new Appointment(
                appointmentDAO.nextId("APT", 3), patientId, slot.getDoctorId(), slot.getSlotId(),
                slot.getDate(), slot.getStartTime(), AppointmentStatus.BOOKED, reason.trim());

        if (!appointmentDAO.append(appointment)) {
            return ServiceResult.fail("The booking could not be saved. Please try again.");
        }
        slotDAO.updateStatus(slot.getSlotId(), SlotStatus.BOOKED);

        AppLogger.info("AppointmentService",
                "Patient " + patientId + " booked " + appointment.getAppointmentId()
                        + " in slot " + slot.getSlotId());
        return ServiceResult.ok("Appointment " + appointment.getAppointmentId() + " confirmed for "
                + slot.getDate() + " at " + slot.getStartTime() + ".");
    }

    /**
     * Moves an existing booking to a different slot.
     *
     * <p>The previously held slot is released only after the replacement slot has been
     * validated, so a failed reschedule never leaves the patient without an appointment.</p>
     *
     * @param patientId     identifier of the patient making the request
     * @param appointmentId identifier of the appointment to move
     * @param newSlotId     identifier of the slot to move it to
     * @return the outcome of the operation
     */
    public ServiceResult reschedule(String patientId, String appointmentId, String newSlotId) {
        Appointment appointment = appointmentDAO.findById(appointmentId);
        if (appointment == null) {
            return ServiceResult.fail("That appointment could not be found.");
        }
        if (!appointment.getPatientId().equals(patientId)) {
            return ServiceResult.fail("You may only reschedule your own appointments.");
        }
        if (!appointment.isActive()) {
            return ServiceResult.fail("Only an appointment that is still booked can be rescheduled.");
        }
        if (Validator.isBlank(newSlotId)) {
            return ServiceResult.fail("Please select the new consultation slot.");
        }
        if (newSlotId.equals(appointment.getSlotId())) {
            return ServiceResult.fail("That is the slot the appointment already occupies.");
        }

        DoctorSlot newSlot = slotDAO.findById(newSlotId);
        if (newSlot == null) {
            return ServiceResult.fail("That consultation slot no longer exists.");
        }
        if (!newSlot.isBookable()) {
            return ServiceResult.fail("That slot has just been taken. Please refresh and choose another.");
        }
        if (isInThePast(newSlot.getDate(), newSlot.getStartTime())) {
            return ServiceResult.fail("That slot is in the past and can no longer be booked.");
        }
        if (hasClashingAppointment(patientId, newSlot.getDate(), newSlot.getStartTime(), appointmentId)) {
            return ServiceResult.fail("You already have another appointment at that date and time.");
        }

        String previousSlotId = appointment.getSlotId();
        appointment.setSlotId(newSlot.getSlotId());
        appointment.setDoctorId(newSlot.getDoctorId());
        appointment.setDate(newSlot.getDate());
        appointment.setTime(newSlot.getStartTime());

        if (!appointmentDAO.update(appointment)) {
            return ServiceResult.fail("The change could not be saved. Please try again.");
        }
        slotDAO.updateStatus(newSlot.getSlotId(), SlotStatus.BOOKED);
        slotDAO.updateStatus(previousSlotId, SlotStatus.AVAILABLE);

        AppLogger.info("AppointmentService",
                "Appointment " + appointmentId + " moved from slot " + previousSlotId
                        + " to slot " + newSlot.getSlotId());
        return ServiceResult.ok("Appointment " + appointmentId + " moved to "
                + newSlot.getDate() + " at " + newSlot.getStartTime() + ".");
    }

    /**
     * Cancels a booking and returns its slot to the pool of available slots.
     *
     * @param patientId     identifier of the patient making the request
     * @param appointmentId identifier of the appointment to cancel
     * @return the outcome of the operation
     */
    public ServiceResult cancel(String patientId, String appointmentId) {
        Appointment appointment = appointmentDAO.findById(appointmentId);
        if (appointment == null) {
            return ServiceResult.fail("That appointment could not be found.");
        }
        if (!appointment.getPatientId().equals(patientId)) {
            return ServiceResult.fail("You may only cancel your own appointments.");
        }
        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            return ServiceResult.fail("That appointment has already been cancelled.");
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            return ServiceResult.fail("A consultation that has already taken place cannot be cancelled.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        if (!appointmentDAO.update(appointment)) {
            return ServiceResult.fail("The cancellation could not be saved. Please try again.");
        }
        slotDAO.updateStatus(appointment.getSlotId(), SlotStatus.AVAILABLE);

        AppLogger.info("AppointmentService",
                "Appointment " + appointmentId + " cancelled by patient " + patientId);
        return ServiceResult.ok("Appointment " + appointmentId + " has been cancelled.");
    }

    /**
     * Marks as completed any of the patient's bookings whose start time has passed.
     *
     * <p>Without this step a consultation that has already taken place would remain in the
     * {@code BOOKED} state for ever, and the patient could neither rate it nor see an accurate
     * history. The method is called whenever the appointment list is displayed.</p>
     *
     * @param patientId identifier of the patient whose bookings should be refreshed
     * @return the number of appointments moved to {@link AppointmentStatus#COMPLETED}
     */
    public int refreshCompletedAppointments(String patientId) {
        List<Appointment> all = appointmentDAO.loadAll();
        int changed = 0;
        for (Appointment appointment : all) {
            if (appointment.getPatientId().equals(patientId)
                    && appointment.getStatus() == AppointmentStatus.BOOKED
                    && isInThePast(appointment.getDate(), appointment.getTime())) {
                appointment.setStatus(AppointmentStatus.COMPLETED);
                changed++;
            }
        }
        if (changed > 0) {
            appointmentDAO.saveAll(all);
            AppLogger.info("AppointmentService",
                    "Marked " + changed + " past appointment(s) as completed for " + patientId);
        }
        return changed;
    }

    /**
     * Determines whether a date and time have already passed.
     *
     * <p>A slot whose date or time cannot be parsed is treated as <em>not</em> in the past, so a
     * malformed roster entry is left visible for a human to notice rather than silently hidden.</p>
     *
     * @param date the date in {@code yyyy-MM-dd} form
     * @param time the time in {@code HH:mm} form
     * @return {@code true} when the moment lies before now
     */
    private boolean isInThePast(String date, String time) {
        LocalDate parsedDate = Validator.parseDate(date);
        LocalTime parsedTime = Validator.parseTime(time);
        if (parsedDate == null || parsedTime == null) {
            return false;
        }
        return LocalDateTime.of(parsedDate, parsedTime).isBefore(LocalDateTime.now());
    }

    /**
     * Detects whether a patient already holds an active booking at a given moment.
     *
     * @param patientId        identifier of the patient
     * @param date             the date to test, in {@code yyyy-MM-dd} form
     * @param time             the time to test, in {@code HH:mm} form
     * @param ignoreAppointmentId an appointment to exclude from the check, or {@code null};
     *                            used when rescheduling so a booking does not clash with itself
     * @return {@code true} when a clashing active booking exists
     */
    private boolean hasClashingAppointment(String patientId, String date, String time,
                                           String ignoreAppointmentId) {
        for (Appointment appointment : appointmentDAO.findByPatient(patientId)) {
            if (!appointment.isActive()) {
                continue;
            }
            if (ignoreAppointmentId != null
                    && appointment.getAppointmentId().equals(ignoreAppointmentId)) {
                continue;
            }
            if (appointment.getDate().equals(date) && appointment.getTime().equals(time)) {
                return true;
            }
        }
        return false;
    }
}
