package de.warehouse.domain.model;

import java.time.Instant;

public class Order {

    private Long id;
    private final String storeCode;
    private final Priority priority;
    private OrderStatus status;
    private final Instant createdAt;

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
}
