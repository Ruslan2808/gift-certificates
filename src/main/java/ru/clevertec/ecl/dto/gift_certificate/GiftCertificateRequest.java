package ru.clevertec.ecl.dto.gift_certificate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.clevertec.ecl.dto.tag.TagRequest;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateRequest {

    @Size(min = 1, message = "Description cannot be empty")
    private String name;

    @Size(min = 1, message = "Description cannot be empty")
    private String description;

    @DecimalMin(value = "0.01", message = "Price can be greater than or equal to 0.01")
    private BigDecimal price;

    @Min(value = 1, message = "Duration can be greater than or equal to 1 day")
    private Integer duration;
    private List<TagRequest> tags;

}
