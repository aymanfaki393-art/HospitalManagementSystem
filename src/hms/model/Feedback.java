/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.model;

/**
 *
 * @author HP
 */
public class Feedback {
    
     private String feedbackId;
    private String patientId;
    private String doctorId;
    private String comment;
    private String rating;

    public Feedback(String feedbackId, String patientId, String doctorId, String comment, String rating) {
        this.feedbackId = feedbackId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.comment = comment;
        this.rating = rating;
    }
    
     public String getFeedbackId() {
         return feedbackId; }
     
    public String getPatientId() { 
        return patientId; }
    
    public String getDoctorId() {
        return doctorId; }
    
    public String getComment() { 
        return comment; }
    
    public String getRating() { 
        return rating; }
}
