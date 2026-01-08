package de.warehouse.domain.model;

public class Item {

    private Long id;
    private final String sku;
    private final String name;
    private final String unit;
    private final String defaultLocation;

    public Item(Long id, String sku, String name, String unit, String defaultLocation) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.unit = unit;
        this.defaultLocation = defaultLocation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getDefaultLocation() {
        return defaultLocation;
    }
}
