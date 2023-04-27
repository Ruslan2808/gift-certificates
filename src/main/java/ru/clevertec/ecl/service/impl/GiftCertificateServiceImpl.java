package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import ru.clevertec.ecl.dto.gift_certificate.GiftCertificateRequest;
import ru.clevertec.ecl.dto.gift_certificate.GiftCertificateResponse;
import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.impl.GiftCertificateRepositoryImpl;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * An implementation of the {@link GiftCertificateService} interface for performing operations with {@link GiftCertificate}
 * {@link GiftCertificateRepositoryImpl} is used to perform operations with gift certificates in the database
 * {@link GiftCertificateMapper} is used to map gift certificate of type {@link GiftCertificate} to gift certificate request
 * of type {@link GiftCertificateRequest} and gift certificate response {@link GiftCertificateResponse}
 *
 * @author Ruslan Kantsevich
 */
@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepositoryImpl giftCertificateRepository;
    private final GiftCertificateMapper giftCertificateMapper;
    private final TagMapper tagMapper;

    /**
     * Returns a list of all gift certificates of type {@link GiftCertificate} in the database
     *
     * @return the list of all gift certificates of type {@link GiftCertificateResponse}
     * or an empty list if there are none in the database
     */
    @Override
    public List<GiftCertificateResponse> findAll() {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findAll();
        return giftCertificateMapper.mapToGiftCertificateResponses(giftCertificates);
    }

    /**
     * Returns a list of all gift certificates of type {@link GiftCertificate} with a specific tag name
     * in the database
     *
     * @param tagName the gift certificate tag name
     * @return the list of all gift certificates of type {@link GiftCertificateResponse}
     * or an empty list if there are none in the database
     */
    @Override
    public List<GiftCertificateResponse> findAllByTagName(String tagName) {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findAllByTagName(tagName);
        return giftCertificateMapper.mapToGiftCertificateResponses(giftCertificates);
    }

    /**
     * Returns a list of all gift certificates of type {@link GiftCertificate} containing part
     * of the name or description in the database
     *
     * @param part the part of the name or description
     * @return the list of all gift certificates of type {@link GiftCertificateResponse}
     * or an empty list if there are none in the database
     */
    @Override
    public List<GiftCertificateResponse> findAllByPartNameOrDescription(String part) {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findAllByPartNameOrDescription(part);
        return giftCertificateMapper.mapToGiftCertificateResponses(giftCertificates);
    }

    /**
     * Returns a list sorted by parameter and order of all gift certificates of type {@link GiftCertificate}
     * in the database
     *
     * @param sortBy the parameter by which gift certificates are sorted
     * @param order  the sort order of gift certificates by the given parameter
     * @return the list of all gift certificates of type {@link GiftCertificateResponse}
     * or an empty list if there are none in the database
     */
    @Override
    public List<GiftCertificateResponse> findAllSortByParam(String sortBy, String order) {
        List<GiftCertificate> giftCertificates = giftCertificateRepository.findAllSortByParam(sortBy, order);
        return giftCertificateMapper.mapToGiftCertificateResponses(giftCertificates);
    }

    /**
     * Returns a gift certificate of type {@link GiftCertificate} by id or
     * throws a {@link GiftCertificateNotFoundException}if the gift certificate with the given id is not found
     * in the database
     *
     * @param id the gift certificate id
     * @return the gift certificate of type {@link GiftCertificateResponse} with given id
     * @throws GiftCertificateNotFoundException if the gift certificate with the given id is
     *                                          not found in the database
     */
    @Override
    public GiftCertificateResponse findById(Long id) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(String.format("Gift certificate with id = %d not found", id)));

        return giftCertificateMapper.mapToGiftCertificateResponse(giftCertificate);
    }

    /**
     * Saves the gift certificate of type {@link GiftCertificate} in the database
     *
     * @param giftCertificateRequest the gift certificate of type {@link GiftCertificateRequest} to save
     * @return the saved gift certificate of type {@link GiftCertificateResponse}
     */
    @Override
    public GiftCertificateResponse save(GiftCertificateRequest giftCertificateRequest) {
        GiftCertificate giftCertificate = giftCertificateMapper.mapToGiftCertificate(giftCertificateRequest);
        LocalDateTime dateTimeNow = LocalDateTime.now();

        giftCertificate.setCreateDate(dateTimeNow);
        giftCertificate.setLastUpdateDate(dateTimeNow);
        GiftCertificate savedGiftCertificate = giftCertificateRepository.save(giftCertificate);

        return giftCertificateMapper.mapToGiftCertificateResponse(savedGiftCertificate);
    }

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
    @Override
    public GiftCertificateResponse update(Long id, GiftCertificateRequest giftCertificateRequest) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(String.format("Gift certificate with id = %d not found", id)));

        updateGiftCertificateFields(giftCertificateRequest, giftCertificate);
        GiftCertificate updatedGiftCertificate = giftCertificateRepository.update(giftCertificate);

        return giftCertificateMapper.mapToGiftCertificateResponse(updatedGiftCertificate);
    }

    /**
     * Deletes the gift certificate of type {@link GiftCertificate} with the given id from the database or
     * throws a {@link GiftCertificateNotFoundException} if the gift certificate with the given id is not found
     *
     * @param id the id of the gift certificate to be deleted
     * @throws GiftCertificateNotFoundException if the gift certificate with the given id is not found in the database
     */
    @Override
    public void deleteById(Long id) {
        giftCertificateRepository.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException(String.format("Gift certificate with id = %d not found", id)));

        giftCertificateRepository.deleteById(id);
    }

    /**
     * Updates all or part of the fields of a gift certificate of type {@link GiftCertificate}
     * data passed in the gift certificate request of type {@link GiftCertificateRequest}
     *
     * @param giftCertificate        the gift certificate whose fields need to be updated
     * @param giftCertificateRequest the gift certificate request whose fields contain data to update
     */
    private void updateGiftCertificateFields(GiftCertificateRequest giftCertificateRequest, GiftCertificate giftCertificate) {
        if (Objects.nonNull(giftCertificateRequest.getName())) {
            giftCertificate.setName(giftCertificateRequest.getName());
        }
        if (Objects.nonNull(giftCertificateRequest.getDescription())) {
            giftCertificate.setDescription(giftCertificateRequest.getDescription());
        }
        if (Objects.nonNull(giftCertificateRequest.getPrice())) {
            giftCertificate.setPrice(giftCertificateRequest.getPrice());
        }
        if (Objects.nonNull(giftCertificateRequest.getDuration())) {
            giftCertificate.setDuration(giftCertificateRequest.getDuration());
        }
        if (Objects.nonNull(giftCertificateRequest.getTags())) {
            List<Tag> tags = giftCertificateRequest.getTags().stream()
                    .map(tagMapper::mapToTag)
                    .toList();
            giftCertificate.setTags(tags);
        }
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
    }
}
