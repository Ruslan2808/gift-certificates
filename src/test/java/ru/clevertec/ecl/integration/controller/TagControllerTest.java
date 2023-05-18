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

import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.integration.BaseIntegrationTest;
import ru.clevertec.ecl.util.TagTestData;

import java.util.stream.Stream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TagControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class TagControllerGetAllTest {

        @Test
        void checkGetAllShouldReturnStatusOkAndSize3() throws Exception {
            int expectedSize = 3;

            mockMvc.perform(get("/api/v1/tags")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @Test
        void checkGetAllShouldReturnStatusOkAndTagResponses() throws Exception {
            List<TagResponse> tagResponses = TagTestData.buildTagResponses();
            String expectedResponse = objectMapper.writeValueAsString(tagResponses);

            mockMvc.perform(get("/api/v1/tags")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }

        @ParameterizedTest
        @MethodSource("provideFilterNamesAndExpectedSizes")
        void checkGetAllShouldReturnStatusOkAndTagResponseSizes(String tagFilterName, int expectedSize) throws Exception {
            mockMvc.perform(get("/api/v1/tags")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("name", tagFilterName))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @ParameterizedTest
        @MethodSource("providePaginationParamsAndExpectedNotEmptySizes")
        void checkGetAllShouldReturnStatusOkAndTagResponseSizesPerPageSize(String page,
                                                                           String size,
                                                                           int expectedSize) throws Exception {
            mockMvc.perform(get("/api/v1/tags")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", page)
                            .param("size", size))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        @ParameterizedTest
        @MethodSource("providePaginationParamsWithExpectedEmptySizes")
        void checkGetAllShouldReturnStatusOkAndEmptyTagResponsesPerPageSize(String page, String size) throws Exception {
            mockMvc.perform(get("/api/v1/tags")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("page", page)
                            .param("size", size))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.keys()").isEmpty());
        }

        @ParameterizedTest
        @MethodSource("providePaginationParamsAndFilterNamesAndExpectedNotEmptySizes")
        void checkGetAllShouldReturnStatusOkAndFilterTagResponseSizesPerPageSize(String tagFilterName,
                                                                                 String page,
                                                                                 String size,
                                                                                 int expectedSize) throws Exception {
            mockMvc.perform(get("/api/v1/tags")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .param("name", tagFilterName)
                            .param("page", page)
                            .param("size", size))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(expectedSize));
        }

        private static Stream<Arguments> provideFilterNamesAndExpectedSizes() {
            return Stream.of(
                    arguments("bea", 1),
                    arguments("rel", 1),
                    arguments("mas", 1),
                    arguments("a", 3),
                    arguments("e", 3)
            );
        }

        private static Stream<Arguments> providePaginationParamsAndExpectedNotEmptySizes() {
            return Stream.of(
                    arguments("0", "1", 1),
                    arguments("0", "2", 2),
                    arguments("0", "3", 3),
                    arguments("1", "1", 1),
                    arguments("2", "1", 1)
            );
        }

        private static Stream<Arguments> providePaginationParamsWithExpectedEmptySizes() {
            return Stream.of(
                    arguments("0", "4"),
                    arguments("1", "3"),
                    arguments("2", "2"),
                    arguments("3", "1")
            );
        }

        private static Stream<Arguments> providePaginationParamsAndFilterNamesAndExpectedNotEmptySizes() {
            return Stream.of(
                    arguments("a", "0", "1", 1),
                    arguments("a", "0", "2", 2),
                    arguments("a", "1", "1", 1),
                    arguments("e", "0", "1", 1),
                    arguments("e", "0", "2", 2),
                    arguments("e", "0", "3", 3),
                    arguments("e", "1", "1", 1),
                    arguments("e", "1", "2", 1),
                    arguments("e", "2", "1", 1)
            );
        }
    }

    @Nested
    class TagControllerGetByIdTest {

        @ParameterizedTest
        @MethodSource("provideTagIds")
        void checkGetByIdShouldReturnStatusOkAndNotEmptyTagResponse(Long id) throws Exception {
            mockMvc.perform(get("/api/v1/tags/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result -> assertThat(result.getResponse().getContentAsString()).isNotBlank());
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundTagIds")
        void checkGetByIdShouldReturnStatusNotFoundAndMessageTagNotFound(Long id) throws Exception {
            String expectedMessage = "Tag with id = [%d] not found".formatted(id);

            mockMvc.perform(get("/api/v1/tags/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<Long> provideTagIds() {
            return Stream.of(1L, 2L, 3L);
        }

        private static Stream<Long> provideNotFoundTagIds() {
            return Stream.of(-1L, 0L, 10L, 100L, 1000L);
        }
    }

    @Nested
    class TagControllerGetMostWidelyUsedByUserWithHighestAmountOrdersTest {

        @Test
        void checkGetMostWidelyUsedByUserWithHighestAmountOrdersShouldReturnStatusOkAndTagResponse() throws Exception {
            TagResponse tagResponse = TagTestData.buildRelaxTagResponse();
            String expectedResponse = objectMapper.writeValueAsString(tagResponse);

            mockMvc.perform(get("/api/v1/tags/most-widely-used")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }
    }

    @Nested
    class TagControllerSaveTest {

        @Test
        void checkSaveShouldReturnStatusCreatedAndTagResponse() throws Exception {
            TagRequest tagRequest = TagTestData.buildSpaTagRequest();
            TagResponse tagResponse = TagTestData.buildSpaTagResponse();
            String expectedResponse = objectMapper.writeValueAsString(tagResponse);

            mockMvc.perform(post("/api/v1/tags")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tagRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(content().string(expectedResponse));
        }

        @ParameterizedTest
        @MethodSource("provideTagRequestsWithAlreadyExistsNames")
        void checkSaveShouldReturnStatusConflictAndMessageTagAlreadyExists(TagRequest tagRequest) throws Exception {
            String expectedMessage = "Tag with name = [%s] already exists".formatted(tagRequest.getName());

            mockMvc.perform(post("/api/v1/tags")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tagRequest)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        @ParameterizedTest
        @MethodSource("provideNotValidTagRequests")
        void checkSaveShouldReturnStatusBadRequestAndMessageCannotBeEmpty(TagRequest tagRequest) throws Exception {
            String expectedMessage = "Name cannot be empty";

            mockMvc.perform(post("/api/v1/tags")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tagRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<TagRequest> provideTagRequestsWithAlreadyExistsNames() {
            return Stream.of(
                    TagRequest.builder().name("beauty").build(),
                    TagRequest.builder().name("relax").build(),
                    TagRequest.builder().name("massage").build()
            );
        }

        private static Stream<TagRequest> provideNotValidTagRequests() {
            return Stream.of(
                    TagRequest.builder().name(null).build(),
                    TagRequest.builder().name("").build(),
                    TagRequest.builder().name(" ").build()
            );
        }
    }

    @Nested
    class TagControllerUpdateTest {

        @Test
        void checkUpdateShouldReturnStatusOkAndTagResponse() throws Exception {
            Long id = 2L;
            TagRequest tagRequest = TagTestData.buildSpaTagRequest();
            TagResponse tagResponse = TagTestData.buildSpaTagResponse();
            tagResponse.setId(id);
            String expectedResponse = objectMapper.writeValueAsString(tagResponse);

            mockMvc.perform(put("/api/v1/tags/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tagRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundTagIds")
        void checkUpdateShouldReturnStatusNotFoundAndMessageTagNotFound(Long id) throws Exception {
            String expectedMessage = "Tag with id = [%d] not found".formatted(id);
            TagRequest tagRequest = TagTestData.buildBeautyTagRequest();

            mockMvc.perform(put("/api/v1/tags/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tagRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        @ParameterizedTest
        @MethodSource("provideTagRequestsWithAlreadyExistsNames")
        void checkUpdateShouldReturnStatusConflictAndMessageTagAlreadyExists(TagRequest tagRequest) throws Exception {
            Long id = 2L;
            String expectedMessage = "Tag with name = [%s] already exists".formatted(tagRequest.getName());

            mockMvc.perform(put("/api/v1/tags/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tagRequest)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        @ParameterizedTest
        @MethodSource("provideNotValidTagRequests")
        void checkUpdateShouldReturnStatusBadRequestAndMessageCannotBeEmpty(TagRequest tagRequest) throws Exception {
            Long id = 1L;
            String expectedMessage = "Name cannot be empty";

            mockMvc.perform(put("/api/v1/tags/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tagRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<Long> provideNotFoundTagIds() {
            return Stream.of(-1L, 0L, 10L, 100L, 1000L);
        }

        private static Stream<TagRequest> provideTagRequestsWithAlreadyExistsNames() {
            return Stream.of(
                    TagRequest.builder().name("beauty").build(),
                    TagRequest.builder().name("relax").build(),
                    TagRequest.builder().name("massage").build()
            );
        }

        private static Stream<TagRequest> provideNotValidTagRequests() {
            return Stream.of(
                    TagRequest.builder().name(null).build(),
                    TagRequest.builder().name("").build(),
                    TagRequest.builder().name(" ").build()
            );
        }
    }

    @Nested
    class TagControllerDeleteByIdTest {

        @Test
        void checkDeleteByIdShouldReturnStatusOk() throws Exception {
            Long id = 3L;

            mockMvc.perform(delete("/api/v1/tags/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundTagIds")
        void checkDeleteByIdShouldReturnStatusNotFoundAndMessageTagNotFound(Long id) throws Exception {
            String expectedMessage = "Tag with id = [%d] not found".formatted(id);

            mockMvc.perform(delete("/api/v1/tags/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<Long> provideNotFoundTagIds() {
            return Stream.of(-1L, 0L, 10L, 100L, 1000L);
        }
    }
}
