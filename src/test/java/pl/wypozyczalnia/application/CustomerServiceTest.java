package pl.wypozyczalnia.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import pl.wypozyczalnia.domain.Customer;
import pl.wypozyczalnia.infrastructure.InMemoryRepository;

class CustomerServiceTest {

    private final InMemoryRepository<Customer> repository = new InMemoryRepository<>();
    private final CustomerService service = new CustomerService(repository);

    @Test
    void listsCustomersInOrderOfAdding() {
        repository.save(new Customer("K1", "Jan Kowalski"));
        repository.save(new Customer("K2", "Anna Nowak"));

        List<String> ids = service.allCustomers().stream().map(Customer::id).toList();

        assertEquals(List.of("K1", "K2"), ids);
    }

    @Test
    void returnsEmptyListWhenThereAreNoCustomers() {
        assertTrue(service.allCustomers().isEmpty());
    }
}
