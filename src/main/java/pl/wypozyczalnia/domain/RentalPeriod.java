package pl.wypozyczalnia.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/** Okres wypożyczenia: od dnia odbioru do planowanego dnia zwrotu. */
public record RentalPeriod(LocalDate start, LocalDate end) {

    public RentalPeriod {
        Objects.requireNonNull(start, "start");
        Objects.requireNonNull(end, "end");
        if (!end.isAfter(start)) {
            throw new DomainException("Data zwrotu musi być późniejsza niż data odbioru");
        }
    }

    public long days() {
        return ChronoUnit.DAYS.between(start, end);
    }

    public long daysLate(LocalDate returnDate) {
        return Math.max(0, ChronoUnit.DAYS.between(end, returnDate));
    }
}
