package com.fastcampus.java.service;

import com.fastcampus.java.model.entity.Item;
import com.fastcampus.java.model.entity.OrderGroup;
import com.fastcampus.java.model.entity.User;
import com.fastcampus.java.model.enumclass.UserStatus;
import com.fastcampus.java.model.network.Header;
import com.fastcampus.java.model.network.request.UserApiRequest;
import com.fastcampus.java.model.network.response.ItemApiResponse;
import com.fastcampus.java.model.network.response.OrderGroupApiResponse;
import com.fastcampus.java.model.network.response.UserApiResponse;
import com.fastcampus.java.model.network.response.UserOrderInfoApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserApiLogicService extends BaseService<UserApiRequest, UserApiResponse, User> {

    @Autowired
    private OrderGroupApiLogicService orderGroupApiLogicService;

    @Autowired
    private ItemApiLogicService itemApiLogicService;

    @Override
    public Header<UserApiResponse> create(Header<UserApiRequest> request) {

        // 1. request data
        UserApiRequest userApiRequest = request.getData();

        // 2. User 생성
        User user = User.builder()
                .account(userApiRequest.getAccount())
                .password(userApiRequest.getPassword())
                .status(UserStatus.REGISTERED)
                .phoneNumber(userApiRequest.getPhoneNumber())
                .email(userApiRequest.getEmail())
                .registeredAt(LocalDateTime.now())
                .build();

        User newUser = baseRepository.save(user);

        // 3. 생성된 데이터 -> userApiResponse return
        return Header.OK(response(newUser));
    }

    @Override
    public Header<UserApiResponse> read(Long id) {

        return baseRepository.findById(id)
                .map(user -> response(user))
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<UserApiResponse> update(Header<UserApiRequest> request) {

        // 1. data
        UserApiRequest userApiRequest = request.getData();

        // 2. id -> user 데이터 찾기
        Optional<User> optional = baseRepository.findById(userApiRequest.getId());

        return optional
                .map(user -> {
                    // 3. update
                    user.setAccount(userApiRequest.getAccount())
                            .setPassword(userApiRequest.getPassword())
                            .setStatus(userApiRequest.getStatus())
                            .setPhoneNumber(userApiRequest.getPhoneNumber())
                            .setEmail(userApiRequest.getEmail())
                            .setRegisteredAt(userApiRequest.getRegisteredAt())
                            .setUnregisteredAt(userApiRequest.getUnregisteredAt());

                    return user;
                })
                .map(user -> baseRepository.save(user)) // update
                .map(user -> response(user))//userApiResponse
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {

        // 1. id -> repository -> user
        Optional<User> optional = baseRepository.findById(id);

        // 2. repository -> delete
        return optional
                .map(user -> {
                    baseRepository.delete(user);
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("데이터 없음"));

    }

    @Override
    protected UserApiResponse response(User user) {
        UserApiResponse userApiResponse = UserApiResponse.builder()
                .id(user.getId())
                .account(user.getAccount())
                .password(user.getPassword()) // todo 암호화, 길이
                .status(user.getStatus())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .registeredAt(user.getRegisteredAt())
                .unregisteredAt(user.getUnregisteredAt())
                .build();

        return userApiResponse;
    }

    public Header<UserOrderInfoApiResponse> orderInfo(Long id) {

        // user
        User user = baseRepository.getOne(id);
        UserApiResponse userApiResponse = response(user);

        // orderGroup
        List<OrderGroup> orderGroupList = user.getOrderGroupList();
        List<OrderGroupApiResponse> orderGroupApiResponseList = orderGroupList.stream()
                .map(orderGroup -> {
                    OrderGroupApiResponse orderGroupApiResponse = orderGroupApiLogicService.response(orderGroup);
                    // item
                    List<ItemApiResponse> itemApiResponseList = orderGroup.getOrderDetailList().stream()
                            .map(detail -> detail.getItem())
                            .map(item -> itemApiLogicService.response(item))
                            .collect(Collectors.toList());
                    orderGroupApiResponse.setItemApiResponseList(itemApiResponseList);
                    return orderGroupApiResponse;
                })
                .collect(Collectors.toList());

        userApiResponse.setOrderGroupApiResponseList(orderGroupApiResponseList);

        UserOrderInfoApiResponse userOrderInfoApiResponse = UserOrderInfoApiResponse.builder()
                .userApiResponse(userApiResponse)
                .build();

        return Header.OK(userOrderInfoApiResponse);
    }
}
