package vehicle_display;

public class Vehicle {
    private final String vehicleId;
    private final String name;
    private final String brand;
    private final String type;
    private final int seats;
    private final String transmission;
    private final double dailyRate;
    private boolean available;

    public Vehicle(
            String vehicleId,
            String name,
            String brand,
            String type,
            int seats,
            String transmission,
            double dailyRate,
            boolean available) {
        this.vehicleId = vehicleId;
        this.name = name;
        this.brand = brand;
        this.type = type;
        this.seats = seats;
        this.transmission = transmission;
        this.dailyRate = dailyRate;
        this.available = available;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getType() {
        return type;
    }

    public int getSeats() {
        return seats;
    }

    public String getTransmission() {
        return transmission;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void rent() {
        available = false;
    }

    public String getDisplayName() {
        return brand + " " + name + " (" + vehicleId + ")";
    }
}