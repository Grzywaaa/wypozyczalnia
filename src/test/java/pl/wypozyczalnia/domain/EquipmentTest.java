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
    void availableEquipmentCanBeSentToService() {
        drill.sendToService();
        assertEquals(EquipmentStatus.IN_SERVICE, drill.status());
    }

    @Test
    void rentedEquipmentCannotBeSentToService() {
        drill.rent();
        assertThrows(DomainException.class, drill::sendToService);
    }

    @Test
    void equipmentInServiceCannotBeRented() {
        drill.sendToService();
        assertThrows(DomainException.class, drill::rent);
    }

    @Test
    void finishedServiceMakesEquipmentAvailable() {
        drill.sendToService();
        drill.finishService();
        assertEquals(EquipmentStatus.AVAILABLE, drill.status());
    }

    @Test
    void cannotFinishServiceOfEquipmentNotInService() {
        assertThrows(DomainException.class, drill::finishService);
    }

    @Test
    void requiresName() {
        assertThrows(DomainException.class, () -> new Equipment("E2", " ", Money.of("10")));
    }
}
