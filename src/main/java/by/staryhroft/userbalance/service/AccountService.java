package by.staryhroft.userbalance.service;

import by.staryhroft.userbalance.entity.Account;

public interface AccountService {
    Account getAccountByUserId(Long userId);
}