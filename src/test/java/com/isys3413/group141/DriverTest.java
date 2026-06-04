package com.isys3413.group141;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

// Unit tests for the Driver class covering conditions D1 through D5.
// Each condition has at least three test cases: normal, invalid, and edge.

public class DriverTest {

    // D1 - Driver ID Rules
    // driverID must be exactly 10 chars, first 2 digits 2-9,
    // at least 2 special chars in positions 2-7, last 2 uppercase A-Z.

    // D1 - Normal case: all four ID rules are satisfied, driver should be accepted.
    @Test
    void D1_ValidDriverID() {
        // "23ab#$cdXY" -> first two digits are 2 and 3 (both in 2-9),
        // middle "ab#$cd" contains '#' and '$' (2 specials), last two "XY" uppercase
        Driver driver = new Driver("23ab#$cdXY", "Alice Brown", 5,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertTrue(driver.isValidDriverID());
    }

    // D1 - Invalid input: ID is only 8 characters, length check must reject it.
    @Test
    void D1_DriverIDTooShort() {
        // "23#$abXY" is 8 characters - fails the length check
        Driver driver = new Driver("23#$abXY", "Bob Lee", 3,
                "Medium", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertFalse(driver.isValidDriverID());
    }

    // D1 - Invalid input: first digit is '1', which is outside the allowed 2-9 range.
    @Test
    void D1_DriverIDFirstDigitOutOfRange() {
        // "13ab#$cdXY" -> first char '1' is outside the 2-9 range
        Driver driver = new Driver("13ab#$cdXY", "Carol White", 4,
                "Light", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertFalse(driver.isValidDriverID());
    }

    // D1 - Invalid input: middle section has no special characters, minimum of 2 not met.
    @Test
    void D1_DriverIDNoSpecialCharsInMiddle() {
        // "23abcdcdXY" -> middle "abcdcd" contains zero special characters
        Driver driver = new Driver("23abcdcdXY", "Dan Green", 6,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertFalse(driver.isValidDriverID());
    }

    // D1 - Edge case: exactly 2 special characters in the middle, the minimum boundary, should pass.
    @Test
    void D1_DriverIDExactlyTwoSpecialCharsInMiddle() {
        // "23@#abcdXY" -> middle "@#abcd" has exactly '@' and '#' (2 specials)
        Driver driver = new Driver("23@#abcdXY", "Eva Black", 7,
                "PublicTransport", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertTrue(driver.isValidDriverID());
    }

    // D2 - Address Format
    // Address must follow: Street Number|Street Name|City|State|Country (5 non-blank segments).

    // D2 - Normal case: address has all 5 pipe-separated segments, should be accepted.
    @Test
    void D2_ValidAddress() {
        Driver driver = new Driver("23ab#$cdXY", "Frank Hill", 5,
                "Heavy", "99|Bourke Street|Melbourne|VIC|Australia", "01-01-1990");

        assertTrue(driver.isValidAddress());
    }

    // D2 - Invalid input: address uses spaces instead of pipes, split yields 1 part not 5.
    @Test
    void D2_AddressWithSpaceDelimitersRejected() {
        // No '|' separator - split produces only 1 part
        Driver driver = new Driver("23ab#$cdXY", "Grace Park", 5,
                "Heavy", "99 Bourke Street Melbourne VIC Australia", "01-01-1990");

        assertFalse(driver.isValidAddress());
    }

    // D2 - Edge case: address has only 4 segments, one short of the required 5.
    @Test
    void D2_AddressMissingFifthSegmentRejected() {
        // Only 4 segments: Street Number|Street Name|City|State (no Country)
        Driver driver = new Driver("23ab#$cdXY", "Henry Scott", 5,
                "Heavy", "99|Bourke Street|Melbourne|VIC", "01-01-1990");

        assertFalse(driver.isValidAddress());
    }

    // D3 - Birthdate Format
    // Birthdate must be a real calendar date in DD-MM-YYYY format.

    // D3 - Normal case: birthdate in correct DD-MM-YYYY format, should be accepted.
    @Test
    void D3_ValidBirthdate() {
        Driver driver = new Driver("23ab#$cdXY", "Isla Reed", 5,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "15-06-1990");

        assertTrue(driver.isValidBirthdate());
    }

    // D3 - Invalid input: date is in YYYY-MM-DD format, which the parser must reject.
    @Test
    void D3_BirthdateInWrongFormatRejected() {
        // ISO format YYYY-MM-DD is not accepted
        Driver driver = new Driver("23ab#$cdXY", "Jake Stone", 5,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "1990-06-15");

        assertFalse(driver.isValidBirthdate());
    }

    // D3 - Edge case: 29-02-2000 is a valid leap year date and should be accepted.
    @Test
    void D3_LeapYearBirthdateAccepted() {
        Driver driver = new Driver("23ab#$cdXY", "Karen Lane", 5,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "29-02-2000");

        assertTrue(driver.isValidBirthdate());
    }

    // D4 - License Update Restriction
    // Drivers with more than 10 years of experience cannot change their license type.

    // D4 - Normal case: driver has 5 years experience, license change should be allowed.
    @Test
    void D4_DriverUnder10YearsCanChangeLicense() {
        Driver original = new Driver("23ab#$cdXY", "Leo Fox", 5,
                "Light", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        // Same ID and name, different licenseType - allowed because exp <= 10
        Driver updated = new Driver("23ab#$cdXY", "Leo Fox", 5,
                "Medium", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertTrue(original.canUpdate(updated));
    }

    // D4 - Invalid input: driver has 11 years experience, license change must be blocked.
    @Test
    void D4_DriverOver10YearsCannotChangeLicense() {
        Driver original = new Driver("23ab#$cdXY", "Mia Cross", 11,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        // Same ID and name, but licenseType changed - blocked because exp > 10
        Driver updated = new Driver("23ab#$cdXY", "Mia Cross", 11,
                "Light", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertFalse(original.canUpdate(updated));
    }

    // D4 - Edge case: exactly 10 years sits on the boundary, restriction is >10 so change must be allowed.
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
    // driverID and name cannot be modified during an update operation.

    // D5 - Normal case: only mutable fields change, driverID and name stay the same, update should pass.
    @Test
    void D5_UpdateMutableFieldsIsAllowed() {
        Driver original = new Driver("23ab#$cdXY", "Olivia Hart", 5,
                "Light", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        // Only licenseType and address change - driverID and name unchanged
        Driver updated = new Driver("23ab#$cdXY", "Olivia Hart", 5,
                "Heavy", "99|Collins St|Melbourne|VIC|Australia", "01-01-1990");

        assertTrue(original.canUpdate(updated));
    }

    // D5 - Invalid input: driverID is different in the update object, must be rejected.
    @Test
    void D5_CannotChangeDriverID() {
        Driver original = new Driver("23ab#$cdXY", "Paul Nash", 5,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        // Different driverID in the updated object - must be blocked
        Driver updated = new Driver("45ef!@ghAB", "Paul Nash", 5,
                "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1990");

        assertFalse(original.canUpdate(updated));
    }

    // D5 - Invalid input: name is different in the update object, must be rejected.
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
