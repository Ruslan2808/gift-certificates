package ru.clevertec.ecl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.clevertec.ecl.dto.filter.GiftCertificateFilter;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.service.impl.GiftCertificateServiceImpl;
import ru.clevertec.ecl.util.GiftCertificateTestData;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(GiftCertificateController.class)
class GiftCertificateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GiftCertificateServiceImpl giftCertificateService;

    @Nested
    class GiftCertificateControllerGetAllTest {

        @Test
        void checkGetAllShouldReturnStatusOkAndSize2() throws Exception {
            int expectedSize = 2;
            List<GiftCertificateResponse> giftCertificateResponses = GiftCertificateTestData.buildGiftCertificateResponses();

            doReturn(giftCertificateResponses)
                    .when(giftCertificateService)
                    .findAll(any(GiftCertificateFilter.class), any(Pageable.class));

            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @Test
        void checkGetAllShouldReturnStatusOkAndGiftCertificateResponses() throws Exception {
            List<GiftCertificateResponse> giftCertificateResponses = GiftCertificateTestData.buildGiftCertificateResponses();
            String expectedResponse = objectMapper.writeValueAsString(giftCertificateResponses);

            doReturn(giftCertificateResponses)
                    .when(giftCertificateService)
                    .findAll(any(GiftCertificateFilter.class), any(Pageable.class));

            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }

        @Test
        void checkGetAllShouldReturnStatusOkAndEmptyGiftCertificateResponses() throws Exception {
            doReturn(Collections.emptyList())
                    .when(giftCertificateService)
                    .findAll(any(GiftCertificateFilter.class), any(Pageable.class));

            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.keys()").isEmpty());
        }
    }

    @Nested
    class GiftCertificateControllerGetAllByTagNameTest {

        @Test
        void checkGetAllByTagNameShouldReturnStatusOkAndSize2() throws Exception {
            int expectedSize = 2;
            String tagName = "relax";
            List<GiftCertificateResponse> giftCertificateResponses = GiftCertificateTestData.buildGiftCertificateResponses();

            doReturn(giftCertificateResponses)
                    .when(giftCertificateService)
                    .findAllByTagName(anyString(), any(Pageable.class));

            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tagName", tagName))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @Test
        void checkGetAllByTagNameShouldReturnStatusOkAndGiftCertificateResponses() throws Exception {
            String tagName = "relax";
            List<GiftCertificateResponse> giftCertificateResponses = GiftCertificateTestData.buildGiftCertificateResponses();
            String expectedResponse = objectMapper.writeValueAsString(giftCertificateResponses);

            doReturn(giftCertificateResponses)
                    .when(giftCertificateService)
                    .findAllByTagName(anyString(), any(Pageable.class));

            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tagName", tagName))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }

        @Test
        void checkGetAllByTagNameShouldReturnStatusOkAndEmptyGiftCertificateResponses() throws Exception {
            String tagName = "relax";

            doReturn(Collections.emptyList())
                    .when(giftCertificateService)
                    .findAllByTagName(anyString(), any(Pageable.class));

            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("tagName", tagName))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.keys()").isEmpty());
        }
    }

    @Nested
    class GiftCertificateControllerGetAllByPartOrDescriptionTest {

        @Test
        void checkGetAllByPartNameOrDescriptionShouldReturnStatusOkAndSize2() throws Exception {
            int expectedSize = 2;
            String part = "gift";
            List<GiftCertificateResponse> giftCertificateResponses = GiftCertificateTestData.buildGiftCertificateResponses();

            doReturn(giftCertificateResponses)
                    .when(giftCertificateService)
                    .findAllByPartNameOrDescription(anyString(), any(Pageable.class));

            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("part", part))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @Test
        void checkGetAllByPartNameOrDescriptionShouldReturnStatusOkAndGiftCertificateResponses() throws Exception {
            String part = "gift";
            List<GiftCertificateResponse> giftCertificateResponses = GiftCertificateTestData.buildGiftCertificateResponses();
            String expectedResponse = objectMapper.writeValueAsString(giftCertificateResponses);

            doReturn(giftCertificateResponses)
                    .when(giftCertificateService)
                    .findAllByPartNameOrDescription(anyString(), any(Pageable.class));

            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("part", part))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }

        @Test
        void checkGetAllByPartNameOrDescriptionShouldReturnStatusOkAndEmptyGiftCertificateResponses() throws Exception {
            String part = "gift";

            doReturn(Collections.emptyList())
                    .when(giftCertificateService)
                    .findAllByPartNameOrDescription(anyString(), any(Pageable.class));

            mockMvc.perform(get("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("part", part))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.keys()").isEmpty());
        }
    }

    @Nested
    class GiftCertificateControllerGetByIdTest {

        @Test
        void checkGetByIdShouldReturnStatusOkAndGiftCertificateResponse() throws Exception {
            GiftCertificateResponse giftCertificateResponse = GiftCertificateTestData.buildBeautyGiftCertificateResponse();
            String expectedResponse = objectMapper.writeValueAsString(giftCertificateResponse);

            doReturn(giftCertificateResponse)
                    .when(giftCertificateService)
                    .findById(giftCertificateResponse.getId());

            mockMvc.perform(get("/api/v1/gift-certificates/{id}", giftCertificateResponse.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundGiftCertificateIds")
        void checkGetByIdShouldReturnStatusNotFoundAndMessageGiftCertificateNotFound(Long id) throws Exception {
            String expectedMessage = "Gift certificate with id = [%d] not found".formatted(id);

            doThrow(new GiftCertificateNotFoundException(expectedMessage))
                    .when(giftCertificateService)
                    .findById(id);

            mockMvc.perform(get("/api/v1/gift-certificates/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<Long> provideNotFoundGiftCertificateIds() {
            return Stream.of(-1L, 0L, 1L, 100L, 1000L);
        }
    }

    @Nested
    class GiftCertificateControllerSaveTest {

        @Test
        void checkSaveShouldReturnStatusCreatedAndGiftCertificateResponse() throws Exception {
            GiftCertificateRequest giftCertificateRequest = GiftCertificateTestData.buildBeautyGiftCertificateRequest();
            GiftCertificateResponse giftCertificateResponse = GiftCertificateTestData.buildBeautyGiftCertificateResponse();
            String expectedResponse = objectMapper.writeValueAsString(giftCertificateResponse);

            doReturn(giftCertificateResponse)
                    .when(giftCertificateService)
                    .save(giftCertificateRequest);

            mockMvc.perform(post("/api/v1/gift-certificates")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(giftCertificateRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(expectedResponse));
        }
    }

    @Nested
    class GiftCertificateControllerUpdateTest {

        @Test
        void checkUpdateShouldReturnStatusOkAndGiftCertificateResponse() throws Exception {
            GiftCertificateRequest giftCertificateRequest = GiftCertificateTestData.buildBeautyGiftCertificateRequest();
            GiftCertificateResponse giftCertificateResponse = GiftCertificateTestData.buildBeautyGiftCertificateResponse();
            String expectedResponse = objectMapper.writeValueAsString(giftCertificateResponse);

            doReturn(giftCertificateResponse)
                    .when(giftCertificateService)
                    .update(giftCertificateResponse.getId(), giftCertificateRequest);

            mockMvc.perform(put("/api/v1/gift-certificates/{id}", giftCertificateResponse.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(giftCertificateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundGiftCertificateIds")
        void checkUpdateShouldReturnStatusNotFoundAndMessageGiftCertificateNotFound(Long id) throws Exception {
            String expectedMessage = "Gift certificate with id = [%d] not found".formatted(id);
            GiftCertificateRequest giftCertificateRequest = GiftCertificateTestData.buildBeautyGiftCertificateRequest();

            doThrow(new GiftCertificateNotFoundException(expectedMessage))
                    .when(giftCertificateService)
                    .update(id, giftCertificateRequest);

            mockMvc.perform(put("/api/v1/gift-certificates/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(giftCertificateRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<Long> provideNotFoundGiftCertificateIds() {
            return Stream.of(-1L, 0L, 1L, 100L, 1000L);
        }
    }

    @Nested
    class GiftCertificateControllerDeleteByIdTest {

        @Test
        void checkDeleteByIdShouldReturnStatusOk() throws Exception {
            Long id = 1L;

            mockMvc.perform(delete("/api/v1/gift-certificates/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundGiftCertificateIds")
        void checkDeleteByIdShouldReturnStatusNotFoundAndMessageTagNotFound(Long id) throws Exception {
            String expectedMessage = "Gift certificate with id = [%d] not found".formatted(id);

            doThrow(new GiftCertificateNotFoundException(expectedMessage))
                    .when(giftCertificateService)
                    .deleteById(id);

            mockMvc.perform(delete("/api/v1/gift-certificates/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        public static Stream<Long> provideNotFoundGiftCertificateIds() {
            return Stream.of(-1L, 0L, 1L, 100L, 1000L);
        }
    }
}
