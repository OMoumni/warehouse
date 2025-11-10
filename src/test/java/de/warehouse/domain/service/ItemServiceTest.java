package de.warehouse.domain.service;

import de.warehouse.domain.model.Item;
import de.warehouse.domain.ports.ItemRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemServiceTest {

    static class FakeRepo implements ItemRepositoryPort {
        private final Set<String> skus = new HashSet<>();

        @Override public boolean existsBySku(String sku) { return skus.contains(sku); }

        @Override public Item save(Item i) {
            skus.add(i.getSku());
            return i;
        }
    }

    @Test
    void creatingDuplicateSkuFails() {
        var service = new ItemService(new FakeRepo()); // <— KEIN zweiter Parameter!

        service.create("A123", "Bananas 1kg", "Box", "R1-A");

        assertThrows(ItemService.DuplicateSkuException.class, () ->
                service.create("A123", "Bananas 1kg", "Box", "R1-A")
        );
    }
}
