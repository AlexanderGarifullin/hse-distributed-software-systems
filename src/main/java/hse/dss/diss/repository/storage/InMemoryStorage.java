package hse.dss.diss.repository.storage;

import hse.dss.diss.exception.EntityAlreadyExistsException;

import java.util.List;
import java.util.Optional;

public interface InMemoryStorage<T, ID> {

     /**
     * Retrieves all stored entities as an immutable list.
     *
     * @return List of all stored entities.
     */
     List<T> getAll();

     /**
     * Retrieves an entity by its ID.
     *
     * @param id The ID of the entity.
     * @return An Optional containing the entity if found, or an empty Optional if not.
     */
     Optional<T> getById(ID id);

     /**
     * Creates a new entity in the storage.
     * Throws an exception if an entity with the same ID already exists.
     *
     * @param id     The ID of the new entity.
     * @param entity The entity to be stored.
     * @throws EntityAlreadyExistsException if an entity with the given ID already exists.
     */
     void create(ID id, T entity);

     /**
     * Updates an existing entity in the storage.
     * If the entity exists, it is replaced with the new entity.
     *
     * @param id     The ID of the entity to update.
     * @param entity The new entity to replace the existing one.
     * @return true if the entity was updated successfully, false if the entity was not found.
     */
     boolean update(ID id, T entity);

     /**
     * Deletes an entity by its ID.
     *
     * @param id The ID of the entity to delete.
     * @return true if the entity was deleted, false if the entity was not found.
     */
     boolean delete(ID id);

    /**
     * Retrieves an entity by its ID, if it exists.
     *
     * @param id The ID of the entity to find.
     * @return An Optional containing the entity if found, or an empty Optional if not.
     */
     Optional<T> findById(ID id);
}
