package de.warehouse.domain.service;

import de.warehouse.domain.model.Item;
import de.warehouse.domain.ports.ItemRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ItemServiceTest {

    // Fake repository ausschließlich für Tests
    static class FakeRepo implements ItemRepositoryPort {

        // Map<Long, Item> für ID-basiertes Speichern
        private final Map<Long, Item> items = new HashMap<>();
        private long idCounter = 1;

        @Override
        public boolean existsBySku(String sku) {
            return items.values().stream()
                    .anyMatch(i -> i.getSku().equals(sku));
        }

        @Override
        public Item save(Item i) {
            items.put(idCounter++, i);
            return i;
        }

        @Override
        public boolean existsById(Long id) {
            return items.containsKey(id);
        }

        @Override
        public void deleteById(Long id) {
            items.remove(id);
        }
    }

    // --- TESTS ---

    @Test
    void creatingDuplicateSkuFails() {
        var service = new ItemService(new FakeRepo());

        service.create("A123", "Bananas 1kg", "Box", "R1-A");

        assertThrows(ItemService.DuplicateSkuException.class, () ->
                service.create("A123", "Bananas 1kg", "Box", "R1-A")
        );
    }

    @Test
    void deletingNonExistingIdFails() {
        var service = new ItemService(new FakeRepo());

        assertThrows(IllegalArgumentException.class, () ->
                service.deleteById(999L)
        );
    }

    @Test
    void deleteExistingIdWorks() {
        var repo = new FakeRepo();
        var service = new ItemService(repo);

        // Speichere Item → erhält ID = 1
        repo.save(new Item("X1", "Test", "Unit", "Loc"));

        assertTrue(repo.existsById(1L));

        service.deleteById(1L);

        assertFalse(repo.existsById(1L));
    }
}
