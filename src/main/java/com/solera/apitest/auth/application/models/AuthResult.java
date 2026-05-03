package com.solera.apitest.auth.application.models;

public record AuthResult(
        String accessToken,
        Long userId,
        String email
) {
}
