package ru.clevertec.ecl.service;

import org.springframework.data.domain.Pageable;

import ru.clevertec.ecl.dto.filter.GiftCertificateFilter;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.entity.GiftCertificate;

import java.util.List;

/**
 * Interface for performing operations with {@link GiftCertificate}
 *
 * @author Ruslan Kantsevitch
 */
public interface GiftCertificateService {
    List<GiftCertificateResponse> findAll(GiftCertificateFilter giftCertificateFilter, Pageable pageable);
    List<GiftCertificateResponse> findAllByTagName(String tagName, Pageable pageable);
    List<GiftCertificateResponse> findAllByPartNameOrDescription(String part, Pageable pageable);
    GiftCertificateResponse findById(Long id);
    GiftCertificateResponse save(GiftCertificateRequest giftCertificateRequest);
    GiftCertificateResponse update(Long id, GiftCertificateRequest giftCertificateRequest);
    void deleteById(Long id);
}
