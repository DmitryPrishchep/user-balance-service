package by.staryhroft.userbalance.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest {
    private String email;       // может быть пустым, если аутентификация по phone
    private String phone;       // может быть пустым, если по email
    @NotBlank
    private String password;
}