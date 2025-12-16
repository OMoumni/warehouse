package de.warehouse.adapters.outbound.jpa;

import de.warehouse.domain.model.Item;
import jakarta.persistence.*;

@Entity
@Table(name = "items", uniqueConstraints = {
        @UniqueConstraint(columnNames = "sku")
})
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String sku;

    @Column(nullable = false)
    public String name;

    public String unit;
    public String defaultLocation;

    public static ItemEntity fromDomain(Item item) {
        ItemEntity e = new ItemEntity();
        e.id = item.getId();
        e.sku = item.getSku();
        e.name = item.getName();
        e.unit = item.getUnit();
        e.defaultLocation = item.getDefaultLocation();
        return e;
    }

    public Item toDomain() {
        return new Item(id, sku, name, unit, defaultLocation);
    }
}
