package ru.clevertec.ecl.repository.impl;

import org.assertj.core.api.Assertions;

import org.hibernate.SessionFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import ru.clevertec.ecl.config.DataBaseConfig;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.util.gift_certificate.GiftCertificateTestBuilder;
import ru.clevertec.ecl.util.tag.TagTestBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringJUnitConfig(DataBaseConfig.class)
class GiftCertificateRepositoryImplTest {

    @Autowired
    private SessionFactory sessionFactory;
    private GiftCertificateRepository giftCertificateRepository;
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        giftCertificateRepository = new GiftCertificateRepositoryImpl(sessionFactory);
        tagRepository = new TagRepositoryImpl(sessionFactory);
    }

    @Nested
    class GiftCertificateRepositoryImplFindAllTest {

        @Test
        void checkFindAllShouldReturnNotEmptyGiftCertificates() {
            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository.findAll();

            assertThat(actualGiftCertificates).isNotEmpty();
        }

        @Test
        void checkFindAllByTagNameShouldContainsGiftCertificatesWithTagName() {
            String tagName = "massage";

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository.findAllByTagName(tagName);
            List<Long> actualIds = actualGiftCertificates.stream()
                    .map(GiftCertificate::getId)
                    .toList();

            assertThat(actualIds).containsOnly(2L);
        }

        @Test
        void checkFindAllByTagNameShouldReturnEmptyGiftCertificates() {
            String tagName = "read";

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository.findAllByTagName(tagName);

            assertThat(actualGiftCertificates).isEmpty();
        }

        @Test
        void checkFindAllByPartNameOrDescriptionShouldContainsGiftCertificatesWithPart() {
            String part = "Spa";

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository.findAllByPartNameOrDescription(part);
            List<Long> actualIds = actualGiftCertificates.stream()
                    .map(GiftCertificate::getId)
                    .toList();

            assertThat(actualIds).containsOnly(1L);
        }

        @Test
        void checkFindAllByPartNameOrDescriptionShouldReturnEmptyGiftCertificates() {
            String part = "jewelry";

            List<GiftCertificate> actualGiftCertificates = giftCertificateRepository.findAllByPartNameOrDescription(part);

            assertThat(actualGiftCertificates).isEmpty();
        }
    }

    @Nested
    class GiftCertificateRepositoryImplFindByIdTest {

        @Test
        void checkFindByIdShouldReturnNotEmptyOptionalGiftCertificate() {
            Long expectedId = 1L;

            Optional<GiftCertificate> actualOptionalGiftCertificate = giftCertificateRepository.findById(expectedId);

            assertThat(actualOptionalGiftCertificate).isPresent();
            assertThat(actualOptionalGiftCertificate.get().getId()).isEqualTo(expectedId);
        }

        @Test
        void checkFindByIdShouldReturnEmptyOptionalGiftCertificate() {
            Long id = 10L;

            Optional<GiftCertificate> actualOptionalGiftCertificate = giftCertificateRepository.findById(id);

            assertThat(actualOptionalGiftCertificate).isNotPresent();
        }
    }

    @Nested
    class GiftCertificateRepositoryImplSaveTest {

        private GiftCertificate giftCertificate;

        @BeforeEach
        void setUp() {
            Tag happinessTag = TagTestBuilder.tag()
                    .withId(null)
                    .withName("happiness")
                    .build();
            Tag relaxTag = TagTestBuilder.tag()
                    .withId(null)
                    .withName("relax")
                    .build();
            giftCertificate = GiftCertificateTestBuilder.giftCertificate()
                    .withId(null)
                    .withName("Shop")
                    .withDescription("Shop gift certificate")
                    .withPrice(BigDecimal.valueOf(500.0))
                    .withDuration(180)
                    .withTags(List.of(happinessTag, relaxTag))
                    .build();
        }

        @Test
        void checkSaveShouldReturnGiftCertificate() {
            GiftCertificate actualGiftCertificate = giftCertificateRepository.save(giftCertificate);

            assertAll(() -> {
                assertThat(actualGiftCertificate.getName()).isEqualTo(giftCertificate.getName());
                assertThat(actualGiftCertificate.getDescription()).isEqualTo(giftCertificate.getDescription());
                assertThat(actualGiftCertificate.getPrice()).isEqualTo(giftCertificate.getPrice());
                assertThat(actualGiftCertificate.getDuration()).isEqualTo(giftCertificate.getDuration());
                assertThat(actualGiftCertificate.getCreateDate()).isEqualTo(giftCertificate.getCreateDate());
                assertThat(actualGiftCertificate.getLastUpdateDate()).isEqualTo(giftCertificate.getLastUpdateDate());
                assertThat(actualGiftCertificate.getTags().get(0).getName()).isEqualTo(giftCertificate.getTags().get(0).getName());
                assertThat(actualGiftCertificate.getTags().get(1).getName()).isEqualTo(giftCertificate.getTags().get(1).getName());
            });
        }

        @Test
        void checkSaveShouldSaveNewTag() {
            String tagName = "relax";

            giftCertificateRepository.save(giftCertificate);
            Optional<Tag> actualOptionalTag = tagRepository.findByName(tagName);

            assertThat(actualOptionalTag).isPresent();
        }
    }

    @Nested
    class GiftCertificateRepositoryImplUpdateTest {

        private GiftCertificate giftCertificate;

        @BeforeEach
        void setUp() {
            Tag happinessTag = TagTestBuilder.tag()
                    .withId(null)
                    .withName("happiness")
                    .build();
            Tag relaxTag = TagTestBuilder.tag()
                    .withId(null)
                    .withName("money")
                    .build();
            giftCertificate = GiftCertificateTestBuilder.giftCertificate()
                    .withId(3L)
                    .withName("Shop")
                    .withDescription("Shop gift certificate")
                    .withPrice(BigDecimal.valueOf(500.0))
                    .withDuration(180)
                    .withTags(List.of(happinessTag, relaxTag))
                    .build();
        }

        @Test
        void checkSaveShouldReturnGiftCertificate() {
            GiftCertificate actualGiftCertificate = giftCertificateRepository.update(giftCertificate);

            assertAll(() -> {
                assertThat(actualGiftCertificate.getName()).isEqualTo(giftCertificate.getName());
                assertThat(actualGiftCertificate.getDescription()).isEqualTo(giftCertificate.getDescription());
                assertThat(actualGiftCertificate.getPrice()).isEqualTo(giftCertificate.getPrice());
                assertThat(actualGiftCertificate.getDuration()).isEqualTo(giftCertificate.getDuration());
                assertThat(actualGiftCertificate.getCreateDate()).isEqualTo(giftCertificate.getCreateDate());
                assertThat(actualGiftCertificate.getLastUpdateDate()).isEqualTo(giftCertificate.getLastUpdateDate());
                assertThat(actualGiftCertificate.getTags().get(0).getName()).isEqualTo(giftCertificate.getTags().get(0).getName());
                assertThat(actualGiftCertificate.getTags().get(1).getName()).isEqualTo(giftCertificate.getTags().get(1).getName());
            });
        }

        @Test
        void checkUpdateShouldSaveNewTag() {
            String tagName = "money";

            giftCertificateRepository.update(giftCertificate);
            Optional<Tag> actualOptionalTag = tagRepository.findByName(tagName);

            assertThat(actualOptionalTag).isPresent();
        }
    }

    @Nested
    class GiftCertificateRepositoryImplDeleteByIdTest {

        @Test
        void checkDeleteByIdShouldReturnEmptyOptionalGiftCertificate() {
            Long id = 3L;

            giftCertificateRepository.deleteById(id);
            Optional<GiftCertificate> optionalGiftCertificate = giftCertificateRepository.findById(id);

            assertThat(optionalGiftCertificate).isNotPresent();
        }

        @Test
        void checkDeleteByIdShouldThrowsIllegalArgumentException() {
            Long id = 10L;

            assertThatThrownBy(() -> giftCertificateRepository.deleteById(id))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}