package de.warehouse.adapters.outbound.jpa;

import jakarta.persistence.*;

@Entity @Table(name="items", uniqueConstraints=@UniqueConstraint(columnNames="sku"))
public class ItemEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable=false, unique=true)
    public String sku;

    @Column(nullable=false)
    public String name;

    public String unit;
    public String defaultLocation;
}
