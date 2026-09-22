package hms.dao;

import hms.model.MedicalRecord;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MedicalRecordDAO {
    private final String fileName = "medicalrecords.txt";

    public void saveMedicalRecord(MedicalRecord record) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(record.toFileString());
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error saving medical record: " + e.getMessage());
        }
    }

    public ArrayList<MedicalRecord> loadAllRecords() {
        ArrayList<MedicalRecord> records = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) {
            return records;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                MedicalRecord record = MedicalRecord.fromFileString(line);
                if (record != null) {
                    records.add(record);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading medical records: " + e.getMessage());
        }
        return records;
    }
}
