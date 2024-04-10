package com.example.api.owner;

import static com.example.api.reservation.ReservationStatus.PLANNED;
import static com.example.api.restaurant.exception.RestaurantExceptionType.NOT_FOUND;

import com.example.api.member.MemberDTO;
import com.example.api.member.MemberException;
import com.example.api.owner.dto.GetOwnerRes;
import com.example.api.owner.dto.Owner;
import com.example.api.reservation.ReservationMapper;
import com.example.api.reservation.ReservationStatus;
import com.example.api.reservation.dto.ReservationWithRestaurantAndPaymentDTO;
import com.example.api.reservation.dto.request.ChangeReservationsStatusToNoShowReq;
import com.example.api.reservation.exception.ReservationExceptionType;
import com.example.api.restaurant.RestaurantMapper;
import com.example.api.restaurant.dto.GetRestaurantRes;
import com.example.api.restaurant.dto.GetRestaurantWithReview;
import com.example.api.review.ReviewMapper;
import com.example.api.review.dto.GetReviewCommentRes;
import com.example.core.dto.HumanStatus;
import com.example.core.exception.SystemException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerMapper ownerMapper;
    private final RestaurantMapper restaurantMapper;
    private final ReviewMapper reviewMapper;
    private final ReservationMapper reservationMapper;

    // 사장이라면 Member, Owner 테이블에 정보 저장
    // 회원이라면 Member 테이블에 정보 저장
    @Transactional
    public void createOwner(MemberDTO memberDTO, String businessRegistrationNumber) {
        if (ownerMapper.isExistByEmail(memberDTO.getEmail())) {
            throw new SystemException("사장의 중복 생성은 불가능합니다.");
        }

        ownerMapper.createOwner(new Owner(memberDTO, businessRegistrationNumber));
    }

    @Transactional(readOnly = true)
    public GetOwnerRes getOwner(long ownerId) {
        Owner owner = ownerMapper.getOwner(ownerId);
        checkOwnerExists(owner);
        return owner.toDto();
    }

    @Transactional
    public void deleteOwner(long ownerId) {
        checkOwnerExists(ownerMapper.getOwner(ownerId));
        ownerMapper.deleteOwner(ownerId, HumanStatus.WITHDRAWN);
    }

    @Transactional(readOnly = true)
    public GetRestaurantWithReview getRestaurantByOwnerId(long ownerId) {
        GetRestaurantRes getRestaurantRes = restaurantMapper.findRestaurantInfoByOwnerId(ownerId)
                .orElseThrow(() -> new SystemException(NOT_FOUND.getMessage()));
        List<GetReviewCommentRes> reviewComments = reviewMapper.getReviewComments(getRestaurantRes.getRestaurantId());
        return new GetRestaurantWithReview(getRestaurantRes, reviewComments);
    }

    private void checkOwnerExists(Owner owner) {
        if (owner == null) {
            throw new SystemException(MemberException.NOT_FOUND.getMessage());
        }
    }

    @Transactional
    public void changeReservationsToNoShow(ChangeReservationsStatusToNoShowReq req) {
        List<ReservationWithRestaurantAndPaymentDTO> reservations = reservationMapper.findDetailByIds(
                req.getNoShowIds());

        boolean notValidStatus = reservations.stream()
                .map(ReservationWithRestaurantAndPaymentDTO::getStatus)
                .anyMatch(reservationStatus -> reservationStatus != PLANNED);
        if (notValidStatus) {
            throw new SystemException(ReservationExceptionType.NOT_VALID_STATUS);
        }

        reservationMapper.bulkUpdateStatusByIds(req.getNoShowIds(), ReservationStatus.CANCEL);
    }
}