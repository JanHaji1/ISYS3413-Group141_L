package com.isys3413.group141;

public class Bus {
    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType;

    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
    }

    public boolean isValidBusID() {
        return busID != null && busID.matches("\\d{8}");
    }

    public boolean canUpdateCapacity(Bus updatedBus) {
        return updatedBus.capacity <= this.capacity;
    }

    public boolean canBeDrivenBy(Driver driver, int age) {
        if (fuelType.equals("Electricity") && driver.getExperienceYears() < 5) return false;

        if ((fuelType.equals("Electricity") || fuelType.equals("Hybrid"))
                && !(driver.getLicenseType().equals("Heavy") ||
                     driver.getLicenseType().equals("PublicTransport"))) {
            return false;
        }

        if (age > 50 && capacity >= 50) return false;

        return true;
    }
}