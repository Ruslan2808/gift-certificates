package ru.clevertec.ecl.util.gift_certificate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import ru.clevertec.ecl.dto.gift_certificate.GiftCertificateRequest;
import ru.clevertec.ecl.dto.tag.TagRequest;
import ru.clevertec.ecl.util.TestBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "giftCertificateRequest")
public class GiftCertificateRequestTestBuilder implements TestBuilder<GiftCertificateRequest> {

    private String name = "";
    private String description = "";
    private BigDecimal price = BigDecimal.valueOf(0.01);
    private Integer duration = 1;
    private List<TagRequest> tags = new ArrayList<>();

    @Override
    public GiftCertificateRequest build() {
        final GiftCertificateRequest giftCertificateRequest = new GiftCertificateRequest();

        giftCertificateRequest.setName(name);
        giftCertificateRequest.setDescription(description);
        giftCertificateRequest.setPrice(price);
        giftCertificateRequest.setDuration(duration);
        giftCertificateRequest.setTags(tags);

        return giftCertificateRequest;
    }
}
