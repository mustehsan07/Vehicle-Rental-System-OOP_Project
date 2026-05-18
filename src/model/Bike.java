package model;

public class Bike extends Vehicle {
    private int engineCapacity;

    public Bike(String id, String brand, String model, double rentPerDay, boolean available, int engineCapacity) {
        super(id, brand, model, rentPerDay, available);
        this.engineCapacity = engineCapacity;
    }

    public int getEngineCapacity() { return engineCapacity; }
    public void setEngineCapacity(int engineCapacity) { this.engineCapacity = engineCapacity; }

    @Override
    public double calculateRent(int days) {
        return Math.max(1, days) * (rentPerDay * 0.85);
    }
}