package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import ru.clevertec.ecl.dto.filter.GiftCertificateFilter;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.util.GiftCertificateTestData;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private GiftCertificateMapper giftCertificateMapper;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private GiftCertificateServiceImpl giftCertificateService;

    @Nested
    class GiftCertificateServiceImplFindAllTest {

        private GiftCertificateFilter giftCertificateFilter;
        private Pageable pageable;
        private GiftCertificate giftCertificate;
        private Example<GiftCertificate> giftCertificateExample;

        @BeforeEach
        void setUp() throws IOException {
            giftCertificateFilter = GiftCertificateTestData.buildGiftCertificateFilter();
            pageable = Pageable.unpaged();
            ExampleMatcher tagMatcher = ExampleMatcher.matching()
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                    .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                    .withMatcher("price", ExampleMatcher.GenericPropertyMatchers.exact())
                    .withMatcher("duration", ExampleMatcher.GenericPropertyMatchers.exact());
            giftCertificate = GiftCertificate.builder()
                    .name(giftCertificateFilter.getName())
                    .description(giftCertificateFilter.getDescription())
                    .price(giftCertificateFilter.getPrice())
                    .duration(giftCertificateFilter.getDuration())
                    .build();
            giftCertificateExample = Example.of(giftCertificate, tagMatcher);
        }

        @Test
        void checkFindAllShouldReturnSize2() throws IOException {
            int expectedSize = 2;
            List<GiftCertificate> giftCertificates = GiftCertificateTestData.buildGiftCertificates();
            Page<GiftCertificate> giftCertificatePage = new PageImpl<>(giftCertificates);
            List<GiftCertificateResponse> giftCertificateResponses = GiftCertificateTestData.buildGiftCertificateResponses();

            doReturn(giftCertificate).when(giftCertificateMapper).mapToGiftCertificate(giftCertificateFilter);
            doReturn(giftCertificatePage).when(giftCertificateRepository).findAll(giftCertificateExample, pageable);
            doReturn(giftCertificateResponses).when(giftCertificateMapper).mapToGiftCertificateResponses(giftCertificates);

            List<GiftCertificateResponse> actualGiftCertificateResponses = giftCertificateService.findAll(giftCertificateFilter, pageable);

            assertThat(actualGiftCertificateResponses).hasSize(expectedSize);
        }

        @Test
        void checkFindAllShouldReturnGiftCertificateResponses() throws IOException {
            List<GiftCertificate> giftCertificates = GiftCertificateTestData.buildGiftCertificates();
            Page<GiftCertificate> giftCertificatePage = new PageImpl<>(giftCertificates);
            List<GiftCertificateResponse> giftCertificateResponses = GiftCertificateTestData.buildGiftCertificateResponses();

            doReturn(giftCertificate).when(giftCertificateMapper).mapToGiftCertificate(giftCertificateFilter);
            doReturn(giftCertificatePage).when(giftCertificateRepository).findAll(giftCertificateExample, pageable);
            doReturn(giftCertificateResponses).when(giftCertificateMapper).mapToGiftCertificateResponses(giftCertificates);

            List<GiftCertificateResponse> actualGiftCertificateResponses = giftCertificateService.findAll(giftCertificateFilter, pageable);

            assertThat(actualGiftCertificateResponses).isEqualTo(giftCertificateResponses);
        }

        @Test
        void checkFindAllShouldReturnEmptyGiftCertificateResponses() {
            doReturn(giftCertificate).when(giftCertificateMapper).mapToGiftCertificate(giftCertificateFilter);
            doReturn(Page.empty()).when(giftCertificateRepository).findAll(giftCertificateExample, pageable);

            List<GiftCertificateResponse> actualGiftCertificateResponses = giftCertificateService.findAll(giftCertificateFilter, pageable);

            assertThat(actualGiftCertificateResponses).isEmpty();
        }
    }

    @Nested
    class GiftCertificateServiceImplFindAllByTagNameTest {

        private Pageable pageable;
        private String tagName;

        @BeforeEach
        void setUp() {
            pageable = Pageable.unpaged();
            tagName = "relax";
        }

        @Test
        void checkFindAllByTagNameShouldReturnSize2() throws IOException {
            int expectedSize = 2;
            List<GiftCertificate> giftCertificates = GiftCertificateTestData.buildGiftCertificates();
            Page<GiftCertificate> giftCertificatePage = new PageImpl<>(giftCertificates);
            List<GiftCertificateResponse> giftCertificateResponses = GiftCertificateTestData.buildGiftCertificateResponses();

            doReturn(giftCertificatePage).when(giftCertificateRepository).findAllByTagsName(tagName, pageable);
            doReturn(giftCertificateResponses).when(giftCertificateMapper).mapToGiftCertificateResponses(giftCertificates);

            List<GiftCertificateResponse> actualGiftCertificateResponses = giftCertificateService.findAllByTagName(tagName, pageable);

            assertThat(actualGiftCertificateResponses).hasSize(expectedSize);
        }

        @Test
        void checkFindAllByTagNameShouldReturnGiftCertificateResponses() throws IOException {
            List<GiftCertificate> giftCertificates = GiftCertificateTestData.buildGiftCertificates();
            Page<GiftCertificate> giftCertificatePage = new PageImpl<>(giftCertificates);
            List<GiftCertificateResponse> giftCertificateResponses = GiftCertificateTestData.buildGiftCertificateResponses();

            doReturn(giftCertificatePage).when(giftCertificateRepository).findAllByTagsName(tagName, pageable);
            doReturn(giftCertificateResponses).when(giftCertificateMapper).mapToGiftCertificateResponses(giftCertificates);

            List<GiftCertificateResponse> actualGiftCertificateResponses = giftCertificateService.findAllByTagName(tagName, pageable);

            assertThat(actualGiftCertificateResponses).isEqualTo(giftCertificateResponses);
        }

        @Test
        void checkFindAllByTagNameShouldReturnEmptyGiftCertificateResponses() {
            doReturn(Page.empty()).when(giftCertificateRepository).findAllByTagsName(tagName, pageable);

            List<GiftCertificateResponse> actualGiftCertificateResponses = giftCertificateService.findAllByTagName(tagName, pageable);

            assertThat(actualGiftCertificateResponses).isEmpty();
        }
    }

    @Nested
    class GiftCertificateServiceImplFindAllByPartNameOrDescriptionTest {

        private Pageable pageable;
        private String part;

        @BeforeEach
        void setUp() {
            pageable = Pageable.unpaged();
            part = "gift";
        }

        @Test
        void checkFindAllByPartNameOrDescriptionShouldReturnSize2() throws IOException {
            int expectedSize = 2;
            List<GiftCertificate> giftCertificates = GiftCertificateTestData.buildGiftCertificates();
            Page<GiftCertificate> giftCertificatePage = new PageImpl<>(giftCertificates);
            List<GiftCertificateResponse> giftCertificateResponses = GiftCertificateTestData.buildGiftCertificateResponses();

            doReturn(giftCertificatePage).when(giftCertificateRepository)
                    .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(part, part, pageable);
            doReturn(giftCertificateResponses).when(giftCertificateMapper).mapToGiftCertificateResponses(giftCertificates);

            List<GiftCertificateResponse> actualGiftCertificateResponses = giftCertificateService
                    .findAllByPartNameOrDescription(part, pageable);

            assertThat(actualGiftCertificateResponses).hasSize(expectedSize);
        }

        @Test
        void checkFindAllByByPartNameOrDescriptionShouldReturnGiftCertificateResponses() throws IOException {
            List<GiftCertificate> giftCertificates = GiftCertificateTestData.buildGiftCertificates();
            Page<GiftCertificate> giftCertificatePage = new PageImpl<>(giftCertificates);
            List<GiftCertificateResponse> giftCertificateResponses = GiftCertificateTestData.buildGiftCertificateResponses();

            doReturn(giftCertificatePage).when(giftCertificateRepository)
                    .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(part, part, pageable);
            doReturn(giftCertificateResponses).when(giftCertificateMapper).mapToGiftCertificateResponses(giftCertificates);

            List<GiftCertificateResponse> actualGiftCertificateResponses = giftCertificateService
                    .findAllByPartNameOrDescription(part, pageable);

            assertThat(actualGiftCertificateResponses).isEqualTo(giftCertificateResponses);
        }

        @Test
        void checkFindAllByByPartNameOrDescriptionShouldReturnEmptyGiftCertificateResponses() {
            doReturn(Page.empty()).when(giftCertificateRepository)
                    .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(part, part, pageable);

            List<GiftCertificateResponse> actualGiftCertificateResponses = giftCertificateService
                    .findAllByPartNameOrDescription(part, pageable);

            assertThat(actualGiftCertificateResponses).isEmpty();
        }
    }

    @Nested
    class GiftCertificateServiceImplFindByIdTest {

        @Test
        void checkFindByIdShouldReturnGiftCertificateResponse() throws IOException {
            GiftCertificate giftCertificate = GiftCertificateTestData.buildBeautyGiftCertificate();
            GiftCertificateResponse giftCertificateResponse = GiftCertificateTestData.buildBeautyGiftCertificateResponse();

            doReturn(Optional.of(giftCertificate)).when(giftCertificateRepository).findById(giftCertificate.getId());
            doReturn(giftCertificateResponse).when(giftCertificateMapper).mapToGiftCertificateResponse(giftCertificate);

            GiftCertificateResponse actualGiftCertificateResponse = giftCertificateService.findById(giftCertificate.getId());

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

        @Test
        void checkSaveShouldReturnGiftCertificateResponse() throws IOException {
            GiftCertificateRequest giftCertificateRequest = GiftCertificateTestData.buildBeautyGiftCertificateRequest();
            GiftCertificateResponse giftCertificateResponse =  GiftCertificateTestData.buildBeautyGiftCertificateResponse();
            GiftCertificate giftCertificate = GiftCertificateTestData.buildBeautyGiftCertificate();

            doReturn(giftCertificate).when(giftCertificateMapper).mapToGiftCertificate(giftCertificateRequest);
            doReturn(giftCertificate).when(giftCertificateRepository).save(giftCertificate);
            doReturn(giftCertificateResponse).when(giftCertificateMapper).mapToGiftCertificateResponse(giftCertificate);

            GiftCertificateResponse actualGiftCertificateResponse = giftCertificateService.save(giftCertificateRequest);

            assertThat(actualGiftCertificateResponse).isEqualTo(giftCertificateResponse);
        }
    }

    @Nested
    class GiftCertificateServiceImplUpdateTest {

        private Long id;
        private GiftCertificate giftCertificate;
        private GiftCertificate updatedGiftCertificate;
        private GiftCertificateRequest giftCertificateRequest;
        private GiftCertificateResponse giftCertificateResponse;

        @BeforeEach
        void setUp() throws IOException {
            giftCertificate = GiftCertificateTestData.buildHealthGiftCertificate();
            updatedGiftCertificate = GiftCertificateTestData.buildBeautyGiftCertificate();
            id = 1L;
            giftCertificateResponse = GiftCertificateTestData.buildBeautyGiftCertificateResponse();
            giftCertificateRequest = GiftCertificateTestData.buildBeautyGiftCertificateRequest();
        }

        @Test
        void checkUpdateShouldReturnGiftCertificate() {
            doReturn(Optional.of(giftCertificate)).when(giftCertificateRepository).findById(id);
            doReturn(giftCertificateResponse).when(giftCertificateMapper).mapToGiftCertificateResponse(updatedGiftCertificate);
            doReturn(updatedGiftCertificate).when(giftCertificateRepository).save(giftCertificate);

            GiftCertificateResponse actualGiftCertificateResponse = giftCertificateService.update(id, giftCertificateRequest);

            assertThat(actualGiftCertificateResponse).isEqualTo(giftCertificateResponse);
        }

        @Test
        void checkUpdateShouldReturnNewLastUpdateDateGiftCertificate() {
            doReturn(Optional.of(giftCertificate)).when(giftCertificateRepository).findById(id);
            doReturn(giftCertificateResponse).when(giftCertificateMapper).mapToGiftCertificateResponse(updatedGiftCertificate);
            doReturn(updatedGiftCertificate).when(giftCertificateRepository).save(giftCertificate);

            GiftCertificateResponse actualGiftCertificateResponse = giftCertificateService.update(id, giftCertificateRequest);

            assertThat(actualGiftCertificateResponse.getLastUpdateDate()).isAfter(giftCertificate.getLastUpdateDate());
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
        void checkDeleteByIdShouldCallDeleteById() throws IOException {
            Long id = 1L;
            GiftCertificate giftCertificate = GiftCertificateTestData.buildBeautyGiftCertificate();

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
