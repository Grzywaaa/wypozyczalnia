package pl.wypozyczalnia.domain;

import java.util.Objects;

/** Sprzęt do wypożyczenia. Sam pilnuje dozwolonych zmian swojego stanu. */
public class Equipment implements Entity {

    private final String id;
    private final String name;
    private final Money dailyRate;
    private EquipmentStatus status = EquipmentStatus.AVAILABLE;

    public Equipment(String id, String name, Money dailyRate) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Nazwa sprzętu jest wymagana");
        }
        this.id = Objects.requireNonNull(id);
        this.name = name;
        this.dailyRate = Objects.requireNonNull(dailyRate);
    }

    public void rent() {
        changeStatus(EquipmentStatus.AVAILABLE, EquipmentStatus.RENTED, "Sprzęt nie jest dostępny");
    }

    public void giveBack() {
        changeStatus(EquipmentStatus.RENTED, EquipmentStatus.AVAILABLE, "Sprzęt nie jest wypożyczony");
    }

    private void changeStatus(EquipmentStatus required, EquipmentStatus next, String error) {
        if (status != required) {
            throw new DomainException(error + ": " + name);
        }
        status = next;
    }

    @Override
    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Money dailyRate() {
        return dailyRate;
    }

    public EquipmentStatus status() {
        return status;
    }
}
