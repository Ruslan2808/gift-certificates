package ru.clevertec.ecl.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotNull(message = "User id cannot be null")
    @Positive(message = "User id must be positive")
    private Long userId;

    @NotNull(message = "Gift certificate id cannot be null")
    @Positive(message = "Gift certificate id must be positive")
    private Long giftCertificateId;

}
