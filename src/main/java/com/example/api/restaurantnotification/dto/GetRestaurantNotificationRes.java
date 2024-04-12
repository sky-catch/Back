package com.example.api.restaurantnotification.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.example.core.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Schema(description = "식당 공지사항 응답값")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_NULL)
public class GetRestaurantNotificationRes extends BaseDTO {

    @Schema(description = "공지사항 ID", example = "1")
    private long notificationId;
    @JsonIgnore
    private long restaurantId;
    @JsonIgnore
    private long ownerId;
    @Schema(description = "공지사항 제목", example = "공연안내")
    private String title;
    @Schema(description = "공지사항 내용", example = "플랫나인은 매일 국내 최정상 재즈 뮤지션의 공연이 진행됩니다.")
    private String content;
    @Schema(description = "공지사항 시작일", example = "2023-05-01", type = "string")
    private LocalDate startDate;
    @Schema(description = "공지사항 종료일", example = "2023-11-01", type = "string")
    private LocalDate endDate;
}