package pl.wypozyczalnia.domain.pricing;

/** Fabryka: wybiera cennik odpowiedni do długości wypożyczenia. */
public final class PricingPolicyFactory {

    public static final int WEEK = 7;

    private PricingPolicyFactory() {
    }

    public static PricingPolicy forDays(long days) {
        return days >= WEEK ? new WeeklyDiscountPricing() : new StandardPricing();
    }
}
