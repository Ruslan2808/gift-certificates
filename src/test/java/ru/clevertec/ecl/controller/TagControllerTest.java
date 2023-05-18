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

import ru.clevertec.ecl.dto.filter.TagFilter;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.exception.TagAlreadyExistsException;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.service.impl.TagServiceImpl;
import ru.clevertec.ecl.util.TagTestData;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
@ExtendWith(MockitoExtension.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TagServiceImpl tagService;

    @Nested
    class TagControllerGetAllTest {

        @Test
        void checkGetAllShouldReturnStatusOkAndSize3() throws Exception {
            int expectedSize = 3;
            List<TagResponse> tagResponses = TagTestData.buildTagResponses();

            doReturn(tagResponses)
                    .when(tagService)
                    .findAll(any(TagFilter.class), any(Pageable.class));

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

            doReturn(tagResponses)
                    .when(tagService)
                    .findAll(any(TagFilter.class), any(Pageable.class));

            mockMvc.perform(get("/api/v1/tags")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }

        @Test
        void checkGetAllShouldReturnStatusOkAndEmptyTagResponses() throws Exception {
            doReturn(Collections.emptyList())
                    .when(tagService)
                    .findAll(any(TagFilter.class), any(Pageable.class));

            mockMvc.perform(get("/api/v1/tags")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.keys()").isEmpty());
        }
    }

    @Nested
    class TagControllerGetByIdTest {

        @Test
        void checkGetByIdShouldReturnStatusOkAndTagResponse() throws Exception {
            TagResponse tagResponse = TagTestData.buildBeautyTagResponse();
            String expectedResponse = objectMapper.writeValueAsString(tagResponse);

            doReturn(tagResponse)
                    .when(tagService)
                    .findById(tagResponse.getId());

            mockMvc.perform(get("/api/v1/tags/{id}", tagResponse.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundTagIds")
        void checkGetByIdShouldReturnStatusNotFoundAndMessageTagNotFound(Long id) throws Exception {
            String expectedMessage = "Tag with id = [%d] not found".formatted(id);

            doThrow(new TagNotFoundException(expectedMessage))
                    .when(tagService)
                    .findById(anyLong());

            mockMvc.perform(get("/api/v1/tags/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<Long> provideNotFoundTagIds() {
            return Stream.of(-1L, 0L, 1L, 100L, 1000L);
        }
    }

    @Nested
    class TagControllerGetMostWidelyUsedByUserWithHighestAmountOrdersTest {

        @Test
        void checkGetMostWidelyUsedByUserWithHighestAmountOrdersShouldReturnStatusOkAndTagResponse() throws Exception {
            TagResponse tagResponse = TagTestData.buildBeautyTagResponse();
            String expectedResponse = objectMapper.writeValueAsString(tagResponse);

            doReturn(tagResponse)
                    .when(tagService)
                    .findMostWidelyUsedByUserWithHighestAmountOrders();

            mockMvc.perform(get("/api/v1/tags/most-widely-used")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedResponse));
        }

        @Test
        void checkGetMostWidelyUsedByUserWithHighestAmountOrdersShouldReturnStatusNotFoundAndMessageTagNotFound() throws Exception {
            String expectedMessage = "Most widely used user tag with highest order amount not found";

            doThrow(new TagNotFoundException(expectedMessage))
                    .when(tagService)
                    .findMostWidelyUsedByUserWithHighestAmountOrders();

            mockMvc.perform(get("/api/v1/tags/most-widely-used")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }
    }

    @Nested
    class TagControllerSaveTest {

        @Test
        void checkSaveShouldReturnStatusCreatedAndTagResponse() throws Exception {
            TagRequest tagRequest = TagTestData.buildBeautyTagRequest();
            TagResponse tagResponse = TagTestData.buildBeautyTagResponse();
            String expectedResponse = objectMapper.writeValueAsString(tagResponse);

            doReturn(tagResponse)
                    .when(tagService)
                    .save(tagRequest);

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

            doThrow(new TagAlreadyExistsException(expectedMessage))
                    .when(tagService)
                    .save(tagRequest);

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
                    TagRequest.builder().name("cool time").build(),
                    TagRequest.builder().name("best_time").build(),
                    TagRequest.builder().name("RELAX").build(),
                    TagRequest.builder().name("12345").build(),
                    TagRequest.builder().name("#####").build()
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
            TagRequest tagRequest = TagTestData.buildBeautyTagRequest();
            TagResponse tagResponse = TagTestData.buildBeautyTagResponse();
            String expectedResponse = objectMapper.writeValueAsString(tagResponse);

            doReturn(tagResponse)
                    .when(tagService)
                    .update(tagResponse.getId(), tagRequest);

            mockMvc.perform(put("/api/v1/tags/{id}", tagResponse.getId())
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

            doThrow(new TagNotFoundException(expectedMessage))
                    .when(tagService)
                    .update(id, tagRequest);

            mockMvc.perform(put("/api/v1/tags/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(tagRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        @ParameterizedTest
        @MethodSource("provideIdsAndTagRequestsWithAlreadyExistsNames")
        void checkUpdateShouldReturnStatusConflictAndMessageTagAlreadyExists(Long id, TagRequest tagRequest) throws Exception {
            String expectedMessage = "Tag with name = [%s] already exists".formatted(tagRequest.getName());

            doThrow(new TagAlreadyExistsException(expectedMessage))
                    .when(tagService)
                    .update(id, tagRequest);

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
            return Stream.of(-1L, 0L, 1L, 100L, 1000L);
        }

        private static Stream<Arguments> provideIdsAndTagRequestsWithAlreadyExistsNames() {
            return Stream.of(
                    arguments(1L, TagRequest.builder().name("beauty").build()),
                    arguments(2L, TagRequest.builder().name("cool time").build()),
                    arguments(3L, TagRequest.builder().name("best_time").build()),
                    arguments(4L, TagRequest.builder().name("RELAX").build()),
                    arguments(5L, TagRequest.builder().name("12345").build()),
                    arguments(5L, TagRequest.builder().name("#####").build())
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
            Long id = 1L;

            mockMvc.perform(delete("/api/v1/tags/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @ParameterizedTest
        @MethodSource("provideNotFoundTagIds")
        void checkDeleteByIdShouldReturnStatusNotFoundAndMessageTagNotFound(Long id) throws Exception {
            String expectedMessage = "Tag with id = [%d] not found".formatted(id);

            doThrow(new TagNotFoundException(expectedMessage))
                    .when(tagService)
                    .deleteById(id);

            mockMvc.perform(delete("/api/v1/tags/{id}", id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.messages[0]").value(expectedMessage));
        }

        private static Stream<Long> provideNotFoundTagIds() {
            return Stream.of(-1L, 0L, 1L, 100L, 1000L);
        }
    }
}
