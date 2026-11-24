package pl.wypozyczalnia.ui;

import java.time.Clock;
import java.util.List;
import java.util.Scanner;
import pl.wypozyczalnia.application.CustomerService;
import pl.wypozyczalnia.application.EquipmentService;
import pl.wypozyczalnia.application.RentalService;
import pl.wypozyczalnia.domain.Customer;
import pl.wypozyczalnia.domain.Equipment;
import pl.wypozyczalnia.domain.Money;
import pl.wypozyczalnia.domain.Rental;
import pl.wypozyczalnia.domain.rules.RentalRules;
import pl.wypozyczalnia.infrastructure.InMemoryRepository;

/** Punkt startowy: ręczne tworzenie i łączenie obiektów (bez kontenera DI). */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        InMemoryRepository<Equipment> equipment = InMemoryRepository.of(List.of(
                new Equipment("E1", "Wiertarka udarowa", Money.of("25")),
                new Equipment("E2", "Rower górski", Money.of("60")),
                new Equipment("E3", "Namiot czteroosobowy", Money.of("40"))));
        InMemoryRepository<Customer> customers = InMemoryRepository.of(List.of(
                new Customer("K1", "Jan Kowalski"),
                new Customer("K2", "Anna Nowak")));
        InMemoryRepository<Rental> rentals = new InMemoryRepository<>();

        RentalService rentalService = new RentalService(equipment, customers, rentals, RentalRules.standard(),
                Clock.systemDefaultZone());
        rentalService.addListener(new ConsoleNotifier(System.out));

        new ConsoleApp(new Scanner(System.in), System.out, rentalService, new EquipmentService(equipment),
                new CustomerService(customers)).run();
    }
}
