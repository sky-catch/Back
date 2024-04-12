package com.example.api.member;

import com.example.api.member.dto.MyMainRes;
import com.example.api.member.dto.UpdateMemberDTO;
import com.example.api.member.dto.UpdateMemberReq;
import com.example.core.exception.ExceptionResponse;
import com.example.core.web.security.login.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "마이 페이지 회원 정보 조회 ", description = "마이 페이지 정보를 조회하는 API입니다. - 나의 저장 정렬 조건: 최신 생성순, - 리뷰 정렬 조건: 최신 생성순, - 리뷰 이미지 정렬 조건: ID 오름차순")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 정보, 회원이 저장한 식당, 회원이 작성한 리뷰 조회 성공.", content = @Content(schema = @Schema(implementation = MyMainRes.class))),
            @ApiResponse(responseCode = "404", description = "회원이 DB에 존재하지 않는 에러", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public ResponseEntity<MyMainRes> getMyMain(@LoginMember MemberDTO loginMember) {
        MyMainRes myMainRes = memberService.getMyMainById(loginMember.getMemberId());
        return ResponseEntity.ok(myMainRes);
    }

    @PatchMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "프로필 수정 ", description = "프로필 수정하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 프로필 수정 성공"),
    })
    public void updateProfile(@LoginMember MemberDTO loginMember,
                              @RequestPart UpdateMemberReq updateMemberReq,
                              @Parameter(description = "프로필 이미지 파일 - 첨부하지 않은 경우 이미지는 변경되지 않습니다.") @RequestPart(required = false) MultipartFile file) {

        UpdateMemberDTO dto = UpdateMemberDTO.builder()
                .memberId(loginMember.getMemberId())
                .nickname(updateMemberReq.getNickname())
                .profileImageUrl(loginMember.getProfileImageUrl())
                .build();
        memberService.updateMember(dto, file);
    }
}
