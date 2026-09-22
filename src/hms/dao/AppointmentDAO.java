package hms.dao;

import hms.model.Appointment;
import hms.model.AppointmentStatus;
import java.util.ArrayList;
import java.util.List;

/**
 * Data-access object for appointments, stored in {@code Appointments.txt}.
 *
 * <p>Line format: {@code appointmentId,patientId,doctorId,slotId,date,time,status,reason}.
 * The patient's free-text reason is written last so that a comma inside it cannot shift the
 * other fields.</p>
 *
 * @author Patient module
 */
public class AppointmentDAO extends TextFileDAO<Appointment> {

    /** Number of fields on a well-formed line. */
    private static final int FIELD_COUNT = 8;

    /** Creates a data-access object bound to {@code Appointments.txt}. */
    public AppointmentDAO() {
        super("Appointments.txt");
    }

    /** {@inheritDoc} */
    @Override
    protected String toLine(Appointment appointment) {
        return String.join(DELIMITER,
                clean(appointment.getAppointmentId()),
                clean(appointment.getPatientId()),
                clean(appointment.getDoctorId()),
                clean(appointment.getSlotId()),
                clean(appointment.getDate()),
                clean(appointment.getTime()),
                appointment.getStatus().name(),
                clean(appointment.getReason(), true));
    }

    /** {@inheritDoc} */
    @Override
    protected Appointment fromLine(String line) {
        String[] parts = line.split(DELIMITER, FIELD_COUNT);
        if (parts.length < FIELD_COUNT) {
            return null;
        }
        return new Appointment(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(),
                parts[4].trim(), parts[5].trim(), AppointmentStatus.fromText(parts[6]), parts[7].trim());
    }

    /** {@inheritDoc} */
    @Override
    protected String getId(Appointment appointment) {
        return appointment.getAppointmentId();
    }

    /**
     * Finds an appointment by its identifier.
     *
     * @param appointmentId identifier to search for
     * @return the matching appointment, or {@code null} when none carries that identifier
     */
    public Appointment findById(String appointmentId) {
        for (Appointment appointment : loadAll()) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                return appointment;
            }
        }
        return null;
    }

    /**
     * Lists every appointment belonging to one patient.
     *
     * @param patientId identifier of the patient
     * @return the patient's appointments, in file order
     */
    public List<Appointment> findByPatient(String patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : loadAll()) {
            if (appointment.getPatientId().equals(patientId)) {
                result.add(appointment);
            }
        }
        return result;
    }

    /**
     * Replaces the stored copy of an appointment with the supplied one.
     *
     * @param updated the appointment carrying the new values
     * @return {@code true} when a matching appointment was found and the file was updated
     */
    public boolean update(Appointment updated) {
        List<Appointment> appointments = loadAll();
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentId().equals(updated.getAppointmentId())) {
                appointments.set(i, updated);
                return saveAll(appointments);
            }
        }
        return false;
    }
}
