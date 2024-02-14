package com.example.core.oauth.domain;

import static java.util.Locale.ENGLISH;

public enum MemberStatus {
    ACTIVE,
    DORMANCY,
    EXTINCT,
    ;

    public static MemberStatus fromName(String status) {
        return MemberStatus.valueOf(status.toUpperCase(ENGLISH));
    }
}
