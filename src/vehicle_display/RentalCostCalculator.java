public class RentalCostCalculator {
    private static final double SERVICE_CHARGE_RATE = 0.05;
    private static final double LONG_RENTAL_DISCOUNT_RATE = 0.10;
    private static final int DISCOUNT_MINIMUM_DAYS = 7;

    public RentalCost calculate(Vehicle vehicle, int rentalDays) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Please select a vehicle first.");
        }
        if (rentalDays <= 0) {
            throw new IllegalArgumentException("Rental days must be greater than zero.");
        }

        double subtotal = vehicle.getDailyRate() * rentalDays;
        double discount = rentalDays >= DISCOUNT_MINIMUM_DAYS ? subtotal * LONG_RENTAL_DISCOUNT_RATE : 0;
        double serviceCharge = (subtotal - discount) * SERVICE_CHARGE_RATE;
        double grandTotal = subtotal - discount + serviceCharge;

        return new RentalCost(subtotal, discount, serviceCharge, grandTotal);
    }
}