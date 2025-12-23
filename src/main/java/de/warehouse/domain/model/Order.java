package de.warehouse.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
public class Order {

    private Long id;
    private final String storeCode;
    private final Priority priority;
    private OrderStatus status;
    private final Instant createdAt;
    private final List<OrderLine> lines = new ArrayList<>();

    public Order(Long id, String storeCode, Priority priority) {
        this.id = id;
        this.storeCode = storeCode;
        this.priority = priority;
        this.status = OrderStatus.CREATED;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getStoreCode() { return storeCode; }
    public Priority getPriority() { return priority; }
    public OrderStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }

    public void addItem(Long itemId, int quantity) {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("cannot add items when status is " + status);
        }
        lines.add(new OrderLine(itemId, quantity));
    }

    public List<OrderLine> getLines() {
        return List.copyOf(lines);
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

}
