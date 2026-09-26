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
import hms.model.Roster;
/**
 *
 * @author HP
 */
public class RosterDAO {
    
    public void saveRoster(Roster r) {
        String line = r.getRosterId() + "," + r.getDoctorId() + "," + r.getShiftDay() + "," + r.getShiftTime();

        try {
            FileWriter writer = new FileWriter("Roster.txt", true);
            writer.write(line);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving roster: " + e.getMessage());
        }
    }
    
    public ArrayList<Roster> loadAllRosters() {
        ArrayList<Roster> list = new ArrayList<>();
        File file = new File("Roster.txt");

        if (!file.exists()) {
            return list;
        }

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                Roster r = new Roster(parts[0], parts[1], parts[2], parts[3]);
                list.add(r);
            }

        } catch (IOException e) {
            System.out.println("Error loading rosters: " + e.getMessage());
        }

        return list;
    }
    
}
