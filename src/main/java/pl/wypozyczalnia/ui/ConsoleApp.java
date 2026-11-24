package pl.wypozyczalnia.ui;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import pl.wypozyczalnia.application.CustomerService;
import pl.wypozyczalnia.application.EquipmentService;
import pl.wypozyczalnia.application.RentalService;
import pl.wypozyczalnia.domain.Customer;
import pl.wypozyczalnia.domain.DomainException;
import pl.wypozyczalnia.domain.Equipment;
import pl.wypozyczalnia.domain.Rental;

/** Menu tekstowe. Tylko czyta dane od użytkownika i wywołuje przypadki użycia. */
public class ConsoleApp {

    private static final String MENU = """

            1. Lista sprzętu
            2. Wypożycz sprzęt
            3. Zwróć sprzęt
            4. Aktywne wypożyczenia
            5. Oddaj sprzęt do serwisu
            6. Zakończ serwis sprzętu
            7. Lista klientów
            0. Koniec""";

    private final Scanner in;
    private final PrintStream out;
    private final RentalService rentals;
    private final EquipmentService equipment;
    private final CustomerService customers;

    public ConsoleApp(Scanner in, PrintStream out, RentalService rentals, EquipmentService equipment,
                      CustomerService customers) {
        this.in = in;
        this.out = out;
        this.rentals = rentals;
        this.equipment = equipment;
        this.customers = customers;
    }

    public void run() {
        while (true) {
            out.println(MENU);
            String choice = ask("Wybór");
            if (choice.equals("0")) {
                return;
            }
            try {
                handle(choice);
            } catch (DomainException e) {
                out.println("Błąd: " + e.getMessage());
            } catch (DateTimeParseException e) {
                out.println("Błąd: nieprawidłowa data, użyj formatu RRRR-MM-DD");
            }
        }
    }

    private void handle(String choice) {
        switch (choice) {
            case "1" -> equipment.allEquipment().forEach(this::print);
            case "2" -> {
                Rental rental = rentals.rent(ask("Id klienta (np. K1)"), ask("Id sprzętu (np. E1)"),
                        askDate("Data odbioru"), askDate("Data zwrotu"));
                out.println("Utworzono wypożyczenie " + rental.id());
            }
            case "3" -> out.println("Do zapłaty: "
                    + rentals.returnEquipment(ask("Nr wypożyczenia (np. W1)"), askDate("Data zwrotu")));
            case "4" -> rentals.activeRentals().forEach(this::print);
            case "5" -> {
                equipment.sendToService(ask("Id sprzętu (np. E1)"));
                out.println("Sprzęt przekazany do serwisu");
            }
            case "6" -> {
                equipment.finishService(ask("Id sprzętu (np. E1)"));
                out.println("Sprzęt ponownie dostępny");
            }
            case "7" -> customers.allCustomers().forEach(this::print);
            default -> out.println("Nieznana opcja");
        }
    }

    private String ask(String prompt) {
        out.print(prompt + ": ");
        return in.hasNextLine() ? in.nextLine().trim() : "0";
    }

    private LocalDate askDate(String prompt) {
        return LocalDate.parse(ask(prompt + " (RRRR-MM-DD)"));
    }

    private void print(Equipment item) {
        out.println(item.id() + "  " + item.name() + "  " + item.dailyRate() + "/dzień  " + item.status());
    }

    private void print(Customer customer) {
        out.println(customer.id() + "  " + customer.name());
    }

    private void print(Rental rental) {
        out.println(rental.id() + "  klient " + rental.customerId() + "  sprzęt " + rental.equipmentId()
                + "  " + rental.period().start() + " do " + rental.period().end());
    }
}
