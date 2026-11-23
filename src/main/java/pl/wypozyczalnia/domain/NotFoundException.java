package pl.wypozyczalnia.domain;

/** Brak obiektu domeny o podanym identyfikatorze. */
public class NotFoundException extends DomainException {

    public NotFoundException(String what, String id) {
        super("Nie znaleziono: " + what + " " + id);
    }
}
