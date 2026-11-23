package pl.wypozyczalnia.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Kwota w złotych. Obiekt wartości, niemutowalny. */
public record Money(BigDecimal amount) {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money {
        Objects.requireNonNull(amount, "amount");
        if (amount.signum() < 0) {
            throw new DomainException("Kwota nie może być ujemna");
        }
        amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Money of(String value) {
        return new Money(new BigDecimal(value));
    }

    public Money plus(Money other) {
        return new Money(amount.add(other.amount));
    }

    public Money times(BigDecimal factor) {
        return new Money(amount.multiply(factor));
    }

    public Money times(long count) {
        return times(BigDecimal.valueOf(count));
    }

    @Override
    public String toString() {
        return amount + " zł";
    }
}
