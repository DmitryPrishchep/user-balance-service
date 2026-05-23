package by.staryhroft.userbalance.service.impl;

import by.staryhroft.userbalance.entity.Account;
import by.staryhroft.userbalance.repository.AccountRepository;
import by.staryhroft.userbalance.service.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void applyInterest() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            BigDecimal maxBalance = account.getInitialBalance()
                    .multiply(BigDecimal.valueOf(2.07));
            BigDecimal newBalance = account.getBalance()
                    .multiply(BigDecimal.valueOf(1.10));
            if (newBalance.compareTo(maxBalance) > 0) {
                newBalance = maxBalance;
            }
            account.setBalance(newBalance.setScale(2, RoundingMode.HALF_UP));
            accountRepository.save(account);
        }
    }
}
