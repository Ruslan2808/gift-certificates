package ru.clevertec.ecl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.clevertec.ecl.dto.filter.OrderFilter;
import ru.clevertec.ecl.dto.request.OrderRequest;
import ru.clevertec.ecl.dto.response.OrderResponse;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.exception.OrderNotFoundException;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.service.impl.OrderServiceImpl;
import ru.clevertec.ecl.util.OrderTestData;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderServiceImpl orderService;

    @Nested
    class OrderControllerGetAllTest {

        @Test
        void checkGetAllShouldReturnStatusOkAndSize2() throws Exception {
            int expectedSize = 2;
            List<OrderResponse> orderResponses = OrderTestData.buildOrderResponses();

            doReturn(orderResponses)
                    .when(orderService)
                    .findAll(any(OrderFilter.class), any(Pageable.class));

            mockMvc.perform(get("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @Test
        void checkGetAllShouldReturnStatusOkAndOrderResponses() throws Exception {
            List<OrderResponse> orderResponses = OrderTestData.buildOrderResponses();
            String expectedResponse = objectMapper.writeValueAsString(orderResponses);

            doReturn(orderResponses)
                    .when(orderService)
                    .findAll(any(OrderFilter.class), any(Pageable.class));

            mockMvc.perform(get("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }

        @Test
        void checkGetAllShouldReturnStatusOkAndEmptyOrderResponses() throws Exception {
            doReturn(Collections.emptyList())
                    .when(orderService)
                    .findAll(any(OrderFilter.class), any(Pageable.class));

            mockMvc.perform(get("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.keys()").isEmpty());
        }
    }

    @Nested
    class OrderControllerGetByIdTest {

        @Test
        void checkGetByIdShouldReturnStatusOkAndOrderResponse() throws Exception {
            OrderResponse orderResponse = OrderTestData.buildBeautyOrderResponse();
            String expectedResponse = objectMapper.writeValueAsString(orderResponse);

            doReturn(orderResponse)
                    .when(orderService)
                    .findById(orderResponse.getId());

            mockMvc.perform(get("/api/v1/orders/{id}", orderResponse.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundOrderIds")
        void checkGetByIdShouldReturnStatusNotFoundAndMessageOrderNotFound(Long id) throws Exception {
            String expectedMessage = "Order with id = [%d] not found".formatted(id);

            doThrow(new OrderNotFoundException(expectedMessage))
                    .when(orderService)
                    .findById(anyLong());

            mockMvc.perform(get("/api/v1/orders/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<Long> provideNotFoundOrderIds() {
            return Stream.of(-1L, 0L, 1L, 100L, 1000L);
        }
    }

    @Nested
    class OrderControllerSaveTest {

        @Test
        void checkSaveShouldReturnStatusCreatedAndOrderResponse() throws Exception {
            OrderRequest orderRequest = OrderTestData.buildBeautyOrderRequest();
            OrderResponse orderResponse = OrderTestData.buildBeautyOrderResponse();
            String expectedResponse = objectMapper.writeValueAsString(orderResponse);

            doReturn(orderResponse)
                    .when(orderService)
                    .save(orderRequest);

            mockMvc.perform(post("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(expectedResponse));
        }

        @ParameterizedTest
        @MethodSource("provideOrderRequestWithNotFoundIds")
        void checkSaveShouldReturnStatusNotFoundAndMessageUserNotFound(OrderRequest orderRequest) throws Exception {
            String expectedMessage = "User with id = [%d] not found".formatted(orderRequest.getUserId());

            doThrow(new UserNotFoundException(expectedMessage))
                    .when(orderService)
                    .save(orderRequest);

            mockMvc.perform(post("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        @ParameterizedTest
        @MethodSource("provideOrderRequestWithNotFoundIds")
        void checkSaveShouldReturnStatusNotFoundAndMessageGiftCertificateNotFound(OrderRequest orderRequest) throws Exception {
            String expectedMessage = "Gift certificate with id = [%d] not found".formatted(orderRequest.getGiftCertificateId());

            doThrow(new GiftCertificateNotFoundException(expectedMessage))
                    .when(orderService)
                    .save(orderRequest);

            mockMvc.perform(post("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        @ParameterizedTest
        @MethodSource("provideNotValidOrderRequests")
        void checkSaveShouldReturnStatusBadRequestAndErrorMessage(OrderRequest orderRequest, String expectedMessage) throws Exception {
            mockMvc.perform(post("/api/v1/orders")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(orderRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains(expectedMessage)));
        }

        private static Stream<OrderRequest> provideOrderRequestWithNotFoundIds() {
            return Stream.of(
                    OrderRequest.builder().userId(1L).giftCertificateId(1L).build(),
                    OrderRequest.builder().userId(100L).giftCertificateId(100L).build(),
                    OrderRequest.builder().userId(1000L).giftCertificateId(1000L).build()
            );
        }

        private static Stream<Arguments> provideNotValidOrderRequests() {
            return Stream.of(
                    arguments(OrderRequest.builder().userId(null).build(), "User id cannot be null"),
                    arguments(OrderRequest.builder().userId(0L).build(), "User id must be positive"),
                    arguments(OrderRequest.builder().userId(-1L).build(), "User id must be positive"),
                    arguments(OrderRequest.builder().giftCertificateId(null).build(), "Gift certificate id cannot be null"),
                    arguments(OrderRequest.builder().giftCertificateId(0L).build(), "Gift certificate id must be positive"),
                    arguments(OrderRequest.builder().giftCertificateId(-1L).build(), "Gift certificate id must be positive")
            );
        }
    }
}
