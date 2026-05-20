package rental;

public class Vehicle {
    private final String vehicleId;
    private final String brand;
    private final String model;
    private final String category;
    private final double dailyRate;
    private boolean available;

    public Vehicle(String vehicleId, String brand, String model, String category, double dailyRate) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.category = category;
        this.dailyRate = dailyRate;
        this.available = true;
    }

    public String getVehicleId() { return vehicleId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public String getCategory() { return category; }
    public double getDailyRate() { return dailyRate; }
    public boolean isAvailable() { return available; }

    public void rent() { available = false; }
    public void returnVehicle() { available = true; }

    public String getDisplayName() {
        return brand + " " + model + " (" + vehicleId + ")";
    }
}
