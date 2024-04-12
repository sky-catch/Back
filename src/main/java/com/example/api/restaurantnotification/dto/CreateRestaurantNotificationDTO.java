package com.example.api.restaurantnotification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(description = "식당 공지사항 생성 요청값")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestaurantNotificationDTO {

    @NotBlank(message = "공지사항 제목은 빈 값을 허용하지 않습니다.")
    @Schema(description = "공지사항 제목", example = "공연안내")
    private String title;
    @NotBlank(message = "공지사항 제목은 빈 값을 허용하지 않습니다.")
    @Schema(description = "공지사항 내용", example = "플랫나인은 매일 국내 최정상 재즈 뮤지션의 공연이 진행됩니다.")
    private String content;
    @NotNull(message = "시작일은 필수입니다.")
    @Schema(description = "시작일", example = "2023-05-01", type = "string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;
    @NotNull(message = "종료일 필수입니다.")
    @Schema(description = "종료일", example = "2023-12-30", type = "string")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;
}