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

    public List<OrderLine> getLines() { return lines; }

    public void setId(Long id) { this.id = id; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public void addItem(Long itemId, int qtyRequired, String location) {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("cannot add items when status is " + status);
        }
        lines.add(new OrderLine(itemId, qtyRequired, location));
    }

    public void pickItem(Long itemId, int qty) {
        if (itemId == null) throw new IllegalArgumentException("itemId must not be null");
        if (qty <= 0) throw new IllegalArgumentException("qty must be > 0");

        if (status == OrderStatus.CREATED) {
            status = OrderStatus.IN_PROGRESS;
        }
        if (status != OrderStatus.IN_PROGRESS) {
            throw new IllegalStateException("Picking only allowed in IN_PROGRESS");
        }

        OrderLine line = lines.stream()
                .filter(l -> l.getItemId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not in order"));

        line.pick(qty);
    }

    public void complete() {
        if (status == OrderStatus.CREATED) {
            status = OrderStatus.FAILED;
            return;
        }

        if (status != OrderStatus.IN_PROGRESS) {
            throw new IllegalStateException("Order can only be completed in CREATED or IN_PROGRESS");
        }

        boolean allPicked = !lines.isEmpty()
                && lines.stream().allMatch(OrderLine::isFullyPicked);

        status = allPicked ? OrderStatus.DONE : OrderStatus.FAILED;
    }
    public void restoreLine(Long itemId, int qtyRequired, int qtyPicked, String location) {
        OrderLine line = new OrderLine(itemId, qtyRequired, location);
        if (qtyPicked > 0) {
            line.pick(qtyPicked);
        }
        lines.add(line);
    }

}
