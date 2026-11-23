package pl.wypozyczalnia.ui;

import java.time.Clock;
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
        InMemoryRepository<Equipment> equipment = new InMemoryRepository<>();
        InMemoryRepository<Customer> customers = new InMemoryRepository<>();
        InMemoryRepository<Rental> rentals = new InMemoryRepository<>();
        addSampleData(equipment, customers);


        RentalService rentalService = new RentalService(equipment, customers, rentals, RentalRules.standard(),
                Clock.systemDefaultZone());
        rentalService.addListener(new ConsoleNotifier(System.out));

        new ConsoleApp(new Scanner(System.in), System.out, rentalService, new EquipmentService(equipment),
                new CustomerService(customers)).run();
    }

    private static void addSampleData(InMemoryRepository<Equipment> equipment,
                                      InMemoryRepository<Customer> customers) {
        equipment.save(new Equipment("E1", "Wiertarka udarowa", Money.of("25")));
        equipment.save(new Equipment("E2", "Rower górski", Money.of("60")));
        equipment.save(new Equipment("E3", "Namiot czteroosobowy", Money.of("40")));
        customers.save(new Customer("K1", "Jan Kowalski"));
        customers.save(new Customer("K2", "Anna Nowak"));
    }
}
