package pl.wypozyczalnia.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    void addsAmounts() {
        assertEquals(Money.of("12.50"), Money.of("10").plus(Money.of("2.5")));
    }

    @Test
    void multipliesByFactorAndRoundsToGrosze() {
        assertEquals(Money.of("33.33"), Money.of("100").times(new BigDecimal("0.33333")));
    }

    @Test
    void rejectsNegativeAmount() {
        assertThrows(DomainException.class, () -> Money.of("-1"));
    }
}
