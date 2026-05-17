package admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomerManagementService {
    private final List<CustomerRecord> customers = new ArrayList<>();

    public CustomerManagementService() {
        customers.add(new CustomerRecord("C001", "Ali Raza", "ali@example.com", "Active"));
        customers.add(new CustomerRecord("C002", "Sara Khan", "sara@example.com", "Active"));
        customers.add(new CustomerRecord("C003", "Hamza Malik", "hamza@example.com", "Blocked"));
    }

    public List<CustomerRecord> getAll() {
        return Collections.unmodifiableList(customers);
    }

    public boolean add(CustomerRecord record) {
        if (record == null || record.id == null || record.id.trim().isEmpty() || findById(record.id) != null) {
            return false;
        }
        return customers.add(record);
    }

    public boolean update(CustomerRecord updated) {
        if (updated == null || updated.id == null) {
            return false;
        }
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).id.equalsIgnoreCase(updated.id)) {
                customers.set(i, updated);
                return true;
            }
        }
        return false;
    }

    public boolean remove(String id) {
        return customers.removeIf(c -> c.id.equalsIgnoreCase(id));
    }

    public CustomerRecord findById(String id) {
        for (CustomerRecord c : customers) {
            if (c.id.equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
    }

    public static class CustomerRecord {
        public final String id;
        public final String name;
        public final String email;
        public final String status;

        public CustomerRecord(String id, String name, String email, String status) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.status = status;
        }
    }
}
