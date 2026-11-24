package pl.wypozyczalnia.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import pl.wypozyczalnia.domain.pricing.StandardPricing;

class RentalTest {

    private final LocalDate start = LocalDate.of(2026, 12, 7);
    private final Rental rental = new Rental("W1", "K1", "E1", Money.of("25"),
            new RentalPeriod(start, start.plusDays(3)));

    @Test
    void returnOnTimeChargesPriceForPlannedDays() {
        Money fee = rental.returnOn(start.plusDays(3), new StandardPricing());

        assertEquals(Money.of("75"), fee);
        assertFalse(rental.isActive());
        assertEquals(fee, rental.fee().orElseThrow());
    }

    @Test
    void earlyReturnChargesOnlyDaysActuallyUsed() {
        Money fee = rental.returnOn(start.plusDays(1), new StandardPricing());

        assertEquals(Money.of("25"), fee);
    }

    @Test
    void lateReturnAddsOneAndHalfDailyRateForEachDayLate() {
        Money fee = rental.returnOn(start.plusDays(5), new StandardPricing());

        assertEquals(Money.of("150"), fee);
    }

    @Test
    void cannotReturnTwice() {
        rental.returnOn(start.plusDays(3), new StandardPricing());

        assertThrows(DomainException.class, () -> rental.returnOn(start.plusDays(4), new StandardPricing()));
    }

    @Test
    void cannotReturnBeforeStart() {
        assertThrows(DomainException.class, () -> rental.returnOn(start.minusDays(1), new StandardPricing()));
    }
}
