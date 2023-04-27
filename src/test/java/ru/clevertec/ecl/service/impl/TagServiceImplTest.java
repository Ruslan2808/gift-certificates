package ru.clevertec.ecl.service.impl;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.dto.tag.TagResponse;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.TagAlreadyExistsException;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.impl.TagRepositoryImpl;
import ru.clevertec.ecl.util.tag.TagRequestTestBuilder;
import ru.clevertec.ecl.util.tag.TagResponseTestBuilder;
import ru.clevertec.ecl.util.tag.TagTestBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Mock
    private TagRepositoryImpl tagRepository;

    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    @Nested
    class TagServiceImplFindAllTest {

        @Test
        void checkFindAllShouldReturnSize1() {
            Tag tag = TagTestBuilder.tag().build();
            List<Tag> tags = List.of(tag);
            TagResponse tagResponse = TagResponseTestBuilder.tagResponse().build();
            List<TagResponse> tagResponses = List.of(tagResponse);
            int expectedSize = 1;
            doReturn(tags).when(tagRepository).findAll();
            doReturn(tagResponses).when(tagMapper).mapToTagResponses(tags);

            List<TagResponse> actualTagResponses = tagService.findAll();

            Assertions.assertThat(actualTagResponses).hasSize(expectedSize);
        }

        @Test
        void checkFindAllShouldReturnEmptyTags() {
            doReturn(Collections.emptyList()).when(tagRepository).findAll();

            List<TagResponse> actualTagResponses = tagService.findAll();

            Assertions.assertThat(actualTagResponses).isEmpty();
        }
    }

    @Nested
    class TagServiceImplFindByIdTest {

        @Test
        void checkFindByIdShouldReturnTag() {
            Long id = 1L;
            Tag tag = TagTestBuilder.tag().build();
            TagResponse tagResponse = TagResponseTestBuilder.tagResponse().build();
            doReturn(Optional.of(tag)).when(tagRepository).findById(id);
            doReturn(tagResponse).when(tagMapper).mapToTagResponse(tag);

            TagResponse actualTagResponse = tagService.findById(id);

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
    class TagServiceImplSaveTest {

        @Test
        void checkSaveShouldReturnTag() {
            TagRequest tagRequest = TagRequestTestBuilder.tagRequest().build();
            Tag tag = TagTestBuilder.tag().build();
            TagResponse tagResponse = TagResponseTestBuilder.tagResponse().build();
            doReturn(tag).when(tagMapper).mapToTag(tagRequest);
            doReturn(tag).when(tagRepository).save(tag);
            doReturn(tagResponse).when(tagMapper).mapToTagResponse(tag);

            TagResponse actualTagResponse = tagService.save(tagRequest);

            assertThat(actualTagResponse).isEqualTo(tagResponse);
        }

        @Test
        void checkSaveShouldThrowsTagAlreadyExistsException() {
            TagRequest tagRequest = TagRequestTestBuilder.tagRequest()
                    .withName("health")
                    .build();
            Tag tag = TagTestBuilder.tag()
                    .withName("health")
                    .build();
            doReturn(Optional.of(tag)).when(tagRepository).findByName(tagRequest.getName());

            assertThatThrownBy(() -> tagService.save(tagRequest))
                    .isInstanceOf(TagAlreadyExistsException.class);
        }
    }

    @Nested
    class TagServiceImplUpdateTest {

        @Test
        void checkUpdateShouldReturnTag() {
            Long id = 1L;
            TagRequest tagRequest = TagRequestTestBuilder.tagRequest()
                    .withName("health")
                    .build();
            Tag tag = TagTestBuilder.tag()
                    .withName("beauty")
                    .build();
            Tag updatedTag = TagTestBuilder.tag()
                    .withName("health")
                    .build();
            TagResponse tagResponse = TagResponseTestBuilder.tagResponse()
                    .withName("health")
                    .build();
            doReturn(Optional.of(tag)).when(tagRepository).findById(id);
            doReturn(Optional.empty()).when(tagRepository).findByName(tagRequest.getName());
            doReturn(updatedTag).when(tagRepository).update(tag);
            doReturn(tagResponse).when(tagMapper).mapToTagResponse(updatedTag);

            TagResponse actualTagResponse = tagService.update(id, tagRequest);

            assertThat(actualTagResponse).isEqualTo(tagResponse);
        }

        @Test
        void checkUpdateShouldThrowsTagNotFoundException() {
            Long id = 1L;
            TagRequest tagRequest = TagRequestTestBuilder.tagRequest().build();
            doReturn(Optional.empty()).when(tagRepository).findById(id);

            assertThatThrownBy(() -> tagService.update(id, tagRequest))
                    .isInstanceOf(TagNotFoundException.class);
        }

        @Test
        void checkUpdateShouldThrowsTagAlreadyExistsException() {
            Long id = 1L;
            TagRequest tagRequest = TagRequestTestBuilder.tagRequest()
                    .withName("health")
                    .build();
            Tag tag = TagTestBuilder.tag()
                    .withName("health")
                    .build();
            doReturn(Optional.of(tag)).when(tagRepository).findById(id);
            doReturn(Optional.of(tag)).when(tagRepository).findByName(tagRequest.getName());

            assertThatThrownBy(() -> tagService.update(id, tagRequest))
                    .isInstanceOf(TagAlreadyExistsException.class);
        }
    }

    @Nested
    class TagServiceImplDeleteByIdTest {

        @Test
        void checkDeleteByIdShouldCall() {
            Long id = 1L;
            Tag tag = TagTestBuilder.tag().build();
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
