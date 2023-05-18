package ru.clevertec.ecl.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import ru.clevertec.ecl.dto.filter.UserFilter;
import ru.clevertec.ecl.dto.request.UserRequest;
import ru.clevertec.ecl.dto.response.UserOrderResponse;
import ru.clevertec.ecl.dto.response.UserResponse;
import ru.clevertec.ecl.entity.User;
import ru.clevertec.ecl.exception.UserAlreadyExistsException;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.mapper.UserMapper;
import ru.clevertec.ecl.repository.UserRepository;
import ru.clevertec.ecl.util.OrderTestData;
import ru.clevertec.ecl.util.UserTestData;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    class UserServiceImplFindAllTest {

        private UserFilter userFilter;
        private Pageable pageable;
        private User user;
        private Example<User> userExample;

        @BeforeEach
        void setUp() throws IOException {
            userFilter = UserTestData.buildUserFilter();
            pageable = Pageable.unpaged();
            ExampleMatcher userMatcher = ExampleMatcher.matching()
                    .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                    .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                    .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                    .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
            user = User.builder()
                    .username(userFilter.getUsername())
                    .firstName(userFilter.getFirstName())
                    .lastName(userFilter.getLastName())
                    .email(userFilter.getEmail())
                    .build();
            userExample = Example.of(user, userMatcher);
        }

        @Test
        void checkFindAllShouldReturnSize2() throws IOException {
            int expectedSize = 2;
            List<User> users = UserTestData.buildUsers();
            Page<User> userPage = new PageImpl<>(users);
            List<UserResponse> userResponses = UserTestData.buildUserResponses();

            doReturn(user).when(userMapper).mapToUser(userFilter);
            doReturn(userPage).when(userRepository).findAll(userExample, pageable);
            doReturn(userResponses).when(userMapper).mapToUserResponses(users);

            List<UserResponse> actualUserResponses = userService.findAll(userFilter, pageable);

            assertThat(actualUserResponses).hasSize(expectedSize);
        }

        @Test
        void checkFindAllShouldReturnUserResponses() throws IOException {
            List<User> users = UserTestData.buildUsers();
            Page<User> userPage = new PageImpl<>(users);
            List<UserResponse> userResponses = UserTestData.buildUserResponses();

            doReturn(user).when(userMapper).mapToUser(userFilter);
            doReturn(userPage).when(userRepository).findAll(userExample, pageable);
            doReturn(userResponses).when(userMapper).mapToUserResponses(users);

            List<UserResponse> actualUserResponses = userService.findAll(userFilter, pageable);

            assertThat(actualUserResponses).isEqualTo(userResponses);
        }

        @Test
        void checkFindAllShouldReturnEmptyUserResponses() {
            doReturn(user).when(userMapper).mapToUser(userFilter);
            doReturn(Page.empty()).when(userRepository).findAll(userExample, pageable);

            List<UserResponse> actualUserResponses = userService.findAll(userFilter, pageable);

            assertThat(actualUserResponses).isEmpty();
        }
    }

    @Nested
    class UserServiceImplFindByIdTest {

        @Test
        void checkFindByIdShouldReturnUserResponse() throws IOException {
            User user = UserTestData.buildIvanovUser();
            UserResponse userResponse = UserTestData.buildIvanovUserResponse();

            doReturn(Optional.of(user)).when(userRepository).findById(user.getId());
            doReturn(userResponse).when(userMapper).mapToUserResponse(user);

            UserResponse actualUserResponse = userService.findById(user.getId());

            assertThat(actualUserResponse).isEqualTo(userResponse);
        }

        @Test
        void checkFindByIdShouldThrowsUserNotFoundException() {
            Long id = 1L;

            doReturn(Optional.empty()).when(userRepository).findById(id);

            assertThatThrownBy(() -> userService.findById(id))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested
    class UserServiceImplFindOrdersByIdTest {

        @Test
        void checkFindOrdersByIdShouldReturnSize1() throws IOException {
            int expectedSize = 1;
            User user = UserTestData.buildIvanovUser();
            List<UserOrderResponse> userOrderResponses = OrderTestData.buildUserOrderResponses();

            doReturn(Optional.of(user)).when(userRepository).findById(user.getId());
            doReturn(userOrderResponses).when(userMapper).mapToUserOrderResponses(user.getOrders());

            List<UserOrderResponse> actualUserOrderResponses = userService.findOrdersById(user.getId());

            assertThat(actualUserOrderResponses).hasSize(expectedSize);
        }

        @Test
        void checkFindOrdersByIdShouldReturnUserOrders() throws IOException {
            User user = UserTestData.buildIvanovUser();
            List<UserOrderResponse> userOrderResponses = OrderTestData.buildUserOrderResponses();

            doReturn(Optional.of(user)).when(userRepository).findById(user.getId());
            doReturn(userOrderResponses).when(userMapper).mapToUserOrderResponses(user.getOrders());

            List<UserOrderResponse> actualUserOrderResponses = userService.findOrdersById(user.getId());

            assertThat(actualUserOrderResponses).isEqualTo(userOrderResponses);
        }

        @Test
        void checkFindByIdShouldThrowsUserNotFoundException() {
            Long id = 1L;

            doReturn(Optional.empty()).when(userRepository).findById(id);

            assertThatThrownBy(() -> userService.findOrdersById(id))
                    .isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested
    class UserServiceImplSaveTest {

        private UserRequest userRequest;
        private User user;

        @BeforeEach
        void setUp() throws IOException {
            userRequest = UserTestData.buildIvanovUserRequest();
            user = UserTestData.buildIvanovUser();
        }

        @Test
        void checkSaveShouldReturnUserResponse() throws IOException {
            UserResponse userResponse = UserTestData.buildIvanovUserResponse();

            doReturn(Optional.empty()).when(userRepository).findByUsername(userRequest.getUsername());
            doReturn(Optional.empty()).when(userRepository).findByEmail(userRequest.getEmail());
            doReturn(user).when(userMapper).mapToUser(userRequest);
            doReturn(user).when(userRepository).save(user);
            doReturn(userResponse).when(userMapper).mapToUserResponse(user);

            UserResponse actualUserResponse = userService.save(userRequest);

            assertThat(actualUserResponse).isEqualTo(userResponse);
        }

        @Test
        void checkSaveShouldThrowsUserWithUsernameAlreadyExistsException() {
            doReturn(Optional.of(user)).when(userRepository).findByUsername(userRequest.getUsername());

            assertThatThrownBy(() -> userService.save(userRequest))
                    .isInstanceOf(UserAlreadyExistsException.class);
        }

        @Test
        void checkSaveShouldThrowsUserWithEmailAlreadyExistsException() {
            doReturn(Optional.of(user)).when(userRepository).findByEmail(userRequest.getEmail());

            assertThatThrownBy(() -> userService.save(userRequest))
                    .isInstanceOf(UserAlreadyExistsException.class);
        }
    }
}
