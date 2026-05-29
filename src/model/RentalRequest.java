package model;

public class RentalRequest {
    private String requestId;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String vehicleId;
    private String vehicleBrand;
    private String vehicleModel;
    private String vehicleType;
    private double dailyRate;
    private int days;
    private double totalCost;
    private String requestDate;
    private String status;

    public RentalRequest(String requestId, String customerId, String customerName, String customerEmail, String customerPhone,
                         String vehicleId, String vehicleBrand, String vehicleModel, String vehicleType,
                         double dailyRate, int days, double totalCost, String requestDate, String status) {
        this.requestId = requestId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.vehicleId = vehicleId;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.vehicleType = vehicleType;
        this.dailyRate = dailyRate;
        this.days = days;
        this.totalCost = totalCost;
        this.requestDate = requestDate;
        this.status = status;
    }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public String getVehicleBrand() { return vehicleBrand; }
    public void setVehicleBrand(String vehicleBrand) { this.vehicleBrand = vehicleBrand; }
    public String getVehicleModel() { return vehicleModel; }
    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public double getDailyRate() { return dailyRate; }
    public void setDailyRate(double dailyRate) { this.dailyRate = dailyRate; }
    public int getDays() { return days; }
    public void setDays(int days) { this.days = days; }
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public String getRequestDate() { return requestDate; }
    public void setRequestDate(String requestDate) { this.requestDate = requestDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getVehicleDisplayName() {
        return vehicleBrand + " " + vehicleModel + " (" + vehicleId + ")";
    }

    public String getCustomerDisplayName() {
        return customerName + " (" + customerId + ")";
    }
}
