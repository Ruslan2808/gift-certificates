package ru.clevertec.ecl.repository;

import ru.clevertec.ecl.entity.GiftCertificate;

import java.util.List;

/**
 * Interface that extends {@link CrudRepository} for performing operations with {@link GiftCertificate}
 * in the database
 *
 * @author Ruslan Kantsevitch
 */
public interface GiftCertificateRepository extends CrudRepository<GiftCertificate, Long> {

    /**
     * Returns a list of all gift certificates of type {@link GiftCertificate} with
     * a specific tag name in the database
     *
     * @param tagName the gift certificate tag name
     * @return the list of all gift certificates of type {@link GiftCertificate} or
     * an empty list if there are none in the database
     */
    List<GiftCertificate> findAllByTagName(String tagName);

    /**
     * Returns a list of all gift certificates of type {@link GiftCertificate} containing
     * part of the name or description in the database
     *
     * @param part the part of the name or description
     * @return the list of all gift certificates of type {@link GiftCertificate} or
     * an empty list if there are none in the database
     */
    List<GiftCertificate> findAllByPartNameOrDescription(String part);

    /**
     * Returns a list sorted by parameter and order of all gift certificates
     * of type {@link GiftCertificate} in the database
     *
     * @param sortBy the parameter by which gift certificates are sorted
     * @param order  the sort order of gift certificates by the given parameter
     * @return the list of all gift certificates of type {@link GiftCertificate}
     * or an empty list if there are none in the database
     */
    List<GiftCertificate> findAllSortByParam(String sortBy, String order);
}
