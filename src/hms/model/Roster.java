/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.model;

/**
 *
 * @author HP
 */
public class Roster {
    private String rosterId;
    private String doctorId;
    private String shiftDay;
    private String shiftTime;

    public Roster(String rosterId, String doctorId, String shiftDay, String shiftTime) {
        this.rosterId = rosterId;
        this.doctorId = doctorId;
        this.shiftDay = shiftDay;
        this.shiftTime = shiftTime;
    }
     public String getRosterId() {
         return rosterId; }
     
    public String getDoctorId() {
        return doctorId; }
    
    public String getShiftDay() { 
        return shiftDay; }
    
    public String getShiftTime() { 
        return shiftTime; }
    
}
