package com.example.core.oauth.service;

import com.example.api.member.MemberDTO;
import com.example.api.member.MemberMapper;
import com.example.core.oauth.domain.OauthServerType;
import com.example.core.oauth.domain.authcode.AuthCodeRequestUrlProviderComposite;
import com.example.core.oauth.domain.client.OauthMemberClientComposite;
import com.example.core.web.security.jwt.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final MemberMapper memberMapper;
    private final JWTProvider jwtProvider;

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public String login(OauthServerType oauthServerType, String authCode) {
        MemberDTO member = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        MemberDTO saved = memberMapper.findByOauthId(member.oauthId())
                .orElseGet(() -> {
                    memberMapper.save(member);
                    memberMapper.findByOauthId(member.oauthId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
                    return memberMapper.findByOauthId(member.oauthId()).get();
                });

        return jwtProvider.createToken(saved);
    }
}
