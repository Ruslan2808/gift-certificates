package ru.clevertec.ecl.service;

import org.springframework.data.domain.Pageable;

import ru.clevertec.ecl.dto.filter.TagFilter;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.exception.TagAlreadyExistsException;
import ru.clevertec.ecl.exception.TagNotFoundException;

import java.util.List;

/**
 * Interface for performing operations with {@link Tag}
 *
 * @author Ruslan Kantsevitch
 */
public interface TagService {

    /**
     * Returns a list of all tags of type {@link TagResponse} in the database
     *
     * @param tagFilter the object of type {@link TagFilter} with fields for filtering: name
     * @param pageable the object of type {@link Pageable} with fields for pagination
     * @return the list of all tags of type {@link TagResponse} or an empty list if there are
     * none in the database
     */
    List<TagResponse> findAll(TagFilter tagFilter, Pageable pageable);

    /**
     * Returns a tag of type {@link TagResponse} by id or throws a {@link TagNotFoundException}
     * if the tag with the given id is not found in the database
     *
     * @param id the tag id
     * @return the tag of type {@link TagResponse} with given id
     * @throws TagNotFoundException if the tag with the given id is not found in the database
     */
    TagResponse findById(Long id);

    /**
     * Returns the most widely used tag of type {@link TagResponse} by user with the highest amount orders
     * or throws a {@link TagNotFoundException} if the that tag is not found in the database
     *
     * @return the tag of type {@link TagResponse}
     * @throws TagNotFoundException if the most widely used tag by user with the highest amount orders
     * is not found in the database
     * */
    TagResponse findMostWidelyUsedByUserWithHighestAmountOrders();

    /**
     * Saves the tag of type {@link Tag} in the database or throws a {@link TagAlreadyExistsException}
     * if a tag with the given name already exists
     *
     * @param tagRequest the tag of type {@link TagRequest} to save
     * @return the saved tag of type {@link TagResponse}
     * @throws TagAlreadyExistsException if a tag with the given name already exists
     *                                   in the database
     */
    TagResponse save(TagRequest tagRequest);

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
    TagResponse update(Long id, TagRequest tagRequest);

    /**
     * Deletes the tag of type {@link Tag} with the given id from the database or
     * throws a {@link TagNotFoundException} if the tag with the given id is not found
     *
     * @param id the id of the tag to be deleted
     * @throws TagNotFoundException if the tag with the given id is not found in the database
     */
    void deleteById(Long id);
}
