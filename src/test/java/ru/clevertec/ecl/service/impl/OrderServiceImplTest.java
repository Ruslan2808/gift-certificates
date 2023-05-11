package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import ru.clevertec.ecl.dto.filter.OrderFilter;
import ru.clevertec.ecl.dto.request.OrderRequest;
import ru.clevertec.ecl.dto.response.OrderResponse;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.exception.OrderNotFoundException;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.util.OrderTestData;
import ru.clevertec.ecl.util.UserTestData;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.junit.jupiter.api.Assertions.assertAll;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Nested
    class OrderServiceImplFindAllTest {

        private OrderFilter orderFilter;
        private Pageable pageable;
        private Order order;
        private Example<Order> orderExample;

        @BeforeEach
        void setUp() throws IOException {
            orderFilter = OrderTestData.buildOrderFilter();
            pageable = Pageable.unpaged();
            ExampleMatcher orderMatcher = ExampleMatcher.matching()
                    .withMatcher("price", ExampleMatcher.GenericPropertyMatchers.exact());
            order = Order.builder().price(orderFilter.getPrice()).build();
            orderExample = Example.of(order, orderMatcher);
        }

        @Test
        void checkFindAllShouldReturnSize2() throws IOException {
            int expectedSize = 2;
            List<Order> orders = OrderTestData.buildOrders();
            Page<Order> orderPage = new PageImpl<>(orders);
            List<OrderResponse> orderResponses = OrderTestData.buildOrderResponses();

            doReturn(order).when(orderMapper).mapToOrder(orderFilter);
            doReturn(orderPage).when(orderRepository).findAll(orderExample, pageable);
            doReturn(orderResponses).when(orderMapper).mapToOrderResponses(orders);

            List<OrderResponse> actualOrderResponses = orderService.findAll(orderFilter, pageable);

            assertThat(actualOrderResponses).hasSize(expectedSize);
        }

        @Test
        void checkFindAllShouldReturnOrderResponses() throws IOException {
            List<Order> orders = OrderTestData.buildOrders();
            Page<Order> orderPage = new PageImpl<>(orders);
            List<OrderResponse> orderResponses = OrderTestData.buildOrderResponses();

            doReturn(order).when(orderMapper).mapToOrder(orderFilter);
            doReturn(orderPage).when(orderRepository).findAll(orderExample, pageable);
            doReturn(orderResponses).when(orderMapper).mapToOrderResponses(orders);

            List<OrderResponse> actualOrderResponses = orderService.findAll(orderFilter, pageable);

            assertThat(actualOrderResponses).isEqualTo(orderResponses);
        }

        @Test
        void checkFindAllShouldReturnEmptyOrderResponses() {
            doReturn(order).when(orderMapper).mapToOrder(orderFilter);
            doReturn(Page.empty()).when(orderRepository).findAll(orderExample, pageable);

            List<OrderResponse> actualOrderResponses = orderService.findAll(orderFilter, pageable);

            assertThat(actualOrderResponses).isEmpty();
        }
    }

    @Nested
    class OrderServiceImplFindByIdTest {

        @Test
        void checkFindByIdShouldReturnOrderResponse() throws IOException {
            Order order = OrderTestData.buildBeautyOrder();
            OrderResponse orderResponse = OrderTestData.buildBeautyOrderResponse();

            doReturn(Optional.of(order)).when(orderRepository).findById(order.getId());
            doReturn(orderResponse).when(orderMapper).mapToOrderResponse(order);

            OrderResponse actualOrderResponse = orderService.findById(order.getId());

            assertThat(actualOrderResponse).isEqualTo(orderResponse);
        }

        @Test
        void checkFindByIdShouldThrowsOrderNotFoundException() {
            Long id = 1L;

            doReturn(Optional.empty()).when(orderRepository).findById(id);

            assertThatThrownBy(() -> orderService.findById(id))
                    .isInstanceOf(OrderNotFoundException.class);
        }
    }

    @Nested
    class OrderServiceImplSaveTest {

        @Captor
        private ArgumentCaptor<Order> orderCaptor;

        private OrderRequest orderRequest;
        private Order order;
        private User user;

        @BeforeEach
        void setUp() throws IOException {
            orderRequest = OrderTestData.buildBeautyOrderRequest();
            order = OrderTestData.buildBeautyOrder();
            user = UserTestData.buildIvanovUser();
        }

        @Test
        void checkSaveShouldReturnOrderResponse() {
            doReturn(Optional.of(order.getUser())).when(userRepository).findById(orderRequest.getUserId());
            doReturn(Optional.of(order.getGiftCertificate())).when(giftCertificateRepository).findById(orderRequest.getGiftCertificateId());

            orderService.save(orderRequest);

            verify(orderRepository).save(orderCaptor.capture());
            assertAll(() -> {
                assertThat(orderCaptor.getValue().getPrice()).isEqualTo(order.getPrice());
                assertThat(orderCaptor.getValue().getUser()).isEqualTo(order.getUser());
                assertThat(orderCaptor.getValue().getGiftCertificate()).isEqualTo(order.getGiftCertificate());
            });
        }

        @Test
        void checkSaveShouldThrowsUserNotFoundException() {
            doReturn(Optional.empty()).when(userRepository).findById(orderRequest.getUserId());

            assertThatThrownBy(() -> orderService.save(orderRequest))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        void checkSaveShouldThrowsGiftCertificateNotFoundException() {
            doReturn(Optional.of(user)).when(userRepository).findById(orderRequest.getUserId());
            doReturn(Optional.empty()).when(giftCertificateRepository).findById(orderRequest.getGiftCertificateId());

            assertThatThrownBy(() -> orderService.save(orderRequest))
                    .isInstanceOf(GiftCertificateNotFoundException.class);
        }
    }
}
