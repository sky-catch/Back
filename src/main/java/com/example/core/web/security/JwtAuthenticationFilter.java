package com.example.core.web.security;

import static com.example.core.web.security.jwt.JWTUtils.AUTHORIZATION_HEADER;
import static com.example.core.web.security.jwt.JWTUtils.TOKEN_PREFIX;

import com.example.api.member.MemberMapper;
import com.example.core.web.security.dto.Authority;
import com.example.core.web.security.jwt.JWTProvider;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String[] NO_CHECK_URIS = {"/oauth"};

    private final JWTProvider jwtProvider;
    private final MemberMapper memberMapper;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        return Arrays.stream(NO_CHECK_URIS).anyMatch(uri::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("JwtAuthenticationFilter.doFilterInternal");
        String accessToken = getAccessToken(request);
        if (isValidToken(accessToken)) {
            setAuthentication(accessToken);
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(HttpServletRequest request) {
        String tokenHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith(TOKEN_PREFIX)) {
            return tokenHeader.replace(TOKEN_PREFIX, "");
        }
        return null;
    }

    private boolean isValidToken(String accessToken) {
        return accessToken != null && jwtProvider.validateToken(accessToken);
    }

    private void setAuthentication(String accessToken) {
        String email = jwtProvider.getMemberEmail(accessToken);
        boolean isOwner = jwtProvider.isOwner(accessToken);
        memberMapper.findByEmail(email).ifPresent(memberDTO -> saveAuthentication(email, isOwner));
    }

    private void saveAuthentication(String email, boolean isOwner) {
        UserDetails userDetailsUser = User.builder()
                .username(email)
                .password(UUID.randomUUID().toString())
                .roles(Authority.MEMBER.name())
                .build();

        if (isOwner) {
            userDetailsUser = User.builder()
                    .username(email)
                    .password(UUID.randomUUID().toString())
                    .roles(Authority.OWNER.name())
                    .build();
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
