package com.example.api.reservation.dto;

import static com.example.api.reservation.ReservationStatus.PLANNED;

import com.example.api.reservation.ReservationDTO;
import com.example.api.reservation.ReservationStatus;
import com.example.api.reservation.dto.request.CreateReservationReq;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Data
@NoArgsConstructor
public class CreateReservationDTO {

    private long restaurantId;
    private long memberId;
    private LocalDateTime time;
    private int numberOfPeople;
    private String memo;
    private ReservationStatus status;
    private int amountToPay;
    private boolean shouldPay;

    @Builder
    private CreateReservationDTO(long restaurantId, long memberId, LocalDateTime time, int numberOfPeople, String memo,
                                 ReservationStatus status, int amountToPay, boolean shouldPay) {
        this.restaurantId = restaurantId;
        this.memberId = memberId;
        this.time = time;
        this.numberOfPeople = numberOfPeople;
        this.memo = memo;
        this.status = status;
        this.amountToPay = amountToPay;
        this.shouldPay = shouldPay;
    }

    public static CreateReservationDTO of(long restaurantId, long memberId, CreateReservationReq req) {
        return CreateReservationDTO.builder()
                .restaurantId(restaurantId)
                .memberId(memberId)
                .time(req.getVisitDateTime())
                .numberOfPeople(req.getNumberOfPeople())
                .memo(req.getMemo())
                .status(PLANNED)
                .amountToPay(req.getAmountToPay())
                .shouldPay(req.isShouldPay())
                .build();
    }

    public ReservationDTO toReservationDTO() {
        return ReservationDTO.builder()
                .restaurantId(restaurantId)
                .memberId(memberId)
                .time(time)
                .numberOfPeople(numberOfPeople)
                .memo(memo)
                .status(status)
                .build();
    }

    public LocalDate getVisitDate() {
        return time.toLocalDate();
    }

    public LocalTime getVisitTime() {
        return time.toLocalTime();
    }
}
