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

import ru.clevertec.ecl.dto.filter.TagFilter;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.TagAlreadyExistsException;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.util.TagTestData;

import java.io.IOException;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    @Nested
    class TagServiceImplFindAllTest {

        private TagFilter tagFilter;
        private Pageable pageable;
        private Tag tag;
        private Example<Tag> tagExample;

        @BeforeEach
        void setUp() throws IOException {
            tagFilter = TagTestData.buildTagFilter();
            pageable = Pageable.unpaged();
            ExampleMatcher tagMatcher = ExampleMatcher.matching()
                    .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
            tag = Tag.builder().name(tagFilter.getName()).build();
            tagExample = Example.of(tag, tagMatcher);
        }

        @Test
        void checkFindAllShouldReturnSize3() throws IOException {
            int expectedSize = 3;
            List<Tag> tags = TagTestData.buildTags();
            Page<Tag> tagPage = new PageImpl<>(tags);
            List<TagResponse> tagResponses = TagTestData.buildTagResponses();

            doReturn(tag).when(tagMapper).mapToTag(tagFilter);
            doReturn(tagPage).when(tagRepository).findAll(tagExample, pageable);
            doReturn(tagResponses).when(tagMapper).mapToTagResponses(tags);

            List<TagResponse> actualTagResponses = tagService.findAll(tagFilter, pageable);

            assertThat(actualTagResponses).hasSize(expectedSize);
        }

        @Test
        void checkFindAllShouldReturnTagResponses() throws IOException {
            List<Tag> tags = TagTestData.buildTags();
            Page<Tag> tagPage = new PageImpl<>(tags);
            List<TagResponse> tagResponses = TagTestData.buildTagResponses();

            doReturn(tag).when(tagMapper).mapToTag(tagFilter);
            doReturn(tagPage).when(tagRepository).findAll(tagExample, pageable);
            doReturn(tagResponses).when(tagMapper).mapToTagResponses(tags);

            List<TagResponse> actualTagResponses = tagService.findAll(tagFilter, pageable);

            assertThat(actualTagResponses).isEqualTo(tagResponses);
        }

        @Test
        void checkFindAllShouldReturnEmptyTagResponses() {
            doReturn(tag).when(tagMapper).mapToTag(tagFilter);
            doReturn(Page.empty()).when(tagRepository).findAll(tagExample, pageable);

            List<TagResponse> actualTagResponses = tagService.findAll(tagFilter, pageable);

            assertThat(actualTagResponses).isEmpty();
        }
    }

    @Nested
    class TagServiceImplFindByIdTest {

        @Test
        void checkFindByIdShouldReturnTagResponse() throws IOException {
            Tag tag = TagTestData.buildBeautyTag();
            TagResponse tagResponse = TagTestData.buildBeautyTagResponse();

            doReturn(Optional.of(tag)).when(tagRepository).findById(tag.getId());
            doReturn(tagResponse).when(tagMapper).mapToTagResponse(tag);

            TagResponse actualTagResponse = tagService.findById(tag.getId());

            assertThat(actualTagResponse).isEqualTo(tagResponse);
        }

        @Test
        void checkFindByIdShouldThrowsTagNotFoundException() {
            Long id = 1L;

            doReturn(Optional.empty()).when(tagRepository).findById(id);

            assertThatThrownBy(() -> tagService.findById(id))
                    .isInstanceOf(TagNotFoundException.class);
        }
    }

    @Nested
    class TagServiceImplFindMostWidelyUsedByUserWithHighestAmountOrdersTest {

        @Test
        void checkFindMostWidelyUsedByUserWithHighestAmountOrdersShouldReturnTagResponse() throws IOException {
            Tag tag = TagTestData.buildBeautyTag();
            TagResponse tagResponse = TagTestData.buildBeautyTagResponse();

            doReturn(Optional.of(tag)).when(tagRepository).findMostWidelyUsedByUserWithHighestAmountOrders();
            doReturn(tagResponse).when(tagMapper).mapToTagResponse(tag);

            TagResponse actualTagResponse = tagService.findMostWidelyUsedByUserWithHighestAmountOrders();

            assertThat(actualTagResponse).isEqualTo(tagResponse);
        }

        @Test
        void checkFindMostWidelyUsedByUserWithHighestAmountOrdersShouldThrowsTagNotFoundException() {
            doReturn(Optional.empty()).when(tagRepository).findMostWidelyUsedByUserWithHighestAmountOrders();

            assertThatThrownBy(() -> tagService.findMostWidelyUsedByUserWithHighestAmountOrders())
                    .isInstanceOf(TagNotFoundException.class);
        }
    }

    @Nested
    class TagServiceImplSaveTest {

        private TagRequest tagRequest;
        private Tag tag;

        @BeforeEach
        void setUp() throws IOException {
            tagRequest = TagTestData.buildBeautyTagRequest();
            tag = TagTestData.buildBeautyTag();
        }

        @Test
        void checkSaveShouldReturnTagResponse() throws IOException {
            TagResponse tagResponse = TagTestData.buildBeautyTagResponse();

            doReturn(tag).when(tagMapper).mapToTag(tagRequest);
            doReturn(tag).when(tagRepository).save(tag);
            doReturn(tagResponse).when(tagMapper).mapToTagResponse(tag);

            TagResponse actualTagResponse = tagService.save(tagRequest);

            assertThat(actualTagResponse).isEqualTo(tagResponse);
        }

        @Test
        void checkSaveShouldThrowsTagAlreadyExistsException() {
            doReturn(Optional.of(tag)).when(tagRepository).findByName(tagRequest.getName());

            assertThatThrownBy(() -> tagService.save(tagRequest))
                    .isInstanceOf(TagAlreadyExistsException.class);
        }
    }

    @Nested
    class TagServiceImplUpdateTest {

        private TagRequest tagRequest;

        @BeforeEach
        void setUp() throws IOException {
            tagRequest = TagTestData.buildBeautyTagRequest();
        }

        @Test
        void checkUpdateShouldReturnTagResponse() throws IOException {
            Tag tag = TagTestData.buildHealthTag();
            Tag updatedTag = TagTestData.buildBeautyTag();
            TagResponse tagResponse = TagTestData.buildBeautyTagResponse();

            doReturn(Optional.of(tag)).when(tagRepository).findById(tag.getId());
            doReturn(Optional.empty()).when(tagRepository).findByName(tagRequest.getName());
            doReturn(updatedTag).when(tagRepository).save(tag);
            doReturn(tagResponse).when(tagMapper).mapToTagResponse(updatedTag);

            TagResponse actualTagResponse = tagService.update(tag.getId(), tagRequest);

            assertThat(actualTagResponse).isEqualTo(tagResponse);
        }

        @Test
        void checkUpdateShouldThrowsTagNotFoundException() {
            Long id = 1L;

            doReturn(Optional.empty()).when(tagRepository).findById(id);

            assertThatThrownBy(() -> tagService.update(id, tagRequest))
                    .isInstanceOf(TagNotFoundException.class);
        }

        @Test
        void checkUpdateShouldThrowsTagAlreadyExistsException() throws IOException {
            Long id = 1L;
            Tag tag = TagTestData.buildHealthTag();

            doReturn(Optional.of(tag)).when(tagRepository).findById(id);
            doReturn(Optional.of(tag)).when(tagRepository).findByName(tagRequest.getName());

            assertThatThrownBy(() -> tagService.update(id, tagRequest))
                    .isInstanceOf(TagAlreadyExistsException.class);
        }
    }

    @Nested
    class TagServiceImplDeleteByIdTest {

        @Test
        void checkDeleteByIdShouldCallDeleteGiftCertificateRelationsById() throws IOException {
            Long id = 1L;
            Tag tag = TagTestData.buildBeautyTag();

            doReturn(Optional.of(tag)).when(tagRepository).findById(id);

            tagService.deleteById(id);

            verify(tagRepository).deleteGiftCertificateRelationsById(id);
        }

        @Test
        void checkDeleteByIdShouldCallDeleteById() throws IOException {
            Long id = 1L;
            Tag tag = TagTestData.buildBeautyTag();

            doReturn(Optional.of(tag)).when(tagRepository).findById(id);

            tagService.deleteById(id);

            verify(tagRepository).deleteById(id);
        }

        @Test
        void checkDeleteByIdShouldThrowsTagNotFoundException() {
            Long id = 1L;

            doReturn(Optional.empty()).when(tagRepository).findById(id);

            assertThatThrownBy(() -> tagService.deleteById(id))
                    .isInstanceOf(TagNotFoundException.class);
        }
    }
}
