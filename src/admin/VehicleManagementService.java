package admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VehicleManagementService {
    private final List<VehicleRecord> vehicles = new ArrayList<>();

    public VehicleManagementService() {
        vehicles.add(new VehicleRecord("V001", "Car", "Toyota Corolla", true, 50.0));
        vehicles.add(new VehicleRecord("V002", "Bike", "Honda CB150F", true, 20.0));
        vehicles.add(new VehicleRecord("V003", "Truck", "Isuzu NKR", false, 95.0));
    }

    public List<VehicleRecord> getAll() {
        return Collections.unmodifiableList(vehicles);
    }

    public boolean add(VehicleRecord record) {
        if (record == null || record.id == null || record.id.trim().isEmpty() || findById(record.id) != null) {
            return false;
        }
        return vehicles.add(record);
    }

    public boolean update(VehicleRecord updated) {
        if (updated == null || updated.id == null) {
            return false;
        }
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).id.equalsIgnoreCase(updated.id)) {
                vehicles.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public boolean remove(String id) {
        return vehicles.removeIf(v -> v.id.equalsIgnoreCase(id));
    }

    public VehicleRecord findById(String id) {
        for (VehicleRecord v : vehicles) {
            if (v.id.equalsIgnoreCase(id)) {
                return v;
            }
        }
        return null;
    }

    public static class VehicleRecord {
        public final String id;
        public final String type;
        public final String model;
        public final boolean available;
        public final double dailyRate;

        public VehicleRecord(String id, String type, String model, boolean available, double dailyRate) {
            this.id = id;
            this.type = type;
            this.model = model;
            this.available = available;
            this.dailyRate = dailyRate;
        }
    }
}
