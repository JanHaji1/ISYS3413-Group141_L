package com.isys3413.group141;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores, retrieves, updates and counts Bus records using a txt file.
 */
public class BusRepository {

  private static final Path FILE_PATH = Path.of("src/main/resources/buses.txt");

  /**
   * Adds a new bus if the bus is valid and the bus is valid and the busID is
   * unique.
   * 
   * @param bus
   * @return
   */
  public boolean add(Bus bus) {

    if (bus == null || !bus.isValidBusID()) {
      return false;
    }

    if (retrieve(bus.getBusID()) != null) {
      return false;
    }

    try {
      Files.writeString(FILE_PATH, toRecord(bus) + System.lineSeparator(), StandardOpenOption.CREATE,
          StandardOpenOption.APPEND);

      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Retrieves a bus using its busID.
   * 
   * @param busID
   * @return
   */
  public Bus retrieve(String busID) {

    try {
      for (String line : Files.readAllLines(FILE_PATH)) {

        if (line.isBlank()) {
          continue;
        }

        Bus bus = fromRecord(line);

        if (bus != null && bus.getBusID().equals(busID)) {
          return bus;
        }
      }
    } catch (IOException e) {
      return null;
    }

    return null;
  }

  /**
   * Updates an existing bus record.
   * Condition B2 is enforced: capacity cannot increase.
   * 
   * @param updatedBus
   * @return
   */
  public boolean update(Bus updatedBus) {

    if (updatedBus == null) {
      return false;
    }

    Bus existingBus = retrieve(updatedBus.getBusID());

    if (existingBus == null) {
      return false;
    }

    if (!existingBus.canUpdateCapacity(updatedBus)) {
      return false;
    }

    try {

      List<String> lines = Files.readAllLines(FILE_PATH);
      List<String> updatedLines = new ArrayList<>();

      for (String line : lines) {

        if (line.isBlank()) {
          continue;
        }

        Bus currentBus = fromRecord(line);

        if (currentBus != null && currentBus.getBusID().equals(updatedBus.getBusID())) {
          updatedLines.add(toRecord(updatedBus));
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
   * Counts stored bus records.
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
   * Converts a Bus object into a txt record.
   * 
   * @param bus
   * @return
   */
  private String toRecord(Bus bus) {

    return bus.getBusID() + "," + bus.getCapacity() + "," + bus.getFuelLevel() + "," + bus.getFuelType();
  }

  /**
   * Converts a txt record into a Bus object.
   * 
   * @param record
   * @return
   */
  private Bus fromRecord(String record) {

    String[] parts = record.split(",", -1);

    if (parts.length != 4) {
      return null;
    }

    return new Bus(
        parts[0],
        Integer.parseInt(parts[1]),
        Double.parseDouble(parts[2]),
        parts[3]);
  }
}
