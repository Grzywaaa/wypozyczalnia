package pl.wypozyczalnia.domain;

/** Naruszenie reguły biznesowej. */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }
}
