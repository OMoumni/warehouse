package de.warehouse.domain.service;

import de.warehouse.domain.model.Item;
import de.warehouse.domain.ports.ItemRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class ItemService {

    private final ItemRepositoryPort repo;

    public ItemService(ItemRepositoryPort repository) {
        this.repo = repository;
    }

    @Transactional
    public Item create(String sku, String name, String unit, String defaultLocation) {
        validateCreate(sku, name);

        if (repo.existsBySku(sku)) {
            throw new DuplicateSkuException();
        }

        Item item = new Item(null, sku, name, unit, defaultLocation);
        return repo.save(item);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!repo.existsById(id)) {
            throw new ItemNotFoundException();
        }
        repo.deleteById(id);
    }

    private void validateCreate(String sku, String name) {
        if (sku == null || sku.isBlank()) {
            throw new ValidationException("sku must not be blank");
        }
        if (name == null || name.isBlank()) {
            throw new ValidationException("name must not be blank");
        }
    }

    // Use-Case-spezifische Exceptions

    public static class DuplicateSkuException extends RuntimeException {}
    public static class ItemNotFoundException extends RuntimeException {}
    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
    @Transactional
    public Item getById(Long id) {
        return repo.findById(id).orElseThrow(ItemNotFoundException::new);
    }

    @Transactional
    public List<Item> list(int offset, int limit) {
        if (offset < 0) throw new ValidationException("offset must be >= 0");
        if (limit <= 0 || limit > 200) throw new ValidationException("limit must be between 1 and 200");
        return repo.findAll(offset, limit);
    }
    @Transactional
    public Item update(Long id, String name, String unit, String defaultLocation) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("name must not be blank");
        }

        Item existing = repo.findById(id).orElseThrow(ItemNotFoundException::new);

        Item updated = new Item(
                existing.getId(),
                existing.getSku(), // SKU bleibt unverändert
                name,
                unit,
                defaultLocation
        );

        return repo.save(updated);
    }

}
