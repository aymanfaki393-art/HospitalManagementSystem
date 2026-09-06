/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.model;

/**
 *
 * @author HP
 */
public abstract class User {
    private String userId;
    private String username;
    private String password;
    private String name;
    private String role;
    
    public User(String userId, String username, String password, String name, String role){
        
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }
    public String getUserId(){
        return userId;
    }
    public String getUserName(){
        return username;
    }
    public String getPassword(){
        return password;
    
    }
    
    public String getName(){
        return name;
    }
    
    public String getRole(){
        return role;
    }
    
    public void setName (String name){
        this.name = name;
    
    }
    public void setPassword (String newPassword){
        if (newPassword.length() >=4){
            this.password = newPassword;
        
        }
    
    }
    
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    
    }
    
    public abstract void editProfile();
    
    
}
