package by.staryhroft.userbalance.service;

import java.math.BigDecimal;

public interface TransferService {
    void transferMoney(Long fromUserId, Long toUserId, BigDecimal amount);
}
