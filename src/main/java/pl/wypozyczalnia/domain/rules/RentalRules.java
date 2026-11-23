package pl.wypozyczalnia.domain.rules;

/** Regulamin wypożyczalni: jedno miejsce, w którym składany jest łańcuch reguł. */
public final class RentalRules {

    public static final int MAX_DAYS = 14;
    public static final int MAX_ACTIVE_RENTALS = 3;

    private RentalRules() {
    }

    public static RentalRule standard() {
        RentalRule chain = new StartDateRule();
        chain.then(new MaxPeriodRule(MAX_DAYS)).then(new CustomerLimitRule(MAX_ACTIVE_RENTALS));
        return chain;
    }
}
