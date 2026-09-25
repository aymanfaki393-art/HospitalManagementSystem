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
import hms.model.LabRequest;
public class LabRequestDAO {
    
    
     public void saveRequest(LabRequest r) {
        String line = r.getRequestId() + "," + r.getPatientId() + "," + r.getDoctorId() + "," + r.getTestType() + "," + r.getDate();

        try {
            FileWriter writer = new FileWriter("LabRequest.txt", true);
            writer.write(line);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving lab request: " + e.getMessage());
        }
    }

    public ArrayList<LabRequest> loadAllRequests() {
        ArrayList<LabRequest> list = new ArrayList<>();
        File file = new File("LabRequest.txt");

        if (!file.exists()) {
            return list;
        }

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                LabRequest r = new LabRequest(parts[0], parts[1], parts[2], parts[3], parts[4]);
                list.add(r);
            }

        } catch (IOException e) {
            System.out.println("Error loading lab requests: " + e.getMessage());
        }

        return list;
    }
}
