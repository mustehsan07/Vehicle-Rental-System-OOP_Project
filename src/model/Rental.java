package model;

public class Rental {
    private String rentalId;
    private Customer customer;
    private Vehicle vehicle;
    private int days;
    private double totalCost;
    private String rentDate;
    private String returnDate;
    private String status;

    public Rental(String rentalId, Customer customer, Vehicle vehicle, int days, double totalCost, String rentDate, String returnDate) {
        this.rentalId = rentalId;
        this.customer = customer;
        this.vehicle = vehicle;
        this.days = days;
        this.totalCost = totalCost;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
        this.status = "Active";
    }

    public String getRentalId() { return rentalId; }
    public void setRentalId(String rentalId) { this.rentalId = rentalId; }
    public String getId() { return rentalId; }
    public void setId(String id) { this.rentalId = id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    public int getDays() { return days; }
    public void setDays(int days) { this.days = days; }
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public String getRentDate() { return rentDate; }
    public void setRentDate(String rentDate) { this.rentDate = rentDate; }
    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public double getBill() { return totalCost; }
    public void setBill(double bill) { this.totalCost = bill; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}