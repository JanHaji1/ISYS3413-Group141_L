package com.isys3413.group141;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DriverTest {

 
   // D1 - Normal case.
    @Test
    void D1_ValidDriverID() {

        // "23ab#$cdXY" -> first two digits are 2 and 3 (both in 2-9),
        // middle "ab#$cd" contains '#' and '$' (2 specials), last two "XY" uppercase
        
        Driver driver = new Driver("23ab#$cdXY", "Alice Brown", 5,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertTrue(driver.isValidDriverID());
    }

    // D1 - Invalid input
    @Test
    void D1_DriverIDTooShort() {
        // "23#$abXY" is 8 characters - fails the length check
        Driver driver = new Driver("23#$abXY", "Bob Lee", 3,
                "Medium", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertFalse(driver.isValidDriverID());
    }

    // D1 - Invalid input
    @Test
    void D1_DriverIDFirstDigitOutOfRange() {
        // "13ab#$cdXY" -> first char '1' is outside the 2-9 range
        Driver driver = new Driver("13ab#$cdXY", "Carol White", 4,
                "Light", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertFalse(driver.isValidDriverID());
    }


    // D1 - Invalid input
    @Test
    void D1_DriverIDNoSpecialCharsInMiddle() {
        // "23abcdcdXY" -> middle "abcdcd" contains zero special characters
        Driver driver = new Driver("23abcdcdXY", "Dan Green", 6,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertFalse(driver.isValidDriverID());
    }

    // D1 - Edge case.
    @Test
    void D1_DriverIDExactlyTwoSpecialCharsInMiddle() {
        // "23@#abcdXY" -> middle "@#abcd" has exactly '@' and '#' (2 specials)
        Driver driver = new Driver("23@#abcdXY", "Eva Black", 7,
                "PublicTransport", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertTrue(driver.isValidDriverID());
    }

    // D2 - Normal case.
    @Test
    void D2_ValidAddress() {
        Driver driver = new Driver("23ab#$cdXY", "Frank Hill", 5,
                "Heavy", "99|Bourke Street|Melbourne|VIC|Australia", "01-01-1990");

        assertTrue(driver.isValidAddress());
    }

    // D2 - Invalid Input 
    @Test
    void D2_AddressWithSpaceDelimitersRejected() {
        // No '|' separator - split produces only 1 part
        Driver driver = new Driver("23ab#$cdXY", "Grace Park", 5,
                "Heavy", "99 Bourke Street Melbourne VIC Australia", "01-01-1990");

        assertFalse(driver.isValidAddress());
    }

    
    // D2 - Edge case.
 
    @Test
    void D2_AddressMissingFifthSegmentRejected() {
        // Only 4 segments: Street Number|Street Name|City|State (no Country)
        Driver driver = new Driver("23ab#$cdXY", "Henry Scott", 5,
                "Heavy", "99|Bourke Street|Melbourne|VIC", "01-01-1990");

        assertFalse(driver.isValidAddress());
    }

    // D3 - Birthdate Format

    //  * D3 - Normal case.

    @Test
    void D3_ValidBirthdate() {
        Driver driver = new Driver("23ab#$cdXY", "Isla Reed", 5,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "15-06-1990");

        assertTrue(driver.isValidBirthdate());
    }

    // D3 - Invalid input.
    @Test
    void D3_BirthdateInWrongFormatRejected() {
        // ISO format YYYY-MM-DD is not accepted
        Driver driver = new Driver("23ab#$cdXY", "Jake Stone", 5,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "1990-06-15");

        assertFalse(driver.isValidBirthdate());
    }

    //D3 - Edge case.
    @Test
    void D3_LeapYearBirthdateAccepted() {
        Driver driver = new Driver("23ab#$cdXY", "Karen Lane", 5,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "29-02-2000");

        assertTrue(driver.isValidBirthdate());
    }

    // D4 - License Update Restriction
    
    // D4 - Normal case
    @Test
    void D4_DriverUnder10YearsCanChangeLicense() {
        Driver original = new Driver("23ab#$cdXY", "Leo Fox", 5,
                "Light", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        // Same ID and name, different licenseType - allowed because exp <= 10
        Driver updated = new Driver("23ab#$cdXY", "Leo Fox", 5,
                "Medium", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertTrue(original.canUpdate(updated));
    }

    //D4 - Invalid input.

    @Test
    void D4_DriverOver10YearsCannotChangeLicense() {
        Driver original = new Driver("23ab#$cdXY", "Mia Cross", 11,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        // Same ID and name, but licenseType changed - blocked because exp > 10
        Driver updated = new Driver("23ab#$cdXY", "Mia Cross", 11,
                "Light", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertFalse(original.canUpdate(updated));
    }

    //D4 - Edge case.

    @Test
    void D4_DriverWithExactly10YearsCanChangeLicense() {
        Driver original = new Driver("23ab#$cdXY", "Noah King", 10,
                "Medium", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        // Exactly 10 years - the restriction (>10) does not apply
        Driver updated = new Driver("23ab#$cdXY", "Noah King", 10,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertTrue(original.canUpdate(updated));
    }


    // D5 - Immutable Fields
 
    // D5 - Normal Case 
    @Test
    void D5_UpdateMutableFieldsIsAllowed() {
        Driver original = new Driver("23ab#$cdXY", "Olivia Hart", 5,
                "Light", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        // Only licenseType and address change - driverID and name unchanged
        Driver updated = new Driver("23ab#$cdXY", "Olivia Hart", 5,
                "Heavy", "99|Collins St|Melbourne|VIC|Australia", "01-01-1990");

        assertTrue(original.canUpdate(updated));
    }

    // D5 - Invalid input.
    @Test
    void D5_CannotChangeDriverID() {
        Driver original = new Driver("23ab#$cdXY", "Paul Nash", 5,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        // Different driverID in the updated object - must be blocked
        Driver updated = new Driver("45ef!@ghAB", "Paul Nash", 5,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertFalse(original.canUpdate(updated));
    }

    //D5 - Invalid input.
    @Test
    void D5_CannotChangeName() {
        Driver original = new Driver("23ab#$cdXY", "Quinn Blake", 5,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        // Name changed in updated object - must be blocked
        Driver updated = new Driver("23ab#$cdXY", "Quinn Smith", 5,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertFalse(original.canUpdate(updated));
    }
}
