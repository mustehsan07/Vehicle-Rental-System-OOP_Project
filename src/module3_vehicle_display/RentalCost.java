public class RentalCost {
    private final double subtotal;
    private final double discount;
    private final double serviceCharge;
    private final double grandTotal;

    public RentalCost(double subtotal, double discount, double serviceCharge, double grandTotal) {
        this.subtotal = subtotal;
        this.discount = discount;
        this.serviceCharge = serviceCharge;
        this.grandTotal = grandTotal;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public double getServiceCharge() {
        return serviceCharge;
    }

    public double getGrandTotal() {
        return grandTotal;
    }
}