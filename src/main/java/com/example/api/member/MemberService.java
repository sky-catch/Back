package com.example.api.member;

import com.example.api.member.dto.MyMainDTO;
import com.example.api.member.dto.MyMainRes;
import com.example.api.member.dto.UpdateMemberDTO;
import com.example.api.owner.OwnerMapper;
import com.example.core.exception.SystemException;
import com.example.core.file.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final OwnerMapper ownerMapper;
    private final S3UploadService s3UploadService;

    @Transactional(readOnly = true)
    public MemberDTO getMemberById(long memberId) {
        return memberMapper.findById(memberId);
    }

    @Transactional
    public void updateMember(UpdateMemberDTO dto, MultipartFile file) {
        if (!file.isEmpty()) {
            s3UploadService.delete(dto.getProfileImageUrl());
            dto.setProfileImageUrl(s3UploadService.upload(file));
        }

        memberMapper.updateMember(dto);
    }

    @Transactional(readOnly = true)
    public MyMainRes getMyMainById(long memberId) {
        MyMainDTO myMainDTO = memberMapper.findMyMainById(memberId)
                .orElseThrow(() -> new SystemException(MemberExceptionType.NOT_FOUND));
        boolean existByEmail = ownerMapper.isExistByEmail(myMainDTO.getEmail());

        return MyMainRes.builder()
                .nickname(myMainDTO.getNickname())
                .profileImageUrl(myMainDTO.getProfileImageUrl())
                .name(myMainDTO.getName())
                .status(myMainDTO.getStatus())
                .owner(existByEmail)
                .savedRestaurants(myMainDTO.getSavedRestaurants())
                .reviews(myMainDTO.getReviews())
                .comments(myMainDTO.getComments())
                .build();
    }
}
