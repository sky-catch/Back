package com.example.api.reservation;

import static java.util.Locale.ENGLISH;

public enum ReservationStatus {
    PLANNED, // 방문예정
    DONE, // 방문완료
    CANCEL, // 취소
    NO_SHOW, // 노쇼
    ;

    public static ReservationStatus fromName(String status) {
        return ReservationStatus.valueOf(status.toUpperCase(ENGLISH));
    }
}