package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.clevertec.ecl.dto.filter.UserFilter;
import ru.clevertec.ecl.dto.request.UserRequest;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.dto.response.UserOrderResponse;
import ru.clevertec.ecl.dto.response.UserResponse;
import ru.clevertec.ecl.entity.Order;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.exception.UserAlreadyExistsException;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.service.UserService;

import java.util.List;

/**
 * An implementation of the {@link UserService} interface for performing operations with {@link User}.
 * {@link UserRepository} is used to perform operations with users in the database
 * {@link UserMapper} is used to map user of type {@link User} to user request of type {@link UserRequest} and
 * user response {@link UserResponse}
 *
 * @author Ruslan Kantsevich
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Returns a list of all users of type {@link UserResponse} in the database
     *
     * @param userFilter the object of type {@link UserFilter} with fields for filtering:
     *                   username, firstName, lastName, email
     * @param pageable the object of type {@link Pageable} with fields for pagination
     * @return the list of all users of type {@link TagResponse} or an empty list if there are
     * none in the database
     */
    @Override
    public List<UserResponse> findAll(UserFilter userFilter, Pageable pageable) {
        ExampleMatcher userMatcher = ExampleMatcher.matching()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        User user = userMapper.mapToUser(userFilter);
        Example<User> userExample = Example.of(user, userMatcher);

        List<User> users = userRepository.findAll(userExample, pageable).getContent();

        return userMapper.mapToUserResponses(users);
    }

    /**
     * Returns a user of type {@link UserResponse} by id or throws a {@link UserNotFoundException}
     * if the user with the given id is not found in the database
     *
     * @param id the user id
     * @return the user of type {@link UserResponse} with given id
     * @throws UserNotFoundException if the user with the given id is not found in the database
     */
    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id = [%d] not found".formatted(id)));

        return userMapper.mapToUserResponse(user);
    }

    /**
     * Returns a list orders of user of type {@link UserOrderResponse} by id or throws a {@link UserNotFoundException}
     * if the user with the given id is not found in the database
     *
     * @param id the user id
     * @return the user of type {@link UserResponse} with given id
     * @throws UserNotFoundException if the user with the given id is not found in the database
     */
    @Override
    public List<UserOrderResponse> findOrdersById(Long id) {
        List<Order> orders = userRepository.findById(id)
                .map(User::getOrders)
                .orElseThrow(() -> new UserNotFoundException("User with id = [%d] not found".formatted(id)));

        return userMapper.mapToUserOrderResponses(orders);
    }

    /**
     * Saves the user of type {@link User} in the database or throws a {@link UserAlreadyExistsException}
     * if a user with the given username or email already exists
     *
     * @param userRequest the user of type {@link UserRequest} to save
     * @return the saved user of type {@link UserResponse}
     * @throws UserAlreadyExistsException if a user with the given username or email already exists
     */
    @Override
    @Transactional
    public UserResponse save(UserRequest userRequest) {
        userRepository.findByUsername(userRequest.getUsername())
                .ifPresent(u -> {
                    throw new UserAlreadyExistsException("User with username = [%s] already exists".formatted(userRequest.getUsername()));
                });
        userRepository.findByEmail(userRequest.getEmail())
                .ifPresent(u -> {
                    throw new UserAlreadyExistsException("User with email = [%s] already exists".formatted(userRequest.getEmail()));
                });

        User user = userMapper.mapToUser(userRequest);
        User savedUser = userRepository.save(user);

        return userMapper.mapToUserResponse(savedUser);
    }
}
