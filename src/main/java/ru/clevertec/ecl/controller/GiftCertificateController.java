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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.clevertec.ecl.dto.filter.GiftCertificateFilter;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.service.impl.GiftCertificateServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateServiceImpl giftCertificateService;

    @GetMapping
    public ResponseEntity<List<GiftCertificateResponse>> getAll(GiftCertificateFilter giftCertificateFilter, Pageable pageable) {
        List<GiftCertificateResponse> giftCertificateResponses = giftCertificateService.findAll(giftCertificateFilter, pageable);
        return ResponseEntity.ok(giftCertificateResponses);
    }

    @GetMapping(params = { "tagName" })
    public ResponseEntity<List<GiftCertificateResponse>> getAllByTagName(@RequestParam String tagName, Pageable pageable) {
        List<GiftCertificateResponse> giftCertificateResponses = giftCertificateService.findAllByTagName(tagName, pageable);
        return ResponseEntity.ok(giftCertificateResponses);
    }

    @GetMapping(params = { "part" })
    public ResponseEntity<List<GiftCertificateResponse>> getAllByPartNameOrDescription(@RequestParam String part, Pageable pageable) {
        List<GiftCertificateResponse> giftCertificateResponses = giftCertificateService.findAllByPartNameOrDescription(part, pageable);
        return ResponseEntity.ok(giftCertificateResponses);
    }

    @GetMapping("{id}")
    public ResponseEntity<GiftCertificateResponse> getById(@PathVariable Long id) {
        GiftCertificateResponse giftCertificateResponse = giftCertificateService.findById(id);
        return ResponseEntity.ok(giftCertificateResponse);
    }

    @PostMapping
    public ResponseEntity<GiftCertificateResponse> save(@RequestBody @Valid GiftCertificateRequest giftCertificateRequest) {
        GiftCertificateResponse giftCertificateResponse = giftCertificateService.save(giftCertificateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(giftCertificateResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<GiftCertificateResponse> update(@PathVariable Long id,
                                                          @RequestBody @Valid GiftCertificateRequest giftCertificateRequest) {
        GiftCertificateResponse giftCertificateResponse = giftCertificateService.update(id, giftCertificateRequest);
        return ResponseEntity.ok(giftCertificateResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        giftCertificateService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
