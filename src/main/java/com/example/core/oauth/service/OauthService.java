package com.example.core.oauth.service;

import com.example.core.oauth.domain.OauthMember;
import com.example.core.oauth.domain.OauthServerType;
import com.example.core.oauth.domain.authcode.AuthCodeRequestUrlProviderComposite;
import com.example.core.oauth.domain.client.OauthMemberClientComposite;
import com.example.core.oauth.domain.mapper.OauthMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final OauthMemberMapper oauthMemberMapper;

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public Long login(OauthServerType oauthServerType, String authCode) {
        OauthMember oauthMember = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        OauthMember saved = oauthMemberMapper.findByOauthId(oauthMember.oauthId())
                .orElseGet(() -> {
                    oauthMemberMapper.save(oauthMember);
                    oauthMemberMapper.findByOauthId(oauthMember.oauthId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
                    return oauthMemberMapper.findByOauthId(oauthMember.oauthId()).get();
                });

        return saved.id();
    }
}
