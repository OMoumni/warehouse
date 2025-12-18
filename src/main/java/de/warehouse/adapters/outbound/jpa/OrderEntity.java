package de.warehouse.adapters.outbound.jpa;

import de.warehouse.domain.model.Order;
import de.warehouse.domain.model.OrderStatus;
import de.warehouse.domain.model.Priority;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String storeCode;

    @Enumerated(EnumType.STRING)
    public Priority priority;

    @Enumerated(EnumType.STRING)
    public OrderStatus status;

    public Instant createdAt;

    public static OrderEntity fromDomain(Order order) {
        OrderEntity e = new OrderEntity();
        e.id = order.getId();
        e.storeCode = order.getStoreCode();
        e.priority = order.getPriority();
        e.status = order.getStatus();
        e.createdAt = order.getCreatedAt();
        return e;
    }

    public Order toDomain() {
        Order order = new Order(id, storeCode, priority);
        order.setId(id);
        return order;
    }
}
