import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RentalHistoryRecord {
    private String rentalId;
    private String customerName;
    private String vehicleName;
    private LocalDate rentDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double dailyRate;
    private RentalStatus status;

    public RentalHistoryRecord(String rentalId, String customerName, String vehicleName,
                               LocalDate rentDate, LocalDate dueDate, LocalDate returnDate,
                               double dailyRate, RentalStatus status) {
        this.rentalId = rentalId;
        this.customerName = customerName;
        this.vehicleName = vehicleName;
        this.rentDate = rentDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.dailyRate = dailyRate;
        this.status = status;
    }

    public long getTotalDays() {
        LocalDate endDate = returnDate;
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        long days = ChronoUnit.DAYS.between(rentDate, endDate);
        if (days < 1) {
            return 1;
        }
        return days;
    }

    public double getTotalCost() {
        return getTotalDays() * dailyRate;
    }

    public boolean isOverdue() {
        return status == RentalStatus.ACTIVE && LocalDate.now().isAfter(dueDate);
    }

    public String getDisplayStatus() {
        if (isOverdue()) {
            return "Overdue";
        }
        return status.getText();
    }

    public String getRentalId() {
        return rentalId;
    }

    public void setRentalId(String rentalId) {
        this.rentalId = rentalId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public LocalDate getRentDate() {
        return rentDate;
    }

    public void setRentDate(LocalDate rentDate) {
        this.rentDate = rentDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }
}