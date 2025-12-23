package de.warehouse.domain.ports;

import de.warehouse.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(Long id);
    List<Order> findByStoreCode(String storeCode);
}
