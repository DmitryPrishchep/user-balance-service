package by.staryhroft.userbalance.dto;

import javax.validation.constraints.NotBlank;

public class UpdateContactRequest {
    @NotBlank
    private String value;

    public UpdateContactRequest() {}

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}