package by.staryhroft.userbalance.service.impl;

import by.staryhroft.userbalance.entity.Account;
import by.staryhroft.userbalance.repository.AccountRepository;
import by.staryhroft.userbalance.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account getAccountByUserId(Long userId) {
        return accountRepository.findByUserIdWithLock(userId)
                .orElseThrow(() -> new RuntimeException("Учетная запись не найдена"));
    }
}
