/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.dao;

import java.io.FileWriter;
import java.io.IOException;
import hms.model.User;
import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import hms.model.Doctor;
import hms.model.Patient;
import hms.model.AdminStaff;
import hms.model.MedicalManager;
/**
 *
 * @author HP
 */
public class UserDAO {

    /**
     * Single source of truth for the credential file name.
     *
     * <p>The file was previously written as {@code Users.txt} but read back as
     * {@code users.txt}. Windows ignores the difference, but on any case-sensitive file
     * system every login failed because the reader opened a file that did not exist.
     * Routing every reference through one constant removes the possibility.</p>
     */
    private static final String USERS_FILE = "Users.txt";
    
    
    public void saveUser(User user){
    String line = user.getUserId() + "," + user.getUserName() + "," + user.getPassword() + "," + user.getName() + "," + user.getRole();
    
    try {
        FileWriter writer = new FileWriter (USERS_FILE, true);
        writer.write(line);
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
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            return userList;
        }

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");

            // A short or blank line is skipped rather than aborting the entire load.
            if (parts.length < 5) {
                continue;
            }

            if (parts[4].equals("Doctor")) {
    Doctor d = new Doctor(parts[0], parts[1], parts[2], parts[3], "Unknown");
    userList.add(d);
}
            else if (parts[4].equals("Patient")) {
    Patient p = new Patient(parts[0], parts[1], parts[2], parts[3], "Unknown");
    userList.add(p);
} else if (parts[4].equals("AdminStaff")) {
    AdminStaff a = new AdminStaff(parts[0], parts[1], parts[2], parts[3]);
    userList.add(a);
} else if (parts[4].equals("MedicalManager")) {
    MedicalManager m = new MedicalManager(parts[0], parts[1], parts[2], parts[3]);
    userList.add(m);
}
        }
        scanner.close();

    } catch (IOException e) {
        System.out.println("Error loading users: " + e.getMessage());
    }

    return userList;
}
    
    public void deleteUser(String userIdToDelete) {
    ArrayList<User> allUsers = loadAllUsers();

    try {
        FileWriter writer = new FileWriter(USERS_FILE, false);

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

    try {
        FileWriter writer = new FileWriter(USERS_FILE, false);

        for (User u : allUsers) {
            if (u.getUserId().equals(updatedUser.getUserId())) {
                String line = updatedUser.getUserId() + "," + updatedUser.getUserName() + "," + updatedUser.getPassword() + "," + updatedUser.getName() + "," + updatedUser.getRole();
                writer.write(line);
                writer.write("\n");
            } else {
                String line = u.getUserId() + "," + u.getUserName() + "," + u.getPassword() + "," + u.getName() + "," + u.getRole();
                writer.write(line);
                writer.write("\n");
            }
        }

        writer.close();
    } catch (IOException e) {
        System.out.println("Error updating user: " + e.getMessage());
    }
}
    
}
    


