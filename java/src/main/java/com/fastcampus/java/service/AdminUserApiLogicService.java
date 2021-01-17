package com.fastcampus.java.service;

import com.fastcampus.java.model.entity.AdminUser;
import com.fastcampus.java.model.network.Header;
import com.fastcampus.java.model.network.request.AdminUserApiRequest;
import com.fastcampus.java.model.network.response.AdminUserApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminUserApiLogicService extends BaseService<AdminUserApiRequest, AdminUserApiResponse, AdminUser> {

    @Override
    public Header<AdminUserApiResponse> create(Header<AdminUserApiRequest> request) {
        AdminUserApiRequest body = request.getData();
        AdminUser adminUser = AdminUser.builder()
                .account(body.getAccount())
                .password(body.getPassword())
                .status(body.getStatus())
                .role(body.getRole())
                .lastLoginAt(LocalDateTime.now())
                .loginFailCount(0)
                .registeredAt(LocalDateTime.now())
                .build();

        AdminUser newAdminUser = baseRepository.save(adminUser);

        return Header.OK(response(newAdminUser));
    }

    @Override
    public Header<AdminUserApiResponse> read(Long id) {
        return baseRepository.findById(id)
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<AdminUserApiResponse> update(Header<AdminUserApiRequest> request) {
        AdminUserApiRequest body = request.getData();
        return baseRepository.findById(body.getId())
                .map(adminUser -> {
                    adminUser
                            .setAccount(body.getAccount())
                            .setPassword(body.getPassword())
                            .setStatus(body.getStatus())
                            .setRole(body.getRole())
                            .setLastLoginAt(body.getLastLoginAt())
                            .setLoginFailCount(body.getLoginFailCount())
                            .setRegisteredAt(body.getRegisteredAt())
                            .setUnregisteredAt(body.getUnregisteredAt())
                            .setPasswordUpdatedAt(body.getPasswordUpdatedAt());
                    return adminUser;
                })
                .map(newAdminUser -> baseRepository.save(newAdminUser))
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(adminUser -> {
                    baseRepository.delete(adminUser);
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    protected AdminUserApiResponse response(AdminUser adminUser) {
        AdminUserApiResponse body = AdminUserApiResponse.builder()
                .id(adminUser.getId())
                .account(adminUser.getAccount())
                .lastLoginAt(adminUser.getLastLoginAt())
                .loginFailCount(adminUser.getLoginFailCount())
                .password(adminUser.getPassword())
                .passwordUpdatedAt(adminUser.getPasswordUpdatedAt())
                .registeredAt(adminUser.getRegisteredAt())
                .role(adminUser.getRole())
                .status(adminUser.getStatus())
                .unregisteredAt(adminUser.getUnregisteredAt())
                .build();
        return body;
    }
}
