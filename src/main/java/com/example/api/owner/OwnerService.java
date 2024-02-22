package com.example.api.owner;

import com.example.api.owner.dto.CreateOwnerReq;
import com.example.api.owner.dto.GetOwnerRes;
import com.example.api.owner.dto.Owner;
import com.example.core.dto.HumanStatus;
import com.example.core.exception.SystemException;
import com.example.core.file.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OwnerService {

    private final OwnerMapper ownerMapper;
    private final S3UploadService s3UploadService;

    @Transactional
    public void createOwner(CreateOwnerReq createOwnerReq, MultipartFile file) throws IOException {
        Owner owner = new Owner(createOwnerReq);
        //todo 카카오 중복 로그인 검사

        if (file!= null && !file.isEmpty()) {
            owner.setImagePath(s3UploadService.upload(file));
        }
        owner.setPlatform("카카오");
        owner.setStatus(HumanStatus.ACTIVE);
        ownerMapper.createOwner(owner);
    }

    public GetOwnerRes getOwner(long ownerId) {
        Owner owner = ownerMapper.getOwner(ownerId);
        if(owner != null){
            return owner.toDto();
        }
        throw new SystemException("존재하지 않는 사용자입니다.");
    }

    @Transactional
    public void updateOwner(CreateOwnerReq createOwnerReq, MultipartFile file) throws IOException {
        Owner owner = new Owner(createOwnerReq);
        if (file!= null && !file.isEmpty()) {
            owner.setImagePath(s3UploadService.upload(file));
        }
        ownerMapper.updateOwner(owner);
    }

    @Transactional
    public void deleteOwner(long ownerId) {
        ownerMapper.deleteOwner(ownerId, HumanStatus.WITHDRAWN);
    }
}