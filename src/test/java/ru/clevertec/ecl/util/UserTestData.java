package ru.clevertec.ecl.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import ru.clevertec.ecl.dto.filter.UserFilter;
import ru.clevertec.ecl.dto.request.UserRequest;
import ru.clevertec.ecl.dto.response.UserResponse;
import ru.clevertec.ecl.entity.User;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserTestData {

    public static final DateTimeFormatter ISO_DATE_TIME_FORMAT = DateTimeFormatter.ISO_DATE_TIME;

    private static final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder()
            .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(ISO_DATE_TIME_FORMAT))
            .build();

    public static UserRequest buildIvanovUserRequest() throws IOException {
        InputStream json = load("__files/user/ivanov_user_request.json");
        return objectMapper.readValue(json, UserRequest.class);
    }

    public static User buildIvanovUser() throws IOException {
        InputStream json = load("__files/user/ivanov_user.json");
        return objectMapper.readValue(json, User.class);
    }

    public static List<User> buildUsers() throws IOException {
        InputStream json = load("__files/user/users.json");
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);
        return objectMapper.readValue(json, listType);
    }

    public static UserResponse buildIvanovUserResponse() throws IOException {
        InputStream json = load("__files/user/ivanov_user.json");
        return objectMapper.readValue(json, UserResponse.class);
    }

    public static List<UserResponse> buildUserResponses() throws IOException {
        InputStream json = load("__files/user/users.json");
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, User.class);
        return objectMapper.readValue(json, listType);
    }

    public static UserFilter buildUserFilter() throws IOException {
        InputStream json = load("__files/user/user_filter.json");
        return objectMapper.readValue(json, UserFilter.class);
    }

    private static InputStream load(String fileName) {
        return ClassLoader.getSystemResourceAsStream(fileName);
    }
}
