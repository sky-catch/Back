package com.example.core.oauth.domain.client;

import com.example.core.oauth.domain.OauthMember;
import com.example.core.oauth.domain.OauthServerType;

public interface OauthMemberClient {

    OauthServerType supportServer();

    OauthMember fetch(String code);
}
