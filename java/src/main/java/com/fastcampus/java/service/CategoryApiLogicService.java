package com.fastcampus.java.service;

import com.fastcampus.java.model.entity.Category;
import com.fastcampus.java.model.network.Header;
import com.fastcampus.java.model.network.request.CategoryApiRequest;
import com.fastcampus.java.model.network.response.CategoryApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CategoryApiLogicService extends BaseService<CategoryApiRequest, CategoryApiResponse, Category> {

    @Override
    public Header<CategoryApiResponse> create(Header<CategoryApiRequest> request) {
        CategoryApiRequest body = request.getData();
        Category category = Category.builder()
                .type(body.getType())
                .title(body.getTitle())
                .build();
        Category newCategory = baseRepository.save(category);

        return Header.OK(response(newCategory));
    }

    @Override
    public Header<CategoryApiResponse> read(Long id) {

        return baseRepository.findById(id)
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header<CategoryApiResponse> update(Header<CategoryApiRequest> request) {
        CategoryApiRequest body = request.getData();
        return baseRepository.findById(body.getId())
                .map(category -> {
                    category
                            .setType(body.getType())
                            .setTitle(body.getTitle());
                    return category;
                })
                .map(newCategory -> baseRepository.save(newCategory))
                .map(this::response)
                .map(Header::OK)
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    public Header delete(Long id) {
        return baseRepository.findById(id)
                .map(category -> {
                    baseRepository.delete(category);
                    return Header.OK();
                })
                .orElseGet(() -> Header.ERROR("데이터 없음"));
    }

    @Override
    protected CategoryApiResponse response(Category category) {
        CategoryApiResponse body = CategoryApiResponse.builder()
                .id(category.getId())
                .type(category.getType())
                .title(category.getTitle())
                .build();
        return body;
    }

}
