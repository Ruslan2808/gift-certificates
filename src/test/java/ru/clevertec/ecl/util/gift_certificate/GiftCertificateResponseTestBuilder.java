package ru.clevertec.ecl.util.gift_certificate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import org.springframework.format.annotation.DateTimeFormat;

import ru.clevertec.ecl.dto.gift_certificate.GiftCertificateResponse;
import ru.clevertec.ecl.dto.tag.TagResponse;
import ru.clevertec.ecl.util.TestBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "giftCertificateResponse")
public class GiftCertificateResponseTestBuilder implements TestBuilder<GiftCertificateResponse> {

    private Long id = 1L;
    private String name = "";
    private String description = "";
    private BigDecimal price = BigDecimal.valueOf(0.01);
    private Integer duration = 1;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createDate = LocalDateTime.now();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lastUpdateDate = LocalDateTime.now();
    private List<TagResponse> tags = new ArrayList<>();

    @Override
    public GiftCertificateResponse build() {
        final GiftCertificateResponse giftCertificateResponse = new GiftCertificateResponse();

        giftCertificateResponse.setId(id);
        giftCertificateResponse.setName(name);
        giftCertificateResponse.setDescription(description);
        giftCertificateResponse.setPrice(price);
        giftCertificateResponse.setDuration(duration);
        giftCertificateResponse.setCreateDate(createDate);
        giftCertificateResponse.setLastUpdateDate(lastUpdateDate);
        giftCertificateResponse.setTags(tags);

        return giftCertificateResponse;
    }
}
