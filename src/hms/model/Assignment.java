/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.model;

/**
 *
 * @author HP
 */
public class Assignment {
    private String doctorId;
    private String managerId;
    
    public Assignment(String doctorId, String managerId){
        this.doctorId = doctorId;
        this.managerId = managerId;
    }
    
    public String getDoctorId() {
        return doctorId;
    }
    public String getManagerId(){
        return managerId;
    }
   
    
}
