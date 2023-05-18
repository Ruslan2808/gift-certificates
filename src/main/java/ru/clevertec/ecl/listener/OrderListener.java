package ru.clevertec.ecl.listener;

import jakarta.persistence.PrePersist;

import ru.clevertec.ecl.entity.Order;

import java.time.LocalDateTime;

public class OrderListener {

    @PrePersist
    public void beforeSave(Order order) {
        order.setDate(LocalDateTime.now());
    }
}
