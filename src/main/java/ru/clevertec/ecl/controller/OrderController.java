package ru.clevertec.ecl.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.clevertec.ecl.dto.filter.OrderFilter;
import ru.clevertec.ecl.dto.request.OrderRequest;
import ru.clevertec.ecl.dto.response.OrderResponse;
import ru.clevertec.ecl.service.impl.OrderServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderServiceImpl orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAll(OrderFilter orderFilter, Pageable pageable) {
        List<OrderResponse> orderResponses = orderService.findAll(orderFilter, pageable);
        return ResponseEntity.ok(orderResponses);
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        OrderResponse orderResponse = orderService.findById(id);
        return ResponseEntity.ok(orderResponse);
    }

    @PostMapping
    public ResponseEntity<OrderResponse> save(@RequestBody @Valid OrderRequest orderRequest) {
        OrderResponse orderResponse = orderService.save(orderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }
}
