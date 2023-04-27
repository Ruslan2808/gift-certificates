package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

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

import ru.clevertec.ecl.dto.gift_certificate.GiftCertificateRequest;
import ru.clevertec.ecl.dto.gift_certificate.GiftCertificateResponse;
import ru.clevertec.ecl.service.impl.GiftCertificateServiceImpl;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateServiceImpl giftCertificateService;

    @GetMapping
    public ResponseEntity<List<GiftCertificateResponse>> getAll() {
        List<GiftCertificateResponse> giftCertificateResponses = giftCertificateService.findAll();
        return ResponseEntity.ok(giftCertificateResponses);
    }

    @GetMapping(params = { "tagName" })
    public ResponseEntity<List<GiftCertificateResponse>> getAllByTagName(@RequestParam(defaultValue = "") String tagName) {
        List<GiftCertificateResponse> giftCertificateResponses = giftCertificateService.findAllByTagName(tagName);
        return ResponseEntity.ok(giftCertificateResponses);
    }

    @GetMapping(params = { "part" })
    public ResponseEntity<List<GiftCertificateResponse>> getAllByPartNameOrDescription(@RequestParam(defaultValue = "") String part) {
        List<GiftCertificateResponse> giftCertificateResponses = giftCertificateService.findAllByPartNameOrDescription(part);
        return ResponseEntity.ok(giftCertificateResponses);
    }

    @GetMapping(params = { "sortBy" })
    public ResponseEntity<List<GiftCertificateResponse>> getAllSortByParam(@RequestParam(defaultValue = "name") String sortBy,
                                                                           @RequestParam(defaultValue = "asc") String order) {
        List<GiftCertificateResponse> giftCertificateResponses = giftCertificateService.findAllSortByParam(sortBy, order);
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
    public void deleteById(@PathVariable Long id) {
        giftCertificateService.deleteById(id);
    }
}
