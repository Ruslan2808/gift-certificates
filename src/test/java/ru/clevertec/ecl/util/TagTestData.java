package ru.clevertec.ecl.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import ru.clevertec.ecl.dto.filter.TagFilter;
import ru.clevertec.ecl.dto.request.TagRequest;
import ru.clevertec.ecl.dto.response.TagResponse;
import ru.clevertec.ecl.entity.Tag;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TagTestData {

    private static final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();

    public static TagRequest buildBeautyTagRequest() throws IOException {
        InputStream json = load("__files/tag/beauty_tag_request.json");
        return objectMapper.readValue(json, TagRequest.class);
    }

    public static Tag buildBeautyTag() throws IOException {
        InputStream json = load("__files/tag/beauty_tag.json");
        return objectMapper.readValue(json, Tag.class);
    }

    public static Tag buildHealthTag() throws IOException {
        InputStream json = load("__files/tag/health_tag.json");
        return objectMapper.readValue(json, Tag.class);
    }

    public static List<Tag> buildTags() throws IOException {
        InputStream json = load("__files/tag/tags.json");
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, Tag.class);
        return objectMapper.readValue(json, listType);
    }

    public static TagResponse buildBeautyTagResponse() throws IOException {
        InputStream json = load("__files/tag/beauty_tag.json");
        return objectMapper.readValue(json, TagResponse.class);
    }

    public static List<TagResponse> buildTagResponses() throws IOException {
        InputStream json = load("__files/tag/tags.json");
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, TagResponse.class);
        return objectMapper.readValue(json, listType);
    }

    public static TagFilter buildTagFilter() throws IOException {
        InputStream json = load("__files/tag/tag_filter.json");
        return objectMapper.readValue(json, TagFilter.class);
    }

    private static InputStream load(String fileName) {
        return ClassLoader.getSystemResourceAsStream(fileName);
    }
}
