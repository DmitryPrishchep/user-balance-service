package by.staryhroft.userbalance.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
public class TransferRequest {
    @NotNull
    private Long toUserId;

    @NotNull
    @Positive
    private BigDecimal value;
}