package com.example.api.member;


import com.example.api.member.dto.MyMainRes;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
