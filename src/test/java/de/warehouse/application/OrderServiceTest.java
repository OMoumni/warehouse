package de.warehouse.application;

import de.warehouse.domain.model.Item;
import de.warehouse.domain.model.Order;
import de.warehouse.domain.model.OrderStatus;
import de.warehouse.domain.model.Priority;
import de.warehouse.domain.ports.ItemRepositoryPort;
import de.warehouse.domain.ports.OrderRepositoryPort;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    // 🔹 Fake Order Repo
    static class FakeOrderRepo implements OrderRepositoryPort {

        private final Map<Long, Order> orders = new HashMap<>();
        private long idCounter = 1;

        @Override
        public Order save(Order order) {
            if (order.getId() == null) {
                order.setId(idCounter++);
            }
            orders.put(order.getId(), order);
            return order;
        }

        @Override
        public Optional<Order> findById(Long id) {
            return Optional.ofNullable(orders.get(id));
        }

        @Override
        public List<Order> findByStoreCode(String storeCode) {
            return orders.values().stream()
                    .filter(o -> o.getStoreCode().equals(storeCode))
                    .toList();
        }
    }


    // 🔹 Fake Item Repo
    static class FakeItemRepo implements ItemRepositoryPort {

        @Override
        public boolean existsById(Long id) {
            return true;
        }

        @Override
        public boolean existsBySku(String sku) {
            return false;
        }

        @Override
        public Item save(Item item) {
            return item;
        }

        @Override
        public Optional<Item> findById(Long id) {
            return Optional.empty();
        }

        @Override
        public List<Item> findAll(int offset, int limit) {
            return List.of();
        }
        @Override
        public void deleteById(Long id) {
            // no-op
        }

    }

    @Test
    void create_order_sets_status_created() {
        var orderRepo = new FakeOrderRepo();
        var itemRepo = new FakeItemRepo();
        var service = new OrderService(orderRepo, itemRepo);

        Order order = service.create("STORE-1", Priority.HIGH);

        assertNotNull(order.getId());
        assertEquals(OrderStatus.CREATED, order.getStatus());
    }
    @Test
    void add_item_to_order_works() {
        var orderRepo = new FakeOrderRepo();
        var itemRepo = new FakeItemRepo();
        var service = new OrderService(orderRepo, itemRepo);

        Order order = service.create("STORE-1", Priority.HIGH);

        service.addItem(order.getId(), 10L, 3);

        assertEquals(1, order.getLines().size());
        assertEquals(3, order.getLines().get(0).getQuantity());
    }
    @Test
    void get_orders_by_storeCode_returns_only_matching_orders() {
        var orderRepo = new FakeOrderRepo();
        var itemRepo = new FakeItemRepo();
        var service = new OrderService(orderRepo, itemRepo);

        service.create("30", Priority.HIGH);
        service.create("30", Priority.LOW);
        service.create("99", Priority.HIGH);

        var result = service.getOrdersByStore("30");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(o -> o.getStoreCode().equals("30")));
    }


}
