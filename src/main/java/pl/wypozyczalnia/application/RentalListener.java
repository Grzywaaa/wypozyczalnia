package pl.wypozyczalnia.application;

import pl.wypozyczalnia.domain.Money;
import pl.wypozyczalnia.domain.Rental;

/** Obserwator zdarzeń wypożyczalni, np. powiadomienia dla klienta. */
public interface RentalListener {

    void rented(Rental rental);

    void returned(Rental rental, Money fee);
}
