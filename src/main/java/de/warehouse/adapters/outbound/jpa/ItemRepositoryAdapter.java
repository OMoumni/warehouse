package de.warehouse.adapters.outbound.jpa;

import de.warehouse.domain.model.Item;
import de.warehouse.domain.ports.ItemRepositoryPort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ItemRepositoryAdapter implements ItemRepositoryPort {

    private final ItemEntityRepository repository;

    public ItemRepositoryAdapter(ItemEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Item save(Item item) {
        ItemEntity entity = ItemEntity.fromDomain(item);

        ItemEntity managed;
        if (item.getId() == null) {
            managed = repository.persist(entity);
        } else {
            managed = repository.merge(entity);
        }

        item.setId(managed.id);
        return item;
    }


    @Override
    public boolean existsBySku(String sku) {
        return repository.existsBySku(sku);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    @Override
    public Optional<Item> findById(Long id) {
        return repository.findById(id).map(ItemEntity::toDomain);
    }

    @Override
    public List<Item> findAll(int offset, int limit) {
        return repository.findAll(offset, limit).stream()
                .map(ItemEntity::toDomain)
                .toList();
    }
}
