package com.example.api.review.dto;

import com.example.core.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class GetReviewRes extends BaseDTO {
    //todo 레스토랑 이름 등 정보 추가
    private long reviewId;
    private long restaurantId;
    private long reservationId;
    private int rate;
    private String comment;
    private List<ReviewImageDTO> images;

}