package com.solera.apitest.auth.application.usecases;

import com.solera.apitest.auth.application.models.AuthResult;
import com.solera.apitest.auth.domain.exceptions.InvalidCredentialsException;
import com.solera.apitest.auth.domain.repositories.TokenRepository;
import com.solera.apitest.users.domain.exceptions.UserCannotLoginException;
import com.solera.apitest.users.domain.exceptions.UserTemporarilyLockedException;
import com.solera.apitest.users.domain.models.User;
import com.solera.apitest.users.domain.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;

    public AuthResult execute(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            user.recordFailedLogin();
            userRepository.save(user);
            throw new InvalidCredentialsException();
        }

        try {
            user.validateCanLogin();
        } catch (UserCannotLoginException | UserTemporarilyLockedException ex) {
            throw new InvalidCredentialsException();
        }

        user.recordSuccessfulLogin();
        User savedUser = userRepository.save(user);
        String token = tokenRepository.generateToken(savedUser.getEmail());

        return new AuthResult(token, savedUser.getId(), savedUser.getEmail());
    }
}
