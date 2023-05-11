package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.clevertec.ecl.entity.Tag;

import java.util.Optional;

/**
 * Interface for performing operations with {@link Tag} in the database
 *
 * @author Ruslan Kantsevitch
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Returns a tag of type {@link Optional<Tag>} by name or empty optional tag
     * if not found in the database
     *
     * @param name the tag name
     * @return the tag of type {@link Optional<Tag>} with given name
     */
    Optional<Tag> findByName(String name);

    /**
     * Returns the most widely used tag of type {@link Optional<Tag>} by user with the highest amount orders
     * or empty optional tag if not found in the database
     *
     * @return the tag of type {@link Optional<Tag>}
     */
    @Query(value = """
            SELECT
            	t.id,
            	t.name
            FROM tags AS t
            INNER JOIN gift_certificates_tags AS gct ON t.id = gct.tag_id
            INNER JOIN gift_certificates AS gc ON gct.gift_certificate_id = gc.id
            INNER JOIN orders AS o ON gc.id = o.gift_certificate_id
            WHERE o.user_id = (
              	SELECT user_id
            	FROM orders
            	GROUP BY user_id
            	ORDER BY SUM(price) DESC
            	LIMIT 1
            )
            GROUP BY t.id, t.name
            ORDER BY COUNT(*) DESC
            LIMIT 1;
            """,
            nativeQuery = true)
    Optional<Tag> findMostWidelyUsedByUserWithHighestAmountOrders();

    /**
     * Deletes all gift certificate relations tag of type {@link Tag} with the given id from the database
     *
     * @param id the id of the gift certificate relations tag to be deleted
     */
    @Modifying
    @Query(value = """
            DELETE FROM gift_certificates_tags
            WHERE tag_id = :id
            """,
            nativeQuery = true)
    void deleteGiftCertificateRelationsById(Long id);
}
