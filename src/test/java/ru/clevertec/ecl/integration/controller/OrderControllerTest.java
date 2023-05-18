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

import ru.clevertec.ecl.dto.request.OrderRequest;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.util.OrderTestData;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class OrderControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class OrderControllerGetAllTest {

        @Test
        void checkGetAllShouldReturnStatusOkAndSize5() throws Exception {
            int expectedSize = 5;

            mockMvc.perform(get("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @ParameterizedTest
        @MethodSource("provideFilterPricesAndExpectedSizes")
        void checkGetAllShouldReturnSize1(String price, int expectedSize) throws Exception {
            mockMvc.perform(get("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("price", price))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @ParameterizedTest
        @MethodSource("providePaginationParamsAndExpectedNotEmptySizes")
        void checkGetAllShouldReturnTagResponsesOnPage(String page, String size, int expectedSize) throws Exception {
            mockMvc.perform(get("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", page)
                            .param("size", size))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @ParameterizedTest
        @MethodSource("providePaginationParamsWithExpectedEmptySizes")
        void checkGetAllShouldReturnEmptyTagResponsesOnPage(String page, String size) throws Exception {
            mockMvc.perform(get("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", page)
                            .param("size", size))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.keys()").isEmpty());
        }

        @ParameterizedTest
        @MethodSource("providePaginationParamsAndFilterPricesAndExpectedNotEmptySizes")
        void checkGetAllShouldReturnTwoOrderResponsesOnFirstPageWithFilter(String orderPrice,
                                                                           String page,
                                                                           String size,
                                                                           int expectedSize) throws Exception {
            mockMvc.perform(get("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("price", orderPrice)
                            .param("page", page)
                            .param("size", size))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        private static Stream<Arguments> provideFilterPricesAndExpectedSizes() {
            return Stream.of(
                    arguments("480", 3),
                    arguments("360", 1),
                    arguments("250", 1)
            );
        }

        private static Stream<Arguments> providePaginationParamsAndExpectedNotEmptySizes() {
            return Stream.of(
                    arguments("0", "1", 1),
                    arguments("0", "2", 2),
                    arguments("0", "3", 3),
                    arguments("0", "4", 4),
                    arguments("0", "5", 5),
                    arguments("1", "1", 1),
                    arguments("1", "2", 2),
                    arguments("2", "1", 1),
                    arguments("3", "1", 1),
                    arguments("4", "1", 1)
            );
        }

        private static Stream<Arguments> providePaginationParamsWithExpectedEmptySizes() {
            return Stream.of(
                    arguments("0", "6"),
                    arguments("1", "5"),
                    arguments("2", "4"),
                    arguments("3", "3"),
                    arguments("4", "2"),
                    arguments("5", "1")
            );
        }

        private static Stream<Arguments> providePaginationParamsAndFilterPricesAndExpectedNotEmptySizes() {
            return Stream.of(
                    arguments("480", "0", "1", 1),
                    arguments("480", "0", "2", 2),
                    arguments("480", "0", "3", 3),
                    arguments("480", "1", "1", 1),
                    arguments("480", "1", "2", 1),
                    arguments("480", "2", "1", 1)
            );
        }
    }

    @Nested
    class OrderControllerGetByIdTest {

        @ParameterizedTest
        @MethodSource("provideOrderIds")
        void checkGetByIdShouldReturnStatusOkAndNotEmptyOrderResponse(Long id) throws Exception {
            mockMvc.perform(get("/api/v1/orders/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isNotBlank());
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundOrderIds")
        void checkGetByIdShouldReturnStatusNotFoundAndMessageOrderNotFound(Long id) throws Exception {
            String expectedMessage = "Order with id = [%d] not found".formatted(id);

            mockMvc.perform(get("/api/v1/orders/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<Long> provideOrderIds() {
            return Stream.of(1L, 2L, 3L, 4L, 5L);
        }

        private static Stream<Long> provideNotFoundOrderIds() {
            return Stream.of(-1L, 0L, 10L, 100L, 1000L);
        }
    }

    @Nested
    class OrderControllerSaveTest {

        @Test
        void checkSaveShouldReturnStatusCreatedAndNotEmptyOrderResponse() throws Exception {
            OrderRequest orderRequest = OrderTestData.buildBeautyOrderRequest();

            mockMvc.perform(post("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isNotBlank());
        }

        @ParameterizedTest
        @MethodSource("provideUserRequestsWithNotFoundUserIds")
        void checkSaveShouldReturnStatusNotFoundAndMessageUserNotFound(OrderRequest orderRequest) throws Exception {
            String expectedMessage = "User with id = [%d] not found".formatted(orderRequest.getUserId());

            mockMvc.perform(post("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        @ParameterizedTest
        @MethodSource("provideUserRequestsWithNotFoundGiftCertificateIds")
        void checkSaveShouldReturnStatusNotFoundAndMessageGiftCertificateNotFound(OrderRequest orderRequest) throws Exception {
            String expectedMessage = "Gift certificate with id = [%d] not found".formatted(orderRequest.getGiftCertificateId());

            mockMvc.perform(post("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        @ParameterizedTest
        @MethodSource("provideNotValidOrderRequests")
        void checkSaveShouldReturnStatusBadRequestAndErrorMessage(OrderRequest orderRequest,
                                                                  String expectedMessage) throws Exception {
            mockMvc.perform(post("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<OrderRequest> provideUserRequestsWithNotFoundUserIds() {
            return Stream.of(
                    OrderRequest.builder().userId(10L).giftCertificateId(1L).build(),
                    OrderRequest.builder().userId(100L).giftCertificateId(2L).build()
            );
        }

        private static Stream<OrderRequest> provideUserRequestsWithNotFoundGiftCertificateIds() {
            return Stream.of(
                    OrderRequest.builder().userId(1L).giftCertificateId(10L).build(),
                    OrderRequest.builder().userId(2L).giftCertificateId(100L).build()
            );
        }

        private static Stream<Arguments> provideNotValidOrderRequests() {
            return Stream.of(
                    arguments(OrderRequest.builder().userId(null).giftCertificateId(1L).build(), "User id cannot be null"),
                    arguments(OrderRequest.builder().userId(0L).giftCertificateId(1L).build(), "User id must be positive"),
                    arguments(OrderRequest.builder().userId(-1L).giftCertificateId(1L).build(), "User id must be positive"),
                    arguments(OrderRequest.builder().userId(1L).giftCertificateId(null).build(), "Gift certificate id cannot be null"),
                    arguments(OrderRequest.builder().userId(1L).giftCertificateId(0L).build(), "Gift certificate id must be positive"),
                    arguments(OrderRequest.builder().userId(1L).giftCertificateId(-1L).build(), "Gift certificate id must be positive")
            );
        }
    }
}
