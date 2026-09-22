/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.dao;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;
import hms.model.Rate;

/**
 *
 * @author HP
 */
public class RateDAO {
    
    public void saveRate(Rate rate) {
        try {
            FileWriter writer = new FileWriter("Rate.txt", false);
            writer.write(rate.getConsultationRate() + "," + rate.getInsuranceNetworks());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving rate: " + e.getMessage());
        }
        
    }
    public Rate loadRate() {
        File file = new File("Rate.txt");

        if (!file.exists()) {
            return null;
        }

        try {
            Scanner scanner = new Scanner(file);
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                return new Rate(parts[0], parts[1]);
            }
        } catch (IOException e) {
            System.out.println("Error loading rate: " + e.getMessage());
        }

        return null;
    }
    
}
