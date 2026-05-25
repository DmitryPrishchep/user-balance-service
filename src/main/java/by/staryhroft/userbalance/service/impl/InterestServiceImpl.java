package by.staryhroft.userbalance.service.impl;

import by.staryhroft.userbalance.entity.Account;
import by.staryhroft.userbalance.repository.AccountRepository;
import by.staryhroft.userbalance.service.InterestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class InterestServiceImpl implements InterestService {

    private static final Logger log = LoggerFactory.getLogger(InterestServiceImpl.class);

    private final AccountRepository accountRepository;

    public InterestServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public void applyInterest() {
        List<Account> accounts = accountRepository.findAll();
        log.info("Начисление процентов для {} счетов", accounts.size());
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
        log.info("Начисление процентов завершено");
    }
}