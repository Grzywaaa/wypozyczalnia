package pl.wypozyczalnia.domain.rules;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import pl.wypozyczalnia.domain.DomainException;
import pl.wypozyczalnia.domain.RentalPeriod;

class RentalRulesTest {

    private final LocalDate today = LocalDate.of(2026, 12, 7);

    private RentalRequest request(long activeRentals, LocalDate start, int days) {
        return new RentalRequest(activeRentals, new RentalPeriod(start, start.plusDays(days)), today);
    }

    @Test
    void allowsPeriodUpToMaximum() {
        new MaxPeriodRule(14).check(request(0, today, 14));
    }

    @Test
    void blocksPeriodLongerThanMaximum() {
        assertThrows(DomainException.class, () -> new MaxPeriodRule(14).check(request(0, today, 15)));
    }
}
