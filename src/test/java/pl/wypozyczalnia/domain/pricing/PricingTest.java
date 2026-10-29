package pl.wypozyczalnia.domain.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import pl.wypozyczalnia.domain.Money;

class PricingTest {

    @Test
    void standardPriceIsDailyRateTimesDays() {
        assertEquals(Money.of("75"), new StandardPricing().price(Money.of("25"), 3));
    }

    @Test
    void weeklyPriceHasTenPercentDiscount() {
        assertEquals(Money.of("157.50"), new WeeklyDiscountPricing().price(Money.of("25"), 7));
    }

    @Test
    void factoryChoosesStandardPricingForShortRental() {
        assertTrue(PricingPolicyFactory.forDays(6) instanceof StandardPricing);
    }

    @Test
    void factoryChoosesDiscountForWeekOrLonger() {
        assertTrue(PricingPolicyFactory.forDays(7) instanceof WeeklyDiscountPricing);
    }
}
