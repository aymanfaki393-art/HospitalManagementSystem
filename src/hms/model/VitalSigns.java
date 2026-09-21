package hms.model;

public class VitalSigns {
    private final String bloodPressure;
    private final String heartRate;
    private final String temperature;

    public VitalSigns(String bloodPressure, String heartRate, String temperature) {
        this.bloodPressure = bloodPressure;
        this.heartRate = heartRate;
        this.temperature = temperature;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public String getTemperature() {
        return temperature;
    }
}