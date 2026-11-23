package pl.wypozyczalnia.application;

import java.util.List;
import pl.wypozyczalnia.domain.Customer;
import pl.wypozyczalnia.domain.Repository;

/** Przypadki użycia: przegląd klientów wypożyczalni. */
public class CustomerService {

    private final Repository<Customer> customers;

    public CustomerService(Repository<Customer> customers) {
        this.customers = customers;
    }

    public List<Customer> allCustomers() {
        return customers.findAll();
    }
}
