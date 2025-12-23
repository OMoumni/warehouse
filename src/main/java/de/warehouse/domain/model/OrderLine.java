package de.warehouse.domain.model;

public class OrderLine {

    private final Long itemId;
    private final int quantity;

    public OrderLine(Long itemId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public Long getItemId() { return itemId; }
    public int getQuantity() { return quantity; }
}
