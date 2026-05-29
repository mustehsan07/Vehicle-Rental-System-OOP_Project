package data;

import model.Admin;
import model.Bike;
import model.Car;
import model.Customer;
import model.Rental;
import model.RentalHistory;
import model.Truck;
import model.Vehicle;

public final class SampleDataLoader {
    private static boolean loaded;
    private static Admin admin;

    private SampleDataLoader() {
    }

    public static synchronized void loadSampleData() {
        if (loaded) {
            return;
        }

        CustomerData.clear();
        VehicleData.clear();
        RentalData.clear();
        RentalHistoryData.clear();
        RentalRequestData.clear();

        admin = new Admin("mustehsansajjad2007@gmail.com", "admin123");

        CustomerData.addCustomer(new Customer("C001", "Hashir", "zainhashir209@gmail.com", "c123", "03001234567"));
        CustomerData.addCustomer(new Customer("C002", "Zain", "zani8297677@gmail.com", "c123", "03009876543"));
        CustomerData.addCustomer(new Customer("C003", "Abdullah", "azherabdullah24@gmail.com", "c123", "03009876543"));
        CustomerData.addCustomer(new Customer("C004", "Sumeed", "sumeedahmad71@gmail.com", "c123", "03009876543"));

        VehicleData.addVehicle(new Car("V001", "Toyota", "Corolla", 5500, true, 4));
        VehicleData.addVehicle(new Car("V002", "Honda", "Civic", 7500, true, 4));
        VehicleData.addVehicle(new Bike("V003", "Suzuki", "GS 150", 2200, true, 150));
        VehicleData.addVehicle(new Truck("V004", "Hyundai", "Porter", 9800, true, 2.5));

        Customer activeCustomer = CustomerData.findByEmail("zainhashir209@gmail.com");
        Vehicle activeVehicle = VehicleData.getVehicleById("V001");
        if (activeCustomer != null && activeVehicle != null) {
            RentalData.addActiveRental(new Rental("R001", activeCustomer, activeVehicle, 3, activeVehicle.calculateRent(3), "2026-05-17", "2026-05-20"));
        }

        Customer historyCustomer = CustomerData.findByEmail("zainhashir209@gmail.com");
        Vehicle historyVehicle = VehicleData.getVehicleById("V002");
        if (historyCustomer != null && historyVehicle != null) {
            RentalHistoryData.addHistoryRecord(new RentalHistory(
                    "H001",
                    historyCustomer.getId(),
                    historyCustomer.getFullName(),
                    historyVehicle.getId(),
                    historyVehicle.getBrand(),
                    historyVehicle.getModel(),
                    3,
                    historyVehicle.calculateRent(3),
                    "2026-05-12",
                    "2026-05-15"
            ));
        }

        loaded = true;
    }

    public static Admin getAdmin() {
        if (!loaded) {
            loadSampleData();
        }
        return admin;
    }
}