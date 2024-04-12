package com.example.core.oauth.service;

import com.example.api.member.MemberDTO;
import com.example.api.member.MemberExceptionType;
import com.example.api.member.MemberMapper;
import com.example.api.owner.OwnerMapper;
import com.example.core.exception.SystemException;
import com.example.core.oauth.domain.OauthServerType;
import com.example.core.oauth.domain.authcode.AuthCodeRequestUrlProviderComposite;
import com.example.core.oauth.domain.client.OauthMemberClientComposite;
import com.example.core.oauth.dto.LoginResponse;
import com.example.core.web.security.dto.UsersDTO;
import com.example.core.web.security.jwt.JWTProvider;
import com.example.core.web.security.jwt.dto.AccessToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthMemberClientComposite oauthMemberClientComposite;
    private final MemberMapper memberMapper;
    private final OwnerMapper ownerMapper;
    private final JWTProvider jwtProvider;

    public String getAuthCodeRequestUrl(OauthServerType oauthServerType) {
        return authCodeRequestUrlProviderComposite.provide(oauthServerType);
    }

    public LoginResponse login(OauthServerType oauthServerType, String authCode) {
        MemberDTO member = oauthMemberClientComposite.fetch(oauthServerType, authCode);
        MemberDTO saved = memberMapper.findByOauthId(member.oauthId())
                .orElseGet(() -> {
                    log.info("새로운 member = {}", member);
                    memberMapper.save(member);
                    return memberMapper.findByOauthId(member.oauthId())
                            .orElseThrow(() -> new SystemException(MemberExceptionType.NOT_FOUND.getMessage()));
                });

        UsersDTO usersDTO = getUsersDTO(saved);

        AccessToken accessToken = jwtProvider.createToken(usersDTO);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .usersDTO(usersDTO)
                .build();
    }

    private UsersDTO getUsersDTO(MemberDTO saved) {
        UsersDTO usersDTO = UsersDTO.createNotOwner(saved);

        if (ownerMapper.isExistByEmail(saved.getEmail())) {
            usersDTO.setMemberToOwner();
        }
        return usersDTO;
    }
}
