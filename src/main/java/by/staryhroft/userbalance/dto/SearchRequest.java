package by.staryhroft.userbalance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class SearchRequest {

    @Schema(description = "Фильтр: дата рождения больше указанной (формат dd.MM.yyyy)",
            example = "01.01.1990")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate dateOfBirth;

    @Schema(description = "Фильтр: точное совпадение телефона",
            example = "375291234567")
    private String phone;

    @Schema(description = "Фильтр: имя начинается с указанной строки",
            example = "Иван")
    private String name;

    @Schema(description = "Фильтр: точное совпадение email",
            example = "ivan@example.com")
    private String email;

    public SearchRequest() {}

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}