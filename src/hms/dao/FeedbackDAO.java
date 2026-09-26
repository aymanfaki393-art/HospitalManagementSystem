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
import hms.model.Feedback;

/**
 *
 * @author HP
 */
public class FeedbackDAO {
     public void saveFeedback(Feedback f) {
        String line = f.getFeedbackId() + "," + f.getPatientId() + "," + f.getDoctorId() + "," + f.getComment() + "," + f.getRating();

        try {
            FileWriter writer = new FileWriter("Feedback.txt", true);
            writer.write(line);
            writer.write("\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving feedback: " + e.getMessage());
        }
        
        
        
     }   


  public ArrayList<Feedback> loadAllFeedback() {
        ArrayList<Feedback> list = new ArrayList<>();
        File file = new File("Feedback.txt");

        if (!file.exists()) {
            return list;
        }

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");

                Feedback f = new Feedback(parts[0], parts[1], parts[2], parts[3], parts[4]);
                list.add(f);
            }

        } catch (IOException e) {
            System.out.println("Error loading feedback: " + e.getMessage());
        }

        return list;
    }
}