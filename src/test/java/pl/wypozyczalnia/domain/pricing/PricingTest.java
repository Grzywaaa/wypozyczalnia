package pl.wypozyczalnia.domain.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import pl.wypozyczalnia.domain.Money;

class PricingTest {

    @Test
    void standardPriceIsDailyRateTimesDays() {
        assertEquals(Money.of("75"), new StandardPricing().price(Money.of("25"), 3));
    }
}
