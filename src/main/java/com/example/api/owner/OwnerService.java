package com.example.api.owner;

import com.example.api.member.MemberDTO;
import com.example.api.member.MemberException;
import com.example.api.owner.dto.GetOwnerRes;
import com.example.api.owner.dto.Owner;
import com.example.api.restaurant.RestaurantMapper;
import com.example.api.restaurant.dto.GetRestaurantRes;
import com.example.core.dto.HumanStatus;
import com.example.core.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.api.restaurant.exception.RestaurantExceptionType.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerMapper ownerMapper;
    private final RestaurantMapper restaurantMapper;

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
    public GetRestaurantRes getRestaurantByOwnerId(long ownerId) {
        return restaurantMapper.findRestaurantInfoByOwnerId(ownerId)
                .orElseThrow(() -> new SystemException(NOT_FOUND.getMessage()));
    }

    private void checkOwnerExists(Owner owner) {
        if (owner == null) {
            throw new SystemException(MemberException.NOT_FOUND.getMessage());
        }
    }

}