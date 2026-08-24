/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.dao;

import java.io.FileWriter;
import java.io.IOException;
import hms.model.User;

/**
 *
 * @author HP
 */
public class UserDAO {
    
    
    public void saveUser(User user){
    String line = user.getUserId() + "," + user.getUserName() + "," + user.getPassword() + "," + user.getName() + "," + user.getRole();
    }
}
