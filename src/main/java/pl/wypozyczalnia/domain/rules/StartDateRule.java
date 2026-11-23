package pl.wypozyczalnia.domain.rules;

import pl.wypozyczalnia.domain.DomainException;

/** Wypożyczenie nie może zaczynać się w przeszłości. */
public class StartDateRule extends RentalRule {

    @Override
    protected void verify(RentalRequest request) {
        if (request.period().start().isBefore(request.today())) {
            throw new DomainException("Data odbioru nie może być w przeszłości");
        }
    }
}
