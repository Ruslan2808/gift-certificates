package ru.clevertec.ecl.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface for performing CRUD operations with entities in the database
 *
 * @author Ruslan Kantsevitch
 */
public interface CrudRepository<E, K> {

    /**
     * Returns a list of all entities of type {@link E} in the database
     *
     * @return the list of all entities of type {@link E} or an empty list if there are
     * none in the database
     */
    List<E> findAll();

    /**
     * Returns an entity of type {@link Optional<E>} by id of type {@link K} or
     * empty optional entity if not found in the database
     *
     * @param id the entity id of type {@link K}
     * @return the entity of type {@link Optional<E>} with given id
     */
    Optional<E> findById(K id);

    /**
     * Saves the entity of type {@link E} in the database
     *
     * @param entity the entity of type {@link E} to save
     * @return the saved entity of type {@link E}
     */
    E save(E entity);

    /**
     * Updates the entity of type {@link E} in the database
     *
     * @param entity the entity of type {@link E} to update
     * @return the updated entity of type {@link E}
     */
    E update(E entity);

    /**
     * Deletes the entity with the given id of type {@link K} from the database
     *
     * @param id the id of type {@link K} the entity to be deleted
     */
    void deleteById(K id);
}
