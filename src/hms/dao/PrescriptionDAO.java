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
import hms.model.Prescription;



public class PrescriptionDAO {

    public void savePrescription(Prescription p) {
        String line = p.getPrescriptionId() + "," + p.getPatientId() + "," + p.getDoctorId() + "," + p.getMedication() + "," + p.getDosage() + "," + p.getDate();

        try {
            FileWriter writer = new FileWriter("Prescription.txt", true);
            writer.write(line);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving prescription: " + e.getMessage());
        }
    }

    public ArrayList<Prescription> loadAllPrescriptions() {
        ArrayList<Prescription> list = new ArrayList<>();
        File file = new File("Prescription.txt");

        if (!file.exists()) {
            return list;
        }

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                Prescription p = new Prescription(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
                list.add(p);
            }

        } catch (IOException e) {
            System.out.println("Error loading prescriptions: " + e.getMessage());
        }

        return list;
    }
}
