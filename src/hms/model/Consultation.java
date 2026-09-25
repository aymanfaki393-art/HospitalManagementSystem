/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.model;

/**
 *
 * @author HP
 */
public class Consultation {
    private String consultationId;
    private String patientId;
    private String doctorId;
    private String vitals;
    private String notes;
    private String date;
    
public Consultation(String consultationId, String patientId, String doctorId, String vitals, String notes, String date) {
        this.consultationId = consultationId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.vitals = vitals;
        this.notes = notes;
        this.date = date;
    }
public String getConsultationId() {
        return consultationId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getVitals() {
        return vitals;
    }

    public String getNotes() {
        return notes;
    }

    public String getDate() {
        return date;
    }



}


