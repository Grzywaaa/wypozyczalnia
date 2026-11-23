package pl.wypozyczalnia.application;

import java.util.List;
import pl.wypozyczalnia.domain.Equipment;
import pl.wypozyczalnia.domain.NotFoundException;
import pl.wypozyczalnia.domain.Repository;

/** Przypadki użycia: przegląd sprzętu oraz obsługa serwisu. */
public class EquipmentService {

    private final Repository<Equipment> equipment;

    public EquipmentService(Repository<Equipment> equipment) {
        this.equipment = equipment;
    }

    public List<Equipment> allEquipment() {
        return equipment.findAll();
    }

    public void sendToService(String equipmentId) {
        Equipment item = find(equipmentId);
        item.sendToService();
        equipment.save(item);
    }

    public void finishService(String equipmentId) {
        Equipment item = find(equipmentId);
        item.finishService();
        equipment.save(item);
    }

    private Equipment find(String equipmentId) {
        return equipment.findById(equipmentId).orElseThrow(() -> new NotFoundException("sprzęt", equipmentId));
    }
}
