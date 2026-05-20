package rental;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RentalRecord {
    private final String rentalId;
    private final Vehicle vehicle;
    private final String customerName;
    private final String customerPhone;
    private final LocalDate rentDate;
    private final LocalDate expectedReturnDate;
    private final double estimatedCost;
    private LocalDate actualReturnDate;
    private double finalCost;
    private String status;

    public RentalRecord(String rentalId, Vehicle vehicle, String customerName, String customerPhone,
                        LocalDate rentDate, LocalDate expectedReturnDate) {
        this.rentalId = rentalId;
        this.vehicle = vehicle;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.rentDate = rentDate;
        this.expectedReturnDate = expectedReturnDate;
        this.estimatedCost = calculateCost(rentDate, expectedReturnDate);
        this.finalCost = estimatedCost;
        this.status = "Active";
    }

    public String getRentalId() { return rentalId; }
    public Vehicle getVehicle() { return vehicle; }
    public String getCustomerName() { return customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public LocalDate getRentDate() { return rentDate; }
    public LocalDate getExpectedReturnDate() { return expectedReturnDate; }
    public double getEstimatedCost() { return estimatedCost; }
    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public double getFinalCost() { return finalCost; }
    public String getStatus() { return status; }

    public void close(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
        this.finalCost = calculateReturnCost(actualReturnDate);
        this.status = "Returned";
    }

    private double calculateReturnCost(LocalDate returnDate) {
        double baseCost = calculateCost(rentDate, returnDate);
        long lateDays = ChronoUnit.DAYS.between(expectedReturnDate, returnDate);
        double lateFee = lateDays > 0 ? lateDays * vehicle.getDailyRate() * 0.25 : 0;
        return baseCost + lateFee;
    }

    private double calculateCost(LocalDate startDate, LocalDate endDate) {
        long days = Math.max(1, ChronoUnit.DAYS.between(startDate, endDate));
        return days * vehicle.getDailyRate();
    }
}
