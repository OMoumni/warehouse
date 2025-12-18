package de.warehouse.adapters.outbound.jpa;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class OrderEntityRepository {

    @PersistenceContext
    EntityManager em;

    public void persist(OrderEntity entity) {
        em.persist(entity);
    }

    public OrderEntity find(Long id) {
        return em.find(OrderEntity.class, id);
    }
}
