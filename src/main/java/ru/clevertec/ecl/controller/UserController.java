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

import ru.clevertec.ecl.dto.filter.UserFilter;
import ru.clevertec.ecl.dto.request.UserRequest;
import ru.clevertec.ecl.dto.response.UserOrderResponse;
import ru.clevertec.ecl.dto.response.UserResponse;
import ru.clevertec.ecl.service.impl.UserServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll(UserFilter userFilter, Pageable pageable) {
        List<UserResponse> userResponses = userService.findAll(userFilter, pageable);
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        UserResponse userResponse = userService.findById(id);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("{id}/orders")
    public ResponseEntity<List<UserOrderResponse>> getOrdersById(@PathVariable Long id) {
        List<UserOrderResponse> userOrderResponses = userService.findOrdersById(id);
        return ResponseEntity.ok(userOrderResponses);
    }

    @PostMapping
    public ResponseEntity<UserResponse> save(@RequestBody @Valid UserRequest userRequest) {
        UserResponse userResponse = userService.save(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }
}
