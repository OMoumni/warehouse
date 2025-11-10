package de.warehouse.domain.ports;

import de.warehouse.domain.model.Item;

public interface ItemRepositoryPort {
    boolean existsBySku(String sku);
    Item save(Item item);
}
