package auth;

import data.CustomerData;
import data.SampleDataLoader;
import model.Admin;
import model.Customer;

public final class AuthController {
    private AuthController() {
    }

    public static final class AuthRecord {
        public final String id;
        public final String fullName;
        public final String email;
        public final String password;
        public final String status;
        public final String role;

        public AuthRecord(String id, String fullName, String email, String password, String status, String role) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.status = status;
            this.role = role;
        }
    }

    public static AuthRecord authenticate(String identifier, String password) {
        if (identifier == null || password == null) {
            return null;
        }

        String normalizedIdentifier = identifier.trim();
        String normalizedPassword = password;
        if (normalizedIdentifier.isEmpty() || normalizedPassword.isEmpty()) {
            return null;
        }

        for (Customer user : CustomerData.getCustomers()) {
            if (normalizedIdentifier.equalsIgnoreCase(user.getEmail()) && normalizedPassword.equals(user.getPassword())) {
                return new AuthRecord(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getPassword(),
                        "Active",
                        "customer"
                );
            }
        }

        Admin admin = SampleDataLoader.getAdmin();
        if (admin != null && normalizedIdentifier.equalsIgnoreCase(admin.getEmail()) && normalizedPassword.equals(admin.getPassword())) {
            return new AuthRecord("A001", "Administrator", admin.getEmail(), admin.getPassword(), "Active", "admin");
        }
        return null;
    }

    public static boolean registerCustomer(String fullName, String email, String password) {
        String trimmedName = safeTrim(fullName);
        String trimmedEmail = safeTrim(email);
        String trimmedPassword = safeTrim(password);

        if (trimmedName.isEmpty() || trimmedEmail.isEmpty() || trimmedPassword.isEmpty()) {
            return false;
        }

        for (Customer user : CustomerData.getCustomers()) {
            if (trimmedEmail.equalsIgnoreCase(user.getEmail())) {
                return false;
            }
        }

        CustomerData.registerCustomer(new Customer(nextUserId(), trimmedName, trimmedEmail, trimmedPassword, ""));
        return true;
    }

    private static String nextUserId() {
        int nextNumber = CustomerData.getCustomers().size() + 1;
        while (containsUserId(String.format("U%03d", nextNumber))) {
            nextNumber++;
        }
        return String.format("U%03d", nextNumber);
    }

    private static boolean containsUserId(String userId) {
        for (Customer user : CustomerData.getCustomers()) {
            if (userId.equalsIgnoreCase(user.getId())) {
                return true;
            }
        }
        return false;
    }

    private static String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }
}