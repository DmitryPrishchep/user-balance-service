package by.staryhroft.userbalance.controller;

import by.staryhroft.userbalance.dto.LoginRequest;
import by.staryhroft.userbalance.dto.LoginResponse;
import by.staryhroft.userbalance.entity.User;
import by.staryhroft.userbalance.repository.UserRepository;
import by.staryhroft.userbalance.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Аутентификация", description = "Вход в систему и получение JWT-токена")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Operation(summary = "Войти в систему", description = "Аутентификация по email+пароль или phone+пароль")
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        log.info("Попытка входа: email={}, phone={}", request.getEmail(), request.getPhone());
        User user;
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Неверный email или пароль"));
        } else if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            user = userRepository.findByPhone(request.getPhone())
                    .orElseThrow(() -> new RuntimeException("Неверный телефон или пароль"));
        } else {
            throw new IllegalArgumentException("Email или телефон обязательны");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }
        String token = tokenProvider.generateToken(user.getId());
        log.info("Успешный вход для пользователя с id {}", user.getId());
        return new LoginResponse(token);
    }
}