package ru.clevertec.ecl.service.impl;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.clevertec.ecl.dto.gift_certificate.GiftCertificateRequest;
import ru.clevertec.ecl.dto.gift_certificate.GiftCertificateResponse;
import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.dto.tag.TagResponse;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.impl.GiftCertificateRepositoryImpl;
import ru.clevertec.ecl.util.gift_certificate.GiftCertificateRequestTestBuilder;
import ru.clevertec.ecl.util.gift_certificate.GiftCertificateResponseTestBuilder;
import ru.clevertec.ecl.util.gift_certificate.GiftCertificateTestBuilder;
import ru.clevertec.ecl.util.tag.TagRequestTestBuilder;
import ru.clevertec.ecl.util.tag.TagResponseTestBuilder;
import ru.clevertec.ecl.util.tag.TagTestBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepositoryImpl giftCertificateRepository;

    @Mock
    private GiftCertificateMapper giftCertificateMapper;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Nested
    class GiftCertificateServiceImplFindAllTest {

        private Tag healthTag;
        private TagResponse healthTagResponse;
        private GiftCertificate healthGiftCertificate;
        private GiftCertificate beautyGiftCertificate;
        private GiftCertificateResponse healthGiftCertificateResponse;
        private GiftCertificateResponse beautyGiftCertificateResponse;

        @BeforeEach
        void setUp() {
            healthTag = TagTestBuilder.tag()
                    .withName("health")
                    .build();
            Tag beautyTag = TagTestBuilder.tag()
                    .withName("beauty")
                    .build();
            healthTagResponse = TagResponseTestBuilder.tagResponse()
                    .withName("health")
                    .build();
            TagResponse beautyTagResponse = TagResponseTestBuilder.tagResponse()
                    .withName("beauty")
                    .build();
            healthGiftCertificate = GiftCertificateTestBuilder.giftCertificate()
                    .withName("Health")
                    .withDescription("Gift certificate 'Health'")
                    .withDuration(365)
                    .withTags(List.of(healthTag))
                    .build();
            beautyGiftCertificate = GiftCertificateTestBuilder.giftCertificate()
                    .withId(2L)
                    .withName("Beauty")
                    .withDescription("Gift certificate 'Beauty'")
                    .withDuration(180)
                    .withTags(List.of(beautyTag))
                    .build();
            healthGiftCertificateResponse = GiftCertificateResponseTestBuilder.giftCertificateResponse()
                    .withName("Health")
                    .withDescription("Gift certificate 'Health'")
                    .withDuration(365)
                    .withTags(List.of(healthTagResponse))
                    .build();
            beautyGiftCertificateResponse = GiftCertificateResponseTestBuilder.giftCertificateResponse()
                    .withId(2L)
                    .withName("Beauty")
                    .withDescription("Gift certificate 'Beauty'")
                    .withDuration(180)
                    .withTags(List.of(beautyTagResponse))
                    .build();
        }

        @Test
        void checkFindAllShouldReturnSize2() {
            int expectedSize = 2;
            List<GiftCertificate> giftCertificates = List.of(healthGiftCertificate, beautyGiftCertificate);
            List<GiftCertificateResponse> giftCertificateResponses = List.of(healthGiftCertificateResponse,beautyGiftCertificateResponse);
            doReturn(giftCertificates).when(giftCertificateRepository).findAll();
            doReturn(giftCertificateResponses).when(giftCertificateMapper).mapToGiftCertificateResponses(giftCertificates);

            List<GiftCertificateResponse> actualGiftCertificateResponses = giftCertificateService.findAll();

            Assertions.assertThat(actualGiftCertificateResponses).hasSize(expectedSize);
        }

        @Test
        void checkFindAllShouldReturnEmptyGiftCertificates() {
            doReturn(Collections.emptyList()).when(giftCertificateRepository).findAll();

            List<GiftCertificateResponse> actualGiftCertificateServiceResponses = giftCertificateService.findAll();

            Assertions.assertThat(actualGiftCertificateServiceResponses).isEmpty();
        }

        @Test
        void checkFindAllByTagNameShouldContainsGiftCertificatesWithTagNameHealth() {
            String tagName = "health";
            List<GiftCertificate> giftCertificatesWithTagName =
                    Stream.of(healthGiftCertificate, beautyGiftCertificate)
                            .filter(giftCertificate -> giftCertificate.getTags().contains(healthTag))
                            .toList();
            List<GiftCertificateResponse> giftCertificateResponsesWithTagName =
                    Stream.of(healthGiftCertificateResponse, beautyGiftCertificateResponse)
                            .filter(giftCertificateResponse -> giftCertificateResponse.getTags().contains(healthTagResponse))
                            .toList();
            doReturn(giftCertificatesWithTagName).when(giftCertificateRepository).findAllByTagName(tagName);
            doReturn(giftCertificateResponsesWithTagName).when(giftCertificateMapper).mapToGiftCertificateResponses(giftCertificatesWithTagName);

            List<GiftCertificateResponse> actualGiftCertificateServiceResponses = giftCertificateService.findAllByTagName(tagName);

            Assertions.assertThat(actualGiftCertificateServiceResponses).containsOnly(healthGiftCertificateResponse);
        }

        @Test
        void checkFindAllByTagNameShouldReturnEmptyGiftCertificates() {
            String tagName = "massage";
            doReturn(Collections.emptyList()).when(giftCertificateRepository).findAllByTagName(tagName);

            List<GiftCertificateResponse> actualGiftCertificateServiceResponses = giftCertificateService.findAllByTagName(tagName);

            Assertions.assertThat(actualGiftCertificateServiceResponses).isEmpty();
        }

        @Test
        void checkFindAllByPartNameOrDescriptionShouldContainsGiftCertificatesWithPartCertificateNameOrDescription() {
            String part = "certificate";
            List<GiftCertificate> giftCertificatesWithPartCertificate =
                    Stream.of(healthGiftCertificate, beautyGiftCertificate)
                            .filter(giftCertificate -> giftCertificate.getName().contains(part) ||
                                    giftCertificate.getDescription().contains(part))
                            .toList();
            List<GiftCertificateResponse> giftCertificateResponsesWithPartCertificate =
                    Stream.of(healthGiftCertificateResponse, beautyGiftCertificateResponse)
                            .filter(giftCertificateResponse -> giftCertificateResponse.getName().contains(part) ||
                                    giftCertificateResponse.getDescription().contains(part))
                            .toList();
            doReturn(giftCertificatesWithPartCertificate).when(giftCertificateRepository).findAllByPartNameOrDescription(part);
            doReturn(giftCertificateResponsesWithPartCertificate).when(giftCertificateMapper).mapToGiftCertificateResponses(giftCertificatesWithPartCertificate);

            List<GiftCertificateResponse> actualGiftCertificateServiceResponses = giftCertificateService.findAllByPartNameOrDescription(part);

            Assertions.assertThat(actualGiftCertificateServiceResponses).contains(healthGiftCertificateResponse, beautyGiftCertificateResponse);
        }

        @Test
        void checkFindAllByPartNameOrDescriptionShouldReturnEmptyGiftCertificates() {
            String part = "happiness";
            doReturn(Collections.emptyList()).when(giftCertificateRepository).findAllByPartNameOrDescription(part);

            List<GiftCertificateResponse> actualGiftCertificateServiceResponses = giftCertificateService.findAllByPartNameOrDescription(part);

            Assertions.assertThat(actualGiftCertificateServiceResponses).isEmpty();
        }

        @Test
        void checkFindAllSortByParamShouldReturnGiftCertificatesSortByNameAsc() {
            String sortBy = "name";
            String order = "asc";
            List<GiftCertificate> giftCertificates = Stream.of(healthGiftCertificate, beautyGiftCertificate)
                    .sorted(Comparator.comparing(GiftCertificate::getName))
                    .toList();
            List<GiftCertificateResponse> giftCertificateResponses = Stream.of(healthGiftCertificateResponse,beautyGiftCertificateResponse)
                    .sorted(Comparator.comparing(GiftCertificateResponse::getName))
                    .toList();
            doReturn(giftCertificates).when(giftCertificateRepository).findAllSortByParam(sortBy, order);
            doReturn(giftCertificateResponses).when(giftCertificateMapper).mapToGiftCertificateResponses(giftCertificates);

            List<GiftCertificateResponse> actualGiftCertificateServiceResponses = giftCertificateService.findAllSortByParam(sortBy, order);

            Assertions.assertThat(actualGiftCertificateServiceResponses).containsExactly(beautyGiftCertificateResponse, healthGiftCertificateResponse);
        }

        @Test
        void checkFindAllSortByParamShouldReturnGiftCertificatesSortByDurationDesc() {
            String sortBy = "duration";
            String order = "desc";
            List<GiftCertificate> giftCertificates = Stream.of(healthGiftCertificate, beautyGiftCertificate)
                    .sorted(Comparator.comparing(GiftCertificate::getDuration).reversed())
                    .toList();
            List<GiftCertificateResponse> giftCertificateResponses = Stream.of(healthGiftCertificateResponse,beautyGiftCertificateResponse)
                    .sorted(Comparator.comparing(GiftCertificateResponse::getDuration).reversed())
                    .toList();
            doReturn(giftCertificates).when(giftCertificateRepository).findAllSortByParam(sortBy, order);
            doReturn(giftCertificateResponses).when(giftCertificateMapper).mapToGiftCertificateResponses(giftCertificates);

            List<GiftCertificateResponse> actualGiftCertificateServiceResponses = giftCertificateService.findAllSortByParam(sortBy, order);

            Assertions.assertThat(actualGiftCertificateServiceResponses).containsExactly(healthGiftCertificateResponse, beautyGiftCertificateResponse);
        }
    }

    @Nested
    class GiftCertificateServiceImplFindByIdTest {

        @Test
        void checkFindByIdShouldReturnGiftCertificate() {
            Long id = 1L;
            GiftCertificate giftCertificate = GiftCertificateTestBuilder.giftCertificate().build();
            GiftCertificateResponse giftCertificateResponse = GiftCertificateResponseTestBuilder.giftCertificateResponse().build();
            doReturn(Optional.of(giftCertificate)).when(giftCertificateRepository).findById(id);
            doReturn(giftCertificateResponse).when(giftCertificateMapper).mapToGiftCertificateResponse(giftCertificate);

            GiftCertificateResponse actualGiftCertificateResponse = giftCertificateService.findById(id);

            assertThat(actualGiftCertificateResponse).isEqualTo(giftCertificateResponse);
        }

        @Test
        void checkFindByIdShouldThrowsGiftCertificateNotFoundException() {
            Long id = 1L;
            doReturn(Optional.empty()).when(giftCertificateRepository).findById(id);

            assertThatThrownBy(() -> giftCertificateService.findById(id))
                    .isInstanceOf(GiftCertificateNotFoundException.class);
        }
    }

    @Nested
    class GiftCertificateServiceImplSaveTest {

        private GiftCertificateRequest giftCertificateRequest;
        private GiftCertificateResponse giftCertificateResponse;

        @BeforeEach
        void setUp() {
            giftCertificateRequest = GiftCertificateRequestTestBuilder.giftCertificateRequest().build();
            giftCertificateResponse =  GiftCertificateResponseTestBuilder.giftCertificateResponse().build();
            GiftCertificate giftCertificate = GiftCertificateTestBuilder.giftCertificate().build();
            doReturn(giftCertificate).when(giftCertificateMapper).mapToGiftCertificate(giftCertificateRequest);
            doReturn(giftCertificate).when(giftCertificateRepository).save(giftCertificate);
            doReturn(giftCertificateResponse).when(giftCertificateMapper).mapToGiftCertificateResponse(giftCertificate);
        }

        @Test
        void checkSaveShouldReturnGiftCertificate() {
            GiftCertificateResponse actualGiftCertificateResponse = giftCertificateService.save(giftCertificateRequest);

            assertThat(actualGiftCertificateResponse).isEqualTo(giftCertificateResponse);
        }

        @Test
        void checkSaveShouldReturnEqualCreateDateAndLastUpdateDate() {
            GiftCertificateResponse actualGiftCertificateResponse = giftCertificateService.save(giftCertificateRequest);

            assertThat(actualGiftCertificateResponse.getCreateDate())
                    .isCloseTo(giftCertificateResponse.getLastUpdateDate(), within(50, ChronoUnit.MILLIS));
        }
    }

    @Nested
    class GiftCertificateServiceImplUpdateTest {

        private Long id;
        private TagRequest tagRequest;
        private Tag tag;
        private GiftCertificate giftCertificate;
        private GiftCertificate updatedGiftCertificate;
        private GiftCertificateRequest giftCertificateRequest;
        private GiftCertificateResponse giftCertificateResponse;

        @BeforeEach
        void setUp() {
            tagRequest = TagRequestTestBuilder.tagRequest()
                    .withName("spring")
                    .build();
            tag = TagTestBuilder.tag()
                    .withName("spring")
                    .build();
            TagResponse tagResponse = TagResponseTestBuilder.tagResponse()
                    .withName("spring")
                    .build();
            giftCertificate = GiftCertificateTestBuilder.giftCertificate()
                    .withName("Spring")
                    .withDescription("Spring course for beginner")
                    .withPrice(BigDecimal.valueOf(10.0))
                    .withDuration(180)
                    .build();
            updatedGiftCertificate = GiftCertificateTestBuilder.giftCertificate()
                    .withName("Spring course")
                    .withDescription("Spring course for advanced")
                    .withPrice(BigDecimal.valueOf(25.0))
                    .withDuration(365)
                    .withTags(List.of(tag))
                    .build();
            id = 1L;
            giftCertificateResponse = GiftCertificateResponseTestBuilder.giftCertificateResponse()
                    .withName("Spring course")
                    .withDescription("Spring course for advanced")
                    .withPrice(BigDecimal.valueOf(25.0))
                    .withDuration(365)
                    .withTags(List.of(tagResponse))
                    .build();
            giftCertificateRequest = GiftCertificateRequestTestBuilder.giftCertificateRequest()
                    .withName("Spring course")
                    .withDescription("Spring course for advanced")
                    .withPrice(BigDecimal.valueOf(25.0))
                    .withDuration(365)
                    .withTags(List.of(tagRequest))
                    .build();
        }

        @Test
        void checkUpdateShouldReturnGiftCertificate() {
            doReturn(Optional.of(giftCertificate)).when(giftCertificateRepository).findById(id);
            doReturn(tag).when(tagMapper).mapToTag(tagRequest);
            doReturn(giftCertificateResponse).when(giftCertificateMapper).mapToGiftCertificateResponse(updatedGiftCertificate);
            doReturn(updatedGiftCertificate).when(giftCertificateRepository).update(giftCertificate);

            GiftCertificateResponse actualGiftCertificateResponse = giftCertificateService.update(id, giftCertificateRequest);

            assertThat(actualGiftCertificateResponse).isEqualTo(giftCertificateResponse);
        }

        @Test
        void checkUpdateShouldReturnNewLastUpdateDate() {
            doReturn(Optional.of(giftCertificate)).when(giftCertificateRepository).findById(id);
            doReturn(tag).when(tagMapper).mapToTag(tagRequest);
            doReturn(giftCertificateResponse).when(giftCertificateMapper).mapToGiftCertificateResponse(updatedGiftCertificate);
            doReturn(updatedGiftCertificate).when(giftCertificateRepository).update(giftCertificate);

            GiftCertificateResponse actualGiftCertificateResponse = giftCertificateService.update(id, giftCertificateRequest);

            assertThat(actualGiftCertificateResponse.getLastUpdateDate())
                    .isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS));
        }

        @Test
        void checkUpdateShouldThrowsGiftCertificateNotFoundException() {
            doReturn(Optional.empty()).when(giftCertificateRepository).findById(id);

            assertThatThrownBy(() -> giftCertificateService.update(id, giftCertificateRequest))
                    .isInstanceOf(GiftCertificateNotFoundException.class);
        }
    }

    @Nested
    class GiftCertificateServiceImplDeleteByIdTest {

        @Test
        void checkDeleteByIdShouldCall() {
            Long id = 1L;
            GiftCertificate giftCertificate = GiftCertificateTestBuilder.giftCertificate().build();
            doReturn(Optional.of(giftCertificate)).when(giftCertificateRepository).findById(id);

            giftCertificateService.deleteById(id);

            verify(giftCertificateRepository).deleteById(id);
        }

        @Test
        void checkDeleteByIdShouldThrowsGiftCertificateNotFoundException() {
            Long id = 1L;
            doReturn(Optional.empty()).when(giftCertificateRepository).findById(id);

            assertThatThrownBy(() -> giftCertificateService.deleteById(id))
                    .isInstanceOf(GiftCertificateNotFoundException.class);
        }
    }
}
