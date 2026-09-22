/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.dao;

import hms.model.AdminStaff;
import hms.model.Doctor;
import hms.model.MedicalManager;
import hms.model.Patient;
import hms.model.User;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *
 * @author HP
 */
public class UserDAO {
    
    
    public void saveUser(User user){
    StringBuilder line = new StringBuilder();
    line.append(user.getUserId()).append(",").append(user.getUserName()).append(",")
        .append(user.getPassword()).append(",").append(user.getName()).append(",")
        .append(user.getRole());

    if (user instanceof Doctor) {
        line.append(",").append(((Doctor) user).getSpecialty());
    } else if (user instanceof Patient) {
        line.append(",").append(((Patient) user).getContactnumber());
    }
    
    try {
        FileWriter writer = new FileWriter ("Users.txt", true);
        writer.write(line.toString());
        writer.write("\n");
        writer.close();
        
    }
    
    catch (IOException e){
        System.out.println("Error saving user: " + e.getMessage());
    }
    
    }

   

    public ArrayList<User> loadAllUsers() {
    ArrayList<User> userList = new ArrayList<>();

    try {
        File file = new File("Users.txt");
        if (!file.exists()) {
            return userList;
        }
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] parts = line.split(",");
            if (parts.length < 5) {
                continue;
            }
            String role = parts[4];
            
            if (role.equals("Doctor")) {
                String specialty = parts.length > 5 ? parts[5] : "Unknown";
                Doctor d = new Doctor(parts[0], parts[1], parts[2], parts[3], specialty);
                userList.add(d);
            }
            else if (role.equals("Patient")) {
                String contact = parts.length > 5 ? parts[5] : "Unknown";
                Patient p = new Patient(parts[0], parts[1], parts[2], parts[3], contact);
                userList.add(p);
            } else if (role.equals("AdminStaff")) {
                AdminStaff a = new AdminStaff(parts[0], parts[1], parts[2], parts[3]);
                userList.add(a);
            } else if (role.equals("MedicalManager")) {
                MedicalManager m = new MedicalManager(parts[0], parts[1], parts[2], parts[3]);
                userList.add(m);
            }
        }

    } catch (IOException e) {
        System.out.println("Error loading users: " + e.getMessage());
    }

    return userList;
}
    
    public void deleteUser(String userIdToDelete) {
    ArrayList<User> allUsers = loadAllUsers();

    try {
        FileWriter writer = new FileWriter("Users.txt", false);

        for (User u : allUsers) {
            if (!u.getUserId().equals(userIdToDelete)) {
                String line = u.getUserId() + "," + u.getUserName() + "," + u.getPassword() + "," + u.getName() + "," + u.getRole();
                writer.write(line);
                writer.write("\n");
            }
        }

        writer.close();
    } catch (IOException e) {
        System.out.println("Error deleting user: " + e.getMessage());
    }
}

    public void updateUser(User updatedUser) {
        ArrayList<User> allUsers = loadAllUsers();

        try (FileWriter writer = new FileWriter("Users.txt", false)) {
            for (User user : allUsers) {
                User userToWrite = user.getUserId().equals(updatedUser.getUserId()) ? updatedUser : user;
                StringBuilder line = new StringBuilder();
                line.append(userToWrite.getUserId()).append(",")
                        .append(userToWrite.getUserName()).append(",")
                        .append(userToWrite.getPassword()).append(",")
                        .append(userToWrite.getName()).append(",")
                        .append(userToWrite.getRole());

                if (userToWrite instanceof Doctor) {
                    line.append(",").append(((Doctor) userToWrite).getSpecialty());
                } else if (userToWrite instanceof Patient) {
                    line.append(",").append(((Patient) userToWrite).getContactnumber());
                }

                writer.write(line.toString());
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }
}
    

