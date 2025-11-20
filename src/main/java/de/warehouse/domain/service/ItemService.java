package de.warehouse.domain.service;

import de.warehouse.domain.model.Item;
import de.warehouse.domain.ports.ItemRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ItemService {

    private final ItemRepositoryPort repo;

    // 👇 Quarkus benutzt diesen Konstruktor (CDI),
    //    und dein Test kann ihn auch direkt aufrufen.
    @Inject
    public ItemService(ItemRepositoryPort repo) {
        this.repo = repo;
    }

    public Item create(String sku, String name, String unit, String defaultLocation) {
        if (repo.existsBySku(sku)) {
            throw new DuplicateSkuException("SKU already exists: " + sku);
        }
        return repo.save(new Item(sku, name, unit, defaultLocation));
    }

    public void deleteById(Long id) {
        if (!repo.existsById(id)) {
            throw new IllegalArgumentException("Item not found: " + id);
        }
        repo.deleteById(id);
    }


    public static class DuplicateSkuException extends RuntimeException {
        public DuplicateSkuException(String msg) { super(msg); }
    }
}
