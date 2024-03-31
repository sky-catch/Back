package com.example.core.oauth.controller;

import com.example.core.oauth.domain.OauthServerType;
import com.example.core.oauth.dto.LoginResponse;
import com.example.core.oauth.service.OauthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Tag(name = "로그인", description = "SNS 로그인 관련 API입니다.")
public class OauthController {

    private final OauthService oauthService;

    @SneakyThrows
    @GetMapping("/{oauthServerType}")
    @Operation(summary = "인가 코드 받기")
    @ApiResponse(responseCode = "200", description = "성공하면 설정된 redirect page로 이동합니다.")
    public ResponseEntity<Void> redirectAuthCodeRequestUrl(
            @Parameter(description = "SNS 종류", required = true) @PathVariable OauthServerType oauthServerType,
            HttpServletResponse response) {

        String redirectedUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        log.info("redirectedUrl = {}", redirectedUrl);
        response.sendRedirect(redirectedUrl);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/{oauthServerType}")
    @Operation(summary = "사용자 로그인 처리")
    @ApiResponse(responseCode = "200", description = "카카오로부터 사용자 정보를 성공적으로 받아온 경우 accessToken, isOwner를 반환합니다.")
    public ResponseEntity<LoginResponse> login(
            @Parameter(description = "SNS 종류", required = true) @PathVariable OauthServerType oauthServerType,
            @Parameter(description = "카카오 서버에서 받은 인가 코드", required = true) @RequestParam String code) {
        LoginResponse response = oauthService.login(oauthServerType, code);
        return ResponseEntity.ok(response);
    }
}
