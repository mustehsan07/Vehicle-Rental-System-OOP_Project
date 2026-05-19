public enum RentalStatus {
    ACTIVE("Active"),
    RETURNED("Returned"),
    OVERDUE("Overdue"),
    CANCELLED("Cancelled");

    private final String text;

    RentalStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String toString() {
        return text;
    }
}