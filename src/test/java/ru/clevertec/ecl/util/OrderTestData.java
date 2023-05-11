package ru.clevertec.ecl.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import ru.clevertec.ecl.dto.filter.OrderFilter;
import ru.clevertec.ecl.dto.request.OrderRequest;
import ru.clevertec.ecl.dto.response.OrderResponse;
import ru.clevertec.ecl.dto.response.UserOrderResponse;
import ru.clevertec.ecl.entity.Order;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderTestData {

    public static final DateTimeFormatter ISO_DATE_TIME_FORMAT = DateTimeFormatter.ISO_DATE_TIME;

    private static final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder()
            .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(ISO_DATE_TIME_FORMAT))
            .build();

    public static OrderRequest buildBeautyOrderRequest() throws IOException {
        InputStream json = load("__files/order/beauty_order.json");
        return objectMapper.readValue(json, OrderRequest.class);
    }

    public static Order buildBeautyOrder() throws IOException {
        InputStream json = load("__files/order/beauty_order.json");
        return objectMapper.readValue(json, Order.class);
    }

    public static List<Order> buildOrders() throws IOException {
        InputStream json = load("__files/order/orders.json");
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, Order.class);
        return objectMapper.readValue(json, listType);
    }

    public static OrderResponse buildBeautyOrderResponse() throws IOException {
        InputStream json = load("__files/order/beauty_order.json");
        return objectMapper.readValue(json, OrderResponse.class);
    }

    public static List<OrderResponse> buildOrderResponses() throws IOException {
        InputStream json = load("__files/order/orders.json");
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, Order.class);
        return objectMapper.readValue(json, listType);
    }

    public static OrderFilter buildOrderFilter() throws IOException {
        InputStream json = load("__files/order/order_filter.json");
        return objectMapper.readValue(json, OrderFilter.class);
    }

    public static List<UserOrderResponse> buildUserOrderResponses() throws IOException {
        InputStream json = load("__files/order/user_orders.json");
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, UserOrderResponse.class);
        return objectMapper.readValue(json, listType);
    }

    private static InputStream load(String fileName) {
        return ClassLoader.getSystemResourceAsStream(fileName);
    }
}
