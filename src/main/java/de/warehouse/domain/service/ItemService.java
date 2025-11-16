package de.warehouse.domain.service;

import de.warehouse.domain.model.Item;
import de.warehouse.domain.ports.ItemRepositoryPort;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ItemService {

    private final ItemRepositoryPort repo;

    // EIN Konstruktor reicht. Quarkus kann ihn injizieren, und im Unit-Test rufst du ihn manuell auf.
    @Inject
    public ItemService(ItemRepositoryPort repo) {
        this.repo = repo;
    }

    public Item create(String sku, String name, String unit, String defaultLocation) {
        if (sku == null || sku.isBlank()) throw new IllegalArgumentException("SKU required");
        if (repo.existsBySku(sku)) throw new DuplicateSkuException(sku);
        return repo.save(new Item(sku, name, unit, defaultLocation));
    }

    public static class DuplicateSkuException extends RuntimeException {
        public DuplicateSkuException(String sku) { super("SKU already exists: " + sku); }

    }


}
