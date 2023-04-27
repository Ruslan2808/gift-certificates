package ru.clevertec.ecl.mapper;

import org.mapstruct.Mapper;

import ru.clevertec.ecl.dto.gift_certificate.GiftCertificateRequest;
import ru.clevertec.ecl.dto.gift_certificate.GiftCertificateResponse;
import ru.clevertec.ecl.entity.GiftCertificate;

import java.util.List;

@Mapper(componentModel = "spring", uses = TagMapper.class)
public interface GiftCertificateMapper {
    GiftCertificate mapToGiftCertificate(GiftCertificateRequest giftCertificateRequest);
    GiftCertificateResponse mapToGiftCertificateResponse(GiftCertificate giftCertificate);
    List<GiftCertificateResponse> mapToGiftCertificateResponses(List<GiftCertificate> giftCertificates);
}
