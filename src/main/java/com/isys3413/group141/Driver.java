package com.isys3413.group141;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a bus driver within the Intelligent Bus Driver Guidance System.
 * This class encapsulates driver attributes and enforces business conditions
 * (D1-D5) required for adding and updating driver records.
 */
public class Driver {
    private String driverID;
    private String name;
    private int experienceYears;
    private String licenseType;
    private String address;
    private String birthdate;

    /**
     * Constructs a new Driver instance with the specified core details.
     * 
     * @param driverID        The unique 10-character identifier for the driver
     * @param name            The full name of the driver
     * @param experienceYears The total number of years of driving experience
     * @param licenseType     The category of license held by the driver
     * @param address         The pipe-delimited residential address
     * @param birthdate       The date of birth in DD-MM-YYYY format
     */
    public Driver(String driverID, String name, int experienceYears,
            String licenseType, String address, String birthdate) {
        this.driverID = driverID;
        this.name = name;
        this.experienceYears = experienceYears;
        this.licenseType = licenseType;
        this.address = address;
        this.birthdate = birthdate;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public String getDriverID() {
        return driverID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthDate() {
        return birthdate;
    }

    /**
     * Validates the driver identifier according to Condition D1.
     * The rules require:
     * 1. Exactly 10 characters in length.
     * 2. First 2 characters must be digits between 2 and 9.
     * 3. Last 2 characters must be uppercase letters (A-Z).
     * 4. At least two special characters located between indices 2 and 7
     * (inclusive).
     * 
     * @return true if the driverID meets all syntax criteria, false otherwise
     */
    public boolean isValidDriverID() {

        // Enforce basic null check and exact length constraint.
        if (driverID == null || driverID.length() != 10) {
            return false;
        }

        // Validate that the first 2 characters are digits in the range 2-9.
        if (!driverID.substring(0, 2).matches("[2-9]{2}")) {
            return false;
        }

        // Validate that the final 2 characters are uppercase English letters.
        if (!driverID.substring(8, 10).matches("[A-Z]{2}")) {
            return false;
        }

        // Isolate the middle segment (characters 3 to 8, indices 2 to 7) to check for
        // special characters.
        String middle = driverID.substring(2, 8);
        int specialCount = 0;

        // Iterate through the middle segment to identify non-alphanumeric characters.
        for (char c : middle.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                specialCount++;
            }
        }

        // Require a minimum of 2 special characters in the middle segment
        return specialCount >= 2;
    }

    /**
     * Validates the address format according to Condition D2.
     * The address must contain exactly five non-blank segments separated by pipe
     * characters.
     * Expected format: Street Number | Street Name | City | State | Country
     *
     * @return true if the address matches the required structured layout, false
     *         otherwise
     */
    public boolean isValidAddress() {

        if (address == null) {
            return false;
        }

        // Split using the pipe character regex literal.
        // Note: consecutive pipes or trailing spaces may result in empty array
        // elements.
        String[] parts = address.split("\\|");

        // Ensure all five distinct address fields are present
        if (parts.length != 5) {
            return false;
        }

        // Confirm that no individual field consists purely of whitespace or empty
        // strings
        return !parts[0].isBlank() && !parts[1].isBlank() && !parts[2].isBlank() && !parts[3].isBlank()
                && !parts[4].isBlank();
    }

    /**
     * Validates the birthdate format according to Condition D3.
     * The birthdate must conform strictly to the standard calendar layout:
     * DD-MM-YYYY.
     *
     * @return true if the birthdate string is a real, parsable calendar date, false
     *         otherwise
     */
    public boolean isValidBirthdate() {

        try {
            // Define strict date pattern matching the assignment specification
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            // Attempt to parse the date string. Invalid days (e.g., 31-11-2000) will throw
            // a parse exception.
            LocalDate.parse(birthdate, formatter);

            return true;

        } catch (DateTimeParseException e) {
            // Catch invalid formats, characters, or non-existent calendar dates
            return false;
        }
    }

    /**
     * Verifies if an existing driver record can be modified with new data.
     * This method enforces the following rules during system update operations:
     * 1. Condition D5: The driverID and name fields are immutable and cannot be
     * altered.
     * 2. Condition D4: If a driver has more than 10 years of experience, their
     * licenseType
     * cannot be changed.
     *
     * @param updatedDriver The new driver data instance containing proposed
     *                      modifications
     * @return true if the update complies with all state modification rules, false
     *         otherwise
     */
    public boolean canUpdate(Driver updatedDriver) {
        // Enforce Condition D5: Reject update if the unique driver identifier does not
        // match
        if (!this.driverID.equals(updatedDriver.driverID)) {
            return false;
        }

        // Enforce Condition D5: Reject update if the driver name has been altered
        if (!this.name.equals(updatedDriver.name)) {
            return false;
        }

        // Enforce Condition D4: Lock the licence type for highly experienced drivers
        // (greater than 10 years)
        if (this.experienceYears > 10 && !this.licenseType.equals(updatedDriver.licenseType)) {
            return false;
        }

        return true;
    }
}