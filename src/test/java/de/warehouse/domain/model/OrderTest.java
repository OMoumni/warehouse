package de.warehouse.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void add_item_only_allowed_in_created() {
        Order order = new Order(null, "STORE-1", Priority.HIGH);

        order.addItem(1L, 2, "R1-A");

        assertEquals(1, order.getLines().size());
        assertEquals(1L, order.getLines().get(0).getItemId());
        assertEquals(2, order.getLines().get(0).getQtyRequired());
        assertEquals(0, order.getLines().get(0).getQtyPicked());
        assertEquals("R1-A", order.getLines().get(0).getLocation());
    }

    @Test
    void add_item_not_allowed_when_not_created() {
        Order order = new Order(null, "STORE-1", Priority.HIGH);
        order.addItem(1L, 2, "R1-A");

        // first pick -> moves to IN_PROGRESS
        order.pickItem(1L, 1);

        assertThrows(IllegalStateException.class, () ->
                order.addItem(2L, 1, "R2-B")
        );
    }
}
