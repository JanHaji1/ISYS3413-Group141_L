# Intelligent Bus Driver Guidance System

## Overview

This project implements part of the Intelligent Bus Driver Guidance System.

The system manages drivers and buses while enforcing business rules specified in the assignment requirements. Data is stored using human-readable TXT files and is tested using JUnit 5.

---

## Technologies Used

- Java 22
- Maven
- JUnit 5
- GitHub Actions

---

## Project Structure

```text
src
├── main
│   └── java
│       └── com.isys3413.group141
│           ├── Driver.java
│           ├── Bus.java
│           ├── DriverRepository.java
│           └── BusRepository.java
│
├── resources
│   ├── drivers.txt
│   └── buses.txt
│
└── test
    └── java
        └── com.isys3413.group141
            ├── DriverTest.java
            ├── BusTest.java
            ├── DriverIntegrationTest.java
            └── BusIntegrationTest.java
```

---

## Driver Conditions

### D1 - Driver ID Rules

- Must be unique
- Exactly 10 characters
- First 2 characters must be digits between 2 and 9
- At least 2 special characters between positions 3 and 8
- Last 2 characters must be uppercase letters

### D2 - Address Format

Format:

```text
Street Number|Street Name|City|State|Country
```

### D3 - Birthdate Format

Format:

```text
DD-MM-YYYY
```

### D4 - Licence Update Restriction

Drivers with more than 10 years of experience cannot change licence type.

### D5 - Immutable Fields

Driver ID and Name cannot be modified.

---

## Bus Conditions

### B1 - Bus ID Rules

- Must be unique
- Exactly 8 digits

### B2 - Capacity Update Restriction

Bus capacity can decrease but cannot increase.

### B3 - Driver Age Restriction

Drivers older than 50 cannot drive buses with capacity 50 or greater.

### B4 - Electric Bus Restriction

Drivers must have at least 5 years of experience to operate electric buses.

### B5 - Driver Licence Restriction

Only drivers with:

- Heavy licence
- PublicTransport licence

may operate Hybrid or Electric buses.

---

## Data Storage

Driver and bus records are stored in:

```text
src/main/resources/drivers.txt
src/main/resources/buses.txt
```

Example bus record:

```text
12345678,50,80.0,Diesel
```

Example driver record:

```text
23ab#$cdXY,John Smith,5,Heavy,12|Collins Street|Melbourne|VIC|Australia,01-01-1995
```

---

## Running Tests

Run all tests using Maven:

```bash
mvn test
```

Clean and rebuild:

```bash
mvn clean test
```

---

## Continuous Integration

GitHub Actions automatically runs all JUnit tests on:

- Push
- Pull Request

Workflow file:

```text
.github/workflows/ci.yml
```

---

## Authors

- Jan Haji
- Pranav Ramesh
- Shimar Yasin Khan
- Timothy Nguyen
