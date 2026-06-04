package com.isys3413.group141;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for DriverRepository.
 * 
 * These tests verify that Driver objects are correctly stored, retrieve,
 * updated and counted using the real txt persistence layer.
 * 
 * Unlike unit tests, these tests interact with the actual drivers.txt file and
 * exercise multiple classes together.
 */
public class DriverIntegrationTest {

  private DriverRepository repository;

  private static final Path DRIVER_FILE = Path.of("src/main/resources/drivers.txt");

  /**
   * Resets the driver storage file before each test execution.
   * 
   * This ensures that tests remain independent and do not interfere with one
   * another through persisted data.
   * 
   * @throws IOException
   */
  @BeforeEach
  void setUp() throws IOException {
    repository = new DriverRepository();

    // Clear the real txt file before each test so tests are independent.
    Files.writeString(DRIVER_FILE, "");
  }

  /**
   * Verifies Task 3 Req 1:
   * Valid drivers are stored correctly.
   * 
   * A valid driver is added to the repository, retrieved from the txt file and
   * compared against the original values.
   */
  @Test
  void validDriverIsStoredAndRetrievedCorrectly() {
    Driver driver = new Driver(
        "23ab#$cdXY",
        "John Smith",
        5,
        "Heavy",
        "12|Collins Street|Melbourne|VIC|Australia",
        "01-01-1995");

    assertTrue(repository.add(driver));

    Driver retrievedDriver = repository.retrieve("23ab#$cdXY");

    assertNotNull(retrievedDriver);
    assertEquals("23ab#$cdXY", retrievedDriver.getDriverID());
    assertEquals("John Smith", retrievedDriver.getName());
    assertEquals(5, retrievedDriver.getExperienceYears());
    assertEquals("Heavy", retrievedDriver.getLicenseType());
  }

  /**
   * Verifies Task 3 Req 2:
   * Invalid drivers are rejected.
   * 
   * Attempts to add a driver with an invalid driver ID and confirms that the
   * repository rejects the record and does not store it.
   */
  @Test
  void invalidDriverIsRejected() {
    Driver invalidDriver = new Driver(
        "12345",
        "Invalid Driver",
        3,
        "Medium",
        "12|Collins Street|Melbourne|VIC|Australia",
        "01-01-1995");

    assertFalse(repository.add(invalidDriver));
    assertEquals(0, repository.count());
  }

  /**
   * Verifies Task 3 Req 3:
   * Updates are persisted correctly.
   * 
   * A driver is first stored in the repository, then updated with modified
   * details. The updated record is retrieved to confirm that the changes were
   * successfully written to the txt file.
   */
  @Test
  void driverUpdateIsPersistedCorrectly() {
    Driver originalDriver = new Driver(
        "23ab#$cdXY",
        "John Smith",
        5,
        "Medium",
        "12|Collins Street|Melbourne|VIC|Australia",
        "01-01-1995");

    Driver updatedDriver = new Driver(
        "23ab#$cdXY",
        "John Smith",
        5,
        "Heavy",
        "99|Bourke Street|Melbourne|VIC|Australia",
        "01-01-1995");

    assertTrue(repository.add(originalDriver));
    assertTrue(repository.update(updatedDriver));

    Driver retrievedDriver = repository.retrieve("23ab#$cdXY");

    assertNotNull(retrievedDriver);
    assertEquals("Heavy", retrievedDriver.getLicenseType());
    assertEquals("99|Bourke Street|Melbourne|VIC|Australia", retrievedDriver.getAddress());
  }

  /**
   * Verifies Task 3 Req 4:
   * Record counts are updated correctly.
   * 
   * Multiple valid drivers are added to the repository and the count is used to
   * confirm that the number of stored records increases approppriately.
   */
  @Test
  void driverCountIsUpdatedCorrectly() {
    Driver driver1 = new Driver(
        "23ab#$cdXY",
        "John Smith",
        5,
        "Medium",
        "12|Collins Street|Melbourne|VIC|Australia",
        "01-01-1995");

    Driver driver2 = new Driver(
        "45ef!@ghAB",
        "Jane Brown",
        8,
        "PublicTransport",
        "20|Swanston Street|Melbourne|VIC|Australia",
        "02-02-1990");

    assertEquals(0, repository.count());

    assertTrue(repository.add(driver1));
    assertEquals(1, repository.count());

    assertTrue(repository.add(driver2));
    assertEquals(2, repository.count());
  }
}