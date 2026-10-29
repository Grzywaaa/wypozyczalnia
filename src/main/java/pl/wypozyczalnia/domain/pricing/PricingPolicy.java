package pl.wypozyczalnia.domain.pricing;

import pl.wypozyczalnia.domain.Money;

/** Strategia: sposób wyliczenia ceny za okres wypożyczenia. */
public interface PricingPolicy {

    Money price(Money dailyRate, long days);
}
