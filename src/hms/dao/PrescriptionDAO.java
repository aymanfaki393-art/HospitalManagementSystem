package hms.dao;

import hms.model.Prescription;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class PrescriptionDAO {
    private final String fileName = "prescriptions.txt";

    public void savePrescription(Prescription prescription) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(prescription.toFileString());
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error saving prescription: " + e.getMessage());
        }
    }

    public ArrayList<Prescription> loadAllPrescriptions() {
        ArrayList<Prescription> prescriptions = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) {
            return prescriptions;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                Prescription prescription = Prescription.fromFileString(line);
                if (prescription != null) {
                    prescriptions.add(prescription);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading prescriptions: " + e.getMessage());
        }
        return prescriptions;
    }
}
