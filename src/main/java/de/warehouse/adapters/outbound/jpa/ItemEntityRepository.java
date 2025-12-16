package de.warehouse.adapters.outbound.jpa;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ItemEntityRepository {

    @PersistenceContext
    EntityManager em;

    public ItemEntity persist(ItemEntity entity) {
        em.persist(entity);
        return entity;
    }

    public ItemEntity merge(ItemEntity entity) {
        return em.merge(entity);
    }

    public boolean existsBySku(String sku) {
        Long count = em.createQuery(
                        "select count(i) from ItemEntity i where i.sku = :sku",
                        Long.class
                ).setParameter("sku", sku)
                .getSingleResult();

        return count > 0;
    }

    public boolean existsById(Long id) {
        return em.find(ItemEntity.class, id) != null;
    }

    public void deleteById(Long id) {
        ItemEntity entity = em.find(ItemEntity.class, id);
        if (entity != null) {
            em.remove(entity);
        }
    }
    public Optional<ItemEntity> findById(Long id) {
        return Optional.ofNullable(em.find(ItemEntity.class, id));
    }

    public List<ItemEntity> findAll(int offset, int limit) {
        return em.createQuery("select i from ItemEntity i order by i.id", ItemEntity.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
