package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.clevertec.ecl.dto.filter.OrderFilter;
import ru.clevertec.ecl.dto.request.OrderRequest;
import ru.clevertec.ecl.dto.response.OrderResponse;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.exception.OrderNotFoundException;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.mapper.OrderMapper;
import ru.clevertec.ecl.repository.GiftCertificateRepository;
import ru.clevertec.ecl.repository.OrderRepository;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.service.OrderService;

import java.util.List;

/**
 * An implementation of the {@link OrderService} interface for performing operations with {@link Order}.
 * {@link OrderRepository} is used to perform operations with orders in the database
 * {@link UserRepository} is used to perform operations with users in the database
 * {@link GiftCertificateRepository} is used to perform operations with gift certificates in the database
 * {@link OrderMapper} is used to map order of type {@link Order} to order response {@link TagResponse}
 *
 * @author Ruslan Kantsevich
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;
    private final OrderMapper orderMapper;

    /**
     * Returns a list of all orders of type {@link OrderResponse} in the database
     *
     * @param orderFilter the object of type {@link OrderFilter} with fields for filtering: price
     * @param pageable the object of type {@link Pageable} with fields for pagination
     * @return the list of all orders of type {@link TagResponse} or an empty list if there are
     * none in the database
     */
    @Override
    public List<OrderResponse> findAll(OrderFilter orderFilter, Pageable pageable) {
        ExampleMatcher orderMatcher = ExampleMatcher.matching()
                .withMatcher("price", ExampleMatcher.GenericPropertyMatchers.exact());

        Order order = orderMapper.mapToOrder(orderFilter);
        Example<Order> orderExample = Example.of(order, orderMatcher);

        List<Order> orders = orderRepository.findAll(orderExample, pageable).getContent();

        return orderMapper.mapToOrderResponses(orders);
    }

    /**
     * Returns an order of type {@link OrderResponse} by id or throws a {@link OrderNotFoundException}
     * if the order with the given id is not found in the database
     *
     * @param id the order id
     * @return the order of type {@link OrderResponse} with given id
     * @throws OrderNotFoundException if the order with the given id is not found in the database
     */
    @Override
    public OrderResponse findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with id = [%d] not found".formatted(id)));

        return orderMapper.mapToOrderResponse(order);
    }

    /**
     * Saves the order of type {@link Order} in the database or throws a {@link UserNotFoundException}
     * if a user not found and a {@link GiftCertificateNotFoundException} if a gift certificate not found
     * in the database
     *
     * @param orderRequest the order of type {@link OrderRequest} to save
     * @return the saved order of type {@link OrderResponse}
     * @throws UserNotFoundException if a user not found in the database
     * @throws GiftCertificateNotFoundException if a gift certificate not found in the database
     */
    @Override
    @Transactional
    public OrderResponse save(OrderRequest orderRequest) {
        Long userId = orderRequest.getUserId();
        Long giftCertificateId = orderRequest.getGiftCertificateId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id = [%d] not found".formatted(userId)));
        GiftCertificate giftCertificate = giftCertificateRepository.findById(giftCertificateId)
                .orElseThrow(() -> new GiftCertificateNotFoundException("Gift certificate with id = [%d] not found".formatted(giftCertificateId)));

        Order order = Order.builder()
                .price(giftCertificate.getPrice())
                .user(user)
                .giftCertificate(giftCertificate)
                .build();

        Order savedOrder = orderRepository.save(order);

        return orderMapper.mapToOrderResponse(savedOrder);
    }
}
