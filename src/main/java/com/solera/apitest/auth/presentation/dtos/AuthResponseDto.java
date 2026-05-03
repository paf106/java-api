package com.solera.apitest.auth.presentation.dtos;

public record AuthResponseDto(
        String tokenType,
        String accessToken,
        Long userId,
        String email
) {
}
