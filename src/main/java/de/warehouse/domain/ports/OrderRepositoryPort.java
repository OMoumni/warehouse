package de.warehouse.domain.ports;

import de.warehouse.domain.model.Order;

import java.util.Optional;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(Long id);
}
