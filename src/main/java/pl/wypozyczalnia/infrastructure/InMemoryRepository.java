package pl.wypozyczalnia.infrastructure;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import pl.wypozyczalnia.domain.Entity;
import pl.wypozyczalnia.domain.Repository;

/** Repozytorium w pamięci. Mapa daje szybkie wyszukiwanie po id i zachowuje kolejność dodawania. */
public class InMemoryRepository<T extends Entity> implements Repository<T> {

    private final Map<String, T> items = new LinkedHashMap<>();

    @Override
    public void save(T entity) {
        items.put(entity.id(), entity);
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public List<T> findAll() {
        return List.copyOf(items.values());
    }
}
