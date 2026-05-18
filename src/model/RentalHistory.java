package model;

public class RentalHistory {
    private String historyId;
    private String customerId;
    private String customerName;
    private String vehicleId;
    private String vehicleBrand;
    private String vehicleModel;
    private int days;
    private double totalCost;
    private String rentDate;
    private String returnDate;
    private String status;

    public RentalHistory(String historyId, String customerId, String customerName, String vehicleId,
                         String vehicleBrand, String vehicleModel, int days, double totalCost,
                         String rentDate, String returnDate) {
        this.historyId = historyId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.vehicleId = vehicleId;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.days = days;
        this.totalCost = totalCost;
        this.rentDate = rentDate;
        this.returnDate = returnDate;
        this.status = "Returned";
    }

    public String getHistoryId() { return historyId; }
    public void setHistoryId(String historyId) { this.historyId = historyId; }
    public String getId() { return historyId; }
    public void setId(String id) { this.historyId = id; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public String getVehicleBrand() { return vehicleBrand; }
    public void setVehicleBrand(String vehicleBrand) { this.vehicleBrand = vehicleBrand; }
    public String getVehicleModel() { return vehicleModel; }
    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }
    public int getDays() { return days; }
    public void setDays(int days) { this.days = days; }
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public String getRentDate() { return rentDate; }
    public void setRentDate(String rentDate) { this.rentDate = rentDate; }
    public String getReturnDate() { return returnDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getBill() { return totalCost; }
    public void setBill(double bill) { this.totalCost = bill; }

    public String getVehicleDisplayName() {
        String brand = vehicleBrand != null ? vehicleBrand : "";
        String model = vehicleModel != null ? vehicleModel : "";
        String combined = (brand + " " + model).trim();
        if (combined.isEmpty()) {
            combined = "Vehicle";
        }
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            return combined;
        }
        return combined + " (" + vehicleId + ")";
    }
}