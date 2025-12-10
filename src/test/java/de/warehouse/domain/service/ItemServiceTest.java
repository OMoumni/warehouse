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

        private final Map<Long, Item> items = new HashMap<>();
        private long idCounter = 1;

        @Override
        public boolean existsBySku(String sku) {
            return items.values().stream()
                    .anyMatch(i -> i.getSku().equals(sku));
        }

        @Override
        public Item save(Item item) {
            if (item.getId() == null) {
                item.setId(Long.valueOf(idCounter++));
            }
            items.put(item.getId(), item);
            return item;
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

        Item item = new Item("X1", "Test", "Unit", "Loc");
        repo.save(item);

        Long id = item.getId();

        assertNotNull(id);
        assertTrue(repo.existsById(id));

        service.deleteById(id);

        assertFalse(repo.existsById(id));
    }
}
