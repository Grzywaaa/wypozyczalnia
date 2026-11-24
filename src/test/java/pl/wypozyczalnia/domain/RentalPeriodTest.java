package pl.wypozyczalnia.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class RentalPeriodTest {

    private final LocalDate monday = LocalDate.of(2026, 12, 7);

    @Test
    void countsDays() {
        assertEquals(3, new RentalPeriod(monday, monday.plusDays(3)).days());
    }

    @Test
    void rejectsEndNotAfterStart() {
        assertThrows(DomainException.class, () -> new RentalPeriod(monday, monday));
    }

    @Test
    void chargesOnlyDaysActuallyUsedWhenReturnedEarly() {
        RentalPeriod period = new RentalPeriod(monday, monday.plusDays(6));
        assertEquals(2, period.chargedDays(monday.plusDays(2)));
    }

    @Test
    void chargesAtLeastOneDayWhenReturnedSameDay() {
        RentalPeriod period = new RentalPeriod(monday, monday.plusDays(6));
        assertEquals(1, period.chargedDays(monday));
    }

    @Test
    void chargesAtMostPlannedDaysWhenReturnedLate() {
        RentalPeriod period = new RentalPeriod(monday, monday.plusDays(3));
        assertEquals(3, period.chargedDays(monday.plusDays(5)));
    }

    @Test
    void countsDaysLateOnlyAfterEnd() {
        RentalPeriod period = new RentalPeriod(monday, monday.plusDays(3));
        assertEquals(0, period.daysLate(monday.plusDays(2)));
        assertEquals(2, period.daysLate(monday.plusDays(5)));
    }
}
