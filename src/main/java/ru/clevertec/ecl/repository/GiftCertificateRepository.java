package ru.clevertec.ecl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.clevertec.ecl.entity.GiftCertificate;

public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {
    Page<GiftCertificate> findAllByTagsName(String tagName, Pageable pageable);
    Page<GiftCertificate> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String namePart,
                                                                                             String descriptionPart,
                                                                                             Pageable pageable);
}
