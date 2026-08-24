/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.model;

/**
 *
 * @author HP
 */
public class Patient extends User{
    private String ContactNumber;
    
    public Patient (String userId, String username, String password, String name, String Contact_Number){
    super(userId, username, password, name, "Patient");
    this.ContactNumber = ContactNumber;
    
    
    }
    
    @Override
    public void editProfile(){
        System.out.println("Patient" + getName()+ " is editing their profile.");
    
    }
    
    public String getContactnumber(){
        return ContactNumber;
    
    }
}
