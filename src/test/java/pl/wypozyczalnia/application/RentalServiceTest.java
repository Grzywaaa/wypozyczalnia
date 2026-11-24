package pl.wypozyczalnia.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.wypozyczalnia.domain.Customer;
import pl.wypozyczalnia.domain.DomainException;
import pl.wypozyczalnia.domain.Equipment;
import pl.wypozyczalnia.domain.EquipmentStatus;
import pl.wypozyczalnia.domain.Money;
import pl.wypozyczalnia.domain.NotFoundException;
import pl.wypozyczalnia.domain.Rental;
import pl.wypozyczalnia.domain.rules.RentalRules;
import pl.wypozyczalnia.infrastructure.InMemoryRepository;

class RentalServiceTest {

    private final LocalDate today = LocalDate.of(2026, 12, 7);
    private final InMemoryRepository<Equipment> equipment = new InMemoryRepository<>();
    private final InMemoryRepository<Rental> rentals = new InMemoryRepository<>();
    private final List<String> events = new ArrayList<>();
    private RentalService service;

    @BeforeEach
    void setUp() {
        InMemoryRepository<Customer> customers = new InMemoryRepository<>();
        customers.save(new Customer("K1", "Jan Kowalski"));
        equipment.save(new Equipment("E1", "Wiertarka", Money.of("25")));

        Clock clock = Clock.fixed(today.atStartOfDay().toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

        service = new RentalService(equipment, customers, rentals, RentalRules.standard(), clock);
        service.addListener(new RentalListener() {
            public void rented(Rental rental) {
                events.add("rented " + rental.id());
            }

            public void returned(Rental rental, Money fee) {
                events.add("returned " + rental.id() + " " + fee);
            }
        });
    }

    @Test
    void rentsAvailableEquipmentAndNotifiesListener() {
        Rental rental = service.rent("K1", "E1", today, today.plusDays(2));

        assertEquals(EquipmentStatus.RENTED, equipment.findById("E1").orElseThrow().status());
        assertEquals(rental, rentals.findById(rental.id()).orElseThrow());
        assertEquals(List.of("rented W1"), events);
    }

    @Test
    void rejectsUnknownCustomer() {
        assertThrows(NotFoundException.class, () -> service.rent("K9", "E1", today, today.plusDays(1)));
    }

    @Test
    void rejectsRentalBrokenByRules() {
        assertThrows(DomainException.class, () -> service.rent("K1", "E1", today, today.plusDays(20)));
        assertEquals(EquipmentStatus.AVAILABLE, equipment.findById("E1").orElseThrow().status());
    }

    @Test
    void returnMakesEquipmentAvailableAndChargesFee() {
        Rental rental = service.rent("K1", "E1", today, today.plusDays(2));

        Money fee = service.returnEquipment(rental.id(), today.plusDays(2));

        assertEquals(Money.of("50"), fee);
        assertEquals(EquipmentStatus.AVAILABLE, equipment.findById("E1").orElseThrow().status());
        assertEquals(List.of(), service.activeRentals());
        assertEquals(List.of("rented W1", "returned W1 50.00 zł"), events);
    }

    @Test
    void weekLongRentalGetsDiscount() {
        Rental rental = service.rent("K1", "E1", today, today.plusDays(7));

        assertEquals(Money.of("157.50"), service.returnEquipment(rental.id(), today.plusDays(7)));
    }

    @Test
    void earlyReturnOfWeekLongRentalLosesDiscount() {
        Rental rental = service.rent("K1", "E1", today, today.plusDays(7));

        assertEquals(Money.of("75"), service.returnEquipment(rental.id(), today.plusDays(3)));
    }

    @Test
    void rejectsUnknownRental() {
        assertThrows(NotFoundException.class, () -> service.returnEquipment("W9", today));
    }
}
