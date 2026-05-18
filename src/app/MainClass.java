package app;

import auth.ProVehicleLogin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class MainClass {

    public static final List<UserRow> USERS = new ArrayList<>();
    public static final List<VehicleRow> VEHICLES = new ArrayList<>();
    public static final List<RentalHistoryRow> RENTAL_HISTORY = new ArrayList<>();

    static {
        USERS.add(new UserRow("U001", "Mustehsan", "mustehsan@gmail.com", "admin123", "Active", "admin"));
        USERS.add(new UserRow("U002", "Ibraheem", "ibraheem@gmail.com", "customer123", "Active", "customer"));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ignored) {
        }

        SwingUtilities.invokeLater(() -> new ProVehicleLogin().setVisible(true));
    }

    public static void addUser(UserRow user) {
        if (user != null) {
            USERS.add(user);
        }
    }

    public static void addVehicle(VehicleRow vehicle) {
        if (vehicle != null) {
            VEHICLES.add(vehicle);
        }
    }

    public static void addRentalHistory(RentalHistoryRow historyRow) {
        if (historyRow != null) {
            RENTAL_HISTORY.add(historyRow);
        }
    }

    public static List<UserRow> getUsers() {
        return Collections.unmodifiableList(USERS);
    }

    public static List<VehicleRow> getVehicles() {
        return Collections.unmodifiableList(VEHICLES);
    }

    public static List<RentalHistoryRow> getRentalHistory() {
        return Collections.unmodifiableList(RENTAL_HISTORY);
    }

    public static final class UserRow {
        public final String id;
        public final String fullName;
        public final String email;
        public final String password;
        public final String status;
        public final String role;

        public UserRow(String id, String fullName, String email, String password, String status, String role) {
            this.id = id;
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.status = status;
            this.role = role;
        }
    }

    public static final class VehicleRow {
        public final String id;
        public final String type;
        public final String vehicleName;
        public final String status;
        public final double ratePerMinute;

        public VehicleRow(String id, String type, String vehicleName, String status, double ratePerMinute) {
            this.id = id;
            this.type = type;
            this.vehicleName = vehicleName;
            this.status = status;
            this.ratePerMinute = ratePerMinute;
        }
    }

    public static final class RentalHistoryRow {
        public final String id;
        public final String customerName;
        public final String vehicleName;
        public final String date;
        public final String duration;
        public final double bill;

        public RentalHistoryRow(String id, String customerName, String vehicleName, String date, String duration, double bill) {
            this.id = id;
            this.customerName = customerName;
            this.vehicleName = vehicleName;
            this.date = date;
            this.duration = duration;
            this.bill = bill;
        }
    }
}