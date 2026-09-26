/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.model;

/**
 *
 * @author HP
 */
public class Department {
            
    
    private String departmentId;
    private String departmentName;
    private String headDoctorId;

    public Department(String departmentId, String departmentName, String headDoctorId) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.headDoctorId = headDoctorId;
    
}
    public String getDepartmentId() { 
        return departmentId; }
    
    public String getDepartmentName() {
        return departmentName; }
    
    public String getHeadDoctorId() { 
        
        return headDoctorId; }
    
    
    
}
