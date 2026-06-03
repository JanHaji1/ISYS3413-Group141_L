package com.isys3413.group141;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BusTest {

    // B1 - Bus ID Rules

    @Test
    void B1_ValidBusId() {
        Bus bus = new Bus("12345678", 40, 80.0, "Diesel");
        assertTrue(bus.isValidBusID());
    }

    @Test
    void B1_BusIdTooShort() {
        Bus bus = new Bus("1234567", 40, 80.0, "Diesel");
        assertFalse(bus.isValidBusID());
    }

    @Test
    void B1_BusIdContainsLetters() {
        Bus bus = new Bus("1234ABCD", 40, 80.0, "Diesel");
        assertFalse(bus.isValidBusID());
    }

    // B2 - Capacity Update Restriction

    @Test
    void B2_CapacityDecreaseAllowed() {
        Bus oldBus = new Bus("12345678", 50, 80.0, "Diesel");
        Bus updatedBus = new Bus("12345678", 45, 80.0, "Diesel");

        assertTrue(oldBus.canUpdateCapacity(updatedBus));
    }

    @Test
    void B2_CapacityIncreaseRejected() {
        Bus oldBus = new Bus("12345678", 45, 80.0, "Diesel");
        Bus updatedBus = new Bus("12345678", 50, 80.0, "Diesel");

        assertFalse(oldBus.canUpdateCapacity(updatedBus));
    }

    @Test
    void B2_CapacityUnchangedAllowed() {
        Bus oldBus = new Bus("12345678", 50, 80.0, "Diesel");
        Bus updatedBus = new Bus("12345678", 50, 80.0, "Diesel");

        assertTrue(oldBus.canUpdateCapacity(updatedBus));
    }

    // B3 - Driver Age Restriction

    @Test
    void B3_DriverUnder50CanDriveLargeBus() {
        Driver driver = new Driver("1234567890", "John", 10,
                "Heavy", "address", "01-01-1990");

        Bus bus = new Bus("12345678", 60, 90.0, "Diesel");

        assertTrue(bus.canBeDrivenBy(driver, 45));
    }

    @Test
    void B3_DriverOver50CannotDriveLargeBus() {
        Driver driver = new Driver("1234567890", "John", 10,
                "Heavy", "address", "01-01-1970");

        Bus bus = new Bus("12345678", 60, 90.0, "Diesel");

        assertFalse(bus.canBeDrivenBy(driver, 55));
    }

    @Test
    void B3_DriverExactly50CanDriveLargeBus() {
        Driver driver = new Driver("1234567890", "John", 10,
                "Heavy", "address", "01-01-1975");

        Bus bus = new Bus("12345678", 50, 90.0, "Diesel");

        assertTrue(bus.canBeDrivenBy(driver, 50));
    }

    // B4 - Electric Bus Restriction

    @Test
    void B4_ExperiencedDriverCanDriveElectricBus() {
        Driver driver = new Driver("1234567890", "John", 7,
                "Heavy", "address", "01-01-1990");

        Bus bus = new Bus("12345678", 40, 90.0, "Electricity");

        assertTrue(bus.canBeDrivenBy(driver, 40));
    }

    @Test
    void B4_InexperiencedDriverCannotDriveElectricBus() {
        Driver driver = new Driver("1234567890", "John", 4,
                "Heavy", "address", "01-01-1990");

        Bus bus = new Bus("12345678", 40, 90.0, "Electricity");

        assertFalse(bus.canBeDrivenBy(driver, 40));
    }

    @Test
    void B4_DriverWithExactlyFiveYearsCanDriveElectricBus() {
        Driver driver = new Driver("1234567890", "John", 5,
                "Heavy", "address", "01-01-1990");

        Bus bus = new Bus("12345678", 40, 90.0, "Electricity");

        assertTrue(bus.canBeDrivenBy(driver, 40));
    }

    // B5 - Driver Licence Restriction 

    @Test
    void B5_HeavyLicenceCanDriveElectricBus() {
        Driver driver = new Driver("1234567890", "John", 8,
                "Heavy", "address", "01-01-1990");

        Bus bus = new Bus("12345678", 40, 90.0, "Electricity");

        assertTrue(bus.canBeDrivenBy(driver, 40));
    }

    @Test
    void B5_PublicTransportLicenceCanDriveHybridBus() {
        Driver driver = new Driver("1234567890", "John", 8,
                "PublicTransport", "address", "01-01-1990");

        Bus bus = new Bus("12345678", 40, 90.0, "Hybrid");

        assertTrue(bus.canBeDrivenBy(driver, 40));
    }

    @Test
    void B5_MediumLicenceCannotDriveHybridBus() {
        Driver driver = new Driver("1234567890", "John", 8,
                "Medium", "address", "01-01-1990");

        Bus bus = new Bus("12345678", 40, 90.0, "Hybrid");

        assertFalse(bus.canBeDrivenBy(driver, 40));
    }
}