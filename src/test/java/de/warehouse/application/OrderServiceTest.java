package de.warehouse.application;

import de.warehouse.domain.model.Order;
import de.warehouse.domain.model.OrderStatus;
import de.warehouse.domain.model.Priority;
import de.warehouse.domain.ports.OrderRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    static class FakeOrderRepo implements OrderRepositoryPort {

        private Order stored;

        @Override
        public Order save(Order order) {
            if (order.getId() == null) {
                order.setId(1L);
            }
            this.stored = order;
            return order;
        }

        @Override
        public Optional<Order> findById(Long id) {
            return Optional.ofNullable(stored);
        }
    }

    @Test
    void create_order_sets_status_created() {
        var repo = new FakeOrderRepo();
        var service = new OrderService(repo);

        Order order = service.create("DE-BE-05", Priority.HIGH);

        assertNotNull(order.getId());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals("DE-BE-05", order.getStoreCode());
        assertEquals(Priority.HIGH, order.getPriority());
    }
}
