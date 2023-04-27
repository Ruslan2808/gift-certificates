package ru.clevertec.ecl.repository;

import ru.clevertec.ecl.entity.Tag;

import java.util.Optional;

/**
 * Interface that extends {@link CrudRepository} for performing operations with {@link Tag}
 * in the database
 *
 * @author Ruslan Kantsevitch
 */
public interface TagRepository extends CrudRepository<Tag, Long> {

    /**
     * Returns a tag of type {@link Optional<Tag>} by name or empty optional tag
     * if not found in the database
     *
     * @param name the tag name
     * @return the tag of type {@link Optional<Tag>} with given name
     */
    Optional<Tag> findByName(String name);
}
