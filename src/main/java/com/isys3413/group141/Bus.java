package com.isys3413.group141;

/**
 * Represents a bus within the Intelligent Bus Driver Guidance System.
 * This class encapsulates bus attributes and enforces business conditions
 * (B1-B5) related to bus validation, capacity updates, and driver eligibility.
 */
public class Bus {
    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType;

    /**
     * Constructs a new Bus instance with the specified details.
     *
     * @param busID     The unique 8-digit identifier for the bus
     * @param capacity  The passenger capacity of the bus
     * @param fuelLevel The current fuel or battery level
     * @param fuelType  The fuel type used by the bus
     *                  (Diesel, Hybrid, or Electricity)
     */
    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
    }

    public String getBusID() {
        return busID;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public String getFuelType() {
        return fuelType;
    }

    /**
     * Validates the bus identifier according to Condition B1.
     *
     * Rules:
     * 1. The bus ID must contain exactly 8 characters.
     * 2. All characters must be numeric digits.
     *
     * Example valid IDs:
     * 12345678
     * 87654321
     *
     * @return true if the bus ID satisfies all requirements,
     *         false otherwise
     */
    public boolean isValidBusID() {
        return busID != null && busID.matches("\\d{8}");
    }

    /**
     * Determines whether a bus capacity update is permitted according
     * to Condition B2.
     *
     * The system allows bus capacity to remain unchanged or decrease.
     * Capacity increases are prohibited.
     *
     * @param updatedBus The proposed updated bus record
     * @return true if the updated capacity is less than or equal to the
     *         current capacity, false otherwise
     */
    public boolean canUpdateCapacity(Bus updatedBus) {
        return updatedBus.capacity <= this.capacity;
    }

    /**
     * Determines whether a driver is permitted to operate this bus
     * according to Conditions B3, B4, and B5.
     *
     * Business Rules:
     *
     * B3:
     * Drivers older than 50 years cannot drive buses with a capacity
     * of 50 or more passengers.
     *
     * B4:
     * Electric buses may only be operated by drivers with at least
     * 5 years of driving experience.
     *
     * B5:
     * Electric and Hybrid buses may only be operated by drivers
     * holding either a Heavy or Public Transport licence.
     *
     * @param driver The driver being assessed
     * @param age    The age of the driver
     * @return true if the driver satisfies all applicable restrictions,
     *         false otherwise
     */
    public boolean canBeDrivenBy(Driver driver, int age) {
        if (fuelType.equals("Electricity") && driver.getExperienceYears() < 5)
            return false;

        if ((fuelType.equals("Electricity") || fuelType.equals("Hybrid"))
                && !(driver.getLicenseType().equals("Heavy") ||
                        driver.getLicenseType().equals("PublicTransport"))) {
            return false;
        }

        if (age > 50 && capacity >= 50)
            return false;

        return true;
    }
}