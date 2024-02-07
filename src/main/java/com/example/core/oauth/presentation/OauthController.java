package com.example.core.oauth.presentation;

import com.example.core.oauth.application.OauthService;
import com.example.core.oauth.domain.OauthServerType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @SneakyThrows
    @GetMapping("/{oauthServerType}")
    public ResponseEntity<Void> redirectAuthCodeRequestUrl(@PathVariable OauthServerType oauthServerType,
                                                           HttpServletResponse response) {

        String redirectedUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        response.sendRedirect(redirectedUrl);
        return ResponseEntity.ok().build();
    }
}
