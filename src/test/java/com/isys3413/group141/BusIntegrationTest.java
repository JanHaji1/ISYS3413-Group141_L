package com.isys3413.group141;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for BusRepository
 *
 * These integration tests verify that Bus objects are correctly stored, retrieved,
 * updated and counted using the real txt persistence layer
 *
 * Unlike unit tests, these tests interact with the actual buses.txt file and
 * exercise multiple classes together
 */
public class BusIntegrationTest {

    private BusRepository repository;

    private static final Path BUS_FILE = Path.of("src/main/resources/buses.txt");

    /**
     * Resets the bus storage file before each test execution
     *
     * This ensures that tests remain independent and do not interfere with one
     * another through persisted data
     *
     * @throws IOException
     */
    @BeforeEach
    void setUp() throws IOException {
        repository = new BusRepository();

        // Clear the real txt file before each test so tests are independent
        Files.writeString(BUS_FILE, "");
    }

    /**
     * Verifies Task 4 Req 1:
     * Valid buses are stored correctly
     *
     * A valid bus is added to the repository, retrieved from the txt file and
     * compared against the original values
     */
    @Test
    void validBusIsStoredAndRetrievedCorrectly() {
        Bus bus = new Bus("12345678", 50, 80.0, "Diesel");

        assertTrue(repository.add(bus));

        Bus retrieved = repository.retrieve("12345678");

        assertNotNull(retrieved);
        assertEquals("12345678", retrieved.getBusID());
        assertEquals(50, retrieved.getCapacity());
        assertEquals(80.0, retrieved.getFuelLevel());
        assertEquals("Diesel", retrieved.getFuelType());
    }

    /**
     * Verifies Task 4 Req 2:
     * Invalid buses are rejected
     *
     * Attempts to add a bus with an invalid bus ID and confirms that the
     * repository rejects the record and does not store it
     */
    @Test
    void invalidBusIsRejected() {
        // Bus ID contains letters which violates Condition B1
        Bus invalidBus = new Bus("ABCD1234", 40, 90.0, "Diesel");

        assertFalse(repository.add(invalidBus));
        assertEquals(0, repository.count());
    }

    /**
     * Verifies Task 4 Req 3:
     * Updates are persisted correctly
     *
     * A bus is first stored in the repository, then updated with a decreased
     * capacity (allowed by Condition B2). The updated record is retrieved to
     * confirm that the changes were successfully written to the txt file
     */
    @Test
    void busUpdateIsPersistedCorrectly() {
        Bus originalBus = new Bus("12345678", 50, 80.0, "Diesel");

        // Capacity decreased from 50 to 40, which is allowed by Condition B2
        Bus updatedBus = new Bus("12345678", 40, 75.0, "Diesel");

        assertTrue(repository.add(originalBus));
        assertTrue(repository.update(updatedBus));

        Bus retrieved = repository.retrieve("12345678");

        assertNotNull(retrieved);
        assertEquals(40, retrieved.getCapacity());
        assertEquals(75.0, retrieved.getFuelLevel());
    }

    /**
     * Verifies Task 4 Req 4:
     * Record counts are updated correctly
     *
     * Multiple valid buses are added to the repository and the count is used to
     * confirm that the number of stored records increases appropriately
     */
    @Test
    void busCountIsUpdatedCorrectly() {
        Bus bus1 = new Bus("12345678", 50, 80.0, "Diesel");
        Bus bus2 = new Bus("87654321", 30, 60.0, "Electricity");

        assertEquals(0, repository.count());

        assertTrue(repository.add(bus1));
        assertEquals(1, repository.count());

        assertTrue(repository.add(bus2));
        assertEquals(2, repository.count());
    }
}
