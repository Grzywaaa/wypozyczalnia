package pl.wypozyczalnia.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import pl.wypozyczalnia.domain.Equipment;
import pl.wypozyczalnia.domain.EquipmentStatus;
import pl.wypozyczalnia.domain.Money;
import pl.wypozyczalnia.domain.NotFoundException;
import pl.wypozyczalnia.infrastructure.InMemoryRepository;

class EquipmentServiceTest {

    private final InMemoryRepository<Equipment> repository = new InMemoryRepository<>();
    private final EquipmentService service = new EquipmentService(repository);

    @Test
    void sendsAvailableEquipmentToServiceAndBack() {
        repository.save(new Equipment("E1", "Wiertarka", Money.of("25")));

        service.sendToService("E1");
        assertEquals(EquipmentStatus.IN_SERVICE, repository.findById("E1").orElseThrow().status());

        service.finishService("E1");
        assertEquals(EquipmentStatus.AVAILABLE, repository.findById("E1").orElseThrow().status());
    }

    @Test
    void rejectsUnknownEquipment() {
        assertThrows(NotFoundException.class, () -> service.sendToService("E9"));
    }
}
