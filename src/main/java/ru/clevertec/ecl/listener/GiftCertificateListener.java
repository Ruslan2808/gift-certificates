package ru.clevertec.ecl.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import ru.clevertec.ecl.entity.GiftCertificate;

import java.time.LocalDateTime;

public class GiftCertificateListener {

    @PrePersist
    public void beforeSave(GiftCertificate giftCertificate) {
        LocalDateTime dateTimeNow = LocalDateTime.now();

        giftCertificate.setCreateDate(dateTimeNow);
        giftCertificate.setLastUpdateDate(dateTimeNow);
    }

    @PreUpdate
    public void beforeUpdate(GiftCertificate giftCertificate) {
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
    }
}
