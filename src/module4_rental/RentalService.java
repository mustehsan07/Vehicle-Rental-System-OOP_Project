package vehiclerental;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RentalService {
    private final List<Vehicle> vehicles;
    private final List<RentalRecord> rentalRecords;
    private int nextRentalNumber;

    public RentalService() {
        vehicles = new ArrayList<>();
        rentalRecords = new ArrayList<>();
        nextRentalNumber = 1001;
        seedVehicles();
    }

    public List<Vehicle> getVehicles() {
        return Collections.unmodifiableList(vehicles);
    }

    public List<RentalRecord> getRentalRecords() {
        return Collections.unmodifiableList(rentalRecords);
    }

    public List<RentalRecord> getActiveRentals() {
        List<RentalRecord> active = new ArrayList<>();
        for (RentalRecord record : rentalRecords) {
            if ("Active".equals(record.getStatus())) {
                active.add(record);
            }
        }
        return active;
    }

    public RentalRecord rentVehicle(String vehicleId, String customerName, String customerPhone,
                                    LocalDate rentDate, LocalDate expectedReturnDate) {
        Vehicle vehicle = findVehicle(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found."));

        if (!vehicle.isAvailable()) {
            throw new IllegalStateException("This vehicle is already rented.");
        }

        if (customerName == null || customerName.trim().length() < 3) {
            throw new IllegalArgumentException("Customer name must contain at least 3 characters.");
        }

        if (customerPhone == null || customerPhone.trim().length() < 7) {
            throw new IllegalArgumentException("Enter a valid customer phone number.");
        }

        if (expectedReturnDate == null || !expectedReturnDate.isAfter(rentDate)) {
            throw new IllegalArgumentException("Expected return date must be after rent date.");
        }

        RentalRecord record = new RentalRecord(
                "RNT-" + nextRentalNumber++,
                vehicle,
                customerName.trim(),
                customerPhone.trim(),
                rentDate,
                expectedReturnDate
        );

        vehicle.rent();
        rentalRecords.add(record);
        return record;
    }

    public RentalRecord returnVehicle(String rentalId, LocalDate actualReturnDate) {
        RentalRecord record = findRental(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Rental record not found."));

        if (!"Active".equals(record.getStatus())) {
            throw new IllegalStateException("This rental has already been returned.");
        }

        if (actualReturnDate == null || actualReturnDate.isBefore(record.getRentDate())) {
            throw new IllegalArgumentException("Return date cannot be before rent date.");
        }

        record.close(actualReturnDate);
        record.getVehicle().returnVehicle();
        return record;
    }

    public Optional<Vehicle> findVehicle(String vehicleId) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equals(vehicleId)) {
                return Optional.of(vehicle);
            }
        }
        return Optional.empty();
    }

    public Optional<RentalRecord> findRental(String rentalId) {
        for (RentalRecord record : rentalRecords) {
            if (record.getRentalId().equals(rentalId)) {
                return Optional.of(record);
            }
        }
        return Optional.empty();
    }

    private void seedVehicles() {
        vehicles.add(new Vehicle("VH-101", "Toyota", "Corolla", "Sedan", 5500));
        vehicles.add(new Vehicle("VH-102", "Honda", "Civic", "Sedan", 7500));
        vehicles.add(new Vehicle("VH-103", "Suzuki", "Cultus", "Hatchback", 4200));
        vehicles.add(new Vehicle("VH-104", "Hyundai", "Tucson", "SUV", 10500));
        vehicles.add(new Vehicle("VH-105", "KIA", "Sportage", "SUV", 12000));
        vehicles.add(new Vehicle("VH-106", "Toyota", "Hiace", "Van", 14000));
    }
}
