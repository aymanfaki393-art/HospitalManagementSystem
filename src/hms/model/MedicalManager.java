/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.model;

/**
 *
 * @author HP
 */
public class MedicalManager extends User{
    
    public MedicalManager (String userId, String username, String password, String name){
        super(userId, username, password, name, "MedicalManager");
        
    
    }
    @Override
    public void editProfile(){
        System.out.println("MedicalManager" + getName()+ " is editing their profile.");
    
    }
    
    
   
}
