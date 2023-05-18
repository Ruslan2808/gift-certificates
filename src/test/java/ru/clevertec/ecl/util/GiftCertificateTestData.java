package ru.clevertec.ecl.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import ru.clevertec.ecl.dto.filter.GiftCertificateFilter;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.entity.GiftCertificate;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GiftCertificateTestData {

    public static final DateTimeFormatter ISO_DATE_TIME_FORMAT = DateTimeFormatter.ISO_DATE_TIME;

    private static final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder()
            .deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer(ISO_DATE_TIME_FORMAT))
            .build();

    public static GiftCertificateRequest buildBeautyGiftCertificateRequest() throws IOException {
        InputStream json = load("__files/gift_certificate/beauty_gift_certificate_request.json");
        return objectMapper.readValue(json, GiftCertificateRequest.class);
    }

    public static GiftCertificate buildBeautyGiftCertificate() throws IOException {
        InputStream json = load("__files/gift_certificate/beauty_gift_certificate.json");
        return objectMapper.readValue(json, GiftCertificate.class);
    }

    public static GiftCertificate buildHealthGiftCertificate() throws IOException {
        InputStream json = load("__files/gift_certificate/health_gift_certificate.json");
        return objectMapper.readValue(json, GiftCertificate.class);
    }

    public static List<GiftCertificate> buildGiftCertificates() throws IOException {
        InputStream json = load("__files/gift_certificate/gift_certificates.json");
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, GiftCertificate.class);
        return objectMapper.readValue(json, listType);
    }

    public static GiftCertificateResponse buildBeautyGiftCertificateResponse() throws IOException {
        InputStream json = load("__files/gift_certificate/beauty_gift_certificate.json");
        return objectMapper.readValue(json, GiftCertificateResponse.class);
    }

    public static List<GiftCertificateResponse> buildGiftCertificateResponses() throws IOException {
        InputStream json = load("__files/gift_certificate/gift_certificates.json");
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, GiftCertificateResponse.class);
        return objectMapper.readValue(json, listType);
    }

    public static GiftCertificateFilter buildGiftCertificateFilter() throws IOException {
        InputStream json = load("__files/gift_certificate/gift_certificate_filter.json");
        return objectMapper.readValue(json, GiftCertificateFilter.class);
    }

    private static InputStream load(String fileName) {
        return ClassLoader.getSystemResourceAsStream(fileName);
    }
}
