package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Customer;
import model.Rental;

public final class RentalData {
    private static final List<Rental> RENTALS = new ArrayList<>();

    private RentalData() {
    }

    public static void clear() {
        RENTALS.clear();
    }

    public static void addRental(Rental rental) {
        if (rental != null) {
            RENTALS.add(rental);
        }
    }

    public static void addActiveRental(Rental rental) {
        addRental(rental);
    }

    public static boolean removeRental(String rentalId) {
        Rental rental = findById(rentalId);
        if (rental == null) {
            return false;
        }
        return RENTALS.remove(rental);
    }

    public static List<Rental> getActiveRentals() {
        return Collections.unmodifiableList(RENTALS);
    }

    public static List<Rental> getRentals() {
        return getActiveRentals();
    }

    public static List<Rental> getRentalsForCustomer(Customer customer) {
        return getActiveRentalsForCustomer(customer);
    }

    public static List<Rental> getActiveRentalsForCustomer(Customer customer) {
        List<Rental> matches = new ArrayList<>();
        if (customer == null) {
            return matches;
        }
        for (Rental rental : RENTALS) {
            if (rental.getCustomer() != null && customer.getId().equalsIgnoreCase(rental.getCustomer().getId())) {
                matches.add(rental);
            }
        }
        return matches;
    }

    public static List<Rental> getActiveRentalsForCustomer(String customerId) {
        List<Rental> matches = new ArrayList<>();
        if (customerId == null || customerId.trim().isEmpty()) {
            return matches;
        }
        for (Rental rental : RENTALS) {
            if (rental.getCustomer() != null && customerId.equalsIgnoreCase(rental.getCustomer().getId())) {
                matches.add(rental);
            }
        }
        return matches;
    }

    public static Rental findById(String id) {
        if (id == null) {
            return null;
        }
        for (Rental rental : RENTALS) {
            if (id.equalsIgnoreCase(rental.getId())) {
                return rental;
            }
        }
        return null;
    }
}