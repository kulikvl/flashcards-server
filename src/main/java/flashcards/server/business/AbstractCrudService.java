package flashcards.server.business;

import flashcards.server.domain.DomainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

public abstract class AbstractCrudService<E extends DomainEntity<K>, K> {

    protected JpaRepository<E, K> repository;

    protected AbstractCrudService(JpaRepository<E, K> repository) {
        this.repository = repository;
    }

    public E create(E entity) throws EntityStateException {
        if (entity.getId() != null && repository.existsById(entity.getId()))
            throw new EntityStateException("entity " + entity + " already exists");

        return repository.save(entity);
    }

    public Optional<E> readById(K id) {
        return repository.findById(id);
    }

    public Collection<E> readAll() {
        return repository.findAll();
    }

    public void update(E entity) throws EntityStateException {
        if (!repository.existsById(entity.getId()))
            throw new EntityStateException("entity " + entity + " does not exist");

        repository.save(entity);
    }

    public void deleteById(K id) {
        repository.deleteById(id);
    }

}
