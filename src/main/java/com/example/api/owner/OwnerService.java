package com.example.api.owner;

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
    public void createOwner(OwnerDTO dto, MultipartFile file) throws IOException {
        //todo 카카오 중복 로그인 검사

        if (file!= null && !file.isEmpty()) {
            dto.setImagePath(s3UploadService.upload(file));
        }
        dto.setStatus(HumanStatus.ACTIVE);
        ownerMapper.createOwner(dto);
    }

    public OwnerDTO getOwner(long ownerId) {
        OwnerDTO owner = ownerMapper.getOwner(ownerId);
        if(owner != null){
            return owner;
        }
        throw new SystemException("존재하지 않는 사용자입니다.");
    }

    @Transactional
    public void updateOwner(OwnerDTO dto, MultipartFile file) throws IOException {
        if (file!= null && !file.isEmpty()) {
            dto.setImagePath(s3UploadService.upload(file));
        }
        ownerMapper.updateOwner(dto);
    }

    @Transactional
    public void deleteOwner(long ownerId) {
        ownerMapper.deleteOwner(OwnerDTO.builder().ownerId(ownerId).status(HumanStatus.WITHDRAWN).build());
    }
}