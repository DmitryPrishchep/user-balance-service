package by.staryhroft.userbalance.service.impl;

import by.staryhroft.userbalance.entity.Account;
import by.staryhroft.userbalance.repository.AccountRepository;
import by.staryhroft.userbalance.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void transferMoney(Long fromUserId, Long toUserId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма перевода должна быть положительной");
        }
        if (fromUserId.equals(toUserId)) {
            throw new IllegalArgumentException("Перевод самому себе запрещён");
        }

        Account fromAccount = accountRepository.findByUserIdWithLock(fromUserId)
                .orElseThrow(() -> new RuntimeException("Учетная запись отправителя не найдена"));
        Account toAccount = accountRepository.findByUserIdWithLock(toUserId)
                .orElseThrow(() -> new RuntimeException("Учетная запись получателя не найдена"));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Недостаточно средств");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}
