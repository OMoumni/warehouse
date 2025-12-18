package de.warehouse.application;

import de.warehouse.domain.model.Order;
import de.warehouse.domain.model.Priority;
import de.warehouse.domain.ports.OrderRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class OrderService {

    private final OrderRepositoryPort repo;

    public OrderService(OrderRepositoryPort repo) {
        this.repo = repo;
    }

    @Transactional
    public Order create(String storeCode, Priority priority) {
        if (storeCode == null || storeCode.isBlank()) {
            throw new IllegalArgumentException("storeCode must not be blank");
        }

        Order order = new Order(null, storeCode, priority);
        return repo.save(order);
    }
    @Transactional
    public Order getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

}
