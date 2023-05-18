package ru.clevertec.ecl.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateFilter {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;

}
