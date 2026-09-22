package hms.dao;

import hms.model.Appointment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AppointmentDAO {
    private final String fileName;

    public AppointmentDAO() {
        this("appointments.txt");
    }

    public AppointmentDAO(String fileName) {
        this.fileName = fileName;
    }

    public void saveAppointment(Appointment appointment) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(appointment.toFileString());
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error saving appointment: " + e.getMessage());
        }
    }

    public ArrayList<Appointment> loadAppointments() {
        ArrayList<Appointment> appointments = new ArrayList<>();
        File file = new File(fileName);

        if (!file.exists()) {
            return appointments;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                Appointment appointment = Appointment.fromFileString(line);
                if (appointment != null) {
                    appointments.add(appointment);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }

        return appointments;
    }

    public ArrayList<Appointment> getAppointmentsForDoctor(String doctorId) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment appointment : loadAppointments()) {
            if (doctorId.equals(appointment.getDoctorId())) {
                result.add(appointment);
            }
        }
        return result;
    }

    public void updateAppointment(Appointment updatedAppointment) {
        ArrayList<Appointment> appointments = loadAppointments();
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentId().equals(updatedAppointment.getAppointmentId())) {
                appointments.set(i, updatedAppointment);
                break;
            }
        }

        try (FileWriter writer = new FileWriter(fileName, false)) {
            for (Appointment appointment : appointments) {
                writer.write(appointment.toFileString());
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error updating appointment: " + e.getMessage());
        }
    }
}
