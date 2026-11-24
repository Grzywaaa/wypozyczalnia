package pl.wypozyczalnia.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import pl.wypozyczalnia.domain.Entity;
import pl.wypozyczalnia.domain.NotFoundException;

class InMemoryRepositoryTest {

    private record Item(String id) implements Entity {
    }

    private final InMemoryRepository<Item> repository = new InMemoryRepository<>();

    @Test
    void findsSavedEntityById() {
        repository.save(new Item("A"));
        assertEquals(new Item("A"), repository.findById("A").orElseThrow());
    }

    @Test
    void returnsEmptyOptionalForUnknownId() {
        assertTrue(repository.findById("X").isEmpty());
    }

    @Test
    void requireThrowsNotFoundForUnknownId() {
        NotFoundException error = assertThrows(NotFoundException.class, () -> repository.require("X", "element"));
        assertEquals("Nie znaleziono: element X", error.getMessage());
    }

    @Test
    void returnsCopyThatCannotBeModified() {
        repository.save(new Item("A"));
        List<Item> all = repository.findAll();
        assertThrows(UnsupportedOperationException.class, () -> all.add(new Item("B")));
    }

    @Test
    void filtersByCondition() {
        repository.save(new Item("A"));
        repository.save(new Item("B"));
        assertEquals(List.of(new Item("B")), repository.findAll(item -> item.id().equals("B")));
    }

    @Test
    void createsRepositoryFilledWithGivenEntities() {
        // given
        List<Item> items = List.of(new Item("A"), new Item("B"));

        // when
        InMemoryRepository<Item> filled = InMemoryRepository.of(items);

        // then
        assertEquals(items, filled.findAll());
    }

    @Test
    void createsEmptyRepositoryFromEmptyList() {
        // when
        InMemoryRepository<Item> empty = InMemoryRepository.of(List.<Item>of());

        // then
        assertTrue(empty.findAll().isEmpty());
    }
}
