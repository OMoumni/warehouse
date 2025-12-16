package de.warehouse.domain.ports;

import de.warehouse.domain.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepositoryPort {

    Item save(Item item);

    boolean existsBySku(String sku);

    boolean existsById(Long id);

    void deleteById(Long id);
    Optional<Item> findById(Long id);

    List<Item> findAll(int offset, int limit);
}
