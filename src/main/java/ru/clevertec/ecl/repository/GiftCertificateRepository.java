package ru.clevertec.ecl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.clevertec.ecl.entity.GiftCertificate;

/**
 * Interface that extends for performing operations with {@link GiftCertificate}
 * in the database
 *
 * @author Ruslan Kantsevitch
 */
@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    /**
     * Returns a page of all gift certificates of type {@link GiftCertificate} with
     * a specific tag name in the database
     *
     * @param tagName the gift certificate tag name
     * @param pageable the object of type {@link Pageable} with fields for pagination
     * @return the list of all gift certificates of type {@link GiftCertificate} or
     * an empty list if there are none in the database
     */
    Page<GiftCertificate> findAllByTagsName(String tagName, Pageable pageable);

    /**
     * Returns a list of all gift certificates of type {@link GiftCertificate} containing
     * part of the name or description in the database
     *
     * @param namePart the part of the name
     * @param descriptionPart the part of the description
     * @param pageable the object of type {@link Pageable} with fields for pagination
     * @return the list of all gift certificates of type {@link GiftCertificate} or
     * an empty list if there are none in the database
     */
    Page<GiftCertificate> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String namePart, String descriptionPart, Pageable pageable);
}
