package ru.clevertec.ecl.service;

import org.springframework.data.domain.Pageable;

import ru.clevertec.ecl.dto.filter.UserFilter;
import ru.clevertec.ecl.dto.request.UserRequest;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.dto.response.UserOrderResponse;
import ru.clevertec.ecl.dto.response.UserResponse;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.exception.UserAlreadyExistsException;
import ru.clevertec.ecl.exception.UserNotFoundException;

import java.util.List;

/**
 * Interface for performing operations with {@link User}
 *
 * @author Ruslan Kantsevitch
 */
public interface UserService {

    /**
     * Returns a list of all users of type {@link UserResponse} in the database
     *
     * @param userFilter the object of type {@link UserFilter} with fields for filtering:
     *                   username, firstName, lastName, email
     * @param pageable the object of type {@link Pageable} with fields for pagination
     * @return the list of all users of type {@link TagResponse} or an empty list if there are
     * none in the database
     */
    List<UserResponse> findAll(UserFilter userFilter, Pageable pageable);

    /**
     * Returns a user of type {@link UserResponse} by id or throws a {@link UserNotFoundException}
     * if the user with the given id is not found in the database
     *
     * @param id the user id
     * @return the user of type {@link UserResponse} with given id
     * @throws UserNotFoundException if the user with the given id is not found in the database
     */
    UserResponse findById(Long id);

    /**
     * Returns a list orders of user of type {@link UserOrderResponse} by id or throws a {@link UserNotFoundException}
     * if the user with the given id is not found in the database
     *
     * @param id the user id
     * @return the user of type {@link UserResponse} with given id
     * @throws UserNotFoundException if the user with the given id is not found in the database
     */
    List<UserOrderResponse> findOrdersById(Long id);

    /**
     * Saves the user of type {@link User} in the database or throws a {@link UserAlreadyExistsException}
     * if a user with the given username or email already exists
     *
     * @param userRequest the user of type {@link UserRequest} to save
     * @return the saved user of type {@link UserResponse}
     * @throws UserAlreadyExistsException if a user with the given username or email already exists
     */
    UserResponse save(UserRequest userRequest);
}
