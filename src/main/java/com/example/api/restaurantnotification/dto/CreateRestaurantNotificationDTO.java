package com.example.api.restaurantnotification.dto;

import com.example.core.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestaurantNotificationDTO extends BaseDTO {

    @NotNull
    @Schema(description = "제목", example = "공연안내")
    private String title;
    @NotNull
    @Schema(description = "내용", example = "플랫나인은 매일 국내 최정상 재즈 뮤지션의 공연이 진행됩니다.")
    private String content;
    @NotNull
    @Schema(description = "시작일", example = "2023.05.01")
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate startDate;
    @NotNull
    @Schema(description = "종료일", example = "2023.12.30")
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDate endDate;
}