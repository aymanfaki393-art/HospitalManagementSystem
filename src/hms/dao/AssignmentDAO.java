/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.dao;
import java.io.FileWriter;
import java.io.IOException;
import hms.model.Assignment;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class AssignmentDAO {
    public void saveAssignment(Assignment assignment){
        String line = assignment.getDoctorId() + "," + assignment.getManagerId();
        
        try{
            FileWriter writer = new FileWriter("Assignment.txt", true);
            writer.write(line);
            writer.write("\n");
            writer.close();
        }
    catch (IOException e){
        System.out.println("Error saving assignment: " + e.getMessage());
    }
    }
    
    
    public ArrayList<Assignment> loadAllAssignments() {
    ArrayList<Assignment> assignmentList = new ArrayList<>();
    File file = new File("Assignment.txt");

    if (!file.exists()) {
        return assignmentList;
    }

    try {
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");

            Assignment a = new Assignment(parts[0], parts[1]);
            assignmentList.add(a);
        }

    } catch (IOException e) {
        System.out.println("Error loading assignments: " + e.getMessage());
    }

    return assignmentList;
}
}
