package pl.wypozyczalnia.domain;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import pl.wypozyczalnia.domain.pricing.PricingPolicy;

/**
 * Agregat wypożyczenia. Stawka dzienna jest zapamiętana w chwili wypożyczenia,
 * a opłatę można naliczyć tylko raz, przy zwrocie.
 */
public class Rental implements Entity {

    private final String id;
    private final String customerId;
    private final String equipmentId;
    private final Money dailyRate;
    private final RentalPeriod period;
    private RentalStatus status = RentalStatus.ACTIVE;
    private Money fee;

    public Rental(String id, String customerId, String equipmentId, Money dailyRate, RentalPeriod period) {
        this.id = Objects.requireNonNull(id);
        this.customerId = Objects.requireNonNull(customerId);
        this.equipmentId = Objects.requireNonNull(equipmentId);
        this.dailyRate = Objects.requireNonNull(dailyRate);
        this.period = Objects.requireNonNull(period);
    }

    public Money returnOn(LocalDate returnDate, PricingPolicy pricing) {
        if (status == RentalStatus.RETURNED) {
            throw new DomainException("Wypożyczenie " + id + " zostało już zakończone");
        }
        if (returnDate.isBefore(period.start())) {
            throw new DomainException("Data zwrotu nie może być wcześniejsza niż data odbioru");
        }
        fee = pricing.price(dailyRate, period.days());
        status = RentalStatus.RETURNED;
        return fee;
    }

    public boolean isActive() {
        return status == RentalStatus.ACTIVE;
    }

    public boolean isActiveFor(String customer) {
        return isActive() && customerId.equals(customer);
    }

    @Override
    public String id() {
        return id;
    }

    public String customerId() {
        return customerId;
    }

    public String equipmentId() {
        return equipmentId;
    }

    public RentalPeriod period() {
        return period;
    }

    public Optional<Money> fee() {
        return Optional.ofNullable(fee);
    }
}
