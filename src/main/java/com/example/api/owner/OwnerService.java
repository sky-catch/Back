package com.example.api.owner;

import com.example.api.owner.dto.CreateOwnerReq;
import com.example.api.owner.dto.GetOwnerRes;
import com.example.api.owner.dto.Owner;
import com.example.api.owner.dto.UpdateOwnerReq;
import com.example.core.dto.HumanStatus;
import com.example.core.exception.SystemException;
import com.example.core.file.S3UploadService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerMapper ownerMapper;
    private final S3UploadService s3UploadService;

    // 사장이라면 Member, Owner 테이블에 정보 저장
    // 회원이라면 Member 테이블에 정보 저장
    @Transactional
    public void createOwner(CreateOwnerReq createOwnerReq, MultipartFile file) throws IOException {
        Owner owner = new Owner(createOwnerReq);
        //todo 중복 가입 검사

        if (file != null && !file.isEmpty()) {
            owner.setImagePath(s3UploadService.upload(file));
        }
        owner.setPlatform("카카오");
        owner.setStatus(HumanStatus.ACTIVE);
        ownerMapper.createOwner(owner);
    }

    public GetOwnerRes getOwner(long ownerId) {
        return checkOwnerExists(ownerId).toDto();
    }

    @Transactional
    public void updateOwner(UpdateOwnerReq dto, MultipartFile file) throws IOException {
        checkOwnerExists(dto.getOwnerId());

        Owner owner = new Owner(dto);
        if (file != null && !file.isEmpty()) {
            owner.setImagePath(s3UploadService.upload(file));
        }
        ownerMapper.updateOwner(owner);
    }

    @Transactional
    public void deleteOwner(long ownerId) {
        checkOwnerExists(ownerId);
        ownerMapper.deleteOwner(ownerId, HumanStatus.WITHDRAWN);
    }

    private Owner checkOwnerExists(long ownerId) {
        Owner owner = ownerMapper.getOwner(ownerId);
        if (owner == null) {
            throw new SystemException("존재하지 않는 사용자입니다.");
        }
        return owner;
    }


}