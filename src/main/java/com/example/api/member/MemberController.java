package com.example.api.member;


import com.example.api.member.dto.MyMainRes;
import com.example.api.member.dto.ProfileRes;
import com.example.api.member.dto.UpdateMemberDTO;
import com.example.api.member.dto.UpdateMemberReq;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "회원", description = "회원 관련 API입니다.")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/myMain")
    @Operation(summary = "마이 페이지 회원 정보 조회 ", description = "마이 페이지에서 회원 정보 부분만 조회하는 API입니다.")
    public ResponseEntity<MyMainRes> getMyMain(@Parameter(hidden = true) @LoginMember MemberDTO loginMember) {
        MyMainRes myMainRes = MyMainRes.builder()
                .nickname(loginMember.getNickname())
                .profileImageUrl(loginMember.getProfileImageUrl())
                .name(loginMember.getName())
                .status(loginMember.getStatus())
                .build();
        return ResponseEntity.ok(myMainRes);
    }

    @GetMapping("/profile")
    @Operation(summary = "프로필 수정 회원 정보 조회 ", description = "프로필 수정 회원 정보 부분만 조회하는 API입니다.")
    public ResponseEntity<ProfileRes> getProfile(@Parameter(hidden = true) @LoginMember MemberDTO loginMember) {
        ProfileRes profileRes = ProfileRes.builder()
                .nickname(loginMember.getNickname())
                .profileImageUrl(loginMember.getProfileImageUrl())
                .name(loginMember.getName())
                .status(loginMember.getStatus())
                .build();
        return ResponseEntity.ok(profileRes);
    }

    @PatchMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "프로필 수정 ", description = "프로필 수정하는 API입니다.")
    public void updateProfile(@Parameter(hidden = true) @LoginMember MemberDTO loginMember,
                              @Parameter(description = "프로필 수정 요청값") @RequestPart UpdateMemberReq updateMemberReq,
                              @Parameter(description = "프로필 이미지 파일") @RequestPart(required = false) MultipartFile file) {

        UpdateMemberDTO dto = UpdateMemberDTO.builder()
                .memberId(loginMember.getMemberId())
                .nickname(updateMemberReq.getNickname())
                .profileImageUrl(loginMember.getProfileImageUrl())
                .build();
        memberService.updateMember(dto, file);
    }
}
