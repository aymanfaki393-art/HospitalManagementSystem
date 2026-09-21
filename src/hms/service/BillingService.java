package hms.service;

public class BillingService {
    public static String getMedicalGrade(String bloodPressure, String heartRate, String temperature) {
        int systolic = parseSystolic(bloodPressure);
        int hr = parseInt(heartRate);
        double temp = parseDouble(temperature);

        if (systolic <= 120 && hr <= 80 && temp <= 37.3) {
            return "A";
        }
        if (systolic <= 140 && hr <= 100 && temp <= 38.0) {
            return "B";
        }
        if (systolic <= 160 && hr <= 110 && temp <= 39.0) {
            return "C";
        }
        return "D";
    }

    private static int parseSystolic(String bloodPressure) {
        if (bloodPressure == null || bloodPressure.trim().isEmpty()) {
            return 0;
        }
        String cleaned = bloodPressure.replace("/", " ").replace("-", " ");
        String[] parts = cleaned.split("\\s+");
        if (parts.length > 0) {
            try {
                return Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    private static int parseInt(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
