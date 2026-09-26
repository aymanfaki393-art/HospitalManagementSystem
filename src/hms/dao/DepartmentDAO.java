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
import hms.model.Department;
/**
 *
 * @author HP
 */
public class DepartmentDAO {
    
    public void saveDepartment(Department d) {
        String line = d.getDepartmentId() + "," + d.getDepartmentName() + "," + d.getHeadDoctorId();

        try {
            FileWriter writer = new FileWriter("Department.txt", true);
            writer.write(line);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving department: " + e.getMessage());
        }
    }
public ArrayList<Department> loadAllDepartments() {
        ArrayList<Department> list = new ArrayList<>();
        File file = new File("Department.txt");

        if (!file.exists()) {
            return list;
        }

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                Department d = new Department(parts[0], parts[1], parts[2]);
                list.add(d);
            }

        } catch (IOException e) {
            System.out.println("Error loading departments: " + e.getMessage());
        }

        return list;
    }
}
