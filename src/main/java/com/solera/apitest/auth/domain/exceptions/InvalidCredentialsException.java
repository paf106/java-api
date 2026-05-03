package com.solera.apitest.auth.domain.exceptions;

import com.solera.apitest.shared.presentation.errors.UnauthorizedException;

public class InvalidCredentialsException extends UnauthorizedException {
    public InvalidCredentialsException() {
        super("Credenciales inválidas");
    }
}
