/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hms.model;

/**
 *
 * @author HP
 */
public class Rate {
    private String consultationRate;
    private String insuranceNetworks;

    public Rate(String consultationRate, String insuranceNetworks) {
        this.consultationRate = consultationRate;
        this.insuranceNetworks = insuranceNetworks;
        
    }
    public String getConsultationRate() {
        return consultationRate;
    }

    public String getInsuranceNetworks() {
        return insuranceNetworks;
    }
    
}
