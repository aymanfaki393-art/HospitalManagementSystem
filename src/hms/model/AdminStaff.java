/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.model;

/**
 *
 * @author HP
 */



public class AdminStaff extends User {

    public AdminStaff(String userId, String username, String password, String name) {
        super(userId, username, password, name, "AdminStaff");// required because every object needs its shared User data built first, and User demands 5 specific values to do that
    }

    @Override
    public void editProfile() {
        System.out.println("Admin " + getName() + " is editing their profile.");//required because User declared an unfinished method that every non-abstract subclass MUST finish
    }
    
    public void createUser(String userId, String name) {
        System.out.println("Admin " + getName() + " created new user: " + name + " (ID: " + userId + ")");

        
}
    public void deleteUser (String userId){
        System.out.println("Admin " + getName() + "Deleted user with Id:" + userId );
    
    }
    
    
    
}