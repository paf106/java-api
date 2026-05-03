package com.solera.apitest.orders.domain.exceptions;

import com.solera.apitest.shared.presentation.errors.BadRequestException;

public class InvalidOrderException extends BadRequestException {
    public InvalidOrderException(String message) {
        super(message);
    }
}
