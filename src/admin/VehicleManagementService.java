package admin;

import data.VehicleData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import model.Bike;
import model.Car;
import model.Truck;
import model.Vehicle;
import vehicle_display.VehicleCatalog;

public class VehicleManagementService {
    public VehicleManagementService() {
    }

    public List<VehicleRecord> getAll() {
        LinkedHashMap<String, VehicleRecord> recordsById = new LinkedHashMap<>();
        for (Vehicle vehicle : VehicleData.getVehicles()) {
            VehicleRecord record = toRecord(vehicle);
            recordsById.put(record.id.toLowerCase(), record);
        }

        VehicleCatalog catalog = new VehicleCatalog();
        for (vehicle_display.Vehicle vehicle : catalog.getAllVehicles()) {
            VehicleRecord record = toRecord(vehicle);
            recordsById.putIfAbsent(record.id.toLowerCase(), record);
        }

        List<VehicleRecord> records = new ArrayList<>(recordsById.values());
        return Collections.unmodifiableList(records);
    }

    public boolean add(VehicleRecord record) {
        if (record == null || record.id == null || record.id.trim().isEmpty() || findById(record.id) != null) {
            return false;
        }
        VehicleData.addVehicle(createVehicle(record));
        return true;
    }

    public boolean update(VehicleRecord updated) {
        if (updated == null || updated.id == null) {
            return false;
        }
        Vehicle modelVehicle = createVehicle(updated);
        if (VehicleData.updateVehicle(modelVehicle)) {
            return true;
        }

        vehicle_display.Vehicle displayVehicle = createDisplayVehicle(updated);
        return new VehicleCatalog().updateVehicle(displayVehicle);
    }

    public boolean remove(String id) {
        if (VehicleData.removeVehicle(id)) {
            return true;
        }
        return new VehicleCatalog().removeVehicle(id);
    }

    public VehicleRecord findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null;
        }
        for (VehicleRecord record : getAll()) {
            if (id.equalsIgnoreCase(record.id)) {
                return record;
            }
        }
        return null;
    }

    private VehicleRecord toRecord(Vehicle vehicle) {
        return new VehicleRecord(
                vehicle.getId(),
                vehicle.getClass().getSimpleName(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.isAvailable(),
                vehicle.getRentPerDay()
        );
    }

    private VehicleRecord toRecord(vehicle_display.Vehicle vehicle) {
        return new VehicleRecord(
                vehicle.getVehicleId(),
                vehicle.getType(),
                vehicle.getBrand(),
                vehicle.getName(),
                vehicle.isAvailable(),
                vehicle.getDailyRate()
        );
    }

    private Vehicle createVehicle(VehicleRecord record) {
        String type = record.type == null ? "Car" : record.type.trim();
        String brand = record.brand == null ? "" : record.brand.trim();
        String model = record.model == null ? "" : record.model.trim();
        double rate = record.dailyRate;
        boolean available = record.available;

        return switch (type.toLowerCase()) {
            case "bike" -> new Bike(record.id, brand, model, rate, available, 150);
            case "truck" -> new Truck(record.id, brand, model, rate, available, 2.5);
            default -> new Car(record.id, brand, model, rate, available, 4);
        };
    }

    private vehicle_display.Vehicle createDisplayVehicle(VehicleRecord record) {
        return new vehicle_display.Vehicle(
                record.id,
                record.model,
                record.brand,
                record.type,
                "Bike".equalsIgnoreCase(record.type) ? 2 : ("Truck".equalsIgnoreCase(record.type) ? 2 : 5),
                "Automatic",
                record.dailyRate,
                record.available
        );
    }

    public static class VehicleRecord {
        public final String id;
        public final String type;
        public final String brand;
        public final String model;
        public final boolean available;
        public final double dailyRate;

        public VehicleRecord(String id, String type, String brand, String model, boolean available, double dailyRate) {
            this.id = id;
            this.type = type;
            this.brand = brand;
            this.model = model;
            this.available = available;
            this.dailyRate = dailyRate;
        }
    }
}
