package ru.clevertec.ecl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import ru.clevertec.ecl.entity.Tag;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

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

    @Modifying
    @Query(value = """
            DELETE FROM gift_certificates_tags
            WHERE tag_id = :id
            """,
            nativeQuery = true)
    void deleteGiftCertificateRelationsById(Long id);
}
