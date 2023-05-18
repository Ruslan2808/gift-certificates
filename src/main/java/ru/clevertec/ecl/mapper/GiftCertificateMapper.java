package ru.clevertec.ecl.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import ru.clevertec.ecl.dto.filter.GiftCertificateFilter;
import ru.clevertec.ecl.dto.request.GiftCertificateRequest;
import ru.clevertec.ecl.dto.response.GiftCertificateResponse;
import ru.clevertec.ecl.entity.GiftCertificate;

import java.util.List;

@Mapper(uses = TagMapper.class)
public interface GiftCertificateMapper {

    GiftCertificate mapToGiftCertificate(GiftCertificateRequest giftCertificateRequest);
    GiftCertificate mapToGiftCertificate(GiftCertificateFilter giftCertificateFilter);
    GiftCertificateResponse mapToGiftCertificateResponse(GiftCertificate giftCertificate);
    List<GiftCertificateResponse> mapToGiftCertificateResponses(List<GiftCertificate> giftCertificates);

    @Mapping(target = "tags", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void mapUpdateFieldsToGiftCertificate(GiftCertificateRequest giftCertificateRequest,
                                          @MappingTarget GiftCertificate giftCertificate);
}
