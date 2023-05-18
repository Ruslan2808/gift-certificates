package ru.clevertec.ecl.service;

import org.springframework.data.domain.Pageable;

import ru.clevertec.ecl.dto.filter.UserFilter;
import ru.clevertec.ecl.dto.request.UserRequest;
import ru.clevertec.ecl.dto.response.UserOrderResponse;
import ru.clevertec.ecl.dto.response.UserResponse;
import ru.clevertec.ecl.entity.User;

import java.util.List;

/**
 * Interface for performing operations with {@link User}
 *
 * @author Ruslan Kantsevitch
 */
public interface UserService {
    List<UserResponse> findAll(UserFilter userFilter, Pageable pageable);
    UserResponse findById(Long id);
    List<UserOrderResponse> findOrdersById(Long id);
    UserResponse save(UserRequest userRequest);
}
