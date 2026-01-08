package de.warehouse.domain.model;

public class OrderLine {

    private final Long itemId;
    private final int qtyRequired;
    private int qtyPicked;

    private final String location; // wenn du Location schon nutzt, drin lassen

    public OrderLine(Long itemId, int qtyRequired, String location) {
        if (itemId == null) throw new IllegalArgumentException("itemId must not be null");
        if (qtyRequired <= 0) throw new IllegalArgumentException("qtyRequired must be > 0");
        if (location == null || location.isBlank()) throw new IllegalArgumentException("location must not be blank");

        this.itemId = itemId;
        this.qtyRequired = qtyRequired;
        this.qtyPicked = 0;
        this.location = location;
    }


    public Long getItemId() { return itemId; }
    public int getQtyRequired() { return qtyRequired; }
    public int getQtyPicked() { return qtyPicked; }
    public String getLocation() { return location; }

    public void pick(int qty) {
        if (qty <= 0) throw new IllegalArgumentException("qty must be > 0");
        if (qtyPicked + qty > qtyRequired) {
            throw new IllegalStateException("Cannot pick more than required");
        }
        qtyPicked += qty;
    }

    public boolean isFullyPicked() {
        return qtyPicked == qtyRequired;
    }
}
