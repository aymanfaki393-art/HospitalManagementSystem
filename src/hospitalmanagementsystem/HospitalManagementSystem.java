/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


package hospitalmanagementsystem;

/**
 *
 * @author HP
 */

import hms.model.Doctor;
import hms.model.AdminStaff;


public class HospitalManagementSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Doctor doc1 = new Doctor("D001", "drAli", "1234", "Dr. Ali", "Dermatologist");
        doc1.editProfile();
        
        AdminStaff admin1 = new AdminStaff("AD001", "ad_omar", "1234", "Omar");
        System.out.println(admin1.getName());
        admin1.editProfile();
        
        
        
}
        

    
    
}
