package de.warehouse.adapters.outbound.jpa;

import de.warehouse.domain.model.Order;
import de.warehouse.domain.ports.OrderRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
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

        OrderEntity managed;
        if (entity.id == null) {
            repo.persist(entity);
            managed = entity;
        } else {
            managed = repo.merge(entity);
        }

        order.setId(managed.id);
        return order;
    }


    @Override
    public Optional<Order> findById(Long id) {
        OrderEntity entity = repo.find(id);
        return entity == null
                ? Optional.empty()
                : Optional.of(entity.toDomain());
    }

    @Override
    public List<Order> findByStoreCode(String storeCode) {
        return repo.findByStoreCode(storeCode)
                .stream()
                .map(OrderEntity::toDomain)
                .toList();
    }

}
