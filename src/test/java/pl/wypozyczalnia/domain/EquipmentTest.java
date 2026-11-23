package pl.wypozyczalnia.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class EquipmentTest {

    private final Equipment drill = new Equipment("E1", "Wiertarka", Money.of("25"));

    @Test
    void availableEquipmentCanBeRented() {
        drill.rent();
        assertEquals(EquipmentStatus.RENTED, drill.status());
    }

    @Test
    void rentedEquipmentCannotBeRentedAgain() {
        drill.rent();
        assertThrows(DomainException.class, drill::rent);
    }

    @Test
    void rentedEquipmentCanBeGivenBack() {
        drill.rent();
        drill.giveBack();
        assertEquals(EquipmentStatus.AVAILABLE, drill.status());
    }

    @Test
    void notRentedEquipmentCannotBeGivenBack() {
        assertThrows(DomainException.class, drill::giveBack);
    }

    @Test
    void requiresName() {
        assertThrows(DomainException.class, () -> new Equipment("E2", " ", Money.of("10")));
    }
}
