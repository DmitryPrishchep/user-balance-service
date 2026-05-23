package by.staryhroft.userbalance.controller;

import by.staryhroft.userbalance.dto.LoginRequest;
import by.staryhroft.userbalance.dto.LoginResponse;
import by.staryhroft.userbalance.entity.User;
import by.staryhroft.userbalance.repository.UserRepository;
import by.staryhroft.userbalance.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
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
        return new LoginResponse(token);
    }
}
