package ru.clevertec.ecl.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lastUpdateDate;
    private List<TagResponse> tags;

}
