package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Customer;
import model.Rental;
import model.RentalHistory;

public final class RentalHistoryData {
    private static final List<RentalHistory> HISTORY = new ArrayList<>();

    private RentalHistoryData() {
    }

    public static void clear() {
        HISTORY.clear();
    }

    public static void addHistoryRecord(RentalHistory record) {
        if (record != null) {
            HISTORY.add(record);
        }
    }

    public static void addHistoryFromRental(Rental rental) {
        if (rental == null || rental.getCustomer() == null || rental.getVehicle() == null) {
            return;
        }

        addHistoryRecord(new RentalHistory(
                rental.getRentalId(),
                rental.getCustomer().getId(),
                rental.getCustomer().getFullName(),
                rental.getVehicle().getId(),
                rental.getVehicle().getBrand(),
                rental.getVehicle().getModel(),
                rental.getDays(),
                rental.getTotalCost(),
                rental.getRentDate(),
                rental.getReturnDate()
        ));
    }

    public static List<RentalHistory> getHistoryRecords() {
        return Collections.unmodifiableList(HISTORY);
    }

    public static List<RentalHistory> getHistoryForCustomer(String customerId) {
        List<RentalHistory> matches = new ArrayList<>();
        if (customerId == null || customerId.trim().isEmpty()) {
            return matches;
        }

        for (RentalHistory record : HISTORY) {
            if (customerId.equalsIgnoreCase(record.getCustomerId())) {
                matches.add(record);
            }
        }
        return matches;
    }

    public static List<RentalHistory> getHistoryForCustomer(Customer customer) {
        if (customer == null) {
            return new ArrayList<>();
        }
        return getHistoryForCustomer(customer.getId());
    }
}