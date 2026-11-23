package pl.wypozyczalnia.domain.rules;

import pl.wypozyczalnia.domain.DomainException;

/** Klient może mieć jednocześnie tylko ograniczoną liczbę aktywnych wypożyczeń. */
public class CustomerLimitRule extends RentalRule {

    private final int maxActiveRentals;

    public CustomerLimitRule(int maxActiveRentals) {
        this.maxActiveRentals = maxActiveRentals;
    }

    @Override
    protected void verify(RentalRequest request) {
        if (request.activeRentals() >= maxActiveRentals) {
            throw new DomainException("Przekroczony limit " + maxActiveRentals + " aktywnych wypożyczeń");
        }
    }
}
