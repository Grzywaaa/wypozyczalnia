package pl.wypozyczalnia.domain.pricing;

import java.math.BigDecimal;
import pl.wypozyczalnia.domain.Money;

/** Cena z rabatem 10% dla wypożyczeń tygodniowych i dłuższych. */
public class WeeklyDiscountPricing implements PricingPolicy {

    private static final BigDecimal DISCOUNT_FACTOR = new BigDecimal("0.90");

    @Override
    public Money price(Money dailyRate, long days) {
        return dailyRate.times(days).times(DISCOUNT_FACTOR);
    }
}
