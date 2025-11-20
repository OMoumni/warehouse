package de.warehouse.adapters.outbound.jpa;

import de.warehouse.domain.model.Item;
import de.warehouse.domain.ports.ItemRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ItemRepositoryAdapter implements ItemRepositoryPort {

    @Inject
    EntityManager em;

    @Override
    public boolean existsBySku(String sku) {
        Long cnt = em.createQuery(
                        "select count(i) from ItemEntity i where i.sku = :sku", Long.class)
                .setParameter("sku", sku)
                .getSingleResult();
        return cnt != null && cnt > 0;
    }

    @Override
    @Transactional
    public Item save(Item item) {
        ItemEntity e = new ItemEntity();
        e.sku = item.getSku();
        e.name = item.getName();
        e.unit = item.getUnit();
        e.defaultLocation = item.getDefaultLocation();
        em.persist(e);
        return new Item(e.sku, e.name, e.unit, e.defaultLocation);
    }

    @Override
    public boolean existsById(Long id) {
        return em.find(ItemEntity.class, id) != null;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ItemEntity entity = em.find(ItemEntity.class, id);
        if (entity != null) {
            em.remove(entity);
        }
    }
}
