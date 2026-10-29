package pl.wypozyczalnia.domain.pricing;

import pl.wypozyczalnia.domain.Money;

/** Cena zwykła: stawka dzienna razy liczba dni. */
public class StandardPricing implements PricingPolicy {

    @Override
    public Money price(Money dailyRate, long days) {
        return dailyRate.times(days);
    }
}
