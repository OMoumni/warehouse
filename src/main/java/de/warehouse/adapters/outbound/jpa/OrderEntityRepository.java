package de.warehouse.adapters.outbound.jpa;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@ApplicationScoped
public class OrderEntityRepository {

    @PersistenceContext
    EntityManager em;

    public void persist(OrderEntity entity) {
        em.persist(entity);
    }

    public OrderEntity merge(OrderEntity entity) {
        return em.merge(entity);
    }

    public OrderEntity find(Long id) {
        return em.find(OrderEntity.class, id);
    }
    public List<OrderEntity> findByStoreCode(String storeCode) {
        return em.createQuery(
                        "select o from OrderEntity o where o.storeCode = :storeCode",
                        OrderEntity.class
                )
                .setParameter("storeCode", storeCode)
                .getResultList();
    }
}
