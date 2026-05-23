package by.staryhroft.userbalance.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class UpdateContactRequest {
    @NotBlank
    private String value; // email или телефон
}
