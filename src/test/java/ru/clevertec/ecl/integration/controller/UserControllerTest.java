package ru.clevertec.ecl.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.clevertec.ecl.dto.filter.UserFilter;
import ru.clevertec.ecl.dto.request.UserRequest;
import ru.clevertec.ecl.dto.response.UserResponse;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.util.UserTestData;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class UserControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class UserControllerGetAllTest {

        @Test
        void checkGetAllShouldReturnStatusOkAndSize2() throws Exception {
            int expectedSize = 2;

            mockMvc.perform(get("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @ParameterizedTest
        @MethodSource("provideFilterParamsAndExpectedSizes")
        void checkGetAllShouldReturnStatusOkAndUserResponseSizes(UserFilter userFilter,
                                                                 int expectedSize) throws Exception {
            mockMvc.perform(get("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("username", userFilter.getUsername())
                            .param("firstName", userFilter.getFirstName())
                            .param("lastName", userFilter.getLastName())
                            .param("email", userFilter.getEmail()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @ParameterizedTest
        @MethodSource("providePaginationParamsAndExpectedNotEmptySizes")
        void checkGetAllShouldReturnStatusOkAndUserResponseSizesPerPageSize(String page,
                                                                            String size,
                                                                            int expectedSize) throws Exception {
            mockMvc.perform(get("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", page)
                            .param("size", size))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @ParameterizedTest
        @MethodSource("providePaginationParamsWithExpectedEmptySizes")
        void checkGetAllShouldReturnStatusOkAndEmptyUserResponsesPerPageSize(String page, String size) throws Exception {
            mockMvc.perform(get("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", page)
                            .param("size", size))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.keys()").isEmpty());
        }

        @ParameterizedTest
        @MethodSource("providePaginationParamsAndFilterParamsAndExpectedNotEmptySizes")
        void checkGetAllShouldReturnStatusOkAndFilterUserResponseSizesPerPageSize(UserFilter userFilter,
                                                                                 String page,
                                                                                 String size,
                                                                                 int expectedSize) throws Exception {
            mockMvc.perform(get("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("username", userFilter.getUsername())
                            .param("firstName", userFilter.getFirstName())
                            .param("lastName", userFilter.getLastName())
                            .param("email", userFilter.getEmail())
                            .param("page", page)
                            .param("size", size))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        private static Stream<Arguments> provideFilterParamsAndExpectedSizes() {
            return Stream.of(
                    arguments(UserFilter.builder().username("ivan").build(), 1),
                    arguments(UserFilter.builder().username("petr").build(), 1),
                    arguments(UserFilter.builder().username("i").build(), 1),
                    arguments(UserFilter.builder().username("p").build(), 1),
                    arguments(UserFilter.builder().firstName("ivan").build(), 1),
                    arguments(UserFilter.builder().firstName("petr").build(), 1),
                    arguments(UserFilter.builder().firstName("i").build(), 1),
                    arguments(UserFilter.builder().firstName("p").build(), 1),
                    arguments(UserFilter.builder().lastName("ivan").build(), 1),
                    arguments(UserFilter.builder().lastName("petr").build(), 1),
                    arguments(UserFilter.builder().lastName("i").build(), 1),
                    arguments(UserFilter.builder().lastName("p").build(), 1),
                    arguments(UserFilter.builder().lastName("ov").build(), 2),
                    arguments(UserFilter.builder().email("ivan").build(), 1),
                    arguments(UserFilter.builder().email("petr").build(), 1),
                    arguments(UserFilter.builder().email("ov").build(), 2),
                    arguments(UserFilter.builder().email("mail.ru").build(), 2),
                    arguments(UserFilter.builder().email("mail.ru").build(), 2)
            );
        }

        private static Stream<Arguments> providePaginationParamsAndExpectedNotEmptySizes() {
            return Stream.of(
                    arguments("0", "1", 1),
                    arguments("0", "2", 2),
                    arguments("1", "1", 1)
            );
        }

        private static Stream<Arguments> providePaginationParamsWithExpectedEmptySizes() {
            return Stream.of(
                    arguments("0", "3"),
                    arguments("1", "2"),
                    arguments("2", "1")
            );
        }

        private static Stream<Arguments> providePaginationParamsAndFilterParamsAndExpectedNotEmptySizes() {
            return Stream.of(
                    arguments(UserFilter.builder().lastName("ov").build(), "0", "1", 1),
                    arguments(UserFilter.builder().lastName("ov").build(), "0", "2", 2),
                    arguments(UserFilter.builder().lastName("ov").build(), "1", "1", 1),
                    arguments(UserFilter.builder().email("ov").build(), "0", "1", 1),
                    arguments(UserFilter.builder().email("ov").build(), "0", "2", 2),
                    arguments(UserFilter.builder().email("ov").build(), "1", "1", 1),
                    arguments(UserFilter.builder().email("mail.ru").build(), "0", "1", 1),
                    arguments(UserFilter.builder().email("mail.ru").build(), "0", "2", 2),
                    arguments(UserFilter.builder().email("mail.ru").build(), "1", "1", 1)
            );
        }
    }

    @Nested
    class UserControllerGetByIdTest {

        @ParameterizedTest
        @MethodSource("provideUserIds")
        void checkGetByIdShouldReturnStatusOkAndNotEmptyUserResponse(Long id) throws Exception {
            mockMvc.perform(get("/api/v1/users/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isNotBlank());
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundUserIds")
        void checkGetByIdShouldReturnStatusNotFoundAndMessageUserNotFound(Long id) throws Exception {
            String expectedMessage = "User with id = [%d] not found".formatted(id);

            mockMvc.perform(get("/api/v1/users/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<Long> provideUserIds() {
            return Stream.of(1L, 2L);
        }

        private static Stream<Long> provideNotFoundUserIds() {
            return Stream.of(-1L, 0L, 10L, 100L, 1000L);
        }
    }

    @Nested
    class UserControllerGetOrdersById {

        @ParameterizedTest
        @MethodSource("provideUserIdsAndExpectedOrderSizes")
        void checkGetOrdersByIdShouldReturnStatusOkAndNotEmptyUserOrderSizes(Long id, int expectedSize) throws Exception {
            mockMvc.perform(get("/api/v1/users/{id}/orders", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundUserIds")
        void checkGetOrdersByIdShouldReturnStatusNotFoundAndMessageUserNotFound(Long id) throws Exception {
            String expectedMessage = "User with id = [%d] not found".formatted(id);

            mockMvc.perform(get("/api/v1/users/{id}/orders", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<Arguments> provideUserIdsAndExpectedOrderSizes() {
            return Stream.of(
                    arguments(1L, 3),
                    arguments(2L, 2)
            );
        }

        private static Stream<Long> provideNotFoundUserIds() {
            return Stream.of(-1L, 0L, 10L, 100L, 1000L);
        }
    }

    @Nested
    class UserControllerSaveTest {

        @Test
        void checkSaveShouldReturnStatusCreatedAndUserResponse() throws Exception {
            UserRequest userRequest = UserTestData.buildAlexUserRequest();
            UserResponse userResponse = UserTestData.buildAlexUserResponse();
            String expectedResponse = objectMapper.writeValueAsString(userResponse);

            mockMvc.perform(post("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(expectedResponse));
        }

        @ParameterizedTest
        @MethodSource("provideUserRequestsWithAlreadyExistsUsernames")
        void checkSaveShouldReturnStatusConflictAndMessageUserWithUsernameAlreadyExists(UserRequest userRequest) throws Exception {
            String expectedMessage = "User with username = [%s] already exists".formatted(userRequest.getUsername());

            mockMvc.perform(post("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequest)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        @ParameterizedTest
        @MethodSource("provideUserRequestsWithAlreadyExistsEmails")
        void checkSaveShouldReturnStatusConflictAndMessageUserWithEmailAlreadyExists(UserRequest userRequest) throws Exception {
            String expectedMessage = "User with email = [%s] already exists".formatted(userRequest.getEmail());

            mockMvc.perform(post("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequest)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        @ParameterizedTest
        @MethodSource("provideNotValidUserRequests")
        void checkSaveShouldReturnStatusBadRequestAndMessageFieldCannotBeEmpty(UserRequest userRequest,
                                                                               String expectedMessage) throws Exception {
            mockMvc.perform(post("/api/v1/users")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains(expectedMessage)));
        }

        private static Stream<UserRequest> provideUserRequestsWithAlreadyExistsUsernames() {
            return Stream.of(
                    UserRequest.builder()
                            .username("ivan")
                            .firstName("Ivan")
                            .lastName("Ivanov")
                            .email("ivanov@mail.ru")
                            .build(),
                    UserRequest.builder()
                            .username("petr")
                            .firstName("Petr")
                            .lastName("Petrov")
                            .email("petrov@mail.ru'")
                            .build()
            );
        }

        private static Stream<UserRequest> provideUserRequestsWithAlreadyExistsEmails() {
            return Stream.of(
                    UserRequest.builder()
                            .username("ivan_i")
                            .firstName("Ivan")
                            .lastName("Ivanov")
                            .email("ivanov@mail.ru")
                            .build(),
                    UserRequest.builder()
                            .username("petr_p")
                            .firstName("Petr")
                            .lastName("Petrov")
                            .email("petrov@mail.ru")
                            .build()
            );
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
