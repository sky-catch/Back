package com.example.api.reservation;


import com.example.api.reservation.ReservationController.GetMyReservationDTO;
import com.example.api.reservation.dto.GetReservationRes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationMapper reservationMapper;

    public List<GetReservationRes> getMyReservations(GetMyReservationDTO dto) {
        return reservationMapper.getReservationsByMemberAndStatus(dto);
    }
}