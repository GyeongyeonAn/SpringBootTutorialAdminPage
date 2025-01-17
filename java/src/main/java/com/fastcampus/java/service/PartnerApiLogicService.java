package com.fastcampus.java.service;

import com.fastcampus.java.model.entity.Partner;
import com.fastcampus.java.model.network.Header;
import com.fastcampus.java.model.network.request.PartnerAPiRequest;
import com.fastcampus.java.model.network.response.PartnerAPiResponse;
import com.fastcampus.java.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PartnerApiLogicService extends BaseService<PartnerAPiRequest, PartnerAPiResponse, Partner> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Header<PartnerAPiResponse> create(Header<PartnerAPiRequest> request) {
        PartnerAPiRequest body = request.getData();
        Partner partner = Partner.builder()
                .name(body.getName())
                .status(body.getStatus())
                .address(body.getAddress())
                .callCenter(body.getCallCenter())
                .partnerNumber(body.getPartnerNumber())
                .businessNumber(body.getBusinessNumber())
                .ceoName(body.getCeoName())
                .registeredAt(LocalDateTime.now())
                .category(categoryRepository.getOne(body.getCategoryId()))
                .build();
        Partner newPartner = baseRepository.save(partner);
        return Header.OK(response(newPartner));
    }

    @Override
    public Header<PartnerAPiResponse> read(Long id) {
        return baseRepository.findById(id)
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<PartnerAPiResponse> update(Header<PartnerAPiRequest> request) {
        PartnerAPiRequest body = request.getData();
        return baseRepository.findById(body.getId())
                .map(partner -> {
                    partner
                            .setName(body.getName())
                            .setStatus(body.getStatus())
                            .setAddress(body.getAddress())
                            .setCallCenter(body.getCallCenter())
                            .setPartnerNumber(body.getPartnerNumber())
                            .setBusinessNumber(body.getBusinessNumber())
                            .setCeoName(body.getCeoName())
                            .setRegisteredAt(body.getRegisteredAt())
                            .setUnregisteredAt(body.getUnregisteredAt())
                            .setCategory(categoryRepository.getOne(body.getCategoryId()));
                    return partner;
                })
                .map(newPartner -> baseRepository.save(newPartner))
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(partner -> {
                    baseRepository.delete(partner);
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    protected PartnerAPiResponse response(Partner partner) {
        PartnerAPiResponse body = PartnerAPiResponse.builder()
                .id(partner.getId())
                .name(partner.getName())
                .status(partner.getStatus())
                .address(partner.getAddress())
                .callCenter(partner.getCallCenter())
                .partnerNumber(partner.getPartnerNumber())
                .businessNumber(partner.getBusinessNumber())
                .ceoName(partner.getCeoName())
                .registeredAt(partner.getRegisteredAt())
                .unregisteredAt(partner.getUnregisteredAt())
                .categoryId(partner.getCategory().getId())
                .build();

        return body;
    }
}
