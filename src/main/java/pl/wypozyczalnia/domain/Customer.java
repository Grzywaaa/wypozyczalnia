package pl.wypozyczalnia.domain;

import java.util.Objects;

/** Klient wypożyczalni. */
public class Customer implements Entity {

    private final String id;
    private final String name;

    public Customer(String id, String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Imię i nazwisko klienta jest wymagane");
        }
        this.id = Objects.requireNonNull(id);
        this.name = name;
    }

    @Override
    public String id() {
        return id;
    }

    public String name() {
        return name;
    }
}
