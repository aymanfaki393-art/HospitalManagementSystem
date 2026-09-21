package hms.dao;

import hms.model.LabRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LabRequestDAO {
    private final String fileName;

    public LabRequestDAO() {
        this("labrequests.txt");
    }

    public LabRequestDAO(String fileName) {
        this.fileName = fileName;
    }

    public void saveLabRequest(LabRequest labRequest) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(labRequest.toFileString());
            writer.write(System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error saving lab request: " + e.getMessage());
        }
    }

    public ArrayList<LabRequest> loadAllLabRequests() {
        ArrayList<LabRequest> labRequests = new ArrayList<>();
        File file = new File(fileName);
        if (!file.exists()) {
            return labRequests;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                LabRequest labRequest = LabRequest.fromFileString(line);
                if (labRequest != null) {
                    labRequests.add(labRequest);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error loading lab requests: " + e.getMessage());
        }
        return labRequests;
    }

    public boolean updateRequestStatus(String requestId, String status) {
        ArrayList<LabRequest> requests = loadAllLabRequests();
        boolean updated = false;
        for (LabRequest request : requests) {
            if (request.getRequestId().equals(requestId)) {
                if (!isValidStatusTransition(request.getStatus(), status)) {
                    return false;
                }
                request.setStatus(status);
                updated = true;
                break;
            }
        }

        if (!updated) {
            return false;
        }

        try (FileWriter writer = new FileWriter(fileName, false)) {
            for (LabRequest request : requests) {
                writer.write(request.toFileString());
                writer.write(System.lineSeparator());
            }
            return true;
        } catch (IOException e) {
            System.out.println("Error updating lab request: " + e.getMessage());
            return false;
        }
    }

    private boolean isValidStatusTransition(String currentStatus, String nextStatus) {
        if ("Pending".equalsIgnoreCase(currentStatus)) {
            return "In Progress".equalsIgnoreCase(nextStatus);
        }
        if ("In Progress".equalsIgnoreCase(currentStatus)) {
            return "Completed".equalsIgnoreCase(nextStatus);
        }
        return false;
    }
}
