/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.dao;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import hms.model.Appointment;


public class AppointmentDAO {
    public void saveAppointment(Appointment a) {
        String line = a.getAppointmentId() + "," + a.getPatientId() + "," + a.getDoctorId() + "," + a.getDate() + "," + a.getTime();

        try {
            FileWriter writer = new FileWriter("Appointment.txt", true);
            writer.write(line);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving appointment: " + e.getMessage());
        }
    }
    
    public ArrayList<Appointment> loadAllAppointments() {
        ArrayList<Appointment> list = new ArrayList<>();
        File file = new File("Appointment.txt");

        if (!file.exists()) {
            return list;
        }

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                Appointment a = new Appointment(parts[0], parts[1], parts[2], parts[3], parts[4]);
                list.add(a);
            }

        } catch (IOException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }

        return list;
    }
    
}
