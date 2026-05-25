package by.staryhroft.userbalance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

public class UpdateContactRequest {

    @Schema(description = "Новое значение (email или телефон)",
            example = "newemail@example.com")
    @NotBlank
    private String value;

    public UpdateContactRequest() {}

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}