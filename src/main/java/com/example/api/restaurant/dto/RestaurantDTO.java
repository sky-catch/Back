package com.example.api.restaurant.dto;

import com.example.api.reservation.ReservationDTO;
import com.example.core.dto.BaseDTO;
import com.example.core.exception.SystemException;
import java.time.LocalTime;
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
public class RestaurantDTO extends BaseDTO {

    private long restaurantId;
    private long ownerId;
    private String name;
    private String category;
    private String content;
    private String phone;
    private int tablePersonMax;
    private int tablePersonMin;
    private LocalTime openTime;
    private LocalTime lastOrderTime;
    private LocalTime closeTime;
    private String address;
    private String detailAddress;
    private int lunchPrice;
    private int dinnerPrice;
    private long savedCount;
    private long reviewCount;
    private float reviewAvg;

    // todo 생성자 값 검증하기 - tablePersonMax < tablePersonMin

    public boolean isOwner(long ownerId) {
        return this.ownerId == ownerId;
    }

    public void checkCanMakeReservation(ReservationDTO dto) {
        int numberOfPeople = dto.getNumberOfPeople();
        if (numberOfPeople < tablePersonMin || numberOfPeople > tablePersonMax) {
            throw new SystemException("예약 인원 수가 잘못됐습니다.");
        }

        LocalTime reservationTime = dto.getTime().toLocalTime();
        if (openTime.isAfter(reservationTime) || lastOrderTime.isBefore(reservationTime)) {
            throw new SystemException("예약 가능한 시간이 아닙니다.");
        }
    }

    public boolean checkNumberOfPeople(int numberOfPeople) {
        return tablePersonMax < numberOfPeople || tablePersonMin > numberOfPeople;
    }

    // for mybatis
    public String getOpenTime() {
        return openTime.toString();
    }

    // for mybatis
    public String getLastOrderTime() {
        return lastOrderTime.toString();
    }

    // for mybatis
    public String getCloseTime() {
        return closeTime.toString();
    }
}
