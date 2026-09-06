/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.service;

import hms.dao.UserDAO;
import hms.model.User;
import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class LoginService {
    public User login (String username, String password){
        UserDAO dao = new UserDAO();
        ArrayList<User> allUsers = dao.loadAllUsers();
        
        for (User u : allUsers) {
            if (u.getUserName().equals(username) && u.checkPassword(password)) {
                return u;
            }
        }
        return null;
    
    }
    
    
}
