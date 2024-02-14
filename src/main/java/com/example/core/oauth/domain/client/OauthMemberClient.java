package com.example.core.oauth.domain.client;

import com.example.api.member.MemberDTO;
import com.example.core.oauth.domain.OauthServerType;

public interface OauthMemberClient {

    OauthServerType supportServer();

    MemberDTO fetch(String code);
}
