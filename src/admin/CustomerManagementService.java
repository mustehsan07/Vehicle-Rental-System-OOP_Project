package admin;

import data.CustomerData;
import data.RentalData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Customer;

public class CustomerManagementService {
    public CustomerManagementService() {
    }

    public List<CustomerRecord> getAll() {
        List<CustomerRecord> records = new ArrayList<>();
        for (Customer customer : CustomerData.getCustomers()) {
            records.add(toRecord(customer));
        }
        return Collections.unmodifiableList(records);
    }

    public boolean add(CustomerRecord record) {
        if (record == null || record.id == null || record.id.trim().isEmpty() || findById(record.id) != null) {
            return false;
        }
        return CustomerData.registerCustomer(new Customer(record.id, record.name, record.email, "", record.phone));
    }

    public boolean update(CustomerRecord updated) {
        if (updated == null || updated.id == null) {
            return false;
        }
        Customer existing = CustomerData.findById(updated.id);
        if (existing == null) {
            return false;
        }
        existing.setFullName(updated.name);
        existing.setEmail(updated.email);
        existing.setPhone(updated.phone);
        return CustomerData.updateCustomer(existing);
    }

    public boolean remove(String id) {
        return CustomerData.removeCustomer(id);
    }

    public CustomerRecord findById(String id) {
        Customer customer = CustomerData.findById(id);
        return customer != null ? toRecord(customer) : null;
    }

    private CustomerRecord toRecord(Customer customer) {
        return new CustomerRecord(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhone(),
                RentalData.getActiveRentalsForCustomer(customer).size()
        );
    }

    public static class CustomerRecord {
        public final String id;
        public final String name;
        public final String email;
        public final String phone;
        public final int activeRentals;

        public CustomerRecord(String id, String name, String email, String phone, int activeRentals) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.activeRentals = activeRentals;
        }
    }
}
