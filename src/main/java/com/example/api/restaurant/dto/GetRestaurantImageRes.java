package com.example.api.restaurant.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.example.core.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema(description = "식당 이미지 응답값")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class GetRestaurantImageRes extends BaseDTO {

    @Schema(description = "식당 이미지 ID", example = "1")
    private long restaurantImageId;
    @JsonIgnore
    private long restaurantId;
    @Schema(description = "식당 이미지 경로", example = "https://skyware-toy-project-imgae-bucket.s3.ap-northeast-2.amazonaws.com/image/81a0a8f7-ad30-40ad-ab9f-ca36d2735e47.png")
    private String path;
    @Schema(description = "식당 이미지 종류", example = "REPRESENTATIVE")
    private RestaurantImageType type;
}