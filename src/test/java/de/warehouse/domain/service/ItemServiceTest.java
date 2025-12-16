package de.warehouse.domain.service;

import de.warehouse.application.ItemService;
import de.warehouse.domain.model.Item;
import de.warehouse.domain.ports.ItemRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.*;
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
                item.setId(idCounter++);
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

        @Override
        public Optional<Item> findById(Long id) {
            return Optional.ofNullable(items.get(id));
        }

        @Override
        public List<Item> findAll(int offset, int limit) {
            return items.values().stream()
                    .sorted(Comparator.comparing(Item::getId))
                    .skip(offset)
                    .limit(limit)
                    .toList();
        }
    }

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

        assertThrows(ItemService.ItemNotFoundException.class, () ->
                service.deleteById(999L)
        );
    }
}
