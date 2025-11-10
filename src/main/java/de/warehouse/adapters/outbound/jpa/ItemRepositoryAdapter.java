// de.warehouse.adapters.outbound.jpa.ItemRepositoryAdapter
package de.warehouse.adapters.outbound.jpa;

import de.warehouse.domain.model.Item;
import de.warehouse.domain.ports.ItemRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;   // <— wichtig

@ApplicationScoped
public class ItemRepositoryAdapter implements ItemRepositoryPort {

    @Inject EntityManager em;

    @Override
    public boolean existsBySku(String sku) {
        Long cnt = em.createQuery(
                        "select count(i) from ItemEntity i where i.sku = :sku", Long.class)
                .setParameter("sku", sku)
                .getSingleResult();
        return cnt != null && cnt > 0;
    }

    @Override
    @Transactional   // <— startet eine TX für persist()
    public Item save(Item item) {
        ItemEntity e = new ItemEntity();
        e.sku = item.getSku();
        e.name = item.getName();
        e.unit = item.getUnit();
        e.defaultLocation = item.getDefaultLocation();
        em.persist(e);
        return new Item(e.sku, e.name, e.unit, e.defaultLocation);
    }
}
