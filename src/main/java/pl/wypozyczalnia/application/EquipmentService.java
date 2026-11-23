package pl.wypozyczalnia.application;

import java.util.List;
import pl.wypozyczalnia.domain.Equipment;
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
        Equipment item = equipment.require(equipmentId, "sprzęt");
        item.sendToService();
        equipment.save(item);
    }

    public void finishService(String equipmentId) {
        Equipment item = equipment.require(equipmentId, "sprzęt");
        item.finishService();
        equipment.save(item);
    }
}
