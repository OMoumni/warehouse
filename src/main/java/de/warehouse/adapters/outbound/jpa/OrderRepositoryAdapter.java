package de.warehouse.adapters.outbound.jpa;

import de.warehouse.domain.model.Order;
import de.warehouse.domain.ports.OrderRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderEntityRepository repo;

    public OrderRepositoryAdapter(OrderEntityRepository repo) {
        this.repo = repo;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = OrderEntity.fromDomain(order);
        repo.persist(entity);
        order.setId(entity.id);
        return order;
    }

    @Override
    public Optional<Order> findById(Long id) {
        OrderEntity entity = repo.find(id);
        return entity == null ? Optional.empty() : Optional.of(entity.toDomain());
    }
}
