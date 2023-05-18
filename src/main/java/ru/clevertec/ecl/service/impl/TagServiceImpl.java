package ru.clevertec.ecl.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.clevertec.ecl.dto.filter.TagFilter;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.TagAlreadyExistsException;
import ru.clevertec.ecl.exception.TagNotFoundException;
import ru.clevertec.ecl.mapper.TagMapper;
import ru.clevertec.ecl.repository.TagRepository;
import ru.clevertec.ecl.service.TagService;

import java.util.List;

/**
 * An implementation of the {@link TagService} interface for performing operations with {@link Tag}.
 * {@link TagRepository} is used to perform operations with tags in the database
 * {@link TagMapper} is used to map tag of type {@link Tag} to tag request of type {@link TagRequest} and
 * tag response {@link TagResponse}
 *
 * @author Ruslan Kantsevich
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    /**
     * Returns a list of all tags of type {@link TagResponse} in the database
     *
     * @param tagFilter the object of type {@link TagFilter} with fields for filtering: name
     * @param pageable the object of type {@link Pageable} with fields for pagination
     * @return the list of all tags of type {@link TagResponse} or an empty list if there are
     * none in the database
     */
    @Override
    public List<TagResponse> findAll(TagFilter tagFilter, Pageable pageable) {
        ExampleMatcher tagMatcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Tag tag = tagMapper.mapToTag(tagFilter);
        Example<Tag> tagExample = Example.of(tag, tagMatcher);

        List<Tag> tags = tagRepository.findAll(tagExample, pageable).getContent();

        return tagMapper.mapToTagResponses(tags);
    }

    /**
     * Returns a tag of type {@link TagResponse} by id or throws a {@link TagNotFoundException}
     * if the tag with the given id is not found in the database
     *
     * @param id the tag id
     * @return the tag of type {@link TagResponse} with given id
     * @throws TagNotFoundException if the tag with the given id is not found in the database
     */
    @Override
    public TagResponse findById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag with id = [%d] not found".formatted(id)));

        return tagMapper.mapToTagResponse(tag);
    }

    /**
     * Returns the most widely used tag of type {@link TagResponse} by user with the highest amount orders
     * or throws a {@link TagNotFoundException} if the that tag is not found in the database
     *
     * @return the tag of type {@link TagResponse}
     * @throws TagNotFoundException if the most widely used tag by user with the highest amount orders
     * is not found in the database
     * */
    @Override
    public TagResponse findMostWidelyUsedByUserWithHighestAmountOrders() {
        Tag tag = tagRepository.findMostWidelyUsedByUserWithHighestAmountOrders()
                .orElseThrow(() -> new TagNotFoundException("Most widely used user tag with highest order amount not found"));

        return tagMapper.mapToTagResponse(tag);
    }

    /**
     * Saves the tag of type {@link Tag} in the database or throws a {@link TagAlreadyExistsException}
     * if a tag with the given name already exists
     *
     * @param tagRequest the tag of type {@link TagRequest} to save
     * @return the saved tag of type {@link TagResponse}
     * @throws TagAlreadyExistsException if a tag with the given name already exists
     *                                   in the database
     */
    @Override
    @Transactional
    public TagResponse save(TagRequest tagRequest) {
        tagRepository.findByName(tagRequest.getName())
                .ifPresent(t -> {
                    throw new TagAlreadyExistsException("Tag with name = [%s] already exists".formatted(tagRequest.getName()));
                });

        Tag tag = tagMapper.mapToTag(tagRequest);
        Tag savedTag = tagRepository.save(tag);

        return tagMapper.mapToTagResponse(savedTag);
    }

    /**
     * Updates the tag of type {@link Tag} with the given id in the database or
     * throws a {@link TagNotFoundException} if the tag with the given id is not found or
     * throws a {@link TagAlreadyExistsException} if the tag with the given name already exists
     *
     * @param id         the id of the updated tag
     * @param tagRequest the tag of type {@link TagRequest} with data to update an existing
     *                   tag
     * @return the updated tag of type {@link TagResponse}
     * @throws TagNotFoundException      if the tag with the given id is not found in the database
     * @throws TagAlreadyExistsException if the tag with the given name already exists in the database
     */
    @Override
    @Transactional
    public TagResponse update(Long id, TagRequest tagRequest) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag with id = [%d] not found".formatted(id)));
        tagRepository.findByName(tagRequest.getName())
                .ifPresent(t -> {
                    throw new TagAlreadyExistsException("Tag with name = [%s] already exists".formatted(tagRequest.getName()));
                });

        tag.setName(tagRequest.getName());
        Tag updatedTag = tagRepository.save(tag);

        return tagMapper.mapToTagResponse(updatedTag);
    }

    /**
     * Deletes the tag of type {@link Tag} with the given id from the database or
     * throws a {@link TagNotFoundException} if the tag with the given id is not found
     *
     * @param id the id of the tag to be deleted
     * @throws TagNotFoundException if the tag with the given id is not found in the database
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag with id = [%d] not found".formatted(id)));

        tagRepository.deleteGiftCertificateRelationsById(id);
        tagRepository.deleteById(id);
    }
}
