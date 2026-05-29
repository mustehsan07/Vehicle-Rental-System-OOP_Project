package model;

public class RentalVehicle extends Vehicle {
    private String vehicleType;

    public RentalVehicle(String id, String brand, String model, String vehicleType, double rentPerDay, boolean available) {
        super(id, brand, model, rentPerDay, available);
        this.vehicleType = vehicleType;
    }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    @Override
    public String getType() {
        return vehicleType;
    }

    @Override
    public void setType(String type) {
        this.vehicleType = type;
    }

    @Override
    public double calculateRent(int days) {
        return Math.max(1, days) * rentPerDay;
    }
}
