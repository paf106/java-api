package com.solera.apitest.auth.presentation;


import com.solera.apitest.auth.application.models.AuthResult;
import com.solera.apitest.auth.application.usecases.LoginUserUseCase;
import com.solera.apitest.auth.application.usecases.RegisterUserUseCase;
import com.solera.apitest.auth.presentation.dtos.AuthResponseDto;
import com.solera.apitest.auth.presentation.dtos.LoginRequestDto;
import com.solera.apitest.auth.presentation.dtos.RegisterRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    @PostMapping("/register")
    ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        AuthResult authResult = registerUserUseCase.execute(request.email(), request.password());
        return ResponseEntity.ok(toDto(authResult));
    }

    @PostMapping("/login")
    ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        AuthResult authResult = loginUserUseCase.execute(request.email(), request.password());
        return ResponseEntity.ok(toDto(authResult));
    }

    private AuthResponseDto toDto(AuthResult authResult) {
        return new AuthResponseDto(
                "Bearer",
                authResult.accessToken(),
                authResult.userId(),
                authResult.email()
        );
    }
}
