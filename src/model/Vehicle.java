package model;

public abstract class Vehicle {
    protected String id;
    protected String brand;
    protected String model;
    protected double rentPerDay;
    protected boolean available;

    protected Vehicle(String id, String brand, String model, double rentPerDay, boolean available) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.rentPerDay = rentPerDay;
        this.available = available;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public double getRentPerDay() { return rentPerDay; }
    public void setRentPerDay(double rentPerDay) { this.rentPerDay = rentPerDay; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String getName() { return model; }
    public void setName(String name) { this.model = name; }
    public double getRatePerMinute() { return rentPerDay; }
    public void setRatePerMinute(double rentPerMinute) { this.rentPerDay = rentPerMinute; }
    public String getType() { return getClass().getSimpleName(); }
    public void setType(String type) { }

    public abstract double calculateRent(int days);
}