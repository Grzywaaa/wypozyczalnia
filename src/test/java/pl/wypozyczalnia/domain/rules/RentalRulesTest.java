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

    @Test
    void allowsCustomerBelowLimit() {
        new CustomerLimitRule(3).check(request(2, today, 1));
    }

    @Test
    void blocksCustomerAtLimit() {
        assertThrows(DomainException.class, () -> new CustomerLimitRule(3).check(request(3, today, 1)));
    }

    @Test
    void allowsStartToday() {
        new StartDateRule().check(request(0, today, 1));
    }

    @Test
    void blocksStartInPast() {
        assertThrows(DomainException.class, () -> new StartDateRule().check(request(0, today.minusDays(1), 1)));
    }

    @Test
    void chainChecksEveryRule() {
        RentalRule chain = new StartDateRule();
        chain.then(new MaxPeriodRule(14)).then(new CustomerLimitRule(3));

        assertThrows(DomainException.class, () -> chain.check(request(3, today, 1)));
    }
}
