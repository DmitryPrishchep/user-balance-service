package by.staryhroft.userbalance.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class TransferRequest {

    @Schema(description = "ID получателя", example = "2")
    @NotNull
    private Long toUserId;

    @Schema(description = "Сумма перевода", example = "100.00")
    @NotNull
    @Positive
    private BigDecimal value;

    public TransferRequest() {}

    public Long getToUserId() { return toUserId; }
    public void setToUserId(Long toUserId) { this.toUserId = toUserId; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
}