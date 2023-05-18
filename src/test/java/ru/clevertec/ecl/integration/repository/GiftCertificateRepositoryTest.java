package ru.clevertec.ecl.integration.repository;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.repository.GiftCertificateRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertAll;

public class GiftCertificateRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private GiftCertificateRepository giftCertificateRepository;

    @Nested
    class GiftCertificateRepositoryFindAllByTagsNameTest {

        @Test
        void checkFindAllByTagsNameShouldReturnSize2() {
            int expectedSize = 2;
            String tagName = "beauty";
            Pageable pageable = Pageable.unpaged();

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository
                    .findAllByTagsName(tagName, pageable)
                    .getContent();

            assertThat(actualGiftCertificates).hasSize(expectedSize);
        }

        @Test
        void checkFindAllByTagsNameShouldReturnGiftCertificates() {
            int expectedSize = 2;
            String tagName = "beauty";
            Pageable pageable = Pageable.unpaged();

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository
                    .findAllByTagsName(tagName, pageable)
                    .getContent();

            assertThat(actualGiftCertificates).hasSize(expectedSize);
            assertAll(
                    () -> assertThat(actualGiftCertificates.get(0).getId()).isEqualTo(1L),
                    () -> assertThat(actualGiftCertificates.get(1).getId()).isEqualTo(2L)
            );
        }

        @Test
        void checkFindAllByTagsNameShouldReturnEmptyGiftCertificates() {
            String tagName = "spa";
            Pageable pageable = Pageable.unpaged();

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository
                    .findAllByTagsName(tagName, pageable)
                    .getContent();

            assertThat(actualGiftCertificates).isEmpty();
        }

        @Test
        void checkFindAllByTagsNameShouldReturnOneGiftCertificateOnFirstPage() {
            int expectedSize = 1;
            Long expectedId = 1L;
            String tagName = "beauty";
            Pageable pageable = PageRequest.of(0, 1);

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository
                    .findAllByTagsName(tagName, pageable)
                    .getContent();

            assertThat(actualGiftCertificates).hasSize(expectedSize);
            assertThat(actualGiftCertificates.get(0).getId()).isEqualTo(expectedId);
        }

        @Test
        void checkFindAllByTagsNameShouldReturnEmptyGiftCertificatesOnThirdPage() {
            String tagName = "beauty";
            Pageable pageable = PageRequest.of(2, 1);

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository
                    .findAllByTagsName(tagName, pageable)
                    .getContent();

            assertThat(actualGiftCertificates).isEmpty();
        }

        @Test
        void checkFindAllByTagsNameShouldReturnSortByPriceAscGiftCertificates() {
            int expectedSize = 2;
            String tagName = "relax";
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "price"));

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository
                    .findAllByTagsName(tagName, pageable)
                    .getContent();

            assertThat(actualGiftCertificates).hasSize(expectedSize);
            assertAll(
                    () -> assertThat(actualGiftCertificates.get(0).getId()).isEqualTo(3L),
                    () -> assertThat(actualGiftCertificates.get(1).getId()).isEqualTo(2L)
            );
        }

        @Test
        void checkFindAllByTagsNameShouldReturnEmptySortByNameDescGiftCertificates() {
            int expectedSize = 2;
            String tagName = "relax";
            Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "name"));

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository
                    .findAllByTagsName(tagName, pageable)
                    .getContent();

            assertThat(actualGiftCertificates).hasSize(expectedSize);
            assertAll(
                    () -> assertThat(actualGiftCertificates.get(0).getId()).isEqualTo(2L),
                    () -> assertThat(actualGiftCertificates.get(1).getId()).isEqualTo(3L)
            );
        }
    }

    @Nested
    class GiftCertificateRepositoryFindAllByPartNameOrDescriptionTest {

        @Test
        void checkFindAllByPartNameOrDescriptionShouldReturnSize3() {
            int expectedSize = 3;
            String part = "gift";
            Pageable pageable = Pageable.unpaged();

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository
                    .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(part, part, pageable)
                    .getContent();

            assertThat(actualGiftCertificates).hasSize(expectedSize);
        }

        @Test
        void checkFindAllByPartNameOrDescriptionShouldReturnGiftCertificates() {
            String part = "gift";
            Pageable pageable = Pageable.unpaged();

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository
                    .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(part, part, pageable)
                    .getContent();

            assertAll(
                    () -> assertThat(actualGiftCertificates.get(0).getId()).isEqualTo(1L),
                    () -> assertThat(actualGiftCertificates.get(1).getId()).isEqualTo(2L),
                    () -> assertThat(actualGiftCertificates.get(2).getId()).isEqualTo(3L)
            );
        }

        @Test
        void checkFindAllByPartNameOrDescriptionShouldReturnEmptyGiftCertificates() {
            String part = "free";
            Pageable pageable = Pageable.unpaged();

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository
                    .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(part, part, pageable)
                    .getContent();

            assertThat(actualGiftCertificates).isEmpty();
        }

        @Test
        void checkFindAllByPartNameOrDescriptionShouldReturnOneGiftCertificateOnThirdPage() {
            int expectedSize = 1;
            Long expectedId = 3L;
            String part = "gift";
            Pageable pageable = PageRequest.of(2, 1);

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository
                    .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(part, part, pageable)
                    .getContent();

            assertThat(actualGiftCertificates).hasSize(expectedSize);
            assertThat(actualGiftCertificates.get(0).getId()).isEqualTo(expectedId);
        }

        @Test
        void checkFindAllByPartNameOrDescriptionShouldReturnEmptyGiftCertificatesOnFourthPage() {
            String part = "gift";
            Pageable pageable = PageRequest.of(3, 1);

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository
                    .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(part, part, pageable)
                    .getContent();

            assertThat(actualGiftCertificates).isEmpty();
        }

        @Test
        void checkFindAllByPartNameOrDescriptionReturnSortByPriceAscGiftCertificates() {
            int expectedSize = 3;
            String part = "gift";
            Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "price"));

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository
                    .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(part, part, pageable)
                    .getContent();

            assertThat(actualGiftCertificates).hasSize(expectedSize);
            assertAll(
                    () -> assertThat(actualGiftCertificates.get(0).getId()).isEqualTo(3L),
                    () -> assertThat(actualGiftCertificates.get(1).getId()).isEqualTo(2L),
                    () -> assertThat(actualGiftCertificates.get(2).getId()).isEqualTo(1L)
            );
        }

        @Test
        void checkFindAllByPartNameOrDescriptionReturnSortByDescriptionDescGiftCertificates() {
            int expectedSize = 3;
            String part = "gift";
            Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "description"));

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository
                    .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(part, part, pageable)
                    .getContent();

            assertThat(actualGiftCertificates).hasSize(expectedSize);
            assertAll(
                    () -> assertThat(actualGiftCertificates.get(0).getId()).isEqualTo(2L),
                    () -> assertThat(actualGiftCertificates.get(1).getId()).isEqualTo(3L),
                    () -> assertThat(actualGiftCertificates.get(2).getId()).isEqualTo(1L)
            );
        }
    }
}
