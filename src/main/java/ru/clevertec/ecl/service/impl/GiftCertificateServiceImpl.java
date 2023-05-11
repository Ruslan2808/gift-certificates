package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.clevertec.ecl.dto.filter.GiftCertificateFilter;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.GiftCertificateService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * An implementation of the {@link GiftCertificateService} interface for performing operations with {@link GiftCertificate}
 * {@link GiftCertificateRepository} is used to perform operations with gift certificates in the database
 * {@link TagRepository} is used to perform operations with tags in the database
 * {@link GiftCertificateMapper} is used to map gift certificate of type {@link GiftCertificate} to gift certificate request
 * of type {@link GiftCertificateRequest} and gift certificate response {@link GiftCertificateResponse}
 * {@link TagMapper} is used to map tag of type {@link Tag} to tag request of type {@link TagRequest} and
 * tag response {@link TagResponse}
 *
 * @author Ruslan Kantsevich
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagRepository tagRepository;
    private final GiftCertificateMapper giftCertificateMapper;
    private final TagMapper tagMapper;

    /**
     * Returns a list of all gift certificates of type {@link GiftCertificateResponse} in the database
     *
     * @param giftCertificateFilter the object of type {@link GiftCertificateFilter} with fields for filtering:
     *                              name, description, price and duration
     * @param pageable the object of type {@link Pageable} with fields for pagination
     * @return the list of all gift certificates of type {@link GiftCertificateResponse} or an empty list
     * if there are none in the database
     */
    @Override
    public List<GiftCertificateResponse> findAll(GiftCertificateFilter giftCertificateFilter, Pageable pageable) {
        ExampleMatcher giftCertificateMatcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("price", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("duration", ExampleMatcher.GenericPropertyMatchers.exact());

        GiftCertificate giftCertificate = giftCertificateMapper.mapToGiftCertificate(giftCertificateFilter);
        Example<GiftCertificate> giftCertificateExample = Example.of(giftCertificate, giftCertificateMatcher);

        List<GiftCertificate> giftCertificates = giftCertificateRepository
                .findAll(giftCertificateExample, pageable)
                .getContent();

        return giftCertificateMapper.mapToGiftCertificateResponses(giftCertificates);
    }

    /**
     * Returns a list of all gift certificates of type {@link GiftCertificateResponse} containing tags
     * with the given name in the database
     *
     * @param tagName the gift certificate tag name
     * @param pageable the object of type {@link Pageable} with fields for pagination
     * @return the list of all gift certificates of type {@link GiftCertificateResponse} or an empty list
     * if there are none in the database
     */
    @Override
    public List<GiftCertificateResponse> findAllByTagName(String tagName, Pageable pageable) {
        List<GiftCertificate> giftCertificates = giftCertificateRepository
                .findAllByTagsName(tagName, pageable)
                .getContent();

        return giftCertificateMapper.mapToGiftCertificateResponses(giftCertificates);
    }

    /**
     * Returns a list of all gift certificates of type {@link GiftCertificateResponse} containing
     * a part of the name or description in the database
     *
     * @param part the part of name or description
     * @param pageable the object of type {@link Pageable} with fields for pagination
     * @return the list of all gift certificates of type {@link GiftCertificateResponse} or an empty list
     * if there are none in the database
     */
    @Override
    public List<GiftCertificateResponse> findAllByPartNameOrDescription(String part, Pageable pageable) {
        List<GiftCertificate> giftCertificates = giftCertificateRepository
                .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(part, part, pageable)
                .getContent();

        return giftCertificateMapper.mapToGiftCertificateResponses(giftCertificates);
    }

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
    @Override
    public GiftCertificateResponse findById(Long id) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException("Gift certificate with id = [%d] not found".formatted(id)));

        return giftCertificateMapper.mapToGiftCertificateResponse(giftCertificate);
    }

    /**
     * Saves the gift certificate of type {@link GiftCertificate} in the database
     *
     * @param giftCertificateRequest the gift certificate of type {@link GiftCertificateRequest} to save
     * @return the saved gift certificate of type {@link GiftCertificateResponse}
     */
    @Override
    @Transactional
    public GiftCertificateResponse save(GiftCertificateRequest giftCertificateRequest) {
        GiftCertificate giftCertificate = giftCertificateMapper.mapToGiftCertificate(giftCertificateRequest);

        refreshTags(giftCertificateRequest, giftCertificate);
        GiftCertificate savedGiftCertificate = giftCertificateRepository.save(giftCertificate);

        return giftCertificateMapper.mapToGiftCertificateResponse(savedGiftCertificate);
    }

    /**
     * Updates all or part of the gift certificate of type {@link GiftCertificate} with the given id in the database or
     * throws a {@link GiftCertificateNotFoundException} if the tag with the given id is not found
     *
     * @param id                     the id of the updated gift certificate
     * @param giftCertificateRequest the gift certificate of type {@link GiftCertificateRequest} with data to
     *                               update an existing gift certificate
     * @return the updated gift certificate of type {@link GiftCertificateResponse}
     * @throws GiftCertificateNotFoundException if the gift certificate with the given id is not found
     *                                          in the database
     */
    @Override
    @Transactional
    public GiftCertificateResponse update(Long id, GiftCertificateRequest giftCertificateRequest) {
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException("Gift certificate with id = [%d] not found".formatted(id)));

        giftCertificateMapper.mapUpdateFieldsToGiftCertificate(giftCertificateRequest, giftCertificate);
        refreshTags(giftCertificateRequest, giftCertificate);

        GiftCertificate updatedGiftCertificate = giftCertificateRepository.save(giftCertificate);

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
    @Transactional
    public void deleteById(Long id) {
        giftCertificateRepository.findById(id)
                .orElseThrow(() -> new GiftCertificateNotFoundException("Gift certificate with id = [%d] not found".formatted(id)));

        giftCertificateRepository.deleteById(id);
    }

    /**
     * Refresh gift certificates of type {@link GiftCertificate} tags. If the tag of type {@link Tag}
     * exists in the database, it will be bound to the gift certificate. If the tag does not exist
     * in the database, it will be created and linked to the certificate
     *
     * @param giftCertificateRequest gift certificate request tags of type {@link Tag} to update
     * @param giftCertificate gift certificate of type {@link GiftCertificate} whose tags need to be updated
     */
    private void refreshTags(GiftCertificateRequest giftCertificateRequest, GiftCertificate giftCertificate) {
        List<Tag> tags = Optional.ofNullable(giftCertificateRequest.getTags())
                .map(tagMapper::mapToTags)
                .orElse(giftCertificate.getTags());

        List<Tag> refreshTags = tags.stream()
                .map(tag -> tagRepository.findByName(tag.getName()).orElse(tag))
                .collect(Collectors.toList());

        giftCertificate.setTags(refreshTags);
    }
}
