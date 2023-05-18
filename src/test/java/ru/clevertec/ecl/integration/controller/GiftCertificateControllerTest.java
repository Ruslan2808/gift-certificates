package ru.clevertec.ecl.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.clevertec.ecl.dto.filter.GiftCertificateFilter;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.util.GiftCertificateTestData;

import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class GiftCertificateControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class GiftCertificateControllerGetAllTest {

        @Test
        void checkGetAllShouldReturnStatusOkAndSize3() throws Exception {
            int expectedSize = 3;

            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @ParameterizedTest
        @MethodSource("provideFilterParamsAndExpectedSizes")
        void checkGetAllShouldReturnStatusOkAndGiftCertificateResponseSizes(GiftCertificateFilter giftCertificateFilter,
                                                                            int expectedSize) throws Exception {
            String price = Objects.nonNull(giftCertificateFilter.getPrice())
                    ? giftCertificateFilter.getPrice().toString()
                    : null;
            String duration = Objects.nonNull(giftCertificateFilter.getDuration())
                    ? giftCertificateFilter.getDuration().toString()
                    : null;

            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("name", giftCertificateFilter.getName())
                            .param("description", giftCertificateFilter.getDescription())
                            .param("price", price)
                            .param("duration", duration))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @ParameterizedTest
        @MethodSource("providePaginationParamsAndExpectedNotEmptySizes")
        void checkGetAllShouldReturnStatusOkAndGiftCertificateResponseSizesPerPageSize(String page,
                                                                                       String size,
                                                                                       int expectedSize) throws Exception {
            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", page)
                            .param("size", size))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @ParameterizedTest
        @MethodSource("providePaginationParamsWithExpectedEmptySizes")
        void checkGetAllShouldReturnStatusOkAndEmptyGiftCertificateResponsesPerPageSize(String page,
                                                                                        String size) throws Exception {
            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", page)
                            .param("size", size))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.keys()").isEmpty());
        }

        @ParameterizedTest
        @MethodSource("providePaginationParamsAndFilterNamesAndExpectedNotEmptySizes")
        void checkGetAllShouldReturnStatusOkAndFilterGiftCertificateResponseSizesPerPageSize(GiftCertificateFilter giftCertificateFilter,
                                                                                             String page,
                                                                                             String size,
                                                                                             int expectedSize) throws Exception {
            String price = Objects.nonNull(giftCertificateFilter.getPrice())
                    ? giftCertificateFilter.getPrice().toString()
                    : null;
            String duration = Objects.nonNull(giftCertificateFilter.getDuration())
                    ? giftCertificateFilter.getDuration().toString()
                    : null;

            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("name", giftCertificateFilter.getName())
                            .param("description", giftCertificateFilter.getDescription())
                            .param("price", price)
                            .param("duration", duration)
                            .param("page", page)
                            .param("size", size))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        private static Stream<Arguments> provideFilterParamsAndExpectedSizes() {
            return Stream.of(
                    arguments(GiftCertificateFilter.builder()
                            .name("bea")
                            .build(), 1),
                    arguments(GiftCertificateFilter.builder()
                            .name("mas")
                            .build(), 1),
                    arguments(GiftCertificateFilter.builder()
                            .description("gift")
                            .build(), 3),
                    arguments(GiftCertificateFilter.builder()
                            .description("gift certificate")
                            .build(), 3),
                    arguments(GiftCertificateFilter.builder()
                            .duration(365)
                            .build(), 2)
            );
        }

        private static Stream<Arguments> providePaginationParamsAndExpectedNotEmptySizes() {
            return Stream.of(
                    arguments("0", "1", 1),
                    arguments("0", "2", 2),
                    arguments("0", "3", 3),
                    arguments("1", "1", 1),
                    arguments("2", "1", 1)
            );
        }

        private static Stream<Arguments> providePaginationParamsWithExpectedEmptySizes() {
            return Stream.of(
                    arguments("0", "4"),
                    arguments("1", "3"),
                    arguments("2", "2"),
                    arguments("3", "1")
            );
        }

        private static Stream<Arguments> providePaginationParamsAndFilterNamesAndExpectedNotEmptySizes() {
            return Stream.of(
                    arguments(GiftCertificateFilter.builder()
                            .description("gift")
                            .build(),
                            "0", "1", 1),
                    arguments(GiftCertificateFilter.builder()
                            .description("gift")
                            .build(),
                            "0", "2", 2),
                    arguments(GiftCertificateFilter.builder()
                            .description("gift")
                            .build(),
                            "0", "3", 3),
                    arguments(GiftCertificateFilter.builder()
                            .description("gift")
                            .build(),
                            "1", "1", 1),
                    arguments(GiftCertificateFilter.builder()
                            .description("gift")
                            .build(),
                            "2", "1", 1),
                    arguments(GiftCertificateFilter.builder()
                            .description("gift certificate")
                            .build(),
                            "0", "1", 1),
                    arguments(GiftCertificateFilter.builder()
                            .description("gift certificate")
                            .build(),
                            "0", "2", 2),
                    arguments(GiftCertificateFilter.builder()
                            .description("gift certificate")
                            .build(),
                            "0", "3", 3),
                    arguments(GiftCertificateFilter.builder()
                            .description("gift certificate")
                            .build(),
                            "1", "1", 1),
                    arguments(GiftCertificateFilter.builder()
                            .description("gift certificate")
                            .build(),
                            "2", "1", 1),
                    arguments(GiftCertificateFilter.builder()
                            .duration(365)
                            .build(),
                            "0", "1", 1),
                    arguments(GiftCertificateFilter.builder()
                            .duration(365)
                            .build(),
                            "0", "2", 2),
                    arguments(GiftCertificateFilter.builder()
                            .duration(365)
                            .build(),
                            "1", "1", 1)
            );
        }
    }

    @Nested
    class GiftCertificateControllerGetAllByTagNameTest {

        @ParameterizedTest
        @MethodSource("provideTagNamesAndExpectedNotEmptySizes")
        void checkGetAllByTagNameShouldReturnStatusOkAndSize2(String tagName, int expectedSize) throws Exception {
            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tagName", tagName))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @ParameterizedTest
        @MethodSource("provideTagNamesWithExpectedEmptySizes")
        void checkGetAllShouldReturnStatusOkAndEmptyGiftCertificateResponsesWithTagName(String tagName) throws Exception {
            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tagName", tagName))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.keys()").isEmpty());
        }

        private static Stream<Arguments> provideTagNamesAndExpectedNotEmptySizes() {
            return Stream.of(
                    arguments("beauty", 2),
                    arguments("relax", 3),
                    arguments("massage", 2)
            );
        }

        private static Stream<String> provideTagNamesWithExpectedEmptySizes() {
            return Stream.of("spa", "cool", "certificate");
        }
    }

    @Nested
    class GiftCertificateControllerGetAllByPartOrDescriptionTest {

        @ParameterizedTest
        @MethodSource("providePartsNameOrDescriptionAndExpectedNotEmptySizes")
        void checkGetAllByTagNameShouldReturnStatusOkAndSize2(String part, int expectedSize) throws Exception {
            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("part", part))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @ParameterizedTest
        @MethodSource("providePartsWithExpectedEmptySizes")
        void checkGetAllShouldReturnStatusOkAndEmptyGiftCertificateResponsesWithTagName(String part) throws Exception {
            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("part", part))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.keys()").isEmpty());
        }

        private static Stream<Arguments> providePartsNameOrDescriptionAndExpectedNotEmptySizes() {
            return Stream.of(
                    arguments("bea", 1),
                    arguments("beauty", 1),
                    arguments("spa", 1),
                    arguments("mas", 1),
                    arguments("massage", 1),
                    arguments("gift", 3),
                    arguments("GIFT", 3),
                    arguments("gift certificate", 3),
                    arguments("GIFT CERTIFICATE", 3)
            );
        }

        private static Stream<String> providePartsWithExpectedEmptySizes() {
            return Stream.of("relax", "cool", "free");
        }
    }

    @Nested
    class GiftCertificateControllerGetByIdTest {

        @ParameterizedTest
        @MethodSource("provideGiftCertificateIds")
        void checkGetByIdShouldReturnStatusOkAndGiftCertificateResponse(Long id) throws Exception {
            mockMvc.perform(get("/api/v1/gift-certificates/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isNotBlank());
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundGiftCertificateIds")
        void checkGetByIdShouldReturnStatusNotFoundAndMessageGiftCertificateNotFound(Long id) throws Exception {
            String expectedMessage = "Gift certificate with id = [%d] not found".formatted(id);

            mockMvc.perform(get("/api/v1/gift-certificates/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<Long> provideGiftCertificateIds() {
            return Stream.of(1L, 2L, 3L);
        }

        private static Stream<Long> provideNotFoundGiftCertificateIds() {
            return Stream.of(-1L, 0L, 10L, 100L, 1000L);
        }
    }

    @Nested
    class GiftCertificateControllerSaveTest {

        @Test
        void checkSaveShouldReturnStatusCreatedAndNotEmptyGiftCertificateResponse() throws Exception {
            GiftCertificateRequest giftCertificateRequest = GiftCertificateTestData.buildBeautyGiftCertificateRequest();

            mockMvc.perform(post("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(giftCertificateRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isNotBlank());
        }
    }

    @Nested
    class GiftCertificateControllerUpdateTest {

        @Test
        void checkUpdateShouldReturnStatusOkAndGiftNotEmptyCertificateResponse() throws Exception {
            Long id = 2L;
            GiftCertificateRequest giftCertificateRequest = GiftCertificateTestData.buildBeautyGiftCertificateRequest();

            mockMvc.perform(put("/api/v1/gift-certificates/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(giftCertificateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isNotBlank());
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundGiftCertificateIds")
        void checkUpdateShouldReturnStatusNotFoundAndMessageGiftCertificateNotFound(Long id) throws Exception {
            String expectedMessage = "Gift certificate with id = [%d] not found".formatted(id);
            GiftCertificateRequest giftCertificateRequest = GiftCertificateTestData.buildBeautyGiftCertificateRequest();

            mockMvc.perform(put("/api/v1/gift-certificates/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(giftCertificateRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<Long> provideNotFoundGiftCertificateIds() {
            return Stream.of(-1L, 0L, 10L, 100L, 1000L);
        }
    }

    @Nested
    class GiftCertificateControllerDeleteByIdTest {

        @Test
        void checkDeleteByIdShouldReturnStatusOk() throws Exception {
            Long id = 3L;

            mockMvc.perform(delete("/api/v1/gift-certificates/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundGiftCertificateIds")
        void checkDeleteByIdShouldReturnStatusNotFoundAndMessageTagNotFound(Long id) throws Exception {
            String expectedMessage = "Gift certificate with id = [%d] not found".formatted(id);

            mockMvc.perform(delete("/api/v1/gift-certificates/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        public static Stream<Long> provideNotFoundGiftCertificateIds() {
            return Stream.of(-1L, 0L, 10L, 100L, 1000L);
        }
    }
}
