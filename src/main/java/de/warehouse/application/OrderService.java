package de.warehouse.application;

import de.warehouse.domain.model.Order;
import de.warehouse.domain.model.OrderStatus;
import de.warehouse.domain.model.Priority;
import de.warehouse.domain.ports.ItemRepositoryPort;
import de.warehouse.domain.ports.OrderRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class OrderService {

    private final OrderRepositoryPort orderRepo;
    private final ItemRepositoryPort itemRepo;

    public OrderService(OrderRepositoryPort orderRepo,
                        ItemRepositoryPort itemRepo) {
        this.orderRepo = orderRepo;
        this.itemRepo = itemRepo;
    }

    @Transactional
    public Order create(String storeCode, Priority priority) {
        if (storeCode == null || storeCode.isBlank()) {
            throw new IllegalArgumentException("storeCode must not be blank");
        }

        Order order = new Order(null, storeCode, priority);
        return orderRepo.save(order);
    }

    @Transactional
    public Order getById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    @Transactional
    public void addItem(Long orderId, Long itemId, int quantity) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalStateException("Items can only be added to CREATED orders");
        }

        if (!itemRepo.existsById(itemId)) {
            throw new IllegalArgumentException("Item not found");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        order.addItem(itemId, quantity);
        orderRepo.save(order);
    }

    @Transactional
    public List<Order> getOrdersByStore(String storeCode) {
        return orderRepo.findByStoreCode(storeCode);
    }


}
