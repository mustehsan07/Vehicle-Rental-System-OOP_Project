package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Customer;

public final class CustomerData {
    private static final List<Customer> CUSTOMERS = new ArrayList<>();

    private CustomerData() {
    }

    public static void clear() {
        CUSTOMERS.clear();
    }

    public static void addCustomer(Customer customer) {
        if (customer != null) {
            CUSTOMERS.add(customer);
        }
    }

    public static boolean registerCustomer(Customer customer) {
        if (customer == null) {
            return false;
        }
        if (findCustomer(customer.getEmail()) != null) {
            return false;
        }
        CUSTOMERS.add(customer);
        return true;
    }

    public static boolean updateCustomer(Customer customer) {
        if (customer == null) {
            return false;
        }
        Customer existing = findById(customer.getId());
        if (existing == null) {
            return false;
        }
        int index = CUSTOMERS.indexOf(existing);
        CUSTOMERS.set(index, customer);
        return true;
    }

    public static boolean removeCustomer(String customerId) {
        Customer customer = findById(customerId);
        if (customer == null) {
            return false;
        }
        return CUSTOMERS.remove(customer);
    }

    public static List<Customer> getCustomers() {
        return Collections.unmodifiableList(CUSTOMERS);
    }

    public static Customer findCustomer(String identifier) {
        Customer byId = findById(identifier);
        return byId != null ? byId : findByEmail(identifier);
    }

    public static Customer findById(String id) {
        if (id == null) {
            return null;
        }
        for (Customer customer : CUSTOMERS) {
            if (id.equalsIgnoreCase(customer.getId())) {
                return customer;
            }
        }
        return null;
    }

    public static Customer findByEmail(String email) {
        if (email == null) {
            return null;
        }
        for (Customer customer : CUSTOMERS) {
            if (email.equalsIgnoreCase(customer.getEmail())) {
                return customer;
            }
        }
        return null;
    }

    public static boolean loginValidation(String email, String password) {
        Customer customer = findByEmail(email);
        return customer != null && customer.getPassword().equals(password);
    }
}