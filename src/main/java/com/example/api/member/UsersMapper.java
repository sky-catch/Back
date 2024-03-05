package com.example.api.member;

import java.util.Optional;

public interface UsersMapper<T> {

    Optional<T> findByEmail(String email);
}