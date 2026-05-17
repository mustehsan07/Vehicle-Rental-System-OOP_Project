import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class VehicleCatalog {
    private final List<Vehicle> vehicles;

    public VehicleCatalog() {
        vehicles = new ArrayList<>();
        loadSampleVehicles();
    }

    public List<Vehicle> getAvailableVehicles() {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isAvailable()) {
                result.add(vehicle);
            }
        }
        return Collections.unmodifiableList(result);
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

    private void loadSampleVehicles() {
        vehicles.add(new Vehicle("CAR-101", "Civic Oriel", "Honda", "Car", 5, "Automatic", 8500, true));
        vehicles.add(new Vehicle("CAR-102", "Corolla Altis", "Toyota", "Car", 5, "Automatic", 8000, true));
        vehicles.add(new Vehicle("SUV-201", "Sportage AWD", "KIA", "SUV", 5, "Automatic", 14500, true));
        vehicles.add(new Vehicle("SUV-202", "Fortuner Sigma", "Toyota", "SUV", 7, "Automatic", 22000, false));
        vehicles.add(new Vehicle("VAN-301", "Hiace Grand Cabin", "Toyota", "Van", 14, "Manual", 18000, true));
        vehicles.add(new Vehicle("BIK-401", "YBR 125G", "Yamaha", "Bike", 2, "Manual", 2200, true));
        vehicles.add(new Vehicle("BIK-402", "CB 150F", "Honda", "Bike", 2, "Manual", 2500, true));
        vehicles.add(new Vehicle("LUX-501", "S-Class", "Mercedes", "Luxury", 5, "Automatic", 38000, true));
    }
}