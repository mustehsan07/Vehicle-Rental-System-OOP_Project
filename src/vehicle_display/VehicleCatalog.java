package vehicle_display;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class VehicleCatalog {
    private static final List<Vehicle> VEHICLES = new ArrayList<>();
    private static boolean loaded;

    public VehicleCatalog() {
        ensureLoaded();
    }

    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : VEHICLES) {
            if (vehicle.isAvailable()) {
                result.add(vehicle);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public List<Vehicle> getAllVehicles() {
        return Collections.unmodifiableList(new ArrayList<>(VEHICLES));
    }

    public Vehicle findVehicleById(String vehicleId) {
        if (vehicleId == null) {
            return null;
        }

        for (Vehicle vehicle : VEHICLES) {
            if (vehicle.getVehicleId().equalsIgnoreCase(vehicleId)) {
                return vehicle;
            }
        }
        return null;
    }

    public boolean rentVehicle(String vehicleId) {
        Vehicle vehicle = findVehicleById(vehicleId);
        if (vehicle == null || !vehicle.isAvailable()) {
            return false;
        }

        vehicle.rent();
        return true;
    }

    public boolean returnVehicle(String vehicleId) {
        Vehicle vehicle = findVehicleById(vehicleId);
        if (vehicle == null || vehicle.isAvailable()) {
            return false;
        }

        vehicle.returnVehicle();
        return true;
    }

    public boolean updateVehicle(Vehicle updatedVehicle) {
        if (updatedVehicle == null || updatedVehicle.getVehicleId() == null) {
            return false;
        }

        for (int index = 0; index < VEHICLES.size(); index++) {
            Vehicle current = VEHICLES.get(index);
            if (current.getVehicleId().equalsIgnoreCase(updatedVehicle.getVehicleId())) {
                VEHICLES.set(index, updatedVehicle);
                return true;
            }
        }
        return false;
    }

    public boolean removeVehicle(String vehicleId) {
        Vehicle vehicle = findVehicleById(vehicleId);
        if (vehicle == null) {
            return false;
        }
        return VEHICLES.remove(vehicle);
    }

    public List<Vehicle> searchAvailableVehicles(String keyword, String type) {
        String query = keyword == null ? "" : keyword.trim().toLowerCase(Locale.ROOT);
        String selectedType = type == null ? "All" : type;

        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : getAvailableVehicles()) {
            boolean matchesType = selectedType.equals("All") || vehicle.getType().equalsIgnoreCase(selectedType);
            boolean matchesQuery = query.isEmpty()
                    || vehicle.getName().toLowerCase(Locale.ROOT).contains(query)
                    || vehicle.getBrand().toLowerCase(Locale.ROOT).contains(query)
                    || vehicle.getVehicleId().toLowerCase(Locale.ROOT).contains(query);

            if (matchesType && matchesQuery) {
                result.add(vehicle);
            }
        }
        return result;
    }

    private void ensureLoaded() {
        if (loaded) {
            return;
        }

        loadSampleVehicles();
        loaded = true;
    }

    private void loadSampleVehicles() {
        VEHICLES.add(new Vehicle("CAR-101", "Civic Oriel", "Honda", "Car", 5, "Automatic", 8500, true));
        VEHICLES.add(new Vehicle("CAR-102", "Corolla Altis", "Toyota", "Car", 5, "Automatic", 8000, true));
        VEHICLES.add(new Vehicle("SUV-201", "Sportage AWD", "KIA", "SUV", 5, "Automatic", 14500, true));
        VEHICLES.add(new Vehicle("SUV-202", "Fortuner Sigma", "Toyota", "SUV", 7, "Automatic", 22000, false));
        VEHICLES.add(new Vehicle("VAN-301", "Hiace Grand Cabin", "Toyota", "Van", 14, "Manual", 18000, true));
        VEHICLES.add(new Vehicle("BIK-401", "YBR 125G", "Yamaha", "Bike", 2, "Manual", 2200, true));
        VEHICLES.add(new Vehicle("BIK-402", "CB 150F", "Honda", "Bike", 2, "Manual", 2500, true));
        VEHICLES.add(new Vehicle("LUX-501", "S-Class", "Mercedes", "Luxury", 5, "Automatic", 38000, true));
    }
}