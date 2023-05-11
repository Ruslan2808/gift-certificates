package ru.clevertec.ecl.service;

import org.springframework.data.domain.Pageable;

import ru.clevertec.ecl.dto.filter.OrderFilter;
import ru.clevertec.ecl.dto.request.OrderRequest;
import ru.clevertec.ecl.dto.response.OrderResponse;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.exception.GiftCertificateNotFoundException;
import ru.clevertec.ecl.exception.OrderNotFoundException;
import ru.clevertec.ecl.exception.UserNotFoundException;

import java.util.List;

/**
 * Interface for performing operations with {@link Order}
 *
 * @author Ruslan Kantsevitch
 */
public interface OrderService {

    /**
     * Returns a list of all orders of type {@link OrderResponse} in the database
     *
     * @param orderFilter the object of type {@link OrderFilter} with fields for filtering: price
     * @param pageable the object of type {@link Pageable} with fields for pagination
     * @return the list of all orders of type {@link TagResponse} or an empty list if there are
     * none in the database
     */
    List<OrderResponse> findAll(OrderFilter orderFilter, Pageable pageable);

    /**
     * Returns an order of type {@link OrderResponse} by id or throws a {@link OrderNotFoundException}
     * if the order with the given id is not found in the database
     *
     * @param id the order id
     * @return the order of type {@link OrderResponse} with given id
     * @throws OrderNotFoundException if the order with the given id is not found in the database
     */
    OrderResponse findById(Long id);

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
    OrderResponse save(OrderRequest orderRequest);
}
