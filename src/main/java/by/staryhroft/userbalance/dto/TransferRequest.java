package by.staryhroft.userbalance.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class TransferRequest {
    @NotNull
    private Long toUserId;

    @NotNull
    @Positive
    private BigDecimal value;

    public TransferRequest() {}

    public Long getToUserId() { return toUserId; }
    public void setToUserId(Long toUserId) { this.toUserId = toUserId; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
}