/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.model;

/**
 *
 * @author HP
 */
public class Prescription {
    
     private String prescriptionId;
    private String patientId;
    private String doctorId;
    private String medication;
    private String dosage;
    private String date;

    public Prescription(String prescriptionId, String patientId, String doctorId, String medication, String dosage, String date) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.medication = medication;
        this.dosage = dosage;
        this.date = date;
    }

    public String getPrescriptionId(){ 
        return prescriptionId; 
    }
    public String getPatientId(){ 
        return patientId; 
    
    }
    public String getDoctorId(){ 
        return doctorId; 
    }
    public String getMedication(){ 
        return medication; 
    
    }
    public String getDosage(){ 
        return dosage; 
    
    }
    public String getDate(){ 
        return date; 
    }
}
