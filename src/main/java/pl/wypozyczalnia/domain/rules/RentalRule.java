package pl.wypozyczalnia.domain.rules;

/** Łańcuch zobowiązań: każda reguła sprawdza jedno wymaganie i przekazuje żądanie dalej. */
public abstract class RentalRule {

    private RentalRule next;

    public RentalRule then(RentalRule nextRule) {
        this.next = nextRule;
        return nextRule;
    }

    public void check(RentalRequest request) {
        verify(request);
        if (next != null) {
            next.check(request);
        }
    }

    /** Rzuca DomainException, gdy żądanie łamie regułę. */
    protected abstract void verify(RentalRequest request);
}
