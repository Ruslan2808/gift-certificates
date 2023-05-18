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

import ru.clevertec.ecl.dto.filter.UserFilter;
import ru.clevertec.ecl.dto.request.UserRequest;
import ru.clevertec.ecl.dto.response.UserOrderResponse;
import ru.clevertec.ecl.dto.response.UserResponse;
import ru.clevertec.ecl.exception.UserAlreadyExistsException;
import ru.clevertec.ecl.exception.UserNotFoundException;
import ru.clevertec.ecl.service.impl.UserServiceImpl;
import ru.clevertec.ecl.util.UserTestData;

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

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImpl userService;

    @Nested
    class UserControllerGetAllTest {

        @Test
        void checkGetAllShouldReturnStatusOkAndSize2() throws Exception {
            int expectedSize = 2;
            List<UserResponse> userResponses = UserTestData.buildUserResponses();

            doReturn(userResponses)
                    .when(userService)
                    .findAll(any(UserFilter.class), any(Pageable.class));

            mockMvc.perform(get("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @Test
        void checkGetAllShouldReturnStatusOkAndUserResponses() throws Exception {
            List<UserResponse> userResponses = UserTestData.buildUserResponses();
            String expectedResponse = objectMapper.writeValueAsString(userResponses);

            doReturn(userResponses)
                    .when(userService)
                    .findAll(any(UserFilter.class), any(Pageable.class));

            mockMvc.perform(get("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }

        @Test
        void checkGetAllShouldReturnStatusOkAndEmptyUserResponses() throws Exception {
            doReturn(Collections.emptyList())
                    .when(userService)
                    .findAll(any(UserFilter.class), any(Pageable.class));

            mockMvc.perform(get("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.keys()").isEmpty());
        }
    }

    @Nested
    class UserControllerGetByIdTest {

        @Test
        void checkGetByIdShouldReturnStatusOkAndUserResponse() throws Exception {
            UserResponse userResponse = UserTestData.buildIvanovUserResponse();
            String expectedResponse = objectMapper.writeValueAsString(userResponse);

            doReturn(userResponse)
                    .when(userService)
                    .findById(userResponse.getId());

            mockMvc.perform(get("/api/v1/users/{id}", userResponse.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundUserIds")
        void checkGetByIdShouldReturnStatusNotFoundAndMessageUserNotFound(Long id) throws Exception {
            String expectedMessage = "User with id = [%d] not found".formatted(id);

            doThrow(new UserNotFoundException(expectedMessage))
                    .when(userService)
                    .findById(anyLong());

            mockMvc.perform(get("/api/v1/users/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<Long> provideNotFoundUserIds() {
            return Stream.of(-1L, 0L, 1L, 100L, 1000L);
        }
    }

    @Nested
    class UserControllerGetOrdersById {

        @Test
        void checkGetOrdersByIdShouldReturnStatusOkAndSize1() throws Exception {
            int expectedSize = 1;
            Long id = 1L;
            List<UserOrderResponse> userOrderResponses = UserTestData.buildIvanovUserResponse().getOrders();

            doReturn(userOrderResponses)
                    .when(userService)
                    .findOrdersById(anyLong());

            mockMvc.perform(get("/api/v1/users/{id}/orders", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @Test
        void checkGetOrdersByIdShouldReturnStatusOkAndUserOrderResponses() throws Exception {
            Long id = 1L;
            List<UserOrderResponse> userOrderResponses = UserTestData.buildIvanovUserResponse().getOrders();
            String expectedResponse = objectMapper.writeValueAsString(userOrderResponses);

            doReturn(userOrderResponses)
                    .when(userService)
                    .findOrdersById(anyLong());

            mockMvc.perform(get("/api/v1/users/{id}/orders", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }

        @Test
        void checkGetOrdersByIdShouldReturnStatusOkAndEmptyUserOrderResponses() throws Exception {
            Long id = 1L;

            doReturn(Collections.emptyList())
                    .when(userService)
                    .findOrdersById(anyLong());

            mockMvc.perform(get("/api/v1/users/{id}/orders", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.keys()").isEmpty());
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundUserIds")
        void checkGetOrdersByIdShouldReturnStatusNotFoundAndMessageUserNotFound(Long id) throws Exception {
            String expectedMessage = "User with id = [%d] not found".formatted(id);

            doThrow(new UserNotFoundException(expectedMessage))
                    .when(userService)
                    .findOrdersById(anyLong());

            mockMvc.perform(get("/api/v1/users/{id}/orders", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<Long> provideNotFoundUserIds() {
            return Stream.of(-1L, 0L, 1L, 100L, 1000L);
        }
    }

    @Nested
    class UserControllerSaveTest {

        @Test
        void checkSaveShouldReturnStatusCreatedAndUserResponse() throws Exception {
            UserRequest userRequest = UserTestData.buildIvanovUserRequest();
            UserResponse userResponse = UserTestData.buildIvanovUserResponse();
            String expectedResponse = objectMapper.writeValueAsString(userResponse);

            doReturn(userResponse)
                    .when(userService)
                    .save(userRequest);

            mockMvc.perform(post("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(expectedResponse));
        }

        @Test
        void checkSaveShouldReturnStatusConflictAndMessageUserWithUsernameAlreadyExists() throws Exception {
            UserRequest userRequest = UserTestData.buildIvanovUserRequest();
            String expectedMessage = "User with username = [%s] already exists".formatted(userRequest.getUsername());

            doThrow(new UserAlreadyExistsException(expectedMessage))
                    .when(userService)
                    .save(userRequest);

            mockMvc.perform(post("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequest)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        @Test
        void checkSaveShouldReturnStatusConflictAndMessageUserWithEmailAlreadyExists() throws Exception {
            UserRequest userRequest = UserTestData.buildIvanovUserRequest();
            String expectedMessage = "User with email = [%s] already exists".formatted(userRequest.getEmail());

            doThrow(new UserAlreadyExistsException(expectedMessage))
                    .when(userService)
                    .save(userRequest);

            mockMvc.perform(post("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequest)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        @ParameterizedTest
        @MethodSource("provideNotValidUserRequests")
        void checkSaveShouldReturnStatusBadRequestAndMessageFieldCannotBeEmpty(UserRequest userRequest, String expectedMessage) throws Exception {
            mockMvc.perform(post("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains(expectedMessage)));
        }

        private static Stream<Arguments> provideNotValidUserRequests() {
            return Stream.of(
                    arguments(UserRequest.builder().username(null).build(), "Username cannot be empty"),
                    arguments(UserRequest.builder().username("").build(), "Username cannot be empty"),
                    arguments(UserRequest.builder().username(" ").build(), "Username cannot be empty"),
                    arguments(UserRequest.builder().firstName(null).build(), "First name cannot be empty"),
                    arguments(UserRequest.builder().firstName("").build(), "First name cannot be empty"),
                    arguments(UserRequest.builder().firstName(" ").build(), "First name cannot be empty"),
                    arguments(UserRequest.builder().lastName(null).build(), "Last name cannot be empty"),
                    arguments(UserRequest.builder().lastName("").build(), "Last name cannot be empty"),
                    arguments(UserRequest.builder().lastName(" ").build(), "Last name cannot be empty"),
                    arguments(UserRequest.builder().email(null).build(), "Email cannot be empty"),
                    arguments(UserRequest.builder().email("").build(), "Email cannot be empty"),
                    arguments(UserRequest.builder().email(" ").build(), "Email cannot be empty")
            );
        }
    }
}
