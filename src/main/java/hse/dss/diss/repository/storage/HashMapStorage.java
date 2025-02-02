package hse.dss.diss.repository.storage;

import hse.dss.diss.exception.EntityAlreadyExistsException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple in-memory storage implementation using ConcurrentHashMap.
 * This class provides basic CRUD operations.
 *
 * @param <T>  The type of entity being stored.
 * @param <ID> The type of the entity's identifier.
 */
public class HashMapStorage<T, ID> implements InMemoryStorage<T, ID> {
    // Thread-safe map to store entities with their corresponding IDs.
    protected final ConcurrentHashMap<ID, T> storage = new ConcurrentHashMap<>();

    @Override
    public List<T> getAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public Optional<T> getById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void create(ID id, T entity) {
        if (storage.containsKey(id)) {
            throw new EntityAlreadyExistsException(entityAlreadyExistsMsg(id));
        }
        storage.put(id, entity);
    }

    @Override
    public boolean update(ID id, T entity) {
        if (storage.containsKey(id)) {
            storage.put(id, entity);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(ID id) {
        return storage.remove(id) != null;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }

    /**
     * Generates an error message for entity existence conflict.
     *
     * @param id The ID of the existing entity.
     * @return A formatted error message.
     */
    private String entityAlreadyExistsMsg(ID id) {
        return "Entity with ID " + id + " already exists.";
    }

    /**
     * Clears all the entities from the storage.
     * This method removes all entries from the underlying storage (ConcurrentHashMap),
     * effectively resetting the storage to an empty state.
     *
     * This operation is irreversible, and once called, all stored data will be lost.
     */
    public void clear() {
        storage.clear();
    }
}
