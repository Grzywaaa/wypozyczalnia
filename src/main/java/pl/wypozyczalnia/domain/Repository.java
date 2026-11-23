package pl.wypozyczalnia.domain;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/** Generyczne repozytorium encji domeny. */
public interface Repository<T extends Entity> {

    void save(T entity);

    Optional<T> findById(String id);

    List<T> findAll();

    default List<T> findAll(Predicate<? super T> condition) {
        return findAll().stream().filter(condition).toList();
    }
}
