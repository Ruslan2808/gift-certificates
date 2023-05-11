package ru.clevertec.ecl.service;

import org.springframework.data.domain.Pageable;

import ru.clevertec.ecl.dto.filter.TagFilter;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.entity.Tag;

import java.util.List;

/**
 * Interface for performing operations with {@link Tag}
 *
 * @author Ruslan Kantsevitch
 */
public interface TagService {
    List<TagResponse> findAll(TagFilter tagFilter, Pageable pageable);
    TagResponse findById(Long id);
    TagResponse findMostWidelyUsedByUserWithHighestAmountOrders();
    TagResponse save(TagRequest tagRequest);
    TagResponse update(Long id, TagRequest tagRequest);
    void deleteById(Long id);
}
