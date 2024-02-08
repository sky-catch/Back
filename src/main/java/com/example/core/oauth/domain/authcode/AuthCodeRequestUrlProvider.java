package com.example.core.oauth.domain.authcode;

import com.example.core.oauth.domain.OauthServerType;

public interface AuthCodeRequestUrlProvider {

    OauthServerType supportServer();

    String provide();
}
