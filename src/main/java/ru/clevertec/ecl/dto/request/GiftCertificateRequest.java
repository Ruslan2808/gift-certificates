package ru.clevertec.ecl.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateRequest {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private List<TagRequest> tags;

}
