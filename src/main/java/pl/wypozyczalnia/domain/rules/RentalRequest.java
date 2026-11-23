package pl.wypozyczalnia.domain.rules;

import java.time.LocalDate;
import pl.wypozyczalnia.domain.RentalPeriod;

/** Dane potrzebne do sprawdzenia, czy klient może wypożyczyć sprzęt. */
public record RentalRequest(long activeRentals, RentalPeriod period, LocalDate today) {
}
