package ru.clevertec.ecl.util.gift_certificate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import org.springframework.format.annotation.DateTimeFormat;

import ru.clevertec.ecl.entity.GiftCertificate;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.util.TestBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@With
@AllArgsConstructor
@NoArgsConstructor(staticName = "giftCertificate")
public class GiftCertificateTestBuilder implements TestBuilder<GiftCertificate> {

    private Long id = 1L;
    private String name = "";
    private String description = "";
    private BigDecimal price = BigDecimal.valueOf(0.01);
    private Integer duration = 1;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createDate = LocalDateTime.now();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime lastUpdateDate = LocalDateTime.now();
    private List<Tag> tags = new ArrayList<>();

    @Override
    public GiftCertificate build() {
        final GiftCertificate giftCertificate = new GiftCertificate();

        giftCertificate.setId(id);
        giftCertificate.setName(name);
        giftCertificate.setDescription(description);
        giftCertificate.setPrice(price);
        giftCertificate.setDuration(duration);
        giftCertificate.setCreateDate(createDate);
        giftCertificate.setLastUpdateDate(lastUpdateDate);
        giftCertificate.setTags(tags);

        return giftCertificate;
    }
}
