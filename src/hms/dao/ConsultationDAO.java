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
import hms.model.Consultation;

/**
 *
 * @author HP
 */
public class ConsultationDAO {
    public void saveConsultation(Consultation c) {
        String line = c.getConsultationId() + "," + c.getPatientId() + "," + c.getDoctorId() + "," + c.getVitals() + "," + c.getNotes() + "," + c.getDate();

        try {
            FileWriter writer = new FileWriter("Consultation.txt", true);
            writer.write(line);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving consultation: " + e.getMessage());
        }
}
    
    public ArrayList<Consultation> loadAllConsultations() {
        ArrayList<Consultation> list = new ArrayList<>();
        File file = new File("Consultation.txt");

        if (!file.exists()) {
            return list;
        }

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                Consultation c = new Consultation(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                list.add(c);
            }

        } catch (IOException e) {
            System.out.println("Error loading consultations: " + e.getMessage());
        }

        return list;
    }
}
