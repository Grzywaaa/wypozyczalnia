package pl.wypozyczalnia.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.wypozyczalnia.domain.Customer;
import pl.wypozyczalnia.domain.DomainException;
import pl.wypozyczalnia.domain.Equipment;
import pl.wypozyczalnia.domain.EquipmentStatus;
import pl.wypozyczalnia.domain.Money;
import pl.wypozyczalnia.domain.Rental;
import pl.wypozyczalnia.domain.rules.RentalRules;
import pl.wypozyczalnia.infrastructure.InMemoryRepository;

/** Scenariusze BDD opisane w dokumentacji. */
class RentalScenariosTest {

    private final LocalDate today = LocalDate.of(2026, 12, 7);
    private final InMemoryRepository<Equipment> equipment = new InMemoryRepository<>();
    private RentalService rentalService;
    private EquipmentService equipmentService;

    @BeforeEach
    void setUp() {
        InMemoryRepository<Customer> customers = new InMemoryRepository<>();
        customers.save(new Customer("K1", "Jan Kowalski"));
        for (int i = 1; i <= 4; i++) {
            equipment.save(new Equipment("E" + i, "Sprzęt " + i, Money.of("25")));
        }
        Clock clock = Clock.fixed(today.atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

        rentalService = new RentalService(equipment, customers, new InMemoryRepository<>(),
                RentalRules.standard(), clock);
        equipmentService = new EquipmentService(equipment);
    }

    @Test
    @DisplayName("Klient z trzema aktywnymi wypożyczeniami nie może wypożyczyć czwartego sprzętu")
    void customerWithThreeActiveRentalsCannotRentFourth() {
        // given
        rentalService.rent("K1", "E1", today, today.plusDays(1));
        rentalService.rent("K1", "E2", today, today.plusDays(1));
        rentalService.rent("K1", "E3", today, today.plusDays(1));

        // when
        DomainException error = assertThrows(DomainException.class,
                () -> rentalService.rent("K1", "E4", today, today.plusDays(1)));

        // then
        assertEquals("Przekroczony limit 3 aktywnych wypożyczeń", error.getMessage());
        assertEquals(EquipmentStatus.AVAILABLE, equipment.findById("E4").orElseThrow().status());
    }

    @Test
    @DisplayName("Zwrot dwa dni po terminie dolicza po 150% stawki za każdy dzień spóźnienia")
    void lateReturnIsChargedWithLateFee() {
        // given
        Rental rental = rentalService.rent("K1", "E1", today, today.plusDays(3));

        // when
        Money fee = rentalService.returnEquipment(rental.id(), today.plusDays(5));

        // then
        assertEquals(Money.of("150"), fee);
    }

    @Test
    @DisplayName("Sprzętu przebywającego w serwisie nie można wypożyczyć")
    void equipmentInServiceCannotBeRented() {
        // given
        equipmentService.sendToService("E1");

        // when
        DomainException error = assertThrows(DomainException.class,
                () -> rentalService.rent("K1", "E1", today, today.plusDays(1)));

        // then
        assertEquals("Sprzęt nie jest dostępny: Sprzęt 1", error.getMessage());
    }

    @Test
    @DisplayName("Wypożyczenie na tydzień lub dłużej dostaje 10% rabatu")
    void weekLongRentalGetsDiscount() {
        // given
        Rental rental = rentalService.rent("K1", "E1", today, today.plusDays(7));

        // when
        Money fee = rentalService.returnEquipment(rental.id(), today.plusDays(7));

        // then
        assertEquals(Money.of("157.50"), fee);
    }

    @Test
    @DisplayName("Wcześniejszy zwrot kosztuje tylko za dni faktycznie wykorzystane")
    void earlyReturnIsChargedForDaysActuallyUsed() {
        // given
        Rental rental = rentalService.rent("K1", "E1", today, today.plusDays(6));

        // when
        Money fee = rentalService.returnEquipment(rental.id(), today.plusDays(2));

        // then
        assertEquals(Money.of("50"), fee);
    }
}
