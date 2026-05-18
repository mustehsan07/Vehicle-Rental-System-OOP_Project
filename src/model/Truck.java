package model;

public class Truck extends Vehicle {
    private double loadCapacity;

    public Truck(String id, String brand, String model, double rentPerDay, boolean available, double loadCapacity) {
        super(id, brand, model, rentPerDay, available);
        this.loadCapacity = loadCapacity;
    }

    public double getLoadCapacity() { return loadCapacity; }
    public void setLoadCapacity(double loadCapacity) { this.loadCapacity = loadCapacity; }

    @Override
    public double calculateRent(int days) {
        return Math.max(1, days) * rentPerDay + 500;
    }
}