package by.staryhroft.userbalance.service.impl;

import by.staryhroft.userbalance.entity.Account;
import by.staryhroft.userbalance.exception.InvalidTransferException;
import by.staryhroft.userbalance.exception.UserNotFoundException;
import by.staryhroft.userbalance.repository.AccountRepository;
import by.staryhroft.userbalance.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferServiceImpl implements TransferService {

    private static final Logger log = LoggerFactory.getLogger(TransferServiceImpl.class);

    private final AccountRepository accountRepository;

    public TransferServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public void transferMoney(Long fromUserId, Long toUserId, BigDecimal amount) {
        log.info("Перевод средств: от пользователя {} к пользователю {}, сумма {}", fromUserId, toUserId, amount);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferException("Сумма перевода должна быть положительной");
        }
        if (fromUserId.equals(toUserId)) {
            throw new InvalidTransferException("Нельзя перевести деньги самому себе");
        }

        Account fromAccount = accountRepository.findByUserIdWithLock(fromUserId)
                .orElseThrow(() -> new UserNotFoundException("Счёт отправителя не найден"));
        Account toAccount = accountRepository.findByUserIdWithLock(toUserId)
                .orElseThrow(() -> new UserNotFoundException("Счёт получателя не найден"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InvalidTransferException("Недостаточно средств");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        log.info("Перевод успешно выполнен: пользователь {} -> {}, сумма {}", fromUserId, toUserId, amount);
    }
}