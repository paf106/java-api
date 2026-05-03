package com.solera.apitest.auth.application.usecases;

import com.solera.apitest.auth.application.models.AuthResult;
import com.solera.apitest.auth.domain.repositories.TokenRepository;
import com.solera.apitest.shared.presentation.errors.BadRequestException;
import com.solera.apitest.users.domain.models.Role;
import com.solera.apitest.users.domain.models.Status;
import com.solera.apitest.users.domain.models.User;
import com.solera.apitest.users.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    public AuthResult execute(String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Ya existe un usuario con ese email");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.CUSTOMER);

        User savedUser = userRepository.save(user);
        String token = tokenRepository.generateToken(savedUser.getEmail());

        return new AuthResult(token, savedUser.getId(), savedUser.getEmail());
    }
}
