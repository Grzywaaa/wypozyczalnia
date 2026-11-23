package pl.wypozyczalnia.domain.rules;

import pl.wypozyczalnia.domain.DomainException;

/** Wypożyczenie nie może trwać dłużej niż ustalona liczba dni. */
public class MaxPeriodRule extends RentalRule {

    private final int maxDays;

    public MaxPeriodRule(int maxDays) {
        this.maxDays = maxDays;
    }

    @Override
    protected void verify(RentalRequest request) {
        if (request.period().days() > maxDays) {
            throw new DomainException("Maksymalny okres wypożyczenia to " + maxDays + " dni");
        }
    }
}
