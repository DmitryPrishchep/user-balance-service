package by.staryhroft.userbalance.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SearchRequest {
    private LocalDate dateOfBirth;
    private String phone;
    private String name;
    private String email;
}