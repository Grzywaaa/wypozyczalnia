package pl.wypozyczalnia.application;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import pl.wypozyczalnia.domain.Customer;
import pl.wypozyczalnia.domain.Equipment;
import pl.wypozyczalnia.domain.Money;
import pl.wypozyczalnia.domain.Rental;
import pl.wypozyczalnia.domain.RentalPeriod;
import pl.wypozyczalnia.domain.Repository;
import pl.wypozyczalnia.domain.pricing.PricingPolicyFactory;
import pl.wypozyczalnia.domain.rules.RentalRequest;
import pl.wypozyczalnia.domain.rules.RentalRule;

/** Przypadki użycia: wypożyczenie i zwrot sprzętu. */
public class RentalService {

    private final Repository<Equipment> equipment;
    private final Repository<Customer> customers;
    private final Repository<Rental> rentals;
    private final RentalRule rules;
    private final Clock clock;
    private final List<RentalListener> listeners = new ArrayList<>();

    public RentalService(Repository<Equipment> equipment, Repository<Customer> customers,
                         Repository<Rental> rentals, RentalRule rules, Clock clock) {
        this.equipment = equipment;
        this.customers = customers;
        this.rentals = rentals;
        this.rules = rules;
        this.clock = clock;
    }

    public void addListener(RentalListener listener) {
        listeners.add(listener);
    }

    public Rental rent(String customerId, String equipmentId, LocalDate start, LocalDate end) {
        customers.require(customerId, "klient");
        Equipment item = equipment.require(equipmentId, "sprzęt");
        RentalPeriod period = new RentalPeriod(start, end);
        long active = rentals.findAll(rental -> rental.isActiveFor(customerId)).size();

        rules.check(new RentalRequest(active, period, LocalDate.now(clock)));
        item.rent();

        Rental rental = new Rental(nextId(), customerId, equipmentId, item.dailyRate(), period);
        equipment.save(item);
        rentals.save(rental);
        listeners.forEach(listener -> listener.rented(rental));
        return rental;
    }

    public Money returnEquipment(String rentalId, LocalDate returnDate) {
        Rental rental = rentals.require(rentalId, "wypożyczenie");
        Equipment item = equipment.require(rental.equipmentId(), "sprzęt");

        Money fee = rental.returnOn(returnDate, PricingPolicyFactory.forDays(rental.period().days()));
        item.giveBack();

        equipment.save(item);
        rentals.save(rental);
        listeners.forEach(listener -> listener.returned(rental, fee));
        return fee;
    }

    public List<Rental> activeRentals() {
        return rentals.findAll(Rental::isActive);
    }

    private String nextId() {
        return "W" + (rentals.findAll().size() + 1);
    }
}
