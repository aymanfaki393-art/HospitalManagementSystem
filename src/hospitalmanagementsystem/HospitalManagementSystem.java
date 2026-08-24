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
import hms.model.Patient;
import hms.model.MedicalManager;

public class HospitalManagementSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
       
        
        AdminStaff admin1 = new AdminStaff("AD001", "ad_omar", "1234", "Omar");
        System.out.println(admin1.getName());
        
        
        admin1.createUser("P001", "Sarah");
         
        Patient Patient1 = new Patient("PAT001", "pat_Eve", "1234", "Evelyn", "2637982301");
        System.out.println(Patient1.getName());
        Patient1.editProfile();
        
        MedicalManager Manager1 = new MedicalManager("MG001", "MAG_bilal", "1234", " Sayyed Bilal");
        System.out.println(Manager1.getName());
        Manager1.editProfile();
        
        
        
}
        

    
    
}
