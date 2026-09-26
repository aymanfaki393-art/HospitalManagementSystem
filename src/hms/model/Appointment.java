/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.model;

/**
 *
 * @author HP
 */
public class Appointment {
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String date;
    private String time;
    
    public Appointment(String appointmentId, String patientId, String doctorId, String date, String time) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
    }
    
    public String getAppointmentId() { 
       return appointmentId; }
    
    public String getPatientId() { 
        return patientId; }
    
    public String getDoctorId() { 
        return doctorId; }
    
    public String getDate() { 
        
        return date; }
    
    public String getTime() { 
        return time; }
        
        
}
