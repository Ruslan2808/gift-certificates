package ru.clevertec.ecl.service;

import org.springframework.data.domain.Pageable;

import ru.clevertec.ecl.dto.filter.OrderFilter;
import ru.clevertec.ecl.dto.request.OrderRequest;
import ru.clevertec.ecl.dto.response.OrderResponse;
import ru.clevertec.ecl.entity.Order;

import java.util.List;

/**
 * Interface for performing operations with {@link Order}
 *
 * @author Ruslan Kantsevitch
 */
public interface OrderService {
    List<OrderResponse> findAll(OrderFilter orderFilter, Pageable pageable);
    OrderResponse findById(Long id);
    OrderResponse save(OrderRequest orderRequest);
}
