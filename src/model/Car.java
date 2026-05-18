package model;

public class Car extends Vehicle {
    private int numberOfDoors;

    public Car(String id, String brand, String model, double rentPerDay, boolean available, int numberOfDoors) {
        super(id, brand, model, rentPerDay, available);
        this.numberOfDoors = numberOfDoors;
    }

    public int getNumberOfDoors() { return numberOfDoors; }
    public void setNumberOfDoors(int numberOfDoors) { this.numberOfDoors = numberOfDoors; }

    @Override
    public double calculateRent(int days) {
        return Math.max(1, days) * rentPerDay;
    }
}