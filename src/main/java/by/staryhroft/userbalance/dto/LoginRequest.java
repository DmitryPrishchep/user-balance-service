package by.staryhroft.userbalance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

public class LoginRequest {

    @Schema(description = "Email пользователя", example = "ivan@example.com")
    private String email;       // может быть пустым, если аутентификация по phone

    @Schema(description = "Телефон пользователя", example = "375291234567")
    private String phone;       // может быть пустым, если по email

    @NotBlank
    @Schema(description = "Пароль", example = "password")
    private String password;

    public LoginRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}