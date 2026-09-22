/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.model;

/**
 *
 * @author HP
 */

public class Doctor extends User {

    private String specialty;

    public Doctor(String userId, String username, String password, String name, String specialty) {
        super(userId, username, password, name, "Doctor");
        this.specialty = specialty;
    }

    @Override
    public void editProfile() {
        System.out.println("Doctor " + getName() + " is editing their profile.");
    }
    
    public String getSpecialty(){
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}
    


