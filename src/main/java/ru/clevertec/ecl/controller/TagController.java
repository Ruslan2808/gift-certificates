package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.clevertec.ecl.dto.filter.TagFilter;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.service.impl.TagServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tags")
public class TagController {

    private final TagServiceImpl tagService;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAll(TagFilter tagFilter, Pageable pageable) {
        List<TagResponse> tagResponses = tagService.findAll(tagFilter, pageable);
        return ResponseEntity.ok(tagResponses);
    }

    @GetMapping("{id}")
    public ResponseEntity<TagResponse> getById(@PathVariable Long id) {
        TagResponse tagResponse = tagService.findById(id);
        return ResponseEntity.ok(tagResponse);
    }

    @GetMapping("most-widely-used")
    public ResponseEntity<TagResponse> getMostWidelyUsedByUserWithHighestAmountOrders() {
        TagResponse tagResponse = tagService.findMostWidelyUsedByUserWithHighestAmountOrders();
        return ResponseEntity.ok(tagResponse);
    }

    @PostMapping
    public ResponseEntity<TagResponse> save(@RequestBody @Valid TagRequest tagRequest) {
        TagResponse tagResponse = tagService.save(tagRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(tagResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<TagResponse> update(@PathVariable Long id,
                                              @RequestBody @Valid TagRequest tagRequest) {
        TagResponse tagResponse = tagService.update(id, tagRequest);
        return ResponseEntity.ok(tagResponse);
    }

    @DeleteMapping("{id}")
    public void deleteById(@PathVariable Long id) {
        tagService.deleteById(id);
    }
}
