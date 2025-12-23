package de.warehouse.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    @Test
    void add_item_only_allowed_in_created() {
        Order order = new Order(null, "STORE-1", Priority.HIGH);

        order.addItem(1L, 2);

        assertEquals(1, order.getLines().size());
    }


}