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
    private String contactNumber;
    
    public Patient (String userId, String username, String password, String name, String contactNumber){
        super(userId, username, password, name, "Patient");
        this.contactNumber = contactNumber;
    }
    
    @Override
    public void editProfile(){
        System.out.println("Patient" + getName()+ " is editing their profile.");
    }
    
    public String getContactnumber(){
        return contactNumber;
    }

    public void setContactnumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
