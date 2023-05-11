package ru.clevertec.ecl.dto.filter;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GiftCertificateFilter {

    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;

}
