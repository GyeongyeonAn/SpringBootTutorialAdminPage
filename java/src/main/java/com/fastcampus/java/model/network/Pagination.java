package com.fastcampus.java.model.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class Pagination {

    private Integer totalPages; // 총 페이지 수
    private Long totalElements; // 유저 수가 총 몇 명인지
    private Integer currentPage; // 현재 페이지 수
    private Integer currentElements; // 현재 몇 개의 데이터가 내려갔는지

}
