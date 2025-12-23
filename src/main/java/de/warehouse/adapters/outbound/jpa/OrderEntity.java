package de.warehouse.adapters.outbound.jpa;

import de.warehouse.domain.model.Order;
import de.warehouse.domain.model.OrderStatus;
import de.warehouse.domain.model.Priority;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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

   // public Instant createdAt;

    public static OrderEntity fromDomain(Order order) {
        OrderEntity e = new OrderEntity();
        e.id = order.getId();
        e.storeCode = order.getStoreCode();
        e.priority = order.getPriority();
        e.status = order.getStatus();

        e.lines = order.getLines().stream().map(line -> {
            OrderLineEntity le = new OrderLineEntity();
            le.itemId = line.getItemId();
            le.quantity = line.getQuantity();
            le.order = e;
            return le;
        }).toList();

        return e;
    }

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    public List<OrderLineEntity> lines = new ArrayList<>();

    public Order toDomain() {
        Order order = new Order(id, storeCode, priority);


        order.setStatus(this.status);

        for (OrderLineEntity line : lines) {
            order.addItem(line.itemId, line.quantity);
        }

        return order;
    }

}

