package com.example.api.reservation.dto;

import com.example.api.reservation.ReservationStatus;
import com.example.core.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MyReservationDTO extends BaseDTO {

    @Schema(description = "예약 ID", example = "1")
    private long reservationId;
    @Schema(description = "식당 ID", example = "1")
    private long restaurantId;
    @Schema(description = "식당 이름", example = "스시마루")
    private String restaurantName;
    @Schema(description = "카테고리", example = "스시오마카세")
    private String restaurantCategory;
    @Schema(description = "주소", example = "압구정로데오")
    private String restaurantAddress;
    @Schema(description = "식당 대표 이미지 url", example = "https://skyware-toy-project-imgae-bucket.s3.ap-northeast-2.amazonaws.com/image/8453543d-290d-40ae-a047-225de9131f32.png")
    private String restaurantImage;
    @Schema(description = "회원 ID", example = "1")
    private long memberId;
    @Schema(description = "결제 ID", example = "1")
    private long paymentId;
    @Schema(description = "예약 시간", example = "2024-03-13 11:00:00", type = "string")
    private LocalDateTime time;
    @Schema(description = "예약 인원 수", example = "2")
    private int numberOfPeople;
    @Schema(description = "예약 메모", example = "창가자리 부탁드려요.")
    private String memo;
    @Schema(description = "예약 상태", example = "PLANNED")
    private ReservationStatus status;
    @Schema(description = "나의 리뷰 작성 여부", example = "false")
    private boolean isReview;
}
