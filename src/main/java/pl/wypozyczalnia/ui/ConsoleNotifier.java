package pl.wypozyczalnia.ui;

import java.io.PrintStream;
import pl.wypozyczalnia.application.RentalListener;
import pl.wypozyczalnia.domain.Money;
import pl.wypozyczalnia.domain.Rental;

/** Wypisuje powiadomienia o wypożyczeniach na konsolę. */
public class ConsoleNotifier implements RentalListener {

    private final PrintStream out;

    public ConsoleNotifier(PrintStream out) {
        this.out = out;
    }

    @Override
    public void rented(Rental rental) {
        out.println("[powiadomienie] Klient " + rental.customerId() + " wypożyczył " + rental.equipmentId()
                + " do " + rental.period().end() + " (nr " + rental.id() + ")");
    }

    @Override
    public void returned(Rental rental, Money fee) {
        out.println("[powiadomienie] Zwrot " + rental.id() + ", do zapłaty: " + fee);
    }
}
