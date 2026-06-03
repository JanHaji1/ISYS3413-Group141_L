package com.isys3413.group141;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores retrieves, updates and counts Driver records using a human-readable
 * TXT file.
 */
public class DriverRepository {

  private static final Path FILE_PATH = Path.of("src/main/resources/drivers.txt");

  /**
   * Adds a new driver if the driver is valid and the driverID is unique.
   * 
   * @param driver driver to add
   * @return true if added successfully, false otherwise
   */
  public boolean add(Driver driver) {
    if (driver == null || !driver.isValidDriverID() || !driver.isValidAddress() || !driver.isValidBirthdate()) {
      return false;
    }

    if (retrieve(driver.getDriverID()) != null) {
      return false;
    }

    try {
      Files.writeString(FILE_PATH, toRecord(driver) + System.lineSeparator(), StandardOpenOption.CREATE,
          StandardOpenOption.APPEND);

      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Retrieves a driver by driverID.
   * 
   * @param driverID
   * @return
   */
  public Driver retrieve(String driverID) {
    try {
      for (String line : Files.readAllLines(FILE_PATH)) {
        if (line.isBlank()) {
          continue;
        }

        Driver driver = fromRecord(line);

        if (driver != null && driver.getDriverID().equals(driverID)) {
          return driver;
        }
      }
    } catch (IOException e) {
      return null;
    }

    return null;
  }

  /**
   * Updates an existing driver if update rules D4 and D5 are satisfied.
   * 
   * @param updatedDriver
   * @return
   */
  public boolean update(Driver updatedDriver) {
    if (updatedDriver == null) {
      return false;
    }

    Driver existingDriver = retrieve(updatedDriver.getDriverID());

    if (existingDriver == null) {
      return false;
    }

    if (!existingDriver.canUpdate(updatedDriver)) {
      return false;
    }

    if (!updatedDriver.isValidAddress() || !updatedDriver.isValidBirthdate()) {
      return false;
    }

    try {
      List<String> lines = Files.readAllLines(FILE_PATH);
      List<String> updatedLines = new ArrayList<>();

      for (String line : lines) {
        if (line.isBlank()) {
          continue;
        }

        Driver currentDriver = fromRecord(line);

        if (currentDriver != null && currentDriver.getDriverID().equals(updatedDriver.getDriverID())) {
          updatedLines.add(toRecord(updatedDriver));
        } else {
          updatedLines.add(line);
        }
      }

      Files.write(FILE_PATH, updatedLines);
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Counts all stored driver records.
   * 
   * @return
   */
  public int count() {
    try {
      int count = 0;

      for (String line : Files.readAllLines(FILE_PATH)) {
        if (!line.isBlank()) {
          count++;
        }
      }

      return count;

    } catch (IOException e) {
      return 0;
    }
  }

  /**
   * Converts a Driver object into 1 line for the txt file.
   * 
   * @param driver
   * @return
   */
  private String toRecord(Driver driver) {
    return driver.getDriverID() + ","
        + driver.getName() + ","
        + driver.getExperienceYears() + ","
        + driver.getLicenseType() + ","
        + driver.getAddress() + ","
        + driver.getBirthDate();
  }

  /**
   * Converts 1 txt file line back into Driver object.
   */
  private Driver fromRecord(String record) {
    String[] parts = record.split(",", -1);

    if (parts.length != 6) {
      return null;
    }

    return new Driver(parts[0], parts[1], Integer.parseInt(parts[2]), parts[3], parts[4], parts[5]);
  }
}
