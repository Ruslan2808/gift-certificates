package ru.clevertec.ecl.service;

import ru.clevertec.ecl.dto.gift_certificate.GiftCertificateRequest;
import ru.clevertec.ecl.dto.gift_certificate.GiftCertificateResponse;
import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;

import java.util.List;

/**
 * Interface for performing operations with {@link GiftCertificate}
 *
 * @author Ruslan Kantsevitch
 */
public interface GiftCertificateService {

    /**
     * Returns a list of all gift certificates of type {@link GiftCertificate} in the database
     *
     * @return the list of all gift certificates of type {@link GiftCertificateResponse}
     * or an empty list if there are none in the database
     */
    List<GiftCertificateResponse> findAll();

    /**
     * Returns a list of all gift certificates of type {@link GiftCertificate} with a specific tag name
     * in the database
     *
     * @param tagName the gift certificate tag name
     * @return the list of all gift certificates of type {@link GiftCertificateResponse}
     * or an empty list if there are none in the database
     */
    List<GiftCertificateResponse> findAllByTagName(String tagName);

    /**
     * Returns a list of all gift certificates of type {@link GiftCertificate} containing part
     * of the name or description in the database
     *
     * @param part the part of the name or description
     * @return the list of all gift certificates of type {@link GiftCertificateResponse}
     * or an empty list if there are none in the database
     */
    List<GiftCertificateResponse> findAllByPartNameOrDescription(String part);

    /**
     * Returns a list sorted by parameter and order of all gift certificates of type {@link GiftCertificate}
     * in the database
     *
     * @param sortBy the parameter by which gift certificates are sorted
     * @param order  the sort order of gift certificates by the given parameter
     * @return the list of all gift certificates of type {@link GiftCertificateResponse}
     * or an empty list if there are none in the database
     */
    List<GiftCertificateResponse> findAllSortByParam(String sortBy, String order);

    /**
     * Returns a gift certificate of type {@link GiftCertificate} by id or
     * throws a {@link GiftCertificateNotFoundException} if the gift certificate with the given id is
     * not found in the database
     *
     * @param id the gift certificate id
     * @return the gift certificate of type {@link GiftCertificateResponse} with given id
     * @throws GiftCertificateNotFoundException if the gift certificate with the given id is
     *                                          not found in the database
     */
    GiftCertificateResponse findById(Long id);

    /**
     * Saves the gift certificate of type {@link GiftCertificate} in the database
     *
     * @param giftCertificateRequest the gift certificate of type {@link GiftCertificateRequest} to save
     * @return the saved gift certificate of type {@link GiftCertificateResponse}
     */
    GiftCertificateResponse save(GiftCertificateRequest giftCertificateRequest);

    /**
     * Updates all or part of the gift certificate of type {@link GiftCertificate} with the given id in the database or
     * throws a {@link GiftCertificateNotFoundException} if the tag with the given id is not found
     *
     * @param id                     the id of the updated gift certificate
     * @param giftCertificateRequest the gift certificate of type {@link TagRequest} with data to
     *                               update an existing gift certificate
     * @return the updated gift certificate of type {@link GiftCertificateResponse}
     * @throws GiftCertificateNotFoundException if the gift certificate with the given id is not found
     *                                          in the database
     */
    GiftCertificateResponse update(Long id, GiftCertificateRequest giftCertificateRequest);

    /**
     * Deletes the gift certificate of type {@link GiftCertificate} with the given id from the database or
     * throws a {@link GiftCertificateNotFoundException} if the gift certificate with the given id is not found
     *
     * @param id the id of the gift certificate to be deleted
     * @throws GiftCertificateNotFoundException if the gift certificate with the given id is not found in the database
     */
    void deleteById(Long id);
}
