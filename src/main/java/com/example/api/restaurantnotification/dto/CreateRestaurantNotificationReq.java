package com.example.api.restaurantnotification.dto;

import com.example.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Schema(description = "식당 공지사항 생성 요청값")
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestaurantNotificationReq extends BaseDTO {

    @NotNull
    @Schema(description = "제목", example = "공연안내")
    private String title;
    @NotNull
    @Schema(description = "내용", example = "플랫나인은 매일 국내 최정상 재즈 뮤지션의 공연이 진행됩니다.")
    private String content;
    @NotNull
    @Schema(description = "시작 날짜", example = "2023.05.02")
    private String startDate;
    @NotNull
    @Schema(description = "종료 날짜", example = "2023.12.31")
    private String endDate;
}
