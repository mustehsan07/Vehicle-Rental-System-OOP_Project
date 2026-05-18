package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Vehicle;

public final class VehicleData {
    private static final List<Vehicle> VEHICLES = new ArrayList<>();

    private VehicleData() {
    }

    public static void clear() {
        VEHICLES.clear();
    }

    public static void addVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            VEHICLES.add(vehicle);
        }
    }

    public static boolean removeVehicle(String vehicleId) {
        Vehicle vehicle = getVehicleById(vehicleId);
        if (vehicle == null) {
            return false;
        }
        return VEHICLES.remove(vehicle);
    }

    public static boolean updateVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            return false;
        }
        Vehicle existing = getVehicleById(vehicle.getId());
        if (existing == null) {
            return false;
        }
        int index = VEHICLES.indexOf(existing);
        VEHICLES.set(index, vehicle);
        return true;
    }

    public static List<Vehicle> getVehicles() {
        return Collections.unmodifiableList(VEHICLES);
    }

    public static Vehicle getVehicleById(String id) {
        if (id == null) {
            return null;
        }
        for (Vehicle vehicle : VEHICLES) {
            if (id.equalsIgnoreCase(vehicle.getId())) {
                return vehicle;
            }
        }
        return null;
    }
}