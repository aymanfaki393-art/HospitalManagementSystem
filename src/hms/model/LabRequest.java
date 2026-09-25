/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.model;

/**
 *
 * @author HP
 */
public class LabRequest {
    
    private String requestId;
    private String patientId;
    private String doctorId;
    private String testType;
    private String date;

    public LabRequest(String requestId, String patientId, String doctorId, String testType, String date) {
        this.requestId = requestId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.testType = testType;
        this.date = date;
    }

    public String getRequestId(){ 
        return requestId; 
    }
    
    public String getPatientId(){ 
        return patientId; 
    }
    
    public String getDoctorId(){
        return doctorId; 
    }
    
    public String getTestType(){ 
        return testType; 
    }
    
    public String getDate(){
        return date; 
    }
    
    
    
    
}
