package de.warehouse.domain.model;

public final class Item {
    private Long id;
    private final String sku;   // Stock Keeping Unit (business code)
    private final String name;
    private final String unit;
    private final String defaultLocation;

    public Item( String sku, String name, String unit, String defaultLocation) {
      this.sku = sku; this.name = name; this.unit = unit; this.defaultLocation = defaultLocation;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() { return sku; }
    public String getName() { return name; }
    public String getUnit() { return unit; }
    public String getDefaultLocation() { return defaultLocation; }
}
