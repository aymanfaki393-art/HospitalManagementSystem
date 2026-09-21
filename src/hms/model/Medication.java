package hms.model;

public class Medication {
    private final String name;
    private final String brand;
    private final String chemicalFormula;
    private final boolean available;

    public Medication(String name, String brand, String chemicalFormula, boolean available) {
        this.name = name;
        this.brand = brand;
        this.chemicalFormula = chemicalFormula;
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getChemicalFormula() {
        return chemicalFormula;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public String toString() {
        return name + " | Brand: " + brand + " | Formula: " + chemicalFormula;
    }
}