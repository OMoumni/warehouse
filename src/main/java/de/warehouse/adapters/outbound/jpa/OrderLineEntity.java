package de.warehouse.adapters.outbound.jpa;

import jakarta.persistence.*;

@Entity
@Table(name = "order_lines")
public class OrderLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public Long itemId;

    @Column(nullable = false)
    public int qtyRequired;

    @Column(nullable = false)
    public int qtyPicked;

    @Column(nullable = false)
    public String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    public OrderEntity order;
}
